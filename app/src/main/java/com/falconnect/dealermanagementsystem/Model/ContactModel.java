package com.falconnect.dealermanagementsystem.Model;


public class ContactModel {
    private String name;
    private String mobilenum;
    private String email;
    private String contactimage;
    private String address;
    private String contact_id;
    private String contact_owner;
    private String contact_type_id;
    private String contact_type_name;
    private String pan_number;
    private String contact_gender;
    private String contact_email;
    private String contact_sms;
    private String lead_city;
    private String lead_make;
    private String lead_model;
    private String lead_prcie;
    private String lead_time;

    private String lead_makeid;
    private String lead_cityid;
    private String lead_modelid;
    private String dealer_document_management_id;
    private String contact_management_id;
    private String document_id_type;
    private String document_id_number;
    private String document_dob;
    private String doc_link_fullpath;
    private String document_name;

    private String lead_timename;
    private String lead_pricename;



    public ContactModel(String name, String mobilenum, String email,
                        String contactimage, String address, String contact_id,
                        String contact_owner, String contact_type_id, String contact_type_name, String pan_number,
                        String contact_gender, String contact_email, String contact_sms, String lead_city, String lead_make,
                        String lead_model, String lead_prcie , String  lead_time,
                        String lead_makeid, String lead_cityid, String lead_modelid,
                        String dealer_document_management_id, String contact_management_id, String document_id_type,
                        String document_id_number, String document_dob, String doc_link_fullpath, String document_name,
                        String lead_timename, String lead_pricename) {
        super();
        this.name = name;
        this.mobilenum = mobilenum;
        this.email = email;
        this.contactimage = contactimage;
        this.address = address;
        this.contact_id = contact_id;
        this.contact_owner = contact_owner;
        this.contact_type_id = contact_type_id;
        this.contact_type_name = contact_type_name;
        this.pan_number = pan_number;

        this.contact_gender = contact_gender;
        this.contact_email = contact_email;
        this.contact_sms = contact_sms;
        this.lead_city = lead_city;
        this.lead_make = lead_make;
        this.lead_model = lead_model;
        this.lead_prcie = lead_prcie;
        this.lead_time = lead_time;

        this.lead_makeid = lead_makeid;
        this.lead_cityid = lead_cityid;
        this.lead_modelid = lead_modelid;
        this.dealer_document_management_id = dealer_document_management_id;
        this.contact_management_id = contact_management_id;
        this.document_id_type = document_id_type;
        this.document_id_number = document_id_number;
        this.document_dob = document_dob;
        this.document_name = document_name;

        this.doc_link_fullpath = doc_link_fullpath;

        this.lead_timename = lead_timename;
        this.lead_pricename = lead_pricename;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobilenum() {
        return mobilenum;
    }

    public void setMobilenum(String mobilenum) {
        this.mobilenum = mobilenum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactimage() {
        return contactimage;
    }

    public void setContactimage(String contactimage) {
        this.contactimage = contactimage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getContact_owner() {
        return contact_owner;
    }

    public String getContact_type_id() {
        return contact_type_id;
    }

    public String getContact_type_name() {
        return contact_type_name;
    }

    public String getPan_number() {
        return pan_number;
    }

    public String getContact_email() {
        return contact_email;
    }

    public String getContact_gender() {
        return contact_gender;
    }

    public String getContact_sms() {
        return contact_sms;
    }

    public String getLead_city() {
        return lead_city;
    }

    public String getLead_make() {
        return lead_make;
    }

    public String getLead_model() {
        return lead_model;
    }

    public String getLead_prcie() {
        return lead_prcie;
    }

    public String getLead_time() {
        return lead_time;
    }

    public String getContact_management_id() {
        return contact_management_id;
    }

    public String getDealer_document_management_id() {
        return dealer_document_management_id;
    }

    public String getDoc_link_fullpath() {
        return doc_link_fullpath;
    }

    public String getDocument_dob() {
        return document_dob;
    }

    public String getDocument_id_number() {
        return document_id_number;
    }

    public String getDocument_id_type() {
        return document_id_type;
    }

    public String getDocument_name() {
        return document_name;
    }

    public String getLead_cityid() {
        return lead_cityid;
    }

    public String getLead_makeid() {
        return lead_makeid;
    }

    public String getLead_modelid() {
        return lead_modelid;
    }

    public String getLead_pricename() {
        return lead_pricename;
    }

    public String getLead_timename() {
        return lead_timename;
    }

}







