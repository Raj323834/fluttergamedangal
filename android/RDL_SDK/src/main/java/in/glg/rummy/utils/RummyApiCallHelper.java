package in.glg.rummy.utils;

import static in.glg.rummy.utils.RummyConstants.ACCESS_TOKEN_REST;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import in.glg.rummy.NetworkProvider.RummyVolleySingleton;
import in.glg.rummy.RummyApplication;

public class RummyApiCallHelper {
    private static final String TAG = RummyApiCallHelper.class.getName();
    private static Gson gson = new Gson();
    public static StringRequest getApiResponse(
            String apiURL, ApiResponseListener apiResponseListener
    ) {
        StringRequest stringRequest = null;
        try {
            //  trackPaymentInitiatedEventWE();
            RummyTLog.d("ApiCallHelper GET", apiURL);
            RequestQueue queue = RummyVolleySingleton.getInstance(RummyApplication.getInstance().getContext()).getRequestQueue();
            final String TOKEN = RummyPrefManager.getString(RummyApplication.getInstance().getContext(),
                    RummyConstants.ACCESS_TOKEN_REST, "");
            
            
            stringRequest = new StringRequest(Request.Method.GET, apiURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            RummyTLog.d(RummyApiCallHelper.class.getName(), "Response: " + response.toString());
                            
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                apiResponseListener.response(RummyResultFactory.success(jsonResponse));
                                
                            } catch (Exception e) {
                                RummyTLog.e(TAG, "json error = " + e.toString());
                                apiResponseListener.response(RummyResultFactory.error("Something went wrong"));
                                
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            RummyTLog.e(TAG, "Error Resp: " + error.toString());
                            if (error.toString().contains("NoConnectionError")) {
                                apiResponseListener.response(RummyResultFactory.error("Oops! Connectivity Problem. Please try again!"));
                                
                            }
                            NetworkResponse response = error.networkResponse;
                            if (error instanceof ServerError && response != null) {
                                try {
                                    String res = new String(response.data,
                                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    RummyTLog.d(TAG, "Error: " + res);
                                    if (res != null) {
                                        try {
                                            JSONObject json = new JSONObject(res.toString());
                                            if (json.getString("status").equalsIgnoreCase("Error")) {
                                                String message = json.getString("message");
                                                if (message != null) {
                                                    apiResponseListener.response(RummyResultFactory.error(message));
                                                } else {
                                                    apiResponseListener.response(RummyResultFactory.error("Something went wrong"));
                                                }
                                            }
                                        } catch (Exception e) {
                                            RummyTLog.e(TAG, "EXP: parsing error for login -->> " + e.toString());
                                            apiResponseListener.response(RummyResultFactory.error("Something went wrong"));
                                        }
                                    }
                                } catch (UnsupportedEncodingException e1) {
                                    // Couldn't properly decode data to string
                                    e1.printStackTrace();
                                    apiResponseListener.response(RummyResultFactory.error("Something went wrong"));
                                }
                            }else if(error instanceof  com.android.volley.TimeoutError){
                                apiResponseListener.response(RummyResultFactory.error("Time out!"));
    
                            }else {
                                apiResponseListener.response(RummyResultFactory.error("Something went wrong"));
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Token " + TOKEN);
                    return headers;
                }
                
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }
            };
            
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, //TIMEOUT INTERVAL (Default: 2500ms)
                    2,    //No.Of Retries (Default: 1)
                    2));  //BackOff Multiplier (Default: 1.0)
            
