<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<h3>Ouvrir un compte</h3>

<form method="post" action="${pageContext.request.contextPath}/accounts/create" class="row g-3 mt-1">

    <div class="col-md-6">
        <label class="form-label">Client *</label>
        <select name="clientId" class="form-select ${not empty errors.clientId ? 'is-invalid' : ''}">
            <option value="">-- Choisir un client --</option>
            <c:forEach items="${clients}" var="c">
                <option value="${c.id}" ${c.id == selectedClient ? 'selected' : ''}>
                    <c:out value="${c.nom} ${c.prenom}"/> (${c.numero})
                </option>
            </c:forEach>
        </select>
        <div class="invalid-feedback">${errors.clientId}</div>
    </div>

    <div class="col-md-6">
        <label class="form-label">Type de compte *</label>
        <select name="type" class="form-select ${not empty errors.type ? 'is-invalid' : ''}">
            <c:forEach items="${types}" var="t">
                <option value="${t}" ${t == selectedType ? 'selected' : ''}>${t}</option>
            </c:forEach>
        </select>
        <div class="invalid-feedback">${errors.type}</div>
    </div>

    <div class="col-md-6">
        <label class="form-label">Dépôt initial (FCFA)</label>
        <input type="text" name="depot" value="<c:out value='${depot}'/>" placeholder="0"
               class="form-control ${not empty errors.depot ? 'is-invalid' : ''}">
        <div class="invalid-feedback">${errors.depot}</div>
    </div>

    <div class="col-12">
        <button class="btn btn-primary">Ouvrir le compte</button>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/accounts">Annuler</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>