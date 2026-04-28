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
