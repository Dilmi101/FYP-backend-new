package com.fyp.ehb.enums;

public enum Status {

    PENDING_STATUS("P","PENDING"),
    ACTIVE_STATUS("A","ACTIVE"),
    INACTIVE_STATUS("I","INACTIVE"),
    YES_STATUS("Y","YES"),
    NO_STATUS("N","NO"),
    APPROVED_STATUS("A","APPROVED"),
    DECLINED_STATUS("D","DECLINED");

    private String status;
    private String description;

    Status(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }
    public String getDescription() {
        return description;
    }
}
