package com.project.visa.controller;

import com.project.visa.entity.*;
import com.project.visa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;

@Controller
@RequestMapping("/demande")
public class DecisionController {

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private StatutDemandeService statutDemandeService;

    @Autowired
    private VisaService visaService;

    @Autowired
    private CarteResidentService carteResidentService;

    @Autowired
    private ScanFichierService scanFichierService;

    /**
     * Affiche la page de confirmation de validation d'une demande
     * @param idDemande ID de la demande
     * @return ModelAndView avec les détails de la demande
     */
    @GetMapping("/{idDemande}/confirmation-valider")
    public ModelAndView showConfirmationValider(@PathVariable Long idDemande) {
        ModelAndView mav = new ModelAndView("template");
        
        DemandeEntity demande = demandeService.findById(idDemande.intValue());
        if (demande == null) {
            mav.addObject("errorMessage", "Demande non trouvée");
            mav.addObject("template", "error");
            return mav;
        }

        // Vérifier si la demande peut être validée
        StatutDemandeEntity dernierStatut = statutDemandeService.findLatestByDemandeId(demande.getId()).orElse(null);
        
        if (dernierStatut == null || dernierStatut.getStatut() != StatutDemandeEntity.STATUT_SCANNE) {
            mav.addObject("errorMessage", 
                "Cette demande ne peut pas être validée car elle n'est pas au statut 'Scanné'");
            mav.addObject("template", "error");
            return mav;
        }

        // Vérifier que tous les documents sont uploadés
        if (!scanFichierService.verifierTousDocumentsUploades(idDemande)) {
            mav.addObject("errorMessage", 
                "Tous les documents requis doivent être uploadés avant validation");
            mav.addObject("template", "error");
            return mav;
        }

        // Récupérer les informations pour l'affichage
        String reference = buildReference(demande);
        String statutLibelle = dernierStatut.getLibelleStatut();
        
        mav.addObject("demande", demande);
        mav.addObject("reference", reference);
        mav.addObject("statutActuel", statutLibelle);
        mav.addObject("demandeur", demande.getDemandeur());
        mav.addObject("typeVisa", demande.getTypeVisa());
        mav.addObject("typeDemande", demande.getTypeDemande());
        mav.addObject("dateDemande", demande.getDateDemande());
        mav.addObject("template", "demande/confirmation_valider");
        
        return mav;
    }

    /**
     * Valide une demande, crée le visa et la carte de résident
     * @param idDemande ID de la demande
     * @param redirectAttributes Attributs pour la redirection
     * @return RedirectView vers la liste des demandes
     */
    @PostMapping("/{idDemande}/valider")
    public RedirectView validerDemande(@PathVariable Long idDemande, RedirectAttributes redirectAttributes) {
        try {
            DemandeEntity demande = demandeService.findById(idDemande.intValue());
            if (demande == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Demande non trouvée");
                return new RedirectView("/demande/liste");
            }

            // Vérifier si la demande peut être validée
            StatutDemandeEntity dernierStatut = statutDemandeService.findLatestByDemandeId(demande.getId()).orElse(null);
            
            if (dernierStatut == null || dernierStatut.getStatut() != StatutDemandeEntity.STATUT_SCANNE) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Cette demande ne peut pas être validée car elle n'est pas au statut 'Scanné'");
                return new RedirectView("/demande/liste");
            }

            // Vérifier que tous les documents sont uploadés
            if (!scanFichierService.verifierTousDocumentsUploades(idDemande)) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Tous les documents requis doivent être uploadés avant validation");
                return new RedirectView("/demande/" + idDemande + "/documents-a-scanner");
            }

            // 1. Ajouter le statut "Approuvé"
            LocalDate currentDate = LocalDate.now();
            StatutDemandeEntity statutApprouve = new StatutDemandeEntity();
            statutApprouve.setDemande(demande);
            statutApprouve.setStatut(StatutDemandeEntity.STATUT_APPROUVE);
            statutApprouve.setDateChangementStatut(currentDate);
            statutDemandeService.save(statutApprouve);

            // 2. Mettre à jour la date de traitement de la demande
            demande.setDateTraitement(currentDate);
            demandeService.save(demande);

