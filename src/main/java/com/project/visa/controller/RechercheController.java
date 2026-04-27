package com.project.visa.controller;

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
import com.project.visa.repository.CarteResidentRepository;
import com.project.visa.repository.VisaRepository;
import com.project.visa.repository.DemandeRepository;
import com.project.visa.entity.CarteResidentEntity;
import com.project.visa.entity.DemandeEntity;
import com.project.visa.entity.VisaEntity;


@Controller
public class RechercheController {
    @Autowired
    private CarteResidentRepository carteResidentRepository;
    @Autowired
    private VisaRepository visaRepository;
    @Autowired
    private DemandeRepository demandeRepository;

    @GetMapping("/duplicata/recherche_numero")
    public String recherche_numero(Model model) {
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
            if (carte != null) demandeTrouvee = carte.getDemande();
        } else if (!numVisa.isEmpty()) {
            VisaEntity visa = visaRepository.findByReference(numVisa); 
            if (visa != null) {
                demandeTrouvee = visa.getDemande();
                // On passe le visa pour l'afficher sur le résumé
                redirectAttributes.addFlashAttribute("visa", visa);
            }
        }

        if (demandeTrouvee != null) {
            // Indispensable pour la JSP
            redirectAttributes.addFlashAttribute("demande", demandeTrouvee);
            redirectAttributes.addFlashAttribute("transfer", transfer);
            redirectAttributes.addFlashAttribute("duplicata", duplicata);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Aucun dossier trouvé.");
        }
        return "redirect:/duplicata/recherche_numero";
    }

    @GetMapping("/duplicata/resume_duplicata")
    public String resumeDuplicata(@RequestParam(value = "id", required = false) Integer demandeId, Model model) {
        if (demandeId == null && !model.containsAttribute("demande")) {
            return "redirect:/duplicata/recherche_numero";
        }
        // Utilisation de .orElse(null) pour l'Optional
        if (demandeId != null && !model.containsAttribute("demande")) {
            DemandeEntity demande = demandeRepository.findById(demandeId);
            if (demande != null && !demande.getVisas().isEmpty()) {
                model.addAttribute("visa", demande.getVisas().get(0));
            }
        }
        model.addAttribute("template", "demande/resume_duplicata");
        return "template";
    }

    @GetMapping("/duplicata/nouveau_passeport")
    public String nouveau_passeport(@RequestParam(value = "id", required = false) Integer demandeId, 
                                   @RequestParam(value = "duplicata", defaultValue = "0") String duplicata,
                                   Model model) {
        if (demandeId == null && !model.containsAttribute("demande")) {
            return "redirect:/duplicata/recherche_numero";
        }
        if (demandeId != null && !model.containsAttribute("demande")) {
            DemandeEntity demande = demandeRepository.findById(demandeId);
            model.addAttribute("demande", demande);
            if (demande != null && !demande.getVisas().isEmpty()) {
                model.addAttribute("visa", demande.getVisas().get(0));
            }
        }
        // On repasse le flag duplicata pour la suite du flux
        model.addAttribute("duplicata", duplicata);
        model.addAttribute("template", "demande/nouveau_passeport");
        return "template";
    }

    @GetMapping("/transfert/resume_transfert")
    public String resumeTransfert(@RequestParam("demandeId") Integer demandeId, 
                                  @RequestParam("nouveauNumPasseport") String nouveauNum, 
                                  @RequestParam(value = "duplicata", defaultValue = "0") String duplicata,
                                  Model model) {
        DemandeEntity demande = demandeRepository.findById(demandeId);
        model.addAttribute("demande", demande);
        model.addAttribute("nouveauNumPasseport", nouveauNum);
        model.addAttribute("duplicata", duplicata);
        
        if (demande != null && !demande.getVisas().isEmpty()) {
            model.addAttribute("visa", demande.getVisas().get(0));
        }
        model.addAttribute("template", "demande/resume_transfert");
        return "template";
    }

    @PostMapping("/transfert/accepter")
    public String accepterTransfert(
            @RequestParam("demandeId") Integer demandeId, 
            @RequestParam("nouveauNumPasseport") String nouveauNum,
            @RequestParam(value = "duplicata", defaultValue = "0") String duplicata,
            RedirectAttributes ra) {

        // Ici : Logique de sauvegarde en base (nouveau passeport)
        
        if ("1".equals(duplicata)) {
            ra.addFlashAttribute("successMessage", "Transfert enregistré. Procédez au duplicata.");
            return "redirect:/duplicata/resume_duplicata?id=" + demandeId;
        }

        ra.addFlashAttribute("successMessage", "Transfert terminé avec succès.");
        return "redirect:/demande/liste";
    }
}