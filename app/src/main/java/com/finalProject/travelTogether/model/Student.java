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
    boolean flag = false;
    Long updateDate = new Long(0);
    String avatarUrl;

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Student(){}
    public Student(String name, String id, boolean flag) {
        this.name = name;
        this.id = id;
        this.flag = flag;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public boolean isFlag() {
        return flag;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id",id);
        json.put("name",name);
        json.put("flag",flag);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("avatarUrl",avatarUrl);
        return json;
    }

    public static Student create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String name = (String) json.get("name");
        Boolean flag = (Boolean) json.get("flag");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();
        String avatarUrl = (String)json.get("avatarUrl");

        Student student = new Student(name,id,flag);
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
