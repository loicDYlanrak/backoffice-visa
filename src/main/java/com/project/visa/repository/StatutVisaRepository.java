package com.project.visa.repository;

import com.project.visa.entity.StatutVisaEntity;
import com.project.visa.entity.VisaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatutVisaRepository extends JpaRepository<StatutVisaEntity, Integer> {
    
    List<StatutVisaEntity> findByVisa(VisaEntity visa);
    
    Optional<StatutVisaEntity> findTopByVisaOrderByDateChangementStatutDesc(VisaEntity visa);
    
    List<StatutVisaEntity> findByVisaAndStatut(VisaEntity visa, int statut);
}