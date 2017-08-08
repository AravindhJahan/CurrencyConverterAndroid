package com.falconnect.dealermanagementsystem.Model;


public class MyPostingDetailsListModel {

    private String fuel_type;
    private String kms;
    private String milage;
    private String seat;
    private String color;
    private String location;

    public MyPostingDetailsListModel(String fuel_type, String kms, String seat, String milage, String color,
                                     String location) {
        super();

        this.fuel_type = fuel_type;
        this.kms = kms;
        this.seat = seat;
        this.milage = milage;
        this.color = color;
        this.location = location;

    }


    public String getMilage() {
        return milage;
    }

    public String getLocation() {
        return location;
    }

    public String getKms() {
        return kms;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public String getColor() {
        return color;
    }

    public String getSeat() {
        return seat;
    }

    public void setMilage(String milage) {
        this.milage = milage;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public void setKms(String kms) {
        this.kms = kms;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }
}






