package com.fullsail.fundamentals;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;


public class MainActivity extends Activity {

    public static final int STANDARD_NOTIFICATION = 0x01001;
    public static final int EXPANDED_NOTIFICATION = 0x01002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String uri1 = "android.resource://" + getPackageName() + "/raw/";

        String uri2 = "android.resource://" + getPackageName() + "/" + R.raw.audio1;

// Player created, in idle state.
        MediaPlayer player = new MediaPlayer();

// Player initialized, in initialized state.
        try {
            player.setDataSource(this, Uri.parse(uri2));
        } catch (IOException e) {
            e.printStackTrace();
        }
// Setting a prepared listener.
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // Player prepared!
                mp.start();
            }
        });

        NotificationManager mgr =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setLargeIcon(BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_launcher));
        builder.setContentTitle("Standard Title");
        builder.setContentText("Standard Message");
        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
        bigStyle.bigText("Some really long text goes here.");
        bigStyle.setBigContentTitle("Expanded Title");
        bigStyle.setSummaryText("Expanded Summary");
        builder.setStyle(bigStyle);
        mgr.notify(EXPANDED_NOTIFICATION, builder.build());

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
