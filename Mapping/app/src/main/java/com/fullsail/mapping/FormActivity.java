package com.fullsail.mapping;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * Created by administrator on 9/23/14.
 */
public class FormActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);


        Bundle extras = getIntent().getExtras();
        double mLat = extras.getDouble("Latitude");
        double mLong = extras.getDouble("Longitude");


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        FormFragment formFragment = FormFragment.newInstance(mLat,mLong);
        transaction.replace(R.id.form_container, formFragment, FormFragment.TAG);
        transaction.commit();
    }



}
