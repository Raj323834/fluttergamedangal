package in.glg.rummy.anim;

import android.view.View;

public abstract class RummyAnimation {
   public static final int DIRECTION_DOWN = 4;
   public static final int DIRECTION_LEFT = 1;
   public static final int DIRECTION_RIGHT = 2;
   public static final int DIRECTION_UP = 3;
   public static final int DURATION_DEFAULT = 300;
   public static final int DURATION_LONG = 500;
   public static final int DURATION_SHORT = 100;
   protected View view;

   public abstract void animate();
}
