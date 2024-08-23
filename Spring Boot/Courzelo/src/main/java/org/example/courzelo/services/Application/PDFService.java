package org.example.courzelo.services.Application;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PDFService {

    public String extractText(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return "File is empty";
        }

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error extracting text from PDF";
        }
    }


}
