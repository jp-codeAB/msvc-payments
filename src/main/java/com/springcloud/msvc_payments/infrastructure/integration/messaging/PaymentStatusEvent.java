package com.springcloud.msvc_payments.infrastructure.integration.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusEvent {
    private String eventId;
    private Long orderId;
    private Long customerId;
    private String customerEmail;
    private String paymentStatus;
    private LocalDateTime timestamp;
}
