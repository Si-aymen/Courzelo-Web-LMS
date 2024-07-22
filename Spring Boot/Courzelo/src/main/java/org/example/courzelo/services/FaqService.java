package org.example.courzelo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.FAQ;
import org.example.courzelo.models.Ticket;
import org.example.courzelo.repositories.FaqRepository;
import org.example.courzelo.serviceImpls.IFaqService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FaqService implements IFaqService {
    private final FaqRepository faqRepository;
    @Override
    public Optional<FAQ> getFAQ(String id) {
        return faqRepository.findById(id);
    }

    @Override
    public List<FAQ> getAllFAQS() {
        return faqRepository.findAll();
    }

    @Override
    public void deleteFAQ(String id) {
        faqRepository.deleteById(id);
    }

    @Override
    public void saveFAQ(FAQ faq) {
        faqRepository.save(faq);
    }

    @Override
    public void updateFAQ(FAQ faq) {
        faqRepository.save(faq);
    }

    @Override
    public Boolean existbyID(String id) {
        return faqRepository.existsById(id);
    }

    @Override
    public FAQ updateFAQ1(String id, String answer, String question) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faq not found"));

        // Update only specific fields
        faq.setAnswer(answer);
        faq.setQuestion(question);
        return faqRepository.save(faq);
    }
}
