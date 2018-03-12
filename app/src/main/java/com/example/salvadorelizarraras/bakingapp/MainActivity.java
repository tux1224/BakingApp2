package com.example.salvadorelizarraras.bakingapp;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FragmentsStepDetailView listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            fragment = new FragmentHome();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction().add(R.id.main_container, fragment, fragment.getTag()).addToBackStack(fragment.getTag());
            fragmentTransaction.commit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBackPressed() {

            for (android.app.Fragment fragment : getFragmentManager().getFragments()) {
                if(fragment.getClass().getSimpleName() == FragmentsStepDetailView.class.getSimpleName()){

                }
            }

        super.onBackPressed();
    }

    public interface Listener{

        void  SetData();
    }
}