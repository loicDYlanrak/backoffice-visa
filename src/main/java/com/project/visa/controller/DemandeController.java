package com.project.visa.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.visa.entity.DemandeEntity;
import com.project.visa.entity.DemandeurEntity;
import com.project.visa.entity.PasseportEntity;
import com.project.visa.entity.StatutDemandeEntity;
import com.project.visa.entity.VisaTransformableEntity;

import com.project.visa.service.DemandeService;
import com.project.visa.service.DemandeurService;
import com.project.visa.service.PasseportService;
import com.project.visa.service.StatutDemandeService;
import com.project.visa.service.VisaTransformableService;
@Controller
public class DemandeController {

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private DemandeurService demandeurService;

    @Autowired
    private PasseportService passeportService;

    @Autowired
    private VisaTransformableService visaTransformableService;
    
    @Autowired
    private StatutDemandeService statutDemandeService;
  
    @GetMapping("/demandes")
    public String list(Model model) {
        List<DemandeEntity> demandes = demandeService.findAll();
        model.addAttribute("demandes", demandes);
        model.addAttribute("template", "demande/liste");
        return "template";
    }

@PostMapping("/demande")
public String createDemande(@ModelAttribute DemandeEntity demandeEntity, 
                            @ModelAttribute DemandeurEntity demandeurEntity, 
                            @ModelAttribute PasseportEntity passeportEntity,
                            @ModelAttribute VisaTransformableEntity visaTransformableEntity,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
    
    LocalDate currentDate = LocalDate.now();
    
    if (demandeurEntity == null || !demandeurEntity.isValid(currentDate)) {
        redirectAttributes.addFlashAttribute("error", "Demandeur invalide : problème dans les informations personnelles.");
        return "redirect:/demande/formulaire";
    }
    
    // Valider le passeport
    if (passeportEntity == null || !passeportEntity.isValid(currentDate)) {
        redirectAttributes.addFlashAttribute("error", "Passeport invalide : problème dans les dates de validité.");
        return "redirect:/demande/formulaire";
    }
    
    // Valider le visa transformable
    if (visaTransformableEntity == null || !visaTransformableEntity.demandeValide(currentDate)) {
        redirectAttributes.addFlashAttribute("error", "Visa transformable invalide : problème dans les dates d'entrée/sortie.");
        return "redirect:/demande/formulaire";
    }
    
    // Valider la demande
    if (demandeEntity == null || !demandeEntity.isValide()) {
        redirectAttributes.addFlashAttribute("error", "Demande invalide : problème dans les dates ou types.");
        return "redirect:/demande/formulaire";
    }
    
    // ========== 2. SAUVEGARDE (seulement si tout est valide) ==========
    
    try {
        // Sauvegarder le demandeur
        DemandeurEntity savedDemandeur = demandeurService.save(demandeurEntity);
        
        // Sauvegarder le passeport (si non existant)
        PasseportEntity savedPasseport;
        Optional<PasseportEntity> existingPasseport = passeportService.findByNumeroPasseport(passeportEntity.getNumeroPasseport());
        if (existingPasseport.isEmpty()) {
            savedPasseport = passeportService.save(passeportEntity);
        } else {
            savedPasseport = existingPasseport.get();
        }
        
        // Lier le visa transformable au demandeur et passeport
        visaTransformableEntity.setDemandeur(savedDemandeur);
        visaTransformableEntity.setPasseport(savedPasseport);
        VisaTransformableEntity savedVisaTransformable = visaTransformableService.save(visaTransformableEntity);
        
        // Lier la demande
        demandeEntity.setDemandeur(savedDemandeur);
        demandeEntity.setVisaTransformable(savedVisaTransformable);
        demandeEntity.setDateDemande(currentDate);
        DemandeEntity savedDemande = demandeService.save(demandeEntity);
        
        // Créer le statut initial
        StatutDemandeEntity statutDemandeEntity = new StatutDemandeEntity();
        statutDemandeEntity.setDemande(savedDemande);
        statutDemandeEntity.setDateChangementStatut(currentDate);
        statutDemandeEntity.setStatut(1); // 1 = "soumise" ou "en attente"
        statutDemandeService.save(statutDemandeEntity);
        
        redirectAttributes.addFlashAttribute("successMessage", "Demande créée avec succès" );
        
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la création : " + e.getMessage());
        return "redirect:/demande/formulaire";
    }
    
    return "redirect:/demandes";
}

    @GetMapping("/demande/formulaire")
    public String showForm(Model model) {
        populateFormDefaults(model);
        model.addAttribute("template", "demande/formulaire");
        return "template";
    }

    @GetMapping("/demandes/form")
    public String showFormAlias(Model model) {
        return showForm(model);
    }

