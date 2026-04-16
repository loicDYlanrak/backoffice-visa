package com.project.visa.service;

<<<<<<< Updated upstream
import com.project.visa.entity.TypeVisaEntity;
import com.project.visa.repository.TypeVisaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.visa.entity.TypeVisaEntity;
import com.project.visa.repository.TypeVisaRepository;
>>>>>>> Stashed changes

@Service
public class TypeVisaService {

<<<<<<< Updated upstream
    private final TypeVisaRepository typeVisaRepository;

    public TypeVisaService(TypeVisaRepository typeVisaRepository) {
        this.typeVisaRepository = typeVisaRepository;
    }

    public List<TypeVisaEntity> listerTous() {
        return typeVisaRepository.findAll();
    }

    
    public TypeVisaEntity creerTypeSiInexistant(String libelle) {
        return typeVisaRepository.findByLibelle(libelle)
                .orElseGet(() -> typeVisaRepository.save(new TypeVisaEntity(libelle)));
    }
}
=======
    @Autowired
    private TypeVisaRepository typeVisaRepository;

    public TypeVisaEntity findById(Long id) {
        return typeVisaRepository.findById(id).orElse(null);
    }
}
>>>>>>> Stashed changes
