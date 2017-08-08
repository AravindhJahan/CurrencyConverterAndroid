package com.falconnect.dealermanagementsystem.Model;

public class FundingDataModel {

    String name;
    int id_;
    int image;

    public FundingDataModel(String name, int id_, int image) {
        this.name = name;
        this.id_ = id_;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public int getId() {
        return id_;
    }
}
