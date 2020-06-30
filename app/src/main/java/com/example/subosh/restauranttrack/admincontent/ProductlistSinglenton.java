package com.example.subosh.restauranttrack.admincontent;


import java.io.Serializable;

public class ProductlistSinglenton {
    public String productname;
    private Float productprice;
    private String productImageDownloadPath;


    public ProductlistSinglenton(String productname, Float productprice,String productImageDownloadPath) {
        this.productname = productname;
        this.productprice = productprice;
        this.productImageDownloadPath=productImageDownloadPath;
    }

    public String getProductImageDownloadPath() {
        return productImageDownloadPath;
    }

    public void setProductImageDownloadPath(String productImageDownloadPath) {
        this.productImageDownloadPath = productImageDownloadPath;
    }

    public ProductlistSinglenton() {


    }


    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public Float getProductprice() {
        return productprice;
    }

    public void setProductprice(Float productprice) {
        this.productprice = productprice;
    }

}