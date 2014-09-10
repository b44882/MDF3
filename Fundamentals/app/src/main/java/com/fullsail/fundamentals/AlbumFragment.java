package com.fullsail.fundamentals;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by administrator on 9/9/14.
 */
public class AlbumFragment extends Fragment {

    public static final String TAG = "AlbumFragment.TAG";

    public static AlbumFragment newInstance() {

        return new AlbumFragment();

    }

    public View onCreateView(LayoutInflater _inflater, ViewGroup _container,
                             Bundle _savedInstanceState) {

        View view = _inflater.inflate(R.layout.fragment_album, _container, false);

        return view;
    }
}
