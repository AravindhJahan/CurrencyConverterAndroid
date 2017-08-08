package com.falconnect.dealermanagementsystem.Model;


public class BussinessProfileModel {

    private String image;
    private String car_id;
    private String listing_id;
    private String inventory_type;
    private String car_locality;
    private String dealer_id;
    private String price;
    private String kms_done;
    private String registration_year;
    private String owner_type;
    private String make;
    private String model;
    private String varient;
    private String fuel_type;
    private String imagecount;
    private String days;


    public BussinessProfileModel(String image, String car_id, String listing_id, String inventory_type, String car_locality, String dealer_id,
                                 String price, String kms_done, String registration_year, String owner_type, String make,
                                 String model, String varient, String fuel_type, String imagecount,
                                 String days) {
        super();

        this.image = image;
        this.car_id = car_id;
        this.listing_id = listing_id;
        this.inventory_type = inventory_type;
        this.car_locality = car_locality;
        this.dealer_id = dealer_id;
        this.price = price;
        this.kms_done = kms_done;
        this.registration_year = registration_year;
        this.owner_type = owner_type;
        this.make = make;
        this.model = model;
        this.varient = varient;
        this.fuel_type = fuel_type;
        this.imagecount = imagecount;
        this.days = days;
    }

    public String getVarient() {
        return varient;
    }

    public String getCar_id() {
        return car_id;
    }

    public String getDealer_id() {
        return dealer_id;
    }

    public String getImage() {
        return image;
    }

    public String getDays() {
        return days;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public String getInventory_type() {
        return inventory_type;
    }

    public String getImagecount() {
        return imagecount;
    }

    public String getKms_done() {
        return kms_done;
    }

    public String getListing_id() {
        return listing_id;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getOwner_type() {
        return owner_type;
    }

    public String getPrice() {
        return price;
    }

    public String getRegistration_year() {
        return registration_year;
    }

    public String getCar_locality() {
        return car_locality;
    }
}






