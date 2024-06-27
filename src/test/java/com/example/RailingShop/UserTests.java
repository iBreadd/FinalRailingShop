package com.example.RailingShop;

import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Enums.Role;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testUserCreation_WithValidData() {
        User user = new User(1L, "Иван", "Иванов", "ivan@example.com", "ivan123", "1234567890", "София", "1000", "ул. Примерна 1");
        user.setPassword("password");
        user.setRole(Role.USER);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testUserCreation_WithInvalidEmail() {
        User user = new User(1L, "Иван", "Иванов", "invalidemail", "ivan123", "1234567890", "София", "1000", "ул. Примерна 1");
        user.setPassword("password");
        user.setRole(Role.USER);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Въведете валиден имейл адрес", violations.iterator().next().getMessage());
    }

    @Test
    public void testUserCreation_WithInvalidPostalCode() {
        User user = new User(1L, "Иван", "Иванов", "ivan@example.com", "ivan123", "1234567890", "София", "123", "ул. Примерна 1");
        user.setPassword("password");
        user.setRole(Role.USER);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Пощенският код трябва да съдържа точно 4 цифри", violations.iterator().next().getMessage());
    }

    @Test
    public void testUserEquality() {
        User user1 = new User(1L, "Иван", "Иванов", "ivan@example.com", "ivan123", "1234567890", "София", "1000", "ул. Примерна 1");
        user1.setPassword("password");
        user1.setRole(Role.USER);

        User user2 = new User(1L, "Иван", "Иванов", "ivan@example.com", "ivan123", "1234567890", "София", "1000", "ул. Примерна 1");
        user2.setPassword("password");
        user2.setRole(Role.USER);

        // Сравнение на всички полета, които считате за важни
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getFirstName(), user2.getFirstName());
        assertEquals(user1.getLastName(), user2.getLastName());
        assertEquals(user1.getEmail(), user2.getEmail());
        assertEquals(user1.getUsername(), user2.getUsername());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertEquals(user1.getPhone(), user2.getPhone());
        assertEquals(user1.getCity(), user2.getCity());
        assertEquals(user1.getPostalCode(), user2.getPostalCode());
        assertEquals(user1.getAddress(), user2.getAddress());
        assertEquals(user1.getRole(), user2.getRole());
    }

    @Test
    public void testUserInequality() {
        User user1 = new User(1L, "Иван", "Иванов", "ivan@example.com", "ivan123", "1234567890", "София", "1000", "ул. Примерна 1");
        user1.setPassword("password");
        user1.setRole(Role.USER);

        User user2 = new User(2L, "Петър", "Петров", "petar@example.com", "petar123", "0987654321", "Пловдив", "2000", "ул. Друга 2");
        user2.setPassword("pass");

        assertNotEquals(user1, user2);
    }
}
