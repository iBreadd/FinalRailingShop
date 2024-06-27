package com.example.RailingShop.Repositories;

import com.example.RailingShop.Entities.Comparison;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface ComparisonRepository extends JpaRepository<Comparison, Long> {
    List<Comparison> findByUserId(Long userId);
}

