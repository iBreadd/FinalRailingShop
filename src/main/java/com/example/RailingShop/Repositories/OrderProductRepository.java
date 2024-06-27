package com.example.RailingShop.Repositories;

import com.example.RailingShop.Entities.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct,Long> {
    void deleteByProductId(Long productId);
}
