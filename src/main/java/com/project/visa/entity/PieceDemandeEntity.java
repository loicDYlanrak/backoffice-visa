package com.project.visa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "piece_demande")
public class PieceDemandeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_piece", nullable = false)
    private PieceEntity piece;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_demande", nullable = false)
    private DemandeEntity demande;

    public PieceDemandeEntity() {
    }
    public PieceDemandeEntity(PieceEntity piece, DemandeEntity demande) {
        this.piece = piece;
        this.demande = demande;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PieceEntity getPiece() {
        return piece;
    }

    public void setPiece(PieceEntity piece) {
        this.piece = piece;
    }

    public DemandeEntity getDemande() {
        return demande;
    }

    public void setDemande(DemandeEntity demande) {
        this.demande = demande;
    }
}