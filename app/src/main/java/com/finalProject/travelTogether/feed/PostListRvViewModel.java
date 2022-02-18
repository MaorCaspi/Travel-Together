package com.finalProject.travelTogether.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.finalProject.travelTogether.model.Model;
import com.finalProject.travelTogether.model.Post;
import java.util.List;

public class PostListRvViewModel extends ViewModel {
    LiveData<List<Post>> data;

    public PostListRvViewModel(){
        data = Model.instance.getAll();
    }
    public LiveData<List<Post>> getData() {
        return data;
    }
}
//