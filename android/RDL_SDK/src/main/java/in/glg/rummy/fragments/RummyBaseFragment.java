package in.glg.rummy.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Browser;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import in.glg.rummy.NetworkProvider.RummyVolleySingleton;
import in.glg.rummy.R;
import in.glg.rummy.RummyApplication;
import in.glg.rummy.activities.RummyHomeActivity;
import in.glg.rummy.activities.RummyInstance;
import in.glg.rummy.adapter.RummyGameResultAdapter;
import in.glg.rummy.adapter.RummySplitWinnerAdapter;
import in.glg.rummy.api.requests.RummyLoginRequest;
import in.glg.rummy.api.response.RummyCheckMeldResponse;
import in.glg.rummy.fancycoverflow.RummyFancyCoverFlow;
import in.glg.rummy.models.RummyCheckMeldResult;
import in.glg.rummy.models.RummyEngineRequest;
import in.glg.rummy.models.RummyEvent;
import in.glg.rummy.models.RummyGamePlayer;
import in.glg.rummy.models.RummyMeldBox;
import in.glg.rummy.models.RummyMeldCard;
import in.glg.rummy.models.RummyPlayingCard;
import in.glg.rummy.models.RummyResults;
import in.glg.rummy.models.RummyTableDetails;
import in.glg.rummy.packagedev.android.api.base.builders.json.RummyGenericGsonListResult;
import in.glg.rummy.utils.RummyApiResult;
import in.glg.rummy.utils.RummyCommonEventTracker;
import in.glg.rummy.utils.RummyGameRoomCustomScreenLess700;
import in.glg.rummy.utils.RummyGameRoomCustomScreenMore700;
import in.glg.rummy.utils.RummyPrefManager;
import in.glg.rummy.utils.RummyConstants;
import in.glg.rummy.utils.RummyResultListener;
import in.glg.rummy.utils.RummyTLog;
import in.glg.rummy.utils.RummyUtils;
import in.glg.rummy.view.RummyMeldView;
import in.glg.rummy.view.RummyView;

import static in.glg.rummy.utils.RummyConstants.ACCESS_TOKEN_REST;

public class RummyBaseFragment extends Fragment {

   public Activity mBaseFragmentActivity = null;

   private String TAG = RummyBaseFragment.class.getName();
   private AlertDialog dialog;
   private Dialog mLoadingDialog;
   private ArrayList<LinearLayout> preMeldViews;
   public boolean userPlacedShow = false;



