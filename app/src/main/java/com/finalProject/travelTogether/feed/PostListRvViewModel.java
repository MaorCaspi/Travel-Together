package com.finalProject.travelTogether.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.finalProject.travelTogether.model.Model;
import com.finalProject.travelTogether.model.Post;
import com.finalProject.travelTogether.model.User;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;

public class PostListRvViewModel extends ViewModel {
    LiveData<List<Post>> data;
    LiveData<List<User>> usersData;
    LiveData<User> currentUser;

    public PostListRvViewModel(){
        data = Model.instance.getAll();
        usersData = Model.instance.getAllUsers();
        currentUser = Model.instance.getUserByEmailAddress(FirebaseAuth.getInstance().getCurrentUser().getEmail());

    }

    public LiveData<List<Post>> getData() {
        return data;
    }

    public LiveData<List<User>> getUsersData() {
        return usersData;
    }

    public LiveData<User> getCurrentUser() {
        currentUser = Model.instance.getUserByEmailAddress(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        return currentUser;
    }
}