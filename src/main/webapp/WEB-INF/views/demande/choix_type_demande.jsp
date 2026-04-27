<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<div class="card">
    <div class="d-flex flex-wrap gap-2 p-3">

        <a class="btn btn-primary" href="${pageContext.request.contextPath}/demande/create">Nouveau demande</a>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/duplicata/recherche_numero?duplicata=1">Demande de duplicata</a>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/duplicata/recherche_numero?transfer=1">Demande de transfert de visa</a>
        <%-- <a class="btn btn-primary" href="${pageContext.request.contextPath}/duplicata/recherche_numero?transfer=2&duplicata=1">Demande de transfert de visa et duplicata</a> --%>
    </div>
   
</div>
