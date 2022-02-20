package com.finalProject.travelTogether.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.finalProject.travelTogether.R;
import com.finalProject.travelTogether.feed.BaseActivity;
import com.finalProject.travelTogether.model.Model;
import com.finalProject.travelTogether.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class RegisterFragment extends Fragment {

    private static final int REQUEST_CAMERA = 1;
    private static final int PICK_IMAGE = 2;
    private FirebaseAuth mAuth;
    EditText fullNameEt;
    EditText emailEt;
    EditText passwordEt;
    Button registerBtn;
    ImageView avatarImv;
    ImageButton camBtn;
    ImageButton galleryBtn;
    ProgressBar progressBar;
    Bitmap imageBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        emailEt = view.findViewById(R.id.register_email_et);
        fullNameEt = view.findViewById(R.id.register_fullName_et);
        passwordEt = view.findViewById(R.id.register_password_et);
        registerBtn = view.findViewById(R.id.register_register_btn);
        progressBar = view.findViewById(R.id.register_progressbar);
        progressBar.setVisibility(View.GONE);
        avatarImv = view.findViewById(R.id.register_avatar_imv);
        camBtn = view.findViewById(R.id.register_cam_btn);
        galleryBtn = view.findViewById(R.id.register_gallery_btn);

        camBtn.setOnClickListener(v -> {
            openCam();
        });

        galleryBtn.setOnClickListener(v -> {
            openGallery();
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(v -> {
            String fullName=fullNameEt.getText().toString();
            String email=emailEt.getText().toString();
            String password=passwordEt.getText().toString();
            if(fullName.equals("") || email.equals("") || password.equals("")){
                Toast.makeText(getContext(), "fields can not be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            register(email,password);
        });
        return view;
    }

    private void register(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        registerBtn.setEnabled(false);
        camBtn.setEnabled(false);
        galleryBtn.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            createNewUser();;
                            FirebaseUser user = mAuth.getCurrentUser();
                            toFeedActivity();
                        }
                        else {//If the registration was failed
                            progressBar.setVisibility(View.GONE);
                            registerBtn.setEnabled(true);
                            camBtn.setEnabled(true);
                            galleryBtn.setEnabled(true);
                            //Display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
    }
    private void createNewUser(){
        String email=emailEt.getText().toString();
        String fullName=fullNameEt.getText().toString();
        User user = new User(email,fullName);
        if (imageBitmap == null){
            Model.instance.addUser(user,()->{ });
        }
        else{
            Model.instance.saveImage(imageBitmap, email + ".jpg", url -> {
                user.setAvatarUrl(url);
                Model.instance.addUser(user,()->{
                });
            },0);
        }
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

    private void toFeedActivity() {
        Intent intent = new Intent(getContext(), BaseActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}