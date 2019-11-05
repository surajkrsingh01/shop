package com.shoppursshop.interfaces;

import java.util.List;

public interface FirebaseImageUploadListener {

    void onImageUploaded(String position,String url);
    void onImageFailed(String position);
}
