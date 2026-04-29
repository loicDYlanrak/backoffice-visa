package com.project.visa.service;

import com.project.visa.entity.*;
import com.project.visa.repository.ScanFichierRepository;
import com.project.visa.repository.PieceDemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ScanFichierService {

    @Autowired
    private ScanFichierRepository scanFichierRepository;

    @Autowired
    private PieceDemandeRepository pieceDemandeRepository;

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private PieceService pieceService;

    @Value("${upload.directory:uploads}")
    private String uploadDirectory;

    /**
     * Upload un fichier pour une demande et un document requis spécifique
     * 
     * @param idDemande        ID de la demande
     * @param idDocumentRequis ID du document requis (PieceEntity)
     * @param fichier          Fichier à uploader
     * @return ScanFichierEntity créé
     * @throws RuntimeException si le fichier est vide, mauvais type ou déjà uploadé
     */
    public ScanFichierEntity uploadFichier(Long idDemande, Long idDocumentRequis, MultipartFile fichier) {
        // Validation du fichier
        if (fichier == null || fichier.isEmpty()) {
            throw new RuntimeException("Fichier vide");
        }

        // Vérification du type de fichier (extensions autorisées)
        String contentType = fichier.getContentType();
        if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
            throw new RuntimeException("Type de fichier non autorisé. Seules les images et PDF sont acceptés.");
        }

        // Vérifier si déjà uploadé
        if (hasUploaded(idDemande, idDocumentRequis)) {
            throw new RuntimeException("Ce document a déjà été uploadé");
        }

        try {
            // Récupérer la pieceDemande correspondante
            PieceDemandeEntity pieceDemande = pieceDemandeRepository.findByDemandeIdAndPieceId(
                    idDemande.intValue(),
                    idDocumentRequis.intValue());

            if (pieceDemande == null) {
                throw new RuntimeException("Document requis non trouvé pour cette demande");
            }

            // Créer le répertoire si nécessaire
            Path uploadPath = Paths.get(uploadDirectory, "demande_" + idDemande);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Générer un nom de fichier unique
            String originalFilename = fichier.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString() + extension;

            // Sauvegarder le fichier
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(fichier.getInputStream(), filePath);

            // Créer l'entité ScanFichier
            ScanFichierEntity scanFichier = new ScanFichierEntity();
            scanFichier.setPieceDemande(pieceDemande);
            scanFichier.setCheminFichier(filePath.toString());
            scanFichier.setDateUpload(LocalDate.now());

            return scanFichierRepository.save(scanFichier);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload du fichier: " + e.getMessage(), e);
        }
    }

    /**
     * Vérifie si un document a déjà été uploadé pour une demande
     * 
     * @param idDemande        ID de la demande
     * @param idDocumentRequis ID du document requis
     * @return true si déjà uploadé, false sinon
     */
    public boolean hasUploaded(Long idDemande, Long idDocumentRequis) {
        return scanFichierRepository.existsByIdDemandeAndIdPiece(
                idDemande.intValue(),
                idDocumentRequis.intValue());
    }

    /**
     * Récupère tous les fichiers uploadés pour une demande
     * 
     * @param idDemande ID de la demande
     * @return Liste des ScanFichierEntity
     */
    public List<ScanFichierEntity> getUploadsParDemande(Long idDemande) {
        return scanFichierRepository.findByIdDemande(idDemande.intValue());
    }

    /**
     * Vérifie si tous les documents requis pour une demande ont été uploadés
     * 
     * @param idDemande ID de la demande
     * @return true si tous les documents sont uploadés, false sinon
     */
    public boolean verifierTousDocumentsUploades(Long idDemande) {
        DemandeEntity demande = demandeService.findById(idDemande.intValue());
        if (demande == null) {
            return false;
        }

        // Récupérer tous les documents requis pour ce type de demande
        List<PieceEntity> documentsRequis = pieceService.findByTypeVisaId(demande.getTypeVisa().getId());

        // Vérifier pour chaque document s'il a été uploadé
        for (PieceEntity document : documentsRequis) {
            if (!hasUploaded(idDemande, (long) document.getId())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Récupère les documents requis avec leur statut d'upload pour un type de
     * demande
     * 
     * @param typeDemande Type de demande (PREMIERE, RENOUVELLEMENT, etc.)
     * @return Liste des documents avec leur statut
     */
    public List<DocumentUploadStatus> getDocumentsParTypeDemande(String typeDemande) {
        // Cette méthode nécessite de connaître la relation entre typeDemande et les
        // documents requis
        // Implémentation selon votre logique métier
        return null;
    }

    /**
     * Classe interne pour le statut d'upload des documents
     */
    public static class DocumentUploadStatus {
        private PieceEntity piece;
        private boolean isUploaded;
        private ScanFichierEntity scanFichier;

        public DocumentUploadStatus(PieceEntity piece, boolean isUploaded, ScanFichierEntity scanFichier) {
            this.piece = piece;
            this.isUploaded = isUploaded;
            this.scanFichier = scanFichier;
        }

        // Getters
        public PieceEntity getPiece() {
            return piece;
        }

        public boolean isUploaded() {
            return isUploaded;
        }

        public ScanFichierEntity getScanFichier() {
            return scanFichier;
        }
    }

    /**
     * Récupère les documents requis pour une demande avec leur statut d'upload
     * 
     * @param idDemande ID de la demande
     * @return Liste des documents avec statut d'upload
     */
    public List<DocumentUploadStatus> getDocumentsAvecStatutUpload(Long idDemande) {
        DemandeEntity demande = demandeService.findById(idDemande.intValue());
        if (demande == null) {
            return List.of();
        }
        List<PieceEntity> documentsRequis = pieceService.findByTypeVisaEntityIsNull(); 
        documentsRequis.addAll(pieceService.findByTypeVisaId(demande.getTypeVisa().getId()));
        
        List<ScanFichierEntity> uploads = getUploadsParDemande(idDemande);

        List<DocumentUploadStatus> resultats = new ArrayList<>();

        for (PieceEntity document : documentsRequis) {
            ScanFichierEntity scanTrouve = null;
            System.out.println("Vérification pour document ID = " + document.getId());
            for (ScanFichierEntity upload : uploads) {
                System.out.println("Comparaison: upload piece ID = " + upload.getPieceDemande().getPiece().getId() +
                        " vs document ID = " + document.getId());
                if (upload.getPieceDemande().getPiece().getId() == document.getId()) {
                    scanTrouve = upload;
                    break; 
                }
            }

            boolean estUpload = (scanTrouve != null);
            DocumentUploadStatus status = new DocumentUploadStatus(document, estUpload, scanTrouve);

            resultats.add(status);
        }

        return resultats;
    }
}