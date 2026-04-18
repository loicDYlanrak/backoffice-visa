package com.project.visa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Visa_transformable")
public class VisaTransformableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_demandeur", nullable = false)
    private DemandeurEntity demandeur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_passeport", nullable = false)
    private PasseportEntity passeport;

    @Column(name = "date_entree", nullable = false)
    private LocalDate dateEntree;

    @Column(name = "date_sortie", nullable = false)
    private LocalDate dateSortie;

    @Column(name = "numero_reference", nullable = false, unique = true, length = 50)
    private String numeroReference;

    @OneToMany(mappedBy = "visaTransformable", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DemandeEntity> demandes;

    public VisaTransformableEntity() {
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DemandeurEntity getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(DemandeurEntity demandeur) {
        this.demandeur = demandeur;
    }

    public PasseportEntity getPasseport() {
        return passeport;
    }

    public void setPasseport(PasseportEntity passeport) {
        this.passeport = passeport;
    }

    public LocalDate getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(LocalDate dateEntree) {
        this.dateEntree = dateEntree;
    }

    public LocalDate getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(LocalDate dateSortie) {
        this.dateSortie = dateSortie;
    }

    public String getNumeroReference() {
        return numeroReference;
    }

    public void setNumeroReference(String numeroReference) {
        this.numeroReference = numeroReference;
    }

    public List<DemandeEntity> getDemandes() {
        return demandes;
    }

    public void setDemandes(List<DemandeEntity> demandes) {
        this.demandes = demandes;
    }
}