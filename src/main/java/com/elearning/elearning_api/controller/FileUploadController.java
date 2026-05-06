package com.elearning.elearning_api.controller;

import com.elearning.elearning_api.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/pdf")
    @PreAuthorize("hasRole('FORMATEUR')")
    public ResponseEntity<Map<String, String>> uploadPdf(
            @RequestParam("file") MultipartFile file) throws IOException {

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Seuls les fichiers PDF sont acceptés"));
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Fichier trop volumineux (max 10 MB)"));
        }

        // ✅ Upload vers Cloudinary — URL permanente
        String fileUrl = cloudinaryService.uploadFile(file);

        return ResponseEntity.ok(Map.of(
            "url",      fileUrl,
            "fileName", file.getOriginalFilename(),
            "size",     String.valueOf(file.getSize())
        ));
    }

    // ✅ Endpoint pour images aussi
    @PostMapping("/image")
    @PreAuthorize("hasRole('FORMATEUR')")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file) throws IOException {

        if (file.getSize() > 10 * 1024 * 1024) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Fichier trop volumineux (max 10 MB)"));
        }

        String fileUrl = cloudinaryService.uploadFile(file);

        return ResponseEntity.ok(Map.of(
            "url",      fileUrl,
            "fileName", file.getOriginalFilename(),
            "size",     String.valueOf(file.getSize())
        ));
    }
}