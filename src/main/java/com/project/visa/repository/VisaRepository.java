package com.project.visa.repository;

<<<<<<< Updated upstream
import com.project.visa.entity.VisaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VisaRepository extends JpaRepository<VisaEntity, Long> {
    
    List<VisaEntity> findByPasseportId(Long idPasseport);
    
    VisaEntity findByNumeroVisa(String numeroVisa);
}
=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.visa.entity.VisaEntity;

@Repository
public interface VisaRepository extends JpaRepository<VisaEntity, Long> {
}
>>>>>>> Stashed changes
