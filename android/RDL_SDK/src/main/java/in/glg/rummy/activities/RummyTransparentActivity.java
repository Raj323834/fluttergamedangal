package in.glg.rummy.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import androidx.appcompat.app.ActionBar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;

import in.glg.rummy.R;
import in.glg.rummy.RummyApplication;
import in.glg.rummy.api.RummyOnResponseListener;
import in.glg.rummy.api.requests.RummyLoginRequest;
import in.glg.rummy.api.response.RummyLoginResponse;
import in.glg.rummy.engine.RummyGameEngine;
import in.glg.rummy.enums.RummyGameEvent;
import in.glg.rummy.exceptions.RummyGameEngineNotRunning;
import in.glg.rummy.utils.RummyPrefManager;
import in.glg.rummy.utils.RummyConstants;
import in.glg.rummy.utils.RummyTLog;
import in.glg.rummy.utils.RummyUtils;

public class RummyTransparentActivity extends RummyBaseActivity
{
   private static final String TAG = RummyTransparentActivity.class.getSimpleName();

   private RummyOnResponseListener listener = new RummyOnResponseListener(RummyLoginResponse.class) {
      public void onResponse(Object var1) {
         RummyLoginResponse var2 = (RummyLoginResponse)var1;
         RummyTransparentActivity.this.onLoginResult(var2);
      }
   };

   private final Handler mHideHandler = new Handler();

   private final Runnable mHidePart2Runnable = new Runnable() {
      @SuppressLint({"InlinedApi"})
      public void run() {
      }
   };

   private final Runnable mHideRunnable = new Runnable() {
      public void run() {
         RummyTransparentActivity.this.hide();
      }
   };

   private Handler mNetworkHandler;
   private final Runnable mShowPart2Runnable = new Runnable() {
      public void run() {
         ActionBar var1 = RummyTransparentActivity.this.getSupportActionBar();
         if(var1 != null) {
            var1.show();
         }

      }
   };
   private RummyApplication mRummyApp;
   private boolean mVisible;

   private void delayedHide(int delayMillis) {
      this.mHideHandler.removeCallbacks(this.mHideRunnable);
      this.mHideHandler.postDelayed(this.mHideRunnable, (long)delayMillis);
   }

   private void doLogin(String email, String password) {
      Log.e(TAG, "Doing Login via credentials");
      RummyLoginRequest r = new RummyLoginRequest();
      r.setEmail(email);
      r.setPassword(password);
      r.setSessionId("None");
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

   private void gotoLogin() {
      finish();
     RummyInstance.getInstance().close();
   }

   private void gotoMain(boolean isFromIamBack) {
      RummyUtils.isFromSocketDisconnection = true;
      Intent intent = new Intent(this, RummyHomeActivity.class);
      intent.putExtra("isIamBack", isFromIamBack);
      intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
      launchNewActivity(intent, false);
      LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("CLOSE_ACTIVITIES"));
      finish();
   }

   private void hide() {
      this.mVisible = false;
      this.mHideHandler.removeCallbacks(this.mShowPart2Runnable);
      this.mHideHandler.postDelayed(this.mHidePart2Runnable, 300L);
   }

   private void init() {
      RummyGameEngine.getInstance().stop();
      this.mRummyApp = RummyApplication.getInstance();
      if(this.mRummyApp != null)
      {
         this.mRummyApp.clearJoinedTablesIds();
         this.mRummyApp.getEventList().clear();
      }

      this.registerEventBus();
   }

   private void launchHomeActivity() {
      UnsupportedEncodingException e;
      if (RummyPrefManager.getBool(getApplicationContext(), "isLoggedIn", false))
      {
         String uniqueId = RummyPrefManager.getString(getBaseContext(), RummyConstants.UNIQUE_ID_REST, "");
         doLogin(uniqueId);
         return;
      }
      gotoLogin();
   }