            // 3. Créer le visa
            VisaEntity visa = new VisaEntity();
            visa.setDemande(demande);
            visa.setPasseport(demande.getVisaTransformable().getPasseport());
            
            // Calculer les dates du visa selon le type de visa
            LocalDate visaStartDate = currentDate;
            LocalDate visaEndDate = calculateVisaEndDate(currentDate, demande.getTypeVisa());
            
            visa.setDateDebut(visaStartDate);
            visa.setDateFin(visaEndDate);
            
            // Générer une référence unique pour le visa
            String visaReference = generateVisaReference(demande);
            visa.setReference(visaReference);
            visaService.save(visa);

            // 4. Créer la carte de résident (si applicable)
            if (shouldCreateCarteResident(demande)) {
                CarteResidentEntity carteResident = new CarteResidentEntity();
                carteResident.setDemande(demande);
                carteResident.setPasseport(demande.getVisaTransformable().getPasseport());
                
                // Calculer les dates de la carte de résident
                LocalDate carteStartDate = currentDate;
                LocalDate carteEndDate = calculateCarteResidentEndDate(currentDate, demande.getTypeVisa());
                
                carteResident.setDateDebut(carteStartDate);
                carteResident.setDateFin(carteEndDate);
                
                // Générer une référence unique pour la carte de résident
                String carteReference = generateCarteResidentReference(demande);
                carteResident.setReference(carteReference);
                carteResidentService.save(carteResident);
            }

