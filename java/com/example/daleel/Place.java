package com.example.daleel;

public class Place {
    private int id;

    private String name;
    private String category;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private String phone;
    private byte[] image;

    public Place() {

    }

    public Place(String name, String category, String street, String city, String postalCode, String country, String phone) {
        this.name = name;
        this.category = category;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.phone = phone;
    }

    public Place(int id, String name, String category, String street, String city, String postalCode, String country, String phone) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.phone = phone;
    }

    public Place(int id, String name, String category, String street, String city, String postalCode, String country, String phone, byte[] image) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.phone = phone;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
