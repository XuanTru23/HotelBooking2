package com.example.hotelbooking.Models;

public class Booking {
    String id_booking;
    String id_user;
    String id_hotel;
    String name;
    String phone;
    String check_in;
    String check_out;
    String people;
    String rooms;
    String id_nguoi_dang;
    String name_hotel;
    String image_hotel;

    public Booking(){

    }

    public Booking(String id_booking, String id_user, String id_hotel, String name, String phone, String check_in, String check_out, String people, String rooms, String id_nguoi_dang, String name_hotel, String image_hotel) {
        this.id_booking = id_booking;
        this.id_user = id_user;
        this.id_hotel = id_hotel;
        this.name = name;
        this.phone = phone;
        this.check_in = check_in;
        this.check_out = check_out;
        this.people = people;
        this.rooms = rooms;
        this.id_nguoi_dang = id_nguoi_dang;
        this.name_hotel = name_hotel;
        this.image_hotel = image_hotel;
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

    public String getId_nguoi_dang() {
        return id_nguoi_dang;
    }

    public void setId_nguoi_dang(String id_nguoi_dang) {
        this.id_nguoi_dang = id_nguoi_dang;
    }

    public String getId_booking() {
        return id_booking;
    }

    public void setId_booking(String id_booking) {
        this.id_booking = id_booking;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_hotel() {
        return id_hotel;
    }

    public void setId_hotel(String id_hotel) {
        this.id_hotel = id_hotel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }

    public String getCheck_out() {
        return check_out;
    }

    public void setCheck_out(String check_out) {
        this.check_out = check_out;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }
}
