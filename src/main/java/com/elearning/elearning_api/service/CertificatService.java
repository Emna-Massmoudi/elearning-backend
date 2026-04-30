package com.elearning.elearning_api.service;

import com.elearning.elearning_api.dto.request.CertificatRequest;
import com.elearning.elearning_api.dto.response.CertificatResponse;
import com.elearning.elearning_api.entity.Certificat;
import com.elearning.elearning_api.entity.Cours;
import com.elearning.elearning_api.entity.Etudiant;
import com.elearning.elearning_api.exception.ResourceNotFoundException;
import com.elearning.elearning_api.repository.CertificatRepository;
import com.elearning.elearning_api.repository.CoursRepository;
import com.elearning.elearning_api.repository.EtudiantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificatService {

    private final CertificatRepository certificatRepository;
    private final EtudiantRepository etudiantRepository;
    private final CoursRepository coursRepository;

    public CertificatResponse create(CertificatRequest request) {
        Etudiant etudiant = etudiantRepository.findById(request.getEtudiantId())
                .orElseThrow(() -> new ResourceNotFoundException("Etudiant not found: " + request.getEtudiantId()));

        Cours cours = coursRepository.findById(request.getCoursId())
                .orElseThrow(() -> new ResourceNotFoundException("Cours not found: " + request.getCoursId()));

        Certificat certificat = new Certificat();
        certificat.setCode(UUID.randomUUID().toString());
        certificat.setUrlPdf(request.getUrlPdf());
        certificat.setEtudiant(etudiant);
        certificat.setCours(cours);

        return toResponse(certificatRepository.save(certificat));
    }

    public void delete(Long id) {
        if (!certificatRepository.existsById(id))
            throw new ResourceNotFoundException("Certificat not found: " + id);
        certificatRepository.deleteById(id);
    }

    public CertificatResponse getById(Long id) {
        return certificatRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Certificat not found: " + id));
    }

    public CertificatResponse getByCode(String code) {
        return certificatRepository.findByCode(code)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Certificat not found with code: " + code));
    }

    public List<CertificatResponse> getByEtudiant(Long etudiantId) {
        return certificatRepository.findByEtudiantId(etudiantId)
                .stream().map(this::toResponse).toList();
    }

    private CertificatResponse toResponse(Certificat certificat) {
        CertificatResponse response = new CertificatResponse();
        response.setId(certificat.getId());
        response.setCode(certificat.getCode());
        response.setDateObtention(certificat.getDateObtention());
        response.setUrlPdf(certificat.getUrlPdf());
        response.setEtudiantNom(certificat.getEtudiant().getNom());
        response.setCoursTitre(certificat.getCours().getTitre());
        return response;
    }
}