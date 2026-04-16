<<<<<<< Updated upstream

CREATE TABLE Genre (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL
);

=======
>>>>>>> Stashed changes
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


CREATE TABLE Passeport (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demandeur INT NOT NULL,
    numero_passeport VARCHAR(50) NOT NULL UNIQUE,
    date_delivrance DATE NOT NULL,
    date_expiration DATE NOT NULL,
    pays_delivrance VARCHAR(100),
    FOREIGN KEY (id_demandeur) REFERENCES Demandeur(id)
);

CREATE TABLE Statut_passeport (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_passeport INT NOT NULL,
    statut INT NOT NULL ,-- 'actif', 'expire', 'perdu', 'volee'
    date_changement_statut DATE,
    FOREIGN KEY (id_passeport) REFERENCES Passeport(id)
);

CREATE TABLE Visa_transformable(
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demandeur INT NOT NULL,
    id_passeport INT NOT NULL,
    numero_reference VARCHAR(50) NOT NULL UNIQUE,
    FOREIGN KEY (id_demandeur) REFERENCES Demandeur(id),
    FOREIGN KEY (id_passeport) REFERENCES Passeport(id)
);

CREATE TABLE Visa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demande INT NOT NULL,
    reference VARCHAR(50),
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    id_passeport INT NOT NULL,
<<<<<<< Updated upstream
    FOREIGN KEY (id_type_visa) REFERENCES type_visa(id),
=======
    FOREIGN KEY (id_passeport) REFERENCES Passeport(id),
    FOREIGN KEY (id_demande) REFERENCES Demande(id)
);

CREATE TABLE carte_resident(
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demande INT NOT NULL,
    reference VARCHAR(50),
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    id_passeport INT NOT NULL,
    FOREIGN KEY (id_demande) REFERENCES Demande(id),
>>>>>>> Stashed changes
    FOREIGN KEY (id_passeport) REFERENCES Passeport(id)
);

CREATE TABLE Demandeur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    date_naissance DATE NOT NULL,
    lieu_naissance VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    adresse TEXT NOT NULL,
    
    id_situation_familiale INT NOT NULL,
    id_nationalite INT NOT NULL,
    
    FOREIGN KEY (id_situation_familiale) REFERENCES Situation_familiale(id),
    FOREIGN KEY (id_nationalite) REFERENCES Nationalite(id)
);

CREATE TABLE Type_demande (
    id INT AUTO_INCREMENT PRIMARY KEY,
<<<<<<< Updated upstream
    libelle VARCHAR(255) NOT NULL,
    est_commun BOOLEAN DEFAULT FALSE,  -- TRUE = pièce commune
    id_type_visa INT NULL,              -- NULL = commun, sinon spécifique au type
    FOREIGN KEY (id_type_visa) REFERENCES type_visa(id)
=======
    libelle VARCHAR(50) NOT NULL  
>>>>>>> Stashed changes
);


CREATE TABLE Demande (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_visa_transformable INT NOT NULL,
    date_demande DATE NOT NULL,
    id_statut INT NOT NULL,
    id_demandeur INT NOT NULL,
    id_type_visa INT NOT NULL,
    id_type_demande INT NOT NULL,
    date_traitement DATE,
    FOREIGN KEY (id_type_demande) REFERENCES Type_demande(id),
    FOREIGN KEY (id_demandeur) REFERENCES Demandeur(id),
    FOREIGN KEY (id_type_visa) REFERENCES Type_visa(id),
    FOREIGN KEY (id_visa_transformable) REFERENCES Visa_transformable(id)
);

CREATE TABLE Statut_demande (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demande INT NOT NULL,
    statut INT NOT NULL,-- 'brouillon', 'soumise', 'en_cours', 'validee', 'rejetee'
    date_changement_statut DATE,
    FOREIGN KEY (id_demande) REFERENCES Demande(id)
);

<<<<<<< Updated upstream
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

=======
>>>>>>> Stashed changes
