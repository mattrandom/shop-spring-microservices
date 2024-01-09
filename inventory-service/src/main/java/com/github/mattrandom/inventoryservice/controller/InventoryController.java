package com.github.mattrandom.inventoryservice.controller;

import com.github.mattrandom.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{sku-code}")
    public ResponseEntity<Boolean> isInStock(@PathVariable("sku-code") String skuCode) {
        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.isInStock(skuCode));
    }
}
