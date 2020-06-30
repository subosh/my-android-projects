package com.example.subosh.restauranttrack.customerscontent;

import java.io.Serializable;

public class CustomerInformation implements Serializable {
 private String email;
    private String customerphone;
    private String date;
    private String downloadpath;
    private String customeraddress;
    public String customername;

    public CustomerInformation(){

}

    public String getCustomeraddress() {
        return customeraddress;
    }

    public void setCustomeraddress(String customeraddress) {
        this.customeraddress = customeraddress;
    }

    public CustomerInformation(String email, String customerphone, String date , String downloadpath, String customername, String customeraddress)
        {
        this.email = email;
        this.customerphone = customerphone;
        this.date = date;
        this.downloadpath=downloadpath;
        this.customername=customername;

        this.customeraddress=customeraddress;
        }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomerphone() {
        return customerphone;
    }

    public void setCustomerphone(String customerphone) {
        this.customerphone = customerphone;
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
        this.downloadpath =downloadpath;
    }


    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername=customername;
    }


}
