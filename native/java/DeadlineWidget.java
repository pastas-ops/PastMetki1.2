package com.pastmetki.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class DeadlineWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context c, AppWidgetManager m, int[] ids) {
        for (int id : ids) update(c, m, id);
    }

    static void update(Context c, AppWidgetManager m, int id) {
        RemoteViews v = new RemoteViews(c.getPackageName(), R.layout.widget_deadline);
        v.setTextViewText(R.id.msg, text(PmData.dl(c)));
        v.setOnClickPendingIntent(R.id.msg,
            PendingIntent.getActivity(c, 2, new Intent(c, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE));
        m.updateAppWidget(id, v);
    }

    static String text(int n) {
        if (n < 0) return "Войдите в PastMetka";
        if (n == 0) return "На ближайшие 3 дня задач нет 🎉";
        int a = n % 10, b = n % 100;
        String w = (a == 1 && b != 11) ? "задачу" : (a >= 2 && a <= 4 && (b < 12 || b > 14)) ? "задачи" : "задач";
        return "У вас на следующие 3 дня нужно сделать " + n + " " + w;
    }
}
