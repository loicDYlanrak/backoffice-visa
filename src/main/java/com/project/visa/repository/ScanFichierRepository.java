package com.project.visa.repository;

import com.project.visa.entity.ScanFichierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScanFichierRepository extends JpaRepository<ScanFichierEntity, Long> {

    // Trouver tous les scans par ID de pièce demande
    List<ScanFichierEntity> findByPieceDemande_Id(int idPieceDemande);

    // Trouver tous les scans par ID de demande via les pièces demande
    @Query("SELECT s FROM ScanFichierEntity s WHERE s.pieceDemande.demande.id = :idDemande")
    List<ScanFichierEntity> findByIdDemande(@Param("idDemande") int idDemande);

    // Vérifier si un fichier a déjà été uploadé pour une pièce demande spécifique
    Optional<ScanFichierEntity> findByPieceDemandeId(int idPieceDemande);

    // Vérifier si un fichier existe pour une demande et un document requis via pieceDemande
    @Query("SELECT COUNT(s) > 0 FROM ScanFichierEntity s WHERE s.pieceDemande.demande.id = :idDemande AND s.pieceDemande.piece.id = :idPiece")
    boolean existsByIdDemandeAndIdPiece(@Param("idDemande") int idDemande, @Param("idPiece") int idPiece);

    // Supprimer tous les scans d'une demande
    @Modifying
    @Transactional
    @Query("DELETE FROM ScanFichierEntity s WHERE s.pieceDemande.demande.id = :idDemande")
    void deleteByIdDemande(@Param("idDemande") int idDemande);
}