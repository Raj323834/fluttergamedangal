package in.glg.rummy.api;

import in.glg.rummy.api.requests.RummySignUpRequest;

public class RummyUrlBuilder {
    public static final String BASE_URL = "https://www.tajrummy.com/";
    public static final String LIVE_WEBSITE = "www.tajrummy.com";
    public static final String TEST_WEBSITE = "ir.devserv.rummy_info";

    public static String getSignUpURL(RummySignUpRequest request) {
        return String.format("%smobilesignup/?Username=%s&Password=%s&Email=%s&channel=%s&subchannel=%s", new Object[]{BASE_URL, request.userName, request.password, request.email, request.channel, request.subChannel});
    }

    public static String getForgotPasswordUrl(RummySignUpRequest request) {
        return String.format("%smobilepasswordreset/?email=%s", new Object[]{BASE_URL, request.email});
    }

    public static String getContactUsUrl() {
//        return String.format("%sapi/support/", new Object[]{BASE_URL});
        return String.format("%sapi/v1/support/", new Object[]{BASE_URL});
    }
}