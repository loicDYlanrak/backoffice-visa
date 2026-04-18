package com.project.visa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.project.visa.entity.VisaEntity;
import com.project.visa.repository.PasseportRepository;
import com.project.visa.repository.TypeVisaRepository;
import com.project.visa.repository.VisaRepository;

import jakarta.transaction.Transactional;

@Service
public class VisaService {

    private final VisaRepository visaRepository;

    public VisaService(VisaRepository visaRepository, 
                       PasseportRepository passeportRepository, 
                       TypeVisaRepository typeVisaRepository) {
        this.visaRepository = visaRepository;
    }

    public List<VisaEntity> listerVisasParPasseport(Long idPasseport) {
        return visaRepository.findByPasseportId(idPasseport);
    }

    @Transactional
    public void supprimerVisa(Long id) {
        visaRepository.deleteById(id);
    }

    @Transactional
    public VisaEntity save(VisaEntity visa) {
        return visaRepository.save(visa);
    }
}
