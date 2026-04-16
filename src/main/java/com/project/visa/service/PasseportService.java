package com.project.visa.service;

import com.project.visa.entity.PasseportEntity;
import com.project.visa.repository.PasseportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PasseportService {
    
    @Autowired
    private PasseportRepository passeportRepository;
    
    public boolean existsByNumeroPasseport(String numeroPasseport){return passeportRepository.existsByNumeroPasseport(numeroPasseport);};
    // Sauvegarder un passeport
    public PasseportEntity save(PasseportEntity passeportEntity) {
        return passeportRepository.save(passeportEntity);
    }
    
    // Trouver tous les passeports
    public List<PasseportEntity> findAll() {
        return passeportRepository.findAll();
    }
    
    // Trouver un passeport par ID
    public Optional<PasseportEntity> findById(Long id) {
        return passeportRepository.findById(id);
    }
    
    // Trouver un passeport par numéro
    public Optional<PasseportEntity> findByNumeroPasseport(String numeroPasseport) {
        return passeportRepository.findByNumeroPasseport(numeroPasseport);
    }
    
  
    // Supprimer un passeport
    public void deleteById(Long id) {
        passeportRepository.deleteById(id);
    }
    
    // Vérifier si un passeport existe
    public boolean existsById(Long id) {
        return passeportRepository.existsById(id);
    }
    
    // Compter le nombre de passeports
    public long count() {
        return passeportRepository.count();
    }
    
}