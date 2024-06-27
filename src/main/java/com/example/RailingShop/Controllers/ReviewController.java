package com.example.RailingShop.Controllers;

import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Entities.Review;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Repositories.ProductRepository;
import com.example.RailingShop.Repositories.UserRepository;
import com.example.RailingShop.Services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/shop")
public class ReviewController {
    @Autowired
    ReviewService reviewService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/products/{productId}/reviews")
    public String showReviews(@PathVariable Long productId, Model model) {
        Product product = productRepository.findById(productId).orElseThrow();
        model.addAttribute("product", product);
        model.addAttribute("reviews", product.getReviews());

        // Проверка дали потребителят е аутентикиран
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);
            if (user != null) {
                // Подаваме информацията за потребителя на модела
                model.addAttribute("user", user);
                model.addAttribute("userRole", user.getRole().toString());
            }
        }

        return "reviews";
    }


    @GetMapping("/products/{productId}/reviews/new")
    public String showNewReviewForm(@PathVariable Long productId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return "redirect:/shop/login";
        }

        Review review = new Review();
        review.setProduct(productRepository.findById(productId).orElseThrow());
        review.setUser(user);
        model.addAttribute("review", review);
        return "new-review";
    }

    @PostMapping("/products/{productId}/reviews")
    public String saveReview(@PathVariable Long productId, @ModelAttribute Review review, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return "redirect:/shop/login";
        }

        review.setUser(user);
        review.setProduct(productRepository.findById(productId).orElseThrow());
        reviewService.saveReview(review);
        redirectAttributes.addFlashAttribute("message", "You add a review to product!");
        return "redirect:/shop/products/" + productId + "/reviews";
    }
}
