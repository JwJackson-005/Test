package com.example.c196_pa.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.c196_pa.DAO.AssessmentDAO;
import com.example.c196_pa.DAO.Converters;
import com.example.c196_pa.DAO.CourseDAO;
import com.example.c196_pa.DAO.TermDAO;
import com.example.c196_pa.Entities.Assessment;
import com.example.c196_pa.Entities.Course;
import com.example.c196_pa.Entities.Term;

@Database(entities = {Term.class, Course.class, Assessment.class}, version = 4, exportSchema=false)
public abstract class DatabaseBuilder extends RoomDatabase {
    public abstract AssessmentDAO assessmentDAO();
    public abstract CourseDAO courseDAO();
    public abstract TermDAO termDAO();

    private static volatile DatabaseBuilder INSTANCE;

    static DatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseBuilder.class, "MyDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
