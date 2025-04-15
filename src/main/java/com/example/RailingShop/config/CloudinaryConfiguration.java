package com.example.RailingShop.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CloudinaryConfiguration {
    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
       "cloud_name","dvrordtc2",
        "api_key","YOUR_API_KEY",
        "api_secret","YOUR_API_SECRET_KEY"));
    }
}
