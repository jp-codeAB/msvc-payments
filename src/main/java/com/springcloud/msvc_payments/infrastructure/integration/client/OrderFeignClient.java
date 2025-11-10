package com.springcloud.msvc_payments.infrastructure.integration.client;


import com.springcloud.msvc_payments.infrastructure.integration.config.FeignOkHttpConfig;
import com.springcloud.msvc_payments.infrastructure.web.dto.response.OrderDetailsResponse;
import com.springcloud.msvc_payments.infrastructure.web.dto.response.StatusUpdateNotification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "msvc-orders", configuration = FeignOkHttpConfig.class)
public interface OrderFeignClient {

    @GetMapping("/orders/{id}")
    OrderDetailsResponse getOrderById(@PathVariable("id") Long id);

    @PatchMapping("/orders/{id}/status")
    void updateOrderStatus(@PathVariable("id") Long id, @RequestBody StatusUpdateNotification request);

    @GetMapping("/orders/{orderId}/owner/{customerId}")
    boolean isOrderOwner(@PathVariable("orderId") Long orderId, @PathVariable("customerId") Long customerId);
}