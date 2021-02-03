package com.osmacademy.mkpss.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.osmacademy.mkpss.Common.Common;
import com.osmacademy.mkpss.Model.Category;
import com.osmacademy.mkpss.Model.CurrentQuestion;
import com.osmacademy.mkpss.Model.Testler;
import com.osmacademy.mkpss.QuestionActivity;
import com.osmacademy.mkpss.R;
import com.osmacademy.mkpss.TestlerActivity;

import java.util.List;

public class AnswerSheetAdapter extends RecyclerView.Adapter<AnswerSheetAdapter.MyViewHolder> {

    Context context;
    List<CurrentQuestion> currentQuestionList;

    public AnswerSheetAdapter(Context context, List<CurrentQuestion> currentQuestionList) {
        this.context = context;
        this.currentQuestionList = currentQuestionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_grid_answer_sheet_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        if (currentQuestionList.get(position).getType() == Common.ANSWER_TYPE.RIGHT_ANSWER){
            myViewHolder.view_answer.setBackgroundResource(R.drawable.grid_question_right_answer);
        }else if (currentQuestionList.get(position).getType() == Common.ANSWER_TYPE.WRONG_ANSWER){
            myViewHolder.view_answer.setBackgroundResource(R.drawable.grid_question_right_answer);
        }else {
            myViewHolder.view_answer.setBackgroundResource(R.drawable.grid_question_no_answer);
        }


    }

    @Override
    public int getItemCount() {
        return currentQuestionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        View view_answer;
        int döngü;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            view_answer = itemView.findViewById(R.id.question_item);
            döngü=0;

        }
    }
}
