package com.saul.arf.Proyectos;

import java.io.Serializable;

public class Proyecto implements Serializable {
    private int id;
    private String client_firstname;
    private String client_lastname;
    private String client_phone;
    private String client_email;
    public Proyecto(int id, String client_firstname, String client_lastname, String client_phone, String client_email) {
        this.id = id;
        this.client_firstname = client_firstname;
        this.client_lastname = client_lastname;
        this.client_phone = client_phone;
        this.client_email = client_email;
    }

    public Proyecto(String client_firstname, String client_lastname, String client_phone, String client_email) {
        this.client_firstname = client_firstname;
        this.client_lastname = client_lastname;
        this.client_phone = client_phone;
        this.client_email = client_email;
    }

    public int getId() {
        return id;
    }

    public String getClient_firstname() {
        return client_firstname;
    }

    public String getClient_lastname() {
        return client_lastname;
    }

    public String getClient_phone() {
        return client_phone;
    }

    public String getClient_email() {
        return client_email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClient_firstname(String client_firstname) {
        this.client_firstname = client_firstname;
    }

    public void setClient_lastname(String client_lastname) {
        this.client_lastname = client_lastname;
    }

    public void setClient_phone(String client_phone) {
        this.client_phone = client_phone;
    }

    public void setClient_email(String client_email) {
        this.client_email = client_email;
    }

}

