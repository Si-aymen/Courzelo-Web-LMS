package org.example.courzelo.dto.responses;

import lombok.Data;
import org.example.courzelo.models.User;

import java.util.Date;
import java.util.List;

@Data
public class SimplifiedUserResponse {
    private String email;
    private String name;
    private String lastname;
    private Date birthDate;
    private String gender;
    private String country;
    private List<String> roles;

    public SimplifiedUserResponse(User user){
        this.email = user.getEmail();
        this.name = user.getProfile().getName();
        this.lastname = user.getProfile().getLastname();
        this.birthDate = user.getProfile().getBirthDate();
        this.country = user.getProfile().getCountry();
        this.gender = user.getProfile().getGender();
        this.roles = user.getRoles().stream().map(Enum::name).toList();
    }
}
