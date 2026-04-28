package com.project.visa.repository;

import com.project.visa.entity.CarteResidentEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;  

@Repository
public interface CarteResidentRepository extends JpaRepository<CarteResidentEntity, Long> {
    CarteResidentEntity findById(int id);

    @Query("SELECT c FROM CarteResidentEntity c " +
            "JOIN FETCH c.demande d " +
            "JOIN FETCH d.demandeur " +
            "WHERE c.reference = :ref")
    CarteResidentEntity findByReference(@Param("ref") String reference);

    List<CarteResidentEntity> findByDemandeId(int demandeId);
}