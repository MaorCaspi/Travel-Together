package com.finalProject.travelTogether.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Post {
    final public static String COLLECTION_NAME = "students";
    @PrimaryKey
    @NonNull
    String id = "";
    String countryName = "";
    String description="";
    Long updateDate = new Long(0);
    String avatarUrl;

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Post(){}
    public Post(String countryName, String id, String description) {
        this.countryName = countryName;
        this.id = id;
        this.description=description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id",id);
        json.put("countryName",countryName);
        json.put("description",description);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("avatarUrl",avatarUrl);
        return json;
    }

    public static Post create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String name = (String) json.get("countryName");
        String description = (String) json.get("description");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();
        String avatarUrl = (String)json.get("avatarUrl");

        Post student = new Post(name,id,description);
        student.setUpdateDate(updateDate);
        student.setAvatarUrl(avatarUrl);
        return student;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setAvatarUrl(String url) {
        avatarUrl = url;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
