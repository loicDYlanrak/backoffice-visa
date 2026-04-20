package com.project.visa.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.visa.entity.StatutDemandeEntity;
import java.util.Optional;

@Repository
public interface  StatutDemandeRepository  extends JpaRepository<StatutDemandeEntity, Long>{
	Optional<StatutDemandeEntity> findTopByDemandeIdOrderByDateChangementStatutDesc(int demandeId);
}
