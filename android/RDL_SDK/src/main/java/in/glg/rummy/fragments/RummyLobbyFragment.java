package in.glg.rummy.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.glg.rummy.GameRoom.RummyTableActivity;
import in.glg.rummy.R;
import in.glg.rummy.RummyApplication;
import in.glg.rummy.activities.RummyInstance;
import in.glg.rummy.adapter.RummyLobbyAdapter;
import in.glg.rummy.adapter.RummyVariantsAdapter;
import in.glg.rummy.api.RummyOnResponseListener;
import in.glg.rummy.api.requests.RummyLobbyDataRequest;
import in.glg.rummy.api.response.RummyJoinTableResponse;
import in.glg.rummy.api.response.RummyLobbyTablesResponse;
import in.glg.rummy.api.response.RummyLoginResponse;
import in.glg.rummy.engine.RummyGameEngine;
import in.glg.rummy.enums.RummyGameEvent;
import in.glg.rummy.exceptions.RummyGameEngineNotRunning;
import in.glg.rummy.models.RummyBaseTrRequest;
import in.glg.rummy.models.RummyEvent;
import in.glg.rummy.models.RummyFavouriteRequest;
import in.glg.rummy.models.RummyJoinRequest;
import in.glg.rummy.models.RummyJoinedTable;
import in.glg.rummy.models.RummySearchTableRequest;
import in.glg.rummy.models.RummyTable;
import in.glg.rummy.utils.RummyCommonEventTracker;
import in.glg.rummy.utils.RummyErrorCodes;
import in.glg.rummy.utils.RummyPrefManager;
import in.glg.rummy.utils.RummyConstants;
import in.glg.rummy.utils.RummyTLog;
import in.glg.rummy.utils.RummyUtils;

import static in.glg.rummy.utils.RummyConstants.ACCESS_TOKEN_REST;

public class RummyLobbyFragment extends RummyBaseFragment implements OnItemClickListener, OnClickListener, CompoundButton.OnCheckedChangeListener {
    public Activity mActivity;
    private static final String LOBBY_POSITION = "LOBBY_POSITION";
    private static final String TAG = RummyLobbyFragment.class.getSimpleName();
    private RummyVariantsAdapter betAdapter;
    //FirebaseCrashlytics crashlytics;
    private RummyOnResponseListener chipLoadListner = new RummyOnResponseListener(RummyLoginResponse.class) {
        public void onResponse(Object response) {
            if (response != null) {
                RummyLoginResponse loginResponse = (RummyLoginResponse) response;
                String message = "";
                int code = Integer.parseInt(loginResponse.getCode());
                if (code == RummyErrorCodes.SUCCESS) {
                    (RummyApplication.getInstance()).getUserData().setFunChips(loginResponse.getFunChips());
                    message = String.format("%s %s %s", new Object[]{RummyLobbyFragment.this.mActivity.getString(R.string.chips_reload_success_msg), loginResponse.getFunChips(), RummyLobbyFragment.this.mActivity.getString(R.string.lobby_chips_title).toLowerCase()});
                } else if (code == RummyErrorCodes.PLAYER_HAS_CHIPS_MORE_THAN_MINIMUN) {
                    message = String.format("%s %s %s", new Object[]{RummyLobbyFragment.this.mActivity.getString(R.string.balance_reload_err_msg), loginResponse.getMinToReload(), RummyLobbyFragment.this.mActivity.getString(R.string.lobby_chips_title).toLowerCase()});
                }
                RummyLobbyFragment.this.showGenericDialog(RummyLobbyFragment.this.mActivity, message);
            }
        }
    };
    private RummyVariantsAdapter chipsAdapter;
    private RummyVariantsAdapter gameTypeAdapter;
    private String gameVariant;
    private String gamePlayer;
    private String poolType;
    private RummyOnResponseListener favGameListner=new RummyOnResponseListener(RummyLoginResponse.class) {
        @Override
        public void onResponse(Object o) {

        }
    };
    private RummyOnResponseListener joinTableListner = new RummyOnResponseListener(RummyJoinTableResponse.class) {
        public void onResponse(Object response) {
            if (response != null) {
                RummyJoinTableResponse joinTableResponse = (RummyJoinTableResponse) response;
                int code = Integer.parseInt(joinTableResponse.getCode());

                if (code == RummyErrorCodes.PLAYER_ALREADY_INPLAY || code == RummyErrorCodes.SUCCESS) {
                    RummyLobbyFragment.this.mRummyApplication.clearEventList();
                    if (!RummyLobbyFragment.this.isFoundTable(RummyLobbyFragment.this.mSelectedTable)) {
                        RummyJoinedTable joinedTable = new RummyJoinedTable();
                        joinedTable.setTabelId(joinTableResponse.getTableId());
                        RummyLobbyFragment.this.mRummyApplication.setJoinedTableIds(joinedTable);
                    }
                    RummyLobbyFragment.this.launchTableActivity();
                } else if (code == RummyErrorCodes.LOW_BALANCE) {
                    callRefundApi();
                    if (RummyLobbyFragment.this.mSelectedTable.getTable_cost().contains("CASH_CASH")) {
                        String msg = ""+getResources().getString(R.string.rummy_low_balance_first)+" "+getResources().getString(R.string.rupee_symbol)+getRestAmounttoAdd(RummyLobbyFragment.this.mSelectedTable.getBet()+"")+" "+getResources().getString(R.string.rummy_low_balance_second);
                        showErrorBalanceBuyChips(RummyLobbyFragment.this.mActivity, msg,RummyLobbyFragment.this.mSelectedTable.getBet());
                        //LobbyFragment.this.showErrorChipsDialog(LobbyFragment.this.getContext(), String.format("%s - %s", new Object[]{"You don't have enough balance to join this rummy_table , please deposit from website", Utils.getWebSite()}));
                    } else {
                        RummyLobbyFragment.this.showLowBalanceDialog(RummyLobbyFragment.this.mActivity, RummyLobbyFragment.this.mActivity.getResources().getString(R.string.low_balance_free_chip));
                    }
                } else if (code == RummyErrorCodes.TABLE_FULL) {
                    callRefundApi();
                    RummyLobbyFragment.this.showGenericDialog(RummyLobbyFragment.this.mActivity, "This rummy_table is full");
                } else if (code == 483) {
                    callRefundApi();
                    showGenericDialog(RummyLobbyFragment.this.mActivity.getResources().getString(R.string.state_block_message));
                }else
                {
                    callRefundApi();
                }
            }
            RummyLobbyFragment.this.dismissLoadingDialog();
        }
    };
    private RummyLobbyAdapter lobbyAdapter;

    private RummyOnResponseListener searchTableResponse = new RummyOnResponseListener(RummyJoinTableResponse.class) {
        public void onResponse(Object response) {
            RummyLobbyFragment.this.dismissDialog();
            if (response != null) {
                RummyJoinTableResponse joinTableResponse = (RummyJoinTableResponse) response;
                int code = Integer.parseInt(joinTableResponse.getCode());
                if (code == RummyErrorCodes.PLAYER_ALREADY_INPLAY || code == RummyErrorCodes.SUCCESS) {
                    RummyLobbyFragment.this.mRummyApplication.clearEventList();
                    if (!RummyLobbyFragment.this.isFoundTable(RummyLobbyFragment.this.mSelectedTable)) {
                        RummyJoinedTable joinedTable = new RummyJoinedTable();
                        joinedTable.setTabelId(joinTableResponse.getTableId());
                        RummyLobbyFragment.this.mRummyApplication.setJoinedTableIds(joinedTable);
                    }
                    RummyLobbyFragment.this.launchTableActivity();

                } else if (code == RummyErrorCodes.LOW_BALANCE) {
                    RummyLobbyFragment.this.showGenericDialog(RummyLobbyFragment.this.mActivity, RummyLobbyFragment.this.mActivity.getResources().getString(R.string.low_balance_free_chip));
                } else if (code == RummyErrorCodes.TABLE_FULL) {
                    RummyLobbyFragment.this.showGenericDialog(RummyLobbyFragment.this.mActivity, "This rummy_table is full");
                }
            }

            RummyLobbyFragment.this.dismissLoadingDialog();
        }
    };


