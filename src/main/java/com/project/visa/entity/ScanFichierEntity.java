package com.project.visa.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "scan_fichier")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ScanFichierEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_piece_demande", nullable = false)
    private PieceDemandeEntity pieceDemande;

    @Column(name = "chemin_fichier", nullable = false, length = 250)
    private String cheminFichier;

    @Column(name = "date_upload", nullable = false)
    private LocalDate dateUpload;

    public ScanFichierEntity() {
    }

    public ScanFichierEntity(PieceDemandeEntity pieceDemande, String cheminFichier, LocalDate dateUpload) {
        this.pieceDemande = pieceDemande;
        this.cheminFichier = cheminFichier;
        this.dateUpload = dateUpload;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PieceDemandeEntity getPieceDemande() {
        return pieceDemande;
    }

    public void setPieceDemande(PieceDemandeEntity pieceDemande) {
        this.pieceDemande = pieceDemande;
    }

    public String getCheminFichier() {
        return cheminFichier;
    }

    public void setCheminFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }

    public LocalDate getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(LocalDate dateUpload) {
        this.dateUpload = dateUpload;
    }
}