package com.project.visa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Visa")
public class VisaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_visa", length = 50)
    private String numeroVisa;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(nullable = false)
    private Boolean transformable = true;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_visa", nullable = false)
    private TypeVisaEntity typeVisa;

   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_passeport", nullable = false)
    private PasseportEntity passeport;

    // --- Constructeurs ---
    public VisaEntity() {
    }

    // --- Getters et Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroVisa() {
        return numeroVisa;
    }

    public void setNumeroVisa(String numeroVisa) {
        this.numeroVisa = numeroVisa;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getTransformable() {
        return transformable;
    }

    public void setTransformable(Boolean transformable) {
        this.transformable = transformable;
    }

    public TypeVisaEntity getTypeVisa() {
        return typeVisa;
    }

    public void setTypeVisa(TypeVisaEntity typeVisa) {
        this.typeVisa = typeVisa;
    }

    public PasseportEntity getPasseport() {
        return passeport;
    }

    public void setPasseport(PasseportEntity hidePasseport) {
        this.passeport = hidePasseport;
    }
}