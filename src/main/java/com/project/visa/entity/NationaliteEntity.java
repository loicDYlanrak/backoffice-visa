package com.project.visa.entity;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Nationalite")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class NationaliteEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false, length = 50)
    private String libelle;
    
    @OneToMany(mappedBy = "nationalite")
    @JsonIgnore
    private List<DemandeurEntity> demandeurs;
    
    public NationaliteEntity() {
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