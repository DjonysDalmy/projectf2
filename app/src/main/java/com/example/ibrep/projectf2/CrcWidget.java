package com.example.ibrep.projectf2;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.ibrep.projectf2.Pages.CRC;
import com.example.ibrep.projectf2.Pages.KMP;

/**
 * Implementation of App Widget functionality.
 */
public class CrcWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, CRC.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widcrc);
            views.setOnClickPendingIntent(R.id.crcView, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}

