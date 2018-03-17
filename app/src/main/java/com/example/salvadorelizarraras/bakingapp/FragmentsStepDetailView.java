package com.example.salvadorelizarraras.bakingapp;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.salvadorelizarraras.bakingapp.Recipe.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

/**
 * Created by Salvador Elizarraras on 11/03/2018.
 */

public class FragmentsStepDetailView extends Fragment implements ExoPlayer.EventListener, View.OnClickListener{

    private SimpleExoPlayer mExoPlayer;
    public final static String TAG = FragmentsStepDetailView.class.getSimpleName();
    private PlaybackStateCompat.Builder mStateBuilder;
    private long mPreviousPosition;
    private final String SESION = "remaining_video";


    Steps mStep;
    private Bundle mArguments;
    ArrayList<Steps> mSteps;
    TextView tvDescription;
    private int mPosition;
    ImageView left;
    ImageView right;
    SimpleExoPlayerView mPlayerView;
    FragmentManager fragmentManager;
    private Uri uriView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: ");


        View view       = inflater.inflate(R.layout.fragment_step_detail_view,container,false);
        tvDescription   = (TextView) view.findViewById(R.id.tv_description);
        left            = (ImageView) view.findViewById(R.id.left);
        right           = (ImageView) view.findViewById(R.id.right);
        mPlayerView     = (SimpleExoPlayerView) view.findViewById(R.id.player_view);
        left.setOnClickListener(this);
        right.setOnClickListener(this);

        if(getArguments() != null && getArguments().containsKey("steps")) {
            Log.d(TAG, "onCreateView: + Arguments" );
            mArguments = getArguments();
            mPosition = mArguments.getInt("position");
            mSteps = mArguments.getParcelableArrayList("steps");
            mStep = mSteps.get(mPosition);
            uriView = Uri.parse(mStep.getVideoURL());
            tvDescription.setText(mStep.getDescription());
            Log.d(TAG, "onCreateView: "+uriView);
        }



        if (savedInstanceState!= null) {
            Log.d(TAG, "onCreateView: ");
            mPreviousPosition = (savedInstanceState.getLong(SESION, 0));
            mPosition = savedInstanceState.getInt("position");
            mSteps = savedInstanceState.getParcelableArrayList("steps");
            mStep = mSteps.get(mPosition);
            uriView = Uri.parse(mStep.getVideoURL());
            tvDescription.setText(mStep.getDescription());

        }
        return view;
    }

     private void initializePlayer(Uri mediaUri) {

        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveVideoTrackSelection.Factory(bandwidthMeter));
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity().getApplicationContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
                    if(mediaUri.toString().isEmpty()) {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                               R.drawable.novideo);
                        mPlayerView.setDefaultArtwork(bitmap);
                        mPlayerView.setUseController(false);

                    }else {
                        // Set the ExoPlayer.EventListener to this activity.
                        mExoPlayer.addListener(this);

                        // Prepare the MediaSource.
                        mExoPlayer.seekTo(mPreviousPosition);
                        String userAgent = Util.getUserAgent(getContext(), "BakingApp2");
                        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                                getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
                        mExoPlayer.prepare(mediaSource);
                        mExoPlayer.setPlayWhenReady(true);
                    }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: "+ mExoPlayer.getCurrentPosition());
        outState.putLong(SESION, mExoPlayer.getCurrentPosition());
        outState.putParcelableArrayList("steps",mSteps);
        outState.putInt("position",mPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "onViewStateRestored: ");

    }
    //#region lifeCycle

    @Override
    public void onStart() {
        super.onStart();
            initializePlayer(uriView);


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            updateData();

        Log.d(TAG, "onStart: " + uriView);

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
        saveMyPosition();
        releasePlayer();

        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        try {
            if (!Utils.isTablet(getContext()) && getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT)
            getFragmentManager().beginTransaction().remove(this).commit();

        }catch (IllegalStateException e){

        }
    }

    @Override
    public void onDetach() {

        super.onDetach();
        Log.d(TAG, "onDetach: ");
    }
//#endregion

    private void updateData(){

        Log.d(TAG, mPosition+"updateData: "+mSteps.size());
        if (mPosition <= 0){
            left.setVisibility(View.INVISIBLE);
            right.setVisibility(View.VISIBLE);

        }else if(mPosition >= mSteps.size()-1){
            right.setVisibility(View.INVISIBLE);
            left.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.left:
                if(mPosition > 0) {
                    mPosition = mPosition - 1;
                    restartFragment( mPosition, mArguments);
                }
                break;
            case R.id.right:
                if (mPosition < mSteps.size()-1) {
                    mPosition = mPosition + 1;
                    restartFragment( mPosition, mArguments);
                }
                break;
        }
    }

    public void restartFragment(int mPosition, Bundle arg) {

        Fragment fragmentItself = new FragmentsStepDetailView();
        Bundle bundle = new Bundle();
        bundle.putInt("position",mPosition);
        bundle.putParcelableArrayList("steps", mSteps);
        fragmentItself.setArguments(bundle);
        getFragmentManager().popBackStack();
        getFragmentManager().beginTransaction().replace(R.id.main_container, fragmentItself).addToBackStack(FragmentsStepDetailView.TAG).commit();


    }

    private void releasePlayer() {

              if (mExoPlayer != null) {
                  mExoPlayer.stop();
                  mExoPlayer.release();
              }
              mExoPlayer = null;

    }


    //#region callbacks
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }


    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }


        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }


//#endregion
private void saveMyPosition(){
        if(mExoPlayer != null){
            mPreviousPosition = mExoPlayer.getCurrentPosition();
        }
}
@Override
public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    Log.d(TAG, "onConfigurationChanged: ");
}

}
