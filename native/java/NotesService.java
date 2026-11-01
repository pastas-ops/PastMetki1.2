package com.pastmetki.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import org.json.JSONArray;
import org.json.JSONObject;

public class NotesService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new Factory(getApplicationContext());
    }

    static class Factory implements RemoteViewsFactory {
        final Context c;
        JSONArray a = new JSONArray();

        Factory(Context c) { this.c = c; }

        @Override public void onCreate() {}

        @Override public void onDataSetChanged() {
            try { a = new JSONArray(PmData.get(c, "notes")); } catch (Exception e) { a = new JSONArray(); }
        }

        @Override public void onDestroy() {}

        @Override public int getCount() { return a.length(); }

        @Override
        public RemoteViews getViewAt(int p) {
            RemoteViews v = new RemoteViews(c.getPackageName(), R.layout.widget_item_note);
            JSONObject o = a.optJSONObject(p);
            if (o == null) return v;
            boolean d = o.optBoolean("d");
            String title = o.optString("t");
            SpannableString s = new SpannableString(title);
            if (d && title.length() > 0) s.setSpan(new StrikethroughSpan(), 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            v.setTextViewText(R.id.t, s);
            v.setTextColor(R.id.t, d ? Color.parseColor("#77777C") : Color.WHITE);
            v.setTextViewText(R.id.ck, d ? "☑" : "☐");
            v.setTextColor(R.id.ck, d ? Color.parseColor("#77777C") : Color.parseColor("#FFC400"));
            Intent f = new Intent();
            f.putExtra("id", o.optLong("i"));
            v.setOnClickFillInIntent(R.id.row, f);
            return v;
        }

        @Override public RemoteViews getLoadingView() { return null; }
        @Override public int getViewTypeCount() { return 1; }
        @Override public long getItemId(int p) {
            JSONObject o = a.optJSONObject(p);
            return o == null ? p : o.optLong("i", p);
        }
        @Override public boolean hasStableIds() { return true; }
    }
}
