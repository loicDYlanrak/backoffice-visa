package com.project.visa.controller;

import com.project.visa.entity.DemandeEntity;
import com.project.visa.entity.PhotoSignatureDemandeEntity;
import com.project.visa.entity.StatutDemandeEntity;
import com.project.visa.service.DemandeService;
import com.project.visa.service.PhotoSignatureDemandeService;
import com.project.visa.service.StatutDemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PhotoSignatureController {

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private PhotoSignatureDemandeService photoSignatureService;

    @Autowired
    private StatutDemandeService statutDemandeService;

    @GetMapping("/demande/photo-signature/{id}")
    public String showPhotoSignaturePage(@PathVariable int id, Model model, RedirectAttributes ra) {
        DemandeEntity demande = demandeService.findById(id);
        if (demande == null) {
            ra.addFlashAttribute("errorMessage", "Demande non trouvée");
            return "redirect:/demande/liste";
        }

        // Vérifier si photo et signature existent déjà
        PhotoSignatureDemandeEntity existing = photoSignatureService.findLatestByDemandeId(id);

        model.addAttribute("demande", demande);
        model.addAttribute("existingPhoto", existing != null ? existing.getPhotoUrl() : null);
        model.addAttribute("existingSignature", existing != null ? existing.getSignatureUrl() : null);
        model.addAttribute("template", "demande/photo_signature");

        return "template";
    }

    @PostMapping("/demande/save-photo-signature")
    public String savePhotoSignature(
            @RequestParam("demandeId") int demandeId,
            @RequestParam(value = "photoData", required = false) String photoData,
            @RequestParam(value = "signatureData", required = false) String signatureData,
            RedirectAttributes ra) {

        DemandeEntity demande = demandeService.findById(demandeId);
        if (demande == null) {
            ra.addFlashAttribute("errorMessage", "Demande non trouvée");
            return "redirect:/demande/liste";
        }

        // Supprimer l'ancienne entrée si elle existe
        if (photoSignatureService.existsByDemandeId(demandeId)) {
            photoSignatureService.deleteByDemandeId(demandeId);
        }

        // Enregistrer la nouvelle photo et signature
        PhotoSignatureDemandeEntity saved = photoSignatureService.enregistrerPhotoSignature(
                demandeId, photoData, signatureData, demande);

        if (saved == null) {
            ra.addFlashAttribute("errorMessage", "Erreur lors de l'enregistrement de la photo/signature");
            return "redirect:/demande/photo-signature/" + demandeId;
        }

        // Mettre à jour le statut de la demande si nécessaire
        StatutDemandeEntity statut = new StatutDemandeEntity();
        statut.setDemande(demande);
        statut.setDateChangementStatut(LocalDate.now());
        statut.setStatut(15); // Statut "Photo et signature validées"
        statutDemandeService.save(statut);

        ra.addFlashAttribute("successMessage", "Photo et signature enregistrées avec succès");
        return "redirect:/demande/liste";
    }

    @GetMapping("/demande/check-photo-signature/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkPhotoSignature(@PathVariable int id) {
        Map<String, Boolean> response = new HashMap<>();
        boolean hasPhotoSignature = demandeService.hasValidPhotoAndSignature(id);
        response.put("hasPhotoSignature", hasPhotoSignature);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/demande/reset-photo-signature/{id}")
    public String resetPhotoSignature(@PathVariable int id, RedirectAttributes ra) {
        photoSignatureService.deleteByDemandeId(id);
        ra.addFlashAttribute("successMessage", "Photo et signature réinitialisées. Vous pouvez recommencer.");
        return "redirect:/demande/photo-signature/" + id;
    }

    @GetMapping("/uploads/{demandeId}/{filename}")
    @ResponseBody
    public ResponseEntity<byte[]> getUploadedFile(
            @PathVariable int demandeId,
            @PathVariable String filename) {
        try {
            String baseUploadPath = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
            String filePath = baseUploadPath + "demande_" + demandeId + File.separator + filename;
            // Normaliser le chemin (corrige les slashs)
            Path path = Paths.get(filePath).normalize();

            if (!Files.exists(path)) {
                return ResponseEntity.notFound().build();
            }

            byte[] fileContent = Files.readAllBytes(path);

            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "image/png";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(fileContent);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}