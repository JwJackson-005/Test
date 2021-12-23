package com.example.c196_pa.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.c196_pa.Database.Repository;
import com.example.c196_pa.Entities.Assessment;
import com.example.c196_pa.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AssessmentDetails extends AppCompatActivity {

    EditText editAssessmentTitle;
    Spinner typeSpinner;
    TextView editAssessmentDate;
    int courseId;
    int assessmentId;
    Assessment currentAssessment;
    List<Assessment> allAssessments;
    Repository repository;
    Calendar assessmentCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener  assessmentDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        courseId = getIntent().getIntExtra("courseId", -1);
        assessmentId = getIntent().getIntExtra("assessmentId", -1);

        editAssessmentTitle = findViewById(R.id.editAssessmentTitle);
        typeSpinner = findViewById(R.id.assessmentSpinner);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.assessment_array,
                android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        editAssessmentDate = findViewById(R.id.editAssessmentDate);

        repository = new Repository(getApplication());
        allAssessments = repository.getAllAssessments();

        if (assessmentId != -1) {
            for (Assessment assessment : allAssessments) {
                if (assessment.getAssessmentId() == assessmentId)
                    currentAssessment = assessment;
            }
        }

        if (currentAssessment != null) {
            editAssessmentTitle.setText(currentAssessment.getAssessmentTitle());
            for (int i = 0; i < typeSpinner.getAdapter().getCount(); i++) {
                if (typeSpinner.getItemAtPosition(i).toString().compareTo(currentAssessment.getAssessmentType()) == 0) {
                    typeSpinner.setSelection(i);
                    break;
                }
            }
            editAssessmentDate.setText(currentAssessment.getAssessmentDate());
            setTitle(currentAssessment.getAssessmentTitle() + " Details");
        }
        else
            setTitle("New Assessment");

        assessmentDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                assessmentCalendar.set(Calendar.YEAR, year);
                assessmentCalendar.set(Calendar.MONTH, month);
                assessmentCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                updateAssessmentDate();
            }
        };

        editAssessmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AssessmentDetails.this, assessmentDateListener, assessmentCalendar
                        .get(Calendar.YEAR), assessmentCalendar.get(Calendar.MONTH),
                        assessmentCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_assessment_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.delete:
                if (currentAssessment != null) {
                    repository.delete(currentAssessment);
                    Toast.makeText(getApplicationContext(), "Assessment deleted", Toast.LENGTH_LONG).show();
                    this.finish();
                    return true;
                }
                else {
                    this.finish();
                    return true;
                }
            case R.id.assessmentNotification:
                createNotification(editAssessmentDate, editAssessmentTitle.getText().toString() + " is today!");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNotification(TextView dateView, String message) {
        String startDate = dateView.getText().toString();
        String format = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        Date newDate = null;

        try {
            newDate = sdf.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Long trigger = newDate.getTime();
        Intent notificationIntent = new Intent(AssessmentDetails.this, MyReceiver.class);
        notificationIntent.putExtra("key", message);
        PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetails.this, ++MainActivity.numAlert, notificationIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
    }

    private void updateAssessmentDate() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editAssessmentDate.setText(sdf.format(assessmentCalendar.getTime()));
    }

    public void addAssessment(View view) {
        if(assessmentId == -1){ // new Assessment
            assessmentId = allAssessments.get(allAssessments.size()-1).getAssessmentId();
            Assessment assessment = new Assessment(++assessmentId,editAssessmentTitle.getText().toString(), editAssessmentDate.getText().toString(),
                    typeSpinner.getSelectedItem().toString(), courseId);
            repository.insert(assessment);
        }
        else { // updating a assessment
            Assessment assessment = new Assessment(assessmentId, editAssessmentTitle.getText().toString(), editAssessmentDate.getText().toString(),
                    typeSpinner.getSelectedItem().toString(), courseId);
            repository.update(assessment);
        }
        this.finish();
    }
}