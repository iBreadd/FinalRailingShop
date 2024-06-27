package com.example.RailingShop.Entities;

import com.example.RailingShop.Enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @NotBlank(message = "Името е задължително")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Фамилията е задължителна")
    private String lastName;

    @Column(name = "email", unique = true)
    @NotBlank(message = "Имейлът е задължителен")
    @Email(message = "Въведете валиден имейл адрес")
    private String email;

    @Column(name = "username", unique = true)
    @NotBlank(message = "Потребителското име е задължително")
    private String username;
    @Length(min = 8)
    private String password;
    @Column(name = "phone")
    @NotBlank(message = "Телефонът е задължителен")
    @Pattern(regexp = "^\\+?[0-9\\s-]+$", message = "Въведете валиден телефонен номер")
    private String phone;

    @Column(name = "city")
    @NotBlank(message = "Градът е задължителен")
    private String city;

    @Column(name = "postal_code")
    @NotNull(message = "Пощенският код е задължителен")
    @Size(min = 4, max = 4, message = "Пощенският код трябва да съдържа точно 4 цифри")
    private String postalCode;

    @Column(name = "address")
    @NotBlank(message = "Адресът е задължителен")
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String firstName, String lastName, String email, String username, String phone, String city, String postalCode, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.city = city;
        this.postalCode = postalCode;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
