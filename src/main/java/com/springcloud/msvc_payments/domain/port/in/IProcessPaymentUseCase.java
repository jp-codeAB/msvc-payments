package com.springcloud.msvc_payments.domain.port.in;

import com.springcloud.msvc_payments.domain.model.Payment;
import java.math.BigDecimal;

public interface IProcessPaymentUseCase {
    Payment processPayment(Long orderId, String paymentMethod, BigDecimal amount, String customerEmail);
}
