package com.elearning.elearning_api.controller;

import com.elearning.elearning_api.dto.response.EtudiantResponse;
import com.elearning.elearning_api.dto.response.FormateurResponse;
import com.elearning.elearning_api.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ── Étudiants ──────────────────────────────────────

    @GetMapping("/etudiants")
    @PreAuthorize("hasRole('ADMIN')")
    public List<EtudiantResponse> getAllEtudiants() {
        return adminService.getAllEtudiants();
    }

    @GetMapping("/etudiants/bloques")
    @PreAuthorize("hasRole('ADMIN')")
    public List<EtudiantResponse> getEtudiantsBloques() {
        return adminService.getEtudiantsBloques();
    }

    @PatchMapping("/etudiants/{id}/bloquer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EtudiantResponse> bloquerEtudiant(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.bloquerEtudiant(id));
    }

    @PatchMapping("/etudiants/{id}/debloquer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EtudiantResponse> debloquerEtudiant(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.debloquerEtudiant(id));
    }

    // ── Formateurs ─────────────────────────────────────

    @GetMapping("/formateurs/bloques")
    @PreAuthorize("hasRole('ADMIN')")
    public List<FormateurResponse> getFormateursBloques() {
        return adminService.getFormateursBloques();
    }

    @PatchMapping("/formateurs/{id}/bloquer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FormateurResponse> bloquerFormateur(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.bloquerFormateur(id));
    }

    @PatchMapping("/formateurs/{id}/debloquer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FormateurResponse> debloquerFormateur(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.debloquerFormateur(id));
    }
}