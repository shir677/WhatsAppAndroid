package com.example.whatsapp.serverJson;


import com.example.whatsapp.Entities.User;

public class getContact {
    private String id;
    private User user;
    private getMsg lastMessage;


    public getContact(String id, User user, getMsg lastMessage) {
        this.id = id;
        this.user = user;
        this.lastMessage = lastMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public getMsg getMessage() {
        return lastMessage;
    }

    public void setMessage(getMsg lastMessage) {
        this.lastMessage = lastMessage;
    }
}
