package com.dev.smsphishingdetector.apis;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response implements Serializable {
    @SerializedName("status")
    String status;

    @SerializedName("domain_name")
    String domain_name;

    @SerializedName("create_date")
    String create_date;

    @SerializedName("registrant_contact")
    private RegistrantContact registrantContact;



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }




    public RegistrantContact getRegistrantContact() {
        return registrantContact;
    }

    public void setRegistrantContact(RegistrantContact registrantContact) {
        this.registrantContact = registrantContact;
    }






    public class RegistrantContact implements Serializable {
        @SerializedName("company")
        private String registrantCompany;

        @SerializedName("country_name")
        private String registrantCountryName;

        // Getter and setter methods for registrant contact fields

        public String getRegistrantCompany() {
            return registrantCompany;
        }

        public void setRegistrantCompany(String registrantCompany) {
            this.registrantCompany = registrantCompany;
        }

        public String getRegistrantCountryName() {
            return registrantCountryName;
        }

        public void setRegistrantCountryName(String registrantCountryName) {
            this.registrantCountryName = registrantCountryName;
        }


    }




}
