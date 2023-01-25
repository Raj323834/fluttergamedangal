package in.glg.rummy.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class RummyNetworkChangeReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "NetworkChangeReceiver";
    private boolean isConnected = false;
    private OnConnectionChangeListener listner = null;

    public interface OnConnectionChangeListener {
        void onConnectionChange(boolean z);
    }

    public void onReceive(Context context, Intent intent) {
        boolean isConnected = isNetworkAvailable(context);
        if (this.listner != null) {
            this.listner.onConnectionChange(isConnected);
        }
    }

    private boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetwork = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void setOnConnectionChangeListener(Context context) {
        this.listner = (OnConnectionChangeListener) context;
    }
}
