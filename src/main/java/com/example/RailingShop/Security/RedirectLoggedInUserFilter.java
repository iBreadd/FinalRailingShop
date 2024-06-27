package com.example.RailingShop.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RedirectLoggedInUserFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String requestURI = request.getRequestURI();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser") &&
                (requestURI.equals("/") || requestURI.equals("/shop/login") || requestURI.equals("/shop/register"))) {
            response.sendRedirect("/shop/home");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
