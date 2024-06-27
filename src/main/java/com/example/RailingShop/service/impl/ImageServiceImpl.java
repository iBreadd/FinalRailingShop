package com.example.RailingShop.service.impl;

import com.example.RailingShop.service.CloudinaryService;
import com.example.RailingShop.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinaryService.uploadFile(file);
        return (String) uploadResult.get("url");
    }
}
