package com.example.myproject;

public class Data_Upload {
    public String uDescription;
    public String uImageUrl;
    public String uTitle;

    public String uLocation;




    //step 2 sa floating naglagay ng key
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long uPrice;
    public Data_Upload() {

    }

    public String getuTitle() {
        return uTitle;
    }

    public String getuLocation() {
        return uLocation;
    }

    public Long getuPrice() {
        return uPrice; // Updated to Long
    }

    public String getuDescription() {
        return uDescription;
    }

    public String getuImageUrl() {
        return uImageUrl;
    }

    public Data_Upload(String uTitle, Long uPrice, String uDescription, String uImageUrl, String uLocation) {
        this.uTitle = uTitle;
        this.uPrice = uPrice;
        this.uDescription = uDescription;
        this.uImageUrl = uImageUrl;
        this.uLocation = uLocation;
    }
}
