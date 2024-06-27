package com.example.RailingShop.Controllers;

import com.example.RailingShop.Entities.Order;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Enums.OrderStatus;
import com.example.RailingShop.Repositories.UserRepository;
import com.example.RailingShop.Services.OrderService;
import com.example.RailingShop.Services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/shop")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("user",new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute @Valid User user, BindingResult bindingResult,Model model){
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            userService.register(user);
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
        return "redirect:/shop/login";
    }

    @GetMapping("/userOrders")
    public String userOrders(Model model, RedirectAttributes redirectAttributes){
//        User loggedInUser=(User) session.getAttribute("user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loggedInUser = userRepository.findByUsername(username);
        if(loggedInUser==null){
            redirectAttributes.addFlashAttribute("message","You are not logged in!!!");
            return "redirect:/shop/all";
        }
        List<Order>userOrders=orderService.getOrdersByUser(loggedInUser);
        model.addAttribute("userOrders",userOrders);
        model.addAttribute("user",loggedInUser);
        return "userOrders";
    }


    @GetMapping("/admins")
    public String getEmployees(@RequestParam(defaultValue = "username") String sortBy,
                            @RequestParam(defaultValue = "asc") String sortDirection,
                            Model model) {
        List<User> admins = userService.getAdminUsers(sortBy, sortDirection);

        model.addAttribute("admins", admins);
        model.addAttribute("sortDirection", sortDirection);

        return "admins";
    }

    @GetMapping("/orders")
    public String showOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("statuses", OrderStatus.values());
        return "orders";
    }

    @PostMapping("/orders/filter")
    public String filterOrders(@RequestParam("status") String status, Model model) {
        List<Order> orders;
        if (status.equals("ALL")) {
            orders = orderService.getAllOrders();
        } else {
            OrderStatus orderStatus = OrderStatus.valueOf(status);
            orders = orderService.getOrdersByStatus(orderStatus);
        }
        model.addAttribute("orders", orders);
        model.addAttribute("statuses", OrderStatus.values());
        return "orders";
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam(name = "orderStatus") OrderStatus status, Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user==null){
            return "redirect:/shop/login";
        }
        orderService.updateOrderStatus(id, status);
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "redirect:/shop/orders";
    }

    @GetMapping("/orders/details/{id}")
    public String getOrderDetails(@PathVariable("id") Long id, Model model) {
        Order order = orderService.getOrderById(id);
        if (order == null || order.getOrderProducts().isEmpty()) {
            return "redirect:/orders";
        }
        model.addAttribute("order", order);
        model.addAttribute("user", order.getUser());
        return "orderDetails";
    }

}
