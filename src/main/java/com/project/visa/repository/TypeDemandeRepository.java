package com.project.visa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.visa.entity.TypeDemandeEntity;

@Repository
public interface TypeDemandeRepository extends JpaRepository<TypeDemandeEntity, Integer> {
    TypeDemandeEntity findByLibelle(String libelle);
}
