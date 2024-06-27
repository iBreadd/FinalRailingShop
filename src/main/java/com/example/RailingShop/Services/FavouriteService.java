package com.example.RailingShop.Services;

import com.example.RailingShop.Entities.Favourite;
import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Repositories.FavouriteRepository;
import com.example.RailingShop.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavouriteService {

    @Autowired
    private FavouriteRepository favouriteRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getFavouritesByUserId(Long userId) {
        List<Favourite> favourites = favouriteRepository.findByUserId(userId);
        List<Product> favouriteProducts = new ArrayList<>();
        for (Favourite favourite : favourites) {
            favouriteProducts.add(favourite.getProduct());
        }
        return favouriteProducts;
    }

    public void addFavourite(Long userId, Long productId) {
        Favourite favourite = new Favourite();
        User user = new User();
        user.setId(userId);
        favourite.setUser(user);

        Product product = new Product();
        product.setId(productId);
        favourite.setProduct(product);

        favouriteRepository.save(favourite);
    }

    public void removeFavourite(Long userId, Long productId) {
        List<Favourite> favourites = favouriteRepository.findByUserIdAndProductId(userId, productId);
        for (Favourite favourite : favourites) {
            favouriteRepository.delete(favourite);
        }
    }
}