            //add request to queue
            queue.add(stringRequest);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringRequest;
    }
    
    public static StringRequest postApiResponse(String apiURL, Map<String, String> map, ApiResponseListener apiResponseListener) {
        StringRequest stringRequest = null;
        
        try {
            RequestQueue queue = RummyVolleySingleton.getInstance(RummyApplication.getInstance().getContext()).getRequestQueue();
            final String TOKEN = RummyPrefManager.getString(RummyApplication.getInstance().getContext(),
                    RummyConstants.ACCESS_TOKEN_REST, "");
    
            RummyTLog.d("ApiCallHelper POST", apiURL);
            RummyTLog.d("ApiCallHelper POST params", gson.toJson(map));
            
            
            stringRequest = new StringRequest(Request.Method.POST, apiURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            RummyTLog.d(TAG, "Response: " + response.toString());
                            
                            try {
                                JSONObject mainObject = new JSONObject(response.toString());
                                apiResponseListener.response(RummyResultFactory.success(mainObject));
                                
                            } catch (Exception e) {
                                RummyTLog.e(TAG, "EXP: doFlowback -->> " + e.toString());
                                apiResponseListener.response(RummyResultFactory.error("Something went wrong"));
                                
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            RummyTLog.d(TAG, "Error Resp: " + error.toString());
                            
                            if (error.toString().contains("NoConnectionError"))
                                apiResponseListener.response(RummyResultFactory.error("Oops! Connectivity Problem. Please try again!"));
                            
                            NetworkResponse response = error.networkResponse;
                            if (error instanceof ServerError && response != null) {
                                try {
                                    String res = new String(response.data,
                                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    RummyTLog.d(TAG, "Error: " + res);
                                    if (res != null) {
                                        try {
                                            JSONObject json = new JSONObject(res.toString());
                                            if (json.getString("status").equalsIgnoreCase("Error"))
                                                apiResponseListener.response(RummyResultFactory.error(json.getString("message")));
                                        } catch (Exception e) {
                                            RummyTLog.e(TAG, "EXP: parsing error for login -->> " + e.toString());
                                            apiResponseListener.response(RummyResultFactory.error("Something went wrong"));
                                            
                                        }
                                    }
                                } catch (UnsupportedEncodingException e1) {
                                    // Couldn't properly decode data to string
                                    e1.printStackTrace();
                                    apiResponseListener.response(RummyResultFactory.error("Something went wrong"));
                                }
                            }else if(error instanceof  com.android.volley.TimeoutError){
                                apiResponseListener.response(RummyResultFactory.error("Time out!"));
    
                            }else {
                                apiResponseListener.response(RummyResultFactory.error("Something went wrong"));
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Token " + TOKEN);
                    return headers;
                }
                
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }
                
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return map;
                }
            };
            
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, //TIMEOUT INTERVAL (Default: 2500ms)
                    2,    //No.Of Retries (Default: 1)
                    2));  //BackOff Multiplier (Default: 1.0)
            
            //add request to queue
            queue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            apiResponseListener.response(RummyResultFactory.error("Something went wrong"));
        }
        return stringRequest;
        
    }

    public static StringRequest postApiResponseTourney(String apiURL, Map<String, String> map, ApiResponseListener apiResponseListener) {
        StringRequest stringRequest = null;

        try {
            RequestQueue queue = RummyVolleySingleton.getInstance(RummyApplication.getInstance().getContext()).getRequestQueue();
            final String TOKEN = RummyPrefManager.getString(RummyApplication.getInstance().getContext(),
                    RummyConstants.ACCESS_TOKEN_REST, "");

            RummyTLog.d("ApiCallHelper POST", apiURL);
            RummyTLog.d("ApiCallHelper POST params", gson.toJson(map));


            stringRequest = new StringRequest(Request.Method.POST, apiURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            RummyTLog.d(TAG, "Response: " + response.toString());

                            try {
                                JSONObject mainObject = new JSONObject(response.toString());
                                apiResponseListener.response(RummyResultFactory.success(mainObject));

                            } catch (Exception e) {
                                RummyTLog.e(TAG, "EXP: doFlowback -->> " + e.toString());
                                apiResponseListener.response(RummyResultFactory.error("Something went wrong"));

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            RummyTLog.d(TAG, "Error Resp: " + error.toString());

                            if (error.toString().contains("NoConnectionError"))
                                apiResponseListener.response(RummyResultFactory.error("Oops! Connectivity Problem. Please try again!"));

                            NetworkResponse response = error.networkResponse;
                            if (error instanceof ServerError && response != null) {
                                try {
                                    String res = new String(response.data,
                                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    RummyTLog.d(TAG, "Error: " + res);
                                    if (res != null) {
                                        try {
                                            JSONObject json = new JSONObject(res.toString());
                                            if (json.getString("status").equalsIgnoreCase("Error"))
                                                apiResponseListener.response(RummyResultFactory.error(json.toString()));
                                        } catch (Exception e) {
                                            RummyTLog.e(TAG, "EXP: parsing error for login -->> " + e.toString());
                                            apiResponseListener.response(RummyResultFactory.error("Something went wrong"));

                                        }
                                    }
                                } catch (UnsupportedEncodingException e1) {
                                    // Couldn't properly decode data to string
                                    e1.printStackTrace();
                                    apiResponseListener.response(RummyResultFactory.error("Something went wrong"));
                                }
                            }else if(error instanceof  com.android.volley.TimeoutError){
                                apiResponseListener.response(RummyResultFactory.error("Time out!"));

                            }else {
                                apiResponseListener.response(RummyResultFactory.error("Something went wrong"));
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Token " + TOKEN);
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return map;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, //TIMEOUT INTERVAL (Default: 2500ms)
                    2,    //No.Of Retries (Default: 1)
                    2));  //BackOff Multiplier (Default: 1.0)

            //add request to queue
            queue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            apiResponseListener.response(RummyResultFactory.error("Something went wrong"));
        }
        return stringRequest;

    }
    public interface ApiResponseListener {
        void response(RummyApiResult<JSONObject> result);
    }
}
