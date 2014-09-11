package com.fullsail.fundamentals;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by administrator on 9/9/14.
 */
public class ControlsFragment extends AlbumFragment implements View.OnClickListener {

    CheckBox continuousCheckBox;
    CheckBox loopCheckBox;
    CheckBox shuffleCheckBox;
    MusicPlayerService mService;
    boolean mBound;
    int trackNumber = 0;
    Intent intent;
    ArrayList<String> playlist = new ArrayList<String>();

    public static final String TAG = "ControlsFragment.TAG";

    public static final String EXTRA_RECEIVER = "com.fullsail.Fundamentals.MainActivity.EXTRA_RECEIVER";
    public static final String DATA_RETURNED = "MainActivity.DATA_RETURNED";
    public static final int RESULT_DATA_RETURNED = 0x0101010;

    public static ControlsFragment newInstance() {

        return new ControlsFragment();

    }

    private Callbacks mCallbacks;

    public interface Callbacks {

        public void trackNameChange(String trackName);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (Callbacks) activity;
        } catch (ClassCastException ex) {
            Log.e(TAG, "Casting the activity as a Callbacks listener failed"
                    + ex);
            mCallbacks = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container,
                             Bundle _savedInstanceState) {

        View view = _inflater.inflate(R.layout.fragment_controls, _container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getView().findViewById(R.id.playButton).setOnClickListener(this);
        getView().findViewById(R.id.prevButton).setOnClickListener(this);
        getView().findViewById(R.id.stopButton).setOnClickListener(this);
        getView().findViewById(R.id.nextButton).setOnClickListener(this);
        getView().findViewById(R.id.pauseButton).setOnClickListener(this);

        playlist.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.audio1);
        playlist.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.audio2);
        playlist.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.audio3);

        continuousCheckBox = (CheckBox) getView().findViewById(R.id.continuousCheckBox);
        continuousCheckBox.setOnClickListener(this);

        loopCheckBox = (CheckBox) getView().findViewById(R.id.loopCheckBox);
        loopCheckBox.setOnClickListener(this);

        shuffleCheckBox = (CheckBox) getView().findViewById(R.id.shuffleCheckBox);
        shuffleCheckBox.setOnClickListener(this);

        if(intent==null){
            intent = new Intent(getActivity(), MusicPlayerService.class);
            getActivity().bindService(intent, musicConnection, Context.BIND_AUTO_CREATE);
            intent.putExtra(EXTRA_RECEIVER, new ControlsFragment.DataReceiver());
            getActivity().startService(intent);
        }
    }

    private final Handler mHandler = new Handler();

    public class DataReceiver extends ResultReceiver {

        public DataReceiver() {
            super(mHandler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(resultData != null && resultData.containsKey(DATA_RETURNED)) {
                if (mCallbacks != null) {
                    mCallbacks.trackNameChange((resultData.getString(DATA_RETURNED, "")));
                }
                Log.i(TAG, "Breakpoint");
                if (resultData.containsKey("continuous_checked")){
                    if (resultData.getBoolean("continuous_checked")) {
                        continuousCheckBox.setChecked(true);
                    }
                }
                if (resultData.containsKey("loop_checked")){
                    if (resultData.getBoolean("loop_checked")) {
                        loopCheckBox.setChecked(true);
                    }
                }
                if (resultData.containsKey("shuffle_checked")){
                    if (resultData.getBoolean("shuffle_checked")) {
                        shuffleCheckBox.setChecked(true);
                    }
                }
            }

        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.playButton) {
            mService.playSong();
        } else if (view.getId() == R.id.stopButton) {
            mService.stopSong();
        } else if (view.getId() == R.id.nextButton) {
            mService.nextSong();
        } else if (view.getId() == R.id.prevButton) {
            mService.prevSong();
        } else if (view.getId() == R.id.pauseButton) {
            mService.pauseSong();
        } else if (view.getId() == R.id.continuousCheckBox){
            mService.setContinuousPlay();
            loopCheckBox.setChecked(false);
        } else if (view.getId() == R.id.loopCheckBox){
            mService.setLoop();
            continuousCheckBox.setChecked(false);
            shuffleCheckBox.setChecked(false);
        } else if (view.getId() == R.id.shuffleCheckBox){
            mService.setShuffle();
            loopCheckBox.setChecked(false);
        }
    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlayerService.BoundServiceBinder binder = (MusicPlayerService.BoundServiceBinder)service;
            mService = binder.getService();
            mService.setList(playlist);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

}
