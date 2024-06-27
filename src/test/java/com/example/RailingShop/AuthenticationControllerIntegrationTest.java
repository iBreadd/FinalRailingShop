package com.example.RailingShop;

import com.example.RailingShop.Controllers.AuthenticationController;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Enums.Role;
import com.example.RailingShop.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    public void testGetIndexMenu() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/shop"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void testShowLoginForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/shop/login"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("login"));
    }

    @Test
    public void testLoginWithValidCredentials() throws Exception {
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword("encodedPassword");

        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(mockUser);
        Mockito.when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/shop/login")
                        .param("username", "testuser")
                        .param("password", "password"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/shop/home"));
    }

    @Test
    public void testLoginWithInvalidCredentials() throws Exception {
        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/shop/login")
                        .param("username", "testuser")
                        .param("password", "wrongpassword"))
                .andExpect(status().isFound())
                .andExpect(flash().attributeExists("wrongCredentials"))
                .andExpect(view().name("/shop/login"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testShowHome() throws Exception {
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setRole(Role.USER);

        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(mockUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/shop/home"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("userRole"))
                .andExpect(view().name("home"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testLogoutButton() throws Exception {
        User mockUser = new User();
        mockUser.setUsername("testuser");

        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(mockUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/shop/logout")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/shop/login"));
    }
}
