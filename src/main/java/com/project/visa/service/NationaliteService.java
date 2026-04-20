package com.project.visa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.visa.entity.NationaliteEntity;
import com.project.visa.repository.NationaliteRepository;

@Service
public class NationaliteService {

    private final NationaliteRepository nationaliteRepository;

    public NationaliteService(NationaliteRepository nationaliteRepository) {
        this.nationaliteRepository = nationaliteRepository;
    }

    public List<NationaliteEntity> findAll() {
        return nationaliteRepository.findAll();
    }
}
