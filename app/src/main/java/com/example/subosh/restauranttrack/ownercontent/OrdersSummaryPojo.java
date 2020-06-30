package com.example.subosh.restauranttrack.ownercontent;

import com.example.subosh.restauranttrack.customerscontent.CustomerInformation;
import com.example.subosh.restauranttrack.customerscontent.CustomerOrdersPojo;

import java.util.ArrayList;

public class OrdersSummaryPojo {
private String ownerdeliveryRequestStatus;
private ArrayList<OwnerInformation> ownerInformationArrayList;
private OwnerInformation ownerInformation;
private String orderedDate,orderedTime,orderId;

    public OwnerInformation getOwnerInformation() {
        return ownerInformation;
    }

    public void setOwnerInformation(OwnerInformation ownerInformation) {
        this.ownerInformation = ownerInformation;
    }

    public OrdersSummaryPojo(String orderedDate,String orderedTime,String orderId,ArrayList<CustomerInformation> customerDeliveryDetails, ArrayList<CustomerOrdersPojo> customerOrdersPojo, String customername, String adminordercaretaker, String not_needed, OwnerInformation ownerInformationObject, String orderstatus, String ordernodestring, Float orderAmount) {
       this.orderedDate=orderedDate;
       this.orderedTime=orderedTime;
        this.orderId=orderId;
        this.customerDeliveryAddressDetails=customerDeliveryDetails;
        this.customerOrdersPojos=customerOrdersPojo;
        this.customername = customername;
        this.admincaretaker=adminordercaretaker;
        this.ownerdeliveryRequestStatus=not_needed;
        this.ownerInformation=ownerInformationObject;
        this.orderStatus=orderstatus;
        this.ordernodestring=ordernodestring;
        this.orderAmount=orderAmount;
    }

    public String getOwnerdeliveryRequestStatus() {
        return ownerdeliveryRequestStatus;
    }

    public void setOwnerdeliveryRequestStatus(String ownerdeliveryRequestStatus) {
        this.ownerdeliveryRequestStatus = ownerdeliveryRequestStatus;
    }

    public ArrayList<OwnerInformation> getOwnerInformationArrayList() {
        return ownerInformationArrayList;
    }

    public void setOwnerInformationArrayList(ArrayList<OwnerInformation> ownerInformationArrayList) {
        this.ownerInformationArrayList = ownerInformationArrayList;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrdernodestring() {
        return ordernodestring;
    }

    public void setOrdernodestring(String ordernodestring) {
        this.ordernodestring = ordernodestring;
    }

    public float getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(float orderAmount) {
        this.orderAmount = orderAmount;
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

    OrdersSummaryPojo(){


}
private ArrayList<CustomerInformation> customerDeliveryAddressDetails;
private float orderAmount;
private String orderStatus,ordernodestring;

    public ArrayList<CustomerInformation> getCustomerDeliveryAddressDetails() {
        return customerDeliveryAddressDetails;
    }

    public void setCustomerDeliveryAddressDetails(ArrayList<CustomerInformation> customerDeliveryAddressDetails) {
        this.customerDeliveryAddressDetails = customerDeliveryAddressDetails;
    }

    public OrdersSummaryPojo(String orderedDate,String orderedTime,String orderId,ArrayList<CustomerInformation> customerDeliveryAddressDetails, ArrayList<CustomerOrdersPojo> customerOrdersPojos, String customername, String admincaretaker, String ownerdeliveryRequestStatus, ArrayList<OwnerInformation> ownerInformationArrayList, String orderStatus, String ordernodestring, Float orderAmount) {
        this.orderedDate=orderedDate;
        this.orderedTime=orderedTime;
        this.orderId=orderId;
        this.customerDeliveryAddressDetails=customerDeliveryAddressDetails;
        this.customername = customername;
        this.customerOrdersPojos=customerOrdersPojos;
        this.admincaretaker=admincaretaker;
        this.ownerdeliveryRequestStatus=ownerdeliveryRequestStatus;
        this.ownerInformationArrayList=ownerInformationArrayList;
        this.orderStatus=orderStatus;
        this.ordernodestring=ordernodestring;

        this.orderAmount=orderAmount;
    }

    public String getAdmincaretaker() {
        return admincaretaker;
    }

    public void setAdmincaretaker(String admincaretaker) {
        this.admincaretaker = admincaretaker;
    }

    private  String customername;
private String admincaretaker;

    public ArrayList<CustomerOrdersPojo> getCustomerOrdersPojos() {
        return customerOrdersPojos;
    }

    public void setCustomerOrdersPojos(ArrayList<CustomerOrdersPojo> customerOrdersPojos) {
        this.customerOrdersPojos = customerOrdersPojos;
    }

    private  ArrayList<CustomerOrdersPojo> customerOrdersPojos;

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }
}
