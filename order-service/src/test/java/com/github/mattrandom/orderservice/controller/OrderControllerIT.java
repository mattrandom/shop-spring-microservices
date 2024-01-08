package com.github.mattrandom.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mattrandom.orderservice.dto.OrderLineItemsDto;
import com.github.mattrandom.orderservice.dto.OrderRequest;
import com.github.mattrandom.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Testcontainers
public class OrderControllerIT {

    @Container
    private static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.2.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
    }

    @Test
    void shouldCreateOrder() throws Exception {
        //given
        OrderRequest orderRequest = prepareOrderRequest("C1", 100.20, 2);

        //when
        ResultActions response = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("Order placed successfully"));

        assertEquals(1, orderRepository.findAll().size());
    }

    private OrderRequest prepareOrderRequest(String skuCode, double price, int quantity) {
        return OrderRequest.builder()
                .orderLineItemsDtoList(
                        List.of(OrderLineItemsDto.builder()
                                .skuCode(skuCode)
                                .price(BigDecimal.valueOf(price))
                                .quantity(quantity)
                                .build()
                        ))
                .build();
    }
}
