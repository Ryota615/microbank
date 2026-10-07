<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<h3>${empty client.id ? 'Nouveau client' : 'Modifier le client'}</h3>

<form method="post"
      action="${pageContext.request.contextPath}/clients/${empty client.id ? 'create' : 'update'}"
      class="row g-3 mt-1">

    <input type="hidden" name="id" value="${client.id}">

    <div class="col-md-6">
        <label class="form-label">Nom *</label>
        <input type="text" name="nom" value="<c:out value='${client.nom}'/>"
               class="form-control ${not empty errors.nom ? 'is-invalid' : ''}">
        <div class="invalid-feedback">${errors.nom}</div>
    </div>

    <div class="col-md-6">
        <label class="form-label">Prenom *</label>
        <input type="text" name="prenom" value="<c:out value='${client.prenom}'/>"
               class="form-control ${not empty errors.prenom ? 'is-invalid' : ''}">
        <div class="invalid-feedback">${errors.prenom}</div>
    </div>

    <div class="col-md-6">
        <label class="form-label">Date de naissance</label>
        <input type="date" name="dateNaissance" value="${client.dateNaissance}" class="form-control">
    </div>

    <div class="col-md-6">
        <label class="form-label">Telephone *</label>
        <input type="text" name="telephone" value="<c:out value='${client.telephone}'/>"
               class="form-control ${not empty errors.telephone ? 'is-invalid' : ''}">
        <div class="invalid-feedback">${errors.telephone}</div>
    </div>

    <div class="col-md-6">
        <label class="form-label">Email</label>
        <input type="text" name="email" value="<c:out value='${client.email}'/>"
               class="form-control ${not empty errors.email ? 'is-invalid' : ''}">
        <div class="invalid-feedback">${errors.email}</div>
    </div>

    <div class="col-md-6">
        <label class="form-label">Numero de piece d'identite</label>
        <input type="text" name="numeroPiece" value="<c:out value='${client.numeroPiece}'/>"
               class="form-control ${not empty errors.numeroPiece ? 'is-invalid' : ''}">
        <div class="invalid-feedback">${errors.numeroPiece}</div>
    </div>

    <div class="col-12">
        <label class="form-label">Adresse</label>
        <input type="text" name="adresse" value="<c:out value='${client.adresse}'/>" class="form-control">
    </div>

    <div class="col-12">
        <button class="btn btn-primary">Enregistrer</button>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/clients">Annuler</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>