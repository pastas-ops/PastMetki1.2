package com.pastmetki.app;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;

public class PmData {
    static SharedPreferences sp(Context c) {
        return c.getSharedPreferences("pm_widget", Context.MODE_PRIVATE);
    }

    static void save(Context c, String chats, String notes, int dl) {
        SharedPreferences s = sp(c);
        SharedPreferences.Editor e = s.edit();
        e.putString("chats", chats);
        e.putInt("dl", dl);
        // пока в виджете есть непринятые отметки, список заметок не перезаписываем
        if ("[]".equals(s.getString("pending", "[]"))) {
            e.putString("notes", notes);
        }
        e.apply();
        refresh(c);
    }

    static String get(Context c, String k) {
        return sp(c).getString(k, "[]");
    }

    static int dl(Context c) {
        return sp(c).getInt("dl", -1);
    }

    static String takePending(Context c) {
        SharedPreferences s = sp(c);
        String p = s.getString("pending", "[]");
        s.edit().putString("pending", "[]").apply();
        return p;
    }

    static void refresh(Context c) {
        AppWidgetManager m = AppWidgetManager.getInstance(c);
        int[] a = m.getAppWidgetIds(new ComponentName(c, ChatsWidget.class));
        if (a.length > 0) m.notifyAppWidgetViewDataChanged(a, R.id.list);
        int[] b = m.getAppWidgetIds(new ComponentName(c, NotesWidget.class));
        if (b.length > 0) m.notifyAppWidgetViewDataChanged(b, R.id.list);
        int[] d = m.getAppWidgetIds(new ComponentName(c, DeadlineWidget.class));
        for (int id : d) DeadlineWidget.update(c, m, id);
    }
}
