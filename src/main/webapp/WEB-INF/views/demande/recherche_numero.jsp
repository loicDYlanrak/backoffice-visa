<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<div class="card">
    <div class="card-body">
        <div class="card-header">
            <h2>${pageTitle}</h2>
        </div>
        <form action="${pageContext.request.contextPath}/duplicata/rechercher" method="post">
            <input type="hidden" name="transfer" value="${finalTransfer}">
            <input type="hidden" name="duplicata" value="${finalDuplicata}">

            <div class="mb-3">
                <label for="numCarte" class="form-label">Numéro de carte résident</label>
                <input type="text" name="numCarte" id="numCarte" class="form-control">
            </div>
            <div class="mb-3">
                <label for="numVisa" class="form-label">Numéro de visa</label>
                <input type="text" name="numVisa" id="numVisa" class="form-control">
            </div>

            <button type="submit" class="btn btn-primary w-100">Rechercher</button>
        </form>
    </div>
    <c:set var="finalTransfer" value="${not empty transfer ? transfer : param.transfer}" />
    <c:set var="finalDuplicata" value="${not empty duplicata ? duplicata : param.duplicata}" />
    <a href="${pageContext.request.contextPath}/demande/create?provenance=${finalTransfer == '2' ? 'TRANSFERT_DUPLICATA' : (finalTransfer == '1' ? 'TRANSFERT' : 'DUPLICATA')}"
    class="btn btn-secondary">
    Sans donnée antérieure
</a>
<c:if test="${not empty demande}">
    <div class="card-footer">
        <p>Dossier trouvé : <strong>${demande.demandeur.nom} ${demande.demandeur.prenom}</strong></p>

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

        <a href="${urlEtapeSuivante}" class="btn btn-success w-100">
            Continuer vers l'étape suivante
        </a>
    </div>

</c:if>

</div>