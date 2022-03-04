package com.finalProject.travelTogether.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.finalProject.travelTogether.feed.relations.PostAndUser;
import com.finalProject.travelTogether.model.Model;
import com.finalProject.travelTogether.model.Post;
import com.finalProject.travelTogether.model.User;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;

public class PostListRvViewModel extends ViewModel {
    LiveData<List<PostAndUser>> data;
    LiveData<List<PostAndUser>> userPosts;
    LiveData<List<User>> usersData;
    LiveData<User> currentUser;

    public PostListRvViewModel(){
        data = Model.instance.getAll();
        usersData = Model.instance.getAllUsers();
        currentUser = Model.instance.getUserByEmailAddress(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        userPosts = Model.instance.getUserPosts();
    }

    public LiveData<List<PostAndUser>> getData() {
        return data;
    }

    public LiveData<List<User>> getUsersData() {
        return usersData;
    }

    public LiveData<List<PostAndUser>> getUserPosts() {
        return userPosts;
    }

    public LiveData<User> getCurrentUser() {
        currentUser = Model.instance.getUserByEmailAddress(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        return currentUser;
    }
}