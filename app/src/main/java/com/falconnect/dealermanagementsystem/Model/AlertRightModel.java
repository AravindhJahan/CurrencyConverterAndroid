package com.falconnect.dealermanagementsystem.Model;


public class AlertRightModel {
    private String message;
    private String state;
    private String date;


    public AlertRightModel(String message, String state, String date) {
        super();
        this.message = message;
        this.state = state;
        this.date = date;

    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getState() {
        return state;
    }

}






