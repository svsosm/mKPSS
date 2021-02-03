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
import com.osmacademy.mkpss.Model.Testler;
import com.osmacademy.mkpss.QuestionActivity;
import com.osmacademy.mkpss.R;
import com.osmacademy.mkpss.TestlerActivity;

import java.util.List;

public class TestlerAdapter extends RecyclerView.Adapter<TestlerAdapter.MyViewHolder> {

    Context context;
    List<Testler> testler;

    public TestlerAdapter(Context context, List<Testler> testler) {
        this.context = context;
        this.testler = testler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_testler_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        myViewHolder.txt_testler.setText(testler.get(position).getName());




    }

    @Override
    public int getItemCount() {
        return testler.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CardView card_testler;
        TextView txt_testler;
        Button btn_testCoz;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            btn_testCoz = itemView.findViewById(R.id.btn_testCoz);
            card_testler = itemView.findViewById(R.id.card_testler);
            txt_testler = itemView.findViewById(R.id.txt_testler_name);
            btn_testCoz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.selectedTestler = testler.get(getAdapterPosition());  //assign current category
                    Intent intent = new Intent(context, QuestionActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
