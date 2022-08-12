package com.example.computersolutions.Model;

public class UpdateUserModel {
    private String name;
    private String email;
    private String card;
    private String address;
    private String postal;

    public UpdateUserModel(String name, String email, String card, String address, String postal) {
        this.name = name;
        this.email = email;
        this.card = card;
        this.address = address;
        this.postal = postal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }
}