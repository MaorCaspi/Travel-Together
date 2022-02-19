package com.finalProject.travelTogether.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;

@Entity
public class User {

    final public static String COLLECTION_NAME = "users";
    @PrimaryKey
    @NonNull
    String emailAddress = "";
    String fullName = "";
    Long updateDate = new Long(0);
    String avatarUrl;

    public User(@NonNull String emailAddress, String fullName) {
        this.emailAddress = emailAddress;
        this.fullName = fullName;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    @NonNull
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(@NonNull String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("emailAddress",emailAddress);
        json.put("fullName",fullName);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("avatarUrl",avatarUrl);
        return json;
    }

    public static User create(Map<String, Object> json) {
        String emailAddress = (String) json.get("emailAddress");
        String fullName = (String) json.get("fullName");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();
        String avatarUrl = (String)json.get("avatarUrl");

        User user = new User(emailAddress,fullName);
        user.setUpdateDate(updateDate);
        user.setAvatarUrl(avatarUrl);
        return user;
    }
}
