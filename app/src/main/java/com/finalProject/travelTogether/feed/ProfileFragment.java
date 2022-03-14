package com.finalProject.travelTogether.feed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.finalProject.travelTogether.R;
import com.finalProject.travelTogether.feed.relations.PostAndUser;
import com.finalProject.travelTogether.model.Model;
import com.finalProject.travelTogether.model.User;
import com.squareup.picasso.Picasso;
import java.io.IOException;

public class ProfileFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1, PICK_IMAGE = 2;
    ProfileViewModel viewModel;
    ImageView avatarImv;
    TextView name, email;
    Button editBtn,saveBtn,cancelBtn;
    ImageButton camBtn, galleryBtn;
    MyAdapter adapter;
    EditText nameEdit;
    LinearLayout imgEditLayout;
    User currentUser;
    ProgressBar progressBar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        avatarImv = view.findViewById(R.id.profile_img);
        name = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        editBtn = view.findViewById(R.id.profile_edit_btn);
        saveBtn = view.findViewById(R.id.profile_save_btn);
        cancelBtn = view.findViewById(R.id.profile_cancel_btn);
        nameEdit = view.findViewById(R.id.profile_edit_name);
        imgEditLayout = view.findViewById(R.id.profile_edit_img_layout);
        progressBar = view.findViewById(R.id.profile_progressbar);
        camBtn = view.findViewById(R.id.profile_cam_btn);
        galleryBtn = view.findViewById(R.id.profile_gallery_btn);

        progressBar.setVisibility(View.GONE);
        cancelBtn.setVisibility(View.GONE);

        RecyclerView list = view.findViewById(R.id.profile_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            name.setText(user.getFullName());
            email.setText(user.getEmailAddress());
            if(viewModel.getImageBitmap()==null && user.getAvatarUrl() != null){
                Picasso.get().load(user.getAvatarUrl()).into(avatarImv);
            }
            this.currentUser = user;
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editOptionsShow();
                viewModel.setEditIsInProgress(true);
                nameEdit.setText(name.getText());
            }
        });
        saveBtn.setOnClickListener((v) -> {
            progressBar.setVisibility(View.VISIBLE);
            viewModel.setEditIsInProgress(false);
            String newUserName = nameEdit.getText().toString();
            if(newUserName.isEmpty())
            {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Please fill the name field",Toast.LENGTH_SHORT).show();
            }else {
                currentUser.setFullName(newUserName);
                if(viewModel.getImageBitmap()!=null){//If the profile picture has changed
                    Model.instance.saveImage(viewModel.getImageBitmap(), email + ".jpg", url -> {
                        currentUser.setAvatarUrl(url);
                        viewModel.updateUser(currentUser);
                        editOptionsHide();
                        list.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    },0);
                }
                else {
                    viewModel.updateUser(currentUser);
                    editOptionsHide();
                    list.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        cancelBtn.setOnClickListener((v) -> {
            viewModel.setEditIsInProgress(false);
            editOptionsHide();
            viewModel.setImageBitmap(null);
            name.setText(currentUser.getFullName());
            if(currentUser.getAvatarUrl() != null){
                Picasso.get().load(currentUser.getAvatarUrl()).into(avatarImv);
            }
            else{
                avatarImv.setImageResource(R.drawable.avatar);
            }
        });

        camBtn.setOnClickListener(v -> {
            openCam();
        });

        galleryBtn.setOnClickListener(v -> {
            openGallery();
        });

        return view;
    }

    private void editOptionsShow() {
        name.setVisibility(View.GONE);
        nameEdit.setVisibility(View.VISIBLE);
        imgEditLayout.setVisibility(View.VISIBLE);
        saveBtn.setVisibility(View.VISIBLE);
        editBtn.setVisibility(View.GONE);
        cancelBtn.setVisibility(View.VISIBLE);
    }

    private void editOptionsHide() {
        saveBtn.setVisibility(View.GONE);
        editBtn.setVisibility(View.VISIBLE);
        nameEdit.setVisibility(View.GONE);
        imgEditLayout.setVisibility(View.GONE);
        name.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);
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
        avatarImv.setImageBitmap(viewModel.getImageBitmap());
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView postImageImv;
        TextView countryNameTv;
        TextView descriptionTv;
        TextView authorNameTv;
        ImageView authorImageImv;
        Button editBtn;
        Button deleteBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            countryNameTv = itemView.findViewById(R.id.listrow_countryName_tv);
            descriptionTv = itemView.findViewById(R.id.listrow_description_tv);
            postImageImv = itemView.findViewById(R.id.listrow_postImage_imv);
            authorNameTv = itemView.findViewById(R.id.listrow_authorName_tv);
            authorImageImv = itemView.findViewById(R.id.listrow_avatar_imv);
            editBtn = itemView.findViewById(R.id.listrow_edit_btn);
            deleteBtn = itemView.findViewById(R.id.listrow_delete_btn);
        }

        void bind(PostAndUser post){
            if(post==null || post.user==null || post.post==null) {
                Model.instance.refreshPostList();
                return;
            }
            countryNameTv.setText(post.post.getCountryName());
            descriptionTv.setText(post.post.getDescription());
            authorNameTv.setText(currentUser.getFullName());
            if (post.post.getPostImageUrl() != null) {
                Picasso.get()
                        .load(post.post.getPostImageUrl())
                        .into(postImageImv);
                postImageImv.setVisibility(View.VISIBLE);
            }
            else{//if the post does not have a photo
                postImageImv.setVisibility(View.GONE);
            }
            authorImageImv.setImageResource(R.drawable.avatar);
            if (post.user.getAvatarUrl() != null) {
                Picasso.get()
                        .load(currentUser.getAvatarUrl())
                        .into(authorImageImv);
            }
            editBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(viewModel.getImageBitmap()!=null) {
            avatarImv.setImageBitmap(viewModel.getImageBitmap());
        }
        if(viewModel.isEditIsInProgress()==true){
            editOptionsShow();
        }
    }

    class MyAdapter extends RecyclerView.Adapter<ProfileFragment.MyViewHolder>{

        @NonNull
        @Override
        public ProfileFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.post_list_row,parent,false);
            ProfileFragment.MyViewHolder holder = new ProfileFragment.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ProfileFragment.MyViewHolder holder, int position) {
            PostAndUser post = viewModel.getUserPosts().getValue().get(position);
            holder.bind(post);
        }

        @Override
        public int getItemCount() {
            if(viewModel.getUserPosts().getValue() == null){
                return 0;
            }
            return viewModel.getUserPosts().getValue().size();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.post_list_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addPostFragment){
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}