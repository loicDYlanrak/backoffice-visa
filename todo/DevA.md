Dev: Nofy

:::::::TODO 1

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

creer fonction pour recevoir les données du formulaire (POST): done
    Valide les champs requis (*):    
        pas vide: pour nom, prenom, lieuNaissance, profession, telephone, adresse
        pas null: pour dateNaissance, idSituationFamiliale, idNationaliteActuelle, idNationaliteOrigine, idGenre
        format email: pour email
        Vérifier que la date d'expiration du passeport est postérieure à aujourd'hui
        Vérifier que la date de fin du visa est après la date de début

    Sauvegarde en BDD
        - Vérifier si le demandeur existe déjà (par numéro de passeport ou email/téléphone)
        x- Créer l'entité Passeport(Vérifier que le passeport n'existe pas déjà x(passeportRepository.findByNumeroPasseport())) : set numero, dateDelivrance, dateExpiration, paysDelivrance, puis passeportRepository.save()
        x- Créer l'entité Visa : set numeroVisa, dateDebut, dateFin, idTypeVisa, idPasseport, puis visaRepository.save()
        x- Créer l'entité Demandeur (Verifier que le demandeur n'existe pas deja): set nom, prenom, nomJeuneFille, dateNaissance, lieuNaissance, profession, telephone, email, adresse, les ids des foreign keys, puis demandeurRepository.save()
        - Lier Demandeur et Passeport : créer entrée dans DemandeurPasseport (id_demandeur, id_passeport, est_actif = true)
        x- Créer l'entité Demande : générer la référence, set date_demande = LocalDate.now(), id_statut = 2 (soumise), id_demandeur, id_visa

    retourne JSON (succès/erreur + référence)
    Route POST /demande 

Endpoint GET /listes
    Retourne toutes les demandes (id, reference, nom, prenom, date_creation, type_visa)

Endpoint GET /recherche?ref=XXX
    Recherche par référence (retourne la demande complète)

:::::::TODO 2

x ,inserer les donnes dans le script 18-04-2026.sql


creer une fonction pour valider les donnes 
(creer une fonction de validation par entites. ex: ValidateDemandeur , ensuite mettre tous les validate dans une fonction validate pour le post) 
validation des champs not null
   -pour tous les champs marqués NOT NULL dans la BDD ,creer des conditions if pour verifier qu il ne soit pas null
   - Si un champ NOT NULL est manquant ou vide → retourner erreur immédiate, ne pas continuer.
validation des champs specifiques
   - Pas vide : nom, prenom, lieu_naissance, telephone, adresse
   - Pas null : date_naissance, id_situation_familiale, id_nationalite
   - Format email valide (si email fourni)
   - Vérifier que la date d'expiration du passeport > aujourd'hui
   - Vérifier que date_sortie > date_entree pour visa_transformable

modifier fonction pour recevoir les données du formulaire (POST)
    utilisation des fonctions de validation de donne

    sauvegarde des entites
    - creer demandeur , passeport(statut automatiquement actif) si il n existe pas 
    - Créer l'entité VisaTransformable (id_demandeur, id_passeport(id du passeport qui a ete creer), date_entree, date_sortie, numero_reference)
    - Créer l'entité Demande (id_visa_transformable, date_demande, id_demandeur, id_passeport(id du passeport qui a ete creer), id_type_visa, id_type_demande, date_traitement = NULL)
    - Créer l'entité StatutDemande (id_demande, statut = 1(creer), date_changement_statut)
    - si il y a une erreur retourner le message d erreur dans le formulaire avec les donnes pour ne plus remplir de nouveau le formulaire 
    - redirection vers page liste demande avec message succes


creer une fonction pour modifier les donnes d'une demande (POST)
    - reutiliser les fonctions de validation de donne 
    - instancier les entites et appeller les .update()
    demandeur , passeport , visatransformable , demande
    - si il y a une erreur retourner le message d erreur dans le formulaire avec les donnes pour ne plus remplir de nouveau le formulaire 
   - vérifier que l'entité existe avant de modifier
    - redirection vers page liste demande avec message succes

