package in.glg.rummy.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.github.aakira.expandablelayout.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import in.glg.rummy.GameRoom.RummyTableActivity;
import in.glg.rummy.R;
import in.glg.rummy.RummyApplication;
import in.glg.rummy.api.RummyOnResponseListener;
import in.glg.rummy.api.requests.RummyLoginRequest;
import in.glg.rummy.api.response.RummyLoginResponse;
import in.glg.rummy.engine.RummyGameEngine;
import in.glg.rummy.exceptions.RummyGameEngineNotRunning;
import in.glg.rummy.models.RummyEvent;
import in.glg.rummy.models.RummyHeartBeatEvent;
import in.glg.rummy.models.RummyJoinedTable;
import in.glg.rummy.models.RummyLoginResponseRest;
import in.glg.rummy.models.RummyLogoutRequest;
import in.glg.rummy.models.RummyTableCards;
import in.glg.rummy.service.RummyHeartBeatService;
import in.glg.rummy.utils.RummyCommonEventTracker;
import in.glg.rummy.utils.RummyErrorCodes;
import in.glg.rummy.utils.RummyPrefManager;
import in.glg.rummy.utils.RummyConstants;
import in.glg.rummy.utils.RummyTLog;
import in.glg.rummy.utils.RummyUtils;

public abstract class RummyBaseActivity extends AppCompatActivity {
   // private static final String TAG = BaseActivity.class.getSimpleName();
   private static final String TAG = "vikas base activity";

    private static RummyOnResponseListener heartBeatListener = new RummyOnResponseListener(RummyEvent.class) {
        public void onResponse(Object response) {
            RummyUtils.isCardUpdateOnGameRoom = false;
        }
    };
    protected Context context;
    private RummyOnResponseListener listener = new RummyOnResponseListener(RummyLoginResponse.class) {
        public void onResponse(Object response) {
            RummyBaseActivity.this.onLoginResult((RummyLoginResponse) response);
        }
    };
    private RummyOnResponseListener facebookLoginListener = new RummyOnResponseListener(RummyLoginResponse.class) {
        public void onResponse(Object response) {
            RummyBaseActivity.this.onFacebookLoginResult((RummyLoginResponse) response);
        }
    };
    private EditText mEmailView;
    private View mForm;
    private Handler mHeartBeatHandler;
    private boolean mIsFromRgistration = false;
    private EditText mPasswordView;
    private View mProgressView;
    private RummyTableCards mTableCards;
    protected Toolbar toolbar;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 101;

    public static String fb_email = "";
    private String login_method = "FORM";

    class C16327 implements Runnable {
        C16327() {
        }

        public void run() {
            if (RummyGameEngine.getInstance().haveAuthRequest()) {
                RummyBaseActivity.this.sendHeartBeat();
                RummyBaseActivity.this.mHeartBeatHandler.postDelayed(this, RummyHeartBeatService.NOTIFY_INTERVAL);
                return;
            }
            RummyBaseActivity.this.mHeartBeatHandler.removeCallbacks(null);
            RummyBaseActivity.this.mHeartBeatHandler.removeCallbacksAndMessages(null);
        }
    }

    protected abstract int getLayoutResource();

    protected abstract int getToolbarResource();

