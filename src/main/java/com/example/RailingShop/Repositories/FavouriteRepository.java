package com.example.RailingShop.Repositories;

import com.example.RailingShop.Entities.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    List<Favourite> findByUserId(Long userId);
    List<Favourite> findByUserIdAndProductId(Long userId, Long productId);
}
