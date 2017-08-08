package com.falconnect.dealermanagementsystem.Model;


public class QueriesReceivedListModel {

    private String noimages;
    private String imagelink;
    private String listing_type;
    private String price;
    private String car_id;
    private String status;
    private String from_dealer_id;
    private String to_dealer_name;
    private String to_dealer_id;
    private String make;
    private String title;
    private String dealer_name;
    private String dealer_email;
    private String message;
    private String contact_transactioncode;
    private String days;


    public QueriesReceivedListModel(String noimages, String imagelink, String listing_type,
                                    String price, String car_id, String status, String from_dealer_id, String to_dealer_name,
                                    String to_dealer_id, String make, String title, String dealer_name, String dealer_email,
                                    String message, String contact_transactioncode, String days) {
        super();

        this.noimages = noimages;
        this.imagelink = imagelink;
        this.listing_type = listing_type;
        this.price = price;
        this.car_id = car_id;
        this.status = status;
        this.from_dealer_id = from_dealer_id;
        this.to_dealer_name = to_dealer_name;
        this.to_dealer_id = to_dealer_id;
        this.make = make;
        this.title = title;
        this.dealer_name = dealer_name;
        this.dealer_email = dealer_email;
        this.message = message;
        this.contact_transactioncode = contact_transactioncode;
        this.days = days;

    }

    public String getDealer_name() {
        return dealer_name;
    }

    public String getCar_id() {
        return car_id;
    }

    public String getDealer_email() {
        return dealer_email;
    }

    public String getContact_transactioncode() {
        return contact_transactioncode;
    }

    public String getDays() {
        return days;
    }

    public String getFrom_dealer_id() {
        return from_dealer_id;
    }

    public String getImagelink() {
        return imagelink;
    }

    public String getListing_type() {
        return listing_type;
    }

    public String getMake() {
        return make;
    }

    public String getNoimages() {
        return noimages;
    }

    public String getPrice() {
        return price;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getTo_dealer_id() {
        return to_dealer_id;
    }

    public String getTo_dealer_name() {
        return to_dealer_name;
    }


}