package com.example.RailingShop.Repositories;

import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Enums.ProductColor;
import com.example.RailingShop.Enums.ProductMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("SELECT p FROM Product p WHERE (:id IS NULL OR p.id = :id) " +
            "AND (:name IS NULL OR p.name LIKE %:name%) " +
            "AND (:quantity IS NULL OR p.quantity = :quantity) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Product> searchProducts(@Param("id") Long id,
                                 @Param("name") String name,
                                 @Param("quantity") Integer quantity,
                                 @Param("minPrice") BigDecimal minPrice,
                                 @Param("maxPrice") BigDecimal maxPrice);

    List<Product> findByDescriptionContainingIgnoreCase(String keyword);

    List<Product> findAllByOrderByPriceAsc();
    List<Product> findAllByOrderByPriceDesc();

    @Query("SELECT p FROM Product p ORDER BY SIZE(p.reviews) DESC")
    List<Product> findAllOrderByReviewsCount();

}
