package com.example.RailingShop.Entities;

import com.example.RailingShop.Enums.ProductColor;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "deleted_products")
public class DeletedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private ProductColor color;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public DeletedProduct(){

    }
    public DeletedProduct(Product product){
        this.name=product.getName();
        this.price=product.getPrice();
        this.quantity=product.getQuantity();
        this.color=product.getColor();
        this.deletedAt=new Timestamp(System.currentTimeMillis()).toLocalDateTime();
    }
}
