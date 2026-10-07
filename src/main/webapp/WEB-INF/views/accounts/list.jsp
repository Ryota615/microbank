<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h3>Comptes <small class="text-muted fs-6">(${total})</small></h3>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/accounts/new">Ouvrir un compte</a>
</div>

<form method="get" action="${pageContext.request.contextPath}/accounts" class="row g-2 mb-3">
    <div class="col-md-6">
        <input type="text" name="search" value="<c:out value='${search}'/>" class="form-control"
               placeholder="N° de compte, nom ou prénom du client">
    </div>
    <input type="hidden" name="size" value="${size}">
    <div class="col-auto"><button class="btn btn-outline-primary">Rechercher</button></div>
</form>

<table class="table table-striped table-hover">
    <thead class="table-dark">
    <tr><th>N° de compte</th><th>Client</th><th>Type</th><th>Solde</th><th>Statut</th><th>Actions</th></tr>
    </thead>
    <tbody>
    <c:forEach items="${accounts}" var="a">
        <tr>
            <td>${a.numeroCompte}</td>
            <td><c:out value="${a.client.prenom} ${a.client.nom}"/></td>
            <td>${a.type}</td>
            <td>${a.solde} FCFA</td>
            <td>
                <span class="badge ${a.statut == 'ACTIF' ? 'bg-success' : (a.statut == 'BLOQUE' ? 'bg-warning text-dark' : 'bg-secondary')}">${a.statut}</span>
            </td>
            <td>
                <a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/accounts/details?id=${a.id}">Voir</a>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty accounts}">
        <tr><td colspan="6" class="text-center text-muted">Aucun compte trouvé</td></tr>
    </c:if>
    </tbody>
</table>

<nav>
    <ul class="pagination">
        <c:url var="prevUrl" value="/accounts">
            <c:param name="page" value="${page - 1}"/>
            <c:param name="size" value="${size}"/>
            <c:param name="search" value="${search}"/>
        </c:url>
        <li class="page-item ${page == 0 ? 'disabled' : ''}">
            <a class="page-link" href="${prevUrl}">&lt; Précédent</a>
        </li>
        <c:forEach begin="0" end="${totalPages - 1}" var="i">
            <c:url var="pageUrl" value="/accounts">
                <c:param name="page" value="${i}"/>
                <c:param name="size" value="${size}"/>
                <c:param name="search" value="${search}"/>
            </c:url>
            <li class="page-item ${i == page ? 'active' : ''}">
                <a class="page-link" href="${pageUrl}">${i + 1}</a>
            </li>
        </c:forEach>
        <c:url var="nextUrl" value="/accounts">
            <c:param name="page" value="${page + 1}"/>
            <c:param name="size" value="${size}"/>
            <c:param name="search" value="${search}"/>
        </c:url>
        <li class="page-item ${page >= totalPages - 1 ? 'disabled' : ''}">
            <a class="page-link" href="${nextUrl}">Suivant &gt;</a>
        </li>
    </ul>
</nav>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>