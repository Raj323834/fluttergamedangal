package in.glg.rummy.fancycoverflow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.SpinnerAdapter;

import in.glg.rummy.R;


public class RummyFancyCoverFlow extends Gallery {
   public static final int ACTION_DISTANCE_AUTO = Integer.MAX_VALUE;
   public static final float SCALEDOWN_GRAVITY_BOTTOM = 1.0F;
   public static final float SCALEDOWN_GRAVITY_CENTER = 0.5F;
   public static final float SCALEDOWN_GRAVITY_TOP = 0.0F;
   private int actionDistance;
   private int maxRotation = 75;
   private boolean reflectionEnabled = false;
   private int reflectionGap = 20;
   private float reflectionRatio = 0.4F;
   private float scaleDownGravity = 0.5F;
   private Camera transformationCamera;
   private float unselectedAlpha;
   private float unselectedSaturation;
   private float unselectedScale;

   public RummyFancyCoverFlow(Context context) {
      super(context);
      this.initialize();
   }

   public RummyFancyCoverFlow(Context context, AttributeSet attrs) {
      super(context, attrs);
      initialize();
      applyXmlAttributes(attrs);
   }

   public RummyFancyCoverFlow(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      initialize();
      applyXmlAttributes(attrs);
   }

   private void applyXmlAttributes(AttributeSet attrs) {
      TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FancyCoverFlow);
      this.actionDistance = a.getInteger(R.styleable.FancyCoverFlow_actionDistance, Integer.MAX_VALUE);
      this.scaleDownGravity = a.getFloat(R.styleable.FancyCoverFlow_scaleDownGravity, 1.0f);
      this.maxRotation = a.getInteger(R.styleable.FancyCoverFlow_maxRotation, 45);
      this.unselectedAlpha = a.getFloat(R.styleable.FancyCoverFlow_unselectedAlpha, 1.0f);
      this.unselectedSaturation = a.getFloat(R.styleable.FancyCoverFlow_unselectedSaturation, 0.0f);
      this.unselectedScale = a.getFloat(R.styleable.FancyCoverFlow_unselectedScale, 0.75f);
   }

   private void initialize() {
      this.transformationCamera = new Camera();
      this.setSpacing(0);
   }

   public int getActionDistance() {
      return this.actionDistance;
   }

   protected boolean getChildStaticTransformation(View child, Transformation t) {
      RummyFancyCoverFlowItemWrapper item = (RummyFancyCoverFlowItemWrapper) child;

      // Since Jelly Bean childs won't get invalidated automatically, needs to be added for the smooth coverflow animation
      if (android.os.Build.VERSION.SDK_INT >= 16) {
         item.invalidate();
      }

      final int coverFlowWidth = this.getWidth();
      final int coverFlowCenter = coverFlowWidth / 2;
      final int childWidth = item.getWidth();
      final int childHeight = item.getHeight();
      final int childCenter = item.getLeft() + childWidth / 2;

      // Use coverflow width when its defined as automatic.
      final int actionDistance = (this.actionDistance == ACTION_DISTANCE_AUTO) ? (int) ((coverFlowWidth + childWidth) / 2.0f) : this.actionDistance;

      // Calculate the abstract amount for all effects.
      final float effectsAmount = Math.min(1.0f, Math.max(-1.0f, (1.0f / actionDistance) * (childCenter - coverFlowCenter)));

      // Clear previous transformations and set transformation type (matrix + alpha).
      t.clear();
      t.setTransformationType(Transformation.TYPE_BOTH);

      // Alpha
      if (this.unselectedAlpha != 1) {
         final float alphaAmount = (this.unselectedAlpha - 1) * Math.abs(effectsAmount) + 1;
         t.setAlpha(alphaAmount);
      }

      // Saturation
      if (this.unselectedSaturation != 1) {
         // Pass over saturation to the wrapper.
         final float saturationAmount = (this.unselectedSaturation - 1) * Math.abs(effectsAmount) + 1;
         item.setSaturation(saturationAmount);
      }

      final Matrix imageMatrix = t.getMatrix();

      // Apply rotation.
      if (this.maxRotation != 0) {
         final int rotationAngle = (int) (-effectsAmount * this.maxRotation);
         this.transformationCamera.save();
         this.transformationCamera.rotateY(rotationAngle);
         this.transformationCamera.getMatrix(imageMatrix);
         this.transformationCamera.restore();
      }

      // Zoom.
      if (this.unselectedScale != 1) {
         final float zoomAmount = (this.unselectedScale - 1) * Math.abs(effectsAmount) + 1;
         // Calculate the scale anchor (y anchor can be altered)
         final float translateX = childWidth / 2.0f;
         final float translateY = childHeight * this.scaleDownGravity;
         imageMatrix.preTranslate(-translateX, -translateY);
         imageMatrix.postScale(zoomAmount, zoomAmount);
         imageMatrix.postTranslate(translateX, translateY);
      }

      return true;
   }

   public int getMaxRotation() {
      return this.maxRotation;
   }

   public int getReflectionGap() {
      return this.reflectionGap;
   }

   public float getReflectionRatio() {
      return this.reflectionRatio;
   }

   public float getScaleDownGravity() {
      return this.scaleDownGravity;
   }

   public float getUnselectedAlpha() {
      return this.unselectedAlpha;
   }

   public float getUnselectedSaturation() {
      return this.unselectedSaturation;
   }

   public float getUnselectedScale() {
      return this.unselectedScale;
   }

   public boolean isReflectionEnabled() {
      return this.reflectionEnabled;
   }

   public void setActionDistance(int actionDistance) {
      this.actionDistance = actionDistance;
   }

   public void setAdapter(SpinnerAdapter adapter) {
      if (adapter instanceof RummyFancyCoverFlowAdapter) {
         super.setAdapter(adapter);
         return;
      }
      throw new ClassCastException(RummyFancyCoverFlow.class.getSimpleName() + " only works in conjunction with a " + RummyFancyCoverFlowAdapter.class.getSimpleName());
   }

   public void setMaxRotation(int maxRotation) {
      this.maxRotation = maxRotation;
   }

   public void setReflectionEnabled(boolean reflectionEnabled) {
      this.reflectionEnabled = reflectionEnabled;
      if (getAdapter() != null) {
         ((RummyFancyCoverFlowAdapter) getAdapter()).notifyDataSetChanged();
      }
   }

   public void setReflectionGap(int reflectionGap) {
      this.reflectionGap = reflectionGap;
      if (getAdapter() != null) {
         ((RummyFancyCoverFlowAdapter) getAdapter()).notifyDataSetChanged();
      }
   }

   public void setReflectionRatio(float reflectionRatio) {
      if (reflectionRatio <= 0.0f || reflectionRatio > SCALEDOWN_GRAVITY_CENTER) {
         throw new IllegalArgumentException("reflectionRatio may only be in the interval (0, 0.5]");
      }
      this.reflectionRatio = reflectionRatio;
      if (getAdapter() != null) {
         ((RummyFancyCoverFlowAdapter) getAdapter()).notifyDataSetChanged();
      }
   }

   public void setScaleDownGravity(float scaleDownGravity) {
      this.scaleDownGravity = scaleDownGravity;
   }

   public void setUnselectedAlpha(float unselectedAlpha) {
      super.setUnselectedAlpha(unselectedAlpha);
      this.unselectedAlpha = unselectedAlpha;
   }

   public void setUnselectedSaturation(float unselectedSaturation) {
      this.unselectedSaturation = unselectedSaturation;
   }

   public void setUnselectedScale(float unselectedScale) {
      this.unselectedScale = unselectedScale;
   }

   public static class LayoutParams extends Gallery.LayoutParams
   {
      public LayoutParams(Context c, AttributeSet attrs) {
         super(c, attrs);
      }

      public LayoutParams(int w, int h) {
         super(w, h);
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams source) {
         super(source);
      }
   }
}
