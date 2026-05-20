package com.project.visa.service;

import com.project.visa.entity.DemandeEntity;
import com.project.visa.entity.PhotoSignatureDemandeEntity;
import com.project.visa.repository.PhotoSignatureDemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PhotoSignatureDemandeService {

    @Autowired
    private PhotoSignatureDemandeRepository repository;

    @Value("${upload.path}")
    private String uploadPath;

    public PhotoSignatureDemandeEntity save(PhotoSignatureDemandeEntity entity) {
        return repository.save(entity);
    }

    public List<PhotoSignatureDemandeEntity> findByDemandeId(int demandeId) {
        return repository.findByDemandeId(demandeId);
    }

    public PhotoSignatureDemandeEntity findLatestByDemandeId(int demandeId) {
        List<PhotoSignatureDemandeEntity> list = repository.findLatestByDemandeId(demandeId);
        return list.isEmpty() ? null : list.get(0);
    }

    public boolean existsByDemandeId(int demandeId) {
        return repository.existsByDemandeId(demandeId);
    }

    public void deleteByDemandeId(int demandeId) {
        repository.deleteByDemandeId(demandeId);
    }

    public String saveBase64Image(String base64Data, String type, int demandeId) throws IOException {
        String baseUploadPath = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
        Path uploadDir = Paths.get(baseUploadPath + "demande_" + demandeId);

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
            System.out.println("Dossier créé: " + uploadDir.toString());
        }

        String[] parts = base64Data.split(",");
        String imageData = parts.length > 1 ? parts[1] : parts[0];

        byte[] imageBytes = java.util.Base64.getDecoder().decode(imageData);

        String fileName = type + "_" + UUID.randomUUID().toString() + ".png";
        Path filePath = uploadDir.resolve(fileName);

        Files.write(filePath, imageBytes);

        System.out.println("Fichier sauvegardé: " + filePath.toString());

        return "uploads/demande_" + demandeId + "/" + fileName;
    }

    public PhotoSignatureDemandeEntity enregistrerPhotoSignature(int demandeId, String photoBase64,
            String signatureBase64, DemandeEntity demande) {
        try {
            String photoPath = null;
            String signaturePath = null;

            if (photoBase64 != null && !photoBase64.isEmpty()) {
                photoPath = saveBase64Image(photoBase64, "photo", demandeId);
            }

            if (signatureBase64 != null && !signatureBase64.isEmpty()) {
                signaturePath = saveBase64Image(signatureBase64, "signature", demandeId);
            }

            PhotoSignatureDemandeEntity entity = new PhotoSignatureDemandeEntity();
            entity.setDemande(demande);
            entity.setPhotoUrl(photoPath);
            entity.setSignatureUrl(signaturePath);
            entity.setDateUpload(LocalDate.now());

            return repository.save(entity);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}