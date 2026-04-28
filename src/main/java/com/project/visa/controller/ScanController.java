package com.project.visa.controller;

import com.project.visa.entity.*;
import com.project.visa.service.*;
import com.project.visa.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/demande")
public class ScanController {

    @Autowired
    private ScanFichierService scanFichierService;

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private StatutDemandeService statutDemandeService;

    @Autowired
    private PieceDemandeService pieceDemandeService;

    @Autowired
    private StatutDemandeRepository statutDemandeRepository;


    /**
     * Affiche la page des documents à scanner pour une demande
     * @param idDemande ID de la demande
     * @return ModelAndView avec la liste des documents à scanner
     */
    @GetMapping("/{idDemande}/documents-a-scanner")
    public ModelAndView showDocumentsAScanner(@PathVariable Long idDemande) {
        ModelAndView mav = new ModelAndView("template");
        
        DemandeEntity demande = demandeService.findById(idDemande.intValue());
        if (demande == null) {
            mav.addObject("errorMessage", "Demande non trouvée");
            mav.addObject("template", "error");
            return mav;
        }

        List<ScanFichierService.DocumentUploadStatus> documents = 
            scanFichierService.getDocumentsAvecStatutUpload(idDemande);
        
        boolean tousUploades = scanFichierService.verifierTousDocumentsUploades(idDemande);
        
        mav.addObject("demande", demande);
        mav.addObject("documents", documents);
        mav.addObject("tousUploades", tousUploades);
        mav.addObject("template", "demande/documents_a_scanner");
        
        return mav;
    }

    /**
     * Upload d'un fichier pour un document spécifique d'une demande
     * @param idDemande ID de la demande
     * @param idDocumentRequis ID du document requis (PieceEntity)
     * @param fichier Fichier à uploader
     * @return ResponseEntity avec statut HTTP approprié
     */
    @PostMapping("/{idDemande}/upload/{idDocumentRequis}")
    public ResponseEntity<?> uploadFichier(
            @PathVariable Long idDemande,
            @PathVariable Long idDocumentRequis,
            @RequestParam("fichier") MultipartFile fichier) {
        
        try {
            // Vérifier si le fichier est vide
            if (fichier == null || fichier.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Fichier vide",
                    "message", "Veuillez sélectionner un fichier à uploader"
                ));
            }

