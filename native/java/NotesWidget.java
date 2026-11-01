package com.pastmetki.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;
import org.json.JSONArray;
import org.json.JSONObject;

public class NotesWidget extends AppWidgetProvider {
    static final String TOGGLE = "com.pastmetki.app.TOGGLE";

    @Override
    public void onUpdate(Context c, AppWidgetManager m, int[] ids) {
        for (int id : ids) {
            RemoteViews v = new RemoteViews(c.getPackageName(), R.layout.widget_list);
            v.setTextViewText(R.id.title, "📝 Заметки");
            Intent s = new Intent(c, NotesService.class);
            s.setData(Uri.parse(s.toUri(Intent.URI_INTENT_SCHEME)));
            v.setRemoteAdapter(R.id.list, s);
            v.setEmptyView(R.id.list, R.id.empty);
            Intent t = new Intent(c, NotesWidget.class);
            t.setAction(TOGGLE);
            v.setPendingIntentTemplate(R.id.list,
                PendingIntent.getBroadcast(c, 0, t, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE));
            v.setOnClickPendingIntent(R.id.title,
                PendingIntent.getActivity(c, 1, new Intent(c, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE));
            m.updateAppWidget(id, v);
        }
    }

    @Override
    public void onReceive(Context c, Intent i) {
        super.onReceive(c, i);
        if (!TOGGLE.equals(i.getAction())) return;
        long id = i.getLongExtra("id", 0);
        try {
            JSONArray a = new JSONArray(PmData.get(c, "notes"));
            JSONObject hit = null;
            for (int k = 0; k < a.length(); k++) {
                JSONObject o = a.getJSONObject(k);
                if (o.optLong("i") == id) { hit = o; break; }
            }
            if (hit == null) return;
            boolean nd = !hit.optBoolean("d");
            hit.put("d", nd);
            JSONArray open = new JSONArray();
            JSONArray done = new JSONArray();
            for (int k = 0; k < a.length(); k++) {
                JSONObject o = a.getJSONObject(k);
                if (o.optBoolean("d")) done.put(o); else open.put(o);
            }
            for (int k = 0; k < done.length(); k++) open.put(done.getJSONObject(k));
            SharedPreferences s = PmData.sp(c);
            JSONArray p = new JSONArray(s.getString("pending", "[]"));
            JSONObject op = new JSONObject();
            op.put("id", id);
            op.put("done", nd);
            p.put(op);
            s.edit().putString("notes", open.toString()).putString("pending", p.toString()).apply();
            PmData.refresh(c);
        } catch (Exception e) {
            // ignore
        }
    }
}
