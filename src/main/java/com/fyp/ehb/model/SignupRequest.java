package com.fyp.ehb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignupRequest {

    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9]{6,12}$",
            message = "invalid username. username must be of 6 to 12 length with no special characters")
    private String username;

    @Pattern(regexp = "^07\\d{8}$", message = "invalid mobile number")
    private String mobileNo;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "invalid email address")
    private String email;

    @Pattern(regexp = "^\\d{9}[VvXx]|\\d{12}$", message = "invalid nic")
    private String nic;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\\\S+$).{8,}$", message = "invalid password")
    private String password;

    private String language;
    private String businessName;
    private String businessAddress;
    private String typeOfProduct;
    private String businessRegistrationNo;

}
