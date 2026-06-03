<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!-- Meta tag pour le context path -->
<meta name="app-context" content="${pageContext.request.contextPath}">
<div class="card">
    <div class="card-header bg-primary text-white">
        <h3>Liste des demandes</h3>
    </div>
    <div class="card-body">
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <form class="row g-2 align-items-end mb-3" method="get" action="${pageContext.request.contextPath}/demande/liste">
            <div class="col-md-6">
                <label for="reference" class="form-label">Recherche par reference</label>
                <input type="text" class="form-control" id="reference" name="reference" placeholder="RES-2026-001"
                value="${reference}">
            </div>
            <div class="col-md-6 d-flex gap-2">
                <button type="submit" class="btn btn-primary">Rechercher</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/demande/liste">Reinitialiser</a>
            </div>
        </form>

        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th>Reference</th>
                        <th>Nom complet</th>
                        <th>Type visa</th>
                        <th>Date</th>
                        <th>Status</th>
                        <th>type de demande</th>
                        <th>qrCode scan</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>

                    <c:forEach items="${demandes}" var="demande">
                        <c:set var="status" value="${statusMap[demande.id]}" />
                        <c:set var="canScan" value="${canBeScannedMap[demande.id]}" />

                        <tr>
                            <%-- Référence --%>
                            <td>${referenceMap[demande.id]}</td>

                            <%-- Nom Complet --%>
                            <td>${demande.demandeur.nom} ${demande.demandeur.prenom}</td>

                            <%-- Type Visa --%>
                            <td>${demande.typeVisa.libelle}</td>

                            <%-- Date --%>
                            <td>${demande.dateDemande}</td>

                            <%-- Statut avec Badge --%>
                            <td>
                                <c:choose>
                                    <c:when test="${status == 'Créé'}">
                                        <span class="badge bg-success">crée</span>
                                    </c:when>
                                    <c:when test="${status == 'En cours de scan'}">
                                        <span class="badge bg-danger">En cours de scan</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">${status}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                ${demande.typeDemande.libelle}
                            </td>
                            <td>
                                <c:if test="${not empty demande.cheminQR}">
                                    <!-- Icône cliquable pour ouvrir la modale -->
                                    <a href="#" data-bs-toggle="modal" data-bs-target="#qrModal${demande.id}">
                                        <i class="bi bi-qr-code-scan" style="font-size: 1.5rem; color: #0d6efd;"></i>
                                    </a>

                                    <!-- Modale Bootstrap propre à chaque demande -->
                                    <div class="modal fade" id="qrModal${demande.id}" tabindex="-1" aria-hidden="true">
                                        <div class="modal-dialog modal-dialog-centered">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title">QR Code - ${referenceMap[demande.id]}</h5>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body text-center">
                                                    <%-- On utilise le chemin stocké en base. Assurez-vous que le chemin est accessible via une URL --%>
                                                    <img src="${pageContext.request.contextPath}/${demande.cheminQR}"
                                                    alt="QR Code"
                                                    class="img-fluid"
                                                    style="max-width: 300px;">
                                                </div>

                                                <div class="modal-footer">

                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fermer</button>
                                                    <button type="button" class="btn btn-secondary">
                                                        <a href="${api}${demande.getId()}" target="_blank">
                                                            ${api}${demande.getId()}
                                                        </a>
                                                    </button>
                                                </div>



                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${empty demande.cheminQR}">
                                    <span class="text-muted small">N/A</span>
                                </c:if>
                            </td>
                            <%-- Actions --%>
                            <td>
                                <div class="d-flex gap-1">
                                    <%-- Correction : Ajout du <c:choose> obligatoire autour des <c:when> --%>
                                    <c:choose>
                                        <%-- Logique pour le statut Cree --%>
                                    <c:when test="${status == 'Photo et signature Termine'}">
                                        <c:if test="${canScan}">
                                            <a href="${pageContext.request.contextPath}/demande/scanner/${demande.id}"
                                            class="btn btn-sm btn-info text-white">Scanner</a>
                                        </c:if>
                                            <a href="${pageContext.request.contextPath}/demande/modifier/${demande.id}"
                                            class="btn btn-sm btn-warning">Modifier</a>
                                    </c:when>
                                    <c:when test="${status == 'Créé' || status == 'En cours de scan'}">
                                            <a href="${pageContext.request.contextPath}/demande/photo-signature/${demande.id}"
                                                class="btn btn-sm btn-primary">
                                                <i class="bi bi-camera"></i> Photo/Signature
                                            </a>
                                        
                                        <c:if test="${status == 'Créé'}">
                                            <a href="${pageContext.request.contextPath}/demande/modifier/${demande.id}"
                                            class="btn btn-sm btn-warning">Modifier</a>
                                        </c:if>

                                    </c:when>

                                    <%-- Logique pour le statut Scanner --%>
                                    <c:when test="${status == 'Scanné Termine'}">
                                        <button type="button" class="btn btn-sm btn-secondary" 
                                                data-bs-toggle="modal" data-bs-target="#previewModal${demande.id}"
                                                onclick="chargerDocumentsApercu(${demande.id})">
                                            <i class="bi bi-eye"></i> Aperçu
                                        </button>
                                        <a href="${pageContext.request.contextPath}/demande/telecharger-accuse/${demande.id}"
                                           class="btn btn-sm btn-primary" title="Télécharger l'accusé de réception">
                                            <i class="bi bi-download"></i> Accuse PDF
                                        </a>
                                        <a href="${pageContext.request.contextPath}/demande/valider/${demande.id}"
                                        class="btn btn-sm btn-success">Valider</a>
                                        <a href="${pageContext.request.contextPath}/demande/rejeter/${demande.id}"
                                        class="btn btn-sm btn-danger">Rejeter</a>
                                    </c:when>

                                    <c:otherwise>
                                        <span class="text-muted small">Aucune action</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </td>
                    </tr>
                </c:forEach>

                <%-- Modales d'aperçu des documents pour chaque demande --%>
                <c:if test="${empty demandes}">
                    <tr>
                        <td colspan="6" class="text-center text-muted">
                            Aucune demande trouvee.
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<%-- Modales d'aperçu des documents pour chaque demande (EN DEHORS du tableau) --%>
<c:forEach items="${demandes}" var="demande">
    <c:set var="status" value="${statusMap[demande.id]}" />
    <c:if test="${status == 'Scanné Termine'}">
        <!-- Modal d'aperçu des documents -->
        <div class="modal fade" id="previewModal${demande.id}" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-fullscreen">
                <div class="modal-content">
                    <div class="modal-header">
                        <div class="d-flex align-items-center w-100">
                            <h5 class="modal-title flex-grow-1">Aperçu des documents - ${referenceMap[demande.id]}</h5>
                            <span id="docCounter${demande.id}" class="text-muted me-3"></span>
                        </div>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body d-flex flex-column">
                        <div id="documentsContainer${demande.id}" class="flex-grow-1" style="min-height: 600px; width: 100%;">
                            <div class="text-center" >
                                <div class="spinner-border" role="status">
                                    <span class="visually-hidden">Chargement...</span>
                                </div>
                            </div>
                        </div>
                                            
                        <!-- Zone d'information du document -->
                        
                    </div>
                    <div class="modal-footer">
                        <div class="mt-3 p-3 bg-light rounded">
                            <div class="row">
                                <div class="col-md-12">
                                    <p class="mb-2"><strong>Type de piece:</strong> <span id="docName${demande.id}"></span></p>
                                    <p class="mb-0"><strong>Date:</strong> <span id="docDate${demande.id}"></span></p>
                                </div>
                            </div>
                        </div>
                        <button type="button" class="btn btn-secondary" id="prevBtn${demande.id}" onclick="previousDocument(${demande.id})" style="display:none;">
                            <i class="bi bi-chevron-left"></i> Précédent
                        </button>
                        <button type="button" class="btn btn-secondary" id="nextBtn${demande.id}" onclick="nextDocument(${demande.id})" style="display:none;">
                            Suivant <i class="bi bi-chevron-right"></i>
                        </button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fermer</button>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</c:forEach>
