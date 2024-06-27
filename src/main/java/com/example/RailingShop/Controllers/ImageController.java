package com.example.RailingShop.Controllers;

import com.example.RailingShop.dto.ImageUploadResponse;
//import com.example.RailingShop.Repositories.ImageRepository;
import com.example.RailingShop.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ImageUploadResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = imageService.uploadImage(file);
            return ResponseEntity.ok(new ImageUploadResponse(imageUrl));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new ImageUploadResponse("Image upload failed"));
        }
    }
}
