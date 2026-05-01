package com.project.visa.entity;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Type_demande")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TypeDemandeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false, length = 50)
    private String libelle;
    
    @OneToMany(mappedBy = "typeDemande")
    @JsonIgnore
    private List<DemandeEntity> demandes;
    
    public TypeDemandeEntity() {
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    
    public List<DemandeEntity> getDemandes() {
        return demandes;
    }
    
    public void setDemandes(List<DemandeEntity> demandes) {
        this.demandes = demandes;
    }
}