package com.example.c196_pa.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.c196_pa.Database.Repository;
import com.example.c196_pa.Entities.Course;
import com.example.c196_pa.Entities.Term;
import com.example.c196_pa.R;

import java.util.ArrayList;
import java.util.List;

public class TermList extends AppCompatActivity {

    private Repository repository;
    private RecyclerView recyclerView;
    private List<Term> allTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        repository = new Repository(getApplication());
        recyclerView = findViewById(R.id.termsRecyclerView);

        refreshList();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_term_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.refresh:
                refreshList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshList() {

        final TermAdapter termAdapter =new TermAdapter(this);
        recyclerView.setAdapter(termAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        allTerms = repository.getAllTerms();
        termAdapter.setTerms(allTerms);
    }


    public void newTermScreen(View view) {
        Intent courseScreenIntent = new Intent(TermList.this, TermDetails.class);
        startActivity(courseScreenIntent);
    }
}