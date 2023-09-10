package com.example.whatsapp.Entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Contact  implements Serializable {
    @PrimaryKey() @NonNull
    private String id;
    private String username;
    private String displayName;
    private String profilePic;
    private String lastContent;
    private String lastCreated;

    public Contact(String id, String username, String displayName, String profilePic, String lastContent, String lastCreated) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.profilePic = profilePic;
        this.lastContent = lastContent;
        this.lastCreated = lastCreated;
    }
    @Ignore
    public Contact() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getLastContent() {
        return lastContent;
    }

    public void setLastContent(String lastContent) {
        this.lastContent = lastContent;
    }

    public void setLastCreated(String lastCreated) {
        this.lastCreated = lastCreated;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getLastCreated() {
        return lastCreated;
    }

    public String getUsername() {
        return username;
    }


    public void setId(String id) {
        this.id = id;
    }

}