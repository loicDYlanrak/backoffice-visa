<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<div class="card">
    <div class="card-header bg-primary text-white">
        <h3>Liste des demandeurs</h3>
    </div>
    <div class="card-body">
        <!-- Success/Error Messages from Flash Attributes -->
        <c:if test="${not empty success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th>Nom</th>
                        <th>Prenom</th>
                        <th>Date naissance</th>
                        <th>Lieu naissance</th>
                        <th>Profession</th>
                        <th>Telephone</th>
                        <th>Email</th>
                        <th>Adresse</th>
                        <th>Situation familiale</th>
                        <th>Genre</th>
                        <th>Nationalite actuelle</th>
                        <th>Nationalite origine</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${demandeurs}" var="demandeur">
                        <tr>
                            <td>${demandeur.nom}</td>
                            <td>${demandeur.prenom}</td>
                            <td>${demandeur.dateNaissance}</td>
                            <td>${demandeur.lieuNaissance}</td>
                            <td>${demandeur.profession}</td>
                            <td>${demandeur.telephone}</td>
                            <td>${demandeur.email}</td>
                            <td>${demandeur.adresse}</td>
                            <td>${demandeur.idSituationFamiliale}</td>
                            <td>${demandeur.idGenre}</td>
                            <td>${demandeur.idNationaliteActuelle}</td>
                            <td>${demandeur.idNationaliteOrigine}</td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty demandeurs}">
                        <tr>
                            <td colspan="11" class="text-center text-muted">
                                Aucun demandeur trouvé.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>