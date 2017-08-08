package com.falconnect.dealermanagementsystem.Model;

public class MyPostingDetailsDataModel {

    String mypostingname;
    int mypostingimage;
    int mypostingid_;

    public MyPostingDetailsDataModel(String mypostingname, int mypostingimage, int mypostingid_) {
        this.mypostingname = mypostingname;
        this.mypostingimage = mypostingimage;
        this.mypostingid_ = mypostingid_;
    }

    public int getMypostingid_() {
        return mypostingid_;
    }

    public int getMypostingimage() {
        return mypostingimage;
    }

    public String getMypostingname() {
        return mypostingname;
    }

    public void setMypostingid_(int mypostingid_) {
        this.mypostingid_ = mypostingid_;
    }

    public void setMypostingimage(int mypostingimage) {
        this.mypostingimage = mypostingimage;
    }

    public void setMypostingname(String mypostingname) {
        this.mypostingname = mypostingname;
    }
}

