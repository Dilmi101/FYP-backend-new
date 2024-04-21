package com.fyp.ehb.enums;

public enum ReminderFrequency {

    DAILY("DAILY"),
    WEEKLY("WEEKLY"),
    MONTHLY("MONTHLY"),
    YEARLY("YEARLY");

    private String frequencyType;

    private ReminderFrequency(String frequencyType) {
        this.frequencyType = frequencyType;
    }

    public String getFrequencyType() {
        return frequencyType;
    }
}
