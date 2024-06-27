package com.example.RailingShop;

import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Entities.Review;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Repositories.ReviewRepository;
import com.example.RailingShop.Services.ProductService;
import com.example.RailingShop.Services.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllReviews() {
        // Mock data
        List<Review> mockReviews = new ArrayList<>();
        mockReviews.add(new Review(1L, new User(), new Product(), 5, "Good product"));
        mockReviews.add(new Review(2L, new User(), new Product(), 4, "Not bad"));
        when(reviewRepository.findAll()).thenReturn(mockReviews);

        // Call service method
        List<Review> reviews = reviewService.getAllReviews();

        // Assertions
        assertEquals(2, reviews.size());
    }

    @Test
    void testSaveReview() {
        // Mock data
        Review review = new Review(1L, new User(), new Product(), 4, "Nice product");

        // Mock ProductService behavior
        when(productService.getAverageRatingForProduct(any(Long.class))).thenReturn(4.5); // Assuming average rating is 4.5 after save
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // Call service method
        reviewService.saveReview(review);

        // Verify interactions
        verify(reviewRepository, times(1)).save(review);
        verify(productService, times(1)).getAverageRatingForProduct(any(Long.class));

        // Assertions (assuming ProductService saves the product correctly)
        assertEquals(1, review.getProduct().getReviewsCount());
        assertEquals(4.5, review.getProduct().getAverageRating());
    }
}
