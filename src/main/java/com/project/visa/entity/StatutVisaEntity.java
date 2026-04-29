package com.project.visa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "statut_visa")
public class StatutVisaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_visa", nullable = false)
    private VisaEntity visa;

    @Column(name = "statut", nullable = false)
    private int statut; // 1: non valide, 5: créé, 30: approuvé, 40: rejeté

    @Column(name = "date_changement_statut")
    private LocalDate dateChangementStatut;

    public StatutVisaEntity() {
    }

    public StatutVisaEntity(VisaEntity visa, int statut, LocalDate dateChangementStatut) {
        this.visa = visa;
        this.statut = statut;
        this.dateChangementStatut = dateChangementStatut;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VisaEntity getVisa() {
        return visa;
    }

    public void setVisa(VisaEntity visa) {
        this.visa = visa;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public LocalDate getDateChangementStatut() {
        return dateChangementStatut;
    }

    public void setDateChangementStatut(LocalDate dateChangementStatut) {
        this.dateChangementStatut = dateChangementStatut;
    }
}