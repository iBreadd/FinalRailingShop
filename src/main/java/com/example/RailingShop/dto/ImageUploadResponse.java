package com.example.RailingShop.dto;

import lombok.Data;

@Data
public class ImageUploadResponse {
    private String url;

    public ImageUploadResponse(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
