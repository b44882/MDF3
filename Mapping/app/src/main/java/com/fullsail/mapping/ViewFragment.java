package com.fullsail.mapping;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by administrator on 9/23/14.
 */
public class ViewFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_SER = "ViewFragment.ARG_SER";
    public static final String TAG = "ViewFragment.TAG";

    TextView mLatTV;
    TextView mLongTV;
    TextView desc1;
    TextView desc2;
    ImageView pictureIV;
    Uri mImageUri;

    MarkerItem mMarkerItem;

    public static ViewFragment newInstance(MarkerItem item) {
        ViewFragment frag = new ViewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SER, item);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_SER)){
            mMarkerItem = (MarkerItem) args.getSerializable(ARG_SER);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mLatTV = (TextView) getView().findViewById(R.id.viewLatText);
        mLongTV = (TextView) getView().findViewById(R.id.viewLonText);
        desc1 = (TextView) getView().findViewById(R.id.viewDesc1Text);
        desc2 = (TextView) getView().findViewById(R.id.viewDesc2Text);
        pictureIV = (ImageView) getView().findViewById(R.id.viewMapImage);

        if (mMarkerItem != null){
            mLatTV.setText(mMarkerItem.getLat());
            mLongTV.setText(mMarkerItem.getLong());
            desc1.setText(mMarkerItem.getDesc1());
            desc2.setText(mMarkerItem.getDesc2());
            mImageUri = Uri.parse(mMarkerItem.getUri());
            pictureIV.setImageBitmap(BitmapFactory.decodeFile(mImageUri.getPath()));
        }

        getView().findViewById(R.id.viewCloseButton).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.viewCloseButton) {
        finishActivity();
        }
    }

    private void finishActivity() {
        Intent nextActivity = new Intent(getActivity(), MainMapActivity.class);
        getActivity().startActivity(nextActivity);
        getActivity().finish();
    }
}
