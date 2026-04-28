<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<div class="container mt-4">
    <div class="card border-success shadow">
        <div class="card-header bg-success text-white">
            <h3><i class="bi bi-check2-all"></i> Résumé du Transfert</h3>
        </div>
        <div class="card-body">
            <!-- Informations de la demande -->
            <div class="row mb-4">
                <div class="col-md-12">
                    <h5><i class="bi bi-receipt"></i> Demande N°: ${demande.id}</h5>
                    <p class="mb-1"><strong>Date de demande:</strong> ${demande.dateDemande}</p>
                    <p><strong>Statut:</strong> <span class="badge bg-info">${libelleStatut}</span></p>
                </div>
            </div>

            <div class="row">
                <div class="col-12">
                    <h5><i class="bi bi-people"></i>Nouveau Passeport</h5>
                    <hr>
                </div>
            </div>

            <c:forEach items="${demandeurIds}" var="demandeurId" varStatus="status">
                <div class="row mb-4 p-3 border rounded">
                    <div class="col-md-6">
                        <%-- <h6><i class="bi bi-person-badge"></i> Demandeur #${status.index + 1}</h6> --%>
                        <p class="mb-1"><strong>Nouveau Numéro Passeport:</strong> <span class="text-success fw-bold">${nouveauxNumPasseports[status.index]}</span></p>
                        <p class="mb-1"><strong>ID Demandeur:</strong> ${demandeurId}</p>
                        <p class="mb-1"><strong>Date de délivrance:</strong> ${dateDelivrances[status.index]}</p>
                        <p class="mb-1"><strong>Date d'expiration:</strong> ${dateExpirations[status.index]}</p>
                        <p class="mb-1"><strong>Pays émetteur:</strong> ${paysEmetteurs[status.index]}</p>
                    </div>
                    
                    <div class="col-md-6">
                        <h6><i class="bi bi-card-heading"></i> Ancien Visa</h6>
                        <c:if test="${not empty visa}">
                            <p class="mb-1"><strong>Référence Visa:</strong> ${visa.reference}</p>
                            <p class="mb-1"><strong>Type de visa:</strong> ${demande.typeVisa.libelle}</p>
                            <p><strong>Ancien passeport:</strong> ${visa.passeport.numeroPasseport}</p>
                        </c:if>
                        <c:if test="${empty visa}">
                            <p class="text-muted">Aucune information de visa disponible</p>
                        </c:if>
                    </div>
                </div>
            </c:forEach>

            <!-- Message d'avertissement -->
            <div class="alert alert-warning mt-3">
                <i class="bi bi-exclamation-triangle"></i> 
                <strong>Attention:</strong> En cliquant sur "Accepter", vous confirmez le transfert définitif du/des visa(s) sur les nouveaux passeports pour tous les demandeurs.
            </div>
        </div>

        <div class="card-footer d-flex justify-content-between">
            <a href="javascript:history.back()" class="btn btn-secondary">
                <i class="bi bi-arrow-left"></i> Modifier
            </a>
            
            <form action="${pageContext.request.contextPath}/transfert/accepter" method="post">
                <input type="hidden" name="demandeId" value="${demande.id}">
                <input type="hidden" name="duplicata" value="${duplicata}">
                
                <!-- Passer toutes les listes pour chaque demandeur -->
                <c:forEach items="${demandeurIds}" var="demandeurId" varStatus="status">
                    <input type="hidden" name="demandeurId" value="${demandeurId}">
                    <input type="hidden" name="nouveauNumPasseport" value="${nouveauxNumPasseports[status.index]}">
                    <input type="hidden" name="dateDelivrance" value="${dateDelivrances[status.index]}">
                    <input type="hidden" name="dateExpiration" value="${dateExpirations[status.index]}">
                    <input type="hidden" name="paysEmetteur" value="${paysEmetteurs[status.index]}">
                    <c:if test="${not empty autoriteEmetteurs && status.index < autoriteEmetteurs.size()}">
                        <input type="hidden" name="autoriteEmettrice" value="${autoriteEmetteurs[status.index]}">
                    </c:if>
                </c:forEach>
                
                <button type="submit" class="btn btn-success btn-lg">
                    Accepter et Terminer <i class="bi bi-check-circle"></i>
                </button>
            </form>
        </div>
    </div>
</div>

<style>
    .border-rounded {
        border-radius: 8px;
    }
    .bg-light-gray {
        background-color: #f8f9fa;
    }
</style>