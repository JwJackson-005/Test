package com.example.c196_pa.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.c196_pa.Database.Repository;
import com.example.c196_pa.Entities.Assessment;
import com.example.c196_pa.Entities.Course;
import com.example.c196_pa.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AssessmentList extends AppCompatActivity {
    private static final String TAG = "AssessmentList";
    Repository repository;
    List<Course> allCourses;
    int courseId;
    Course currentCourse;
    public static int numAssessments;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        courseId = getIntent().getIntExtra("courseId", -1);

        repository = new Repository(getApplication());
        allCourses = repository.getAllCourses();

        for (Course course : allCourses) {
            if(course.getCourseId() == courseId)
                currentCourse = course;
        }

        setTitle("Assessment List");
        recyclerView = findViewById(R.id.assessmentsRecyclerView);
        refreshList();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_term_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.refresh).setTitle("Refresh Assessments");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.refresh:
                Log.d(TAG, "onOptionsItemSelected: refresh");
                Toast.makeText(getApplicationContext(), "Refreshing assessments...", Toast.LENGTH_LONG).show();// make another toast
                refreshList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void assessmentDetails(View view) {
        Intent intent = new Intent(AssessmentList.this, AssessmentDetails.class);
        if (currentCourse != null)
            intent.putExtra("courseId",currentCourse.getCourseId());

        startActivity(intent);
    }

    private void refreshList(){
        final AssessmentAdapter adapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Assessment> filteredAssessments = new ArrayList<>();
        List<Assessment> allAssessments = repository.getAllAssessments();

        for (Assessment assessment : allAssessments) {
            if (assessment.getCourseId() == courseId)
                filteredAssessments.add(assessment);
        }
        numAssessments = filteredAssessments.size();
        adapter.setAssessments(filteredAssessments);
    }
}