   public Dialog getLeaveTableDialog(Context context, String message) {
      final Dialog dialog = new Dialog(context, R.style.DialogTheme);
      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
      //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
      dialog.setContentView(R.layout.rummy_dialog_leave_table);
      ((TextView) dialog.findViewById(R.id.dialog_msg_tv)).setText(message);
      ((ImageView) dialog.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            dialog.dismiss();
         }
      });
      return dialog;
   }

   public void showGenericDialog(Context context, String message) {
      final Dialog dialog = new Dialog(context);
      dialog.requestWindowFeature(1);
      dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
      dialog.setContentView(R.layout.rummy_dialog_generic);
      ((TextView) dialog.findViewById(R.id.dialog_msg_tv)).setText(message);
      ((Button) dialog.findViewById(R.id.ok_btn)).setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            dialog.dismiss();
         }
      });
      ((ImageView) dialog.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            dialog.dismiss();
         }
      });
      dialog.show();
   }

   public void showErrorChipsDialog(Context context, String message) {
      final Dialog dialog = new Dialog(context);
      dialog.requestWindowFeature(1);
      dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
      dialog.setContentView(R.layout.rummy_dialog_error_balance);
      ((TextView) dialog.findViewById(R.id.dialog_msg_tv)).setText(message);
      ((Button) dialog.findViewById(R.id.ok_btn)).setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            dialog.dismiss();
         }
      });
      ((ImageView) dialog.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            dialog.dismiss();
         }
      });
      dialog.show();
   }


   public void handleLowBalanceListener(String betAmount)  // type is bet is ponts rummy or not
   {
      RummyApplication mRummyApplication = RummyApplication.getInstance();
      double userbalance = 0;
      if(!mRummyApplication.getUserData().getRealChips().equalsIgnoreCase("") && mRummyApplication.getUserData().getRealChips() != null)
      {
         userbalance = Math.round(Double.parseDouble(mRummyApplication.getUserData().getRealChips()));
      }

      double betammount = 0;
      if(!betAmount.equalsIgnoreCase("") && betAmount != null)
      {
         betammount = Math.round(Double.parseDouble(betAmount));
      }


      double lowBalance = betammount - userbalance;

      RummyInstance.getInstance().getRummyListener().lowBalance(lowBalance+1);
   }
   public void showErrorBuyChipsDialog(final Context context, String message)
   {
      final Dialog dialog = new Dialog(context);
      dialog.requestWindowFeature(1);
      dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
      dialog.setContentView(R.layout.rummy_dialog_error_buychips);
      ((TextView) dialog.findViewById(R.id.dialog_msg_tv)).setText(message);
      ((Button) dialog.findViewById(R.id.ok_btn)).setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            dialog.dismiss();
         }
      });
      ((Button) dialog.findViewById(R.id.buychips_btn)).setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            //launchFragment(new DepositFragment(), true, DepositFragment.class.getName());
            //checkPlayerDeposit(context);
            RummyInstance.getInstance().getRummyListener().lowBalance(0);
            dialog.dismiss();
         }
      });
      dialog.show();
   }

   public void showErrorBalanceBuyChips(final Context context, String text, final String betAmount)
   {
      String msg1 = "Add"+" "+context.getResources().getString(R.string.rupee_symbol)+getRestAmounttoAdd(betAmount) +" "+"to join this table";
      final Dialog dialog = new Dialog(context);
      dialog.requestWindowFeature(1);
      dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
      dialog.setContentView(R.layout.rummy_dialog_error_balance_buy_chips);
      TextView tv_msg1 = (TextView)dialog.findViewById(R.id.tv_add_rest_amount_msg);
      TextView label = (TextView) dialog.findViewById(R.id.dialog_msg_tv);
      Button deposit = (Button) dialog.findViewById(R.id.ok_btn);
      Button cancel = (Button) dialog.findViewById(R.id.cancel);

      label.setText(text);
      tv_msg1.setText(msg1);
      cancel.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View view) {
            dialog.dismiss();
         }
      });

      deposit.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View view) {
           // checkPlayerDeposit(context);
            dialog.dismiss();
            if(RummyApplication.getInstance().getJoinedTableIds().size() == 0)
            {
               handleLowBalanceListener("0.0");
            }
            else
            {
               showGenericDialog(getActivity(), getResources().getString(R.string.add_money_msg_when_table_joined));
            }
         }
      });

      dialog.show();
   }

   public void showErrorLoyaltyChipsDialog(final Context context, String message)
   {
      final Dialog dialog = new Dialog(context);
      dialog.requestWindowFeature(1);
      dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
      dialog.setContentView(R.layout.rummy_dialog_error_buychips);
      ((TextView) dialog.findViewById(R.id.dialog_msg_tv)).setText(message);
      ((Button) dialog.findViewById(R.id.ok_btn2)).setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            dialog.dismiss();
         }
      });
      ((LinearLayout) dialog.findViewById(R.id.button_layout)).setVisibility(View.GONE);
      ((Button) dialog.findViewById(R.id.ok_btn2)).setVisibility(View.VISIBLE);
      dialog.show();
   }

   public Dialog getDialog(Context context, int layoutResourceId) {
      Dialog dialog = new Dialog(context);
      dialog.requestWindowFeature(1);
      dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
      dialog.setContentView(layoutResourceId);
      return dialog;
   }

   public String getDeviceType() {
      if (getResources().getBoolean(R.bool.isTablet)) {
         return "Tablet";
      }
      return "Mobile";
   }

   @SuppressLint("WrongConstant")
   public void showHideView(boolean canShow, View view, boolean canGone) {
//      Log.e("autodrop","showHideView");
      int i = canShow ? 0 : canGone ? 8 : 4;
      view.setVisibility(i);
   }

   public void hideView(View view) {
      view.setVisibility(View.GONE);
   }

   public void invisibleView(View view) {
      view.setVisibility(View.INVISIBLE);
   }

   public void goneView(View view)
   {
      view.setVisibility(View.INVISIBLE);
   }

   public void showView(View view) {
//      Log.e("view@238",getResources().getResourceName(view.getId())+"");
      view.setVisibility(View.VISIBLE);
   }

   public void invisibleView() {
      getView().setVisibility(View.INVISIBLE);
   }

   public void disableView(View view) {
//      Log.e("view@248",getResources().getResourceName(view.getId())+"");
      view.setEnabled(false);
      view.setClickable(false);
      view.setAlpha(RummyFancyCoverFlow.SCALEDOWN_GRAVITY_CENTER);
   }

   public void disableClick(View view) {
      view.setEnabled(false);
      view.setClickable(false);
   }

   public void enableView(View view) {
      view.setEnabled(true);
      view.setClickable(true);
      view.setAlpha(1.0f);
   }
   
   public void showLongToast(Context context, String message) {
      if (context != null) {
         Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
      }
   }

   public void showDialog(Context context, String message) {
      this.dialog = new SpotsDialog(context, message);
      this.dialog.setCancelable(false);
      this.dialog.show();
   }

   public void dismissDialog() {
      if (this.dialog != null && this.dialog.isShowing()) {
         this.dialog.dismiss();
      }
   }

   public void dismissDialog(Dialog mDialog) {
      if (mDialog != null && mDialog.isShowing()) {
         mDialog.dismiss();
      }
   }

   public void launchFragment(Fragment fragment, String tag) {
      FragmentTransaction fragmentTransaction = ((FragmentActivity)mBaseFragmentActivity).getSupportFragmentManager().beginTransaction();
      fragment.setArguments(new Bundle());
      fragmentTransaction.add(R.id.content_frame, fragment, tag);
      fragmentTransaction.addToBackStack(tag);
      fragmentTransaction.commit();
   }

   public void launchFragment(Fragment fragment, String tag, Bundle bundle)
   {
      FragmentTransaction fragmentTransaction = ((FragmentActivity)mBaseFragmentActivity).getSupportFragmentManager().beginTransaction();
      fragment.setArguments(bundle);
       fragmentTransaction.replace(R.id.home_content_frame, fragment, tag);
      fragmentTransaction.addToBackStack(tag);
      fragmentTransaction.commit();
   }

    public void removeFragment(String tag) {
      FragmentManager fm = ((FragmentActivity)mBaseFragmentActivity).getSupportFragmentManager();
      fm.beginTransaction().remove(fm.findFragmentByTag(tag));
   }

   public void launchNewActivity(Intent intent, boolean removeStack) {
      if (removeStack) {
         //intent.setFlags(268468224);
         intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
      }
      startActivity(intent);
   }

   public void setColorFilter(ImageView view) {
   }

   public void showFragment(Fragment fragment) {
      FragmentTransaction ft = ((FragmentActivity)mBaseFragmentActivity).getSupportFragmentManager().beginTransaction();
      ft.show(fragment);
      ft.commit();
   }

   public void hideFragment(Fragment fragment) {
      FragmentTransaction ft = ((FragmentActivity)mBaseFragmentActivity).getSupportFragmentManager().beginTransaction();
      ft.hide(fragment);
      ft.commit();
   }

   public void showLoadingDialog(Context context)
   {
      try {
         this.mLoadingDialog = new Dialog(context, R.style.DialogTheme);
         this.mLoadingDialog.requestWindowFeature(1);
         this.mLoadingDialog.setContentView(R.layout.rummy_dialog_loading);
         if(!this.mLoadingDialog.isShowing())
         {
            this.mLoadingDialog.show();
         }

         Log.e("PARTH","LOADING SHOWING");
      }
      catch (Exception e){
         RummyTLog.e("show dialog loading catch", e.toString());
      }
   }

   public void dismissLoadingDialog()
   {
      try {
         if (this.mLoadingDialog != null && this.mLoadingDialog.isShowing()) {
            this.mLoadingDialog.dismiss();
            Log.e("PARTH","LOADING HID");
         }
      }
      catch (Exception e){
         e.printStackTrace();
      }
   }

   /*public void showWinnerFragment(final RelativeLayout linearLayout, final View winnerView, final View searchView, View splitRejectedView, RummyEvent winnerEvent, RummyTableDetails tableDetails) {
      showView(linearLayout);
      showView(winnerView);
      invisibleView(splitRejectedView);
      boolean isFreeTable = false;
      if (tableDetails != null && tableDetails.getTableCost().contains("FUNCHIPS_FUNCHIPS")) {
         isFreeTable = true;
      }
      ((ListView) winnerView.findViewById(R.id.split_winner_lv)).setAdapter(new RummySplitWinnerAdapter(mBaseFragmentActivity, winnerEvent.getPlayer(), isFreeTable));
      ((ImageView) winnerView.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            *//*if (searchView.getVisibility() == View.VISIBLE) {
               RummyBaseFragment.this.invisibleView(winnerView);
               return;
            }
            RummyBaseFragment.this.hideView(winnerView);
            RummyBaseFragment.this.hideView(linearLayout);*//*

            hideView(winnerView);
            joinAnotherGame(winnerEvent,"");
         }
      });
   }*/

   public void hideWinnerView(RelativeLayout linearLayout, View winnerView, View searchView, View splitRejectedView) {
      showView(linearLayout);
      invisibleView(winnerView);
      invisibleView(splitRejectedView);
      showView(searchView);
   }

   public void setUpPlayerRank(View view, RummyGamePlayer player)
   {
      ((TextView) view.findViewById(R.id.player_rank_tv)).setText("("+player.getRank()+")");
      ((TextView) view.findViewById(R.id.player_rank_tv)).setVisibility(View.VISIBLE);
      ((LinearLayout) view.findViewById(R.id.ll_player_rank_square)).setVisibility(View.VISIBLE);
   }

   /*public void setUpPlayerRank(View view, RummyGamePlayer player,View rankview)
   {

      ((TextView) rankview.findViewById(R.id.player_rank_tv)).setText("("+player.getRank()+")");
      ((TextView) rankview.findViewById(R.id.player_rank_tv)).setVisibility(View.VISIBLE);
      ((LinearLayout) rankview.findViewById(R.id.ll_player_rank_square)).setVisibility(View.VISIBLE);
      // showView(rankview);

   }*/

   public void setUpPlayerDetails(View view, RummyGamePlayer player, String dealerId, boolean isPlayerJoined)
   {
      ((RelativeLayout) view.findViewById(R.id.player_details_root_layout)).setAlpha(1.0f);
      ((TextView) view.findViewById(R.id.player_name_tv)).setText(player.getNick_name());
      if (isPlayerJoined) {
         hideDropPayerImage(view);
      }
      View ratingBar = view.findViewById(R.id.player_rating_bar);
      String playerLevelStr = player.getPlayerlevel();
      int rating = (playerLevelStr == null || playerLevelStr.length() <= 0) ? -1 : Integer.parseInt(playerLevelStr);
      setPlayerLevel(ratingBar, rating);
      setDealer(dealerId, player.getUser_id(), (ImageView) view.findViewById(R.id.player_dealer_iv));
    //  setPlayerSystem((ImageView) view.findViewById(R.id.player_system_iv), rummy_player.getDEVICE_ID());
      String points = player.getPoints();
      TextView playerPoints = (TextView) view.findViewById(R.id.player_points_tv);
      if (points == null) {
         String buyInAmount = player.getBuyinammount();
         points = buyInAmount != null ? buyInAmount : "0";
      }
      
      String str_points = String.valueOf(new DecimalFormat("#").format(Float.valueOf(points)));
      String str_pts = "PTS";

     /* SpannableString span1 = new SpannableString(str_points);
      span1.setSpan(new AbsoluteSizeSpan(18), 0, str_points.length(), SPAN_INCLUSIVE_INCLUSIVE);

      SpannableString span2 = new SpannableString(str_pts);
      span2.setSpan(new AbsoluteSizeSpan(10), 0, str_pts.length(), SPAN_INCLUSIVE_INCLUSIVE);*/


     // CharSequence finalText = TextUtils.concat(span1, "\n", span2);

      playerPoints.setText(str_points);
      playerPoints.setVisibility(View.VISIBLE);
      //((LinearLayout) view.findViewById(R.id.ll_player_point_round)).setVisibility(View.VISIBLE);


      showView(view.findViewById(R.id.ll_player_point_round));

   }

   public void resetPlayerData(View view) {
      //((RelativeLayout) view.findViewById(R.id.player_details_root_layout)).setAlpha(FancyCoverFlow.SCALEDOWN_GRAVITY_CENTER);
      ((TextView) view.findViewById(R.id.player_name_tv)).setText("");
      setPlayerLevel(view.findViewById(R.id.player_rating_bar), 0);
      invisibleView((ImageView) view.findViewById(R.id.player_dealer_iv));
      invisibleView((ImageView) view.findViewById(R.id.player_system_iv));
      ((TextView) view.findViewById(R.id.player_points_tv)).setText("");
      ((TextView) view.findViewById(R.id.player_points_tv)).setVisibility(View.GONE);
      ((LinearLayout) view.findViewById(R.id.ll_player_point_round)).setVisibility(View.GONE);
      ((LinearLayout) view.findViewById(R.id.ll_player_rank_square)).setVisibility(View.GONE);
      ((TextView) view.findViewById(R.id.player_rank_tv)).setText("");
      ((ImageView) view.findViewById(R.id.iv_avtar)).setImageDrawable(null);
      hideWaitingPayerImage(view);
      hideDropPayerImage(view);
      RelativeLayout autoPlayLayout = (RelativeLayout) view.findViewById(R.id.auto_play_layout);
      if (autoPlayLayout != null) {
         invisibleView(autoPlayLayout);
      }
   }

   public void resetAutoPlayUI(View view) {
      RelativeLayout autoPlayLayout = (RelativeLayout) view.findViewById(R.id.auto_play_layout);
      if (autoPlayLayout != null) {
         invisibleView(autoPlayLayout);
      }
   }

   public void hideDropPayerImage(View view) {
      RelativeLayout dropImage = (RelativeLayout) view.findViewById(R.id.player_drop_iv);
      if (dropImage != null) {
         hideView(dropImage);
      }
   }
   public void hideWaitingPayerImage(View view) {
      RelativeLayout waitingImg = (RelativeLayout) view.findViewById(R.id.player_waiting_iv);
      if (waitingImg != null) {
         hideView(waitingImg);
      }
   }

   public void setAutoPlayView(View view, RummyGamePlayer gamePlayer) {
      String autoPlay = gamePlayer.getAutoplay();
      String autoPlayCount = gamePlayer.getAutoplay_count();
      String totalCount = gamePlayer.getTotalCount();
      RelativeLayout autoPlayLayout = (RelativeLayout) view.findViewById(R.id.auto_play_layout);
      TextView autoPlayCountTv = (TextView) view.findViewById(R.id.auto_play_count_tv);
      if (autoPlay == null) {
         hideView(autoPlayLayout);
      } else if (autoPlay.equalsIgnoreCase("True")) {
         showView(autoPlayLayout);
         if (autoPlayCount != null) {
            autoPlayCountTv.setText("Auto Play " + autoPlayCount + " Of " + totalCount);
         }
      } else {
         hideView(autoPlayLayout);
      }
   }

   public void hideAutoPlayView(View view) {
      hideView((RelativeLayout) view.findViewById(R.id.auto_play_layout));
   }

   public void setUserDetails(View view, RummyGamePlayer player, String dealerId, boolean isPlayerJoined)
   {
      ((RelativeLayout) view.findViewById(R.id.player_details_root_layout)).setAlpha(1.0f);
      if (isPlayerJoined) {
         hideDropPayerImage(view);
      }
      ((TextView) view.findViewById(R.id.player_name_tv)).setText(player.getNick_name());
      View ratingBar = view.findViewById(R.id.player_rating_bar);
      String playerLevelStr = player.getPlayerlevel();
      int rating = (playerLevelStr == null || playerLevelStr.length() <= 0) ? -1 : Integer.parseInt(playerLevelStr);
      setPlayerLevel(ratingBar, rating);
      setDealer(dealerId, player.getUser_id(), (ImageView) view.findViewById(R.id.player_dealer_iv));
     // setPlayerSystem((ImageView) view.findViewById(R.id.player_system_iv), rummy_player.getDEVICE_ID());
      String points = player.getPoints();
      TextView playerPoints = (TextView) view.findViewById(R.id.player_points_tv);
      if (points == null) {
         String buyInAmount = player.getBuyinammount();
         points = buyInAmount != null ? buyInAmount : "0";
      }

      String str_points = String.valueOf(new DecimalFormat("#").format(Float.valueOf(points)));
      String str_pts = "PTS";

     /* SpannableString span1 = new SpannableString(str_points);
      span1.setSpan(new AbsoluteSizeSpan(18), 0, str_points.length(), SPAN_INCLUSIVE_INCLUSIVE);

      SpannableString span2 = new SpannableString(str_pts);
      span2.setSpan(new AbsoluteSizeSpan(10), 0, str_pts.length(), SPAN_INCLUSIVE_INCLUSIVE);


      CharSequence finalText = TextUtils.concat(span1, "\n", span2);*/

      playerPoints.setText(str_points);
      playerPoints.setVisibility(View.VISIBLE);
      ((LinearLayout) view.findViewById(R.id.ll_player_point_round)).setVisibility(View.VISIBLE);


   }

   public void setDealer(String mDealerId, String user_id, ImageView dealerImage) {
      if (mDealerId.equalsIgnoreCase(user_id)) {
         showView(dealerImage);
      }
   }

   public void setPlayerLevel(View ratingBar, int rating) {
      if (rating >= 0) {
         if (ratingBar == null || rating <= 0) {
            invisibleView(ratingBar);
            return;
         }
         //showView(ratingBar);
         ImageView star1 = (ImageView) ratingBar.findViewById(R.id.star_1);
         ImageView star2 = (ImageView) ratingBar.findViewById(R.id.star_2);
         ImageView star3 = (ImageView) ratingBar.findViewById(R.id.star_3);
         ImageView star4 = (ImageView) ratingBar.findViewById(R.id.star_4);
         ImageView star5 = (ImageView) ratingBar.findViewById(R.id.star_5);
         switch (rating) {
            case 1:
               showView(star1);
               hideView(star2);
               hideView(star3);
               hideView(star4);
               hideView(star5);
               return;
            case 2:
               showView(star1);
               showView(star2);
               hideView(star3);
               hideView(star4);
               hideView(star5);
               return;
            case 3:
               showView(star1);
               showView(star2);
               showView(star3);
               hideView(star4);
               hideView(star5);
               return;
            case 4:
               showView(star1);
               showView(star2);
               showView(star3);
               showView(star4);
               hideView(star5);
               return;
            case 5:
               showView(star1);
               showView(star2);
               showView(star3);
               showView(star4);
               showView(star5);
               return;
            default:
               return;
         }
      }
   }

   public void setPlayerSystem(ImageView playerSystem, String system) {
      if (system == null || system.length() <= 0) {
         invisibleView(playerSystem);
         return;
      }
      showView(playerSystem);
      if (system.equalsIgnoreCase("Desktop") || system.equalsIgnoreCase("TV")) {
         playerSystem.setImageResource(R.drawable.rummy_desktop_icon);
      } else if (system.equalsIgnoreCase("Tablet")) {
         playerSystem.setImageResource(R.drawable.rummy_ipad_icon);
      } else {
         playerSystem.setImageResource(R.drawable.rummy_mobile);
      }
   }

   public void setPlayerPoints(View view, RummyGamePlayer player) {
      String playerName = ((TextView) view.findViewById(R.id.player_name_tv)).getText().toString();
      if (playerName != null && !playerName.isEmpty()) {
         Log.e("TwoTables",String.valueOf(new DecimalFormat("0.#").format(Float.valueOf(player.getTotalScore())))+"@555");
         RummyUtils.PR_JOKER_POINTS = String.valueOf(new DecimalFormat("0.#").format(Float.valueOf(player.getTotalScore())));

         String str_points = String.valueOf(new DecimalFormat("#").format(Float.valueOf(player.getTotalScore())));
         String str_pts = "PTS";

       /*  SpannableString span1 = new SpannableString(str_points);
         span1.setSpan(new AbsoluteSizeSpan(18), 0, str_points.length(), SPAN_INCLUSIVE_INCLUSIVE);

         SpannableString span2 = new SpannableString(str_pts);
         span2.setSpan(new AbsoluteSizeSpan(10), 0, str_pts.length(), SPAN_INCLUSIVE_INCLUSIVE);


         CharSequence finalText = TextUtils.concat(span1, "\n", span2);*/

         ((TextView) view.findViewById(R.id.player_points_tv)).setText(str_points);

         ((TextView) view.findViewById(R.id.player_points_tv)).setVisibility(View.VISIBLE);
         ((LinearLayout) view.findViewById(R.id.ll_player_point_round)).setVisibility(View.VISIBLE);
      }
   }

   public void setGameResultsView(View view, RummyEvent event) {
      showView(view);

      ((TextView) view.findViewById(R.id.game_id_tv)).setText(event.getGameId());
      ((TextView) view.findViewById(R.id.table_id_tv)).setText(event.getTableId());

      RummyPlayingCard jokerCard = new RummyPlayingCard();
      jokerCard.setFace(event.getFace());
      jokerCard.setSuit(event.getSuit());
      ImageView jokerIv = (ImageView) view.findViewById(R.id.game_jocker_iv);
      ((ListView) view.findViewById(R.id.game_results_lv)).setAdapter(new RummyGameResultAdapter(mBaseFragmentActivity, event));
      setJokerCard(jokerCard, jokerIv);
   }

   private void setJokerCard(RummyPlayingCard jokerCard, ImageView mGameJockerIv) {
      if (jokerCard != null) {
         String cardValue = String.format("%s%s%s", new Object[]{"jocker_", jokerCard.getSuit(), jokerCard.getFace()});
         if (cardValue.equalsIgnoreCase("jocker_jo0")) {
            cardValue = "jocker_d1";
         }
         int imgId = getResources().getIdentifier(cardValue, "drawable", mBaseFragmentActivity.getPackageName());
         mGameJockerIv.setVisibility(View.VISIBLE);
         mGameJockerIv.setImageResource(imgId);
      }
   }

   public void setGameResultsTimer(View view, String message) {
      TextView mTimerTv = (TextView) view.findViewById(R.id.game_timer);

      showView(mTimerTv);
      mTimerTv.setText(message);
   }

   public void resetTimerInfo(View view) {
      ((TextView) view.findViewById(R.id.game_timer)).setText("");
   }

   public void setMeldCardsView(View view, RummyMeldCard meldCard) {
      showView(view);
      RummyView mMeldView = (RummyView) view.findViewById(R.id.meld_rummy_view);
      setJokerCard(meldCard.jokerCard, (ImageView) view.findViewById(R.id.game_jocker_iv));
      setPreMeldView(view, meldCard);
   }

   private void setPreMeldView(View view, RummyMeldCard meldCard)
   {
      this.preMeldViews = new ArrayList();
      RummyMeldView meldView = (RummyMeldView) view.findViewById(R.id.meld_meld_view);
      meldView.removeViews();
      int index = 0;
      Iterator it = meldCard.meldGroup.iterator();
      while (it.hasNext()) {
         ArrayList<RummyPlayingCard> groupList = (ArrayList) it.next();
         if (groupList.size() > 0) {
            LinearLayout linearLayout = meldView.getRummyLayout();
            RummyView rv = (RummyView) linearLayout.findViewById(R.id.meld_sc_rv);
            Iterator it2 = groupList.iterator();
            while (it2.hasNext())
            {
               addCardToRummyView((RummyPlayingCard) it2.next(), index, rv, meldCard.jokerCard);
               index++;
            }
            this.preMeldViews.add(linearLayout);
            meldView.addMeldView(linearLayout);
         }
      }
   }

   public void setSmartCorrectionMeldView(View view, RummyPlayingCard playingCard, RummyEngineRequest engineRequest) {
      this.setJokerCard(playingCard, (ImageView)view.findViewById(R.id.game_jocker_iv));
      RummyMeldView scMeldView = (RummyMeldView)view.findViewById(R.id.sc_wmeld_view);
      scMeldView.removeViews();

      RummyView rummyView;
      LinearLayout var6;
      for(Iterator var7 = engineRequest.getWrongMeldList().iterator(); var7.hasNext(); scMeldView.addMeldView(var6)) {
         RummyMeldBox var8 = (RummyMeldBox)var7.next();
         var6 = scMeldView.getRummyLayout();
         rummyView = (RummyView)var6.findViewById(R.id.meld_sc_rv);
         List var15 = var8.getMeldBoxes();
         if(var15 != null && var15.size() > 0) {
            Iterator var16 = var15.iterator();

            while(var16.hasNext()) {
               this.addCardToSCRummyView((RummyPlayingCard)var16.next(), 0, rummyView, playingCard);
            }
         }
      }

      RummyMeldView var9 = (RummyMeldView)view.findViewById(R.id.sc_cmeld_view);
      var9.removeViews();

      LinearLayout var11;
      for(Iterator var10 = engineRequest.getCheckMeldList().iterator(); var10.hasNext(); var9.addMeldView(var11)) {
         RummyMeldBox var12 = (RummyMeldBox)var10.next();
         var11 = var9.getRummyLayout();
         rummyView = (RummyView)var11.findViewById(R.id.meld_sc_rv);
         List var13 = var12.getMeldBoxes();
         if(var13 != null && var13.size() > 0) {
            Iterator var14 = var13.iterator();

            while(var14.hasNext()) {
               this.addCardToSCRummyView((RummyPlayingCard)var14.next(), 0, rummyView, playingCard);
            }
         }
      }
   }

   public void updateScoreOnPreMeld(RummyCheckMeldResponse checkMeldResponse, View mMeldCardsView)
   {
      if (checkMeldResponse != null)
      {
         RummyResults results = checkMeldResponse.getResults();
         if (results != null)
         {
            TextView invalidShow = (TextView) mMeldCardsView.findViewById(R.id.invalid_show_tv);
            invalidShow.setVisibility(View.INVISIBLE);

            List<RummyCheckMeldResult> checkMeldResultList = results.getCheckMeldResults();

            if (checkMeldResultList != null && checkMeldResultList.size() == this.preMeldViews.size())
            {
               for (int i = 0; i < checkMeldResultList.size(); i++)
               {
                  LinearLayout layOut = (LinearLayout) this.preMeldViews.get(i);
                  RummyCheckMeldResult checkMeldResult = (RummyCheckMeldResult) checkMeldResultList.get(i);
                  ImageView line = (ImageView) layOut.findViewById(R.id.meld_line_iv);
                  ImageView arrowIv = (ImageView) layOut.findViewById(R.id.down_arrow_iv);
                  TextView scoreTv = (TextView) layOut.findViewById(R.id.meld_score_tv);

                  scoreTv.setText(checkMeldResult.getScore());

                  if (checkMeldResult.getResult().equalsIgnoreCase("True") || checkMeldResult.getScore().equalsIgnoreCase("0"))
                  {
                     line.setBackgroundColor(ContextCompat.getColor(mBaseFragmentActivity, R.color.rummy_meldScoreGreenColor));
                     scoreTv.setTextColor(ContextCompat.getColor(mBaseFragmentActivity, R.color.rummy_meldScoreGreenColor));
                     invisibleView(scoreTv);
                     invisibleView(arrowIv);
                     invalidShow.setText("Invalid Show - Some sets are missing");
                  }
                  else
                  {
                     showView(invalidShow);
                     showView(scoreTv);
                     showView(arrowIv);
                     line.setBackgroundColor(ContextCompat.getColor(mBaseFragmentActivity, R.color.rummy_meldScoreRedColor));
                     scoreTv.setTextColor(ContextCompat.getColor(mBaseFragmentActivity, R.color.rummy_meldScoreRedColor));
                  }
               }

               if(!this.userPlacedShow)
                  invalidShow.setVisibility(View.INVISIBLE);
            }
         }
      }
   }

   private void setGroupView(RummyView mMeldView, ArrayList<ArrayList<RummyPlayingCard>> meldGroup, RummyPlayingCard jokerCard) {
      mMeldView.removeViews();
      int index = 0;
      Iterator it = meldGroup.iterator();
      while (it.hasNext()) {
         ArrayList<RummyPlayingCard> groupList = (ArrayList) it.next();
         if (groupList.size() > 0) {
            Iterator it2 = groupList.iterator();
            while (it2.hasNext()) {
               addCardToRummyView((RummyPlayingCard) it2.next(), index, mMeldView, jokerCard);
               index++;
            }
            mMeldView.addCard(mMeldView.getCard(),false);
         }
      }
   }

   private void addCardToRummyView(RummyPlayingCard var1, int var2, RummyView rummyView, RummyPlayingCard var4) {
      LinearLayout var8 = rummyView.getCard();

      //// setscreensize

      if(!isTablet(getActivity()))
      {
         RummyUtils.setViewWidth((LinearLayout)var8.findViewById(R.id.ll_cardview_main_container), RummyGameRoomCustomScreenLess700.stackCardWidth_fixed);
         RummyUtils.setViewWidth((RelativeLayout)var8.findViewById(R.id.card_view_root_layout), RummyGameRoomCustomScreenLess700.stackCardWidth_fixed);
         RummyUtils.setViewHeight((RelativeLayout)var8.findViewById(R.id.card_view_root_layout), RummyGameRoomCustomScreenLess700.stackCardHeight_fixed);
         RummyUtils.setViewWidth((ImageView)var8.findViewById(R.id.cardImageView), RummyGameRoomCustomScreenLess700.stackCardWidth_fixed);
         RummyUtils.setViewHeight((ImageView)var8.findViewById(R.id.cardImageView), RummyGameRoomCustomScreenLess700.stackCardHeight_fixed);
         RummyUtils.setViewHeight((ImageView)var8.findViewById(R.id.jokerCardImg), RummyGameRoomCustomScreenLess700.jokerCardHeight_fixed);
         RummyUtils.setViewWidth((ImageView)var8.findViewById(R.id.jokerCardImg), RummyGameRoomCustomScreenLess700.jokerCardWidth_fixed);
         RummyUtils.setViewWidth((ImageView)var8.findViewById(R.id.cardImageViewSelected), RummyGameRoomCustomScreenLess700.stackCardWidth_fixed);
         RummyUtils.setViewHeight((ImageView)var8.findViewById(R.id.cardImageViewSelected), RummyGameRoomCustomScreenLess700.stackCardHeight_fixed);
         RummyUtils.setViewWidth((ImageView)var8.findViewById(R.id.autoPlayIv), RummyGameRoomCustomScreenLess700.autoPlayIconSize_fixed);
         RummyUtils.setViewHeight((ImageView)var8.findViewById(R.id.autoPlayIv), RummyGameRoomCustomScreenLess700.autoPlayIconSize_fixed);
      }
      else
      {
         RummyUtils.setViewWidth((LinearLayout)var8.findViewById(R.id.ll_cardview_main_container), RummyGameRoomCustomScreenMore700.stackCardWidth_fixed);
         RummyUtils.setViewWidth((RelativeLayout)var8.findViewById(R.id.card_view_root_layout), RummyGameRoomCustomScreenMore700.stackCardWidth_fixed);
         RummyUtils.setViewHeight((RelativeLayout)var8.findViewById(R.id.card_view_root_layout), RummyGameRoomCustomScreenMore700.stackCardHeight_fixed);
         RummyUtils.setViewWidth((ImageView)var8.findViewById(R.id.cardImageView), RummyGameRoomCustomScreenMore700.stackCardWidth_fixed);
         RummyUtils.setViewHeight((ImageView)var8.findViewById(R.id.cardImageView), RummyGameRoomCustomScreenMore700.stackCardHeight_fixed);
         RummyUtils.setViewHeight((ImageView)var8.findViewById(R.id.jokerCardImg), RummyGameRoomCustomScreenMore700.jokerCardHeight_fixed);
         RummyUtils.setViewWidth((ImageView)var8.findViewById(R.id.jokerCardImg), RummyGameRoomCustomScreenMore700.jokerCardWidth_fixed);
         RummyUtils.setViewWidth((ImageView)var8.findViewById(R.id.cardImageViewSelected), RummyGameRoomCustomScreenMore700.stackCardWidth_fixed);
         RummyUtils.setViewHeight((ImageView)var8.findViewById(R.id.cardImageViewSelected), RummyGameRoomCustomScreenMore700.stackCardHeight_fixed);
         RummyUtils.setViewWidth((ImageView)var8.findViewById(R.id.autoPlayIv), RummyGameRoomCustomScreenMore700.autoPlayIconSize_fixed);
         RummyUtils.setViewHeight((ImageView)var8.findViewById(R.id.autoPlayIv), RummyGameRoomCustomScreenMore700.autoPlayIconSize_fixed);
      }



      ImageView var9 = (ImageView)var8.findViewById(R.id.cardImageView);
      ImageView var7 = (ImageView)var8.findViewById(R.id.jokerCardImg);
      String var6 = String.format("%s%s", new Object[]{var1.getSuit(), var1.getFace()});
      int var5 = this.getResources().getIdentifier(var6, "drawable", mBaseFragmentActivity.getPackageName());
      var9.setVisibility(View.VISIBLE);
      var9.setImageResource(var5);
      if(var4 != null) {
         if(var4.getFace().equalsIgnoreCase(var1.getFace())) {
            var7.setVisibility(View.VISIBLE);
         } else {
            var7.setVisibility(View.GONE);
         }
      }

      var9.setTag(String.format("%s%s", new Object[]{var6, String.valueOf(var2)}));
      var1.setIndex(String.valueOf(var2));
      rummyView.addCard(var8,true);
   }

   public static boolean isTablet(Context context) {
      return (context.getResources().getConfiguration().screenLayout
              & Configuration.SCREENLAYOUT_SIZE_MASK)
              >= Configuration.SCREENLAYOUT_SIZE_LARGE;
   }

   public void launchFragment(Fragment fragment, boolean addToBackStack, String tag) {
      FragmentTransaction fragmentTransaction = ((FragmentActivity)mBaseFragmentActivity).getSupportFragmentManager().beginTransaction();
      fragmentTransaction.replace(R.id.home_content_frame, fragment, tag);
      if (addToBackStack) {
         fragmentTransaction.addToBackStack(tag);
      }
      fragmentTransaction.commitAllowingStateLoss();
   }

   public void popFragment(String tag) {
      ((FragmentActivity)mBaseFragmentActivity).getSupportFragmentManager().popBackStack(tag, 1);
   }



   public void showDropImage(View view) {
      showView((RelativeLayout) view.findViewById(R.id.player_drop_iv));
      ((TextView)view.findViewById(R.id.player_name_tv)).setAlpha(.3f);
      //((TextView)view.findViewById(R.id.player_points_tv)).setAlpha(.3f);
      (view.findViewById(R.id.player_rating_bar)).setAlpha(.3f);
      (view.findViewById(R.id.player_system_iv)).setAlpha(.3f);
   }

   public void showWaitingImage(View view) {
      showView((RelativeLayout) view.findViewById(R.id.player_waiting_iv));
      ((TextView)view.findViewById(R.id.player_name_tv)).setAlpha(0);
    //  ((TextView)view.findViewById(R.id.player_points_tv)).setAlpha(.3f);
      (view.findViewById(R.id.player_rating_bar)).setAlpha(0);
      (view.findViewById(R.id.player_system_iv)).setAlpha(0);
   }

   private void addCardToSCRummyView(RummyPlayingCard var1, int var2, RummyView var3, RummyPlayingCard var4) {
      LinearLayout var9 = var3.getSmartCorrectionCard();
      ImageView var6 = (ImageView)var9.findViewById(R.id.cardImageView);
      ImageView var8 = (ImageView)var9.findViewById(R.id.jokerCardImg);
      String var7 = String.format("%s%s", new Object[]{var1.getSuit(), var1.getFace()});
      int var5 = this.getResources().getIdentifier(var7, "drawable", mBaseFragmentActivity.getPackageName());
      var6.setVisibility(View.VISIBLE);
      var6.setImageResource(var5);
      if(var4 != null) {
         if(var4.getFace().equalsIgnoreCase(var1.getFace())) {
            var8.setVisibility(View.VISIBLE);
         } else {
            var8.setVisibility(View.GONE);
         }
      }

      var6.setTag(String.format("%s%s", new Object[]{var7, String.valueOf(var2)}));
      var1.setIndex(String.valueOf(var2));
      var3.addCard(var9,true);
   }

   private void openBrowserToBuyChips(String TOKEN)
   {
      String url = RummyUtils.getApiSeverAddress()+"sendpaymentrequest/?client_type="
              + RummyUtils.CLIENT_TYPE+"&device_type="+getDeviceType();
      Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
      Bundle bundle = new Bundle();
      bundle.putString("Authorization", "Token "+TOKEN);
      i.putExtra(Browser.EXTRA_HEADERS, bundle);
      startActivity(i);
   }

   public String getRestAmounttoAdd(String betAmount)
   {
      RummyApplication mRummyApplication  = RummyApplication.getInstance();

      double userbalance = 0;
      if(!mRummyApplication.getUserData().getRealChips().equalsIgnoreCase("") && mRummyApplication.getUserData().getRealChips() != null)
      {
         userbalance = Math.round(Double.parseDouble(mRummyApplication.getUserData().getRealChips()));
      }

      double betammount = 0;
      if(!betAmount.equalsIgnoreCase("") && betAmount != null)
      {
         betammount = Math.round(Double.parseDouble(betAmount));
      }


      double lowBalance = betammount - userbalance;

      return (lowBalance+1)+"";
   }

   public void checkPlayerDeposit(final Context context)
   {
      try
      {

            RummyCommonEventTracker.trackAddCashClickEvent(RummyCommonEventTracker.Add_Cash, RummyPrefManager.getString(mBaseFragmentActivity, RummyConstants.PLAYER_USER_ID, ""),context);
            trackPaymentInitiatedEventWE();

         showLoadingDialog(mBaseFragmentActivity);
         RequestQueue queue = RummyVolleySingleton.getInstance(context).getRequestQueue();
         final String TOKEN = RummyPrefManager.getString(context, ACCESS_TOKEN_REST, "");

         String apiURL = RummyUtils.getApiSeverAddress()+"api/v1/player-deposit-check/";

         final StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL,
                 new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response)
                    {
                       Log.d(TAG, "Response: "+response.toString());

                       try
                       {
                          dismissLoadingDialog();
                          JSONObject jsonResponse = new JSONObject(response);
                          jsonResponse = jsonResponse.getJSONObject("data");

                          boolean is_pending_bonus = jsonResponse.getBoolean("is_pending_bonus");

                          if(is_pending_bonus)
                          {
                             String msg_to_show = jsonResponse.getString("bonus_message");

                             final Dialog dialog = new Dialog(context, R.style.DialogTheme);
                             dialog.requestWindowFeature(1);
                             //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                             dialog.setContentView(R.layout.rummy_dialog_confirm_dynamic);
                             dialog.setCancelable(false);
                             ((TextView) dialog.findViewById(R.id.title)).setText(msg_to_show);
                             ((Button) dialog.findViewById(R.id.yes_btn)).setOnClickListener(new OnClickListener() {
                                public void onClick(View v) {
                                   dialog.dismiss();
                                   /*if(RummyLoginRequest.flash.equalsIgnoreCase("1")){
                                      Intent intent = new Intent(mBaseFragmentActivity, DepositActivity.class);
                                      intent.putExtra("response", response.toString());
                                      intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                      startActivity(intent);
                                   }else {
                                      launchFragment(new RummyDepositFragment(), true, RummyDepositFragment.class.getName());
                                   }*/
                                }
                             });
                             ((Button) dialog.findViewById(R.id.no_btn)).setOnClickListener(new OnClickListener() {
                                public void onClick(View v) {
                                   dialog.dismiss();
                                }
                             });
                             dialog.show();
                          }
                          else
                          {
                             if(RummyLoginRequest.flash.equalsIgnoreCase("1")){
                                //PARTH
                                Intent intent = new Intent(mBaseFragmentActivity, RummyHomeActivity.class);
                                intent.putExtra("response", response.toString());
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                             }else {
                             //   launchFragment(new RummyDepositFragment(), true, RummyDepositFragment.class.getName());
                             }
                          }


                          //launchFragment(new DepositFragment(), true, DepositFragment.class.getName());


                       }
                       catch (Exception e){
                           Log.e(TAG,"json error = "+e.toString());
                       }
                    }
                 },
                 new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       Log.e(TAG, "Error Resp: " + error.toString());
                       dismissLoadingDialog();
                       NetworkResponse response = error.networkResponse;
                       if (error instanceof ServerError && response != null) {
                          try {
                             String res = new String(response.data,
                                     HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                             Log.d(TAG, "Error: "+res);
                             if(res != null) {
                                try{
                                   JSONObject json = new JSONObject(res.toString());
                                   if(json.getString("status").equalsIgnoreCase("Error"))
                                   {
                                      showGenericDialog(context, json.getString("message"));
                                   }
                                }
                                catch (Exception e){
                                   Log.e(TAG, "EXP: parsing error for login -->> "+e.toString());
                                }
                             }
                          } catch (UnsupportedEncodingException e1) {
                             // Couldn't properly decode data to string
                             e1.printStackTrace();
                          }
                       }
                    }
                 })
         {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
               HashMap<String, String> headers = new HashMap<String, String>();
               headers.put("Authorization", "Token "+TOKEN);
               return headers;
            }

            @Override
            public String getBodyContentType() {
               return "application/x-www-form-urlencoded; charset=UTF-8";
            }
         };

         stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                 DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, //TIMEOUT INTERVAL (Default: 2500ms)
                 2,    //No.Of Retries (Default: 1)
                 2));  //BackOff Multiplier (Default: 1.0)

         //add request to queue
         queue.add(stringRequest);

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void trackPaymentInitiatedEventWE()
   {
      Map<String, Object> map = new HashMap<>();
      map.put(RummyCommonEventTracker.USER_ID, RummyPrefManager.getString(mBaseFragmentActivity, RummyConstants.PLAYER_USER_ID, ""));
      map.put(RummyCommonEventTracker.DEVICE_TYPE, getDeviceType());
      map.put(RummyCommonEventTracker.CLIENT_TYPE, RummyUtils.CLIENT_TYPE);

      Bundle bundle=new Bundle();
      bundle.putString(RummyCommonEventTracker.USER_ID, RummyPrefManager.getString(mBaseFragmentActivity, RummyConstants.PLAYER_USER_ID, ""));
      bundle.putString(RummyCommonEventTracker.DEVICE_TYPE, getDeviceType());
      bundle.putString(RummyCommonEventTracker.CLIENT_TYPE, RummyUtils.CLIENT_TYPE);

      RummyCommonEventTracker.trackEvent(RummyCommonEventTracker.PAYMENT_PAGE_VISITED, map,mBaseFragmentActivity,bundle);
   }

   public void hideKeyboard()
   {
      InputMethodManager imm = (InputMethodManager) mBaseFragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
   }


   @Override
   public void onAttach(Context context) {
      super.onAttach(context);
      this.mBaseFragmentActivity = (Activity) context;
      Log.e("vikas","onattach call lobby bragment");
   }

   @Override
   public void onDetach() {
      super.onDetach();

      Log.e("vikas","on detach call lobby bragment");
   }

   public boolean checkAndAskMessagePermission(Activity context) {
      if (Build.VERSION.SDK_INT >= 23) {

         if (context.checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
            return false;
         }
         return true;
      }
      return true;
   }



   protected  void handleResult(RummyApiResult<JSONObject> result, RummyResultListener rummyResultListener){
      if(!result.isLoading()){
         if(result.isSuccess()){
            rummyResultListener.onSuccess(result.getResult());
         }else if(!result.isConsumed()){
            rummyResultListener.onFailed(result.getErrorMessage());
         }
      }
   }
}
