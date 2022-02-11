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
import android.widget.ImageView;
import android.widget.TextView;
import com.finalProject.travelTogether.R;
import com.finalProject.travelTogether.model.Model;
import com.finalProject.travelTogether.model.Post;
import com.squareup.picasso.Picasso;

public class PostListRvFragment extends Fragment {
    PostListRvViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PostListRvViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts_list,container,false);

        swipeRefresh = view.findViewById(R.id.postlist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshPostList());

        RecyclerView list = view.findViewById(R.id.postlist_rv);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v,int position) {
                String stId = viewModel.getData().getValue().get(position).getId();
                Navigation.findNavController(v).navigate(PostListRvFragmentDirections.actionPostListRvFragmentToPostDetailsFragment(stId));
            }
        });

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

    private void refresh() {
        adapter.notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView avatarImv;
        TextView nameTv;
        TextView idTv;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.listrow_name_tv);
            idTv = itemView.findViewById(R.id.listrow_id_tv);
            avatarImv = itemView.findViewById(R.id.listrow_avatar_imv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(v,pos);
                }
            });
        }

        void bind(Post post){
            nameTv.setText(post.getCountryName());
            idTv.setText(post.getId());
            avatarImv.setImageResource(R.drawable.avatar);
            if (post.getAvatarUrl() != null) {
                Picasso.get()
                        .load(post.getAvatarUrl())
                        .into(avatarImv);
            }
        }
    }

    interface OnItemClickListener{
        void onItemClick(View v,int position);
    }
    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        OnItemClickListener listener;
        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.post_list_row,parent,false);
            MyViewHolder holder = new MyViewHolder(view,listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Post post = viewModel.getData().getValue().get(position);
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
        }else {
            return super.onOptionsItemSelected(item);
        }
    }
}