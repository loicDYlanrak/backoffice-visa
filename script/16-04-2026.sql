
CREATE TABLE Genre (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL
);

CREATE TABLE Nationalite (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL
);

CREATE TABLE Situation_familiale (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL
);

CREATE TABLE type_visa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL  -- 'investisseur', 'travailleur'
);

CREATE TABLE Statut_demande (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL  -- 'brouillon', 'soumise', 'en_cours', 'validee', 'rejetee'
);

CREATE TABLE Passeport (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero_passeport VARCHAR(50) NOT NULL UNIQUE,
    date_delivrance DATE NOT NULL,
    date_expiration DATE NOT NULL,
    pays_delivrance VARCHAR(100)
);

CREATE TABLE Visa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero_visa VARCHAR(50),
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    transformable BOOLEAN DEFAULT TRUE,
    id_type_visa INT NOT NULL,
    id_passeport INT NOT NULL,
    FOREIGN KEY (id_type_visa) REFERENCES type_visa(id),
    FOREIGN KEY (id_passeport) REFERENCES Passeport(id)
);

CREATE TABLE Demandeur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    nom_jeune_fille VARCHAR(50),
    date_naissance DATE NOT NULL,
    lieu_naissance VARCHAR(100) NOT NULL,
    profession VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    adresse TEXT NOT NULL,
    
    id_situation_familiale INT NOT NULL,
    id_nationalite_actuelle INT NOT NULL,
    id_nationalite_origine INT NOT NULL,
    id_genre INT NOT NULL,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (id_situation_familiale) REFERENCES Situation_familiale(id),
    FOREIGN KEY (id_nationalite_actuelle) REFERENCES Nationalite(id),
    FOREIGN KEY (id_nationalite_origine) REFERENCES Nationalite(id),
    FOREIGN KEY (id_genre) REFERENCES Genre(id)
);

CREATE TABLE Document_requis (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(255) NOT NULL,
    est_commun BOOLEAN DEFAULT FALSE,  -- TRUE = pièce commune
    id_type_visa INT NULL,              -- NULL = commun, sinon spécifique au type
    FOREIGN KEY (id_type_visa) REFERENCES type_visa(id)
);

CREATE TABLE Demande (
    id INT AUTO_INCREMENT PRIMARY KEY,
    reference VARCHAR(50) UNIQUE NOT NULL,  -- Référence unique pour recherche
    date_demande DATE NOT NULL,
    id_statut INT NOT NULL,
    id_demandeur INT NOT NULL,
    id_visa INT NOT NULL,
    observations TEXT,
    date_traitement DATE,
    motif_rejet TEXT,
    
    FOREIGN KEY (id_statut) REFERENCES Statut_demande(id),
    FOREIGN KEY (id_demandeur) REFERENCES Demandeur(id),
    FOREIGN KEY (id_visa) REFERENCES Visa(id)
);
/* 
CREATE TABLE Demande_document (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demande INT NOT NULL,
    id_document_requis INT NOT NULL,
    est_fourni BOOLEAN DEFAULT FALSE,
    chemin_fichier VARCHAR(255),
    date_upload TIMESTAMP NULL,
    
    FOREIGN KEY (id_demande) REFERENCES Demande(id) ON DELETE CASCADE,
    FOREIGN KEY (id_document_requis) REFERENCES Document_requis(id),
    UNIQUE KEY unique_demande_document (id_demande, id_document_requis)
); */

CREATE TABLE Histo_statut_demande (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demande INT NOT NULL,
    id_statut_ancien INT,
    id_statut_nouveau INT NOT NULL,
    date_changement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    commentaire VARCHAR(255),
    
    FOREIGN KEY (id_demande) REFERENCES Demande(id) ON DELETE CASCADE,
    FOREIGN KEY (id_statut_ancien) REFERENCES Statut_demande(id),
    FOREIGN KEY (id_statut_nouveau) REFERENCES Statut_demande(id)
);

CREATE TABLE Demandeur_Passeport (
    id_demandeur INT NOT NULL,
    id_passeport INT NOT NULL,
    est_actif BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id_demandeur, id_passeport),
    FOREIGN KEY (id_demandeur) REFERENCES Demandeur(id) ON DELETE CASCADE,
    FOREIGN KEY (id_passeport) REFERENCES Passeport(id) ON DELETE CASCADE
);

INSERT INTO Genre (libelle) VALUES ('Masculin'), ('Féminin'), ('Autre');

INSERT INTO Situation_familiale (libelle) VALUES 
('Célibataire'), ('Marié(e)'), ('Divorcé(e)'), ('Veuf/Veuve'), ('Pacsé(e)');

INSERT INTO type_visa (libelle) VALUES ('investisseur'), ('travailleur');

INSERT INTO Statut_demande (libelle) VALUES 
('brouillon'), ('soumise'), ('en_cours'), ('validee'), ('rejetee');

INSERT INTO Nationalite (libelle) VALUES 
('Malgache'), ('Française'), ('Chinoise'), ('Américaine'), ('Canadienne'), 
('Allemande'), ('Italienne'), ('Espagnole'), ('Britannique'), ('Belge');

INSERT INTO Document_requis (libelle, est_commun, id_type_visa) VALUES
('02 photos d''identité', TRUE, NULL),
('Notice de renseignement', TRUE, NULL),
('Demande adressée à Mr le Ministère de l''Intérieur et de la Décentralisation avec e-mail et téléphone', TRUE, NULL),
('Photocopie certifiée du visa en cours de validité', TRUE, NULL),
('Photocopie certifiée de la première page du passeport', TRUE, NULL),
('Photocopie certifiée de la carte résident en cours de validité', TRUE, NULL),
('Certificat de résidence à Madagascar', TRUE, NULL),
('Extrait de casier judiciaire moins de 3 mois', TRUE, NULL);

INSERT INTO Document_requis (libelle, est_commun, id_type_visa) VALUES
('Statut de la Société', FALSE, (SELECT id FROM type_visa WHERE libelle = 'investisseur')),
('Extrait d''inscription au registre de commerce', FALSE, (SELECT id FROM type_visa WHERE libelle = 'investisseur')),
('Carte fiscale', FALSE, (SELECT id FROM type_visa WHERE libelle = 'investisseur'));

INSERT INTO Document_requis (libelle, est_commun, id_type_visa) VALUES
('Autorisation emploi délivrée à Madagascar par le Ministère de la Fonction publique', FALSE, (SELECT id FROM type_visa WHERE libelle = 'travailleur')),
('Attestation d''emploi délivré par l''employeur (Original)', FALSE, (SELECT id FROM type_visa WHERE libelle = 'travailleur'));

