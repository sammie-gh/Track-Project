package com.gh.sammie.trackproject.model;

public class Goods {


    private String name, image, id, description, shortdesc, price, url, menuId, status, days, ut_penalty, tt_cost, terminal, date, location, destination;


    public Goods() {
    }

    public Goods(String name, String image, String id, String description, String shortdesc, String price, String url, String menuId, String status, String days, String ut_penalty, String tt_cost, String terminal, String date, String location, String destination) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.description = description;
        this.shortdesc = shortdesc;
        this.price = price;
        this.url = url;
        this.menuId = menuId;
        this.status = status;
        this.days = days;
        this.ut_penalty = ut_penalty;
        this.tt_cost = tt_cost;
        this.terminal = terminal;
        this.date = date;
        this.location = location;
        this.destination = destination;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getUt_penalty() {
        return ut_penalty;
    }

    public void setUt_penalty(String ut_penalty) {
        this.ut_penalty = ut_penalty;
    }

    public String getTt_cost() {
        return tt_cost;
    }

    public void setTt_cost(String tt_cost) {
        this.tt_cost = tt_cost;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
