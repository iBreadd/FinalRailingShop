package com.example.RailingShop;

import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Entities.Review;
import com.example.RailingShop.Entities.User;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReviewTests {

    private Validator validator;

    public ReviewTests() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testReviewConstructorAndGetters() {
        User user = new User(1L, "Иван", "Иванов", "ivan@example.com", "ivan123", "1234567890", "София", "1000", "ул. Примерна 1");
        Product product = new Product(1L, "Някакъв продукт", "Описание на продукта", BigDecimal.valueOf(100.0), 1, true, null, null, null, null);

        Review review = new Review(1L, user, product, 4, "Добър продукт");

        assertEquals(1L, review.getId());
        assertEquals(user, review.getUser());
        assertEquals(product, review.getProduct());
        assertEquals(4, review.getRating());
        assertEquals("Добър продукт", review.getComment());
    }

    @Test
    public void testReviewSetters() {
        Review review = new Review();
        User user = new User(1L, "Иван", "Иванов", "ivan@example.com", "ivan123", "1234567890", "София", "1000", "ул. Примерна 1");
        Product product = new Product(1L, "Някакъв продукт", "Описание на продукта", BigDecimal.valueOf(100.0), 1, true, null, null, null, null);

        review.setId(1L);
        review.setUser(user);
        review.setProduct(product);
        review.setRating(5);
        review.setComment("Много добър продукт");

        assertEquals(1L, review.getId());
        assertEquals(user, review.getUser());
        assertEquals(product, review.getProduct());
        assertEquals(5, review.getRating());
        assertEquals("Много добър продукт", review.getComment());
    }

    @Test
    public void testRatingValidation() {
        User user = new User(1L, "Иван", "Иванов", "ivan@example.com", "ivan123", "1234567890", "София", "1000", "ул. Примерна 1");
        Product product = new Product(1L, "Някакъв продукт", "Описание на продукта", BigDecimal.valueOf(100.0), 1, true, null, null, null, null);

        Review review = new Review(1L, user, product, 3, "Общо взето");

        assertEquals(3, review.getRating());

        // Очакваме грешка при опит за създаване на review с невалиден rating
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            new Review(1L, user, product, 6, "Невалиден rating");
        });

        assertEquals("rating", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testCommentValidation() {
        User user = new User(1L, "Иван", "Иванов", "ivan@example.com", "ivan123", "1234567890", "София", "1000", "ул. Примерна 1");
        Product product = new Product(1L, "Някакъв продукт", "Описание на продукта", BigDecimal.valueOf(100.0), 1, true, null, null, null, null);

        Review review = new Review(1L, user, product, 4, "Добър продукт");

        assertEquals("Добър продукт", review.getComment());

        // Очакваме грешка при опит за създаване на review с празен коментар
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            new Review(1L, user, product, 4, "");
        });

        assertEquals("comment", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }
}
