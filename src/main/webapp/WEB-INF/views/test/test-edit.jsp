<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<div class="card">
    <div class="card-header bg-warning text-dark">
        <h3>Modifier le Test</h3>
    </div>
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/test/update/${test.id}" method="post">
            <div class="mb-3">
                <label for="name" class="form-label">Nom *</label>
                <input type="text" class="form-control" id="name" name="name" value="${test.name}" required>
            </div>
            
            <div class="mb-3">
                <label for="description" class="form-label">Description</label>
                <textarea class="form-control" id="description" name="description" rows="3">${test.description}</textarea>
            </div>
            
            <div class="mb-3">
                <label for="status" class="form-label">Statut</label>
                <select class="form-select" id="status" name="status">
                    <option value="1" ${test.status == 1 ? 'selected' : ''}>Actif</option>
                    <option value="0" ${test.status == 0 ? 'selected' : ''}>Inactif</option>
                </select>
            </div>
            
            <button type="submit" class="btn btn-primary">Mettre à jour</button>
            <a href="${pageContext.request.contextPath}/test/list" class="btn btn-secondary">Annuler</a>
        </form>
    </div>
</div>