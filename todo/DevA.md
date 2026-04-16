utiliser script_create_database.sql
(CREATE DATABASE )

utiliser script,done

test Connexion à MySQL/MariaDB  done

test d'utilisation de spring boot par le crud de test ,done

creer Controller DemandeController 

creer fonction qui Génère une référence unique (ex: RES-2026-0001),done
    dans DemandeService, créer une méthode genererReference()
    Récupérer l'année courante : int annee = LocalDate.now().getYear();
    Compter les demandes: long compteur = demandeRepository.count(); (créer la méthode dans Repository)
    Formater le compteur sur 4 chiffres : String num = String.format("%04d", compteur + 1);
    Retourner "RES-" + annee + "-" + num → ex: RES-2026-0001
    Gérer le cas où aucune demande n'existe encore (compteur = 0)

creer fonction pour recevoir les données du formulaire (POST):
    Valide les champs requis (*):    
        pas vide: pour nom, prenom, lieuNaissance, profession, telephone, adresse
        pas null: pour dateNaissance, idSituationFamiliale, idNationaliteActuelle, idNationaliteOrigine, idGenre
        format email: pour email
        Vérifier que la date d'expiration du passeport est postérieure à aujourd'hui
        Vérifier que la date de fin du visa est après la date de début

    Sauvegarde en BDD
        - Vérifier si le demandeur existe déjà (par numéro de passeport ou email/téléphone)
        - Créer l'entité Passeport(Vérifier que le passeport n'existe pas déjà (passeportRepository.findByNumeroPasseport())) : set numero, dateDelivrance, dateExpiration, paysDelivrance, puis passeportRepository.save()
        - Créer l'entité Visa : set numeroVisa, dateDebut, dateFin, idTypeVisa, idPasseport, puis visaRepository.save()
        - Créer l'entité Demandeur (Verifier que le demandeur n'existe pas deja): set nom, prenom, nomJeuneFille, dateNaissance, lieuNaissance, profession, telephone, email, adresse, les ids des foreign keys, puis demandeurRepository.save()
        - Lier Demandeur et Passeport : créer entrée dans DemandeurPasseport (id_demandeur, id_passeport, est_actif = true)
        - Créer l'entité Demande : générer la référence, set date_demande = LocalDate.now(), id_statut = 2 (soumise), id_demandeur, id_visa

    retourne JSON (succès/erreur + référence)
    Route POST /demande 

Endpoint GET /listes
    Retourne toutes les demandes (id, reference, nom, prenom, date_creation, type_visa)

Endpoint GET /recherche?ref=XXX
    Recherche par référence (retourne la demande complète)