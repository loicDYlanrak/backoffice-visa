package com.project.visa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "Statut_demande")
public class StatutDemandeEntity {
    
    public static final int STATUT_CREER = 1;
    public static final int STATUT_EN_COURS_SCAN = 10;
    public static final int STATUT_SCANNE = 20;
    public static final int STATUT_APPROUVE = 30;
    public static final int STATUT_REJETER = 40;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_demande", nullable = false)
    private DemandeEntity demande;
    
    @Column(nullable = false)
    private Integer statut;
    
    @Column(name = "date_changement_statut")
    private LocalDate dateChangementStatut;
    
    private static final Map<Integer, String> STATUT_LIBELLES = new HashMap<>();
    
    static {
        STATUT_LIBELLES.put(STATUT_CREER, "Créé");
        STATUT_LIBELLES.put(STATUT_EN_COURS_SCAN, "En cours de scan");
        STATUT_LIBELLES.put(STATUT_SCANNE, "Scanné");
        STATUT_LIBELLES.put(STATUT_APPROUVE, "Approuvé");
        STATUT_LIBELLES.put(STATUT_REJETER, "Rejeté");
    }
    
    public StatutDemandeEntity() {
    }

    public static String getLibelleStatut(Integer codeStatut) {
        if (codeStatut == null) {
            return "Non défini";
        }
        return STATUT_LIBELLES.getOrDefault(codeStatut, "Statut inconnu: " + codeStatut);
    }

    public String getLibelleStatut() {
        return getLibelleStatut(this.statut);
    }
    
    public static boolean isStatutValide(Integer codeStatut) {
        return codeStatut != null && STATUT_LIBELLES.containsKey(codeStatut);
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public DemandeEntity getDemande() {
        return demande;
    }
    
    public void setDemande(DemandeEntity demande) {
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