package com.project.visa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.visa.entity.SituationFamilialeEntity;

@Repository
public interface SituationFamilialeRepository extends JpaRepository<SituationFamilialeEntity, Integer> {
}