            String reference = buildReference(demande);
            redirectAttributes.addFlashAttribute("successMessage", 
                String.format("La demande N° %s a été validée avec succès. Visa créé : %s", 
                    reference, visa.getReference()));

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur lors de la validation de la demande: " + e.getMessage());
        }
        
        return new RedirectView("/demande/liste");
    }

    /**
     * Affiche la page de confirmation de rejet d'une demande
     * @param idDemande ID de la demande
     * @return ModelAndView avec les détails de la demande
     */
    @GetMapping("/{idDemande}/confirmation-rejeter")
    public ModelAndView showConfirmationRejeter(@PathVariable Long idDemande) {
        ModelAndView mav = new ModelAndView("template");
        
        DemandeEntity demande = demandeService.findById(idDemande.intValue());
        if (demande == null) {
            mav.addObject("errorMessage", "Demande non trouvée");
            mav.addObject("template", "error");
            return mav;
        }

        // Vérifier si la demande peut être rejetée
        StatutDemandeEntity dernierStatut = statutDemandeService.findLatestByDemandeId(demande.getId()).orElse(null);
        
        if (dernierStatut == null) {
            mav.addObject("errorMessage", "Impossible de déterminer le statut de la demande");
            mav.addObject("template", "error");
            return mav;
        }

        if (dernierStatut.getStatut() == StatutDemandeEntity.STATUT_APPROUVE) {
            mav.addObject("errorMessage", "Cette demande a déjà été validée et ne peut pas être rejetée");
            mav.addObject("template", "error");
            return mav;
        }

        if (dernierStatut.getStatut() == StatutDemandeEntity.STATUT_REJETER) {
            mav.addObject("errorMessage", "Cette demande a déjà été rejetée");
            mav.addObject("template", "error");
            return mav;
        }

        // Récupérer les informations pour l'affichage
        String reference = buildReference(demande);
        String statutLibelle = dernierStatut.getLibelleStatut();
        
        mav.addObject("demande", demande);
        mav.addObject("reference", reference);
        mav.addObject("statutActuel", statutLibelle);
        mav.addObject("demandeur", demande.getDemandeur());
        mav.addObject("typeVisa", demande.getTypeVisa());
        mav.addObject("typeDemande", demande.getTypeDemande());
        mav.addObject("dateDemande", demande.getDateDemande());
        mav.addObject("template", "demande/confirmation_rejeter");
        
        return mav;
    }

    /**
     * Rejette une demande
     * @param idDemande ID de la demande
     * @param motif Motif du rejet (optionnel)
     * @param redirectAttributes Attributs pour la redirection
     * @return RedirectView vers la liste des demandes
     */
    @PostMapping("/{idDemande}/rejeter")
    public RedirectView rejeterDemande(
            @PathVariable Long idDemande,
            @RequestParam(required = false) String motif,
            RedirectAttributes redirectAttributes) {
        
        try {
            DemandeEntity demande = demandeService.findById(idDemande.intValue());
            if (demande == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Demande non trouvée");
                return new RedirectView("/demande/liste");
            }

            // Vérifier si la demande peut être rejetée
            StatutDemandeEntity dernierStatut = statutDemandeService.findLatestByDemandeId(demande.getId()).orElse(null);
            
            if (dernierStatut == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Impossible de déterminer le statut de la demande");
                return new RedirectView("/demande/liste");
            }

            if (dernierStatut.getStatut() == StatutDemandeEntity.STATUT_APPROUVE) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Cette demande a déjà été validée et ne peut pas être rejetée");
                return new RedirectView("/demande/liste");
            }

            if (dernierStatut.getStatut() == StatutDemandeEntity.STATUT_REJETER) {
                redirectAttributes.addFlashAttribute("errorMessage", "Cette demande a déjà été rejetée");
                return new RedirectView("/demande/liste");
            }

            // Ajouter le statut "Rejeté"
            StatutDemandeEntity statutRejete = new StatutDemandeEntity();
            statutRejete.setDemande(demande);
            statutRejete.setStatut(StatutDemandeEntity.STATUT_REJETER);
            statutRejete.setDateChangementStatut(LocalDate.now());
            statutDemandeService.save(statutRejete);

            // Mettre à jour la date de traitement
            demande.setDateTraitement(LocalDate.now());
            demandeService.save(demande);

            String reference = buildReference(demande);
            String message = String.format("La demande N° %s a été rejetée.", reference);
            if (motif != null && !motif.trim().isEmpty()) {
                message += " Motif: " + motif;
            }
            
            redirectAttributes.addFlashAttribute("successMessage", message);

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur lors du rejet de la demande: " + e.getMessage());
        }
        
        return new RedirectView("/demande/liste");
    }

    // ========== Méthodes utilitaires privées ==========

    private String buildReference(DemandeEntity demande) {
        int year = demande.getDateDemande().getYear();
        return String.format("RES-%d-%03d", year, demande.getId());
    }

    private String generateVisaReference(DemandeEntity demande) {
        int year = LocalDate.now().getYear();
        return String.format("VIS-%d-%05d", year, demande.getId());
    }

    private String generateCarteResidentReference(DemandeEntity demande) {
        int year = LocalDate.now().getYear();
        return String.format("CR-%d-%05d", year, demande.getId());
    }

    /**
     * Calcule la date de fin du visa selon le type de visa
     */
    private LocalDate calculateVisaEndDate(LocalDate startDate, TypeVisaEntity typeVisa) {
        // A adapter selon les règles métier
        // Exemple: visa touriste 3 mois, visa résident 1 an, etc.
        String libelle = typeVisa.getLibelle().toLowerCase();
        
        if (libelle.contains("touriste") || libelle.contains("court sejour")) {
            return startDate.plusMonths(3);
        } else if (libelle.contains("resident") || libelle.contains("long sejour")) {
            return startDate.plusYears(1);
        } else {
            return startDate.plusMonths(6); // durée par défaut
        }
    }

    /**
     * Détermine si une carte de résident doit être créée
     */
    private boolean shouldCreateCarteResident(DemandeEntity demande) {
        // A adapter selon les règles métier
        // Exemple: uniquement pour les visas de résident
        String typeVisaLibelle = demande.getTypeVisa().getLibelle().toLowerCase();
        return typeVisaLibelle.contains("resident") || typeVisaLibelle.contains("long sejour");
    }

    /**
     * Calcule la date de fin de la carte de résident
     */
    private LocalDate calculateCarteResidentEndDate(LocalDate startDate, TypeVisaEntity typeVisa) {
        // A adapter selon les règles métier
        // Généralement 5 ou 10 ans pour une carte de résident
        String libelle = typeVisa.getLibelle().toLowerCase();
        
        if (libelle.contains("permanent")) {
            return startDate.plusYears(10);
        } else {
            return startDate.plusYears(5);
        }
    }
}