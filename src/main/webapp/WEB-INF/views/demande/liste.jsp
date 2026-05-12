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
                                    <c:when test="${status == 'Scanner'}">
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