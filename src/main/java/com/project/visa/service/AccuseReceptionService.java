package com.project.visa.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.project.visa.entity.DemandeEntity;
import com.project.visa.entity.DemandeurEntity;
import com.project.visa.entity.PhotoSignatureDemandeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class AccuseReceptionService {

    @Autowired
    private PhotoSignatureDemandeService photoSignatureService;

    public byte[] genererAccuseReception(DemandeEntity demande) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (PdfWriter pdfWriter = new PdfWriter(baos);
                PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                Document document = new Document(pdfDocument, PageSize.A4)) {

            // Définition des polices
            PdfFont fontTitre = PdfFontFactory.createFont();
            PdfFont fontNormal = PdfFontFactory.createFont();
            PdfFont fontBold = PdfFontFactory.createFont();

            // Marges
            document.setMargins(20, 20, 20, 20);

            // En-tête
            Paragraph titre = new Paragraph("ACCUSÉ DE RÉCEPTION")
                    .setFont(fontTitre)
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(titre);

            Paragraph soustitre = new Paragraph("Enregistrement de votre demande de visa")
                    .setFont(fontNormal)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(soustitre);

            // Ligne de séparation
            Paragraph separator = new Paragraph("_".repeat(80))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(separator);

            // Message de confirmation
            Paragraph confirmation = new Paragraph(
                    "Nous vous confirmons que votre demande a bien été enregistrée dans notre système.")
                    .setFont(fontNormal)
                    .setFontSize(11)
                    .setMarginBottom(20)
                    .setTextAlignment(TextAlignment.JUSTIFIED);
            document.add(confirmation);

            // Section avec photo et informations
            Table mainTable = new Table(2);
            mainTable.setWidth(500);
            mainTable.setMarginBottom(20);

            // Colonne gauche : Photo
            Cell photoCell = new Cell();
            photoCell.setVerticalAlignment(VerticalAlignment.TOP);
            photoCell.setBorder(new SolidBorder(ColorConstants.BLACK, 1));
            photoCell.setPadding(10);

            try {
                PhotoSignatureDemandeEntity photoSignature = photoSignatureService.findByDemandeId(demande.getId())
                        .get(0);
                if (photoSignature != null && !photoSignature.getPhotoUrl().isEmpty()) {
                    File photoFile = new File(photoSignature.getPhotoUrl());
                    if (photoFile.exists()) {
                        ImageData imageData = ImageDataFactory.create(photoSignature.getPhotoUrl());
                        Image image = new Image(imageData);
                        image.setWidth(150);
                        image.setHeight(180);
                        photoCell.add(image);
                    } else {
                        photoCell.add(new Paragraph("Photo non disponible")
                                .setTextAlignment(TextAlignment.CENTER)
                                .setFontSize(10));
                    }
                } else {
                    photoCell.add(new Paragraph("Photo non disponible")
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(10));
                }
            } catch (Exception e) {
                photoCell.add(new Paragraph("Erreur lors du chargement de la photo")
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(10));
            }

            mainTable.addCell(photoCell);

            // Colonne droite : Informations
            Cell infoCell = new Cell();
            infoCell.setVerticalAlignment(VerticalAlignment.TOP);
            infoCell.setPadding(10);

            DemandeurEntity demandeur = demande.getDemandeur();

            infoCell.add(new Paragraph("INFORMATIONS DE LA DEMANDE")
                    .setFont(fontBold)
                    .setFontSize(11)
                    .setMarginBottom(10));

            infoCell.add(createInfoLine("Demandeur:",
                    demandeur.getNom() + " " + demandeur.getPrenom()));
            infoCell.add(createInfoLine("Numéro de demande:",
                    String.valueOf(demande.getId())));
            infoCell.add(createInfoLine("Type de visa:",
                    demande.getTypeVisa().getLibelle()));
            infoCell.add(createInfoLine("Type de demande:",
                    demande.getTypeDemande().getLibelle()));
            infoCell.add(createInfoLine("Date de demande:",
                    demande.getDateDemande().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            infoCell.add(createInfoLine("Date d'enregistrement:",
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

            mainTable.addCell(infoCell);
            document.add(mainTable);

            // Séparation
            document.add(new Paragraph("_".repeat(80))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            // Détails additionnels
            Paragraph details = new Paragraph()
                    .setFont(fontNormal)
                    .setFontSize(10)
                    .setMarginBottom(20)
                    .setTextAlignment(TextAlignment.JUSTIFIED);

            details.add("Votre demande a été traitée avec succès et enregistrée dans notre système. "
                    + "Vous pouvez utiliser votre numéro de demande (#" + demande.getId() + ") "
                    + "pour suivre l'état de votre demande.\n\n"
                    + "Les documents suivants ont été reçus et seront traités conformément "
                    + "aux procédures en vigueur.\n\n"
                    + "Vous serez contacté ultérieurement pour toute information supplémentaire.");

            document.add(details);

            // Pied de page
            document.add(new Paragraph("_".repeat(80))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(40));

            Paragraph footer = new Paragraph("Merci d'avoir déposé votre demande. Nous vous souhaitons bonne chance.")
                    .setFont(fontNormal)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10);
            document.add(footer);

        }

        return baos.toByteArray();
    }

    private Paragraph createInfoLine(String label, String value) {
        Paragraph p = new Paragraph();
        p.add(new Paragraph(label)
                .setBold()
                .setFontSize(10)
                .setMarginBottom(2));
        p.add(new Paragraph(value)
                .setFontSize(10)
                .setMarginBottom(10));
        return p;
    }
}
