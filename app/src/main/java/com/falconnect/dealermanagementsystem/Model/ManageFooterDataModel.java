package com.falconnect.dealermanagementsystem.Model;

public class ManageFooterDataModel {

    String managefootername;
    int managefooterimage;
    int managefooterid_;

    public ManageFooterDataModel(String managefootername, int managefooterimage, int managefooterid_) {
        this.managefootername = managefootername;
        this.managefooterimage = managefooterimage;
        this.managefooterid_ = managefooterid_;
    }

    public String getManagefootername() {
        return managefootername;
    }

    public int getManagefooterimage() {
        return managefooterimage;
    }

    public int getManagefooterid_() {
        return managefooterid_;
    }

    public void setManagefooterid_(int managefooterid_) {
        this.managefooterid_ = managefooterid_;
    }

    public void setManagefooterimage(int managefooterimage) {
        this.managefooterimage = managefooterimage;
    }

    public void setManagefootername(String managefootername) {
        this.managefootername = managefootername;
    }
}
