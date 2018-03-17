package com.example.salvadorelizarraras.bakingapp;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.icu.util.UniversalTimeScale;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Salvador Elizarraras on 11/03/2018.
 */

public class RecipeDetail extends Fragment implements AdapterSteps.Listeners {

    private static Fragment fragmentVideo;
    public static final String TAG = RecipeDetail.class.getSimpleName();
    private Recipe mRecipe;
    public static String fragmentName = "";
    @BindView(R.id.mRecycler_ingredients)
    RecyclerView mRecicler;
    private AdapterSteps mAdapter;
    private int mOrientation;
    private static String configurationChanged;
    private MainActivity mainActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        mainActivity = (MainActivity) getActivity();


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
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
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putInt("orientation",mOrientation);
        outState.putString("fragment",fragmentName);
        outState.putString("changed",configurationChanged);
        super.onSaveInstanceState(outState);
    }



    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, fragmentName +" Restore "+ mOrientation);

        if(savedInstanceState != null && savedInstanceState.containsKey("orientation")){
            Log.d(TAG, "onViewStateRestored: ");
            mOrientation = savedInstanceState.getInt("orientation");
            fragmentName = savedInstanceState.getString("fragment","");
            configurationChanged = savedInstanceState.getString("changed","");
            


                switch (getResources().getConfiguration().orientation) {

                    case Configuration.ORIENTATION_PORTRAIT:
                        Log.d(TAG, "onViewStateRestored: IS PORTRAIIT");
                        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE && !fragmentName.isEmpty() && Utils.isTablet(getContext())) {
                            Log.d(TAG, "onViewStateRestored: was LANDSCAPE");
                            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(fragmentName)).commit();
                        }

                        if (mOrientation == Configuration.ORIENTATION_PORTRAIT && !fragmentName.isEmpty() && Utils.isTablet(getContext())) {
                            Log.d(TAG, "onViewStateRestored: was PORTRAIIT");
                            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(fragmentName)).commit();
                        }
                        break;

                }
            }

    }

    @Override
    public void onClick(View view) {

        int id = ((int) view.getTag() - 1);
        Log.d(TAG, "onClick: "+id);

        Bundle bundle = new Bundle();
        configurationChanged = "";


        if (id == -1) {
            Fragment fragment;
            fragment = new FragmentIngredients();
            bundle.putParcelableArrayList("ingredients", mRecipe.getIngredients());
            fragment.setArguments(bundle);
            if(Utils.isTablet(getContext())) {
                if (fragmentName.equals(FragmentsStepDetailView.TAG)) {
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.step_view_container)).commit();
                }
                fragmentName = FragmentIngredients.TAG;
                getFragmentManager().beginTransaction().replace(R.id.main_container, fragment, FragmentIngredients.TAG).addToBackStack(FragmentIngredients.TAG).commit();

            }else{
                getFragmentManager().beginTransaction().replace(R.id.main_container, fragment, FragmentIngredients.TAG).addToBackStack(FragmentIngredients.TAG).commit();
            }

        } else {

            fragmentName = FragmentsStepDetailView.TAG;
            fragmentVideo = new FragmentsStepDetailView();
            bundle = new Bundle();
            bundle.putParcelableArrayList("steps", mRecipe.getSteps());
            bundle.putInt("position", id);
            fragmentVideo.setArguments(bundle);

            if (Utils.isTablet(getContext())) {
                Log.d(TAG, "Landscape and tablet");
                configurationChanged = "changed";
                fragmentName = FragmentsStepDetailView.TAG;

                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

                    getFragmentManager().beginTransaction().replace(R.id.main_container, fragmentVideo, FragmentsStepDetailView.TAG).addToBackStack(FragmentsStepDetailView.TAG).commit();

                }else {
                    getFragmentManager().beginTransaction().replace(R.id.step_view_container, fragmentVideo, FragmentsStepDetailView.TAG).commit();
                }
            } else {
                Log.d(TAG, "Landscape and phone");
                getFragmentManager().beginTransaction().replace(R.id.main_container, fragmentVideo, FragmentsStepDetailView.TAG).addToBackStack(FragmentsStepDetailView.TAG).commit();
                fragmentName = "";
            }

            mOrientation = getResources().getConfiguration().orientation;
        }
    }

    //#regionlifecycle
    @Override
    public void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();

    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");


    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: ");
        super.onDetach();
    }
    //#endregion

}

