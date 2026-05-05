package com.project.visa.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.visa.entity.CarteResidentEntity;
import com.project.visa.entity.DemandeEntity;
import com.project.visa.entity.DemandeurEntity;
import com.project.visa.entity.NationaliteEntity;
import com.project.visa.entity.PasseportEntity;
import com.project.visa.entity.PieceDemandeEntity;
import com.project.visa.entity.PieceEntity;
import com.project.visa.entity.ScanFichierEntity;
import com.project.visa.entity.SituationFamilialeEntity;
import com.project.visa.entity.StatutDemandeEntity;
import com.project.visa.entity.StatutVisaEntity;
import com.project.visa.entity.TypeDemandeEntity;
import com.project.visa.entity.VisaEntity;
import com.project.visa.entity.VisaTransformableEntity;
import com.project.visa.util.*;
import com.project.visa.service.*;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.nio.file.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Controller
public class DemandeController {

    @Value("${Front_url}")
    private String apiUrl;
    @Autowired
    private ScanFichierService scanFichierService;

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private DemandeurService demandeurService;

    @Autowired
    private PasseportService passeportService;

    @Autowired
    private PieceService pieceService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private PieceDemandeService pieceDemandeService;

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
    private CarteResidentService carteResidentService;


    @Autowired
    private TypeVisaService typeVisaService;

    @Autowired
    private VisaService visaService;

    @Autowired
    private StatutVisaService statutVisaService;

    @GetMapping("/demande/choix_type_demande")
    public String choix_type_demande(Model model) {
        model.addAttribute("template", "demande/choix_type_demande");
        return "template";
    }

    @GetMapping("/demandes")
    public String list(@RequestParam(name = "reference", required = false) String reference,
            Model model) {
        List<DemandeEntity> demandes = demandeService.findAll();
        Map<Integer, String> referenceMap = buildReferenceMap(demandes);
        Map<Integer, String> statusMap = buildStatusMap(demandes);
        Map<Integer, Boolean> canBeScannedMap = new HashMap<>();
        statusMap.forEach((id, statut) -> {
            System.out.println("ID Demande: " + id + " | Statut: " + statut);
        });
        if (reference != null && !reference.isBlank()) {
            String trimmed = reference.trim().toUpperCase();
            demandes = demandes.stream()
                    .filter(d -> {
                        String ref = referenceMap.get(d.getId());
                        return ref != null && ref.contains(trimmed);
                    })
                    .collect(Collectors.toList());
        }
        for (DemandeEntity demande : demandes) {
            boolean canScan = demandeService.canBeScan(demande);
            canBeScannedMap.put(demande.getId(), canScan);
        }

        model.addAttribute("canBeScannedMap", canBeScannedMap);
        model.addAttribute("reference", reference);
        model.addAttribute("referenceMap", referenceMap);
        model.addAttribute("statusMap", statusMap);
        model.addAttribute("demandes", demandes);
        model.addAttribute("template", "demande/liste");
        model.addAttribute("api", apiUrl);
        return "template";
    }

