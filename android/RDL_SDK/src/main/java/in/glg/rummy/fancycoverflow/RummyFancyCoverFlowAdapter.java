package in.glg.rummy.fancycoverflow;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class RummyFancyCoverFlowAdapter extends BaseAdapter {
   public abstract View getCoverFlowItem(int var1, View var2, ViewGroup var3);

   public final View getView(int var1, View var2, ViewGroup var3) {
      RummyFancyCoverFlow var6 = (RummyFancyCoverFlow)var3;
      View var5 = null;
      RummyFancyCoverFlowItemWrapper var7;
      if(var2 != null) {
         var7 = (RummyFancyCoverFlowItemWrapper)var2;
         var5 = var7.getChildAt(0);
         var7.removeAllViews();
      } else {
         var7 = new RummyFancyCoverFlowItemWrapper(var3.getContext());
      }

      View var8 = this.getCoverFlowItem(var1, var5, var3);
      if(var8 == null) {
         throw new NullPointerException("getCoverFlowItem() was expected to return a view, but null was returned.");
      } else {
         boolean var4 = var6.isReflectionEnabled();
         var7.setReflectionEnabled(var4);
         if(var4) {
            var7.setReflectionGap(var6.getReflectionGap());
            var7.setReflectionRatio(var6.getReflectionRatio());
         }

         var7.addView(var8);
         var7.setLayoutParams(var8.getLayoutParams());
         return var7;
      }
   }
}
