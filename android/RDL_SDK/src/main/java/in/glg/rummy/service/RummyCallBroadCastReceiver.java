package in.glg.rummy.service;

import android.content.Context;

import java.util.Date;

public class RummyCallBroadCastReceiver extends RummyPhonecallReceiver {
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {
    }

    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
    }

    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
    }

    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    protected void onMissedCall(Context ctx, String number, Date start) {
    }
}
