package com.finalProject.travelTogether.feed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.finalProject.travelTogether.R;
import com.finalProject.travelTogether.feed.relations.PostAndUser;
import com.finalProject.travelTogether.model.Model;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class PostListRvFragment extends Fragment {
    PostListRvViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    Button privateUserPageBtn;
    ImageButton avatarBtn;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PostListRvViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        Model.instance.refreshPostList();
        refresh();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts_list,container,false);

        swipeRefresh = view.findViewById(R.id.postlist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshPostList());
        privateUserPageBtn=view.findViewById(R.id.postlist_userName_btn);
        avatarBtn = view.findViewById(R.id.postlist_avatar_btn);
        RecyclerView list = view.findViewById(R.id.postlist_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));


        avatarBtn.setOnClickListener(v -> {
            toUserProfile();
        });
        privateUserPageBtn.setOnClickListener(v -> {
            toUserProfile();
        });
        viewModel.getCurrentUser().observe(getViewLifecycleOwner(),user -> {
            if(user!=null) {
                privateUserPageBtn.setText(user.getFullName());
                String avatarUrl = user.getAvatarUrl();
                if (avatarUrl != null) {
                    Picasso.get().load(avatarUrl).into(avatarBtn);
                }
                else{// if the user doesn't have profile picture
                    avatarBtn.setImageResource(R.drawable.avatar);
                }
            }
        });
        viewModel.getUsersData().observe(getViewLifecycleOwner(),l -> {
            viewModel.getCurrentUser();
        });

        adapter = new MyAdapter();
        list.setAdapter(adapter);


        setHasOptionsMenu(true);
        viewModel.getData().observe(getViewLifecycleOwner(), list1 -> refresh());
        swipeRefresh.setRefreshing(Model.instance.getPostListLoadingState().getValue() == Model.PostListLoadingState.loading);
        Model.instance.getPostListLoadingState().observe(getViewLifecycleOwner(), postListLoadingState -> {
            if (postListLoadingState == Model.PostListLoadingState.loading){
                swipeRefresh.setRefreshing(true);
            }else{
                swipeRefresh.setRefreshing(false);
            }
        });
        return view;
    }

    private void toUserProfile() {
        Navigation.findNavController(getView()).navigate(PostListRvFragmentDirections.actionPostListRvFragmentToProfileFragment());
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        Model.instance.refreshUserList();
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
            if(post.post.getAuthorEmailAddress().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){//if the post belongs to current user, then show the buttons
                editBtn.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.VISIBLE);
                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Navigation.findNavController(v).navigate(PostListRvFragmentDirections.actionPostListRvFragmentToEditPostFragment(getPosition()));
                    }
                });
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post.post.setDeleted(true);
                        viewModel.editPost(post.post);
                    }
                });
            }
            else{
                editBtn.setVisibility(View.GONE);
                deleteBtn.setVisibility(View.GONE);
            }
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.post_list_row,parent,false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            PostAndUser post = viewModel.getData().getValue().get(position);
            holder.bind(post);
        }

        @Override
        public int getItemCount() {
            if(viewModel.getData().getValue() == null){
                return 0;
            }
            return viewModel.getData().getValue().size();
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