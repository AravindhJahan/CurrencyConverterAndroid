package com.falconnect.dealermanagementsystem.Model;


public class MyEmployeeModel {
    private String employee_id;
    private String employee_type;
    private String employee_name;
    private String employee_gender;
    private String employee_mobile;
    private String employee_landline;
    private String employee_email;
    private String employee_address;
    private String employee_location;
    private String employee_pincode;
    private String contactimage;
    private String status;
    private String employee_designation;
    //private String employee_document;

    public MyEmployeeModel(String employee_id, String employee_type, String employee_name,
                           String employee_gender, String employee_mobile, String employee_landline,
                           String employee_email, String employee_address, String employee_location,
                           String employee_pincode, String contactimage, String status,
                           String employee_designation) {
        super();
        this.employee_id = employee_id;
        this.employee_type = employee_type;
        this.employee_name = employee_name;
        this.employee_gender = employee_gender;
        this.employee_mobile = employee_mobile;
        this.employee_landline = employee_landline;
        this.employee_email = employee_email;
        this.employee_address = employee_address;
        this.employee_location = employee_location;
        this.employee_pincode = employee_pincode;
        this.contactimage = contactimage;
        this.employee_pincode = employee_pincode;
        this.status = status;
        this.employee_designation = employee_designation;
        //this.employee_document = employee_document;

    }

    public String getContactimage() {
        return contactimage;
    }

    public String getEmployee_address() {
        return employee_address;
    }

    public String getEmployee_email() {
        return employee_email;
    }

    public String getEmployee_gender() {
        return employee_gender;
    }

    public String getEmployee_landline() {
        return employee_landline;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public String getEmployee_location() {
        return employee_location;
    }

    public String getEmployee_mobile() {
        return employee_mobile;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public String getEmployee_pincode() {
        return employee_pincode;
    }

    public String getEmployee_type() {
        return employee_type;
    }

    public String getStatus() {
        return status;
    }

    public String getEmployee_designation() {
        return employee_designation;
    }
}







