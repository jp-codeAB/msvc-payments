package com.springcloud.msvc_payments.domain.port.out;

import com.springcloud.msvc_payments.domain.model.OrderItem;
import java.util.List;

public interface InventoryServicePort {
    void convertReservationToSale(List<OrderItem> items);
}
