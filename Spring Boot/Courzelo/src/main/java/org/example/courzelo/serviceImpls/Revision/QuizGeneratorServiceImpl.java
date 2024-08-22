package org.example.courzelo.serviceImpls.Revision;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.RevisionEntities.revision.FileMetadatarevision;
import org.example.courzelo.models.RevisionEntities.QizzRevision.QuestionRevision;
import org.example.courzelo.models.RevisionEntities.QizzRevision.QuizRevision;

import org.example.courzelo.repositories.RevisionRepo.FileMetadatarevisionRepository;
import org.example.courzelo.repositories.RevisionRepo.QuizzRevisionRepo.AnswerRevisionRepository;
import org.example.courzelo.repositories.RevisionRepo.QuizzRevisionRepo.QuestionRevisionRepository;
import org.example.courzelo.repositories.RevisionRepo.QuizzRevisionRepo.QuizRevisionRepository;
import org.example.courzelo.repositories.RevisionRepo.RevisionRepository;
import org.example.courzelo.services.Revision.IQuizGenerator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@Slf4j
@RequiredArgsConstructor
public class QuizGeneratorServiceImpl implements IQuizGenerator {

    @Autowired
    private final QuizRevisionRepository quizRevisionRepository;
    @Autowired
    private final QuestionRevisionRepository questionRevisionRepository;
    @Autowired
    private final FileMetadatarevisionRepository fileMetadatarevisionRepository;

