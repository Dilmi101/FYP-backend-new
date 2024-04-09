package com.fyp.ehb.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "customers")
public class Customer {

    @Id
    private String id;

    private String name;

    private String username;

    private String mobile;

    private String email;

    private String nic;

    private String password;

    private String language;

    private String pushToken;
    
    @Field("user_login_count")
    private int userLoginCount;

    @Field("user_password_reset_count")
    private int userPasswordResetCount;

    @DBRef
    private Business business;

    @DBRef
    private List<Goal> goals;
    
}
