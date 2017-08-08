package com.falconnect.dealermanagementsystem.Model;


public class MyInventoryModel {

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
    private String colors;
    private String fuel_type;
    private String statuc_number;
    private String imagecount;
    private String videoscount;
    private String documentcount;
    private String viewscount;
    private String carstatus;
    private  String millege;


    public MyInventoryModel(String image, String car_id, String listing_id, String inventory_type, String dealer_id,
                            String price, String kms_done, String registration_year, String owner_type, String make,
                            String model, String varient, String colors, String fuel_type, String statuc_number, String imagecount,
                            String videoscount, String documentcount, String viewscount, String carstatus, String millege) {
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
        this.colors = colors;
        this.fuel_type = fuel_type;
        this.statuc_number = statuc_number;
        this.imagecount = imagecount;
        this.videoscount = videoscount;
        this.documentcount = documentcount;
        this.viewscount = viewscount;
        this.carstatus = carstatus;
        this.millege=millege;


    }

    public String getFuel_type() {
        return fuel_type;
    }

    public String getCar_id() {
        return car_id;
    }

    public String getColors() {
        return colors;
    }

    public String getDealer_id() {
        return dealer_id;
    }

    public String getImage() {
        return image;
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

    public String getCarstatus() {
        return carstatus;
    }

    public String getDocumentcount() {
        return documentcount;
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

    public String getStatuc_number() {
        return statuc_number;
    }

    public String getVarient() {
        return varient;
    }

    public String getVideoscount() {
        return videoscount;
    }

    public String getViewscount() {
        return viewscount;
    }

    public String getMillege() {
        return millege;
    }

}







