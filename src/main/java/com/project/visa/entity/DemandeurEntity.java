package com.project.visa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Demandeur")
public class DemandeurEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nom", nullable = false, length = 50)
    private String nom;
    
    @Column(name = "prenom", nullable = false, length = 50)
    private String prenom;
    
    @Column(name = "nom_jeune_fille", length = 50)
    private String nomJeuneFille;
    
    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;
    
    @Column(name = "lieu_naissance", nullable = false, length = 100)
    private String lieuNaissance;
    
    @Column(name = "profession", nullable = false, length = 100)
    private String profession;
    
    @Column(name = "telephone", nullable = false, length = 20)
    private String telephone;
    
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    
    @Column(name = "adresse", nullable = false, columnDefinition = "TEXT")
    private String adresse;
    
    @Column(name = "id_situation_familiale", nullable = false)
    private Long idSituationFamiliale;
    
    @Column(name = "id_nationalite_actuelle", nullable = false)
    private Long idNationaliteActuelle;
    
    @Column(name = "id_nationalite_origine", nullable = false)
    private Long idNationaliteOrigine;
    
    @Column(name = "id_genre", nullable = false)
    private Long idGenre;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructeur par défaut (obligatoire pour JPA)
    public DemandeurEntity() {
    }
    
    // Constructeur avec tous les paramètres essentiels
    public DemandeurEntity(String nom, String prenom, LocalDate dateNaissance, 
                           String lieuNaissance, String profession, String telephone, 
                           String email, String adresse, Long idSituationFamiliale, 
                           Long idNationaliteActuelle, Long idNationaliteOrigine, 
                           Long idGenre) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.lieuNaissance = lieuNaissance;
        this.profession = profession;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
        this.idSituationFamiliale = idSituationFamiliale;
        this.idNationaliteActuelle = idNationaliteActuelle;
        this.idNationaliteOrigine = idNationaliteOrigine;
        this.idGenre = idGenre;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
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
    
    public String getNomJeuneFille() {
        return nomJeuneFille;
    }
    
    public void setNomJeuneFille(String nomJeuneFille) {
        this.nomJeuneFille = nomJeuneFille;
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
    
    public String getProfession() {
        return profession;
    }
    
    public void setProfession(String profession) {
        this.profession = profession;
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
    
    public Long getIdSituationFamiliale() {
        return idSituationFamiliale;
    }
    
    public void setIdSituationFamiliale(Long idSituationFamiliale) {
        this.idSituationFamiliale = idSituationFamiliale;
    }
    
    public Long getIdNationaliteActuelle() {
        return idNationaliteActuelle;
    }
    
    public void setIdNationaliteActuelle(Long idNationaliteActuelle) {
        this.idNationaliteActuelle = idNationaliteActuelle;
    }
    
    public Long getIdNationaliteOrigine() {
        return idNationaliteOrigine;
    }
    
    public void setIdNationaliteOrigine(Long idNationaliteOrigine) {
        this.idNationaliteOrigine = idNationaliteOrigine;
    }
    
    public Long getIdGenre() {
        return idGenre;
    }
    
    public void setIdGenre(Long idGenre) {
        this.idGenre = idGenre;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Méthodes automatiques pour les timestamps
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "DemandeurEntity{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    
}