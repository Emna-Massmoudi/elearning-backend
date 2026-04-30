package com.elearning.elearning_api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Value("${upload.dir:uploads}")
    private String uploadDir;

    /**
     * POST /api/upload/pdf
     * Upload un fichier PDF — retourne l'URL accessible
     */
    @PostMapping("/pdf")
    @PreAuthorize("hasRole('FORMATEUR')")
    public ResponseEntity<Map<String, String>> uploadPdf(
            @RequestParam("file") MultipartFile file) throws IOException {

        // Valider le type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Seuls les fichiers PDF sont acceptés"));
        }

        // Valider la taille (10 MB max)
        if (file.getSize() > 10 * 1024 * 1024) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Fichier trop volumineux (max 10 MB)"));
        }

        // Créer le dossier si nécessaire
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Nom unique pour éviter les conflits
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename()
                .replaceAll("[^a-zA-Z0-9._-]", "_");

        // Sauvegarder le fichier
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Retourner l'URL
        String fileUrl = "/uploads/" + fileName;
        return ResponseEntity.ok(Map.of(
            "url",      fileUrl,
            "fileName", fileName,
            "size",     String.valueOf(file.getSize())
        ));
    }
}