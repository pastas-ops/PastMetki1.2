package com.pastmetki.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class ChatsWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context c, AppWidgetManager m, int[] ids) {
        for (int id : ids) {
            RemoteViews v = new RemoteViews(c.getPackageName(), R.layout.widget_list);
            v.setTextViewText(R.id.title, "💬 Чаты");
            Intent s = new Intent(c, ChatsService.class);
            s.setData(Uri.parse(s.toUri(Intent.URI_INTENT_SCHEME)));
            v.setRemoteAdapter(R.id.list, s);
            v.setEmptyView(R.id.list, R.id.empty);
            Intent t = new Intent(c, MainActivity.class);
            t.setAction(Intent.ACTION_VIEW);
            v.setPendingIntentTemplate(R.id.list,
                PendingIntent.getActivity(c, 0, t, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE));
            v.setOnClickPendingIntent(R.id.title,
                PendingIntent.getActivity(c, 1, new Intent(c, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE));
            m.updateAppWidget(id, v);
        }
    }
}
