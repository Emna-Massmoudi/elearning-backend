package com.elearning.elearning_api.dto.response;

import com.elearning.elearning_api.enums.TypeEvaluation;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EvaluationResponse {
    private Long id;
    private String titre;
    private TypeEvaluation type;
    private Integer noteMax;
    private Integer noteMin;
    private LocalDateTime dateCreation;
    private Long leconId;
    private String leconTitre;
}