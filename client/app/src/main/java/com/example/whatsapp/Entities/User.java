package com.example.whatsapp.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey @NonNull
    private String username;
    private String displayName;
    private String password;
    private String profilePic;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String username, String displayName, String password,String profilePic) {
        this.username = username;
        this.displayName = displayName;
        this.profilePic = profilePic;
        this.password = password;
    }

    @Ignore
    public User(String username, String displayName,String profilePic) {
        this.username = username;
        this.displayName = displayName;
        this.profilePic = profilePic;
    }

    @Ignore
    public User(String username) {
        this.username = username;
    }
    @Ignore
    public User() {
    }

    @Ignore
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    @Override
    public String toString() {
        if(this!=null){
            return "User{" +
                    "username=" + username +
                    ", profilePic='" + profilePic +
                    ", displayName='" + displayName + '\'' +
                    '}';
        }
    return "null";
    }
}
