package com.elearning.elearning_api.service;

import com.elearning.elearning_api.dto.request.CoursRequest;
import com.elearning.elearning_api.dto.response.CoursResponse;
import com.elearning.elearning_api.entity.Cours;
import com.elearning.elearning_api.entity.Formateur;
import com.elearning.elearning_api.entity.SousCategorie;
import com.elearning.elearning_api.enums.EtatCours;
import com.elearning.elearning_api.exception.BadRequestException;
import com.elearning.elearning_api.exception.ResourceNotFoundException;
import com.elearning.elearning_api.repository.CoursRepository;
import com.elearning.elearning_api.repository.FormateurRepository;
import com.elearning.elearning_api.repository.SousCategorieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CoursService {

    private final CoursRepository coursRepository;
    private final FormateurRepository formateurRepository;
    private final SousCategorieRepository sousCategorieRepository;

    public CoursResponse create(CoursRequest request) {
        Formateur formateur = formateurRepository.findById(request.getFormateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Formateur not found"));

        SousCategorie sousCategorie = sousCategorieRepository.findById(request.getSousCategorieId())
                .orElseThrow(() -> new ResourceNotFoundException("SousCategorie not found"));

        validateSousCategorieBelongsToCategorie(sousCategorie, request.getCategorieId());

        Cours cours = new Cours();
        cours.setTitre(request.getTitre());
        cours.setDescription(request.getDescription());
        cours.setFormateur(formateur);
        cours.setSousCategorie(sousCategorie);
        cours.setEtatPublication(EtatCours.BROUILLON);
        cours.setDuree(request.getDuree());
        cours.setNiveau(request.getNiveau());
        cours.setImageUrl(request.getImageUrl());
        cours.setVideoUrl(request.getVideoUrl());
        cours.setPdfUrl(request.getPdfUrl());

        return toResponse(coursRepository.save(cours));
    }

    public CoursResponse update(Long id, CoursRequest request) {
        Cours existing = coursRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours not found: " + id));

        existing.setTitre(request.getTitre());
        existing.setDescription(request.getDescription());
        existing.setDuree(request.getDuree());
        existing.setNiveau(request.getNiveau());
        existing.setImageUrl(request.getImageUrl());
        existing.setVideoUrl(request.getVideoUrl());
        existing.setPdfUrl(request.getPdfUrl());

        if (request.getSousCategorieId() != null) {
            SousCategorie sousCategorie = sousCategorieRepository.findById(request.getSousCategorieId())
                    .orElseThrow(() -> new ResourceNotFoundException("SousCategorie not found"));

            validateSousCategorieBelongsToCategorie(sousCategorie, request.getCategorieId());
            existing.setSousCategorie(sousCategorie);
        }

        return toResponse(coursRepository.save(existing));
    }

    public void delete(Long id) {
        Cours existing = coursRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours not found: " + id));

        existing.setEtatPublication(EtatCours.SUPPRIME);
        coursRepository.save(existing);
    }

    @Transactional(readOnly = true)
    public CoursResponse getById(Long id) {
        return coursRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Cours not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<CoursResponse> getAll() {
        return coursRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CoursResponse> getByFormateur(Long formateurId) {
        return coursRepository.findByFormateurId(formateurId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CoursResponse> getByEtat(EtatCours etat) {
        return coursRepository.findByEtatPublication(etat)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CoursResponse updateEtat(Long id, EtatCours etat) {
        Cours existing = coursRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours not found: " + id));

        existing.setEtatPublication(etat);

        if (etat == EtatCours.PUBLIE) {
            existing.setDatePublication(LocalDateTime.now());
        }

        return toResponse(coursRepository.save(existing));
    }

    private CoursResponse toResponse(Cours cours) {
        CoursResponse response = new CoursResponse();

        response.setId(cours.getId());
        response.setTitre(cours.getTitre());
        response.setDescription(cours.getDescription());
        response.setEtatPublication(cours.getEtatPublication());
        response.setDateCreation(cours.getDateCreation());
        response.setDatePublication(cours.getDatePublication());

        if (cours.getFormateur() != null) {
            response.setFormateurId(cours.getFormateur().getId());
            response.setFormateurNom(cours.getFormateur().getNom());
            response.setFormateurEmail(cours.getFormateur().getEmail());
        }

        if (cours.getSousCategorie() != null) {
            response.setSousCategorieId(cours.getSousCategorie().getId());
            response.setSousCategorieNom(cours.getSousCategorie().getNom());

            if (cours.getSousCategorie().getCategorie() != null) {
                response.setCategorieId(cours.getSousCategorie().getCategorie().getId());
                response.setCategorieNom(cours.getSousCategorie().getCategorie().getNom());
            }
        }

        response.setDuree(cours.getDuree());
        response.setNiveau(cours.getNiveau());
        response.setImageUrl(cours.getImageUrl());
        response.setVideoUrl(cours.getVideoUrl());
        response.setPdfUrl(cours.getPdfUrl());

        return response;
    }

    private void validateSousCategorieBelongsToCategorie(SousCategorie sousCategorie, Long categorieId) {
        if (sousCategorie.getCategorie() == null
                || !sousCategorie.getCategorie().getId().equals(categorieId)) {
            throw new BadRequestException("SousCategorie does not belong to the selected categorie");
        }
    }
}