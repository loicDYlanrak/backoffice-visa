<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<div class="container mt-4">
    <div class="card shadow">
        <div class="card-header bg-success text-white">
            <h2 class="mb-0">Résumé de la demande de duplicata</h2>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col-md-6">
                    <h4><i class="bi bi-person"></i> Informations Demandeur</h4>
                    <hr>
                    <p><strong>Nom & Prénom :</strong> ${demande.demandeur.nom} ${demande.demandeur.prenom}</p>
                </div>
                <div class="col-md-6">
                    <h4><i class="bi bi-file-earmark-text"></i> Détails Demande</h4>
                    <hr>
                    <p><strong>Numéro :</strong> # ${demande.id}</p>
                    <p><strong>Type :</strong> ${demande.typeVisa.libelle}</p>
                </div>
            </div>

            <c:if test="${not empty visa}">
                <div class="row mt-4">
                    <div class="col-12">
                        <div class="card border-warning">
                            <div class="card-header bg-warning text-dark">
                                <strong><i class="bi bi-patch-check"></i> Détails du Visa trouvé</strong>
                            </div>
                            <div class="card-body bg-light">
                                <div class="row">
                                    <div class="col-md-4">
                                        <p><strong>Référence Visa :</strong> <span class="text-danger">${visa.reference}</span></p>
                                    </div>
                                    <div class="col-md-4">
                                        <p><strong>Date Début :</strong> ${visa.dateDebut}</p>
                                    </div>
                                    <div class="col-md-4">
                                        <p><strong>Date Fin :</strong> ${visa.dateFin}</p>
                                    </div>
                                </div>
                                <div class="row mt-2">
                                    <div class="col-md-6">
                                        <p><strong>Numéro de Passeport lié :</strong> ${visa.passeport.numeroPasseport}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>

            <div class="alert alert-info mt-4">
                <i class="bi bi-info-circle"></i> Vérifiez bien la référence du visa avant de confirmer.
            </div>
        </div>
        
        <div class="card-footer d-flex justify-content-between">
            <a href="${pageContext.request.contextPath}/duplicata/recherche_numero" class="btn btn-outline-secondary">Retour</a>
            <form action="${pageContext.request.contextPath}/duplicata/valider" method="post">
                <input type="hidden" name="demandeId" value="${demande.id}">
                <input type="hidden" name="visaId" value="${visa.id}">
                <button type="submit" class="btn btn-primary">Valider le Duplicata</button>
            </form>
        </div>
    </div>
</div>