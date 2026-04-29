package com.project.visa.repository;

import com.project.visa.entity.PieceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PieceRepository extends JpaRepository<PieceEntity, Long> {
    PieceEntity findById(int id);

    List<PieceEntity> findByTypeVisaEntity_Id(int typeVisaId);
    List<PieceEntity> findByTypeVisaEntityIsNull();
}