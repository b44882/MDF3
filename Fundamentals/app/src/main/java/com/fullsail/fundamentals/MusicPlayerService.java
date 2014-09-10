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

import java.util.ArrayList;

public class MusicPlayerService extends IntentService implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    public static final String TAG = "MusicPlayerService.TAG";
    public static final int STANDARD_NOTIFICATION = 0x01001;

    BoundServiceBinder mBinder;
    private MediaPlayer mPlayer;
    ArrayList<String> playlist;
    int position = 0;
    Boolean continuousPlay = false;
    Boolean pause = false;
    Boolean loop = false;
    Boolean shuffle = false;
    int mediaLength;
    Intent mIntent;
    NotificationManager mgr;
    NotificationCompat.Builder builder;
    boolean playing;

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
        mgr = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
    }

    public void setList(ArrayList<String> arrayList){
        playlist = arrayList;
    }

    public void playSong(){
        mPlayer.reset();
        playing = true;
        try {
            mPlayer.setDataSource(this, Uri.parse(playlist.get(position)));
        } catch (Exception e) {
            e.printStackTrace();
            mPlayer.release();
            mPlayer = null;
        }
        setTrackText("Audio " + (position + 1));
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
        playing = false;
        setTrackText("");
        mgr.cancel(STANDARD_NOTIFICATION);
        pause = false;
        mPlayer.stop();
    }

    public void pauseSong(){
        setTrackText("Audio " + (position + 1) + " paused");
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
        if(intent.hasExtra(ControlsFragment.EXTRA_RECEIVER)) {
            mIntent = intent;
            if (playing){
                if (pause){
                    setTrackText("Audio " + (position + 1) + " paused");
                } else {
                    setTrackText("Audio " + (position + 1));
                }
            } else {
                setTrackText("");
            }

        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (continuousPlay){
            nextSong();
        } else {
            playing = false;
            setTrackText("");
            mgr.cancel(STANDARD_NOTIFICATION);
        }
    }

    public void setTrackText(String track){
        if(mIntent.hasExtra(ControlsFragment.EXTRA_RECEIVER)) {
            ResultReceiver receiver = mIntent.getParcelableExtra(ControlsFragment.EXTRA_RECEIVER);
            Bundle result = new Bundle();
            result.putString(ControlsFragment.DATA_RETURNED,track);
            result.putBoolean("checked", continuousPlay);
            receiver.send(ControlsFragment.RESULT_DATA_RETURNED, result);
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
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (pause){
            mediaPlayer.seekTo(mediaLength);
        }
        pause = false;
        mediaPlayer.start();
    }

}
