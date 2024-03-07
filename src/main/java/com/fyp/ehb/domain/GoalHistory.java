package com.fyp.ehb.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "goal_records")
public class GoalHistory {

    @Id
    private String id;

    @Field("achieved_amount")
    private String achievedAmount;

    @Field("created_date")
    private LocalDateTime createdDate;

    @DBRef
    private Goal goal;
}
