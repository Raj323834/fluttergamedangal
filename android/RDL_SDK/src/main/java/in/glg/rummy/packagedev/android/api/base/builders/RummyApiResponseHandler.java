package in.glg.rummy.packagedev.android.api.base.builders;

import android.os.Handler;
import android.os.Message;

import in.glg.rummy.packagedev.android.api.base.result.RummyDataResult;

public class RummyApiResponseHandler<T> extends Handler {
    private static final int ENTITY_RESPONSE = 2;
    private static final int STRING_RESPONSE = 1;
    private RummyOnRequestListener listener;

    public RummyApiResponseHandler(RummyOnRequestListener listener) {
        this.listener = listener;
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                this.listener.onRequestStart();
                return;
            case 2:
                this.listener.onRequestResult((RummyDataResult) msg.obj);
                return;
            case 3:
                if (msg.arg1 == 1) {
                    this.listener.onRequestFail((String) msg.obj);
                    return;
                } else if (msg.arg1 == 2) {
                    this.listener.onRequestFail((RummyDataResult) msg.obj);
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }
}
