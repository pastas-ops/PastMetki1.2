package com.pastmetki.app;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "WidgetBridge")
public class WidgetBridgePlugin extends Plugin {

    @PluginMethod
    public void setData(PluginCall call) {
        String chats = call.getString("chats", "[]");
        String notes = call.getString("notes", "[]");
        Integer dl = call.getInt("dl", -1);
        PmData.save(getContext(), chats, notes, dl == null ? -1 : dl);
        call.resolve();
    }

    @PluginMethod
    public void getPending(PluginCall call) {
        JSObject r = new JSObject();
        r.put("ops", PmData.takePending(getContext()));
        call.resolve(r);
    }
}
