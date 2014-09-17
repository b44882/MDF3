package com.fullsail.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class FormWidgetProvider extends AppWidgetProvider {


    public static final String ACTION_VIEW_DETAILS = "com.fullsail.widget.ACTION_VIEW_DETAILS";
    public static final String EXTRA_ITEM = "com.fullsail.Widget.EXTRA_ITEM";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        for(int i = 0; i < appWidgetIds.length; i++) {

            int widgetId = appWidgetIds[i];

            Intent intent = new Intent(context, FormWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

            RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            widgetView.setRemoteAdapter(R.id.itemListView, intent);
            widgetView.setEmptyView(R.id.itemListView, R.id.empty);

            Intent itemIntent = new Intent(ACTION_VIEW_DETAILS);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, itemIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            widgetView.setPendingIntentTemplate(R.id.itemListView, pIntent);

            appWidgetManager.updateAppWidget(widgetId, widgetView);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(ACTION_VIEW_DETAILS)) {
            CharacterItem character = (CharacterItem)intent.getSerializableExtra(EXTRA_ITEM);
            if(character != null) {
                Intent details = new Intent(context, ItemActivity.class);
                details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                details.putExtra(ItemActivity.EXTRA_ITEM, character);
                context.startActivity(details);
            }
        }

        super.onReceive(context, intent);
    }
}