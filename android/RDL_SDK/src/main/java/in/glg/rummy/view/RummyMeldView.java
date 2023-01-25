package in.glg.rummy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import in.glg.rummy.R;


public class RummyMeldView extends LinearLayout {
   private LayoutInflater inflater;
   private LinearLayout mainHolder;

   public RummyMeldView(Context context) {
      super(context);
      this.init(context);
   }

   public RummyMeldView(Context context, AttributeSet attrs) {
      super(context, attrs);
      init(context);
   }

   private void init(Context context) {
      if (!isInEditMode()) {
         this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
         View view = this.inflater.inflate(R.layout.rummy_meld_smart_correction, this, false);
         this.mainHolder = (LinearLayout) view.findViewById(R.id.meld_view_root_layout);
         addView(view);
      }
   }

   public void addEmptyView(LinearLayout card) {
      this.mainHolder.addView(card, new LayoutParams(card.getLayoutParams()));
      this.mainHolder.setGravity(17);
   }

   public void addMeldView(LinearLayout card) {
      this.mainHolder.addView(card, new LayoutParams(card.getLayoutParams()));
      this.mainHolder.setGravity(17);
   }

   public LinearLayout getEmptyCard() {
      return (LinearLayout) this.inflater.inflate(R.layout.rummy_card_view, this, false);
   }

   public LinearLayout getRummyLayout() {
      return (LinearLayout) this.inflater.inflate(R.layout.rummy_meld_sc_item, this, false);
   }

   public void removeViews() {
      this.mainHolder.removeAllViews();
   }
}
