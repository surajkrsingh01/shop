package com.shoppursshop.models;

public class MyKyc {

    private int id;
    private String kycDocType,kycDocNumber,kycDocPic,kycStatus,signature;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKycDocType() {
        return kycDocType;
    }

    public void setKycDocType(String kycDocType) {
        this.kycDocType = kycDocType;
    }

    public String getKycDocNumber() {
        return kycDocNumber;
    }

    public void setKycDocNumber(String kycDocNumber) {
        this.kycDocNumber = kycDocNumber;
    }

    public String getKycDocPic() {
        return kycDocPic;
    }

    public void setKycDocPic(String kycDocPic) {
        this.kycDocPic = kycDocPic;
    }

    public String getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
