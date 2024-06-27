package com.example.RailingShop.Services;

import com.example.RailingShop.Entities.Order;
import com.example.RailingShop.Entities.OrderProduct;
import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Enums.Cart;
import com.example.RailingShop.Enums.OrderStatus;
import com.example.RailingShop.Exceptions.OrderNotFoundException;
import com.example.RailingShop.Repositories.OrderProductRepository;
import com.example.RailingShop.Repositories.OrderRepository;
import com.example.RailingShop.Repositories.ProductRepository;
import com.example.RailingShop.Repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Order> getAllOrders(){
        return (List<Order>) orderRepository.findAll();
    }
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).get();
    }

    public Order saveOrder(Order order){
        return orderRepository.save(order);
    }

    public void deleteOrderById(Long id){
        orderRepository.deleteById(id);
    }

    public void saveOrder(Cart cart, HttpSession session){
//        User User =(User) session.getAttribute("user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User is not logged in");
        }
        Order order=new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.valueOf("PENDING"));
        order.setTotalPrice(cart.getTotalPrice());
        List<OrderProduct>orderProductsChecked=checkProductQuantity(cart,order);

        orderRepository.save(order);
        order.setOrderProducts(orderProductsChecked);
        orderProductRepository.saveAll(orderProductsChecked);
    }
    public List<OrderProduct> checkProductQuantity(Cart cart,Order order){
        List<OrderProduct> orderProductsChecked = new ArrayList<>();
        for (OrderProduct orderProduct : cart.getOrderProducts()) {
            Product product = productRepository.findById(orderProduct.getProduct().getId()).get();
            if (product.getQuantity() < orderProduct.getQuantity()) {
                throw new RuntimeException("The available quantity for: " + product.getName() + " is " + product.getQuantity());
            }
            product.setQuantity(product.getQuantity() - orderProduct.getQuantity());
            productRepository.save(product);
            orderProduct.setOrder(order);
            orderProductsChecked.add(orderProduct);
        }
        return orderProductsChecked;
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        if (status == null) {
            return (List<Order>) orderRepository.findAll();
        } else {
            return orderRepository.getOrdersByStatus(status);
        }
    }

    @Transactional
    public void updateOrderStatus(Long id, OrderStatus status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
//        User user =(User) session.getAttribute("user");
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        order.setStatus(status);
        orderRepository.save(order);
    }
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }
}
