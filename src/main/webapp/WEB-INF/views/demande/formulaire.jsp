<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
                <strong>Erreur :</strong> ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>Erreur de validation :</strong> ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${not empty fieldErrors}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>Veuillez corriger les erreurs suivantes :</strong>
                <ul>
                    <c:forEach items="${fieldErrors}" var="error">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}${empty formAction ? '/demande/save' : formAction}" method="post" id="demandeForm" novalidate>
            
            <%-- --- AJOUT DES INPUTS CACHÉS POUR LA PROVENANCE --- --%>
            <c:set var="prov" value="${not empty provenance ? provenance : (not empty param.provenance ? param.provenance : 'CLASSIQUE')}" />
            <input type="hidden" name="provenance" value="${prov}">
            <%-- -------------------------------------------------- --%>

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
                    <label for="email" class="form-label">Email <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
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
                    <label for="paysDelivrance" class="form-label">Pays de delivrance <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
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
                    <label for="dateEntree" class="form-label">Date de delivrance <span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
                    <input type="date" class="form-control" id="dateEntree" name="dateEntree" value="${prefillDateEntree}" required>
                </div>
                <div class="col-md-4 mb-3">
                    <label for="dateSortie" class="form-label">Date d'expiration<span class="text-danger">*</span> <span class="text-muted">(Obligatoire)</span></label>
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
            <h5 class="mt-2 mb-3 text-secondary">Informations du visa à générer</h5>
            
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="dateDebutVisa" class="form-label">Date de debut de validité <span class="text-danger">*</span></label>
                    <input type="date" class="form-control" id="dateDebutVisa" name="dateFinVisa" 
                        value="${prefillDateDebutVisa}" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="dateFinVisa" class="form-label">Date de fin de validité <span class="text-danger">*</span></label>
                    <input type="date" class="form-control" id="dateFinVisa" name="dateFinVisa" 
                        value="${prefillDateFinVisa}" required>
                </div>
            </div>

            <hr>
            <h5 class="mt-2 mb-3 text-secondary">Pieces communes</h5>

            <div id="piecesCommunes" class="row">
                <c:forEach items="${listpiece}" var="piece">
                    <c:if test="${piece.typeVisa == null}">
                        <div class="col-md-4 mb-2">
                            <div class="form-check">
                                <c:set var="isChecked" value="" />
                                <c:forEach items="${pieceDemandes}" var="pd">
                                    <c:if test="${pd.piece.id == piece.id}">
                                        <c:set var="isChecked" value="checked" />
                                    </c:if>
                                </c:forEach>

                                <input class="form-check-input" type="checkbox" 
                                       id="piece_${piece.id}" name="piecesIds" 
                                       value="${piece.id}" ${isChecked}>
                                <label class="form-check-label" for="piece_${piece.id}">
                                    ${piece.libelle}
                                </label>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <hr>
            <h5 class="mt-2 mb-3 text-secondary">Type de visa</h5>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="typeVisa" class="form-label">Type de visa <span class="text-danger">*</span></label>
                    <select class="form-select" id="typeVisa" name="typeVisa.id" required>
                        <option value="">-- Selectionner --</option>
                        <c:forEach items="${typeVisas}" var="tv">
                            <option value="${tv.id}" data-visa-label="${tv.libelle}" 
                                ${(not empty prefillTypeVisaId and prefillTypeVisaId == tv.id) or (demandeEntity.typeVisa.id == tv.id) ? 'selected' : ''}>
                                ${tv.libelle}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <hr>
            <h5 class="mt-2 mb-3 text-secondary">Pieces specifiques</h5>

            <div id="piecesInvestisseur" style="${demande.typeVisa.id == 1 ? 'display:block;' : 'display:none;'}">
                <div class="row">
                    <c:forEach items="${listpiece}" var="piece">
                        <c:if test="${piece.typeVisa != null && piece.typeVisa.id == 1}">
                            <div class="col-md-4 mb-2">
                                <div class="form-check">
                                    <c:set var="isChecked" value="" />
                                    <c:forEach items="${pieceDemandes}" var="pd">
                                        <c:if test="${pd.piece.id == piece.id}">
                                            <c:set var="isChecked" value="checked" />
                                        </c:if>
                                    </c:forEach>
                                    <input class="form-check-input" type="checkbox" 
                                           id="piece_${piece.id}" name="piecesIds" 
                                           value="${piece.id}" ${isChecked}>
                                    <label class="form-check-label" for="piece_${piece.id}">${piece.libelle}</label>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </div>

            <div id="piecesTravailleur" style="${demande.typeVisa.id == 2 ? 'display:block;' : 'display:none;'}">
                <div class="row">
                    <c:forEach items="${listpiece}" var="piece">
                        <c:if test="${piece.typeVisa != null && piece.typeVisa.id == 2}">
                            <div class="col-md-4 mb-2">
                                <div class="form-check">
                                    <c:set var="isChecked" value="" />
                                    <c:forEach items="${pieceDemandes}" var="pd">
                                        <c:if test="${pd.piece.id == piece.id}">
                                            <c:set var="isChecked" value="checked" />
                                        </c:if>
                                    </c:forEach>
                                    <input class="form-check-input" type="checkbox" 
                                           id="piece_${piece.id}" name="piecesIds" 
                                           value="${piece.id}" ${isChecked}>
                                    <label class="form-check-label" for="piece_${piece.id}">${piece.libelle}</label>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </div>

            <hr>
            <%-- --- BOUTON DYNAMIQUE SELON PROVENANCE --- --%>
            <button type="submit" class="btn btn-primary">
                <c:choose>
                    <c:when test="${prov == 'TRANSFERT' || prov == 'TRANSFERT_DUPLICATA'}">
                        Enregistrer et continuer vers le transfert
                    </c:when>
                    <c:when test="${prov == 'DUPLICATA'}">
                        Enregistrer et voir le résumé
                    </c:when>
                    <c:otherwise>
                        Enregistrer les modifications
                    </c:otherwise>
                </c:choose>
            </button>
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

    function getFieldLabel(field) {
        var labels = {
            nom: 'Nom',
            prenom: 'Prenom',
            dateNaissance: 'Date de naissance',
            lieuNaissance: 'Lieu de naissance',
            idSituationFamiliale: 'Situation familiale',
            idNationaliteActuelle: 'Nationalite',
            telephone: 'Contact',
            adresse: 'Adresse',
            email: 'Email',
            numeroPasseport: 'Numero de passeport',
            paysDelivrance: 'Pays de delivrance',
            dateDelivrance: 'Date de delivrance',
            dateExpiration: "Date d'expiration",
            numeroReference: 'Numero de reference',
            dateEntree: 'Date de delivrance',
            dateSortie: "Date d'expiration",
            typeDemande: 'Type de demande',
            dateDebutVisa: 'Date de debut de validite',
            dateFinVisa: 'Date de fin de validite',
            typeVisa: 'Type de visa'
        };
        return labels[field.id] || labels[field.name] || 'Champ';
    }

    function getFieldErrorElement(field) {
        var errorId = (field.id || field.name) + 'Error';
        var existing = document.getElementById(errorId);
        if (existing) {
            return existing;
        }

        var error = document.createElement('div');
        error.className = 'invalid-feedback d-block';
        error.id = errorId;
        field.insertAdjacentElement('afterend', error);
        return error;
    }

    function setFieldError(field, message) {
        if (!field) {
            return;
        }
        var error = getFieldErrorElement(field);
        field.classList.add('is-invalid');
        error.textContent = message;
    }

    function clearFieldError(field) {
        if (!field) {
            return;
        }
        var errorId = (field.id || field.name) + 'Error';
        var error = document.getElementById(errorId);
        if (error) {
            error.textContent = '';
        }
        field.classList.remove('is-invalid');
    }

    function validateRequiredFields(form) {
        var fields = form.querySelectorAll('input:not([type="hidden"]):not([type="checkbox"]):not([type="radio"]):not([type="submit"]):not([type="button"]), select, textarea');
        var firstInvalid = null;
        var hasError = false;

        fields.forEach(function (field) {
            var value = field.value == null ? '' : field.value.trim();
            if (!value) {
                setFieldError(field, 'Le champ ' + getFieldLabel(field) + ' est obligatoire.');
                if (!firstInvalid) {
                    firstInvalid = field;
                }
                hasError = true;
                return;
            }

            clearFieldError(field);
        });

        var email = document.getElementById('email');
        if (email && email.value.trim()) {
            var emailRegex = /^[A-Za-z0-9+_.-]+@(.+)$/;
            if (!emailRegex.test(email.value.trim())) {
                setFieldError(email, 'Le format de l\'email est invalide.');
                if (!firstInvalid) {
                    firstInvalid = email;
                }
                hasError = true;
            }
        }

        var numeroReference = document.getElementById('numeroReference');
        if (numeroReference && numeroReference.value.trim()) {
            var referenceRegex = /^(REF-\d{4}-\d{4}|VISA-\d{4}-\d{3})$/;
            if (!referenceRegex.test(numeroReference.value.trim())) {
                setFieldError(numeroReference, 'Le numero de reference doit suivre le format attendu.');
                if (!firstInvalid) {
                    firstInvalid = numeroReference;
                }
                hasError = true;
            }
        }

        return {
            hasError: hasError,
            firstInvalid: firstInvalid
        };
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
                if (input.type === 'hidden' || input.type === 'checkbox' || input.type === 'radio') {
                    return;
                }

                var clearHandler = function () {
                    var errorBox = document.getElementById('formError');
                    if (errorBox) {
                        errorBox.style.display = 'none';
                    }
                    clearFieldError(input);
                };

                input.addEventListener('input', clearHandler);
                input.addEventListener('change', clearHandler);
            });
        }

        if (form) {
            form.addEventListener('submit', function (event) {
                var errorBox = document.getElementById('formError');
                if (errorBox) {
                    errorBox.style.display = 'none';
                }

                var validation = validateRequiredFields(form);
                if (validation.hasError) {
                    event.preventDefault();
                    if (validation.firstInvalid && validation.firstInvalid.focus) {
                        validation.firstInvalid.focus();
                    }
                    showFormError('Veuillez remplir tous les champs obligatoires du formulaire.');
                    return;
                }

                var email = document.getElementById('email');

                var dateExpiration = parseDate(document.getElementById('dateExpiration').value);
                var today = new Date();
                today.setHours(0, 0, 0, 0);
                if (dateExpiration && dateExpiration <= today) {
                    event.preventDefault();
                    setFieldError(document.getElementById('dateExpiration'), 'La date d\'expiration doit etre superieure a aujourd\'hui.');
                    showFormError('La date d\'expiration doit etre superieure a aujourd\'hui');
                    return;
                }

                var dateEntree = parseDate(document.getElementById('dateEntree').value);
                var dateSortie = parseDate(document.getElementById('dateSortie').value);
                if (dateEntree && dateSortie && dateSortie <= dateEntree) {
                    event.preventDefault();
                    setFieldError(document.getElementById('dateSortie'), 'La date de sortie doit etre superieure a la date d\'entree.');
                    showFormError('La date de sortie doit etre superieure a la date d\'entree');
                }
            });
        }
    });
</script>
