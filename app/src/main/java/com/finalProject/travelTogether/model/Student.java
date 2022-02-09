package com.finalProject.travelTogether.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Student {
    final public static String COLLECTION_NAME = "students";
    @PrimaryKey
    @NonNull
    String id = "";
    String name = "";
    String description="";
    Long updateDate = new Long(0);
    String avatarUrl;

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Student(){}
    public Student(String name, String id, String description) {
        this.name = name;
        this.id = id;
        this.description=description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id",id);
        json.put("name",name);
        json.put("description",description);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("avatarUrl",avatarUrl);
        return json;
    }

    public static Student create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String name = (String) json.get("name");
        String description = (String) json.get("description");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();
        String avatarUrl = (String)json.get("avatarUrl");

        Student student = new Student(name,id,description);
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
