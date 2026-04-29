<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<div class="card shadow-sm mx-auto" style="max-width: 600px;">
    <%-- En-tête de la carte --%>
    <div class="card-header bg-primary text-white">
        <h2 class="h5 mb-0">${pageTitle}</h2>
    </div>

    <div class="card-body">
        <%-- Affichage de l'erreur --%>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i> ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <%-- Formulaire de recherche --%>
        <form action="${pageContext.request.contextPath}/duplicata/rechercher" method="post" class="mb-3">
            <input type="hidden" name="transfer" value="${finalTransfer}">
            <input type="hidden" name="duplicata" value="${finalDuplicata}">

            <div class="mb-3">
                <label for="numCarte" class="form-label fw-bold">Numéro de carte résident</label>
                <input type="text" name="numCarte" id="numCarte" class="form-control" placeholder="Entrez le numéro de carte">
            </div>
            
            <div class="mb-3">
                <label for="numVisa" class="form-label fw-bold">Numéro de visa</label>
                <input type="text" name="numVisa" id="numVisa" class="form-control" placeholder="Entrez le numéro de visa">
            </div>

            <button type="submit" class="btn btn-primary w-100 shadow-sm">
                <i class="bi bi-search me-2"></i>Rechercher
            </button>
        </form>

        <%-- Lien "Sans donnée antérieure" --%>
        <c:set var="finalTransfer" value="${not empty transfer ? transfer : param.transfer}" />
        <c:set var="finalDuplicata" value="${not empty duplicata ? duplicata : param.duplicata}" />
        
        <div class="text-center border-top pt-3">
            <a href="${pageContext.request.contextPath}/demande/create?provenance=${finalTransfer == '2' ? 'TRANSFERT_DUPLICATA' : (finalTransfer == '1' ? 'TRANSFERT' : 'DUPLICATA')}"
               class="btn btn-outline-secondary btn-sm">
                <i class="bi bi-plus-circle me-1"></i>Sans donnée antérieure
            </a>
        </div>
    </div>

    <%-- Affichage des résultats --%>
    <c:if test="${not empty demande}">
        <div class="card-footer bg-light border-top">
            <h6 class="text-muted border-bottom pb-2 mb-3">Dossier trouvé</h6>
            <ul class="list-unstyled">
                <li class="mb-2"><strong>Nom & Prénom :</strong> ${demande.demandeur.nom} ${demande.demandeur.prenom}</li>
                <li class="mb-2"><strong>Type de visa :</strong> <span class="badge bg-info text-dark">${demande.typeVisa.libelle}</span></li>
                <li class="mb-2"><strong>Numéro visa :</strong> <code>${visa.reference}</code></li>
                <li class="mb-2"><strong>Date de demande :</strong> ${demande.dateDemande}</li>
            </ul>

            <c:choose>
                <%-- CAS 3 & 4 (Transfert simple ou Transfert+Duplicata) --%>
                <c:when test="${finalTransfer == '1' || finalTransfer == '2'}">
                    <c:url var="urlEtapeSuivante" value="/duplicata/nouveau_passeport">
                        <c:param name="id" value="${demande.id}" />
                        <c:param name="duplicata" value="${finalDuplicata}" />
                    </c:url>
                </c:when>
                <%-- CAS 2 (Duplicata simple) --%>
                <c:otherwise>
                    <c:url var="urlEtapeSuivante" value="/duplicata/resume_duplicata">
                        <c:param name="id" value="${demande.id}" />
                    </c:url>
                </c:otherwise>
            </c:choose>

            <a href="${urlEtapeSuivante}" class="btn btn-success w-100 mt-2 py-2 fw-bold shadow-sm">
                Continuer vers l'étape suivante <i class="bi bi-arrow-right-short ms-1"></i>
            </a>
        </div>
    </c:if>
</div>