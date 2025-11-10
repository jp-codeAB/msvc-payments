package com.springcloud.msvc_payments.domain.port.out;

import com.springcloud.msvc_payments.domain.model.Payment;

public interface IPaymentRepositoryPort {
    Payment save(Payment payment);
}