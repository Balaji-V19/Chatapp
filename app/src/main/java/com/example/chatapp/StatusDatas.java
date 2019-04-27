package com.example.chatapp;

public class StatusDatas {
    public String email;
    public String uid;
    public String ChatorStory;

    public StatusDatas(String email, String uid, String chatorStory) {
        this.email = email;
        this.uid = uid;
        ChatorStory = chatorStory;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getChatorStory() {
        return ChatorStory;
    }

    public void setChatorStory(String chatorStory) {
        ChatorStory = chatorStory;
    }

    @Override
    public boolean equals( Object obj) {
        boolean value=false;
        if (obj!=null && obj instanceof StatusDatas)
        {
            value=this.uid==((StatusDatas) obj).uid;
        }
        return value;
    }

    @Override
    public int hashCode() {

        int i=17;
        i=31*i+(this.uid==null ? 0 : this.uid.hashCode());

        return i;
    }
}