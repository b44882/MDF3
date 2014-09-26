package com.fullsail.mapping;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.http.protocol.HTTP;


public class MainMapActivity extends Activity implements View.OnClickListener {

    double mLat = 0;
    double mLon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainMapFragment frag = new MainMapFragment();
        getFragmentManager().beginTransaction().replace(R.id.main_container, frag).commit();
        findViewById(R.id.mapAddButton).setOnClickListener(this);
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

    public void setLatLon (Double lat, Double lon){
        mLat = lat;
        mLon = lon;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mapAddButton){

            Intent nextActivity = new Intent(MainMapActivity.this, FormActivity.class);
            nextActivity.putExtra("Latitude",mLat);
            nextActivity.putExtra("Longitude",mLon);
            MainMapActivity.this.startActivity(nextActivity);
            finish();

        }
    }


}
