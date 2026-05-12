CREATE TABLE nationalite (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL
);

CREATE TABLE situation_familiale (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL
);

CREATE TABLE demandeur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    date_naissance DATE NOT NULL,
    lieu_naissance VARCHAR(100),
    telephone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    adresse TEXT NOT NULL,
    id_situation_familiale INT NOT NULL,
    id_nationalite INT NOT NULL,
    FOREIGN KEY (id_situation_familiale) REFERENCES situation_familiale(id),
    FOREIGN KEY (id_nationalite) REFERENCES nationalite(id)
);

CREATE TABLE passeport (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demandeur INT NOT NULL,
    numero_passeport VARCHAR(50) NOT NULL UNIQUE,
    date_delivrance DATE NOT NULL,
    date_expiration DATE NOT NULL,
    pays_delivrance VARCHAR(100),
    FOREIGN KEY (id_demandeur) REFERENCES demandeur(id)
);

CREATE TABLE statut_passeport (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_passeport INT NOT NULL,
    statut INT NOT NULL ,-- 'actif', 'expire', 'perdu', 'volee'
    date_changement_statut DATE,
    FOREIGN KEY (id_passeport) REFERENCES passeport(id)
);
 
CREATE TABLE visa_transformable(
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demandeur INT NOT NULL,
    id_passeport INT NOT NULL,
    date_entree DATE NOT NULL,
    date_sortie DATE NOT NULL,
    numero_reference VARCHAR(50) NOT NULL UNIQUE,
    FOREIGN KEY (id_demandeur) REFERENCES demandeur(id),
    FOREIGN KEY (id_passeport) REFERENCES passeport(id)
);

CREATE TABLE type_visa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL  -- 'investisseur', 'travailleur'
);

CREATE TABLE type_demande (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL  -- duplicata, transformation
);

CREATE TABLE demande (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_visa_transformable INT NOT NULL,
    date_demande DATE NOT NULL,
    id_demandeur INT NOT NULL,
    id_type_visa INT NOT NULL,
    id_type_demande INT NOT NULL,
    date_traitement DATE,
    FOREIGN KEY (id_type_demande) REFERENCES type_demande(id),
    FOREIGN KEY (id_demandeur) REFERENCES demandeur(id),
    FOREIGN KEY (id_type_visa) REFERENCES type_visa(id),
    FOREIGN KEY (id_visa_transformable) REFERENCES visa_transformable(id)
);

CREATE TABLE statut_demande (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demande INT NOT NULL,
    statut INT NOT NULL,-- 'creer : 1', 'en_cours_scan: 10','photo et signature termine: 15', 'scanne : 20 ', 'approuve : 30', ' rejeter : 40 '
    date_changement_statut DATE,
    FOREIGN KEY (id_demande) REFERENCES demande(id)
);

CREATE TABLE visa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demande INT NOT NULL,
    reference VARCHAR(50),
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    id_passeport INT NOT NULL,
    FOREIGN KEY (id_passeport) REFERENCES passeport(id),
    FOREIGN KEY (id_demande) REFERENCES demande(id)
);

CREATE TABLE carte_resident (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demande INT NOT NULL,
    reference VARCHAR(50) NOT NULL UNIQUE,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    id_passeport INT NOT NULL,
    FOREIGN KEY (id_passeport) REFERENCES passeport(id),
    FOREIGN KEY (id_demande) REFERENCES demande(id)
);

