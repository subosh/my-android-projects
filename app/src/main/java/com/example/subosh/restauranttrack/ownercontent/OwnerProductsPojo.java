package com.example.subosh.restauranttrack.ownercontent;



public class OwnerProductsPojo {
    private String  productname,productprice;
 OwnerProductsPojo(){

}
    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public OwnerProductsPojo(String productname, String productprice){
        this.productname=productname;
        this.productprice=productprice;

    }

}
