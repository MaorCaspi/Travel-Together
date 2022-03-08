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
    final public static String COLLECTION_NAME = "posts";
    @PrimaryKey
    @NonNull
    String id = "";
    String countryName = "";
    String description="";
    Long updateDate = new Long(0);
    String postImageUrl;
    String authorEmailAddress;
    boolean isDeleted=false;

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Post(){}
    public Post(String countryName, String id, String description, String authorEmailAddress) {
        this.countryName = countryName;
        this.id = id;
        this.description=description;
        this.authorEmailAddress=authorEmailAddress;
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

    public String getAuthorEmailAddress() {
        return authorEmailAddress;
    }

    public void setAuthorEmailAddress(String authorEmailAddress) {
        this.authorEmailAddress = authorEmailAddress;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setPostImageUrl(String url) {
        postImageUrl = url;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id",id);
        json.put("countryName",countryName);
        json.put("description",description);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("postImageUrl",postImageUrl);
        json.put("authorEmailAddress",authorEmailAddress);
        json.put("isDeleted",isDeleted);
        return json;
    }

    public static Post create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String name = (String) json.get("countryName");
        String description = (String) json.get("description");
        Timestamp ts = (Timestamp)json.get("updateDate");
        String postImageUrl = (String)json.get("postImageUrl");
        String authorEmailAddress = (String)json.get("authorEmailAddress");
        boolean isDeleted = (boolean)json.get("isDeleted");
        Post post = new Post(name,id,description,authorEmailAddress);
        Long updateDate = ts.getSeconds();
        post.setUpdateDate(updateDate);
        post.setPostImageUrl(postImageUrl);
        post.setDeleted(isDeleted);
        return post;
    }
}