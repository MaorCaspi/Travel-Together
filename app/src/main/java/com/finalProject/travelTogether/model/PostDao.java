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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... posts);

    @Query("DELETE FROM Post WHERE id = :postId")
    void deleteByPostId(String postId);

    @Transaction
    @Query("select * from Post order by updateDate desc")
    List<PostAndUser> getAll();
}