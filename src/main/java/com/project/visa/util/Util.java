package com.project.visa.util;

import java.nio.file.FileSystems;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.nio.file.Path;

public class Util {
     public static void genererQRCode(String texte, String nomFichier) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(texte, BarcodeFormat.QR_CODE, 300, 300);
        Path path = FileSystems.getDefault().getPath(nomFichier);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
