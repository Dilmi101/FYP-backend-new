package com.fyp.ehb.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@Document(collection = "businesses")
public class Business {

    @Id
    private String businessID;

    @Field("name")
    private String businessName;

    @Field("address")
    private String businessAddress;

    @Field("product_type")
    private String typeOfProduct;

    @Field("registration_no")
    private String businessRegistrationNo;

    @DBRef
    private Customer customer;

}
