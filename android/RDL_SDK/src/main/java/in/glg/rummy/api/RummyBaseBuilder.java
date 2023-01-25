package in.glg.rummy.api;

/**
 * Created by GridLogic on 31/8/17.
 */

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;

import java.lang.reflect.Type;
import java.security.KeyStore;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;
import in.glg.rummy.packagedev.android.api.base.builders.BaseBuilder;
import in.glg.rummy.packagedev.android.api.base.builders.RummyOnRequestListener;
import in.glg.rummy.packagedev.android.api.base.builders.json.RummyCommonJsonBuilder;
import in.glg.rummy.packagedev.android.api.base.result.RummyDataResult;
import in.glg.rummy.utils.RummyTLog;

public abstract class RummyBaseBuilder<T> extends BaseBuilder<T> {
    public RummyBaseBuilder(RummyOnRequestListener listener, Class<T> entity) {
        super(listener, (Class) entity);
        try {
            AsyncHttpClient asyncHttpClient = getAsyncHttpClient();
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore);
            socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            asyncHttpClient.setSSLSocketFactory(socketFactory);
        } catch (Exception e) {
            RummyTLog.e("RummyBaseBuilder ", e.getMessage());
        }
    }

    public RummyBaseBuilder(RummyOnRequestListener listener, Type type) {
        super(listener, type);
    }

    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
        if (isResultOk(statusCode)) {
            RummyDataResult<T> dataresult = new RummyDataResult();
            dataresult.statusCode = statusCode;
            dataresult.successful = isResultOk(statusCode);
            sendOnResultMessage(dataresult);
            return;
        }
        sendOnFailMessage("Server Error");
    }

    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable t) {
        super.onFailure(statusCode, headers, errorResponse, t);
        if (errorResponse != null) {
            RummyDataResult<Error> dataresult = new RummyDataResult();
            dataresult.statusCode = statusCode;
            dataresult.successful = isResultOk(statusCode);
            try {
                dataresult.entity = new RummyCommonJsonBuilder().getEntityForJson(new String(errorResponse), Error.class);
                sendOnFailMessage((RummyDataResult) dataresult);
            } catch (Exception e) {
                sendOnFailMessage(e.getLocalizedMessage());
            }
        }
    }

    protected Header[] getDefautHeaders() {
        return new Header[]{new BasicHeader("Content-Type", "application/x-www-form-urlencoded;"), new BasicHeader("charset", "utf-8")};
    }
}