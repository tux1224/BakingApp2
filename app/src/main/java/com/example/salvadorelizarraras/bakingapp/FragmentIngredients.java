package com.example.salvadorelizarraras.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.salvadorelizarraras.bakingapp.Recipe.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by Salvador Elizarraras on 09/03/2018.
 */

public class FragmentIngredients extends Fragment implements AdapterIngredients.Listener{

    public static  final String TAG = FragmentIngredients.class.getSimpleName();
    @BindView(R.id.ingredients_recycler)
    public RecyclerView mRecycler;
    private ArrayList<Ingredient> mData;
    AdapterIngredients mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)

    {
        View view = inflater.inflate(R.layout.ingredients_fragment, container, false);
        ButterKnife.bind(this,view);
        Bundle bundle = getArguments();
        mData = bundle.getParcelableArrayList("ingredients");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mAdapter = new AdapterIngredients();
        mAdapter.setListener(this);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter.setData(mData);
    }

    @Override
    public void onClick(View view){
        Log.d(TAG, "onClick: ");
    }
}
