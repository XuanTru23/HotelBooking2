package com.example.hotelbooking.Models;

public class Hotel {
    String id_hotel;
    String id_user;
    String name_hotel;
    String image_hotel;
    String address_hotel;
    String description_hotel;
    String gmail_hotel;
    String phone_hotel;
    String price_hotel;
    String priceKM_hotel;
    private boolean isSaved;
    public Hotel(){

    }


    public Hotel(String id_hotel, String id_user, String name_hotel, String image_hotel, String address_hotel, String description_hotel, String gmail_hotel, String phone_hotel, String price_hotel, String priceKM_hotel, boolean isSaved) {
        this.id_hotel = id_hotel;
        this.id_user = id_user;
        this.name_hotel = name_hotel;
        this.image_hotel = image_hotel;
        this.address_hotel = address_hotel;
        this.description_hotel = description_hotel;
        this.gmail_hotel = gmail_hotel;
        this.phone_hotel = phone_hotel;
        this.price_hotel = price_hotel;
        this.priceKM_hotel = priceKM_hotel;
        this.isSaved = isSaved;
    }

    public String getPriceKM_hotel() {
        return priceKM_hotel;
    }

    public void setPriceKM_hotel(String priceKM_hotel) {
        this.priceKM_hotel = priceKM_hotel;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public String getId_hotel() {
        return id_hotel;
    }

    public void setId_hotel(String id_hotel) {
        this.id_hotel = id_hotel;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getName_hotel() {
        return name_hotel;
    }

    public void setName_hotel(String name_hotel) {
        this.name_hotel = name_hotel;
    }

    public String getImage_hotel() {
        return image_hotel;
    }

    public void setImage_hotel(String image_hotel) {
        this.image_hotel = image_hotel;
    }

    public String getAddress_hotel() {
        return address_hotel;
    }

    public void setAddress_hotel(String address_hotel) {
        this.address_hotel = address_hotel;
    }

    public String getDescription_hotel() {
        return description_hotel;
    }

    public void setDescription_hotel(String description_hotel) {
        this.description_hotel = description_hotel;
    }

    public String getGmail_hotel() {
        return gmail_hotel;
    }

    public void setGmail_hotel(String gmail_hotel) {
        this.gmail_hotel = gmail_hotel;
    }

    public String getPhone_hotel() {
        return phone_hotel;
    }

    public void setPhone_hotel(String phone_hotel) {
        this.phone_hotel = phone_hotel;
    }

    public String getPrice_hotel() {
        return price_hotel;
    }

    public void setPrice_hotel(String price_hotel) {
        this.price_hotel = price_hotel;
    }
}
