package com.falconnect.dealermanagementsystem.Model;


public class ApplyFundingListModel {

    private String fundingid;
    private String dealername;
    private String dealermobileno;
    private String dealermailid;
    private String requested_amount;
    private String created_date;
    private String dealercity;
    private String branchname;
    private String dealer_listing_id;
    private String status;
    private String dealer_funding_ticket_id;


    public ApplyFundingListModel(String fundingid,
                                 String dealername,
                                 String dealermobileno,
                                 String dealermailid,
                                 String requested_amount,
                                 String created_date,
                                 String dealercity,
                                 String branchname,
                                 String dealer_listing_id,
                                 String status,
                                 String dealer_funding_ticket_id) {
        super();

        this.fundingid = fundingid;
        this.dealername = dealername;
        this.dealermobileno = dealermobileno;
        this.dealermailid = dealermailid;
        this.requested_amount = requested_amount;
        this.created_date = created_date;
        this.dealercity = dealercity;
        this.branchname = branchname;
        this.dealer_listing_id = dealer_listing_id;
        this.status = status;
        this.dealer_funding_ticket_id = dealer_funding_ticket_id;

    }

    public String getBranchname() {
        return branchname;
    }

    public String getCreated_date() {
        return created_date;
    }

    public String getDealer_funding_ticket_id() {
        return dealer_funding_ticket_id;
    }

    public String getDealer_listing_id() {
        return dealer_listing_id;
    }

    public String getDealercity() {
        return dealercity;
    }

    public String getDealermailid() {
        return dealermailid;
    }

    public String getDealermobileno() {
        return dealermobileno;
    }

    public String getDealername() {
        return dealername;
    }

    public String getFundingid() {
        return fundingid;
    }

    public String getRequested_amount() {
        return requested_amount;
    }

    public String getStatus() {
        return status;
    }
}






