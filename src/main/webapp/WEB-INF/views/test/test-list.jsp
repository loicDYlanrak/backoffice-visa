<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<div class="card">
    <div class="card-header bg-primary text-white">
        <h3>Liste des Tests</h3>
    </div>
    <div class="card-body">
        <!-- Search Form -->
        <form action="${pageContext.request.contextPath}/test/search" method="get" class="mb-4">
            <div class="input-group">
                <input type="text" name="keyword" class="form-control" placeholder="Rechercher par nom..." value="${keyword}">
                <button type="submit" class="btn btn-primary">Rechercher</button>
                <a href="${pageContext.request.contextPath}/test/create" class="btn btn-success">Nouveau Test</a>
            </div>
        </form>

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
                        <th>ID</th>
                        <th>Nom</th>
                        <th>Description</th>
                        <th>Statut</th>
                        <th>Date Creation</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${tests}" var="test">
                        <tr>
                            <td>${test.id}</td>
                            <td>${test.name}</td>
                            <td>${test.description}</td>
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
                            <td>${test.createdAt}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/test/edit/${test.id}" class="btn btn-sm btn-warning">Modifier</a>
                                <a href="${pageContext.request.contextPath}/test/delete/${test.id}" class="btn btn-sm btn-danger" onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce test ?')">Supprimer</a>
                            </td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty tests}">
                        <tr>
                            <td colspan="6" class="text-center text-muted">
                                Aucun test trouvé. Cliquez sur "Nouveau Test" pour en créer un.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>