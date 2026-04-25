package com.project.visa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.visa.entity.PasseportEntity;
import com.project.visa.entity.VisaEntity;

@Repository
public interface VisaRepository extends JpaRepository<VisaEntity, Long> {
    
    List<VisaEntity> findByPasseport(PasseportEntity Passeport);
    
    VisaEntity findByReference(String numeroVisa);
}
