package com.project.visa.controller;

import com.project.visa.entity.*;
import com.project.visa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
public class DemandeController {
    
    @Autowired
    private DemandeService demandeService;
    
    @GetMapping("/demandes")
    public String list(Model model) {
        List<DemandeEntity> tests = demandeService.findAll();
        model.addAttribute("tests", tests);
        model.addAttribute("template", "test/test-list");
        return "template";
    }
    
    @PostMapping("/demande")
   public String create(@ModelAttribute DemandeEntity demandeEntity, 
                     @ModelAttribute DemandeurEntity demandeurEntity, 
                     @ModelAttribute PasseportEntity passeportEntity,
                     @ModelAttribute VisaEntity visaEntity,
                     RedirectAttributes redirectAttributes) {
    try {

        
        
        if (demandeurEntity.getNom() == null || demandeurEntity.getNom().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Le nom est obligatoire");
            return "redirect:/demandes/form";
        }
        
        if (demandeurEntity.getPrenom() == null || demandeurEntity.getPrenom().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Le prénom est obligatoire");
            return "redirect:/demandes/form";
        }
        
        if (demandeurEntity.getLieuNaissance() == null || demandeurEntity.getLieuNaissance().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Le lieu de naissance est obligatoire");
            return "redirect:/demandes/form";
        }
        
        if (demandeurEntity.getProfession() == null || demandeurEntity.getProfession().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "La profession est obligatoire");
            return "redirect:/demandes/form";
        }
        
        if (demandeurEntity.getTelephone() == null || demandeurEntity.getTelephone().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Le téléphone est obligatoire");
            return "redirect:/demandes/form";
        }
        
        if (demandeurEntity.getAdresse() == null || demandeurEntity.getAdresse().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "L'adresse est obligatoire");
            return "redirect:/demandes/form";
        }
        
        // 2. Vérifier les champs non null
        if (demandeurEntity.getDateNaissance() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "La date de naissance est obligatoire");
            return "redirect:/demandes/form";
        }
        
        if (demandeurEntity.getIdSituationFamiliale() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "La situation familiale est obligatoire");
            return "redirect:/demandes/form";
        }
        
        if (demandeurEntity.getIdNationaliteActuelle() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "La nationalité actuelle est obligatoire");
            return "redirect:/demandes/form";
        }
        
        if (demandeurEntity.getIdNationaliteOrigine() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "La nationalité d'origine est obligatoire");
            return "redirect:/demandes/form";
        }
        
        if (demandeurEntity.getIdGenre() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Le genre est obligatoire");
            return "redirect:/demandes/form";
        }
        
        // 3. Vérifier le format de l'email
        if (demandeurEntity.getEmail() == null || demandeurEntity.getEmail().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "L'email est obligatoire");
            return "redirect:/demandes/form";
        }
        
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!demandeurEntity.getEmail().matches(emailRegex)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Le format de l'email est invalide");
            return "redirect:/demandes/form";
        }
        
        // Vérifier si l'email existe déjà
        if (demandeurService.emailExists(demandeurEntity.getEmail())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cet email est déjà utilisé");
            return "redirect:/demandes/form";
        }
        
        // ========== VALIDATIONS PASSEPORT ==========
        
        // 4. Vérifier que la date d'expiration du passeport est postérieure à aujourd'hui
        if (passeportEntity.getDateExpiration() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "La date d'expiration du passeport est obligatoire");
            return "redirect:/demandes/form";
        }
        
        LocalDate today = LocalDate.now();
        if (passeportEntity.getDateExpiration().isBefore(today) || passeportEntity.getDateExpiration().isEqual(today)) {
            redirectAttributes.addFlashAttribute("errorMessage", "La date d'expiration du passeport doit être postérieure à aujourd'hui");
            return "redirect:/demandes/form";
        }
        
        // Vérification supplémentaire : le passeport ne doit pas expirer dans moins de 6 mois (recommandation)
        LocalDate sixMonthsLater = today.plusMonths(6);
        if (passeportEntity.getDateExpiration().isBefore(sixMonthsLater)) {
            redirectAttributes.addFlashAttribute("warningMessage", "Attention : Le passeport expirera dans moins de 6 mois");
        }
        
        // ========== VALIDATIONS VISA ==========
        
        // 5. Vérifier que la date de fin du visa est après la date de début
        if (visaEntity.getDateDebut() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "La date de début du visa est obligatoire");
            return "redirect:/demandes/form";
        }
        
        if (visaEntity.getDateFin() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "La date de fin du visa est obligatoire");
            return "redirect:/demandes/form";
        }
        
        if (visaEntity.getDateFin().isBefore(visaEntity.getDateDebut()) || 
            visaEntity.getDateFin().isEqual(visaEntity.getDateDebut())) {
            redirectAttributes.addFlashAttribute("errorMessage", "La date de fin du visa doit être postérieure à la date de début");
            return "redirect:/demandes/form";
        }
        
        // Vérification supplémentaire : la durée du visa ne dépasse pas 1 an (exemple)
        long daysBetween = ChronoUnit.DAYS.between(visaEntity.getDateDebut(), visaEntity.getDateFin());
        if (daysBetween > 365) {
            redirectAttributes.addFlashAttribute("warningMessage", "Attention : La durée du visa dépasse 1 an");
        }
        
        // ========== SAUVEGARDE DES DONNÉES ==========
        
        // Sauvegarder le demandeur
        DemandeurEntity savedDemandeur = demandeurService.save(demandeurEntity);
        
        // Sauvegarder le passeport (lié au demandeur)
        passeportEntity.setIdDemandeur(savedDemandeur.getId());
        PasseportEntity savedPasseport = passeportService.save(passeportEntity);
        
        // Sauvegarder le visa (lié au passeport)
        visaEntity.setIdPasseport(savedPasseport.getId());
        VisaEntity savedVisa = visaService.save(visaEntity);
        
        // Créer la demande
        String reference = demandeService.genererReference();
        demandeEntity.setReference(reference);
        demandeEntity.setIdDemandeur(savedDemandeur.getId());
        demandeEntity.setIdVisa(savedVisa.getId());
        demandeEntity.setDateDemande(LocalDateTime.now());
        demandeEntity.setIdStatus(1); // Statut "En attente" par exemple
        
        demandeService.save(demandeEntity);
        
        redirectAttributes.addFlashAttribute("successMessage", 
            "Demande créée avec succès. Référence : " + reference);
            
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", 
            "Erreur lors de la création de la demande : " + e.getMessage());
    }
    
    return "redirect:/demandes";
}

    
}