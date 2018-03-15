package com.example.salvadorelizarraras.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.salvadorelizarraras.bakingapp.Recipe.Steps;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Salvador Elizarraras on 09/03/2018.
 */

public class AdapterSteps extends RecyclerView.Adapter<AdapterSteps.MyViewHolder> {

    private final int INGREDIENTS = 101;
    private final int STEPS = 102;

    private Listeners listener;
    private ArrayList<Steps> mData;
    public void setListener(Listeners listener){
        this.listener = listener;
    }

    public void setData(ArrayList<Steps> mData){
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = (INGREDIENTS == viewType) ?
                inflater.inflate(R.layout.ingredient_item, parent,false) :
                inflater.inflate(R.layout.step_item, parent,false);
                view.setTag(viewType);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });

        Log.d(TAG, "onBindViewHolder() returned: " + holder.getItemViewType());

        switch (holder.getItemViewType()){

            case INGREDIENTS:
                holder.textView.setText("Recipe Ingredients");

                break;
            case STEPS:
                holder.textView.setText(mData.get(position-1).getShortDescription());
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return INGREDIENTS;
        }else {
            return STEPS;
        }
    }

    @Override
    public int getItemCount() {

        return (mData == null) ? 0 : mData.size() + 1;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            int id = (int) itemView.getTag();
            textView = (TextView) itemView.findViewById((id == INGREDIENTS) ? R.id.mTvIngredinet : R.id.mTvSteps);

        }
    }

    public interface Listeners{

        void onClick(View view);
    }

}
