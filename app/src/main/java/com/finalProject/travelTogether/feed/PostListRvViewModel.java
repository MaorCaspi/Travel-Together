package com.finalProject.travelTogether.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.finalProject.travelTogether.feed.relations.PostAndUser;
import com.finalProject.travelTogether.model.AppLocalDb;
import com.finalProject.travelTogether.model.Model;
import com.finalProject.travelTogether.model.Post;
import com.finalProject.travelTogether.model.User;
import java.util.List;

public class PostListRvViewModel extends ViewModel {
    LiveData<List<PostAndUser>> data;
    LiveData<List<PostAndUser>> userPosts;
    LiveData<List<User>> usersData;
    MutableLiveData<User> currentUser;

    public PostListRvViewModel(){
        data = Model.instance.getAll();
        usersData = Model.instance.getAllUsers();
        currentUser = Model.instance.getCurrentUser();
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


    public void updateUser(User user) {
        Model.instance.addUser(user, new Model.AddUserListener() {
            @Override
            public void onComplete() {
                currentUser.postValue(user);
            }
        });
    }

    public LiveData<User> getCurrentUser() {
        currentUser = Model.instance.getCurrentUser();
        return currentUser;
    }

    public void editPost(Post post) {
        Model.instance.editPost(post);
    }
}