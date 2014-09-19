//Brett Gear
//MDF3 1409

package com.fullsail.widget;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

/**
 * Created by administrator on 9/17/14.
 */
public class FormFragment extends Fragment implements Serializable {

    public static final String TAG = "FormFragment.TAG";

    public static FormFragment newInstance() {
        return new FormFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form, container, false);

        return view;
    }
}
