package org.example.courzelo.dto.responses.institution;

import lombok.Data;
import org.example.courzelo.models.Institution;

@Data
public class InstitutionResponse {
    private String id;
    private String name;
    private String slogan;
    private String logo;
    private String country;
    private String address;
    private String description;
    private String website;
    private double latitude;
    private double longitude;
    private boolean hasCalendar;
    public InstitutionResponse(Institution institution)
    {
        this.id = institution.getId();
        this.name = institution.getName();
        this.slogan = institution.getSlogan();
        this.logo = institution.getLogo();
        this.country = institution.getCountry();
        this.address = institution.getAddress();
        this.description = institution.getDescription();
        this.website = institution.getWebsite();
        this.latitude = institution.getLatitude();
        this.longitude = institution.getLongitude();
        this.hasCalendar = institution.getExcelFile() != null;
    }
}