    protected void setupActionBar() {
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setHomeButtonEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setUpFullScreen() {
    }

    public void navigateToGameRoom() {
        Intent playIntent = new Intent(this, RummyTableActivity.class);
        playIntent.putExtra("iamBack", true);
        startActivity(playIntent);
        finish();
    }

    protected void setTitles(String title, String subTitle) {
        if (this.toolbar != null) {
            this.toolbar.setTitle(title);
            this.toolbar.setSubtitle(subTitle);
        }
    }

    protected void setTitles(int title, int subTitle) {
        if (this.toolbar != null) {
            this.toolbar.setTitle(title);
            this.toolbar.setSubtitle(subTitle);
        }
    }

    protected void setActionBarIcon(int iconRes) {
        this.toolbar.setNavigationIcon(iconRes);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        this.context = this;
       // setToolbar(getToolbarResource());
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void setToolbar(int id) {
        if (id > 0) {
            this.toolbar = (Toolbar) findViewById(id);
            if (this.toolbar != null) {
                setSupportActionBar(this.toolbar);
            }
        }
    }

    public Typeface getTypeFace(String fontName) {
        try {
            return Typeface.createFromAsset(getResources().getAssets(), "fonts/" + fontName);
        } catch (Exception e) {
            RummyTLog.e(TAG, "Exception in getOpenSansLightTypeFace" + e.getMessage());
            return null;
        }
    }

    @SuppressLint("WrongConstant")
    protected void showShortSb(int messageId, View v) {
        Snackbar.make(v, messageId, -1).show();
    }

    @SuppressLint("WrongConstant")
    public void showLongSb(int messageId, View v) {
        try {
            Snackbar snackbar =
                    Snackbar.make(
                            findViewById(android.R.id.content),
                            messageId,
                            Snackbar.LENGTH_SHORT);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e + "");
        }
    }

    @SuppressLint("WrongConstant")
    protected void showIndefiniteSb(int messageId, View v) {
        try {
            Snackbar snackbar =
                    Snackbar.make(
                            findViewById(android.R.id.content),
                            messageId,
                            Snackbar.LENGTH_SHORT);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e + "");
        }
    }

    public void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void launchNewActivity(Intent intent, boolean removeStack) {
        if (removeStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
        startActivity(intent);
    }

    public void launchNewActivityFinishAll(Intent intent, boolean removeStack) {
        if (removeStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        startActivity(intent);
    }

    public void showLobbyScreen() {

        Intent lobbyIntent = new Intent(this, RummyHomeActivity.class);
        lobbyIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(lobbyIntent);
    }

    public void showFragment(Fragment fragment) {
//        Log.e(TAG, "showFragment");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(fragment);
        ft.commitAllowingStateLoss();
//        ft.commitAllowingStateLoss();

    }

    public void hideFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(fragment);
        ft.commitAllowingStateLoss();
    }

    public void removeFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }

