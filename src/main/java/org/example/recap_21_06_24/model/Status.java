package org.example.recap_21_06_24.model;

public enum Status {
    OPEN("OPEN"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE");

    private final String statusName;

    Status(String statusName){
        this.statusName = statusName;
    }

    public String getStatusName(){
        return this.statusName;
    }
}
