package in.glg.rummy.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.math.NumberUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.glg.rummy.R;
import in.glg.rummy.RummyApplication;
import in.glg.rummy.api.RummyOnResponseListener;
import in.glg.rummy.api.requests.RummyLoginRequest;
import in.glg.rummy.api.response.RummyLoginResponse;
import in.glg.rummy.engine.RummyGameEngine;
import in.glg.rummy.enums.RummyGameEvent;
import in.glg.rummy.exceptions.RummyGameEngineNotRunning;
import in.glg.rummy.utils.RummyConstants;
import in.glg.rummy.utils.RummyPrefManager;
import in.glg.rummy.utils.RummyTLog;
import in.glg.rummy.utils.RummyUtils;

import static in.glg.rummy.utils.RummyConstants.ACCESS_TOKEN_REST;
import static in.glg.rummy.utils.RummyConstants.SERVER_IP_REST;
import static in.glg.rummy.utils.RummyConstants.UNIQUE_ID_REST;
import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

public class RummyJsonActivity extends RummyBaseActivity {

    private RummyOnResponseListener listener = new RummyOnResponseListener(RummyLoginResponse.class) {
        public void onResponse(Object var1) {
            RummyLoginResponse var2 = (RummyLoginResponse)var1;
            RummyJsonActivity.this.onLoginResult(var2);
        }
    };

    ProgressBar progressBarH;

    private static final String TAG = "rummyJsonActivity";
    TextView tv_json;
    @Override
    protected int getLayoutResource() {
        return R.layout.rummy_activity_json;
    }

    @Override
    protected int getToolbarResource() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RummyApplication.inItsingleton(this.getApplicationContext());

        if(RummyApplication.getInstance() != null)
        {
            RummyApplication.getInstance().clearJoinedTablesIds();
            RummyApplication.getInstance().getEventList().clear();
            RummyApplication.getInstance().registerEventBus();
        }

        RummyUtils.HOME_BACK_PRESSED = false;
        RummyApplication.userNeedsAuthentication = true;

        EventBus.getDefault().register(this);
        tv_json = findViewById(R.id.tv_json);
        progressBarH = findViewById(R.id.pBar);



        Intent in = getIntent();
        if(in.hasExtra("merchant_id") && in.hasExtra("userid") && in.hasExtra("user_name"))
        {
            merchant_id = in.getStringExtra("merchant_id");
            userId = in.getStringExtra("userid");
            user_name = in.getStringExtra("user_name");

        }
        else
        {
            merchant_id = RummyPrefManager.getString(RummyJsonActivity.this,"NOSTRO_MERCHANT_ID","");
            userId = RummyPrefManager.getString(RummyJsonActivity.this,"NOSTRO_USER_ID","");
        }

        if(in.hasExtra("email"))
        {
            email = in.getStringExtra("email");
        }

        if(in.hasExtra("phone"))
        {
            phone = in.getStringExtra("phone");
        }
        if(RummyUtils.isNetworkAvailable(this))
        {
            initProgressThread();
            getCheckSumFromServer();
        }
        else
        {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setContentView(R.layout.rummy_dialog_no_internet);
            ((Button) dialog.findViewById(R.id.ok_btn)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }




    }

