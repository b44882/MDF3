// Brett Gear
// MDF3 1409

package com.fullsail.fundamentals;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;
import com.fullsail.fundamentals.MusicPlayerService.BoundServiceBinder;


public class MainActivity extends Activity implements OnClickListener {


    public static final String EXTRA_RECEIVER = "com.fullsail.Fundamentals.MainActivity.EXTRA_RECEIVER";
    public static final String DATA_RETURNED = "MainActivity.DATA_RETURNED";
    public static final int RESULT_DATA_RETURNED = 0x0101010;

    MusicPlayerService mService;
    boolean mBound;
    int trackNumber = 0;
    Intent intent;
    ArrayList<String> playlist = new ArrayList<String>();
    TextView trackText;
    CheckBox continuousCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playlist.add("android.resource://" + getPackageName() + "/" + R.raw.audio1);
        playlist.add("android.resource://" + getPackageName() + "/" + R.raw.audio2);
        playlist.add("android.resource://" + getPackageName() + "/" + R.raw.audio3);
        findViewById(R.id.playButton).setOnClickListener(this);
        findViewById(R.id.prevButton).setOnClickListener(this);
        findViewById(R.id.stopButton).setOnClickListener(this);
        findViewById(R.id.skipButton).setOnClickListener(this);
        findViewById(R.id.pauseButton).setOnClickListener(this);

        trackText = (TextView) findViewById(R.id.trackTextField);
        continuousCheckBox = (CheckBox) findViewById(R.id.continuousBox);
        continuousCheckBox.setOnClickListener(this);
    }

    private final Handler mHandler = new Handler();

    public class DataReceiver extends ResultReceiver {

        public DataReceiver() {
            super(mHandler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(resultData != null && resultData.containsKey(DATA_RETURNED)) {
                trackText.setText(resultData.getString(DATA_RETURNED, ""));
                if (resultData.containsKey("checked")){
                    if (resultData.getBoolean("checked")) {
                        continuousCheckBox.setChecked(true);
                    }
                }
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(intent==null){
            intent = new Intent(this, MusicPlayerService.class);
            bindService(intent, musicConnection, Context.BIND_AUTO_CREATE);
            intent.putExtra(EXTRA_RECEIVER, new DataReceiver());
            startService(intent);
        }
    }


    public void onClick(View view) {
        if(view.getId() == R.id.playButton) {
            mService.playSong();
        } else if (view.getId() == R.id.stopButton) {
            mService.stopSong();
        } else if (view.getId() == R.id.skipButton) {
            mService.nextSong();
        } else if (view.getId() == R.id.prevButton) {
            mService.prevSong();
        } else if (view.getId() == R.id.pauseButton) {
            mService.pauseSong();
        } else if (view.getId() == R.id.continuousBox){
            mService.setContinuousPlay();
        }

    }

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
