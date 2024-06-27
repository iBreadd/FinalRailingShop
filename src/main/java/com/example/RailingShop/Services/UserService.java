package com.example.RailingShop.Services;

import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Enums.Role;
import com.example.RailingShop.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(User user){
        User existingUser=userRepository.findByUsername(user.getUsername());
        if (existingUser!=null){
            throw new RuntimeException("Потребител с такова потребителско име вече съществува");
        }
        String hashedPassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public void updateUserInformation(User user){
        userRepository.save(user);
    }

    public User findById(Long userId){
        return userRepository.findById(userId).get();
    }

    public List<User> getAdminUsers(String sortBy, String sortDirection) {
        List<User> admins = userRepository.findAllByRole(Role.ADMIN);

        Comparator<User> comparator = switch (sortBy) {
            case "username" -> Comparator.comparing(User::getUsername);
            case "firstName" -> Comparator.comparing(User::getFirstName);
            case "lastName" -> Comparator.comparing(User::getLastName);
            default -> null;
        };

        if (comparator != null) {
            if ("desc".equals(sortDirection)) {
                comparator = comparator.reversed();
            }
            admins.sort(comparator);
        }

        return admins;
    }

}
