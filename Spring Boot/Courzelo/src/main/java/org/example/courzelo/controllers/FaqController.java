package org.example.courzelo.controllers;

import lombok.RequiredArgsConstructor;

import org.example.courzelo.models.FAQ;
import org.example.courzelo.models.Ticket;
import org.example.courzelo.serviceImpls.IFaqService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/api/v1/faq")
@RestController
@RequiredArgsConstructor
public class FaqController {
    private final IFaqService faqService;

    @PostMapping("/add")
    public void addFAQ(@RequestBody FAQ faq) {
        faqService.saveFAQ(faq);
    }
    @GetMapping("/all")
    public List<FAQ> getFAQS() {
        return faqService.getAllFAQS();
    }
    @GetMapping("/get/{id}")
    public Optional<FAQ> getFAQ(@PathVariable String id) {
        return faqService.getFAQ( id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFAQ(@PathVariable String id) {
        if (faqService.existbyID(id)) {
            faqService.deleteFAQ(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "FAQ deleted successfully.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "FAQ not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/update/{id}")
    public FAQ updateClass(@PathVariable String id,@RequestBody FAQ faq) {
        String anwser = faq.getAnswer();
        String question = faq.getQuestion();
        return faqService.updateFAQ1(id, anwser, question);
    }
}
