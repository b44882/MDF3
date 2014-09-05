package com.fullsail.fundamentals;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import java.io.IOException;

import com.fullsail.fundamentals.SimpleService.BoundServiceBinder;


public class MainActivity extends Activity implements OnClickListener, ServiceConnection {

    SimpleService mService;
    boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String uri = "android.resource://" + getPackageName() + "/" + R.raw.audio1;
        findViewById(R.id.playButton).setOnClickListener(this);
        findViewById(R.id.stopButton).setOnClickListener(this);
        findViewById(R.id.nextButton).setOnClickListener(this);

    }

    public void onClick(View view) {
        Intent intent = new Intent(this, SimpleService.class);
        if(view.getId() == R.id.playButton) {
            bindService(intent, this, Context.BIND_AUTO_CREATE);
        } else if(view.getId() == R.id.stopButton) {
            if(mBound) {
                unbindService(this);
                mBound = false;
                mService = null;
            }
        } else if (view.getId() == R.id.nextButton) {
            if(mService != null) {
                mService.showText("Next Pressed");
            }
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        BoundServiceBinder binder = (BoundServiceBinder)service;
        mService = binder.getService();
        mBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService = null;
        mBound = false;
    }

}
