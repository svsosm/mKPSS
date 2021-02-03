package com.osmacademy.mkpss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.osmacademy.mkpss.Adapter.CategoryAdapter;
import com.osmacademy.mkpss.Adapter.TestlerAdapter;
import com.osmacademy.mkpss.Common.Common;
import com.osmacademy.mkpss.DBHelper.DBHelper;
import com.osmacademy.mkpss.Model.Category;

import java.util.List;

public class TestlerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recycler_testler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testler);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle(Common.selectedCategory.getName().toString());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TestlerActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        recycler_testler = findViewById(R.id.recycler_testler);
        recycler_testler.setHasFixedSize(false);
        recycler_testler.setLayoutManager(new GridLayoutManager(this,1));

        //GET ALL TESTLER
        TestlerAdapter adapter = new TestlerAdapter(TestlerActivity.this, DBHelper.getInstance(this).getAllTestler(Common.selectedCategory.getId()));
        recycler_testler.setAdapter(adapter);






    }
}
