package com.project.visa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "scan_fichier")
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