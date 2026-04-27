<div class="card shadow">
    <div class="card-header bg-primary text-white"><h3>Nouveau Passeport</h3></div>
    <div class="card-body">
        <div class="alert alert-info">
            <strong>Visa actuel :</strong> ${visa.reference} (Expire le : ${visa.dateFin})
        </div>
        
        <form action="${pageContext.request.contextPath}/transfert/resume_transfert" method="get">
            <input type="hidden" name="demandeId" value="${demande.id}">
            <div class="mb-3">
                <label class="form-label">Nouveau numéro de passeport</label>
                <input type="text" name="nouveauNumPasseport" class="form-control" required>
            </div>
            <button type="submit" class="btn btn-primary">Valider</button>
        </form>
    </div>
</div>