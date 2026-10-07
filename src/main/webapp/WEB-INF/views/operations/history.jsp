<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<h3>Historique <small class="text-muted fs-6">(${total})</small></h3>

<form method="get" action="${pageContext.request.contextPath}/history" class="row g-2 mb-3">
    <div class="col-md-3"><input name="numero" value="<c:out value='${numero}'/>" class="form-control" placeholder="N° de compte"></div>
    <div class="col-md-2">
        <select name="type" class="form-select">
            <option value="">Tous les types</option>
            <option value="DEPOT" ${type == 'DEPOT' ? 'selected' : ''}>Dépôt</option>
            <option value="RETRAIT" ${type == 'RETRAIT' ? 'selected' : ''}>Retrait</option>
            <option value="VIREMENT" ${type == 'VIREMENT' ? 'selected' : ''}>Virement</option>
        </select>
    </div>
    <div class="col-auto"><button class="btn btn-outline-primary">Filtrer</button></div>
</form>

<table class="table table-striped">
    <thead class="table-dark">
    <tr><th>Date</th><th>Compte</th><th>Type</th><th>Montant</th><th>Solde après</th><th>Description</th></tr>
    </thead>
    <tbody>
    <c:forEach items="${operations}" var="o">
        <tr>
            <td>${o.dateFormatee}</td><td>${o.compte.numeroCompte}</td><td>${o.type}</td>
            <td>${o.montant}</td><td>${o.soldeApres}</td><td><c:out value="${o.description}"/></td>
        </tr>
    </c:forEach>
    <c:if test="${empty operations}">
        <tr><td colspan="6" class="text-center text-muted">Aucune opération</td></tr>
    </c:if>
    </tbody>
</table>

<c:url var="prevUrl" value="/history">
    <c:param name="page" value="${page - 1}"/><c:param name="numero" value="${numero}"/>
    <c:param name="type" value="${type}"/><c:param name="debut" value="${debut}"/><c:param name="fin" value="${fin}"/>
</c:url>
<c:url var="nextUrl" value="/history">
    <c:param name="page" value="${page + 1}"/><c:param name="numero" value="${numero}"/>
    <c:param name="type" value="${type}"/><c:param name="debut" value="${debut}"/><c:param name="fin" value="${fin}"/>
</c:url>
<a class="btn btn-sm btn-outline-secondary ${page == 0 ? 'disabled' : ''}" href="${prevUrl}">&lt; Précédent</a>
<span class="mx-2">Page ${page + 1} / ${totalPages}</span>
<a class="btn btn-sm btn-outline-secondary ${page >= totalPages - 1 ? 'disabled' : ''}" href="${nextUrl}">Suivant &gt;</a>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>