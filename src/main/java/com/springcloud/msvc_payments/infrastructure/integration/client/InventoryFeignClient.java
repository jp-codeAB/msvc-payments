package com.springcloud.msvc_payments.infrastructure.integration.client;

import com.springcloud.msvc_payments.infrastructure.integration.config.FeignOkHttpConfig;
import com.springcloud.msvc_payments.domain.model.StockItemRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name = "msvc-inventory", configuration = FeignOkHttpConfig.class)
public interface InventoryFeignClient {

    @PostMapping("/products/convert-to-sale")
    void convertReservationToSale(@RequestBody List<StockItemRequest> request);
}