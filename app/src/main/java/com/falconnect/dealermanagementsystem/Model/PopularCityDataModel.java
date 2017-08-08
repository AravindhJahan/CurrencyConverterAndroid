package com.falconnect.dealermanagementsystem.Model;

public class PopularCityDataModel {

    String city_name;
    int city_id;
    int city_image;

    public PopularCityDataModel(String city_name, int city_id, int city_image) {
        this.city_name = city_name;
        this.city_id = city_id;
        this.city_image = city_image;
    }

    public String getName() {
        return city_name;
    }

    public int getId() {
        return city_id;
    }

    public int getImage() {
        return city_image;
    }
}
