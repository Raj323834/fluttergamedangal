package in.glg.rummy.packagedev.android.api.base.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import in.glg.rummy.packagedev.android.api.base.requests.RummyAbstractDataRequest;
import in.glg.rummy.packagedev.android.api.base.utils.RummyNetworkConnection;
import in.glg.rummy.packagedev.cmdpatters.RummyResource;

public abstract class RummyAbstractDataApiAsyncTask extends AsyncTask<RummyAbstractDataRequest, Void, Void> {
    protected Context context;
    protected boolean isConnected;
    protected boolean showToasts;

    protected RummyAbstractDataApiAsyncTask(Context context, boolean showToasts) {
        if (context != null) {
            this.context = context.getApplicationContext();
        }
        this.showToasts = showToasts;
    }

    protected Void doInBackground(RummyAbstractDataRequest... params) {
        if (this.context != null) {
            this.isConnected = RummyNetworkConnection.isConnectedToNetwork(this.context);
            Log.i(getClass().getName(), String.format("isConnected to network = %s", new Object[]{String.valueOf(this.isConnected)}));
        }
        return null;
    }

    protected void onPostExecute(Void result) {
        if (!(this.context == null || this.isConnected || !this.showToasts)) {
            Toast.makeText(this.context, this.context.getString(RummyResource.string.no_internet_connection), 0).show();
        }
        super.onPostExecute(result);
    }
}
