package com.project.visa.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.visa.entity.PieceEntity;
import com.project.visa.repository.PieceRepository;
@Service
public class PieceService {
    @Autowired
    private PieceRepository pieceRepository;
 public List<PieceEntity> findAll() {
        return pieceRepository.findAll();
    }
    public PieceEntity findById(int id){
        return pieceRepository.findById(id);
    }
    public PieceEntity save(PieceEntity pieceEntity){
        return pieceRepository.save(pieceEntity);
    }

    public List<PieceEntity> findByTypeVisaId(int typeVisaId) {
        return pieceRepository.findByTypeVisaEntity_Id(typeVisaId);
    }

}