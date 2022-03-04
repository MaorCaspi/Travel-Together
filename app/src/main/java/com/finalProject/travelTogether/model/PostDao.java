package com.finalProject.travelTogether.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.finalProject.travelTogether.feed.relations.PostAndUser;

import java.util.List;

@Dao
public interface PostDao {

    @Query("select * from Post")
    List<Post> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... posts);

    @Transaction
    @Query("select * from Post")
    List<PostAndUser> getMaor();
}