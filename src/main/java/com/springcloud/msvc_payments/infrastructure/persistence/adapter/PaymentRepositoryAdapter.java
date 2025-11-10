package com.springcloud.msvc_payments.infrastructure.persistence.adapter;

import com.springcloud.msvc_payments.domain.model.Payment;
import com.springcloud.msvc_payments.domain.port.out.IPaymentRepositoryPort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Repository // ¡Añade esta anotación!
public class PaymentRepositoryAdapter implements IPaymentRepositoryPort {

    @Override
    public Payment save(Payment payment) {
        // Lógica simulada de guardado de pagos
        if (payment.getId() == null || payment.getId() == 0) {
            payment.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        }
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDateTime.now());
        }
        return payment;
    }


}