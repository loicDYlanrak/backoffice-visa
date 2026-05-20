<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<div class="card">
    <div class="card-header bg-primary text-white">
        <h3>Capture photo et signature - Demande N° ${demande.id}</h3>
    </div>
    <div class="card-body">
        
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>
        
        <div class="row">
            <!-- Section Photo -->
            <div class="col-md-6">
                <div class="card mb-3">
                    <div class="card-header bg-secondary text-white">
                        <h4>Photo d'identité</h4>
                    </div>
                    <div class="card-body text-center">
                        <video id="video" width="100%" height="300" autoplay style="border: 1px solid #ccc; display: none;"></video>
                        <canvas id="canvas" width="400" height="300" style="border: 1px solid #ccc; display: none;"></canvas>
                        <img id="photoPreview" src="${existingPhoto != null ? '/uploads/' + existingPhoto : ''}" 
                             style="max-width: 100%; max-height: 300px; ${existingPhoto != null ? '' : 'display: none;'}" />
                        
                        <div class="mt-3">
                            <button type="button" id="startCameraBtn" class="btn btn-primary" onclick="startCamera()">
                                <i class="bi bi-camera"></i> Prendre une photo
                            </button>
                            <button type="button" id="captureBtn" class="btn btn-success" style="display: none;" onclick="capturePhoto()">
                                <i class="bi bi-camera-fill"></i> Capturer
                            </button>
                            <button type="button" id="retakePhotoBtn" class="btn btn-warning" style="display: none;" onclick="retakePhoto()">
                                <i class="bi bi-arrow-repeat"></i> Reprendre
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Section Signature -->
            <div class="col-md-6">
                <div class="card mb-3">
                    <div class="card-header bg-secondary text-white">
                        <h4>Signature</h4>
                    </div>
                    <div class="card-body text-center">
                        <canvas id="signatureCanvas" width="400" height="200" style="border: 1px solid #ccc; background: white; touch-action: none;"></canvas>
                        <img id="signaturePreview" src="${existingSignature != null ? '/uploads/' + existingSignature : ''}" 
                             style="max-width: 100%; max-height: 200px; ${existingSignature != null ? '' : 'display: none;'}" />
                        
                        <div class="mt-3">
                            <button type="button" id="drawSignatureBtn" class="btn btn-primary" onclick="enableDrawing()">
                                <i class="bi bi-pencil"></i> Dessiner signature
                            </button>
                            <button type="button" id="clearSignatureBtn" class="btn btn-danger" style="display: none;" onclick="clearSignature()">
                                <i class="bi bi-eraser"></i> Effacer
                            </button>
                            <button type="button" id="saveSignatureBtn" class="btn btn-success" style="display: none;" onclick="saveSignature()">
                                <i class="bi bi-check"></i> Valider signature
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <hr>
        
        <!-- Formulaire d'enregistrement -->
        <form id="photoSignatureForm" action="${pageContext.request.contextPath}/demande/save-photo-signature" method="post">
            <input type="hidden" name="demandeId" value="${demande.id}">
            <input type="hidden" id="photoData" name="photoData">
            <input type="hidden" id="signatureData" name="signatureData">
            
            <div class="text-center">
                <button type="button" onclick="validateAndSubmit()" class="btn btn-success btn-lg">
                    <i class="bi bi-save"></i> Enregistrer photo et signature
                </button>
                <a href="${pageContext.request.contextPath}/demande/liste" class="btn btn-secondary btn-lg">
                    <i class="bi bi-arrow-left"></i> Annuler
                </a>
            </div>
        </form>
    </div>
</div>

<!-- Bibliothèques JavaScript pour signature -->
<script src="https://cdn.jsdelivr.net/npm/signature_pad@4.1.7/dist/signature_pad.umd.min.js"></script>

