package com.fullsail.fundamentals;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.util.ArrayList;

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    public static final String TAG = "MusicPlayerService.TAG";
    BoundServiceBinder mBinder;
    private MediaPlayer mPlayer;
    ArrayList<String> playlist;
    int position = 0;
    Boolean continuousPlay = false;

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
        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
    }

    public void setList(ArrayList<String> arrayList){
        playlist = arrayList;
    }

    public void playSong(){
        mPlayer.reset();
        try{
            mPlayer.setDataSource(this, Uri.parse(playlist.get(position)));
        }
        catch(Exception e){
            e.printStackTrace();
            mPlayer.release();
            mPlayer = null;
        }
        mPlayer.prepareAsync();
    }

    public void nextSong(){
        position +=1;
        if (position > 2){
            position = 0;
        }
        this.playSong();
    }

    public void prevSong(){
        position =-1;
        if (position < 0){
            position = 2;
        }
        this.playSong();
    }

    public void pauseSong(){
        mPlayer.pause();
    }

    public void setContinuousPlay(){
        continuousPlay = !continuousPlay;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mPlayer.stop();
        mPlayer.release();
        Toast.makeText(this, "Service Unbound", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "Service Bound", Toast.LENGTH_SHORT).show();
        return mBinder;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        nextSong();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    public void showText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
