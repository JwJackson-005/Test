package com.example.c196_pa.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.c196_pa.Database.Repository;
import com.example.c196_pa.Entities.Assessment;
import com.example.c196_pa.Entities.Course;
import com.example.c196_pa.Entities.Term;
import com.example.c196_pa.R;

public class MainActivity extends AppCompatActivity {

    public static int numAlert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            populateDatabase();

    }

    private void populateDatabase() {
        Repository repository = new Repository(getApplication());

        Assessment a1 = new Assessment(1, "TestAssessment_1","10/07/2021",
                "Performance", 1);
        Course c1 = new Course(1, "TestCourse_1", "07/07/2021", "10/07/2021",
                "In Progress", "testInstructor1", "111-1111",
                "testEmail@wgu.edu", 1);
        Term t1 = new Term(1, "TestTerm_1", "07/07/2021", "10/07/2021");

        repository.insert(a1);
        repository.insert(c1);
        repository.insert(t1);

        Assessment a2 = new Assessment(2, "TestAssessment_2","10/07/2021",
                "Performance", 2);
        Course c2 = new Course(2, "TestCourse_2", "07/07/2021", "10/07/2021",
                "In Progress", "testInstructor2", "222-2222",
                "testEmail2@wgu.edu",1);
        Term t2 = new Term(2, "TestTerm_2", "07/07/2021", "10/07/2021");

        repository.insert(a2);
        repository.insert(c2);
        repository.insert(t2);
    }

    public void enterButtonClicked(View view) {
        Intent termViewIntent = new Intent(MainActivity.this, TermList.class);
        startActivity(termViewIntent);
    }
}