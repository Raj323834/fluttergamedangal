package in.glg.rummy.packagedev.android.api.base.requests;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public abstract class RummyAbstractDataRequest {
    public transient String URL;
    public transient Context context;
    public transient HttpEntity entity;
    public transient Header[] headers;
    public transient RummyDataRequestDelegate requestDelegate;
    public transient RummyDataRequestType requestType;
    public transient AsyncHttpResponseHandler responseHandler;
}
