<jsp:include page="formulaire.jsp" />
<script>
    document.addEventListener('DOMContentLoaded', function () {
        var form = document.getElementById('demandeForm');
        if (!form) {
            return;
        }

        function addHidden(name, value) {
            if (!value || form.querySelector('input[name="' + name + '"]')) {
                return;
            }
            var input = document.createElement('input');
            input.type = 'hidden';
            input.name = name;
            input.value = value;
            form.appendChild(input);
        }

        addHidden('id', '${prefillDemandeId}');
        addHidden('demandeur.id', '${prefillDemandeurId}');
        addHidden('passeport.id', '${prefillPasseportId}');
        addHidden('visaTransformable.id', '${prefillVisaTransformableId}');
    });
</script>