    public void removeIamBackFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.remove(fragment);
        ft.commit();
        manager.popBackStack();
    }

    public void checkIamBack(RummyApplication app) {
        app = RummyApplication.getInstance();
        String tableId = app.getUserData().getTableId();
        if (tableId != null) {
            String[] tableIds = tableId.split(",");
            for (String id : tableIds) {
                RummyJoinedTable joinedTable = new RummyJoinedTable();
                joinedTable.setTabelId(id);
                app.setJoinedTableIds(joinedTable);
            }
            if (tableIds.length > 0) {
                navigateToGameRoom();
            }
        }
    }

    public void launchFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(new Bundle());
        fragmentTransaction.add(R.id.content_frame, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void showGenericDialog(Context context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.rummy_dialog_generic);
        ((TextView) dialog.findViewById(R.id.dialog_msg_tv)).setText(message);
        ((Button) dialog.findViewById(R.id.ok_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((ImageView) dialog.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }




    public void doLoginWithEngine(View form, EditText emailView, EditText passwordView, View progressView, String uniqueId) {
        Log.e(TAG, "doLoginWithEngine1");
        this.mProgressView = progressView;
        this.mEmailView = emailView;
        this.mPasswordView = passwordView;
        this.mForm = form;
        showProgress(true);
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
            showIndefiniteSb(R.string.error_restart, this.mForm);
        }
    }

    /*public void doLoginWithEngine(View form, EditText emailView, EditText passwordView, View progressView, String uniqueId, String login_method) {
        Log.e(TAG, "doLoginWithEngine2");
        PrefManager.saveString(getBaseContext(), RummyConstants.SOCIAL_LOGIN, login_method);
        RummyConstants.doLogin = true;
        this.login_method = login_method;
        this.mProgressView = progressView;
        this.mEmailView = emailView;
        this.mPasswordView = passwordView;
        this.mForm = form;
        //showProgress(true);
        LoginRequest r = new LoginRequest();
        r.setEmail("None");
        r.setPassword("None");
        r.setSessionId(uniqueId);
        r.setDeviceType(getDeviceType());
        r.setUuid(Utils.generateUuid());
        r.setDeviceId(getDeviceType());
        r.setBuildVersion(Utils.getVersionNumber(this));
        String loginReust = Utils.getObjXMl(r);
        try {
            GameEngine.getInstance();
            GameEngine.sendRequestToEngine(getApplicationContext(), loginReust, this.listener);
        } catch (GameEngineNotRunning e) {
            showIndefiniteSb(R.string.error_restart, this.mForm);
        }
    }*/

    public void doLoginWithEngine(String uniqueId, String login_method) {
        Log.e(TAG, "doLoginWithEngine3");
        RummyPrefManager.saveString(getBaseContext(), RummyConstants.SOCIAL_LOGIN, login_method);
        RummyConstants.doLogin = true;
        this.login_method = login_method;
        this.mProgressView = null;
        this.mEmailView = null;
        this.mPasswordView = null;
        this.mForm = null;
        //showProgress(true);
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
            RummyGameEngine.sendRequestToEngine(getApplicationContext(), loginReust, this.facebookLoginListener);
        } catch (RummyGameEngineNotRunning e) {
            showIndefiniteSb(R.string.error_restart, this.mForm);
        }
    }

    /*public void doLoginWithEngine(String uniqueId, String login_method, EditText emailView, EditText passwordView) {
        Log.e(TAG, "doLoginWithEngine4");
        this.mEmailView = emailView;
        this.mPasswordView = passwordView;
        this.login_method = login_method;
        LoginRequest r = new LoginRequest();
        r.setEmail("None");
        r.setPassword("None");
        r.setSessionId(uniqueId);
        r.setDeviceType(getDeviceType());
        r.setUuid(Utils.generateUuid());
        r.setDeviceId(getDeviceType());
        r.setBuildVersion(Utils.getVersionNumber(this));
        String loginReust = Utils.getObjXMl(r);
        try {
            GameEngine.getInstance();
            GameEngine.sendRequestToEngine(getApplicationContext(), loginReust, this.listener);
        } catch (GameEngineNotRunning e) {
            //showIndefiniteSb(com.vc.rummyvilla.R.string.error_restart, this.mForm);
        }
    }*/

    private void onLoginResult(RummyLoginResponse response) {
        Log.e("onLoginResult","onLoginResult");
        if (response != null) {
            int code = Integer.parseInt(response.getCode());
            if (code == RummyErrorCodes.SUCCESS) {

                RummyCommonEventTracker.trackLoginEvent(RummyCommonEventTracker.USER_LOGGED_IN, response.getUserId(),this);

                saveCredentials();
                (RummyApplication.getInstance()).setUserData(response);
                Intent lobbyIntent = new Intent(this, RummyHomeActivity.class);
                lobbyIntent.putExtra("isFromReg", isFromRegistration());
                lobbyIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                launchNewActivity(lobbyIntent, true);
                runTimer();
                if (!isFromRegistration()) {
                    checkIamBack(RummyApplication.getInstance());
                }
            } else if (code == RummyErrorCodes.INVALID_PASSWORD) {
                this.mPasswordView.setError(getString(R.string.error_invalid_password));
                this.mPasswordView.requestFocus();
            } else if (code == RummyErrorCodes.INVALID_USER_NAME) {
                this.mEmailView.setError(getString(R.string.error_invalid_email));
                this.mEmailView.requestFocus();
            } else if (code == RummyErrorCodes.WRONG_SESSION_ID) {
                showGenericDialog(context,"Something went wrong");
            } else {
                showLongSb(R.string.unknown_server_error, this.mEmailView);
            }
            showProgress(false);
        }
    }

    private void onFacebookLoginResult(RummyLoginResponse response) {
        Log.e("onFacebookLoginResult","onFacebookLoginResult");
        if (response != null) {
            int code = Integer.parseInt(response.getCode());
            if (code == RummyErrorCodes.SUCCESS) {

                RummyCommonEventTracker.trackLoginEvent(RummyCommonEventTracker.USER_LOGGED_IN, response.getUserId(),this);

                saveCredentials();
                (RummyApplication.getInstance()).setUserData(response);
                runTimer();
                Intent lobbyIntent = new Intent(this, RummyHomeActivity.class);
                lobbyIntent.putExtra("isFromReg", isFromRegistration());
                lobbyIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                launchNewActivity(lobbyIntent, true);
                if (!isFromRegistration()) {
                    checkIamBack(RummyApplication.getInstance());
                }
            } else if (code == RummyErrorCodes.WRONG_SESSION_ID) {
                showGenericDialog(context,"Something went wrong");
            } else {
                showLongSb(R.string.unknown_server_error, this.mEmailView);
            }
            showProgress(false);
        }
    }


    public void doLoginWithUsernamePassword(String username, String password) {
        Log.e(TAG+"@542","doLoginWithUsernamePassword");
        showProgress(true);
        RummyLoginRequest r = new RummyLoginRequest();
        r.setEmail(username);
        r.setPassword(password);
        r.setSessionId("None");
        r.setDeviceType(getDeviceType());
        r.setUuid(RummyUtils.generateUuid());
        r.setDeviceId(getDeviceType());
        r.setBuildVersion(RummyUtils.getVersionCode(this));
        String loginReust = RummyUtils.getObjXMl(r);
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(getApplicationContext(), loginReust, this.listener);
        } catch (RummyGameEngineNotRunning e) {
            showIndefiniteSb(R.string.error_restart, this.mForm);
        }
    }

    public void hideKeyboard(View view) {
        try {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getApplicationWindowToken(), 2);
        } catch (Exception e) {

        }
    }

    @TargetApi(13)
    private void showProgress(final boolean var1) {
        try {
            Log.e(TAG,"showProgress1");
            float var3 = 1.0F;
            byte var6 = 4;
            byte var5 = 0;
            byte var4;
            View var7;
            if (VERSION.SDK_INT >= 13) {
                int var8 = 100;
                var7 = this.mForm;
                if (var1) {
                    var4 = 4;
                } else {
                    var4 = 0;
                }
                Log.e(TAG,"showProgress2");
                var7.setVisibility(var4);
                ViewPropertyAnimator var9 = this.mForm.animate().setDuration((long) var8);
                float var2;
                if (var1) {
                    var2 = 0.0F;
                } else {
                    var2 = 1.0F;
                }

                var9.alpha(var2).setListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator var1x) {
                        Log.e(TAG,"showProgress3");
                        View var3 = RummyBaseActivity.this.mForm;
                        byte var2;
                        if (var1) {
                            var2 = 4;
                        } else {
                            var2 = 0;
                        }
                        if(var3 != null)
                        {
                            var3.setVisibility(var2);
                        }

                    }
                });
                var7 = this.mProgressView;
                if (var1) {
                    var4 = var5;
                } else {
                    var4 = 4;
                }

                Log.e(TAG,"showProgress4");
                if(var7 != null)
                {
                    var7.setVisibility(var4);
                }

                var9 = this.mProgressView.animate().setDuration((long) var8);
                if (var1) {
                    var2 = var3;
                } else {
                    var2 = 0.0F;
                }

                var9.alpha(var2).setListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator var1x) {
                        Log.e(TAG,"showProgress5");
                        View var3 = RummyBaseActivity.this.mProgressView;
                        byte var2;
                        if (var1) {
                            var2 = 0;
                        } else {
                            var2 = 4;
                        }
                        if(var3 != null)
                        {
                            var3.setVisibility(var2);
                        }

                    }
                });
            } else {
                Log.e(TAG,"showProgress6");
                var7 = this.mProgressView;
                if (var1) {
                    var4 = 0;
                } else {
                    var4 = 4;
                }
                if(var7 != null)
                {
                    var7.setVisibility(var4);
                }

                var7 = this.mForm;
                if (var1) {
                    var4 = var6;
                } else {
                    var4 = 0;
                }

                Log.e(TAG,"showProgress7");
                if(var7 != null)
                {
                    var7.setVisibility(var4);
                }

            }
        } catch (Exception e) {
            Log.e(TAG,e+"");
        }

    }

    private void saveCredentials() {
        String userName = "";
        String password = "";
        if (mEmailView != null && mPasswordView != null) {
            userName = this.mEmailView.getText().toString();
            password = this.mPasswordView.getText().toString();
        }

        byte[] userData = new byte[0];
        byte[] passwordData = new byte[0];
        try {
            userData = userName.getBytes("UTF-8");
            passwordData = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String encryptedUserName = Base64.encodeToString(userData, 0);
        String encryptedPassword = Base64.encodeToString(passwordData, 0);
        RummyPrefManager.saveString(getApplicationContext(), "userName", encryptedUserName);
        RummyPrefManager.saveString(getApplicationContext(), "password", encryptedPassword);
        RummyPrefManager.saveBool(getApplicationContext(), "isLoggedIn", true);
    }

    public void setIsFromRegistration(boolean isFromRegistration) {
        this.mIsFromRgistration = isFromRegistration;
    }

    private boolean isFromRegistration() {
        return this.mIsFromRgistration;
    }

    public void showSuccessPopUp() {
        /*final Dialog dialog = new Dialog(this.context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_registarion);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.rummy_show();
        ((Button) dialog.findViewById(R.id.play_now_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
    }

    public boolean isInputValid(String input) {
        return Pattern.compile("^[a-zA-Z ]+$").matcher(input.toString()).matches();
    }

    public void runTimer() {
        disableHearBeat();
        this.mHeartBeatHandler = new Handler();
        this.mHeartBeatHandler.postDelayed(new C16327(), RummyHeartBeatService.NOTIFY_INTERVAL);
    }

    public void sendCardSlots(RummyTableCards tableCards) {

        Log.e("vikas","heart_beat card set");
        RummyUtils.mTableCards = tableCards;
        RummyUtils.isCardUpdateOnGameRoom = true;


       /* this.mTableCards = tableCards;
        RummyLoginResponse userData = (RummyApplication.getInstance()).getUserData();
        RummyHeartBeatEvent request = new RummyHeartBeatEvent();
        request.setEventName("HEART_BEAT");
        request.setPlayerIn("new_lobby");
        request.setMsg_uuid(RummyUtils.generateUuid());
        request.setNickName(userData.getNickName());
        request.setTable(tableCards);
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(getApplicationContext(), RummyUtils.getObjXMl(request), heartBeatListener);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            RummyTLog.d(TAG, "EXTRA_TIME" + gameEngineNotRunning.getLocalizedMessage());
        }*/
    }

    public RummyTableCards getTableCards() {
        return this.mTableCards;
    }

    private void sendHeartBeat() {
       /* if (RummyGameEngine.getInstance().isSocketConnected()) {
            RummyLoginResponse userData = (RummyApplication.getInstance()).getUserData();
            RummyHeartBeatEvent request = new RummyHeartBeatEvent();
            request.setEventName("HEART_BEAT");
            request.setPlayerIn("new_lobby");
            request.setMsg_uuid(RummyUtils.generateUuid());
            request.setNickName(userData.getNickName());
            if (this.mTableCards != null) {
                request.setTable(this.mTableCards);
            }
            request.setTimestamp(String.valueOf(System.currentTimeMillis()));
            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(getApplicationContext(), RummyUtils.getObjXMl(request), heartBeatListener);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                RummyTLog.d(TAG, "EXTRA_TIME" + gameEngineNotRunning.getLocalizedMessage());
            }
            this.mTableCards = null;
        }*/


        if(RummyGameEngine.getInstance().isSocketConnected() && RummyUtils.isCardUpdateOnGameRoom && RummyUtils.mTableCards != null)
        {
            Log.e("vikas","heart_beat sending cards");
            RummyUtils.isCardUpdateOnGameRoom = false;
            RummyLoginResponse userData = (RummyApplication.getInstance()).getUserData();
            RummyHeartBeatEvent request = new RummyHeartBeatEvent();
            request.setEventName("HEART_BEAT");
            request.setPlayerIn("new_lobby");
            request.setMsg_uuid(RummyUtils.generateUuid());
            if(userData != null)
                request.setNickName(userData.getNickName());
            request.setTable(RummyUtils.mTableCards);
            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(getApplicationContext(), RummyUtils.getObjXMl(request), heartBeatListener);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                RummyTLog.d(TAG, "EXTRA_TIME" + gameEngineNotRunning.getLocalizedMessage());
            }
        }
        else
        {
           // Log.e("vikas","heart_beat sending false");
        }
    }

    public void disableHearBeat() {
        if (this.mHeartBeatHandler != null) {
            this.mHeartBeatHandler.removeCallbacks(null);
            this.mHeartBeatHandler.removeCallbacksAndMessages(null);
        }
    }

    public String getDeviceType() {
        if (getResources().getBoolean(R.bool.isTablet)) {
            return "Tablet";
        }
        return "Mobile";
    }

    public void handleOtherLogin() {
        disableHearBeat();
        RummyPrefManager.saveBool(getApplicationContext(), "isLoggedIn", false);
        RummyLogoutRequest request = new RummyLogoutRequest();
        request.setCommand("logout");
        request.setMsg_uuid(RummyUtils.generateUuid());
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(getApplicationContext(), RummyUtils.getObjXMl(request), null);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            RummyTLog.e("Error", "logout" + gameEngineNotRunning.getLocalizedMessage());
        }


        if(RummyLoadingActivity.getInstance() != null)
        {
            RummyLoadingActivity.getInstance().finish();
        }

        this.finish();
    }

  /*  public String getIMEI() {
        Log.d(TAG, "Getting IMEI");
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECEIVE_SMS},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            return "";
        }
        Log.e(TAG, telephonyManager.getDeviceId()+"");
        if(null == telephonyManager.getDeviceId()){
            return "";
        }
        return telephonyManager.getDeviceId();
    }*/

    public void createLoginResponseObject(String response) {
        try {
            RummyPrefManager.saveString(getBaseContext(), RummyConstants.LOGIN_RESPONSE_REST, response);
            Gson gson;
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("MM/dd/yyyy hh:mm aa");
            gson = gsonBuilder.create();
            RummyLoginResponseRest loginResponseRest = gson.fromJson(response, RummyLoginResponseRest.class);


            RummyPrefManager.saveString(getBaseContext(), RummyConstants.PLAYER_USER_ID, String.valueOf(loginResponseRest.getPlayerid()));

            RummyPrefManager.saveString(getBaseContext(), RummyConstants.PLAYER_MOBILE, String.valueOf(loginResponseRest.getMobile()));

            RummyCommonEventTracker.trackUserProfile(loginResponseRest,getBaseContext());

        } catch (Exception e) {
            Log.w(TAG, e.getMessage());
        }
    }

    public void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    public void invisibleView(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    public void emptyDynamicAffiliateStrings() {
        RummyPrefManager.saveString(getApplicationContext(), RummyConstants.DYNAMIC_AFFILIATE_ID, "");
        RummyPrefManager.saveString(getApplicationContext(), RummyConstants.DYNAMIC_CAMPAIGN_ID, "");
        RummyPrefManager.saveString(getApplicationContext(), RummyConstants.DYNAMIC_CLICK_ID, "");
        RummyPrefManager.saveString(getApplicationContext(), RummyConstants.UTM_STRING, "");

        //Log.w(LoginActivity.class.getName(), "Clearing affiliate data from "+source);
    }

    public boolean checkAndAskMessagePermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {

            if (context.checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
                return false;
            }
            return true;
        }
        return true;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
