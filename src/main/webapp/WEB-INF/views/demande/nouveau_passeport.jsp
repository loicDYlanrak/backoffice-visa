<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<div class="card shadow">
    <div class="card-header bg-primary text-white"><h3>Nouveau Passeport</h3></div>
    <div class="card-body">
        <div class="alert alert-info">
            <strong>Visa actuel :</strong> ${visa.reference} (Expire le : ${visa.dateFin})
        </div>

        <form action="${pageContext.request.contextPath}/transfert/resume_transfert" method="get">
            <input type="hidden" name="demandeId" value="${demande.id}">
            <input type="hidden" name="duplicata" value="${duplicata}">

            <c:forEach items="${demandeurs}" var="demandeur" varStatus="status">
                <div class="card mb-3">
                    <div class="card-header">
                        <h5>Demandeur: ${demandeur.prenom} ${demandeur.nom}</h5>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label class="form-label">Nouveau numéro de passeport</label>
                            <input type="text" name="nouveauNumPasseport"
                            value="${prefillNumeroPasseport}"
                            class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Date de délivrance</label>
                            <input type="date" name="dateDelivrance"
                            value="${prefillDateDelivrance}"
                            class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Date d'expiration</label>
                            <input type="date" name="dateExpiration"
                            value="${prefillDateExpiration}"
                            class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Pays émetteur</label>
                            <input type="text" name="paysEmetteur"
                            value="${prefillPaysDelivrance}"
                            class="form-control" required>
                        </div>

                        <input type="hidden" name="demandeurId"
                        value="${demandeur.id}">
                    </div>
                </div>
            </c:forEach>

            <button type="submit" class="btn btn-primary">Continuer</button>
        </form>
    </div>
</div>