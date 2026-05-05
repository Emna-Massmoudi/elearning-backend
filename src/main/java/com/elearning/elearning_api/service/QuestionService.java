package com.elearning.elearning_api.service;

import com.elearning.elearning_api.dto.request.QuestionRequest;
import com.elearning.elearning_api.dto.response.ChoixResponse;
import com.elearning.elearning_api.dto.response.QuestionResponse;
import com.elearning.elearning_api.entity.Choix;
import com.elearning.elearning_api.entity.Evaluation;
import com.elearning.elearning_api.entity.Question;
import com.elearning.elearning_api.exception.ResourceNotFoundException;
import com.elearning.elearning_api.repository.EvaluationRepository;
import com.elearning.elearning_api.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final EvaluationRepository evaluationRepository;

    public QuestionResponse create(QuestionRequest request) {
        Evaluation evaluation = evaluationRepository.findById(request.getEvaluationId())
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found: " + request.getEvaluationId()));

        Question question = new Question();
        question.setEnonce(request.getEnonce());
        question.setPoint(request.getPoint());
        question.setEvaluation(evaluation);

        return toResponse(questionRepository.save(question));
    }

    public QuestionResponse update(Long id, QuestionRequest request) {
        Question existing = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found: " + id));

        existing.setEnonce(request.getEnonce());
        existing.setPoint(request.getPoint());

        return toResponse(questionRepository.save(existing));
    }

    public void delete(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question not found: " + id);
        }

        questionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public QuestionResponse getById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found: " + id));

        return toResponse(question);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getByEvaluation(Long evaluationId) {
        return questionRepository.findByEvaluationId(evaluationId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private QuestionResponse toResponse(Question question) {
        QuestionResponse response = new QuestionResponse();

        response.setId(question.getId());
        response.setEnonce(question.getEnonce());
        response.setPoint(question.getPoint());

        if (question.getEvaluation() != null) {
            response.setEvaluationId(question.getEvaluation().getId());
            response.setEvaluationTitre(question.getEvaluation().getTitre());
        }

        List<ChoixResponse> choixResponses = new ArrayList<>();

        if (question.getChoix() != null) {
            for (Choix c : question.getChoix()) {
                ChoixResponse cr = new ChoixResponse();
                cr.setId(c.getId());
                cr.setTexte(c.getTexte());
                cr.setEstCorrect(c.getEstCorrect());

                if (c.getQuestion() != null) {
                    cr.setQuestionId(c.getQuestion().getId());
                }

                choixResponses.add(cr);
            }
        }

        response.setChoix(choixResponses);

        return response;
    }
}