package in.glg.rummy.fancycoverflow;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class RummyFancyCoverFlowItemWrapper extends ViewGroup {
   private ColorMatrix colorMatrix;
   private float imageReflectionRatio;
   private boolean isReflectionEnabled = false;
   private float originalScaledownFactor;
   private Paint paint;
   private int reflectionGap;
   private float saturation;
   private Bitmap wrappedViewBitmap;
   private Canvas wrappedViewDrawingCanvas;

   public RummyFancyCoverFlowItemWrapper(Context var1) {
      super(var1);
      this.init();
   }

   public RummyFancyCoverFlowItemWrapper(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init();
   }

   public RummyFancyCoverFlowItemWrapper(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init();
   }

   private void createReflectedImages() {
      int var1 = this.wrappedViewBitmap.getWidth();
      int var3 = this.wrappedViewBitmap.getHeight();
      Matrix var5 = new Matrix();
      var5.postScale(1.0F, -1.0F);
      int var4 = (int)((float)var3 * this.originalScaledownFactor);
      int var2 = var3 - var4 - this.reflectionGap;
      Bitmap var6 = Bitmap.createBitmap(this.wrappedViewBitmap, 0, var4 - var2, var1, var2, var5, true);
      this.wrappedViewDrawingCanvas.drawBitmap(var6, 0.0F, (float)(this.reflectionGap + var4), (Paint)null);
      Paint var7 = new Paint();
      var7.setShader(new LinearGradient(0.0F, (float)var3 * this.imageReflectionRatio + (float)this.reflectionGap, 0.0F, (float)var3, 1895825407, 16777215, TileMode.CLAMP));
      var7.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
      this.wrappedViewDrawingCanvas.drawRect(0.0F, (float)var3 * (1.0F - this.imageReflectionRatio), (float)var1, (float)var3, var7);
   }

   private void init() {
      this.paint = new Paint();
      this.colorMatrix = new ColorMatrix();
      this.setSaturation(1.0F);
   }

   private void remeasureChildren() {
      float var1 = 1.0F;
      if(this.getChildAt(0) != null) {
         int var2 = this.getMeasuredHeight();
         if(this.isReflectionEnabled) {
            var1 = (float)var2;
            var1 = ((1.0F - this.imageReflectionRatio) * var1 - (float)this.reflectionGap) / (float)var2;
         }

         this.originalScaledownFactor = var1;
         var2 = (int)(this.originalScaledownFactor * (float)var2);
         int var3 = (int)(this.originalScaledownFactor * (float)this.getMeasuredWidth());
         var2 = MeasureSpec.makeMeasureSpec(var2, Integer.MIN_VALUE);
         var3 = MeasureSpec.makeMeasureSpec(var3, Integer.MIN_VALUE);
         this.getChildAt(0).measure(var3, var2);
      }

   }

   @TargetApi(11)
   protected void dispatchDraw(Canvas var1) {
      View var2 = this.getChildAt(0);
      if(var2 != null) {
         if(VERSION.SDK_INT >= 11) {
            if(var2.isDirty()) {
               var2.draw(this.wrappedViewDrawingCanvas);
               if(this.isReflectionEnabled) {
                  this.createReflectedImages();
               }
            }
         } else {
            var2.draw(this.wrappedViewDrawingCanvas);
         }
      }

      var1.drawBitmap(this.wrappedViewBitmap, (float)((this.getWidth() - var2.getWidth()) / 2), 0.0F, this.paint);
   }

   @SuppressLint({"DrawAllocation"})
   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if(var1) {
         var2 = this.getMeasuredWidth();
         var3 = this.getMeasuredHeight();
         if(this.wrappedViewBitmap == null || this.wrappedViewBitmap.getWidth() != var2 || this.wrappedViewBitmap.getHeight() != var3) {
            this.wrappedViewBitmap = Bitmap.createBitmap(var2, var3, Config.ARGB_8888);
            this.wrappedViewDrawingCanvas = new Canvas(this.wrappedViewBitmap);
         }

         View var6 = this.getChildAt(0);
         var4 = var6.getMeasuredWidth();
         var3 = var6.getMeasuredHeight();
         var4 = (var2 - var4) / 2;
         var6.layout(var4, 0, var2 - var4, var3);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      this.remeasureChildren();
      if(this.isReflectionEnabled) {
         this.setMeasuredDimension((int)((float)this.getMeasuredWidth() * this.originalScaledownFactor), this.getMeasuredHeight());
      }

   }

   void setReflectionEnabled(boolean var1) {
      if(var1 != this.isReflectionEnabled) {
         this.isReflectionEnabled = var1;
         if(VERSION.SDK_INT >= 11) {
            byte var2;
            if(var1) {
               var2 = 1;
            } else {
               var2 = 2;
            }

            this.setLayerType(var2, (Paint)null);
         }

         this.remeasureChildren();
      }

   }

   void setReflectionGap(int var1) {
      if(var1 != this.reflectionGap) {
         this.reflectionGap = var1;
         this.remeasureChildren();
      }

   }

   void setReflectionRatio(float var1) {
      if(var1 != this.imageReflectionRatio) {
         this.imageReflectionRatio = var1;
         this.remeasureChildren();
      }

   }

   public void setSaturation(float var1) {
      if(var1 != this.saturation) {
         this.saturation = var1;
         this.colorMatrix.setSaturation(var1);
         this.paint.setColorFilter(new ColorMatrixColorFilter(this.colorMatrix));
      }

   }
}
