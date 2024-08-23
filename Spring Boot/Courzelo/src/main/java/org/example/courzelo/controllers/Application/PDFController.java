package org.example.courzelo.controllers.Application;
import org.example.courzelo.services.Application.OCRService;
import org.example.courzelo.services.Application.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/pdf")
public class PDFController {

    @Autowired
    private PDFService pdfService;

    @Autowired
    private OCRService ocrService;

    @PostMapping("/extract-with-ocr")
    public ResponseEntity<String> extractTextFromScannedPDF(@RequestParam("file") MultipartFile file) {
        String extractedText = ocrService.extractTextWithOCR(file);
        return new ResponseEntity<>(extractedText, HttpStatus.OK);
    }

    @PostMapping("/extract")
    public ResponseEntity<String> extractTextFromPDF(@RequestParam("file") MultipartFile file) {
        try {
            String extractedText = pdfService.extractText(file);
            System.out.println("Extracted Text: " + extractedText);
            if (extractedText.isEmpty()) {
                return new ResponseEntity<>("No text found in PDF", HttpStatus.OK);
            }
            return new ResponseEntity<>(extractedText, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to extract text from PDF", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
