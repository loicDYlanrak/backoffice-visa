<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>

<div class="card">
    <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
        <h3 class="mb-0">Demande N° ${idDemande} - Scan des pièces justificatives</h3>
        <span class="badge bg-light text-primary">${nbUpload} / ${nbTotal} Documents</span>
    </div>

    <div class="card-body">
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

        <div class="mb-4">
            <c:set var="progress" value="${nbTotal > 0 ? (nbUpload * 100 / nbTotal) : 0}" />
            <div class="d-flex justify-content-between mb-1">
                <span class="fw-bold">Progression du dossier</span>
                <span>${nbUpload} sur ${nbTotal} documents chargés</span>
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
            <c:forEach items="${documentsStatus}" var="item">
                <div class="col-md-6 col-lg-4">
                    <div class="card h-100 border-${item.uploaded ? 'success' : 'warning'}">
                        <div class="card-body shadow-sm">
                            <h5 class="card-title text-primary">${item.piece.libelle}</h5>
                            
                            <c:choose>
                                <c:when test="${item.uploaded}">
                                    <p class="text-success small mb-3">
                                        <i class="bi bi-check-circle-fill"></i> ✓ Uploadé le ${item.scanFichier.dateUpload}
                                    </p>
                                    <button class="btn btn-outline-secondary btn-sm w-100" 
                                            data-bs-toggle="modal" 
                                            data-bs-target="#modalDoc${item.piece.id}">
                                        Remplacer
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <p class="text-muted small mb-3">
                                        <i class="bi bi-exclamation-triangle"></i> Non uploadé
                                    </p>
                                    <button class="btn btn-primary btn-sm w-100" 
                                            data-bs-toggle="modal" 
                                            data-bs-target="#modalDoc${item.piece.id}">
                                        Scanner / Uploader
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <div class="modal fade" id="modalDoc${item.piece.id}" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header bg-light">
                                <h5 class="modal-title">Charger : ${item.piece.libelle}</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <form action="${pageContext.request.contextPath}/demande/upload-doc" method="post" enctype="multipart/form-data">
                                <div class="modal-body">
                                    <input type="hidden" name="demandeId" value="${idDemande}">
                                    <input type="hidden" name="documentId" value="${item.piece.id}">
                                    <div class="mb-3">
                                        <label class="form-label">Sélectionnez le fichier</label>
                                        <input type="file" class="form-control" name="fichier" required>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                                    <button type="submit" class="btn btn-success">Valider l'envoi</button>
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
            Retour à la liste
        </a>
        
        <c:if test="${nbUpload == nbTotal && nbTotal > 0}">
            <form action="${pageContext.request.contextPath}/demande/finaliser-scan" method="post">
                <input type="hidden" name="demandeId" value="${idDemande}">
                <button type="submit" class="btn btn-success px-4">
                    Valider tous les documents
                </button>
            </form>
        </c:if>
    </div>
</div>