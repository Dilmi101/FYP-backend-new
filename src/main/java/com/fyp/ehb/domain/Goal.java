package com.fyp.ehb.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "goals")
public class Goal {

    @Id
    private String id;

    @Field("goal_title")
    private String goalTitle;

    @Field("goal_description")
    private String goalDescription;

    @Field("start_date")
    private LocalDateTime startDate;

    @Field("end_date")
    private LocalDateTime endDate;

    private String unit;
    private String target;
    private String priority;
    private String reminder;

    @Field("status")
    private String goalStatus;

    @Field("is_recurring_goal")
    private String isRecurringGoal;

    @DBRef
    private Customer customer;

    @DBRef
    private List<GoalHistory> histories;
}
