package com.example.whatsapp.Entities;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.Date;

@Entity
@TypeConverters(DateConverter.class)
public class Message {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String ChatId;
    private String content;
    private String created;
    private String sender;

    public void setId(int id) {
        this.id = id;
    }

    public Message(String ChatId , String content, String created, String sender ) {
        this.ChatId = ChatId;
        this.content = content;
        this.created =  created;
        this.sender = sender;
    }

    public Message() {
    }

    public int getId() {
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getChatId() {
        return ChatId;
    }

    public void setChatId(String chatId) {
        ChatId = chatId;
    }

    @Override
    public String toString() {
        return
                "content=" + content +
                        ", sender=" + sender;
    }
}
