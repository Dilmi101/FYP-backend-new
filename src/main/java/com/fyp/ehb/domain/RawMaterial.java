package com.fyp.ehb.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "raw_materials")
public class RawMaterial {

    @Id
    private String id;
    private String name;
    private String unit;
    private String remainingStock;
    private String lowStockLvl;
    private String reminder;
    private String availability;
    private String supplierName;
    private String supplierEmail;
    private String status;
    private String action;

    @DBRef
    private Customer customer;
}
