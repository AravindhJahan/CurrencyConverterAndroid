package com.falconnect.dealermanagementsystem.Model;


public class StaticReportModel {

    private String car_name;
    private String car_image;
    private String car_count;


    public StaticReportModel(String car_name, String car_image, String car_count) {
        super();

        this.car_name = car_name;
        this.car_image = car_image;
        this.car_count = car_count;


    }

    public String getCar_count() {
        return car_count;
    }

    public String getCar_image() {
        return car_image;
    }

    public String getCar_name() {
        return car_name;
    }
}







