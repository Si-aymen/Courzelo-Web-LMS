package org.example.courzelo.models;

import lombok.Data;
import org.example.courzelo.dto.requests.InstitutionRequest;
import org.example.courzelo.dto.responses.institution.InstitutionResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "institutions")
@Data
public class Institution {
    @Id
    private String id;
    private String name;
    private String slogan;
    private String logo;
    private String country;
    private String address;
    private String description;
    private String website;
    @DBRef
    private List<User> admins = new ArrayList<>();
    @DBRef
    private List<User> teachers = new ArrayList<>();
    @DBRef
    private List<User> students = new ArrayList<>();
    private byte[] excelFile;
    private double latitude;
    private double longitude;

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.addAll(admins);
        users.addAll(teachers);
        users.addAll(students);
        return users;
    }
    public Institution()
    {
    }
    public Institution(InstitutionRequest institutionRequest){
        this.name = institutionRequest.getName();
        this.slogan = institutionRequest.getSlogan();
        this.country = institutionRequest.getCountry();
        this.address = institutionRequest.getAddress();
        this.description = institutionRequest.getDescription();
        this.website = institutionRequest.getWebsite();
        this.latitude = institutionRequest.getLatitude();
        this.longitude = institutionRequest.getLongitude();
    }
    public void updateInstitution(InstitutionRequest institutionRequest){
        this.name = institutionRequest.getName();
        this.slogan = institutionRequest.getSlogan();
        this.country = institutionRequest.getCountry();
        this.address = institutionRequest.getAddress();
        this.description = institutionRequest.getDescription();
        this.website = institutionRequest.getWebsite();
        this.latitude = institutionRequest.getLatitude()!=null ? institutionRequest.getLatitude() : 0;
        this.longitude = institutionRequest.getLongitude()!=null ? institutionRequest.getLongitude() : 0;
    }
}
