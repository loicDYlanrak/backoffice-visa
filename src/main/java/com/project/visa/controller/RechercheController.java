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
    public String recherche_numero( Model model){
         model.addAttribute("template", "demande/recherche_numero");
        return "template";
    }
    @PostMapping("/duplicata/rechercher")
    public String recherche_numero(
            @RequestParam(value = "transfer", required = false, defaultValue = "0") String transfer,
            @RequestParam("numCarte") String numCarte, 
            @RequestParam("numVisa") String numVisa,
            RedirectAttributes redirectAttributes) { 

                redirectAttributes.addAttribute("transfer",transfer);
        if (!numCarte.isEmpty()) {
            CarteResidentEntity carte = carteResidentRepository.findByReference(numCarte);
            if (carte != null) {
                DemandeEntity demande = carte.getDemande();
                demande.getDateDemande();
                redirectAttributes.addFlashAttribute("demande",demande);
                redirectAttributes.addFlashAttribute("carte", carte);
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Aucune carte trouvée avec la référence : " + numCarte);
            }
            return "redirect:/duplicata/recherche_numero";
        }

        if (!numVisa.isEmpty()) {
            VisaEntity visa = visaRepository.findByReference(numVisa); 
            if (visa != null) {
                DemandeEntity demande = visa.getDemande();
                demande.getDateDemande();
                redirectAttributes.addFlashAttribute("demande", demande);
                redirectAttributes.addFlashAttribute("visa", visa);
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Aucun visa trouvé avec la référence : " + numVisa);
            }
            return "redirect:/duplicata/recherche_numero";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "Veuillez saisir un numéro.");
        return "redirect:/duplicata/recherche_numero";

}
@GetMapping("/duplicata/resume_duplicata")
public String resumeDuplicata(@RequestParam(value = "id", required = false) Integer demandeId, Model model) {
    
    if (demandeId == null && !model.containsAttribute("demande")) {
        return "redirect:/duplicata/recherche_numero";
    }

    // Si on a l'ID mais pas l'objet complet (cas d'un rafraîchissement de page)
    if (demandeId != null && !model.containsAttribute("demande")) {
        
        DemandeEntity demande = demandeRepository.findById(demandeId);
        model.addAttribute("demande", demande);
        if (demande.getVisas() != null && !demande.getVisas().isEmpty()) {
                VisaEntity visa = demande.getVisas().get(0);
                model.addAttribute("visa", visa);
            }
    }
    

    model.addAttribute("template", "demande/resume_duplicata");
    return "template";
}
@GetMapping("/duplicata/nouveau_passeport")
public String nouveau_passeport(@RequestParam(value = "id", required = false) Integer demandeId, Model model) {
    
    if (demandeId == null && !model.containsAttribute("demande")) {
        return "redirect:/duplicata/recherche_numero";
    }

    // Si on a l'ID mais pas l'objet complet (cas d'un rafraîchissement de page)
    if (demandeId != null && !model.containsAttribute("demande")) {
        
        DemandeEntity demande = demandeRepository.findById(demandeId);
        model.addAttribute("demande", demande);
        if (demande.getVisas() != null && !demande.getVisas().isEmpty()) {
                VisaEntity visa = demande.getVisas().get(0);
                model.addAttribute("visa", visa);
            }
    }
    

    model.addAttribute("template", "demande/nouveau_passeport");
    return "template";
}
@GetMapping("/transfert/resume_transfert")
public String resumeTransfert(@RequestParam("demandeId") Integer demandeId, 
                              @RequestParam("nouveauNumPasseport") String nouveauNum, 
                              Model model) {
    DemandeEntity demande = demandeRepository.findById(demandeId);
    model.addAttribute("demande", demande);
    model.addAttribute("nouveauNumPasseport", nouveauNum);
    
    if (demande != null && !demande.getVisas().isEmpty()) {
        model.addAttribute("visa", demande.getVisas().get(0));
    }
    
    model.addAttribute("template", "demande/resume_transfert");
    return "template";
}
@PostMapping("/transfert/accepter")
public String accepterTransfert(@RequestParam("demandeId") Integer demandeId, 
                                @RequestParam("nouveauNumPasseport") String nouveauNum,
                                RedirectAttributes ra) {
    // Logique métier ici : 
    // - Créer le nouveau passeport en base
    // - Mettre à jour la demande ou générer le nouveau visa
    
    ra.addFlashAttribute("successMessage", "Demande terminée avec succès. Le transfert a été enregistré.");
    return "redirect:/demande/liste"; // Redirection vers votre liste
}
}
