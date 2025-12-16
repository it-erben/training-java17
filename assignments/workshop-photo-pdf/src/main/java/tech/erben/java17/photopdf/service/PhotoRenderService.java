package tech.erben.java17.photopdf.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.erben.java17.photopdf.model.ColorMode;
import tech.erben.java17.photopdf.model.PrintOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PhotoRenderService {

    private final ImageProcessingService imageProcessingService;

    public PhotoRenderService(ImageProcessingService imageProcessingService) {
        this.imageProcessingService = imageProcessingService;
    }

    public byte[] renderPdf(MultipartFile file, PrintOptions printOptions) throws IOException {
        BufferedImage sourceImage = ImageIO.read(file.getInputStream());
        if (sourceImage == null) {
            throw new IOException("Konnte das hochgeladene Bild nicht lesen.");
        }

        BufferedImage processedImage = imageProcessingService.applyColorMode(sourceImage, printOptions.colorMode());

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDFont font = loadFont(document);

            PDImageXObject imageXObject = LosslessFactory.createFromImage(document, processedImage);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                drawImageWithMetadata(file, printOptions, processedImage, page, imageXObject, contentStream, font);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    private PDFont loadFont(PDDocument document) throws IOException {
        try (InputStream fontStream = getClass().getResourceAsStream("/org/apache/pdfbox/resources/ttf/LiberationSans-Regular.ttf")) {
            if (fontStream == null) {
                throw new IOException("Schriftart konnte nicht geladen werden.");
            }
            return PDType0Font.load(document, fontStream);
        }
    }

    private void drawImageWithMetadata(MultipartFile file,
                                       PrintOptions printOptions,
                                       BufferedImage processedImage,
                                       PDPage page,
                                       PDImageXObject imageXObject,
                                       PDPageContentStream contentStream,
                                       PDFont font) throws IOException {
        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();
        float margin = 48f;
        float textBlockHeight = 140f;

        int imagePixelWidth = processedImage.getWidth();
        int imagePixelHeight = processedImage.getHeight();
        float scaleX = (pageWidth - 2 * margin) / imagePixelWidth;
        float scaleY = (pageHeight - textBlockHeight - 2 * margin) / imagePixelHeight;
        float scale = Math.min(scaleX, scaleY);

        float drawWidth = imagePixelWidth * scale;
        float drawHeight = imagePixelHeight * scale;
        float x = (pageWidth - drawWidth) / 2;
        float y = pageHeight - margin - drawHeight;

        contentStream.drawImage(imageXObject, x, y, drawWidth, drawHeight);
        contentStream.beginText();
        contentStream.setFont(font, 11);
        contentStream.newLineAtOffset(margin, margin + textBlockHeight - 16);

        float lineHeight = 14f;
        String metadataBlock = buildMetadataBlock(file, printOptions, processedImage);
        String[] lines = metadataBlock.split("\n");
        for (String line : lines) {
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, -lineHeight);
        }

        contentStream.endText();
    }

    private String buildMetadataBlock(MultipartFile file, PrintOptions printOptions, BufferedImage processedImage) {
        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "unbenannt";
        long sizeInKb = file.getSize() / 1024;
        String requestedBy = printOptions.requestedBy() != null && !printOptions.requestedBy().isBlank()
                ? printOptions.requestedBy()
                : "Unbekannt";
        String annotation = printOptions.annotation() != null && !printOptions.annotation().isBlank()
                ? printOptions.annotation()
                : "Keine Anmerkung hinterlassen.";

        String metadata = "Bild-Upload Report\n" +
                "Dateiname: " + originalFilename + " (" + sizeInKb + " KB)\n" +
                "Auflösung: " + processedImage.getWidth() + " x " + processedImage.getHeight() + " px\n" +
                "Farbmodus: " + readableColorMode(printOptions.colorMode()) + "\n" +
                "Hochgeladen von: " + requestedBy + "\n" +
                "Kommentar: " + annotation + "\n" +
                "Generiert am: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

        return metadata;
    }

    private String readableColorMode(ColorMode colorMode) {
        return colorMode != null ? colorMode.label() : ColorMode.COLOR.label();
    }
}
