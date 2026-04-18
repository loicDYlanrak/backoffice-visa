package com.project.visa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Statut_passeport")
public class StatutPasseportEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_passeport", nullable = false)
    private PasseportEntity passeport;
    
    @Column(nullable = false)
    private Integer statut; // 1: actif, 2: expiré, 3: perdu, 4: volé
    
    @Column(name = "date_changement_statut")
    private LocalDate dateChangementStatut;
    
    public StatutPasseportEntity() {
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public PasseportEntity getPasseport() {
        return passeport;
    }
    
    public void setPasseport(PasseportEntity passeport) {
        this.passeport = passeport;
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