package org.example.courzelo.services.Application;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class OCRService {

    public String extractTextWithOCR(MultipartFile file) {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("<path_to_tessdata>");

        StringBuilder extractedText = new StringBuilder();

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300); // Convert PDF page to image

                // Perform OCR on the image
                String text = tesseract.doOCR(image);
                extractedText.append(text).append("\n");
            }

        } catch (TesseractException | IOException e) {
            e.printStackTrace();
            return "Error extracting text with OCR";
        }

        return extractedText.toString();
    }
}