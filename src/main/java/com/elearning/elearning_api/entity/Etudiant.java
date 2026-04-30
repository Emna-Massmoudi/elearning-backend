package com.elearning.elearning_api.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("ETUDIANT")  
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Etudiant extends Utilisateur {
}