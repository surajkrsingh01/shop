package com.shoppursshop.models;

public class MyDevice {

    private int id;
    private String shopCode,serialNumber,model,allotment,merchantId,merchantName,maker, allottedUserId,
            allottedUserName,allottedUserMobile,status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAllotment() {
        return allotment;
    }

    public void setAllotment(String allotment) {
        this.allotment = allotment;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getAllottedUserId() {
        return allottedUserId;
    }

    public void setAllottedUserId(String allottedUserId) {
        this.allottedUserId = allottedUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAllottedUserName() {
        return allottedUserName;
    }

    public void setAllottedUserName(String allottedUserName) {
        this.allottedUserName = allottedUserName;
    }

    public String getAllottedUserMobile() {
        return allottedUserMobile;
    }

    public void setAllottedUserMobile(String allottedUserMobile) {
        this.allottedUserMobile = allottedUserMobile;
    }
}