    @PostMapping("/demande")
    @Transactional
    public String createDemande(@ModelAttribute DemandeEntity demandeEntity,
            @ModelAttribute DemandeurEntity demandeurEntity,
            @ModelAttribute PasseportEntity passeportEntity,
            @ModelAttribute VisaTransformableEntity visaTransformableEntity,
            @RequestParam(name = "idSituationFamiliale", required = false) Integer idSituationFamiliale,
            @RequestParam(name = "idNationaliteActuelle", required = false) Integer idNationaliteActuelle,
            BindingResult result,
            @RequestParam(value = "piecesIds", required = false) List<Long> piecesIds,
            @RequestParam(value = "provenance", required = false) String provenance,
            @RequestParam(value = "dateDebutVisa", required = false) String dateDebutVisaStr,
            @RequestParam(value = "dateFinVisa", required = false) String dateFinVisaStr,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        LocalDate currentDate = LocalDate.now();
        List<String> fieldErrors = new ArrayList<>();

        if (demandeurEntity == null) {
            fieldErrors.add("Informations personnelles invalides");
            redirectAttributes.addFlashAttribute("demandeEntity", demandeEntity);
            redirectAttributes.addFlashAttribute("demandeurEntity", demandeurEntity);
            redirectAttributes.addFlashAttribute("passeportEntity", passeportEntity);
            redirectAttributes.addFlashAttribute("visaTransformableEntity", visaTransformableEntity);
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            return "redirect:/demande/formulaire";
        }

        if (demandeurEntity.getNom() == null || demandeurEntity.getNom().trim().isEmpty()) {
            fieldErrors.add("Le nom est obligatoire");
        }
        if (demandeurEntity.getPrenom() == null || demandeurEntity.getPrenom().trim().isEmpty()) {
            fieldErrors.add("Le prénom est obligatoire");
        }
        if (demandeurEntity.getDateNaissance() == null) {
            fieldErrors.add("La date de naissance est obligatoire");
        }
        if (demandeurEntity.getLieuNaissance() == null || demandeurEntity.getLieuNaissance().trim().isEmpty()) {
            fieldErrors.add("Le lieu de naissance est obligatoire");
        }
        if (demandeurEntity.getTelephone() == null || demandeurEntity.getTelephone().trim().isEmpty()) {
            fieldErrors.add("Le numéro de téléphone est obligatoire");
        }
        if (demandeurEntity.getAdresse() == null || demandeurEntity.getAdresse().trim().isEmpty()) {
            fieldErrors.add("L'adresse est obligatoire");
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
            fieldErrors.add("Informations du passeport invalides");
        } else {
            if (passeportEntity.getNumeroPasseport() == null || passeportEntity.getNumeroPasseport().trim().isEmpty()) {
                fieldErrors.add("Le numéro de passeport est obligatoire");
            }
            if (passeportEntity.getDateDelivrance() == null) {
                fieldErrors.add("La date de délivrance du passeport est obligatoire");
            }
            if (passeportEntity.getDateExpiration() == null) {
                fieldErrors.add("La date d'expiration du passeport est obligatoire");
            }

            if (passeportEntity.getDateDelivrance() != null && passeportEntity.getDateExpiration() != null) {
                if (passeportEntity.getDateExpiration().isBefore(passeportEntity.getDateDelivrance())) {
                    fieldErrors.add("La date d'expiration du passeport doit être postérieure à la date de délivrance");
                }
                if (passeportEntity.getDateExpiration().isBefore(currentDate)) {
                    fieldErrors.add("Le passeport est expiré");
                }
            }

            passeportEntity.setDemandeur(demandeurEntity);
            demandeurEntity.setPasseports(List.of(passeportEntity));
        }
        if (!passeportEntity.isValid(currentDate)) {
            redirectAttributes.addFlashAttribute("error", "Passeport invalide : problème dans les dates de validité.");
            return "redirect:/demande/formulaire";
        }

        if (!demandeurEntity.isValid(currentDate)) {
            redirectAttributes.addFlashAttribute("error",
                    buildDemandeurValidationMessage(demandeurEntity, currentDate));
            return "redirect:/demande/formulaire";
        }

        if (visaTransformableEntity == null) {
            fieldErrors.add("Informations du visa transformable invalides");
        } else {
            if (visaTransformableEntity.getNumeroReference() == null
                    || visaTransformableEntity.getNumeroReference().trim().isEmpty()) {
                fieldErrors.add("Le numéro de référence du visa transformable est obligatoire");
            }

            if (visaTransformableEntity.getDateEntree() == null) {
                fieldErrors.add("La date d'entrée du visa transformable est obligatoire");
            }
            if (visaTransformableEntity.getDateSortie() == null) {
                fieldErrors.add("La date de sortie du visa transformable est obligatoire");
            }

            if (visaTransformableEntity.getDateEntree() != null && visaTransformableEntity.getDateSortie() != null) {
                if (visaTransformableEntity.getDateSortie().isBefore(visaTransformableEntity.getDateEntree())) {
                    fieldErrors.add("La date de sortie doit être postérieure à la date d'entrée");
                }
            }

            visaTransformableEntity.setDemandeur(demandeurEntity);
            visaTransformableEntity.setPasseport(passeportEntity);
        }

        // Valider la demande
        if (demandeEntity == null) {
            fieldErrors.add("Informations de la demande invalides");
        } else {
            if (demandeEntity.getTypeDemande() == null || demandeEntity.getTypeVisa() == null) {
                fieldErrors.add("Le type de demande est obligatoire");
            }

            if (demandeEntity.getTypeVisa() == null || demandeEntity.getTypeVisa() == null) {
                fieldErrors.add("Le type de visa est obligatoire");
            }

            demandeEntity.setVisaTransformable(visaTransformableEntity);
            demandeEntity.setDemandeur(demandeurEntity);
            demandeEntity.setDateDemande(currentDate);
        }
        if (demandeEntity.getTypeDemande() == null) {
            TypeDemandeEntity typeDemande = new TypeDemandeEntity();
            typeDemande.setId(1);
            demandeEntity.setTypeDemande(typeDemande);
        }
        demandeEntity.setVisaTransformable(visaTransformableEntity);
        demandeEntity.setDemandeur(demandeurEntity);
        demandeEntity.setDateDemande(currentDate);

        String numeroVisa1 = null;
        LocalDate dateDebutVisa = null;
        LocalDate dateFinVisa = null;
        
        if (request.getParameter("numeroVisa") != null && !request.getParameter("numeroVisa").isEmpty()) {
            numeroVisa1 = request.getParameter("numeroVisa");
            String visaPattern = "VISA-[A-Z0-9]{4}-[0-9]{6}";
            if (!numeroVisa1.matches(visaPattern)) {
                fieldErrors.add("Le numéro de visa doit être au format VISA-XXXX-XXXXXX");
            }
            if (dateDebutVisaStr != null && !dateDebutVisaStr.isEmpty()) {
                try {
                    dateDebutVisa = LocalDate.parse(dateDebutVisaStr);
                } catch (Exception e) {
                    fieldErrors.add("Format de date de debut de visa invalide");
                }
            } else {
                fieldErrors.add("La date de fin de validité du visa est obligatoire si un numéro de visa est fourni");
            }
            if (dateFinVisaStr != null && !dateFinVisaStr.isEmpty()) {
                try {
                    dateFinVisa = LocalDate.parse(dateFinVisaStr);
                    if (dateFinVisa.isBefore(currentDate)) {
                        fieldErrors.add("La date de fin de validité du visa doit être postérieure à aujourd'hui");
                    }
                    if (visaTransformableEntity != null && visaTransformableEntity.getDateEntree() != null) {
                        if (dateFinVisa.isBefore(visaTransformableEntity.getDateEntree())) {
                            fieldErrors.add("La date de fin de validité du visa doit être postérieure à la date d'entrée");
                        }
                    }
                } catch (Exception e) {
                    fieldErrors.add("Format de date de fin de visa invalide");
                }
            } else {
                fieldErrors.add("La date de fin de validité du visa est obligatoire si un numéro de visa est fourni");
            }
        }

        if (!fieldErrors.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Veuillez corriger les erreurs ci-dessous");
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);

            redirectAttributes.addFlashAttribute("demandeEntity", demandeEntity);
            redirectAttributes.addFlashAttribute("demandeurEntity", demandeurEntity);
            redirectAttributes.addFlashAttribute("passeportEntity", passeportEntity);
            redirectAttributes.addFlashAttribute("visaTransformableEntity", visaTransformableEntity);
            redirectAttributes.addFlashAttribute("prefillNom", demandeurEntity != null ? demandeurEntity.getNom() : "");
            redirectAttributes.addFlashAttribute("prefillPrenom",
                    demandeurEntity != null ? demandeurEntity.getPrenom() : "");
            redirectAttributes.addFlashAttribute("prefillDateNaissance",
                    demandeurEntity != null && demandeurEntity.getDateNaissance() != null
                            ? demandeurEntity.getDateNaissance().toString()
                            : "");
            redirectAttributes.addFlashAttribute("prefillLieuNaissance",
                    demandeurEntity != null ? demandeurEntity.getLieuNaissance() : "");
            redirectAttributes.addFlashAttribute("prefillTelephone",
                    demandeurEntity != null ? demandeurEntity.getTelephone() : "");
            redirectAttributes.addFlashAttribute("prefillAdresse",
                    demandeurEntity != null ? demandeurEntity.getAdresse() : "");
            redirectAttributes.addFlashAttribute("prefillEmail",
                    demandeurEntity != null ? demandeurEntity.getEmail() : "");
            redirectAttributes.addFlashAttribute("prefillSituationId", idSituationFamiliale);
            redirectAttributes.addFlashAttribute("prefillNationaliteActuelleId", idNationaliteActuelle);
            redirectAttributes.addFlashAttribute("prefillNumeroPasseport",
                    passeportEntity != null ? passeportEntity.getNumeroPasseport() : "");
            redirectAttributes.addFlashAttribute("prefillPaysDelivrance",
                    passeportEntity != null ? passeportEntity.getPaysDelivrance() : "");
            redirectAttributes.addFlashAttribute("prefillDateDelivrance",
                    passeportEntity != null && passeportEntity.getDateDelivrance() != null
                            ? passeportEntity.getDateDelivrance().toString()
                            : "");
            redirectAttributes.addFlashAttribute("prefillDateExpiration",
                    passeportEntity != null && passeportEntity.getDateExpiration() != null
                            ? passeportEntity.getDateExpiration().toString()
                            : "");
            redirectAttributes.addFlashAttribute("prefillNumeroReference",
                    visaTransformableEntity != null ? visaTransformableEntity.getNumeroReference() : "");
            redirectAttributes.addFlashAttribute("prefillDateEntree",
                    visaTransformableEntity != null && visaTransformableEntity.getDateEntree() != null
                            ? visaTransformableEntity.getDateEntree().toString()
                            : "");
            redirectAttributes.addFlashAttribute("prefillDateSortie",
                    visaTransformableEntity != null && visaTransformableEntity.getDateSortie() != null
                            ? visaTransformableEntity.getDateSortie().toString()
                            : "");
            redirectAttributes.addFlashAttribute("prefillTypeDemandeId",
                    demandeEntity != null && demandeEntity.getTypeDemande() != null
                            ? demandeEntity.getTypeDemande().getId()
                            : null);
            redirectAttributes.addFlashAttribute("prefillTypeVisaId",
                    demandeEntity != null && demandeEntity.getTypeVisa() != null ? demandeEntity.getTypeVisa().getId()
                            : null);
            redirectAttributes.addFlashAttribute("prefillNumeroVisa", numeroVisa1);
            redirectAttributes.addFlashAttribute("prefillDateDebutVisa", dateDebutVisaStr);
            redirectAttributes.addFlashAttribute("prefillDateFinVisa", dateFinVisaStr);
            redirectAttributes.addFlashAttribute("piecesIds", piecesIds);
            redirectAttributes.addFlashAttribute("provenance", provenance);

            return "redirect:/demande/formulaire";
        }
        // ========== 2. SAUVEGARDE (seulement si tout est valide) ==========

