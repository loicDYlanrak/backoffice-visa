package com.project.visa.repository;

import com.project.visa.entity.DemandeEntity;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandeRepository extends JpaRepository<DemandeEntity, Long> {
    DemandeEntity findById(int id);
    List<DemandeEntity>findByDemandeurId(int id);
    @Query("SELECT d FROM DemandeEntity d " +
       "JOIN d.visaTransformable v " +
       "JOIN v.passeport p " +
       "WHERE p.numeroPasseport = :num " +
       "ORDER BY d.dateDemande DESC")
    List<DemandeEntity> rechercherParNumeroPasseport(@Param("num") String num);

}
