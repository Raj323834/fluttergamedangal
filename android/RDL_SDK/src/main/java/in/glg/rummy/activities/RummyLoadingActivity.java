package in.glg.rummy.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.glg.rummy.R;
import in.glg.rummy.RummyApplication;
import in.glg.rummy.utils.RummyUtils;

public class RummyLoadingActivity extends RummyBaseActivity {
   private static final String TAG = RummyLoadingActivity.class.getSimpleName();
   private boolean isBackpressed = false;
   private Handler mHeartBeatHandler;
   private Handler mNetworkHandler;
   private RelativeLayout mRootLayout;
   private static RummyLoadingActivity mloadingactivity = null;

   private void launchSplashScreen() {
      Log.d(TAG,"vikas calling launchSplashScreen");
      this.launchNewActivity(new Intent(this, RummyTransparentActivity.class), false);
      finish();
   }

   private void resetNetworkHandler() {
      if(this.mNetworkHandler != null) {
         this.mNetworkHandler.removeCallbacks((Runnable)null);
         this.mNetworkHandler.removeCallbacksAndMessages((Object)null);
         this.mNetworkHandler = null;
      }
   }

   private void setNetworkConnectionTimer() {
      if(RummyUtils.isAppInDebugMode())
      {
         Log.d(TAG,"vikas calling start Network connection timer");
      }
      this.resetNetworkHandler();
      this.mNetworkHandler = new Handler();
      this.mNetworkHandler.postDelayed(new Runnable() {
         public void run() {
            RummyLoadingActivity.this.startGameEngine();
         }
      }, 2000L);
   }

   private void startGameEngine() {
      if(RummyUtils.isAppInDebugMode())
      {
         Log.d(TAG,"vikas calling start game engine");
      }
      if(RummyUtils.isNetworkAvailable(this)) {
         if(RummyUtils.isAppInDebugMode())
         {
            Log.d(TAG,"vikas network awailable");
         }

         (new SetupAsyncTask()).execute(new Void[0]);

      } else {
         if(RummyUtils.isAppInDebugMode())
         {
            Log.d(TAG,"vikas network not available");
         }
         this.setNetworkConnectionTimer();
      }
   }

   protected int getLayoutResource() {
      return R.layout.rummy_activity_loading;
   }

   protected int getToolbarResource() {
      return 0;
   }

   public void onBackPressed() {
   }

   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      mloadingactivity = this;
      setUpFullScreen();
      if(RummyUtils.isAppInDebugMode())
      {
         Log.d(TAG,"vikas loading screen initialize");
      }
      this.mRootLayout = (RelativeLayout) findViewById(R.id.pop_up_bg_layout);
      TextView messageTv = (TextView) findViewById(R.id.loading_tv);
      RummyApplication app = (RummyApplication.getInstance());
      Bundle bundle = getIntent().getExtras();
      boolean isSocketDisconnected = false;
      if (bundle != null) {
         isSocketDisconnected = bundle.getBoolean("isSocketDisconnected");
      }

      if(RummyUtils.isAppInDebugMode())
      {
         Log.d(TAG,"vikas socket disconnected ="+isSocketDisconnected);
      }

      if (isSocketDisconnected) {
         messageTv.setVisibility(View.VISIBLE);
         if (app != null && app.getJoinedTableIds().size() > 0) {
            messageTv.setText(getString(R.string.socket_discon_msg));
         }
      } else {
         //this.mRootLayout.setBackgroundResource(ContextCompat.getColor(this, R.color.rummy_transparent));
         this.mRootLayout.setBackgroundResource(R.color.rummy_transparent);
         messageTv.setVisibility(View.GONE);
      }
      startGameEngine();
   }

   public class SetupAsyncTask extends AsyncTask<Void, Void, Void> {
      protected void onPreExecute() {
         super.onPreExecute();
         if(RummyUtils.isAppInDebugMode())
         {
            Log.d(TAG,"vikas calling async thread preexecute");
         }
         RummyLoadingActivity.this.resetNetworkHandler();
      }

      protected Void doInBackground(Void... params) {
         Log.d(TAG,"vikas calling async thread do in background");
         SystemClock.sleep(500);
         return null;
      }

      protected void onPostExecute(Void result) {
         super.onPostExecute(result);
         Log.d(TAG,"vikas calling async thread post execute");
         RummyLoadingActivity.this.launchSplashScreen();
      }


   }
   public static Activity getInstance()
   {
      return mloadingactivity;
   }
}
