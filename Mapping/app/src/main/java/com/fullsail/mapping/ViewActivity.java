package com.fullsail.mapping;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by administrator on 9/23/14.
 */
public class ViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        ViewFragment frag = new ViewFragment();
        getFragmentManager().beginTransaction().replace(R.id.view_container, frag).commit();
    }

}
