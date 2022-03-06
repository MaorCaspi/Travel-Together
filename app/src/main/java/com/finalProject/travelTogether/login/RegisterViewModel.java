package com.finalProject.travelTogether.login;

import android.graphics.Bitmap;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel  {

    private Bitmap imageBitmap;

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}