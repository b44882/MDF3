//Brett Gear
//MDF3 1409

package com.fullsail.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class FormWidgetProvider extends AppWidgetProvider {


    public static final String ACTION_VIEW_DETAILS = "com.fullsail.widget.ACTION_VIEW_DETAILS";
    public static final String ACTION_ADD_ITEM = "com.fullsail.widget.ACTION_ADD_ITEM";
    public static final String ACTION_UPDATE = "com.fullsail.widget.ACTION_UPDATE";
    public static final String EXTRA_ITEM = "com.fullsail.Widget.EXTRA_ITEM";
    public static final String TAG = "FormWidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        Log.i(TAG, "OnUpdate");

        for(int i = 0; i < appWidgetIds.length; i++) {

            int widgetId = appWidgetIds[i];

            Intent intent = new Intent(context, FormWidgetService.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);


            RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            widgetView.setRemoteAdapter(R.id.widgetListView, intent);
            widgetView.setEmptyView(R.id.widgetListView, R.id.empty);


            Intent itemIntent = new Intent(ACTION_VIEW_DETAILS);
            Intent addIntent = new Intent(ACTION_ADD_ITEM);


            PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, itemIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pIntent2 = PendingIntent.getBroadcast(context, 0, addIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            widgetView.setPendingIntentTemplate(R.id.widgetListView, pIntent);
            widgetView.setOnClickPendingIntent(R.id.widgetAddButton, pIntent2);

            appWidgetManager.updateAppWidget(widgetId, widgetView);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "In receive intent");
        if(intent.getAction().equals(ACTION_VIEW_DETAILS)) {
            Log.i(TAG, ACTION_VIEW_DETAILS);
            CharacterItem character = (CharacterItem)intent.getSerializableExtra(EXTRA_ITEM);
            if(character != null) {
                Intent details = new Intent(context, ItemActivity.class);
                details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                details.putExtra(ItemActivity.EXTRA_ITEM, character);
                context.startActivity(details);
            }
        }
        if(intent.getAction().equals(ACTION_ADD_ITEM)) {
            Log.i(TAG, "ACTION_ADD_ITEM");
            Intent addItem = new Intent(context, FormActivity.class);
            addItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(addItem);
        }
        if(intent.getAction().equals(ACTION_UPDATE)) {
            Log.i(TAG, "ACTION_UPDATE");

        }

        super.onReceive(context, intent);
    }


}