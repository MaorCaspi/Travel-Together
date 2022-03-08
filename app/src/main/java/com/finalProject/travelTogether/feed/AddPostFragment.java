package com.finalProject.travelTogether.feed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.auth.FirebaseAuth;
import java.io.IOException;
import java.util.UUID;

public class AddPostFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    private static final int PICK_IMAGE = 2;
    AddPostViewModel viewModel;
    Spinner countryNameSP;
    EditText descriptionEt;
    Button saveBtn;
    Button cancelBtn;
    ProgressBar progressBar;
    ImageView postImageImv;
    ImageButton camBtn;
    ImageButton galleryBtn;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(AddPostViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        postImageImv.setImageBitmap(viewModel.getImageBitmap());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post,container, false);
        countryNameSP = (Spinner) view.findViewById(R.id.addPost_countryName_sp);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.countries_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        countryNameSP.setAdapter(adapter);
        descriptionEt = view.findViewById(R.id.addPost_description_et);
        saveBtn = view.findViewById(R.id.addPost_save_btn);
        cancelBtn = view.findViewById(R.id.addPost_cancel_btn);
        progressBar = view.findViewById(R.id.addPost_progressbar);
        progressBar.setVisibility(View.GONE);
        postImageImv = view.findViewById(R.id.addPost_postImage_imv);

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

        camBtn = view.findViewById(R.id.addPost_cam_btn);
        galleryBtn = view.findViewById(R.id.addPost_gallery_btn);

        camBtn.setOnClickListener(v -> {
            openCam();
        });

        galleryBtn.setOnClickListener(v -> {
            openGallery();     
        });
        return view;
    }

    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(pickPhoto, "Select Picture"), PICK_IMAGE);
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
            viewModel.setImageBitmap((Bitmap) extras.get("data"));
        }
        else if (requestCode == PICK_IMAGE){
            try {
                viewModel.setImageBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData()));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        postImageImv.setImageBitmap(viewModel.getImageBitmap());
    }

    private void save() {
        progressBar.setVisibility(View.VISIBLE);
        saveBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        camBtn.setEnabled(false);
        galleryBtn.setEnabled(false);
        String countryName = countryNameSP.getSelectedItem().toString();
        String description = descriptionEt.getText().toString();
        String authorEmailAddress = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String id = UUID.randomUUID().toString();
        Post post = new Post(countryName,id,description,authorEmailAddress);
        if (viewModel.getImageBitmap() == null){
            Model.instance.addPost(post,()->{
                Navigation.findNavController(getView()).navigateUp();
            });
        }else{
            Model.instance.saveImage(viewModel.getImageBitmap(), id + ".jpg", url -> {
                post.setPostImageUrl(url);
                Model.instance.addPost(post,()->{
                    Navigation.findNavController(getView()).navigateUp();
                });
            },1);
        }
    }
}