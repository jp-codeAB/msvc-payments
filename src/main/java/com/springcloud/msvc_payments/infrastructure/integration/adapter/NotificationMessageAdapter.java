package com.springcloud.msvc_payments.infrastructure.integration.adapter;

import com.springcloud.msvc_payments.domain.model.PaymentStatus;
import com.springcloud.msvc_payments.domain.port.out.INotificationServicePort;
import com.springcloud.msvc_payments.infrastructure.integration.messaging.IPaymentEventMapper;
import com.springcloud.msvc_payments.infrastructure.integration.messaging.PaymentStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationMessageAdapter implements INotificationServicePort {

    private final StreamBridge streamBridge;
    private final IPaymentEventMapper eventMapper;
    private static final String BINDING_NAME = "paymentStatusUpdate-out-0";

    @Override
    public void sendPaymentStatusUpdate(Long customerId, Long orderId, PaymentStatus status, String customerEmail) {

        if (customerEmail == null || customerEmail.isBlank()) {
            log.error("CRITICAL: Email is null/blank for Order ID: {}. Cannot send ASYNC notification.", orderId);
            return;
        }

        PaymentStatusEvent event = eventMapper.toPaymentStatusEvent(
                orderId,
                customerId,
                status,
                customerEmail
        );

        streamBridge.send(BINDING_NAME, event);
        log.info("📢 ASYNC EVENT SENT: Payment status update for Order ID: {} to Binding: {}. Email: {}",
                orderId, BINDING_NAME, customerEmail);
    }
}