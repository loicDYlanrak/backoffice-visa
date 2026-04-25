package com.project.visa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Demandeur")
public class DemandeurEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "nom", nullable = false, length = 50)
    private String nom;
    
    @Column(name = "prenom", nullable = false, length = 50)
    private String prenom;
    
    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;
    
    @Column(name = "lieu_naissance", nullable = true, length = 100)
    private String lieuNaissance;

    @Column(name = "telephone", nullable = false, length = 20)
    private String telephone;
    
    @Column(name = "email", nullable = true, length = 100)
    private String email;
    
    @Column(name = "adresse", nullable = false, columnDefinition = "TEXT")
    private String adresse;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_situation_familiale", nullable = false)
    private SituationFamilialeEntity situationFamiliale;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nationalite", nullable = false)
    private NationaliteEntity nationalite;
    
    @OneToMany(mappedBy = "demandeur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PasseportEntity> passeports;
    
    @OneToMany(mappedBy = "demandeur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VisaTransformableEntity> visasTransformables;
    
    @OneToMany(mappedBy = "demandeur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DemandeEntity> demandes;
    
    public DemandeurEntity() {
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public LocalDate getDateNaissance() {
        return dateNaissance;
    }
    
    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
    
    public String getLieuNaissance() {
        return lieuNaissance;
    }
    
    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public SituationFamilialeEntity getSituationFamiliale() {
        return situationFamiliale;
    }
    
    public void setSituationFamiliale(SituationFamilialeEntity situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }
    
    public NationaliteEntity getNationalite() {
        return nationalite;
    }
    
    public void setNationalite(NationaliteEntity nationalite) {
        this.nationalite = nationalite;
    }

    public List<PasseportEntity> getPasseports() {
        return passeports;
    }
    
    public void setPasseports(List<PasseportEntity> passeports) {
        this.passeports = passeports;
    }
    
    public List<VisaTransformableEntity> getVisasTransformables() {
        return visasTransformables;
    }
    
    public void setVisasTransformables(List<VisaTransformableEntity> visasTransformables) {
        this.visasTransformables = visasTransformables;
    }
    
    public List<DemandeEntity> getDemandes() {
        return demandes;
    }
    
    public void setDemandes(List<DemandeEntity> demandes) {
        this.demandes = demandes;
    }
    public  boolean isValid(LocalDate currentDate) {
        if (dateNaissance == null || dateNaissance.isAfter(currentDate)) {
            return false;
        }
        if(situationFamiliale == null || nationalite == null) {
            return false;
        }
        if(nom == null || nom.isEmpty() || prenom == null || prenom.isEmpty() || telephone == null || telephone.isEmpty() || adresse == null || adresse.isEmpty()) {
            return false;
        }
        if (passeports == null) {
           return false;
        }
        return true;
    }
}