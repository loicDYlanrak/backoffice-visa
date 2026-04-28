Dev: Loic

:::::::TODO 1

 Menu principal et navigation 

 modifier route GET / nouvel demande pour le rediriger vers le nouvel page

 Page choix type de demande 
    - creer fonction ChoixTypeController avec méthode :
        - showChoixPage() : renvoie la vue avec 4 boutons

 CAS 2 : Demande de duplicata (perte carte résidence) 
    - creer contrôleur RechercheController avec méthodes :
        - showForm() : GET /recherche-numero, avec parametre duplicata true
        - showResumeDuplicata(numero) : POST /duplicata/resume/
        - acceptDuplicata(numero) : POST /duplicata/accepter
            Signature : acceptDuplicata(String numero, Long idPersonne) -> status termine
    - ajouter fonction getInfoByNumeroVisq et getInfoByNumeroCarte
    - créer service DuplicataService avec méthode :
        - verifierExistenceTitre(numero) : boolean 
        - creerDemandeDuplicata(ancienTitre, personne) : void

 CAS 3 : Demande de transfert de visa (perte passeport) 
    - utiliser contrôleur RechercheController avec méthodes :
      - showFom() : GET /recherche-numero, avec parametre transfert true
      - showNewPasseportForm(numVisa) : GET /transfert/nouveau-passeport/{numVisa}
      - saveNewPasseport(request) : POST /transfert/save-passeport
        Signature : saveNewPasseport(String numVisa, String nouveauPasseport) -> redirection vers /transfert/resume/{numVisa}
      - showResume(numVisa) : GET /transfert/resume/{numVisa}
      - acceptTransfert(numVisa) : POST /transfert/accepter
        Signature : acceptTransfert(String numVisa, String nouveauPasseport) -> status termine

 Créer service TransfertService avec méthode :
      - transfererVisa(ancienVisa, nouveauPasseport) : met à jour le numéro passeport

 CAS 4 : Transfert de visa + duplicata (perte des deux) 
    - Réutiliser les fonctions dans RechercheController jusqu'à acceptTransfert
    - Créer méthode enchaînée :
        - apresAcceptTransfert(numVisa) : appel automatique à DuplicataController.acceptDuplicata(même numéro carte)
        -"termine" après duplicata

 bouton "Sans donné antérieur" (dans CAS 2,3,4) 
    - modififer la fonction showForm de nouveau titre 
    - ajouter un parametre pour la redirection apres -> NouveauTitreController.showForm()
    - traitement de nouveau titre comme d habitude 
    - créer nouveau titre avec status "visa_approuve"
    - rediriger vers la suite normale (CAS 2/3/4 selon provenance)

:::::::TODO 2

 Créer l'entité ScanFichier avec :
      - id 
      - id_piece_demande
      - cheminFichier 
      - dateUpload 

 Créer un service ScanFichierService avec méthodes :
      - uploadFichier(Long idDemande, Long idDocumentRequis, MultipartFile fichier) : ScanFichier
      - hasUploaded(Long idDemande, Long idDocumentRequis) : boolean
      - getUploadsParDemande(Long idDemande) : List<ScanFichier>
      - verifierTousDocumentsUploades(Long idDemande) : boolean
      - getDocumentsParTypeDemande(String typeDemande) : List<ScanFichier>

 Créer ScanController avec méthodes :
      // Affiche la page des documents à scanner
      - GET /demande/{idDemande}/documents-a-scanner
        Signature : showDocumentsAScanner(@PathVariable Long idDemande) : ModelAndView
        → Renvoie la liste des DocumentRequis pour ce type de demande
        → Pour chaque document, indique si déjà uploadé

      // Upload d'un fichier pour un document spécifique
      - POST /demande/{idDemande}/upload/{idDocumentRequis}
        Signature : uploadFichier(
                    @PathVariable Long idDemande,
                    @PathVariable Long idDocumentRequis,
                    @RequestParam("fichier") MultipartFile fichier) : ResponseEntity<?>
        → Retourne 200 OK si succès
        → Retourne 400 si fichier vide ou mauvais type
        → Retourne 409 si déjà uploadé

      // Vérifie si tous les documents sont uploadés
      - GET /demande/{idDemande}/tous-uploades
        Signature : verifierTousUploades(@PathVariable Long idDemande) : ResponseEntity<Boolean>

      // Validation finale après tous les uploads
      - POST /demande/{idDemande}/valider-scans
        Signature : validerScansEtChangerStatus(@PathVariable Long idDemande) : RedirectView
        → Change status de "cree" à "scanner"
        → Redirige vers la liste des demandes

 GESTION VALIDATION / REJET (status = "scanner") 

 Créer DecisionController avec méthodes :

      // Page de confirmation de validation
      - GET /demande/{idDemande}/confirmation-valider
        Signature : showConfirmationValider(@PathVariable Long idDemande) : ModelAndView
        → Affiche résumé demande + message "Confirmez-vous la validation ?"

      // Action de validation
      - POST /demande/{idDemande}/valider
        Signature : validerDemande(@PathVariable Long idDemande) : RedirectView
        → Change status de "scanner" à "valide"
        → Redirige vers liste des demandes

      // Page de confirmation de rejet
      - GET /demande/{idDemande}/confirmation-rejeter
        Signature : showConfirmationRejeter(@PathVariable Long idDemande) : ModelAndView
        → Affiche résumé demande + champ "Motif du rejet" (optionnel)

      // Action de rejet
      - POST /demande/{idDemande}/rejeter
        Signature : rejeterDemande(@PathVariable Long idDemande, 
                                   @RequestParam(required=false) String motif) : RedirectView
        → Change status de "scanner" à "rejete"
        → Sauvegarde le motif si fourni
        → Redirige vers liste des demandes

 CONFIGURATION STOCKAGE FICHIERS 
 Créer propriétés dans application.properties :
      - scan.upload.directory = /uploads/scans/
      - scan.max-file-size = 5MB
      - scan.allowed-extensions = pdf,jpg,jpeg,png

 Créer FileStorageService avec méthodes :
      - sauvegarderFichier(MultipartFile fichier, Long idDemande, Long idDocumentRequis) : String
      - supprimerFichier(String cheminFichier) : void
      - getExtension(String nomFichier) : String
