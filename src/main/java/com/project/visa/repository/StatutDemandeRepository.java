package com.project.visa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.visa.entity.DemandeEntity;
import com.project.visa.entity.StatutDemandeEntity;

import java.util.List;

@Repository
public interface StatutDemandeRepository extends JpaRepository<StatutDemandeEntity, Long> {
	Optional<StatutDemandeEntity> findFirstByDemandeIdOrderByIdDesc(int demandeId);
	boolean existsByDemandeAndStatut(DemandeEntity demande, Integer statutCode);

	@Query("SELECT s FROM StatutDemandeEntity s WHERE s.demande.id = :demandeId ORDER BY s.dateChangementStatut DESC")
	List<StatutDemandeEntity> findByDemandeIdOrderByDateChangementStatutDesc(@Param("demandeId") int demandeId);

	default Optional<StatutDemandeEntity> findLatestByDemandeId(int demandeId) {
		List<StatutDemandeEntity> statuts = findByDemandeIdOrderByDateChangementStatutDesc(demandeId);
		return statuts.isEmpty() ? Optional.empty() : Optional.of(statuts.get(0));
	}

}
