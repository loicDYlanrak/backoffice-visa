package com.project.visa.repository;

import com.project.visa.entity.DemandeurEntity;
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
    
    List<DemandeurEntity> findByProfession(String profession);
    
    List<DemandeurEntity> findByLieuNaissance(String lieuNaissance);

    // --- Recherches par IDs de référence ---
    List<DemandeurEntity> findByIdNationaliteActuelle(Long idNationalite);
    
    List<DemandeurEntity> findByIdNationaliteOrigine(Long idNationalite);
    
    List<DemandeurEntity> findByIdGenre(Long idGenre);
    
    List<DemandeurEntity> findByIdSituationFamiliale(Long idSituationFamiliale);

    // --- Recherches avancées et floues (String) ---
    List<DemandeurEntity> findByNomContainingIgnoreCase(String nom);

    @Query("SELECT d FROM DemandeurEntity d WHERE " +
           "(:nom IS NULL OR d.nom LIKE %:nom%) AND " +
           "(:prenom IS NULL OR d.prenom LIKE %:prenom%) AND " +
           "(:email IS NULL OR d.email = :email) AND " +
           "(:profession IS NULL OR d.profession LIKE %:profession%)")
    List<DemandeurEntity> searchDemandeurs(@Param("nom") String nom, 
                                           @Param("prenom") String prenom, 
                                           @Param("email") String email, 
                                           @Param("profession") String profession);

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

    List<DemandeurEntity> findTop10ByOrderByCreatedAtDesc();

    @Query("SELECT d.idNationaliteActuelle, COUNT(d) FROM DemandeurEntity d GROUP BY d.idNationaliteActuelle")
    List<Object[]> countDemandeursByNationalite();

    boolean existsByEmail(String email);

    Optional<DemandeurEntity> findByTelephone(String telephone);
}