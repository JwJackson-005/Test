package com.example.c196_pa.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int courseId;
    private String courseTitle;
    private String startDate;
    private String endDate;
    private String status; //TODO: make enum? for this
    private String instructorName; //TODO Maybe make instructor database?
    private String instructorPhone;
    private String instructorEmail;
    private String optionalNotes;
    private int termId;


    public Course(int courseId, String courseTitle, String startDate, String endDate, String status,
                  String instructorName, String instructorPhone, String instructorEmail, int termId) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.instructorName = instructorName;
        this.instructorPhone = instructorPhone;
        this.instructorEmail = instructorEmail;
        this.termId = termId;
    }

    @Override
    public String toString() {
        return "Course{" + "courseTitle='" + courseTitle + '}';
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorPhone() {
        return instructorPhone;
    }

    public void setInstructorPhone(String instructorPhone) {
        this.instructorPhone = instructorPhone;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getOptionalNotes() {
        return optionalNotes;
    }

    public void setOptionalNotes(String optionalNotes) {
        this.optionalNotes = optionalNotes;
    }
}
