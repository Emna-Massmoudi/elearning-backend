package com.elearning.elearning_api.service;

import com.elearning.elearning_api.dto.request.LeconRequest;
import com.elearning.elearning_api.dto.response.LeconResponse;
import com.elearning.elearning_api.entity.Cours;
import com.elearning.elearning_api.entity.Lecon;
import com.elearning.elearning_api.exception.ResourceNotFoundException;
import com.elearning.elearning_api.repository.CoursRepository;
import com.elearning.elearning_api.repository.LeconRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeconService {

    private final LeconRepository leconRepository;
    private final CoursRepository coursRepository;

    public LeconResponse create(LeconRequest request) {
        Cours cours = coursRepository.findById(request.getCoursId())
                .orElseThrow(() -> new ResourceNotFoundException("Cours not found: " + request.getCoursId()));
        Lecon lecon = new Lecon();
        lecon.setTitre(request.getTitre());
        lecon.setDescription(request.getDescription());
        lecon.setOrdre(request.getOrdre());
        lecon.setVideoUrl(request.getVideoUrl());   
        lecon.setPdfUrl(request.getPdfUrl());      
        lecon.setCours(cours);
        return toResponse(leconRepository.save(lecon));
    }

    public LeconResponse update(Long id, LeconRequest request) {
        Lecon existing = leconRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lecon not found: " + id));
        existing.setTitre(request.getTitre());
        existing.setDescription(request.getDescription());
        existing.setOrdre(request.getOrdre());
        existing.setVideoUrl(request.getVideoUrl());   
        existing.setPdfUrl(request.getPdfUrl());      
        return toResponse(leconRepository.save(existing));
    }

    public void delete(Long id) {
        if (!leconRepository.existsById(id))
            throw new ResourceNotFoundException("Lecon not found: " + id);
        leconRepository.deleteById(id);
    }

    public LeconResponse getById(Long id) {
        return leconRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Lecon not found: " + id));
    }

    public List<LeconResponse> getByCours(Long coursId) {
        return leconRepository.findByCoursIdOrderByOrdreAsc(coursId)
                .stream().map(this::toResponse).toList();
    }

    private LeconResponse toResponse(Lecon lecon) {
        LeconResponse response = new LeconResponse();
        response.setId(lecon.getId());
        response.setTitre(lecon.getTitre());
        response.setDescription(lecon.getDescription());
        response.setOrdre(lecon.getOrdre());
        response.setCoursId(lecon.getCours().getId());
        response.setCoursTitre(lecon.getCours().getTitre());
        response.setVideoUrl(lecon.getVideoUrl());   // ← AJOUTÉ
        response.setPdfUrl(lecon.getPdfUrl());       // ← AJOUTÉ
        return response;
    }
}