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
    Valide les champs requis ():    
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


x ,creer une fonction pour valider les donnes 
(creer une fonction de validation par entites. ex: ValidateDemandeur , ensuite mettre tous les validate dans une fonction validate pour le post) 
x,validation des champs not null
   -pour tous les champs marqués NOT NULL dans la BDD ,creer des conditions if pour verifier qu il ne soit pas null
  x, - Si un champ NOT NULL est manquant ou vide → retourner erreur immédiate, ne pas continuer.
validation des champs specifiques
   x- Pas vide : nom, prenom, lieu_naissance, telephone, adresse
   x- Pas null : date_naissance, id_situation_familiale, id_nationalite
   - Format email valide (si email fourni)
   x- Vérifier que la date d'expiration du passeport > aujourd'hui
   x- Vérifier que date_sortie > date_entree pour visa_transformable

modifier fonction pour recevoir les données du formulaire (POST)
    utilisation des fonctions de validation de donne

    sauvegarde des entites
    - creer demandeur , passeport(statut automatiquement actif) si il n existe pas 
    - Créer l'entité VisaTransformable (id_demandeur, id_passeport(id du passeport qui a ete creer), date_entree, date_sortie, numero_reference)
    - Créer l'entité Demande (id_visa_transformable, date_demande, id_demandeur, id_passeport(id du passeport qui a ete creer), id_type_visa, id_type_demande, date_traitement = NULL)
    x- Créer l'entité StatutDemande (id_demande, statut = 1(creer), date_changement_statut)
    - si il y a une erreur retourner le message d erreur dans le formulaire avec les donnes pour ne plus remplir de nouveau le formulaire 
    - redirection vers page liste demande avec message succes


creer une fonction pour modifier les donnes d'une demande (POST)
    - reutiliser les fonctions de validation de donne 
    - instancier les entites et appeller les .update()
    demandeur , passeport , visatransformable , demande
    - si il y a une erreur retourner le message d erreur dans le formulaire avec les donnes pour ne plus remplir de nouveau le formulaire 
   - vérifier que l'entité existe avant de modifier
    - redirection vers page liste demande avec message succes

:::::::TODO 3

Créer page choix_type_demande( respecter le style existant) :
      - 4 boutons :
          1. Nouveau titre
          2. Demande de duplicata (perte carte résidence)
          3. Demande de transfert de visa (perte passeport)
          4. Demande de transfert de visa et duplicata (perte des deux)

modifier la redirection du lien nouvelle demande
      - Au clic sur "Nouvelle demande" -> redirection vers nouvel page choix_type_demande GET /choix-type

lorsqu on clique sur les booutons : 
    x,CAS 1 : Nouveau titre
        - rediriger vers le formulaire exitant (faire nouveau titre comme d habitude)

    CAS 2 : Demande de duplicata 
        - créer page recherche_par_numero :
            - mettre au dessus du formalaire le bouton : 
                - Bouton "Sans donné antérieur" 
            - Champ : "Numéro de carte résident"
            - Champ : "Numéro de visa"
            - Bouton "Rechercher" -> POST /duplicata/rechercher
            - Ca affiche la demande relier au numero , on selectinne cette demande 
            - et ensuite une bouton continuer s affiche et on se dirige vers la page resume duplicata avec les informaitons dont on a besoin dans cette page

        - créer page resume_duplicata :
            - Affiche les infos du titre trouvé (nom, prénom, numéro titre, date expiration)
            - Message " validation de demande de duplicata"
            - Bouton "Accepter" -> POST /duplicata/accepter
            - Après acceptation -> message "Demande terminée" 
            - redirection vers liste de demande avec message succes

    CAS 3 : Demande de transfert de visa 
        - utiliser page recherche_par_numero :
            - cette fois ci redirige vers page nouveau_passeport
            
        - créer page nouveau_passeport :
            - Affiche les infos du visa trouvé
            - Champ : "Nouveau numéro de passeport" (obligatoire)
            - Bouton "Valider" -> GET /transfert/save-passeport
    
        - créer page resume_transfert :
            - Affiche résumé : ancien visa, nouveau passeport, infos personne
            - Bouton "Accepter" -> POST /transfert/accepter
            - Après acceptation -> message "Demande terminée"
            - redirection vers liste de demande avec message succes

    CAS 4 : Transfert + duplicata 
            - rediriger page identique à CAS 3 jusqu'à acceptation
            - après acceptation du transfert -> charger automatiquement la page resume_duplicata (même numéro carte de resident)

    Formulaire "Sans donné antérieur" après clic 
            - rediriger vers formulaire_demande pour inserer les demandes
            - valider le formulaire avec champ caché provenance (DUPLICATA ou TRANSFERT ou les deux) 
            - après validation du formulaire -> retourner à la page resume correspondante (avec le nouveau numéro généré)

:::::::TODO 4

 Entité Demande & QR Code
    - Ajouter l'attribut `cheminQR` (string) à l'entité Demande.
    - Générer automatiquement un QR Code à la création d'une demande.(statut creer)
        - Contenu du QR : URL vers la fiche React (Frontend) de la demande.
        (recuperer le lien vers la fiche duw demande dans le projet frontend react)

 API GET /demandesRecherche
    - Endpoint : /demandesRecherche
    - Paramètres : numeroDemande (string, optionnel), numeroPasseport (string, optionnel)
    - Règles métier :
        x- Si les deux paramètres sont vides -> retourner [] (liste vide)
        x- Si numeroDemande fourni -> retourner la demande + toutes les demandes du même demandeur
        x- Si numeroPasseport fourni -> retourner toutes les demandes liées à ce passeport (tri chronologique)
    - Gestion d'erreur :
        x- 400 si les deux paramètres sont vides
        x- 404 si aucune demande trouvée (mais si liste vide, retourner 200 avec [] ? À clarifier : spec dit 404 si aucune trouvée)

 API GET /demandeDetails/{idDemande}
    - Endpoint : /demandeDetails/{idDemande}
    - Retourne :
        x- qrCode
        x- Etat civil
        x- Passeport
        x- Visa transformable
        x- Statut actuel
        x- Visa et carte de résidence (si statut = approuvé)

 API GET /demandeDetails/HistoStatut/{idDemande}
    - Endpoint : /demandeDetails/HistoStatut/{idDemande}
    - Retourne : historique complet des statuts (avec dates)

 API GET /demandeDetails/DetailsFichier/{idDemande}
    - Endpoint : /demandeDetails/DetailsFichier/{idDemande}
    - Retourne : liste des fichiers uploadés
        - Pour chaque fichier : nomFichier, apercu (base64 ou URL), statut (coché/non coché)
