package com.example.RailingShop;

import com.example.RailingShop.Controllers.AccountController;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Enums.Role;
import com.example.RailingShop.Services.UserService;
import com.example.RailingShop.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "testuser")
    void testAccountDetails() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setRole(Role.USER);

        when(userRepository.findByUsername(anyString())).thenReturn(user);

        mockMvc.perform(get("/shop/accountDetails"))
                .andExpect(status().isOk())
                .andExpect(view().name("address_up"))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("userRole", user.getRole().toString()));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testSaveUserForm() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userService.findById(anyLong())).thenReturn(user);

        mockMvc.perform(post("/shop/updateAddress")
                        .param("firstName", "NewFirstName")
                        .param("lastName", "NewLastName")
                        .param("email", "newemail@example.com")
                        .param("phone", "1234567890")
                        .param("city", "NewCity")
                        .param("postalCode", "12345")
                        .param("address", "NewAddress")
                        .with(csrf())) // Добавяне на CSRF токен
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/shop/all"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "Your information has been updated successfully!!!"));

        verify(userService, times(1)).updateUserInformation(any(User.class));
        verify(userService, times(1)).findById(anyLong());
    }
}
