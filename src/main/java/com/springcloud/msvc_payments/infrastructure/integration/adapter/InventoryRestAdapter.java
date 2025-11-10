package com.springcloud.msvc_payments.infrastructure.integration.adapter;

import com.springcloud.msvc_payments.domain.model.OrderItem;
import com.springcloud.msvc_payments.domain.model.StockItemRequest;
import com.springcloud.msvc_payments.domain.port.out.InventoryServicePort;
import com.springcloud.msvc_payments.infrastructure.integration.client.InventoryFeignClient;
import com.springcloud.msvc_payments.shared.exception.BusinessException;
import feign.FeignException;
import feign.FeignException.NotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryRestAdapter implements InventoryServicePort {
    private final InventoryFeignClient inventoryClient;

    @Override
    public void convertReservationToSale(List<OrderItem> items) {

        List<StockItemRequest> stockItemsToConvert = items.stream()
                .map(item -> new StockItemRequest(item.getProductId(), item.getQuantity()))
                .collect(Collectors.toList());

        log.info("Attempting to convert stock reservation to sale for {} items.", stockItemsToConvert.size());

        try {
            inventoryClient.convertReservationToSale(stockItemsToConvert);
            log.info("Stock reservation converted to sale successfully.");
        } catch (NotFound e) {
            log.error("404 Error calling msvc-inventory. Check endpoint path: {}", e.getMessage());
            throw new BusinessException("Failed to convert reservation to sale in msvc-inventory. Check server name/path: " + e.contentUTF8(), e);
        } catch (FeignException e) {
            log.error("Feign error during stock conversion: {}", e.contentUTF8());
            throw new BusinessException("Failed to convert reservation to sale in msvc-inventory. Response: " + e.contentUTF8(), e);
        } catch (Exception e) {
            log.error("Unexpected error during stock conversion: {}", e.getMessage());
            throw new BusinessException("Unexpected error communicating with msvc-inventory: " + e.getMessage(), e);
        }
    }
}