package com.example.RailingShop;

import com.example.RailingShop.Controllers.ProductController;
import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void testGetProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100));

        when(productService.getProduct(anyLong())).thenReturn(Optional.of(product));

        mockMvc.perform(get("/shop/product/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void testGetSortedProducts() throws Exception {
        Product product1 = new Product();
        product1.setName("Product A");
        product1.setPrice(BigDecimal.valueOf(50));

        Product product2 = new Product();
        product2.setName("Product B");
        product2.setPrice(BigDecimal.valueOf(100));

        when(productService.getSortedProducts("name", "asc")).thenReturn(List.of(product1, product2));

        mockMvc.perform(get("/shop/products?sortBy=name&sortDirection=asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product A"))
                .andExpect(jsonPath("$[1].name").value("Product B"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/shop/product/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    // Additional tests for other endpoints can be added here
}
