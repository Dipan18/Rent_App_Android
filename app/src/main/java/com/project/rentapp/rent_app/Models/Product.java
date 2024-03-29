package com.project.rentapp.rent_app.Models;

public class Product {
    private int pro_id;
    private int user_id;
    private String pro_title;
    private String pro_desc;
    private double pro_price;
    private int pro_cat;
    private String cat_name;
    private int rent_period;
    private int pro_pincode;
    private String pro_address;
    private String created_at;
    private String[] img_path;
    private String first_name;
    private String last_name;
    private String email;
    private String phone_no;
    private int days_remaining;
    private String rented_on;
    private String requested_on;
    private String expiry_date;
    private User user;
    private String status;

    public int getProId() {
        return pro_id;
    }

    public String[] getImages() {
        return img_path;
    }

    public int getUserId() {
        return user_id;
    }

    public String getCatName() {
        return cat_name;
    }

    public String getProTitle() {
        return pro_title;
    }

    public String getProDesc() {
        return pro_desc;
    }

    public double getProPrice() {
        return pro_price;
    }

    public int getProCat() {
        return pro_cat;
    }

    public int getRentPeriod() {
        return rent_period;
    }

    public int getProPincode() {
        return pro_pincode;
    }

    public String getProAddress() {
        return pro_address;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phone_no;
    }

    public User getUser() { return user; }

    public String getStatus() {
        return status;
    }

    public int getDaysRemaining() {
        return days_remaining;
    }

    public String getRentedOn() {
        return rented_on;
    }

    public String getRequestedOn() {
        return requested_on;
    }

    public String getExpiryDate() { return expiry_date; }
}
