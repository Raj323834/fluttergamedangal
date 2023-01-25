package in.glg.rummy.utils;

/**
 * Created by GridLogic on 31/8/17.
 */

import android.content.Context;
import android.os.Vibrator;

public class RummyVibrationManager {
    private static RummyVibrationManager instance;
    private boolean mIsVibrate;
    private Vibrator mVibrator;
    private long[] pattern = new long[]{60, 100, 200, 300, 400};

    public static synchronized RummyVibrationManager getInstance() {
        RummyVibrationManager vibrationManager;
        synchronized (RummyVibrationManager.class) {
            vibrationManager = instance;
        }
        return vibrationManager;
    }

    public static void CreateInstance() {
        if (instance == null) {
            instance = new RummyVibrationManager();
        }
    }

    public void InitializeVibrator(Context context) throws Exception {
        this.mVibrator = (Vibrator) context.getSystemService("vibrator");
    }

    public void setVibration(boolean isVibrate) {
        this.mIsVibrate = isVibrate;
    }

    public void vibrate(int repeat) {
        if (this.mIsVibrate) {
            this.mVibrator.vibrate(100);
        }
    }
}