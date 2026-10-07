<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<h3>Compte ${account.numeroCompte}</h3>

<div class="card mt-3">
  <div class="card-body">
    <dl class="row mb-0">
      <dt class="col-sm-3">Client</dt>
      <dd class="col-sm-9">
        <a href="${pageContext.request.contextPath}/clients/details?id=${account.client.id}">
          <c:out value="${account.client.prenom} ${account.client.nom}"/>
        </a>
      </dd>
      <dt class="col-sm-3">Type</dt><dd class="col-sm-9">${account.type}</dd>
      <dt class="col-sm-3">Solde</dt><dd class="col-sm-9">${account.solde} FCFA</dd>
      <dt class="col-sm-3">Date d'ouverture</dt><dd class="col-sm-9">${account.dateOuverture}</dd>
      <dt class="col-sm-3">Statut</dt><dd class="col-sm-9">${account.statut}</dd>
    </dl>
  </div>
</div>

<c:if test="${account.statut != 'CLOTURE'}">
  <form method="post" action="${pageContext.request.contextPath}/accounts/status" class="mt-3 d-flex gap-2">
    <input type="hidden" name="id" value="${account.id}">
    <c:if test="${account.statut == 'ACTIF'}">
      <button name="statut" value="BLOQUE" class="btn btn-warning">Bloquer</button>
    </c:if>
    <c:if test="${account.statut == 'BLOQUE'}">
      <button name="statut" value="ACTIF" class="btn btn-success">Réactiver</button>
    </c:if>
    <button name="statut" value="CLOTURE" class="btn btn-danger"
            onclick="return confirm('Clôturer ce compte ? Cette action est définitive.')">Clôturer</button>
  </form>
</c:if>

<div class="mt-3">
  <a class="btn btn-secondary" href="${pageContext.request.contextPath}/accounts">Retour à la liste</a>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>