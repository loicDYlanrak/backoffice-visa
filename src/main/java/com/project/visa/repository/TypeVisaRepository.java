package com.project.visa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.visa.entity.TypeVisaEntity;

@Repository
public interface TypeVisaRepository extends JpaRepository<TypeVisaEntity, Long> {

    Optional<TypeVisaEntity> findByLibelle(String libelle);

    boolean existsByLibelle(String libelle);
}