        try {
            // Sauvegarder le demandeur
            DemandeurEntity savedDemandeur = new DemandeurEntity();
            if (!demandeurService.emailExists(demandeurEntity.getEmail())) {
                savedDemandeur = demandeurService.save(demandeurEntity);
            } else {
                savedDemandeur = demandeurService.findTopByEmail(demandeurEntity.getEmail()).get();
            }

            // Sauvegarder le passeport (si non existant)
            PasseportEntity savedPasseport;
            Optional<PasseportEntity> existingPasseport = passeportService
                    .findByNumeroPasseport(passeportEntity.getNumeroPasseport());
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
            try{
                    String folderPath = servletContext.getRealPath("/images/qrcodes/");
                    File folder = new File(folderPath);

                    if (!folder.exists()) {
                        boolean created = folder.mkdirs();
                        if (created) {
                            System.out.println("Dossier créé avec succès : " + folderPath);
                        }
                    }  
                    String fileName = "qr_" + savedDemande.getId() + ".png";
                    String fullPath = folderPath + fileName;
                    String pathSave= "images/qrcodes/"+fileName;
                    String baseUrlReact = apiUrl; 
                    String urlFiche = baseUrlReact + savedDemande.getId();
                    Util.genererQRCode(urlFiche, fullPath);
                    savedDemande.setCheminQR(pathSave);
                    savedDemande = demandeService.save(savedDemande);

            
            }catch(Exception e){
                e.printStackTrace();

            }
            System.out.println("provenace: " + provenance);
            if (provenance != null && !provenance.equals("CLASSIQUE")) {
                statutDemandeEntity.setStatut(30);
            }
            
            statutDemandeService.save(statutDemandeEntity);

            String reference = buildReference(savedDemande);
            if (piecesIds != null) {
                for (Long id : piecesIds) {
                    System.out.println("ID de la pièce cochée : " + id);
                    PieceEntity piece = pieceService.findById(id.intValue());
                    if (piece != null) {
                        PieceDemandeEntity pieceDemande = new PieceDemandeEntity(piece, savedDemande);
                        pieceDemandeService.save(pieceDemande);
                    } else {
                        System.out.println("un piece null id:  " + id);
                    }

                }
            }
                System.out.println("Date fin Str: " + dateFinVisaStr);

            StatutVisaEntity statutVisa = new StatutVisaEntity();
            if (dateDebutVisaStr != null && !dateDebutVisaStr.isEmpty()) {
                dateDebutVisa = LocalDate.parse(dateDebutVisaStr);
                if (dateFinVisaStr != null && !dateFinVisaStr.isEmpty()) {
                    try {
                        dateFinVisa = LocalDate.parse(dateFinVisaStr);
                    } catch (Exception e) {

                    }
                }

                VisaEntity visa = generateVisa(savedDemande, savedPasseport, dateDebutVisa, dateFinVisa);
                System.out.println("Date début: " + dateDebutVisa);
                System.out.println("Date fin: " + dateFinVisa);
                VisaEntity savedVisa = visaService.save(visa);

                statutVisa.setVisa(savedVisa);
                statutVisa.setStatut(1); 
                statutVisa.setDateChangementStatut(currentDate);

                redirectAttributes.addFlashAttribute("generatedVisaNumber", numeroVisa1);
                redirectAttributes.addFlashAttribute("visaDateDebut", currentDate.toString());
                redirectAttributes.addFlashAttribute("visaDateFin", dateFinVisa.toString());
                redirectAttributes.addFlashAttribute("visaStatut", "Non valide");
                redirectAttributes.addFlashAttribute("visaReference", buildReference(savedDemande));
            }
            redirectAttributes.addFlashAttribute("successMessage",
                    "Votre demande N° " + reference + " a été enregistrée");

            if (provenance != null) {
                if (provenance.equals("TRANSFERT")) {
                    statutVisa.setStatut(30);
                    statutVisaService.save(statutVisa);
                    redirectAttributes.addAttribute("id", savedDemande.getId());
                    return "redirect:/duplicata/nouveau_passeport?duplicata=0";

                } else if (provenance.equals("DUPLICATA")) {
                    statutVisa.setStatut(30);
                    statutVisaService.save(statutVisa);
                    List<Integer> demandeurIds = new ArrayList<>();
                    demandeurIds.add(savedDemandeur.getId());

                    // Récupérer les informations du nouveau passeport
                    List<String> nouveauxNumPasseports = new ArrayList<>();
                    List<LocalDate> dateDelivrances = new ArrayList<>();
                    List<LocalDate> dateExpirations = new ArrayList<>();
                    List<String> paysEmetteurs = new ArrayList<>();

                    nouveauxNumPasseports.add(passeportEntity.getNumeroPasseport());
                    dateDelivrances.add(passeportEntity.getDateDelivrance());
                    dateExpirations.add(passeportEntity.getDateExpiration());
                    paysEmetteurs.add(passeportEntity.getPaysDelivrance());

                    // Ajout des attributs à redirectAttributes
                    redirectAttributes.addAttribute("id", savedDemande.getId());
                    redirectAttributes.addAttribute("duplicata", "1");
                    redirectAttributes.addAttribute("demandeurId", demandeurIds);
                    redirectAttributes.addAttribute("nouveauNumPasseport", nouveauxNumPasseports);
                    redirectAttributes.addAttribute("dateDelivrance", dateDelivrances);
                    redirectAttributes.addAttribute("dateExpiration", dateExpirations);
                    redirectAttributes.addAttribute("paysEmetteur", paysEmetteurs);

                    return "redirect:/duplicata/resume_duplicata";
                }
            }

            if (request.getParameter("numeroVisa") != null && !request.getParameter("numeroVisa").isEmpty()) {
                statutVisaService.save(statutVisa);

            }

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur technique : " + e.getMessage());
            redirectAttributes.addFlashAttribute("demandeEntity", demandeEntity);
            redirectAttributes.addFlashAttribute("demandeurEntity", demandeurEntity);
            redirectAttributes.addFlashAttribute("passeportEntity", passeportEntity);
            redirectAttributes.addFlashAttribute("visaTransformableEntity", visaTransformableEntity);
            redirectAttributes.addFlashAttribute("fieldErrors", List.of("Erreur technique : " + e.getMessage()));
            return "redirect:/demande/formulaire";
        }

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
        PasseportEntity passeport = demande.getVisaTransformable() != null
                ? demande.getVisaTransformable().getPasseport()
                : null;
        VisaTransformableEntity visaTransformable = demande.getVisaTransformable();
        List<PieceDemandeEntity> pieceDemandes = pieceDemandeService.findByIdDemande(id);

