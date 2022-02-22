package com.finalProject.travelTogether.feed;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.finalProject.travelTogether.R;
import com.finalProject.travelTogether.model.Model;
import com.finalProject.travelTogether.model.Post;
import com.squareup.picasso.Picasso;

public class PostDetailsFragment extends Fragment {
    TextView countryNameTv;
    TextView descriptionTv;
    ImageView avatarImv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);

        String postId = PostDetailsFragmentArgs.fromBundle(getArguments()).getPostId();

        Model.instance.getPostById(postId, new Model.GetPostById() {
            @Override
            public void onComplete(Post post) {
                countryNameTv.setText(post.getCountryName());
                descriptionTv.setText(post.getDescription());
                if (post.getPostImageUrl() != null) {
                    Picasso.get().load(post.getPostImageUrl()).into(avatarImv);
                }
            }
        });
        descriptionTv = view.findViewById(R.id.details_description_tv);
        countryNameTv = view.findViewById(R.id.details_countryName_tv);
        avatarImv = view.findViewById(R.id.details_avatar_img);

        Button backBtn = view.findViewById(R.id.details_back_btn);
        backBtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigateUp();
        });
        return view;
    }
}