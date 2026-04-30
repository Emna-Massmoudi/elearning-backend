package com.elearning.elearning_api.service;

import com.elearning.elearning_api.dto.request.EvaluationRequest;
import com.elearning.elearning_api.dto.response.EvaluationResponse;
import com.elearning.elearning_api.entity.Evaluation;
import com.elearning.elearning_api.entity.Lecon;
import com.elearning.elearning_api.exception.ResourceNotFoundException;
import com.elearning.elearning_api.repository.EvaluationRepository;
import com.elearning.elearning_api.repository.LeconRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final LeconRepository leconRepository;

    public EvaluationResponse create(EvaluationRequest request) {
        Lecon lecon = leconRepository.findById(request.getLeconId())
                .orElseThrow(() -> new ResourceNotFoundException("Lecon not found: " + request.getLeconId()));

        Evaluation evaluation = new Evaluation();
        evaluation.setTitre(request.getTitre());
        evaluation.setType(request.getType());
        evaluation.setNoteMax(request.getNoteMax());
        evaluation.setNoteMin(request.getNoteMin());
        evaluation.setLecon(lecon);

        return toResponse(evaluationRepository.save(evaluation));
    }

    public EvaluationResponse update(Long id, EvaluationRequest request) {
        Evaluation existing = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found: " + id));

        existing.setTitre(request.getTitre());
        existing.setType(request.getType());
        existing.setNoteMax(request.getNoteMax());
        existing.setNoteMin(request.getNoteMin());

        return toResponse(evaluationRepository.save(existing));
    }

    public void delete(Long id) {
        if (!evaluationRepository.existsById(id))
            throw new ResourceNotFoundException("Evaluation not found: " + id);
        evaluationRepository.deleteById(id);
    }

    public EvaluationResponse getById(Long id) {
        return evaluationRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found: " + id));
    }

    public List<EvaluationResponse> getByLecon(Long leconId) {
        return evaluationRepository.findByLeconId(leconId)
                .stream().map(this::toResponse).toList();
    }

    private EvaluationResponse toResponse(Evaluation evaluation) {
        EvaluationResponse response = new EvaluationResponse();
        response.setId(evaluation.getId());
        response.setTitre(evaluation.getTitre());
        response.setType(evaluation.getType());
        response.setNoteMax(evaluation.getNoteMax());
        response.setNoteMin(evaluation.getNoteMin());
        response.setDateCreation(evaluation.getDateCreation());
        response.setLeconId(evaluation.getLecon().getId());  
        response.setLeconTitre(evaluation.getLecon().getTitre());
        return response;
    }
}