CREATE TABLE photo_signature_demande (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_demande INT NOT NULL,
    photo_url VARCHAR(250) NOT NULL,
    signature_url VARCHAR(250) NOT NULL,
    date_upload date not null,
    FOREIGN KEY (id_demande) REFERENCES demande(id)
);