package com.elearning.elearning_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeconRequest {
    @NotNull
    private Integer ordre;
    @NotBlank
    private String titre;
    private String description;
    @NotNull
    private Long coursId;
    private String videoUrl;  
    private String pdfUrl;   
}