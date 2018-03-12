package com.example.salvadorelizarraras.bakingapp;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

/**
 * Created by Salvador Elizarraras on 11/03/2018.
 */

public class FragmentsStepDetailView extends Fragment implements ExoPlayer.EventListener, View.OnClickListener, MainActivity.Listener{
    private SimpleExoPlayer mExoPlayer;

    private final String TAG = FragmentsStepDetailView.class.getSimpleName();
    private MediaSessionCompat mMediaSession;
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



        View view = inflater.inflate(R.layout.fragment_step_detail_view,container,false);
        tvDescription = (TextView) view.findViewById(R.id.tv_description);
        left = (ImageView) view.findViewById(R.id.left);
        right = (ImageView) view.findViewById(R.id.right);
        mPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.player_view);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        if(getArguments() != null && getArguments().containsKey("steps")) {
            Bundle bundle = getArguments();
            mPosition = bundle.getInt("position");

            mSteps = bundle.getParcelableArrayList("steps");
            mStep = mSteps.get(mPosition);
        }
        Log.d(TAG, "onCreate: " + mStep.getVideoURL());
        uriView = Uri.parse(mStep.getVideoURL());

        tvDescription.setText(mStep.getDescription());


        if (savedInstanceState!= null) {
            mPreviousPosition = (savedInstanceState.getLong(SESION, 0));
            Log.d(TAG, " mPreviousPosition "+ mPreviousPosition);

        }

;


        return view;
    }
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.

        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());




        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            if (mediaUri.toString().isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.novideo);
                mPlayerView.setDefaultArtwork(bitmap);
                mPlayerView.setUseController(false);
            }
            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

            mExoPlayer.seekTo(mPreviousPosition);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SESION,mExoPlayer.getCurrentPosition());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
        mPreviousPosition = savedInstanceState.getLong(SESION,0);
    }

    @Override
    public void onStart() {
        initializeMediaSession();
        initializePlayer(uriView);
        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
            updateData();

        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (mExoPlayer != null) {
            //isPlayerPlaying = mExoPlayer.getPlayWhenReady();
          //  mExoPlayer.setPlayWhenReady(true);
        }

    }
    @Override
    public void onPause() {

        releasePlayer();

        super.onPause();
        Log.d(TAG, "onPause: ");



    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

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
        getFragmentManager().beginTransaction().replace(R.id.main_container,fragmentItself).commit();
    }

    private void releasePlayer() {
        Log.d(TAG, "releasePlayer: ");
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void SetData() {
        Log.d(TAG, "SetData: ");
        releasePlayer();
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
