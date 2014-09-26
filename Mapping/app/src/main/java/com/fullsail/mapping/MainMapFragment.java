package com.fullsail.mapping;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * Created by administrator on 9/23/14.
 */
public class MainMapFragment extends MapFragment implements GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener, LocationListener {

    GoogleMap mMap;
    LocationManager mManager;
    double mLat;
    double mLong;

    HashMap<Marker, MarkerItem> markerMap = new HashMap <Marker, MarkerItem>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        mManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        Location loc = mManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc != null){
            mLat= loc.getLatitude();
            mLong = loc.getLongitude();
            ((MainMapActivity)getActivity()).setLatLon(mLat, mLong);
        }

        mMap = getMap();

        ArrayList<MarkerItem> markerList = openObjectSerialize();

        if (markerList != null){
            for (int i = 0; i < markerList.size(); i++){
                MarkerItem currentItem = markerList.get(i);
                double cLat = Double.parseDouble(currentItem.getLat());
                double cLon = Double.parseDouble(currentItem.getLong());
                String title = currentItem.getDesc1();
                Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(cLat,cLon)).title(title));
                markerMap.put(marker,currentItem);
            }
        }
        mMap.setInfoWindowAdapter(new MarkerAdapter());
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapLongClickListener(this);
        if (loc != null){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 16));
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.593770, -81.303797), 16));
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<MarkerItem> openObjectSerialize() {
        ArrayList<MarkerItem> list;
        try {
            FileInputStream fin = getActivity().openFileInput("marker_save.bin");
            ObjectInputStream oin = new ObjectInputStream(fin);
            list = (ArrayList<MarkerItem>) oin.readObject();
            oin.close();
        } catch(Exception e) {
            e.printStackTrace();
            list = null;
        }

        return list;
    }


    @Override
    public void onInfoWindowClick(final Marker marker) {
        MarkerItem currentItem = markerMap.get(marker);

        if(currentItem != null) {
            Intent items = new Intent(getActivity(), ViewActivity.class);
            items.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            items.putExtra("MarkerItem", currentItem);
            MainMapFragment.this.startActivity(items);
            getActivity().finish();
        }

    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null){
            mLat= location.getLatitude();
            mLong = location.getLongitude();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



    @Override
    public void onMapLongClick(LatLng latLng) {
        ((MainMapActivity)getActivity()).setLatLon(latLng.latitude, latLng.longitude);
        Intent nextActivity = new Intent(getActivity(), FormActivity.class);
        nextActivity.putExtra("Latitude", latLng.latitude);
        nextActivity.putExtra("Longitude", latLng.longitude);
        MainMapFragment.this.startActivity(nextActivity);
    }

    private class MarkerAdapter implements GoogleMap.InfoWindowAdapter {

        TextView mText;

        public MarkerAdapter() {
            mText = new TextView(getActivity());
        }

        @Override
        public View getInfoContents(Marker marker) {
            mText.setText(marker.getTitle());
            return mText;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }
    }
}
