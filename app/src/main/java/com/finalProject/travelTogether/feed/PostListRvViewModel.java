package com.finalProject.travelTogether.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.finalProject.travelTogether.feed.relations.PostAndUser;
import com.finalProject.travelTogether.model.Model;
import com.finalProject.travelTogether.model.Post;
import com.finalProject.travelTogether.model.User;
import java.util.List;

public class PostListRvViewModel extends ViewModel {
    LiveData<List<PostAndUser>> data;
    LiveData<List<User>> usersData;
    MutableLiveData<User> currentUser;

    public PostListRvViewModel(){
        data = Model.instance.getAll();
        usersData = Model.instance.getAllUsers();
        currentUser = Model.instance.getCurrentUser();
    }

    public LiveData<List<PostAndUser>> getData() {
        return data;
    }

    public LiveData<List<User>> getUsersData() {
        return usersData;
    }

    public LiveData<User> getCurrentUser() {
        currentUser = Model.instance.getCurrentUser();
        return currentUser;
    }

    public void editPost(Post post) {
        Model.instance.editPost(post);
    }
}