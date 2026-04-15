<nav class="navbar navbar-expand-lg navbar-light bg-light rounded shadow-sm">
    <div class="container-fluid">
        <button type="button" id="sidebarCollapse" class="btn btn-dark" style="background: #030BA6; border: none;">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="ms-auto">
            <span class="navbar-text">
                <strong>Bienvenue sur Visa Application</strong>
            </span>
        </div>
    </div>
</nav>

<script>
    document.getElementById('sidebarCollapse').addEventListener('click', function() {
        document.getElementById('sidebar').classList.toggle('active');
    });
</script>