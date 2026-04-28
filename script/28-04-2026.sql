CREATE TABLE scan_fichier {
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_piece_demande int NOT NULL,
    chemin_fichier VARCHAR(250) NOT NULL,
    date_upload date not null,
    FOREIGN key (id_piece_demande) references piece_demande(id) 
};