package com.project.visa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Demande")
public class DemandeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_visa_transformable", nullable = false)
    private VisaTransformableEntity visaTransformable;
    
    @Column(name = "date_demande", nullable = false)
    private LocalDate dateDemande;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_demandeur", nullable = false)
    private DemandeurEntity demandeur;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_visa", nullable = false)
    private TypeVisaEntity typeVisa;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_demande", nullable = false)
    private TypeDemandeEntity typeDemande;
    
    
    @Column(name = "date_traitement")
    private LocalDate dateTraitement;
    
    @OneToMany(mappedBy = "demande", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VisaEntity> visas;
    
    @OneToMany(mappedBy = "demande", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CarteResidentEntity> cartesResident;
    
    public DemandeEntity() {
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VisaTransformableEntity getVisaTransformable() {
        return visaTransformable;
    }

    public void setVisaTransformable(VisaTransformableEntity visaTransformable) {
        this.visaTransformable = visaTransformable;
    }

    public LocalDate getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    public DemandeurEntity getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(DemandeurEntity demandeur) {
        this.demandeur = demandeur;
    }

    public TypeVisaEntity getTypeVisa() {
        return typeVisa;
    }

    public void setTypeVisa(TypeVisaEntity typeVisa) {
        this.typeVisa = typeVisa;
    }

    public TypeDemandeEntity getTypeDemande() {
        return typeDemande;
    }

    public void setTypeDemande(TypeDemandeEntity typeDemande) {
        this.typeDemande = typeDemande;
    }

    public LocalDate getDateTraitement() {
        return dateTraitement;
    }

    public void setDateTraitement(LocalDate dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public List<VisaEntity> getVisas() {
        return visas;
    }

    public void setVisas(List<VisaEntity> visas) {
        this.visas = visas;
    }

    public List<CarteResidentEntity> getCartesResident() {
        return cartesResident;
    }

    public void setCartesResident(List<CarteResidentEntity> cartesResident) {
        this.cartesResident = cartesResident;
    }

    public boolean isValide() {
        if (visaTransformable == null || dateDemande == null || demandeur == null || typeVisa == null || typeDemande == null) {
            return false;
        }
        if(visaTransformable.demandeValide(dateDemande)) {
            return true;
        }else {
            return false;
        }
    }
}