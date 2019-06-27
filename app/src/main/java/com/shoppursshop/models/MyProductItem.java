package com.shoppursshop.models;

import java.io.Serializable;
import java.util.List;

public class MyProductItem implements Serializable {

    private int id,pcoId,prodId,prodCatId,prodSubCatId,prodReorderLevel,prodQoh,qty;
    private String offerId,offerType,comboProductIds,prodName,prodCode,prodBarCode,prodDesc,prodHsnCode,prodMfgDate,prodExpiryDate,prodMfgBy,prodImage1,prodImage2,prodImage3,
            createdBy,updatedBy,createdDate,updatedDate,status,isBarCodeAvailable;
    private String unit,color,size,retRetailerId,dbName,dbUserName,dbPassword;
    private float offerPrice,prodCgst,prodIgst,prodSgst,prodWarranty,prodMrp,prodSp,totalAmount;
    private boolean isSelected;
    private int position,freeProductPosition,offerCounter;

    private List<ProductUnit> productUnitList;
    private List<ProductSize> productSizeList;

    public List<ProductUnit> getProductUnitList() {
        return productUnitList;
    }

    public void setProductUnitList(List<ProductUnit> productUnitList) {
        this.productUnitList = productUnitList;
    }

    public List<ProductSize> getProductSizeList() {
        return productSizeList;
    }

    public void setProductSizeList(List<ProductSize> productSizeList) {
        this.productSizeList = productSizeList;
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

    public String getComboProductIds() {
        return comboProductIds;
    }

    public void setComboProductIds(String comboProductIds) {
        this.comboProductIds = comboProductIds;
    }

    public int getFreeProductPosition() {
        return freeProductPosition;
    }

    public void setFreeProductPosition(int freeProductPosition) {
        this.freeProductPosition = freeProductPosition;
    }

    public int getOfferCounter() {
        return offerCounter;
    }

    public void setOfferCounter(int offerCounter) {
        this.offerCounter = offerCounter;
    }

    private Object productOffer;

    private List<ProductComboOffer> productPriceOfferList;

    public Object getProductOffer() {
        return productOffer;
    }

    public void setProductOffer(Object productOffer) {
        this.productOffer = productOffer;
    }

    public List<ProductComboOffer> getProductPriceOfferList() {
        return productPriceOfferList;
    }

    public void setProductPriceOfferList(List<ProductComboOffer> productPriceOfferList) {
        this.productPriceOfferList = productPriceOfferList;
    }

    public float getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(float offerPrice) {
        this.offerPrice = offerPrice;
    }

    public int getPcoId() {
        return pcoId;
    }

    public void setPcoId(int pcoId) {
        this.pcoId = pcoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private List<Barcode> barcodeList;

    public List<Barcode> getBarcodeList() {
        return barcodeList;
    }

    public void setBarcodeList(List<Barcode> barcodeList) {
        this.barcodeList = barcodeList;
    }

    public String getIsBarCodeAvailable() {
        return isBarCodeAvailable;
    }

    public void setIsBarCodeAvailable(String isBarCodeAvailable) {
        this.isBarCodeAvailable = isBarCodeAvailable;
    }

    public int getProdSubCatId() {
        return prodSubCatId;
    }

    public void setProdSubCatId(int prodSubCatId) {
        this.prodSubCatId = prodSubCatId;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public int getProdId() {
        return prodId;
    }
    public void setProdId(int prodId) {
        this.prodId = prodId;
    }
    public int getProdCatId() {
        return prodCatId;
    }
    public void setProdCatId(int prodCatId) {
        this.prodCatId = prodCatId;
    }
    public String getProdName() {
        return prodName;
    }
    public void setProdName(String prodName) {
        this.prodName = prodName;
    }
    public String getProdBarCode() {
        return prodBarCode;
    }
    public void setProdBarCode(String prodBarCode) {
        this.prodBarCode = prodBarCode;
    }
    public String getProdDesc() {
        return prodDesc;
    }
    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }
    public int getProdReorderLevel() {
        return prodReorderLevel;
    }
    public void setProdReorderLevel(int prodReorderLevel) {
        this.prodReorderLevel = prodReorderLevel;
    }
    public int getProdQoh() {
        return prodQoh;
    }
    public void setProdQoh(int prodQoh) {
        this.prodQoh = prodQoh;
    }
    public String getProdHsnCode() {
        return prodHsnCode;
    }
    public void setProdHsnCode(String prodHsnCode) {
        this.prodHsnCode = prodHsnCode;
    }
    public String getProdMfgDate() {
        return prodMfgDate;
    }
    public void setProdMfgDate(String prodMfgDate) {
        this.prodMfgDate = prodMfgDate;
    }
    public String getProdExpiryDate() {
        return prodExpiryDate;
    }
    public void setProdExpiryDate(String prodExpiryDate) {
        this.prodExpiryDate = prodExpiryDate;
    }
    public String getProdMfgBy() {
        return prodMfgBy;
    }
    public void setProdMfgBy(String prodMfgBy) {
        this.prodMfgBy = prodMfgBy;
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
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public String getUpdatedBy() {
        return updatedBy;
    }
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    public String getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    public String getUpdatedDate() {
        return updatedDate;
    }
    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
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

    public float getProdSp() {
        return prodSp;
    }
    public void setProdSp(float prodSp) {
        this.prodSp = prodSp;
    }
    public float getProdWarranty() {
        return prodWarranty;
    }
    public void setProdWarranty(float prodWarranty) {
        this.prodWarranty = prodWarranty;
    }
    public float getProdMrp() {
        return prodMrp;
    }
    public void setProdMrp(float prodMrp) {
        this.prodMrp = prodMrp;
    }
    public String getRetRetailerId() {
        return retRetailerId;
    }
    public void setRetRetailerId(String retRetailerId) {
        this.retRetailerId = retRetailerId;
    }
    public String getDbName() {
        return dbName;
    }
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
    public String getDbUserName() {
        return dbUserName;
    }
    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }
    public String getDbPassword() {
        return dbPassword;
    }
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
