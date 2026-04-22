package com.project.visa.entity;
import java.util.List;

import jakarta.persistence.*;
@Entity
@Table(name = "Piece")
public class PieceEntity {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false, length = 50)
    private String libelle;
    
     @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_visa", nullable = true)
    private TypeVisaEntity typeVisaEntity;

    @OneToMany(mappedBy = "piece")
    private List<PieceDemandeEntity> demandes;
    
    public PieceEntity() {
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
    public void setTypeVisa(TypeVisaEntity typeVisaEntity){
        this.typeVisaEntity=typeVisaEntity;
    }
    public TypeVisaEntity getTypeVisa(){
        return typeVisaEntity;
    }
    public List<PieceDemandeEntity> getDemandes() {
        return demandes;
    }
    
    public void setDemandes(List<PieceDemandeEntity> demandes) {
        this.demandes = demandes;
    }
}
