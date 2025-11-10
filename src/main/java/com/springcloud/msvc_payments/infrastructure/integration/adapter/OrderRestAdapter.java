package com.springcloud.msvc_payments.infrastructure.integration.adapter;

import com.springcloud.msvc_payments.domain.model.OrderDetails;
import com.springcloud.msvc_payments.domain.model.OrderItem;
import com.springcloud.msvc_payments.domain.port.out.IOrderServicePort;
import com.springcloud.msvc_payments.infrastructure.integration.client.OrderFeignClient;
import com.springcloud.msvc_payments.infrastructure.web.dto.response.OrderDetailsResponse;
import com.springcloud.msvc_payments.infrastructure.web.dto.response.StatusUpdateNotification;
import com.springcloud.msvc_payments.shared.exception.BusinessException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderRestAdapter implements IOrderServicePort {
    private final OrderFeignClient orderClient;

    @Override
    public Optional<OrderDetails> getOrderDetails(Long orderId) {

        try {
            OrderDetailsResponse response = orderClient.getOrderById(orderId);

            List<OrderItem> domainItems = response.getItems().stream()
                    .map(item -> new OrderItem(item.getProductId(), item.getQuantity()))
                    .collect(Collectors.toList());

            return Optional.of(new OrderDetails(
                    response.getId(),
                    response.getCustomerId(),
                    response.getTotalAmount(),
                    domainItems
            ));
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        } catch (FeignException e) {
            throw new BusinessException("Error fetching order details: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateOrderStatus(Long orderId, String status) {
        try {
            orderClient.updateOrderStatus(orderId, new StatusUpdateNotification(status));
        } catch (FeignException e) {
            throw new BusinessException("Failed to update order status in msvc-orders: " + e.contentUTF8(), e);
        }
    }

    @Override
    public boolean isOrderOwner(Long orderId, Long customerId) {
        try {
            return orderClient.isOrderOwner(orderId, customerId);
        } catch (FeignException.NotFound e) {
            return false;
        } catch (FeignException e) {
            throw new BusinessException("Error verifying order ownership in msvc-orders: " + e.contentUTF8(), e);
        }
    }
}