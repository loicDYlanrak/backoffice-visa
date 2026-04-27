<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>


<div class="container mt-4">
    <div class="card border-success shadow">
        <div class="card-header bg-success text-white">
            <h3><i class="bi bi-check2-all"></i> Résumé du Transfert</h3>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col-md-4">
                    <h5><i class="bi bi-person"></i> Bénéficiaire</h5>
                    <p class="mb-1"><strong>Nom :</strong> ${demande.demandeur.nom}</p>
                    <p><strong>Prénom :</strong> ${demande.demandeur.prenom}</p>
                </div>
                
                <div class="col-md-4 border-start">
                    <h5><i class="bi bi-card-heading"></i> Ancien Visa</h5>
                    <p class="mb-1"><strong>Référence :</strong> ${visa.reference}</p>
                   ${visa.passeport.numeroPasseport}
                </div>

                <div class="col-md-4 border-start">
                    <h5><i class="bi bi-passport"></i> Destination</h5>
                    <p class="mb-1"><strong>Nouveau Passeport :</strong></p>
                    <h4 class="text-success">${nouveauNumPasseport}</h4>
                </div>
            </div>

            <div class="alert alert-warning mt-4">
                <i class="bi bi-exclamation-triangle"></i> En cliquant sur "Accepter", vous confirmez le transfert définitif du visa sur ce nouveau passeport.
            </div>
        </div>

        <div class="card-footer d-flex justify-content-between">
            <a href="javascript:history.back()" class="btn btn-secondary">Modifier</a>
            
            <form action="${pageContext.request.contextPath}/transfert/accepter" method="post">
                <input type="hidden" name="demandeId" value="${demande.id}">
                <input type="hidden" name="nouveauNumPasseport" value="${nouveauNumPasseport}">
                <button type="submit" class="btn btn-success btn-lg">
                    Accepter et Terminer <i class="bi bi-check-circle"></i>
                </button>
            </form>
        </div>
    </div>
</div>