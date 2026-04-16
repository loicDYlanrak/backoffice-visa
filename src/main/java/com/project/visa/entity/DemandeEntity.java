package com.project.visa.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Demande")
public class DemandeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "reference",nullable = false, unique = true)
    private String reference;
    
    @Column(name = "date_demande", updatable = false)
    private LocalDateTime dateDemande;
    
    @Column(name = "id_status",nullable = false)
    private int idStatus;
    
    @Column(name = "id_demandeur",nullable = false)
    private int idDemandeur;

    @Column(name = "id_visa",nullable = false)
    private int idVisa;

    @Column(name = "observations")
    private String observations;

    @Column(name = "date_traitement")
    private LocalDateTime dateTraitement;

    @Column(name = "motif_rejet")
    private String motifRejet;

    public DemandeEntity(long id, String reference, LocalDateTime dateDemande, int idStatus, int idDemandeur, int idVisa, String observations, LocalDateTime dateTraitement, String motifRejet) {
        this.id = id;
        this.reference = reference;
        this.dateDemande = dateDemande;
        this.idStatus = idStatus;
        this.idDemandeur = idDemandeur;
        this.idVisa = idVisa;
        this.observations = observations;
        this.dateTraitement = dateTraitement;
        this.motifRejet = motifRejet;
    }
     public DemandeEntity(long id, String reference, LocalDateTime dateDemande, int idStatus, int idDemandeur, int idVisa, String observations) {
        this.id = id;
        this.reference = reference;
        this.dateDemande = dateDemande;
        this.idStatus = idStatus;
        this.idDemandeur = idDemandeur;
        this.idVisa = idVisa;
        this.observations = observations;
    }
    public DemandeEntity() {
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public LocalDateTime getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public int getIdDemandeur() {
        return idDemandeur;
    }

    public void setIdDemandeur(int idDemandeur) {
        this.idDemandeur = idDemandeur;
    }

    public int getIdVisa() {
        return idVisa;
    }

    public void setIdVisa(int idVisa) {
        this.idVisa = idVisa;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public LocalDateTime getDateTraitement() {
        return dateTraitement;
    }

    public void setDateTraitement(LocalDateTime dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public String getMotifRejet() {
        return motifRejet;
    }

    public void setMotifRejet(String motifRejet) {
        this.motifRejet = motifRejet;
    }
}