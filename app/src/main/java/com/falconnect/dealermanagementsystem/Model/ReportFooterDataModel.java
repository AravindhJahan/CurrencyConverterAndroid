package com.falconnect.dealermanagementsystem.Model;

public class ReportFooterDataModel {

    String reportfootername;
    int reportfooterimage;
    int reportfooterid_;

    public ReportFooterDataModel(String reportfootername, int reportfooterimage, int reportfooterid_) {
        this.reportfootername = reportfootername;
        this.reportfooterimage = reportfooterimage;
        this.reportfooterid_ = reportfooterid_;
    }

    public String getReportname() {
        return reportfootername;
    }

    public void setReportname(String sellfootername) {
        this.reportfootername = reportfootername;
    }

    public int getReportimage() {
        return reportfooterimage;
    }

    public void setReportimage(int sellfooterimage) {
        this.reportfooterimage = reportfooterimage;
    }

    public int getReportfooterid_() {
        return reportfooterid_;
    }

    public void setReportfooterid_(int sellfooterid_) {
        this.reportfooterid_ = reportfooterid_;
    }
}