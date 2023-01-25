package in.glg.rummy.timer;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.ArrayList;

import in.glg.rummy.fragments.RummyTablesFragment;
import in.glg.rummy.utils.RummyUtils;

public class RummyCountDownTimer extends CountDownTimer {
   private boolean isUserPlacedShow = false;
   private TextView mMeldTimerTv;
   private String mTimerMessage;
   private TextView mTimerTv;
   private String meldMessage = "Please send your cards : ";
   private RummyTablesFragment tablesFragment;

   public RummyCountDownTimer(RummyTablesFragment var1, long var2, long var4, TextView var6, TextView var7, String var8, boolean var9) {
      super(var2, var4);
      this.mTimerMessage = var8;
      this.mMeldTimerTv = var7;
      this.mTimerTv = var6;
      this.tablesFragment = var1;
      this.isUserPlacedShow = var9;
   }

   public void onFinish() {
      this.mTimerTv.setVisibility(4);
      if(this.tablesFragment != null && this.isUserPlacedShow) {
         ArrayList var1 = this.tablesFragment.getUpdatedCardGroups();
         this.tablesFragment.sendCardsToEngine(var1);
      }

   }

   public void onTick(long var1) {
      this.mTimerTv.setVisibility(0);
      if(this.mMeldTimerTv != null) {
         this.mMeldTimerTv.setVisibility(0);
         this.mMeldTimerTv.setText(String.format("%s%s%s", new Object[]{this.meldMessage, Long.valueOf(var1 / 1000L), " seconds."}));
      }

      RummyUtils.MELD_TIMER_REMAINING = var1 / 1000L;
      if(this.tablesFragment != null) {
         if(this.tablesFragment.isUserNotDeclaredCards()) {
            this.mTimerTv.setText("Please wait while we check your opponent cards in " + var1 / 1000L + " seconds.");
            TextView var3 = this.tablesFragment.getGameResultsMessageView();
            if(var3 != null) {
               var3.setText("Please wait while we check your opponent cards in " + var1 / 1000L + " seconds.");
            }
         } else {
            this.tablesFragment.animateTableButtons();
            this.mTimerTv.setText(String.format("%s%s%s", new Object[]{this.mTimerMessage+" ", Long.valueOf(var1 / 1000L), " seconds."}));
         }
      } else {
         this.mTimerTv.setText(String.format("%s%s%s", new Object[]{this.mTimerMessage+" ", Long.valueOf(var1 / 1000L), " seconds."}));
      }

   }
}
