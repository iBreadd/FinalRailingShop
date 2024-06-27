package com.example.RailingShop.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.RailingShop.Entities.DeletedProduct;
import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Entities.Review;
import com.example.RailingShop.Enums.ProductColor;
import com.example.RailingShop.Enums.ProductMaterial;
import com.example.RailingShop.Exceptions.ResourceNotFoundException;
import com.example.RailingShop.Repositories.DeletedProductRepository;
import com.example.RailingShop.Repositories.OrderProductRepository;
import com.example.RailingShop.Repositories.ProductRepository;
import com.example.RailingShop.Repositories.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DeletedProductRepository deletedProductRepository;

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private ReviewRepository reviewRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100));
    }

    @Test
    void saveProduct_withImage() throws IOException {
        MultipartFile imageFile = mock(MultipartFile.class);
        when(imageFile.isEmpty()).thenReturn(false);
        when(cloudinary.uploader().upload(any(byte[].class), anyMap()))
                .thenReturn(Map.of("url", "http://test-url.com"));

        Product savedProduct = new Product();
        savedProduct.setImageUrl("http://test-url.com");
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.saveProduct(product, imageFile);

        assertEquals("http://test-url.com", result.getImageUrl());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void saveProduct_withoutImage() throws IOException {
        MultipartFile imageFile = mock(MultipartFile.class);
        when(imageFile.isEmpty()).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.saveProduct(product, imageFile);

        assertNull(result.getImageUrl());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getProduct() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProduct(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getName());
    }

    @Test
    void deleteProduct() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        DeletedProduct deletedProduct = new DeletedProduct(product);
        when(deletedProductRepository.save(any(DeletedProduct.class))).thenReturn(deletedProduct);

        productService.deleteProduct(1L);

        verify(orderProductRepository, times(1)).deleteByProductId(1L);
        verify(deletedProductRepository, times(1)).save(any(DeletedProduct.class));
        verify(productRepository, times(1)).delete(any(Product.class));
    }

    @Test
    void getProductById() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertEquals("Test Product", result.getName());
    }

    @Test
    void searchProducts() {
        List<Product> products = List.of(product);
        when(productRepository.searchProducts(any(), any(), any(), any(), any())).thenReturn(products);

        List<Product> result = productService.searchProducts(null, "Test", null, null, null);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
    }

    // Additional tests for other methods can be added here
}
