package com.github.mattrandom.inventoryservice.repository;

import com.github.mattrandom.inventoryservice.model.Inventory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // it disables in-memory DB support
public class InventoryRepositoryTests {

    @Container
    private static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.2.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
    }

    @Autowired
    private InventoryRepository inventoryRepository;

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        this.inventory = inventoryRepository.save(Inventory.builder()
                .quantity(1)
                .skuCode("TEST")
                .build());
    }

    @AfterEach
    void tearDown() {
        inventoryRepository.deleteAll();
    }

    @Test
    void shouldReturnExistingProduct() {
        //when
        Optional<Inventory> c1 = inventoryRepository.findBySkuCode("TEST");

        //then
        assertEquals(inventory, c1.get());
    }

    @Test
    void shouldThrowExceptionWhenProductNotExist() {
        //when
        Optional<Inventory> c1 = inventoryRepository.findBySkuCode("");

        //then
        assertThrows(NoSuchElementException.class, c1::get);
    }
}
