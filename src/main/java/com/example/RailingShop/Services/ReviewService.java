package com.example.RailingShop.Services;

import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Entities.Review;
import com.example.RailingShop.Repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ProductService productService;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public void saveReview(Review review) {
        reviewRepository.save(review);
        Product product = review.getProduct();
        product.setReviewsCount(product.getReviewsCount() + 1);
        double averageRating = productService.getAverageRatingForProduct(product.getId());
        product.setAverageRating(averageRating);
        productService.save(product);
    }

}
