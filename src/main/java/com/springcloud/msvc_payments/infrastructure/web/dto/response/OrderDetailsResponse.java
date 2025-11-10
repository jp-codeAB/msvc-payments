package com.springcloud.msvc_payments.infrastructure.web.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDetailsResponse {
    private Long id;
    private Long customerId;
    private BigDecimal totalAmount;
    private List<OrderItemDetails> items;
}