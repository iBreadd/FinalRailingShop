package com.example.RailingShop;

import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Enums.Role;
import com.example.RailingShop.Repositories.UserRepository;
import com.example.RailingShop.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        // Mock data
        User user = new User(1L, "John", "Doe", "john.doe@example.com", "johndoe", "123456789", "City", "12345", "Street 1");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Call service method
        assertDoesNotThrow(() -> userService.register(user));

        // Verify interactions
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(passwordEncoder, times(1)).encode(user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

//    @Test
//    void testRegisterUser_UsernameAlreadyExists() {
//        // Mock data
//        User existingUser = new User(1L, "Jane", "Smith", "jane.smith@example.com", "janesmith", "987654321", "City", "54321", "Street 2");
//        User newUser = new User(2L, "John", "Doe", "john.doe@example.com", "janesmith", "123456789", "City", "12345", "Street 1");
//        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(existingUser);
//
//        // Call service method and assert exception
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.register(newUser));
//        assertEquals("Потребител с такова потребителско име вече съществува", exception.getMessage());
//
//        // Verify interactions
//        verify(userRepository, times(1)).findByUsername(newUser.getUsername());
//        verifyZeroInteractions(passwordEncoder);
//        verifyZeroInteractions(userRepository.save(any(User.class)));
//    }

    @Test
    void testUpdateUserInformation() {
        // Mock data
        User user = new User(1L, "John", "Doe", "john.doe@example.com", "johndoe", "123456789", "City", "12345", "Street 1");

        // Call service method
        assertDoesNotThrow(() -> userService.updateUserInformation(user));

        // Verify interactions
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindUserById() {
        // Mock data
        Long userId = 1L;
        User user = new User(userId, "John", "Doe", "john.doe@example.com", "johndoe", "123456789", "City", "12345", "Street 1");
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        // Call service method
        User foundUser = userService.findById(userId);

        // Assertions
        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());

        // Verify interactions
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetAdminUsers() {
        // Mock data
        List<User> admins = new ArrayList<>();
        admins.add(new User(1L, "John", "Doe", "john.doe@example.com", "johndoe", "123456789", "City", "12345", "Street 1"));
        admins.add(new User(2L, "Jane", "Smith", "jane.smith@example.com", "janesmith", "987654321", "City", "54321", "Street 2"));
        when(userRepository.findAllByRole(Role.ADMIN)).thenReturn(admins);

        // Call service method
        List<User> sortedAdminsByUsername = userService.getAdminUsers("username", "asc");
        List<User> sortedAdminsByFirstName = userService.getAdminUsers("firstName", "asc");
        List<User> sortedAdminsByLastName = userService.getAdminUsers("lastName", "asc");

        // Assertions
        assertEquals(2, sortedAdminsByUsername.size());
        assertEquals(2, sortedAdminsByFirstName.size());
        assertEquals(2, sortedAdminsByLastName.size());

        // Verify interactions
        verify(userRepository, times(3)).findAllByRole(Role.ADMIN);
    }
}
