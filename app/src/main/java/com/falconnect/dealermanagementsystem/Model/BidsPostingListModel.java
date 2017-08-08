package com.falconnect.dealermanagementsystem.Model;



public class BidsPostingListModel {

    private String result;
    private String message;
    private String position;
    private String dealername;
    private String amount;
    private String date;


    public BidsPostingListModel(String result, String message, String position, String dealername, String amount, String date) {
        super();

        this.result = result;
        this.message = message;
        this.position = position;
        this.dealername = dealername;
        this.amount = amount;
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDealername() {
        return dealername;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public void setDealername(String dealername) {
        this.dealername = dealername;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }
}






