package com.finalProject.travelTogether.feed;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.finalProject.travelTogether.R;
import com.finalProject.travelTogether.feed.relations.PostAndUser;
import com.finalProject.travelTogether.model.Model;
import com.finalProject.travelTogether.model.User;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {
    ProfileViewModel viewModel;
    ImageView img;
    TextView name, email;
    Button editBtn,saveBtn;
    MyAdapter adapter;
    EditText nameEdit;
    LinearLayout imgEditLayout;
    User currentUser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        img = view.findViewById(R.id.profile_img);
        name = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        editBtn = view.findViewById(R.id.profile_edit_btn);
        saveBtn = view.findViewById(R.id.profile_save_btn);
        nameEdit = view.findViewById(R.id.profile_edit_name);
        imgEditLayout = view.findViewById(R.id.profile_edit_img_layout);


        RecyclerView list = view.findViewById(R.id.profile_rv);

        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            name.setText(user.getFullName());
            email.setText(user.getEmailAddress());
            if(user.getAvatarUrl() != null){
                Picasso.get().load(user.getAvatarUrl()).into(img);
            }
            this.currentUser = user;
         });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setVisibility(View.GONE);
                nameEdit.setText(name.getText());
                nameEdit.setVisibility(View.VISIBLE);
                imgEditLayout.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.VISIBLE);
                editBtn.setVisibility(View.GONE);
            }
        });
        saveBtn.setOnClickListener((v) -> {
            String newUserName = nameEdit.getText().toString();
            if(newUserName.isEmpty())
            {
                Toast.makeText(getContext(),"Please fill the name field",Toast.LENGTH_SHORT).show();
            }else {
                currentUser.setFullName(newUserName);
                viewModel.updateUser(currentUser);
                saveBtn.setVisibility(View.GONE);
                editBtn.setVisibility(View.VISIBLE);
                nameEdit.setVisibility(View.GONE);
                imgEditLayout.setVisibility(View.GONE);
                name.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        return view;
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
            authorNameTv.setText(post.user.getFullName());
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
                        .load(post.user.getAvatarUrl())
                        .into(authorImageImv);
            }
            editBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
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