package com.fyp.ehb.enums;

public enum ExpenseReminderFrequency {

    DAILY("D"),
    WEEKLY("W"),
    MONTHLY("M"),
    YEARLY("Y");

    private String frequencyType;

    private ExpenseReminderFrequency(String frequencyType) {
        this.frequencyType = frequencyType;
    }

    public String getFrequencyType() {
        return frequencyType;
    }
}
