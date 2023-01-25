package in.glg.rummy.api;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Type;

import in.glg.rummy.api.requests.RummySignUpRequest;
import in.glg.rummy.models.RummyEmialUsRequest;
import in.glg.rummy.packagedev.android.api.base.builders.RummyOnRequestListener;
import in.glg.rummy.packagedev.android.api.base.requests.RummyAbstractDataRequest;
import in.glg.rummy.packagedev.android.api.base.requests.RummyDataRequestType;

public class RummyAccountBuilder<T> extends RummyBaseBuilder {

    public enum LoginRequestType implements RummyDataRequestType {
        FORGOT_PASSWORD,
        REGISTER,
        CONTACT_US
    }

    public RummyAccountBuilder(RummyOnRequestListener listener, Class<T> entity) {
        super(listener, (Class) entity);
    }

    public RummyAccountBuilder(RummyOnRequestListener listener, Type type) {
        super(listener, type);
    }

    public void execute(RummyAbstractDataRequest dataRequest) {
        switch ((LoginRequestType) dataRequest.requestType) {
            case FORGOT_PASSWORD:
                forgotPassword((RummySignUpRequest) dataRequest);
                return;
            case REGISTER:
                register((RummySignUpRequest) dataRequest);
                return;
            case CONTACT_US:
                contactUs((RummyEmialUsRequest) dataRequest);
                return;
            default:
                return;
        }
    }

    private void register(RummySignUpRequest dataRequest) {
        dataRequest.responseHandler = this;
        dataRequest.URL = RummyUrlBuilder.getSignUpURL(dataRequest);
        dataRequest.entity = getRequestEntity(new Gson().toJson(dataRequest));
        get(dataRequest);
    }

    private void forgotPassword(RummySignUpRequest dataRequest) {
        dataRequest.responseHandler = this;
        dataRequest.URL = RummyUrlBuilder.getForgotPasswordUrl(dataRequest);
        dataRequest.entity = getRequestEntity(new Gson().toJson(dataRequest));
        get(dataRequest);
    }

    private void contactUs(RummyEmialUsRequest dataRequest) {
        dataRequest.responseHandler = this;
        RequestParams params = new RequestParams();
        params.add("name", dataRequest.getName());
        params.add("message", dataRequest.getMessage());
        params.add("email", dataRequest.getEmail());
        params.add("subject", dataRequest.getSubject());
        params.add("playerid", dataRequest.getPlayerid());
        dataRequest.URL = RummyUrlBuilder.getContactUsUrl();
        postFormData(dataRequest, params);
    }
}
