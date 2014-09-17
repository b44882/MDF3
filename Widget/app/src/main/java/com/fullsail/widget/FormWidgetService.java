package com.fullsail.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class FormWidgetService extends RemoteViewsService {

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new FormWidgetViewFactory(getApplicationContext());
	}
}