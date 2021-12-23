package com.example.c196_pa.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.c196_pa.Entities.Course;
import com.example.c196_pa.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CourseDetails extends AppCompatActivity {

    private static final String TAG = "CourseDetails";
    Repository repository;
    List<Course> allCourses;
    List<Assessment> courseAssessments;
    int numAssessments;
    int courseId;
    int termId;
    EditText editCourseTitle;
    TextView editCourseStartDate;
    TextView editCourseEndDate;
    Spinner statusSpinner;
    EditText editInstructorName;
    EditText editInstructorPhone;
    EditText editInstructorEmail;
    TextView notesTextView;
    EditText editNotes;
    Course currentCourse;
    Calendar startCalendar=Calendar.getInstance();
    DatePickerDialog.OnDateSetListener  startDateListener;
    Calendar endCalendar=Calendar.getInstance();
    DatePickerDialog.OnDateSetListener  endDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        courseId = getIntent().getIntExtra("courseId", -1);
        termId = getIntent().getIntExtra("termId", -1);
        repository = new Repository(getApplication());
        allCourses = repository.getAllCourses();

        for (Course course : allCourses) {
            if (course.getCourseId() == courseId)
                currentCourse = course;
        }
        editCourseTitle = findViewById(R.id.editTitle);
        editCourseStartDate = findViewById(R.id.editStartDate);
        editCourseEndDate = findViewById(R.id.editEndDate);
        statusSpinner = findViewById(R.id.statusSpinner);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.status_array,
                android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
        editInstructorName = findViewById(R.id.editInstructorName);
        editInstructorPhone = findViewById(R.id.editInstructorPhone);
        editInstructorEmail = findViewById(R.id.editInstructorEmail);
        editNotes = findViewById(R.id.editNotes);
        notesTextView = findViewById(R.id.notesTextView);

        notesTextView.setEnabled(false);
        notesTextView.setVisibility(View.GONE);
        editNotes.setEnabled(false);
        editNotes.setVisibility(View.GONE);

        if (currentCourse != null) {
            editCourseTitle.setText(currentCourse.getCourseTitle());
            editCourseStartDate.setText(currentCourse.getStartDate());
            editCourseEndDate.setText(currentCourse.getEndDate());
            for (int i = 0; i < statusSpinner.getAdapter().getCount(); i++) {
                if (statusSpinner.getItemAtPosition(i).toString().compareTo(currentCourse.getStatus()) == 0) {
                    statusSpinner.setSelection(i);
                    break;
                }
            }
            Log.d(TAG, "onCreate: Spinner initialized");
            editInstructorName.setText(currentCourse.getInstructorName());
            editInstructorPhone.setText(currentCourse.getInstructorPhone());
            editInstructorEmail.setText(currentCourse.getInstructorEmail());
            editNotes.setText(currentCourse.getOptionalNotes());
            setTitle(editCourseTitle.getText());
        } else
            setTitle("New Course");

        courseAssessments = repository.getAllByCourse(courseId);
        if (courseAssessments != null)
            numAssessments = courseAssessments.size();
        else
            numAssessments = 0;

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

        editCourseStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CourseDetails.this, startDateListener, startCalendar
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

        editCourseEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CourseDetails.this, endDateListener, endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    private void updateStartDate() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editCourseStartDate.setText(sdf.format(startCalendar.getTime()));
    }

    private void updateEndDate() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editCourseEndDate.setText(sdf.format(endCalendar.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course_details, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if (editNotes.isEnabled()) {
            menu.findItem(R.id.showNotes).setVisible(false);
            menu.findItem(R.id.hideNotes).setVisible(true);
            return true;
        }
        else {
            menu.findItem(R.id.showNotes).setVisible(true);
            menu.findItem(R.id.hideNotes).setVisible(false);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.deleteCourse:
                if (numAssessments == 0 && currentCourse != null) {
                    repository.delete(currentCourse);
                    Toast.makeText(getApplicationContext(), "Course deleted", Toast.LENGTH_LONG).show();
                    this.finish();
                    return true;
                } else if (numAssessments > 0 && currentCourse != null) {
                    Toast.makeText(getApplicationContext(), "Can't delete a course with assigned assessments", Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), "Wasn't able to delete course", Toast.LENGTH_LONG).show();
                    this.finish();
                    return true;
                }
            case R.id.showNotes:
                notesTextView.setEnabled(true);
                notesTextView.setVisibility(View.VISIBLE);
                editNotes.setEnabled(true);
                editNotes.setVisibility(View.VISIBLE);
                return true;
            case R.id.hideNotes:
                notesTextView.setEnabled(false);
                notesTextView.setVisibility(View.GONE);
                editNotes.setEnabled(false);
                editNotes.setVisibility(View.GONE);
                return true;
            case R.id.shareNotes:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,  editNotes.getText().toString());
                sendIntent.putExtra(Intent.EXTRA_TITLE, "Share Notes");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            case R.id.startNotification:
                Log.d(TAG, "onOptionsItemSelected: Start Date Notification Selection");
                createNotification(editCourseStartDate, editCourseTitle.getText().toString() + " starts today!");
                return true;
            case R.id.endNotification:
                Log.d(TAG, "onOptionsItemSelected: End Date Notification Selection");
                createNotification(editCourseEndDate, editCourseTitle.getText().toString() + " ends today!");
                return true;

        }
        return false;
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
        Intent notificationIntent = new Intent(CourseDetails.this, MyReceiver.class);
        notificationIntent.putExtra("key", message);
        PendingIntent sender = PendingIntent.getBroadcast(CourseDetails.this, ++MainActivity.numAlert, notificationIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
    }

    public void saveCourse (View view) {

        if(courseId == -1){ // new Course
            courseId = allCourses.get(allCourses.size()-1).getCourseId();
            Course course = new Course(++courseId,editCourseTitle.getText().toString(), editCourseStartDate.getText().toString(),
                    editCourseEndDate.getText().toString(), statusSpinner.getSelectedItem().toString(), editInstructorName.getText().toString(),
                    editInstructorPhone.getText().toString(), editInstructorEmail.getText().toString(), termId);

            course.setOptionalNotes(editNotes.getText().toString());
            repository.insert(course);
        }
        else { // updating a course
            Course course = new Course(courseId,editCourseTitle.getText().toString(), editCourseStartDate.getText().toString(),
                    editCourseEndDate.getText().toString(), statusSpinner.getSelectedItem().toString(), editInstructorName.getText().toString(),
                    editInstructorPhone.getText().toString(), editInstructorEmail.getText().toString(), termId);

            course.setOptionalNotes(editNotes.getText().toString());
            repository.update(course);
        }
        this.finish();
    }
}