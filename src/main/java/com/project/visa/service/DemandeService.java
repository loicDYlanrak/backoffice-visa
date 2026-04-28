package com.project.visa.service;

import com.project.visa.entity.DemandeEntity;
import com.project.visa.entity.DemandeurEntity;
import com.project.visa.entity.PasseportEntity;
import com.project.visa.entity.StatutDemandeEntity;
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
    @Autowired
    private StatutDemandeService statutDemandeService;

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
            VisaTransformableEntity visaTransformableEntity, LocalDate currentDate) {

        return demandeurEntity != null && demandeurEntity.isValid(currentDate)
                && passeportEntity != null && passeportEntity.isValid(currentDate)
                && visaTransformableEntity != null && visaTransformableEntity.demandeValide(currentDate)
                && demandeEntity != null && demandeEntity.isValide();
    }

    public boolean peutEtreValidee(DemandeEntity demande) {
        // Vérifier si la demande a le statut "Scanné" (20)
        StatutDemandeEntity dernierStatut = statutDemandeService.findLatestByDemandeId(demande.getId()).orElse(null);
        if (dernierStatut == null || dernierStatut.getStatut() != StatutDemandeEntity.STATUT_SCANNE) {
            return false;
        }
        // Vérifier que tous les documents sont uploadés
        // (à implémenter avec ScanFichierService)
        return true;
    }

    public boolean peutEtreRejetee(DemandeEntity demande) {
        // Vérifier que la demande n'est pas déjà validée ou rejetée
        StatutDemandeEntity dernierStatut = statutDemandeService.findLatestByDemandeId(demande.getId()).orElse(null);
        if (dernierStatut == null) {
            return false;
        }
        return dernierStatut.getStatut() != StatutDemandeEntity.STATUT_APPROUVE &&
                dernierStatut.getStatut() != StatutDemandeEntity.STATUT_REJETER;
    }

}