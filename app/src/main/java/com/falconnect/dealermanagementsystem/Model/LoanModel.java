package com.falconnect.dealermanagementsystem.Model;


public class LoanModel {
    private String dealer_customer_loan_id;
    private String customername;
    private String user_image;
    private String customermobileno;
    private String customerpannumber;
    private String dealer_loan_ticket_id;
    private String customermailid;
    private String customercity;
    private String branchname;
    private String vehicle_details;
    private String status;
    private String created_date;
    private String requested_amount;


    public LoanModel(String dealer_customer_loan_id, String customername,
                     String user_image, String customermobileno, String customerpannumber,
                     String dealer_loan_ticket_id, String customermailid, String customercity,
                     String branchname, String vehicle_details, String status, String created_date,
                     String requested_amount) {
        super();
        this.dealer_customer_loan_id = dealer_customer_loan_id;
        this.customername = customername;
        this.user_image = user_image;
        this.customermobileno = customermobileno;
        this.customerpannumber = customerpannumber;
        this.dealer_loan_ticket_id = dealer_loan_ticket_id;
        this.customermailid = customermailid;
        this.customercity = customercity;
        this.branchname = branchname;
        this.vehicle_details = vehicle_details;
        this.status = status;
        this.created_date = created_date;
        this.requested_amount = requested_amount;

    }


    public String getStatus() {
        return status;
    }

    public String getRequested_amount() {
        return requested_amount;
    }

    public String getBranchname() {
        return branchname;
    }

    public String getCreated_date() {
        return created_date;
    }

    public String getCustomercity() {
        return customercity;
    }

    public String getCustomermailid() {
        return customermailid;
    }

    public String getCustomermobileno() {
        return customermobileno;
    }

    public String getCustomername() {
        return customername;
    }

    public String getCustomerpannumber() {
        return customerpannumber;
    }

    public String getDealer_customer_loan_id() {
        return dealer_customer_loan_id;
    }

    public String getDealer_loan_ticket_id() {
        return dealer_loan_ticket_id;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getVehicle_details() {
        return vehicle_details;
    }

}






