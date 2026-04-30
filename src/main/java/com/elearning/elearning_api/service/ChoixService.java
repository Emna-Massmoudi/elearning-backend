package com.elearning.elearning_api.service;

import com.elearning.elearning_api.dto.request.ChoixRequest;
import com.elearning.elearning_api.dto.response.ChoixResponse;
import com.elearning.elearning_api.entity.Choix;
import com.elearning.elearning_api.entity.Question;
import com.elearning.elearning_api.exception.ResourceNotFoundException;
import com.elearning.elearning_api.repository.ChoixRepository;
import com.elearning.elearning_api.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChoixService {

    private final ChoixRepository choixRepository;
    private final QuestionRepository questionRepository;

    public ChoixResponse create(ChoixRequest request) {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found: " + request.getQuestionId()));

        Choix choix = new Choix();
        choix.setTexte(request.getTexte());
        choix.setEstCorrect(request.getEstCorrect());
        choix.setQuestion(question);

        return toResponse(choixRepository.save(choix));
    }

    public ChoixResponse update(Long id, ChoixRequest request) {
        Choix existing = choixRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Choix not found: " + id));

        existing.setTexte(request.getTexte());
        existing.setEstCorrect(request.getEstCorrect());

        return toResponse(choixRepository.save(existing));
    }

    public void delete(Long id) {
        if (!choixRepository.existsById(id))
            throw new ResourceNotFoundException("Choix not found: " + id);
        choixRepository.deleteById(id);
    }

    public List<ChoixResponse> getByQuestion(Long questionId) {
        return choixRepository.findByQuestionId(questionId)
                .stream().map(this::toResponse).toList();
    }

    private ChoixResponse toResponse(Choix choix) {
        ChoixResponse response = new ChoixResponse();
        response.setId(choix.getId());
        response.setTexte(choix.getTexte());
        response.setEstCorrect(choix.getEstCorrect());
        return response;
    }
}