    private RummyOnResponseListener lobbyDataListener = new RummyOnResponseListener(RummyLobbyTablesResponse.class) {
        public void onResponse(Object response) {
            if (response != null) {



                /*List<Table> mylist=((LobbyTablesResponse) response).getTables();
                for(int i=0;i<mylist.size();i++)
                {
                    String value=mylist.get(i).getFavorite();
                    if(value.equalsIgnoreCase("true"))
                        Log.e("gopal","value true");
                }*/

                RummyLobbyFragment.this.tables = ((RummyLobbyTablesResponse) response).getTables();

                if(RummyLobbyFragment.this.tables != null && RummyLobbyFragment.this.tables.size() > 0)
                {
                    for(int i = 0; i< RummyLobbyFragment.this.tables.size(); i++)
                    {
                        RummyTable t = RummyLobbyFragment.this.tables.get(i);
                        if(t.getTable_type().toLowerCase().contains("pr") && t.getMaxplayer().equalsIgnoreCase("6"))
                        {
                            Log.e("alltables","tableid="+t.getTable_id()+" bet="+t.getBet() +" maxplayer="+t.getMaxplayer()+" gametype= "+t.getTable_type()+" gamepaytype= "+t.getTable_cost());
                        }

                    }
                }

                RummyLobbyFragment.this.sortTablesList();
                RummyLobbyFragment.this.progressBar.setVisibility(View.GONE);
                RummyLobbyFragment.this.tablesList.setVisibility(View.VISIBLE);
                RummyLobbyFragment.this.setListnersToViews();
            }
        }
    };

    private boolean showFavourites=false;

    private TextView mBet;
    private RelativeLayout mBetLayout;
    private ImageView mBetTypeIv;
    private TextView mChips;
    private RelativeLayout mChipsLayout;
    private ImageView mChipsTypeDropDownIv;
    //private ImageView lobby_back_button;
    private TextView mFunChips;
    private TextView mFunInPlay;
    private Button mBuyChipsBtn;
    private ImageView mChipRelodBtn;
    private TextView mRealChips;
    private TextView mRealInPlay;
    private TextView mUser;


    private TextView mGameType;
    private ImageView mGameTypeDropDownIv;
    private RelativeLayout mGameTypeLayout;
    private TextView mNoOfPlayers;
    private TextView mNoOfTables;
    private TextView mPlayerType;
    private ImageView mPlayerTypeIv;
    private RelativeLayout mPlayerTypeLayout;
    private RummyTable mSelectedTable;
    private List<RummyTable> mSortedList;
    private RummyApplication mRummyApplication;
    private TextView mVariant;
    private RelativeLayout mVariantsLayout;
    private ImageView mVarieantsDropDown;
    private RummyVariantsAdapter playersAdapter;
    private float poolLow;
    private float poolMedium;
    private float prLow;
    private float prMedium;
    private ProgressBar progressBar;
    private List<RummyTable> tables;
    private ListView tablesList;
    private RummyLoginResponse userData;
    private boolean variantSelected = false;
    private RummyVariantsAdapter variantsAdapter = null;


    //filter items

    private LinearLayout filter_layout, empty_layout;
    private LinearLayout game_type_pools, game_type_deals, game_type_points;
    private ImageView closeFilter;
    private CheckBox free, cash;
    private CheckBox select_variant;
    private CheckBox jokerType, noJokerType;
    private CheckBox pools, deals, points;
    private CheckBox pool101, pool201, bestOf3;
    private CheckBox bestOf2, bestOf6;
    private CheckBox lowBet, mediumBet, highBet;
    private CheckBox players2, players6;
    private Drawable unchecked_dr;
    private Drawable checked_dr;
    private static int paddingPixel;
    private static int CHIPS_FILTER = 0;
    private static int VARIANTS_FILTER = 1;
    private static int GAME_TYPE_FILTER = 2;
    private static int BET_FILTER = 3;
    private static int PLAYERS_FILTER = 4;
    private static boolean isAutoToggle = false;

    private void filterTableList(int position, PopupWindow popupWindow, int id) {
        popupWindow.dismiss();
        handleMultiSelection(position, id);
        sortTablesList();
    }

