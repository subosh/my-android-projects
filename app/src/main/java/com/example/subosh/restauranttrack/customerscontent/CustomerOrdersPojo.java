package com.example.subosh.restauranttrack.customerscontent;

public class CustomerOrdersPojo {
    private String productname;
    private Float productsquantity;
    private String measure;
    private  Float productamount;
    private String orderstatus;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private String productImagePath;
    private boolean isSelected;
CustomerOrdersPojo(){

}
    public Float getProductamount() {
        return productamount;
    }

    public void setProductamount(Float productamount) {
        this.productamount = productamount;
    }

    public String getProductname() {
        return productname;
    }

    public String getProductImagePath() {
        return productImagePath;
    }

    public void setProductImagePath(String productImagePath) {
        this.productImagePath = productImagePath;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public Float getProductsquantity() {
        return productsquantity;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public void setProductsquantity(Float productsquantity) {
        this.productsquantity = productsquantity;

    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public CustomerOrdersPojo(String productname, Float productsquantity, String measure,Float productamount,String productImagePath) {
        this.productname = productname;
        this.productsquantity = productsquantity;
        this.measure=measure;
        this.productamount=productamount;
        this.productImagePath=productImagePath;
    }



}
