package com.finalProject.travelTogether.feed;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.finalProject.travelTogether.model.Model;
import com.finalProject.travelTogether.model.Post;
import com.finalProject.travelTogether.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {
    PostListRvViewModel viewModel;
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
        viewModel = new ViewModelProvider(this).get(PostListRvViewModel.class);
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

//        adapter.setOnItemClickListener(new ProfileFragment.OnItemClickListener() {
//            @Override
//            public void onItemClick(View v,int position) {
//                String stId = viewModel.getData().getValue().get(position).getId();
//                Navigation.findNavController(v).navigate(PostListRvFragmentDirections.actionPostListRvFragmentToPostDetailsFragment(stId));
//            }
//        });
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

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            countryNameTv = itemView.findViewById(R.id.listrow_countryName_tv);
            descriptionTv = itemView.findViewById(R.id.listrow_description_tv);
            postImageImv = itemView.findViewById(R.id.listrow_postImage_imv);
            authorNameTv = itemView.findViewById(R.id.listrow_authorName_tv);
        }

        void bind(Post post){
            countryNameTv.setText(post.getCountryName());
            descriptionTv.setText(post.getDescription());
            authorNameTv.setText(post.getAuthorEmailAddress());//////////////
            postImageImv.setImageResource(R.drawable.avatar);
            if (post.getPostImageUrl() != null) {
                Picasso.get()
                        .load(post.getPostImageUrl())
                        .into(postImageImv);
            }
        }
    }

    interface OnItemClickListener{
        void onItemClick(View v,int position);
    }
    class MyAdapter extends RecyclerView.Adapter<ProfileFragment.MyViewHolder>{

        OnItemClickListener listener;
        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public ProfileFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.post_list_row,parent,false);
            ProfileFragment.MyViewHolder holder = new ProfileFragment.MyViewHolder(view,listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ProfileFragment.MyViewHolder holder, int position) {
            Post post = viewModel.getUserPosts().getValue().get(position).post;
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