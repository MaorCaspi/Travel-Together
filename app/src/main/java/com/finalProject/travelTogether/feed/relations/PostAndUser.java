package com.finalProject.travelTogether.feed.relations;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.finalProject.travelTogether.model.Post;
import com.finalProject.travelTogether.model.User;

public class PostAndUser {
    @Embedded
    public Post post;
    @Relation(parentColumn = "authorEmailAddress",entityColumn = "emailAddress")
    public User user;
}