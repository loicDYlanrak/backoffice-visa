package com.project.visa.repository;

import com.project.visa.entity.PhotoSignatureDemandeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoSignatureDemandeRepository extends JpaRepository<PhotoSignatureDemandeEntity, Long> {

    // Trouver par ID
    Optional<PhotoSignatureDemandeEntity> findById(int id);

    // Trouver toutes les photos/signatures d'une demande spécifique
    List<PhotoSignatureDemandeEntity> findByDemandeId(int demandeId);

    // Trouver la photo/signature la plus récente d'une demande (si une demande peut
    // avoir plusieurs entrées)
    @Query("SELECT p FROM PhotoSignatureDemandeEntity p WHERE p.demande.id = :demandeId ORDER BY p.dateUpload DESC")
    List<PhotoSignatureDemandeEntity> findLatestByDemandeId(@Param("demandeId") int demandeId);

    // Supprimer toutes les entrées d'une demande
    @Modifying
    @Transactional
    @Query("DELETE FROM PhotoSignatureDemandeEntity p WHERE p.demande.id = :demandeId")
    void deleteByDemandeId(@Param("demandeId") int demandeId);

    // Vérifier si une demande a déjà une photo/signature
    boolean existsByDemandeId(int demandeId);
}