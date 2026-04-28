package com.project.visa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.visa.entity.PasseportEntity;
import com.project.visa.entity.VisaEntity;

@Repository
public interface VisaRepository extends JpaRepository<VisaEntity, Long> {
    
    List<VisaEntity> findByPasseport(PasseportEntity Passeport);
    
   @Query("SELECT v FROM VisaEntity v " +
       "JOIN FETCH v.demande d " +
       "JOIN FETCH d.demandeur " +
       "WHERE v.reference = :ref")
    VisaEntity findByReference(@Param("ref") String reference);
}
