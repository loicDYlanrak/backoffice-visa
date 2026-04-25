package com.project.visa.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Situation_familiale")
public class SituationFamilialeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false, length = 50)
    private String libelle;
    
    @OneToMany(mappedBy = "situationFamiliale")
    private List<DemandeurEntity> demandeurs;
    
    public SituationFamilialeEntity() {
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
    
    public List<DemandeurEntity> getDemandeurs() {
        return demandeurs;
    }
    
    public void setDemandeurs(List<DemandeurEntity> demandeurs) {
        this.demandeurs = demandeurs;
    }
}