package com.falconnect.dealermanagementsystem.Model;


import java.util.ArrayList;

public class FundingCarSelectModel {

    public boolean box;
    private String image;
    private String car_id;
    private String listing_id;
    private String inventory_type;
    private String dealer_id;
    private String price;
    private String kms_done;
    private String registration_year;
    private String owner_type;
    private String make;
    private String model;
    private String varient;
    private String fuel_type;
    private String carstatus;
    private ArrayList<String> Select;

    public FundingCarSelectModel(String image, String car_id, String listing_id, String inventory_type, String dealer_id,
                                 String price, String kms_done, String registration_year, String owner_type, String make,
                                 String model, String varient, String fuel_type, String carstatus, ArrayList<String> select,
                                 boolean checkbox) {
        super();

        this.image = image;
        this.car_id = car_id;
        this.listing_id = listing_id;
        this.inventory_type = inventory_type;
        this.dealer_id = dealer_id;
        this.price = price;
        this.kms_done = kms_done;
        this.registration_year = registration_year;
        this.owner_type = owner_type;
        this.make = make;
        this.model = model;
        this.varient = varient;
        this.fuel_type = fuel_type;
        this.carstatus = carstatus;
        this.Select = select;

        this.box = checkbox;

    }

    public String getImage() {
        return image;
    }

    public String getCar_id() {
        return car_id;
    }

    public String getListing_id() {
        return listing_id;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public String getDealer_id() {
        return dealer_id;
    }

    public String getInventory_type() {
        return inventory_type;
    }

    public String getKms_done() {
        return kms_done;
    }

    public String getCarstatus() {
        return carstatus;
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

    public String getVarient() {
        return varient;
    }

}







