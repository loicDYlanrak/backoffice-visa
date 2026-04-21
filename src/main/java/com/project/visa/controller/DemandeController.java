package com.project.visa.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.visa.entity.DemandeEntity;
import com.project.visa.entity.DemandeurEntity;
import com.project.visa.entity.NationaliteEntity;
import com.project.visa.entity.PasseportEntity;
import com.project.visa.entity.SituationFamilialeEntity;
import com.project.visa.entity.StatutDemandeEntity;
import com.project.visa.entity.TypeDemandeEntity;
import com.project.visa.entity.VisaTransformableEntity;
import com.project.visa.service.DemandeService;
import com.project.visa.service.DemandeurService;
import com.project.visa.service.NationaliteService;
import com.project.visa.service.PasseportService;
import com.project.visa.service.SituationFamilialeService;
import com.project.visa.service.StatutDemandeService;
import com.project.visa.service.TypeDemandeService;
import com.project.visa.service.TypeVisaService;
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

    @Autowired
    private NationaliteService nationaliteService;

    @Autowired
    private SituationFamilialeService situationFamilialeService;

    @Autowired
    private TypeDemandeService typeDemandeService;

    @Autowired
    private TypeVisaService typeVisaService;
  
    @GetMapping("/demandes")
    public String list(@RequestParam(name = "reference", required = false) String reference,
                       Model model) {
        List<DemandeEntity> demandes = demandeService.findAll();
        Map<Integer, String> referenceMap = buildReferenceMap(demandes);
        Map<Integer, String> statusMap = buildStatusMap(demandes);

        if (reference != null && !reference.isBlank()) {
            String trimmed = reference.trim().toUpperCase();
            demandes = demandes.stream()
                    .filter(d -> {
                        String ref = referenceMap.get(d.getId());
                        return ref != null && ref.contains(trimmed);
                    })
                    .collect(Collectors.toList());
        }

        model.addAttribute("reference", reference);
        model.addAttribute("referenceMap", referenceMap);
        model.addAttribute("statusMap", statusMap);
        model.addAttribute("demandes", demandes);
        model.addAttribute("template", "demande/liste");
        return "template";
    }

    @PostMapping("/demande")
    public String createDemande(@ModelAttribute DemandeEntity demandeEntity, 
                                @ModelAttribute DemandeurEntity demandeurEntity, 
                                @ModelAttribute PasseportEntity passeportEntity,
                                @ModelAttribute VisaTransformableEntity visaTransformableEntity,
                                @RequestParam(name = "idSituationFamiliale", required = false) Integer idSituationFamiliale,
                                @RequestParam(name = "idNationaliteActuelle", required = false) Integer idNationaliteActuelle,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        
        LocalDate currentDate = LocalDate.now();
        
        if (demandeurEntity == null) {
            redirectAttributes.addFlashAttribute("error", "Demandeur invalide : problème dans les informations personnelles.");
            return "redirect:/demande/formulaire";
        }

        if (idSituationFamiliale != null) {
            SituationFamilialeEntity situation = new SituationFamilialeEntity();
            situation.setId(idSituationFamiliale);
            demandeurEntity.setSituationFamiliale(situation);
        }

        if (idNationaliteActuelle != null) {
            NationaliteEntity nationalite = new NationaliteEntity();
            nationalite.setId(idNationaliteActuelle);
            demandeurEntity.setNationalite(nationalite);
        }

        // Valider le passeport
        if (passeportEntity == null) {
            redirectAttributes.addFlashAttribute("error", "Passeport invalide : problème dans les dates de validité.");
            return "redirect:/demande/formulaire";
        }
        passeportEntity.setDemandeur(demandeurEntity);
        demandeurEntity.setPasseports(List.of(passeportEntity));
        if (!passeportEntity.isValid(currentDate)) {
            redirectAttributes.addFlashAttribute("error", "Passeport invalide : problème dans les dates de validité.");
            return "redirect:/demande/formulaire";
        }

        if (!demandeurEntity.isValid(currentDate)) {
            redirectAttributes.addFlashAttribute("error", buildDemandeurValidationMessage(demandeurEntity, currentDate));
            return "redirect:/demande/formulaire";
        }
        
        // Valider le visa transformable
        if (visaTransformableEntity == null) {
            redirectAttributes.addFlashAttribute("error", "Visa transformable invalide : problème dans les dates d'entrée/sortie.");
            return "redirect:/demande/formulaire";
        }
        visaTransformableEntity.setDemandeur(demandeurEntity);
        visaTransformableEntity.setPasseport(passeportEntity);
        if (!visaTransformableEntity.demandeValide(currentDate)) {
            redirectAttributes.addFlashAttribute("error", "Visa transformable invalide : problème dans les dates d'entrée/sortie.");
            return "redirect:/demande/formulaire";
        }
        
        // Valider la demande
        if (demandeEntity == null) {
            redirectAttributes.addFlashAttribute("error", "Demande invalide : problème dans les dates ou types.");
            return "redirect:/demande/formulaire";
        }
        if (demandeEntity.getTypeDemande() == null) {
            TypeDemandeEntity typeDemande = new TypeDemandeEntity();
            typeDemande.setId(1);
            demandeEntity.setTypeDemande(typeDemande);
        }
        demandeEntity.setVisaTransformable(visaTransformableEntity);
        demandeEntity.setDemandeur(demandeurEntity);
        demandeEntity.setDateDemande(currentDate);
        if (!demandeEntity.isValide()) {
            redirectAttributes.addFlashAttribute("error", "Demande invalide : problème dans les dates ou types.");
            return "redirect:/demande/formulaire";
        }
        
        // ========== 2. SAUVEGARDE (seulement si tout est valide) ==========
        
        try {
            // Sauvegarder le demandeur
            DemandeurEntity savedDemandeur=new DemandeurEntity();
            if(!demandeurService.emailExists(demandeurEntity.getEmail())){
                savedDemandeur= demandeurService.save(demandeurEntity);
            }
            else{
                savedDemandeur=demandeurService.findTopByEmail(demandeurEntity.getEmail()).get();
            }
             
            
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
            DemandeEntity savedDemande = demandeService.save(demandeEntity);
            
            // Créer le statut initial
            StatutDemandeEntity statutDemandeEntity = new StatutDemandeEntity();
            statutDemandeEntity.setDemande(savedDemande);
            statutDemandeEntity.setDateChangementStatut(currentDate);
            statutDemandeEntity.setStatut(1); // 1 = "soumise" ou "en attente"
            statutDemandeService.save(statutDemandeEntity);
            
            String reference = buildReference(savedDemande);
            redirectAttributes.addFlashAttribute("successMessage",
                "Votre demande N° " + reference + " a été enregistrée");
            
        } catch (Exception e) {
            String message= e.getMessage()+" "+e.getStackTrace();
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la creation. Veuillez verifier les informations saisies."+message);
            System.out.println("demande/formulaire");
        
            return "redirect:/demande/formulaire";
        }
        System.out.println("demande/liste");
        return "redirect:/demande/liste";

    }

    @GetMapping("/demande/formulaire")
    public String showForm(Model model) {
        populateFormDefaults(model);
        populateFormOptions(model);
        model.addAttribute("formTitle", "Nouvelle demande");
        model.addAttribute("formAction", "/demande");
        model.addAttribute("template", "demande/modification");
        return "template";
    }

    @GetMapping("/demande/modifier/{id}")
    public String showEditForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        DemandeEntity demande = demandeService.findById(id);
        if (demande == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Demande non trouvee.");
            return "redirect:/demande/liste";
        }

        DemandeurEntity demandeur = demande.getDemandeur();
        PasseportEntity passeport = demande.getVisaTransformable() != null ? demande.getVisaTransformable().getPasseport() : null;
        VisaTransformableEntity visaTransformable = demande.getVisaTransformable();

        populateFormOptions(model);
        model.addAttribute("formTitle", "Modifier la demande");
        model.addAttribute("formAction", "/modifier/" + id);

        model.addAttribute("prefillDemandeId", demande.getId());
        if (demandeur != null) {
            model.addAttribute("prefillDemandeurId", demandeur.getId());
            model.addAttribute("prefillNom", demandeur.getNom());
            model.addAttribute("prefillPrenom", demandeur.getPrenom());
            model.addAttribute("prefillDateNaissance", demandeur.getDateNaissance());
            model.addAttribute("prefillLieuNaissance", demandeur.getLieuNaissance());
            model.addAttribute("prefillTelephone", demandeur.getTelephone());
            model.addAttribute("prefillEmail", demandeur.getEmail());
            model.addAttribute("prefillAdresse", demandeur.getAdresse());
            if (demandeur.getSituationFamiliale() != null) {
                model.addAttribute("prefillSituationId", demandeur.getSituationFamiliale().getId());
            }
            if (demandeur.getNationalite() != null) {
                model.addAttribute("prefillNationaliteActuelleId", demandeur.getNationalite().getId());
            }
        }

        if (passeport != null) {
            model.addAttribute("prefillPasseportId", passeport.getId());
            model.addAttribute("prefillNumeroPasseport", passeport.getNumeroPasseport());
            model.addAttribute("prefillDateDelivrance", passeport.getDateDelivrance());
            model.addAttribute("prefillDateExpiration", passeport.getDateExpiration());
            model.addAttribute("prefillPaysDelivrance", passeport.getPaysDelivrance());
        }

        if (visaTransformable != null) {
            model.addAttribute("prefillVisaTransformableId", visaTransformable.getId());
            model.addAttribute("prefillNumeroReference", visaTransformable.getNumeroReference());
            model.addAttribute("prefillDateEntree", visaTransformable.getDateEntree());
            model.addAttribute("prefillDateSortie", visaTransformable.getDateSortie());
        }

        if (demande.getTypeDemande() != null) {
            model.addAttribute("prefillTypeDemandeId", demande.getTypeDemande().getId());
        }
        if (demande.getTypeVisa() != null) {
            model.addAttribute("prefillTypeVisaId", demande.getTypeVisa().getId());
        }

        model.addAttribute("template", "demande/modification");
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
                                @RequestParam(name = "demandeur.id", required = false) Integer demandeurId,
                                @RequestParam(name = "passeport.id", required = false) Integer passeportId,
                                @RequestParam(name = "visaTransformable.id", required = false) Integer visaTransformableId,
                                @RequestParam(name = "idSituationFamiliale", required = false) Integer idSituationFamiliale,
                                @RequestParam(name = "idNationaliteActuelle", required = false) Integer idNationaliteActuelle,
                                RedirectAttributes redirectAttributes) {
        
        LocalDate currentDate = LocalDate.now();
        
        try {
            // ========== 1. VÉRIFICATION DE L'EXISTENCE ==========
            
        DemandeEntity existingDemande = demandeService.findById(id);
    if (existingDemande == null) {
        throw new RuntimeException("Demande non trouvée");
    }

    // Récupérer le demandeur existant
    if (demandeurId == null || passeportId == null || visaTransformableId == null) {
        redirectAttributes.addFlashAttribute("errorMessage", "Identifiants manquants pour la modification.");
        return "redirect:/demande/modifier/" + id;
    }

    DemandeurEntity existingDemandeur = demandeurService.findById(demandeurId);
    if (existingDemandeur == null) {
        throw new RuntimeException("Demandeur non trouvé");
    }

    // Récupérer le passeport existant
    PasseportEntity existingPasseport = passeportService.findById(passeportId);
    if (existingPasseport == null) {
        throw new RuntimeException("Passeport non trouvé");
    }

    // Récupérer le visa transformable existant
    VisaTransformableEntity existingVisaTransformable = visaTransformableService.findById(visaTransformableId);
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
            existingDemandeur.setDateNaissance(demandeurEntity.getDateNaissance());
            existingDemandeur.setLieuNaissance(demandeurEntity.getLieuNaissance());
            existingDemandeur.setTelephone(demandeurEntity.getTelephone());
            existingDemandeur.setEmail(demandeurEntity.getEmail());
            existingDemandeur.setAdresse(demandeurEntity.getAdresse());
            if (idSituationFamiliale != null) {
                SituationFamilialeEntity situation = new SituationFamilialeEntity();
                situation.setId(idSituationFamiliale);
                existingDemandeur.setSituationFamiliale(situation);
            }
            if (idNationaliteActuelle != null) {
                NationaliteEntity nationalite = new NationaliteEntity();
                nationalite.setId(idNationaliteActuelle);
                existingDemandeur.setNationalite(nationalite);
            }
            demandeurService.save(existingDemandeur);
            
            // Mettre à jour le passeport
            Optional<PasseportEntity> duplicatePasseport = passeportService.findByNumeroPasseport(passeportEntity.getNumeroPasseport());
            if (duplicatePasseport.isPresent() && duplicatePasseport.get().getId() != existingPasseport.getId()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Numero de passeport deja utilise.");
                return "redirect:/demande/modifier/" + id;
            }
            existingPasseport.setNumeroPasseport(passeportEntity.getNumeroPasseport());
            existingPasseport.setDateDelivrance(passeportEntity.getDateDelivrance());
            existingPasseport.setDateExpiration(passeportEntity.getDateExpiration());
            existingPasseport.setPaysDelivrance(passeportEntity.getPaysDelivrance());
            passeportService.save(existingPasseport);
            
            // Mettre à jour le visa transformable
            existingVisaTransformable.setDateEntree(visaTransformableEntity.getDateEntree());
            existingVisaTransformable.setDateSortie(visaTransformableEntity.getDateSortie());
            existingVisaTransformable.setNumeroReference(visaTransformableEntity.getNumeroReference());
            visaTransformableService.save(existingVisaTransformable);
            
            // Mettre à jour la demande
            existingDemande.setTypeVisa(demandeEntity.getTypeVisa());
            existingDemande.setTypeDemande(demandeEntity.getTypeDemande());
            demandeService.save(existingDemande);
            
            redirectAttributes.addFlashAttribute("successMessage", "Demande modifiée avec succès");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la modification. Veuillez verifier les informations saisies.");
            redirectAttributes.addFlashAttribute("demandeEntity", demandeEntity);
            return "redirect:/demande/modifier/" + id;
        }
        
        return "redirect:/demande/liste";
    }
    
    @GetMapping("/demande/create")
    public String showCreateForm(Model model) {
        populateFormDefaults(model);
        populateFormOptions(model);
        model.addAttribute("formTitle", "Nouvelle demande");
        model.addAttribute("formAction", "/demande");
        model.addAttribute("template", "demande/formulaire");
        return "template";
    }

    @GetMapping("/demande/liste")
    public String showList(@RequestParam(name = "reference", required = false) String reference,
                           Model model) {
        return list(reference, model);
    }

    private void populateFormDefaults(Model model) {
        LocalDate today = LocalDate.now();
        model.addAttribute("prefillNom", "Rakoto");
        model.addAttribute("prefillPrenom", "Jean");
        model.addAttribute("prefillDateNaissance", "1990-01-01");
        model.addAttribute("prefillLieuNaissance", "Antananarivo");
        model.addAttribute("prefillTelephone", "0320000000");
        model.addAttribute("prefillEmail", "demo@example.com");
        model.addAttribute("prefillAdresse", "Antananarivo");
        model.addAttribute("prefillNumeroPasseport", "P1234567");
        model.addAttribute("prefillDateDelivrance", today.minusYears(2).toString());
        model.addAttribute("prefillDateExpiration", "2030-12-31");
        model.addAttribute("prefillPaysDelivrance", "Madagascar");
        model.addAttribute("prefillDateDebut", today.toString());
        model.addAttribute("prefillDateFin", today.plusMonths(3).toString());
        model.addAttribute("prefillDateEntree", today.minusMonths(1).toString());
        model.addAttribute("prefillDateSortie", today.plusMonths(2).toString());
        model.addAttribute("prefillNumeroReference", "VISA-2024-001");
        model.addAttribute("prefillSituationId", 1);
        model.addAttribute("prefillNationaliteActuelleId", 1);
        model.addAttribute("prefillTypeVisaId", 1);
        model.addAttribute("prefillTypeDemandeId", 1);
    }

    private void populateFormOptions(Model model) {
        model.addAttribute("situations", situationFamilialeService.findAll());
        model.addAttribute("nationalites", nationaliteService.findAll());
        model.addAttribute("typeDemandes", typeDemandeService.findAll());
        model.addAttribute("typeVisas", typeVisaService.findAll());
    }

    private String buildReference(DemandeEntity demande) {
        int year = LocalDate.now().getYear();
        return String.format("RES-%d-%03d", year, demande.getId());
    }

    private String buildDemandeurValidationMessage(DemandeurEntity demandeur, LocalDate currentDate) {
        StringBuilder details = new StringBuilder();

        if (demandeur.getNom() == null || demandeur.getNom().isBlank()) {
            details.append("nom manquant, ");
        }
        if (demandeur.getPrenom() == null || demandeur.getPrenom().isBlank()) {
            details.append("prenom manquant, ");
        }
        if (demandeur.getDateNaissance() == null) {
            details.append("date de naissance manquante, ");
        } else if (demandeur.getDateNaissance().isAfter(currentDate)) {
            details.append("date de naissance future, ");
        }
        if (demandeur.getTelephone() == null || demandeur.getTelephone().isBlank()) {
            details.append("contact manquant, ");
        }
        if (demandeur.getAdresse() == null || demandeur.getAdresse().isBlank()) {
            details.append("adresse manquante, ");
        }
        if (demandeur.getSituationFamiliale() == null) {
            details.append("situation familiale manquante, ");
        }
        if (demandeur.getNationalite() == null) {
            details.append("nationalite manquante, ");
        }

        if (details.length() == 0) {
            return "Demandeur invalide : probleme dans les informations personnelles.";
        }

        details.setLength(details.length() - 2);
        return "Demandeur invalide : " + details + ".";
    }

    private Map<Integer, String> buildReferenceMap(List<DemandeEntity> demandes) {
        return demandes.stream().collect(java.util.stream.Collectors.toMap(
                DemandeEntity::getId,
                this::buildReference
        ));
    }

    private Map<Integer, String> buildStatusMap(List<DemandeEntity> demandes) {
        return demandes.stream().collect(Collectors.toMap(
                DemandeEntity::getId,
                demande -> statutDemandeService.findLatestByDemandeId(demande.getId())
                        .map(statut -> mapStatutLabel(statut.getStatut()))
                        .orElse("Cree")
        ));
    }

    private String mapStatutLabel(Integer statut) {
        if (statut == null) {
            return "Cree";
        }
        return switch (statut) {
            case 1 -> "Cree";
            case 2 -> "Soumise";
            case 3 -> "En cours";
            case 4 -> "Validee";
            case 5 -> "Rejetee";
            default -> "Cree";
        };
    }
}