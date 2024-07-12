package org.example.courzelo.services.RecruitementService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IResumeParserService {
    //parse resume
    public List<String> parseResume(InputStream resumeInputStream);
    // parse resume in pc
    public  List<String> parseResume(String path);
    // extract text from PDF
    public String extractTextFromPDF(InputStream inputStream) throws IOException;
    // extract text from PDF in pc
    public String extractTextFromPDF(String path) throws IOException;
    // identify skills
    public List<String> identifySkills(List<String> entities);
    // identify specialities
    public List<String> identifySpecialities(List<String> entities);
    // is technical skill
    public boolean isTechnicalSkill(String entity);
    // is Speciality
    public boolean isSpeciality(String entity);
    // tokenize
    public List<String> tokenize(String text);
    //perform NER
    public List<String> performNER(List<String> tokens);
}