            // Vérifier le type de fichier
            String contentType = fichier.getContentType();
            if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Type de fichier non autorisé",
                    "message", "Seules les images et les fichiers PDF sont acceptés"
                ));
            }

            // Vérifier si déjà uploadé
            if (scanFichierService.hasUploaded(idDemande, idDocumentRequis)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "error", "Document déjà uploadé",
                    "message", "Ce document a déjà été téléchargé pour cette demande"
                ));
            }

            // Upload du fichier
            ScanFichierEntity scanFichier = scanFichierService.uploadFichier(idDemande, idDocumentRequis, fichier);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Fichier uploadé avec succès",
                "scanId", scanFichier.getId(),
                "chemin", scanFichier.getCheminFichier()
            ));
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Erreur lors de l'upload",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Erreur serveur",
                "message", "Une erreur est survenue lors de l'upload du fichier"
            ));
        }
    }

    /**
     * Vérifie si tous les documents requis pour une demande sont uploadés
     * @param idDemande ID de la demande
     * @return ResponseEntity avec true si tous sont uploadés, false sinon
     */
    @GetMapping("/{idDemande}/tous-uploades")
    public ResponseEntity<Boolean> verifierTousUploades(@PathVariable Long idDemande) {
        boolean tousUploades = scanFichierService.verifierTousDocumentsUploades(idDemande);
        return ResponseEntity.ok(tousUploades);
    }

    /**
     * Valide les scans et change le statut de la demande après vérification de tous les uploads
     * @param idDemande ID de la demande
     * @param redirectAttributes Attributs pour la redirection
     * @return RedirectView vers la liste des demandes ou page d'erreur
     */
    @PostMapping("/{idDemande}/valider-scans")
    public RedirectView validerScansEtChangerStatus(
            @PathVariable Long idDemande,
            RedirectAttributes redirectAttributes) {
        
        try {
            DemandeEntity demande = demandeService.findById(idDemande.intValue());
            if (demande == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Demande non trouvée");
                return new RedirectView("/demande/liste");
            }

            // Vérifier que tous les documents sont uploadés
            if (!scanFichierService.verifierTousDocumentsUploades(idDemande)) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Tous les documents requis doivent être uploadés avant validation");
                return new RedirectView("/demande/" + idDemande + "/documents-a-scanner");
            }

            // Récupérer le statut actuel
            StatutDemandeEntity statutActuel = statutDemandeService.findLatestByDemandeId(idDemande.intValue())
                .orElse(null);
            
            if (statutActuel == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Statut de la demande non trouvé");
                return new RedirectView("/demande/liste");
            }

            // Changer le statut de "cree" (1) à "scanner" (2) - à adapter selon votre mapping
            // Supposons que:
            // 1 = Créée
            // 2 = Documents scannés/En attente de traitement
            
            if (statutActuel.getStatut() == 1) { // Créée
                StatutDemandeEntity nouveauStatut = new StatutDemandeEntity();
                nouveauStatut.setDemande(demande);
                nouveauStatut.setDateChangementStatut(java.time.LocalDate.now());
                nouveauStatut.setStatut(2); // Documents scannés
                statutDemandeService.save(nouveauStatut);
                
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Tous les documents ont été validés. La demande est maintenant en attente de traitement.");
            } else {
                redirectAttributes.addFlashAttribute("warningMessage", 
                    "La demande n'est pas dans un état permettant la validation des scans");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur lors de la validation des scans: " + e.getMessage());
        }
        
        return new RedirectView("/demande/liste");
    }

    /**
     * Version alternative avec redirection vers la page de la demande
     */
    @PostMapping("/{idDemande}/valider-scans-and-return")
    public RedirectView validerScansEtChangerStatusAvecRetour(
            @PathVariable Long idDemande,
            RedirectAttributes redirectAttributes) {
        
        validerScansEtChangerStatus(idDemande, redirectAttributes);
        return new RedirectView("/demande/" + idDemande + "/documents-a-scanner");
    }

    /**
     * Récupère l'URL d'un fichier uploadé pour affichage
     * @param idScan ID du scan
     * @return ResponseEntity avec le chemin du fichier
     */
    @GetMapping("/scan/{idScan}/url")
    public ResponseEntity<?> getScanUrl(@PathVariable Long idScan) {
        try {
            ScanFichierEntity scan = scanFichierService.getUploadsParDemande(null)
                .stream()
                .filter(s -> s.getId() == idScan)
                .findFirst()
                .orElse(null);
            
            if (scan == null) {
                return ResponseEntity.notFound().build();
            }
            
            return ResponseEntity.ok(Map.of(
                "chemin", scan.getCheminFichier(),
                "dateUpload", scan.getDateUpload()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/scanner/{id}")
    public String pageScan(@PathVariable Long id, Model model) {
        // 1. Récupérer la liste des documents avec leur statut (votre DTO interne)
        List<ScanFichierService.DocumentUploadStatus> docsAvecStatut = 
            scanFichierService.getDocumentsAvecStatutUpload(id);
        List<PieceDemandeEntity> piecedemande=pieceDemandeService.findByIdDemande(id.intValue());        
        // 2. Calculer les compteurs pour la barre de progression
        long nbUpload = docsAvecStatut.stream()
            .filter(ScanFichierService.DocumentUploadStatus::isUploaded)
            .count();
        int nbTotal = piecedemande.size();
        DemandeEntity demande=demandeService.findById(id.intValue());
        StatutDemandeEntity status=new StatutDemandeEntity();
        status.setDemande(demande);
        status.setStatut(10);
        status.setDateChangementStatut(LocalDate.now());
        statutDemandeRepository.save(status);
        // 3. Envoyer les données à la JSP
        model.addAttribute("pieceDemande", piecedemande);
        model.addAttribute("idDemande", id);
        model.addAttribute("documentsStatus", docsAvecStatut);
        model.addAttribute("nbUpload", nbUpload);
        model.addAttribute("nbTotal", nbTotal);
        model.addAttribute("template", "demande/documents_a_scanner");
        return "template";
    }

    // Gère l'upload via le formulaire du modal
    @PostMapping("/upload-doc")
    public String uploadDocument(@RequestParam("demandeId") Long demandeId,
                                 @RequestParam("documentId") Long documentId,
                                 @RequestParam("fichier") MultipartFile file,
                                 RedirectAttributes redirectAttributes) {
        try {
            scanFichierService.uploadFichier(demandeId, documentId, file);
            redirectAttributes.addFlashAttribute("successMessage", "Document chargé avec succès !");
        } catch (RuntimeException e) {
            // On attrape vos exceptions personnalisées (Fichier vide, type non autorisé, etc.)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/demande/scanner/" + demandeId;
    }
}