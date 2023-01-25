package in.glg.rummy.packagedev.android.api.base.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Button;

import in.glg.rummy.packagedev.android.api.base.builders.BaseBuilder;
import in.glg.rummy.packagedev.android.api.base.requests.RummyAbstractDataRequest;
import in.glg.rummy.packagedev.cmdpatters.RummyResource;

public class RummyDataApiAsyncTask extends RummyAbstractDataApiAsyncTask {
    private ProgressDialog progressDialog;
    private Button mSubmit;

    public RummyDataApiAsyncTask(boolean showToasts, Context context, ProgressDialog progressDialog, Button mSubmit) {
        super(context, showToasts);
        this.progressDialog = progressDialog;
        this.mSubmit = mSubmit;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if (this.progressDialog != null) {
            this.progressDialog.show();
        }
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (this.progressDialog != null) {
            this.progressDialog.cancel();
        }
        this.mSubmit.setEnabled(true);
    }

    protected Void doInBackground(RummyAbstractDataRequest... params) {
        super.doInBackground(params);
        if (params != null && params[0] != null && this.isConnected) {
            params[0].requestDelegate.execute(params[0]);
        } else if (this.context != null) {
            ((BaseBuilder) params[0].requestDelegate).sendOnFailMessage(this.context.getString(RummyResource.string.no_internet_connection));
        }
        return null;
    }
}
