package com.elearning.elearning_api.dto.request;

import com.elearning.elearning_api.enums.EtatCours;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CoursRequest {
    @NotBlank
    private String titre;
    private String description;
    @NotNull
    private Long formateurId;
    @NotNull
    private Long categorieId;
    @NotNull
    private Long sousCategorieId;
    private EtatCours etatPublication;
    private String duree;
    private String niveau;
    private String imageUrl;
    private String videoUrl;
    private String pdfUrl; 
}
