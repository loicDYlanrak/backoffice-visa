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