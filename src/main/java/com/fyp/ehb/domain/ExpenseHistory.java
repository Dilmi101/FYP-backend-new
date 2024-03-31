package com.fyp.ehb.domain;


import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "expense_records")
public class ExpenseHistory {

    @Id
    private String id;

    @Field("achieved_amount")
    private String achievedAmount;

    @Field("created_date")
    private Date createdDate;
    
    @DBRef
    private Expense expense;
}
