<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<div id="formError" class="alert alert-danger alert-dismissible fade show" role="alert" style="display:none;">
    <span id="formErrorText"></span>
    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
</div>
<div class="card">
    <div class="card-header bg-success text-white">
        <h3>${empty formTitle ? 'Formulaire de demande' : formTitle}</h3>
    </div>
    <div class="card-body">
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}${empty formAction ? '/demande' : formAction}" method="post" id="demandeForm" novalidate>
            <h5 class="mt-2 mb-3 text-secondary">Informations personnelles</h5>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="nom" class="form-label">Nom <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
                    <input type="text" class="form-control" id="nom" name="nom" value="${prefillNom}" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="prenom" class="form-label">Prenom <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
                    <input type="text" class="form-control" id="prenom" name="prenom" value="${prefillPrenom}" required>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="dateNaissance" class="form-label">Date de naissance <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
                    <input type="date" class="form-control" id="dateNaissance" name="dateNaissance" value="${prefillDateNaissance}" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="lieuNaissance" class="form-label">Lieu de naissance <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
                    <input type="text" class="form-control" id="lieuNaissance" name="lieuNaissance" value="${prefillLieuNaissance}" required>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="idSituationFamiliale" class="form-label">Situation familiale <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
                    <select class="form-select" id="idSituationFamiliale" name="idSituationFamiliale" required>
                        <option value="">-- Selectionner --</option>
                        <c:forEach items="${situations}" var="situation">
                            <option value="${situation.id}" ${prefillSituationId == situation.id ? 'selected' : ''}>
                                ${situation.libelle}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="idNationaliteActuelle" class="form-label">Nationalite <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
                    <select class="form-select" id="idNationaliteActuelle" name="idNationaliteActuelle" required>
                        <option value="">-- Selectionner --</option>
                        <c:forEach items="${nationalites}" var="nationalite">
                            <option value="${nationalite.id}" ${prefillNationaliteActuelleId == nationalite.id ? 'selected' : ''}>
                                ${nationalite.libelle}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="telephone" class="form-label">Contact <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
                    <input type="tel" class="form-control" id="telephone" name="telephone" value="${prefillTelephone}" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="adresse" class="form-label">Adresse <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
                    <input type="text" class="form-control" id="adresse" name="adresse" value="${prefillAdresse}" required>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="email" class="form-label">Email <span class="text-muted">(Optionnel)</span></label>
                    <input type="email" class="form-control" id="email" name="email" value="${prefillEmail}">
                </div>
            </div>

            <hr>
            <h5 class="mt-2 mb-3 text-secondary">Passeport</h5>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="numeroPasseport" class="form-label">Numero de passeport <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
                    <input type="text" class="form-control" id="numeroPasseport" name="numeroPasseport" value="${prefillNumeroPasseport}" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="paysDelivrance" class="form-label">Pays de delivrance <span class="text-muted">(Optionnel)</span></label>
                    <input type="text" class="form-control" id="paysDelivrance" name="paysDelivrance" value="${prefillPaysDelivrance}">
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="dateDelivrance" class="form-label">Date de delivrance <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
                    <input type="date" class="form-control" id="dateDelivrance" name="dateDelivrance" value="${prefillDateDelivrance}" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="dateExpiration" class="form-label">Date d'expiration <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
                    <input type="date" class="form-control" id="dateExpiration" name="dateExpiration" value="${prefillDateExpiration}" required>
                </div>
            </div>

            <hr>
            <h5 class="mt-2 mb-3 text-secondary">Visa transformable</h5>

            <div class="row">
                <div class="col-md-4 mb-3">
                    <label for="numeroReference" class="form-label">Numero de reference <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
                          <input type="text" class="form-control" id="numeroReference" name="numeroReference"
                              value="${prefillNumeroReference}" pattern="(REF-\d{4}-\d{4}|VISA-\d{4}-\d{3})" required>
                        <div class="form-text">Exemples: VISA-2024-001 ou REF-2026-0001</div>
                </div>
                <div class="col-md-4 mb-3">
                    <label for="dateEntree" class="form-label">Date d'entree <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
                    <input type="date" class="form-control" id="dateEntree" name="dateEntree" value="${prefillDateEntree}" required>
                </div>
                <div class="col-md-4 mb-3">
                    <label for="dateSortie" class="form-label">Date de sortie <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
                    <input type="date" class="form-control" id="dateSortie" name="dateSortie" value="${prefillDateSortie}" required>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="typeDemande" class="form-label">Type de demande <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
                    <select class="form-select" id="typeDemande" name="typeDemande.id" required>
                        <option value="">-- Selectionner --</option>
                        <c:forEach items="${typeDemandes}" var="typeDemande">
                            <option value="${typeDemande.id}" ${prefillTypeDemandeId == typeDemande.id ? 'selected' : ''}>
                                ${typeDemande.libelle}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <hr>
            <h5 class="mt-2 mb-3 text-secondary">Pieces communes</h5>

            <div id="piecesCommunes" class="row">
                <div class="col-md-4 mb-2">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="photos" name="photos" value="true" checked>
                        <label class="form-check-label" for="photos">02 photos</label>
                    </div>
                </div>
                <div class="col-md-4 mb-2">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="notice" name="notice" value="true" checked>
                        <label class="form-check-label" for="notice">Notice</label>
                    </div>
                </div>
                <div class="col-md-4 mb-2">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="demandeMinistre" name="demandeMinistre" value="true" checked>
                        <label class="form-check-label" for="demandeMinistre">Demande adressee au ministre</label>
                    </div>
                </div>
                <div class="col-md-4 mb-2">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="photocopieVisa" name="photocopieVisa" value="true" checked>
                        <label class="form-check-label" for="photocopieVisa">Photocopie visa</label>
                    </div>
                </div>
                <div class="col-md-4 mb-2">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="photocopiePasseport" name="photocopiePasseport" value="true" checked>
                        <label class="form-check-label" for="photocopiePasseport">Photocopie passeport</label>
                    </div>
                </div>
                <div class="col-md-4 mb-2">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="photocopieCarteResident" name="photocopieCarteResident" value="true" checked>
                        <label class="form-check-label" for="photocopieCarteResident">Photocopie carte resident</label>
                    </div>
                </div>
                <div class="col-md-4 mb-2">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="certificatResidence" name="certificatResidence" value="true" checked>
                        <label class="form-check-label" for="certificatResidence">Certificat residence</label>
                    </div>
                </div>
                <div class="col-md-4 mb-2">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="casierJudiciaire" name="casierJudiciaire" value="true" checked>
                        <label class="form-check-label" for="casierJudiciaire">Casier judiciaire</label>
                    </div>
                </div>
            </div>

            <hr>
            <h5 class="mt-2 mb-3 text-secondary">Type de visa</h5>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="typeVisa" class="form-label">Type de visa <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
                    <select class="form-select" id="typeVisa" name="typeVisa.id" required>
                        <option value="">-- Selectionner --</option>
                        <c:forEach items="${typeVisas}" var="typeVisa">
                            <option value="${typeVisa.id}" data-visa-label="${typeVisa.libelle}" ${prefillTypeVisaId == typeVisa.id ? 'selected' : ''}>
                                ${typeVisa.libelle}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Pieces specifiques</label>
                    <div class="text-muted">Selection automatique selon le type de visa.</div>
                </div>
            </div>

            <hr>
            <h5 class="mt-2 mb-3 text-secondary">Pieces specifiques</h5>

            <div id="piecesInvestisseur" style="display:none;">
                <div class="row">
                    <div class="col-md-4 mb-2">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="statutSociete" name="statutSociete" value="true" checked>
                            <label class="form-check-label" for="statutSociete">Statut societe</label>
                        </div>
                    </div>
                    <div class="col-md-4 mb-2">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="extraitRCS" name="extraitRCS" value="true" checked>
                            <label class="form-check-label" for="extraitRCS">Extrait RCS</label>
                        </div>
                    </div>
                    <div class="col-md-4 mb-2">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="carteFiscale" name="carteFiscale" value="true" checked>
                            <label class="form-check-label" for="carteFiscale">Carte fiscale</label>
                        </div>
                    </div>
                </div>
            </div>

            <div id="piecesTravailleur" style="display:none;">
                <div class="row">
                    <div class="col-md-6 mb-2">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="autorisationEmploi" name="autorisationEmploi" value="true" checked>
                            <label class="form-check-label" for="autorisationEmploi">Autorisation emploi</label>
                        </div>
                    </div>
                    <div class="col-md-6 mb-2">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="attestationEmployeur" name="attestationEmployeur" value="true" checked>
                            <label class="form-check-label" for="attestationEmployeur">Attestation employeur</label>
                        </div>
                    </div>
                </div>
            </div>

            <hr>
            <button type="submit" class="btn btn-primary">Enregistrer</button>
            <a href="${pageContext.request.contextPath}/demande/liste" class="btn btn-secondary">Annuler</a>
        </form>
    </div>
