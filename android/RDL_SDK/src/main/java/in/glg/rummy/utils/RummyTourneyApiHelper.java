package in.glg.rummy.utils;

import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RummyTourneyApiHelper {
    final private static String apiURL = RummyUtils.getApiSeverAddress() + "api/v1/tournament-events/";
    
    public static StringRequest registerTourney(String tourneyId, RummyApiCallHelper.ApiResponseListener apiResponseListener) {
        
        Map<String, String> params = getRegisterParams(tourneyId);
        return RummyApiCallHelper.postApiResponseTourney(apiURL, params, apiResponseListener);
    }
    
    public static StringRequest deRegisterTourney(String tourneyId,
                                                  RummyApiCallHelper.ApiResponseListener apiResponseListener) {
        
        Map<String, String> params = getDeRegisterParams(tourneyId);
        return RummyApiCallHelper.postApiResponse(apiURL, params, apiResponseListener);
    }
    
    public static StringRequest rebuyin(String tourneyId,
                                        String tournamentLevel, String amount,
                                        RummyApiCallHelper.ApiResponseListener apiResponseListener) {
        Map<String, String> params = getRebuyInParams(tourneyId, tournamentLevel, amount);
        return RummyApiCallHelper.postApiResponse(apiURL, params, apiResponseListener);
    }
    
    public static StringRequest rebuyInCancel(String tourneyId,
                                              String tournamentLevel, String amount, String orderId,
                                              RummyApiCallHelper.ApiResponseListener apiResponseListener) {
        Map<String, String> params = getRebuyInCancelParams(tourneyId, tournamentLevel, amount,orderId);
        return RummyApiCallHelper.postApiResponse(apiURL, params, apiResponseListener);
    }
    public static StringRequest rebuyInSuccess(String tourneyId,
                                              String tournamentLevel, String amount, String orderId,
                                              RummyApiCallHelper.ApiResponseListener apiResponseListener) {
        Map<String, String> params = getRebuyInSuccessParams(tourneyId, tournamentLevel, amount,orderId);
        return RummyApiCallHelper.postApiResponse(apiURL, params, apiResponseListener);
    }
    
    private static Map<String, String> getRebuyInParams(String tourneyId, String tournamentLevel, String amount) {
        
        Map<String, String> params = new HashMap<>();
        params.put("event_name", "rebuyin");
        params.put("tournament_id", tourneyId);
        params.put("tournament_level", tournamentLevel);
        params.put("rebuy_amount", amount);
        return params;
    }
    
    private static Map<String, String> getRebuyInCancelParams(String tourneyId, String tournamentLevel, String amount, String orderId) {
        
        Map<String, String> params = new HashMap<>();
        params.put("event_name", "rebuyincancel");
        params.put("tournament_id", tourneyId);
        params.put("tournament_level", tournamentLevel);
        params.put("rebuy_amount", amount);
        params.put("order_id", orderId);
        return params;
    }
    
    private static Map<String, String> getRebuyInSuccessParams(String tourneyId, String tournamentLevel, String amount, String orderId) {
        
        Map<String, String> params = new HashMap<>();
        params.put("event_name", "rebuyinsuccess");
        params.put("tournament_id", tourneyId);
        params.put("tournament_level", tournamentLevel);
        params.put("rebuy_amount", amount);
        params.put("order_id", orderId);
        return params;
    }
    
    private static Map<String, String> getRegisterParams(String tourneyId) {
        Map<String, String> params = new HashMap<>();
        params.put("event_name", "register");
        params.put("tournament_id", tourneyId);
        return params;
    }
    
    private static Map<String, String> getDeRegisterParams(String tourneyId) {
        Map<String, String> params = new HashMap<>();
        params.put("event_name", "deregister");
        params.put("tournament_id", tourneyId);
        return params;
    }
}
