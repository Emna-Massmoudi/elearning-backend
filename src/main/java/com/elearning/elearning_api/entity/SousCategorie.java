package com.elearning.elearning_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sous_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SousCategorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    @OneToMany(mappedBy = "sousCategorie", cascade = CascadeType.ALL)
    private List<Cours> cours = new ArrayList<>();
}