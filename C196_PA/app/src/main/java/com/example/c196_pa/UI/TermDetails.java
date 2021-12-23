package com.example.c196_pa.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.c196_pa.Database.Repository;
import com.example.c196_pa.Entities.Course;
import com.example.c196_pa.Entities.Term;
import com.example.c196_pa.Entities.Term;
import com.example.c196_pa.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TermDetails extends AppCompatActivity {

    Repository repository;
    int termId;
    List<Term> allTerms;
    List<Course> termCourses;
    EditText editTermTitle;
    TextView editTermStartDate;
    TextView editTermEndDate;
    Term currentTerm;
    int numCourses;
    Calendar startCalendar=Calendar.getInstance();
    DatePickerDialog.OnDateSetListener  startDateListener;
    Calendar endCalendar=Calendar.getInstance();
    DatePickerDialog.OnDateSetListener  endDateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        termId = getIntent().getIntExtra("termId", -1);

        editTermTitle = findViewById(R.id.editTermTitle);
        editTermStartDate = findViewById(R.id.editTermStartDate);
        editTermEndDate = findViewById(R.id.editTermEndDate);

        repository = new Repository(getApplication());
        allTerms = repository.getAllTerms();

        if (termId != -1) {
            for (Term Term : allTerms) {
                if (Term.getTermID() == termId)
                    currentTerm = Term;
            }
        }

        if (currentTerm != null) {
            editTermTitle.setText(currentTerm.getTermTitle());
            editTermStartDate.setText(currentTerm.getStartDate());
            editTermEndDate.setText(currentTerm.getEndDate());
            setTitle(currentTerm.getTermTitle() + " Details");
        }
        else
            setTitle("New Term");

        termCourses = repository.getAllByTerm(termId);
        if (termCourses != null)
            numCourses = termCourses.size();
        else
            numCourses = 0;

        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, month);
                startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                updateStartDate();
            }
        };

        editTermStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TermDetails.this, startDateListener, startCalendar
                        .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, month);
                endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                updateEndDate();
            }
        };

        editTermEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TermDetails.this, endDateListener, endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateStartDate() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTermStartDate.setText(sdf.format(startCalendar.getTime()));
    }

    private void updateEndDate() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTermEndDate.setText(sdf.format(endCalendar.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_term_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.delete:
                if (numCourses == 0 && currentTerm != null) {
                    repository.delete(currentTerm);
                    Toast.makeText(getApplicationContext(), "Term deleted", Toast.LENGTH_LONG).show();
                    this.finish();
                }
                else if (numCourses > 0 && currentTerm != null) { //TODO: WILL NEED TO MAKE THIS Cascading delete?
                    Toast.makeText(getApplicationContext(), "Can't delete a term with courses", Toast.LENGTH_LONG).show();
                    return true;
                }
                else {
                    this.finish();
                    return true;
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addTerm(View view) {
        if(termId == -1){ // new Term
            termId = allTerms.get(allTerms.size()-1).getTermID();
            Term term = new Term(++termId,editTermTitle.getText().toString(), editTermStartDate.getText().toString(),
                    editTermEndDate.getText().toString());
            repository.insert(term);
        }
        else { // updating a term
            Term term = new Term(termId,editTermTitle.getText().toString(), editTermStartDate.getText().toString(),
                    editTermEndDate.getText().toString());
            repository.update(term);
        }
        Intent intent= new Intent(TermDetails.this, TermList.class);
        startActivity(intent);
    }
}