package com.springcloud.msvc_payments.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Setter
    private Long id;
    private Long orderId;
    private Long customerId;
    private BigDecimal amount;

    @Setter
    private PaymentStatus status;
    @Setter
    private String transactionId;

    @Setter
    private LocalDateTime paymentDate;
}