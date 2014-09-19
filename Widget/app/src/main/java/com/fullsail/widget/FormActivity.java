package com.fullsail.widget;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.content.BroadcastReceiver;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by administrator on 9/16/14.
 */
public class FormActivity extends Activity {

    Button createItemButton;
    public static final String ACTION_UPDATE = "com.fullsail.widget.ACTION_UPDATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        FormFragment formFragment = FormFragment.newInstance();
        transaction.replace(R.id.list_fragmentHolder, formFragment, FormFragment.TAG);
        transaction.commit();

        createItemButton = (Button) findViewById(R.id.form_button);
        createItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ItemFragment.TAG, "Activity is paused");
        finish();
    }

    private void finishActivity() {

        TextView tv = (TextView)findViewById(R.id.nameEditText);
        String nameString = String.valueOf(tv.getText());

        tv = (TextView)findViewById(R.id.classEditText);
        String classString = String.valueOf(tv.getText());

        tv = (TextView)findViewById(R.id.descEditText);
        String descString = String.valueOf(tv.getText());

        ArrayList<CharacterItem> characterGroup = openObjectSerialize();
        if (characterGroup == null){
            characterGroup = new ArrayList<CharacterItem>();
        }
        characterGroup.add(new CharacterItem(nameString,classString,descString));
        objectSerialize(characterGroup);



        AppWidgetManager widgetManager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName widgetComponent = new ComponentName(getApplicationContext(), FormWidgetProvider.class);
        int[] widgetIds = widgetManager.getAppWidgetIds(widgetComponent);
        Intent update = new Intent();
        update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
        update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        sendBroadcast(update);

        Intent intent = new Intent(ACTION_UPDATE);
        sendBroadcast(intent);

        Intent nextActivity = new Intent(FormActivity.this, ListActivity.class);
        FormActivity.this.startActivity(nextActivity);



        finish();
    }

    public void objectSerialize (ArrayList<CharacterItem> list){

        try {
            FileOutputStream fos = openFileOutput("widget_save.bin", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<CharacterItem> openObjectSerialize() {
        ArrayList<CharacterItem> list;
        try {
            FileInputStream fin = openFileInput("widget_save.bin");
            ObjectInputStream oin = new ObjectInputStream(fin);
            list = (ArrayList<CharacterItem>) oin.readObject();
            oin.close();
        } catch(Exception e) {
            e.printStackTrace();
            list = null;
        }

        return list;
    }





}
