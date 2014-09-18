package com.fullsail.widget;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


public class ListActivity extends Activity implements ListFragment.Callbacks, Serializable, View.OnClickListener {

    public static final String EXTRA_ITEM = "com.fullsail.Widget.EXTRA_ITEM";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        Button createButton = (Button) findViewById(R.id.list_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextActivity = new Intent(ListActivity.this, FormActivity.class);
                ListActivity.this.startActivity(nextActivity);
            }
        });

        ArrayList<PassableObject> passList;
        ArrayList<CharacterItem> serList;

        serList = openObjectSerialize();

        if (serList != null){
            passList = convertSerToParse(serList);

            FragmentManager fragmentManager =  getFragmentManager();
            FragmentTransaction trans = fragmentManager.beginTransaction();
            ListFragment listFragment = ListFragment.newInstance(passList);
            trans.replace(R.id.list_fragmentHolder, listFragment, ListFragment.TAG);
            trans.commit();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ItemFragment.TAG, "Activity is paused");
        finish();
    }

    public ArrayList<PassableObject> convertSerToParse (ArrayList<CharacterItem> list){
        ArrayList parseList = new ArrayList();
        if (list != null){
            CharacterItem currentSerObject;
            PassableObject convertedParObject;

            for (int i = 0; i < list.size(); i++){

                currentSerObject = list.get(i);
                convertedParObject = new PassableObject(currentSerObject.getName(), currentSerObject.getSpec(), currentSerObject.getDesc());
                if (convertedParObject != null){
                    parseList.add(convertedParObject);
                }
            }
        } else {
            parseList = null;
        }
        return parseList;
    }

    @Override
    public void onItemSelected(PassableObject object, int position) {

        String nameString = String.valueOf(object.nameChar);
        String classString = String.valueOf(object.classChar);
        String descString = String.valueOf(object.descChar);

        CharacterItem character = new CharacterItem(nameString,classString,descString);
        if(character != null) {
            Intent items = new Intent(ListActivity.this, ItemActivity.class);
            items.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            items.putExtra(ItemActivity.EXTRA_ITEM, character);
            ListActivity.this.startActivity(items);
        }

    }

    @Override
    public void onClick(View view) {

    }

    public class PassableObject implements Parcelable {

        public String nameChar;
        public String classChar;
        public String descChar;

        public PassableObject(String nameChar, String classChar, String descChar){
            this.nameChar = nameChar;
            this.classChar = classChar;
            this.descChar = descChar;
        }

        @Override
        public String toString() {
            return this.nameChar;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {

        }
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
    public ArrayList openObjectSerialize() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.master, menu);
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

}
