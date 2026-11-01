package com.pastmetki.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class QuickActionsWidget extends AppWidgetProvider {
    private PendingIntent action(Context c, String path, int request) {
        Intent i = new Intent(c, MainActivity.class);
        i.setAction(Intent.ACTION_VIEW);
        i.setData(Uri.parse("pastmetki://" + path));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(c, request, i,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    @Override public void onUpdate(Context c, AppWidgetManager m, int[] ids) {
        for (int id : ids) {
            RemoteViews v = new RemoteViews(c.getPackageName(), R.layout.widget_quick_actions);
            v.setOnClickPendingIntent(R.id.quick_note, action(c, "create-note", 7101));
            v.setOnClickPendingIntent(R.id.quick_task, action(c, "create-task", 7102));
            v.setOnClickPendingIntent(R.id.quick_calendar, action(c, "calendar", 7103));
            m.updateAppWidget(id, v);
        }
    }
}
