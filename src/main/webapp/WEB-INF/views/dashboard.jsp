<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<h3>Tableau de bord</h3>
<div class="row mt-3">
    <div class="col-md-3"><div class="card text-center"><div class="card-body"><h6>Clients</h6><h3>${nbClients}</h3></div></div></div>
    <div class="col-md-3"><div class="card text-center"><div class="card-body"><h6>Comptes</h6><h3>${nbComptes}</h3></div></div></div>
    <div class="col-md-3"><div class="card text-center"><div class="card-body"><h6>Solde total</h6><h3><fmt:formatNumber value="${totalSolde}" maxFractionDigits="0"/> FCFA</h3></div></div></div>
    <div class="col-md-3"><div class="card text-center"><div class="card-body"><h6>Opérations du jour</h6><h3>${nbOperationsJour}</h3></div></div></div>
</div>

<h5 class="mt-4">Dernières opérations</h5>
<table class="table table-striped">
    <thead class="table-dark">
    <tr><th>Date</th><th>Compte</th><th>Type</th><th>Montant</th></tr>
    </thead>
    <tbody>
    <c:forEach items="${dernieres}" var="o">
        <tr><td>${o.dateFormatee}</td><td>${o.compte.numeroCompte}</td><td>${o.type}</td><td>${o.montant} FCFA</td></tr>
    </c:forEach>
    </tbody>
</table>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>