package com.example.chatapp;

public class ReceverDatas {
    private String email;
    private String Uid;
    private boolean receiver;

    public ReceverDatas(String email, String uid, boolean receiver) {
        this.email = email;
        Uid = uid;
        this.receiver = receiver;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public boolean isReceiver() {
        return receiver;
    }

    public void setReceiver(boolean receiver) {
        this.receiver = receiver;
    }
}
