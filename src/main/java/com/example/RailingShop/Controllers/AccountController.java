package com.example.RailingShop.Controllers;

import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Repositories.UserRepository;
import com.example.RailingShop.Services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/shop")
public class AccountController {

    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/accountDetails")
    public String accountDetails(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
             if(user!=null) {
                model.addAttribute("user", user);
                model.addAttribute("userRole", user.getRole().toString());

            }
        }
        return "address_up";
    }

    @PostMapping("/updateAddress")
    public String saveUserForm(@Valid User userForm, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "redirect:/shop/login";
        }
        if (bindingResult.hasErrors()) {
            return "address_up";
        }

        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setEmail(userForm.getEmail());
        user.setPhone(userForm.getPhone());
        user.setCity(userForm.getCity());
        user.setPostalCode(userForm.getPostalCode());
        user.setAddress(userForm.getAddress());

        userService.updateUserInformation(user);

        User updatedUser = userService.findById(user.getId());
        session.setAttribute("user", updatedUser);
        redirectAttributes.addFlashAttribute("message", "Your information has been updated successfully!!!");
        return "redirect:/shop/all";
    }
}


