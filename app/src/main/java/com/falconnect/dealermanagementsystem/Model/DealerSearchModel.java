package com.falconnect.dealermanagementsystem.Model;


public class DealerSearchModel {

    private String dealer_name;
    private String dealership_name;
    private String id;
    private String logo;
    private String d_mobile;
    private String d_email;
    private String city;
    private String dealercarno;

    public DealerSearchModel(String dealer_name, String dealership_name,
                             String id, String logo, String d_mobile, String d_email,
                             String city, String dealercarno) {
        super();

        this.dealer_name = dealer_name;
        this.dealership_name = dealership_name;
        this.id = id;
        this.logo = logo;
        this.d_mobile = d_mobile;
        this.d_email = d_email;
        this.city = city;
        this.dealercarno = dealercarno;
    }

    public String getCity() {
        return city;
    }

    public String getD_email() {
        return d_email;
    }

    public String getD_mobile() {
        return d_mobile;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public String getDealercarno() {
        return dealercarno;
    }

    public String getDealership_name() {
        return dealership_name;
    }

    public String getId() {
        return id;
    }

    public String getLogo() {
        return logo;
    }
}






