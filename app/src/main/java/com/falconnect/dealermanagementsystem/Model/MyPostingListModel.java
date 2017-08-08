package com.falconnect.dealermanagementsystem.Model;


public class MyPostingListModel {

    private String imageurl;
    private String car_id;
    private String year;
    private String price;
    private String kms;
    private String owner;
    private String fuel_type;
    private String make;
    private String model;
    private String variant;
    private String colors;
    private String mongopushdate;

    public MyPostingListModel(String imageurl, String car_id, String year,
                              String price, String kms,
                              String owner, String fuel_type,
                              String make, String model,
                              String variant, String colors,
                              String mongopushdate) {
        super();

        this.imageurl = imageurl;
        this.car_id = car_id;
        this.year = year;
        this.price = price;
        this.kms = kms;
        this.owner = owner;
        this.fuel_type = fuel_type;
        this.make = make;
        this.model = model;
        this.variant = variant;
        this.colors = colors;
        this.mongopushdate = mongopushdate;

    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getKms() {
        return kms;
    }

    public void setKms(String kms) {
        this.kms = kms;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public String getMongopushdate() {
        return mongopushdate;
    }

    public void setMongopushdate(String mongopushdate) {
        this.mongopushdate = mongopushdate;
    }

}






