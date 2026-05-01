package com.project.visa.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Visa_transformable")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VisaTransformableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
    @JsonIgnore
    private List<DemandeEntity> demandes;

    public VisaTransformableEntity() {
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
    public boolean isValid() {
        if (demandeur == null || passeport == null || dateEntree == null || dateSortie == null || numeroReference == null) {
            return false;
        }
        if (dateEntree.isAfter(dateSortie)) {
            return false;
        }
        if (!numeroReference.matches("(REF-\\d{4}-\\d{4}|VISA-\\d{4}-\\d{3})")) {
            return false;
        }
        return true;
    }
    public boolean demandeValide(LocalDate currentDate) {
        if (dateEntree == null || dateSortie == null) {
            return false;
        }
        return !dateEntree.isAfter(dateSortie) && isValid();
    }
}