    @PostMapping("/modifier/{id}")
public String updateDemande(@PathVariable int id,
                            @ModelAttribute DemandeEntity demandeEntity,
                            @ModelAttribute DemandeurEntity demandeurEntity,
                            @ModelAttribute PasseportEntity passeportEntity,
                            @ModelAttribute VisaTransformableEntity visaTransformableEntity,
                            RedirectAttributes redirectAttributes) {
    
    LocalDate currentDate = LocalDate.now();
    
    try {
        // ========== 1. VÉRIFICATION DE L'EXISTENCE ==========
        
       DemandeEntity existingDemande = demandeService.findById(id);
if (existingDemande == null) {
    throw new RuntimeException("Demande non trouvée");
}

// Récupérer le demandeur existant
DemandeurEntity existingDemandeur = demandeurService.findById(demandeurEntity.getId());
if (existingDemandeur == null) {
    throw new RuntimeException("Demandeur non trouvé");
}

// Récupérer le passeport existant
PasseportEntity existingPasseport = passeportService.findById(passeportEntity.getId());
if (existingPasseport == null) {
    throw new RuntimeException("Passeport non trouvé");
}

// Récupérer le visa transformable existant
VisaTransformableEntity existingVisaTransformable = visaTransformableService.findById(visaTransformableEntity.getId());
if (existingVisaTransformable == null) {
    throw new RuntimeException("Visa transformable non trouvé");
}
        // ========== 2. VALIDATION ==========
        
        if (!existingDemandeur.isValid(currentDate)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Demandeur invalide");
            redirectAttributes.addFlashAttribute("demandeEntity", demandeEntity);
            return "redirect:/demande/modifier/" + id;
        }
        
        if (!existingPasseport.isValid(currentDate)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Passeport invalide");
            redirectAttributes.addFlashAttribute("demandeEntity", demandeEntity);
            return "redirect:/demande/modifier/" + id;
        }
        
        if (!existingVisaTransformable.demandeValide(currentDate)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Visa transformable invalide");
            redirectAttributes.addFlashAttribute("demandeEntity", demandeEntity);
            return "redirect:/demande/modifier/" + id;
        }
        
        // ========== 3. MISE À JOUR ==========
        
        // Mettre à jour le demandeur
        existingDemandeur.setNom(demandeurEntity.getNom());
        existingDemandeur.setPrenom(demandeurEntity.getPrenom());
        // ... autres champs
        demandeurService.save(existingDemandeur);
        
        // Mettre à jour le passeport
        existingPasseport.setNumeroPasseport(passeportEntity.getNumeroPasseport());
        // ... autres champs
        passeportService.save(existingPasseport);
        
        // Mettre à jour le visa transformable
        existingVisaTransformable.setDateEntree(visaTransformableEntity.getDateEntree());
        existingVisaTransformable.setDateSortie(visaTransformableEntity.getDateSortie());
        visaTransformableService.save(existingVisaTransformable);
        
        // Mettre à jour la demande
        existingDemande.setTypeVisa(demandeEntity.getTypeVisa());
        existingDemande.setTypeDemande(demandeEntity.getTypeDemande());
        demandeService.save(existingDemande);
        
        redirectAttributes.addFlashAttribute("successMessage", "Demande modifiée avec succès");
        
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Erreur : " + e.getMessage());
        redirectAttributes.addFlashAttribute("demandeEntity", demandeEntity);
        return "redirect:/demande/modifier/" + id;
    }
    
    return "redirect:/demandes";
}
    @GetMapping("/demande/create")
    public String showCreateForm(Model model) {
        populateFormDefaults(model);
        model.addAttribute("template", "demande/formulaire");
        return "template";
    }

    @GetMapping("/demande/liste")
    public String showList(Model model) {
        List<DemandeurEntity> demandeurs = demandeurService.findAll();
        model.addAttribute("demandeurs", demandeurs);
        model.addAttribute("genreMap", buildGenreMap());
        model.addAttribute("situationMap", buildSituationMap());
        model.addAttribute("nationaliteMap", buildNationaliteMap());
        model.addAttribute("template", "demande/liste");
        return "template";
    }

    private Map<Long, String> buildGenreMap() {
        return Map.of(
            1L, "Masculin",
            2L, "Feminin",
            3L, "Autre"
        );
    }

    private Map<Long, String> buildSituationMap() {
        return Map.of(
            1L, "Celibataire",
            2L, "Marie(e)",
            3L, "Divorce(e)",
            4L, "Veuf/Veuve",
            5L, "Pacse(e)"
        );
    }

    private Map<Long, String> buildNationaliteMap() {
        return Map.of(
            1L, "Malgache",
            2L, "Francaise",
            3L, "Chinoise",
            4L, "Americaine",
            5L, "Canadienne",
            6L, "Allemande",
            7L, "Italienne",
            8L, "Espagnole",
            9L, "Britannique",
            10L, "Belge"
        );
    }

    private void populateFormDefaults(Model model) {
        LocalDate today = LocalDate.now();
        model.addAttribute("prefillNom", "Rakoto");
        model.addAttribute("prefillPrenom", "Jean");
        model.addAttribute("prefillDateNaissance", "1990-01-01");
        model.addAttribute("prefillLieuNaissance", "Antananarivo");
        model.addAttribute("prefillProfession", "Ingenieur");
        model.addAttribute("prefillTelephone", "0320000000");
        model.addAttribute("prefillEmail", "demo@example.com");
        model.addAttribute("prefillAdresse", "Antananarivo");
        model.addAttribute("prefillNumeroPasseport", "P1234567");
        model.addAttribute("prefillDateDelivrance", today.minusYears(2).toString());
        model.addAttribute("prefillDateExpiration", "2030-12-31");
        model.addAttribute("prefillPaysDelivrance", "Madagascar");
        model.addAttribute("prefillDateDebut", today.toString());
        model.addAttribute("prefillDateFin", today.plusMonths(3).toString());
        model.addAttribute("prefillGenreId", 1);
        model.addAttribute("prefillSituationId", 1);
        model.addAttribute("prefillNationaliteActuelleId", 1);
        model.addAttribute("prefillNationaliteOrigineId", 1);
        model.addAttribute("prefillTypeVisaId", 1);
    }
}