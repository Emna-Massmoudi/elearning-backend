package com.elearning.elearning_api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", System.getenv("CLOUDINARY_CLOUD_NAME"),
            "api_key",    System.getenv("CLOUDINARY_API_KEY"),
            "api_secret", System.getenv("CLOUDINARY_API_SECRET")
        ));
    }

    public String uploadFile(MultipartFile file) throws IOException {
        Map result = cloudinary.uploader().upload(
            file.getBytes(),
            ObjectUtils.asMap(
                "resource_type", "auto",  // supporte PDF, images, vidéos
                "folder", "elearning"
            )
        );
        return (String) result.get("secure_url");
    }
}