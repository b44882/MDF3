package com.fullsail.mapping;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * Created by administrator on 9/23/14.
 */
public class ViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Bundle extras = getIntent().getExtras();
        MarkerItem item = (MarkerItem) extras.getSerializable("MarkerItem");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ViewFragment viewFragment = ViewFragment.newInstance(item);
        transaction.replace(R.id.view_container, viewFragment, ViewFragment.TAG);
        transaction.commit();
    }

}
