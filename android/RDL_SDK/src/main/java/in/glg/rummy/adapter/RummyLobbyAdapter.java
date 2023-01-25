package in.glg.rummy.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.glg.rummy.R;
import in.glg.rummy.RummyApplication;
import in.glg.rummy.activities.RummyHomeActivity;
import in.glg.rummy.activities.RummyInstance;
import in.glg.rummy.api.response.RummyLoginResponse;
import in.glg.rummy.fragments.RummyLobbyFragment;
import in.glg.rummy.models.RummyTable;
import in.glg.rummy.utils.RummyPrefManager;
import in.glg.rummy.utils.RummyTLog;
import in.glg.rummy.utils.RummyUtils;

import static in.glg.rummy.utils.RummyConstants.ACCESS_TOKEN_REST;

public class RummyLobbyAdapter extends BaseAdapter
{
   private static final String TAG = RummyLobbyAdapter.class.getSimpleName();
   private Context context;
   private LayoutInflater inflater;
   private RummyLobbyFragment mLobbyFragment;
   private List<RummyTable> tables;

   public RummyLobbyAdapter(Context ctx, List<RummyTable> tables, RummyLobbyFragment lobbyFragment) {
      this.context = ctx;
      this.tables = tables;
      this.mLobbyFragment = lobbyFragment;
      this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   }

   private void setUserSeating(RummyTable t, ImageView user1, ImageView user2, ImageView user3, ImageView user4, ImageView user5, ImageView user6, ImageView tableImage) {

      if(Integer.parseInt(t.getMaxplayer()) == 2)
      {
         switch (Integer.parseInt(t.getCurrent_players())) {
            case 0:
               tableImage.setImageResource(R.drawable.rummy_two_player_no_live);
               break;
            case 1:
               tableImage.setImageResource(R.drawable.rummy_two_player_one_live);
               break;
            case 2:
               tableImage.setImageResource(R.drawable.rummy_two_player_two_live);
               break;
         }
      }
      else
      {
         switch (Integer.parseInt(t.getCurrent_players())) {
            case 0:
               tableImage.setImageResource(R.drawable.rummy_six_player_no_live);
               break;
            case 1:
               tableImage.setImageResource(R.drawable.rummy_six_player_one_live);
               break;
            case 2:
               tableImage.setImageResource(R.drawable.rummy_six_player_two_live);
               break;
            case 3:
               tableImage.setImageResource(R.drawable.rummy_six_player_three_live);
               break;
            case 4:
               tableImage.setImageResource(R.drawable.rummy_six_player_four_live);
               break;
            case 5:
               tableImage.setImageResource(R.drawable.rummy_six_player_five_live);
               break;
            case 6:
               tableImage.setImageResource(R.drawable.rummy_six_player_six_live);
               break;
         }
      }

/*
      switch (Integer.parseInt(t.getCurrent_players())) {
         case 0:
            user1.setImageResource(R.drawable.rummy_lobby_player_off);
            user2.setImageResource(R.drawable.rummy_lobby_player_off);
            user3.setImageResource(R.drawable.rummy_lobby_player_off);
            user4.setImageResource(R.drawable.rummy_lobby_player_off);
            user5.setImageResource(R.drawable.rummy_lobby_player_off);
            user6.setImageResource(R.drawable.rummy_lobby_player_off);
            break;
         case 1:
            user1.setImageResource(R.drawable.rummy_lobby_player_off);
            user2.setImageResource(R.drawable.rummy_lobby_player_off);
            user3.setImageResource(R.drawable.rummy_lobby_player_off);
            user4.setImageResource(R.drawable.rummy_lobby_player_on);
            user5.setImageResource(R.drawable.rummy_lobby_player_off);
            user6.setImageResource(R.drawable.rummy_lobby_player_off);
            tableImage.setImageResource(R.drawable.rummy_lobby_players_1_icon);
            break;
         case 2:
            user1.setImageResource(R.drawable.rummy_lobby_player_on);
            user2.setImageResource(R.drawable.rummy_lobby_player_off);
            user3.setImageResource(R.drawable.rummy_lobby_player_off);
            user4.setImageResource(R.drawable.rummy_lobby_player_on);
            user5.setImageResource(R.drawable.rummy_lobby_player_off);
            user6.setImageResource(R.drawable.rummy_lobby_player_off);
            tableImage.setImageResource(R.drawable.rummy_lobby_players_2_icon);
            break;
         case 3:
            user1.setImageResource(R.drawable.rummy_lobby_player_on);
            user2.setImageResource(R.drawable.rummy_lobby_player_on);
            user3.setImageResource(R.drawable.rummy_lobby_player_on);
            user4.setImageResource(R.drawable.rummy_lobby_player_off);
            user5.setImageResource(R.drawable.rummy_lobby_player_off);
            user6.setImageResource(R.drawable.rummy_lobby_player_off);
            tableImage.setImageResource(R.drawable.rummy_lobby_players_3_icon);
            break;
         case 4:
            user1.setImageResource(R.drawable.rummy_lobby_player_on);
            user2.setImageResource(R.drawable.rummy_lobby_player_on);
            user3.setImageResource(R.drawable.rummy_lobby_player_on);
            user4.setImageResource(R.drawable.rummy_lobby_player_on);
            user5.setImageResource(R.drawable.rummy_lobby_player_off);
            user6.setImageResource(R.drawable.rummy_lobby_player_off);
            tableImage.setImageResource(R.drawable.rummy_lobby_players_4_icon);
            break;
         case 5:
            user1.setImageResource(R.drawable.rummy_lobby_player_on);
            user2.setImageResource(R.drawable.rummy_lobby_player_on);
            user3.setImageResource(R.drawable.rummy_lobby_player_on);
            user4.setImageResource(R.drawable.rummy_lobby_player_on);
            user5.setImageResource(R.drawable.rummy_lobby_player_on);
            user6.setImageResource(R.drawable.rummy_lobby_player_off);
            tableImage.setImageResource(R.drawable.rummy_lobby_players_5_icon);
            break;
         case 6:
            user1.setImageResource(R.drawable.rummy_lobby_player_on);
            user2.setImageResource(R.drawable.rummy_lobby_player_on);
            user3.setImageResource(R.drawable.rummy_lobby_player_on);
            user4.setImageResource(R.drawable.rummy_lobby_player_on);
            user5.setImageResource(R.drawable.rummy_lobby_player_on);
            user6.setImageResource(R.drawable.rummy_lobby_player_on);
            tableImage.setImageResource(R.drawable.rummy_lobby_players_6_icon);
            break;
      }
      if (Integer.parseInt(t.getMaxplayer()) == 2) {
         *//*user1.setVisibility(View.VISIBLE);
         user2.setVisibility(View.INVISIBLE);
         user3.setVisibility(View.INVISIBLE);
         user4.setVisibility(View.VISIBLE);
         user5.setVisibility(View.INVISIBLE);
         user6.setVisibility(View.INVISIBLE);*//*
         tableImage.setImageResource(R.drawable.rummy_lobby_players_2_icon);
         return;
      }*/
   }

