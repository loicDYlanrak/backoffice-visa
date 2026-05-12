package com.project.visa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "photo_signature_demande")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PhotoSignatureDemandeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_demande", nullable = false)
    private DemandeEntity demande;

    @Column(name = "photo_url", nullable = false, length = 250)
    private String photoUrl;

    @Column(name = "signature_url", nullable = false, length = 250)
    private String signatureUrl;

    @Column(name = "date_upload", nullable = false)
    private LocalDate dateUpload;

    public PhotoSignatureDemandeEntity() {
    }

    public PhotoSignatureDemandeEntity(DemandeEntity demande, String photoUrl, String signatureUrl,
            LocalDate dateUpload) {
        this.demande = demande;
        this.photoUrl = photoUrl;
        this.signatureUrl = signatureUrl;
        this.dateUpload = dateUpload;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DemandeEntity getDemande() {
        return demande;
    }

    public void setDemande(DemandeEntity demande) {
        this.demande = demande;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getSignatureUrl() {
        return signatureUrl;
    }

    public void setSignatureUrl(String signatureUrl) {
        this.signatureUrl = signatureUrl;
    }

    public LocalDate getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(LocalDate dateUpload) {
        this.dateUpload = dateUpload;
    }
}