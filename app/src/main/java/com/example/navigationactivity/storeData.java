package com.example.navigationactivity;

import android.app.Application;

public class storeData extends Application {
    private static String EmailId;
    private static String username;
    private static String profileUrl;

    public static String getLogStatus() {
        return logStatus;
    }

    public static void setLogStatus(String logStatus) {
        storeData.logStatus = logStatus;
    }

    private static String logStatus;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        storeData.username = username;
    }

    public static String getProfileUrl() {
        return profileUrl;
    }

    public static void setProfileUrl(String profileUrl) {
        storeData.profileUrl = profileUrl;
    }



    public static String getEmailId() {
        return EmailId;
    }

    public static void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public static int flag;

    public static int getFlag() {
        return flag;
    }

    public static void setFlag(int flag) {
        storeData.flag = flag;
    }
}
