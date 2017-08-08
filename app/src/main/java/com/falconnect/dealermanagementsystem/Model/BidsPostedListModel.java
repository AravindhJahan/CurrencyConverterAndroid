package com.falconnect.dealermanagementsystem.Model;


public class BidsPostedListModel {

    private String car_image;
    private String car_rate;
    private String car_name;
    private String car_posted_ago;
    private String leftdate_time;
    private String site_image;
    private String bids_won;
    private String car_id;
    private String dealer_id;

    public BidsPostedListModel(String car_image, String car_rate, String car_name, String car_posted_ago, String leftdate_time, String site_image, String bids_won, String car_id, String dealer_id) {
        super();

        this.car_image = car_image;
        this.car_rate = car_rate;
        this.car_name = car_name;
        this.car_posted_ago = car_posted_ago;
        this.leftdate_time = leftdate_time;
        this.site_image = site_image;
        this.bids_won = bids_won;
        this.car_id = car_id;
        this.dealer_id = dealer_id;

    }

    public String getCar_image() {
        return car_image;
    }

    public void setCar_image(String car_image) {
        this.car_image = car_image;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getCar_rate() {
        return car_rate;
    }

    public void setCar_rate(String car_rate) {
        this.car_rate = car_rate;
    }

    public String getCar_posted_ago() {
        return car_posted_ago;
    }

    public void setCar_posted_ago(String car_posted_ago) {
        this.car_posted_ago = car_posted_ago;
    }

    public String getLeftdate_time() {
        return leftdate_time;
    }

    public void setLeftdate_time(String leftdate_time) {
        this.leftdate_time = leftdate_time;
    }

    public String getSite_image() {
        return site_image;
    }

    public void setSite_image(String site_image) {
        this.site_image = site_image;
    }

    public String getBids_won() {
        return bids_won;
    }

    public void setBids_won(String bids_won) {
        this.bids_won = bids_won;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getDealer_id() {
        return dealer_id;
    }

    public void setDealer_id(String dealer_id) {
        this.dealer_id = dealer_id;
    }

}






