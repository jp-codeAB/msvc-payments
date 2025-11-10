package com.springcloud.msvc_payments.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockItemRequest {
    private Long productId;
    private Integer quantity;
}
