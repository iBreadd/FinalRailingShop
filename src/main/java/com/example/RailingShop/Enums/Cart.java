package com.example.RailingShop.Enums;

import com.example.RailingShop.Entities.OrderProduct;
import com.example.RailingShop.Entities.User;

import java.math.BigDecimal;
import java.util.List;

public class Cart {
    private Long id;
    private BigDecimal totalPrice;
    private User user;
    private List<OrderProduct>orderProducts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", totalPrice=" + totalPrice +
                ", user=" + user +
                ", orderProducts=" + orderProducts +
                '}';
    }
}
