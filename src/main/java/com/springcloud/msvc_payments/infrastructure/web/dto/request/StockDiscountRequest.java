package com.springcloud.msvc_payments.infrastructure.web.dto.request;

import com.springcloud.msvc_payments.infrastructure.web.dto.response.OrderItemDetails;
import lombok.Data;
import java.util.List;

@Data
public class StockDiscountRequest {
    private List<OrderItemDetails> items;
}