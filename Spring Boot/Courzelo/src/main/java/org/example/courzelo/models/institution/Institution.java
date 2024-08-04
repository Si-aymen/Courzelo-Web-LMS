package org.example.courzelo.models.institution;

import lombok.Data;
import org.example.courzelo.dto.requests.InstitutionRequest;
import org.example.courzelo.models.User;
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
    @DBRef
    private List<Course> courses = new ArrayList<>();

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        // only one instance of each user
        for (User user : admins) {
            if (!users.contains(user)) {
                users.add(user);
            }
        }
        for (User user : teachers) {
            if (!users.contains(user)) {
                users.add(user);
            }
        }
        for (User user : students) {
            if (!users.contains(user)) {
                users.add(user);
            }
        }
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
    }

    public Institution(String name, String slogan, String country, String address, String description, String website) {
        this.name = name;
        this.slogan = slogan;
        this.country = country;
        this.address = address;
        this.description = description;
        this.website = website;
    }

    public void updateInstitution(InstitutionRequest institutionRequest){
        this.name = institutionRequest.getName();
        this.slogan = institutionRequest.getSlogan();
        this.country = institutionRequest.getCountry();
        this.address = institutionRequest.getAddress();
        this.description = institutionRequest.getDescription();
        this.website = institutionRequest.getWebsite();
    }
}
