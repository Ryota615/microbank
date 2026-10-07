<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<h3>Client ${client.numero}</h3>

<div class="card mt-3">
    <div class="card-body">
        <dl class="row mb-0">
            <dt class="col-sm-3">Nom</dt><dd class="col-sm-9"><c:out value="${client.nom}"/></dd>
            <dt class="col-sm-3">Prenom</dt><dd class="col-sm-9"><c:out value="${client.prenom}"/></dd>
            <dt class="col-sm-3">Date de naissance</dt><dd class="col-sm-9">${client.dateNaissance}</dd>
            <dt class="col-sm-3">Telephone</dt><dd class="col-sm-9"><c:out value="${client.telephone}"/></dd>
            <dt class="col-sm-3">Email</dt><dd class="col-sm-9"><c:out value="${client.email}"/></dd>
            <dt class="col-sm-3">Adresse</dt><dd class="col-sm-9"><c:out value="${client.adresse}"/></dd>
            <dt class="col-sm-3">N° de pièce</dt><dd class="col-sm-9"><c:out value="${client.numeroPiece}"/></dd>
            <dt class="col-sm-3">Date de création</dt><dd class="col-sm-9">${client.dateCreation}</dd>
            <dt class="col-sm-3">Statut</dt><dd class="col-sm-9">${client.statut}</dd>
        </dl>
    </div>
</div>

<div class="mt-3">
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/clients/edit?id=${client.id}">Modifier</a>
    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/clients">Retour à la liste</a>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>