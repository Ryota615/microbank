<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>MicroBank</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<c:if test="${not empty sessionScope.user}">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">MicroBank</a>
        <ul class="navbar-nav me-auto">
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/clients">Clients</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/accounts">Comptes</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/operations">Opérations</a></li>
            <a class="nav-link" href="${pageContext.request.contextPath}/history">Historique</a>
        </ul>
        <span class="navbar-text me-3">${sessionScope.user.prenom} ${sessionScope.user.nom}</span>
        <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/logout">Déconnexion</a>
    </div>
</nav>
</c:if>
<div class="container mt-4">
<c:if test="${not empty sessionScope.flashSuccess}">
    <div class="alert alert-success">${sessionScope.flashSuccess}</div>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.flashError}">
    <div class="alert alert-danger">${sessionScope.flashError}</div>
    <c:remove var="flashError" scope="session"/>
</c:if>