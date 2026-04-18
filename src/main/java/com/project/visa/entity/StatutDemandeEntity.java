package com.project.visa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Statut_demande")
public class StatutDemandeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_demande", nullable = false)
    private DemandeEntity demande;
    
    @Column(nullable = false)
    private Integer statut; // 'brouillon', 'soumise', 'en_cours', 'validee', 'rejetee'
    
    @Column(name = "date_changement_statut")
    private LocalDate dateChangementStatut;
    
    public StatutDemandeEntity() {
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public DemandeEntity getDemande() {
        return demande;
    }
    
    public void setPasseport(DemandeEntity demande) {
        this.demande = demande;
    }
    
    public Integer getStatut() {
        return statut;
    }
    
    public void setStatut(Integer statut) {
        this.statut = statut;
    }
    
    public LocalDate getDateChangementStatut() {
        return dateChangementStatut;
    }
    
    public void setDateChangementStatut(LocalDate dateChangementStatut) {
        this.dateChangementStatut = dateChangementStatut;
    }
}