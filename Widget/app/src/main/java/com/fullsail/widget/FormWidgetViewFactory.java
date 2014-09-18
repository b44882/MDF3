package com.fullsail.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class FormWidgetViewFactory implements RemoteViewsFactory, Serializable {
	
	private static final int ID_CONSTANT = 0x0101010;
	
	private ArrayList<CharacterItem> mArticles;
	private Context mContext;
	
	public FormWidgetViewFactory(Context context) {
		mContext = context;
		mArticles = openObjectSerialize();

	}

	@Override
	public void onCreate() {
        Log.i("TEXT", "OnCreate");
        if (mArticles == null) {
            Log.i("TEXT", "inNull");
            mArticles = new ArrayList<CharacterItem>();
            String[] names = {"Bill", "Sammy", "Kragnax"};
            String[] classes = {"Fighter", "Wizard", "Destroyer"};
            String[] descriptions = {"Fights bravely to defeat evil", "Knows many spells to destroy monsters", "Crushes boulders for fun"};

            for (int i = 0; i < 3; i++) {
                mArticles.add(new CharacterItem(names[i], classes[i], descriptions[i]));
            }
            objectSerialize(mArticles);
        }
	}

    public void objectSerialize (ArrayList<CharacterItem> list){

        try {
            FileOutputStream fos = mContext.openFileOutput("widget_save.bin", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<CharacterItem> openObjectSerialize() {
        Log.i("TEXT", "openObjectSerialize");
        ArrayList<CharacterItem> list;
        try {
            FileInputStream fin = mContext.openFileInput("widget_save.bin");
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
	public int getCount() {
		return mArticles.size();
	}

	@Override
	public long getItemId(int position) {
		return ID_CONSTANT + position;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public RemoteViews getViewAt(int position) {

        CharacterItem character = mArticles.get(position);
		
		RemoteViews itemView = new RemoteViews(mContext.getPackageName(), R.layout.widget_listview_item);
		itemView.setTextViewText(R.id.nameTextViewWidget, character.getName());
		
		Intent intent = new Intent();
		intent.putExtra(FormWidgetProvider.EXTRA_ITEM, character);
		itemView.setOnClickFillInIntent(R.id.widgetLinearLayoutView, intent);
		
		return itemView;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onDataSetChanged() {
		// Heavy lifting code can go here without blocking the UI.
		// You would update the data in your collection here as well.
	}

	@Override
	public void onDestroy() {
		mArticles.clear();
	}
	
}