        model.addAttribute("pieceDemandes", pieceDemandes);
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

        // Charger les informations du visa généré s'il existe
        if (demande.getVisas() != null && !demande.getVisas().isEmpty()) {
            VisaEntity visa = demande.getVisas().get(0);
            model.addAttribute("prefillNumeroVisa", visa.getReference());
            model.addAttribute("prefillDateDebutVisa", visa.getDateDebut());
            model.addAttribute("prefillDateFinVisa", visa.getDateFin());
        }

        if (demande.getTypeDemande() != null) {
            model.addAttribute("prefillTypeDemandeId", 2);
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
            @RequestParam(value = "piecesIds", required = false) List<Long> piecesIds,
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
            Optional<PasseportEntity> duplicatePasseport = passeportService
                    .findByNumeroPasseport(passeportEntity.getNumeroPasseport());
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
            pieceDemandeService.deleteByDemandeId(existingDemande.getId());

            if (piecesIds != null) {
                for (Long ids : piecesIds) {
                    PieceEntity piece = pieceService.findById(ids.intValue());
                    pieceDemandeService.save(new PieceDemandeEntity(piece, existingDemande));
                }
            }
            redirectAttributes.addFlashAttribute("successMessage", "Demande modifiée avec succès");

        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Erreur lors de la modification. Veuillez verifier les informations saisies.";
            
            if (e.getMessage() != null) {
                if (e.getMessage().contains("Duplicate entry")) {
                    errorMessage = "Erreur : Ce numéro de référence existe déjà. Veuillez utiliser une valeur unique.";
                } else if (e.getMessage().contains("constraint")) {
                    errorMessage = "Erreur : Violation de contrainte de la base de données. Veuillez verifier les informations.";
                } else {
                    errorMessage = "Erreur : " + e.getMessage();
                }
            }
            
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("demandeEntity", demandeEntity);
            return "redirect:/demande/modifier/" + id;
        }

