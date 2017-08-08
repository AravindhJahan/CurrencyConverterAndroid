package com.falconnect.dealermanagementsystem.Model;


public class MyPostingDetailsListViewModel {

    private String imageurl;
    private String year;
    private String duplicate_id;
    private String price;
    private String kms;
    private String owner;
    private String fuel_type;
    private String plan;
    private String mileage;
    private String place;
    private String make;
    private String model;
    private String varient;
    private String colors;
    private String seat;
    private String imagecount;
    private String videoscount;
    private String documentcount;
    private String viewscount;
    private String mongopushdate;
    private String listing_id;
    private String car_id;
    private String listing_site;
    private String listing_status;
    private String list_image;
    private String createddate;

    public MyPostingDetailsListViewModel(String imageurl,
                                         String year,
                                         String duplicate_id,
                                         String price,
                                         String kms,
                                         String owner,
                                         String fuel_type,
                                         String plan,
                                         String mileage,
                                         String place,
                                         String make,
                                         String model,
                                         String varient,
                                         String colors,
                                         String seat,
                                         String imagecount,
                                         String videoscount,
                                         String documentcount,
                                         String viewscount,
                                         String mongopushdate,
                                         String listing_id,
                                         String car_id,
                                         String listing_site,
                                         String listing_status,
                                         String list_image,
                                         String createddate) {
        super();


        this.imageurl = imageurl;
        this.year = year;
        this.duplicate_id = duplicate_id;
        this.price = price;
        this.kms = kms;
        this.owner = owner;
        this.fuel_type = fuel_type;
        this.plan = plan;
        this.mileage = mileage;
        this.place = place;
        this.make = make;
        this.model = model;
        this.varient = varient;
        this.colors = colors;
        this.seat = seat;
        this.imagecount = imagecount;
        this.videoscount = videoscount;
        this.documentcount = documentcount;
        this.viewscount = viewscount;
        this.mongopushdate = mongopushdate;
        this.listing_id = listing_id;
        this.car_id = car_id;
        this.listing_site = listing_site;
        this.listing_status = listing_status;
        this.list_image = list_image;
        this.createddate = createddate;

    }


    public String getImageurl() {
        return imageurl;
    }

    public String getYear() {
        return year;
    }

    public String getDocumentcount() {
        return documentcount;
    }

    public String getMileage() {
        return mileage;
    }

    public String getSeat() {
        return seat;
    }

    public String getKms() {
        return kms;
    }

    public String getDuplicate_id() {
        return duplicate_id;
    }

    public String getCar_id() {
        return car_id;
    }

    public String getImagecount() {
        return imagecount;
    }

    public String getMake() {
        return make;
    }

    public String getCreateddate() {
        return createddate;
    }

    public String getListing_id() {
        return listing_id;
    }

    public String getListing_site() {
        return listing_site;
    }

    public String getListing_status() {
        return listing_status;
    }

    public String getModel() {
        return model;
    }

    public String getOwner() {
        return owner;
    }

    public String getMongopushdate() {
        return mongopushdate;
    }

    public String getPlan() {
        return plan;
    }

    public String getPrice() {
        return price;
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

    public String getList_image() {
        return list_image;
    }

    public String getPlace() {
        return place;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public String getColors() {
        return colors;
    }
}






