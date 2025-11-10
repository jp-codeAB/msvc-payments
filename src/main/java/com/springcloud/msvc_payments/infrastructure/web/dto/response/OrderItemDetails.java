package com.springcloud.msvc_payments.infrastructure.web.dto.response;

import lombok.Data;

@Data
public class OrderItemDetails {
    private Long productId;
    private Integer quantity;
}
