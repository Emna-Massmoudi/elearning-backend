package com.elearning.elearning_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import lombok.Data;
import java.util.List;

@Data
public class CategorieRequest {

    private String nom;
    private String description;

    // 🔥 AJOUTER ÇA
    private List<String> sousCategories;
}