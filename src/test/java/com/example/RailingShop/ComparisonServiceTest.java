package com.example.RailingShop.Services;

import com.example.RailingShop.Entities.Comparison;
import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Repositories.ComparisonRepository;
import com.example.RailingShop.Repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ComparisonServiceTest {

    @Mock
    private ComparisonRepository comparisonRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ComparisonService comparisonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetComparisonsByUserId() {
        // Mock data
        Long userId = 1L;
        List<Comparison> expectedComparisons = new ArrayList<>();
        Comparison comparison1 = new Comparison();
        comparison1.setId(1L);
        comparison1.setUser(new User(userId, "John", "Doe", "john@example.com", "johndoe", "1234567890", "New York", "12345", "123 Street"));
        expectedComparisons.add(comparison1);
        when(comparisonRepository.findByUserId(userId)).thenReturn(expectedComparisons);

        // Call service method
        List<Comparison> actualComparisons = comparisonService.getComparisonsByUserId(userId);

        // Assertions
        assertNotNull(actualComparisons);
        assertEquals(1, actualComparisons.size());
        assertEquals(expectedComparisons.get(0), actualComparisons.get(0));
    }

    @Test
    void testAddProductToComparison() {
        // Mock data
        Long userId = 1L;
        Long productId = 1L;
        Comparison comparison = new Comparison();
        comparison.setId(1L);
        comparison.setUser(new User(userId, "John", "Doe", "john@example.com", "johndoe", "1234567890", "New York", "12345", "123 Street"));
        when(comparisonRepository.findByUserId(userId)).thenReturn(new ArrayList<>());
        when(comparisonRepository.save(any())).thenReturn(comparison);
        Product product = new Product();
        product.setId(productId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Call service method
        Comparison updatedComparison = comparisonService.addProductToComparison(userId, productId);

        // Assertions
        assertNotNull(updatedComparison);
        assertTrue(updatedComparison.getProducts().contains(product));
    }

    @Test
    void testRemoveProductFromComparison() {
        // Mock data
        Long userId = 1L;
        Long productId = 1L;
        Comparison comparison = new Comparison();
        comparison.setId(1L);
        comparison.setUser(new User(userId, "John", "Doe", "john@example.com", "johndoe", "1234567890", "New York", "12345", "123 Street"));
        Product product = new Product();
        product.setId(productId);
        comparison.getProducts().add(product);
        when(comparisonRepository.findByUserId(userId)).thenReturn(List.of(comparison));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Call service method
        comparisonService.removeProductFromComparison(userId, productId);

        // Assertions
        assertFalse(comparison.getProducts().contains(product));
        verify(comparisonRepository, times(1)).save(comparison);
    }

    @Test
    void testGetProductsInComparison() {
        // Mock data
        Long userId = 1L;
        Comparison comparison = new Comparison();
        comparison.setId(1L);
        comparison.setUser(new User(userId, "John", "Doe", "john@example.com", "johndoe", "1234567890", "New York", "12345", "123 Street"));
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);
        comparison.getProducts().add(product1);
        comparison.getProducts().add(product2);
        when(comparisonRepository.findByUserId(userId)).thenReturn(List.of(comparison));

        // Call service method
        List<Product> productsInComparison = comparisonService.getProductsInComparison(userId);

        // Assertions
        assertNotNull(productsInComparison);
        assertEquals(2, productsInComparison.size());
        assertTrue(productsInComparison.contains(product1));
        assertTrue(productsInComparison.contains(product2));
    }

    @Test
    void testGetLowestPrice() {
        // Mock data
        Long userId = 1L;
        Comparison comparison = new Comparison();
        comparison.setId(1L);
        comparison.setUser(new User(userId, "John", "Doe", "john@example.com", "johndoe", "1234567890", "New York", "12345", "123 Street"));
        Product product1 = new Product();
        product1.setId(1L);
        product1.setPrice(BigDecimal.valueOf(100));
        Product product2 = new Product();
        product2.setId(2L);
        product2.setPrice(BigDecimal.valueOf(50));
        comparison.getProducts().add(product1);
        comparison.getProducts().add(product2);
        when(comparisonRepository.findByUserId(userId)).thenReturn(List.of(comparison));

        // Call service method
        BigDecimal lowestPrice = comparisonService.getLowestPrice(userId);

        // Assertions
        assertNotNull(lowestPrice);
        assertEquals(BigDecimal.valueOf(50), lowestPrice);
    }
}
