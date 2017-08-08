package com.falconnect.dealermanagementsystem.Model;

public class SellFooterDataModel {

    String sellfootername;
    int sellfooterimage;
    int sellfooterid_;

    public SellFooterDataModel(String sellfootername, int sellfooterimage, int sellfooterid_) {
        this.sellfootername = sellfootername;
        this.sellfooterimage = sellfooterimage;
        this.sellfooterid_ = sellfooterid_;
    }

    public String getSellname() {
        return sellfootername;
    }

    public int getSellimage() {
        return sellfooterimage;
    }

    public int getSellfooterid_() {
        return sellfooterid_;
    }

    public void setSellfooterid_(int sellfooterid_) {
        this.sellfooterid_ = sellfooterid_;
    }

    public void setSellimage(int sellfooterimage) {
        this.sellfooterimage = sellfooterimage;
    }

    public void setSellname(String sellfootername) {
        this.sellfootername = sellfootername;
    }
}
