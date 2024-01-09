package com.github.mattrandom.inventoryservice.service;

import com.github.mattrandom.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode) {
        boolean isAvailable = inventoryRepository.findBySkuCode(skuCode).isPresent();
        log.info("Verifying if product with sku-code ({}) is available: {}", skuCode, isAvailable);
        return isAvailable;
    }
}
