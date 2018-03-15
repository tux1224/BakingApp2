package com.example.salvadorelizarraras.bakingapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.example.salvadorelizarraras.bakingapp.Recipe.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Salvador Elizarraras on 11/03/2018.
 */

public class RecipeDetail extends Fragment implements AdapterSteps.Listeners {

    private Fragment fragment;
    private final String TAG = RecipeDetail.class.getSimpleName();
    private Recipe mRecipe;

    @BindView(R.id.mRecycler_ingredients)
    RecyclerView mRecicler;
    private AdapterSteps mAdapter;
    private static String fragmentName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        mAdapter = new AdapterSteps();
        mAdapter.setListener(this);
        mAdapter.setData(mRecipe.getSteps());
        mRecicler.setAdapter(mAdapter);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState !=null && savedInstanceState.containsKey("fragment")){
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(fragmentName)).commitAllowingStateLoss();

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("fragment", fragment.getTag());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!= null && savedInstanceState.containsKey("fragment")){
            fragmentName = savedInstanceState.getString("fragment");
            Log.d(TAG, "onViewStateRestored: ");
        }
    }

    @Override
    public void onClick(View view) {

        int id = ((int) view.getTag() -1);
        Log.d(TAG, "onClick: "+ id);

        Bundle bundle = new Bundle();
        if(id == -1){
        fragment = new FragmentIngredients();
        bundle.putParcelableArrayList("ingredients",mRecipe.getIngredients());
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.main_container,fragment,fragment.getTag()).addToBackStack(fragment.getTag()).commit();

        }else{
            fragment = new FragmentsStepDetailView();
            bundle = new Bundle();
            bundle.putParcelableArrayList("steps", mRecipe.getSteps());
            bundle.putInt("position", id);
            fragment.setArguments(bundle);
            if(fragmentName != null){
            getFragmentManager().popBackStack();
            getFragmentManager().beginTransaction().replace(R.id.step_view_container,fragment,fragment.getTag()).addToBackStack(fragment.getTag()).commit();
            }
            if(Utils.isTablet(getContext()) && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

                if(fragmentName != null)
                getActivity().getSupportFragmentManager().popBackStack();


                getFragmentManager().beginTransaction().replace(R.id.step_view_container, fragment, fragment.getTag()).addToBackStack(fragment.getTag()).commit();

            }else if(!Utils.isTablet(getContext()) && getResources().getConfiguration().orientation  == Configuration.ORIENTATION_PORTRAIT) {

                getFragmentManager().beginTransaction().replace(R.id.main_container, fragment, fragment.getTag()).addToBackStack(fragment.getTag()).commit();

            }else if(!Utils.isTablet(getContext()) && getResources().getConfiguration().orientation  == Configuration.ORIENTATION_LANDSCAPE) {

                getFragmentManager().beginTransaction().replace(R.id.main_container, fragment, fragment.getTag()).addToBackStack(fragment.getTag()).commit();

            }

        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged: ");
    }
}
