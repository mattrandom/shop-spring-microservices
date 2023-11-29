package com.github.mattrandom.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mattrandom.productservice.dto.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Testcontainers
class ProductControllerIT {

    // @Container annotation is used to define a container that should be managed by the JUnit test framework.
    // In this case, it's a MongoDB container created with MongoDBContainer("mongo:7.0.4").
    // This means it's a MongoDB container based on the image version 7.0.4
    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.4");

    // @DynamicPropertySource is used to dynamically set properties during the application context initialization.
    // In the method setProperties, it's setting the property 'spring.data.mongodb.uri' to the replica set URL of the MongoDB container.
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    // To be able to make requests from integration test to our ProductController we use MockMvc
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateProduct() throws Exception {
        //given
        ProductRequest productRequest = ProductRequest.builder()
                .name("iPhone 15")
                .description("New iPhone 15!")
                .price(BigDecimal.valueOf(5000))
                .build();

        //when
        ResultActions response = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated());
    }
}