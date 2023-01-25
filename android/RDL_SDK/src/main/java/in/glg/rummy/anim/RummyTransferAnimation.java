package in.glg.rummy.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

public class RummyTransferAnimation extends RummyAnimation {
   View destinationView;
   long duration;
   TimeInterpolator interpolator;
   RummyAnimationListener listener;
   ViewGroup parentView;
   int transX;
   int transY;

   public RummyTransferAnimation(View view) {
      this.view = view;
      this.destinationView = null;
      this.interpolator = new AccelerateDecelerateInterpolator();
      this.duration = 50L;
      this.listener = null;
   }

   public void animate() {
      this.parentView = (ViewGroup)this.view.getParent();
      ViewGroup rootView = (ViewGroup) this.view.getRootView();
      while (!this.parentView.equals(rootView)) {
         this.parentView.setClipChildren(false);
         this.parentView = (ViewGroup) this.parentView.getParent();
      }
      rootView.setClipChildren(false);
      int[] locationDest = new int[2];
      int[] locationView = new int[2];
      this.view.getLocationOnScreen(locationView);
      this.destinationView.getLocationOnScreen(locationDest);
      this.transX = locationDest[0] - locationView[0];
      this.transY = locationDest[1] - locationView[1];
      this.view.animate().scaleX(1.0F).scaleY(1.0F).translationX((float)this.transX).translationY((float)this.transY).setInterpolator(this.interpolator).setDuration(this.duration).setListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator animator) {
            if(RummyTransferAnimation.this.getListener() != null) {
               RummyTransferAnimation.this.getListener().onAnimationEnd(RummyTransferAnimation.this);
            }

         }

         public void onAnimationStart(Animator animator) {
            if(RummyTransferAnimation.this.getListener() != null) {
               RummyTransferAnimation.this.getListener().onAnimationStart(RummyTransferAnimation.this);
            }

         }
      });
   }

   public View getDestinationView() {
      return this.destinationView;
   }

   public long getDuration() {
      return this.duration;
   }

   public TimeInterpolator getInterpolator() {
      return this.interpolator;
   }

   public RummyAnimationListener getListener() {
      return this.listener;
   }

   public RummyTransferAnimation setDestinationView(View view) {
      this.destinationView = view;
      return this;
   }

   public RummyTransferAnimation setDuration(long duration) {
      this.duration = duration;
      return this;
   }

   public RummyTransferAnimation setInterpolator(TimeInterpolator interpolator) {
      this.interpolator = interpolator;
      return this;
   }

   public RummyTransferAnimation setListener(RummyAnimationListener listener) {
      this.listener = listener;
      return this;
   }
}
