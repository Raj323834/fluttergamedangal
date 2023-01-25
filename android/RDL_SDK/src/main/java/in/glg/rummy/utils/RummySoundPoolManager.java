package in.glg.rummy.utils;

/**
 * Created by GridLogic on 31/8/17.
 */

import android.content.Context;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class RummySoundPoolManager {
    private static RummySoundPoolManager instance;
    private HashMap<Integer, SoundSampleEntity> hashMap;
    private boolean isPlaySound;
    private SoundPool soundPool;
    private List<Integer> sounds;

    private class SoundSampleEntity {
        private boolean isLoaded;
        private int sampleId;

        public SoundSampleEntity(int sampleId, boolean isLoaded) {
            this.isLoaded = isLoaded;
            this.sampleId = sampleId;
        }

        public int getSampleId() {
            return this.sampleId;
        }

        public void setSampleId(int sampleId) {
            this.sampleId = sampleId;
        }

        public boolean isLoaded() {
            return this.isLoaded;
        }

        public void setLoaded(boolean isLoaded) {
            this.isLoaded = isLoaded;
        }
    }

    public static synchronized RummySoundPoolManager getInstance() {
        RummySoundPoolManager soundPoolManager;
        synchronized (RummySoundPoolManager.class) {
            soundPoolManager = instance;
        }
        return soundPoolManager;
    }

    public static void CreateInstance() {
        if (instance == null) {
            instance = new RummySoundPoolManager();
        }
    }

    public List<Integer> getSounds() {
        return this.sounds;
    }

    public void setSounds(List<Integer> sounds) {
        this.sounds = sounds;
    }

    public void InitializeSoundPool(Context context, final RummyISoundPoolLoaded callback) throws Exception {
        if (this.sounds == null || this.sounds.size() == 0) {
            throw new Exception("Sounds not set");
        }
        int index;
        this.soundPool = new SoundPool(10, 3, 100);
        this.soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                SoundSampleEntity entity = RummySoundPoolManager.this.getEntity(sampleId);
                if (entity != null) {
                    entity.setLoaded(status == 0);
                }
                if (sampleId == RummySoundPoolManager.this.maxSampleId()) {
                    callback.onSuccess();
                }
            }
        });
        int length = this.sounds.size();
        this.hashMap = new HashMap();
        for (index = 0; index < length; index++) {
            this.hashMap.put(this.sounds.get(index), new SoundSampleEntity(0, false));
        }
        index = 0;
        for (Entry<Integer, SoundSampleEntity> entry : this.hashMap.entrySet()) {
            index++;
            ((SoundSampleEntity) entry.getValue()).setSampleId(this.soundPool.load(context, ((Integer) entry.getKey()).intValue(), index));
        }
    }

    private int maxSampleId() {
        int sampleId = 0;
        for (Entry<Integer, SoundSampleEntity> entry : this.hashMap.entrySet()) {
            SoundSampleEntity entity = (SoundSampleEntity) entry.getValue();
            if (entity.getSampleId() > sampleId) {
                sampleId = entity.getSampleId();
            }
        }
        return sampleId;
    }

    private SoundSampleEntity getEntity(int sampleId) {
        for (Entry<Integer, SoundSampleEntity> entry : this.hashMap.entrySet()) {
            SoundSampleEntity entity = (SoundSampleEntity) entry.getValue();
            if (entity.getSampleId() == sampleId) {
                return entity;
            }
        }
        return null;
    }

    public boolean isPlaySound() {
        return this.isPlaySound;
    }

    public void setPlaySound(boolean isPlaySound) {
        this.isPlaySound = isPlaySound;
    }

    public void playSound(int resourceId) {
        if (isPlaySound()) {
            SoundSampleEntity entity = (SoundSampleEntity) this.hashMap.get(Integer.valueOf(resourceId));
            if (entity.getSampleId() > 0 && entity.isLoaded()) {
                this.soundPool.play(entity.getSampleId(), 0.99f, 0.99f, 1, 0, 1.0f);
            }
        }
    }

    public void release() {
        if (this.soundPool != null) {
            this.soundPool.release();
        }
    }

    public void stop() {
        if (this.soundPool != null) {
            for (Entry<Integer, SoundSampleEntity> entry : this.hashMap.entrySet()) {
                this.soundPool.stop(((SoundSampleEntity) entry.getValue()).getSampleId());
            }
        }
    }
}