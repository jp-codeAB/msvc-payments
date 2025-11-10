package com.springcloud.msvc_payments.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItem {
    private Long productId;
    private Integer quantity;
}
