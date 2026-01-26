package com.sachet.order_service.repo;

import com.sachet.order_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long> {
}
