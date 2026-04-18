package com.project.visa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Type_visa")
public class TypeVisaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String libelle;
    
    public TypeVisaEntity() {
    }

    public TypeVisaEntity(String libelle) {
        this.libelle = libelle;
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

   
}