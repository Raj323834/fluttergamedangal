package in.glg.rummy.actionmenu;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public class RummyPopupWindows {
   protected Drawable mBackground = null;
   protected Context mContext;
   protected View mRootView;
   protected PopupWindow mWindow;
   protected WindowManager mWindowManager;

   public RummyPopupWindows(Context context) {
      this.mContext = context;
      this.mWindow = new PopupWindow(context);
      this.mWindow.setTouchInterceptor(new OnTouchListener() {
         public boolean onTouch(View view, MotionEvent event) {
            boolean touched;
            if(event.getAction() == 4) {
               RummyPopupWindows.this.mWindow.dismiss();
               touched = true;
            } else {
               touched = false;
            }

            return touched;
         }
      });
      this.mWindowManager = (WindowManager)context.getSystemService("window");
   }

   public void dismiss() {
      this.mWindow.dismiss();
   }

   protected void onDismiss() {
   }

   protected void onShow() {
   }

   protected void preShow() {
      if(this.mRootView == null) {
         throw new IllegalStateException("setContentView was not called with a view to display.");
      } else {
         this.onShow();
         if(this.mBackground == null) {
            this.mWindow.setBackgroundDrawable(new BitmapDrawable());
         } else {
            this.mWindow.setBackgroundDrawable(this.mBackground);
         }

         this.mWindow.setWidth(-2);
         this.mWindow.setHeight(-2);
         this.mWindow.setTouchable(true);
         this.mWindow.setContentView(this.mRootView);
      }
   }

   public void setBackgroundDrawable(Drawable drawable) {
      this.mBackground = drawable;
   }

   public void setContentView(int layoutResID) {
      this.setContentView(((LayoutInflater)this.mContext.getSystemService("layout_inflater")).inflate(layoutResID, (ViewGroup)null));
   }

   public void setContentView(View root) {
      this.mRootView = root;
      this.mWindow.setContentView(root);
   }

   public void setOnDismissListener(OnDismissListener listener) {
      this.mWindow.setOnDismissListener(listener);
   }
}
