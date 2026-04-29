package com.project.visa.service;

import com.project.visa.entity.*;
import com.project.visa.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DemandeService {

    @Autowired
    private DemandeRepository demandeRepository;
    @Autowired
    private StatutDemandeService statutDemandeService;
    @Autowired
    private PieceDemandeRepository pieceDemandeRepository;

    @Autowired
    private PieceRepository pieceRepository;

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

    public boolean canBeScan(DemandeEntity demande) {
       
        if (demande == null || demande.getVisaTransformable() == null) {
            return false;
        }

        Integer typeVisaId = demande.getTypeVisa().getId();

        List<PieceDemandeEntity> piecesUploaded = pieceDemandeRepository.findByDemandeId(demande.getId());
        Set<Integer> uploadedPieceIds = piecesUploaded.stream()
                .map(pd -> pd.getPiece().getId())
                .collect(Collectors.toSet());

        List<PieceEntity> piecesGenerales = pieceRepository.findAll().stream()
                .filter(p -> p.getTypeVisa() == null)
                .collect(Collectors.toList());

        for (PieceEntity pieceGenerale : piecesGenerales) {
            if (!uploadedPieceIds.contains(pieceGenerale.getId())) {
                return false; 
            }
        }

        List<PieceEntity> piecesSpecifiques = pieceRepository.findByTypeVisaEntity_Id(typeVisaId);

        for (PieceEntity pieceSpecifique : piecesSpecifiques) {
            if (!uploadedPieceIds.contains(pieceSpecifique.getId())) {
                return false;
            }
        }

        return true;
    }

}