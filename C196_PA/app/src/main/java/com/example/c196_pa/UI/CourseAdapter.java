package com.example.c196_pa.UI;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.c196_pa.Entities.Course;
import com.example.c196_pa.Entities.Term;
import com.example.c196_pa.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseItemView;
        private final TextView courseItemView2;

        private CourseViewHolder(View itemView) {
            super(itemView);
            courseItemView = itemView.findViewById(R.id.courseButton1);
            courseItemView2 = itemView.findViewById(R.id.courseButton2);
            courseItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Course current = mCourses.get(position);
                    Intent intent = new Intent(context, AssessmentList.class);
                    intent.putExtra("courseId",current.getCourseId());
                    intent.putExtra("termId", current.getTermId());
                    context.startActivity(intent);
                }
            });
            courseItemView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Course current = mCourses.get(position);
                    Intent courseDetailsIntent = new Intent(context, CourseDetails.class);
                    courseDetailsIntent.putExtra("courseId", current.getCourseId());
                    courseDetailsIntent.putExtra("termId", current.getTermId());
                    context.startActivity(courseDetailsIntent);
                }
            });
        }

    }

    private final LayoutInflater mInflater;
    private final Context context;
    private List<Course> mCourses; // Cached copy of words

    public CourseAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.course_list_item, parent, false);

        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        if (mCourses != null) {
            Course current = mCourses.get(position);
            holder.courseItemView.setText(current.getCourseTitle());
            holder.courseItemView2.setText(". . .");
        } else {
            // Covers the case of data not being ready yet.
            holder.courseItemView.setText("No Word");
            holder.courseItemView2.setText("No Word");
        }

    }

    public void setCourses(List<Course> words) {
        mCourses = words;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mCourses != null)
            return mCourses.size();
        else return 0;
    }
}
