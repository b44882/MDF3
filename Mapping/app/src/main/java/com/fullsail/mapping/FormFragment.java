package com.fullsail.mapping;

import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by administrator on 9/23/14.
 */
public class FormFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_TAKE_PICTURE = 0x01001;
    private static final String ARG_LAT = "FormFragment.ARG_LAT";
    private static final String ARG_LON = "FormFragment.ARG_LON";
    public static final String TAG = "FormFragment.TAG";

    TextView mLatTV;
    TextView mLongTV;
    TextView desc1;
    TextView desc2;
    ImageView pictureIV;
    Uri mImageUri;
    Double mLat;
    Double mLong;

    public static FormFragment newInstance(double latitude, double longitude) {
        FormFragment frag = new FormFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LAT, latitude);
        args.putDouble(ARG_LON, longitude);

        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_LAT) && args.containsKey(ARG_LON)){
            mLat = args.getDouble(ARG_LAT);
            mLong = args.getDouble(ARG_LON);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mLatTV = (TextView) getView().findViewById(R.id.formLatText);
        mLongTV = (TextView) getView().findViewById(R.id.formLongText);

        if (mLat != null && mLong != null){
            mLatTV.setText(String.valueOf(mLat));
            mLongTV.setText(String.valueOf(mLong));
        }

        desc1 = (TextView) getView().findViewById(R.id.formDesc1);
        desc2 = (TextView) getView().findViewById(R.id.formDesc2);

        pictureIV = (ImageView) getView().findViewById(R.id.formImageView);

        getView().findViewById(R.id.formPictureButton).setOnClickListener(this);
        getView().findViewById(R.id.formSaveButton).setOnClickListener(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_TAKE_PICTURE && resultCode != getActivity().RESULT_CANCELED) {
            if(mImageUri != null) {
                pictureIV.setImageBitmap(BitmapFactory.decodeFile(mImageUri.getPath()));
                addImageToGallery(mImageUri);
            } else {
                pictureIV.setImageBitmap((Bitmap)data.getParcelableExtra("data"));
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.formPictureButton) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mImageUri = getImageUri();
            if(mImageUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            }
            startActivityForResult(intent, REQUEST_TAKE_PICTURE);

        } else if(v.getId() == R.id.formSaveButton) {
            finishActivity();
        }
    }

    private Uri getImageUri() {

        String imageName = new SimpleDateFormat("MMddyyyy_HHmmss").format(new Date(System.currentTimeMillis()));

        File imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File appDir = new File(imageDir, "CameraExample");
        appDir.mkdirs();

        File image = new File(appDir, imageName + ".jpg");
        try {
            image.createNewFile();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }

        return Uri.fromFile(image);
    }

    private void addImageToGallery(Uri imageUri) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        getActivity().sendBroadcast(scanIntent);
    }

    public void objectSerialize (ArrayList<MarkerItem> list){

        try {
            FileOutputStream fos = getActivity().openFileOutput("marker_save.bin", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
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

    private void finishActivity() {


        String latString = String.valueOf(mLatTV.getText());
        String longString = String.valueOf(mLongTV.getText());
        String desc1String = String.valueOf(desc1.getText());
        String desc2String = String.valueOf(desc2.getText());
        String uri = mImageUri.toString();

        ArrayList<MarkerItem> markerList = openObjectSerialize();
        if (markerList == null){
            markerList = new ArrayList<MarkerItem>();
        }
        markerList.add(new MarkerItem(latString,longString,desc1String,desc2String,uri));

        objectSerialize(markerList);

        Intent nextActivity = new Intent(getActivity(), MainMapActivity.class);
        getActivity().startActivity(nextActivity);

        getActivity().finish();
    }
}
