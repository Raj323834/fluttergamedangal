package in.glg.rummy.GameRoom;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.warkiz.widget.IndicatorSeekBar;

import org.apache.commons.lang3.text.WordUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import in.glg.rummy.NetworkProvider.RummyVolleySingleton;
import in.glg.rummy.R;
import in.glg.rummy.RummyApplication;
import in.glg.rummy.activities.RummyBaseActivity;
import in.glg.rummy.activities.RummyInstance;
import in.glg.rummy.activities.RummyLoadingActivity;
import in.glg.rummy.adapter.RummyPlayerDiscardCardsAdapter;
import in.glg.rummy.adapter.RummyPointsScoreBoardAdapter;
import in.glg.rummy.adapter.RummyScoreBoardAdapter;
import in.glg.rummy.adapter.RummySettingsAdapter;
import in.glg.rummy.api.RummyOnResponseListener;
import in.glg.rummy.api.requests.RummyLobbyDataRequest;
import in.glg.rummy.api.response.RummyJoinTableResponse;
import in.glg.rummy.api.response.RummyLobbyTablesResponse;
import in.glg.rummy.api.response.RummyLoginResponse;
import in.glg.rummy.engine.RummyGameEngine;
import in.glg.rummy.enums.RummyGameEvent;
import in.glg.rummy.exceptions.RummyGameEngineNotRunning;
import in.glg.rummy.fragments.RummyIamBackFragment;
import in.glg.rummy.fragments.RummyLobbyFragment;
import in.glg.rummy.fragments.RummyTablesFragment;
import in.glg.rummy.models.RummyEvent;
import in.glg.rummy.models.RummyGameInfo;
import in.glg.rummy.models.RummyGamePlayer;
import in.glg.rummy.models.RummyJoinRequest;
import in.glg.rummy.models.RummyJoinedTable;
import in.glg.rummy.models.RummyPlayingCard;
import in.glg.rummy.models.RummyReportBugRequest;
import in.glg.rummy.models.RummyTable;
import in.glg.rummy.models.RummyTableDetails;
import in.glg.rummy.utils.RummyCommonEventTracker;
import in.glg.rummy.utils.RummyErrorCodes;
import in.glg.rummy.utils.RummyEventComparator;
import in.glg.rummy.utils.RummyGamePlayerComparator;
import in.glg.rummy.utils.RummyGameRoomCustomScreenLess700;
import in.glg.rummy.utils.RummyGameRoomCustomScreenMore700;
import in.glg.rummy.utils.RummyPrefManager;
import in.glg.rummy.utils.RummyPrefManagerTracker;
import in.glg.rummy.utils.RummyConstants;
import in.glg.rummy.utils.RummyScreenSize;
import in.glg.rummy.utils.RummySoundPoolManager;
import in.glg.rummy.utils.RummyTLog;
import in.glg.rummy.utils.RummyUtils;
import in.glg.rummy.utils.RummyVibrationManager;

import static in.glg.rummy.utils.RummyConstants.ACCESS_TOKEN_REST;


public class RummyTableActivity extends RummyBaseActivity implements OnClickListener, OnItemSelectedListener {



    private RummyTable mSelectedTable;

    // points
    private String str_points_cash_no_of_player = "2";


    // pools
    private  String str_pools_cash_no_of_player = "2";
    private  String str_pools_cash_points = "101";



    //deals
    private  String str_deals_cash_no_of_player = "2";
    private  String str_deals_cash_round = "2";


    String str_points_bet_amount,str_pools_bet_amount,str_deals_bet_amount;
    private LinearLayout ll_points_rummy_tab_new_lobby,ll_pools_rummy_tab_new_lobby,ll_deals_rummy_tab_new_lobby;

    private RelativeLayout rl_points_section,rl_pools_section,rl_deals_section;

    private IndicatorSeekBar seek_bar_points_cash,seek_bar_pools_cash,seek_bar_deals_cash;
    private TextView tv_points_min_value,tv_points_max_value,tv_pools_min_value,tv_pools_max_value,tv_deals_min_value,tv_deals_max_value,tv_points_entry_fee;
    private  TextView tv_betAmount_points,tv_betAmount_pools,tv_betAmount_deals,tv_min_buyIn_points,tv_max_buyIn_points;

    // points
    private LinearLayout ll_points_cash_no_of_player_2,ll_points_cash_no_of_player_6;

    // pools
    private LinearLayout ll_pools_cash_no_of_players_2,ll_pools_cash_no_of_players_6,ll_pools_cash_points_101,ll_pools_cash_points_201;

    // deals
    private LinearLayout ll_deals_cash_no_of_player_2,ll_deals_cash_no_of_player_6,ll_deals_cash_round_2,ll_deals_cash_round_3;


    //play now buttons
    private LinearLayout ll_play_now_cash_deals,ll_play_now_cash_pools,ll_play_now_cash_points;

    int points_slider_parts_counts = 0;
    int pools_slider_parts_counts = 0;
    int deals_slider_parts_counts = 0;

    private ImageView game_type_iv1;
    private ImageView game_type_iv2;
    private ImageView game_type_iv1_free;
    private ImageView game_type_iv2_free;


    LinearLayout ll_cancel_btn_deals,ll_cancel_btn_pools,ll_cancel_btn_points;








    private List<RummyTable> tables;
    private boolean showblinking_table1 = false;
    private boolean showblinking_table2 = false;

    private LinearLayout ll_add_new_table_popup_layout;

    private List<Integer> POOL_101_2_array = new ArrayList<>();
    private List<Integer> POOL_101_6_array = new ArrayList<>();

    private List<Integer> POOL_201_2_array = new ArrayList<>();
    private List<Integer> POOL_201_6_array = new ArrayList<>();

    private List<Integer> BEST_OF_2_2 = new ArrayList<>();
    private  List<Integer> BEST_OF_2_6 = new ArrayList<>();

    private   List<Integer> BEST_OF_3_2 = new ArrayList<>();
    private   List<Integer> BEST_OF_3_6 = new ArrayList<>();

    private TextView tv_add_new_table;

    private List<Float> PR_JOKER_2 = new ArrayList<>();
    private  List<Float> PR_JOKER_6 = new ArrayList<>();


    private static final String TAG = RummyGameEngine.class.getSimpleName();
    /*public static int[] settingsImages = new int[]{R.drawable.rummy_game_refresh,
            R.drawable.rummy_game_info,
            R.drawable.rummy_gamesettings,
            R.drawable.rummy_lasthand,
            R.drawable.rummy_scoreboard,
            R.drawable.rummy_report};*/
    public static int[] settingsImages = new int[]{
            R.drawable.rummy_game_info,
            R.drawable.rummy_gamesettings,
            R.drawable.rummy_lasthand,
            R.drawable.rummy_scoreboard,
            R.drawable.rummy_report};
    public static String[] settingsItems;
    private String CALL_ACTION = "android.intent.action.PHONE_STATE";
    private ArrayList<RummyPlayingCard> discardList;
    private boolean isInOnPause = false;
    private boolean isTableAnimting = false;
    private String mActiveTableId = "";
    private String mBugType;
    private Spinner mBugTypeSpinner;
    private BroadcastReceiver mCloseActivityReceiver = new C16601();
    private DrawerLayout mDrawerLayout;
    private RummyEvent mEvent;
    private View mGameInfoView;
    private List<RummyEvent> mGameResultsList = new ArrayList();
    private View mGameSettingsView;
    private View mAddGameView;
    private LinearLayout mGameTablesLayout;
    private Boolean mIamBack = false;
    private boolean mIsActivityVisble = false;
    private boolean mIsBackPressed = false;
    private List<RummyJoinedTable> mJoinedTablesIds;
    private HashMap<String, RummyEvent> mLastHandMap = new HashMap();
    private View mPlayerCardsView;
    private Spinner mPointsRummySpinner;
    private Spinner mPoolsRummySpinner;
    private View mReportView;
    private FrameLayout mRootLayout;
    private Dialog mScoreBoardDialog;
    private View mSecondTableBtn;
    private View mFirstTableBtn;
    private ListView mSettingsListView;
    private Button mTable1Btn;
    private Button mTable2Btn;
    //private LinearLayout mFirstTableBtn;
    //private LinearLayout mSecondTableBtn;
    private HashMap<String, RummyTableDetails> mTableDetailsList = new HashMap();
    private RummyApplication mRummyApplication;
    private View mVisibleView;
    private RummyOnResponseListener reportListner = new RummyOnResponseListener(RummyJoinTableResponse.class) {
        public void onResponse(Object response) {
        }
    };

    private  RummyOnResponseListener joinTableListner = new RummyOnResponseListener(RummyJoinTableResponse.class) {
        @SuppressLint("HandlerLeak")
        public void onResponse(Object response) {
            if (response != null) {
                RummyJoinTableResponse joinTableResponse = (RummyJoinTableResponse) response;
                int code = Integer.parseInt(joinTableResponse.getCode());
                RummyJoinedTable joinedTable = new RummyJoinedTable();
                if (code == RummyErrorCodes.PLAYER_ALREADY_INPLAY || code == RummyErrorCodes.SUCCESS) {
                    RummyTableActivity.this.mRummyApplication.clearEventList();
                    if (!RummyTableActivity.this.isFoundTable(RummyTableActivity.this.mSelectedTable)) {
                        joinedTable = new RummyJoinedTable();
                        joinedTable.setTabelId(joinTableResponse.getTableId());
                        RummyTableActivity.this.mRummyApplication.setJoinedTableIds(joinedTable);
                    }
                    if(!isFinishing())
                    {
                       setUpGameRoom();
                    }

                } else if (code == RummyErrorCodes.LOW_BALANCE) {
                    callRefundApi();
                    if (RummyTableActivity.this.mSelectedTable.getTable_cost().contains("CASH_CASH")) {

                        String msg = ""+getResources().getString(R.string.rummy_low_balance_first)+" "+getResources().getString(R.string.rupee_symbol)+getRestAmounttoAdd(RummyTableActivity.this.mSelectedTable.getBet()+"")+" "+getResources().getString(R.string.rummy_low_balance_second);
                        if(!isFinishing())
                        {
                            showErrorBalanceBuyChips(RummyTableActivity.this, msg, RummyTableActivity.this.mSelectedTable.getBet());
                        }

                    } else {
                        if(!isFinishing())
                        {
                        RummyTableActivity.this.showLowBalanceDialog(RummyTableActivity.this, RummyTableActivity.this.getResources().getString(R.string.low_balance_free_chip), RummyTableActivity.this.mSelectedTable.getBet());
                        }

                    }
                } else if (code == RummyErrorCodes.TABLE_FULL) {
                    callRefundApi();
                    if(!isFinishing())
                    {
                        RummyTableActivity.this.showGenericDialog(RummyTableActivity.this, "This table is full");
                    }

                } else if (code == 483) {
                    callRefundApi();
                    if(!isFinishing())
                    {
                        showGenericDialog(RummyTableActivity.this,getResources().getString(R.string.state_block_message));
                    }

                }
                else
                {
                    callRefundApi();
                }
            }
            RummyUtils.dismissLoadingDialog();
        }
    };

    private RummyOnResponseListener lobbyDataListener = new RummyOnResponseListener(RummyLobbyTablesResponse.class) {
        public void onResponse(Object response) {
            if (response != null) {
                RummyTableActivity.this.tables = ((RummyLobbyTablesResponse) response).getTables();
                RummyInstance.getInstance().setRummyTables(RummyTableActivity.this.tables);
                if(RummyTableActivity.this.tables != null)
                {
                    RummyTLog.e("vikas","calling game room datalister");
                    createBetAmountArray();
                }

            }
        }
    };


    private boolean showCardDistributeAnimation = true;
    private Button soundsOffBtn;
    private boolean soundsOn = false;
    private Button soundsOnBtn;
    private Button vibrationOffBtn;
    private boolean vibrationOn = false;
    private Button vibrationOnBtn;
    private String gameType = "", mTourneyId = "";

    private TextView tableIdButton1;
    private TextView tableIdButton2;

    static RummyTableActivity mTableActivity;

    private LinearLayout first_table_bg,second_table_bg,ll_add_game_layout;




    class C16601 extends BroadcastReceiver {
        C16601() {
        }

        public void onReceive(Context context, Intent intent) {
            RummyTableActivity.this.finish();
        }
    }

    class C16612 implements OnClickListener {
        C16612() {
        }

        public void onClick(View v) {
            RummyTableActivity.this.hideNavigationMenu();
        }
    }

    class C16623 implements OnClickListener {
        C16623() {
        }

        public void onClick(View v) {
            RummyTableActivity.this.mScoreBoardDialog.dismiss();
        }
    }

    class C16634 implements OnClickListener {
        C16634() {
        }

        public void onClick(View view) {
            RummyTableActivity.this.dismissScoreBoard();
        }
    }

    class C16645 implements OnClickListener {
        C16645() {
        }

        public void onClick(View v) {
            RummyTableActivity.this.hideVisibleView();
        }
    }

    protected int getLayoutResource() {
        return R.layout.rummy_activity_table;
    }

