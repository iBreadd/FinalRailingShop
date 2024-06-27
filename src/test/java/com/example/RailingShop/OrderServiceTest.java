package com.example.RailingShop;

import com.example.RailingShop.Entities.*;
import com.example.RailingShop.Enums.Cart;
import com.example.RailingShop.Enums.OrderStatus;
import com.example.RailingShop.Exceptions.OrderNotFoundException;
import com.example.RailingShop.Repositories.OrderProductRepository;
import com.example.RailingShop.Repositories.OrderRepository;
import com.example.RailingShop.Repositories.ProductRepository;
import com.example.RailingShop.Repositories.UserRepository;
import com.example.RailingShop.Services.OrderService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOrders() {
        // Mock data
        List<Order> mockOrders = new ArrayList<>();
        mockOrders.add(new Order(1L, new User(), new ArrayList<>(), OrderStatus.PENDING, BigDecimal.valueOf(100.0)));
        mockOrders.add(new Order(2L, new User(), new ArrayList<>(), OrderStatus.DELIVERED, BigDecimal.valueOf(150.0)));
        when(orderRepository.findAll()).thenReturn(mockOrders);

        // Call service method
        List<Order> orders = orderService.getAllOrders();

        // Assertions
        assertNotNull(orders);
        assertEquals(2, orders.size());
    }

    @Test
    void testGetOrderById() {
        // Mock data
        Long orderId = 1L;
        Order mockOrder = new Order(orderId, new User(), new ArrayList<>(), OrderStatus.PENDING, BigDecimal.valueOf(100.0));
        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(mockOrder));

        // Call service method
        Order foundOrder = orderService.getOrderById(orderId);

        // Assertions
        assertNotNull(foundOrder);
        assertEquals(orderId, foundOrder.getId());
    }


    @Test
    void testDeleteOrderById() {
        // Mock data
        Long orderId = 1L;
        doNothing().when(orderRepository).deleteById(orderId);

        // Call service method
        assertDoesNotThrow(() -> orderService.deleteOrderById(orderId));
    }

    @Test
    void testGetOrdersByStatus() {
        // Mock data
        OrderStatus status = OrderStatus.PENDING;
        List<Order> mockOrders = new ArrayList<>();
        mockOrders.add(new Order(1L, new User(), new ArrayList<>(), status, BigDecimal.valueOf(100.0)));
        when(orderRepository.getOrdersByStatus(status)).thenReturn(mockOrders);

        // Call service method
        List<Order> orders = orderService.getOrdersByStatus(status);

        // Assertions
        assertNotNull(orders);
        assertEquals(1, orders.size());
    }

    @Test
    void testUpdateOrderStatus() {
        // Mock data
        Long orderId = 1L;
        OrderStatus newStatus = OrderStatus.DELIVERED;
        User mockUser = new User(1L, "John", "Doe", "john.doe@example.com", "johndoe", "123456789", "City", "12345", "Address");
        Order mockOrder = new Order(orderId, mockUser, new ArrayList<>(), OrderStatus.PENDING, BigDecimal.valueOf(100.0));
        when(userRepository.findByUsername(anyString())).thenReturn(mockUser);
        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(mockOrder));
        when(orderRepository.save(any())).thenReturn(mockOrder);

        // Call service method
        assertDoesNotThrow(() -> orderService.updateOrderStatus(orderId, newStatus));
    }

    @Test
    void testGetOrdersByUser() {
        // Mock data
        Long userId = 1L;
        User mockUser = new User(userId, "John", "Doe", "john.doe@example.com", "johndoe", "123456789", "City", "12345", "Address");
        List<Order> mockOrders = new ArrayList<>();
        mockOrders.add(new Order(1L, mockUser, new ArrayList<>(), OrderStatus.PENDING, BigDecimal.valueOf(100.0)));
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(mockUser));
        when(orderRepository.findByUser(mockUser)).thenReturn(mockOrders);

        // Call service method
        List<Order> orders = orderService.getOrdersByUser(mockUser);

        // Assertions
        assertNotNull(orders);
        assertEquals(1, orders.size());
    }
}
