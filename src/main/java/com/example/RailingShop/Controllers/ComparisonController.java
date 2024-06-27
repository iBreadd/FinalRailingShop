package com.example.RailingShop.Controllers;

import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Repositories.UserRepository;
import com.example.RailingShop.Services.ComparisonService;
import com.example.RailingShop.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/comparison")
public class ComparisonController {

    @Autowired
    private ComparisonService comparisonService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public String addProductToComparison(@RequestParam("productId") Long productId, Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "redirect:/shop/login";
        }

        comparisonService.addProductToComparison(user.getId(), productId);
        redirectAttributes.addFlashAttribute("message", "You add a product to compare!");
        return "redirect:/shop/all"; // or wherever the user should be redirected
    }

    @GetMapping
    public String getComparison(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "redirect:/shop/login";
        }

        List<Product> products = comparisonService.getProductsInComparison(user.getId());
        if (products.isEmpty()) {
            model.addAttribute("message", "No products to compare. Please add products for comparison.");
            return "comparison";
        }

        model.addAttribute("comparisonProducts", products);
        return "comparison"; // the view name for the comparison page
    }

    @PostMapping("/remove")
    public String removeProductFromComparison(@RequestParam("productId") Long productId, Model model,RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "redirect:/shop/login";
        }

        comparisonService.removeProductFromComparison(user.getId(), productId);
        redirectAttributes.addFlashAttribute("message", "You remove the product of comparing!");
        return "redirect:/comparison";
    }
}