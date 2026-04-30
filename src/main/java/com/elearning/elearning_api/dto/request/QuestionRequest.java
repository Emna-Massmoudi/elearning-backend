package com.elearning.elearning_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionRequest {

    @NotBlank
    private String enonce;

    @NotNull
    private Integer point;

    @NotNull
    private Long evaluationId;
}