package com.project.visa.service;

import com.project.visa.entity.DemandeurEntity;
import com.project.visa.repository.DemandeurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        // Vérifications avant sauvegarde
        if (demandeurEntity.getCreatedAt() == null) {
            demandeurEntity.setCreatedAt(LocalDateTime.now());
        }
        demandeurEntity.setUpdatedAt(LocalDateTime.now());
        
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
    public boolean existsById(Long id) {
        return demandeurRepository.existsById(id);
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
    public List<DemandeurEntity> findByProfession(String profession) {
        return demandeurRepository.findByProfession(profession);
    }
    
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
    public List<DemandeurEntity> findByNationaliteActuelle(Long idNationalite) {
        return demandeurRepository.findByIdNationaliteActuelle(idNationalite);
    }
    
    /**
     * Rechercher par nationalité d'origine
     */
    public List<DemandeurEntity> findByNationaliteOrigine(Long idNationalite) {
        return demandeurRepository.findByIdNationaliteOrigine(idNationalite);
    }
    
    /**
     * Rechercher par genre
     */
    public List<DemandeurEntity> findByGenre(Long idGenre) {
        return demandeurRepository.findByIdGenre(idGenre);
    }
    
    /**
     * Rechercher par situation familiale
     */
    public List<DemandeurEntity> findBySituationFamiliale(Long idSituationFamiliale) {
        return demandeurRepository.findByIdSituationFamiliale(idSituationFamiliale);
    }
    
    // ========== RECHERCHES AVANCÉES ==========
    
    /**
     * Recherche multi-critères
     */
    public List<DemandeurEntity> searchDemandeurs(String nom, String prenom, String email, String profession) {
        return demandeurRepository.searchDemandeurs(nom, prenom, email, profession);
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
    
    /**
     * Trouver les 10 derniers demandeurs inscrits
     */
    public List<DemandeurEntity> findTop10Recent() {
        return demandeurRepository.findTop10ByOrderByCreatedAtDesc();
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
    public ValidationResult validateDemandeur(DemandeurEntity demandeur) {
        ValidationResult result = new ValidationResult();
        
        // Vérifier les champs obligatoires
        if (demandeur.getNom() == null || demandeur.getNom().trim().isEmpty()) {
            result.addError("nom", "Le nom est obligatoire");
        }
        
        if (demandeur.getPrenom() == null || demandeur.getPrenom().trim().isEmpty()) {
            result.addError("prenom", "Le prénom est obligatoire");
        }
        
        if (demandeur.getDateNaissance() == null) {
            result.addError("dateNaissance", "La date de naissance est obligatoire");
        } else if (demandeur.getDateNaissance().isAfter(LocalDate.now())) {
            result.addError("dateNaissance", "La date de naissance ne peut pas être dans le futur");
        }
        
        if (demandeur.getLieuNaissance() == null || demandeur.getLieuNaissance().trim().isEmpty()) {
            result.addError("lieuNaissance", "Le lieu de naissance est obligatoire");
        }
        
        if (demandeur.getProfession() == null || demandeur.getProfession().trim().isEmpty()) {
            result.addError("profession", "La profession est obligatoire");
        }
        
        if (demandeur.getTelephone() == null || demandeur.getTelephone().trim().isEmpty()) {
            result.addError("telephone", "Le téléphone est obligatoire");
        } else if (telephoneExists(demandeur.getTelephone())) {
            result.addError("telephone", "Ce numéro de téléphone est déjà utilisé");
        }
        
        if (demandeur.getAdresse() == null || demandeur.getAdresse().trim().isEmpty()) {
            result.addError("adresse", "L'adresse est obligatoire");
        }
        
        // Vérifier email
        if (demandeur.getEmail() == null || demandeur.getEmail().trim().isEmpty()) {
            result.addError("email", "L'email est obligatoire");
        } else if (!isValidEmail(demandeur.getEmail())) {
            result.addError("email", "Le format de l'email est invalide");
        } else if (emailExists(demandeur.getEmail())) {
            result.addError("email", "Cet email est déjà utilisé");
        }
        
        // Vérifier les IDs de références
        if (demandeur.getIdSituationFamiliale() == null) {
            result.addError("idSituationFamiliale", "La situation familiale est obligatoire");
        }
        
        if (demandeur.getIdNationaliteActuelle() == null) {
            result.addError("idNationaliteActuelle", "La nationalité actuelle est obligatoire");
        }
        
        if (demandeur.getIdNationaliteOrigine() == null) {
            result.addError("idNationaliteOrigine", "La nationalité d'origine est obligatoire");
        }
        
        if (demandeur.getIdGenre() == null) {
            result.addError("idGenre", "Le genre est obligatoire");
        }
        
        return result;
    }
    
    // ========== MISE À JOUR ==========
    
    /**
     * Mettre à jour un demandeur
     */
    @Transactional
    public Optional<DemandeurEntity> update(Long id, DemandeurEntity updatedDemandeur) {
        return demandeurRepository.findById(id).map(existingDemandeur -> {
            existingDemandeur.setNom(updatedDemandeur.getNom());
            existingDemandeur.setPrenom(updatedDemandeur.getPrenom());
            existingDemandeur.setNomJeuneFille(updatedDemandeur.getNomJeuneFille());
            existingDemandeur.setDateNaissance(updatedDemandeur.getDateNaissance());
            existingDemandeur.setLieuNaissance(updatedDemandeur.getLieuNaissance());
            existingDemandeur.setProfession(updatedDemandeur.getProfession());
            existingDemandeur.setTelephone(updatedDemandeur.getTelephone());
            existingDemandeur.setEmail(updatedDemandeur.getEmail());
            existingDemandeur.setAdresse(updatedDemandeur.getAdresse());
            existingDemandeur.setIdSituationFamiliale(updatedDemandeur.getIdSituationFamiliale());
            existingDemandeur.setIdNationaliteActuelle(updatedDemandeur.getIdNationaliteActuelle());
            existingDemandeur.setIdNationaliteOrigine(updatedDemandeur.getIdNationaliteOrigine());
            existingDemandeur.setIdGenre(updatedDemandeur.getIdGenre());
            existingDemandeur.setUpdatedAt(LocalDateTime.now());
            
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
            if (updatedDemandeur.getNomJeuneFille() != null) {
                existingDemandeur.setNomJeuneFille(updatedDemandeur.getNomJeuneFille());
            }
            if (updatedDemandeur.getDateNaissance() != null) {
                existingDemandeur.setDateNaissance(updatedDemandeur.getDateNaissance());
            }
            if (updatedDemandeur.getLieuNaissance() != null) {
                existingDemandeur.setLieuNaissance(updatedDemandeur.getLieuNaissance());
            }
            if (updatedDemandeur.getProfession() != null) {
                existingDemandeur.setProfession(updatedDemandeur.getProfession());
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
            if (updatedDemandeur.getIdSituationFamiliale() != null) {
                existingDemandeur.setIdSituationFamiliale(updatedDemandeur.getIdSituationFamiliale());
            }
            if (updatedDemandeur.getIdNationaliteActuelle() != null) {
                existingDemandeur.setIdNationaliteActuelle(updatedDemandeur.getIdNationaliteActuelle());
            }
            if (updatedDemandeur.getIdNationaliteOrigine() != null) {
                existingDemandeur.setIdNationaliteOrigine(updatedDemandeur.getIdNationaliteOrigine());
            }
            if (updatedDemandeur.getIdGenre() != null) {
                existingDemandeur.setIdGenre(updatedDemandeur.getIdGenre());
            }
            
            existingDemandeur.setUpdatedAt(LocalDateTime.now());
            return demandeurRepository.save(existingDemandeur);
        });
    }
    
    // ========== STATISTIQUES ==========
    
    /**
     * Compter les demandeurs par nationalité
     */
    public List<Object[]> countByNationalite() {
        return demandeurRepository.countDemandeursByNationalite();
    }
    
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