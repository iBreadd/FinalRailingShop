package com.example.RailingShop.Repositories;

import com.example.RailingShop.Entities.Order;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("SELECT o FROM Order o WHERE (:status IS NULL OR o.status = :status)")
    List<Order> getOrdersByStatus(@Param("status") OrderStatus status);

    List<Order> findByUser(User user);
}
