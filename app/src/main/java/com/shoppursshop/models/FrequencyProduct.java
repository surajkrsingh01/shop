package com.shoppursshop.models;

public class FrequencyProduct {

    private int id,prodId,frequencyQty,frequencyValue;
    private String prodName,prodCode,prodDesc,prodImage1,prodImage2,prodImage3,status,frequency,
            frequencyStartDate,frequencyEndDate,nextOrderDate,lastOrderDate;
    private String unit,color,size;
    private float offerPrice,prodCgst,prodIgst,prodSgst,prodMrp,prodSp;

    public String getLastOrderDate() {
        return lastOrderDate;
    }

    public void setLastOrderDate(String lastOrderDate) {
        this.lastOrderDate = lastOrderDate;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrequencyQty() {
        return frequencyQty;
    }

    public void setFrequencyQty(int frequencyQty) {
        this.frequencyQty = frequencyQty;
    }

    public int getFrequencyValue() {
        return frequencyValue;
    }

    public void setFrequencyValue(int frequencyValue) {
        this.frequencyValue = frequencyValue;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getProdImage1() {
        return prodImage1;
    }

    public void setProdImage1(String prodImage1) {
        this.prodImage1 = prodImage1;
    }

    public String getProdImage2() {
        return prodImage2;
    }

    public void setProdImage2(String prodImage2) {
        this.prodImage2 = prodImage2;
    }

    public String getProdImage3() {
        return prodImage3;
    }

    public void setProdImage3(String prodImage3) {
        this.prodImage3 = prodImage3;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFrequencyStartDate() {
        return frequencyStartDate;
    }

    public void setFrequencyStartDate(String frequencyStartDate) {
        this.frequencyStartDate = frequencyStartDate;
    }

    public String getFrequencyEndDate() {
        return frequencyEndDate;
    }

    public void setFrequencyEndDate(String frequencyEndDate) {
        this.frequencyEndDate = frequencyEndDate;
    }

    public String getNextOrderDate() {
        return nextOrderDate;
    }

    public void setNextOrderDate(String nextOrderDate) {
        this.nextOrderDate = nextOrderDate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public float getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(float offerPrice) {
        this.offerPrice = offerPrice;
    }

    public float getProdCgst() {
        return prodCgst;
    }

    public void setProdCgst(float prodCgst) {
        this.prodCgst = prodCgst;
    }

    public float getProdIgst() {
        return prodIgst;
    }

    public void setProdIgst(float prodIgst) {
        this.prodIgst = prodIgst;
    }

    public float getProdSgst() {
        return prodSgst;
    }

    public void setProdSgst(float prodSgst) {
        this.prodSgst = prodSgst;
    }

    public float getProdMrp() {
        return prodMrp;
    }

    public void setProdMrp(float prodMrp) {
        this.prodMrp = prodMrp;
    }

    public float getProdSp() {
        return prodSp;
    }

    public void setProdSp(float prodSp) {
        this.prodSp = prodSp;
    }
}
