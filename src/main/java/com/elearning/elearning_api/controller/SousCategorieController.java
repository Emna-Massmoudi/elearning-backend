package com.elearning.elearning_api.controller;

import com.elearning.elearning_api.dto.request.SousCategorieRequest;
import com.elearning.elearning_api.dto.response.SousCategorieResponse;
import com.elearning.elearning_api.service.SousCategorieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sous-categories")
@RequiredArgsConstructor
public class SousCategorieController {

    private final SousCategorieService sousCategorieService;

    // ADMIN seulement : créer une sous-catégorie
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SousCategorieResponse> create(@Valid @RequestBody SousCategorieRequest request) {
        return ResponseEntity.ok(sousCategorieService.create(request));
    }

    // ADMIN seulement : modifier une sous-catégorie
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SousCategorieResponse> update(@PathVariable Long id,
                                                        @Valid @RequestBody SousCategorieRequest request) {
        return ResponseEntity.ok(sousCategorieService.update(id, request));
    }

    // ADMIN seulement : supprimer une sous-catégorie
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sousCategorieService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // PUBLIC : récupérer toutes les sous-catégories
    @GetMapping
    public ResponseEntity<List<SousCategorieResponse>> getAll() {
        return ResponseEntity.ok(sousCategorieService.getAll());
    }

    // PUBLIC : récupérer les sous-catégories d'une catégorie
    @GetMapping("/categorie/{categorieId}")
    public ResponseEntity<List<SousCategorieResponse>> getByCategorie(@PathVariable Long categorieId) {
        return ResponseEntity.ok(sousCategorieService.getByCategorie(categorieId));
    }

    // PUBLIC : récupérer une sous-catégorie par id
    @GetMapping("/{id}")
    public ResponseEntity<SousCategorieResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sousCategorieService.getById(id));
    }
}