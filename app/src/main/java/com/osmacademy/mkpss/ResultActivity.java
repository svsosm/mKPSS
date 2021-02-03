package com.osmacademy.mkpss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.osmacademy.mkpss.Adapter.ResultGridAdapter;
import com.osmacademy.mkpss.Common.Common;

public class ResultActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txt_right_answer,txt_result,txt_basari_yuzdesi;
    private Button btn_filter_total,btn_right_answer,btn_wrong_answer,btn_no_answer;
    RecyclerView recycler_result;

    ResultGridAdapter adapter,filtered_adapter;


    BroadcastReceiver backToQuestion = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().toString().equals(Common.KEY_BACK_FROM_RESULT)){
                int question = intent.getIntExtra(Common.KEY_BACK_FROM_RESULT,-1);
                goBackActivityWithQuestion(question);
            }
        }
    };

    private void goBackActivityWithQuestion(int question) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Common.KEY_BACK_FROM_RESULT,question);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(backToQuestion,new IntentFilter(Common.KEY_BACK_FROM_RESULT));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txt_result = findViewById(R.id.txt_result);
        txt_right_answer = findViewById(R.id.txt_right_answer);
        txt_basari_yuzdesi = findViewById(R.id.txt_basari_yuzdesi);
        btn_filter_total = findViewById(R.id.btn_filter_total);
        btn_no_answer = findViewById(R.id.btn_no_answer);
        btn_right_answer= findViewById(R.id.btn_right_answer);
        btn_wrong_answer = findViewById(R.id.btn_wrong_answer);

        recycler_result = findViewById(R.id.recycler_result);
        recycler_result.setHasFixedSize(true);
        recycler_result.setLayoutManager(new GridLayoutManager(this,3));

        // set adapter
        adapter = new ResultGridAdapter(this,Common.answerSheetList);
        recycler_result.setAdapter(adapter);

        txt_right_answer.setText(new StringBuilder(" ").append(Common.right_answer_count).append("/").append(Common.questionList.size()));

        btn_filter_total.setText(new StringBuilder(" ").append(Common.questionList.size()));
        btn_right_answer.setText(new StringBuilder(" ").append(Common.right_answer_count));
        btn_wrong_answer.setText(new StringBuilder(" ").append(Common.wrong_answer_count));
        btn_no_answer.setText(new StringBuilder(" ").append(Common.no_answer_count));

        //sonuç hesabu
        int percent = (Common.right_answer_count*100/Common.questionList.size());
        txt_basari_yuzdesi.setText(new StringBuilder(" ").append("%"+String.valueOf(percent)));
        if (percent > 80)
            txt_result.setText("ÇOK İYİ!");
        else if (percent > 60)
            txt_result.setText("İYİ!");
        else if (percent >40)
            txt_result.setText("ORTA!");
        else if (percent >20)
            txt_result.setText("KÖTÜ!");
        else
            txt_result.setText("ÇOK KÖTÜ!");

        //event filter
        btn_filter_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter == null){
                    adapter = new ResultGridAdapter(ResultActivity.this,Common.answerSheetList);
                    recycler_result.setAdapter(adapter);
                }else {
                    recycler_result.setAdapter(adapter);
                }
            }
        });

        btn_no_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.answerSheetListFiltered.clear();
                for (int i=0;i<Common.answerSheetList.size();i++){
                    if (Common.answerSheetList.get(i).getType() == Common.ANSWER_TYPE.NO_ANSWER){
                        Common.answerSheetListFiltered.add(Common.answerSheetList.get(i));
                    }
                    filtered_adapter = new ResultGridAdapter(ResultActivity.this,Common.answerSheetListFiltered);
                    recycler_result.setAdapter(filtered_adapter);
                }
            }
        });

        btn_wrong_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.answerSheetListFiltered.clear();
                for (int i=0;i<Common.answerSheetList.size();i++){
                    if (Common.answerSheetList.get(i).getType() == Common.ANSWER_TYPE.WRONG_ANSWER){
                        Common.answerSheetListFiltered.add(Common.answerSheetList.get(i));
                    }
                    filtered_adapter = new ResultGridAdapter(ResultActivity.this,Common.answerSheetListFiltered);
                    recycler_result.setAdapter(filtered_adapter);
                }
            }
        });

        btn_right_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.answerSheetListFiltered.clear();
                for (int i=0;i<Common.answerSheetList.size();i++){
                    if (Common.answerSheetList.get(i).getType() == Common.ANSWER_TYPE.RIGHT_ANSWER){
                        Common.answerSheetListFiltered.add(Common.answerSheetList.get(i));
                    }
                    filtered_adapter = new ResultGridAdapter(ResultActivity.this,Common.answerSheetListFiltered);
                    recycler_result.setAdapter(filtered_adapter);
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(),TestlerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //delete all activity
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