   private void doLogin(String uniqueId)
   {
      Log.e(TAG, "Doing Login via unique ID");
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

   private void onLoginResult(RummyLoginResponse response) {
      if (response == null) {
         return;
      }
      if (response.isSuccessful()) {
         RummyPrefManager.saveBool(getApplicationContext(), "isLoggedIn", true);
         if(this.mRummyApp != null)
         this.mRummyApp.setUserData(response);
         String tableId = this.mRummyApp.getUserData().getTableId();
         boolean isIamBack = false;
         if (tableId != null && tableId.length() > 0) {
            isIamBack = true;
         }
         gotoMain(isIamBack);
         runTimer();
         checkIamBack(this.mRummyApp);
         if(RummyLoadingActivity.getInstance() != null)
         {
            RummyLoadingActivity.getInstance().finish();
         }
         finish();
         return;
      }
      gotoLogin();
   }

   private void registerEventBus() {
      if(!EventBus.getDefault().isRegistered(this)) {
         EventBus.getDefault().register(this);
      }
   }

   private void resetNetworkHandler() {
      if(this.mNetworkHandler != null) {
         this.mNetworkHandler.removeCallbacks((Runnable)null);
         this.mNetworkHandler.removeCallbacksAndMessages((Object)null);
         this.mNetworkHandler = null;
      }
   }

   private void setNetworkConnectionTimer() {
      this.resetNetworkHandler();
      this.mNetworkHandler = new Handler();
      this.mNetworkHandler.postDelayed(new Runnable() {
         public void run() {
            RummyTransparentActivity.this.startGameEngine();
         }
      }, 1000L);
   }

   @SuppressLint({"InlinedApi"})
   private void show() {
      this.mVisible = true;
      this.mHideHandler.removeCallbacks(this.mHidePart2Runnable);
      this.mHideHandler.postDelayed(this.mShowPart2Runnable, 300L);
   }

   private void startGameEngine() {
      if(RummyUtils.isNetworkAvailable(this)) {
         this.resetNetworkHandler();
         RummyGameEngine.getInstance().start();
      }
      else
      {
         setNetworkConnectionTimer();
      }

   }


   private void unregisterEventBus() {
      if(EventBus.getDefault().isRegistered(this)) {
         EventBus.getDefault().unregister(this);
      }
   }

   protected int getLayoutResource() {
      return R.layout.rummy_activity_transparent;
   }

   protected int getToolbarResource() {
      return 0;
   }

   public void goToNextScreen() {
      this.launchHomeActivity();
   }

   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if(RummyUtils.isAppInDebugMode())
      {
         Log.d(TAG,"vikas TransparentActivity Launched");
      }

      setUpFullScreen();
      init();
   }

   protected void onDestroy() {
      super.onDestroy();
      this.unregisterEventBus();
   }

   @Subscribe
   public void onMessageEvent(RummyGameEvent var1) {
      if(var1.name().equalsIgnoreCase("SERVER_CONNECTED")) {
         if(RummyUtils.isAppInDebugMode())
         {
            Log.d(TAG,"vikas msg event get server connected");
         }
         (new SetupAsyncTask()).execute(new Void[0]);
      }

   }

   protected void onPostCreate(Bundle savedInstanceState) {
      super.onPostCreate(savedInstanceState);
      delayedHide(100);
   }

   protected void onStart() {
      super.onStart();
      if(RummyUtils.isAppInDebugMode())
      {
         Log.d(TAG,"vikas onStart() calling");
      }
      this.startGameEngine();
   }

   protected void onStop() {
      super.onStop();
      this.resetNetworkHandler();
   }

   public class SetupAsyncTask extends AsyncTask<Void, Void, Void> {
      protected void onPreExecute() {
         super.onPreExecute();
         RummyTransparentActivity.this.resetNetworkHandler();
      }

      protected Void doInBackground(Void... params) {
         SystemClock.sleep(500);
         return null;
      }

      protected void onPostExecute(Void result) {
         super.onPostExecute(result);
         if (RummyGameEngine.getInstance().isSocketConnected()) {
            RummyTransparentActivity.this.goToNextScreen();
         } else {
            RummyTransparentActivity.this.startGameEngine();
         }
      }
   }
}