<script>
    let video = document.getElementById('video');
    let canvas = document.getElementById('canvas');
    let photoPreview = document.getElementById('photoPreview');
    let signatureCanvas = document.getElementById('signatureCanvas');
    let signaturePad;
    let photoCaptured = ${existingPhoto != null};
    let signatureDone = ${existingSignature != null};
    let drawingEnabled = false;
    
    // Initialisation Signature Pad
    if (signatureCanvas) {
        signaturePad = new SignaturePad(signatureCanvas, {
            backgroundColor: 'white',
            penColor: 'black',
            velocityFilterWeight: 0.7,
            minWidth: 1,
            maxWidth: 3
        });
        
        // Désactiver le dessin par défaut
        signaturePad.penEnabled = false;
    }
    
    function enableDrawing() {
        drawingEnabled = true;
        signaturePad.penEnabled = true;
        signaturePad.clear();
        document.getElementById('signaturePreview').style.display = 'none';
        signatureCanvas.style.display = 'block';
        document.getElementById('drawSignatureBtn').style.display = 'none';
        document.getElementById('clearSignatureBtn').style.display = 'inline-block';
        document.getElementById('saveSignatureBtn').style.display = 'inline-block';
    }
    
    function clearSignature() {
        signaturePad.clear();
        signatureDone = false;
        document.getElementById('signatureData').value = '';
    }
    
    function saveSignature() {
        if (signaturePad.isEmpty()) {
            alert('Veuillez dessiner votre signature avant de valider.');
            return;
        }
        
        let signatureDataUrl = signaturePad.toDataURL('image/png');
        document.getElementById('signaturePreview').src = signatureDataUrl;
        document.getElementById('signaturePreview').style.display = 'block';
        document.getElementById('signatureData').value = signatureDataUrl;
        signatureCanvas.style.display = 'none';
        signatureDone = true;
        
        // Réinitialiser les boutons
        document.getElementById('drawSignatureBtn').style.display = 'inline-block';
        document.getElementById('clearSignatureBtn').style.display = 'none';
        document.getElementById('saveSignatureBtn').style.display = 'none';
        drawingEnabled = false;
        signaturePad.penEnabled = false;
    }
    
    function startCamera() {
        if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
            navigator.mediaDevices.getUserMedia({ video: true })
                .then(function(stream) {
                    video.srcObject = stream;
                    video.style.display = 'block';
                    canvas.style.display = 'none';
                    photoPreview.style.display = 'none';
                    video.play();
                    document.getElementById('startCameraBtn').style.display = 'none';
                    document.getElementById('captureBtn').style.display = 'inline-block';
                })
                .catch(function(error) {
                    alert("Impossible d'accéder à la caméra: " + error.message);
                });
        } else {
            alert("La caméra n'est pas supportée par ce navigateur.");
        }
    }
    
    function capturePhoto() {
        let context = canvas.getContext('2d');
        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;
        context.drawImage(video, 0, 0, canvas.width, canvas.height);
        
        // Arrêter la caméra
        let stream = video.srcObject;
        if (stream) {
            let tracks = stream.getTracks();
            tracks.forEach(track => track.stop());
        }
        
        video.style.display = 'none';
        canvas.style.display = 'block';
        document.getElementById('captureBtn').style.display = 'none';
        document.getElementById('retakePhotoBtn').style.display = 'inline-block';
        
        // Sauvegarder la photo
        let photoDataUrl = canvas.toDataURL('image/png');
        document.getElementById('photoData').value = photoDataUrl;
        photoPreview.src = photoDataUrl;
        photoCaptured = true;
    }
    
    function retakePhoto() {
        startCamera();
        canvas.style.display = 'none';
        document.getElementById('photoData').value = '';
        photoCaptured = false;
    }
    
    function validateAndSubmit() {
        if (!photoCaptured && !document.getElementById('photoData').value) {
            alert('Veuillez prendre une photo.');
            return;
        }
        
        if (!signatureDone && !document.getElementById('signatureData').value) {
            alert('Veuillez dessiner et valider votre signature.');
            return;
        }
        
        document.getElementById('photoSignatureForm').submit();
    }
    
    // Si les données existent déjà, marquer comme capturées
    if (document.getElementById('photoData').value) {
        photoCaptured = true;
    }
    if (document.getElementById('signatureData').value) {
        signatureDone = true;
    }
</script>

<style>
    .signature-canvas {
        border: 2px solid #ccc;
        border-radius: 5px;
        cursor: crosshair;
    }
    
    #signatureCanvas {
        touch-action: none;
    }
</style>