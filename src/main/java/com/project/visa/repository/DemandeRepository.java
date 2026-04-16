package com.project.visa.repository;

import com.project.visa.entity.DemandeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DemandeRepository extends JpaRepository<DemandeEntity, Long> {
    List<DemandeEntity> findByIdStatus(Integer status);
    List<DemandeEntity> findByReferenceContainingIgnoreCase(String name);
}