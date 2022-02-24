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
    private String userFullName;
    private String avatarURL;

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public PostListRvViewModel(){
        data = Model.instance.getAll();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Model.instance.getUserByEmailAddress(mAuth.getCurrentUser().getEmail(), new Model.GetUserByEmailAddress() {
            @Override
            public void onComplete(User user) {
                userFullName=user.getFullName();
                avatarURL=user.getAvatarUrl();
            }
        });
    }

    public LiveData<List<Post>> getData() {
        return data;
    }
}