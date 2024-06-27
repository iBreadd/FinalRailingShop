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
        "api_key","637785526863458",
        "api_secret","cIUA0SsDfRY0qmGyaCW8SvYSi1s"));
    }
}
