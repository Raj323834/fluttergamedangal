package in.glg.rummy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class RummyCorouselView extends LinearLayout {
   public static final float BIG_SCALE = 1.0F;
   public static final float DIFF_SCALE = 0.3F;
   public static final int FIRST_PAGE = 2500;
   public static final int LOOPS = 1000;
   public static final int PAGES = 5;
   public static final float SMALL_SCALE = 0.7F;
   private float scale = 1.0F;

   public RummyCorouselView(Context var1) {
      super(var1);
   }

   public RummyCorouselView(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   protected void onDraw(Canvas var1) {
      int var2 = this.getWidth();
      int var3 = this.getHeight();
      var1.scale(this.scale, this.scale, (float)(var2 / 2), (float)(var3 / 2));
      super.onDraw(var1);
   }

   public void setScaleBoth(float var1) {
      this.scale = var1;
      this.invalidate();
   }
}
