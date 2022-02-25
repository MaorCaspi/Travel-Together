package com.finalProject.travelTogether.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.finalProject.travelTogether.MyApplication;

@Database(entities = {Post.class}, version = 4)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao postDao();
}

@Database(entities = {User.class}, version = 3)
abstract class UsersAppLocalDbRepository extends RoomDatabase {
    public abstract UserDao userDao();
}

public class AppLocalDb{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "posts.db")
                    .fallbackToDestructiveMigration()
                    .build();

    static public UsersAppLocalDbRepository usersDb =
            Room.databaseBuilder(MyApplication.getContext(),
                    UsersAppLocalDbRepository.class,
                    "users.db")
                    .fallbackToDestructiveMigration()
                    .build();
}