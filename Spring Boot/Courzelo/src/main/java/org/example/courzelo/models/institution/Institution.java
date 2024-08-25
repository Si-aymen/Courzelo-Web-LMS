package org.example.courzelo.models.institution;

import lombok.Data;
import org.example.courzelo.dto.requests.InstitutionRequest;
import org.example.courzelo.models.User;
import org.springframework.data.annotation.Id;
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
    private List<String> admins = new ArrayList<>();
    private List<String> teachers = new ArrayList<>();
    private List<String> students = new ArrayList<>();
    private byte[] excelFile;
    private double latitude;
    private double longitude;
    private List<String> groupsID = new ArrayList<>();
    private List<String> coursesID = new ArrayList<>();

    public List<String> getUsers() {
        List<String> users = new ArrayList<>();
        // only one instance of each user
        for (String user : admins) {
            if (!users.contains(user)) {
                users.add(user);
            }
        }
        for (String user: teachers) {
            if (!users.contains(user)) {
                users.add(user);
            }
        }
        for (String user : students) {
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
