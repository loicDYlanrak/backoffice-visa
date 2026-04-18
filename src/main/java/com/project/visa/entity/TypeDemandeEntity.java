package com.project.visa.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Type_demande")
public class TypeDemandeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String libelle;
    
    @OneToMany(mappedBy = "typeDemande")
    private List<DemandeEntity> demandes;
    
    public TypeDemandeEntity() {
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
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