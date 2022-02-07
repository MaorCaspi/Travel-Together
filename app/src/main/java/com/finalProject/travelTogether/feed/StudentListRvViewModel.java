package com.finalProject.travelTogether.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.finalProject.travelTogether.model.Model;
import com.finalProject.travelTogether.model.Student;

import java.util.List;

public class StudentListRvViewModel extends ViewModel {
    LiveData<List<Student>> data;

    public StudentListRvViewModel(){
        data = Model.instance.getAll();
    }
    public LiveData<List<Student>> getData() {
        return data;
    }
}