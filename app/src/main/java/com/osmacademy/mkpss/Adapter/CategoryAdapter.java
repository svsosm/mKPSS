package com.osmacademy.mkpss.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.osmacademy.mkpss.Common.Common;
import com.osmacademy.mkpss.Model.Category;
import com.osmacademy.mkpss.R;
import com.osmacademy.mkpss.TestlerActivity;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

     Context context;
     List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_category_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        myViewHolder.txt_category_name.setText(categories.get(position).getName());

        if (categories.get(position).getId()==1){
            myViewHolder.img_category.setImageResource(R.drawable.cikmis);
        }
        if (categories.get(position).getId()==2){
            myViewHolder.img_category.setImageResource(R.drawable.turkce);
        }
        if (categories.get(position).getId()==3){
            myViewHolder.img_category.setImageResource(R.drawable.matematik);
        }
        if (categories.get(position).getId()==4){
            myViewHolder.img_category.setImageResource(R.drawable.cografya);
        }
        if (categories.get(position).getId()==5){
            myViewHolder.img_category.setImageResource(R.drawable.vatandaslik);
        }
        if (categories.get(position).getId()==6){
            myViewHolder.img_category.setImageResource(R.drawable.tarih);
        }
        if (categories.get(position).getId()==7){
            myViewHolder.img_category.setImageResource(R.drawable.alantestleri);
        }
        if (categories.get(position).getId()==8){
            myViewHolder.img_category.setImageResource(R.drawable.kpsshk);
        }

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img_category;
         CardView card_category;
         TextView txt_category_name;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            img_category = itemView.findViewById(R.id.img_category_image);
            card_category = itemView.findViewById(R.id.card_category);
            txt_category_name = itemView.findViewById(R.id.txt_category_name);
            card_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.selectedCategory = categories.get(getAdapterPosition());  //assign current category
                    Intent intent = new Intent(context, TestlerActivity.class);
                    context.startActivity(intent);

                }
            });
        }
    }
}
