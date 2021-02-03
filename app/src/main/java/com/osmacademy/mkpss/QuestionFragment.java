package com.osmacademy.mkpss;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;
import com.osmacademy.mkpss.Adapter.QuestionFragmentAdapter;
import com.osmacademy.mkpss.Adapter.TestlerAdapter;
import com.osmacademy.mkpss.Common.Common;
import com.osmacademy.mkpss.DBHelper.DBHelper;
import com.osmacademy.mkpss.Interface.IQuestion;
import com.osmacademy.mkpss.Model.CurrentQuestion;
import com.osmacademy.mkpss.Model.Question;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment implements IQuestion {

    private TextView txt_question;
    private RadioButton answerA,answerB,answerC,answerD,answerE;
    private FrameLayout layout_question;

    private Question question;
    private int questionIndex=-1;




    public QuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =  inflater.inflate(R.layout.fragment_question, container, false);

        //get question
        questionIndex = getArguments().getInt("index",-1);
        question = Common.questionList.get(questionIndex);

        if (question !=null) {
            //View
            txt_question = itemView.findViewById(R.id.txt_question);

            layout_question = itemView.findViewById(R.id.layout_question);

            answerA = itemView.findViewById(R.id.answerA);
            answerB = itemView.findViewById(R.id.answerB);
            answerC = itemView.findViewById(R.id.answerC);
            answerD = itemView.findViewById(R.id.answerD);
            answerE = itemView.findViewById(R.id.answerE);

            txt_question.setText(question.getQuestion());

            answerA.setText(question.getAnswerA());
            answerB.setText(question.getAnswerB());
            answerC.setText(question.getAnswerC());
            answerD.setText(question.getAnswerD());
            answerE.setText(question.getAnswerE());

            answerA.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        Common.selected_values.add(answerA.getText().toString());
                    else
                        Common.selected_values.remove(answerA.getText().toString());
                }
            });



            answerB.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        Common.selected_values.add(answerB.getText().toString());
                    else
                        Common.selected_values.remove(answerB.getText().toString());
                }
            });

            answerC.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        Common.selected_values.add(answerC.getText().toString());
                    else
                        Common.selected_values.remove(answerC.getText().toString());
                }
            });

            answerD.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        Common.selected_values.add(answerD.getText().toString());
                    else
                        Common.selected_values.remove(answerD.getText().toString());
                }
            });

            answerE.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        Common.selected_values.add(answerE.getText().toString());
                    else
                        Common.selected_values.remove(answerE.getText().toString());
                }
            });

        }

        return itemView;
    }

    @Override
    public CurrentQuestion getSelectedAnswer() {
        //this function will return state of answer
        //right,wrong and normal
        CurrentQuestion currentQuestion = new CurrentQuestion(questionIndex, Common.ANSWER_TYPE.NO_ANSWER); //DEFAULT:NO ANSWER
        StringBuilder result = new StringBuilder();
        if (Common.selected_values.size()==1){
            Object[] arrayAnswer = Common.selected_values.toArray();
            result.append((String) arrayAnswer[0]).substring(0,1);
        }

        if (question !=null){

            //compare correct answer and user answer
            if (!TextUtils.isEmpty(result)){
                if (String.valueOf(result.charAt(0)).equals(question.getCorrectAnswer())){
                    currentQuestion.setType(Common.ANSWER_TYPE.RIGHT_ANSWER);
                }else {
                    currentQuestion.setType(Common.ANSWER_TYPE.WRONG_ANSWER);
                }
            }else {
                currentQuestion.setType(Common.ANSWER_TYPE.NO_ANSWER);
            }
        }else {
            Toast.makeText(getContext(),"Cannot get question",Toast.LENGTH_SHORT).show();
            currentQuestion.setType(Common.ANSWER_TYPE.NO_ANSWER);
        }
        Common.selected_values.clear();
        return currentQuestion;
    }

    @Override
    public void showCorrectAnswer()  {

        //bold correct answer
        String[] correctAnswer = question.getCorrectAnswer().split(",");
        for (String answer: correctAnswer){
            if (answer.equals("A")){
                answerA.setTypeface(null,Typeface.BOLD);
                answerA.setTextColor(Color.RED);
            }
            else if (answer.equals("B")){
                answerB.setTypeface(null,Typeface.BOLD);
                answerA.setTextColor(Color.RED);
            }
            else if (answer.equals("C")){
                answerC.setTypeface(null,Typeface.BOLD);
                answerA.setTextColor(Color.RED);
            }
            else if (answer.equals("D")){
                answerD.setTypeface(null,Typeface.BOLD);
                answerA.setTextColor(Color.RED);
            }
            else {
                answerE.setTypeface(null,Typeface.BOLD);
                answerA.setTextColor(Color.RED);
            }
        }

    }

    @Override
    public void disableAnswer() {

        answerA.setEnabled(false);
        answerB.setEnabled(false);
        answerC.setEnabled(false);
        answerD.setEnabled(false);
        answerE.setEnabled(false);

    }

    @Override
    public void resetQuestion() {

        answerA.setEnabled(true);
        answerB.setEnabled(true);
        answerC.setEnabled(true);
        answerD.setEnabled(true);
        answerE.setEnabled(true);


        answerA.setTypeface(null, Typeface.NORMAL);
        answerB.setTypeface(null, Typeface.NORMAL);
        answerC.setTypeface(null, Typeface.NORMAL);
        answerD.setTypeface(null, Typeface.NORMAL);
        answerE.setTypeface(null, Typeface.NORMAL);


    }
}
