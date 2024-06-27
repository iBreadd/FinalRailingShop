package com.example.RailingShop.Repositories;

import com.example.RailingShop.Entities.DeletedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedProductRepository extends JpaRepository<DeletedProduct,Long> {
}
