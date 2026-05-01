package com.project.visa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Passeport")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PasseportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_demandeur", nullable = false)
    @JsonIgnore
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
    @JsonIgnore
    private List<VisaEntity> visas;

    @OneToMany(mappedBy = "passeport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<VisaTransformableEntity> visasTransformables;

    @OneToMany(mappedBy = "passeport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CarteResidentEntity> cartesResident;

    public PasseportEntity() {
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public boolean isExpired(LocalDate currentDate) {
        if (dateExpiration == null || currentDate == null) {
            return true; 
            }
        return dateExpiration.isBefore(currentDate);
    }
   public boolean isValid(LocalDate currentDate) {
        if (dateDelivrance == null || dateExpiration == null || currentDate == null||numeroPasseport==null||numeroPasseport.isEmpty()) {
            return false;
        }
        return !dateDelivrance.isAfter(currentDate) 
            && !isExpired(currentDate) 
            && dateExpiration.isAfter(dateDelivrance);
    }
    public boolean isExpiringSoon(LocalDate currentDate, int months) {
        if (dateExpiration == null || currentDate == null) {
            return false;
            }
        LocalDate threshold = currentDate.plusMonths(months);
        return dateExpiration.isBefore(threshold) && !isExpired(currentDate);
    }
    public long getDaysUntilExpiration(LocalDate currentDate) {
        if (dateExpiration == null || currentDate == null || isExpired(currentDate)) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(currentDate, dateExpiration);
    }
}