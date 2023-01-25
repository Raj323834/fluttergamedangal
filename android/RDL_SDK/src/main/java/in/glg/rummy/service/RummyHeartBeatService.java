package in.glg.rummy.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import in.glg.rummy.RummyApplication;
import in.glg.rummy.api.RummyOnResponseListener;
import in.glg.rummy.api.response.RummyLoginResponse;
import in.glg.rummy.engine.RummyGameEngine;
import in.glg.rummy.exceptions.RummyGameEngineNotRunning;
import in.glg.rummy.models.RummyEvent;
import in.glg.rummy.models.RummyHeartBeatEvent;
import in.glg.rummy.utils.RummyTLog;
import in.glg.rummy.utils.RummyUtils;

public class RummyHeartBeatService extends Service {
    public static final long NOTIFY_INTERVAL = 1000;
    private RummyOnResponseListener heartBeatListener = new RummyOnResponseListener(RummyEvent.class) {
        public void onResponse(Object response)
        {
        }
    };
    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    class TimeDisplayTimerTask extends TimerTask {

        class C17361 implements Runnable {
            C17361() {
            }

            public void run() {
                RummyHeartBeatService.this.sendHeartBeat();
            }
        }

        TimeDisplayTimerTask() {
        }

        public void run() {
            RummyHeartBeatService.this.mHandler.post(new C17361());
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
        } else {
            this.mTimer = new Timer();
        }
        this.mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    private void sendHeartBeat() {
        RummyLoginResponse userData = (RummyApplication.getInstance()).getUserData();
        RummyHeartBeatEvent request = new RummyHeartBeatEvent();
        request.setEventName("HEART_BEAT");
        request.setPlayerIn("new_lobby");
        request.setMsg_uuid(RummyUtils.generateUuid());
        request.setNickName(userData.getNickName());
        try {
            Log.d("flow", "Sending HeartBeat");
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(getApplicationContext(), RummyUtils.getObjXMl(request), this.heartBeatListener);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            RummyTLog.d("HeartBEat", "EXTRA_TIME" + gameEngineNotRunning.getLocalizedMessage());
        }
    }
}
