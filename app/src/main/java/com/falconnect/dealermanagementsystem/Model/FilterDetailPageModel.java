package com.falconnect.dealermanagementsystem.Model;


public class FilterDetailPageModel {
    private String titles;
    private String count;
    private String ids;


    public FilterDetailPageModel(String titles, String count, String ids) {
        super();
        this.titles = titles;
        this.count = count;
        this.ids = ids;
    }

    public String getCount() {
        return count;
    }

    public String getIds() {
        return ids;
    }

    public String getTitles() {
        return titles;
    }
}







