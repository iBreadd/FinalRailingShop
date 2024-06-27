package com.example.RailingShop.Controllers;

import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Enums.Cart;
import com.example.RailingShop.Repositories.UserRepository;
import com.example.RailingShop.Services.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired UserRepository userRepository;
    @GetMapping("/cart")
    public String showCart(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user==null){
            return "redirect:/shop/login";
        }
            Cart cart = (Cart) session.getAttribute("cart");
//            user = (User) session.getAttribute("user");

            if (cart == null || cart.getOrderProducts() == null || cart.getOrderProducts().isEmpty()) {
                cart = new Cart();
                System.out.println("Cart - Cart empty 1 " + cart);
                session.setAttribute("message", "ShoppingCart Empty!!!");
                return "shCart";
            }

            model.addAttribute("cart", cart.getOrderProducts());
            model.addAttribute("total", cart.getTotalPrice());
            model.addAttribute("user", user);
            model.addAttribute("remoteUser", user != null ? user.getUsername() : null);
            return "shCart";

    }

    @PostMapping("/cart/add/{productId}")
    public ModelAndView addToCart(@RequestParam(name = "productId") Long productId, HttpSession session) {

        cartService.addItemToShoppingCart(productId, session);

        return new ModelAndView("redirect:/cart");
    }

    @PostMapping("/cart/updateQuantity")
    public ModelAndView updateQuantity(@RequestParam(name = "productId") Long productId, @RequestParam(name = "quantity") Integer quantity,
                                       HttpSession session) {
        cartService.updateItemQuantityInShoppingCart(productId, quantity, session);
        return new ModelAndView("redirect:/cart");
    }

    @PostMapping("/cart/remove/{productId}")
    public String removeFromCart(@RequestParam(name = "productId") Long productId, HttpSession session, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user==null){
            return "redirect:/shop/login";
        }
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart != null) {
                cartService.removeItemFromShoppingCart(productId, cart);
                redirectAttributes.addFlashAttribute("message", "You removed a product from cart!");
            }
            return "redirect:/cart";

    }

}

