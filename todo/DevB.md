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




 PAGE : CONFIRMATION VALIDATION(lorsqu on clique sur le bouton valider pour les demande avec status scannee) 
 Créer page confirmation_valider.jsp :
      - Afficher récapitulatif de la demande :
            N° demande, nom, prénom, type demande
      - Liste des documents uploadés avec leurs noms
      - Message : "Êtes-vous sûr de vouloir VALIDER cette demande ?"
      - Attention : "Cette action est irréversible"
      - Bouton "◀ Annuler" (retour liste)
      - Bouton " Oui, valider définitivement" :
            - Appel POST /demande/valider/{idDemande}
            - Redirection liste demandes (status passe à "valide")

 PAGE : CONFIRMATION REJET (lorsqu on clique sur le bouton rejeter pour les demande avec status scannee) 
 Créer page confirmation_rejeter.jsp :
      - Afficher récapitulatif de la demande
      - Liste des documents uploadés
      - Message : "Êtes-vous sûr de vouloir REJETER cette demande ?"
      - Bouton "◀ Annuler"
      - Bouton " Oui, rejeter la demande" :
            - Appel POST /demande/rejeter/{idDemande} 
            - Redirection liste demandes (status passe à "rejete")