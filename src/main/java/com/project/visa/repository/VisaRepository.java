package com.project.visa.repository;

import com.project.visa.entity.VisaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VisaRepository extends JpaRepository<VisaEntity, Long> {
    
    List<VisaEntity> findByPasseportId(Long idPasseport);
    
    VisaEntity findByNumeroVisa(String numeroVisa);
}