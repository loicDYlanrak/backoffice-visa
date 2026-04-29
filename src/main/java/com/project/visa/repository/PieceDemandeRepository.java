package com.project.visa.repository;

import com.project.visa.entity.PieceDemandeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PieceDemandeRepository extends JpaRepository<PieceDemandeEntity, Long> {
    PieceDemandeEntity findById(int id);

    List<PieceDemandeEntity> findByDemandeId(int id);

    @Modifying
    @Transactional
    @Query("DELETE FROM PieceDemandeEntity p WHERE p.demande.id = :id")
    void deleteByDemandeId(int id);

    // Dans PieceDemandeRepository.java
    @Query("SELECT pd FROM PieceDemandeEntity pd WHERE pd.demande.id = :idDemande AND pd.piece.id = :idPiece")
    PieceDemandeEntity findByDemandeIdAndPieceId(@Param("idDemande") int idDemande, @Param("idPiece") int idPiece);
}