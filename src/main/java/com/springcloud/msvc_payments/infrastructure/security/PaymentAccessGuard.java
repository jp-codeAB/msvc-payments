package com.springcloud.msvc_payments.infrastructure.security;

import com.springcloud.msvc_payments.domain.port.out.IOrderServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j // <-- Añadir log
@Component("paymentAccessGuard")
@RequiredArgsConstructor
public class PaymentAccessGuard {

    private final IOrderServicePort orderServicePort;

    public boolean isOrderOwner(Long orderId, Long userId) {
        // Añadir log para ver los valores que se usan en la llamada externa
        log.info("SECURITY GUARD CHECK: Verifying if User ID {} owns Order ID {}.", userId, orderId);

        boolean isOwner = orderServicePort.isOrderOwner(orderId, userId);

        log.info("SECURITY GUARD RESULT: User ID {} owner check for Order ID {} returned: {}.", userId, orderId, isOwner);

        return isOwner;
    }
}
