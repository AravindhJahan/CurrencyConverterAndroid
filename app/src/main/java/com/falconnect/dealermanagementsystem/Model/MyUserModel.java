package com.falconnect.dealermanagementsystem.Model;


public class MyUserModel {
    private String branch_id;
    private String user_email;
    private String user_role;
    private String user_id;
    private String user_name;
    private String role_name;
    private String user_moblie_no;
    private String status;




    public MyUserModel(String branch_id, String user_email, String user_role,
                        String user_id, String user_name, String role_name, String user_moblie_no, String status) {
        super();
        this.branch_id = branch_id;
        this.user_email = user_email;
        this.user_role = user_role;
        this.user_id = user_id;
        this.user_name = user_name;
        this.role_name = role_name;
        this.user_moblie_no = user_moblie_no;
        this.status = status;


    }

    public String getBranch_id() {
        return branch_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_role() {
        return user_role;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_moblie_no() {
        return user_moblie_no;
    }

    public String getStatus() {
        return status;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_moblie_no(String user_moblie_no) {
        this.user_moblie_no = user_moblie_no;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }
}







