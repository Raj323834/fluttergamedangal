package in.glg.rummy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class RummyVerticalTextView extends AppCompatTextView {
   private Rect bounds = new Rect();
   private int color;
   private TextPaint textPaint;

   public RummyVerticalTextView(Context context) {
      super(context);
   }

   public RummyVerticalTextView(Context context, AttributeSet attributeSet) {
      super(context, attributeSet);
      this.color = this.getCurrentTextColor();
   }

   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      this.textPaint = getPaint();
      this.textPaint.getTextBounds((String) getText(), 0, getText().length(), this.bounds);
      setMeasuredDimension((int) (((float) this.bounds.height()) + this.textPaint.descent()), this.bounds.width());
   }

   protected void onDraw(Canvas canvas) {
      this.textPaint.setColor(this.color);
      canvas.rotate(-90.0f, (float) this.bounds.width(), 0.0f);
      canvas.drawText((String) getText(), 0.0f, (float) ((-this.bounds.width()) + this.bounds.height()), this.textPaint);
   }
}
