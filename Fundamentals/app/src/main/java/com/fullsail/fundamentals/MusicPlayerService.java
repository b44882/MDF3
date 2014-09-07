// Brett Gear
// MDF3 1409

package com.fullsail.fundamentals;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.ArrayList;

public class MusicPlayerService extends IntentService implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    public static final String TAG = "MusicPlayerService.TAG";
    public static final int STANDARD_NOTIFICATION = 0x01001;

    BoundServiceBinder mBinder;
    private MediaPlayer mPlayer;
    ArrayList<String> playlist;
    int position = 0;
    Boolean continuousPlay = false;
    Boolean pause = false;
    int mediaLength;
    Intent mIntent;
    NotificationManager mgr;
    NotificationCompat.Builder builder;

    public MusicPlayerService() {
        super("MusicPlayerService");
    }

    public class BoundServiceBinder extends Binder {
        public MusicPlayerService getService() {

            return MusicPlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        position = 0;
        mPlayer = new MediaPlayer();
        mBinder = new BoundServiceBinder();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        mgr = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
    }

    public void setList(ArrayList<String> arrayList){
        playlist = arrayList;
    }

    public void playSong(){
        mPlayer.reset();
        try {
            mPlayer.setDataSource(this, Uri.parse(playlist.get(position)));
        } catch (Exception e) {
            e.printStackTrace();
            mPlayer.release();
            mPlayer = null;
        }
        setTrackText();
        setNotification();
        mPlayer.prepareAsync();
    }

    public void nextSong(){
        pause = false;
        position +=1;
        if (position > 2){
            position = 0;
        }
        this.playSong();
    }

    public void prevSong(){
        pause = false;
        position = position - 1;
        if (position < 0){
            position = 2;
        }
        this.playSong();
    }

    public void stopSong(){
        pause = false;
        mPlayer.stop();
    }

    public void pauseSong(){
        pause = true;
        mediaLength = mPlayer.getCurrentPosition();
        mPlayer.pause();
    }

    public void setContinuousPlay(){
        continuousPlay = !continuousPlay;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mPlayer.stop();
        mPlayer.release();
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent.hasExtra(MainActivity.EXTRA_RECEIVER)) {
            mIntent = intent;
            setTrackText();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (continuousPlay){
            nextSong();
        }
    }

    public void setTrackText(){
        if(mIntent.hasExtra(MainActivity.EXTRA_RECEIVER)) {
            ResultReceiver receiver = (ResultReceiver)mIntent.getParcelableExtra(MainActivity.EXTRA_RECEIVER);
            Bundle result = new Bundle();
            int trackNumber = position + 1;
            result.putString(MainActivity.DATA_RETURNED,"Audio " + trackNumber);
            result.putBoolean("checked", continuousPlay);
            receiver.send(MainActivity.RESULT_DATA_RETURNED, result);
        }
    }

    public void setNotification(){
        Context context = getApplicationContext();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        builder.setSmallIcon(R.drawable.ic_music2);
        builder.setLargeIcon(BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_music2));
        builder.setContentTitle("Audio " + (position + 1));
        builder.setContentText("Currently Playing: Audio " + (position + 1));
        builder.setContentIntent(pIntent);
        mgr.notify(STANDARD_NOTIFICATION, builder.build());
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (pause){
            mediaPlayer.seekTo(mediaLength);
        }
        pause = false;
        mediaPlayer.start();
    }

}
