package com.example.subosh.restauranttrack.admincontent;

import com.example.subosh.restauranttrack.customerscontent.CustomerInformation;
import com.example.subosh.restauranttrack.customerscontent.CustomerOrdersPojo;

import java.util.ArrayList;
import java.util.Map;

public class AdminViewOrdersSummaryPojo {

    public   ArrayList<CustomerOrdersPojo> customerOrdersPojos;
    public String customername;
    public String orderstatus;
    public String ordernodes;
    public Float orderamount;
public ArrayList<CustomerInformation> customerDeliveryDetailsList;
public String deliveryRequestStatus;
public ArrayList<Map<String,Double>> coordinateDataList;
    public String getOrdernodes() {
        return ordernodes;
    }

    public void setOrdernodes(String ordernodes) {
        this.ordernodes = ordernodes;
    }

    AdminViewOrdersSummaryPojo(){


    }

    public Float getOrderamount() {
        return orderamount;
    }

    public void setOrderamount(Float orderamount) {
        this.orderamount = orderamount;
    }

    public ArrayList<CustomerInformation> getCustomerDeliveryDetailsList() {
        return customerDeliveryDetailsList;
    }

    public void setCustomerDeliveryDetailsList(ArrayList<CustomerInformation> customerDeliveryDetailsList) {
        this.customerDeliveryDetailsList = customerDeliveryDetailsList;
    }

    public String getDeliveryRequestStatus() {
        return deliveryRequestStatus;
    }

    public ArrayList<Map<String, Double>> getCoordinateDataList() {
        return coordinateDataList;
    }

    public void setCoordinateDataList(ArrayList<Map<String, Double>> coordinateDataList) {
        this.coordinateDataList = coordinateDataList;
    }

    public String getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(String orderedDate) {
        this.orderedDate = orderedDate;
    }

    public String getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(String orderedTime) {
        this.orderedTime = orderedTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setDeliveryRequestStatus(String deliveryRequestStatus) {
        this.deliveryRequestStatus = deliveryRequestStatus;
    }
    private String orderedDate,orderedTime,orderId;

    public AdminViewOrdersSummaryPojo(String orderedDate,String orderedTime,String orderId,ArrayList<CustomerOrdersPojo> customerOrdersPojos, String customername, String orderstatus, String ordernodes, Float orderamount, ArrayList<CustomerInformation> customerDeliveryDetailsList, String deliveryRequestStatus,ArrayList<Map<String,Double>> coordinateDataList) {
        this.orderedDate=orderedDate;
        this.orderedTime=orderedTime;
        this.orderId=orderId;
        this.customername = customername;

        this.customerOrdersPojos=customerOrdersPojos;
        this.orderstatus=orderstatus;
        this.ordernodes=ordernodes;
        this.orderamount=orderamount;

        this.customerDeliveryDetailsList=customerDeliveryDetailsList;
        this.deliveryRequestStatus=deliveryRequestStatus;
        this.coordinateDataList=coordinateDataList;

    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public ArrayList<CustomerOrdersPojo> getCustomerOrdersPojos() {
        return customerOrdersPojos;
    }

    public void setCustomerOrdersPojos(ArrayList<CustomerOrdersPojo> customerOrdersPojos) {
        this.customerOrdersPojos = customerOrdersPojos;
    }


    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }
}

