package com.example.RailingShop;

import com.example.RailingShop.Entities.Favourite;
import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Repositories.FavouriteRepository;
import com.example.RailingShop.Repositories.ProductRepository;
import com.example.RailingShop.Services.FavouriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class FavouriteServiceTest {

    @Mock
    private FavouriteRepository favouriteRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FavouriteService favouriteService;

    @BeforeEach
    void setUp() {
        // Инициализация на моковете и инжектиране на тяхната зависимост
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFavouritesByUserId() {
        // Мокване на резултатите от репозиторията
        Long userId = 1L;
        List<Favourite> favourites = new ArrayList<>();
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);
        favourites.add(new Favourite(userId, product1));
        favourites.add(new Favourite(userId, product2));
        when(favouriteRepository.findByUserId(userId)).thenReturn(favourites);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));

        // Извикване на метода от сервиза
        List<Product> favouriteProducts = favouriteService.getFavouritesByUserId(userId);

        // Проверка на резултатите
        assertNotNull(favouriteProducts);
        assertEquals(2, favouriteProducts.size());
        assertTrue(favouriteProducts.contains(product1));
        assertTrue(favouriteProducts.contains(product2));
    }

    @Test
    void testAddFavourite() {
        // Мокване на параметрите
        Long userId = 1L;
        Long productId = 1L;
        Favourite favourite = new Favourite();
        favourite.setUser(new User(userId));
        favourite.setProduct(new Product(productId));
        when(favouriteRepository.save(any())).thenReturn(favourite);

        // Извикване на метода от сервиза
        favouriteService.addFavourite(userId, productId);

        // Проверка дали методът save на репозиторията е бил извикан точно веднъж с правилния параметър
        verify(favouriteRepository, times(1)).save(any());
    }

    @Test
    void testRemoveFavourite() {
        // Мокване на резултатите от репозиторията
        Long userId = 1L;
        Long productId = 1L;
        List<Favourite> favourites = new ArrayList<>();
        favourites.add(new Favourite(userId, new Product(productId)));
        when(favouriteRepository.findByUserIdAndProductId(userId, productId)).thenReturn(favourites);

        // Извикване на метода от сервиза
        favouriteService.removeFavourite(userId, productId);

        // Проверка дали методът delete на репозиторията е бил извикан точно веднъж с правилния параметър
        verify(favouriteRepository, times(1)).delete(any());
    }
}