</div>

<script>
    function getVisaLabel(selectEl) {
        if (!selectEl || selectEl.selectedIndex < 0) {
            return '';
        }
        var option = selectEl.options[selectEl.selectedIndex];
        return (option && option.dataset && option.dataset.visaLabel)
            ? option.dataset.visaLabel.toLowerCase()
            : '';
    }

    function afficherPiecesSpecifiques() {
        var typeVisa = document.getElementById('typeVisa');
        var label = getVisaLabel(typeVisa);
        document.getElementById('piecesInvestisseur').style.display = label.includes('investisseur') ? 'block' : 'none';
        document.getElementById('piecesTravailleur').style.display = label.includes('travailleur') ? 'block' : 'none';
    }

    function parseDate(value) {
        if (!value) {
            return null;
        }
        return new Date(value + 'T00:00:00');
    }

    function showFormError(message) {
        var container = document.getElementById('formError');
        var text = document.getElementById('formErrorText');
        if (!container || !text) {
            return;
        }
        text.textContent = message;
        container.style.display = 'block';
        container.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }

    document.addEventListener('DOMContentLoaded', function () {
        var typeVisa = document.getElementById('typeVisa');
        var form = document.getElementById('demandeForm');
        var inputs = form ? form.querySelectorAll('input, select, textarea') : [];

        if (typeVisa) {
            afficherPiecesSpecifiques();
            typeVisa.addEventListener('change', afficherPiecesSpecifiques);
        }

        if (inputs.length) {
            inputs.forEach(function (input) {
                input.addEventListener('input', function () {
                    var errorBox = document.getElementById('formError');
                    if (errorBox) {
                        errorBox.style.display = 'none';
                    }
                });
            });
        }

        if (form) {
            form.addEventListener('submit', function (event) {
                var errorBox = document.getElementById('formError');
                if (errorBox) {
                    errorBox.style.display = 'none';
                }
                if (!form.checkValidity()) {
                    event.preventDefault();
                    form.reportValidity();
                    return;
                }

                var email = document.getElementById('email');
                if (email && email.value) {
                    var emailRegex = /^[A-Za-z0-9+_.-]+@(.+)$/;
                    if (!emailRegex.test(email.value)) {
                        event.preventDefault();
                        showFormError('Le format de l\'email est invalide');
                        return;
                    }
                }

                var dateExpiration = parseDate(document.getElementById('dateExpiration').value);
                var today = new Date();
                today.setHours(0, 0, 0, 0);
                if (dateExpiration && dateExpiration <= today) {
                    event.preventDefault();
                    showFormError('La date d\'expiration doit etre superieure a aujourd\'hui');
                    return;
                }

                var dateEntree = parseDate(document.getElementById('dateEntree').value);
                var dateSortie = parseDate(document.getElementById('dateSortie').value);
                if (dateEntree && dateSortie && dateSortie <= dateEntree) {
                    event.preventDefault();
                    showFormError('La date de sortie doit etre superieure a la date d\'entree');
                }
            });
        }
    });
</script>
