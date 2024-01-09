package com.github.mattrandom.productservice.service;

import com.github.mattrandom.productservice.dto.ProductRequest;
import com.github.mattrandom.productservice.dto.ProductResponse;
import com.github.mattrandom.productservice.model.Product;
import com.github.mattrandom.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product [{}] has been saved.", product.getId());
    }

    public List<ProductResponse> getProducts() {
        List<Product> productEntities = productRepository.findAll();

        return productEntities.stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
