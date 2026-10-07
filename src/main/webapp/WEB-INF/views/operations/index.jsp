<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}/operations"/>

<h3>Opérations</h3>
<div class="row g-3 mt-1">
    <form method="post" action="${ctx}/deposit" class="col-md-4 card card-body">
        <h6>Dépôt</h6>
        <input name="numero" class="form-control mb-2" placeholder="N° de compte" required>
        <input name="montant" class="form-control mb-2" placeholder="Montant (FCFA)" required>
        <button class="btn btn-success">Déposer</button>
    </form>
    <form method="post" action="${ctx}/withdraw" class="col-md-4 card card-body">
        <h6>Retrait</h6>
        <input name="numero" class="form-control mb-2" placeholder="N° de compte" required>
        <input name="montant" class="form-control mb-2" placeholder="Montant (FCFA)" required>
        <button class="btn btn-warning">Retirer</button>
    </form>
    <form method="post" action="${ctx}/transfer" class="col-md-4 card card-body">
        <h6>Virement</h6>
        <input name="source" class="form-control mb-2" placeholder="Compte source" required>
        <input name="destination" class="form-control mb-2" placeholder="Compte destinataire" required>
        <input name="montant" class="form-control mb-2" placeholder="Montant (FCFA)" required>
        <button class="btn btn-primary">Virer</button>
    </form>
</div>

<h5 class="mt-4">Dernières opérations</h5>
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
    </tbody>
</table>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>