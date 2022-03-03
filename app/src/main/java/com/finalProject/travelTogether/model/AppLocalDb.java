package com.finalProject.travelTogether.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.finalProject.travelTogether.MyApplication;

@Database(entities = {Post.class , User.class}, version = 1)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao postDao();
    public abstract UserDao userDao();
}

public class AppLocalDb{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "localDb.db")
                    .fallbackToDestructiveMigration()
                    .build();

}