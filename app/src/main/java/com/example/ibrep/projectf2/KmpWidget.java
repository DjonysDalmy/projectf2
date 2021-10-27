package com.example.ibrep.projectf2;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.ibrep.projectf2.Pages.KMP;

/**
 * Implementation of App Widget functionality.
 */
public class KmpWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, KMP.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widkmp);
            views.setOnClickPendingIntent(R.id.kmpView, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}

