package com.elearning.elearning_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    private String description;

    @OneToMany(
            mappedBy = "categorie",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SousCategorie> sousCategories = new ArrayList<>();

    // ✅ Méthode propre pour gérer la relation
    public void addSousCategorie(SousCategorie sc) {
        sousCategories.add(sc);
        sc.setCategorie(this);
    }

    public void removeSousCategorie(SousCategorie sc) {
        sousCategories.remove(sc);
        sc.setCategorie(null);
    }
}