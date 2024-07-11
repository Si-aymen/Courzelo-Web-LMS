package org.example.courzelo.controllers;

import lombok.RequiredArgsConstructor;

import org.example.courzelo.models.FAQ;
import org.example.courzelo.serviceImpls.IFaqService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @DeleteMapping("/delete/{ID}")
    public void deleteClass(@PathVariable String ID) {
        faqService.deleteFAQ(ID);
    }

    @PutMapping("/update/{id}")
    public void updateClass(@RequestBody FAQ faq) {
        faqService.updateFAQ(faq);
    }
}
