package com.project.visa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.visa.entity.NationaliteEntity;

@Repository
public interface NationaliteRepository extends JpaRepository<NationaliteEntity, Integer> {
}
