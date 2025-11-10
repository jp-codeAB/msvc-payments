package com.springcloud.msvc_payments.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderDetails {
    private Long orderId;
    private Long customerId;
    private BigDecimal totalAmount;
    private List<OrderItem> items;
}