package org.example.courzelo.dto.requests;



public class ApplicationREQ {

    private String id;

    private String userid;

    private String admissionid;
    private String universityid;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUniversityid() {
        return universityid;
    }

    public void setUniversityid(String universityid) {
        this.universityid = universityid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdmissionid() {
        return admissionid;
    }

    public void setAdmissionid(String admissionid) {
        this.admissionid = admissionid;
    }
}
