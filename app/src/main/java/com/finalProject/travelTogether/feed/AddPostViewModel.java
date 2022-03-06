package com.finalProject.travelTogether.feed;

import android.graphics.Bitmap;
import androidx.lifecycle.ViewModel;

public class AddPostViewModel extends ViewModel {

    private Bitmap imageBitmap;

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}