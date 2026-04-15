package com.project.visa.service;

import com.project.visa.entity.TestEntity;
import com.project.visa.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TestService {
    
    @Autowired
    private TestRepository testRepository;
    
    public List<TestEntity> findAll() {
        return testRepository.findAll();
    }
    
    public Optional<TestEntity> findById(Long id) {
        return testRepository.findById(id);
    }
    
    public TestEntity save(TestEntity testEntity) {
        return testRepository.save(testEntity);
    }
    
    public void deleteById(Long id) {
        testRepository.deleteById(id);
    }
    
    public List<TestEntity> findByStatus(Integer status) {
        return testRepository.findByStatus(status);
    }
    
    public List<TestEntity> searchByName(String name) {
        return testRepository.findByNameContainingIgnoreCase(name);
    }
}