    public void initProgressThread()
    {
        final Thread thread = new Thread(new Runnable() {
            int progress_value = 0;
            @Override
            public void run() {
                try {
                    while (progress_value != 100)
                    {
                        Log.d(TAG, "calling splash thread");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int percent_tmp =  RummyJsonActivity.this.progressBarH.getProgress();

                                if(percent_tmp == 100)
                                {
                                    progress_value = 100;
                                }
                                else
                                {

                                        int percent_int = percent_tmp;
                                        percent_int = percent_int + 1;
                                        RummyJsonActivity.this.progressBarH.setProgress(percent_int);
                                        progress_value = percent_int;


                                }
                            }
                        });
                        sleep(70);
                    }


                } catch (Exception e) {
                    progress_value = 100;
                    e.printStackTrace();

                }
            }
        });
        thread.start();
    }

    private void getCheckSumFromServer()
    {
        String url = RummyUtils.getApiSeverAddress()+ RummyUtils.checkSumUrl;
        RequestQueue queue = Volley.newRequestQueue(RummyJsonActivity.this);


        Map<String, String> params = new HashMap<String, String>();
        params.clear();
        params.put("merchant_id", merchant_id + "");
        params.put("userid", userId + "");
        if(RummyUtils.isStringEmpty(user_name))
        {
            params.put("username",user_name+ "");
        }
        params.put("email",email);
        params.put("phone",phone);
        params.put("client_type", RummyUtils.CLIENT_TYPE);
        params.put("device_type", getDeviceType());
        params.put("device_id", RummyUtils.getDeviceID(getBaseContext()));
        params.put("version", RummyUtils.getVersionCode(getBaseContext()));
        params.put("device_brand", RummyUtils.getDeviceName());
        params.put("serial_number", RummyUtils.getSerialNumber());
        params.put("imei_number", "");
        if(RummyInstance.getInstance().getAssetsFolderName() != null && !RummyInstance.getInstance().getAssetsFolderName().equalsIgnoreCase(""))
        {
            params.put("profile_logo",RummyInstance.getInstance().getAssetsFolderName());
        }




        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        RummyTLog.e(TAG, response.toString());
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");
                            if (status.equalsIgnoreCase("Success")) {
                                String checksum_str = response.getString("checksum_str");
                                 getUserInfoFromServer(checksum_str);
                            }
                            else
                            {
                                if(!isFinishing())
                                {
                                    showGenericDialog(RummyJsonActivity.this,""+message);
                                }

                            }
                        } catch (Exception e) {
                            RummyTLog.e(TAG, "JsonException" + e.toString());
                            if(!isFinishing())
                            {
                                showGenericDialog(RummyJsonActivity.this,"Something went wrong, Please try again.");
                            }

                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                RummyTLog.e("vikas","calling error response");


                RummyTLog.e(TAG, "Error: " + error.getMessage());

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    RummyTLog.e(TAG, "Error: " +"Volley Time out error");
                } else if (error instanceof AuthFailureError) {
                    RummyTLog.e(TAG, "Error: " +"Volley Authentication error");
                } else if (error instanceof ServerError) {
                    RummyTLog.e(TAG, "Error: " +"Volley server error");
                } else if (error instanceof NetworkError) {
                    RummyTLog.e(TAG, "Error: " +"Volley Network error");
                } else if (error instanceof ParseError) {
                    RummyTLog.e(TAG, "Error: " +"Volley parse error");
                }
                if(!isFinishing())
                {
                    showGenericDialog(RummyJsonActivity.this,"Something went wrong, Please try again.");
                }

            }
            }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                60000, //TIMEOUT INTERVAL (Default: 2500ms)
                1,    //No.Of Retries (Default: 1)
                1));  //BackOff Multiplier (Default: 1.0)

        RummyTLog.e(TAG,"URl = "+jsonObjReq.toString() +" params = "+params.toString());

        queue.add(jsonObjReq);






    }


    private void getUserInfoFromServer(final String checksum_str)
    {
        String url = RummyUtils.getApiSeverAddress()+ RummyUtils.getUserInfoUrl;
        RequestQueue queue = Volley.newRequestQueue(RummyJsonActivity.this);

        Map<String, String> params = new HashMap<String, String>();
        params.clear();
        params.put("merchant_id", merchant_id + "");
        params.put("userid", userId + "");
        params.put("checksum",checksum_str);
        if(RummyUtils.isStringEmpty(user_name))
        {
          params.put("username",user_name+ "");
        }
        params.put("email",email);
        params.put("phone",phone);
        params.put("client_type", RummyUtils.CLIENT_TYPE);
        params.put("device_type", getDeviceType());
        params.put("device_id", RummyUtils.getDeviceID(getBaseContext()));
        params.put("version", RummyUtils.getVersionCode(getBaseContext()));
        params.put("device_brand", RummyUtils.getDeviceName());
        params.put("serial_number", RummyUtils.getSerialNumber());
        params.put("imei_number", "");
        if(RummyInstance.getInstance().getAssetsFolderName() != null && !RummyInstance.getInstance().getAssetsFolderName().equalsIgnoreCase(""))
        {
            params.put("profile_logo",RummyInstance.getInstance().getAssetsFolderName());
        }



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        RummyTLog.e(TAG, response.toString());
                        try {
                            String status = response.getString("status");
                          //  String rummy_message = response.getString("rummy_message");
                            if (status.equalsIgnoreCase("Success")) {

                                loginAttempt(response);
                            }
                            else
                            {
                                if(!isFinishing())
                                {
                                    showGenericDialog(RummyJsonActivity.this,"Something went wrong, Please try again.");
                                }

                            }
                        } catch (Exception e) {
                            RummyTLog.e(TAG, "JsonException" + e.toString());
                            if(!isFinishing())
                            {
                                showGenericDialog(RummyJsonActivity.this,"Something went wrong, Please try again.");
                            }

                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                RummyTLog.e("vikas","calling error response");


                RummyTLog.e(TAG, "Error: " + error.getMessage());

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    RummyTLog.e(TAG, "Error: " +"Volley Time out error");
                } else if (error instanceof AuthFailureError) {
                    RummyTLog.e(TAG, "Error: " +"Volley Authentication error");
                } else if (error instanceof ServerError) {
                    RummyTLog.e(TAG, "Error: " +"Volley server error");
                } else if (error instanceof NetworkError) {
                    RummyTLog.e(TAG, "Error: " +"Volley Network error");
                } else if (error instanceof ParseError) {
                    RummyTLog.e(TAG, "Error: " +"Volley parse error");
                }
                if(!isFinishing())
                {
                    showGenericDialog(RummyJsonActivity.this,"Something went wrong, Please try again.");
                }

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                60000, //TIMEOUT INTERVAL (Default: 2500ms)
                1,    //No.Of Retries (Default: 1)
                1));  //BackOff Multiplier (Default: 1.0)

        RummyTLog.e(TAG,"URl = "+jsonObjReq.toString() +" params = "+params.toString());

        queue.add(jsonObjReq);

}

    private void gotoLogin() {
        RummyTLog.w(TAG, "gotoLogin");
        finish();
    }

    private void gotoMain(boolean isFromIamBack) {
        Intent intent = new Intent(this, RummyHomeActivity.class);
        intent.putExtra("isIamBack", isFromIamBack);
        launchNewActivity(intent, false);
        finish();
    }


    private void onLoginResult(RummyLoginResponse response) {
        if (response == null) {
            return;
        }
        if (response.isSuccessful()) {
            RummyPrefManager.saveBool(getApplicationContext(), "isLoggedIn", true);
            RummyApplication.getInstance().setUserData(response);
            runTimer();
            String tableId = RummyApplication.getInstance().getUserData().getTableId();
            boolean isIamBack = false;
            if (tableId != null && tableId.length() > 0) {
                isIamBack = true;
            }
            gotoMain(isIamBack);
            checkIamBack(RummyApplication.getInstance());
            return;
        }
        else
        {
            gotoLogin();

            RummyTLog.e("vikas",response.toString());
        }


    }


    @Subscribe
    public void onMessageEvent(RummyGameEvent event) {
        if (event.name().equalsIgnoreCase("SERVER_CONNECTED")) {
            RummyTLog.e("vikas","engine connected");
          //  postEngine();
                try {
                if (null == jsonUserInfo) {
                    if(!isFinishing())
                    {
                        showGenericDialog(RummyJsonActivity.this,"Something went wrong, Please try again.");
                    }

                }
                else
                {

                    emptyDynamicAffiliateStrings();
                    RummyConstants.doLogin = true;
                    doLogin(jsonUserInfo.getString("unique_id"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
                RummyTLog.e(TAG, "postEngine of Login -->> " + e.toString());
            }

        }
    }


    private void doLogin(String uniqueId)
    {
        RummyTLog.e("vikas", "Session ID: "+uniqueId);

        RummyLoginRequest r = new RummyLoginRequest();
        r.setEmail("None");
        r.setPassword("None");
        r.setSessionId(uniqueId);
        r.setDeviceType(getDeviceType());
        r.setUuid(RummyUtils.generateUuid());
        r.setDeviceId(getDeviceType());
        r.setBuildVersion(RummyUtils.getVersionNumber(this));
        String loginReust = RummyUtils.getObjXMl(r);

        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(getApplicationContext(), loginReust, this.listener);
        } catch (RummyGameEngineNotRunning e) {
            RummyTLog.e(TAG, "Error in Splash Screen : doLogin");
        }
    }



    private void engineInit() {
        RummyTLog.e("engineInit", "engineInit");

        if (!RummyGameEngine.getInstance().isSocketConnected()) {
            RummyTLog.e("vikas", "socket not connected");
            setNetworkConnectionTimer();
        }
        else
        {
            RummyTLog.e("vikas", "socket connected");
            try {
                if (null == jsonUserInfo) {
                    if(!isFinishing())
                    {
                        showGenericDialog(RummyJsonActivity.this,"Something went wrong, Please try again");
                    }

                }
                else
                {

                    emptyDynamicAffiliateStrings();
                    RummyConstants.doLogin = true;
                    doLogin(jsonUserInfo.getString("unique_id"));
                }


            } catch (Exception e) {
                e.printStackTrace();
                RummyTLog.e(TAG, "postEngine of Login -->> " + e.toString());
            }
        }





    }


    private void startGameEngine() {
        RummyTLog.e("vikas", "calling start game engine");
        resetNetworkHandler();
        RummyGameEngine.getInstance().start();
    }

    private Handler mNetworkHandler;

    private void resetNetworkHandler() {
        if (this.mNetworkHandler != null) {
            this.mNetworkHandler.removeCallbacks((Runnable) null);
            this.mNetworkHandler.removeCallbacksAndMessages((Object) null);
            this.mNetworkHandler = null;
        }
    }

    private void setNetworkConnectionTimer() {
        RummyTLog.e("vikas", "calling set network connection timer");
        this.resetNetworkHandler();
        this.mNetworkHandler = new Handler();
        this.mNetworkHandler.postDelayed(new Runnable() {
            public void run() {
                startGameEngine();
            }
        }, 1);
    }

    String merchant_id = "";
    String userId = "";
    String user_name = "";
    String email = "";
    String phone = "";





    JSONObject jsonUserInfo;

    private void loginAttempt(JSONObject response) {
        try {
            jsonUserInfo = new JSONObject(response.toString());
            RummyPrefManager.saveString(context, ACCESS_TOKEN_REST, jsonUserInfo.getString("token"));
            RummyPrefManager.saveString(context, SERVER_IP_REST, jsonUserInfo.getString("ip"));
            RummyPrefManager.saveString(context, UNIQUE_ID_REST, jsonUserInfo.getString("unique_id"));
            RummyPrefManager.saveString(context, "username", jsonUserInfo.getString("username"));
            RummyPrefManager.saveString(context, RummyConstants.CHANGE_USERNAME, jsonUserInfo.getString("change_username"));

            RummyUtils.ENGINE_IP = RummyPrefManager.getString(this, SERVER_IP_REST, "");
            RummyTLog.e("ENGINE_IP", RummyUtils.ENGINE_IP + " $");

            RummyPrefManager.saveString(context, "NOSTRO_MERCHANT_ID", merchant_id);
            RummyPrefManager.saveString(context, "NOSTRO_USER_ID", userId);

            RummyTLog.e("unique_id", jsonUserInfo.getString("unique_id") + "");

          //  updateBalance();
            engineInit();
        } catch (Exception e) {
            RummyTLog.e(TAG, "EXP: Parsing success response of Login -->> " + e.toString());
        }
    }







    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }


    private void unregisterEventBus() {
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        this.unregisterEventBus();
    }

}
