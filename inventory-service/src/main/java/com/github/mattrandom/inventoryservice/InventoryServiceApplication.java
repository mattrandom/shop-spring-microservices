package com.github.mattrandom.inventoryservice;

import com.github.mattrandom.inventoryservice.model.Inventory;
import com.github.mattrandom.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner createData(InventoryRepository inventoryRepository) {
        return args -> inventoryRepository.save(
                Inventory.builder()
                        .quantity(1)
                        .skuCode("C1")
                        .build()
        );
    }
}
