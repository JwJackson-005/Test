package com.example.c196_pa.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="assessments")
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int assessmentId;
    private String assessmentTitle;
    private String assessmentDate;
    private String assessmentType;
    private int courseId;

    public Assessment(int assessmentId, String assessmentTitle, String assessmentDate, String assessmentType, int courseId) {
        this.assessmentId = assessmentId;
        this.assessmentTitle = assessmentTitle;
        this.assessmentDate = assessmentDate;
        this.assessmentType = assessmentType;
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "assessmentTitle='" + assessmentTitle + '\'' +
                ", assessmentType='" + assessmentType + '\'' +
                '}';
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    public void setAssessmentTitle(String assessmentTitle) {
        this.assessmentTitle = assessmentTitle;
    }

    public String getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(String assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}