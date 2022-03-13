package com.finalProject.travelTogether.feed;

import android.graphics.Bitmap;
import androidx.lifecycle.ViewModel;
import com.finalProject.travelTogether.model.Post;

public class EditPostViewModel extends ViewModel {

    private Post post;
    private Bitmap imageBitmap;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}