package com.project.visa.repository;

import com.project.visa.entity.DemandeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandeRepository extends JpaRepository<DemandeEntity, Long> {
    DemandeEntity findById(int id);
}