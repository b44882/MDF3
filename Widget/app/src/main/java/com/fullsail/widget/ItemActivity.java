//Brett Gear
//MDF3 1409

package com.fullsail.widget;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ItemActivity extends Activity implements View.OnClickListener {
	
	public static final String EXTRA_ITEM = "com.fullsail.android.ItemActivity.EXTRA_ITEM";
    CharacterItem character;
    int page;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Log.i(ItemFragment.TAG, "Activity is created");
		setContentView(R.layout.activity_item);
		
		Intent intent = getIntent();
		character = (CharacterItem)intent.getSerializableExtra(EXTRA_ITEM);
		if(character == null) {
			finish();
			return;
		}

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ItemFragment itemFragment = ItemFragment.newInstance();
        transaction.replace(R.id.list_fragmentHolder, itemFragment, ItemFragment.TAG);
        transaction.commit();

	}

    @Override
    protected void onStart(){
        super.onStart();

        Button createItemButton = (Button) findViewById(R.id.list_button);
        createItemButton.setText("Back");

        findViewById(R.id.list_button).setOnClickListener(this);

        TextView tv = (TextView)findViewById(R.id.nameTextView);
        tv.setText(character.getName());

        tv = (TextView)findViewById(R.id.classTextView);
        tv.setText(character.getSpec());

        tv = (TextView)findViewById(R.id.descTextView);
        tv.setText(character.getDesc());
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ItemFragment.TAG, "Activity is paused");
        finish();
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ItemFragment.TAG, "Activity is destroyed");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.list_button){
            Intent nextActivity = new Intent(ItemActivity.this, ListActivity.class);
            ItemActivity.this.startActivity(nextActivity);

        }
    }
}
