package com.project.visa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.visa.entity.VisaTransformableEntity;
import com.project.visa.repository.VisaTransformableRepository;

@Service
public class VisaTransformableService {
    @Autowired
    private VisaTransformableRepository visaTransformableRepository;
    public VisaTransformableEntity save(VisaTransformableEntity visaTransformableEntity ){
        return visaTransformableRepository.save(visaTransformableEntity);
    }
}
