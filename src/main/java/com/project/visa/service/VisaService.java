package com.project.visa.service;

<<<<<<< Updated upstream
import com.project.visa.entity.*;
import com.project.visa.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.visa.entity.VisaEntity;
import com.project.visa.repository.VisaRepository;
>>>>>>> Stashed changes

@Service
public class VisaService {

<<<<<<< Updated upstream
    private final VisaRepository visaRepository;
    private final PasseportRepository passeportRepository;
    private final TypeVisaRepository typeVisaRepository;

    public VisaService(VisaRepository visaRepository, 
                       PasseportRepository passeportRepository, 
                       TypeVisaRepository typeVisaRepository) {
        this.visaRepository = visaRepository;
        this.passeportRepository = passeportRepository;
        this.typeVisaRepository = typeVisaRepository;
    }

    @Transactional
    public VisaEntity enregistrerVisa(String numeroVisa, String dateDebut, String dateFin, 
                                     Long idTypeVisa, Long idPasseport, Boolean transformable) {
        
        // 1. Récupérer les entités liées (Gestion d'erreur si non trouvées)
        PasseportEntity passeport = passeportRepository.findById(idPasseport)
                .orElseThrow(() -> new RuntimeException("Passeport introuvable avec l'ID: " + idPasseport));
        
        TypeVisaEntity typeVisa = typeVisaRepository.findById(idTypeVisa)
                .orElseThrow(() -> new RuntimeException("Type de Visa introuvable avec l'ID: " + idTypeVisa));

        // 2. Créer et configurer l'entité Visa
        VisaEntity visa = new VisaEntity();
        visa.setNumeroVisa(numeroVisa);
        visa.setDateDebut(java.time.LocalDate.parse(dateDebut)); // Conversion String vers LocalDate
        visa.setDateFin(java.time.LocalDate.parse(dateFin));
        visa.setTransformable(transformable);
        
        // C'est ici que la magie JPA opère : on passe l'objet complet
        visa.setPasseport(passeport);
        visa.setTypeVisa(typeVisa);

        return visaRepository.save(visa);
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
=======
    @Autowired
    private VisaRepository visaRepository;

    public VisaEntity save(VisaEntity visaEntity) {
        return visaRepository.save(visaEntity);
    }
}
>>>>>>> Stashed changes
