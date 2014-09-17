package com.fullsail.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ItemActivity extends Activity {
	
	public static final String EXTRA_ITEM = "com.fullsail.android.DetailsActivity.EXTRA_ITEM";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item);
		
		Intent intent = getIntent();
		CharacterItem character = (CharacterItem)intent.getSerializableExtra(EXTRA_ITEM);
		if(character == null) {
			finish();
			return;
		}
		
		TextView tv = (TextView)findViewById(R.id.nameTextView);
		tv.setText(character.getName());
		
		tv = (TextView)findViewById(R.id.classTextView);
		tv.setText(character.getSpec());
		
		tv = (TextView)findViewById(R.id.descTextView);
		tv.setText(character.getDesc());
	}
}
