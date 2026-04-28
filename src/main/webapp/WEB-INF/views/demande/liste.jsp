<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
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
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>

        <c:forEach items="${demandes}" var="demande">
            <%-- Extraction des données des Maps --%>
            <c:set var="currentStatus" value="${statusMap[demande.id]}" />
            <c:set var="canScan" value="${canBeScannedMap[demande.id]}" />
            
            <tr>
                <td>${referenceMap[demande.id]}</td>
                <td>${demande.demandeur.nom} ${demande.demandeur.prenom}</td>
                <td>${demande.typeVisa.libelle}</td>
                <td>${demande.dateDemande}</td>
                <td>
                    <c:choose>
                        <c:when test="${currentStatus == 'valide'}">
                            <span class="badge bg-success">Validée</span>
                        </c:when>
                        <c:when test="${currentStatus == 'rejete'}">
                            <span class="badge bg-danger">Rejetée</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-secondary">${currentStatus}</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <div class="d-flex gap-1">
                        <c:choose>
                            <%-- CAS : Statut 'Cree' --%>
                            <c:when test="${currentStatus == 'Cree'}">
                                <%-- On affiche le bouton Scanner SEULEMENT si la logique métier l'autorise --%>
                                <c:if test="${canScan}">
                                    <a href="${pageContext.request.contextPath}/demande/scanner/${demande.id}" 
                                    class="btn btn-sm btn-info text-white">Scanner</a>
                                </c:if>
                                
                                <%-- Le bouton Modifier reste visible pour corriger les infos si besoin --%>
                                <a class="btn btn-sm btn-warning" 
                                href="${pageContext.request.contextPath}/demande/modifier/${demande.id}">Modifier</a>
                            </c:when>
                            
                            <%-- CAS : Statut 'Scanner' (Prêt pour validation finale) --%>
                            <c:when test="${currentStatus == 'Scanner'}">
                                <a href="${pageContext.request.contextPath}/demande/valider/${demande.id}" 
                                class="btn btn-sm btn-success">Valider</a>
                                <a href="${pageContext.request.contextPath}/demande/rejeter/${demande.id}" 
                                class="btn btn-sm btn-danger">Rejeter</a>
                            </c:when>
                        </c:choose>
                    </div>
                </td>
            </tr>
        </c:forEach>
                    
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
</div>