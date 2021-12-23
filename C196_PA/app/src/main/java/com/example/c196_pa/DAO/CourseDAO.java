package com.example.c196_pa.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.c196_pa.Entities.Assessment;
import com.example.c196_pa.Entities.Course;

import java.util.List;

@Dao
public interface CourseDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("DELETE FROM courses WHERE termId = :termId")
    void deleteTermCourses(int termId);

    @Query("SELECT * FROM courses WHERE termId = :termId")
    List<Course> getAllByTerm(int termId);

    @Query("SELECT * FROM courses ORDER BY courseId ASC")
    List<Course> getAllCourses();
}
