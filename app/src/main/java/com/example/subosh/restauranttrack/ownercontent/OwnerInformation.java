package com.example.subosh.restauranttrack.ownercontent;

import java.io.Serializable;

public class OwnerInformation implements Serializable {
    private String email;
    private String ownerphone;
    private String date;
    private String downloadpath;
private String ownerAddress;
private String ownerDeliveryRequestStatus;

    public double getOwnerLatitude() {
        return ownerLatitude;
    }

    public void setOwnerLatitude(double ownerLatitude) {
        this.ownerLatitude = ownerLatitude;
    }

    public double getOwnerLongitude() {
        return ownerLongitude;
    }

    public void setOwnerLongitude(double ownerLongitude) {
        this.ownerLongitude = ownerLongitude;
    }

    private double ownerLatitude,ownerLongitude;

    private String ownername;

    public String getOwnerDeliveryRequestStatus() {
        return ownerDeliveryRequestStatus;
    }

    public void setOwnerDeliveryRequestStatus(String ownerDeliveryRequestStatus) {
        this.ownerDeliveryRequestStatus = ownerDeliveryRequestStatus;
    }

    public OwnerInformation(){

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOwnerphone() {
        return ownerphone;
    }

    public void setOwnerphone(String ownerphone) {
        this.ownerphone = ownerphone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDownloadpath() {
        return downloadpath;
    }



    public void setDownloadpath(String downloadpath) {
        this.downloadpath = downloadpath;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public OwnerInformation(String email, String ownerphone, String date,String downloadpath, String ownername,String ownerAddress,String ownerDeliveryRequestStatus,Double ownerLatitude,Double ownerLongitude)
    {
        this.email = email;
        this.ownerphone = ownerphone;
        this.date = date;
        this.downloadpath=downloadpath;
        this.ownername=ownername;
        this.ownerAddress=ownerAddress;
        this.ownerDeliveryRequestStatus=ownerDeliveryRequestStatus;
        this.ownerLatitude=ownerLatitude;
        this.ownerLongitude=ownerLongitude;

    }


}
