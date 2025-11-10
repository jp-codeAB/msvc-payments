package com.springcloud.msvc_payments.application.service;

import com.springcloud.msvc_payments.domain.port.out.INotificationServicePort;
import com.springcloud.msvc_payments.domain.model.OrderDetails;
import com.springcloud.msvc_payments.domain.model.Payment;
import com.springcloud.msvc_payments.domain.model.PaymentStatus;
import com.springcloud.msvc_payments.domain.model.OrderItem;
import com.springcloud.msvc_payments.domain.port.in.IProcessPaymentUseCase;
import com.springcloud.msvc_payments.domain.port.out.InventoryServicePort;
import com.springcloud.msvc_payments.domain.port.out.IOrderServicePort;
import com.springcloud.msvc_payments.domain.port.out.IPaymentRepositoryPort;
import com.springcloud.msvc_payments.shared.exception.BusinessException;
import com.springcloud.msvc_payments.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService implements IProcessPaymentUseCase {

    private final IPaymentRepositoryPort paymentRepositoryPort;
    private final IOrderServicePort orderServicePort;
    private final InventoryServicePort inventoryServicePort;
    private final INotificationServicePort notificationServicePort;

    @Override
    @Transactional
    public Payment processPayment(Long orderId, String paymentMethod, BigDecimal requestedAmount, String customerEmail) {

        OrderDetails order = orderServicePort.getOrderDetails(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + orderId));

        if (requestedAmount.compareTo(order.getTotalAmount()) != 0) {
            throw new BusinessException(
                    String.format("The requested payment amount (%.2f) does not match the order total (%.2f). Payment rejected.",
                            requestedAmount, order.getTotalAmount()),
                    HttpStatus.BAD_REQUEST
            );
        }
        Payment payment = Payment.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomerId())
                .amount(order.getTotalAmount())
                .status(PaymentStatus.PENDING)
                .transactionId(UUID.randomUUID().toString())
                .build();

        payment = paymentRepositoryPort.save(payment);
        notificationServicePort.sendPaymentStatusUpdate(order.getCustomerId(), orderId, PaymentStatus.PENDING, customerEmail);
        List<OrderItem> orderItemsToProcess = order.getItems();

        try {
            inventoryServicePort.convertReservationToSale(orderItemsToProcess);
        } catch (BusinessException e) {
            log.error("Distributed transaction failed (Convert Reservation to Sale) for Order {}. Reverting payment status and order status.", orderId, e);

            revertOrderStatus(orderId, "FAILED");

            payment.setStatus(PaymentStatus.FAILED);
            paymentRepositoryPort.save(payment);
            notificationServicePort.sendPaymentStatusUpdate(order.getCustomerId(), orderId, PaymentStatus.FAILED, customerEmail);
            throw e;
        }

        payment.setStatus(PaymentStatus.CONFIRMED);
        paymentRepositoryPort.save(payment);
        notificationServicePort.sendPaymentStatusUpdate(order.getCustomerId(), orderId, PaymentStatus.CONFIRMED, customerEmail);

        try {
            orderServicePort.updateOrderStatus(orderId, "CONFIRMED");
        } catch (BusinessException e) {
            log.error("Failed to update order status (CONFIRMED) for Order {}. Marking payment as REFUNDED.", orderId, e);
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepositoryPort.save(payment);
            notificationServicePort.sendPaymentStatusUpdate(order.getCustomerId(), orderId, PaymentStatus.REFUNDED, customerEmail);

            throw e;
        }
        return payment;
    }

    private void revertOrderStatus(Long orderId, String newStatus) {
        try {
            orderServicePort.updateOrderStatus(orderId, newStatus);
        } catch (Exception e) {
            log.error("CRITICAL: Failed to update order status to {} for Order {}. Requires manual review/re-process.", newStatus, orderId, e);
        }
    }
}