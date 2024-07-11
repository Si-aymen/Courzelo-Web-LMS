package org.example.courzelo.dto.requests;

import org.example.courzelo.models.Status;

import java.time.LocalDateTime;

public class TicketREQ {
    private String id;

    private String sujet;

    private String details;

    private LocalDateTime dateCreation = LocalDateTime.now();

    private Status status = Status.EN_ATTENTE;

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ReclamationDT{" +
                "id='" + id + '\'' +
                ", sujet='" + sujet + '\'' +
                ", details='" + details + '\'' +
                ", dateCreation=" + dateCreation +
                ", status=" + status +
                ", type='" + type + '\'' +
                '}';
    }
}