</div>

<style>
/* Styles pour la grille de miniatures */
.miniatures-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
    gap: 15px;
    padding: 20px;
}

.miniature-item {
    position: relative;
    cursor: pointer;
    border: 2px solid #ddd;
    border-radius: 8px;
    overflow: hidden;
    background-color: #f8f9fa;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    justify-content: center;
    min-height: 120px;
}

.miniature-item:hover {
    border-color: #0d6efd;
    box-shadow: 0 0 10px rgba(13, 110, 253, 0.3);
    transform: scale(1.05);
}

.miniature-item img,
.miniature-item .miniature-pdf-icon {
    max-width: 90%;
    max-height: 90%;
    object-fit: contain;
}

.miniature-item .miniature-file-icon {
    font-size: 2.5rem;
    color: #6c757d;
}

.miniature-label {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background: rgba(0, 0, 0, 0.7);
    color: white;
    padding: 5px;
    font-size: 0.75rem;
    text-align: center;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

/* Mode detail */
.document-detail-view {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-height: 600px;
}

.back-to-thumbnails-btn {
    margin-bottom: 15px;
}
</style>

<script>
// Stockage des documents par ID de demande et index courant
const documentsStore = {};
const currentIndexStore = {};
const viewModeStore = {}; // 'thumbnails' ou 'detail'

/**
 * Récupère le chemin de base de l'application
 */
function getContextPath() {
    const metaTag = document.querySelector('meta[name="app-context"]');
    return metaTag ? metaTag.getAttribute('content') : '';
}

/**
 * Affiche la grille de miniatures
 * @param {number} idDemande - ID de la demande
 */
function afficherMiniatures(idDemande) {
    const documents = documentsStore[idDemande];
    if (!documents || documents.length === 0) return;
    
    const contextPath = getContextPath();
    const container = document.querySelector("#documentsContainer" + idDemande);
    
    let html = '<div class="miniatures-grid">';
    
    documents.forEach((doc, index) => {
        // Extraire le nom du fichier du chemin
        let fileName = doc.cheminFichier;
        if (fileName.includes('/')) {
            fileName = fileName.split('/').pop();
        }
        if (fileName.includes('\\')) {
            fileName = fileName.split('\\').pop();
        }
        const fileUrl = contextPath + "/uploads/" + idDemande + "/" + fileName;
        let thumbnailContent = '';
        
        if (doc.typeDocument === 'image') {
            thumbnailContent = '<img src="' + fileUrl + '" alt="' + doc.nomPiece + '" onerror="this.src=' + "'data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%22120%22 height=%22120%22%3E%3Crect fill=%22%23f0f0f0%22 width=%22120%22 height=%22120%22/%3E%3Ctext x=%2250%25%22 y=%2250%25%22 text-anchor=%22middle%22 dy=%22.3em%22 fill=%22%23999%22%3EErreur%3C/text%3E%3C/svg%3E'" + '">';
        } else if (doc.typeDocument === 'pdf') {
            thumbnailContent = '<div class="miniature-pdf-icon"><i class="bi bi-file-pdf" style="font-size: 2.5rem; color: #d32f2f;"></i></div>';
        } else {
            thumbnailContent = '<div class="miniature-file-icon"><i class="bi bi-file-earmark"></i></div>';
        }
        
        html += '<div class="miniature-item" onclick="afficherDocumentDetail(' + idDemande + ', ' + index + ')" title="' + doc.nomPiece + '">' +
                thumbnailContent +
                '<div class="miniature-label">' + doc.nomPiece + '</div>' +
                '</div>';
    });
    
    html += '</div>';
    container.innerHTML = html;
    
    // Mettre à jour le compteur et l'info pour afficher le nombre total
    document.querySelector("#docCounter" + idDemande).textContent = documents.length + " document(s)";
    document.querySelector("#docName" + idDemande).textContent = "Vue miniatures";
    document.querySelector("#docDate" + idDemande).textContent = "";
    
    // Masquer les boutons Précédent/Suivant en mode miniatures
    const prevBtn = document.querySelector("#prevBtn" + idDemande);
    const nextBtn = document.querySelector("#nextBtn" + idDemande);
    if (prevBtn) prevBtn.style.display = "none";
    if (nextBtn) nextBtn.style.display = "none";
    
    viewModeStore[idDemande] = 'thumbnails';
}

/**
 * Affiche le document détaillé à l'index spécifié
 * @param {number} idDemande - ID de la demande
 * @param {number} index - Index du document à afficher
 */
function afficherDocumentDetail(idDemande, index) {
    const documents = documentsStore[idDemande];
    
    if (!documents || documents.length === 0) return;
    
    if (index < 0) {
        currentIndexStore[idDemande] = documents.length - 1;
    } else if (index >= documents.length) {
        currentIndexStore[idDemande] = 0;
    } else {
        currentIndexStore[idDemande] = index;
    }
    
    const currentIndex = currentIndexStore[idDemande];
    const doc = documents[currentIndex];
    const contextPath = getContextPath();
    const container = document.querySelector("#documentsContainer" + idDemande);
    
    const dateUpload = new Date(doc.dateUpload).toLocaleDateString('fr-FR');
    
    // Extraire le nom du fichier du chemin
    let fileName = doc.cheminFichier;
    if (fileName.includes('/')) {
        fileName = fileName.split('/').pop();
    }
    if (fileName.includes('\\')) {
        fileName = fileName.split('\\').pop();
    }
    const fileUrl = contextPath + "/uploads/" + idDemande + "/" + fileName;
    
    console.log("Affichage du document: " + fileUrl);
    
    let documentHtml = '<div class="document-detail-view">' +
        '<button type="button" class="btn btn-outline-secondary btn-sm back-to-thumbnails-btn" onclick="retournerAuxMiniatures(' + idDemande + ')"><i class="bi bi-arrow-left"></i> Retour aux miniatures</button>';
    
    if (doc.typeDocument === 'image') {
        documentHtml += '<div class="text-center" style="max-width: 100%; max-height: 600px; display: flex; align-items: center; justify-content: center;"><img src="' + fileUrl + '" alt="' + doc.nomPiece + '" class="img-fluid" style="max-width: 70%;"></div>';
    } else if (doc.typeDocument === 'pdf') {
        documentHtml += '<iframe src="' + fileUrl + '" style="width: 100%; height: 600px; border: none;" frameborder="0"></iframe>';
    } else {
        documentHtml += '<div class="text-center"><div style="font-size: 5rem; color: #6c757d;"><i class="bi bi-file-earmark"></i></div><p class="mt-3 text-muted">Document</p></div>';
    }
    
    documentHtml += '</div>';
    
    container.innerHTML = documentHtml;
    afficherBoutonsNavigation(idDemande, documents.length);
    
    document.querySelector("#docName" + idDemande).textContent = doc.nomPiece;
    document.querySelector("#docDate" + idDemande).textContent = dateUpload;
    
    const counter = document.querySelector("#docCounter" + idDemande);
    counter.textContent = currentIndex + 1 + " / " + documents.length;
    
    viewModeStore[idDemande] = 'detail';
}

/**
 * Affiche les boutons de navigation Précédent/Suivant
 * @param {number} idDemande - ID de la demande
 * @param {number} totalDocuments - Nombre total de documents
 */
function afficherBoutonsNavigation(idDemande, totalDocuments) {
    const prevBtn = document.querySelector("#prevBtn" + idDemande);
    const nextBtn = document.querySelector("#nextBtn" + idDemande);
    
    if (prevBtn && nextBtn) {
        prevBtn.style.display = "inline-block";
        nextBtn.style.display = "inline-block";
    }
}

/**
 * Retourne à la vue miniatures
 * @param {number} idDemande - ID de la demande
 */
function retournerAuxMiniatures(idDemande) {
    afficherMiniatures(idDemande);
}

/**
 * Charge les documents d'une demande et affiche les miniatures
 * @param {number} idDemande - ID de la demande
 */
function chargerDocumentsApercu(idDemande) {
    const contextPath = getContextPath();
    const container = document.querySelector("#documentsContainer" + idDemande);
    
    // Afficher le spinner
    container.innerHTML = '<div class="text-center">' +
        '<div class="spinner-border" role="status">' +
        '<span class="visually-hidden">Chargement...</span>' +
        '</div>' +
        '</div>';
    
    // Récupérer les documents
    const url = contextPath + "/api/demande/" + idDemande + "/documents-apercu";
    console.log("Fetching documents from: " + url);
    
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erreur lors du chargement des documents');
            }
            return response.json();
        })
        .then(documents => {
            if (documents.length === 0) {
                container.innerHTML = '<div class="text-center text-muted">' +
                    '<p>Aucun document n\'a été téléchargé pour cette demande.</p>' +
                    '</div>';
                return;
            }
            
            // Stocker les documents et initialiser l'index
            documentsStore[idDemande] = documents;
            currentIndexStore[idDemande] = 0;
            
            // Afficher les miniatures
            afficherMiniatures(idDemande);
        })
        .catch(error => {
            console.error('Erreur:', error);
            container.innerHTML = '<div class="alert alert-danger">' +
                'Erreur lors du chargement des documents: ' + error.message +
                '</div>';
        });
}

/**
 * Affiche le document suivant
 * @param {number} idDemande - ID de la demande
 */
function nextDocument(idDemande) {
    if (viewModeStore[idDemande] === 'detail') {
        const currentIndex = currentIndexStore[idDemande] || 0;
        afficherDocumentDetail(idDemande, currentIndex + 1);
    }
}

/**
 * Affiche le document précédent
 * @param {number} idDemande - ID de la demande
 */
function previousDocument(idDemande) {
    if (viewModeStore[idDemande] === 'detail') {
        const currentIndex = currentIndexStore[idDemande] || 0;
        afficherDocumentDetail(idDemande, currentIndex - 1);
    }
}
</script>