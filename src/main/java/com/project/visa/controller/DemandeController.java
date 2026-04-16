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
import java.util.List;

@Controller
public class DemandeController {
    
    @Autowired
    private DemandeService demandeService;
    
    @Autowired
    private DemandeurService demandeurService;
    
    @Autowired
    private PasseportService passeportService;
    
    @Autowired
    private VisaService visaService;
    
    @GetMapping("/demandes")
    public String list(Model model) {
        List<DemandeEntity> demandes = demandeService.findAll();
        model.addAttribute("demandes", demandes);
        model.addAttribute("template", "demande/demande-list");
        return "template";
    }
    
    @GetMapping("/demandes/form")
    public String showForm(Model model) {
        model.addAttribute("demandeEntity", new DemandeEntity());
        model.addAttribute("demandeurEntity", new DemandeurEntity());
        model.addAttribute("passeportEntity", new PasseportEntity());
        model.addAttribute("visaEntity", new VisaEntity());
        model.addAttribute("template", "demande/demande-form");
        return "template";
    }
    
    @PostMapping("/demande")
    public String create(@ModelAttribute DemandeEntity demandeEntity, 
                         @ModelAttribute DemandeurEntity demandeurEntity, 
                         @ModelAttribute PasseportEntity passeportEntity,
                         @ModelAttribute VisaEntity visaEntity,
                         RedirectAttributes redirectAttributes) {
        try {
            // ========== VALIDATIONS DEMANDEUR ==========
            
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
            
            // Validation email
            if (demandeurEntity.getEmail() == null || demandeurEntity.getEmail().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "L'email est obligatoire");
                return "redirect:/demandes/form";
            }
            
            String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (!demandeurEntity.getEmail().matches(emailRegex)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Le format de l'email est invalide");
                return "redirect:/demandes/form";
            }
            
            if (demandeurService.emailExists(demandeurEntity.getEmail())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Cet email est déjà utilisé");
                return "redirect:/demandes/form";
            }
            
            // ========== VALIDATIONS PASSEPORT ==========
            
            if (passeportEntity.getNumeroPasseport() == null || passeportEntity.getNumeroPasseport().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Le numéro de passeport est obligatoire");
                return "redirect:/demandes/form";
            }
            
            if (passeportEntity.getDateDelivrance() == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "La date de délivrance du passeport est obligatoire");
                return "redirect:/demandes/form";
            }
            
            if (passeportEntity.getDateExpiration() == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "La date d'expiration du passeport est obligatoire");
                return "redirect:/demandes/form";
            }
            
            LocalDate today = LocalDate.now();
            if (passeportEntity.getDateExpiration().isBefore(today) || passeportEntity.getDateExpiration().isEqual(today)) {
                redirectAttributes.addFlashAttribute("errorMessage", "La date d'expiration du passeport doit être postérieure à aujourd'hui");
                return "redirect:/demandes/form";
            }
            
            if (passeportEntity.getDateDelivrance().isAfter(passeportEntity.getDateExpiration())) {
                redirectAttributes.addFlashAttribute("errorMessage", "La date de délivrance ne peut pas être après la date d'expiration");
                return "redirect:/demandes/form";
            }
            
            if (passeportService.existsByNumeroPasseport(passeportEntity.getNumeroPasseport())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Ce numéro de passeport existe déjà");
                return "redirect:/demandes/form";
            }
            
            // ========== VALIDATIONS VISA ==========
            
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
            
            if (visaEntity.getTypeVisa() == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Le type de visa est obligatoire");
                return "redirect:/demandes/form";
            }
            
            // ========== SAUVEGARDE DES DONNÉES ==========
            
            // 1. Sauvegarder le demandeur
            DemandeurEntity savedDemandeur = demandeurService.save(demandeurEntity);
            
            // 2. Sauvegarder le passeport
            PasseportEntity savedPasseport = passeportService.save(passeportEntity);
            
            // 3. Sauvegarder le visa (lié au passeport)
            visaEntity.setPasseport(savedPasseport);
            VisaEntity savedVisa = visaService.save(visaEntity);
            
            // 4. Créer la demande
            String reference = demandeService.genererReference();
            demandeEntity.setReference(reference);
            demandeEntity.setIdDemandeur(Math.toIntExact(savedDemandeur.getId())); // Long to int
            demandeEntity.setIdVisa(Math.toIntExact(savedVisa.getId())); // Long to int
            demandeEntity.setDateDemande(LocalDateTime.now());
            demandeEntity.setIdStatus(1); // Statut "En attente"
            
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