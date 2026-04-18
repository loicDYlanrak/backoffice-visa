package com.project.visa.repository;

import com.project.visa.entity.DemandeurEntity;
import com.project.visa.entity.NationaliteEntity;
import com.project.visa.entity.SituationFamilialeEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DemandeurRepository extends JpaRepository<DemandeurEntity, Long> {

    // --- Recherches de base ---
    Optional<DemandeurEntity> findByEmail(String email);
    
    List<DemandeurEntity> findByNom(String nom);
    
    List<DemandeurEntity> findByPrenom(String prenom);
    
    List<DemandeurEntity> findByNomAndPrenom(String nom, String prenom);
    
    List<DemandeurEntity> findByLieuNaissance(String lieuNaissance);

    // --- Recherches par IDs de référence ---
    
    
    List<DemandeurEntity> findByNationalite(NationaliteEntity Nationalite);
    
    List<DemandeurEntity> findBySituationFamiliale(SituationFamilialeEntity SituationFamiliale);

    // --- Recherches avancées et floues (String) ---
    List<DemandeurEntity> findByNomContainingIgnoreCase(String nom);

    @Query("SELECT d FROM DemandeurEntity d WHERE " +
           "(:nom IS NULL OR d.nom LIKE %:nom%) AND " +
           "(:prenom IS NULL OR d.prenom LIKE %:prenom%) AND " +
           "(:email IS NULL OR d.email = :email) " )
    List<DemandeurEntity> searchDemandeurs(@Param("nom") String nom, 
                                           @Param("prenom") String prenom, 
                                           @Param("email") String email)
                                         ;

    @Query("SELECT d FROM DemandeurEntity d WHERE d.nom LIKE %:keyword% OR d.prenom LIKE %:keyword%")
    List<DemandeurEntity> searchByNomOrPrenom(@Param("keyword") String keyword);

    // --- Gestion des Dates et Âges ---
    List<DemandeurEntity> findByDateNaissance(LocalDate dateNaissance);
    
    List<DemandeurEntity> findByDateNaissanceBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT d FROM DemandeurEntity d WHERE d.dateNaissance >= :dateMax AND d.dateNaissance <= :dateMin")
    List<DemandeurEntity> findDemandeursByAgeRange(@Param("dateMin") LocalDate dateMin, @Param("dateMax") LocalDate dateMax);

    // --- Utilitaires et Statistiques ---
    void deleteByEmail(String email);
    
    boolean existsByTelephone(String telephone);

    

   

    boolean existsByEmail(String email);

    Optional<DemandeurEntity> findByTelephone(String telephone);
}