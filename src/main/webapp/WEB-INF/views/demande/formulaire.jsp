<div class="card">
    <div class="card-header bg-success text-white">
        <h3>Demande de TRANSFORMATION DE VISA</h3>
    </div>
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/demande" method="post">

            <h5 class="mt-2 mb-3 text-secondary">Informations personnelles</h5>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="nom" class="form-label">Nom *</label>
                    <input type="text" class="form-control" id="nom" name="nom" value="${prefillNom}" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="prenom" class="form-label">Prenom *</label>
                    <input type="text" class="form-control" id="prenom" name="prenom" value="${prefillPrenom}" required>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="dateNaissance" class="form-label">Date de naissance *</label>
                    <input type="date" class="form-control" id="dateNaissance" name="dateNaissance" value="${prefillDateNaissance}" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="lieuNaissance" class="form-label">Lieu de naissance *</label>
                    <input type="text" class="form-control" id="lieuNaissance" name="lieuNaissance" value="${prefillLieuNaissance}" required>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="nomJeuneFille" class="form-label">Nom de jeune fille</label>
                    <input type="text" class="form-control" id="nomJeuneFille" name="nomJeuneFille" value="${prefillNomJeuneFille}">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="idSituationFamiliale" class="form-label">Situation familiale *</label>
                    <select class="form-select" id="idSituationFamiliale" name="idSituationFamiliale" required>
                        <option value="">-- Selectionner --</option>
                        <option value="1" ${prefillSituationId == 1 ? "selected" : ""}>Celibataire</option>
                        <option value="2" ${prefillSituationId == 2 ? "selected" : ""}>Marie(e)</option>
                        <option value="3" ${prefillSituationId == 3 ? "selected" : ""}>Divorce(e)</option>
                        <option value="4" ${prefillSituationId == 4 ? "selected" : ""}>Veuf/Veuve</option>
                        <option value="5" ${prefillSituationId == 5 ? "selected" : ""}>Pacse(e)</option>
                    </select>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="idGenre" class="form-label">Genre *</label>
                    <select class="form-select" id="idGenre" name="idGenre" required>
                        <option value="">-- Selectionner --</option>
                        <option value="1" ${prefillGenreId == 1 ? "selected" : ""}>Masculin</option>
                        <option value="2" ${prefillGenreId == 2 ? "selected" : ""}>Feminin</option>
                        <option value="3" ${prefillGenreId == 3 ? "selected" : ""}>Autre</option>
                    </select>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="idNationaliteActuelle" class="form-label">Nationalite actuelle *</label>
                    <select class="form-select" id="idNationaliteActuelle" name="idNationaliteActuelle" required>
                        <option value="">-- Selectionner --</option>
                        <option value="1" ${prefillNationaliteActuelleId == 1 ? "selected" : ""}>Malgache</option>
                        <option value="2" ${prefillNationaliteActuelleId == 2 ? "selected" : ""}>Francaise</option>
                        <option value="3" ${prefillNationaliteActuelleId == 3 ? "selected" : ""}>Chinoise</option>
                        <option value="4" ${prefillNationaliteActuelleId == 4 ? "selected" : ""}>Americaine</option>
                        <option value="5" ${prefillNationaliteActuelleId == 5 ? "selected" : ""}>Canadienne</option>
                        <option value="6" ${prefillNationaliteActuelleId == 6 ? "selected" : ""}>Allemande</option>
                        <option value="7" ${prefillNationaliteActuelleId == 7 ? "selected" : ""}>Italienne</option>
                        <option value="8" ${prefillNationaliteActuelleId == 8 ? "selected" : ""}>Espagnole</option>
                        <option value="9" ${prefillNationaliteActuelleId == 9 ? "selected" : ""}>Britannique</option>
                        <option value="10" ${prefillNationaliteActuelleId == 10 ? "selected" : ""}>Belge</option>
                    </select>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="idNationaliteOrigine" class="form-label">Nationalite d'origine *</label>
                    <select class="form-select" id="idNationaliteOrigine" name="idNationaliteOrigine" required>
                        <option value="">-- Selectionner --</option>
                        <option value="1" ${prefillNationaliteOrigineId == 1 ? "selected" : ""}>Malgache</option>
                        <option value="2" ${prefillNationaliteOrigineId == 2 ? "selected" : ""}>Francaise</option>
                        <option value="3" ${prefillNationaliteOrigineId == 3 ? "selected" : ""}>Chinoise</option>
                        <option value="4" ${prefillNationaliteOrigineId == 4 ? "selected" : ""}>Americaine</option>
                        <option value="5" ${prefillNationaliteOrigineId == 5 ? "selected" : ""}>Canadienne</option>
                        <option value="6" ${prefillNationaliteOrigineId == 6 ? "selected" : ""}>Allemande</option>
                        <option value="7" ${prefillNationaliteOrigineId == 7 ? "selected" : ""}>Italienne</option>
                        <option value="8" ${prefillNationaliteOrigineId == 8 ? "selected" : ""}>Espagnole</option>
                        <option value="9" ${prefillNationaliteOrigineId == 9 ? "selected" : ""}>Britannique</option>
                        <option value="10" ${prefillNationaliteOrigineId == 10 ? "selected" : ""}>Belge</option>
                    </select>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="profession" class="form-label">Profession *</label>
                    <input type="text" class="form-control" id="profession" name="profession" value="${prefillProfession}" required>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="telephone" class="form-label">Telephone *</label>
                    <input type="tel" class="form-control" id="telephone" name="telephone" value="${prefillTelephone}" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" name="email" value="${prefillEmail}">
                </div>
            </div>

            <div class="mb-3">
                <label for="adresse" class="form-label">Adresse *</label>
                <textarea class="form-control" id="adresse" name="adresse" rows="2" required>${prefillAdresse}</textarea>
            </div>

            <hr>
            <h5 class="mt-2 mb-3 text-secondary">Informations Passeport</h5>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="numeroPasseport" class="form-label">Numero de passeport *</label>
                    <input type="text" class="form-control" id="numeroPasseport" name="numeroPasseport" value="${prefillNumeroPasseport}" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="dateExpiration" class="form-label">Date d'expiration du passeport *</label>
                    <input type="date" class="form-control" id="dateExpiration" name="dateExpiration" value="${prefillDateExpiration}" required>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="dateDelivrance" class="form-label">Date de delivrance du passeport *</label>
                    <input type="date" class="form-control" id="dateDelivrance" name="dateDelivrance" value="${prefillDateDelivrance}" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="paysDelivrance" class="form-label">Pays de delivrance</label>
                    <input type="text" class="form-control" id="paysDelivrance" name="paysDelivrance" value="${prefillPaysDelivrance}">
                </div>
            </div>

            <hr>
            <h5 class="mt-2 mb-3 text-secondary">Informations Visa</h5>

            <div class="row">
                <div class="col-md-4 mb-3">
                    <label for="typeVisa" class="form-label">Type de visa *</label>
                    <select class="form-select" id="typeVisa" name="typeVisa.id" required
                            onchange="afficherPiecesSpecifiques(this.value)">
                        <option value="">-- Selectionner --</option>
                        <option value="1" ${prefillTypeVisaId == 1 ? "selected" : ""}>Investisseur</option>
                        <option value="2" ${prefillTypeVisaId == 2 ? "selected" : ""}>Travailleur</option>
                    </select>                </div>
                <div class="col-md-4 mb-3">
                    <label for="dateDebut" class="form-label">Date de debut *</label>
                    <input type="date" class="form-control" id="dateDebut" name="dateDebut" value="${prefillDateDebut}" required>
                </div>
                <div class="col-md-4 mb-3">
                    <label for="dateFin" class="form-label">Date de fin *</label>
                    <input type="date" class="form-control" id="dateFin" name="dateFin" value="${prefillDateFin}" required>
                </div>
            </div>

            <hr>
            <h5 class="mt-2 mb-3 text-secondary">Pieces communes</h5>

            <div id="piecesCommunes" class="row">
                <div class="col-md-4 mb-2">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="photos" name="photos" value="true" checked>
                        <label class="form-check-label" for="photos">02 Photos</label>
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
                        <input class="form-check-input" type="checkbox" id="photocopyCarteResident" name="photocopyCarteResident" value="true" checked>
                        <label class="form-check-label" for="photocopyCarteResident">Photocopie carte resident</label>
                    </div>
                </div>
                <div class="col-md-4 mb-2">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="certificatResidence" name="certificatResidence" value="true" checked>
                        <label class="form-check-label" for="certificatResidence">Certificat de residence</label>
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
                    <div class="col-md-4 mb-2">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="autorisationEmploi" name="autorisationEmploi" value="true" checked>
                            <label class="form-check-label" for="autorisationEmploi">Autorisation d'emploi</label>
                        </div>
                    </div>
                    <div class="col-md-4 mb-2">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="attestationEmployeur" name="attestationEmployeur" value="true" checked>
                            <label class="form-check-label" for="attestationEmployeur">Attestation employeur</label>
                        </div>
                    </div>
                </div>
            </div>

            <hr>
            <button type="submit" class="btn btn-primary">Enregistrer</button>
            <a href="${pageContext.request.contextPath}/demandes" class="btn btn-secondary">Annuler</a>
        </form>
    </div>
