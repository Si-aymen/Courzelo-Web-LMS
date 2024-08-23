package org.example.courzelo.dto.requests;


import java.util.List;

public class InterviewDTO {

    private String id;

    private String interviewer;

    private List<String> interviewee;

    private String admission;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterviewer() {
        return interviewer;
    }

    public void setInterviewer(String interviewer) {
        this.interviewer = interviewer;
    }

    public List<String> getInterviewee() {
        return interviewee;
    }

    public void setInterviewee(List<String> interviewee) {
        this.interviewee = interviewee;
    }

    public String getAdmission() {
        return admission;
    }

    public void setAdmission(String admission) {
        this.admission = admission;
    }
}
