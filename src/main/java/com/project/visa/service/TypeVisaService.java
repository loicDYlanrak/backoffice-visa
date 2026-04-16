package com.project.visa.service;

import com.project.visa.entity.TypeVisaEntity;
import com.project.visa.repository.TypeVisaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TypeVisaService {

    private final TypeVisaRepository typeVisaRepository;

    public TypeVisaService(TypeVisaRepository typeVisaRepository) {
        this.typeVisaRepository = typeVisaRepository;
    }

    public List<TypeVisaEntity> listerTous() {
    }

    
    public TypeVisaEntity creerTypeSiInexistant(String libelle) {
        return typeVisaRepository.findByLibelle(libelle)
                .orElseGet(() -> typeVisaRepository.save(new TypeVisaEntity(libelle)));
    }

    @Autowired
    private TypeVisaRepository typeVisaRepository;

    public TypeVisaEntity findById(Long id) {
        return typeVisaRepository.findById(id).orElse(null);
    }
}
