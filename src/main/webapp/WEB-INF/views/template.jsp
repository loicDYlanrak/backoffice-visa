<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Visa Application - Test CRUD</title>
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <style>
        #sidebar ul li a i {
            margin-right: 10px;
        }
    </style>
</head>
<body>
    <div class="wrapper">
        <jsp:include page="sidebar.jsp" />
        <div id="content">
            <jsp:include page="header.jsp" />
            <div class="container-fluid mt-4">
                <jsp:include page="${template}.jsp" />
            </div>
            <jsp:include page="footer.jsp" />
        </div>
    </div>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <%-- <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script> --%>
     <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>