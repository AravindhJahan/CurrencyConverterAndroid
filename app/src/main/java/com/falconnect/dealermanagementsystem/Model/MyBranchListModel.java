package com.falconnect.dealermanagementsystem.Model;


public class MyBranchListModel {

    private String branch_id;
    private String dealer_name;
    private String dealer_contact_no;
    private String branch_address;
    private String dealer_mail;
    private String dealer_state;
    private String dealer_city;
    private String dealer_pincode;
    private String dealer_status;
    private String dealer_service;
    private String head_quater;


    public MyBranchListModel(String branch_id, String dealer_name,
                             String dealer_contact_no, String branch_address,
                             String dealer_mail, String dealer_state,
                             String dealer_city, String dealer_pincode,
                             String dealer_status, String dealer_service, String head_quater) {
        super();

        this.branch_id = branch_id;
        this.dealer_name = dealer_name;
        this.dealer_contact_no = dealer_contact_no;
        this.branch_address = branch_address;
        this.dealer_mail = dealer_mail;
        this.dealer_state = dealer_state;
        this.dealer_city = dealer_city;
        this.dealer_pincode = dealer_pincode;
        this.dealer_status = dealer_status;
        this.dealer_service = dealer_service;
        this.head_quater = head_quater;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public String getDealer_contact_no() {
        return dealer_contact_no;
    }

    public String getBranch_address() {
        return branch_address;
    }

    public String getDealer_state() {
        return dealer_state;
    }

    public String getDealer_city() {
        return dealer_city;
    }

    public String getDealer_mail() {
        return dealer_mail;
    }

    public String getDealer_pincode() {
        return dealer_pincode;
    }

    public String getDealer_status() {
        return dealer_status;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public void setBranch_address(String branch_address) {
        this.branch_address = branch_address;
    }

    public void setDealer_city(String dealer_city) {
        this.dealer_city = dealer_city;
    }

    public void setDealer_contact_no(String dealer_contact_no) {
        this.dealer_contact_no = dealer_contact_no;
    }

    public void setDealer_mail(String dealer_mail) {
        this.dealer_mail = dealer_mail;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public void setDealer_pincode(String dealer_pincode) {
        this.dealer_pincode = dealer_pincode;
    }

    public void setDealer_state(String dealer_state) {
        this.dealer_state = dealer_state;
    }

    public void setDealer_status(String dealer_status) {
        this.dealer_status = dealer_status;
    }

    public String getDealer_service() {
        return dealer_service;
    }

    public String getHead_quater() {
        return head_quater;
    }

    public void setDealer_service(String dealer_service) {
        this.dealer_service = dealer_service;
    }

    public void setHead_quater(String head_quater) {
        this.head_quater = head_quater;
    }
}







