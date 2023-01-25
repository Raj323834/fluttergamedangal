package in.glg.rummy.packagedev.android.api.base.builders;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import in.glg.rummy.packagedev.android.api.base.builders.json.RummyCommonJsonBuilder;
import in.glg.rummy.packagedev.android.api.base.requests.RummyAbstractDataRequest;
import in.glg.rummy.packagedev.android.api.base.requests.RummyDataRequestDelegate;
import in.glg.rummy.packagedev.android.api.base.result.RummyDataResult;

public abstract class BaseBuilder<T> extends AsyncHttpResponseHandler implements RummyDataRequestDelegate {
    private static final int ENTITY_RESPONSE = 2;
    private static final int JSON_LIST = 2;
    private static final int JSON_OBJECT = 1;
    private static final int STRING_RESPONSE = 1;
    private static final String TAG = BaseBuilder.class.getSimpleName();
    private final int DEFAULT_TIMEOUT = 120000;
    private int JSON_OBJECT_TYPE = 1;
    protected RummyApiResponseHandler<?> _handler;
    protected AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private Class<T> entity;
    private Type type;

    public BaseBuilder(RummyOnRequestListener listener, Class<T> entity) {
        this._handler = new RummyApiResponseHandler(listener);
        this.asyncHttpClient.setTimeout(120000);
        this.entity = entity;
        this.JSON_OBJECT_TYPE = 1;
    }

    public BaseBuilder(RummyOnRequestListener listener, Type type) {
        this._handler = new RummyApiResponseHandler(listener);
        this.asyncHttpClient.setTimeout(120000);
        this.type = type;
        this.JSON_OBJECT_TYPE = 2;
    }

    public Handler getHandler() {
        return this._handler;
    }

    protected Header[] getRequestHeaders(String headersRaw) {
        int i = 0;
        List<Header> headers = new ArrayList();
        if (headersRaw != null && headersRaw.length() > 3) {
            String[] lines = headersRaw.split("\\r?\\n");
            int length = lines.length;
            while (i < length) {
                String line = lines[i];
                try {
                    String[] kv = line.split("=");
                    if (kv.length != 2) {
                        throw new IllegalArgumentException("Wrong header format, may be 'Key=Value' only");
                    }
                    headers.add(new BasicHeader(kv[0].trim(), kv[1].trim()));
                    i++;
                } catch (Throwable t) {
                    Log.e(TAG, "Not a valid header line: " + line, t);
                }
            }
        }
        return (Header[]) headers.toArray(new Header[headers.size()]);
    }

    protected HttpEntity getRequestEntity(String entitity) {
        try {
            return new StringEntity(entitity);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "cannot create String entity", e);
            return null;
        }
    }

    protected AsyncHttpClient getAsyncHttpClient() {
        return this.asyncHttpClient;
    }

    protected void post(RummyAbstractDataRequest request) {
        try {
            Log.d(TAG, "post() :: URL :: " + request.URL);
            Log.d(TAG, "post() :: entity :: " + request.entity);
            this.asyncHttpClient.post(request.context, request.URL, request.headers, request.entity, null, request.responseHandler);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            sendOnFailMessage(e.toString());
        }
    }

    protected void postFormData(RummyAbstractDataRequest request, RequestParams params) {
        try {
            Log.d(TAG, "post() :: URL :: " + request.URL);
            Log.d(TAG, "post() :: params :: " + params);
            this.asyncHttpClient.post(request.context, request.URL, params, request.responseHandler);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            sendOnFailMessage(e.toString());
        }
    }

    protected void put(RummyAbstractDataRequest request) {
        try {
            Log.d(TAG, "put() :: URL :: " + request.URL);
            Log.d(TAG, "put() :: entity :: " + request.entity);
            for (Header h : request.headers) {
                Log.d(TAG, "put() :: Headers  :: " + h);
            }
            this.asyncHttpClient.put(request.context, request.URL, request.headers, request.entity, null, request.responseHandler);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            sendOnFailMessage(e.toString());
        }
    }

    protected void get(RummyAbstractDataRequest request) {
        try {
            Log.d(TAG, "get() :: URL :: " + request.URL);
            this.asyncHttpClient.get(request.context, request.URL, request.headers, null, request.responseHandler);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            sendOnFailMessage(e.toString());
        }
    }

    public void sendOnResultMessage(RummyDataResult<?> result) {
        Log.d(TAG, "sendOnResultMessage () :: ");
        if (this._handler != null) {
            Message m = Message.obtain();
            m.what = 2;
            m.obj = result;
            this._handler.sendMessage(m);
        }
    }

    public void sendOnFailMessage(RummyDataResult<?> result) {
        if (this._handler != null) {
            Message m = Message.obtain();
            m.what = 3;
            m.obj = result;
            m.arg1 = 2;
            this._handler.sendMessage(m);
        }
    }

    public void sendOnFailMessage(String result) {
        Log.d(TAG, "sendOnFailMessage() :: " + result);
        if (this._handler != null) {
            Message m = Message.obtain();
            m.what = 3;
            m.obj = result;
            m.arg1 = 1;
            this._handler.sendMessage(m);
        }
    }

    public void sendOnStartMessage() {
        Log.d(TAG, "sendOnStartMessage() :: ");
        if (this._handler != null) {
            Message m = Message.obtain();
            m.what = 1;
            this._handler.sendMessage(m);
        }
    }

    public boolean isResultOk(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    public void onStart() {
        super.onStart();
        Log.d(TAG, "start()");
        sendOnStartMessage();
    }

    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
        Log.d(TAG, "onSuccess Response () :: statusCode  " + statusCode + "   " + new String(response));
        if (isResultOk(statusCode)) {
            RummyDataResult<T> dataresult = new RummyDataResult();
            dataresult.statusCode = statusCode;
            dataresult.successful = isResultOk(statusCode);
            if (this.JSON_OBJECT_TYPE == 1) {
                dataresult.entity = new RummyCommonJsonBuilder().getEntityForJson(new String(response), this.entity);
            } else {
                dataresult.entities = new RummyCommonJsonBuilder().getListForJson(new String(response), this.type);
            }
            sendOnResultMessage(dataresult);
            return;
        }
        sendOnFailMessage("Server Error");
    }

    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable t) {
        String error;
        Log.d(TAG, "onfailure Response () ::  statusCode  " + statusCode + "   " + errorResponse + "  " + TAG.toString());
        if (errorResponse != null) {
            error = new String(errorResponse);
        } else {
            error = "Server error";
        }
        sendOnFailMessage(error);
    }
}
