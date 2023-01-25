package in.glg.rummy.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class RummyTimerTextView extends AppCompatTextView {
   public RummyTimerTextView(Context var1) {
      super(var1);
      this.init();
   }

   public RummyTimerTextView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init();
   }

   public RummyTimerTextView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init();
   }

   private void init() {
      /*if(!this.isInEditMode()) {
         this.setTypeface(Typeface.createFromAsset(this.getContext().getAssets(), "fonts/cafeta.ttf"));
      }*/

   }
}
