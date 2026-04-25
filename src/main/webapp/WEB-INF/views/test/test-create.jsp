<div class="card">
    <div class="card-header bg-success text-white">
        <h3>Creer un nouveau Test</h3>
    </div>
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/test/save" method="post">
            <div class="mb-3">
                <label for="name" class="form-label">Nom *</label>
                <input type="text" class="form-control" id="name" name="name" required>
            </div>
            
            <div class="mb-3">
                <label for="description" class="form-label">Description</label>
                <textarea class="form-control" id="description" name="description" rows="3"></textarea>
            </div>
            
            <div class="mb-3">
                <label for="status" class="form-label">Statut</label>
                <select class="form-select" id="status" name="status">
                    <option value="1">Actif</option>
                    <option value="0">Inactif</option>
                </select>
            </div>
            
            <button type="submit" class="btn btn-primary">Enregistrer</button>
            <a href="${pageContext.request.contextPath}/test/list" class="btn btn-secondary">Annuler</a>
        </form>
    </div>
</div>