   private void showBuyInPopUp(RummyTable table) {
      try {
      String balance;
      RummyApplication app = RummyApplication.getInstance();
      RummyLoginResponse userData = (RummyApplication.getInstance()).getUserData();
      int playerBalance;

      final DecimalFormat format = new DecimalFormat("0.#");
      if (table.getTable_cost().equalsIgnoreCase("CASH_CASH")) {
         balance = app.getUserData().getRealChips();
         playerBalance = (int) Float.parseFloat(userData.getRealChips());
      } else {
         balance = app.getUserData().getFunChips();
         playerBalance = (int) Float.parseFloat(userData.getFunChips());
      }
      final Dialog dialog = new Dialog(this.context);
      dialog.requestWindowFeature(1);
      dialog.setContentView(R.layout.table_details_pop_up);
      dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
      dialog.show();
      TextView minBuyTv = (TextView) dialog.findViewById(R.id.min_buy_value_tv);
      TextView maxBuyTv = (TextView) dialog.findViewById(R.id.max_buy_value_tv);
      TextView balanceTv = (TextView) dialog.findViewById(R.id.balance_value_tv);
      TextView minValSeekbar = (TextView) dialog.findViewById(R.id.min_val_seekbar);
      TextView maxValSeekbar = (TextView) dialog.findViewById(R.id.max_val_seekbar);
      final EditText buyInTv = (EditText) dialog.findViewById(R.id.buy_in_value_tv);
      ((TextView) dialog.findViewById(R.id.bet_value_tv)).setText(table.getBet()+"");
      minBuyTv.setText(table.getMinimumbuyin()+"");
      maxBuyTv.setText(table.getMaximumbuyin()+"");
         minValSeekbar.setText(table.getMinimumbuyin()+"");
         maxValSeekbar.setText(table.getMaximumbuyin()+"");
      final String maximumBuyIn = table.getMaximumbuyin();
      final int max;

      if(playerBalance<Integer.parseInt(maximumBuyIn))
         max = playerBalance;
      else
         max = Integer.parseInt(maximumBuyIn);

      final int min = Integer.parseInt(table.getMinimumbuyin());

      boolean decreaseBalance = true;
      if (balance.contains(".")) {
         String subBalance = balance.substring(balance.lastIndexOf(".") + 1);
         if (subBalance != null && subBalance.length() > 0) {
            decreaseBalance = Integer.parseInt(subBalance) > 50;
         }
      }
      final float balanceInt = new Float((float) Math.round(Float.parseFloat(balance))).floatValue();
      balanceTv.setText(String.valueOf(format.format((double) balanceInt)));
      final RummyTable table2 = table;
      ((Button) dialog.findViewById(R.id.join_btn)).setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            dialog.dismiss();
            if (buyInTv.getText() == null || buyInTv.getText().length() <= 0) {
               RummyLobbyAdapter.this.mLobbyFragment.showErrorPopUp("Please enter minimum amount");
               return;
            }
            float selectedBuyInAmt = Float.valueOf(buyInTv.getText().toString()).floatValue();
            if (selectedBuyInAmt <= balanceInt || selectedBuyInAmt >= Float.valueOf((float) max).floatValue()) {
               if (selectedBuyInAmt > Float.valueOf((float) max).floatValue()) {
                  //RummyLobbyAdapter.this.mLobbyFragment.showErrorPopUp("You can take only ( " + maximumBuyIn + " ) " + "in to the table");
                  RummyLobbyAdapter.this.mLobbyFragment.showErrorBuyChipsDialog(context,"You have insufficient balance in your wallet");
               } else if (selectedBuyInAmt < Float.valueOf((float) min).floatValue()) {
                  RummyLobbyAdapter.this.mLobbyFragment.showErrorPopUp("Please enter minimum amount");
               } else {
                //  RummyLobbyAdapter.this.mLobbyFragment.joinTable(table2, buyInTv.getText().toString());
                  sendJoinTableDataToServer(table2, buyInTv.getText().toString());
               }
            } else if (table2.getTable_cost().contains("CASH_CASH")) {
               String msg = ""+context.getResources().getString(R.string.rummy_low_balance_first)+" "+context.getResources().getString(R.string.rupee_symbol)+getRestAmounttoAdd(selectedBuyInAmt+"")+" "+context.getResources().getString(R.string.rummy_low_balance_second);
               showErrorBalanceBuyChips(msg,selectedBuyInAmt+"");

            } else {
               RummyLobbyAdapter.this.mLobbyFragment.showLowBalanceDialog(RummyLobbyAdapter.this.context, RummyLobbyAdapter.this.context.getResources().getString(R.string.low_balance_free_chip));
            }
         }
      });
      ((Button) dialog.findViewById(R.id.cancel_btn)).setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            dialog.dismiss();
         }
      });
      //SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.seek_bar);
      IndicatorSeekBar seekBar =  dialog.findViewById(R.id.seek_bar_buy_in);
      seekBar.setMax((max - min) / 1);
      seekBar.setProgress(seekBar.getMax());
      if (Float.valueOf((float) max).floatValue() <= balanceInt) {
         buyInTv.setText((int)(max)+"");
      } else {
         float newBalance =  balanceInt;
         if (decreaseBalance) {
            newBalance = (int) (balanceInt - 1.0f);
         }
         buyInTv.setText(format.format((double) newBalance));
      }
      final int i = min;
      final float f = balanceInt;
      final EditText editText = buyInTv;
      /*seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
         public void onStopTrackingTouch(SeekBar seekBar) {
         }

         public void onStartTrackingTouch(SeekBar seekBar) {
         }

         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            String sliderValue = "";
            Float sliderValue_num = (float) (i + (progress * 1));

            if (sliderValue_num >= f) {
               sliderValue = String.valueOf(format.format((double) f));
            }
            else
            {
               sliderValue = String.valueOf(format.format((double) sliderValue_num));
            }
            editText.setText(sliderValue);
         }
      });*/

      seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
         @Override
         public void onSeeking(SeekParams seekParams) {

            String sliderValue = "";
            Float sliderValue_num = (float) (i + (seekParams.progress * 1));

            if (sliderValue_num >= f) {
               sliderValue = format.format((double) f);
            }
            else
            {
               sliderValue = format.format((double) sliderValue_num);
            }
            editText.setText(sliderValue);

         }

         @Override
         public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

         }

         @Override
         public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

         }
      });

      }catch (Exception e){
         Log.e(TAG+"",e+"");
      }
   }

   public void sendJoinTableDataToServer(final RummyTable tableToJoin, final String buyInAmount)
   {
      this.mLobbyFragment.showLoadingDialog(context);
      try {
         final String TOKEN = RummyPrefManager.getString(context, ACCESS_TOKEN_REST, "");

         RummyTLog.e("token =",TOKEN);

         String url = RummyUtils.getApiSeverAddress()+ RummyUtils.gameJoinPR;
         RequestQueue queue = Volley.newRequestQueue(context);

         Map<String, String> params = new HashMap<String, String>();
         params.clear();
         params.put("tableid", tableToJoin.getTable_id());
         params.put("bet", tableToJoin.getBet());
         params.put("amount",buyInAmount);
         params.put("game_type",tableToJoin.getTable_type());
         params.put("id",tableToJoin.getId());
         params.put("amount_type",tableToJoin.getTable_cost());
         params.put("user_id",RummyApplication.getInstance().getUserData().getUserId());

         final RummyApplication rummyApplication = RummyApplication.getInstance();
         rummyApplication.setCurrentTableAmount(buyInAmount);
         rummyApplication.setCurrentTableBet(tableToJoin.getBet());
         rummyApplication.setCurrentTableGameType(tableToJoin.getTable_type());
         rummyApplication.setCurrentTableSeqId(tableToJoin.getId());
         rummyApplication.setCurrentTableUserId(RummyApplication.getInstance().getUserData().getUserId());
         rummyApplication.setCurrentTableId(tableToJoin.getTable_id());

         JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                 Request.Method.POST, url, new JSONObject(params),
                 new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       mLobbyFragment.dismissLoadingDialog();
                       RummyTLog.e(TAG+"joining response", response.toString());
                       try {
                          String status = response.getString("status");
                          String message = response.getString("message");
                          String order_id = "";
                          if(response.has("order_id"))
                          {
                             order_id = response.getString("order_id");
                          }

                          rummyApplication.setCurrentTableOrderId(order_id);
                          if (status.equalsIgnoreCase("Success")) {
                             if(tableToJoin.getTable_type().startsWith("PR"))
                             {
                                RummyLobbyAdapter.this.mLobbyFragment.joinTable(tableToJoin, buyInAmount);
                             }
                             else
                             {
                                processToJoinGame(tableToJoin);
                             }

                          }
                          else
                          {
                             if(message.equalsIgnoreCase("LOW_BALANCE"))
                             {
                                RummyUtils.showGenericMsgDialog(context,message);
                             }
                             else {
                                RummyUtils.showGenericMsgDialog(context,message);
                             }

                          }
                       } catch (Exception e) {
                          RummyTLog.e(TAG, "JsonException" + e.toString());
                          RummyUtils.showGenericMsgDialog(context,"Something went wrong");
                       }


                    }
                 }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               mLobbyFragment.dismissLoadingDialog();
               RummyTLog.e("vikas","calling error response");


               RummyTLog.e(TAG, "Error: " + error.getMessage());

               if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                  RummyTLog.e(TAG, "Error: " +"Volley Time out error");
               } else if (error instanceof AuthFailureError) {
                  RummyTLog.e(TAG, "Error: " +"Volley Authentication error");
               } else if (error instanceof ServerError) {
                  RummyTLog.e(TAG, "Error: " +"Volley server error");
               } else if (error instanceof NetworkError) {
                  RummyTLog.e(TAG, "Error: " +"Volley Network error");
               } else if (error instanceof ParseError) {
                  RummyTLog.e(TAG, "Error: " +"Volley parse error");
               }
               RummyUtils.showGenericMsgDialog(context,"Something went wrong, Please try again");
            }
         }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
               HashMap<String, String> headers = new HashMap<String, String>();
               headers.put("Authorization", "Token "+TOKEN);
               headers.put("Content-Type", "application/json; charset=utf-8");

               return headers;
            }
         };

         jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                 60000, //TIMEOUT INTERVAL (Default: 2500ms)
                 1,    //No.Of Retries (Default: 1)
                 1));  //BackOff Multiplier (Default: 1.0)

         RummyTLog.e(TAG,"URl = "+jsonObjReq.toString() +" params = "+params.toString());

         queue.add(jsonObjReq);
      }
      catch (Exception e) {
         mLobbyFragment.dismissLoadingDialog();
         RummyUtils.showGenericMsgDialog(context,"Something went wrong, Please try again after some time");
         e.printStackTrace();
      }
   }

   private void processToJoinGame(RummyTable t)
   {
      if(t.getJoined_players().contains(RummyPrefManager.getString(context, "username", " ")))
      {
         if (!t.getTable_type().startsWith(RummyUtils.PR)) {
            //sendJoinTableDataToServer(t, t.getBet());
            RummyLobbyAdapter.this.mLobbyFragment.joinTable(t, "0");
         } else if (RummyLobbyAdapter.this.mLobbyFragment.isFoundTable(t)) {
            RummyLobbyAdapter.this.mLobbyFragment.setSelectedTable(t);
            RummyLobbyAdapter.this.mLobbyFragment.launchTableActivity();
         } else {
            String balance;
            RummyLobbyAdapter.this.mLobbyFragment.setSelectedTable(t);
            RummyApplication app = (RummyApplication.getInstance());
            DecimalFormat format = new DecimalFormat("0.#");
            if (t.getTable_cost().equalsIgnoreCase("CASH_CASH")) {
               balance = app.getUserData().getRealChips();
            } else {
               balance = app.getUserData().getFunChips();
            }
            if (Math.round(Float.parseFloat(balance)) >= Math.round(Float.parseFloat(t.getMinimumbuyin()))) {
               try {
                  RummyLobbyAdapter.this.showBuyInPopUp(t);
               }catch (Exception e){
                  RummyTLog.e(TAG+"",e+"");
               }
            } else if (t.getTable_cost().contains("CASH_CASH")) {
               String msg = ""+context.getResources().getString(R.string.rummy_low_balance_first)+" "+context.getResources().getString(R.string.rupee_symbol)+getRestAmounttoAdd(t.getMinimumbuyin()+"")+" "+context.getResources().getString(R.string.rummy_low_balance_second);
               showErrorBalanceBuyChips(msg,t.getMinimumbuyin());
            } else {
               RummyLobbyAdapter.this.mLobbyFragment.showLowBalanceDialog(RummyLobbyAdapter.this.context, RummyLobbyAdapter.this.context.getResources().getString(R.string.low_balance_free_chip));
            }
         }
      }
      else
      {
         if (!t.getTable_type().startsWith(RummyUtils.PR)) {
            RummyLobbyAdapter.this.mLobbyFragment.joinTable(t, "0");
         } else if (RummyLobbyAdapter.this.mLobbyFragment.isFoundTable(t)) {
            RummyLobbyAdapter.this.mLobbyFragment.setSelectedTable(t);
            RummyLobbyAdapter.this.mLobbyFragment.launchTableActivity();
         } else {
            String balance;
            RummyLobbyAdapter.this.mLobbyFragment.setSelectedTable(t);
            RummyApplication app = (RummyApplication.getInstance());
            DecimalFormat format = new DecimalFormat("0.#");
            if (t.getTable_cost().equalsIgnoreCase("CASH_CASH")) {
               balance = app.getUserData().getRealChips();
            } else {
               balance = app.getUserData().getFunChips();
            }
            if (new Float((float) Math.round(Float.parseFloat(balance))).floatValue() >= Float.valueOf(t.getMinimumbuyin()).floatValue()) {
               try {
                  RummyLobbyAdapter.this.showBuyInPopUp(t);
               }catch (Exception e){
                  RummyTLog.e(TAG+"",e+"");
               }
            } else if (t.getTable_cost().contains("CASH_CASH")) {
               String msg = ""+context.getResources().getString(R.string.rummy_low_balance_first)+" "+context.getResources().getString(R.string.rupee_symbol)+getRestAmounttoAdd(t.getMinimumbuyin()+"")+" "+context.getResources().getString(R.string.rummy_low_balance_second);
               showErrorBalanceBuyChips(msg,t.getMinimumbuyin());
            } else {

               RummyLobbyAdapter.this.mLobbyFragment.showLowBalanceDialog(RummyLobbyAdapter.this.context, RummyLobbyAdapter.this.context.getResources().getString(R.string.low_balance_free_chip));
            }
         }
         // openConfirmDialog(t);
      }

   }

   private String getRestAmounttoAdd(String betAmount)
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

      return lowBalance+"";
   }

   public int getCount() {
      return this.tables.size();
   }

   public RummyTable getItem(int position) {
      return (RummyTable) this.tables.get(position);
   }

   public long getItemId(int position) {
      return (long) position;
   }

   public View getView(int position, View convertView, ViewGroup parent) {
      View v;
      if (convertView == null) {
         v = this.inflater.inflate(R.layout.rummy_lobby_adapter_item, parent, false);
      } else {
         v = convertView;
      }
      final RummyTable t = getItem(position);
      String tableType = RummyUtils.formatTableName(t.getTable_type());
      if(tableType.equalsIgnoreCase("pr - joker"))
         tableType="Points";
      setUserSeating(t, (ImageView) v.findViewById(R.id.user_1), (ImageView) v.findViewById(R.id.user_2), (ImageView) v.findViewById(R.id.user_3), (ImageView) v.findViewById(R.id.user_4), (ImageView) v.findViewById(R.id.user_5), (ImageView) v.findViewById(R.id.user_6),(ImageView) v.findViewById(R.id.table_iv));
      ImageView playerIv = (ImageView) v.findViewById(R.id.lobby_players_iv);
      final ImageView iv_favourite = (ImageView) v.findViewById(R.id.iv_favourite);
      TextView seatingTv = (TextView) v.findViewById(R.id.table_siting);
      TextView joinTv = (TextView) v.findViewById(R.id.join_tv);
      LinearLayout llcontainer = v.findViewById(R.id.container);
      TextView noOfPlayersTv = (TextView) v.findViewById(R.id.noOfPlayersTv);
      if (Integer.parseInt(t.getCurrent_players()) > 0) {
         playerIv.setImageResource(R.drawable.rummy_lobby_item_players_blue);
         seatingTv.setTextColor(ContextCompat.getColor(this.context, R.color.rummy_app_blue_light));
         joinTv.setTextColor(ContextCompat.getColor(this.context, R.color.rummy_white));
      } else {
         playerIv.setImageResource(R.drawable.rummy_lobby_item_players_blue);
         seatingTv.setTextColor(ContextCompat.getColor(this.context, R.color.rummy_black));
         joinTv.setTextColor(ContextCompat.getColor(this.context, R.color.rummy_white));
      }
      //noOfPlayersTv.setText(Html.fromHtml("<b>" + t.getTotal_player() + "</b>" + "<font color=#A1A09F>" + " Players" + "</font>"));
      noOfPlayersTv.setText(t.getTotal_player());
      ((TextView) v.findViewById(R.id.table_type)).setText(tableType);
      //((TextView) v.findViewById(R.id.table_bet)).setText("Bet: " + t.getBet() + " " + "Chips: " + WordUtils.capitalize(Utils.getChipsType(t.getTable_cost())));
      ((TextView) v.findViewById(R.id.betAmount)).setText(t.getBet());
      ((TextView) v.findViewById(R.id.chipsAmount)).setText(WordUtils.capitalize(RummyUtils.getChipsType(t.getTable_cost())));

      if(t.getFavorite().equalsIgnoreCase("true")) {
         iv_favourite.setImageResource(R.drawable.rummy_favourite_icon_selected);
      }
      else
      {
         iv_favourite.setImageResource(R.drawable.rummy_favourite_icon);
      }

      seatingTv.setText(this.context.getString(R.string.seating) + " " + t.getCurrent_players() + " of " + t.getMaxplayer());
      ((TextView) v.findViewById(R.id.table_total_players)).setText(t.getTotal_player());

      iv_favourite.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {

            Log.e("gopal","icon clicked");
            String favValue="0";
            if(t.getFavorite().equalsIgnoreCase("false")) {
               favValue="1";
               iv_favourite.setImageResource(R.drawable.rummy_favourite_icon_selected);
               t.setFavorite("true");
               (RummyApplication.getInstance()).addFavorite(t);
            }
            else
            {
               iv_favourite.setImageResource(R.drawable.rummy_favourite_icon);
               t.setFavorite("false");
               (RummyApplication.getInstance()).removeFavorite(t);
            }
            notifyDataSetChanged();
            RummyLobbyAdapter.this.mLobbyFragment.SetFavouriteData(favValue,t);
         }
      });
      joinTv.setOnClickListener(new OnClickListener() {
         public void onClick(View v)
         {



            if(RummyUtils.isNetworkAvailable(context))
            {
               if(t.getTable_type().startsWith(RummyUtils.PR))
               {
                  if(RummyApplication.getInstance().getJoinedTableIds().size() <= 1)
                  {
                     processToJoinGame(t);
                  }
                  else
                  {
                     if(mLobbyFragment !=null)
                     {
                        mLobbyFragment.showGenericDialog(context, context.getString(R.string.max_table_reached_msg));
                     }

                  }

               }
               else
               {
                  if(RummyApplication.getInstance().getJoinedTableIds().size() <= 1)
                  {
                     String balance;
                     RummyLobbyAdapter.this.mLobbyFragment.setSelectedTable(t);
                     RummyApplication app = (RummyApplication.getInstance());
                     DecimalFormat format = new DecimalFormat("0.#");
                     if (t.getTable_cost().equalsIgnoreCase("CASH_CASH")) {
                        balance = app.getUserData().getRealChips();
                     } else {
                        balance = app.getUserData().getFunChips();
                     }
                     if (Math.round(Float.parseFloat(balance)) >= Math.round(Float.parseFloat(t.getBet()))) {
                        try {
                          // openConfirmDialog(t);
                           sendJoinTableDataToServer(t,t.getBet());
                        }catch (Exception e){
                           RummyTLog.e(TAG+"",e+"");
                        }
                     } else if (t.getTable_cost().contains("CASH_CASH")) {
                        String msg = "" + context.getResources().getString(R.string.rummy_low_balance_first) + " " + context.getResources().getString(R.string.rupee_symbol) + getRestAmounttoAdd(t.getBet() + "") + " " + context.getResources().getString(R.string.rummy_low_balance_second);
                        showErrorBalanceBuyChips(msg, t.getBet());
                     }
                     else
                     {
                        showLowBalanceDialog(context,context.getResources().getString(R.string.low_balance_free_chip));
                     }

                  }
                  else
                  {
                     if(mLobbyFragment !=null)
                     {
                        mLobbyFragment.showGenericDialog(context, context.getString(R.string.max_table_reached_msg));
                     }
                  }

               }
            }
            else
            {
               final Dialog dialog = new Dialog(context);
               dialog.requestWindowFeature(1);
               dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
               dialog.setContentView(R.layout.rummy_dialog_no_internet);
               ((TextView)dialog.findViewById(R.id.message)).setText("No Internet connection, make sure your Wi-fi or Mobile data connection is turned on, then try again.");
               ((Button) dialog.findViewById(R.id.ok_btn)).setOnClickListener(new View.OnClickListener() {
                  public void onClick(View v) {
                     dialog.dismiss();
                  }
               });

               dialog.show();
            }


         }
      });
      return v;
   }


   public void notifyDataSetChanged() {
      super.notifyDataSetChanged();
   }

    public void showErrorBalanceBuyChips(String text, final String betAmount)
    {
       String msg1 = "Add"+" "+context.getResources().getString(R.string.rupee_symbol)+getRestAmounttoAdd(betAmount) +" "+"to join this table";
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.rummy_dialog_error_balance_buy_chips);

        TextView label = (TextView) dialog.findViewById(R.id.dialog_msg_tv);
       TextView tv_msg1 = (TextView)dialog.findViewById(R.id.tv_add_rest_amount_msg);
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
                  dialog.dismiss();
                 // mLobbyFragment.checkPlayerDeposit(context);
               handleLowBalanceListener(betAmount);

            }
        });

        dialog.show();
    }

   private void handleLowBalanceListener(String betAmount)  // type is bet is ponts rummy or not
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

      RummyInstance.getInstance().getRummyListener().lowBalance(lowBalance+1);
   }



   public void showLowBalanceDialog(Context context, String message) {
      final Dialog dialog = new Dialog(context, R.style.DialogTheme);
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
}
