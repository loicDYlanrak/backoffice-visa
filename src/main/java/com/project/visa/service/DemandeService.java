package com.project.visa.service;

import com.project.visa.entity.DemandeEntity;
import com.project.visa.entity.DemandeurEntity;
import com.project.visa.entity.PasseportEntity;
import com.project.visa.entity.VisaTransformableEntity;
import com.project.visa.repository.DemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DemandeService {
    
    @Autowired
    private DemandeRepository demandeRepository;
    
    public List<DemandeEntity> findAll() {
        return demandeRepository.findAll();
    }
    
    public DemandeEntity findById(int id) {
        return demandeRepository.findById(id);
    }
    
    public DemandeEntity save(DemandeEntity demandeEntity) {
        return demandeRepository.save(demandeEntity);
    }
    
    public void deleteById(Long id) {
        demandeRepository.deleteById(id);
    }
    public boolean validate(DemandeEntity demandeEntity, 
                        DemandeurEntity demandeurEntity, 
                        PasseportEntity passeportEntity,
                        VisaTransformableEntity visaTransformableEntity,LocalDate currentDate ) {
   
    
    return demandeurEntity != null && demandeurEntity.isValid(currentDate)
        && passeportEntity != null && passeportEntity.isValid(currentDate)
        && visaTransformableEntity != null && visaTransformableEntity.demandeValide(currentDate)
        && demandeEntity != null && demandeEntity.isValide();
    }   
   
    
   
   
    
}