    private void showGenericDialog(String message) {
        final Dialog dialog = new Dialog(this.mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rummy_dialog_generic);
        dialog.setCanceledOnTouchOutside(false);

        Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
        TextView message_tv = (TextView) dialog.findViewById(R.id.dialog_msg_tv);
        message_tv.setText(message);

        ok_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private List<RummyTable> getBetTableList(List<RummyTable> tableList, String compareStr, List<RummyTable> betTypeList) {


        for (int i = tableList.size() - 1; i >= 0; i--) {
            RummyTable table = (RummyTable) tableList.get(i);
            float bet = Float.parseFloat(table.getBet());
            if (table.getTable_type().contains("_POOL") || table.getTable_type().contains("BEST")) {
                if (compareStr.toLowerCase().equalsIgnoreCase("low") /*&& Float.compare(bet, this.poolLow) <= 0 */ && bet <= this.poolLow) {
                    betTypeList.add(table);
                }
                if (compareStr.toLowerCase().contains("medium") /*&& Float.compare(bet, this.poolMedium) <= 0*/&& bet >this.poolLow  && bet <= this.poolMedium) {
                    betTypeList.add(table);
                }
                if (compareStr.toLowerCase().contains("high") /*&& Float.compare(bet, this.poolMedium) >= 0*/ && bet > this.poolMedium) {
                    betTypeList.add(table);
                }
            } else if (table.getTable_type().contains(RummyUtils.PR)) {
                if (compareStr.toLowerCase().contains("low") /*&& Float.compare(bet, this.prLow) <= 0*/ && bet <= this.prLow) {
                    betTypeList.add(table);
                }
                if (compareStr.toLowerCase().contains("medium") /*&& Float.compare(bet, this.prMedium) > 0*/ && bet >this.prLow && bet <= this.prMedium) {
                    betTypeList.add(table);
                }
                if (compareStr.toLowerCase().contains("high") /*&& Float.compare(bet, this.prMedium) >= 0*/ && bet > this.prMedium) {
                    betTypeList.add(table);
                }
            }
        }
        return betTypeList;
    }

    private List<RummyTable> getChipsTypeList(List<RummyTable> tableList, String compareStr, List<RummyTable> chipTypeList) {
        for (int i = tableList.size() - 1; i >= 0; i--) {
            RummyTable table = (RummyTable) tableList.get(i);
            if (table.getTable_cost().contains(compareStr)) {
                chipTypeList.add(table);
            }
        }
        return chipTypeList;
    }

    private List<RummyTable> getGameTypeList(List<RummyTable> tableList, String compareStr, List<RummyTable> gameTypeFilerList) {
        if (tableList != null) {
            for (int i = tableList.size() - 1; i >= 0; i--) {
                RummyTable table = (RummyTable) tableList.get(i);
                if (compareStr.equalsIgnoreCase("deals")) {
                    if (table.getTable_type().equalsIgnoreCase("BEST_OF_2") || table.getTable_type().equalsIgnoreCase("BEST_OF_6")
                            || table.getTable_type().equalsIgnoreCase("BEST_OF_3")) {
                        gameTypeFilerList.add(table);
                    }
                } else if (compareStr.equalsIgnoreCase("POOLS")) {
                    if (table.getTable_type().contains("POOL")) {
                        gameTypeFilerList.add(table);
                    }
                } else if (table.getTable_type().contains(compareStr)) {
                    gameTypeFilerList.add(table);
                }
            }
        }
        return gameTypeFilerList;
    }

    private void getLobbyData() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.tablesList.setVisibility(View.INVISIBLE);
        RummyLobbyDataRequest request = new RummyLobbyDataRequest();
        request.setCommand("list_gamesettings");
        request.setUuid(RummyUtils.generateUuid());

        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.lobbyDataListener);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            Toast.makeText(this.mActivity, R.string.error_restart, Toast.LENGTH_SHORT).show();
            RummyTLog.e(TAG, "getLobbyData" + gameEngineNotRunning.getLocalizedMessage());
        }
    }

    private List<RummyTable> getPlayerTableList(List<RummyTable> tableList, String compareStr, List<RummyTable> playerTypeList) {

        for (int i = tableList.size() - 1; i >= 0; i--) {
            RummyTable table = (RummyTable) tableList.get(i);
            if (table.getMaxplayer().contains(compareStr)) {
                playerTypeList.add(table);
            }
        }
        return playerTypeList;
    }

    private List<RummyTable> getSortedList() {
        return (this.mSortedList == null || this.mSortedList.size() <= 0) ? this.tables : this.mSortedList;
    }

    private void handleMultiSelection(int position, int id) {
        Log.w(TAG, "Position: " + position);
        if (id == R.id.variants_layout || id == R.id.variants_drop_down_iv || id == VARIANTS_FILTER) {
            this.variantsAdapter.toggleChecked(position);
            this.variantSelected = true;
        } else if (id == R.id.game_type_layout || id == R.id.game_type_drop_down_iv || id == GAME_TYPE_FILTER) {
            this.variantSelected = false;
            this.gameTypeAdapter.toggleChecked(position);
        } else if (id == R.id.chips_layout || id == R.id.chips_type_drop_down_iv || id == CHIPS_FILTER) {
            this.variantSelected = false;
            this.chipsAdapter.toggleChipsItem(position);
            if (position == 0)
                RummyPrefManager.saveInt(this.mActivity.getApplicationContext(), "tableCost", 0);
            else if (position == 1)
                RummyPrefManager.saveInt(this.mActivity.getApplicationContext(), "tableCost", 1);
        } else if (id == R.id.bet_layout || id == R.id.bet_type_drop_down_iv || id == BET_FILTER) {
            this.variantSelected = false;
            this.betAdapter.toggleChecked(position);
        } else if (id == R.id.player_type_layout || id == R.id.player_type_drop_down_iv || id == PLAYERS_FILTER) {
            this.variantSelected = false;
            this.playersAdapter.toggleChecked(position);
        }
    }

    private void init() {

    //    crashlytics=FirebaseCrashlytics.getInstance();
        // filter inits
        unchecked_dr = this.mActivity.getApplicationContext().getResources().getDrawable(R.drawable.rummy_curve_edges_black);
        checked_dr = this.mActivity.getApplicationContext().getResources().getDrawable(R.drawable.rummy_curve_edges_filled_green);
        int paddingDp = 8;
        paddingPixel = RummyUtils.convertDpToPixel(paddingDp);
        // ENDS HERE
        Bundle bundle = getArguments();
        String favr="";
        if (bundle != null) {
            this.gameVariant = bundle.getString("game_variant");
            favr = bundle.getString("fav_variant");
            this.poolType = bundle.getString("pool_type");
            this.gamePlayer = bundle.getString("player_type");
        }
        if(favr!=null && favr.equalsIgnoreCase("1"))
            showFavourites=true;
        else
            showFavourites=false;

        this.mRummyApplication = RummyApplication.getInstance();
        RummyApplication app = RummyApplication.getInstance();
        if (app != null) {
            this.userData = app.getUserData();
        }
        if (this.userData != null) {
            this.poolLow = Float.valueOf(this.userData.getPoolLowBet()).floatValue();
            this.poolMedium = Float.valueOf(this.userData.getPoolMediumBet()).floatValue();
            this.prLow = Float.valueOf(this.userData.getPrLowBet()).floatValue();
            this.prMedium = Float.valueOf(this.userData.getPrMediumBet()).floatValue();

            Log.e("vikas","poolow= "+this.poolLow);
            Log.e("vikas","poomedium ="+this.poolMedium);
            Log.e("vikas","pr low ="+this.prLow);
            Log.e("vikas","pr medium ="+this.prMedium);

        }

        int[] players = mActivity.getResources().getIntArray(R.array.players_items);
        String[] playersArray = new String[players.length];
        for (int i = 0; i < players.length; i++) {
            playersArray[i] = String.valueOf(players[i]);
        }

        String[] gameTypeArray = new String[]{"101", "201"};

        this.gameTypeAdapter = new RummyVariantsAdapter(this.mActivity, gameTypeArray);
        this.variantsAdapter = new RummyVariantsAdapter(this.mActivity, mActivity.getResources().getStringArray(R.array.variant_items));
        this.chipsAdapter = new RummyVariantsAdapter(this.mActivity, mActivity.getResources().getStringArray(R.array.chips_items));
        this.betAdapter = new RummyVariantsAdapter(this.mActivity, mActivity.getResources().getStringArray(R.array.bet_items));
        this.playersAdapter = new RummyVariantsAdapter(this.mActivity, playersArray);

        if (this.gameVariant != null && this.gameVariant.length() > 0) {
            Log.e("gameVariant", gameVariant + "");
            if (this.gameVariant.equalsIgnoreCase("strikes")) {
                this.variantsAdapter.toggleChecked(2);
                this.gameTypeAdapter = new RummyVariantsAdapter(this.mActivity, getResources().getStringArray(R.array.pr_game_types));
            } else if (this.gameVariant.equalsIgnoreCase("pools")) {
                this.variantsAdapter.toggleChecked(0);
                this.gameTypeAdapter = new RummyVariantsAdapter(this.mActivity, gameTypeArray);
            } else if (this.gameVariant.equalsIgnoreCase("deals")) {
                this.variantsAdapter.toggleChecked(1);
                this.gameTypeAdapter = new RummyVariantsAdapter(this.mActivity, getResources().getStringArray(R.array.deals_game_types));
            }
        }
        if (this.gamePlayer != null && this.gamePlayer.length() > 0) {
            if (this.gamePlayer.equalsIgnoreCase("2")) {
                this.playersAdapter.toggleChecked(1);
            } else if (this.gamePlayer.equalsIgnoreCase("6")) {
                this.playersAdapter.toggleChecked(0);
            }
        }
        if (this.poolType != null && this.poolType.length() > 0) {
            if (this.poolType.equalsIgnoreCase("101")) {
                this.gameTypeAdapter.toggleChecked(1);
            } else if (this.poolType.equalsIgnoreCase("201")) {
                this.gameTypeAdapter.toggleChecked(0);
            }
        }
        if (this.chipsAdapter != null) {
            this.chipsAdapter.toggleChipsItem(RummyPrefManager.getInt(this.mActivity.getApplicationContext(), "tableCost", 0));
        }
    }

    private void setUserNameAndChipsDetails(RummyLoginResponse userData) {
        if (userData != null) {
            String rupeeSymbol = this.mActivity.getString(R.string.rupee_text);
            this.mUser.setText(userData.getNickName());
            String funChips = userData.getFunChips();
            if (funChips == null || funChips.length() <= 0) {
                funChips = "0";
            }
            String realChips = userData.getRealChips();
            if (realChips == null || realChips.length() <= 0) {
                realChips = String.format("%s %s", new Object[]{rupeeSymbol, "0"});
            } else {
                realChips = String.format("%s %s", new Object[]{rupeeSymbol, realChips});
            }
            Spannable realChipsStr = new SpannableString(realChips);
            //realChipsStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this.mActivity, R.color.rummy_white)), 0, 1, 33);
            String funInPlay = userData.getFunInPlay();
            if (funInPlay == null || funInPlay.length() <= 0) {
                funInPlay = "0";
            }
            String realInPlay = userData.getRealInPlay();
            if (realInPlay == null || realInPlay.length() <= 0) {
                realInPlay = String.format("%s %s", new Object[]{rupeeSymbol, "0"});
            } else {
                realInPlay = String.format("%s %s", new Object[]{rupeeSymbol, realInPlay});
            }
            Spannable realInPlayStr = new SpannableString(realInPlay);
            realInPlayStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this.mActivity, R.color.rummy_app_blue)), 0, 1, 33);
            this.mRealChips.setText(realChipsStr);
            this.mRealInPlay.setText(realInPlayStr);
            this.mFunChips.setText(funChips);
            this.mFunInPlay.setText(funInPlay);

          //  crashlytics.setCustomKey("real_chips", realChipsStr + "");
          //  crashlytics.setCustomKey("real_inplay", realInPlayStr + "");
          //  crashlytics.setCustomKey("fun_chips", funChips + "");
         //   crashlytics.setCustomKey("fun_inplay", funInPlay + "");

            this.mRealChips.setMovementMethod(new ScrollingMovementMethod());
            this.mRealInPlay.setMovementMethod(new ScrollingMovementMethod());
            this.mFunChips.setMovementMethod(new ScrollingMovementMethod());
            this.mFunInPlay.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    private void loadChips() {
        RummyBaseTrRequest request = new RummyBaseTrRequest();
        request.setCommand("chipreload");
        request.setUuid(RummyUtils.generateUuid());
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.chipLoadListner);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            Toast.makeText(this.mActivity, R.string.error_restart, Toast.LENGTH_SHORT).show();
            RummyTLog.d(TAG, "getTableData" + gameEngineNotRunning.getLocalizedMessage());
        }
    }

    private int measureContentWidth(RummyVariantsAdapter listAdapter) {
        ViewGroup mMeasureParent = null;
        int maxWidth = 0;
        View itemView = null;
        int itemType = 0;
        RummyVariantsAdapter adapter = listAdapter;
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }
            if (mMeasureParent == null) {
                mMeasureParent = new FrameLayout(this.mActivity);
            }
            itemView = adapter.getView(i, itemView, mMeasureParent);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            int itemWidth = itemView.getMeasuredWidth();
            if (itemWidth > maxWidth) {
                maxWidth = itemWidth;
            }
        }
        return maxWidth;
    }

    public static RummyLobbyFragment newInstance(int position) {
        RummyLobbyFragment fr = new RummyLobbyFragment();
        Bundle b = new Bundle();
        b.putInt(LOBBY_POSITION, position);
        fr.setArguments(b);
        return fr;
    }

    private void setFilterAdapters(int id, PopupWindow popupWindow, ListView listViewVariants) {
        if (id == R.id.variants_layout || id == R.id.variants_drop_down_iv) {
            listViewVariants.setAdapter(this.variantsAdapter);
        } else if (id == R.id.game_type_layout || id == R.id.game_type_drop_down_iv) {
            listViewVariants.setAdapter(this.gameTypeAdapter);
        } else if (id == R.id.chips_layout || id == R.id.chips_type_drop_down_iv) {
            listViewVariants.setAdapter(this.chipsAdapter);
        } else if (id == R.id.bet_layout || id == R.id.bet_type_drop_down_iv) {
            listViewVariants.setAdapter(this.betAdapter);
        } else if (id == R.id.player_type_layout || id == R.id.player_type_drop_down_iv) {
            listViewVariants.setAdapter(this.playersAdapter);
        }
    }

    private void setFilterBatData(List<String> chekedItemsList, TextView textView, int type) {
        try {
            if (chekedItemsList.size() == 0) {
                textView.setText("Select");
            } else if (chekedItemsList.size() == 1) {
                String message = "";
                if (type == RummyUtils.VARIANT) {
//                    rummy_message = String.format("%s%s%s", new Object[]{chekedItemsList.get(0), " ", this.mContext.getString(R.string.lobby_rummy_title_txt)});
                    message = String.format("%s%s%s", new Object[]{chekedItemsList.get(0), " ", ""});
                } else if (type == RummyUtils.GAME_TYPE) {
                    String str = "%s%s%s";
                    Object[] objArr = new Object[3];
                    objArr[0] = chekedItemsList.get(0);
                    objArr[1] = " ";
                    objArr[2] = !((String) chekedItemsList.get(0)).contains("0") ? "" : this.mActivity.getString(R.string.lobby_pool_title_txt);
                    message = String.format(str, objArr);
                } else if (type == RummyUtils.CHIPS) {
                    message = (String) chekedItemsList.get(0);
                } else if (type == RummyUtils.BET) {
                    message = (String) chekedItemsList.get(0);
                } else if (type == RummyUtils.PLAYERS) {
                    message = (String) chekedItemsList.get(0);
                }
                textView.setText(message);
            } else if (chekedItemsList.size() > 1) {
                textView.setText("Multiple");
            }
            if (type == RummyUtils.VARIANT) {
                if (chekedItemsList.size() == this.variantsAdapter.getCount()) {
                    textView.setText("Any");
                }
            } else if (type == RummyUtils.GAME_TYPE) {
                if (chekedItemsList.size() == this.gameTypeAdapter.getCount()) {
                    textView.setText("Any");
                }
            } else if (type == RummyUtils.CHIPS) {
                if (chekedItemsList.size() == this.chipsAdapter.getCount()) {
                    //bonus_code_tv.setText("Any");
                    textView.setText("Free");
                }
            } else if (type == RummyUtils.BET) {
                if (chekedItemsList.size() == this.betAdapter.getCount()) {
                    textView.setText("Any");
                }
            } else if (type == RummyUtils.PLAYERS && chekedItemsList.size() == this.playersAdapter.getCount()) {
                textView.setText("Any");
            }
        } catch (Exception e) {
            RummyTLog.e(TAG, "Exception :: " + e.getMessage());
        }
    }

    private void setFilterSelectionUI(List<String> variantsList, List<String> gameTypeList, List<String> chipsList, List<String> betList, List<String> playerList) {
        setFilterBatData(variantsList, this.mVariant, RummyUtils.VARIANT);
        setFilterBatData(gameTypeList, this.mGameType, RummyUtils.GAME_TYPE);
        setFilterBatData(chipsList, this.mChips, RummyUtils.CHIPS);
        setFilterBatData(betList, this.mBet, RummyUtils.BET);
        setFilterBatData(playerList, this.mPlayerType, RummyUtils.PLAYERS);
    }

    private void setIdsToViews(View v) {
        this.mVarieantsDropDown = (ImageView) v.findViewById(R.id.variants_drop_down_iv);
        this.mGameTypeDropDownIv = (ImageView) v.findViewById(R.id.game_type_drop_down_iv);
        this.mChipsTypeDropDownIv = (ImageView) v.findViewById(R.id.chips_type_drop_down_iv);
        //this.lobby_back_button = (ImageView) v.findViewById(R.id.lobby_back_button);
        this.mChipRelodBtn = (ImageView) v.findViewById(R.id.reload_chips_btn);
        this.mUser = (TextView) v.findViewById(R.id.user_name_tv);
        this.mRealChips = (TextView) v.findViewById(R.id.user_real_chips_value_tv);
        this.mRealInPlay = (TextView) v.findViewById(R.id.inplay_value_tv);
        this.mFunChips = (TextView) v.findViewById(R.id.user_fun_chips_tv);
        this.mFunInPlay = (TextView) v.findViewById(R.id.inplay_fun_tv);
        this.mBuyChipsBtn = (Button) v.findViewById(R.id.buyChipsBtn);

        this.mBetTypeIv = (ImageView) v.findViewById(R.id.bet_type_drop_down_iv);
        this.mPlayerTypeIv = (ImageView) v.findViewById(R.id.player_type_drop_down_iv);
        this.progressBar = (ProgressBar) v.findViewById(R.id.lobby_progress);
        this.tablesList = (ListView) v.findViewById(R.id.lobbylist);
        this.mVariantsLayout = (RelativeLayout) v.findViewById(R.id.variants_layout);
        this.mGameTypeLayout = (RelativeLayout) v.findViewById(R.id.game_type_layout);
        this.mChipsLayout = (RelativeLayout) v.findViewById(R.id.chips_layout);
        this.mBetLayout = (RelativeLayout) v.findViewById(R.id.bet_layout);
        this.mPlayerTypeLayout = (RelativeLayout) v.findViewById(R.id.player_type_layout);
        this.mVariant = (TextView) v.findViewById(R.id.variant_value_tv);
        this.mGameType = (TextView) v.findViewById(R.id.game_type_value_tv);
        this.mChips = (TextView) v.findViewById(R.id.chip_type_value_tv);
        this.mBet = (TextView) v.findViewById(R.id.bet_type_value_tv);
        this.mPlayerType = (TextView) v.findViewById(R.id.player_type_value_tv);
        this.mNoOfPlayers = (TextView) v.findViewById(R.id.no_of_players_tv);
        this.mNoOfTables = (TextView) v.findViewById(R.id.no_of_tables_tv);
    }

    private void setIDsToFilterViews(View v) {
        // filter items

        this.closeFilter = (ImageView) v.findViewById(R.id.closeFilter);
        this.filter_layout = (LinearLayout) v.findViewById(R.id.filter_layout);
        this.game_type_deals = (LinearLayout) v.findViewById(R.id.game_type_deals);
        this.game_type_pools = (LinearLayout) v.findViewById(R.id.game_type_pools);
        this.game_type_points = (LinearLayout) v.findViewById(R.id.game_type_points);
        this.empty_layout = (LinearLayout) v.findViewById(R.id.empty_layout);
        this.free = (CheckBox) v.findViewById(R.id.free);
        this.cash = (CheckBox) v.findViewById(R.id.cash);

        this.pools = (CheckBox) v.findViewById(R.id.pools);
        this.deals = (CheckBox) v.findViewById(R.id.deals);
        this.points = (CheckBox) v.findViewById(R.id.points);
        this.pool101 = (CheckBox) v.findViewById(R.id.pool101);
        this.pool201 = (CheckBox) v.findViewById(R.id.pool201);
        this.bestOf3 = (CheckBox) v.findViewById(R.id.bestOf3);
        this.bestOf2 = (CheckBox) v.findViewById(R.id.bestOf2);
        this.bestOf6 = (CheckBox) v.findViewById(R.id.bestOf6);
        this.lowBet = (CheckBox) v.findViewById(R.id.lowBet);
        this.mediumBet = (CheckBox) v.findViewById(R.id.mediumBet);
        this.highBet = (CheckBox) v.findViewById(R.id.highBet);
        this.players2 = (CheckBox) v.findViewById(R.id.players2);
        this.players6 = (CheckBox) v.findViewById(R.id.players6);
        this.jokerType = (CheckBox) v.findViewById(R.id.jokerType);
        this.noJokerType = (CheckBox) v.findViewById(R.id.noJokerType);
        this.select_variant = (CheckBox) v.findViewById(R.id.select_variant);
    }

    private void setListnersToViews() {
        this.mVarieantsDropDown.setOnClickListener(this);
        this.mGameTypeDropDownIv.setOnClickListener(this);
        this.mChipsTypeDropDownIv.setOnClickListener(this);
        this.mBetTypeIv.setOnClickListener(this);
        this.mPlayerTypeIv.setOnClickListener(this);
        this.mVariantsLayout.setOnClickListener(this);
        this.mGameTypeLayout.setOnClickListener(this);
        this.mChipsLayout.setOnClickListener(this);
        this.mBetLayout.setOnClickListener(this);
        this.mPlayerTypeLayout.setOnClickListener(this);

        //this.lobby_back_button.setOnClickListener(this);
        this.mBuyChipsBtn.setOnClickListener(this);
        this.mChipRelodBtn.setOnClickListener(this);
    }

    private void setListenersForFilters() {
        free.setOnCheckedChangeListener(this);
        cash.setOnCheckedChangeListener(this);
        pools.setOnCheckedChangeListener(this);
        deals.setOnCheckedChangeListener(this);
        points.setOnCheckedChangeListener(this);
        pool101.setOnCheckedChangeListener(this);
        pool201.setOnCheckedChangeListener(this);
        bestOf3.setOnCheckedChangeListener(this);
        bestOf6.setOnCheckedChangeListener(this);
        bestOf2.setOnCheckedChangeListener(this);
        lowBet.setOnCheckedChangeListener(this);
        mediumBet.setOnCheckedChangeListener(this);
        highBet.setOnCheckedChangeListener(this);
        players2.setOnCheckedChangeListener(this);
        players6.setOnCheckedChangeListener(this);
        jokerType.setOnCheckedChangeListener(this);
        noJokerType.setOnCheckedChangeListener(this);

        closeFilter.setOnClickListener(this);
        empty_layout.setOnClickListener(this);
    }

    private void setSortedList(List<RummyTable> list) {
        this.mSortedList = list;
    }

    private void showGameTypeDropDown(View v) {
        if (this.variantsAdapter != null && this.variantsAdapter.getCheckedItems().size() > 0) {
            popUpWindowVariants(v.getId()).showAsDropDown(this.mGameTypeLayout);
        }
    }

    private void sortTablesList() {
        List<String> variantsList = new ArrayList<>();
        List<String> gameTypeList = new ArrayList<>();
        List<String> chipsList = new ArrayList<>();
        List<String> betList = new ArrayList<>();
        List<String> playerList = new ArrayList<>();
        String gameType;
        List<String> temp = new ArrayList();
        if (this.variantsAdapter != null) {
            variantsList = this.variantsAdapter.getCheckedItems();
        } else {
            variantsList = temp;
        }
        if (this.gameTypeAdapter != null) {
            gameTypeList = this.gameTypeAdapter.getCheckedItems();
        } else {
            gameTypeList = temp;
        }
        if (this.chipsAdapter != null) {
            chipsList = this.chipsAdapter.getCheckedItems();
        } else {
            chipsList = temp;
        }
        if (this.betAdapter != null) {
            betList = this.betAdapter.getCheckedItems();
        } else {
            betList = temp;
        }
        if (this.playersAdapter != null) {
            playerList = this.playersAdapter.getCheckedItems();
        } else {
            playerList = temp;
        }

        setFilterSelectionUI(variantsList, gameTypeList, chipsList, betList, playerList);
        List<RummyTable> sortedList = new ArrayList();
        List<RummyTable> variantTypeFilterList = new ArrayList();
        List<String> gameTyeList = new ArrayList();
        if (variantsList.size() > 0) {
            String variant;

            for (String variant2 : variantsList) {
                if (variant2.equalsIgnoreCase(this.mActivity.getString(R.string.points_txt))) {
                    variant2 = RummyUtils.PR_JOKER;
                    gameTyeList.add(RummyUtils.GAME_TYPE_PR_JOKER);
                    //gameTyeList.add(Utils.GAME_TYPE_PR_NO_JOKER);
                }
                if (variant2.equalsIgnoreCase(this.mActivity.getString(R.string.pools_txt))) {
                    variant2 = "POOLS";
                    gameTyeList.add(RummyUtils.GAME_TYPE_101);
                    gameTyeList.add(RummyUtils.GAME_TYPE_201);
                }
                if (variant2.equalsIgnoreCase(this.mActivity.getString(R.string.deals_txt))) {
                    variant2 = "deals";
                    gameTyeList.add(RummyUtils.GAME_TYPE_BEST_OF_2);
                    gameTyeList.add(RummyUtils.GAME_TYPE_BEST_OF_6);
                    gameTyeList.add(RummyUtils.GAME_TYPE_BEST_OF_3);
                }
                sortedList = getGameTypeList(this.tables, variant2, variantTypeFilterList);
            }

            if (this.variantSelected) {
                int i;
                String[] gameTypeArray = (String[]) gameTyeList.toArray(new String[gameTyeList.size()]);
                List<String> unCheckedItems = new ArrayList();
                if (this.gameTypeAdapter != null) {
                    unCheckedItems = this.gameTypeAdapter.getUnCheckedItems();
                }
                this.gameTypeAdapter = new RummyVariantsAdapter(this.mActivity, gameTypeArray);
                for (String variant22 : variantsList) {
                    if (variant22.equalsIgnoreCase("deals")) {
                        for (i = 0; i < gameTypeArray.length; i++) {
                            gameType = gameTypeArray[i];
                            if (gameType.contains(RummyUtils.GAME_TYPE_BEST_OF_2) || gameType.contains("6") || gameType.contains(RummyUtils.GAME_TYPE_BEST_OF_3)) {
                                this.gameTypeAdapter.toggleChecked(i);
                            }
                        }
                    }
                    if (variant22.equalsIgnoreCase("points")) {
                        for (i = 0; i < gameTypeArray.length; i++) {
                            if (gameTypeArray[i].contains(RummyUtils.GAME_TYPE_PR_JOKER)) {
                                this.gameTypeAdapter.toggleChecked(i);
                            }
                        }
                    }
                    if (variant22.equalsIgnoreCase("pools")) {
                        for (i = 0; i < gameTypeArray.length; i++) {
                            gameType = gameTypeArray[i];
                            if (gameType.contains(RummyUtils.GAME_TYPE_101) || gameType.contains(RummyUtils.GAME_TYPE_201)) {
                                this.gameTypeAdapter.toggleChecked(i);
                            }
                        }
                    }
                }
                if (unCheckedItems.size() > 0) {
                    for (i = 0; i < gameTypeArray.length; i++) {
                        for (String chekedItem : unCheckedItems) {
                            if (gameTypeArray[i].equalsIgnoreCase(chekedItem)) {
                                this.gameTypeAdapter.setUnChecked(i);
                            }
                        }
                    }
                }
                if (this.gameTypeAdapter.getCheckedItems().size() == gameTypeArray.length) {
                    this.mGameType.setText("Any");
                } else if (this.gameTypeAdapter.getCheckedItems().size() == 1) {
                    this.mGameType.setText((CharSequence) this.gameTypeAdapter.getCheckedItems().get(0));
                } else {
                    this.mGameType.setText("Multiple");
                }
                if (this.gameTypeAdapter != null) {
                    gameTypeList = this.gameTypeAdapter.getCheckedItems();
                } else {
                    gameTypeList = temp;
                }
                this.variantSelected = false;
            }
        } else {
            gameTypeList.clear();
            this.mGameType.setText("Select");
            this.gameTypeAdapter = null;
            sortedList = this.tables;
        }

        List<RummyTable> gameTypeFilterList = new ArrayList();
        try {
            if (gameTypeList.size() > 0 && sortedList.size() > 0) {
                for (String gameType2 : gameTypeList) {
                    if (gameType2.equalsIgnoreCase(RummyUtils.GAME_TYPE_BEST_OF_3)) {
                        gameType2 = "BEST_OF_3";
                    }
                    if (gameType2.equalsIgnoreCase(RummyUtils.GAME_TYPE_BEST_OF_2)) {
                        gameType2 = "BEST_OF_2";
                    }
                    if (gameType2.equalsIgnoreCase(RummyUtils.GAME_TYPE_BEST_OF_6)) {
                        gameType2 = "BEST_OF_6";
                    }
                    if (gameType2.equalsIgnoreCase(RummyUtils.GAME_TYPE_PR_JOKER)) {
                        gameType2 = "JOKER";
                    }
                   /* if (gameType2.equalsIgnoreCase(Utils.GAME_TYPE_PR_NO_JOKER)) {
                        gameType2 = "NO_JOKER";
                    }*/
                    if (variantTypeFilterList.size() > 0) {
                        sortedList = variantTypeFilterList;
                    }
                    sortedList = getGameTypeList(sortedList, gameType2, gameTypeFilterList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<RummyTable> chipsTypeFilterList = new ArrayList();
        if (chipsList.size() > 0 && sortedList.size() > 0) {
            for (String chipType : chipsList) {
                String chipType2 = chipType;
                if (chipType2.equalsIgnoreCase("Free")) {
                    chipType2 = "FUNCHIPS_FUNCHIPS";
                } else {
                    chipType2 = "CASH_CASH";
                }
                if (gameTypeFilterList.size() > 0) {
                    sortedList = gameTypeFilterList;
                }
                sortedList = getChipsTypeList(sortedList, chipType2, chipsTypeFilterList);
            }
        }

        List<RummyTable> betFilterList = new ArrayList();
        if (betList.size() > 0 && sortedList.size() > 0) {
            Log.e("vikas","calling bet list and sorted list greater than 0");
            for (String betType : betList) {
                Log.e("vikas","battype name is = "+betType);
                if (chipsTypeFilterList.size() > 0) {
                    sortedList = chipsTypeFilterList;
                }
                sortedList = getBetTableList(sortedList, betType, betFilterList);
            }
        }


        List<RummyTable> playerFilterList = new ArrayList();
        if (playerList.size() == 1 && sortedList.size() > 0) {
            for (String playerType : playerList) {
                if (betFilterList.size() > 0) {
                    sortedList = betFilterList;
                }
                sortedList = getPlayerTableList(sortedList, playerType, playerFilterList);
            }
        }
        if (variantsList.size() == 0 && chipsList.size() == 0 && betFilterList.size() == 0 && playerFilterList.size() == 0) {
            sortedList = this.tables;
        }
        setSortedList(sortedList);
        Collections.sort(sortedList, new PlayerComparator());

        for(int j =0;j<this.mRummyApplication.getMyfaverateList().size();j++)
        {
            RummyTable favTable = this.mRummyApplication.getMyfaverateList().get(j);
            for(int i=0;i<sortedList.size();i++)
            {
                RummyTable simpleTable = sortedList.get(i);
                if(favTable.getTable_type().equalsIgnoreCase(simpleTable.getTable_type()) && favTable.getBet().equalsIgnoreCase(simpleTable.getBet()) && favTable.getMaxplayer().equalsIgnoreCase(simpleTable.getMaxplayer()))
                {
                    simpleTable.setFavorite(favTable.getFavorite());
                    break;
                }
            }
        }




        if(showFavourites)
        {
            List<RummyTable> favList=new ArrayList<>();
            for(int i=0;i<sortedList.size();i++)
            {
                if(sortedList.get(i).getFavorite().equalsIgnoreCase("true"))
                {
                    favList.add(sortedList.get(i));
                }
            }

            if(favList.size()>0)
            this.lobbyAdapter = new RummyLobbyAdapter(this.mActivity, favList, this);

        }
        else
        this.lobbyAdapter = new RummyLobbyAdapter(this.mActivity, sortedList, this);

        this.tablesList.setAdapter(this.lobbyAdapter);
        if (this.lobbyAdapter != null) {
            this.lobbyAdapter.notifyDataSetChanged();
        }

    }

    public boolean isFoundTable(RummyTable table) {
        for (RummyJoinedTable joinedTable : this.mRummyApplication.getJoinedTableIds()) {
            if (joinedTable != null) {
                if (joinedTable.getTabelId() !=null && joinedTable.getTabelId().equalsIgnoreCase(table.getTable_id())) {
                    return true;
                }
                else
                {
                    if(joinedTable.getTabelId() == null)
                    {
                        this.mRummyApplication.getJoinedTableIds().remove(joinedTable);
                    }
                }
            }

        }
        return false;
    }

    public void joinTable(RummyTable table, String buyInAmt) {
        showLoadingDialog(this.mActivity);
        this.mRummyApplication = RummyApplication.getInstance();
        List<RummyJoinedTable> joinedTableIds = this.mRummyApplication.getJoinedTableIds();
        boolean foundTable = isFoundTable(table);
        if (joinedTableIds != null && joinedTableIds.size() == 2 && !foundTable) {
            dismissLoadingDialog();
            showGenericDialog(this.mActivity, this.mActivity.getString(R.string.max_table_reached_msg));
        } else if (table != null) {


            try {
                String userId = RummyPrefManager.getString(mActivity, RummyConstants.PLAYER_USER_ID, "0");
                if (table.getTable_cost().equalsIgnoreCase("CASH_CASH")) {

                        RummyCommonEventTracker.trackGameJoinEvent(mActivity, RummyCommonEventTracker.Cash_Game_Click, userId, table.getBet(), buyInAmt, table.getTable_type(), table.getTable_cost(), table.getMaxplayer());
                } else {

                        RummyCommonEventTracker.trackGameJoinEvent(mActivity, RummyCommonEventTracker.Free_Game_Click, userId, table.getBet(), buyInAmt, table.getTable_type(), table.getTable_cost(), table.getMaxplayer());
                }
            } catch (Exception e) {

            }

            int max = 200;
            int min = 1;
            int b = (int)(Math.random()*(max-min+1)+min);

            boolean isJoinFromSearch = false;
            if(b%2 == 0)
            {
                isJoinFromSearch = true;
            }
            else
            {
                isJoinFromSearch= false;
            }


            setSelectedTable(table);


            if(isJoinFromSearch)
            {
                if(table.getMaxplayer().equalsIgnoreCase("6") && table.getTable_type().toLowerCase().contains("pr"))
                {
                    RummySearchTableRequest request = new RummySearchTableRequest();
                    request.setCommand("search_join_table");
                    request.setTableId(table.getTable_id());
                    request.setUuid(RummyUtils.generateUuid());
                    request.setBet(table.getBet());
                    request.setUserId(this.userData.getUserId());
                    request.setTableType(table.getTable_type());
                    request.setTableCost(table.getTable_cost());
                    request.setMaxPlayers(table.getMaxplayer());
                    request.setConversion(table.getConversion());
                    request.setStreamId(table.getStream_id());
                    request.setStreamName(table.getStream_name());
                    request.setUnique_gamesettings_id(table.getId());
                    request.setBuyinamount(buyInAmt);
                    request.setTableJoinAs("play");
                    request.setOrderId(RummyApplication.getInstance().getCurrentTableOrderId());
                    request.setNickName(this.userData.getNickName());
                    try {
                        RummyGameEngine.getInstance();
                        RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.searchTableResponse);
                    } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                        Toast.makeText(this.mActivity, R.string.error_restart, Toast.LENGTH_SHORT).show();
                        RummyTLog.d(TAG, "getTableData" + gameEngineNotRunning.getLocalizedMessage());
                    }
                }
                else
                {
                    RummyJoinRequest request = new RummyJoinRequest();
                    request.setCommand("join_table");
                    request.setTableId(table.getTable_id());
                    request.setUuid(RummyUtils.generateUuid());
                    request.setTableJoinAs("play");
                    request.setTableType(table.getTable_type());
                    request.setTableCost(table.getTable_cost());
                    request.setBuyinamount(buyInAmt);
                    request.setSeat(0);
                    request.setCharNo("0");
                    try {
                        RummyGameEngine.getInstance();
                        RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.joinTableListner);
                    } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                        Toast.makeText(this.mActivity, R.string.error_restart, Toast.LENGTH_SHORT).show();
                        RummyTLog.d(TAG, "getTableData" + gameEngineNotRunning.getLocalizedMessage());
                    }
                }

            }
            else
            {
                RummyJoinRequest request = new RummyJoinRequest();
                request.setCommand("join_table");
                request.setTableId(table.getTable_id());
                request.setUuid(RummyUtils.generateUuid());
                request.setTableJoinAs("play");
                request.setTableType(table.getTable_type());
                request.setTableCost(table.getTable_cost());
                request.setBuyinamount(buyInAmt);
                request.setSeat(0);
                request.setCharNo("0");
                try {
                    RummyGameEngine.getInstance();
                    RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.joinTableListner);
                } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                    Toast.makeText(this.mActivity, R.string.error_restart, Toast.LENGTH_SHORT).show();
                    RummyTLog.d(TAG, "getTableData" + gameEngineNotRunning.getLocalizedMessage());
                }

            }
        }
    }

    public void SetFavouriteData(String favVal, RummyTable table)
    {
        Log.e("gopal","set fav to engine to - "+favVal);
        if(table!=null)
        {
            RummyFavouriteRequest request=new RummyFavouriteRequest();
            request.setCommand("favorite_game");
            request.setSet(favVal);
            request.setTableId(table.getTable_id());
            request.setTableCost(table.getTable_cost());
            request.setUser_id(this.userData.getUserId());
            request.setNick_name(this.userData.getNickName());
            request.setUuid(this.userData.getMsg_uuid());

            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.favGameListner);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mActivity, R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.d(TAG, "getTableData" + gameEngineNotRunning.getLocalizedMessage());
            }
        }

    }

    public void DefaultFavourite()
    {
        showFavourites=false;
    }


    public void launchTableActivity() {
        if(isAdded())
        {
            int joinedTableIds = this.mRummyApplication.getJoinedTableIds().size();
            Intent playIntent = new Intent(this.mActivity, RummyTableActivity.class);
            playIntent.putExtra("iamBack", false);
            playIntent.putExtra("table", this.mSelectedTable);
            playIntent.putExtra("table", this.mSelectedTable);
            playIntent.putExtra("tableId", this.mSelectedTable.getTable_id());
            playIntent.putExtra("tables",(Serializable) this.tables);
            playIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(playIntent);
        }

    }

    public void onClick(View v) {
        int id = v.getId();/*case R.id.lobby_back_button:
                ((HomeActivity) this.mActivity).showFragment(R.id.home);
                return;*/
        if (id == R.id.buyChipsBtn) {
            RummyInstance.getInstance().getRummyListener().lowBalance(0);
           // checkPlayerDeposit(getContext());
            return;
        } else if (id == R.id.reload_chips_btn) {
            loadChips();
            return;
        } else if (id == R.id.chips_layout || id == R.id.chips_type_drop_down_iv) {
            popUpWindowVariants(v.getId()).showAsDropDown(this.mChipsLayout);
            return;
        } else if (id == R.id.variants_layout || id == R.id.variants_drop_down_iv) {
            popUpWindowVariants(v.getId()).showAsDropDown(this.mVariantsLayout);
            return;
        } else if (id == R.id.game_type_layout || id == R.id.game_type_drop_down_iv) {
            showGameTypeDropDown(v);
            return;
        } else if (id == R.id.bet_layout || id == R.id.bet_type_drop_down_iv) {
            popUpWindowVariants(v.getId()).showAsDropDown(this.mBetLayout);
            return;
        } else if (id == R.id.player_type_layout || id == R.id.player_type_drop_down_iv) {
            popUpWindowVariants(v.getId()).showAsDropDown(this.mPlayerTypeLayout);
            return;
        } else if (id == R.id.filter) {
            if (RummyPrefManager.getInt(this.mActivity.getApplicationContext(), "tableCost", 0) == 1)
                cash.setChecked(true);
            else
                free.setChecked(true);
            filter_layout.setVisibility(View.VISIBLE);
            toggleFilterLayoutView(true);
            return;
        } else if (id == R.id.closeFilter) {
            toggleFilterLayoutView(false);
            return;
        } else if (id == R.id.empty_layout) {
            toggleFilterLayoutView(false);
            return;
        }
        return;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.rummy_fragment_lobby, container, false);
        try {
            init();
            setIdsToViews(v);
            setIDsToFilterViews(v);
            setListenersForFilters();
            filter_layout.setVisibility(View.INVISIBLE);
            if (RummyGameEngine.getInstance().isSocketConnected()) {
                getLobbyData();
            }

            RummyLoginResponse userData = (RummyApplication.getInstance()).getUserData();
            setUserNameAndChipsDetails(userData);

            setPlayerLevel(v.findViewById(R.id.player_rating_bar), userData.getPlayerLevel());
        } catch (Exception e) {
            Log.e(TAG, e + "");
        }
        return v;
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d("flow", "onDestroy: LF");
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        RummyTable table = (RummyTable) this.tables.get(position);
        Intent playIntent = new Intent(this.mActivity, RummyTableActivity.class);
        playIntent.putExtra("rummy_table", table);
        startActivity(playIntent);
    }

    @Subscribe
    public void onMessageEvent(RummyGameEvent event) {
        if (event.name().equalsIgnoreCase("SERVER_CONNECTED") || !event.name().equalsIgnoreCase("SERVER_DISCONNECTED")) {
        }
    }

    @Subscribe
    public void onMessageEvent(RummyEvent event) {
        if (event.getEventName().equalsIgnoreCase("BALANCE_UPDATE")) {
            RummyLoginResponse userData = (RummyApplication.getInstance()).getUserData();
            userData.setFunChips(event.getFunChips());
            userData.setFunInPlay(event.getFunInPlay());
            userData.setRealChips(event.getReaChips());
            userData.setRealInPlay(event.getRealInPlay());
            userData.setLoyalityChips(event.getLoyaltyChips());
            setUserNameAndChipsDetails(userData);




        } else if (event.getEventName().equalsIgnoreCase("gamesetting_update")) {
            new RefreshAdapter().execute(new RummyEvent[]{event});
        } else if (event.getEventName().equalsIgnoreCase("HEART_BEAT")) {
            mNoOfPlayers.setText(String.format("%s %s", new Object[]{event.getTotalNoOfPlayers(), "Players"}));
            mNoOfTables.setText(String.format("%s %s", new Object[]{event.getNoOfTables(), "Tables"}));
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


        RummyCommonEventTracker.trackScreenName(RummyCommonEventTracker.LOBBY_SCREEN, this.mActivity.getApplicationContext());
    }

    public PopupWindow popUpWindowVariants(final int id) {
        final PopupWindow popupWindow = new PopupWindow(this.mActivity);
        ListView listViewVariants = new ListView(this.mActivity);
        listViewVariants.setChoiceMode(3);
        ColorDrawable sage = new ColorDrawable(mActivity.getResources().getColor(R.color.rummy_app_blue_light));
        listViewVariants.setDivider(sage);
        listViewVariants.setDividerHeight(3);
        setFilterAdapters(id, popupWindow, listViewVariants);
        listViewVariants.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long longId) {
                RummyLobbyFragment.this.filterTableList(position, popupWindow, id);
            }
        });
        popupWindow.setFocusable(true);
        int width = (int) (this.mActivity.getResources().getDimension(R.dimen.dropDownWidth) / this.mActivity.getResources().getDisplayMetrics().density);
        if (id == R.id.variants_layout || id == R.id.variants_drop_down_iv) {
            width = (int) (this.mActivity.getResources().getDimension(R.dimen.variantDropDownWidth) / this.mActivity.getResources().getDisplayMetrics().density);
        } else if (id == R.id.game_type_layout || id == R.id.game_type_drop_down_iv) {
            width = (int) (this.mActivity.getResources().getDimension(R.dimen.gameTypeDropDownWidth) / this.mActivity.getResources().getDisplayMetrics().density);
        } else if (id == R.id.chips_layout || id == R.id.chips_type_drop_down_iv) {
            width = (int) (this.mActivity.getResources().getDimension(R.dimen.chipsDropDownWidth) / this.mActivity.getResources().getDisplayMetrics().density);
        } else if (id == R.id.bet_layout || id == R.id.bet_type_drop_down_iv) {
            width = (int) (this.mActivity.getResources().getDimension(R.dimen.betDropDownWidth) / this.mActivity.getResources().getDisplayMetrics().density);
        } else if (id == R.id.player_type_layout || id == R.id.player_type_drop_down_iv) {
            width = (int) (this.mActivity.getResources().getDimension(R.dimen.playerDropDownWidth) / this.mActivity.getResources().getDisplayMetrics().density);
        }
        popupWindow.setHeight(-2);
        popupWindow.setWidth(width + 10);
        popupWindow.setContentView(listViewVariants);
        popupWindow.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.rummy_lobby_gradient_top_bar_flat_top));

        return popupWindow;
    }

    public void setSelectedTable(RummyTable table) {
        this.mSelectedTable = table;
    }

    public void showErrorPopUp(String message) {
        showGenericDialog(this.mActivity, message);
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
                if (!RummyLobbyFragment.this.mSelectedTable.getTable_cost().contains("CASH_CASH")) {
                   // RummyLobbyFragment.this.loadChips();
                }
            }
        });
        ((ImageView) dialog.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public class PlayerComparator implements Comparator<RummyTable> {
        public int compare(RummyTable table1, RummyTable table2) {
            int currentPlayers1 = Integer.parseInt(table1.getCurrent_players());
            int currentPlayers2 = Integer.parseInt(table2.getCurrent_players());
            if (currentPlayers2 < currentPlayers1) {
                return -1;
            }
            return currentPlayers2 > currentPlayers1 ? 1 : 0;
        }
    }

    private class RefreshAdapter extends AsyncTask<RummyEvent, Integer, List<RummyTable>> {
        private RefreshAdapter() {
        }

        protected List<RummyTable> doInBackground(RummyEvent... event) {
            String id = event[0].getId();
            if (RummyLobbyFragment.this.tables != null && RummyLobbyFragment.this.tables.size() > 0) {
                for (int i = RummyLobbyFragment.this.tables.size() - 1; i >= 0; i--) {
                    RummyTable table = (RummyTable) RummyLobbyFragment.this.tables.get(i);
                    if (id.equalsIgnoreCase(table.getId())) {
                        table.setCurrent_players(event[0].getCurrentPlayers());
                        table.setTotal_player(event[0].getTotalPlayers());
                        table.setTable_id(event[0].getTableId());
                        table.setJoined_players(event[0].getJoinedPlayers());
                        table.setStatus("Open");
                        break;
                    }
                }
            }
            return RummyLobbyFragment.this.tables;
        }

        protected void onPostExecute(List<RummyTable> list) {
            if (RummyLobbyFragment.this.lobbyAdapter != null) {
                if(RummyLobbyFragment.this.getSortedList() != null)
                {
                    Collections.sort(RummyLobbyFragment.this.getSortedList(), new PlayerComparator());
                }
                RummyLobbyFragment.this.lobbyAdapter.notifyDataSetChanged();
            }
        }
    }

    private void toggleFilterLayoutView(boolean show) {
        int width;

        if (show)
            width = 0;
        else
            width = filter_layout.getWidth();

        filter_layout.animate()
                .translationX(width)
                .alpha(1f)
                .setDuration(1000)
                .setListener(null);
    }

    private void callRefundApi()
    {
        try {
            final String TOKEN = RummyPrefManager.getString(getContext(), ACCESS_TOKEN_REST, "");
            RummyApplication rummyApplication = RummyApplication.getInstance();
            RummyTLog.e("token =",TOKEN);

            String url = RummyUtils.getApiSeverAddress()+ RummyUtils.refundApiUrl;
            RequestQueue queue = Volley.newRequestQueue(getContext());

            Map<String, String> params = new HashMap<String, String>();
            params.clear();
            params.put("tableid", rummyApplication.getCurrentTableId());
            params.put("bet", rummyApplication.getCurrentTableBet());
            params.put("amount",rummyApplication.getCurrentTableAmount());
            params.put("game_type",rummyApplication.getCurrentTableGameType());
            params.put("id",rummyApplication.getCurrentTableSeqId());
            params.put("user_id",rummyApplication.getCurrentTableUserId());
            params.put("order_id",rummyApplication.getCurrentTableOrderId());

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST, url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                RummyTLog.e(TAG+"refund api response", response.toString());


                            } catch (Exception e) {

                            }


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

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
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getBackground().getConstantState() == unchecked_dr.getConstantState())
            toggleBackground(true, (CheckBox) compoundButton);
        else
            toggleBackground(false, (CheckBox) compoundButton);


        if (pools.isChecked()) {
            game_type_pools.setVisibility(View.VISIBLE);
        } else
            game_type_pools.setVisibility(View.GONE);
        if (deals.isChecked())
            game_type_deals.setVisibility(View.VISIBLE);
        else
            game_type_deals.setVisibility(View.GONE);
        if (points.isChecked())
            game_type_points.setVisibility(View.VISIBLE);
        else
            game_type_points.setVisibility(View.GONE);

        if (pools.isChecked() || deals.isChecked() || points.isChecked())
            select_variant.setVisibility(View.GONE);
        else if (!pools.isChecked() && !deals.isChecked() && !points.isChecked()) {
            select_variant.setVisibility(View.VISIBLE);
            game_type_pools.setVisibility(View.GONE);
            game_type_points.setVisibility(View.GONE);
            game_type_deals.setVisibility(View.GONE);
        }


        if (compoundButton == free) {
            if (free.isChecked()) {
                handleMultiSelection(0, CHIPS_FILTER);
                cash.setChecked(false);
            } else {
                handleMultiSelection(1, CHIPS_FILTER);
                cash.setChecked(true);
            }
        } else if (compoundButton == cash) {
            if (cash.isChecked()) {
                handleMultiSelection(1, CHIPS_FILTER);
                free.setChecked(false);
            } else {
                handleMultiSelection(0, CHIPS_FILTER);
                free.setChecked(true);
            }
        }

        if (compoundButton == pools) {
            handleMultiSelection(0, VARIANTS_FILTER);
            toggleBackground(true, pool101);
            toggleBackground(true, pool201);
            toggleBackground(true, bestOf3);
        } else if (compoundButton == deals) {
            handleMultiSelection(1, VARIANTS_FILTER);
            toggleBackground(true, bestOf2);
            toggleBackground(true, bestOf6);
        } else if (compoundButton == points) {
            handleMultiSelection(2, VARIANTS_FILTER);
            toggleBackground(true, jokerType);
            toggleBackground(true, noJokerType);
        } else if (compoundButton == pool101)
            handleMultiSelection(0, GAME_TYPE_FILTER);
        else if (compoundButton == pool201)
            handleMultiSelection(1, GAME_TYPE_FILTER);
        else if (compoundButton == bestOf3)
            handleMultiSelection(2, GAME_TYPE_FILTER);
        else if (compoundButton == lowBet)
            handleMultiSelection(0, BET_FILTER);
        else if (compoundButton == mediumBet)
            handleMultiSelection(1, BET_FILTER);
        else if (compoundButton == highBet)
            handleMultiSelection(2, BET_FILTER);
        else if (compoundButton == players2)
            handleMultiSelection(0, PLAYERS_FILTER);
        else if (compoundButton == players6)
            handleMultiSelection(1, PLAYERS_FILTER);
        else if (compoundButton == bestOf2) {
            if (pools.getBackground().getConstantState() == unchecked_dr.getConstantState())
                handleMultiSelection(0, GAME_TYPE_FILTER);
            else if (pools.getBackground().getConstantState() == checked_dr.getConstantState())
                handleMultiSelection(3, GAME_TYPE_FILTER);
        } else if (compoundButton == bestOf6) {
            if (pools.getBackground().getConstantState() == unchecked_dr.getConstantState())
                handleMultiSelection(1, GAME_TYPE_FILTER);
            else if (pools.getBackground().getConstantState() == checked_dr.getConstantState())
                handleMultiSelection(4, GAME_TYPE_FILTER);
        } else if (compoundButton == jokerType) {
            if (pools.getBackground().getConstantState() == unchecked_dr.getConstantState() && deals.getBackground().getConstantState() == unchecked_dr.getConstantState())
                handleMultiSelection(0, GAME_TYPE_FILTER);
            else if (pools.getBackground().getConstantState() == checked_dr.getConstantState() && deals.getBackground().getConstantState() == checked_dr.getConstantState())
                handleMultiSelection(5, GAME_TYPE_FILTER);
            else if (pools.getBackground().getConstantState() == checked_dr.getConstantState() && deals.getBackground().getConstantState() == unchecked_dr.getConstantState())
                handleMultiSelection(3, GAME_TYPE_FILTER);
            else if (pools.getBackground().getConstantState() == unchecked_dr.getConstantState() && deals.getBackground().getConstantState() == checked_dr.getConstantState())
                handleMultiSelection(2, GAME_TYPE_FILTER);
        } else if (compoundButton == noJokerType) {
            if (pools.getBackground().getConstantState() == unchecked_dr.getConstantState() && deals.getBackground().getConstantState() == unchecked_dr.getConstantState())
                handleMultiSelection(1, GAME_TYPE_FILTER);
            else if (pools.getBackground().getConstantState() == checked_dr.getConstantState() && deals.getBackground().getConstantState() == checked_dr.getConstantState())
                handleMultiSelection(6, GAME_TYPE_FILTER);
            else if (pools.getBackground().getConstantState() == checked_dr.getConstantState() && deals.getBackground().getConstantState() == unchecked_dr.getConstantState())
                handleMultiSelection(4, GAME_TYPE_FILTER);
            else if (pools.getBackground().getConstantState() == unchecked_dr.getConstantState() && deals.getBackground().getConstantState() == checked_dr.getConstantState())
                handleMultiSelection(3, GAME_TYPE_FILTER);
        }

        sortTablesList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
        Log.e("vikas", "onattach call lobby bragment");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.e("vikas", "on detach call lobby bragment");
    }

    private void toggleBackground(boolean checked, CheckBox compoundButton) {
        if (checked)
            compoundButton.setBackground(checked_dr);
        else
            compoundButton.setBackground(unchecked_dr);
        compoundButton.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
    }


    /*public void sendJoinTableDataToServer(final RummyTable tableToJoin, final String buyInAmount)
    {
        showLoadingDialog(mActivity);
        try {
            final String TOKEN = RummyPrefManager.getString(mActivity, ACCESS_TOKEN_REST, "");

            RummyTLog.e("token =",TOKEN);

            String url = RummyUtils.getApiSeverAddress()+ RummyUtils.gameJoinPR;
            RequestQueue queue = Volley.newRequestQueue(mActivity);

            Map<String, String> params = new HashMap<String, String>();
            params.clear();
            params.put("tableid", tableToJoin.getTable_id());
            params.put("bet", tableToJoin.getBet());
            params.put("amount",buyInAmount);
            params.put("game_type",tableToJoin.getTable_type());
            params.put("id",tableToJoin.getId());
            params.put("amount_type",tableToJoin.getTable_cost());
            params.put("user_id", RummyApplication.getInstance().getUserData().getUserId());

            final RummyApplication rummyApplication = RummyApplication.getInstance();
            rummyApplication.setCurrentTableAmount(buyInAmount);
            rummyApplication.setCurrentTableBet(tableToJoin.getBet());
            rummyApplication.setCurrentTableGameType(tableToJoin.getTable_type());
            rummyApplication.setCurrentTableSeqId(tableToJoin.getId());
            rummyApplication.setCurrentTableUserId(RummyApplication.getInstance().getUserData().getUserId());
            rummyApplication.setCurrentTableId(tableToJoin.getTable_id());
            rummyApplication.setCurrentTableCostType(tableToJoin.getTable_cost());

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST, url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dismissLoadingDialog();
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
                                        joinTable(tableToJoin, buyInAmount);
                                    }
                                    else
                                    {
                                        processToJoinGame(tableToJoin);
                                    }

                                }
                                else
                                {
                                    RummyUtils.showGenericMsgDialog(mActivity,message);
                                }
                            } catch (Exception e) {
                                RummyTLog.e(TAG, "JsonException" + e.toString());
                                RummyUtils.showGenericMsgDialog(mActivity,"Something went wrong");
                            }


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    dismissLoadingDialog();
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
                    RummyUtils.showGenericMsgDialog(mContext,"Something went wrong, Please try again");
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
            dismissLoadingDialog();
            RummyUtils.showGenericMsgDialog(mActivity,"Something went wrong, Please try again after some time");
            e.printStackTrace();
        }
    }*/
}
