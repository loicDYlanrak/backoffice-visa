<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>

<div class="card">
    <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
        <h3 class="mb-0">Demande N° ${idDemande} - Scan des pièces justificatives</h3>
        <span class="badge bg-light text-primary">${nbUpload} / ${nbTotal} Documents</span>
    </div>

    <div class="card-body">
        <%-- Alertes de messages --%>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <%-- Barre de progression --%>
        <div class="mb-4">
            <c:set var="progress" value="${nbTotal > 0 ? (nbUpload * 100 / nbTotal) : 0}" />
            <div class="d-flex justify-content-between mb-1">
                <span class="fw-bold">Progression des scans</span>
                <span>${nbUpload} sur ${nbTotal} pièces traitées</span>
            </div>
            <div class="progress" style="height: 25px;">
                <div class="progress-bar progress-bar-striped progress-bar-animated ${progress == 100 ? 'bg-success' : 'bg-info'}" 
                     role="progressbar" 
                     style="width: ${progress}%" 
                     aria-valuenow="${progress}" aria-valuemin="0" aria-valuemax="100">
                    <c:out value="${fn:substringBefore(progress, '.')}" />%
                </div>
            </div>
        </div>

        <div class="row g-3">
            <%-- Boucle sur les pièces spécifiques de cette demande --%>
            <c:forEach items="${pieceDemande}" var="pd">
                
                <%-- On cherche le statut correspondant dans 'documentsStatus' pour savoir si c'est uploadé --%>
                <c:set var="statusInfo" value="${null}" />
                <c:forEach items="${documentsStatus}" var="status">
                    <c:if test="${status.piece.id == pd.piece.id}">
                        <c:set var="statusInfo" value="${status}" />
                    </c:if>
                </c:forEach>

                <div class="col-md-6 col-lg-4">
                    <div class="card h-100 border-${(not empty statusInfo and statusInfo.uploaded) ? 'success' : 'warning'} shadow-sm">
                        <div class="card-body">
                            <h5 class="card-title text-primary">${pd.piece.libelle}</h5>
                            <p class="text-muted small">Réf pièce : ${pd.piece.id}</p>
                            
                            <c:choose>
                                <c:when test="${not empty statusInfo and statusInfo.uploaded}">
                                    <div class="alert alert-sm alert-light border-success py-2">
                                        <p class="text-success small mb-0">
                                            <i class="bi bi-check-circle-fill"></i> Scanné le ${statusInfo.scanFichier.dateUpload}
                                        </p>
                                    </div>
                                    <button class="btn btn-outline-secondary btn-sm w-100 mt-2" 
                                            data-bs-toggle="modal" 
                                            data-bs-target="#modalPd${pd.id}">
                                        <i class="bi bi-arrow-repeat"></i> Remplacer le scan
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-sm alert-light border-warning py-2">
                                        <p class="text-muted small mb-0">
                                            <i class="bi bi-exclamation-triangle"></i> En attente de scan
                                        </p>
                                    </div>
                                    <button class="btn btn-primary btn-sm w-100 mt-2" 
                                            data-bs-toggle="modal" 
                                            data-bs-target="#modalPd${pd.id}">
                                        <i class="bi bi-upc-scan"></i> Scanner maintenant
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <%-- Modal d'upload utilisant l'ID de PieceDemande --%>
                <div class="modal fade" id="modalPd${pd.id}" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header bg-light">
                                <h5 class="modal-title">Numérisation : ${pd.piece.libelle}</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <form action="${pageContext.request.contextPath}/upload-doc" method="post" enctype="multipart/form-data">
                                <div class="modal-body">
                                    <input type="hidden" name="demandeId" value="${idDemande}">
                                    <%-- On envoie l'ID de la pièce pour le service --%>
                                    <input type="hidden" name="documentId" value="${pd.piece.id}">
                                    
                                    <div class="mb-3 text-center">
                                        <p>Veuillez sélectionner le fichier scanné pour cette pièce justificative.</p>
                                        <input type="file" class="form-control" name="fichier" accept=".pdf,image/*" required>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                                    <button type="submit" class="btn btn-success">Enregistrer le scan</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <div class="card-footer d-flex justify-content-between bg-white p-3">
        <a href="${pageContext.request.contextPath}/demande/liste" class="btn btn-secondary">
            <i class="bi bi-arrow-left"></i> Retour
        </a>
        
        <c:if test="${nbUpload == nbTotal && nbTotal > 0}">
            <form action="${pageContext.request.contextPath}/demande/finaliser-scan" method="post">
                <input type="hidden" name="demandeId" value="${idDemande}">
                <button type="submit" class="btn btn-success fw-bold px-4">
                    <i class="bi bi-check-all"></i> Finaliser le dossier de scan
                </button>
            </form>
        </c:if>
    </div>
</div>