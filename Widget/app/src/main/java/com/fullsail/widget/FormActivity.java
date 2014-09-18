package com.fullsail.widget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RemoteViews;

/**
 * Created by administrator on 9/16/14.
 */
public class FormActivity extends Activity implements OnClickListener {

    private int mWidgetId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent launcherIntent = getIntent();
        Bundle extras = launcherIntent.getExtras();

        mWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

        if(extras != null) {
            mWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if(mWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            setResult(RESULT_CANCELED);
            finish();
        }

        findViewById(R.id.addItemButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        updateWidget();
        close();
    }

    private void updateWidget() {
        if(mWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            Intent intent = new Intent(this, FormActivity.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetId);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            RemoteViews widgetView = new RemoteViews(getPackageName(), R.layout.widget_layout);
            widgetView.setOnClickPendingIntent(R.id.widgetAddButton, pIntent);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            appWidgetManager.updateAppWidget(mWidgetId, widgetView);
        }
    }

    private void close() {
        Intent result = new Intent();
        result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetId);
        setResult(RESULT_OK, result);
        finish();
    }
}