    protected int getToolbarResource() {
        return 0;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().addFlags(128); //FLAG_KEEP_SCREEN_ON

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            init();
            setUpFullScreen();
            setIdsToViews();
            setScreenSize();
            addSettingsHeader();
            setUpSettingsView();
            setUpDropDownUI();



            mTableActivity = this;
        } catch (Exception e) {
            Log.e(TAG, e + "");
            e.printStackTrace();
        }
    }

    public void clearScoreBoard() {
        this.mGameResultsList.clear();
    }

    public void setScreenSize()  //setscreensize
    {

        if(!isTablet(RummyTableActivity.this))
        {
          /*  Utils.setViewHeight(this.mGameTablesLayout, GameRoomCustomScreenLess700.game_room_bottom_bar_height);
            Utils.setViewWidth(this.mGameTablesLayout, GameRoomCustomScreenLess700.game_room_bottom_bar_width);

            Utils.setViewMargin(this.mGameTablesLayout,60,0,0,0);


            Utils.setViewWidth(this.mFirstTableBtn.findViewById(R.id.rl_table_detail_root), GameRoomCustomScreenLess700.table_indicator_root_width);
            Utils.setViewWidth(this.mSecondTableBtn.findViewById(R.id.rl_table_detail_root), GameRoomCustomScreenLess700.table_indicator_root_width);


            Utils.setViewWidth(this.mFirstTableBtn.findViewById(R.id.rlView), GameRoomCustomScreenLess700.table_indicator_btn_width);
            Utils.setViewWidth(this.mSecondTableBtn.findViewById(R.id.rlView), GameRoomCustomScreenLess700.table_indicator_btn_width);

            Utils.setViewHeight(this.mFirstTableBtn, GameRoomCustomScreenLess700.table_indicator_btn_layout_height);
            Utils.setViewHeight(this.mSecondTableBtn, GameRoomCustomScreenLess700.table_indicator_btn_layout_height);

            Utils.setViewWidth(this.mFirstTableBtn, GameRoomCustomScreenLess700.table_indicator_btn_layout_width);
            Utils.setViewWidth(this.mSecondTableBtn, GameRoomCustomScreenLess700.table_indicator_btn_layout_width);

            Utils.setViewMargin(mFirstTableBtn.findViewById(R.id.tableButton),4,4,4,4);
            Utils.setViewMargin(mSecondTableBtn.findViewById(R.id.tableButton),4,4,4,4);


            Utils.setViewHeight(this.mFirstTableBtn.findViewById(R.id.rl_table_detail_root), GameRoomCustomScreenLess700.table_indicator_root_height);
            Utils.setViewHeight(this.mSecondTableBtn.findViewById(R.id.rl_table_detail_root), GameRoomCustomScreenLess700.table_indicator_root_height);
            Utils.setViewHeight(this.mFirstTableBtn.findViewById(R.id.rlView), GameRoomCustomScreenLess700.table_indicator_btn_height);
            Utils.setViewHeight(this.mSecondTableBtn.findViewById(R.id.rlView), GameRoomCustomScreenLess700.table_indicator_btn_height);

            Utils.setViewTextSize(this.mFirstTableBtn.findViewById(R.id.tableIdButton),GameRoomCustomScreenLess700.table_indicator_text_size);
            Utils.setViewTextSize(this.mSecondTableBtn.findViewById(R.id.tableIdButton),GameRoomCustomScreenLess700.table_indicator_text_size);

            Utils.setViewMargin(this.mTable1Btn,GameRoomCustomScreenLess700.margin_3_dp,0,0,0);
            Utils.setViewMargin(this.mTable2Btn,GameRoomCustomScreenLess700.margin_3_dp,0,0,0);*/

        }else {
     /*       Utils.setViewHeight(this.mGameTablesLayout, GameRoomCustomScreenMore700.game_room_bottom_bar_height);
            Utils.setViewMargin(this.mGameTablesLayout,100,0,0,0);


            Utils.setViewWidth(this.mFirstTableBtn.findViewById(R.id.rl_table_detail_root), GameRoomCustomScreenMore700.table_indicator_btn_width);
            Utils.setViewWidth(this.mSecondTableBtn.findViewById(R.id.rl_table_detail_root), GameRoomCustomScreenMore700.table_indicator_btn_width);
            Utils.setViewWidth(this.mFirstTableBtn.findViewById(R.id.rlView), GameRoomCustomScreenMore700.table_indicator_btn_width);
            Utils.setViewWidth(this.mSecondTableBtn.findViewById(R.id.rlView), GameRoomCustomScreenMore700.table_indicator_btn_width);

            Utils.setViewHeight(this.mFirstTableBtn, GameRoomCustomScreenMore700.table_indicator_btn_layout_height);
            Utils.setViewHeight(this.mSecondTableBtn, GameRoomCustomScreenMore700.table_indicator_btn_layout_height);

            Utils.setViewWidth(this.mFirstTableBtn, GameRoomCustomScreenMore700.table_indicator_btn_layout_width);
            Utils.setViewWidth(this.mSecondTableBtn, GameRoomCustomScreenMore700.table_indicator_btn_layout_width);

            Utils.setViewMargin(mFirstTableBtn,4,4,4,4);
            Utils.setViewMargin(mSecondTableBtn,4,4,4,4);

            Utils.setViewHeight(this.mFirstTableBtn.findViewById(R.id.rl_table_detail_root), GameRoomCustomScreenMore700.table_indicator_btn_layout_height);
            Utils.setViewHeight(this.mSecondTableBtn.findViewById(R.id.rl_table_detail_root), GameRoomCustomScreenMore700.table_indicator_btn_layout_height);
            Utils.setViewHeight(this.mFirstTableBtn.findViewById(R.id.rlView), GameRoomCustomScreenMore700.table_indicator_btn_height);
            Utils.setViewHeight(this.mSecondTableBtn.findViewById(R.id.rlView), GameRoomCustomScreenMore700.table_indicator_btn_height);

            Utils.setViewTextSize(this.mFirstTableBtn.findViewById(R.id.tableIdButton),GameRoomCustomScreenMore700.table_indicator_text_size);
            Utils.setViewTextSize(this.mSecondTableBtn.findViewById(R.id.tableIdButton),GameRoomCustomScreenMore700.table_indicator_text_size);*/
        }


    //    Utils.setViewWidth(this.ll_add_game_layout, GameRoomCustomScreenLess700.add_game_icon_width);
     //   Utils.setViewMargin(this.ll_add_game_layout,GameRoomCustomScreenLess700.margin_5_dp,GameRoomCustomScreenLess700.margin_5_dp,0,GameRoomCustomScreenLess700.margin_8_dp);
   //     Utils.setViewWidth(this.first_table_bg, GameRoomCustomScreenLess700.table_indicator_icon_bg_width);
    //    Utils.setViewHeight(this.first_table_bg, GameRoomCustomScreenLess700.table_indicator_icon_bg_height);


  //      Utils.setViewWidth(this.second_table_bg, GameRoomCustomScreenLess700.table_indicator_icon_bg_width);
  //      Utils.setViewHeight(this.second_table_bg, GameRoomCustomScreenLess700.table_indicator_icon_bg_height);
  //      Utils.setViewMargin(this.second_table_bg,GameRoomCustomScreenLess700.second_table_indicator_left_margin,0,0,0);


   //     Utils.setViewMargin(this.mTable1Btn,0,0,GameRoomCustomScreenLess700.margin_6_dp,GameRoomCustomScreenLess700.margin_5_dp);
   //     Utils.setViewMargin(this.mTable2Btn,0,0,GameRoomCustomScreenLess700.margin_6_dp,GameRoomCustomScreenLess700.margin_5_dp);




     //   Utils.setViewMargin(this.mFirstTableBtn,0,0,GameRoomCustomScreenLess700.table_indicator_btn_margin_top,0);
     //   Utils.setViewMargin(this.mSecondTableBtn,0,0,GameRoomCustomScreenLess700.table_indicator_btn_margin_top,0);

     //   setSizeFirstTableDots();
     //   setSizeSecondTableDots();



    }


    public void setSizeFirstTableDots()
    {

        View view6 = this.mFirstTableBtn.findViewById(R.id.table_user_6);
        RummyUtils.setViewHeight(view6, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewWidth(view6, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewMargin(view6,0, RummyGameRoomCustomScreenLess700.margin_3_dp, RummyGameRoomCustomScreenLess700.margin_2_dp,0);


        View view5 = this.mFirstTableBtn.findViewById(R.id.table_user_5);
        RummyUtils.setViewHeight(view5, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewWidth(view5, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewMargin(view5,0, RummyGameRoomCustomScreenLess700.margin_3_dp,0,0);


        View view4 = this.mFirstTableBtn.findViewById(R.id.table_user_4);
        RummyUtils.setViewHeight(view4, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewWidth(view4, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);

        View view3 = this.mFirstTableBtn.findViewById(R.id.table_user_3);
        RummyUtils.setViewHeight(view3, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewWidth(view3, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewMargin(view3, RummyGameRoomCustomScreenLess700.margin_3_dp,0,0,0);


        View view2 = this.mFirstTableBtn.findViewById(R.id.table_user_2);
        RummyUtils.setViewHeight(view2, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewWidth(view2, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewMargin(view2, RummyGameRoomCustomScreenLess700.margin_3_dp,0, RummyGameRoomCustomScreenLess700.margin_2_dp,0);


        View view1 = this.mFirstTableBtn.findViewById(R.id.table_user_1);
        RummyUtils.setViewHeight(view1, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewWidth(view1, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);




    }

    public void setSizeSecondTableDots()
    {

        View view6 = this.mSecondTableBtn.findViewById(R.id.table_user_6);
        RummyUtils.setViewHeight(view6, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewWidth(view6, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewMargin(view6,0, RummyGameRoomCustomScreenLess700.margin_3_dp, RummyGameRoomCustomScreenLess700.margin_2_dp,0);


        View view5 = this.mSecondTableBtn.findViewById(R.id.table_user_5);
        RummyUtils.setViewHeight(view5, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewWidth(view5, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewMargin(view5, 0, RummyGameRoomCustomScreenLess700.margin_3_dp,0,0);

        View view4 = this.mSecondTableBtn.findViewById(R.id.table_user_4);
        RummyUtils.setViewHeight(view4, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewWidth(view4, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);

        View view3 = this.mSecondTableBtn.findViewById(R.id.table_user_3);
        RummyUtils.setViewHeight(view3, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewWidth(view3, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewMargin(view3, RummyGameRoomCustomScreenLess700.margin_3_dp,0,0,0);

        View view2 = this.mSecondTableBtn.findViewById(R.id.table_user_2);
        RummyUtils.setViewHeight(view2, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewWidth(view2, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewMargin(view2, RummyGameRoomCustomScreenLess700.margin_3_dp,0, RummyGameRoomCustomScreenLess700.margin_2_dp,0);


        View view1 = this.mSecondTableBtn.findViewById(R.id.table_user_1);
        RummyUtils.setViewHeight(view1, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);
        RummyUtils.setViewWidth(view1, RummyGameRoomCustomScreenLess700.table_indicator_dots_size);


    }

    public void setGameResultEvents(RummyEvent event) {
        this.mGameResultsList.add(event);
        if (((RummyTableDetails) this.mTableDetailsList.get(this.mActiveTableId)) != null && ((RummyTableDetails) this.mTableDetailsList.get(this.mActiveTableId)).getTableType().contains(RummyUtils.PR)) {
            updatePointsScoreBoard();
        } else {
            updateScoreBoard();
        }

    }

    private void setUpGameRoom() {
        Log.e(TAG, "Setting game room........");
        this.mRummyApplication = RummyApplication.getInstance();
        List<RummyJoinedTable> joinedTables = this.mRummyApplication.getJoinedTableIds();
        updateTableButtons();
        if (!this.mIamBack || joinedTables.size() <= 1) {
            for (RummyJoinedTable joinedTable : joinedTables) {
                if (getFragmentByTag(joinedTable.getTabelId()) == null) {
                    launchTableFragment(joinedTable);
                }
            }
        }
        else {
            this.showCardDistributeAnimation = false;
            hideGameTablesLayoutOnImaBack();
            launchTableFragment(joinedTables.get(0));
            this.showCardDistributeAnimation = true;
        }
        if (this.mIamBack.booleanValue() && isIamBackVisible() == false) {
            hideGameTablesLayoutOnImaBack();
            launchFragment(new RummyIamBackFragment(), RummyIamBackFragment.class.getName());
        }
    }

    private boolean isIamBackVisible() {
        RummyIamBackFragment test = (RummyIamBackFragment) getSupportFragmentManager().findFragmentByTag(RummyIamBackFragment.class.getName());
        if (test != null && test.isVisible())
            return true;
        else
            return false;
    }

    private void init() {
        this.mIsActivityVisble = true;
        this.mIamBack = Boolean.valueOf(getIntent().getBooleanExtra("iamBack", false));

        if(getIntent().hasExtra("gameType"))
        {
            this.gameType = getIntent().getStringExtra("gameType");
        }
        if(getIntent().hasExtra("tourneyId"))
        {
            this.mTourneyId = getIntent().getStringExtra("tourneyId");
        }
        if(getIntent().hasExtra("event"))
        {
            this.mEvent = (RummyEvent) getIntent().getSerializableExtra("event");
        }


        if(getIntent().hasExtra("tables"))
        {
            RummyTLog.e("vikas","has table key in intent table activity");
            this.tables = (List<RummyTable>)getIntent().getSerializableExtra("tables");
            if(this.tables != null) {
                RummyTLog.e("vikas","has table key in intent tables not null");
                createBetAmountArray();
            }
            else
            {
                RummyTLog.e("vikas","has table key in intent tables are null");
            }
        }
        else
        {
            RummyTLog.e("vikas","has not table key in intent table activity");
            this.tables = RummyInstance.getInstance().getRummyTables();
            if(this.tables != null && this.tables.size() != 0) {
                RummyTLog.e("vikas","has not table key in intent tables not null");
                createBetAmountArray();
            }
            else
            {
                RummyTLog.e("vikas","has not table key in intent tables are null");
                getTableData();
            }
        }

      //  this.gameType = getIntent().getStringExtra("gameType");
     //   this.mTourneyId = getIntent().getStringExtra("tourneyId");
        this.discardList = new ArrayList();
        this.mJoinedTablesIds = new ArrayList();
        this.mGameResultsList.clear();
        settingsItems = getResources().getStringArray(R.array.settings_items);
        this.mRummyApplication = RummyApplication.getInstance();
        this.mJoinedTablesIds = this.mRummyApplication.getJoinedTableIds();
      //  this.mEvent = (RummyEvent) getIntent().getSerializableExtra("event");
        RummyUtils.HOME_BACK_PRESSED = false;
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mCloseActivityReceiver, new IntentFilter("CLOSE_ACTIVITIES"));

        setScreenWidth();

    }


    public void setScreenWidth()
    {
        Configuration config = getResources().getConfiguration();
        RummyUtils.screenWidthInDp = config.screenWidthDp;
        RummyUtils.screenHeghtInDp = config.smallestScreenWidthDp;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        RummyScreenSize.screen_height_px = height;
        RummyScreenSize.screen_width_px = width;

        Log.e("vikas","screen width config ="+ RummyUtils.screenWidthInDp);
        Log.e("vikas","screen smallest width config ="+config.smallestScreenWidthDp);
        Log.e("vikas","screen Height config ="+config.screenHeightDp);


        Log.e("vikas","hight pixel  ="+height);
        Log.e("vikas","width pixel  ="+width);
        Log.e("vikas","width pixel  ="+ Resources.getSystem().getDisplayMetrics().density);



        RummyScreenSize.availabel_height_px = height - RummyUtils.convertDpToPixel(RummyScreenSize.fixed_height_dp);



        ///set percentage


        ///top bar
        RummyGameRoomCustomScreenLess700.game_room_top_bar_height = RummyUtils.convertPixelsToDp(RummyUtils.getPercentage(RummyScreenSize.screen_height_px, RummyScreenSize.top_bar_height_percentage));
        RummyGameRoomCustomScreenLess700.top_bar_text_size = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.game_room_top_bar_height, RummyScreenSize.top_bar_text_size_percentage);
        RummyGameRoomCustomScreenLess700.game_room_top_bar_rupee_icon_height_width = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.game_room_top_bar_height, RummyScreenSize.top_bar_icon_size_percentage);


        //top bar tourney

        RummyGameRoomCustomScreenLess700.tourney_bar_height = RummyUtils.convertPixelsToDp(RummyUtils.getPercentage(RummyScreenSize.screen_height_px, RummyScreenSize.top_bar_height_percentage));
        RummyGameRoomCustomScreenLess700.tourney_bar_image_height_width = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.tourney_bar_height, RummyScreenSize.top_bar_icon_size_percentage);

        // rummy_player rummy_profile
       // GameRoomCustomScreenLess700.playerBoxHeight = Utils.convertPixelsToDp(Utils.getPercentage(ScreenSize.screen_height_px,ScreenSize.player_profile_box_height_percentage));
       // GameRoomCustomScreenLess700.playerBoxWidth = (int)(GameRoomCustomScreenLess700.playerBoxHeight * ScreenSize.player_profile_width_height_ratio);
      //  GameRoomCustomScreenLess700.playerBoxWidth = Utils.convertPixelsToDp(Utils.getPercentage(ScreenSize.screen_width_px,ScreenSize.player_profile_box_width_percentage));

      /*  GameRoomCustomScreenLess700.player_profile_padding_left = Utils.getPercentage(GameRoomCustomScreenLess700.playerBoxWidth,ScreenSize.player_profile_padding_left_percentage);
        GameRoomCustomScreenLess700.player_profile_padding_Right = Utils.getPercentage(GameRoomCustomScreenLess700.playerBoxWidth,ScreenSize.player_profile_padding_right_percentage);
*/
        RummyGameRoomCustomScreenLess700.playerProfileTextSize = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.playerBoxHeight, RummyScreenSize.player_profile_box_text_size_percentage);
        RummyGameRoomCustomScreenLess700.chunksSize = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.playerBoxHeight, RummyScreenSize.player_profile_box_chunk_size_percentage);
        RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.playerBoxHeight, RummyScreenSize.chunk_margin_left_right_percentage);

        RummyGameRoomCustomScreenLess700.player_system_iv_size = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.playerBoxHeight, RummyScreenSize.player_profile_box_system_iv_size_percentage);
        RummyGameRoomCustomScreenLess700.player_system_iv_marginTop = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.playerBoxHeight, RummyScreenSize.system_iv_margin_top_percentage);
        RummyGameRoomCustomScreenLess700.player_system_iv_marginRight = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.playerBoxHeight, RummyScreenSize.system_iv_margin_right_percentage);

     /*   GameRoomCustomScreenLess700.timer_size = Utils.getPercentage(GameRoomCustomScreenLess700.playerBoxHeight,ScreenSize.timer_size_percentage);
        GameRoomCustomScreenLess700.timer_margin_right = 0-Utils.getPercentage(GameRoomCustomScreenLess700.playerBoxWidth,ScreenSize.timer_margin_right_percentage);
        GameRoomCustomScreenLess700.timer_margin_top = GameRoomCustomScreenLess700.playerRankLayoutHeight /2;
*/
     /*   GameRoomCustomScreenLess700.player_dealer_icon_height = Utils.getPercentage(GameRoomCustomScreenLess700.playerBoxHeight,ScreenSize.dealer_icon_height_percentage);
        GameRoomCustomScreenLess700.player_dealer_icon_width = Utils.getPercentage(GameRoomCustomScreenLess700.playerBoxHeight,ScreenSize.dealer_icon_width_percentage);
*/
        RummyGameRoomCustomScreenLess700.autoPlayIconSizeProfile = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.playerBoxHeight, RummyScreenSize.autoplayicon_size_percentage);
        RummyGameRoomCustomScreenLess700.autoPlayCountTextSize = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.playerBoxHeight, RummyScreenSize.autoplaycountTextsize_percentage);

        RummyGameRoomCustomScreenLess700.opponentCardsWidth = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.playerBoxHeight, RummyScreenSize.opponent_card_width_percentage);
        RummyGameRoomCustomScreenLess700.opponentCardsHeight = RummyUtils.convertPixelsToDp(RummyUtils.getPercentage(RummyScreenSize.screen_height_px, RummyScreenSize.opponent_card_height_percentage));
        RummyGameRoomCustomScreenLess700.opponentCardsMarginTop = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.playerBoxHeight, RummyScreenSize.opponent_card_margin_top_percentage);

        /*GameRoomCustomScreenLess700.player2_6marginTop = 0 - (GameRoomCustomScreenLess700.playerRankLayoutHeight+GameRoomCustomScreenLess700.opponentCardsHeight+GameRoomCustomScreenLess700.opponentCardsMarginTop);
        GameRoomCustomScreenLess700.player2MarginRight = GameRoomCustomScreenLess700.timer_size + GameRoomCustomScreenLess700.timer_margin_right + GameRoomCustomScreenLess700.opponentCardsMarginTop;
        GameRoomCustomScreenLess700.player6TimerMarginLeft = GameRoomCustomScreenLess700.opponentCardsMarginTop;
        GameRoomCustomScreenLess700.player6MarginLeft = GameRoomCustomScreenLess700.timer_size + GameRoomCustomScreenLess700.timer_margin_right + GameRoomCustomScreenLess700.opponentCardsMarginTop;
        GameRoomCustomScreenLess700.player1MarginLeft =  GameRoomCustomScreenLess700.timer_size + GameRoomCustomScreenLess700.timer_margin_right;
*/

        ///rummy_toss card

        RummyGameRoomCustomScreenLess700.toss_card_width = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.playerBoxWidth, RummyScreenSize.toss_card_width_percentage);
        RummyGameRoomCustomScreenLess700.toss_card_height = (int)(RummyGameRoomCustomScreenLess700.toss_card_width * RummyScreenSize.toss_card_width_ratio);

        RummyGameRoomCustomScreenLess700.toss_card_margin_top = 0 - RummyGameRoomCustomScreenLess700.opponentCardsHeight;
        RummyGameRoomCustomScreenLess700.toss_card_margin_left = (RummyGameRoomCustomScreenLess700.playerBoxWidth - RummyGameRoomCustomScreenLess700.toss_card_width)/2;


        // tourney rank layout

        RummyGameRoomCustomScreenLess700.playerRankLayoutHeight = RummyUtils.convertPixelsToDp(RummyUtils.getPercentage(RummyScreenSize.screen_height_px, RummyScreenSize.tourney_rank_layout_height_percentage));
        RummyGameRoomCustomScreenLess700.playerRankLayoutWidth = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.playerBoxHeight, RummyScreenSize.tourney_rank_layout_width_percentage);
        RummyGameRoomCustomScreenLess700.playerRankPrizeIconSize = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.playerRankLayoutHeight, RummyScreenSize.tourney_rank_layout_icon_size_percentage);
        RummyGameRoomCustomScreenLess700.playerRankTextSize = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.playerRankLayoutHeight, RummyScreenSize.tourney_rank_layout_text_size_percentage);


        ////bottom bar

        if(!isTablet(RummyTableActivity.this))
        {
            RummyGameRoomCustomScreenLess700.game_room_bottom_bar_height = RummyUtils.convertPixelsToDp(RummyUtils.getPercentage(RummyScreenSize.screen_height_px, RummyScreenSize.bottom_bar_percentage));
            RummyGameRoomCustomScreenLess700.bottom_bar_btn_width = RummyUtils.convertPixelsToDp(RummyUtils.getPercentage(RummyScreenSize.screen_width_px, RummyScreenSize.bottom_bar_button_widht_percentage));
            RummyGameRoomCustomScreenLess700.bottom_bar_btn_height = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.game_room_bottom_bar_height, RummyScreenSize.bottom_bar_buttom_height_percentage);
            RummyGameRoomCustomScreenLess700.bottom_bar_btn_width = (int)(RummyGameRoomCustomScreenLess700.bottom_bar_btn_height * RummyScreenSize.bottom_bar_button_height_width_ratio);
            RummyGameRoomCustomScreenLess700.table_indicator_icon_bg_height = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.game_room_bottom_bar_height, RummyScreenSize.bottom_bar_table_bg_height_percentage);
        }
        else
        {
         /*   GameRoomCustomScreenMore700.game_room_bottom_bar_height = Utils.convertPixelsToDp(Utils.getPercentage(ScreenSize.screen_height_px,ScreenSize.bottom_bar_percentage_tab));
            GameRoomCustomScreenMore700.bottom_bar_btn_width = Utils.convertPixelsToDp(Utils.getPercentage(ScreenSize.screen_width_px,ScreenSize.bottom_bar_button_widht_percentage_tab));
            GameRoomCustomScreenMore700.bottom_bar_btn_height = Utils.getPercentage(GameRoomCustomScreenMore700.game_room_bottom_bar_height,ScreenSize.bottom_bar_buttom_height_percentage_tab);
            GameRoomCustomScreenMore700.bottom_bar_btn_width = (int)(GameRoomCustomScreenMore700.bottom_bar_btn_height * ScreenSize.bottom_bar_button_height_width_ratio_tab);
            GameRoomCustomScreenMore700.table_indicator_icon_bg_height = Utils.getPercentage(GameRoomCustomScreenMore700.game_room_bottom_bar_height,ScreenSize.bottom_bar_table_bg_height_percentage_tab);*/

            RummyGameRoomCustomScreenLess700.game_room_bottom_bar_height = RummyUtils.convertPixelsToDp(RummyUtils.getPercentage(RummyScreenSize.screen_height_px, RummyScreenSize.bottom_bar_percentage));
            RummyGameRoomCustomScreenLess700.bottom_bar_btn_width = RummyUtils.convertPixelsToDp(RummyUtils.getPercentage(RummyScreenSize.screen_width_px, RummyScreenSize.bottom_bar_button_widht_percentage));
            RummyGameRoomCustomScreenLess700.bottom_bar_btn_height = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.game_room_bottom_bar_height, RummyScreenSize.bottom_bar_buttom_height_percentage);
            RummyGameRoomCustomScreenLess700.bottom_bar_btn_width = (int)(RummyGameRoomCustomScreenLess700.bottom_bar_btn_height * RummyScreenSize.bottom_bar_button_height_width_ratio);
            RummyGameRoomCustomScreenLess700.table_indicator_icon_bg_height = RummyUtils.getPercentage(RummyGameRoomCustomScreenLess700.game_room_bottom_bar_height, RummyScreenSize.bottom_bar_table_bg_height_percentage);
        }


        /////middel deck

        RummyGameRoomCustomScreenLess700.GameDeckHeight = RummyUtils.convertPixelsToDp(RummyUtils.getPercentage(RummyScreenSize.screen_height_px, RummyScreenSize.middel_deck_height_percentage));

        RummyGameRoomCustomScreenLess700.gameScreenClosedDeckWidth = (int)(RummyGameRoomCustomScreenLess700.GameDeckHeight * RummyScreenSize.close_deck_height_width_ratio);
        RummyGameRoomCustomScreenLess700.gameScreenOpenDeckWidth = (int)(RummyGameRoomCustomScreenLess700.GameDeckHeight * RummyScreenSize.open_deck_height_width_ratiio);
        RummyGameRoomCustomScreenLess700.ImagefaceDownCardsWidth = (int)(RummyGameRoomCustomScreenLess700.gameScreenClosedDeckWidth * RummyScreenSize.face_down_image_width_ratio);

        RummyGameRoomCustomScreenLess700.tr_game_room_logo_Height = RummyUtils.convertPixelsToDp(RummyUtils.getPercentage(RummyScreenSize.screen_height_px, RummyScreenSize.logo_height_percentage));


        int total_widhtDp = RummyUtils.convertPixelsToDp(RummyScreenSize.screen_width_px);
        int total_empty_space = total_widhtDp - (RummyGameRoomCustomScreenLess700.gameScreenClosedDeckWidth+ RummyGameRoomCustomScreenLess700.gameScreenOpenDeckWidth+ RummyGameRoomCustomScreenLess700.gameScreenOpenDeckWidth+((int)(RummyGameRoomCustomScreenLess700.tr_game_room_logo_Height* RummyScreenSize.logo_widht_height_ratio)));
        RummyGameRoomCustomScreenLess700.tr_game_room_logo_left_margin = RummyUtils.getPercentage(total_empty_space, RummyScreenSize.logo_margin_left_percentage);

        ///// cards

     /*   int cardHeightFromHeight = Utils.convertPixelsToDp(Utils.getPercentage(ScreenSize.screen_height_px,ScreenSize.cards_height_percentage));
        int cardWidthFromHeight = (int)(cardHeightFromHeight * ScreenSize.card_height_width_ratio);

        int cardWidthFromWidth = Utils.convertPixelsToDp(Utils.getPercentage(ScreenSize.screen_width_px,ScreenSize.cards_width_percentage));
        if(cardWidthFromHeight < cardWidthFromWidth)
        {
            GameRoomCustomScreenLess700.stackCardHeight = Utils.convertPixelsToDp(Utils.getPercentage(ScreenSize.screen_height_px,ScreenSize.cards_height_percentage));
            GameRoomCustomScreenLess700.stackCardWidth = (int)(GameRoomCustomScreenLess700.stackCardHeight * ScreenSize.card_height_width_ratio);
            GameRoomCustomScreenLess700.cardGroupSpacing = (int)(GameRoomCustomScreenLess700.stackCardWidth * ScreenSize.card_left_margin_percentage);

            Log.e("vikas","calling card widht set from height");
        }
        else
        {
            GameRoomCustomScreenLess700.stackCardWidth = cardWidthFromWidth;
            GameRoomCustomScreenLess700.stackCardHeight = (int)(GameRoomCustomScreenLess700.stackCardWidth * ScreenSize.card_width_height_ratio);
            GameRoomCustomScreenLess700.cardGroupSpacing = (int)(GameRoomCustomScreenLess700.stackCardWidth * ScreenSize.card_left_margin_percentage);

            Log.e("vikas","calling card widht set from width");
        }
*/
        if(!isTablet(RummyTableActivity.this))
        {
            int cardHeightFromHeight = RummyUtils.convertPixelsToDp(RummyUtils.getPercentage(RummyScreenSize.screen_height_px, RummyScreenSize.cards_height_percentage));
            int cardWidthFromHeight = (int)(cardHeightFromHeight * RummyScreenSize.card_height_width_ratio);

            int cardWidthFromWidth = RummyUtils.convertPixelsToDp(RummyUtils.getPercentage(RummyScreenSize.screen_width_px, RummyScreenSize.cards_width_percentage));
            if(cardWidthFromHeight < cardWidthFromWidth)
            {
                RummyGameRoomCustomScreenLess700.stackCardHeight = RummyUtils.convertPixelsToDp(RummyUtils.getPercentage(RummyScreenSize.screen_height_px, RummyScreenSize.cards_height_percentage));
                RummyGameRoomCustomScreenLess700.stackCardWidth = (int)(RummyGameRoomCustomScreenLess700.stackCardHeight * RummyScreenSize.card_height_width_ratio);
                RummyGameRoomCustomScreenLess700.cardGroupSpacing = (int)(RummyGameRoomCustomScreenLess700.stackCardWidth * RummyScreenSize.card_left_margin_percentage);

               // GameRoomCustomScreenLess700.cardGroupSpacing = Utils.convertPixelsToDp(Utils.getPercentage(ScreenSize.screen_width_px,ScreenSize.card_left_margin_percentage));
                Log.e("vikas","calling card widht set from height");
            }
            else
            {
                RummyGameRoomCustomScreenLess700.stackCardWidth = cardWidthFromWidth;
                RummyGameRoomCustomScreenLess700.stackCardHeight = (int)(RummyGameRoomCustomScreenLess700.stackCardWidth * RummyScreenSize.card_width_height_ratio);
                RummyGameRoomCustomScreenLess700.cardGroupSpacing = (int)(RummyGameRoomCustomScreenLess700.stackCardWidth * RummyScreenSize.card_left_margin_percentage);

                Log.e("vikas","calling card widht set from width");
            }

        }else
        {
            int cardHeightFromHeight = RummyUtils.convertPixelsToDp(RummyUtils.getPercentage(RummyScreenSize.screen_height_px, RummyScreenSize.cards_height_percentage_tab));
            int cardWidthFromHeight = (int)(cardHeightFromHeight * RummyScreenSize.card_height_width_ratio_tab);

            int cardWidthFromWidth = RummyUtils.convertPixelsToDp(RummyUtils.getPercentage(RummyScreenSize.screen_width_px, RummyScreenSize.cards_width_percentage_tab));
            if(cardWidthFromHeight < cardWidthFromWidth)
            {
                RummyGameRoomCustomScreenMore700.stackCardHeight = RummyUtils.convertPixelsToDp(RummyUtils.getPercentage(RummyScreenSize.screen_height_px, RummyScreenSize.cards_height_percentage_tab));
                RummyGameRoomCustomScreenMore700.stackCardWidth = (int)(RummyGameRoomCustomScreenMore700.stackCardHeight * RummyScreenSize.card_height_width_ratio_tab);
                RummyGameRoomCustomScreenMore700.cardGroupSpacing = (int)(RummyGameRoomCustomScreenMore700.stackCardWidth * RummyScreenSize.card_left_margin_percentage_tab);

                Log.e("vikas","calling card widht set from height");
            }
            else
            {
                RummyGameRoomCustomScreenMore700.stackCardWidth = cardWidthFromWidth;
                RummyGameRoomCustomScreenMore700.stackCardHeight = (int)(RummyGameRoomCustomScreenMore700.stackCardWidth * RummyScreenSize.card_width_height_ratio_tab);
                RummyGameRoomCustomScreenMore700.cardGroupSpacing = (int)(RummyGameRoomCustomScreenMore700.stackCardWidth * RummyScreenSize.card_left_margin_percentage_tab);

                Log.e("vikas","calling card widht set from width");
            }

        }



        //// rummy_profile setup

    /*    int profile_margin = (total_widhtDp - ((GameRoomCustomScreenLess700.playerBoxWidth*5)+(GameRoomCustomScreenLess700.player6TimerMarginLeft+GameRoomCustomScreenLess700.player6MarginLeft)+(GameRoomCustomScreenLess700.player2MarginRight)))/4;
        if(profile_margin > 0)
        {
            GameRoomCustomScreenLess700.player5MarginLeft = profile_margin;
            GameRoomCustomScreenLess700.player5TimerMarginRight = 0 - profile_margin + GameRoomCustomScreenLess700.timer_margin_right;

            GameRoomCustomScreenLess700.player3MarginRight = profile_margin;

        }
*/


    }

    public void launchTableFragment(RummyJoinedTable joinedTable) {
        Log.e("launchTableF", joinedTable.getTabelId() + "");
        this.mActiveTableId = joinedTable.getTabelId();
        handleTableButtonClickEvents(joinedTable.getTabelId());
        RummyTablesFragment tablesFragment = new RummyTablesFragment();
        Bundle args = new Bundle();
        args.putString("tableId", joinedTable.getTabelId());
        if(joinedTable.isTourney())
        {
            args.putString("gameType", "tournament");
        }
        else
        {
            args.putString("gameType", "");
        }

        args.putString("tourneyId", joinedTable.getTourneyId());
        args.putSerializable("event", this.mEvent);
        args.putBoolean("iamBack", this.mIamBack.booleanValue());

        args.putBoolean("canShowAnimation", this.showCardDistributeAnimation);

        tablesFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_frame, tablesFragment, joinedTable.getTabelId());
        fragmentTransaction.commit();
    }

    /*public void updateFragment(String oldTableId, String newtableId, String gameType,String tourneyId) {
        String firstTableBtnStr = this.mTable1Btn.getText().toString().replaceAll("\\D+", "");
        String secondTableBtnStr = this.mTable2Btn.getText().toString().replaceAll("\\D+", "");
        if (oldTableId.equalsIgnoreCase(firstTableBtnStr)) {
         *//*this.mTable1Btn.setText(String.format("%s%s", new Object[]{"TABLE\n", newtableId}));
         this.tableIdButton1.setText(String.format("%s%s", new Object[]{"TABLE\n", newtableId.substring(newtableId.length() - 4)}));*//*

            this.mTable1Btn.setText(String.format("%s", new Object[]{newtableId}));
            this.tableIdButton1.setText(String.format("%s", new Object[]{newtableId.substring(newtableId.length() - 4)}));
        } else if (oldTableId.equalsIgnoreCase(secondTableBtnStr)) {
        *//* this.mTable2Btn.setText(String.format("%s%s", new Object[]{"TABLE\n", newtableId}));
         this.tableIdButton2.setText(String.format("%s%s", new Object[]{"TABLE\n", newtableId.substring(newtableId.length() - 4)}));*//*

            this.mTable2Btn.setText(String.format("%s", new Object[]{newtableId}));
            this.tableIdButton2.setText(String.format("%s", new Object[]{newtableId.substring(newtableId.length() - 4)}));
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment oldFragment = getFragmentByTag(oldTableId);
        if (oldFragment != null) {
            fragmentTransaction.remove(oldFragment);
            fragmentTransaction.commit();
        }
        JoinedTable joinedTable = new JoinedTable();
        joinedTable.setTabelId(newtableId);
        if (gameType != null && gameType.equalsIgnoreCase("tournament"))
        {
            this.gameType = "tournament";
            joinedTable.setTourney(true);

        }


        launchTableFragment(joinedTable);
        if (getActiveTableId().equalsIgnoreCase(firstTableBtnStr)) {
            handleTableButtonClickEvents(firstTableBtnStr);
        } else if (getActiveTableId().equalsIgnoreCase(secondTableBtnStr)) {
            handleTableButtonClickEvents(secondTableBtnStr);
        }
    }*/

    public void updateFragment(String oldTableId, String newtableId, String gameType,String tourneyId)
    {
        String firstTableBtnStr = this.mTable1Btn.getText().toString().replaceAll("\\D+", "");
        String secondTableBtnStr = this.mTable2Btn.getText().toString().replaceAll("\\D+", "");
        if (oldTableId.equalsIgnoreCase(firstTableBtnStr)) {
            this.mTable1Btn.setText(String.format("%s%s", new Object[]{"TABLE\n", newtableId}));
            //this.tableIdButton1.setText("#"+newtableId.substring(newtableId.length() - 4));
        } else if (oldTableId.equalsIgnoreCase(secondTableBtnStr)) {
            this.mTable2Btn.setText(String.format("%s%s", new Object[]{"TABLE\n", newtableId}));
           // this.tableIdButton2.setText("#"+newtableId.substring(newtableId.length() - 4));
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment oldFragment = getFragmentByTag(oldTableId);
        if (oldFragment != null) {
            fragmentTransaction.remove(oldFragment);
            fragmentTransaction.commit();
        }
        if(gameType!=null && gameType.equalsIgnoreCase("tournament"))
            this.gameType = gameType;

        RummyJoinedTable joinedTable = new RummyJoinedTable();
        joinedTable.setTabelId(newtableId);
        joinedTable.setTourneyId(tourneyId);
        if (gameType != null && gameType.equalsIgnoreCase("tournament"))
        {
            this.gameType = "tournament";
            joinedTable.setTourney(true);

        }
        launchTableFragment(joinedTable);
        if (getActiveTableId().equalsIgnoreCase(firstTableBtnStr)) {
            handleTableButtonClickEvents(firstTableBtnStr);
        } else if (getActiveTableId().equalsIgnoreCase(secondTableBtnStr)) {
            handleTableButtonClickEvents(secondTableBtnStr);
        }
    }

    private void setUpSettingsView() {
        this.mSettingsListView.setAdapter(new RummySettingsAdapter(this, settingsImages, settingsItems));
    }

    private void setUpDropDownUI() {
        this.mPoolsRummySpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.pool_rummy_items, R.layout.spinner_item));
        this.mPointsRummySpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.points_rummy_items, R.layout.spinner_item));
        this.mBugTypeSpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.report_problem_items, R.layout.spinner_item));
        this.mBugType = getResources().getStringArray(R.array.report_problem_items)[0];
        this.mPoolsRummySpinner.setOnItemSelectedListener(this);
        this.mPointsRummySpinner.setOnItemSelectedListener(this);
        this.mBugTypeSpinner.setOnItemSelectedListener(this);
    }

    private void setIdsToViews() {
        this.mGameTablesLayout = (LinearLayout) findViewById(R.id.game_tables_layout);
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.mSettingsListView = (ListView) findViewById(R.id.settingsListView);
        this.mGameInfoView = findViewById(R.id.game_info_layout);
        this.mGameSettingsView = findViewById(R.id.game_settings_layout);
        this.mAddGameView = findViewById(R.id.add_game_layout);
        setUpGameSettingsView(this.mGameSettingsView);
        this.mReportView = findViewById(R.id.report_problem_layout);
        this.mPointsRummySpinner = (Spinner) this.mGameSettingsView.findViewById(R.id.points_rummy_spinner);
        this.mPoolsRummySpinner = (Spinner) this.mGameSettingsView.findViewById(R.id.pools_rummy_spinner);
        this.mBugTypeSpinner = (Spinner) this.mReportView.findViewById(R.id.bug_type_spinner);
        ((Button) this.mReportView.findViewById(R.id.submit_report_button)).setOnClickListener(this);
        this.mRootLayout = (FrameLayout) findViewById(R.id.content_frame);
        this.mPlayerCardsView = findViewById(R.id.player_discard_cards_layout);
        this.mRootLayout.setOnClickListener(this);
        this.mPointsRummySpinner.getBackground().setColorFilter(-1, Mode.SRC_ATOP);
        this.mPoolsRummySpinner.getBackground().setColorFilter(-1, Mode.SRC_ATOP);
        this.mBugTypeSpinner.getBackground().setColorFilter(-1, Mode.SRC_ATOP);
        this.mFirstTableBtn = findViewById(R.id.table1Btn);
        this.mSecondTableBtn = findViewById(R.id.table2Btn);
        this.mTable1Btn = (Button) this.mFirstTableBtn.findViewById(R.id.tableButton);
        this.mTable2Btn = (Button) this.mSecondTableBtn.findViewById(R.id.tableButton);
        this.tableIdButton1 = (TextView) this.mFirstTableBtn.findViewById(R.id.tableIdButton);
        this.tableIdButton2 = (TextView) this.mSecondTableBtn.findViewById(R.id.tableIdButton);
        this.mFirstTableBtn.setOnClickListener(this);
        this.mSecondTableBtn.setOnClickListener(this);

        this.game_type_iv1 = this.mFirstTableBtn.findViewById(R.id.game_type_iv);
        this.game_type_iv2 = this.mSecondTableBtn.findViewById(R.id.game_type_iv);
        this.game_type_iv1_free = this.mFirstTableBtn.findViewById(R.id.game_type_free_iv);
        this.game_type_iv2_free = this.mSecondTableBtn.findViewById(R.id.game_type_free_iv);


        /*View view =findViewById(R.id.table1Btn);
        View view2 =findViewById(R.id.table2Btn);
        view.setOnClickListener(this);
        view2.setOnClickListener(this);*/
    }

    public void handleTableButtonClickEvents(String tableId) {
        RummyTablesFragment.myTableId = tableId;
        changeButtonState(tableId, this.mTable1Btn.getText().toString().replaceAll("\\D+", ""), this.mTable2Btn.getText().toString().replaceAll("\\D+", ""));
        for (RummyJoinedTable joinedTable : this.mRummyApplication.getJoinedTableIds()) {
            RummyTablesFragment fragment = (RummyTablesFragment) getFragmentByTag(joinedTable.getTabelId());
            if (!(fragment == null || fragment.getTag() == null)) {
                if (tableId.equalsIgnoreCase(joinedTable.getTabelId())) {
                    this.mActiveTableId = tableId;
                    fragment.showQuickAction(joinedTable.getTabelId());
                    if(fragment.isPlacedShow) fragment.showDeclareButton();
                    showFragment(fragment);
                    fragment.setTableButtonsUI();
                } else {
                    fragment.hideQuickAction();
                    hideFragment(fragment);
                }
            }
        }
    }

    private void changeButtonState(String tableId, String firstTableBtnStr, String secondTableBtnStr) {
        if (firstTableBtnStr.equalsIgnoreCase(tableId)) {
            this.mFirstTableBtn.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rummy_table_shift_on));
            //this.mSecondTableBtn.setBackgroundDrawable(null);
            this.mSecondTableBtn.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rummy_table_shift_off));
        } else if (secondTableBtnStr.equalsIgnoreCase(tableId)) {
            this.mSecondTableBtn.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rummy_table_shift_on));
            //this.mFirstTableBtn.setBackgroundDrawable(null);
            this.mFirstTableBtn.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rummy_table_shift_off));
        }
    }

    private void setUpGameSettingsView(View gameSettingsView) {
        this.soundsOnBtn = (Button) gameSettingsView.findViewById(R.id.sounds_on_btn);
        this.soundsOffBtn = (Button) gameSettingsView.findViewById(R.id.sounds_off_btn);
        this.vibrationOnBtn = (Button) gameSettingsView.findViewById(R.id.vibration_on_btn);
        this.vibrationOffBtn = (Button) gameSettingsView.findViewById(R.id.vibration_off_btn);
        this.soundsOnBtn.setOnClickListener(this);
        this.soundsOffBtn.setOnClickListener(this);
        this.vibrationOnBtn.setOnClickListener(this);
        this.vibrationOffBtn.setOnClickListener(this);
        boolean soundsOn = RummyPrefManager.getBool(this, "sounds", true);
        setSoundSettings(soundsOn);
        if (soundsOn) {
            setSoundsOnButton();
        } else {
            setSoundsOffButton();
        }
        boolean vibrationOn = RummyPrefManager.getBool(this, "vibration", true);
        setVibrationSettings(vibrationOn);
        if (vibrationOn) {
            setVibrationOnButton();
        } else {
            setVibrationOffButton();
        }
    }

    private void addSettingsHeader() {
        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.settings_header, this.mSettingsListView, false);
        ((ImageView) header.findViewById(R.id.header_settings_iv)).setOnClickListener(new C16612());
        this.mSettingsListView.addHeaderView(header, null, false);
    }

    @SuppressLint("WrongConstant")
    public void hideNavigationMenu() {
        this.mDrawerLayout.closeDrawer(5);
    }

    public void showGameInfo() {
        this.mVisibleView = this.mGameInfoView;
        this.mGameInfoView.setVisibility(View.VISIBLE);
        slideInView(this.mGameInfoView);
    }

    public void setReportProblem() {
        this.mVisibleView = this.mReportView;
        this.mReportView.setVisibility(View.VISIBLE);
        slideInView(this.mReportView);
    }

    public void setUpPlayerDiscards() {
        updatePlayerDiscards();
        this.mVisibleView = this.mPlayerCardsView;
        this.mPlayerCardsView.setVisibility(View.VISIBLE);
        slideInView(this.mPlayerCardsView);
    }

    public void setUpGameSettings() {
        this.mVisibleView = this.mGameSettingsView;
        slideInView(this.mGameSettingsView);
        this.mGameSettingsView.setVisibility(View.VISIBLE);
        setUpGameSettingsView(this.mGameSettingsView);
    }

    public void setUpAddGameLayout() {
        this.mVisibleView = this.mAddGameView;
        this.mAddGameView.setVisibility(View.VISIBLE);

        setAddGameView(this.mAddGameView);
    }


    public boolean isSlideMenuVisible() {
        if (this.mVisibleView == null || this.mVisibleView.getVisibility() != View.VISIBLE) {
            return false;
        }
        return true;
    }

    private boolean updateScoreBoard() {
        dismissScoreBoard();
        initScoreBoardDialog();
        ListView scoreBoardList = (ListView) this.mScoreBoardDialog.findViewById(R.id.score_board_lv);
        TextView noEntriesTv = (TextView) this.mScoreBoardDialog.findViewById(R.id.no_entry_found_tv);
        View footerView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.score_board_list_item, null, false);
        int footerCount = scoreBoardList.getFooterViewsCount();
        if (scoreBoardList.getFooterViewsCount() > 0) {
            for (int i = 0; i < footerCount; i++) {
                scoreBoardList.removeFooterView(footerView);
            }
        }
        scoreBoardList.addFooterView(footerView);
        View v = this.mScoreBoardDialog.findViewById(R.id.score_board_header_view);
        ((ImageView) this.mScoreBoardDialog.findViewById(R.id.pop_up_close_btn)).setOnClickListener(new C16623());
        TextView player1 = (TextView) v.findViewById(R.id.sb_player_1_tv);
        TextView player2 = (TextView) v.findViewById(R.id.sb_player_2_tv);
        TextView player3 = (TextView) v.findViewById(R.id.sb_player_3_tv);
        TextView player4 = (TextView) v.findViewById(R.id.sb_player_4_tv);
        TextView player5 = (TextView) v.findViewById(R.id.sb_player_5_tv);
        TextView player6 = (TextView) v.findViewById(R.id.sb_player_6_tv);
        ((TextView) footerView.findViewById(R.id.sb_game_no_tv)).setText("Total");
        LinearLayout llContainer = (LinearLayout) footerView.findViewById(R.id.llContainer);
        TextView player1Score = (TextView) footerView.findViewById(R.id.sb_player_1_tv);
        TextView player2Score = (TextView) footerView.findViewById(R.id.sb_player_2_tv);
        TextView player3Score = (TextView) footerView.findViewById(R.id.sb_player_3_tv);
        TextView player4Score = (TextView) footerView.findViewById(R.id.sb_player_4_tv);
        TextView player5Score = (TextView) footerView.findViewById(R.id.sb_player_5_tv);
        TextView player6Score = (TextView) footerView.findViewById(R.id.sb_player_6_tv);
        List<RummyEvent> scoreEventList = new ArrayList();
        if (this.mGameResultsList != null && this.mGameResultsList.size() > 0) {
            for (RummyEvent event : this.mGameResultsList) {
                if (event.getTableId().equalsIgnoreCase(this.mActiveTableId)) {
                    scoreEventList.add(event);
                }
            }
        }
        if (scoreEventList.size() > 0) {
            scoreEventList = RummyUtils.removeDuplicateEvents(scoreEventList);
        }
        if (scoreEventList.size() > 0) {
            Collections.sort(scoreEventList, Collections.reverseOrder(new RummyEventComparator()));
            scoreBoardList.setVisibility(View.VISIBLE);
            noEntriesTv.setVisibility(View.GONE);
            scoreBoardList.setAdapter(new RummyScoreBoardAdapter(this, scoreEventList));
            int i = 1;
            for (RummyEvent event2 : scoreEventList) {
                int playerCount = event2.getPlayer().size();
                List<RummyGamePlayer> gamePlayerList = event2.getPlayer();
                Collections.sort(gamePlayerList, new RummyGamePlayerComparator());
                if (i % 2 == 0)
                    llContainer.setBackgroundColor(getResources().getColor(R.color.rummy_grey_et_bg));
                else
                    llContainer.setBackgroundColor(getResources().getColor(R.color.rummy_white));

                i++;

                switch (playerCount) {
                    case 1:
                        RummyGamePlayer gamePlayer = (RummyGamePlayer) gamePlayerList.get(0);
                        player1.setText(gamePlayer.getNick_name());
                        player1Score.setText(WordUtils.capitalize(gamePlayer.getTotalScore()));
                        break;
                    case 2:
                        RummyGamePlayer gamePlayer1 = (RummyGamePlayer) gamePlayerList.get(0);
                        RummyGamePlayer gamePlayer2 = (RummyGamePlayer) gamePlayerList.get(1);
                        player1.setText(gamePlayer1.getNick_name());
                        player2.setText(gamePlayer2.getNick_name());
                        player1Score.setText(WordUtils.capitalize(gamePlayer1.getTotalScore()));
                        player2Score.setText(WordUtils.capitalize(gamePlayer2.getTotalScore()));
                        break;
                    case 3:
                        RummyGamePlayer p1 = (RummyGamePlayer) gamePlayerList.get(0);
                        RummyGamePlayer p2 = (RummyGamePlayer) gamePlayerList.get(1);
                        RummyGamePlayer p3 = (RummyGamePlayer) gamePlayerList.get(2);
                        player1.setText(p1.getNick_name());
                        player2.setText(p2.getNick_name());
                        player3.setText(p3.getNick_name());
                        player1Score.setText(WordUtils.capitalize(p1.getTotalScore()));
                        player2Score.setText(WordUtils.capitalize(p2.getTotalScore()));
                        player3Score.setText(WordUtils.capitalize(p3.getTotalScore()));
                        break;
                    case 4:
                        RummyGamePlayer p14 = (RummyGamePlayer) gamePlayerList.get(0);
                        RummyGamePlayer p24 = (RummyGamePlayer) gamePlayerList.get(1);
                        RummyGamePlayer p34 = (RummyGamePlayer) gamePlayerList.get(2);
                        RummyGamePlayer p44 = (RummyGamePlayer) gamePlayerList.get(3);
                        player1.setText(p14.getNick_name());
                        player2.setText(p24.getNick_name());
                        player3.setText(p34.getNick_name());
                        player4.setText(p44.getNick_name());
                        player1Score.setText(WordUtils.capitalize(p14.getTotalScore()));
                        player2Score.setText(WordUtils.capitalize(p24.getTotalScore()));
                        player3Score.setText(WordUtils.capitalize(p34.getTotalScore()));
                        player4Score.setText(WordUtils.capitalize(p44.getTotalScore()));
                        break;
                    case 5:
                        RummyGamePlayer p15 = (RummyGamePlayer) gamePlayerList.get(0);
                        RummyGamePlayer p25 = (RummyGamePlayer) gamePlayerList.get(1);
                        RummyGamePlayer p35 = (RummyGamePlayer) gamePlayerList.get(2);
                        RummyGamePlayer p45 = (RummyGamePlayer) gamePlayerList.get(3);
                        RummyGamePlayer p55 = (RummyGamePlayer) gamePlayerList.get(4);
                        player1.setText(p15.getNick_name());
                        player2.setText(p25.getNick_name());
                        player3.setText(p35.getNick_name());
                        player4.setText(p45.getNick_name());
                        player5.setText(p55.getNick_name());
                        player1Score.setText(p15.getTotalScore());
                        player2Score.setText(p25.getTotalScore());
                        player3Score.setText(p35.getTotalScore());
                        player4Score.setText(p45.getTotalScore());
                        player5Score.setText(p55.getTotalScore());
                        break;
                    case 6:
                        RummyGamePlayer p16 = (RummyGamePlayer) gamePlayerList.get(0);
                        RummyGamePlayer p26 = (RummyGamePlayer) gamePlayerList.get(1);
                        RummyGamePlayer p36 = (RummyGamePlayer) gamePlayerList.get(2);
                        RummyGamePlayer p46 = (RummyGamePlayer) gamePlayerList.get(3);
                        RummyGamePlayer p56 = (RummyGamePlayer) gamePlayerList.get(4);
                        RummyGamePlayer p66 = (RummyGamePlayer) gamePlayerList.get(5);
                        player1.setText(p16.getNick_name());
                        player2.setText(p26.getNick_name());
                        player3.setText(p36.getNick_name());
                        player4.setText(p46.getNick_name());
                        player5.setText(p56.getNick_name());
                        player6.setText(p66.getNick_name());
                        player1Score.setText(WordUtils.capitalize(p16.getTotalScore()));
                        player2Score.setText(WordUtils.capitalize(p26.getTotalScore()));
                        player3Score.setText(WordUtils.capitalize(p36.getTotalScore()));
                        player4Score.setText(WordUtils.capitalize(p46.getTotalScore()));
                        player5Score.setText(WordUtils.capitalize(p56.getTotalScore()));
                        player6Score.setText(WordUtils.capitalize(p66.getTotalScore()));
                        break;
                    default:
                        break;
                }
            }
            return true;
        }
        scoreBoardList.setVisibility(View.GONE);
        noEntriesTv.setVisibility(View.VISIBLE);
        return false;
    }

    private void initScoreBoardDialog() {
        this.mScoreBoardDialog = new Dialog(this);
        this.mScoreBoardDialog.requestWindowFeature(1);
        this.mScoreBoardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.rummy_black_trans85)));
        this.mScoreBoardDialog.setContentView(R.layout.rummy_dialog_score_board);
        ((ImageView) this.mScoreBoardDialog.findViewById(R.id.pop_up_close_btn)).setOnClickListener(new C16634());
    }

    public void dismissScoreBoard() {
        if (this.mScoreBoardDialog != null && this.mScoreBoardDialog.isShowing()) {
            this.mScoreBoardDialog.dismiss();
        }
    }

    public void showScoreBoardView() {
        if (((RummyTableDetails) this.mTableDetailsList.get(this.mActiveTableId)).getTableType().contains(RummyUtils.PR)) {
            if (!updatePointsScoreBoard()) {
                showGenericDialogTA(this, getString(R.string.no_entries_found_msg));
            } else if (this.mScoreBoardDialog != null) {
                this.mScoreBoardDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                this.mScoreBoardDialog.show();
                this.mScoreBoardDialog.getWindow().getDecorView().setSystemUiVisibility(
                        getWindow().getDecorView().getSystemUiVisibility());

                this.mScoreBoardDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            }
        } else if (!updateScoreBoard()) {
            showGenericDialogTA(this, getString(R.string.no_entries_found_msg));
        } else if (this.mScoreBoardDialog != null) {
            this.mScoreBoardDialog.show();
        }
    }

    private boolean updatePointsScoreBoard() {
        initScoreBoardDialog();
        ListView scoreBoardList = (ListView) this.mScoreBoardDialog.findViewById(R.id.score_board_lv);
        TextView noEntriesTv = (TextView) this.mScoreBoardDialog.findViewById(R.id.no_entry_found_tv);
        this.mScoreBoardDialog.findViewById(R.id.score_board_header_view).setVisibility(View.GONE);
        View headerView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.rummy_points_sb_list_header, null, false);
        int headerViewsCount = scoreBoardList.getHeaderViewsCount();
        if (headerViewsCount > 0) {
            for (int i = 0; i < headerViewsCount; i++) {
                scoreBoardList.removeFooterView(headerView);
            }
        }
        scoreBoardList.addHeaderView(headerView);
        List<RummyEvent> scoreEventList = new ArrayList();
        if (this.mGameResultsList != null && this.mGameResultsList.size() > 0) {
            for (RummyEvent event : this.mGameResultsList) {
                if (event.getTableId().equalsIgnoreCase(this.mActiveTableId)) {
                    scoreEventList.add(event);
                }
            }
        }
        if (scoreEventList.size() > 0) {
            scoreEventList = RummyUtils.removeDuplicateEvents(scoreEventList);
        }
        if (scoreEventList.size() > 0) {
            String userId = this.mRummyApplication.getUserData().getUserId();
            List<RummyEvent> updatedScoreEventList = new ArrayList();
            for (RummyEvent scoreEvent : scoreEventList) {
                for (RummyGamePlayer player : scoreEvent.getPlayer()) {
                    if (userId.equalsIgnoreCase(player.getUser_id())) {
                        updatedScoreEventList.add(scoreEvent);
                    }
                }
            }
            if (updatedScoreEventList == null || updatedScoreEventList.size() <= 0) {
                return false;
            }
            Collections.sort(updatedScoreEventList, Collections.reverseOrder(new RummyEventComparator()));
            scoreBoardList.setVisibility(View.VISIBLE);
            noEntriesTv.setVisibility(View.GONE);

            scoreBoardList.setAdapter(new RummyPointsScoreBoardAdapter(this, updatedScoreEventList, (RummyApplication.getInstance()).getUserData().getUserId()));
            return true;
        }
        scoreBoardList.setVisibility(View.GONE);
        noEntriesTv.setVisibility(View.VISIBLE);
        return false;
    }

    public void updateGameId(String tableId, String gameId) {
        RummyTableDetails tableDetails = (RummyTableDetails) this.mTableDetailsList.get(tableId);
        if (tableDetails != null) {
            tableDetails.setGameId(gameId);
        }
    }

    private void handleCloseBtn(View view) {
        ((ImageView) view.findViewById(R.id.pop_up_close_btn)).setOnClickListener(new C16645());
    }

    private void showGenericDialogTA(Context context, String message) {
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
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

        dialog.getWindow().getDecorView().setSystemUiVisibility(
                getWindow().getDecorView().getSystemUiVisibility());

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    private void hideVisibleView() {
        if (this.mVisibleView != null) {
            slideOutView(this.mVisibleView);
            this.mVisibleView.setVisibility(View.GONE);
            this.mVisibleView = null;
            RummyTablesFragment fragment = (RummyTablesFragment) getFragmentByTag(this.mActiveTableId);
            if (fragment != null) {
                fragment.showQuickAction(fragment.getTag());
            }
        }
    }

    public void updateTableButtons() {
        Log.e("vikas","calling update rummy_table button");
        List<RummyJoinedTable> joinedTables = this.mRummyApplication.getJoinedTableIds();
      /*if (joinedTables.size() == 2) {
         showTableButtons();
         this.mTable1Btn.setText(String.format("%s%s", new Object[]{"TABLE\n", joinedTables.get(0)}));
         this.mTable2Btn.setText(String.format("%s%s", new Object[]{"TABLE\n", joinedTables.get(1)}));
         this.tableIdButton1.setText(String.format("%s%s", new Object[]{"TABLE\n", joinedTables.get(0).substring(joinedTables.get(0).length() - 4)}));
         this.tableIdButton2.setText(String.format("%s%s", new Object[]{"TABLE\n", joinedTables.get(1).substring(joinedTables.get(1).length() - 4)}));
         this.mSecondTableBtn.performClick();
      } else if (joinedTables.size() == 1) {
         this.mTable1Btn.setText(String.format("%s%s", new Object[]{"TABLE\n", joinedTables.get(0)}));
         this.tableIdButton1.setText(String.format("%s%s", new Object[]{"TABLE\n", joinedTables.get(0).substring(joinedTables.get(0).length() - 4)}));
         this.mFirstTableBtn.setVisibility(View.VISIBLE);
         this.mSecondTableBtn.setVisibility(View.INVISIBLE);
         this.mFirstTableBtn.performClick();
      }*/

        if (joinedTables.size() == 2) {
            Log.e("vikas","joined rummy_table size = 2");
            showTableButtons();
            this.mTable1Btn.setText(String.format("%s", new Object[]{joinedTables.get(0).getTabelId()}));
            this.mTable2Btn.setText(String.format("%s", new Object[]{joinedTables.get(1).getTabelId()}));
           // this.tableIdButton1.setText(String.format("%s", new Object[]{joinedTables.get(0).getTabelId().substring(joinedTables.get(0).getTabelId().length() - 4)}));
            //this.tableIdButton2.setText(String.format("%s", new Object[]{joinedTables.get(1).getTabelId().substring(joinedTables.get(1).getTabelId().length() - 4)}));
            this.mSecondTableBtn.performClick();
        } else if (joinedTables.size() == 1) {
            Log.e("vikas","joined rummy_table size = 1");
            this.mTable1Btn.setText(String.format("%s", new Object[]{joinedTables.get(0).getTabelId()}));
            //this.tableIdButton1.setText(String.format("%s", new Object[]{joinedTables.get(0).getTabelId().substring(joinedTables.get(0).getTabelId().length() - 4)}));
            this.mFirstTableBtn.setVisibility(View.VISIBLE);
            this.mSecondTableBtn.setVisibility(View.INVISIBLE);
            this.mFirstTableBtn.performClick();
        }

    }

    public void showTableButtons() {
        this.mFirstTableBtn.setVisibility(View.VISIBLE);
        this.mSecondTableBtn.setVisibility(View.VISIBLE);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.content_frame) {
            if (this.mVisibleView != null) {
                hideVisibleView();
                return;
            }
            return;
        } else if (id == R.id.table1Btn) {
            hideVisibleView();
            setIsAnimating(false);
            ((ImageView) this.mFirstTableBtn.findViewById(R.id.table_flash_iv)).clearAnimation();
            handleTableButtonClickEvents(this.mTable1Btn.getText().toString().replaceAll("\\D+", ""));
            return;
        } else if (id == R.id.table2Btn) {
            hideVisibleView();
            setIsAnimating(false);
            ((ImageView) this.mSecondTableBtn.findViewById(R.id.table_flash_iv)).clearAnimation();
            handleTableButtonClickEvents(this.mTable2Btn.getText().toString().replaceAll("\\D+", ""));
            return;
        } else if (id == R.id.sounds_on_btn) {
            setSoundSettings(true);
            setSoundsOnButton();
            return;
        } else if (id == R.id.sounds_off_btn) {
            setSoundSettings(false);
            setSoundsOffButton();
            return;
        } else if (id == R.id.vibration_on_btn) {
            setVibrationSettings(true);
            setVibrationOnButton();
            return;
        } else if (id == R.id.vibration_off_btn) {
            setVibrationSettings(false);
            setVibrationOffButton();
            return;
        } else if (id == R.id.submit_report_button) {
            reportBugViaRest();
            return;
        }
        return;
    }

    public void setIsBackPressed(boolean isBackPressed) {
        this.mIsBackPressed = isBackPressed;
    }

    public boolean isBackPressed() {
        return this.mIsBackPressed;
    }


    private void setAddGameView(View v) {
        final TextView tvCash = v.findViewById(R.id.tvCashOption);
        final TextView tvFree = v.findViewById(R.id.tvFreeOption);
        final TextView tvFav = v.findViewById(R.id.tvFavOption);
        final TextView tvPointsRummyStart = v.findViewById(R.id.tvPointRummyStart);
        final TextView tvPoolsRummyMid = v.findViewById(R.id.tvPoolRummyMiddle);
        final TextView tvDealsRummyEnd = v.findViewById(R.id.tvDealsRummyLast);
        final TextView tvTwoPlayer = v.findViewById(R.id.tvTwoPlayer);
        final TextView tvSixPlayer = v.findViewById(R.id.tvSixPlayer);
        final TextView tv101Pool = v.findViewById(R.id.tv101Pool);
        final TextView tv201Pool = v.findViewById(R.id.tv201Pool);
        final LinearLayout llPoolsType = v.findViewById(R.id.llPoolsType);
        final LinearLayout llCashOptions = v.findViewById(R.id.llCashOptions);
        final Button btnJoin = v.findViewById(R.id.btnJoin);
        final ImageView ivClose = v.findViewById(R.id.ivCloseOption);

        final String[] variantType = {"Strikes"};
        final String[] player = {"2"};
        final String[] poolType = {"101"};

        tvCash.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RummyPrefManager.saveInt(RummyTableActivity.this, "tableCost", 1);
                tvCash.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_et_grey_flat_bottom_less_round));
                tvFree.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_app_blue_dark_flat_bottom_less_round));
                tvFav.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_app_blue_dark_flat_bottom_less_round));
                llCashOptions.setVisibility(View.GONE);
                tvCash.setTextColor(getResources().getColor(R.color.rummy_black));
                tvFree.setTextColor(getResources().getColor(R.color.rummy_white));
                tvFav.setTextColor(getResources().getColor(R.color.rummy_white));
            }
        });
        tvFree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RummyPrefManager.saveInt(RummyTableActivity.this, "tableCost", 0);
                tvCash.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_app_blue_dark_flat_bottom_less_round));
                tvFree.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_et_grey_flat_bottom_less_round));
                tvFav.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_app_blue_dark_flat_bottom_less_round));
                llCashOptions.setVisibility(View.GONE);
                tvCash.setTextColor(getResources().getColor(R.color.rummy_white));
                tvFree.setTextColor(getResources().getColor(R.color.rummy_black));
                tvFav.setTextColor(getResources().getColor(R.color.rummy_white));
            }
        });
        tvFav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RummyPrefManager.saveInt(RummyTableActivity.this, "tableCost", 1);
                tvCash.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_app_blue_dark_flat_bottom_less_round));
                tvFree.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_app_blue_dark_flat_bottom_less_round));
                tvFav.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_et_grey_flat_bottom_less_round));
                llCashOptions.setVisibility(View.GONE);
                tvCash.setTextColor(getResources().getColor(R.color.rummy_white));
                tvFree.setTextColor(getResources().getColor(R.color.rummy_white));
                tvFav.setTextColor(getResources().getColor(R.color.rummy_black));
            }
        });
        tvPointsRummyStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                variantType[0] ="Strikes";
                tvPointsRummyStart.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_app_blue_light_right_flat));
                tvPoolsRummyMid.setBackgroundColor(getResources().getColor(R.color.rummy_white));
                tvDealsRummyEnd.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_white_left_flat));
                llPoolsType.setVisibility(View.GONE);
                tvPointsRummyStart.setTextColor(getResources().getColor(R.color.rummy_white));
                tvPoolsRummyMid.setTextColor(getResources().getColor(R.color.rummy_black));
                tvDealsRummyEnd.setTextColor(getResources().getColor(R.color.rummy_black));
            }
        });
        tvPoolsRummyMid.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                variantType[0] ="Pools";
                tvPointsRummyStart.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_white_right_flat));
                tvPoolsRummyMid.setBackgroundColor(getResources().getColor(R.color.rummy_app_blue_light));
                tvDealsRummyEnd.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_white_left_flat));
                llPoolsType.setVisibility(View.VISIBLE);
                tvPointsRummyStart.setTextColor(getResources().getColor(R.color.rummy_black));
                tvPoolsRummyMid.setTextColor(getResources().getColor(R.color.rummy_white));
                tvDealsRummyEnd.setTextColor(getResources().getColor(R.color.rummy_black));
            }
        });
        tvDealsRummyEnd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                variantType[0] ="Deals";
                tvPointsRummyStart.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_white_right_flat));
                tvPoolsRummyMid.setBackgroundColor(getResources().getColor(R.color.rummy_white));
                tvDealsRummyEnd.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_app_blue_light_left_flat));
                llPoolsType.setVisibility(View.GONE);
                tvPointsRummyStart.setTextColor(getResources().getColor(R.color.rummy_black));
                tvPoolsRummyMid.setTextColor(getResources().getColor(R.color.rummy_black));
                tvDealsRummyEnd.setTextColor(getResources().getColor(R.color.rummy_white));
            }
        });
        tvTwoPlayer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                player[0] ="2";
                tvTwoPlayer.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_app_blue_light_right_flat));
                tvSixPlayer.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_white_left_flat));
                tvTwoPlayer.setTextColor(getResources().getColor(R.color.rummy_white));
                tvSixPlayer.setTextColor(getResources().getColor(R.color.rummy_black));
            }
        });
        tvSixPlayer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                player[0] ="6";
                tvTwoPlayer.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_white_right_flat));
                tvSixPlayer.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_app_blue_light_left_flat));
                tvTwoPlayer.setTextColor(getResources().getColor(R.color.rummy_black));
                tvSixPlayer.setTextColor(getResources().getColor(R.color.rummy_white));
            }
        });
        tv101Pool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                poolType[0] ="101";
                tv101Pool.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_app_blue_light_right_flat));
                tv201Pool.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_white_left_flat));
                tv101Pool.setTextColor(getResources().getColor(R.color.rummy_white));
                tv201Pool.setTextColor(getResources().getColor(R.color.rummy_black));
            }
        });
        tv201Pool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                poolType[0] ="201";
                tv101Pool.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_white_right_flat));
                tv201Pool.setBackground(getResources().getDrawable(R.drawable.rummy_curve_edges_filled_app_blue_light_left_flat));
                tv101Pool.setTextColor(getResources().getColor(R.color.rummy_black));
                tv201Pool.setTextColor(getResources().getColor(R.color.rummy_white));
            }
        });
        btnJoin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               setIsBackPressed(true);
                hideVisibleView();
                RummyUtils.GAME_ROOM_ADD_GAME_VARIANT=variantType[0];
                RummyUtils.GAME_ROOM_ADD_GAME_PLAYER=player[0];
                RummyUtils.GAME_ROOM_ADD_GAME_POOL_TYPE=poolType[0];
               RummyUtils.SHOW_LOBBY=true;
               showLobbyScreen();
            }
        });

        ivClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideVisibleView();
            }
        });
    }

    private void setSoundsOffButton() {
        this.soundsOnBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.rummy_transparent));
        this.soundsOnBtn.setText("");
        this.soundsOffBtn.setText("Off");
        this.soundsOffBtn.setBackground(getResources().getDrawable(R.drawable.rummy_rounded_oval_border_dark_grey));
    }

    private void setSoundsOnButton() {
        this.soundsOffBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.rummy_transparent));
        this.soundsOffBtn.setText("");
        this.soundsOnBtn.setText("On");
        this.soundsOnBtn.setBackground(getResources().getDrawable(R.drawable.rummy_rounded_oval_border_blue_light));
    }

    private void setVibrationOnButton() {
        this.vibrationOffBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.rummy_transparent));
        this.vibrationOffBtn.setText("");
        this.vibrationOnBtn.setText("On");
        this.vibrationOnBtn.setBackground(getResources().getDrawable(R.drawable.rummy_rounded_oval_border_blue_light));
    }

    private void setVibrationOffButton() {
        this.vibrationOnBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.rummy_transparent));
        this.vibrationOnBtn.setText("");
        this.vibrationOffBtn.setText("Off");
        this.vibrationOffBtn.setBackground(getResources().getDrawable(R.drawable.rummy_rounded_oval_border_dark_grey));
    }

    private void setSoundSettings(boolean soundsOn) {
        RummyPrefManager.saveBool(this, "sounds", soundsOn);
        RummySoundPoolManager.getInstance().setPlaySound(soundsOn);
    }

    private void setVibrationSettings(boolean vibrationOn) {
        RummyPrefManager.saveBool(this, "vibration", vibrationOn);
        RummyVibrationManager.getInstance().setVibration(vibrationOn);
    }

    private void slideOutView(View view) {
        if (view != null) {
            view.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.rummy_slide_right));
        }
    }

    private void slideInView(View view) {
        if (view != null) {
            view.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.rummy_slide_left));
            handleCloseBtn(view);
        }
    }

    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onPause() {
        super.onPause();
        this.mIsActivityVisble = false;
        if (!isBackPressed()) {
            this.isInOnPause = true;
            RummyGameEngine.getInstance().stop();
            RummyIamBackFragment iamBackFragment = (RummyIamBackFragment) getFragmentByTag(RummyIamBackFragment.class.getName());
            if (iamBackFragment != null) {
                iamBackFragment.clearDiscardedCards();
                removeIamBackFragment(iamBackFragment);
            }
        }
        setIsBackPressed(false);
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
    }

    //@Subscribe
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(RummyEvent event) {
        if (event.getEventName().equalsIgnoreCase("OTHER_LOGIN")) {
            RummyTLog.e(TAG, "GAME ROOM : OTHER_LOGIN");
            RummyPrefManager.clearPreferences(getApplicationContext());
            showLongToast(this, getString(R.string.other_login_err_msg));
         //   launchNewActivityFinishAll(new Intent(this, SplashActivity.class), true);
        }
        String firstTableId = "";
        String secondTableId = "";
        if (this.mJoinedTablesIds.size() == 1) {
            firstTableId = (String) this.mJoinedTablesIds.get(0).getTabelId();
        } else if (this.mJoinedTablesIds.size() == 2) {
            firstTableId = (String) this.mJoinedTablesIds.get(0).getTabelId();
            secondTableId = (String) this.mJoinedTablesIds.get(1).getTabelId();
        }
        if (event.getTableId() == null) {
            return;
        }
        if (!event.getTableId().equalsIgnoreCase(firstTableId) && !event.getTableId().equalsIgnoreCase(secondTableId)) {
            return;
        }
        if (event.getEventName().equalsIgnoreCase("GAME_RESULT") || event.getEventName().equalsIgnoreCase("PRE_GAME_RESULT")) {
           /* updateLastHandEvent(event.getTableId(), event);
            if (mGameResultsList.size() > 0) {
                for (int i = 0; i < this.mGameResultsList.size(); i++) {
                    if (this.mGameResultsList.get(i).getGameId().equalsIgnoreCase(event.getGameId())) {
                        this.mGameResultsList.remove(i);
                        this.mGameResultsList.add(event);
                    } else
                        this.mGameResultsList.add(event);
                }
            } else
                this.mGameResultsList.add(event);
            updateScoreBoard();*/

            String isTourney = "";
            if (this.mTableDetailsList != null && this.mActiveTableId != null && this.mTableDetailsList.get(this.mActiveTableId) != null) {
                isTourney = ((RummyTableDetails) this.mTableDetailsList.get(this.mActiveTableId)).getTournament_table();
            }

            if (isTourney != null && isTourney.equalsIgnoreCase("yes")) {
                updateLastHandEvent("TOURNEY_TOURNEY", event);
            } else {
                updateLastHandEvent(event.getTableId(), event);
            }


            if (mGameResultsList.size() > 0) {
                for (int i = 0; i < this.mGameResultsList.size(); i++) {
                    if (this.mGameResultsList.get(i).getGameId().equalsIgnoreCase(event.getGameId())) {
                        this.mGameResultsList.remove(i);
                        this.mGameResultsList.add(event);
                    } else
                        this.mGameResultsList.add(event);
                }
            } else
                this.mGameResultsList.add(event);
            updateScoreBoard();

        } else if (event.getEventName().equalsIgnoreCase("START_GAME")) {
            dismissScoreBoard();
            RummyTableDetails tableDetails = (RummyTableDetails) this.mTableDetailsList.get(event.getTableId());
            if (tableDetails != null) {
                tableDetails.setGameId(event.getGameId());
            }
            clearDiscards(event.getTableId());
        } else if (event.getEventName().equalsIgnoreCase("TABLE_CLOSED")) {
            clearDiscards(event.getTableId());
            updateScoreBoard();
        } else if (event.getEventName().equalsIgnoreCase("GAME_END")) {
            clearDiscards(event.getTableId());
        }
    }

    public RummyEvent updateLastHandEvent(String tableId, RummyEvent event) {
        return (RummyEvent) this.mLastHandMap.put(tableId, event);
    }

    public void setLastHandEvent() {
        String isTourney = "";
        if (this.mTableDetailsList != null && this.mActiveTableId != null && this.mTableDetailsList.get(this.mActiveTableId) != null) {
            isTourney = ((RummyTableDetails) this.mTableDetailsList.get(this.mActiveTableId)).getTournament_table();
        }
        
        RummyEvent lastHandEvent;
        if (isTourney != null && isTourney.equalsIgnoreCase("yes")) {
            lastHandEvent = (RummyEvent) this.mLastHandMap.get("TOURNEY_TOURNEY");
        } else {
            lastHandEvent = (RummyEvent) this.mLastHandMap.get(this.mActiveTableId);
        }
    
        RummyTablesFragment fragment = (RummyTablesFragment) getFragmentByTag(this.mActiveTableId);
        if (fragment != null) {
            fragment.showLastGameResult(lastHandEvent);
        }
    }

    public String getActiveTableId() {
        return this.mActiveTableId;
    }

    private void updatePlayerDiscards() {
        Set<String> playerUserIdList = new HashSet();
        HashMap<String, List<RummyPlayingCard>> playerHashMap = new HashMap();
        if (this.discardList != null && this.discardList.size() > 0) {
            Iterator it = this.discardList.iterator();
            while (it.hasNext()) {
                playerUserIdList.add(((RummyPlayingCard) it.next()).getUserName());
            }
            for (String userId : playerUserIdList) {
                List<RummyPlayingCard> playerDiscards = new ArrayList();
                for (int i = this.discardList.size() - 1; i >= 0; i--) {
                    RummyPlayingCard player = (RummyPlayingCard) this.discardList.get(i);
                    if (this.mActiveTableId.equalsIgnoreCase(player.getTableId())) {
                        if (userId.equalsIgnoreCase(player.getUserName())) {
                            playerDiscards.add(player);
                        }
                        if (playerDiscards.size() == 5) {
                            break;
                        }
                    }
                }
                if (playerDiscards.size() > 0) {
                    playerHashMap.put(userId, playerDiscards);
                }
            }
        }
        GridView gridView = (GridView) this.mPlayerCardsView.findViewById(R.id.player_discard_gv);
        if (playerHashMap.size() > 0) {
            gridView = (GridView) this.mPlayerCardsView.findViewById(R.id.player_discard_gv);
            gridView.setVisibility(View.VISIBLE);
            gridView.setAdapter(new RummyPlayerDiscardCardsAdapter(this, playerHashMap));
            return;
        }
        gridView.setVisibility(View.GONE);
    }

    public void addDiscardToPlayer(RummyEvent event) {
        String firstTableId = "";
        String secondTableId = "";
        if (this.mJoinedTablesIds.size() == 1) {
            firstTableId = (String) this.mJoinedTablesIds.get(0).getTabelId();
        } else if (this.mJoinedTablesIds.size() == 2) {
            firstTableId = (String) this.mJoinedTablesIds.get(0).getTabelId();
            secondTableId = (String) this.mJoinedTablesIds.get(1).getTabelId();
        }
        if (event.getTableId() == null) {
            return;
        }
        if (event.getTableId().equalsIgnoreCase(firstTableId) || event.getTableId().equalsIgnoreCase(secondTableId)) {
            RummyPlayingCard discardCard = new RummyPlayingCard();
            discardCard.setFace(event.getFace());
            discardCard.setSuit(event.getSuit());
            discardCard.setUserName(event.getNickName());
            discardCard.setTableId(event.getTableId());

            String username = RummyPrefManager.getString(getBaseContext(), "username", "");

            if (event.getAutoPlay() != null && event.getAutoPlay().equalsIgnoreCase("true") && event.getNickName().equalsIgnoreCase(username)) {
                Log.e(TAG, "Not Adding Card");
            } else {
                this.discardList.add(discardCard);
            }
        }
    }

    public void clearDiscards(String tableId) {
        if (this.discardList != null && this.discardList.size() > 0) {
            Iterator<RummyPlayingCard> cardIterator = this.discardList.iterator();
            while (cardIterator.hasNext()) {
                if (((RummyPlayingCard) cardIterator.next()).getTableId().equalsIgnoreCase(tableId)) {
                    cardIterator.remove();
                }
            }
        }
    }

    @SuppressLint("WrongConstant")
    public void onBackPressed() {
        if (this.mVisibleView != null) {
            hideVisibleView();
        } else if (this.mDrawerLayout != null && this.mDrawerLayout.isDrawerOpen(5)) {
            hideNavigationMenu();
        } else {
            setIsBackPressed(true);
            showLobbyScreen();
        }
    }

    @Subscribe
    public void onMessageEvent(RummyGameEvent event) {
        if (event.name().equalsIgnoreCase("SERVER_DISCONNECTED")) {

            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
            String dateFormat = sdf.format(currentTime);
            RummyPrefManagerTracker.saveString(this, "enginedisconnect", dateFormat + "");
            RummyTablesFragment.alTrackList.add("enginedisconnect");
            Log.e("enginedisconnect", dateFormat + "");

            RummyPrefManagerTracker.saveString(this, "internetdisconnect", dateFormat + "");
            RummyTablesFragment.alTrackList.add("internetdisconnect");
            Log.e("internetdisconnect", dateFormat + "");

            if (this.isInOnPause) {
                this.isInOnPause = false;
            } else if (!RummyUtils.HOME_BACK_PRESSED && this.mIsActivityVisble) {
                disableHearBeat();
                unregisterEventBus();
                navigateToLoadingScreen(true);
            }
        } else if (event.name().equalsIgnoreCase("OTHER_LOGIN")) {
            unregisterEventBus();
            handleOtherLogin();
        }
    }

    private void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void navigateToLoadingScreen(boolean isSocketDisconnected) {
        this.mRummyApplication.getEventList().clear();
        Intent intent = new Intent(this, RummyLoadingActivity.class);
        intent.putExtra("isSocketDisconnected", isSocketDisconnected);
        startActivity(intent);
    }

    public void setGameInfo(boolean isGameStarted,boolean isSubGameGoingOnForPoolsAndDeals) {

        RummyTableDetails tableDetails = (RummyTableDetails) this.mTableDetailsList.get(this.mActiveTableId);
        if(tableDetails != null)
        {
            RummyTLog.e(TAG, "TABLE ID : " + tableDetails.getTableId());
            RummyGameInfo gameInfo = RummyUtils.getGameInfo(tableDetails.getTableType());
            TextView gameType = (TextView) this.mGameInfoView.findViewById(R.id.game_info_game_type_tv);
            TextView gameId = (TextView) this.mGameInfoView.findViewById(R.id.game_info_game_id_tv);
            TextView variantType = (TextView) this.mGameInfoView.findViewById(R.id.game_info_game_variant_tv);
            TextView tableId = (TextView) this.mGameInfoView.findViewById(R.id.game_info_table_id_tv);
            TextView entryFee = (TextView) this.mGameInfoView.findViewById(R.id.entry_fee_tv);
            TextView middleDrop = (TextView) this.mGameInfoView.findViewById(R.id.middle_drop_tv);
            TextView fullCount = (TextView) this.mGameInfoView.findViewById(R.id.full_count_tv);
            TextView moveTime = (TextView) this.mGameInfoView.findViewById(R.id.move_time_tv);
            TextView extraTime = (TextView) this.mGameInfoView.findViewById(R.id.extra_time_tv);
            TextView maxExtraTime = (TextView) this.mGameInfoView.findViewById(R.id.max_time_tv);

            String finalDrops = "";
            if (gameInfo.getFirstDrop() != null && gameInfo.getFirstDrop().length() > 0) {
                ((TextView) this.mGameInfoView.findViewById(R.id.first_drop_tv)).setText(gameInfo.getFirstDrop());
            /*try {
                if (gameInfo.getFirstDrop().contains("x")) {
                    String[] val = gameInfo.getFirstDrop().split("x");
                    finalDrops = val[0];
                }
                else
                {
                    finalDrops = gameInfo.getFirstDrop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            }
            if (gameInfo.getMiddleDrop() != null && gameInfo.getMiddleDrop().length() > 0) {

                ((TextView) this.mGameInfoView.findViewById(R.id.middle_drop_tv)).setText(gameInfo.getMiddleDrop());
            /*try {
                if (gameInfo.getMiddleDrop().contains("x")) {
                    String[] val = gameInfo.getMiddleDrop().split("x");
                    finalDrops = finalDrops + ", " + val[0];
                }
                else
                {
                    finalDrops = finalDrops + ", " + gameInfo.getMiddleDrop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            }

            //middleDrop.setText(gameInfo.getMiddleDrop());

       /* if (gameInfo.getFullCount().contains("x")) {
            String[] val = gameInfo.getFullCount().split("x");
            fullCount.setText(val[0]);
        }*/

            fullCount.setText(gameInfo.getFullCount());

            //moveTime.setText(gameInfo.getMoveTime());
            extraTime.setText(gameInfo.getExtraTime());
            maxExtraTime.setText(gameInfo.getMaxExtraTime());
            if (tableDetails != null) {
                moveTime.setText(tableDetails.getTurnTimeout());
                entryFee.setText(tableDetails.getBet());
                String tableType = tableDetails.getTableType();
                variantType.setText(RummyUtils.getVariantType(tableType));
                gameType.setText(RummyUtils.formatTableName(tableType));
                tableId.setText(tableDetails.getTableId());
                setReportInfo(tableDetails,isGameStarted,isSubGameGoingOnForPoolsAndDeals);
                /*if (tableDetails.getGameId() != null) {
                    setGameId(tableDetails.getGameId());
                } else {
                    setGameId("");
                }*/

                if(isGameStarted || !isSubGameGoingOnForPoolsAndDeals) {
                    if (tableDetails.getGameId() != null) {
                        gameId.setText(tableDetails.getGameId());
                    }else  gameId.setText("");

                    tableId.setText(tableDetails.getTableId());
                }else{
                    gameId.setText("");
                    tableId.setText("");
                }
                //infoBinding.middleDropTv.setText(gameInfo.getMiddleDrop());
                setReportInfo(tableDetails,isGameStarted,isSubGameGoingOnForPoolsAndDeals);

            }
        }

    }

    private void setGameId(String gameId) {
        if (this.mGameInfoView != null) {
            ((TextView) this.mGameInfoView.findViewById(R.id.game_info_game_id_tv)).setText(gameId);
        }
        if (this.mReportView != null) {
            ((TextView) this.mReportView.findViewById(R.id.report_view_game_id_tv)).setText(gameId);
        }
    }

    private void setReportInfo(RummyTableDetails tableDetails,boolean isGameStarted,boolean subGameForPoolsAndDeals) {
        TextView gameType = (TextView) this.mReportView.findViewById(R.id.report_view_game_type_tv);
        TextView tableId = (TextView) this.mReportView.findViewById(R.id.report_view_table_id_tv);
        TextView variantTv = (TextView) this.mReportView.findViewById(R.id.report_view_game_variant_tv);
        TextView gameId =  (TextView) this.mReportView.findViewById(R.id.report_view_game_id_tv);

        if (tableDetails != null) {
            gameType.setText(RummyUtils.formatTableName(tableDetails.getTableType()));
            tableId.setText(tableDetails.getTableId());
            variantTv.setText(RummyUtils.getVariantType(tableDetails.getTableType()));
        }

        if(isGameStarted || !subGameForPoolsAndDeals) {
            tableId.setText(tableDetails.getTableId());
            if(tableDetails.getGameId() != null)
                gameId.setText(tableDetails.getGameId());
            else gameId.setText("");
        }else{
            tableId.setText("");
            gameId.setText("");
        }
    }

    protected void onResume() {
        super.onResume();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  //gg
                //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
            this.mIsActivityVisble = true;
            setUpGameRoom();
            if (!RummyGameEngine.getInstance().isSocketConnected() && !RummyUtils.HOME_BACK_PRESSED) {
                navigateToLoadingScreen(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e + "");
        }
    }


    private void callRefundApi()
    {
        try {
            final String TOKEN = RummyPrefManager.getString(RummyTableActivity.this, ACCESS_TOKEN_REST, "");
            RummyApplication rummyApplication = RummyApplication.getInstance();
            RummyTLog.e("token =",TOKEN);

            String url = RummyUtils.getApiSeverAddress()+ RummyUtils.refundApiUrl;
            RequestQueue queue = Volley.newRequestQueue(RummyTableActivity.this);

            Map<String, String> params = new HashMap<String, String>();
            params.clear();
            params.put("tableid", rummyApplication.getCurrentTableId());
            params.put("bet", rummyApplication.getCurrentTableBet());
            params.put("amount",rummyApplication.getCurrentTableAmount());
            params.put("game_type",rummyApplication.getCurrentTableGameType());
            params.put("id",rummyApplication.getCurrentTableSeqId());
            params.put("user_id",rummyApplication.getCurrentTableUserId());
            params.put("order_id",rummyApplication.getCurrentTableOrderId());
            params.put("amount_type",rummyApplication.getCurrentTableCostType());

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

    private void call_pools_deals_joinTable(String variantType)
    {
        RummyTable t = new RummyTable();
        if(variantType.equalsIgnoreCase("pools_cash"))
        {
            t = getTableWithFilter(str_pools_bet_amount+"",str_pools_cash_no_of_player,RummyUtils.getGamepayType("cash"),RummyUtils.getCombineVariantType("pools",str_pools_cash_points));
        }
        else if(variantType.equalsIgnoreCase("deals_cash"))
        {
            t = getTableWithFilter(str_deals_bet_amount+"",str_deals_cash_no_of_player,RummyUtils.getGamepayType("cash"),RummyUtils.getCombineVariantType("deals",str_deals_cash_round));
        }



        if(t.getTable_id() != null)
        {
            if(RummyApplication.getInstance().getJoinedTableIds().size() <= 1)
            {
                if(isFoundTable(t))
                {
                    Toast.makeText(RummyTableActivity.this,getResources().getString(R.string.same_table_join_msg),Toast.LENGTH_LONG).show();
                }
                else
                {
                    String balance;
                    setSelectedTable(t);
                    RummyApplication app = (RummyApplication.getInstance());
                    DecimalFormat format = new DecimalFormat("0.#");
                    if (t.getTable_cost().equalsIgnoreCase("CASH_CASH")) {
                        balance = app.getUserData().getRealChips();
                    } else {
                        balance = app.getUserData().getFunChips();
                    }
                    if (Math.round(Float.parseFloat(balance)) >= Math.round(Float.parseFloat(t.getBet()))) {
                        try {
                            //openConfirmDialog(t);
                            hideAddTableLayout();
                            sendJoinTableDataToServer(t,t.getBet());
                        }catch (Exception e){
                            RummyTLog.e(TAG+"",e+"");
                        }
                    } else if (t.getTable_cost().contains("CASH_CASH")) {
                        String msg = "" + getResources().getString(R.string.rummy_low_balance_first) + " " + getResources().getString(R.string.rupee_symbol) + getRestAmounttoAdd(t.getBet() + "") + " " + getResources().getString(R.string.rummy_low_balance_second);
                        showErrorBalanceBuyChips(this,msg, t.getBet());
                    }
                }


            }
            else
            {
                showGenericDialog(this, getString(R.string.max_table_reached_msg));
            }
        }
        else
        {
            Toast.makeText(this,getResources().getString(R.string.table_not_found),Toast.LENGTH_LONG).show();
        }

    }

    public boolean isFoundTable(RummyTable table) {
        for (RummyJoinedTable id : this.mRummyApplication.getJoinedTableIds()) {
            if (id.getTabelId().equalsIgnoreCase(table.getTable_id())) {
                return true;
            }
        }
        return false;
    }

    public void sendJoinTableDataToServer(final RummyTable tableToJoin, final String buyInAmount)
    {

        RummyUtils.showLoadingDialog(RummyTableActivity.this);
        try {
            final String TOKEN = RummyPrefManager.getString(this, ACCESS_TOKEN_REST, "");

            RummyTLog.e("token =",TOKEN);

            String url = RummyUtils.getApiSeverAddress()+ RummyUtils.gameJoinPR;
            RequestQueue queue = Volley.newRequestQueue(this);

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
            rummyApplication.setCurrentTableCostType(tableToJoin.getTable_cost());

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST, url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            RummyUtils.dismissLoadingDialog();
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
                                    RummyUtils.showGenericMsgDialog(RummyTableActivity.this,message);
                                }
                            } catch (Exception e) {
                                RummyTLog.e(TAG, "JsonException" + e.toString());
                                RummyUtils.showGenericMsgDialog(RummyTableActivity.this,"Something went wrong");
                            }


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    RummyUtils.dismissLoadingDialog();
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
                    RummyUtils.showGenericMsgDialog(RummyTableActivity.this,"Something went wrong, Please try again");
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
            RummyUtils.dismissLoadingDialog();
            RummyUtils.showGenericMsgDialog(RummyTableActivity.this,"Something went wrong, Please try again after some time");
            e.printStackTrace();
        }
    }


    public void joinTable(RummyTable table, String buyInAmt) {
        RummyUtils.showLoadingDialog(RummyTableActivity.this);
        this.mRummyApplication = (RummyApplication.getInstance());
        List<RummyJoinedTable> joinedTableIds = this.mRummyApplication.getJoinedTableIds();
        boolean foundTable = isFoundTable(table);
        if (joinedTableIds != null && joinedTableIds.size() == 2 && !foundTable) {
            RummyUtils.dismissLoadingDialog();
            showGenericDialog(this, getString(R.string.max_table_reached_msg));
        } else if (table != null) {
            setSelectedTable(table);
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
            request.setOrderId(RummyApplication.getInstance().getCurrentTableOrderId());
            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(getApplicationContext(), RummyUtils.getObjXMl(request), this.joinTableListner);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(RummyTableActivity.this, R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.d(TAG, "getTableData" + gameEngineNotRunning.getLocalizedMessage());
            }
        }
    }




    public void setSelectedTable(RummyTable table) {
        this.mSelectedTable = table;
    }


    public void setIamBackFlag() {
        Fragment fragment = getFragmentByTag(RummyIamBackFragment.class.getName());
        if (fragment != null) {
            removeIamBackFragment(fragment);
        }
        this.mIamBack = Boolean.valueOf(false);
        this.showCardDistributeAnimation = false;
        this.mGameTablesLayout.setVisibility(View.VISIBLE);
        for (RummyJoinedTable joinedTable : this.mRummyApplication.getJoinedTableIds()) {
            if (getFragmentByTag(joinedTable.getTabelId()) == null) {
                launchTableFragment(joinedTable);
            }
        }
        this.showCardDistributeAnimation = true;
    }

    public boolean isFromIamBack() {
        return this.mIamBack.booleanValue();
    }

    public void hideGameTablesLayout(String tableId) {
        /*List<String> joinedTables = this.mRummyApplication.getJoinedTableIds();
        if(joinedTables.size()>1)
        {
            String id1=joinedTables.get(0);
            String id2=joinedTables.get(1);
            if(id1!=null && id1.equalsIgnoreCase(tableId))
            {
                this.mGameTablesLayout.setVisibility(View.INVISIBLE);
            }
            if(id2!=null && id2.equalsIgnoreCase(tableId))
            {
                this.mGameTablesLayout.setVisibility(View.INVISIBLE);
            }
        }
        else {
            if (this.mActiveTableId != null && this.mActiveTableId.equalsIgnoreCase(tableId)) {
                this.mGameTablesLayout.setVisibility(View.INVISIBLE);
            }
        }*/

        if (this.mActiveTableId != null && this.mActiveTableId.equalsIgnoreCase(tableId)) {
            this.mGameTablesLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void hideGameTablesLayoutOnImaBack() {
        this.mGameTablesLayout.setVisibility(View.INVISIBLE);
    }

    public void showGameTablesLayoutOnImaBack() {
        Log.e("OnImaBack", "showGameTablesLayoutOnImaBack");
        this.mGameTablesLayout.setVisibility(View.INVISIBLE);
        RummyTablesFragment tablesFragment = (RummyTablesFragment) getFragmentByTag(this.mActiveTableId);
        boolean isGameResultsShowing = false;
        if (tablesFragment != null) {
            isGameResultsShowing = tablesFragment.isGameResultsShowing() || tablesFragment.isMeldScreenIsShowing();
        }
        if (!isGameResultsShowing) {
            this.mGameTablesLayout.setVisibility(View.VISIBLE);
            if (tablesFragment != null && tablesFragment.isOpponentValidShow()) {
                tablesFragment.showDeclareHelpView();
            }
        }
    }

    public void showGameTablesLayout(String tableId) {
        if (this.mActiveTableId.equalsIgnoreCase(tableId)) {
            this.mGameTablesLayout.setVisibility(View.VISIBLE);
        }
    }

    public boolean isIamBackShowing() {
        return this.mIamBack.booleanValue();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public Fragment getFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    public void updateTableFragment(String tableId) {
        Log.e("vikas","enter  update rummy_table fragment with rummy_table id ="+tableId);
        List<RummyJoinedTable> joinedTables = this.mRummyApplication.getJoinedTableIds();
        Log.e("vikas","enter  update rummy_table fragment with rummy_table size  ="+joinedTables.size());
        for (RummyJoinedTable joinedTable : joinedTables) {
            if (tableId.equalsIgnoreCase(joinedTable.getTabelId())) {
                joinedTables.remove(joinedTable);
                Log.e("vikas","enter  update rummy_table fragment remove rummy_table id  ="+joinedTable.getTabelId());
                break;
            }
        }
        if (joinedTables.size() == 0) {
            Log.e("vikas","enter  update rummy_table fragment, joined tables size  = 0");
            finish();
        } else if (joinedTables.size() == 1) {
            Log.e("vikas","enter  update rummy_table fragment, joined tables size  = 1");
            RummyTablesFragment fragment = (RummyTablesFragment) getFragmentByTag(tableId);
            if (!(fragment == null || fragment.getTag() == null)) {
                Log.e("vikas","enter  update rummy_table fragment, removing rummy_table id="+tableId);
                resetPlayerIconsOnTableBtn(tableId);
                hideFragment(fragment);
                removeFragment(fragment);
            }
            RummyTablesFragment fragment1 = (RummyTablesFragment) getFragmentByTag((String) joinedTables.get(0).getTabelId());
            if (fragment1 != null) {
                Log.e("vikas","rummy_show fragment for rummy_table id = "+joinedTables.get(0).getTabelId());
                showFragment(fragment1);
                updateTableButtons();
                RummyTableDetails tableDetails = fragment1.getTableInfo();
                List<RummyGamePlayer> joinedPlayerList = fragment1.getJoinedPlayerList();
                if (joinedPlayerList != null) {
                    for (RummyGamePlayer player : joinedPlayerList) {
                        if (tableDetails != null) {
                            setGameTableBtnUI(tableDetails.getTableId(), player, Integer.parseInt(tableDetails.getMaxPlayer()), false);
                        }
                    }
                }
            } else {
                finish();
            }

        }
    }

    public void updateScoreBoard(String tableId, RummyEvent event) {

    }

    public void resetPlayerIconsOnTableBtn(String tableId) {
        View view = getActiveTableView(tableId);
        ImageView user2 = (ImageView) view.findViewById(R.id.table_user_2);
        ImageView user3 = (ImageView) view.findViewById(R.id.table_user_3);
        ImageView user4 = (ImageView) view.findViewById(R.id.table_user_4);
        ImageView user5 = (ImageView) view.findViewById(R.id.table_user_5);
        ImageView user6 = (ImageView) view.findViewById(R.id.table_user_6);
        ((ImageView) view.findViewById(R.id.table_user_1)).setImageResource(R.drawable.rummy_table_grey_circle);
        user2.setImageResource(R.drawable.rummy_table_grey_circle);
        user3.setImageResource(R.drawable.rummy_table_grey_circle);
        user4.setImageResource(R.drawable.rummy_table_grey_circle);
        user5.setImageResource(R.drawable.rummy_table_grey_circle);
        user6.setImageResource(R.drawable.rummy_table_grey_circle);
    }

    public void setGameTableBtnUI(String tableId, RummyGamePlayer player, int maxPlayerCount, boolean isLeft) {
        View view = getActiveTableView(tableId);
        ImageView user1 = (ImageView) view.findViewById(R.id.table_user_1);
        ImageView user2 = (ImageView) view.findViewById(R.id.table_user_2);
        ImageView user3 = (ImageView) view.findViewById(R.id.table_user_3);
        ImageView user4 = (ImageView) view.findViewById(R.id.table_user_4);
        ImageView user5 = (ImageView) view.findViewById(R.id.table_user_5);
        ImageView user6 = (ImageView) view.findViewById(R.id.table_user_6);
        if (maxPlayerCount == 2) {
            user2.setVisibility(View.INVISIBLE);
            user3.setVisibility(View.INVISIBLE);
            user5.setVisibility(View.INVISIBLE);
            user6.setVisibility(View.INVISIBLE);
        } else if (maxPlayerCount == 6) {
            user2.setVisibility(View.VISIBLE);
            user3.setVisibility(View.VISIBLE);
            user5.setVisibility(View.VISIBLE);
            user6.setVisibility(View.VISIBLE);
        }
        switch (Integer.parseInt(player.getSeat())) {
            case 1:
                if (isLeft) {
                    user1.setImageResource(R.drawable.rummy_table_grey_circle);
                    return;
                } else {
                    user1.setImageResource(R.drawable.rummy_table_orange_circle);
                    return;
                }
            case 2:
                if (isLeft) {
                    user2.setImageResource(R.drawable.rummy_table_grey_circle);
                    return;
                } else {
                    user2.setImageResource(R.drawable.rummy_table_orange_circle);
                    return;
                }
            case 3:
                if (isLeft) {
                    user3.setImageResource(R.drawable.rummy_table_grey_circle);
                    return;
                } else {
                    user3.setImageResource(R.drawable.rummy_table_orange_circle);
                    return;
                }
            case 4:
                if (isLeft) {
                    user4.setImageResource(R.drawable.rummy_table_grey_circle);
                    return;
                } else {
                    user4.setImageResource(R.drawable.rummy_table_orange_circle);
                    return;
                }
            case 5:
                if (isLeft) {
                    user5.setImageResource(R.drawable.rummy_table_grey_circle);
                    return;
                } else {
                    user5.setImageResource(R.drawable.rummy_table_orange_circle);
                    return;
                }
            case 6:
                if (isLeft) {
                    user6.setImageResource(R.drawable.rummy_table_grey_circle);
                    return;
                } else {
                    user6.setImageResource(R.drawable.rummy_table_orange_circle);
                    return;
                }
            default:
                return;
        }
    }

    private View getActiveTableView(String tableId) {
        String tableId1 = this.mTable1Btn.getText().toString().replaceAll("\\D+", "");
        View view = this.mFirstTableBtn;
        if (tableId.equalsIgnoreCase(tableId1)) {
            return this.mFirstTableBtn;
        }
        return this.mSecondTableBtn;
    }

    public void setTableDetailsList(RummyTableDetails tableDetails) {
        this.mTableDetailsList.put(tableDetails.getTableId(), tableDetails);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == this.mBugTypeSpinner) {
            this.mBugType = (String) adapterView.getItemAtPosition(i);
        }
        ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.rummy_white));
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void reportProblem() {
        EditText bugExplanation = (EditText) this.mReportView.findViewById(R.id.bug_explanation_et);
        TextView tableId = (TextView) this.mReportView.findViewById(R.id.report_view_table_id_tv);
        TextView gameId = (TextView) this.mReportView.findViewById(R.id.report_view_game_id_tv);
        TextView gameType = (TextView) this.mReportView.findViewById(R.id.report_view_game_type_tv);
        String bugExplanationStr = bugExplanation.getText().toString();
        if (bugExplanationStr == null || bugExplanationStr.length() <= 0) {
            bugExplanation.setError("Please enter your rummy_report");
            return;
        }
        RummyReportBugRequest reportBugRequest = new RummyReportBugRequest();
        reportBugRequest.setEventName("REPORT_BUG");
        reportBugRequest.setTableId(tableId.getText().toString());
        reportBugRequest.setGameId(gameId.getText().toString());
        reportBugRequest.setGameType(gameType.getText().toString());
        reportBugRequest.setUuid(RummyUtils.generateUuid());
        reportBugRequest.setBugExplanation(bugExplanation.getText().toString());
        reportBugRequest.setBugType(this.mBugType);
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(getApplicationContext(), RummyUtils.getObjXMl(reportBugRequest), this.reportListner);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            RummyTLog.d(TAG, "discardCard" + gameEngineNotRunning.getLocalizedMessage());
        }
        showGenericDialogTA(this, "Thanks for reporting problem.");
        bugExplanation.setText("");
    }

    private void reportBugViaRest() {
        final String TOKEN = RummyPrefManager.getString(getApplicationContext(), RummyConstants.ACCESS_TOKEN_REST, "");
        EditText bugExplanation = (EditText) this.mReportView.findViewById(R.id.bug_explanation_et);
        final TextView tableId = (TextView) this.mReportView.findViewById(R.id.report_view_table_id_tv);
        final TextView gameId = (TextView) this.mReportView.findViewById(R.id.report_view_game_id_tv);
        final TextView gameType = (TextView) this.mReportView.findViewById(R.id.report_view_game_type_tv);
        final Spinner bugType = (Spinner) this.mReportView.findViewById(R.id.bug_type_spinner);
        final CheckBox cbDisCon = (CheckBox) this.mReportView.findViewById(R.id.cbDisCon);
        final CheckBox cbGameIs = (CheckBox) this.mReportView.findViewById(R.id.cbGameIssue);
        final CheckBox cbScoreIs = (CheckBox) this.mReportView.findViewById(R.id.cbScoreIssue);
        final CheckBox cbReqCall = (CheckBox) this.mReportView.findViewById(R.id.cbReqCall);
        final LinearLayout llContainer = (LinearLayout) this.mReportView.findViewById(R.id.llContainer);
        final LinearLayout llReportSuccess = (LinearLayout) this.mReportView.findViewById(R.id.llReportSuccess);
        final Button btn_ok = (Button) this.mReportView.findViewById(R.id.ok_report_btn);
        final String bugExplanationStr = bugExplanation.getText().toString();
      /*if (bugExplanationStr == null || bugExplanationStr.length() <= 0) {
         bugExplanation.setError("Please enter your rummy_report");
         return;
      }*/
        btn_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                llReportSuccess.setVisibility(View.GONE);
                llContainer.setVisibility(View.VISIBLE);
                cbDisCon.setChecked(false);
                cbGameIs.setChecked(false);
                cbReqCall.setChecked(false);
                cbScoreIs.setChecked(false);
                hideVisibleView();
            }
        });

        if (!cbDisCon.isChecked() && !cbGameIs.isChecked() && !cbScoreIs.isChecked() && !cbReqCall.isChecked()) {
            Toast.makeText(RummyTableActivity.this, "Please Select Issue Type", Toast.LENGTH_LONG).show();
            return;
        }

        List<String> values = new ArrayList<>();
        if (cbDisCon.isChecked())
            values.add("Disconnections");
        if (cbGameIs.isChecked())
            values.add("Game Issues");

        if (cbScoreIs.isChecked())
            values.add("Score Issues");

        if (cbReqCall.isChecked())
            values.add("Request Call Back");

        String bugs = "";
        for (int i = 0; i < values.size(); i++) {
            if (i == 0)
                bugs = values.get(i);
            else
                bugs = bugs + "," + values.get(i);
        }

        try {
            RequestQueue queue = RummyVolleySingleton.getInstance(getApplicationContext()).getRequestQueue();

            String apiURL = RummyUtils.getApiSeverAddress() + "api/v1/bug-report/";

            final String finalBugs = bugs;
            final StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "Response: " + response.toString());

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                trackRABEventWE();

                                llContainer.setVisibility(View.GONE);
                                llReportSuccess.setVisibility(View.VISIBLE);
                          /*Toast.makeText(getApplicationContext(), jsonObject.getString("rummy_message"), Toast.LENGTH_SHORT).rummy_show();
                          hideVisibleView();*/
                            } catch (Exception e) {
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "Error Resp: " + error.toString());

                            NetworkResponse response = error.networkResponse;
                            if (error instanceof ServerError && response != null) {
                                try {
                                    String res = new String(response.data,
                                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    Log.d(TAG, "Error: " + res);
                                    if (res != null) {
                                        try {
                                            JSONObject json = new JSONObject(res.toString());
                                            if (json.getString("status").equalsIgnoreCase("Error"))
                                              //  Toast.makeText(getApplicationContext(), json.getString("rummy_message"), Toast.LENGTH_LONG).show();

                                            hideVisibleView();

                                        } catch (Exception e) {
                                            Log.e(TAG, "EXP: parsing error for login -->> " + e.toString());
                                        }
                                    }
                                } catch (UnsupportedEncodingException e1) {
                                    // Couldn't properly decode data to string
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Token " + TOKEN);
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override

                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("tableid", tableId.getText().toString());
                    params.put("gametype", gameType.getText().toString());
                    params.put("bugtype", bugType.getSelectedItem().toString());
                    params.put("message", finalBugs);
                    //params.put("rummy_message", bugExplanationStr);
                    params.put("gameid", gameId.getText().toString());
                    params.put("device_type", getDeviceType());
                    params.put("client_type", RummyUtils.CLIENT_TYPE);
                    params.put("version", RummyUtils.getVersionCode(getApplicationContext()));

                    return params;
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

    public void flashButton(String tableId) {
        String firstTableBtnStr = this.mTable1Btn.getText().toString().replaceAll("\\D+", "");
        String secondTableBtnStr = this.mTable2Btn.getText().toString().replaceAll("\\D+", "");
        if (firstTableBtnStr.equalsIgnoreCase(tableId) && !tableId.equalsIgnoreCase(this.mActiveTableId)) {
            animateTable(this.mFirstTableBtn);
        } else if (secondTableBtnStr.equalsIgnoreCase(tableId) && !tableId.equalsIgnoreCase(this.mActiveTableId)) {
            animateTable(this.mSecondTableBtn);
        }
    }

    private void animateTable(View view) {
        if (view != null) {
            ImageView flashOImage = (ImageView) view.findViewById(R.id.table_flash_iv);
            Animation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(300);
            animation.setInterpolator(new LinearInterpolator());
            animation.setRepeatCount(-1);
            animation.setRepeatMode(2);
            flashOImage.startAnimation(animation);
        }
    }

    public RummyTable getTableWithFilter(String betamount, String no_of_player, String game_paye_type, String variant_type)
    {
        RummyTable table_return = new RummyTable();
        if(tables != null)
        {
            for (int i=0;i< tables.size();i++)
            {
                RummyTable table = tables.get(i);
                float bettcmp = Float.parseFloat(betamount);
                float tablebet = Float.parseFloat(table.getBet());
                String str_table_bet = tablebet+"";
                String cmp_bet = bettcmp+"";
                if(table.getMaxplayer().equalsIgnoreCase(no_of_player) && table.getTable_type().equalsIgnoreCase(variant_type) && str_table_bet.equalsIgnoreCase(cmp_bet) && table.getTable_cost().equalsIgnoreCase(game_paye_type))
                {
                    return  table;
                }
            }
        }

        return table_return;

    }

    public boolean canStopAnimateTable() {
        return this.isTableAnimting;
    }

    private void setIsAnimating(boolean isAnimating) {
        this.isTableAnimting = isAnimating;
    }

    private void getTableData() {
        RummyLobbyDataRequest request = new RummyLobbyDataRequest();
        request.setCommand("list_gamesettings");
        request.setUuid(RummyUtils.generateUuid());

        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(RummyTableActivity.this.getApplicationContext(), RummyUtils.getObjXMl(request), this.lobbyDataListener);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {

            RummyTLog.e(TAG, "getLobbyData" + gameEngineNotRunning.getLocalizedMessage());
        }
    }


    private void createBetAmountArray()
    {
        if(tables != null)
        {
            POOL_101_2_array.clear();
            POOL_101_6_array.clear();

            POOL_201_2_array.clear();
            POOL_201_6_array.clear();

            BEST_OF_2_2.clear();
            BEST_OF_2_6.clear();

            BEST_OF_3_2.clear();
            BEST_OF_3_6.clear();


            PR_JOKER_2.clear();
            PR_JOKER_6.clear();


            for(int i=0; i<tables.size();i++)
            {
                RummyTable t = tables.get(i);

                if(t.getTable_cost().equalsIgnoreCase("CASH_CASH"))
                {
                    if(t.getTable_type().contains("101"))
                    {
                        int betamt = Integer.parseInt(t.getBet());
                        if(t.getMaxplayer().equalsIgnoreCase("2"))
                        {

                            if(!POOL_101_2_array.contains(betamt))
                            {
                                POOL_101_2_array.add(betamt);
                            }

                        }
                        else
                        {
                            if(!POOL_101_6_array.contains(betamt))
                            {
                                POOL_101_6_array.add(betamt);
                            }

                        }
                    }
                    else if(t.getTable_type().contains("201"))
                    {
                        int betamt = Integer.parseInt(t.getBet());
                        if(t.getMaxplayer().equalsIgnoreCase("2"))
                        {
                            if(!POOL_201_2_array.contains(betamt))
                            {
                                POOL_201_2_array.add(betamt);
                            }

                        }
                        else
                        {
                            if(!POOL_201_6_array.contains(betamt))
                            {
                                POOL_201_6_array.add(betamt);
                            }

                        }

                    }
                    else if(t.getTable_type().contains("BEST_OF_2"))
                    {
                        int betamt = Integer.parseInt(t.getBet());
                        if(t.getMaxplayer().equalsIgnoreCase("2"))
                        {
                            if(!BEST_OF_2_2.contains(betamt))
                            {
                                BEST_OF_2_2.add(betamt);
                            }

                        }
                        else
                        {
                            if(!BEST_OF_2_6.contains(betamt))
                            {
                                BEST_OF_2_6.add(betamt);
                            }

                        }
                    }
                    else if(t.getTable_type().contains("BEST_OF_3"))
                    {
                        int betamt = Integer.parseInt(t.getBet());
                        if(t.getMaxplayer().equalsIgnoreCase("2"))
                        {
                            if(!BEST_OF_3_2.contains(betamt))
                            {
                                BEST_OF_3_2.add(betamt);
                            }

                        }
                        else
                        {
                            if(!BEST_OF_3_6.contains(betamt))
                            {
                                BEST_OF_3_6.add(betamt);
                            }

                        }
                    }
                    else if(t.getTable_type().contains("PR"))
                    {
                        float betamt = Float.parseFloat(t.getBet());
                        if(t.getMaxplayer().equalsIgnoreCase("2"))
                        {
                            if(!PR_JOKER_2.contains(betamt))
                            {
                                PR_JOKER_2.add(betamt);
                            }

                        }
                        else
                        {
                            if(!PR_JOKER_6.contains(betamt))
                            {
                                PR_JOKER_6.add(betamt);
                            }

                        }
                    }
                }

            }

            Collections.sort(POOL_101_2_array);
            Collections.sort(POOL_101_6_array);
            Collections.sort(POOL_201_2_array);
            Collections.sort(POOL_201_6_array);

            Collections.sort(BEST_OF_2_2);
            Collections.sort(BEST_OF_2_6);
            Collections.sort(BEST_OF_3_2);
            Collections.sort(BEST_OF_3_6);

            Collections.sort(PR_JOKER_2);
            Collections.sort(PR_JOKER_6);


        }





    }

    private void processToJoinGame(RummyTable t)
    {
        if(t.getJoined_players().contains(RummyPrefManager.getString(RummyTableActivity.this, "username", " ")))
        {
            if (!t.getTable_type().startsWith(RummyUtils.PR)) {
                if(isFoundTable(t))
                {
                    Toast.makeText(RummyTableActivity.this,getResources().getString(R.string.same_table_join_msg),Toast.LENGTH_LONG).show();
                }
                else
                {
                    joinTable(t, "0");
                }
            } else if (isFoundTable(t)) {
                Toast.makeText(RummyTableActivity.this,getResources().getString(R.string.same_table_join_msg),Toast.LENGTH_LONG).show();
                return;
            } else {
                String balance;
                setSelectedTable(t);
                RummyApplication app = (RummyApplication.getInstance());
                DecimalFormat format = new DecimalFormat("0.#");
                if (t.getTable_cost().equalsIgnoreCase("CASH_CASH")) {
                    balance = app.getUserData().getRealChips();
                } else {
                    balance = app.getUserData().getFunChips();
                }
                RummyTLog.e("vikas","balance before selectmin Buy-in="+balance);
                if (Math.round(Float.parseFloat(balance)) >= Math.round(Float.parseFloat(t.getMinimumbuyin()))) {
                    try {
                        showBuyInPopUp(t);
                    }catch (Exception e){
                        RummyTLog.e(TAG+"",e+"");
                    }
                } else if (t.getTable_cost().contains("CASH_CASH")) {
                    String msg = ""+getResources().getString(R.string.rummy_low_balance_first)+" "+getResources().getString(R.string.rupee_symbol)+getRestAmounttoAdd(t.getMinimumbuyin()+"")+" "+getResources().getString(R.string.rummy_low_balance_second);
                    showErrorBalanceBuyChips(RummyTableActivity.this,msg,t.getMinimumbuyin());
                } else {
                    showLowBalanceDialog(RummyTableActivity.this, getResources().getString(R.string.low_balance_free_chip),t.getMinimumbuyin());
                }
            }
        }
        else
        {
            if (!t.getTable_type().startsWith(RummyUtils.PR)) {
                if(isFoundTable(t))
                {
                    Toast.makeText(RummyTableActivity.this,getResources().getString(R.string.same_table_join_msg),Toast.LENGTH_LONG).show();
                }
                else
                {
                    joinTable(t, "0");
                }

            } else if (isFoundTable(t)) {
                // setSelectedTable(t);
                // launchTableActivity();
                Toast.makeText(RummyTableActivity.this,getResources().getString(R.string.same_table_join_msg),Toast.LENGTH_LONG).show();
                return;
            } else {
                String balance;
                setSelectedTable(t);
                RummyApplication app = (RummyApplication.getInstance());
                DecimalFormat format = new DecimalFormat("0.#");
                if (t.getTable_cost().equalsIgnoreCase("CASH_CASH")) {
                    balance = app.getUserData().getRealChips();
                } else {
                    balance = app.getUserData().getFunChips();
                }
                RummyTLog.e("vikas","balance before selectmin Buy-in else part="+balance);
                if (new Float((float) Math.round(Float.parseFloat(balance))).floatValue() >= Float.valueOf(t.getMinimumbuyin()).floatValue()) {
                    try {
                        showBuyInPopUp(t);
                    }catch (Exception e){
                        RummyTLog.e(TAG+"",e+"");
                    }
                } else if (t.getTable_cost().contains("CASH_CASH")) {
                    String msg = ""+getResources().getString(R.string.rummy_low_balance_first)+" "+getResources().getString(R.string.rupee_symbol)+getRestAmounttoAdd(t.getMinimumbuyin()+"")+" "+getResources().getString(R.string.rummy_low_balance_second);
                    showErrorBalanceBuyChips(RummyTableActivity.this,msg,t.getMinimumbuyin());
                } else {

                    showLowBalanceDialog(RummyTableActivity.this, RummyTableActivity.this.getResources().getString(R.string.low_balance_free_chip),t.getMinimumbuyin());
                }
            }
            // openConfirmDialog(t);
        }

    }

    private void showBuyInPopUp(RummyTable table) {
        try {
            String balance;
            RummyApplication app = (RummyApplication.getInstance());
            RummyLoginResponse userData = ((RummyApplication.getInstance())).getUserData();
            int playerBalance;

            final DecimalFormat format = new DecimalFormat("0.#");
            if (table.getTable_cost().equalsIgnoreCase("CASH_CASH")) {
                balance = app.getUserData().getRealChips();
                playerBalance = (int) Float.parseFloat(userData.getRealChips());
            } else {
                balance = app.getUserData().getFunChips();
                playerBalance = (int) Float.parseFloat(userData.getFunChips());
            }
            final Dialog dialog = new Dialog(RummyTableActivity.this,R.style.DialogTheme);
            dialog.requestWindowFeature(1);
            dialog.setContentView(R.layout.rummy_table_activity_buy_in_popup);
            dialog.show();

            LinearLayout ll_main_container_buy_chips = dialog.findViewById(R.id.ll_main_container_buy_chips);

            ll_main_container_buy_chips.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            TextView minBuyTv = (TextView) dialog.findViewById(R.id.min_buy_value_tv);
            TextView maxBuyTv = (TextView) dialog.findViewById(R.id.max_buy_value_tv);
            TextView tv_minBuy_slider = dialog.findViewById(R.id.tv_minbuyin_slider);
            TextView tv_maxBuy_slider = dialog.findViewById(R.id.tv_maxbuyin_slider);



            TextView balanceTv = (TextView) dialog.findViewById(R.id.balance_value_tv);
            final EditText buyInTv = (EditText) dialog.findViewById(R.id.buy_in_value_tv);
            ((TextView) dialog.findViewById(R.id.bet_value_tv)).setText(table.getBet()+"");

            final String maximumBuyIn = table.getMaximumbuyin();
            final int max;
            if(playerBalance<Integer.parseInt(Math.round(Float.parseFloat(maximumBuyIn))+""))
                max = playerBalance;
            else
                max = Integer.parseInt(Math.round(Float.parseFloat(maximumBuyIn))+"");

            final int min = Integer.parseInt(Math.round(Float.parseFloat(table.getMinimumbuyin()))+"");

            boolean decreaseBalance = true;
            if (balance.contains(".")) {
                String subBalance = balance.substring(balance.lastIndexOf(".") + 1);
                if (subBalance != null && subBalance.length() > 0) {
                    decreaseBalance = Integer.parseInt(Math.round(Float.parseFloat(subBalance))+"") > 50;
                }
            }
            final float balanceInt = new Float((float) Math.round(Float.parseFloat(balance))).floatValue();
            RummyTLog.e("vikas","balnce "+balanceInt);
            balanceTv.setText(String.valueOf(format.format((double) balanceInt)));
            final RummyTable table2 = table;
            ((Button) dialog.findViewById(R.id.join_btn)).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    hideAddTableLayout();
                    dialog.dismiss();
                    if (buyInTv.getText() == null || buyInTv.getText().length() <= 0) {
                        showGenericDialog(RummyTableActivity.this,"Please enter minimum amount");
                        return;
                    }
                    float selectedBuyInAmt = Float.valueOf(buyInTv.getText().toString()).floatValue();
                    if (selectedBuyInAmt <= balanceInt || selectedBuyInAmt > Float.valueOf((float) max).floatValue()) {
                        if (selectedBuyInAmt > Float.valueOf((float) max).floatValue()) {
                            //showGenericDialog(RummyTableActivity.this,"You can take only ( " + maximumBuyIn + " ) " + "in to the table");
                            showErrorBuyChipsDialog(context,"You have insufficient balance in your wallet");
                        } else if (selectedBuyInAmt < Float.valueOf((float) min).floatValue()) {
                            showGenericDialog(RummyTableActivity.this,"Please enter minimum amount");
                        } else {

                            sendJoinTableDataToServer(table2, buyInTv.getText().toString());
                        }
                    } else if (table2.getTable_cost().contains("CASH_CASH")) {
                        String msg = ""+getResources().getString(R.string.rummy_low_balance_first)+" "+getResources().getString(R.string.rupee_symbol)+getRestAmounttoAdd(selectedBuyInAmt+"")+" "+getResources().getString(R.string.rummy_low_balance_second);
                        showErrorBalanceBuyChips(RummyTableActivity.this,msg,selectedBuyInAmt+"");
                    } else {
                        showLowBalanceDialog(RummyTableActivity.this, RummyTableActivity.this.getResources().getString(R.string.low_balance_free_chip),selectedBuyInAmt+"");
                    }
                }
            });
            ((Button) dialog.findViewById(R.id.cancel_btn)).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            minBuyTv.setText(table.getMinimumbuyin()+"");
            maxBuyTv.setText(table.getMaximumbuyin()+"");

            tv_minBuy_slider.setText("Min "+table.getMinimumbuyin());
            tv_maxBuy_slider.setText("Max "+((max)));


            SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.seek_bar);
            seekBar.setMax((max - min) / 1);
            seekBar.setProgress(seekBar.getMax());
            if (Float.valueOf((float) max).floatValue() <= balanceInt) {
                buyInTv.setText(String.valueOf(max));
            } else {
                float newBalance = balanceInt;
                if (decreaseBalance) {
                    newBalance = balanceInt - 1.0f;
                }
                buyInTv.setText(String.valueOf(format.format((double) newBalance)));
            }
            final int i = min;
            final float f = balanceInt;
            final EditText editText = buyInTv;
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    String sliderValue = String.valueOf(format.format((double) (i + (progress * 1))));
                    if (Float.valueOf(sliderValue).floatValue() >= f) {
                        sliderValue = String.valueOf(format.format((double) f));
                    }
                    editText.setText(sliderValue);
                }
            });

        }catch (Exception e){
            RummyTLog.e(TAG+"",e+"");
        }
    }

    public void closeSettingsMenu() {
        hideNavigationMenu();
        dismissScoreBoard();
        hideVisibleView();
    }

    private void trackRABEventWE() {
        Map<String, Object> map = new HashMap<>();
        map.put(RummyCommonEventTracker.USER_ID, RummyPrefManager.getString(getBaseContext(), RummyConstants.PLAYER_USER_ID, ""));
        map.put(RummyCommonEventTracker.DEVICE_TYPE, getDeviceType());
        map.put(RummyCommonEventTracker.CLIENT_TYPE, RummyUtils.CLIENT_TYPE);

        Bundle bundle=new Bundle();
        bundle.putString(RummyCommonEventTracker.USER_ID, RummyPrefManager.getString(getBaseContext(), RummyConstants.PLAYER_USER_ID, ""));
        bundle.putString(RummyCommonEventTracker.DEVICE_TYPE, getDeviceType());
        bundle.putString(RummyCommonEventTracker.CLIENT_TYPE, RummyUtils.CLIENT_TYPE);

        RummyCommonEventTracker.trackEvent(RummyCommonEventTracker.REPORT_BUG, map, RummyTableActivity.this,bundle);
    }

    public static RummyTableActivity getInstance() {
        return mTableActivity;
    }

    public void AddAnotherTourney(String tourneyId,String tableId,String gameType)
    {
        this.gameType=gameType;
        this.mTourneyId=tourneyId;

        setUpGameRoom();

    }

    private void hideAddTableLayout()
    {
        hideView(ll_add_new_table_popup_layout);
    }

    public void handleLowBalanceListener(String betAmount)  // type is bet is ponts rummy or not
    {
        mRummyApplication = RummyApplication.getInstance();
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


    public void showErrorBalanceBuyChips(final Context context, String text, final String betAmount)
    {
        String msg1 = "Add"+" "+context.getResources().getString(R.string.rupee_symbol)+getRestAmounttoAdd(betAmount) +" "+"to join this table";
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.rummy_dialog_error_balance_buy_chips);

        RelativeLayout rl_dialog_low_balance_main_container = dialog.findViewById(R.id.rl_dialog_low_balance_main_container);
        TextView tv_msg1 = (TextView)dialog.findViewById(R.id.tv_add_rest_amount_msg);
        TextView label = (TextView) dialog.findViewById(R.id.dialog_msg_tv);
        final Button deposit = (Button) dialog.findViewById(R.id.ok_btn);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);

        label.setText(text);
        tv_msg1.setText(msg1);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        rl_dialog_low_balance_main_container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        deposit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //checkPlayerDeposit(context);
                dialog.dismiss();
                if(RummyApplication.getInstance().getJoinedTableIds().size() == 0)
                {
                    handleLowBalanceListener(betAmount);
                }
                else
                {
                    showGenericDialog(RummyTableActivity.this, getResources().getString(R.string.add_money_msg_when_table_joined));
                }

            }
        });

        dialog.show();
    }

    public void showLowBalanceDialog(Context context, String message, final String betAmount) {
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.rummy_dialog_generic);
        ((TextView) dialog.findViewById(R.id.dialog_msg_tv)).setText(message);
        ((Button) dialog.findViewById(R.id.ok_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                if (!RummyTableActivity.this.mSelectedTable.getTable_cost().contains("CASH_CASH")) {
                    //  RummyLobbyFragmentNew.this.loadChips();
                }
                handleLowBalanceListener(betAmount);
            }
        });
        ((ImageView) dialog.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
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

    private void checkForGameType() {
        if (getIntent() != null && getIntent().hasExtra("gameType"))
            this.gameType = getIntent().getStringExtra("gameType");

        if (getIntent() != null && getIntent().hasExtra("tourneyId"))
            this.mTourneyId = getIntent().getStringExtra("tourneyId");
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

    //psp function - to update seating table text from fragment
    public void updateTableType(String tableId, String tableCost, String betDetails, String betType,String tournament_table) {

        tableIdButton1.setVisibility(View.VISIBLE);
        tableIdButton2.setVisibility(View.VISIBLE);

        List<RummyJoinedTable> joinedTables = this.mRummyApplication.getJoinedTableIds();

        if (joinedTables.size() == 2) {

            String firstTableBtnStr = this.mTable1Btn.getText().toString().replaceAll("\\D+", "");
            String secondTableBtnStr = this.mTable2Btn.getText().toString().replaceAll("\\D+", "");

            if (firstTableBtnStr.equalsIgnoreCase(tableId)) {
                updateTableText(this.game_type_iv1,this.game_type_iv1_free,this.tableIdButton1,tableId, tableCost, betDetails, betType,tournament_table);
            }
            if (secondTableBtnStr.equalsIgnoreCase(tableId)) {
                updateTableText(this.game_type_iv2,this.game_type_iv2_free,this.tableIdButton2,tableId, tableCost, betDetails, betType,tournament_table);
            }

        } else {
            String firstTableBtnStr = this.mTable1Btn.getText().toString().replaceAll("\\D+", "");
            if (firstTableBtnStr.equalsIgnoreCase(tableId)) {
                updateTableText(this.game_type_iv1,this.game_type_iv1_free,this.tableIdButton1,tableId, tableCost, betDetails, betType,tournament_table);
            }
        }

    }

    private void updateTableText(ImageView iv, ImageView iv_free,TextView tv, String tableId, String tableCost, String betDetails, String betType,String tournament_table) {


        if (tableCost.contains("FUN")) {
            iv_free.setVisibility(View.VISIBLE);
            iv.setVisibility(View.GONE);
            iv_free.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rummy_ic_bottombar_funchips));
        } else {
            iv.setVisibility(View.VISIBLE);
            iv_free.setVisibility(View.GONE);
            iv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_freegames_icon_white));
        }

        if (betType.contains("Pool")) {
            tv.setText(" " + betDetails + " " + "Pools");
        } else if (betType.contains("Best")) {
            if (betType.equalsIgnoreCase("Best of 2")) {
                tv.setText(" " + betDetails + " " + "BO2");
            } else if (betType.equalsIgnoreCase("Best of 3")) {
                tv.setText(" " + betDetails + " " + "BO3");
            } else {
                tv.setText(" " + betDetails + " " + betType);
            }
        } else {
            if(tournament_table.equalsIgnoreCase("yes"))
                tv.setText(" " + betDetails + " " + getResources().getString(R.string.bullet_view) + " " + "Tourney");
            else if(betType.contains("PR")) tv.setText(" " + betDetails + " " + "P..");
            else if(betType.contains("Point")) tv.setText(" " + betDetails + " " + "P..");
            else  tv.setText(" " + betDetails + " " + betType);

        }

    }

    // psp function
    public void updateTableTossText(String tableId) {

        List<RummyJoinedTable> joinedTables = this.mRummyApplication.getJoinedTableIds();

        if (joinedTables.size() == 2) {

            String firstTableBtnStr = this.mTable1Btn.getText().toString().replaceAll("\\D+", "");
            String secondTableBtnStr = this.mTable2Btn.getText().toString().replaceAll("\\D+", "");
            if (firstTableBtnStr.equalsIgnoreCase(tableId) && tableId.equalsIgnoreCase(this.mActiveTableId)) {
                if (joinedTables.get(0).getTabelId().length() <= 4) {
                    this.tableIdButton1.setText("#" + joinedTables.get(0).getTabelId());
                } else {
                    this.tableIdButton1.setText("#" + joinedTables.get(0).getTabelId().substring(joinedTables.get(0).getTabelId().length() - 4));
                }
            } else {
                if (joinedTables.get(1).getTabelId().length() <= 4) {
                    this.tableIdButton2.setText("#" + joinedTables.get(1).getTabelId());
                } else {
                    this.tableIdButton2.setText("#" + joinedTables.get(1).getTabelId().substring(joinedTables.get(1).getTabelId().length() - 4));
                }
            }

        } else {
            String firstTableBtnStr = this.mTable1Btn.getText().toString().replaceAll("\\D+", "");
            if (firstTableBtnStr.equalsIgnoreCase(tableId) && tableId.equalsIgnoreCase(this.mActiveTableId)) {
                if (joinedTables.get(0).getTabelId().length() <= 4) {
                    this.tableIdButton1.setText("#" + joinedTables.get(0).getTabelId());
                } else {
                    this.tableIdButton1.setText("#" + joinedTables.get(0).getTabelId().substring(joinedTables.get(0).getTabelId().length() - 4));
                }
            }
        }
    }

    public void clearDiscardList() {
        if(this.discardList != null)this.discardList.clear();
    }

}
