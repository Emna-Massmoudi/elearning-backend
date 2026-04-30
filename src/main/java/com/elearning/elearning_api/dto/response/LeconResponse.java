package com.elearning.elearning_api.dto.response;

import lombok.Data;

@Data
public class LeconResponse {
    private Long    id;
    private Integer ordre;
    private String  titre;
    private String  description;
    private Long    coursId;
    private String  coursTitre;
    private String  videoUrl;  
    private String  pdfUrl;   
}