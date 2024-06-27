package com.example.RailingShop.Controllers;

import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/shop")
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping
    public String getIndexMenu(){
        return "index";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }
    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
         User userToBlockChain = userRepository.findByUsername(user.getUsername());
        if (userToBlockChain == null || !passwordEncoder.matches(user.getPassword(), userToBlockChain.getPassword())) {
            redirectAttributes.addFlashAttribute("wrongCredentials", "wrong credentials");
            return "/shop/login";
        }
        return "redirect:/shop/home";
    }
    @GetMapping("/home")
    public String showHome(Model model) {
    // Проверяваме дали потребителят е аутентициран
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
        // Ако потребителят е аутентициран, вземаме информацията за него
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);

        if (user != null) {
            // Подаваме информацията за потребителя на модела
            model.addAttribute("user", user);
            model.addAttribute("userRole", user.getRole().toString());
        }
    }
    // Връщаме шаблона за началната страница
        return "home";
    }
    @GetMapping("/logout")
    public String logoutButton(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user==null){
            return "redirect:/shop/login";
        }
        session.removeAttribute("user");
        System.out.println("remove session");
        return "redirect:/shop/login";
    }
}

