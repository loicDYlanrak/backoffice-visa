<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<div class="card">
    <div class="card-body">
        <div class="mb-4">
            <button type="button" class="btn btn-secondary" id="btnSansDonnees">
                Sans donnée antérieure
            </button>
        </div>

        <hr>
        <c:set var="valeurTransfert" value="${not empty param.transfer ? param.transfer: '0'}" />
        <form action="${pageContext.request.contextPath}/duplicata/rechercher" method="post">
            <div class="mb-3">
                <label for="numCarte" class="form-label">Numéro de carte résident</label>
                <input type="text" name="numCarte" id="numCarte" class="form-control" placeholder="Entrez le numéro de carte">
            </div>
            <input type="hidden" name="transfer" value="${valeurTransfert}">
            <div class="mb-3">
                <label for="numVisa" class="form-label">Numéro de visa</label>
                <input type="text" name="numVisa" id="numVisa" class="form-control" placeholder="Entrez le numéro de visa">
            </div>

            <div class="d-grid gap-2">
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-search"></i> Rechercher
                </button>
            </div>
        </form>
    </div>

    <c:if test="${not empty demande}">
        <div class="card-footer border-top">
            <div class="row p-3">
                <h3 class="text-primary">Détails de la demande trouvée</h3>
                <hr>
                <div class="col-md-6 p-3">
                    <p><strong>ID Demande :</strong> ${demande.id}</p>
                    <p><strong>Date de la demande :</strong> ${demande.dateDemande}</p>
                </div>
                    <c:choose>
                        <c:when test="${transfer == 1}">
                            <c:set var="chemin" value="resume_duplicata" />
                        </c:when>
                        <c:otherwise>
                            <c:set var="chemin" value="nouveau_passeport" />
                        </c:otherwise>
                    </c:choose>

                    <%-- Utilisation de la variable dans le lien --%>
                    <a href="${pageContext.request.contextPath}/duplicata/${chemin}?id=${demande.id}" class="btn btn-success">
                        Continuer vers le résumé
                    </a>
                
            </div>
        </div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger m-3">
            ${errorMessage}
        </div>
    </c:if>
</div>