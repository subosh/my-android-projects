package com.example.subosh.restauranttrack.customerscontent;

import java.util.ArrayList;

public class CustomerMonthlyListPojo {
    private ArrayList<CustomerOrdersPojo> customerOrdersPojoArrayList;
    private String orderNodeString;
    private boolean check;
    public CustomerMonthlyListPojo(){}

    public String getOrderNodeString() {
        return orderNodeString;
    }

    public boolean getProductAvailabilityBoolean() {
        return check;
    }

    public void setProductAvailabilityBoolean(boolean check) {
        this.check = check;
    }

    public void setOrderNodeString(String orderNodeString) {
        this.orderNodeString = orderNodeString;

    }

    public CustomerMonthlyListPojo(String orderNodeString,boolean check) {

        this.orderNodeString=orderNodeString;
        this.check=check;
    }


    public ArrayList<CustomerOrdersPojo> getCustomerOrdersPojoArrayList() {
        return customerOrdersPojoArrayList;
    }

    public void setCustomerOrdersPojoArrayList(ArrayList<CustomerOrdersPojo> customerOrdersPojoArrayList) {
        this.customerOrdersPojoArrayList = customerOrdersPojoArrayList;

    }
}
