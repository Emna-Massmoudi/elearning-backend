package com.elearning.elearning_api.service;

import com.elearning.elearning_api.dto.response.EtudiantResponse;
import com.elearning.elearning_api.dto.response.FormateurResponse;
import com.elearning.elearning_api.entity.Etudiant;
import com.elearning.elearning_api.entity.Formateur;
import com.elearning.elearning_api.repository.EtudiantRepository;
import com.elearning.elearning_api.repository.FormateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final EtudiantRepository  etudiantRepository;
    private final FormateurRepository formateurRepository;

    // ── Étudiants ──────────────────────────────────────

    public List<EtudiantResponse> getAllEtudiants() {
        return etudiantRepository.findAll()
                .stream()
                .map(e -> new EtudiantResponse(
                        e.getId(),
                        e.getNom(),
                        e.getEmail(),
                        e.getStatus()
                ))
                .toList();
    }

    public List<EtudiantResponse> getEtudiantsBloques() {
        return etudiantRepository.findByStatus("BLOQUE")
                .stream()
                .map(e -> new EtudiantResponse(
                        e.getId(),
                        e.getNom(),
                        e.getEmail(),
                        e.getStatus()
                ))
                .toList();
    }

    public EtudiantResponse bloquerEtudiant(Long id) {
        Etudiant e = etudiantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Étudiant introuvable : " + id));
        e.setStatus("BLOQUE");
        etudiantRepository.save(e);
        return new EtudiantResponse(e.getId(), e.getNom(), e.getEmail(), e.getStatus());
    }

    public EtudiantResponse debloquerEtudiant(Long id) {
        Etudiant e = etudiantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Étudiant introuvable : " + id));
        e.setStatus("ACTIF");
        etudiantRepository.save(e);
        return new EtudiantResponse(e.getId(), e.getNom(), e.getEmail(), e.getStatus());
    }

    // ── Formateurs ─────────────────────────────────────

    public List<FormateurResponse> getFormateursBloques() {
        return formateurRepository.findByStatus("BLOQUE")
                .stream()
                .map(f -> new FormateurResponse(
                        f.getId(),
                        f.getNom(),
                        f.getEmail(),
                        f.getStatus(),
                        f.getPortfolio(),
                        f.getSpecialite(),
                        f.getBio(),
                        f.getCvUrl(),
                        f.getDiplomeUrl(),
                        f.getCertificatUrl(),
                        f.getAttestationUrl(),
                        f.getMotivation(),
                        null
                ))
                .toList();
    }

    public FormateurResponse bloquerFormateur(Long id) {
        Formateur f = formateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formateur introuvable : " + id));
        f.setStatus("BLOQUE");
        formateurRepository.save(f);
        return new FormateurResponse(
                f.getId(), f.getNom(), f.getEmail(), f.getStatus(),
                f.getPortfolio(), f.getSpecialite(), f.getBio(),
                f.getCvUrl(), f.getDiplomeUrl(), f.getCertificatUrl(),
                f.getAttestationUrl(), f.getMotivation(), null
        );
    }

    public FormateurResponse debloquerFormateur(Long id) {
        Formateur f = formateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formateur introuvable : " + id));
        f.setStatus("ACTIF");
        formateurRepository.save(f);
        return new FormateurResponse(
                f.getId(), f.getNom(), f.getEmail(), f.getStatus(),
                f.getPortfolio(), f.getSpecialite(), f.getBio(),
                f.getCvUrl(), f.getDiplomeUrl(), f.getCertificatUrl(),
                f.getAttestationUrl(), f.getMotivation(), null
        );
    }
}