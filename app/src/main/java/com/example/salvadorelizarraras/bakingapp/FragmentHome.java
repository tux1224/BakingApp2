package com.example.salvadorelizarraras.bakingapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.salvadorelizarraras.bakingapp.Recipe.Recipe;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Salvador Elizarraras on 11/03/2018.
 */

public class FragmentHome extends Fragment implements LoaderManager.LoaderCallbacks<String>, AdapterHome.Listeners{
    private String TAG = FragmentHome.class.getSimpleName();
    private int mPosition = 0;

    @BindView(R.id.m_recycler_home)
    RecyclerView mRecycler;
    @BindView(R.id.mLoading)
    ProgressBar mLoading;

    private AdapterHome adapterHome;
    private String BRING_DATA_URL = "bring_data_url";
    private int LOADER_CODE = 101;
    private int mScrollPosition;
    private static Parcelable savedRecyclerLayoutState;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container, false);
        ButterKnife.bind(this, view);
        // region recycler definition


        RecyclerView.LayoutManager linearLayout = !Utils.isTablet(getContext()) ?
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false):
                new GridLayoutManager(getContext(),3,LinearLayoutManager.VERTICAL,false);
        mRecycler.setLayoutManager(linearLayout);
        adapterHome = new AdapterHome();
        adapterHome.setListener(this);
        mRecycler.setAdapter(adapterHome);
        mRecycler.setHasFixedSize(true);
        // endregion
        Bundle bundle = new Bundle();
        bundle.putString(BRING_DATA_URL,Utils.mUriBase);

        LoaderManager loaderManager = getLoaderManager();
        Loader<String> searchLoader = loaderManager.getLoader(LOADER_CODE);

        if(searchLoader == null){

            loaderManager.initLoader(LOADER_CODE, bundle,this);

        }else{

            loaderManager.restartLoader(LOADER_CODE, bundle, this);
        }
        return view;
    }





    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(getContext()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args == null)
                    return;

                mLoading.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                String mUrl = Utils.mUriBase;
                if(mUrl == null || mUrl.isEmpty()){
                    return null;
                }
                try {
                    URL url = new URL(Utils.mUriBase);
                    String data = Utils.getResponseFromHttpUrl(url);
                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }



        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(data != null && !data.equals("")){
            mLoading.setVisibility(View.GONE);
            adapterHome.swapData(Utils.getDataFromFile(getContext(),data));

            if(savedRecyclerLayoutState != null)
                mRecycler.getLayoutManager().onRestoreInstanceState((savedRecyclerLayoutState));


        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        //utState.putParcelable("KeyForLayoutManagerState", mRecycler.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState() returned: " + savedInstanceState);

        if(savedInstanceState != null)
        {
            savedRecyclerLayoutState = savedInstanceState.getParcelable("KeyForLayoutManagerState");
        }
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick() returned: " );

        int id = Integer.parseInt( String.valueOf(view.getTag()));
        Recipe recipe = adapterHome.getItem(id);
        Fragment fragment = new RecipeDetail();

        Bundle bundle = new Bundle();
        bundle.putParcelable("recipe", recipe );
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.main_container,fragment,fragment.getTag()).addToBackStack(fragment.getTag()).commit();

    }
}