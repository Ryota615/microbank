<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h3>Clients <small class="text-muted fs-6">(${total})</small></h3>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/clients/new">Nouveau client</a>
</div>

<form method="get" action="${pageContext.request.contextPath}/clients" class="row g-2 mb-3">
    <div class="col-md-6">
        <input type="text" name="search" value="<c:out value='${search}'/>" class="form-control"
               placeholder="Nom, prénom, téléphone ou n° de pièce">
    </div>
    <input type="hidden" name="size" value="${size}">
    <div class="col-auto"><button class="btn btn-outline-primary">Rechercher</button></div>
</form>

<table class="table table-striped table-hover">
    <thead class="table-dark">
    <tr><th>N°</th><th>Nom</th><th>Prenom</th><th>Telephone</th><th>Email</th><th>Actions</th></tr>
    </thead>
    <tbody>
    <c:forEach items="${clients}" var="c">
        <tr>
            <td>${c.numero}</td>
            <td><c:out value="${c.nom}"/></td>
            <td><c:out value="${c.prenom}"/></td>
            <td><c:out value="${c.telephone}"/></td>
            <td><c:out value="${c.email}"/></td>
            <td>
                <a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/clients/details?id=${c.id}">Voir</a>
                <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/clients/edit?id=${c.id}">Modifier</a>
                <a class="btn btn-sm btn-outline-danger" href="${pageContext.request.contextPath}/clients/delete?id=${c.id}"
                   onclick="return confirm('Supprimer ce client ?')">Supprimer</a>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty clients}">
        <tr><td colspan="6" class="text-center text-muted">Aucun client</td></tr>
    </c:if>
    </tbody>
</table>

<nav>
    <ul class="pagination">
        <c:url var="prevUrl" value="/clients">
            <c:param name="page" value="${page - 1}"/>
            <c:param name="size" value="${size}"/>
            <c:param name="search" value="${search}"/>
        </c:url>
        <li class="page-item ${page == 0 ? 'disabled' : ''}">
            <a class="page-link" href="${prevUrl}">&lt; Precedent</a>
        </li>

        <c:forEach begin="0" end="${totalPages - 1}" var="i">
            <c:url var="pageUrl" value="/clients">
                <c:param name="page" value="${i}"/>
                <c:param name="size" value="${size}"/>
                <c:param name="search" value="${search}"/>
            </c:url>
            <li class="page-item ${i == page ? 'active' : ''}">
                <a class="page-link" href="${pageUrl}">${i + 1}</a>
            </li>
        </c:forEach>

        <c:url var="nextUrl" value="/clients">
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