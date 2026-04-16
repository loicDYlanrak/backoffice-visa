package com.project.visa.repository;

import com.project.visa.entity.DemandeurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
@Repository
public interface DemandeurRepository extends JpaRepository<DemandeurEntity, Long> {


    Optional<DemandeurEntity> findByEmail(String email);

  
    Optional<DemandeurEntity> findByTelephone(String telephone);

    @Query("SELECT d.idNationaliteActuelle, COUNT(d) FROM DemandeurEntity d GROUP BY d.idNationaliteActuelle")
    List<Object[]> countDemandeursByNationalite();

    boolean existsByEmail(String email);
}