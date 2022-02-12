package com.finalProject.travelTogether.feed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import com.finalProject.travelTogether.R;
import com.finalProject.travelTogether.model.Model;
import com.finalProject.travelTogether.model.Post;
import java.io.IOException;
import java.util.UUID;

public class AddPostFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    private static final int PICK_IMAGE = 2;
    Spinner countryNameSP;
    EditText descriptionEt;
    Button saveBtn;
    Button cancelBtn;
    ProgressBar progressBar;
    Bitmap imageBitmap;
    ImageView avatarImv;
    ImageButton camBtn;
    ImageButton galleryBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post,container, false);
        countryNameSP = (Spinner) view.findViewById(R.id.main_countryName_sp);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.countries_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        countryNameSP.setAdapter(adapter);
        descriptionEt = view.findViewById(R.id.main_description_et);
        saveBtn = view.findViewById(R.id.main_save_btn);
        cancelBtn = view.findViewById(R.id.main_cancel_btn);
        progressBar = view.findViewById(R.id.main_progressbar);
        progressBar.setVisibility(View.GONE);
        avatarImv = view.findViewById(R.id.main_avatar_imv);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

        camBtn = view.findViewById(R.id.main_cam_btn);
        galleryBtn = view.findViewById(R.id.main_gallery_btn);

        camBtn.setOnClickListener(v -> {
            openCam();
        });

        galleryBtn.setOnClickListener(v -> {
            openGallery();     
        });
        return view;
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void openCam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data==null) {
            return;
        }
        if (requestCode == REQUEST_CAMERA){
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
        }
        else if (requestCode == PICK_IMAGE){
            try {
                imageBitmap=MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        avatarImv.setImageBitmap(imageBitmap);
    }

    private void save() {
        progressBar.setVisibility(View.VISIBLE);
        saveBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        camBtn.setEnabled(false);
        galleryBtn.setEnabled(false);
        String countryName = countryNameSP.getSelectedItem().toString();
        String description = descriptionEt.getText().toString();
        String id = UUID.randomUUID().toString();
        Log.d("TAG","country name:" + countryName + " id:" + id + " description:" + description);
        Post post = new Post(countryName,id,description);
        if (imageBitmap == null){
            Model.instance.addPost(post,()->{
                Navigation.findNavController(countryNameSP).navigateUp();
            });
        }else{
            Model.instance.saveImage(imageBitmap, id + ".jpg", url -> {
                post.setAvatarUrl(url);
                Model.instance.addPost(post,()->{
                    Navigation.findNavController(countryNameSP).navigateUp();
                });
            });
        }
    }
}