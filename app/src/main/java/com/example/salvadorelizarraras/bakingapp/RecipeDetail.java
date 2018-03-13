package com.example.salvadorelizarraras.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.salvadorelizarraras.bakingapp.Recipe.Recipe;
import com.google.android.exoplayer2.util.Util;

import java.lang.reflect.Array;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Salvador Elizarraras on 11/03/2018.
 */

public class RecipeDetail extends Fragment implements AdapterIngredients.Listeners {

    private Fragment fragment;
    private final String TAG = RecipeDetail.class.getSimpleName();
    private Recipe mRecipe;
    private static int algo = 0;
    @BindView(R.id.mRecycler_ingredients)
    RecyclerView mRecicler;
    private AdapterIngredients mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        algo =1;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_detail_view,container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        mRecipe = bundle.getParcelable("recipe");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecicler.setLayoutManager(layoutManager);
        mAdapter = new AdapterIngredients();
        mAdapter.setListener(this);
        mAdapter.setData(mRecipe.getSteps());
        mRecicler.setAdapter(mAdapter);


        return view;
    }



    @Override
    public void onClick(View view) {

        int id = ((int) view.getTag() -1);
        Fragment fragment;
        Bundle bundle;
        if(id == -1){
        fragment = new FragmentIngredients();

        }else{
            fragment = new FragmentsStepDetailView();
            bundle = new Bundle();
            bundle.putParcelableArrayList("steps", mRecipe.getSteps());
            bundle.putInt("position", id);
            fragment.setArguments(bundle);

            if(Utils.isTablet(getContext())){
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.step_view_container, fragment, fragment.getTag()).commit();
            }else {

                getFragmentManager().beginTransaction().replace(R.id.main_container, fragment, fragment.getTag()).addToBackStack(fragment.getTag()).commit();

            }

        }

    }


}