</div>

<script>
    function afficherPiecesSpecifiques(type) {
        document.getElementById('piecesInvestisseur').style.display = (type === '1') ? 'block' : 'none';
        document.getElementById('piecesTravailleur').style.display  = (type === '2') ? 'block' : 'none';
    }

    function setAllChecked(containerId) {
        var container = document.getElementById(containerId);
        if (!container) {
            return;
        }
        container.querySelectorAll('input[type="checkbox"]').forEach(function (cb) {
            cb.checked = true;
        });
    }

    function areAllChecked(containerId) {
        var container = document.getElementById(containerId);
        if (!container) {
            return true;
        }
        return Array.from(container.querySelectorAll('input[type="checkbox"]')).every(function (cb) {
            return cb.checked;
        });
    }

    document.addEventListener('DOMContentLoaded', function () {
        var typeVisa = document.getElementById('typeVisa');
        var form = document.querySelector('form');
        if (typeVisa) {
            afficherPiecesSpecifiques(typeVisa.value);
            setAllChecked('piecesCommunes');
            if (typeVisa.value === '1') {
                setAllChecked('piecesInvestisseur');
            }
            if (typeVisa.value === '2') {
                setAllChecked('piecesTravailleur');
            }
            typeVisa.addEventListener('change', function () {
                afficherPiecesSpecifiques(this.value);
                if (this.value === '1') {
                    setAllChecked('piecesInvestisseur');
                }
                if (this.value === '2') {
                    setAllChecked('piecesTravailleur');
                }
            });
        }

        if (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    form.reportValidity();
                    return;
                }
                if (!areAllChecked('piecesCommunes')) {
                    event.preventDefault();
                    alert('Toutes les pieces communes doivent etre cochees');
                    return;
                }
                if (typeVisa && typeVisa.value === '1' && !areAllChecked('piecesInvestisseur')) {
                    event.preventDefault();
                    alert('Toutes les pieces investisseur doivent etre cochees');
                    return;
                }
                if (typeVisa && typeVisa.value === '2' && !areAllChecked('piecesTravailleur')) {
                    event.preventDefault();
                    alert('Toutes les pieces travailleur doivent etre cochees');
                    return;
                }
                var email = document.getElementById('email');
                if (email && email.value) {
                    var emailRegex = /^[A-Za-z0-9+_.-]+@(.+)$/;
                    if (!emailRegex.test(email.value)) {
                        event.preventDefault();
                        alert('Le format de l\'email est invalide');
                    }
                }
            });
        }
    });
</script>
