package in.glg.rummy.packagedev.android.api.base.builders;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public abstract class RummyBaseAsyncHttpResponseHandler extends AsyncHttpResponseHandler {
    public static final String TAG = RummyBaseAsyncHttpResponseHandler.class.getSimpleName();

    public void onStart() {
        super.onStart();
        Log.d(TAG, "start()");
    }

    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
        Log.d(TAG, "onSuccess Response () :: statusCode  " + statusCode + "   " + new String(response));
    }

    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable t) {
        Log.d(TAG, "onfailure Response () ::  statusCode  " + statusCode + "   " + new String(errorResponse) + "  " + TAG.toString());
    }

    public void onProgress(long bytesWritten, long totalSize) {
        super.onProgress(bytesWritten, totalSize);
    }
}
