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

INSERT INTO demandeur (nom, prenom, date_naissance, lieu_naissance, telephone, email, adresse, id_situation_familiale, id_nationalite) VALUES
('DIOP', 'Amadou', '1990-03-15', 'Dakar', '+221771234567', 'amadou.diop@email.com', '12 Rue de la Liberté, Dakar', 2, 2),
('MARTIN', 'Sophie', '1985-07-22', 'Lyon', '+33612345678', 'sophie.martin@email.com', '45 Avenue des Fleurs, Lyon', 1, 1),
('BENALI', 'Karim', '1988-11-05', 'Casablanca', '+212612345678', 'karim.benali@email.com', '8 Boulevard Mohammed V, Casablanca', 2, 3),
('TOURE', 'Fatoumata', '1995-02-28', 'Bamako', '+22370123456', 'fatou.toure@email.com', '23 Rue du Niger, Bamako', 1, 6),
('KONE', 'Ibrahim', '1982-09-10', 'Abidjan', '+22507123456', 'ibrahim.kone@email.com', '56 Rue des Jardins, Abidjan', 3, 7),
('DUPONT', 'Thomas', '1992-12-03', 'Paris', '+33698765432', 'thomas.dupont@email.com', '78 Rue de la République, Paris', 1, 1),
('MEHDI', 'Nadia', '1987-06-18', 'Tunis', '+21620123456', 'nadia.mehdi@email.com', '34 Avenue Habib Bourguiba, Tunis', 2, 4),
('FALL', 'Moussa', '1993-04-25', 'Saint-Louis', '+221778765432', 'moussa.fall@email.com', '15 Rue de la Paix, Saint-Louis', 1, 2),
('BENYOUCEF', 'Laila', '1991-08-14', 'Oran', '+213551234567', 'laila.benyoucef@email.com', '9 Rue Didouche Mourad, Oran', 4, 5),
('NGUYEN', 'Minh', '1989-10-30', 'Douala', '+237612345678', 'minh.nguyen@email.com', '67 Rue de lIndépendance, Douala', 2, 8);

INSERT INTO passeport (id_demandeur, numero_passeport, date_delivrance, date_expiration, pays_delivrance) VALUES
(1, 'PA1234567', '2020-01-10', '2030-01-09', 'Madagascar'),
(2, 'FR87654321', '2019-05-20', '2029-05-19', 'Madagascar'),
(3, 'MA9876543', '2021-03-15', '2031-03-14', 'Madagascar'),
(4, 'ML4567890', '2020-07-01', '2030-06-30', 'Madagascar'),
(5, 'CI2345678', '2018-11-25', '2028-11-24', 'Madagascar'),
(6, 'FR56789012', '2022-02-14', '2032-02-13', 'Madagascar'),
(7, 'TN3456789', '2019-09-30', '2029-09-29', 'Madagascar'),
(8, 'PA7890123', '2021-06-10', '2031-06-09', 'Madagascar'),
(9, 'DZ9012345', '2020-12-05', '2030-12-04', 'Madagascar'),
(10, 'CM6789012', '2022-04-18', '2032-04-17', 'Madagascar');

INSERT INTO statut_passeport (id_passeport, statut, date_changement_statut) VALUES
(1, 1, '2020-01-10'),  -- actif
(2, 1, '2019-05-20'),  -- actif
(3, 1, '2021-03-15'),  -- actif
(4, 3, '2023-08-15'),  -- perdu
(5, 1, '2018-11-25'),  -- actif
(6, 1, '2022-02-14'),  -- actif
(7, 2, '2024-01-10'),  -- expiré
(8, 1, '2021-06-10'),  -- actif
(9, 4, '2023-12-20'),  -- volé
(10, 1, '2022-04-18'); -- actif

    INSERT INTO type_visa (libelle) VALUES
    ('investisseur'),
    ('travailleur');

    INSERT INTO type_demande (libelle) VALUES
    ('duplicata'),
    ('transformation');
    INSERT INTO type_demande (libelle) VALUES
    ('transfert de visa');

INSERT INTO visa_transformable (id_demandeur, id_passeport, date_entree, date_sortie, numero_reference) VALUES
(1, 1, '2024-01-15', '2024-06-15', 'VISA-2024-001'),
(2, 2, '2024-02-01', '2024-08-01', 'VISA-2024-002'),
(3, 3, '2024-01-20', '2024-07-20', 'VISA-2024-003'),
(4, 4, '2023-12-10', '2024-05-10', 'VISA-2024-004'),
(5, 5, '2024-03-05', '2024-09-05', 'VISA-2024-005'),
(6, 6, '2024-01-25', '2024-07-25', 'VISA-2024-006'),
(8, 8, '2024-02-15', '2024-08-15', 'VISA-2024-007'),
(10, 10, '2024-03-10', '2024-09-10', 'VISA-2024-008');