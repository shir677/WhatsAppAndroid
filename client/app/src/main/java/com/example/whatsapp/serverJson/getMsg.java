package com.example.whatsapp.serverJson;

import android.icu.text.SimpleDateFormat;
import android.os.Build;


import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Date;

public class getMsg {
    private String id;
    private String content;
    private String created;
    private sender sender;

    public getMsg( String id , String content,String created, sender sender ) {
        this.id = id;
        this.content = content;
        this.created =  created;
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Date getCreatedDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy, hh-mm");
        try {
            Date date = sdf.parse(created);
            return date;
        }catch (Exception e){
            return null;
        }
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public sender getSender() {
        return sender;
    }

    public String getSenderName() {
        return sender.getUsername();
    }

    public void setSender(sender sender) {
        this.sender = sender;
    }


    @Override
    public String toString() {
        return
                "content=" + content +
                        ", sender=" + sender.getUsername();
    }
}


