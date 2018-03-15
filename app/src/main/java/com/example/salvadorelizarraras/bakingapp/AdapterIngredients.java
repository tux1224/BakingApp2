package com.example.salvadorelizarraras.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.salvadorelizarraras.bakingapp.Recipe.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by Salvador Elizarraras on 14/03/2018.
 */

public class AdapterIngredients extends RecyclerView.Adapter<AdapterIngredients.MyViewHolder> implements View.OnClickListener  {

    private  ArrayList<Ingredient> mData;
    private Listener listener;
    private Context context;

    public void setData(ArrayList<Ingredient> mData){
        if(mData == null) {
            return;
        }

        this.mData = mData;
        this.notifyDataSetChanged();
    }
    public void setListener(Listener listener){
        this.listener = listener;
    }

    @Override
    public AdapterIngredients.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_ingredient,parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvQuantity.setText(String.valueOf(mData.get(position).getQuantity()));
        holder.tvMeasure.setText(mData.get(position).getMeasure());
        holder.tvIngredient.setText(mData.get(position).getIngredient());

    }


    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }


    public Ingredient getItem(int position){
        return mData.get(position);
    }
    @Override
    public void onClick(View view) {
        listener.onClick(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvQuantity)
        public TextView tvQuantity;
        @BindView(R.id.tvMeasure)
        public TextView tvMeasure;
        @BindView(R.id.tvIngredient)
        public TextView tvIngredient;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);


        }
    }
    interface Listener{
        void onClick(View view);
    }
}
