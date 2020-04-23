package com.example.navigationactivity.ui.home;

import com.google.firebase.database.Exclude;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String mKey;
    private String mMailId;
    private String uploaderName;
    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }



    public String getmMailId() {
        return mMailId;
    }

    public void setmMailId(String mMailId) {
        this.mMailId = mMailId;
    }

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String imageUrl, String mail, String uploader) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
        mMailId = mail;
        uploaderName = uploader;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }


    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }

}
