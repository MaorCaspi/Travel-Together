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
import com.finalProject.travelTogether.model.Student;
import com.squareup.picasso.Picasso;

public class StudentDetailsFragment extends Fragment {
    TextView countryNameTv;
    TextView idTv;
    TextView descriptionTv;
    ImageView avatarImv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_details, container, false);

        String stId = StudentDetailsFragmentArgs.fromBundle(getArguments()).getStudentId();

        Model.instance.getStudentById(stId, new Model.GetStudentById() {
            @Override
            public void onComplete(Student student) {
                countryNameTv.setText(student.getCountryName());
                descriptionTv.setText(student.getDescription());
                idTv.setText(student.getId());
                if (student.getAvatarUrl() != null) {
                    Picasso.get().load(student.getAvatarUrl()).into(avatarImv);
                }
            }
        });
        descriptionTv = view.findViewById(R.id.details_description_tv);
        countryNameTv = view.findViewById(R.id.details_name_tv);
        idTv = view.findViewById(R.id.details_id_tv);
        avatarImv = view.findViewById(R.id.details_avatar_img);

        Button backBtn = view.findViewById(R.id.details_back_btn);
        backBtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigateUp();
        });
        return view;
    }
}