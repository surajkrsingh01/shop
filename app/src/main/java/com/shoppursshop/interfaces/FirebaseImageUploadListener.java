package com.shoppursshop.interfaces;

public interface FirebaseImageUploadListener {

    void onImageUploaded(String position,String url);
    void onImageFailed(String position);
}
