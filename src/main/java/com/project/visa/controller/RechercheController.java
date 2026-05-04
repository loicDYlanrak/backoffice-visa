package com.project.visa.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.visa.entity.CarteResidentEntity;
import com.project.visa.entity.DemandeEntity;
import com.project.visa.entity.DemandeurEntity;
import com.project.visa.entity.PasseportEntity;
import com.project.visa.entity.StatutDemandeEntity;
import com.project.visa.entity.VisaEntity;
import com.project.visa.repository.CarteResidentRepository;
import com.project.visa.repository.DemandeRepository;
import com.project.visa.repository.PasseportRepository;
import com.project.visa.repository.StatutDemandeRepository;
import com.project.visa.repository.TypeDemandeRepository;
import com.project.visa.repository.VisaRepository;
@Controller
public class RechercheController {
    @Autowired
    private CarteResidentRepository carteResidentRepository;
    @Autowired
    private VisaRepository visaRepository;
    @Autowired
    private DemandeRepository demandeRepository;
    @Autowired
    private TypeDemandeRepository typeDemandeRepository;
    @Autowired
    private StatutDemandeRepository statutDemandeRepository;
    @Autowired
    private PasseportRepository passeportRepository;


    @GetMapping("/duplicata/recherche_numero")
    public String recherche_numero(
            @RequestParam(value = "duplicata", required = false) String duplicata,
            @RequestParam(value = "transfer", required = false) String transfer,
            Model model) {

        model.addAttribute("finalDuplicata", duplicata);
        model.addAttribute("finalTransfer", transfer);

        String titre = "Recherche";
        if ("1".equals(duplicata) && !"2".equals(transfer)) {
            titre = "Demande de Duplicata";
        } else if ("1".equals(transfer)) {
            titre = "Demande de Transfert";
        } else {
            titre = "Demande de transfert de visa et duplicata";
        }
        model.addAttribute("pageTitle", titre);

        model.addAttribute("template", "demande/recherche_numero");
        return "template";
    }

    @PostMapping("/duplicata/rechercher")
    public String recherche_numero(
            @RequestParam(value = "transfer", required = false, defaultValue = "0") String transfer,
            @RequestParam(value = "duplicata", required = false, defaultValue = "0") String duplicata,
            @RequestParam("numCarte") String numCarte,
            @RequestParam("numVisa") String numVisa,
            RedirectAttributes redirectAttributes) {

        DemandeEntity demandeTrouvee = null;

        if (!numCarte.isEmpty()) {
            CarteResidentEntity carte = carteResidentRepository.findByReference(numCarte);
            if (carte != null)
                demandeTrouvee = carte.getDemande();
        } else if (!numVisa.isEmpty()) {
            VisaEntity visa = visaRepository.findByReference(numVisa);
            if (visa != null) {
                demandeTrouvee = visa.getDemande();
                redirectAttributes.addFlashAttribute("visa", visa);
            }
        }

        if (demandeTrouvee != null) {
            demandeTrouvee.getTypeVisa().getLibelle();
            redirectAttributes.addFlashAttribute("demande", demandeTrouvee);
            redirectAttributes.addFlashAttribute("transfer", transfer);
            redirectAttributes.addFlashAttribute("duplicata", duplicata);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Aucun dossier trouvé.");
        }
        return "redirect:/duplicata/recherche_numero?transfer=" + transfer + "&duplicata=" + duplicata;
    }

    @GetMapping("/duplicata/resume_duplicata")
    public String resumeDuplicata(@RequestParam(value = "id", required = false) Integer demandeId, Model model) {
        System.out.println("ID de la demande reçue : " + demandeId);
        if (demandeId == null && !model.containsAttribute("demande")) {
            return "redirect:/duplicata/recherche_numero";
        }
        if (demandeId != null && !model.containsAttribute("demande")) {
            System.out.println("Recherche de la demande avec ID : " + demandeId);

            DemandeEntity demande = demandeRepository.findById(demandeId);
            System.out.println("Demande trouvée : " + (demande != null ? demande.getId() : "Aucune"));
            model.addAttribute("demande", demande);
            if (demande != null && !demande.getVisas().isEmpty()) {
                model.addAttribute("visa", demande.getVisas().get(0));
            }
        }
        model.addAttribute("template", "demande/resume_duplicata");
        return "template";
    }

