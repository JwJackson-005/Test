package com.example.c196_pa.UI;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.c196_pa.Entities.Term;
import com.example.c196_pa.R;

import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {
    class TermViewHolder extends RecyclerView.ViewHolder{
        private final TextView termItemView;
        private final TextView termItemView2;
        private static final String TAG = "TermViewHolder";

        private TermViewHolder(View itemView){
            super(itemView);
            termItemView = itemView.findViewById(R.id.termButton);
            termItemView2 = itemView.findViewById(R.id.termButton2);
            Log.d(TAG, "TermViewHolder: termItemxView1: " +termItemView.getId() + " termItemView2: " + termItemView2.getId());
            termItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "TermViewHolder: First Button Clicked");
                    int position = getAdapterPosition();
                    final Term current = allTerms.get(position);
                    Intent courseListIntent = new Intent(context, CourseList.class);
                    courseListIntent.putExtra("termId", current.getTermID());
                    context.startActivity(courseListIntent);
                }
            });
            termItemView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "TermViewHolder: Second Button Clicked");
                    int position = getAdapterPosition();
                    final Term current = allTerms.get(position);
                    Intent termDetailsIntent = new Intent(context, TermDetails.class);
                    termDetailsIntent.putExtra("termId", current.getTermID());
                    context.startActivity(termDetailsIntent);
                }
            });
        }
    }
    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<Term> allTerms;
    private static final String TAG = "TermAdapter";

    public TermAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public TermViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Called");
        View itemView = layoutInflater.inflate(R.layout.term_list_item, parent, false);
        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TermViewHolder holder, int position) {

        if (allTerms != null) {
            final Term current = allTerms.get(position);
            holder.termItemView.setText(current.getTermTitle());
            holder.termItemView2.setText(". . .");

        } else {
            // Covers the case of data not being ready yet.
            holder.termItemView.setText("No Word");
            holder.termItemView2.setText("No Word");
        }
    }

    @Override
    public int getItemCount() {
        if (allTerms != null)
            return allTerms.size();
        else return 0;
    }


    public void setTerms(List<Term> words) {
        allTerms = words;
        notifyDataSetChanged();
    }

}
