package com.example.RailingShop.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService(){
        return new MyUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/static/**","/resources/**","/error/**","/img/**","/assets/**","/css/**","/js/**").permitAll()
                                .requestMatchers("/","/shop/home","/shop/all","/shop/index","/shop/login","/shop/register", "/shop/products/{productId}/reviews").permitAll()
                                .requestMatchers("cart","/order/cancel","/order/success","/cart", "/cart/**","/cart/add/{productId}","/cart/updateQuantity"," /cart/remove/{productId}","/messages/send",
                                        "/messages/inbox","/comparison","/comparison/add","/comparison/remove","/favourites",
                                        "/favourites/add","/favourites/remove","/order/add","/order/addOrder" , "/shop/products/{productId}/reviews",
                                        "/shop/products/{productId}/reviews/new" ,"/shop/userOrders").hasAuthority("USER")
                                .requestMatchers("/messages/delete","/shop/products/{productId}/reviews","/shop/accountDetails", "/shop/updateAddress").hasAnyAuthority("ADMIN","USER")
                                .requestMatchers("/logout","/shop/logout").permitAll()
                                .anyRequest().hasAnyAuthority("ADMIN")
                )
                .formLogin((form) -> form
                        .loginPage("/shop/login")
                        .permitAll()
                        .defaultSuccessUrl("/shop/home",true)
                )
                .logout((logout) -> logout
                        .logoutUrl("/shop/logout")
                        .permitAll()
                ).addFilterBefore(new RedirectLoggedInUserFilter(), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}