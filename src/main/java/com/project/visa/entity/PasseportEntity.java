package com.project.visa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Passeport")
public class PasseportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_demandeur", nullable = false)
    private DemandeurEntity demandeur;

    @Column(name = "numero_passeport", nullable = false, unique = true, length = 50)
    private String numeroPasseport;

    @Column(name = "date_delivrance", nullable = false)
    private LocalDate dateDelivrance;

    @Column(name = "date_expiration", nullable = false)
    private LocalDate dateExpiration;

    @Column(name = "pays_delivrance", length = 100)
    private String paysDelivrance;

    @OneToMany(mappedBy = "passeport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StatutPasseportEntity> statuts;

    @OneToMany(mappedBy = "passeport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VisaEntity> visas;

    @OneToMany(mappedBy = "passeport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VisaTransformableEntity> visasTransformables;

    @OneToMany(mappedBy = "passeport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CarteResidentEntity> cartesResident;

    public PasseportEntity() {
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DemandeurEntity getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(DemandeurEntity demandeur) {
        this.demandeur = demandeur;
    }

    public String getNumeroPasseport() {
        return numeroPasseport;
    }

    public void setNumeroPasseport(String numeroPasseport) {
        this.numeroPasseport = numeroPasseport;
    }

    public LocalDate getDateDelivrance() {
        return dateDelivrance;
    }

    public void setDateDelivrance(LocalDate dateDelivrance) {
        this.dateDelivrance = dateDelivrance;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getPaysDelivrance() {
        return paysDelivrance;
    }

    public void setPaysDelivrance(String paysDelivrance) {
        this.paysDelivrance = paysDelivrance;
    }

    public List<StatutPasseportEntity> getStatuts() {
        return statuts;
    }

    public void setStatuts(List<StatutPasseportEntity> statuts) {
        this.statuts = statuts;
    }

    public List<VisaEntity> getVisas() {
        return visas;
    }

    public void setVisas(List<VisaEntity> visas) {
        this.visas = visas;
    }

    public List<VisaTransformableEntity> getVisasTransformables() {
        return visasTransformables;
    }

    public void setVisasTransformables(List<VisaTransformableEntity> visasTransformables) {
        this.visasTransformables = visasTransformables;
    }

    public List<CarteResidentEntity> getCartesResident() {
        return cartesResident;
    }

    public void setCartesResident(List<CarteResidentEntity> cartesResident) {
        this.cartesResident = cartesResident;
    }
}