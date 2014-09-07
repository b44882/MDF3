package com.fullsail.fundamentals;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;

import com.fullsail.fundamentals.MusicPlayerService.BoundServiceBinder;


public class MainActivity extends Activity implements OnClickListener {

    MusicPlayerService mService;
    boolean mBound;
    Intent intent;
    ArrayList<String> playlist = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playlist.add("android.resource://" + getPackageName() + "/" + R.raw.audio1);
        playlist.add("android.resource://" + getPackageName() + "/" + R.raw.audio2);
        playlist.add("android.resource://" + getPackageName() + "/" + R.raw.audio3);
        findViewById(R.id.playButton).setOnClickListener(this);
        findViewById(R.id.stopButton).setOnClickListener(this);
        findViewById(R.id.nextButton).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(intent==null){
            intent = new Intent(this, MusicPlayerService.class);
            bindService(intent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(intent);
        }
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, MusicPlayerService.class);
        if(view.getId() == R.id.playButton) {
            mService.playSong();
        } else if(view.getId() == R.id.stopButton) {
            if(mBound) {
                mBound = false;
                mService = null;
            }
        } else if (view.getId() == R.id.nextButton) {
            if(mService != null) {
                mService.nextSong();
            }
        }
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BoundServiceBinder binder = (BoundServiceBinder)service;
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

    protected void onDestroy(){
        stopService(intent);
        mService=null;
        super.onDestroy();
    }

}
