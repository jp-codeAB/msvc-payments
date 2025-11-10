package com.springcloud.msvc_payments.domain.port.out;

import com.springcloud.msvc_payments.domain.model.PaymentStatus;

public interface INotificationServicePort {
    void sendPaymentStatusUpdate(Long customerId, Long orderId, PaymentStatus status, String customerEmail);
}
