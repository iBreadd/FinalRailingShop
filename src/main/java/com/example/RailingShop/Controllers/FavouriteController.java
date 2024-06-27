package com.example.RailingShop.Controllers;

import com.example.RailingShop.Entities.Favourite;
import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Repositories.UserRepository;
import com.example.RailingShop.Services.FavouriteService;
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

import java.util.List;

@Controller
@RequestMapping("/favourites")
public class FavouriteController {

    @Autowired
    private FavouriteService favouriteService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String getFavourites(Model model) {
        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/shop/login";
        }
        List<Product> favouritesProducts = favouriteService.getFavouritesByUserId(user.getId());
        model.addAttribute("favouritesProducts", favouritesProducts);
        return "favourites"; // Името на HTML страницата за любими продукти
    }

    @PostMapping("/add")
    public String addFavourite(@RequestParam Long productId, RedirectAttributes redirectAttributes) {
        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/shop/login";
        }
        favouriteService.addFavourite(user.getId(), productId);
        redirectAttributes.addFlashAttribute("message", "You add a product to favourites!");
        return "redirect:/shop/all"; // Редирект към страницата за любими продукти
    }

    @PostMapping("/remove")
    public String removeFavourite(@RequestParam Long productId,RedirectAttributes redirectAttributes) {
        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/shop/login";
        }
        favouriteService.removeFavourite(user.getId(), productId);
        redirectAttributes.addFlashAttribute("message", "You removed a product from favourites!");
        return "redirect:/favourites";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }
}




