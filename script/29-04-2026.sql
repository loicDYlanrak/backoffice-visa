INSERT INTO scan_fichier (chemin_fichier, date_upload, id_piece_demande) VALUES
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-04-28', 2),
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-04-28', 3),
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-04-28', 4),
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-04-28', 5),
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-04-28', 6),
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-04-28', 7),
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-04-28', 8),
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-04-28', 9),
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-04-28', 10),
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-04-28', 11);

INSERT INTO scan_fichier (chemin_fichier, date_upload, id_piece_demande) VALUES
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-05-05', 10),
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-05-05', 11),
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-05-05', 12),
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-05-05', 13),
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-05-05', 14),
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-05-05', 15),
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-05-05', 16),
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-05-05', 17),
('uploads\\demande_1\\fd428bd3-3352-4750-a1d3-d99f0778fac6.pdf', '2026-05-05', 18);

-- Insertion de tests simples
INSERT INTO test_entity (name, description, status, created_at, updated_at) 
VALUES 
('Test de connexion', 'Vérification de la connexion à la base de données', 1, NOW(), NOW()),
('Test API', 'Test des endpoints REST', 1, NOW(), NOW()),
('Test performance', 'Vérification des temps de réponse', 1, NOW(), NOW()),
('Test sécurité', 'Test des authentifications et autorisations', 2, NOW(), NOW()),
('Test validation', 'Validation des formulaires et données', 1, NOW(), NOW());



