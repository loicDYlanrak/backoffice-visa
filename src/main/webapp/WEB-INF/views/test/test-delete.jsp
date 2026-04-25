<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Supprimer un Test</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-danger text-white">
                        <h3 class="mb-0">Confirmation de suppression</h3>
                    </div>
                    <div class="card-body">
                        <div class="alert alert-warning">
                            <h5>Êtes-vous sûr de vouloir supprimer ce test ?</h5>
                            <p>Cette action est irréversible.</p>
                        </div>
                        
                        <table class="table table-bordered">
                            <tr>
                                <th>ID</th>
                                <td>${test.id}</td>
                            </tr>
                            <tr>
                                <th>Nom</th>
                                <td>${test.name}</td>
                            </tr>
                            <tr>
                                <th>Description</th>
                                <td>${test.description}</td>
                            </tr>
                            <tr>
                                <th>Statut</th>
                                <td>
                                    <c:choose>
                                        <c:when test="${test.status == 1}">
                                            <span class="badge bg-success">Actif</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger">Inactif</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </table>
                        
                        <form action="${pageContext.request.contextPath}/test/delete-confirm/${test.id}" method="post">
                            <button type="submit" class="btn btn-danger">Confirmer la suppression</button>
                            <a href="${pageContext.request.contextPath}/test/list" class="btn btn-secondary">Annuler</a>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
</body>
</html>