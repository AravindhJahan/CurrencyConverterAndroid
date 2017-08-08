package com.falconnect.dealermanagementsystem.Model;


public class AlertPageModel {
    private String city;
    private String date;
    private String email_status;
    private String listingid;
    private String mobileno;
    private String product;
    private String sms_status;
    private String alert_status;
    private String type;
    private String email;
    private String alertid;
    private String dealername;


    public AlertPageModel(String city, String email_status, String date, String listingid, String mobileno, String product, String sms_status, String alert_status, String type, String email, String alertid, String dealername) {
        super();
        this.city = city;
        this.email_status = email_status;
        this.date = date;
        this.listingid = listingid;
        this.mobileno = mobileno;
        this.product = product;
        this.sms_status = sms_status;
        this.alert_status = alert_status;
        this.type = type;
        this.email = email;
        this.alertid = alertid;
        this.dealername = dealername;
    }

    public String getDate() {
        return date;
    }

    public String getAlert_status() {
        return alert_status;
    }

    public String getAlertid() {
        return alertid;
    }

    public String getCity() {
        return city;
    }

    public String getDealername() {
        return dealername;
    }

    public String getEmail() {
        return email;
    }

    public String getEmail_status() {
        return email_status;
    }

    public String getListingid() {
        return listingid;
    }

    public String getMobileno() {
        return mobileno;
    }

    public String getProduct() {
        return product;
    }

    public String getSms_status() {
        return sms_status;
    }

    public String getType() {
        return type;
    }

}







