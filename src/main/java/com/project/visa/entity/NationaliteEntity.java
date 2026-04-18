package com.project.visa.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Nationalite")
public class NationaliteEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String libelle;
    
    @OneToMany(mappedBy = "nationalite")
    private List<DemandeurEntity> demandeurs;
    
    public NationaliteEntity() {
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
    
    public List<DemandeurEntity> getDemandeurs() {
        return demandeurs;
    }
    
    public void setDemandeurs(List<DemandeurEntity> demandeurs) {
        this.demandeurs = demandeurs;
    }
}