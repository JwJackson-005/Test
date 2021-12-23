package com.example.c196_pa.UI;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196_pa.Database.Repository;
import com.example.c196_pa.Entities.Course;
import com.example.c196_pa.Entities.Term;
import com.example.c196_pa.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class CourseList extends AppCompatActivity {
    private static final String TAG = "CourseList";
    Repository repository;
    List<Term> allTerms;
    int termId;
    Term currentTerm;
    public static int numCourses;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Course List Created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        termId = getIntent().getIntExtra("termId",-1);
        repository = new Repository(getApplication());
        allTerms = repository.getAllTerms();

        for(Term term : allTerms){
            if(term.getTermID() == termId)
                currentTerm = term;
        }

        setTitle("Course List");
        recyclerView=findViewById(R.id.coursesRecyclerView);
        refreshList();
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.refresh).setTitle("Refresh Courses");
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_term_list, menu);
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
                Toast.makeText(getApplicationContext(), "Refreshing courses...", Toast.LENGTH_LONG).show();// make another toast
                refreshList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createNewCourse(View view) {
        Intent intent = new Intent(CourseList.this, CourseDetails.class);
        if (currentTerm != null)
            intent.putExtra("termId",currentTerm.getTermID());

        startActivity(intent);
    }

    private void refreshList() {
        final CourseAdapter adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Course> filteredCourses = new ArrayList<>();
        List<Course> allCourses=repository.getAllCourses();

        for (Course course : allCourses) {
            if (course.getTermId() == termId)
                filteredCourses.add(course);
        }
        numCourses = filteredCourses.size();
        adapter.setCourses(filteredCourses);
    }
}