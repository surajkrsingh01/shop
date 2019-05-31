package com.shoppursshop.models;

import java.io.Serializable;

public class ShopOfferItem implements Serializable {
    private String offerType;
    private String offerId;
    private String offerName;
    private String productName;
    private String productId;
    private String productImage;
    private int productLocalImage;

    private Object productObject;

    public Object getProductObject() {
        return productObject;
    }

    public void setProductObject(Object productObject) {
        this.productObject = productObject;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getProductLocalImage() {
        return productLocalImage;
    }

    public void setProductLocalImage(int productLocalImage) {
        this.productLocalImage = productLocalImage;
    }
}
