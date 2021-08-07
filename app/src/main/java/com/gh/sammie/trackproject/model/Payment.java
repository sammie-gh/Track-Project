package com.gh.sammie.trackproject.model;

public class Payment {
    String status;
    String desc,price;
    String email;
    String id;

    public Payment() {
    }

    public Payment(String status, String desc, String price, String email, String id) {
        this.status = status;
        this.desc = desc;
        this.price = price;
        this.email = email;
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
