package org.example.courzelo.services.RecruitementService;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

@Service
@Slf4j

public class ResumeParserServiceImpl implements IResumeParserService{
    private StanfordCoreNLP pipeline;
    public ResumeParserServiceImpl() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit");
        this.pipeline = new StanfordCoreNLP(props);

    }

    @Override
    public List<String> parseResume(InputStream resumeInputStream) {
        List<String> parsedEntities = new ArrayList<>();
        // to change later
        // path to the resume
        String path = "D:\\Esprit\\3eme\\documents internship\\test3.pdf";
        try {
            // Extract text from PDF
            String resumeText = extractTextFromPDF(path);
            // Tokenization
            List<String> tokens = tokenize(resumeText);
            // Named Entity Recognition
            List<String> namedEntities = performNER(tokens);
            // Identify skills
            List<String> skills = identifySkills(namedEntities);
            // Identify specialities
            List<String> specialities = identifySpecialities(namedEntities);
            // Add skills and specialities to parsed entities
            parsedEntities.addAll(skills);
            parsedEntities.addAll(specialities);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        System.out.println(parsedEntities);
        return parsedEntities;

    }

    @Override
    public List<String> parseResume(String path) {
        List<String> parsedEntities = new ArrayList<>();
        try {
            // Extract text from PDF
            String resumeText = extractTextFromPDF(path);

            // Debugging: Print extracted text
            System.out.println("Extracted Text: " + resumeText);

            // Tokenization
            List<String> tokens = tokenize(resumeText);

            // Debugging: Print tokens
            System.out.println("Tokens: " + tokens);

            // Named Entity Recognition
            List<String> namedEntities = performNER(tokens);

            // Debugging: Print named entities
            System.out.println("Named Entities: " + namedEntities);

            // Identify skills
            List<String> skills = identifySkills(namedEntities);

            // Debugging: Print identified skills
            System.out.println("Skills: " + skills);

            // Identify specialities
            List<String> specialities = identifySpecialities(namedEntities);
            // Debugging: Print identified specialities
            System.out.println("Specialities: " + specialities);

            // Add skills and specialities to parsed entities
            parsedEntities.addAll(skills);
            parsedEntities.addAll(specialities);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        System.out.println(parsedEntities);
        return parsedEntities;

    }

    @Override
    public String extractTextFromPDF(InputStream inputStream) throws IOException {
        try {
            PDDocument document = PDDocument.load(inputStream);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            document.close();
            return text;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }

    }

    @Override
    public String extractTextFromPDF(String path) throws IOException {
        try {
            File file = new File(path);

            if (!file.exists()) {
                log.error("File not found: " + path);
                return null;
            }

            PDDocument document = PDDocument.load(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            // Replace untokenizable characters
            text = text.replaceAll("[^\\p{Print}]", " ");
            // Replace FontAwesome icons with their textual equivalents
            text = replaceFontAwesomeIcons(text);

            document.close();
            return text;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }

    }
    private String replaceFontAwesomeIcons(String text) {
        // Replace FontAwesome icons with textual equivalents
        text = text.replace("\uF095", "Phone"); // Replace FontAwesome phone icon
        text = text.replace("\uF041", "Map Marker"); // Replace FontAwesome map-marker icon
        text = text.replace("\uF233", "LinkedIn"); // Replace FontAwesome linkedin icon
        text = text.replace("\uF165", "GitHub"); // Replace FontAwesome github icon
        text = text.replace("\uF073", "Calendar"); // Replace FontAwesome calendar icon

        // Add more replacements as needed

        return text;
    }


    @Override
    public List<String> identifySkills(List<String> entities) {
        List<String> skills = new ArrayList<>();
        for (String entity : entities) {
            if (isTechnicalSkill(entity)) {
                skills.add(entity);
            }
        }
        return skills;

    }

    @Override
    public List<String> identifySpecialities(List<String> entities) {
        List<String> specialities = new ArrayList<>();
        for (String entity : entities) {
            if (isSpeciality(entity)) {
                specialities.add(entity);
            }
        }
        return specialities;


    }

    @Override
    public boolean isTechnicalSkill(String entity) {
        List<String> skills = Arrays.asList("java","python","c++","c#","javascript","html","css","sql","php","ruby","swift","kotlin","typescript","go","rust","r","matlab","perl","assembly","bash","powershell","objective-c","groovy","scala","lua","dart","haskell","delphi","cobol","fortran","lisp","prolog","scheme","smalltalk","verilog","vhdl","labview","scratch","blockly","app inventor","alice","kodu","tynker","hopscotch","lightbot","robo","codecombat","codesters","codecademy","khan academy","code.org","scratchjr","tickle","sphero","ozobot","dash and dot","littlebits","makey makey","raspberry pi","arduino");
        return skills.contains(entity.toLowerCase());

    }

    @Override
    public boolean isSpeciality(String entity) {
        List<String> specialities = Arrays.asList("Application Development","Software Developer","Software Engineer","Website Design","Website Developer","Database Administrator","IT Support","Helpdesk","IT Project Manager","IT Consultant","IT Sales Professional","IT Trainer","Network Engineer","Network Administrator","Cyber Security","Data Analyst","Data Scientist","Machine Learning Engineer","AI Engineer","Business Intelligence","Data Mining","Data Warehousing","Data Modelling","Data Visualisation","Big Data","Cloud Computing","DevOps","Embedded Systems","Game Developer","Mobile Developer","Multimedia Programmer","Systems Analyst","Systems Developer","Systems Administrator","Systems Architect","Systems Engineer","Technical Author","Technical Sales","Technical Support","Technical Writer","Telecommunications","Web Developer","Web Designer","Web Content Manager","Web Content Developer","Web Content Editor","Web Content Administrator","Web Content Coordinator","Web Content Producer","Web Content Publisher","Web Content Writer","Web Content Designer","Web Content Analyst","Web Content Strategist","Web Content Consultant","Web Content Specialist","Web Content Supervisor","Web Content Manager","Web Content Director","Web Content Executive","Web Content Officer","Web Content Administrator","Web Content Coordinator","Web Content Publisher","Web Content Producer","Web Content Developer","Web Content Designer","Web Content Writer","Web Content Analyst","Web Content Strategist","Web Content Consultant","Web Content Specialist","Web Content Supervisor","Web Content Manager","Web Content Director","Web Content Executive","Web Content Officer","Web Content Administrator","Web Content Coordinator","Web Content Publisher","Web Content Producer","Web Content Developer","Web Content Designer","Web Content Writer","Web Content Analyst","Web Content Strategist","Web Content Consultant","Web Content Specialist","Web Content Supervisor","Web Content Manager","Web Content Director","Web Content Executive","Web Content Officer","Web Content Administrator","Web Content Coordinator","Web Content Publisher","Web Content Producer","Web Content Developer","Web Content Designer","Web Content Writer","Web Content Analyst","Web Content Strategist","Web Content Consultant","Web Content Specialist","Web Content Supervisor","Web Content Manager","Web Content Director","Web Content Executive","Web Content Officer","Web Content Administrator","Web Content Coordinator","Web Content Publisher","Web Content Producer","Web Content Developer","Web Content Designer","Web Content Writer","Web Content Analyst","Web Content Strategist","Web Content Consultant","Web Content Specialist","Web Content Supervisor","Web Content Manager","Web Content Director","Web Content Executive","Web Content Officer","Web Content Administrator","Web Content Coordinator","Web Content Publisher","Web Content Producer","Web Content Developer","Web Content Designer","Web Content Writer","Web Content Analyst","Web Content Strategist","Web Content Consultant","Web Content Specialist","Web Content Supervisor","Web Content Manager","Web Content Director");
        return specialities.contains(entity.toLowerCase());

    }

    @Override
    public List<String> tokenize(String text) {
        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        List<String> tokens = new ArrayList<>();
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                tokens.add(token.word());
            }
        }
        return tokens;

    }

    @Override
    public List<String> performNER(List<String> tokens) {
        List<String> namedEntities = new ArrayList<>();

        for (String token : tokens) {
            // Check if the token represents a skill or speciality
            if (isTechnicalSkill(token) || isSpeciality(token)) {
                namedEntities.add(token);
            }

        }

        return namedEntities;

    }
}
