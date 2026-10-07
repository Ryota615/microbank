<%@ include file="layout/header.jsp" %>
<div class="row justify-content-center">
    <div class="col-md-4">
        <h3 class="mb-3">Connexion</h3>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        <form method="post" action="${pageContext.request.contextPath}/login">
            <div class="mb-3">
                <label class="form-label">Login</label>
                <input type="text" name="login" class="form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Mot de passe</label>
                <input type="password" name="password" class="form-control" required>
            </div>
            <button class="btn btn-primary w-100">Se connecter</button>
        </form>
    </div>
</div>
<%@ include file="layout/footer.jsp" %>