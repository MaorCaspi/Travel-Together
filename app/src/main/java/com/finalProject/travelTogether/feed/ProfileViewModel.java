package com.finalProject.travelTogether.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.finalProject.travelTogether.feed.relations.PostAndUser;
import com.finalProject.travelTogether.model.Model;
import com.finalProject.travelTogether.model.User;
import java.util.List;

public class ProfileViewModel extends ViewModel {
    LiveData<List<PostAndUser>> userPosts;
    MutableLiveData<User> currentUser;

    public ProfileViewModel(){
        currentUser = Model.instance.getCurrentUser();
        userPosts = Model.instance.getUserPosts();
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
}
