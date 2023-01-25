package in.glg.rummy.utils;

/**
 * Created by GridLogic on 31/8/17.
 */

import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;

import java.lang.reflect.Field;

public abstract class RummyClickGuard {
    public static final long DEFAULT_WATCH_PERIOD_MILLIS = 1000;

    private static class ClickGuardImpl extends RummyClickGuard {
        private static final int WATCHING = 0;
        private final Handler mHandler = new Handler(Looper.getMainLooper());
        private final long mWatchPeriodMillis;

        ClickGuardImpl(long watchPeriodMillis) {
            super();
            this.mWatchPeriodMillis = watchPeriodMillis;
        }

        public void watch() {
            this.mHandler.sendEmptyMessageDelayed(0, this.mWatchPeriodMillis);
        }

        public void rest() {
            this.mHandler.removeMessages(0);
        }

        public boolean isWatching() {
            return this.mHandler.hasMessages(0);
        }
    }

    public static abstract class GuardedOnClickListener implements OnClickListener {
        private RummyClickGuard mGuard;
        private OnClickListener mWrapped;

        public abstract boolean onClicked();

        public GuardedOnClickListener() {
            this(1000);
        }

        public GuardedOnClickListener(long watchPeriodMillis) {
            this(RummyClickGuard.newGuard(watchPeriodMillis));
        }

        public GuardedOnClickListener(RummyClickGuard guard) {
            this(null, guard);
        }

        GuardedOnClickListener(OnClickListener onClickListener, RummyClickGuard guard) {
            this.mGuard = guard;
            this.mWrapped = onClickListener;
        }

        public final void onClick(View v) {
            if (this.mGuard.isWatching()) {
                onIgnored();
                return;
            }
            if (this.mWrapped != null) {
                this.mWrapped.onClick(v);
            }
            if (onClicked()) {
                this.mGuard.watch();
            }
        }

        public void onIgnored() {
        }

        public RummyClickGuard getClickGuard() {
            return this.mGuard;
        }
    }

    static class InnerGuardedOnClickListener extends GuardedOnClickListener {
        InnerGuardedOnClickListener(OnClickListener onClickListener, RummyClickGuard guard) {
            super(onClickListener, guard);
        }

        public boolean onClicked() {
            return true;
        }

        public void onIgnored() {
        }
    }

    static abstract class ListenerGetter {
        private static ListenerGetter IMPL;

        private static class ListenerGetterBase extends ListenerGetter {
            private Field mOnClickListenerField = ListenerGetter.getField(View.class, "mOnClickListener");

            ListenerGetterBase() {
            }

            public OnClickListener getOnClickListener(View view) {
                return (OnClickListener) ListenerGetter.getFieldValue(this.mOnClickListenerField, view);
            }
        }

        private static class ListenerGetterIcs extends ListenerGetter {
            private Field mListenerInfoField = ListenerGetter.getField(View.class, "mListenerInfo");
            private Field mOnClickListenerField;

            ListenerGetterIcs() {
                this.mListenerInfoField.setAccessible(true);
                this.mOnClickListenerField = ListenerGetter.getField("android.view.View$ListenerInfo", "mOnClickListener");
            }

            public OnClickListener getOnClickListener(View view) {
                Object listenerInfo = ListenerGetter.getFieldValue(this.mListenerInfoField, view);
                return listenerInfo != null ? (OnClickListener) ListenerGetter.getFieldValue(this.mOnClickListenerField, listenerInfo) : null;
            }
        }

        abstract OnClickListener getOnClickListener(View view);

        ListenerGetter() {
        }

        static {
            if (VERSION.SDK_INT >= 14) {
                IMPL = new ListenerGetterIcs();
            } else {
                IMPL = new ListenerGetterBase();
            }
        }

        static OnClickListener get(View view) {
            return IMPL.getOnClickListener(view);
        }

        static Field getField(Class clazz, String fieldName) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Can't get " + fieldName + " of " + clazz.getName());
            }
        }

        static Field getField(String className, String fieldName) {
            try {
                return getField(Class.forName(className), fieldName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Can't find class: " + className);
            }
        }

        static Object getFieldValue(Field field, Object object) {
            try {
                return field.get(object);
            } catch (IllegalAccessException e) {
                return null;
            }
        }
    }

    public abstract boolean isWatching();

    public abstract void rest();

    public abstract void watch();

    private RummyClickGuard() {
    }

    public static RummyClickGuard newGuard() {
        return newGuard(1000);
    }

    public static RummyClickGuard newGuard(long watchPeriodMillis) {
        return new ClickGuardImpl(watchPeriodMillis);
    }

    public static GuardedOnClickListener wrap(OnClickListener onClickListener) {
        return wrap(newGuard(), onClickListener);
    }

    public static GuardedOnClickListener wrap(long watchPeriodMillis, OnClickListener onClickListener) {
        return newGuard(watchPeriodMillis).wrapOnClickListener(onClickListener);
    }

    public static GuardedOnClickListener wrap(RummyClickGuard guard, OnClickListener onClickListener) {
        return guard.wrapOnClickListener(onClickListener);
    }

    public static RummyClickGuard guard(View view, View... others) {
        return guard(1000, view, others);
    }

    public static RummyClickGuard guard(long watchPeriodMillis, View view, View... others) {
        return guard(newGuard(watchPeriodMillis), view, others);
    }

    public static RummyClickGuard guard(RummyClickGuard guard, View view, View... others) {
        return guard.addAll(view, others);
    }

    public static RummyClickGuard guardAll(Iterable<View> views) {
        return guardAll(1000, (Iterable) views);
    }

    public static RummyClickGuard guardAll(long watchPeriodMillis, Iterable<View> views) {
        return guardAll(newGuard(watchPeriodMillis), (Iterable) views);
    }

    public static RummyClickGuard guardAll(RummyClickGuard guard, Iterable<View> views) {
        return guard.addAll(views);
    }

    public static RummyClickGuard get(View view) {
        OnClickListener listener = retrieveOnClickListener(view);
        if (listener instanceof GuardedOnClickListener) {
            return ((GuardedOnClickListener) listener).getClickGuard();
        }
        throw new IllegalStateException("The view (id: 0x" + view.getId() + ") isn't guarded by ClickGuard!");
    }

    public static OnClickListener retrieveOnClickListener(View view) {
        if (view != null) {
            return ListenerGetter.get(view);
        }
        throw new NullPointerException("Given view is null!");
    }

    public RummyClickGuard add(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View shouldn't be null!");
        }
        OnClickListener listener = retrieveOnClickListener(view);
        if (listener == null) {
            throw new IllegalStateException("Haven't set an OnClickListener to View (id: 0x" + Integer.toHexString(view.getId()) + ")!");
        }
        view.setOnClickListener(wrapOnClickListener(listener));
        return this;
    }

    public RummyClickGuard addAll(View view, View... others) {
        add(view);
        for (View v : others) {
            add(v);
        }
        return this;
    }

    public RummyClickGuard addAll(Iterable<View> views) {
        for (View v : views) {
            add(v);
        }
        return this;
    }

    public GuardedOnClickListener wrapOnClickListener(OnClickListener onClickListener) {
        if (onClickListener == null) {
        }
        if (onClickListener instanceof GuardedOnClickListener) {
        }
        return new InnerGuardedOnClickListener(onClickListener, this);
    }
}