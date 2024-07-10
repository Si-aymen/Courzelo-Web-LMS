package org.example.courzelo.dto.responses;

import lombok.Data;
import org.example.courzelo.models.User;

import java.util.Date;

@Data
public class SimplifiedUserResponse {
    private String email;
    private String name;
    private String lastname;
    private Date birthDate;
    private String gender;
    private String country;

    public SimplifiedUserResponse(User user){
        this.email = user.getEmail();
        this.name = user.getProfile().getName();
        this.lastname = user.getProfile().getLastname();
        this.birthDate = user.getProfile().getBirthDate();
        this.country = user.getProfile().getCountry();
        this.gender = user.getProfile().getGender();
    }
}