    public final RevisionRepository revisionRepository ;
    @Autowired
    private AnswerRevisionRepository answerRevisionRepository;
    @Override
    public void generateQuestionsFromPdf(String filePath, String revisionId) throws IOException {
        try (InputStream pdfInputStream = new UrlResource(filePath).getInputStream()) {
            // Process PDF to generate questions
            PDDocument pdfDocument = PDDocument.load(pdfInputStream);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(pdfDocument);
            pdfDocument.close();

            // Check if text extraction is successful
            if (text == null || text.isEmpty()) {
                log.error("No text extracted from the PDF.");
                throw new RuntimeException("No text extracted from the PDF.");
            } else {
                log.debug("Extracted PDF text: {}", text);
            }

            // Parse questions from the PDF text
            List<QuestionRevision> questions = parseQuestionsFromText(text);

            // Ensure at least one question is parsed
            if (questions.isEmpty()) {
                log.error("No questions parsed from the PDF text.");
                throw new RuntimeException("No questions parsed from the PDF text.");
            }

            // Extract the file name from the path and set it as the quiz title
            String fileName = Paths.get(new UrlResource(filePath).getURI()).getFileName().toString();
            String quizTitle = fileName.substring(0, fileName.lastIndexOf('.'));

            // Fetch or create the FileMetadatarevision entity
            FileMetadatarevision fileMetadatarevision = fileMetadatarevisionRepository.findByFileDownloadUri(filePath);
            if (fileMetadatarevision == null) {
                fileMetadatarevision = new FileMetadatarevision();
                fileMetadatarevision.setFileDownloadUri(filePath);
                fileMetadatarevision.setRevisionId(revisionId); // Ensure you set the revisionId
                fileMetadatarevisionRepository.save(fileMetadatarevision);
            }

            // Fetch or create the QuizRevision entity
            QuizRevision existingQuizRevision = quizRevisionRepository.findByTitle(quizTitle).orElse(null);
            if (existingQuizRevision == null) {
                // Create a new QuizRevision if it does not exist
                QuizRevision newQuizRevision = new QuizRevision();
                newQuizRevision.setTitle(quizTitle); // Set the quiz title
                newQuizRevision.setFileMetadatarevision(fileMetadatarevision); // Link the file metadata
                newQuizRevision.setRevision(revisionRepository.findById(revisionId)
                        .orElseThrow(() -> new RuntimeException("Revision not found for the id: " + revisionId))); // Ensure you set the revision
                existingQuizRevision = quizRevisionRepository.save(newQuizRevision);
            }

            // Link the questions to the quiz revision
            for (QuestionRevision question : questions) {
                question.setQuizRevision(existingQuizRevision);
                questionRevisionRepository.save(question);
            }

            // Set the list of questions in the quizRevision
            existingQuizRevision.setQuestions(questions);
            quizRevisionRepository.save(existingQuizRevision);

        } catch (IOException e) {
            log.error("IOException occurred while processing PDF: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            throw new RuntimeException("Error processing PDF", e);
        }
    }


    @Override
    public List<QuestionRevision> parseQuestionsFromText(String text) {
        List<QuestionRevision> questions = new ArrayList<>();

        log.info("Extracted PDF text (length: {}): {}", text.length(), text);

        // Patterns to match questions and answers
        String questionPattern = "What[^?]*\\?";
        String answerPattern = "Answer:[^.]*\\.";

        // Combine patterns to capture both question and answer
        Pattern pattern = Pattern.compile(questionPattern + "\\s*" + answerPattern, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String matchedText = matcher.group();
            log.info("Matched text: {}", matchedText);

            // Extract question and answer
            String[] parts = matchedText.split("Answer:");
            if (parts.length == 2) {
                String questionText = parts[0].trim();
                String answerText = parts[1].trim().replaceFirst("\\.$", "").trim();

                log.info("Extracted question: {}", questionText);
                log.info("Extracted answer: {}", answerText);

                QuestionRevision question = new QuestionRevision();
                question.setText(questionText);
                question.setCorrectAnswer(answerText);
                questions.add(question);
            } else {
                log.warn("Unexpected format for matched text: {}", matchedText);
            }
        }

        // Log the total number of questions parsed
        log.info("Total questions parsed: {}", questions.size());

        return questions;
    }


    @Override
    public String getFilePath(String id) {
        FileMetadatarevision fileMetadata = fileMetadatarevisionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        return fileMetadata.getFileDownloadUri();
    }

    @Override
    public String getRevisionId(String id) {
        FileMetadatarevision fileMetadata = fileMetadatarevisionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        return fileMetadata.getRevisionId();
    }
    @Override
    public List<QuizRevision> getQuizByRevisionId(String revisionId) {
        return quizRevisionRepository.findByRevision_Id ( revisionId);
    }
    @Override
    public List<QuestionRevision> getQuestionsByQuizRevisionId(String quizRevisionId) {
        return questionRevisionRepository.findByQuizRevisionId(quizRevisionId);
    }


    @Override
    public boolean checkAnswer(String questionId, String userAnswerText) {
        // Fetch the question from the database
        QuestionRevision question = questionRevisionRepository.findById(questionId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Question not found"));

        // Get raw data for debugging
        String rawCorrectAnswer = question.getCorrectAnswer();
        String rawUserAnswer = userAnswerText;

        // Normalize both the correct answer and user's answer
        String normalizedCorrectAnswer = normalizeWhitespace(rawCorrectAnswer).toLowerCase();
        String normalizedUserAnswer = normalizeWhitespace(rawUserAnswer).toLowerCase();

        // Debugging: Print out the values being compared
        System.out.println("Raw Correct Answer: '" + rawCorrectAnswer + "'");
        System.out.println("Raw User Answer: '" + rawUserAnswer + "'");
        System.out.println("Normalized Correct Answer: '" + normalizedCorrectAnswer + "'");
        System.out.println("Normalized User Answer: '" + normalizedUserAnswer + "'");
        System.out.println("Length of Correct Answer: " + normalizedCorrectAnswer.length());
        System.out.println("Length of User Answer: " + normalizedUserAnswer.length());

        // Compare the user's answer with the correct answer
        boolean isCorrect = normalizedCorrectAnswer.equals(normalizedUserAnswer);

        // Debugging: Print out the result of the comparison
        System.out.println("Is Correct: " + isCorrect);

        // Update the question with the user's answer and whether it was correct
        question.setUserAnswerText(userAnswerText);  // Store the original user input
        question.setIsCorrect(isCorrect);

        // Save the updated question to MongoDB
        QuestionRevision savedQuestion = questionRevisionRepository.save(question);

        // Debugging: Print out the saved question details
        System.out.println("Saved Question: " + savedQuestion);

        return isCorrect;
    }

    private String normalizeWhitespace(String str) {
        return str.replaceAll("\\s+", " ").trim();
    }
}