    @PostMapping("/duplicata/valider")
    public String validerDuplicata(@RequestParam("demandeId") Integer demandeId,
            RedirectAttributes redirectAttributes) {
        DemandeEntity demande = demandeRepository.findById(demandeId);
        if (demande == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Demande introuvable.");
            return "redirect:/duplicata/recherche_numero";
        }
        DemandeEntity demandeDuplicata = new DemandeEntity();
        demandeDuplicata.setVisaTransformable(demande.getVisaTransformable());
        demandeDuplicata.setDateDemande(LocalDate.now());
        demandeDuplicata.setDemandeur(demande.getDemandeur());
        demandeDuplicata.setTypeVisa(demande.getTypeVisa());
        demandeDuplicata.setTypeDemande(typeDemandeRepository.findByLibelle("duplicata"));
        demandeDuplicata.setDateTraitement(LocalDate.now());
        StatutDemandeEntity status = new StatutDemandeEntity();
        status.setDemande(demandeDuplicata);
        status.setStatut(1);
        status.setDateChangementStatut(LocalDate.now());
        try {
            demandeRepository.save(demandeDuplicata);
            try {
                statutDemandeRepository.save(status);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Erreur lors de la mise à jour du statut : " + e.getMessage());
                return "redirect:/duplicata/recherche_numero";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de la création du duplicata : " + e.getMessage());
            return "redirect:/duplicata/recherche_numero";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Duplicata validé avec succès.");
        return "redirect:/demande/liste";
    }

    @GetMapping("/duplicata/nouveau_passeport")
    public String nouveau_passeport(@RequestParam(value = "id", required = false) Integer demandeId,
            @RequestParam(value = "duplicata", defaultValue = "0") String duplicata,
            Model model) {
        if (demandeId == null && !model.containsAttribute("demande")) {
            return "redirect:/duplicata/recherche_numero";
        }
         DemandeEntity demande =new DemandeEntity();
        if (demandeId != null && !model.containsAttribute("demande")) {
         demande = demandeRepository.findById(demandeId);
            model.addAttribute("demande", demande);
            if (demande != null && !demande.getVisas().isEmpty()) {
                model.addAttribute("visa", demande.getVisas().get(0));
            }
        }

        List<DemandeurEntity> demandeur = new ArrayList<>();
        
        demandeur.add(demande.getDemandeur());
        // System.out.println("Nombre de demandeurs trouvés : " + demandeur.size());
        
        model.addAttribute("demandeurs", demandeur);
        model.addAttribute("duplicata", duplicata);
        model.addAttribute("prefillNumeroPasseport", "P1234568");
        LocalDate today = LocalDate.now();

        model.addAttribute("prefillDateDelivrance", today.minusYears(2).toString());
        model.addAttribute("prefillDateExpiration", "2030-12-31");
        model.addAttribute("prefillPaysDelivrance", "Madagascar");

        model.addAttribute("template", "demande/nouveau_passeport");
        return "template";
    }

    @GetMapping("/transfert/resume_transfert")
    public String resumeTransfert(@RequestParam("demandeId") Integer demandeId,
            @RequestParam(value = "duplicata", defaultValue = "0") String duplicata,
            @RequestParam("demandeurId") List<Integer> demandeurIds,
            @RequestParam("nouveauNumPasseport") List<String> nouveauxNumPasseports,
            @RequestParam("dateDelivrance") List<LocalDate> dateDelivrances,
            @RequestParam("dateExpiration") List<LocalDate> dateExpirations,
            @RequestParam("paysEmetteur") List<String> paysEmetteurs,
            Model model) {
        DemandeEntity demande = demandeRepository.findById(demandeId);
        demande.getStatuts().size();
        model.addAttribute("demande", demande);
        model.addAttribute("duplicata", duplicata);

        model.addAttribute("demandeurIds", demandeurIds);
        model.addAttribute("nouveauxNumPasseports", nouveauxNumPasseports);
        model.addAttribute("dateDelivrances", dateDelivrances);
        model.addAttribute("dateExpirations", dateExpirations);
        model.addAttribute("paysEmetteurs", paysEmetteurs);

        String libelleStatut = StatutDemandeEntity
                .getLibelleStatut(demande.getStatuts().get(demande.getStatuts().size() - 1).getStatut());
        model.addAttribute("libelleStatut", libelleStatut);

        if (demande != null && !demande.getVisas().isEmpty()) {
            model.addAttribute("visa", demande.getVisas().get(0));
        }

        model.addAttribute("template", "demande/resume_transfert");
        return "template";
    }

    @PostMapping("/transfert/accepter")
    public String accepterTransfert(
            @RequestParam("demandeId") Integer demandeId,
            @RequestParam(value = "duplicata", defaultValue = "0") String duplicata,
            @RequestParam("nouveauNumPasseport") List<String> nouveauxNumPasseports,
            @RequestParam("dateDelivrance") List<LocalDate> dateDelivrances,
            @RequestParam("dateExpiration") List<LocalDate> dateExpirations,
            @RequestParam("paysEmetteur") List<String> paysEmetteurs,
            RedirectAttributes ra) {

        DemandeEntity demande = demandeRepository.findById(demandeId);
        DemandeEntity demandeTransfert = new DemandeEntity();
        demandeTransfert.setVisaTransformable(demande.getVisaTransformable());
        demandeTransfert.setVisaTransformable(demande.getVisaTransformable());
        demandeTransfert.setDateDemande(LocalDate.now());
        demandeTransfert.setDemandeur(demande.getDemandeur());
        demandeTransfert.setTypeVisa(demande.getTypeVisa());
        demandeTransfert.setTypeDemande(typeDemandeRepository.findByLibelle("transfert de visa"));
        demandeTransfert.setDateTraitement(LocalDate.now());
        StatutDemandeEntity status = new StatutDemandeEntity();
        status.setDemande(demandeTransfert);
        status.setStatut(1);
        status.setDateChangementStatut(LocalDate.now());

        PasseportEntity nouveauPasseport = new PasseportEntity();
        for (int i = 0; i < nouveauxNumPasseports.size(); i++) {
            nouveauPasseport.setNumeroPasseport(nouveauxNumPasseports.get(i));
            nouveauPasseport.setDateDelivrance(dateDelivrances.get(i));
            nouveauPasseport.setDateExpiration(dateExpirations.get(i));
            nouveauPasseport.setPaysDelivrance(paysEmetteurs.get(i));
            nouveauPasseport.setDemandeur(demande.getDemandeur());

        }
       PasseportEntity passeportFinal;

        Optional<PasseportEntity> passeportExistant = passeportRepository.findByNumeroPasseport(nouveauPasseport.getNumeroPasseport());
       if (passeportExistant.isPresent()) {
            passeportFinal = passeportExistant.get();
        } else {
            passeportFinal = passeportRepository.saveAndFlush(nouveauPasseport);
        }

VisaEntity visa = demande.getVisas().get(0);
visa.setPasseport(passeportFinal);
        // VisaEntity visa = demande.getVisas().get(0);
        // visa.setPasseport(nouveauPasseport);

        try {
            demandeRepository.save(demandeTransfert);
            try {
                statutDemandeRepository.save(status);
                try {
                    if(!passeportRepository.existsByNumeroPasseport(nouveauPasseport.getNumeroPasseport())){
                        passeportRepository.save(nouveauPasseport);
                    }
                    try {
                        visaRepository.save(visa);
                    } catch (Exception e) {
                        ra.addFlashAttribute("errorMessage",
                                "Erreur lors de la mise à jour du visa : " + e.getMessage());
                        return "redirect:/duplicata/recherche_numero";
                    }
                } catch (Exception e) {
                    ra.addFlashAttribute("errorMessage",
                            "Erreur lors de la création du passeport : " + e.getMessage());
                    return "redirect:/duplicata/recherche_numero";
                }
            } catch (Exception e) {
                ra.addFlashAttribute("errorMessage",
                        "Erreur lors de la mise à jour du statut : " + e.getMessage());
                return "redirect:/duplicata/recherche_numero";
            }
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("errorMessage",
                    "Erreur lors de la création du transfert : " + e.getMessage());
            return "redirect:/duplicata/recherche_numero";
        }

        if ("1".equals(duplicata)) {
            ra.addFlashAttribute("successMessage", "Transfert enregistré. Procédez au duplicata.");
            return "redirect:/duplicata/resume_duplicata?id=" + demandeId;
        }

        ra.addFlashAttribute("successMessage", "Transfert terminé avec succès.");
        return "redirect:/demande/liste";
    }
    @GetMapping("/demandesRecherche")
    @ResponseBody
    public ResponseEntity<Set<DemandeEntity>> recherche_demande(@RequestParam(value = "numeroDemande", required = false) Integer numeroDemande,
                                    @RequestParam(value = "numeroPasseport", required = false) String numeroPasseport
                                     ){
        
       if ((numeroDemande == null ) && 
            (numeroPasseport == null || numeroPasseport.trim().isEmpty())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LinkedHashSet<>());
            
         }
        Set<DemandeEntity> demandes=new LinkedHashSet<>();
         if(numeroDemande!=null){

            DemandeEntity demande=demandeRepository.findById(numeroDemande);
            if(demande!=null){
                demandes.add(demande);
                 List<DemandeEntity> autreDemandes= demandeRepository.findByDemandeurId(demande.getDemandeur().getId());
                  autreDemandes.removeIf(d -> d.getId() == numeroDemande);
            if(!autreDemandes.isEmpty()){
                demandes.addAll(autreDemandes);
            }
            }
             else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LinkedHashSet<>());
            }
           
           
            
         }
         if(numeroPasseport!=null&&!numeroPasseport.isEmpty()){
            List<DemandeEntity> autreDemandes=demandeRepository.rechercherParNumeroPasseport(numeroPasseport);
            if(!autreDemandes.isEmpty()){
                demandes.addAll(autreDemandes);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LinkedHashSet<>());
            }
         }
        return ResponseEntity.ok(demandes);
    }
}