        return "redirect:/demande/liste";
    }

    @GetMapping("/demande/create")
    public String showCreateForm(Model model, @RequestParam(value = "provenance", required = false) String provenance) {
        populateFormDefaults(model);
        populateFormOptions(model);
        model.addAttribute("formTitle", "Nouvelle demande");
        model.addAttribute("formAction", "/demande");
        model.addAttribute("template", "demande/formulaire");
        model.addAttribute("demande", new DemandeEntity());
        model.addAttribute("provenance", provenance);
        model.addAttribute("formTitle", "Nouvelle demande avec génération de visa");
        model.addAttribute("formAction", "/demande");
        model.addAttribute("showVisaSection", true);
        model.addAttribute("template", "demande/modification");
        model.addAttribute("prefillNumeroVisa",
                "VISA-MDGR-" + LocalDate.now().getYear() + "23");
        model.addAttribute("prefillDateDebutVisa", LocalDate.now().minusYears(1).toString());
        model.addAttribute("prefillDateFinVisa", LocalDate.now().plusYears(1).toString());

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
        model.addAttribute("prefillTypeVisaId", 2);
        model.addAttribute("prefillTypeDemandeId", 2);
    }

    private void populateFormOptions(Model model) {
        model.addAttribute("situations", situationFamilialeService.findAll());
        model.addAttribute("nationalites", nationaliteService.findAll());
        model.addAttribute("typeDemandes", typeDemandeService.findAll());
        model.addAttribute("typeVisas", typeVisaService.findAll());
        model.addAttribute("listpiece", pieceService.findAll());
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
                this::buildReference));
    }

    private Map<Integer, String> buildStatusMap(List<DemandeEntity> demandes) {
        return demandes.stream().collect(Collectors.toMap(
                DemandeEntity::getId,
                demande -> statutDemandeService.findLatestByDemandeId(demande.getId())
                        .<String>map(statut -> statut.getLibelleStatut())
                        .orElse("Cree")));
    }

    private VisaEntity generateVisa(DemandeEntity demande, PasseportEntity passeport,
             LocalDate dateDebutVisa, LocalDate dateFinVisa) {
        VisaEntity visa = new VisaEntity();
        visa.setDemande(demande);
        visa.setPasseport(passeport);
        visa.setReference(generateVisaReference());
        visa.setDateDebut(dateDebutVisa);
        visa.setDateFin(dateFinVisa);

        return visa;
    }

    public String generateVisaReference() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-ssSSS");
        
        return "VISA-" + now.format(formatter);
    }

   


    @GetMapping("/demandeDetails/{idDemande}")
    public ResponseEntity<?> getDemandeDetails(@PathVariable Long idDemande) {
        try {
            DemandeEntity demande = demandeService.findById(idDemande.intValue());
            if (demande == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demande non trouvée");
            }
            Map<String, Object> response = new HashMap<>();
            // response.put("demande", demande);
            response.put("idDemande", demande.getId());
            response.put("qrChemin",demande.getCheminQR());
            response.put("EtatCivil", demande.getDemandeur());
            response.put("passeport", demande.getVisaTransformable() != null ? demande.getVisaTransformable().getPasseport() : null);
            response.put("visaTransformable", demande.getVisaTransformable());
            response.put("typeDemande", demande.getTypeDemande());
            response.put("typeVisa", demande.getTypeVisa());
            List<StatutDemandeEntity> statuts = demande.getStatuts();
            response.put("Status",statuts.get(statuts.size() - 1).getLibelleStatut());
            if(statuts.get(statuts.size() - 1).getLibelleStatut()=="Approuvé"){
                List<VisaEntity> visas=visaService.findByDemandeId(demande.getId());
                response.put("visa",visas.get(visas.size() - 1));
                List<CarteResidentEntity> cartes=carteResidentService.findByDemandeId(demande.getId());
                response.put("carteResident",cartes.get(cartes.size() - 1));
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la récupération des détails de la demande: " + e.getMessage());
        }
    }
    @GetMapping("/demandeDetails/HistoStatut/{idDemande}")
    public ResponseEntity<?> getHistoStatut(@PathVariable int idDemande) {
        DemandeEntity demandeOpt = demandeService.findById(idDemande);
        if (demandeOpt==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demande introuvable");
        }

        List<StatutDemandeEntity> historique = demandeOpt.getStatuts();

        if (historique == null || historique.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun historique pour cette demande");
        }
        return ResponseEntity.ok(historique);
    }
    @GetMapping("/demandeDetails/DetailsFichier/{idDemande}")
    public ResponseEntity<?> getDetailsFichier(@PathVariable Long idDemande) {
        List<ScanFichierEntity> fichiers = scanFichierService.getUploadsParDemande(idDemande);

        if (fichiers == null || fichiers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("Aucun fichier uploadé pour cette demande.");
        }

        return ResponseEntity.ok(fichiers);
    }

}