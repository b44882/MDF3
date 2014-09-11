// Brett Gear
// MDF3 1409

package com.fullsail.fundamentals;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

import java.nio.channels.IllegalBlockingModeException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.fullsail.fundamentals.MusicPlayerService.BoundServiceBinder;


public class MainActivity extends Activity implements ControlsFragment.Callbacks {

    TextView trackText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager =  getFragmentManager();
        FragmentTransaction trans = fragmentManager.beginTransaction();
        AlbumFragment albumFragment = AlbumFragment.newInstance();
        trans.replace(R.id.albumHolder, albumFragment, ControlsFragment.TAG);
        trans.commit();


        FragmentTransaction trans2 = fragmentManager.beginTransaction();
        ControlsFragment controlFragment = ControlsFragment.newInstance();
        trans2.replace(R.id.controlsHolder, controlFragment, AlbumFragment.TAG);
        trans2.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        trackText = (TextView) findViewById(R.id.trackTextView);

    }

    @Override
    public void trackNameChange(String trackName) {
        trackText.setText(trackName);
    }
}
