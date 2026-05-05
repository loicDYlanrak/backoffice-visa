Dev: Erica

:::::::TODO 1

-- creer page d'accueil (index ou accueil) : respecter le style existant

    Titre ,Sous-titre 
    Menu : [Accueil] [Nouvelle demande] [Liste des demandes]

-- creer page formulaire_demande 

    Champs obligatoires (*) :
        nom, prenom, date_naissance, lieu_naissance, situation_familiale (select),
        nationalite_actuelle, nationalite_origine, profession, contact, adresse

    Champs optionnels :
        nom_jeune_fille, mail

    Select type de visa : Investisseur / Travailleur

    Section PIÈCES COMMUNES (cases à cocher) :
        02 photos, Notice, Demande adressée ministre, Photocopie visa, Photocopie passeport,
        Photocopie carte résident, Certificat résidence, Casier judiciaire

    Section PIÈCES SPÉCIFIQUES (affichage dynamique selon type visa) :
        Investisseur → Statut société, Extrait RCS, Carte fiscale
        Travailleur → Autorisation emploi, Attestation employeur

    JavaScript (ou jQuery)
        Afficher/masquer les pièces spécifiques au changement du type visa
        Validation côté client (champs requis, email)

    Envoi du formulaire par POST 
    Afficher la référence retournée (Votre demande N° RES-2026-001 a été enregistrée)
    redirection vers la page de liste de demande


-- creer page liste demande
    Afficher tableau : Référence | Nom complet | Type visa | Date  

    Champ de recherche par référence

:::::::TODO 2

modifier le formulaires de demande 

voici les champs obligatoires (*)
   - nom *
   - prenom *
   - date_naissance *
   - lieu_naissance *
   - situation_familiale (select depuis table situation_familiale) *
   - nationalite (select depuis table nationalite) *
   - contact *
   - adresse *
note: pour les select recuperer les donnes depuis la base de donnes et boucler dans le formulaire 

champs optionnels
   - email

verifie les champs de la section passeport
   - numero_passeport *
   - date_delivrance *
   - date_expiration *
   - pays_delivrance 

ajouter section pour entrer les informations concernant le visa transformable
   - numero_reference *
   - date_entree *
   - date_sortie *

   - Select type_demande (duplicata / transformation) – valeurs depuis table type_demande *

verifier les champs de la piece commune (cases à cocher)
   - 02 photos
   - Notice
   - Demande adressée ministre
   - Photocopie visa
   - Photocopie passeport
   - Photocopie carte résident
   - Certificat résidence
   - Casier judiciaire

   - Select type_visa (Investisseur / Travailleur) – valeurs depuis table type_visa *

piece specifique selon le type de visa (affichage dynamique selon type_visa)
   - Investisseur → Statut société, Extrait RCS, Carte fiscale
   - Travailleur → Autorisation emploi, Attestation employeur

validation javascript 
   - Vérifier champs obligatoires non vides
   - Vérifier format email
   - Vérifier date_expiration > aujourd'hui
   - Vérifier date_sortie > date_entree

dans la page du formulaire ajouter une partie pour afficher les erreur si il y a des meesage d erreur lors de la saisie
dans la page liste de demande
    - ajouter une partie en haut pour afficher un message(de succes ou d erreur) de sauvegarde du demande

modification de la page liste de demande 
 - ajoute colonne status pour afficher le status d une demande (creer si 1 )
 - ajouter colonne "Actions" avec bouton "Modifier"
   - Cliquer sur Modifier → redirection vers le formulaire pré-rempli avec les données de la demande
   - Permet de modifier n'importe quelle entité (Demandeur, Passeport, VisaTransformable, Demande)
 - ajoute colonne status pour afficher le status d une demande 

 NOTE: pour la modification mettre dans des variables le titre du formulaire et l url de l envoie du post comme ca on reutilise le formulaire d ajout pour les modificatioon de demande 

 
:::::::TODO 3

pre-rempli 6 chiffre dans le numero visa
modifier rehefa misy erreur de averina daholo ilay donnee rehetra ilay namenona an'ilay input 

PAGE : LISTE DES DEMANDES (MODIFICATION) 
 Modifier la page liste de demandes :
      - Si demande.status == "cree" :
            Afficher un bouton " Scanner"
      - Si demande.status == "scanner" :
            Afficher deux boutons :
                  Valider 
                  Rejeter 
      - Si demande.status == "valide" :
            Afficher un badge vert "Validée"
      - Si demande.status == "rejete" :
            Afficher un badge rouge "Rejetée" 

PAGE : DOCUMENTS À SCANNER 
 Créer page documents_a_scanner.jsp :
      - En-tête : "Demande N°{idDemande} - Scan des pièces justificatives"
      - Barre de progression : X/N documents uploadés
      - Pour chaque document requis :
            Afficher une carte avec :
                  - Nom du document (ex: "CIN recto/verso")
                  - Statut : (Non uploadé / Uploadé)
                  - Si non uploadé : 
                        - on a un modal pour uplader les fichiers (hidden: s affiche que qu on clique sur scanner ou uploader : apres on voit un ipnut type file et bouton valider ) 
                        - Bouton " Scanner / Uploader"
                  - Si uploadé :
                        - Message "✓ Fichier uploadé le {date}"
                        - Bouton " Remplacer" 

      - En bas de page :
            - Bouton "Retour à la liste"
            - Bouton "Valider tous les documents" (visible seulement quand tous sont uploadés)

:::::::TODO 4

Structure de base
    - Créer la page d'accueil avec Sidebar et Header
    - Mettre en place le routing React Router

Composant Recherche.jsx
    - Formulaire avec deux inputs :
        - Input : numéro demande
        - Input : numéro passeport
    - Comportement :
        - Fonctionne même si un seul input est rempli
        - Si les deux inputs sont vides -> afficher "Aucune demande trouvée"
    - Bouton "Rechercher" → déclenche l'appel API et affiche ListeDemande

Composant ListeDemande.jsx
    - Afficher les colonnes :
        - idDemande
        - Nom et prénom demandeur
        - Numéro passeport
        - Numéro visa_transformable
        - Type demande
        - Type visa
        - Date_demande
        - QR code
    - Cas particuliers :
        - Recherche par numéro demande : afficher la demande + ligne de seprations et ajouter un titre demande associer + les demandes associées (même demandeur) en bas
        - Recherche par numéro passeport : afficher toutes les demandes liées à ce passeport (ordre chronologique)
    - Colonne "Action" :
        - Bouton "Voir détails" → redirige vers FicheDemande

Composant FicheDemande.jsx
    - Afficher :
        - QR Code
        - Etat civil
        - Passeport
        - Visa transformable
        - Statut actuel
        - Visa et carte de résidence (si statut approuvé)
    - Bloc "Historique de statut" :
        - Afficher les 3 derniers statuts (avec date)
        - Bouton "Voir plus" → redirige vers HistoStatutDemande
    - Bloc "Fichiers" :
        - Afficher les fichiers avec case à cocher (coché/non coché)
        - Bouton "Voir détail upload" → redirige vers FichiersDetailDemande

Composant HistoStatutDemande.jsx
    - Afficher l'historique complet des statuts (tous) d une demande specifique

Composant FichiersDetailDemande.jsx
    - Afficher la liste des fichiers :
        - Nom du fichier
        - Aperçu (base64 ou URL)
    d une demande specifique

Logique et appels API
    - Séparer la logique de traitement des listes dans des fichiers .js dédiés
    - Isoler les appels fetch dans des services/api.js
    - Conserver la valeur de recherche dans les inputs après chaque recherche
    - respecter le style de page existant