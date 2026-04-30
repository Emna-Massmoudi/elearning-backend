package com.elearning.elearning_api.repository;

import com.elearning.elearning_api.entity.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    List<Inscription> findByEtudiantId(Long etudiantId);
    List<Inscription> findByCoursId(Long coursId);
    List<Inscription> findByCoursFormateurId(Long formateurId);

    
    Optional<Inscription> findByEtudiantIdAndCoursId(Long etudiantId, Long coursId);

    // Garder l'ancienne si utilisée ailleurs
    boolean existsByEtudiantIdAndCoursId(Long etudiantId, Long coursId);
}
