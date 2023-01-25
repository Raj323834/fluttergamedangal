package in.glg.rummy.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import java.util.Date;

public abstract class RummyPhonecallReceiver extends BroadcastReceiver {
    private static Date callStartTime;
    private static boolean isIncoming;
    private static int lastState = 0;
    private static String savedNumber;

    protected abstract void onIncomingCallAnswered(Context context, String str, Date date);

    protected abstract void onIncomingCallEnded(Context context, String str, Date date, Date date2);

    protected abstract void onIncomingCallReceived(Context context, String str, Date date);

    protected abstract void onMissedCall(Context context, String str, Date date);

    protected abstract void onOutgoingCallEnded(Context context, String str, Date date, Date date2);

    protected abstract void onOutgoingCallStarted(Context context, String str, Date date);

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            return;
        }
        String stateStr = intent.getExtras().getString("state");
        String number = intent.getExtras().getString("incoming_number");
        int state = 0;
        if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            state = 0;
        } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            state = 2;
        } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            state = 1;
        }
        onCallStateChanged(context, state, number);
    }

    public void onCallStateChanged(Context context, int state, String number) {
        if (lastState != state) {
            switch (state) {
                case 0:
                    if (lastState != 1) {
                        if (!isIncoming) {
                            onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
                            break;
                        } else {
                            onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                            break;
                        }
                    }
                    onMissedCall(context, savedNumber, callStartTime);
                    break;
                case 1:
                    isIncoming = true;
                    callStartTime = new Date();
                    savedNumber = number;
                    onIncomingCallReceived(context, number, callStartTime);
                    break;
                case 2:
                    if (lastState == 1) {
                        isIncoming = true;
                        callStartTime = new Date();
                        onIncomingCallAnswered(context, savedNumber, callStartTime);
                        break;
                    }
                    isIncoming = false;
                    callStartTime = new Date();
                    onOutgoingCallStarted(context, savedNumber, callStartTime);
                    break;
            }
            lastState = state;
        }
    }
}
