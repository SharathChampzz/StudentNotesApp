package com.example.navigationactivity;

public class Info {

    private String url;
    private String username;
    private String info;
    private String mail;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;
    Info(){

    }

    public Info(String url, String username, String info, String mail) {
        this.url = url;
        this.username = username;
        this.info = info;
        this.mail = mail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
