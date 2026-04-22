package com.project.visa.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.visa.entity.PieceDemandeEntity;
import com.project.visa.repository.PieceDemandeRepository;
@Service
public class PieceDemandeService {
    @Autowired
    private PieceDemandeRepository pieceDemandeRepository;
 public List<PieceDemandeEntity> findAll() {
        return pieceDemandeRepository.findAll();
    }
    public PieceDemandeEntity findById(int id){
        return pieceDemandeRepository.findById(id);
    }
    public PieceDemandeEntity save(PieceDemandeEntity pieceDemandeEntity){
        return pieceDemandeRepository.save(pieceDemandeEntity);
    }
    public List<PieceDemandeEntity> findByIdDemande(int id){
        return pieceDemandeRepository.findByDemandeId(id);
    }
    @Transactional
    public void deleteByDemandeId(int id){
        pieceDemandeRepository.deleteByDemandeId(id);
    }
}