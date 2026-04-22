CREATE TABLE piece (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL,
    id_type_visa int default null,
    FOREIGN key (id_type_visa) references type_visa(id) 
);

CREATE TABLE piece_demande(
    id int AUTO_INCREMENT PRIMARY KEY,
    id_piece int not null,
    id_demande int not null,
    FOREIGN Key (id_piece) references piece(id),
    FOREIGN key (id_demande) references demande(id)
);
-- Pièces Générales
INSERT INTO piece (libelle) VALUES ('02 photos');
INSERT INTO piece (libelle) VALUES ('Notice');
INSERT INTO piece (libelle) VALUES ('Demande adressee au ministre');
INSERT INTO piece (libelle) VALUES ('Photocopie visa');
INSERT INTO piece (libelle) VALUES ('Photocopie passeport');
INSERT INTO piece (libelle) VALUES ('Photocopie carte resident');
INSERT INTO piece (libelle) VALUES ('Certificat residence');
INSERT INTO piece (libelle) VALUES ('Casier judiciaire');

-- Pièces Spécifiques (Investisseur)
INSERT INTO piece (libelle,id_type_visa) VALUES ('Statut societe',1);
INSERT INTO piece (libelle,id_type_visa) VALUES ('Extrait RCS',1);
INSERT INTO piece (libelle,id_type_visa) VALUES ('Carte fiscale',1);

-- Pièces Spécifiques (Travailleur)
INSERT INTO piece (libelle,id_type_visa) VALUES ('Autorisation emploi',2);
INSERT INTO piece (libelle,id_type_visa) VALUES ('Attestation employeur',2);
