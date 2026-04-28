INSERT INTO nationalite (libelle) VALUES
('Française'),
('Sénégalaise'),
('Marocaine'),
('Tunisienne'),
('Algérienne'),
('Malienne'),
('Ivoirienne'),
('Camerounaise'),
('Congolaise'),
('Mauritanienne');

INSERT INTO situation_familiale (libelle) VALUES
('Célibataire'),
('Marié(e)'),
('Divorcé(e)'),
('Veuf/Veuve'),
('Pacsé(e)'),
('En concubinage');

INSERT INTO type_visa (libelle) VALUES
('investisseur'),
('travailleur');

INSERT INTO type_demande (libelle) VALUES
('duplicata'),
('transformation');

INSERT INTO type_demande (libelle) VALUES
('transfert de visa');

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