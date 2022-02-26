package com.finalProject.travelTogether.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface UserDao {

    @Query("select * from User where emailAddress")
    List<User> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);

    @Query("select * from User where emailAddress=:email Limit 1")
    User getUserByEmailAddress(String email);
}