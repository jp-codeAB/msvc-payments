package com.springcloud.msvc_payments.domain.port.out;

import com.springcloud.msvc_payments.domain.model.OrderDetails;
import java.util.Optional;

public interface IOrderServicePort {
    Optional<OrderDetails> getOrderDetails(Long orderId);
    void updateOrderStatus(Long orderId, String status);
    boolean isOrderOwner(Long orderId, Long customerId);
}