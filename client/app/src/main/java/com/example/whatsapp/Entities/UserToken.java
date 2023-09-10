package com.example.whatsapp.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserToken {
    @NonNull
    @PrimaryKey
    private String UserName;
    private String Token;

    public UserToken(String userName, String token) {
        UserName = userName;
        Token = token;
    }

    public UserToken(){

    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
