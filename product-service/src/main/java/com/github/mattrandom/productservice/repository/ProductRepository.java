package com.github.mattrandom.productservice.repository;

import com.github.mattrandom.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
