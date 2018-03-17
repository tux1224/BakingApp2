package com.example.salvadorelizarraras.bakingapp;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FragmentsStepDetailView listener;
    private final String TAG = MainActivity.class.getSimpleName();
    protected MainActivity mainActivity = this;

    ArrayList<String> stack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            if(Utils.isTablet(getApplicationContext())){

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }

        if(savedInstanceState == null) {
            Log.d(TAG, "onCreate: "  );
            fragment = new FragmentHome();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction()
                    .replace(R.id.main_container, fragment, FragmentHome.TAG);
            fragmentTransaction.commit();
        }


    }


}
