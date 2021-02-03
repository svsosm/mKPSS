package com.osmacademy.mkpss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.osmacademy.mkpss.Adapter.AnswerSheetAdapter;
import com.osmacademy.mkpss.Adapter.QuestionFragmentAdapter;
import com.osmacademy.mkpss.Common.Common;
import com.osmacademy.mkpss.DBHelper.DBHelper;
import com.osmacademy.mkpss.Model.CurrentQuestion;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity {

    private static final int CODE_GET_RESULT = 9999;
    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView answer_sheet_view;
    private AnswerSheetAdapter answerSheetAdapter;
    private TextView txt_right_answer,txt_wrong_answer;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private boolean isAnswerModeView;
    private int döngü =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //TOOLBAR AYARLARI
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(Common.selectedCategory.getName()+" Testi");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuestionActivity.this,TestlerActivity.class);
                startActivity(i);
                finish();
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //take question from DB
        takeQuestionFromDB();

        if (Common.questionList.size() >0) {

            //show text view right answer
            txt_right_answer = findViewById(R.id.txt_question_right);

            txt_right_answer.setVisibility(View.VISIBLE);

            txt_right_answer.setText(new StringBuilder(String.format("%d/%d",Common.right_answer_count,Common.questionList.size())));



            //View
            answer_sheet_view = findViewById(R.id.grid_answer);
            answer_sheet_view.setHasFixedSize(true);
            if (Common.questionList.size() > 5) {
                answer_sheet_view.setLayoutManager(new GridLayoutManager(this, Common.questionList.size() / 2));
            }
            answerSheetAdapter = new AnswerSheetAdapter(this, Common.answerSheetList);
            answer_sheet_view.setAdapter(answerSheetAdapter);

            viewPager = findViewById(R.id.viewpager);
            viewPager.setOffscreenPageLimit(Common.questionList.size());
            tabLayout = findViewById(R.id.sliding_tabs);
            
            genFragmentList();

            QuestionFragmentAdapter questionFragmentAdapter = new QuestionFragmentAdapter(getSupportFragmentManager(),
            this,
                    Common.fragmentsList);

            viewPager.setAdapter(questionFragmentAdapter);

            tabLayout.setupWithViewPager(viewPager);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                int SCROLLING_RIGHT =0;
                int SCROLLING_LEFT =1;
                int SCROLLING_UNDETERMINED =2;

                int currentScrollDirection = 2;

                private void setScrollingDirection(float positionOffset){
                    if ((1-positionOffset)>= 0.5){
                        this.currentScrollDirection = SCROLLING_RIGHT;
                    }else if ((1-positionOffset)<= 0.5){
                        this.currentScrollDirection = SCROLLING_LEFT;
                    }
                }


                private boolean isScrollDirectionUndetermined(){
                    return currentScrollDirection == SCROLLING_UNDETERMINED;
                }

                private boolean isScrollingRight(){
                    return currentScrollDirection == SCROLLING_RIGHT;
                }

                private boolean isScrollingLeft(){
                    return currentScrollDirection == SCROLLING_LEFT;
                }

                @Override
                public void onPageScrolled(int i, float v, int i1) {
                    if (isScrollDirectionUndetermined())
                        setScrollingDirection(v);
                }

                @Override
                public void onPageSelected(int i) {
                    QuestionFragment questionFragment;
                    int position=0;
                    if (i>0){
                        if (isScrollingRight()) {
                            //if user scrolling right, get previous fragment to calculate result
                            questionFragment = Common.fragmentsList.get(i - 1);
                            position = i - 1;

                        }else if (isScrollingLeft()){
//                            if user scrolling left, get next fragment to calculate result
                            questionFragment=Common.fragmentsList.get(i+1);
                            position = i+1;
                        }else {
                            questionFragment = Common.fragmentsList.get(position);
                        }
                    }else {
                        questionFragment = Common.fragmentsList.get(0);
                        position=0;
                    }

                    //if you want to show correct answer
                    CurrentQuestion question_state = questionFragment.getSelectedAnswer();
                    Common.answerSheetList.set(position,question_state); //set question answer
                    if (döngü ==0){
                        answerSheetAdapter.notifyDataSetChanged();  //change color in answersheet
                    }

                    countCorrectAnswer();

                    txt_right_answer.setText(new StringBuilder(String.format("%d",Common.right_answer_count+Common.wrong_answer_count))
                    .append("/")
                    .append(String.format("%d",Common.questionList.size())).toString());

//                    if (question_state.getType()== Common.ANSWER_TYPE.NO_ANSWER){
//                        questionFragment.showCorrectAnswer();
//                        questionFragment.disableAnswer();
//                    }



                }

                @Override
                public void onPageScrollStateChanged(int i) {

                    if ( i ==ViewPager.SCROLL_STATE_IDLE){
                        this.currentScrollDirection = SCROLLING_UNDETERMINED;
                    }

                }
            });

        }

    }

    private void finishGame() {
        döngü =1;
        int position = viewPager.getCurrentItem();
        //if you want to show correct answer
        QuestionFragment questionFragment = Common.fragmentsList.get(position);
        CurrentQuestion question_state = questionFragment.getSelectedAnswer();
        Common.answerSheetList.set(position,question_state); //set question answer
//        answerSheetAdapter.notifyDataSetChanged();  //change color in answersheet

        countCorrectAnswer();

        txt_right_answer.setText(new StringBuilder(String.format("%d",Common.right_answer_count+Common.wrong_answer_count))
                .append("/")
                .append(String.format("%d",Common.questionList.size())).toString());

//                    if (question_state.getType()== Common.ANSWER_TYPE.NO_ANSWER){
//                        questionFragment.showCorrectAnswer();
//                        questionFragment.disableAnswer();
//                    }

        //intent result activity
        Intent i = new Intent(QuestionActivity.this,ResultActivity.class);
        Common.no_answer_count = Common.questionList.size()-(Common.right_answer_count+Common.wrong_answer_count);
        Common.data_question = new StringBuilder(new Gson().toJson(Common.answerSheetList));

        startActivityForResult(i,CODE_GET_RESULT);

    }

    private void countCorrectAnswer() {
        //reset variable
        Common.right_answer_count = Common.wrong_answer_count =0;
        for (CurrentQuestion item:Common.answerSheetList)
            if (item.getType() == Common.ANSWER_TYPE.RIGHT_ANSWER)
                Common.right_answer_count++;
            else if (item.getType() == Common.ANSWER_TYPE.WRONG_ANSWER)
                Common.wrong_answer_count++;
            else
                Common.no_answer_count++;
    }

    private void genFragmentList() {

        for (int i=0; i<Common.questionList.size();i++){
            Bundle bundle = new Bundle();
            bundle.putInt("index",i);
            QuestionFragment questionFragment = new QuestionFragment();
            questionFragment.setArguments(bundle);

            Common.fragmentsList.add(questionFragment);
        }
    }


    private void takeQuestionFromDB() {

        Common.questionList = DBHelper.getInstance(this).getAllQuestion(Common.selectedTestler.getId());
        if (Common.questionList.size() ==0){
            //if no question
            new MaterialStyledDialog.Builder(this)
                    .setTitle("Ooops")
                    .setIcon(R.drawable.ic_sentiment_very_dissatisfied_black_24dp)
                    .setDescription("En yakın zamanda yüklenecektir. Sabrınız için teşekkürler..")
                    .setPositiveText("TAMAM")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
        }else {

            if (Common.answerSheetList.size()>0){
                Common.answerSheetList.clear();
            }
            //gen answersheet item from question
            //30 question = 30 answer sheet item
            //1 question = 1 answer sheet item

            for (int i=0;i<Common.questionList.size();i++){
                Common.answerSheetList.add(new CurrentQuestion(i,Common.ANSWER_TYPE.NO_ANSWER));  //default all answer is no answer
            }
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_finish){
            if (!isAnswerModeView){
                new MaterialStyledDialog.Builder(this)
                        .setTitle("Tamamla")
                        .setIcon(R.drawable.ic_warning_24dp)
                        .setDescription("Testi bitirmek istediğine emin misin?")
                        .setNegativeText("Hayır")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("Evet")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                finishGame();
                            }
                        }).show();

            }else
                finishGame();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_view);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_GET_RESULT){
            if (resultCode == Activity.RESULT_OK){
                String action =data.getStringExtra("action");
                if (action == null || TextUtils.isEmpty(action)){
                    int questionNum = data.getIntExtra(Common.KEY_BACK_FROM_RESULT,-1);
                    viewPager.setCurrentItem(questionNum);

                    isAnswerModeView = true;

//                    txt_right_answer.setVisibility(View.GONE);
//                   txt_wrong_answer.setVisibility(View.GONE);
                }
            }
        }
    }
}
