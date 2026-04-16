package com.project.visa.repository;

import com.project.visa.entity.TypeVisaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TypeVisaRepository extends JpaRepository<TypeVisaEntity, Long> {

    Optional<TypeVisaEntity> findByLibelle(String libelle);

    boolean existsByLibelle(String libelle);
}