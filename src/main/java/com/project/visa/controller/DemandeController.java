package com.project.visa.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.visa.entity.DemandeEntity;
import com.project.visa.entity.DemandeurEntity;
import com.project.visa.entity.PasseportEntity;
import com.project.visa.entity.TypeVisaEntity;
import com.project.visa.entity.VisaEntity;
import com.project.visa.service.DemandeService;
import com.project.visa.service.DemandeurService;
import com.project.visa.service.PasseportService;
import com.project.visa.service.TypeVisaService;
import com.project.visa.service.VisaService;

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

    @Autowired
    private TypeVisaService typeVisaService;

    @GetMapping("/demandes")
    public String list(Model model) {
        List<DemandeEntity> demandes = demandeService.findAll();
        model.addAttribute("demandes", demandes);
        model.addAttribute("template", "demande/demande-list");
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
            
            // Vérification date expiration > aujourd'hui
            if (passeportEntity.getDateExpiration().isBefore(today) || passeportEntity.getDateExpiration().isEqual(today)) {
                redirectAttributes.addFlashAttribute("errorMessage", "La date d'expiration du passeport doit être postérieure à aujourd'hui");
                return "redirect:/demandes/form";
            }
            
            // Vérification date délivrance pas dans le futur
            if (passeportEntity.getDateDelivrance().isAfter(today)) {
                redirectAttributes.addFlashAttribute("errorMessage", "La date de délivrance ne peut pas être dans le futur");
                return "redirect:/demandes/form";
            }
            
            // Vérification date délivrance avant date expiration
            if (passeportEntity.getDateDelivrance().isAfter(passeportEntity.getDateExpiration())) {
                redirectAttributes.addFlashAttribute("errorMessage", "La date de délivrance doit être avant la date d'expiration");
                return "redirect:/demandes/form";
            }
            
            // Vérification unicité du numéro de passeport
            if (passeportService.existsByNumeroPasseport(passeportEntity.getNumeroPasseport())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Ce numéro de passeport existe déjà");
                return "redirect:/demandes/form";
            }
            
            // Attention : expiration dans moins de 6 mois (avertissement seulement)
            LocalDate sixMonthsLater = today.plusMonths(6);
            if (passeportEntity.getDateExpiration().isBefore(sixMonthsLater)) {
                redirectAttributes.addFlashAttribute("warningMessage", "Attention : Le passeport expirera dans moins de 6 mois");
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
            
            if (visaEntity.getTypeVisa() == null || visaEntity.getTypeVisa().getId() == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Le type de visa est obligatoire");
                return "redirect:/demandes/form";
            }
            
            // Vérification durée du visa (avertissement seulement)
            long daysBetween = ChronoUnit.DAYS.between(visaEntity.getDateDebut(), visaEntity.getDateFin());
            if (daysBetween > 365) {
                redirectAttributes.addFlashAttribute("warningMessage", "Attention : La durée du visa dépasse 1 an");
            }
            
            // ========== SAUVEGARDE DES DONNÉES ==========
            
            // 1. Sauvegarder le demandeur
            DemandeurEntity savedDemandeur = demandeurService.save(demandeurEntity);
            
            // 2. Sauvegarder le passeport
            PasseportEntity savedPasseport = passeportService.save(passeportEntity);
            
            // 3. Récupérer le TypeVisa depuis la BDD
            TypeVisaEntity typeVisa = typeVisaService.findById(visaEntity.getTypeVisa().getId());
            if (typeVisa == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Type de visa introuvable");
                return "redirect:/demandes/form";
            }
            visaEntity.setTypeVisa(typeVisa);
            
            // 4. Lier le passeport au visa
            visaEntity.setPasseport(savedPasseport);
            VisaEntity savedVisa = visaService.save(visaEntity);
            
            // 5. Créer la demande
            String reference = demandeService.genererReference();
            demandeEntity.setReference(reference);
            demandeEntity.setIdDemandeur(savedDemandeur.getId().intValue());
            demandeEntity.setIdVisa(savedVisa.getId().intValue());
            demandeEntity.setDateDemande(LocalDateTime.now());
            demandeEntity.setIdStatus(1); // "brouillon" ou "soumise"
            
            demandeService.save(demandeEntity);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Demande créée avec succès. Référence : " + reference);
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur lors de la création de la demande : " + e.getMessage());
            return "redirect:/demandes/form";
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
        model.addAttribute("template", "demande/liste");
        return "template";
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