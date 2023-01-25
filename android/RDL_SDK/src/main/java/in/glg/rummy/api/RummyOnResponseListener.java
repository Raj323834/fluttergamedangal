package in.glg.rummy.api;

import android.os.Handler;
import android.os.Message;

import in.glg.rummy.utils.RummyUtils;

public abstract class RummyOnResponseListener<T> extends Handler {
    public static final int SERVER_RESONSE = 1000;
    private final Class<? extends T> entity;

    public abstract void onResponse(T t);

    public RummyOnResponseListener(Class<? extends T> entity) {
        this.entity = entity;
    }

    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what)
        {
            case 1000:
                onResponse(RummyUtils.getObject((String) msg.obj, this.entity));
                return;
            default:
                return;
        }
    }

    public Message getResponseMessage(String response) {
        return obtainMessage(1000, response);
    }
}
