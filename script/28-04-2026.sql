CREATE TABLE scan_fichier {
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_piece_demande int NOT NULL,
    chemin_fichier VARCHAR(250) NOT NULL,
    date_upload date not null,
    FOREIGN key (id_piece_demande) references piece_demande(id) 
};
    
CREATE TABLE statut_visa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_visa INT NOT NULL,
    statut INT NOT NULL,-- 'non valide : 1', 'creer : 5', 'approuve : 30', ' rejeter : 40 '
    date_changement_statut DATE,
    FOREIGN KEY (id_visa) REFERENCES visa(id)
);
