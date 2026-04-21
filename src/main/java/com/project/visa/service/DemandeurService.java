package com.project.visa.service;

import com.project.visa.entity.DemandeurEntity;
import com.project.visa.entity.NationaliteEntity;
import com.project.visa.entity.SituationFamilialeEntity;
import com.project.visa.repository.DemandeurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class DemandeurService {
    
    @Autowired
    private DemandeurRepository demandeurRepository;
    
    // Regex pour validation email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    
    // ========== CRUD DE BASE ==========
    
    /**
     * Sauvegarder un demandeur
     */
    @Transactional
    public DemandeurEntity save(DemandeurEntity demandeurEntity) {
        return demandeurRepository.save(demandeurEntity);
    }
    
    /**
     * Trouver tous les demandeurs
     */
    public List<DemandeurEntity> findAll() {
        return demandeurRepository.findAll();
    }
    
    /**
     * Trouver tous les demandeurs avec pagination
     */
    public Page<DemandeurEntity> findAll(Pageable pageable) {
        return demandeurRepository.findAll(pageable);
    }
    
    /**
     * Trouver un demandeur par son ID
     */
    public Optional<DemandeurEntity> findById(Long id) {
        return demandeurRepository.findById(id);
    }
    
    /**
     * Trouver un demandeur par son email
     */
    public Optional<DemandeurEntity> findByEmail(String email) {
        return demandeurRepository.findByEmail(email);
    }
    
    public Optional<DemandeurEntity> findTopByEmail(String email) {
        return demandeurRepository.findTopByEmail(email);
    }
    
    /**
     * Trouver un demandeur par son téléphone
     */
    public Optional<DemandeurEntity> findByTelephone(String telephone) {
        return demandeurRepository.findByTelephone(telephone);
    }
    
    /**
     * Supprimer un demandeur par son ID
     */
    @Transactional
    public void deleteById(Long id) {
        demandeurRepository.deleteById(id);
    }
    
    /**
     * Supprimer un demandeur par son email
     */
    @Transactional
    public void deleteByEmail(String email) {
        demandeurRepository.deleteByEmail(email);
    }
    
    /**
     * Vérifier si un demandeur existe par ID
     */
    public boolean existsById(int id) {
        return demandeurRepository.existsById(id);
    }

    public boolean existsByEmail(String email){
        return demandeurRepository.existsByEmail(email);
    }
    
    /**
     * Compter le nombre total de demandeurs
     */
    public long count() {
        return demandeurRepository.count();
    }
    
    // ========== RECHERCHES SIMPLES ==========
    
    /**
     * Rechercher par nom
     */
    public List<DemandeurEntity> findByNom(String nom) {
        return demandeurRepository.findByNom(nom);
    }
    
    /**
     * Rechercher par prénom
     */
    public List<DemandeurEntity> findByPrenom(String prenom) {
        return demandeurRepository.findByPrenom(prenom);
    }
    
    /**
     * Rechercher par nom et prénom
     */
    public List<DemandeurEntity> findByNomAndPrenom(String nom, String prenom) {
        return demandeurRepository.findByNomAndPrenom(nom, prenom);
    }
    
    /**
     * Rechercher par nom contenant (insensible à la casse)
     */
    public List<DemandeurEntity> searchByNomContaining(String nom) {
        return demandeurRepository.findByNomContainingIgnoreCase(nom);
    }
    
    /**
     * Rechercher par profession
     */
   
    /**
     * Rechercher par lieu de naissance
     */
    public List<DemandeurEntity> findByLieuNaissance(String lieuNaissance) {
        return demandeurRepository.findByLieuNaissance(lieuNaissance);
    }
    
    /**
     * Rechercher par date de naissance
     */
    public List<DemandeurEntity> findByDateNaissance(LocalDate dateNaissance) {
        return demandeurRepository.findByDateNaissance(dateNaissance);
    }
    
    /**
     * Rechercher par nationalité actuelle
     */
    
    /**
     * Rechercher par nationalité d'origine
     */
    public List<DemandeurEntity> findByNationalite(NationaliteEntity Nationalite) {
        return demandeurRepository.findByNationalite(Nationalite);
    }
   
    public DemandeurEntity findById(int id){
       return demandeurRepository.findById(id);
    }
    /**
   
    /**
     * Rechercher par situation familiale
     */
    public List<DemandeurEntity> findBySituationFamiliale(SituationFamilialeEntity SituationFamiliale) {
        return demandeurRepository.findBySituationFamiliale(SituationFamiliale);
    }
    
    // ========== RECHERCHES AVANCÉES ==========
    
    /**
     * Recherche multi-critères
     */
    public List<DemandeurEntity> searchDemandeurs(String nom, String prenom, String email) {
        return demandeurRepository.searchDemandeurs(nom, prenom, email);
    }
    
    /**
     * Recherche par mot-clé (nom ou prénom)
     */
    public List<DemandeurEntity> searchByKeyword(String keyword) {
        return demandeurRepository.searchByNomOrPrenom(keyword);
    }
    
    /**
     * Recherche par tranche d'âge
     */
    public List<DemandeurEntity> findByAgeRange(int ageMin, int ageMax) {
        LocalDate today = LocalDate.now();
        LocalDate dateMin = today.minusYears(ageMax);
        LocalDate dateMax = today.minusYears(ageMin);
        return demandeurRepository.findDemandeursByAgeRange(dateMin, dateMax);
    }
    
    /**
     * Trouver les demandeurs nés entre deux dates
     */
    public List<DemandeurEntity> findByDateNaissanceBetween(LocalDate startDate, LocalDate endDate) {
        return demandeurRepository.findByDateNaissanceBetween(startDate, endDate);
    }
    
  
    // ========== VALIDATIONS ==========
    
    /**
     * Valider un email
     */
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Vérifier si l'email existe déjà
     */
    public boolean emailExists(String email) {
        if (email == null) {
            return false;
        }
        return demandeurRepository.existsByEmail(email);
    }
    
    /**
     * Vérifier si le téléphone existe déjà
     */
    public boolean telephoneExists(String telephone) {
        if (telephone == null) {
            return false;
        }
        return demandeurRepository.existsByTelephone(telephone);
    }
    
    /**
     * Valider un demandeur avant création
     */
   
    // ========== MISE À JOUR ==========
    
    /**
     * Mettre à jour un demandeur
     */
    @Transactional
    public Optional<DemandeurEntity> update(Long id, DemandeurEntity updatedDemandeur) {
        return demandeurRepository.findById(id).map(existingDemandeur -> {
            existingDemandeur.setNom(updatedDemandeur.getNom());
            existingDemandeur.setPrenom(updatedDemandeur.getPrenom());
            existingDemandeur.setDateNaissance(updatedDemandeur.getDateNaissance());
            existingDemandeur.setLieuNaissance(updatedDemandeur.getLieuNaissance());
            existingDemandeur.setTelephone(updatedDemandeur.getTelephone());
            existingDemandeur.setEmail(updatedDemandeur.getEmail());
            existingDemandeur.setAdresse(updatedDemandeur.getAdresse());
            existingDemandeur.setSituationFamiliale(updatedDemandeur.getSituationFamiliale());
            existingDemandeur.setNationalite(updatedDemandeur.getNationalite());
            
            return demandeurRepository.save(existingDemandeur);
        });
    }
    
    /**
     * Mettre à jour partiellement un demandeur
     */
    @Transactional
    public Optional<DemandeurEntity> updatePartial(Long id, DemandeurEntity updatedDemandeur) {
        return demandeurRepository.findById(id).map(existingDemandeur -> {
            if (updatedDemandeur.getNom() != null) {
                existingDemandeur.setNom(updatedDemandeur.getNom());
            }
            if (updatedDemandeur.getPrenom() != null) {
                existingDemandeur.setPrenom(updatedDemandeur.getPrenom());
            }
            if (updatedDemandeur.getDateNaissance() != null) {
                existingDemandeur.setDateNaissance(updatedDemandeur.getDateNaissance());
            }
            if (updatedDemandeur.getLieuNaissance() != null) {
                existingDemandeur.setLieuNaissance(updatedDemandeur.getLieuNaissance());
            }
            if (updatedDemandeur.getTelephone() != null) {
                existingDemandeur.setTelephone(updatedDemandeur.getTelephone());
            }
            if (updatedDemandeur.getEmail() != null) {
                existingDemandeur.setEmail(updatedDemandeur.getEmail());
            }
            if (updatedDemandeur.getAdresse() != null) {
                existingDemandeur.setAdresse(updatedDemandeur.getAdresse());
            }
            if (updatedDemandeur.getSituationFamiliale() != null) {
                existingDemandeur.setSituationFamiliale(updatedDemandeur.getSituationFamiliale());
            }
            if (updatedDemandeur.getNationalite() != null) {
                existingDemandeur.setNationalite(updatedDemandeur.getNationalite());
            }
            
            return demandeurRepository.save(existingDemandeur);
        });
    }
    
    // ========== STATISTIQUES ==========
    
    /**
     * Compter les demandeurs par nationalité
     */
   
    /**
     * Calculer l'âge d'un demandeur
     */
    public int calculateAge(LocalDate dateNaissance) {
        if (dateNaissance == null) {
            return 0;
        }
        return LocalDate.now().getYear() - dateNaissance.getYear();
    }
    
    
    public static class ValidationResult {
        private boolean valid = true;
        private java.util.Map<String, String> errors = new java.util.HashMap<>();
        
        public void addError(String field, String message) {
            valid = false;
            errors.put(field, message);
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public java.util.Map<String, String> getErrors() {
            return errors;
        }
        
        public String getFirstError() {
            return errors.isEmpty() ? null : errors.values().iterator().next();
        }
    }
}