package com.project.visa.repository;

<<<<<<< Updated upstream
import com.project.visa.entity.PasseportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasseportRepository extends JpaRepository<PasseportEntity, Long> {
    
    // Recherche par numéro de passeport
    Optional<PasseportEntity> findByNumeroPasseport(String numeroPasseport);
    
  boolean existsByNumeroPasseport(String numeroPasseport);
}
=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.visa.entity.PasseportEntity;

@Repository
public interface PasseportRepository extends JpaRepository<PasseportEntity, Long> {
}
>>>>>>> Stashed changes
