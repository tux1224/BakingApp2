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

    private final String TAG = FragmentsStepDetailView.class.getSimpleName();

    private PlaybackStateCompat.Builder mStateBuilder;
    private long mPreviousPosition;
    private final String SESION = "remaining_video";
    private boolean isPlayerPlaying;

    Steps mStep;
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



        View view       = inflater.inflate(R.layout.fragment_step_detail_view,container,false);
        tvDescription   = (TextView) view.findViewById(R.id.tv_description);
        left            = (ImageView) view.findViewById(R.id.left);
        right           = (ImageView) view.findViewById(R.id.right);
        mPlayerView     = (SimpleExoPlayerView) view.findViewById(R.id.player_view);
        left.setOnClickListener(this);
        right.setOnClickListener(this);

        if(getArguments() != null && getArguments().containsKey("steps")) {

            Bundle bundle = getArguments();
            mPosition = bundle.getInt("position");
            mSteps = bundle.getParcelableArrayList("steps");
            mStep = mSteps.get(mPosition);

        }

        uriView = Uri.parse(mStep.getVideoURL());
        tvDescription.setText(mStep.getDescription());


        if (savedInstanceState!= null) {
            mPreviousPosition = (savedInstanceState.getLong(SESION, 0));
            Log.d(TAG, " mPreviousPosition "+ mPreviousPosition);

        }

;


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
        super.onSaveInstanceState(outState);
    }


/*    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
        mPreviousPosition = savedInstanceState.getLong(SESION,0);

    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null)
            mPreviousPosition = savedInstanceState.getLong(SESION,0);
    }

    @Override
    public void onStart() {
        super.onStart();

            initializePlayer(uriView);

        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
            updateData();


    }

    @Override
    public void onResume() {
        super.onResume();
        }
    @Override
    public void onPause() {
        super.onPause();


    }
    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }



    private void updateData(){

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
                    restartFragment( mPosition, getArguments());
                }
                break;
            case R.id.right:
                if (mPosition < mSteps.size()-1) {
                    mPosition = mPosition + 1;
                    restartFragment( mPosition, getArguments());
                }
                break;
        }
    }

    public void restartFragment(int mPosition, Bundle arg) {

        Fragment fragmentItself = new FragmentsStepDetailView();
        Bundle bundle = arg;
        bundle.putInt("position",mPosition);
        fragmentItself.setArguments(bundle);
        getActivity().getSupportFragmentManager().popBackStack();
        getFragmentManager().beginTransaction().replace(R.id.main_container,fragmentItself).addToBackStack(fragmentItself.getTag()).commit();
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
}
