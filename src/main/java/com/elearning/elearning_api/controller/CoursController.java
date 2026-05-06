package com.elearning.elearning_api.controller;

import com.elearning.elearning_api.dto.request.CoursRequest;
import com.elearning.elearning_api.dto.response.CoursResponse;
import com.elearning.elearning_api.enums.EtatCours;
import com.elearning.elearning_api.service.CoursService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cours")
@RequiredArgsConstructor
public class CoursController {

    private final CoursService coursService;

    // FORMATEUR : créer une formation
    @PostMapping
    @PreAuthorize("hasRole('FORMATEUR')")
    public ResponseEntity<CoursResponse> create(@Valid @RequestBody CoursRequest request) {
        return ResponseEntity.ok(coursService.create(request));
    }

    // FORMATEUR : modifier une formation
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FORMATEUR')")
    public ResponseEntity<CoursResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody CoursRequest request) {
        return ResponseEntity.ok(coursService.update(id, request));
    }

    // FORMATEUR ou ADMIN : supprimer une formation
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FORMATEUR') or hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        coursService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // PUBLIC : récupérer toutes les formations
    @GetMapping
    public ResponseEntity<List<CoursResponse>> getAll() {
        return ResponseEntity.ok(coursService.getAll());
    }

    // PUBLIC : récupérer les formations d'un formateur
    @GetMapping("/formateur/{formateurId}")
    public ResponseEntity<List<CoursResponse>> getByFormateur(@PathVariable Long formateurId) {
        return ResponseEntity.ok(coursService.getByFormateur(formateurId));
    }

    // PUBLIC : récupérer les formations par état
    @GetMapping("/etat/{etat}")
    public ResponseEntity<List<CoursResponse>> getByEtat(@PathVariable EtatCours etat) {
        return ResponseEntity.ok(coursService.getByEtat(etat));
    }

    // PUBLIC : récupérer une formation par id
    @GetMapping("/{id}")
    public ResponseEntity<CoursResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(coursService.getById(id));
    }

    // FORMATEUR ou ADMIN : changer l'état d'une formation
    @PatchMapping("/{id}/etat")
    @PreAuthorize("hasRole('FORMATEUR') or hasRole('ADMIN')")
    public ResponseEntity<CoursResponse> updateEtat(@PathVariable Long id,
                                                    @RequestParam EtatCours etat) {
        return ResponseEntity.ok(coursService.updateEtat(id, etat));
    }
}