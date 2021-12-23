package com.example.c196_pa.Database;

import android.app.Application;

import com.example.c196_pa.DAO.AssessmentDAO;
import com.example.c196_pa.DAO.CourseDAO;
import com.example.c196_pa.DAO.TermDAO;
import com.example.c196_pa.Entities.Assessment;
import com.example.c196_pa.Entities.Course;
import com.example.c196_pa.Entities.Term;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Repository {
    private AssessmentDAO assessmentDAO;
    private CourseDAO courseDAO;
    private TermDAO termDAO;
    private List<Assessment> allAssessments;
    private List<Course> allCourses;
    private List<Term> allTerms;
    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        DatabaseBuilder db = DatabaseBuilder.getDatabase(application);
        assessmentDAO = db.assessmentDAO();
        courseDAO = db.courseDAO();
        termDAO = db.termDAO();
    }

    public List<Assessment> getAllByCourse(int courseId) {
        databaseWriteExecutor.execute(() -> {
            allAssessments = assessmentDAO.getAllByCourse(courseId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allAssessments;
    }
    public List<Assessment> getAllAssessments() {
        databaseWriteExecutor.execute(() -> {
            allAssessments = assessmentDAO.getAllAssessments();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allAssessments;
    }

    public List<Course> getAllCourses() {
        databaseWriteExecutor.execute(() -> {
            allCourses = courseDAO.getAllCourses();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allCourses;
    }

    public List<Course> getAllByTerm(int termId) {
        databaseWriteExecutor.execute(() -> {
            allCourses = courseDAO.getAllByTerm(termId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allCourses;
    }

    public List<Term> getAllTerms() {
        databaseWriteExecutor.execute(() -> {
            allTerms = termDAO.getAllTerms();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allTerms;
    }

    public void insert(Assessment assessment) {
        databaseWriteExecutor.execute(() -> {
            assessmentDAO.insert(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void insert(Course course) {
        databaseWriteExecutor.execute(() -> {
            courseDAO.insert(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insert(Term term) {
        databaseWriteExecutor.execute(() -> {
            termDAO.insert(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Assessment assessment) {
        databaseWriteExecutor.execute(() -> {
            assessmentDAO.update(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Course course) {
        databaseWriteExecutor.execute(() -> {
            courseDAO.update(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Term term) {
        databaseWriteExecutor.execute(() -> {
            termDAO.update(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void delete(Assessment assessment) {
        databaseWriteExecutor.execute(() -> {
            assessmentDAO.delete(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Course course) {
        databaseWriteExecutor.execute(() -> {
            courseDAO.delete(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Term term) {
        databaseWriteExecutor.execute(() -> {
            termDAO.delete(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
