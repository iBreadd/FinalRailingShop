package com.example.RailingShop.Services;

import com.example.RailingShop.Entities.Comparison;
import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Repositories.ComparisonRepository;
import com.example.RailingShop.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ComparisonService {
    @Autowired
    private ComparisonRepository comparisonRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<Comparison> getComparisonsByUserId(Long userId) {
        return comparisonRepository.findByUserId(userId);
    }

    public Comparison addProductToComparison(Long userId, Long productId) {
        List<Comparison> comparisons = comparisonRepository.findByUserId(userId);
        Comparison comparison;
        if (comparisons.isEmpty()) {
            comparison = new Comparison();
            User user = new User();
            user.setId(userId);
            comparison.setUser(user);
        } else {
            comparison = comparisons.get(0);
        }

        Product product = new Product();
        product.setId(productId);
        comparison.getProducts().add(product);

        return comparisonRepository.save(comparison);
    }

    public void removeProductFromComparison(Long userId, Long productId) {
        List<Comparison> comparisons = comparisonRepository.findByUserId(userId);
        if (!comparisons.isEmpty()) {
            Comparison comparison = comparisons.get(0);
            Product product = productRepository.findById(productId).orElseThrow();
            comparison.getProducts().remove(product);
            comparisonRepository.save(comparison);
        }
    }


    public List<Product> getProductsInComparison(Long userId) {
        List<Comparison> comparisons = comparisonRepository.findByUserId(userId);
        if (comparisons.isEmpty()) {
            throw new RuntimeException("No comparison found for user " + userId);
        }
        return comparisons.get(0).getProducts();
    }

    public BigDecimal getLowestPrice(Long userId) {
        List<Product> products = getProductsInComparison(userId);
        return products.stream()
                .map(Product::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.valueOf(Double.MAX_VALUE));
    }
}

