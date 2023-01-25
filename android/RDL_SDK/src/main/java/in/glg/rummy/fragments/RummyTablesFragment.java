package in.glg.rummy.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.github.aakira.expandablelayout.Utils;
import com.google.gson.Gson;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.sebchlan.picassocompat.PicassoBridge;
import com.sebchlan.picassocompat.TransformationCompat;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import org.apache.commons.lang3.text.WordUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.cookie.ClientCookie;
import douglasspgyn.com.github.circularcountdown.CircularCountdown;
import in.glg.rummy.CommonMethods.RummyCommonMethods;
import in.glg.rummy.GameRoom.RummyTableActivity;
import in.glg.rummy.R;
import in.glg.rummy.RummyApplication;
import in.glg.rummy.actionmenu.RummyActionItem;
import in.glg.rummy.actionmenu.RummyQuickAction;
import in.glg.rummy.activities.RummyBaseActivity;
import in.glg.rummy.activities.RummyInstance;
import in.glg.rummy.adapter.RummySplitWinnerAdapter;
import in.glg.rummy.adapter.RummyTourneyResultsAdapter;
import in.glg.rummy.anim.RummyAnimation;
import in.glg.rummy.anim.RummyAnimationListener;
import in.glg.rummy.anim.RummyTransferAnimation;
import in.glg.rummy.api.RummyOnResponseListener;
import in.glg.rummy.api.requests.RummyCardDiscard;
import in.glg.rummy.api.requests.RummyLeaveTableRequest;
import in.glg.rummy.api.requests.RummyRebuyRequest;
import in.glg.rummy.api.requests.RummyTableDetailsRequest;
import in.glg.rummy.api.requests.RummyTournamentsDetailsRequest;
import in.glg.rummy.api.response.RummyCheckMeldResponse;
import in.glg.rummy.api.response.RummyJoinTableResponse;
import in.glg.rummy.api.response.RummyLoginResponse;
import in.glg.rummy.api.response.RummyRebuyResponse;
import in.glg.rummy.api.response.RummyShowEventResponse;
import in.glg.rummy.api.response.RummyTableDeatailResponse;
import in.glg.rummy.api.response.RummyTableExtraResponce;
import in.glg.rummy.api.response.RummyTournamentDetailsResponse;
import in.glg.rummy.dialogs.RummyTableConfirmationDialog;
import in.glg.rummy.engine.RummyGameEngine;
import in.glg.rummy.enums.RummyGameEvent;
import in.glg.rummy.exceptions.RummyGameEngineNotRunning;
import in.glg.rummy.fancycoverflow.RummyFancyCoverFlow;
import in.glg.rummy.models.RummyEngineRequest;
import in.glg.rummy.models.RummyEvent;
import in.glg.rummy.models.RummyGame;
import in.glg.rummy.models.RummyGameDetails;
import in.glg.rummy.models.RummyGamePlayer;
import in.glg.rummy.models.RummyJoinedTable;
import in.glg.rummy.models.RummyLevels;
import in.glg.rummy.models.RummyMeldBox;
import in.glg.rummy.models.RummyMeldCard;
import in.glg.rummy.models.RummyMeldReply;
import in.glg.rummy.models.RummyMeldRequest;
import in.glg.rummy.models.RummyMiddle;
import in.glg.rummy.models.RummyPickDiscard;
import in.glg.rummy.models.RummyPlayingCard;
import in.glg.rummy.models.RummyRejoinRequest;
import in.glg.rummy.models.RummyScoreBoard;
import in.glg.rummy.models.RummySearchTableRequest;
import in.glg.rummy.models.RummySmartCorrectionRequest;
import in.glg.rummy.models.RummySplitRequest;
import in.glg.rummy.models.RummyStack;
import in.glg.rummy.models.RummyTable;
import in.glg.rummy.models.RummyTableCards;
import in.glg.rummy.models.RummyTableDetails;
import in.glg.rummy.models.RummyTourney;
import in.glg.rummy.models.RummyUser;
import in.glg.rummy.service.RummyHeartBeatService;
import in.glg.rummy.timer.RummyCountDownTimer;
import in.glg.rummy.utils.RummyApiCallHelper;
import in.glg.rummy.utils.RummyApiResult;
import in.glg.rummy.utils.RummyCommonEventTracker;
import in.glg.rummy.utils.RummyErrorCodes;
import in.glg.rummy.utils.RummyGameRoomCustomScreenLess700;
import in.glg.rummy.utils.RummyGameRoomCustomScreenMore700;
import in.glg.rummy.utils.RummyPlayerComparator;
import in.glg.rummy.utils.RummyPrefManager;
import in.glg.rummy.utils.RummyPrefManagerTracker;
import in.glg.rummy.utils.RummyConstants;
import in.glg.rummy.utils.RummyRebuyApiHelper;
import in.glg.rummy.utils.RummySlideAnimation;
import in.glg.rummy.utils.RummySoundPoolManager;
import in.glg.rummy.utils.RummyTLog;
import in.glg.rummy.utils.RummyTourneyApiHelper;
import in.glg.rummy.utils.RummyUtils;
import in.glg.rummy.utils.RummyVibrationManager;
import in.glg.rummy.view.RummyCorouselView;
import in.glg.rummy.view.RummyView;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip.Builder;


import static in.glg.rummy.utils.RummyConstants.ACCESS_TOKEN_REST;


public class RummyTablesFragment extends RummyBaseFragment implements OnClickListener, OnItemClickListener {


    public static Activity mActivity = null;

    private String Cust_TAG = "TablesFragment";

    private boolean showPlayersJoinedMessage = false;
    private boolean isMeldRequested = false;
    private static final int ID_DISCARD = 1;
    private static final int ID_GROUP = 2;
    private static final String TAG = RummyGameEngine.class.getName();
    private boolean actionPerformed = false;
    private boolean isCurrentlyMyTurn = false;
    private boolean autoExtraTime = false;
    private String autoPlayCount = "0";
    private boolean isMoveToOtherTable = false;
    private boolean canLeaveTable = true;
    private boolean canShowCardDistributeAnimation = true;
    private ArrayList<RummyPlayingCard> cardStack;
    int cardsSize = 12;
    private RummyTableConfirmationDialog dropDialog;
    int count = 0;
    private RummyOnResponseListener checkMeldListner = new RummyOnResponseListener(RummyCheckMeldResponse.class) {
        public void onResponse(Object response) {
            if (response != null) {
                RummyTablesFragment.this.updateScoreOnPreMeld((RummyCheckMeldResponse) response, mMeldCardsView);
            }
        }
    };
    private RummyOnResponseListener autoPLayListner = new RummyOnResponseListener(RummyJoinTableResponse.class) {
        public void onResponse(Object response) {
            RummyTablesFragment.this.getTableDetailsFromAutoPlayResult();
        }
    };
    private RummyOnResponseListener cardPickListner = new RummyOnResponseListener(RummyJoinTableResponse.class) {
        public void onResponse(Object response) {
            if (response == null) {
            }
        }
    };
    private RummyOnResponseListener declareListner = new RummyOnResponseListener(RummyShowEventResponse.class) {
        public void onResponse(Object response) {
            if (response != null) {
                RummyTablesFragment.this.removeMeldCardsFragment();
            }
        }
    };
    private RummyOnResponseListener disCardListner = new RummyOnResponseListener(RummyJoinTableResponse.class) {
        public void onResponse(Object response) {
        }
    };
    private RummyOnResponseListener dropPlayerListner = new RummyOnResponseListener(RummyJoinTableResponse.class) {
        public void onResponse(Object response) {
            if (response == null) {
                isCurrentlyMyTurn = false;
                RummyTablesFragment.this.mRummyView.removeAllViews();
                RummyTablesFragment.this.resetAllGroupsCountUI();
            }
        }
    };
    private RummyOnResponseListener extraTimeListner = new RummyOnResponseListener(RummyJoinTableResponse.class) {
        public void onResponse(Object response) {
            RummyJoinTableResponse extraTimeResponce = (RummyJoinTableResponse) response;
            int code = Integer.parseInt(extraTimeResponce.getCode());
            if (extraTimeResponce != null && code == RummyErrorCodes.SUCCESS) {
                RummyTablesFragment.this.mAutoExtraTimeEvent = null;
                RummyTablesFragment.this.mUserAutoTimerTv.setText("0");
                RummyTablesFragment.this.mGameShecduleTv.setText(RummyTablesFragment.this.mActivity.getString(R.string.user_chosen_extra_time_msg));
                RummyTablesFragment.this.showView(RummyTablesFragment.this.mGameShecduleTv);
                RummyTablesFragment.this.handleTurnUpdateEvent(Integer.parseInt(RummyTablesFragment.this.userData.getUserId()), extraTimeResponce.getTimeOut());
                RummyTablesFragment.this.showView(RummyTablesFragment.this.mUserAutoChunksLayout);
            } else if (code == RummyErrorCodes.ALREADY_REQUESTED_EXTRATIME) {
                RummyTablesFragment.this.showGenericDialogTF(RummyTablesFragment.this.mActivity, RummyTablesFragment.this.mActivity.getString(R.string.extra_time_err_msg));
            }
        }
    };
    private RummyOnResponseListener rebuyResponseListener = new RummyOnResponseListener(RummyRebuyResponse.class) {
        public void onResponse(Object response) {
            try {
                RummyRebuyResponse rebuyResponse = (RummyRebuyResponse) response;
                if (rebuyResponse != null) {
                    String name;
                    for (int i = 0; i < RummyTablesFragment.this.mPlayerBoxesAll.size(); i++) {
                        name = ((TextView) (RummyTablesFragment.this.mPlayerBoxesAll.get(i).findViewById(R.id.player_name_tv))).getText().toString();
                        if (name.equalsIgnoreCase(RummyTablesFragment.this.userData.getNickName())) {
                            Log.e("TwoTables", String.valueOf(new DecimalFormat("0.#").format(Float.valueOf(rebuyResponse.getTable_ammount())) + "@211"));
//                            Utils.PR_JOKER_POINTS = String.valueOf(new DecimalFormat("0.#").format(Float.valueOf(rebuyResponse.getTable_ammount())));
                            ((TextView) (RummyTablesFragment.this.mPlayerBoxesAll.get(i).findViewById(R.id.player_points_tv))).setText(String.valueOf(new DecimalFormat("0.#").format(Float.valueOf(rebuyResponse.getTable_ammount())))+" \nPTS");
                            ((TextView) (RummyTablesFragment.this.mPlayerBoxesAll.get(i).findViewById(R.id.player_points_tv))).setVisibility(View.VISIBLE);
                            showView(RummyTablesFragment.this.mPlayerBoxesAll.get(i).findViewById(R.id.ll_player_point_round));
                            if(isTourneyTable() && rebuyResponse.getTable_id()!=null && rebuyResponse.getTable_id().equalsIgnoreCase(tableId)) {
                                balance_tourney_tv.setText(String.valueOf(new DecimalFormat("0.#").format(Float.valueOf(rebuyResponse.getTable_ammount()))));
                            }
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Button btn_leave_table_game_result;

    private ArrayList<RummyPlayingCard> faceDownCardList;
    private ArrayList<RummyPlayingCard> faceUpCardList;
    private boolean isCardsDistributing = false;
    private boolean isGameDescheduled = false;
    private boolean isGameResultsShowing = false;
    private boolean isGameStarted = false;
    private boolean isMeldFragmentShowing = false;
    public boolean isPlacedShow = false;
    private boolean isSmartCorrectionShowing = false;
    private boolean isSplitRequested = false;
    private boolean isTossEventRunning = false;
    private boolean is_private = false;
    private boolean isUserDropped = false;
    private boolean isUserPlacedValidShow = false;
    private boolean isWinnerEventExecuted = false;
    private boolean isYourTurn = false;
    private RummyOnResponseListener leaveTableListner = new RummyOnResponseListener(RummyJoinTableResponse.class) {
        public void onResponse(Object response) {
            RummyJoinTableResponse joinTableResponse = (RummyJoinTableResponse) response;
            if (joinTableResponse != null) {
                int code = Integer.parseInt(joinTableResponse.getCode());
                if (code == RummyErrorCodes.SUCCESS || code == RummyErrorCodes.NO_SUCH_TABLE) {
                    Log.e("vikas", "exit request success");
                   /* ((RummyTableActivity) RummyTablesFragment.this.mActivity).resetPlayerIconsOnTableBtn(RummyTablesFragment.this.tableId);
                    if ((RummyApplication.getInstance()).getJoinedTableIds().size() == 0) {
                        RummyTablesFragment.this.mActivity.finish();

                    } else {
                        Log.e("vikas", "calling updateTableFragment funtion from leaveTableListener");
                        ((RummyTableActivity) RummyTablesFragment.this.mActivity).updateTableFragment(RummyTablesFragment.this.tableId);
                    }*/
                    onLeaveTableSuccess();
                }
            }
            RummyTablesFragment.this.dismissLoadingDialog();
            Log.e("vikas", "exit request not success");
        }
    };

    private void onLeaveTableSuccess() {
        ((RummyTableActivity) RummyTablesFragment.this.mActivity).resetPlayerIconsOnTableBtn(RummyTablesFragment.this.tableId);
        if (RummyApplication.getInstance().getJoinedTableIds().size() == 1) {
            List<RummyJoinedTable> joinedTables = RummyTablesFragment.this.mApplication.getJoinedTableIds();
            for (RummyJoinedTable joinedTable : joinedTables) {
                if (RummyTablesFragment.this.tableId.equalsIgnoreCase(joinedTable.getTabelId())) {
                    joinedTables.remove(joinedTable);
                    break;
                }
            }
            if (isTourneyTable()) {
                RummyTablesFragment.this.mActivity.finish();
            }
            if (RummyTablesFragment.this.mLastGamePlayer.isChecked()) {
                RummyTablesFragment.this.mLastGamePlayer.setChecked(false);
                if (isAdded())
                    showLastGamePostPopUp(true);
            } else if (RummyTablesFragment.this.mDropAndGoPlayer.isChecked()) {
                if (isAdded())
                    showDropAndGoPostPopUP(true);
            } else {
                RummyTablesFragment.this.mActivity.finish();
            }


        } else {
            if (isTourneyTable() && isAdded()) {
                ((RummyTableActivity) RummyTablesFragment.this.mActivity).updateTableFragment(RummyTablesFragment.this.tableId);
            } else {
                if (RummyTablesFragment.this.mLastGamePlayer.isChecked()) {
                    RummyTablesFragment.this.mLastGamePlayer.setChecked(false);
                    if (isAdded())
                        showLastGamePostPopUp(false);
                } else if (RummyTablesFragment.this.mDropAndGoPlayer.isChecked()) {
                    if (isAdded())
                        showDropAndGoPostPopUP(false);
                } else {
                    if (isAdded())
                        ((RummyTableActivity) RummyTablesFragment.this.mActivity).updateTableFragment(RummyTablesFragment.this.tableId);
                }
            }

        }
    }




    private RummyApplication mApplication;
    private RummyEvent mAutoExtraTimeEvent = null;
    private TextView mBet;
    private ArrayList<RummyPlayingCard> mCards;
    private ImageView mClosedCard;

   // FirebaseCrashlytics crashlytics;

    private String mDealerId = "";
    private Button mDeclareBtn;
    private RelativeLayout mDialogLayout;
    private Drawable mDiscardImage;
    private RummyActionItem mDiscardView;
    private RummyPlayingCard mDiscardedCard;
    private DrawerLayout mDrawerLayout;
    private Button mDropPlayer;
    private LinearLayout ll_last_game_checkbox, ll_drop_go_checkbox;
    private CheckBox mAutoDropPlayer;

    private CheckBox mLastGamePlayer;
    private CheckBox mDropAndGoPlayer;
    private TextView tv_last_game_lable, tv_drop_and_go_lable;
    private RelativeLayout last_game_layout_root;
    private LinearLayout last_game_layout;

    public static String myTableId = "";
    //   public static ArrayList<String> alAutoDrop = new ArrayList<>();
    //   public static ArrayList<Boolean> alAutoDropBoolean = new ArrayList<>();

    private boolean autoDropPlayerFlag = false;

    private boolean isUserWaiting = false;


    private boolean isSelfUserJoinInMiddleMatch = false;

    private TextView player_join_status_tv;

    private RelativeLayout mDummyLayout;
    private RelativeLayout rl_open_discard;
    private RelativeLayout mDummyOpenDeckLayout;
    private ArrayList<ImageView> mDummyVies = new ArrayList();
    private ImageView mExtraTimeBtn;
    private ImageView mFaceDownCards;
    private View mFifthPlayerAutoChunksLayout;
    private View mFifthPlayerAutoTimerLayout;
    private TextView mFifthPlayerAutoTimerTv;
    private View mFifthPlayerLayout;
    private View mFifthPlayerTimerLayout;
    private TextView mFifthPlayerTimerTv;
    private View mFourthPlayerAutoChunksLayout;
    private View mFourthPlayerAutoTimerLayout;
    private TextView mFourthPlayerAutoTimerTv;
    private View mFourthPlayerLayout;
    private View mFourthPlayerTimerLayout;
    private TextView mFourthPlayerTimerTv;
    private LinearLayout mGameDeckLayout;
    private ImageView mGameLogoIv;
    private HashMap<String, RummyGamePlayer> mGamePlayerMap;
    private View mGameResultsView;
    private CountDownTimer mGameScheduleTimer;
    private TextView mGameShecduleTv;
    private TextView mGameType;
    private ArrayList<ArrayList<RummyPlayingCard>> mGroupList = new ArrayList();
    private RummyActionItem mGroupView;
    private boolean mIamBack = false;
    private boolean mIsMelding = false;
    private RummyPlayingCard mJockerCard;
    private ArrayList<RummyGamePlayer> mJoinedPlayersList;
    private ImageView mJokerCard;
    private Dialog mLeaveDialog;
    private ImageView mLeaveTableBtn;
    private ImageView mLobbyBtn;
    private TextView tvLobby;
    private View mMeldCardsView;
    private ArrayList<ArrayList<RummyPlayingCard>> mMeldGroupList;
    private int mNoOfGamesPlayed = 0;
    private ImageView mOpenCard;
    private ImageView mOpenJokerCard;
    private ImageView mPlayer2Cards;
    private ImageView mPlayer2TossCard;
    private ImageView mPlayer3Cards;
    private ImageView mPlayer3TossCard;
    private ImageView mPlayer4Cards;
    private ImageView mPlayer4TossCard;
    private ImageView mPlayer5Cards;
    private ImageView mPlayer5TossCard;
    private ImageView mPlayer6Cards;
    private ImageView mPlayer6TossCard;
    private TextView mPrizeMoney;
    private TextView mBalanceMoney;
    private RummyQuickAction mQuickAction;
    private CountDownTimer mRejoinTimer;
    private View mReshuffleView;
    private RummyView mRummyView;
    private View mSecondPlayerAutoChunksLayout;
    private View mSecondPlayerAutoTimerLayout;
    private TextView mSecondPlayerAutoTimerTv;
    private View mSecondPlayerLayout;
    private View mSecondPlayerTimerLayout;
    private TextView mSecondPlayerTimerTv;
    private ArrayList<RummyPlayingCard> mSelectedCards;
    private ArrayList<ImageView> mSelectedImgList;
    private ArrayList<LinearLayout> mSelectedLayoutList;
    private ImageView mSettingsBtn;
    private Button mShowBtn;
    private View mSixthPlayerAutoChunksLayout;
    private View mSixthPlayerAutoTimerLayout;
    private TextView mSixthPlayerAutoTimerTv;
    private View mSixthPlayerLayout;
    private View mSixthPlayerTimerLayout;
    private TextView mSixthPlayerTimerTv;
    private View mSmartCorrectionView;
    private FrameLayout mSubFragment;
    private RummyTableDetails mTableDetails;
    private TextView mTableId;
    private View mThirdPlayerAutoChunksLayout;
    private View mThirdPlayerAutoTimerLayout;
    private TextView mThirdPlayerAutoTimerTv;
    private View mThirdPlayerLayout;
    private View mThirdPlayerTimerLayout;
    private TextView mThirdPlayerTimerTv;
    private View mUserAutoChunksLayout;
    private int mUserAutoPlayCount = -1;
    private View mUserAutoTimerRootLayout;
    private TextView mUserAutoTimerTv;
    private RelativeLayout mUserDiscardLaout;
    private View mUserPlayerLayout;
    private View mUserTimerRootLayout;
    private TextView mUserTimerTv;
    private ImageView mUserTossCard;
    private CountDownTimer mWrongMeldTimer;
    private TextView mWrongMeldTv;
    private RummyOnResponseListener meldListner = new RummyOnResponseListener(RummyShowEventResponse.class) {
        public void onResponse(Object response) {
            if (response != null) {
                RummyTablesFragment.this.removeMeldCardsFragment();
                RummyShowEventResponse eventResponse = (RummyShowEventResponse) response;
                if (eventResponse != null && eventResponse.getCode() != null && Integer.parseInt(eventResponse.getCode()) == RummyErrorCodes.SUCCESS && eventResponse.getData() != null && eventResponse.getData().equalsIgnoreCase("meld")) {
                    if (eventResponse.getData().equalsIgnoreCase("meld")) {
                        RummyTablesFragment.this.isMeldRequested = false;
                        RummyTablesFragment.this.clearAnimationData();
                        RummyTablesFragment.this.clearSelectedCards();
                        RummyTablesFragment.this.mRummyView.removeViews();
                        RummyTablesFragment.this.mRummyView.invalidate();
                        RummyTablesFragment.this.resetAllGroupsCountUI();
                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mGameShecduleTv);

                        if (eventResponse.getMeldtimer() != null && eventResponse.getMeldtimer().length() > 0 )
                            RummyTablesFragment.this.startGameScheduleTimer(Integer.parseInt(eventResponse.getMeldtimer()), true);

                        RummyTablesFragment.this.hideView(RummyTablesFragment.this.mDeclareBtn);
                        RummyTablesFragment.this.disableView(RummyTablesFragment.this.sortCards);
                        RummyTablesFragment.this.disableUserOptions();
                    }

                }
            }
        }
    };
    private String meldMsgUdid;
    private String meldTimeOut = null;
    private RummyCountDownTimer meldTimer;
    private int messageVisibleCount = 1;
    private boolean opponentValidShow = false;
    private List<LinearLayout> playerCards;
    private CountDownTimer playerTurnOutTimer;
    private int playerUserId = -1;
    private RummyOnResponseListener rejoinListner = new RummyOnResponseListener(RummyShowEventResponse.class) {
        public void onResponse(Object response) {
            if (response != null) {
                RummyTLog.e(RummyTablesFragment.TAG, "rejoinListner :: " + response);
            }
        }
    };
    private View searchGameView;
    private RummyOnResponseListener searchTableResponse = new RummyOnResponseListener(RummyJoinTableResponse.class) {
        public void onResponse(Object response) {
            RummyTablesFragment.this.dismissDialog();
            if (response != null) {
                RummyJoinTableResponse joinTableResponse = (RummyJoinTableResponse) response;
                int code = Integer.parseInt(joinTableResponse.getCode());
                if (code == RummyErrorCodes.PLAYER_ALREADY_INPLAY || code == RummyErrorCodes.SUCCESS) {
                    RummyTablesFragment.this.resetAllPlayers();
                    RummyTablesFragment.this.resetDealer();
                    RummyJoinedTable joinedTable = new RummyJoinedTable();
                    joinedTable.setTabelId(joinTableResponse.getTableId());
                    (RummyApplication.getInstance()).setJoinedTableIds(joinedTable);
                    RummyTablesFragment.this.removeWinnerDialog();
                    RummyTablesFragment.this.removeGameResultFragment();
                    ((RummyTableActivity) RummyTablesFragment.this.mActivity).updateFragment(RummyTablesFragment.this.tableId, joinTableResponse.getTableId(), null,"");
                } else if (code == RummyErrorCodes.LOW_BALANCE) {
                    RummyTablesFragment.this.showGenericDialogTF(RummyTablesFragment.this.mActivity, RummyTablesFragment.this.mActivity.getResources().getString(R.string.low_balance_free_chip));
                } else if (code == RummyErrorCodes.TABLE_FULL) {
                    RummyTablesFragment.this.showGenericDialogTF(RummyTablesFragment.this.mActivity, "This table is full");
                }
            }
        }
    };
    private Dialog showDialog;
    private RummyOnResponseListener showEventListner = new RummyOnResponseListener(RummyShowEventResponse.class) {
        public void onResponse(Object response) {
            if (response == null) {
            }
        }
    };
    private SimpleTooltip simpleTooltip;
    private ArrayList<RummyPlayingCard> slotCards;
    private RummyOnResponseListener slotEventListner = new RummyOnResponseListener(RummyShowEventResponse.class) {
        public void onResponse(Object response) {
            if (response != null) {
                RummyTablesFragment.this.removeMeldCardsFragment();
            }
        }
    };
    private Button sortCards;
    private RummyOnResponseListener splitListner = new RummyOnResponseListener(RummyShowEventResponse.class) {
        public void onResponse(Object response) {
            if (response == null) {
            }
        }
    };
    private View splitRejectedView;
    private RummyOnResponseListener tableDetailsListner = new RummyOnResponseListener(RummyTableDeatailResponse.class) {
        public void onResponse(Object response) {
            RummyTableDeatailResponse tableDeatailResponse = (RummyTableDeatailResponse) response;
            if (tableDeatailResponse != null && tableDeatailResponse.getTableId() != null && tableDeatailResponse.getTableId().equalsIgnoreCase(RummyTablesFragment.this.tableId)) {
                RummyTablesFragment.this.strIsTourneyTable = tableDeatailResponse.getTournamentTable();//12345
                RummyTablesFragment.this.handleGetTableDetailsEvent(tableDeatailResponse.getTableDeatils(), tableDeatailResponse.getTimestamp());
            }
        }
    };
    private RummyOnResponseListener tableExtraLisner = new RummyOnResponseListener(RummyTableExtraResponce.class) {
        public void onResponse(Object response) {
            RummyTableExtraResponce tableExtraResponce = (RummyTableExtraResponce) response;
            if (tableExtraResponce != null && tableExtraResponce.getTableId() != null && tableExtraResponce.getTableId().equalsIgnoreCase(RummyTablesFragment.this.tableId)) {
                List<RummyPickDiscard> pickDsList = tableExtraResponce.getPickDiscardsList();
                if (pickDsList != null && pickDsList.size() > 0) {
                    isCardPicked = true;
                    Log.e(TAG, "CARD PICKED");
                } else
                    Log.e(TAG, "CARD NOT PICKED");

                if (RummyUtils.SHOW_EVENT != null) {
                    Log.w(TAG, "SHOW REQUEST: " + RummyUtils.SHOW_EVENT.getEventName());
                   // doShowTemp(Utils.SHOW_EVENT);
                } else
                    Log.w(TAG, "SHOW REQUEST IS NULL");

                if (RummyUtils.MELD_REQUEST != null) {
                    Log.w(TAG, "MELD REQUEST: " + RummyUtils.MELD_REQUEST.getEventName());
                    doMeldTemp(RummyUtils.MELD_REQUEST);
                } else
                    Log.w(TAG, "MELD REQUEST IS NULL");
                
                getTournamentDetails();

                RummyEvent event;
                Iterator it;
                RummyPickDiscard pickDiscard;
                RummyScoreBoard scoreBoard = tableExtraResponce.getScoreBoard();
                RummyEvent gameResultEvent = tableExtraResponce.getEvent();
                if (scoreBoard != null) {
                    List<RummyGame> gameList = scoreBoard.getGame();
                    if (gameList != null && gameList.size() > 0) {
                      if (!RummyTablesFragment.this.strIsTourneyTable.equalsIgnoreCase("yes") &&
                              tableExtraResponce.getTableId()!=null && tableExtraResponce.getTableId().equalsIgnoreCase(tableId)) {
                        ((RummyTableActivity) RummyTablesFragment.this.mActivity).clearScoreBoard();
                        }
                        for (RummyGame game : gameList) {
                            List<RummyUser> users = game.getGamePlayer();
                            event = new RummyEvent();
                            event.setGameId(game.getGameId());
                            event.setTableId(tableExtraResponce.getTableId());
                            List<RummyGamePlayer> gamePlayerList = new ArrayList();
                            if (users == null || users.size() <= 0) {
                                RummyGamePlayer player = new RummyGamePlayer();
                                player.setUser_id(RummyTablesFragment.this.userData.getUserId());
                                player.setNick_name(game.getNickName());
                                player.setPoints(game.getPoints());
                                player.setScore(game.getScore());
                                player.setTotalScore(game.getScore());
                                player.setResult(game.getResult());
                                gamePlayerList.add(player);
                            } else {
                                for (RummyUser user : users) {
                                    RummyGamePlayer gamePlayer = new RummyGamePlayer();
                                    gamePlayer.setUser_id(user.getUser_id());
                                    gamePlayer.setNick_name(user.getNick_name());
                                    gamePlayer.setScore(user.getScore());
                                    gamePlayer.setTotalScore(user.getTableScore());
                                    gamePlayerList.add(gamePlayer);
                                }
                            }
                            if (gamePlayerList.size() > 0) {
                                event.setPlayer(gamePlayerList);
                                ((RummyTableActivity) RummyTablesFragment.this.mActivity).setGameResultEvents(event);
                            }
                        }
                    }
                }
                List<RummyPickDiscard> pickDiscardList = tableExtraResponce.getPickDiscardsList();
                if (pickDiscardList != null && pickDiscardList.size() > 0) {
                    for (RummyPickDiscard pickDiscard2 : pickDiscardList) {
                        if (pickDiscard2.getType().equalsIgnoreCase(ClientCookie.DISCARD_ATTR)) {
                            RummyTablesFragment.this.turnCount = RummyTablesFragment.this.turnCount + 1;
                            event = new RummyEvent();
                            event.setTableId(tableExtraResponce.getTableId());
                            event.setFace(pickDiscard2.getFace());
                            event.setSuit(pickDiscard2.getSuit());
                            event.setNickName(pickDiscard2.getNickName());
                            ((RummyTableActivity) RummyTablesFragment.this.mActivity).addDiscardToPlayer(event);
                        }
                    }
                }
                ArrayList<RummyPickDiscard> autoDiscardedList = new ArrayList();
                if (pickDiscardList != null && pickDiscardList.size() > 0) {
                    /*for (RummyPickDiscard pickDiscard22 : pickDiscardList) {
                        if (pickDiscard22.getType().equalsIgnoreCase(ClientCookie.DISCARD_ATTR) && pickDiscard22.getAutoPlay().equalsIgnoreCase("true") && pickDiscard22.getUserId().equalsIgnoreCase(RummyTablesFragment.this.userData.getUserId())) {
                            autoDiscardedList.add(pickDiscard22);
                        }
                    }*/


                    boolean flag =false;
                    int counter = 0;  /// this is using for autostatus false detecting
                    for(int j=pickDiscardList.size()-1;j>=0;j--)
                    {
                        RummyPickDiscard pickDiscard22 = (RummyPickDiscard) pickDiscardList.get(j);

                        if(pickDiscard22.getAutoPlay().equalsIgnoreCase("false") && pickDiscard22.getUserId().equalsIgnoreCase(RummyTablesFragment.this.userData.getUserId()))
                        {
                            counter =0;
                            break;
                        }
                        else if(pickDiscard22.getType().equalsIgnoreCase(ClientCookie.DISCARD_ATTR) && RummyPrefManager.getString(RummyTablesFragment.this.mActivity,RummyConstants.PREFS_Last_Auto_discarded_card,"").equalsIgnoreCase(pickDiscard22.getFace()+"_"+pickDiscard22.getSuit()+"_"+tableExtraResponce.getTableId()))
                        {
                            counter =0;
                            break;
                        }
                        else if (pickDiscard22.getType().equalsIgnoreCase(ClientCookie.DISCARD_ATTR) && pickDiscard22.getAutoPlay().equalsIgnoreCase("true") && pickDiscard22.getUserId().equalsIgnoreCase(RummyTablesFragment.this.userData.getUserId())) {
                            autoDiscardedList.add(pickDiscard22);
                            if(!flag)
                            {
                                RummyUtils.temp_last_auto_drop_card = pickDiscard22.getFace()+"_"+pickDiscard22.getSuit()+"_"+tableExtraResponce.getTableId();
                            }
                            flag= true;
                            counter =0;
                        }

                    }



                    RummyPickDiscard pickDiscard22;
                    if (autoDiscardedList.size() > 0) {
                        pickDiscard22 = (RummyPickDiscard) autoDiscardedList.get(autoDiscardedList.size() - 1);
                        RummyPlayingCard autoDiscardedCard = new RummyPlayingCard();
                        autoDiscardedCard.setFace(pickDiscard22.getFace());
                        autoDiscardedCard.setSuit(pickDiscard22.getSuit());
                        RummyPlayingCard lastAutoPlayDiscardedCard = autoDiscardedCard;
                        if (!(RummyTablesFragment.this.slotCards == null || RummyTablesFragment.this.cardStack == null || RummyTablesFragment.this.slotCards.size() <= RummyTablesFragment.this.cardStack.size())) {
                            String lastCardFace = lastAutoPlayDiscardedCard.getFace();
                            String lastCardSuit = lastAutoPlayDiscardedCard.getSuit();
                            it = RummyTablesFragment.this.slotCards.iterator();
                            while (it.hasNext()) {
                                RummyPlayingCard card = (RummyPlayingCard) it.next();
                                String cardFace = card.getFace();
                                String cardSuit = card.getSuit();
                                if (lastCardFace.equalsIgnoreCase(cardFace) && lastCardSuit.equalsIgnoreCase(cardSuit)) {
                                    RummyTablesFragment.this.slotCards.remove(card);
                                    break;
                                }
                            }
                            RummyTablesFragment.this.setCardsOnIamBack(RummyTablesFragment.this.slotCards);
                        }
                    }
                    if (RummyTablesFragment.this.mUserAutoPlayCount != 0) {
                        RummyTablesFragment.this.mUserAutoPlayCount = RummyTablesFragment.this.mUserAutoPlayCount - 1;
                        if (autoDiscardedList.size() > 0 && RummyTablesFragment.this.mUserAutoPlayCount > -1) {
                            int startIndex;
                            if (RummyTablesFragment.this.mUserAutoPlayCount >= autoDiscardedList.size()) {
                                startIndex = 0;
                            } else {
                                startIndex = autoDiscardedList.size() - RummyTablesFragment.this.mUserAutoPlayCount;
                                if (startIndex == autoDiscardedList.size()) {
                                    startIndex = autoDiscardedList.size() - 1;
                                }
                            }


                            for (int i = startIndex; i < autoDiscardedList.size() - 1; i++) {
                                pickDiscard22 = (RummyPickDiscard) autoDiscardedList.get(i);

                                event = new RummyEvent();
                                event.setFace(pickDiscard22.getFace());
                                event.setSuit(pickDiscard22.getSuit());
                                event.setAutoPlay(pickDiscard22.getAutoPlay());
                                event.setUserId(Integer.parseInt(pickDiscard22.getUserId()));
                                event.setNickName(pickDiscard22.getNickName());
                                event.setTableId(tableExtraResponce.getTableId());
                                RummyPlayingCard discardCard = new RummyPlayingCard();
                                discardCard.setFace(event.getFace());
                                discardCard.setSuit(event.getSuit());
                                if(RummyTablesFragment.this.mMeldCardsView != null && RummyTablesFragment.this.mMeldCardsView.getVisibility() != View.VISIBLE)
                                {
                                    RummyTablesFragment.this.showAutoDiscardedCards(event, discardCard);
                                }

                            }
                        }
                    } else {
                        return;
                    }
                }
                if (gameResultEvent != null && gameResultEvent.getEventName().equalsIgnoreCase("GAME_RESULT") &&
                        gameResultEvent.getTableId()!=null && gameResultEvent.getTableId().equalsIgnoreCase(tableId)) {
                   if (RummyTablesFragment.this.strIsTourneyTable != null && RummyTablesFragment.this.strIsTourneyTable.equalsIgnoreCase("yes")) {
                        ((RummyTableActivity) RummyTablesFragment.this.mActivity).updateLastHandEvent("TOURNEY_TOURNEY", gameResultEvent);
                    } else {
                        ((RummyTableActivity) RummyTablesFragment.this.mActivity).updateLastHandEvent(gameResultEvent.getTableId(), gameResultEvent);
                    }

                    ((RummyTableActivity) RummyTablesFragment.this.mActivity).updateLastHandEvent(gameResultEvent.getTableId(), gameResultEvent);
                }


            }
        }
    };

    ////pure impure ui

    LinearLayout group_1,group_2,group_3,group_4,group_5,group_6,group_7,group_8;
    RelativeLayout rl_groups_container;
    private String reBuyInOrderId;
    private String reBuyInAmount;
    private String reBuyInLevel;
    private String tableId;
    private int turnCount = 0;
    private RummyLoginResponse userData;
    private boolean userNotDeclaredCards;
    private String winnerId = "";
    private View winnerView;
    private RummyPlayingCard tempDiscardedCard;
    private String discardedCardChildTag = "";
    private String mGameId = "";
    private LinearLayout levelTimerLayout;
    private RelativeLayout normal_game_bar;
    private TextView levelTimerValue, level_number_tv;
    private ImageView expandTourneyInfo, collapseTourneyInfo;
    private LinearLayout tourney_expanded_layout;
    private RelativeLayout tourneyBar;
    private int tourneyInfoMaxHeight;
    private String mTourneyId = "";
    private RummyTournamentDetailsResponse mTourneyDetailsResponse;
    private List<RummyLevels> mLevels;
    private CountDownTimer levelTimer;
    private CountDownTimer buyInTimerTourney;
    private String strIsTourneyTable = "";
    private RummyEvent mPlayersRank;
    private List<RummyGamePlayer> mPlayersList = new ArrayList<>();
    private HashMap<String, View> mPlayerBoxesForRanks = new HashMap<>();
    private ArrayList<View> mPlayerBoxesAll = new ArrayList<>();
    private TextView tourney_type_tv, entry_tourney_tv, bet_tourney_tv, rebuy_tourney_tv, rank_tourney_tv, balance_tourney_tv, tourney_prize_tv, game_level_tv;
    private TextView tid_tourney_tv, gameid_tourney_tv;
    private boolean isTourneyEnd;
    private Dialog disqualifyDialog;
    private boolean isTourneyBarVisible = false;
    private TextView game_id_tb;
    private static boolean isCardPicked = false;

    String betAmount = "0", tableType = "", table_cost_type = "";

    private LinearLayout player_2_autoplay_box;
    private LinearLayout player_3_autoplay_box;
    private LinearLayout player_4_autoplay_box;
    private LinearLayout player_5_autoplay_box;
    private LinearLayout player_6_autoplay_box;

    private ImageView player_1_autoplay_icon,player_2_autoplay_icon,player_3_autoplay_icon,player_4_autoplay_icon,player_5_autoplay_icon,player_6_autoplay_icon;

    private TextView autoplay_count_player_2;
    private TextView autoplay_count_player_3;
    private TextView autoplay_count_player_4;
    private TextView autoplay_count_player_5;
    private TextView autoplay_count_player_6;

    private LinearLayout players_join_status_layout;

    private TextView game_start_time_tv;

    private ImageView iv_balance_icon, iv_bet_icon;

    private CircularCountdown circularCountdown_player1,circularCountdown_player2,circularCountdown_player3,circularCountdown_player4,circularCountdown_player5,circularCountdown_player6;

    private CircularProgressBar circularProgressBar_player1,circularProgressBar_player2,circularProgressBar_player3,circularProgressBar_player4,circularProgressBar_player5,circularProgressBar_player6;

    /*
     * Added after dynamic layout
     */

    private LinearLayout game_room_top_bar,ll_tourney_top_bar;
    private ImageView iv_ruppe_icon_top_bar_game_room,iv_prize_icon_top_bar_game_room;
    private LinearLayout ll_bottom_bar;
    private RelativeLayout rl_close_deck_main_container,rl_close_card_container,rl_open_deck_container;
    private ImageView iv_rdl_logo;
    private ViewGroup ll_rummy_view_container;
    private ViewGroup clResumeGame;
    private View resumeShadow;
    private AppCompatTextView tvResume;
    private AppCompatTextView tvResumeTitle;
    private View  player_4_rank_layout,player_5_rank_layout,player_6_rank_layout,player_3_rank_layout,player_2_rank_layout,player_1_rank_layout;

    private LinearLayout player_2_timer_root_layout,player_3_timer_root_layout,player_4_timer_root_layout,player_5_timer_root_layout,player_6_timer_root_layout,player_1_timer_root_layout;

    LinearLayout player2Layout,player3Layout,player4Layout,player5Layout,player6Layout;


    private TextView tid_tourney_label_tv,gameid_tourney_label_tv,tv_more_info_tourney;

    private LinearLayout ll_lavel_timer_time,ll_lavel_timer_level;
    private TextView levelTimerValueLebel,level_number_tv_label;

    private LinearLayout small_round_box_player_1,small_round_box_player_2,small_round_box_player_3,small_round_box_player_4,small_round_box_player_5,small_round_box_player_6;

    private RelativeLayout player_1_root_layout,player_2_root_layout,player_3_root_layout,player_4_root_layout,player_5_root_layout,player_6_root_layout;

    private int[] avatars;

    private LinearLayout bottom_actions, secure_bottom_view;
    private RelativeLayout private_club_layout;
    private View resultPrivateBadge;
    /*
     *
     */

    private RummyTournamentDetailsResponse mLevelsResponse;
    private RummyOnResponseListener tournamentsDetailsListener = new RummyOnResponseListener(RummyTournamentDetailsResponse.class) {
        @Override
        public void onResponse(Object response) {
            if (response != null) {
                RummyTablesFragment.this.mTourneyDetailsResponse = (RummyTournamentDetailsResponse) response;

                if (RummyTablesFragment.this.mTourneyDetailsResponse.getCode().equalsIgnoreCase("200")) {
                    if (RummyTablesFragment.this.mTourneyDetailsResponse.getData().equalsIgnoreCase("get_tournament_details")) {
                        RummyTablesFragment.this.mLevels = ((RummyTournamentDetailsResponse) response).getLevels();
                        if (!RummyTablesFragment.this.isMeldRequested) {
                            getLevelTimer();
                        }

                    }
                } else {
                    RummyTLog.e("vikas", RummyTablesFragment.this.mTourneyDetailsResponse.getCode() + " : tournamentsDetailsListener error");
                }
            }
        }
    };

    private RummyOnResponseListener levelsTimerListener = new RummyOnResponseListener(RummyTournamentDetailsResponse.class) {
        @Override
        public void onResponse(Object response) {
            if (response != null) {
                RummyTablesFragment.this.mLevelsResponse = (RummyTournamentDetailsResponse) response;
                if (RummyTablesFragment.this.mLevelsResponse.getCode().equalsIgnoreCase("200")) {
                    if (RummyTablesFragment.this.mLevelsResponse.getData().equalsIgnoreCase("get_level_timer")) {
                        setLevelTimer();
                    }
                } else {
                    Log.d(TAG, RummyTablesFragment.this.mLevelsResponse.getCode() + " : tournamentsDetailsListener");
                }
            }
        }
    };

    private RummyOnResponseListener rebuyinListener = new RummyOnResponseListener(RummyTournamentDetailsResponse.class) {
        @Override
        public void onResponse(Object response) {
            if (response != null) {
                RummyTournamentDetailsResponse rebuyinResponse = (RummyTournamentDetailsResponse) response;
                if (rebuyinResponse.getData().equalsIgnoreCase("tournament_rebuyin")) {
                    if (rebuyinResponse.getCode().equalsIgnoreCase("200")) {
                    
                        checkRebuyIn();}
                       
                        /* RummyTourneyApiHelper.rebuyInSuccess(RummyTablesFragment.this.mTourneyId,
                                RummyTablesFragment.this.reBuyInLevel, reBuyInAmount, reBuyInOrderId, apiResult -> {
                                    Log.d(TAG, "rebuyChips response: success +"+apiResult.isSuccess());
                                    if(apiResult.isSuccess()) {
                                        Log.d(TAG, "rebuyChipSuccesss response: +" + apiResult.getResult().toString());
                                    }else {
                                        Log.d(TAG, "rebuyChipsSuccess response: +" + apiResult.getErrorMessage());
                                    }
                                });
                    }else{
                        RummyTourneyApiHelper.rebuyInCancel(RummyTablesFragment.this.mTourneyId,
                            RummyTablesFragment.this.reBuyInLevel, reBuyInAmount, reBuyInOrderId, apiResult -> {
                                Log.d(TAG, "rebuyChips response: success +"+apiResult.isSuccess());
                                if(apiResult.isSuccess()) {
                                    Log.d(TAG, "rebuyChipSuccesss response: +" + apiResult.getResult().toString());
                                }else {
                                    Log.d(TAG, "rebuyChipsSuccess response: +" + apiResult.getErrorMessage());
                                }
                            });
                        
                    }*/
                } else {
                    Log.d(TAG, rebuyinResponse.getCode() + " : tournamentsDetailsListener");
                }
            }
        }
    };

    class C17265 extends AsyncTask<Void, Void, Void> {
        C17265() {
        }

        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(RummyHeartBeatService.NOTIFY_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            RummyTablesFragment.this.animateDispatchCards();
        }
    }

    class C17276 implements DrawerLayout.DrawerListener {
        C17276() {
        }

        public void onDrawerSlide(View drawerView, float slideOffset) {
        }

        public void onDrawerOpened(View drawerView) {
            RummyTablesFragment.this.dismissQuickMenu();
        }

        public void onDrawerClosed(View drawerView) {
            try {
                if (!((RummyTableActivity) RummyTablesFragment.this.mActivity).isSlideMenuVisible() && RummyTablesFragment.this.mGameResultsView.getVisibility() != View.VISIBLE) {
                    if (RummyTablesFragment.this.getTag() != null) {
                        RummyTablesFragment.this.showQuickAction(RummyTablesFragment.this.getTag());
                    }

                }
            } catch (Exception e) {
                Log.e("onDrawerClosed", e + "");
            }
        }

        public void onDrawerStateChanged(int newState) {
        }
    }

    public class FaceComparator implements Comparator<RummyPlayingCard> {
        public int compare(RummyPlayingCard p1, RummyPlayingCard p2) {
            int face1 = Integer.valueOf(p1.getFace()).intValue();
            int face2 = Integer.valueOf(p2.getFace()).intValue();
            if (face1 == face2) {
                return 0;
            }
            if (face1 > face2) {
                return 1;
            }
            return -1;
        }
    }

    public boolean isUserNotDeclaredCards() {
        return this.userNotDeclaredCards;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.rummy_fragment_game, container, false);
        try {
            init();
            getUserData();
            setIdsToViews(v);
/*
            try {
                if(!isTablet(getActivity()))
                setScreenSize();
            } catch (Exception e) {
                e.printStackTrace();
                setScreenSize();
            }*/
            //   setCardsSize();
            setScreenSize();

            setBuildVersion();
            initializePlayerBoxesInList();
            handleBackButton(v);
            setListnersToViews();
            initGameRoom();
            checkGameType();

            blockUserView();

            if (((RummyTableActivity) this.mActivity).isIamBackShowing()) {
                boolean tableDetailsEventFound = false;
                RummyEvent tableDetailsEvent = null;

                Iterator it = RummyUtils.tableDetailsList.iterator();
                while (it.hasNext()) {
                    RummyEvent event = (RummyEvent) it.next();
                    if (event.getEventName().equalsIgnoreCase("get_table_details")) {
                        RummyTLog.e("vikas", "calling get table details event");
                        RummyTableDetails details = event.getTableDetails();
                        if (details.getTournament_table() != null && details.getTournament_table().equalsIgnoreCase("yes")) {

                            if (this.tableId != null && details.getTableId() != null && this.tableId.equalsIgnoreCase(details.getTableId())) {
                                RummyTLog.e("vikas", "tourney id by schedulename=" + details.getSchedulename());
                                this.strIsTourneyTable = "yes";
                                this.mTourneyId = details.getSchedulename();
                                checkTournament();
                            }

                        }


                        if (!(details == null || details.getTableId() == null || !details.getTableId().equalsIgnoreCase(this.tableId))) {
                            tableDetailsEventFound = true;
                            tableDetailsEvent = event;
                        }

                        if (details.getGameDetails() != null) {
                            this.mGameId = details.getGameDetails().getGameId();
                            this.gameid_tourney_tv.setText(this.mGameId);
                        }
                    } else if (event.getEventName().equalsIgnoreCase("players_rank")) {
                        if (event.getTournamentId().equalsIgnoreCase(this.mTourneyId)) {
                        this.strIsTourneyTable = "yes";
                        this.mTourneyId = event.getTournamentId();
                        this.mPlayersRank = event;
                        this.mPlayersList = event.getPlayers();
                        this.updatePlayersRank();
                         } //12345
                    }
                }
                if (!tableDetailsEventFound) {
                    getTableDetails();
                } else if (!(tableDetailsEvent == null || tableDetailsEvent.getTableDetails() == null)) {
                    handleGetTableDetailsEvent(tableDetailsEvent.getTableDetails(), tableDetailsEvent.getTimestamp());
                }

                // this.checkTournament();
            } else {
                sendAutoPlayStatus();
            }
            setUserOptions(false);
            loadDummyCardsView();
            setTableButtonsUI();
            checkTourneyBalance();

            updateTableTypeView(); // psp
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e + "");
        }
        return v;
    }

    private void setBuildVersion() {
        TextView versionNumber_ms = (TextView) this.mMeldCardsView.findViewById(R.id.versionNumber_ms);
        TextView versionNumber_gs = (TextView) this.mGameResultsView.findViewById(R.id.versionNumber_gs);
        versionNumber_ms.setText("" + RummyUtils.getVersionNumber(this.getContext()));
        versionNumber_gs.setText("" + RummyUtils.getVersionNumber(this.getContext()));
    }

    private void initializePlayerBoxesInList() {
        this.mPlayerBoxesAll.clear();

        this.mPlayerBoxesAll.add(mUserPlayerLayout);
        this.mPlayerBoxesAll.add(mSecondPlayerLayout);
        this.mPlayerBoxesAll.add(mThirdPlayerLayout);
        this.mPlayerBoxesAll.add(mFourthPlayerLayout);
        this.mPlayerBoxesAll.add(mFifthPlayerLayout);
        this.mPlayerBoxesAll.add(mSixthPlayerLayout);
    }

    public void setTableButtonsUI() {
        RummyTableActivity tableActivity = (RummyTableActivity) this.mActivity;
        boolean isIamBackShowing = tableActivity.isIamBackShowing();
        if (this.isGameResultsShowing || isIamBackShowing || this.isMeldFragmentShowing || this.isSmartCorrectionShowing) {
            tableActivity.hideGameTablesLayout(this.tableId);
        } else {
            tableActivity.showGameTablesLayout(this.tableId);
        }
    }

    public void hideQuickAction() {
        dismissQuickMenu();
        dismissToolTipView();
    }

    public void arrangeSelectedCards(String id) {
      /*  if (id.equalsIgnoreCase(this.tableId)) {
            ArrayList<PlayingCard> selectedCards = new ArrayList();
            selectedCards.addAll(this.mSelectedCards);
            this.mSelectedCards.clear();
            Iterator it = selectedCards.iterator();
            while (it.hasNext()) {
                PlayingCard card = (PlayingCard) it.next();
                getLastSelectedCardView(card.getSuit() + card.getFace() + "-" + card.getIndex()).performClick();
            }
        }*/

        if (id.equalsIgnoreCase(this.tableId)) {
            //ArrayList<PlayingCard> selectedCards = new ArrayList();
            //selectedCards.addAll(this.mSelectedCards);
            // this.mSelectedCards.clear();
            if(this.mSelectedCards != null && this.mSelectedCards.size() == 1)
            {
                Iterator it = this.mSelectedCards.iterator();
                while (it.hasNext()) {
                    RummyPlayingCard card = (RummyPlayingCard) it.next();
                    if(getLastSelectedCardView(card.getSuit() + card.getFace() + "-" + card.getIndex()) != null)
                    {
                        getLastSelectedCardView(card.getSuit() + card.getFace() + "-" + card.getIndex()).performClick();
                    }

                }
            }
            else if(this.mSelectedCards != null && this.mSelectedCards.size() > 1)
            {
               // this.mSelectedCards.clear();
                clearSelectedCards();
            }

        }
    }

    public void showQuickAction(String id) {
        try {
            dismissQuickMenu();
            if (this.mGameResultsView.getVisibility() == View.VISIBLE || !id.equalsIgnoreCase(this.tableId)) {
                return;
            }
            if (this.mSelectedCards.size() == 0) {
                dismissQuickMenu();
                return;
            }
            int numberOfCards = getTotalCards();
            if (this.mSelectedCards.size() > 0) {
                RummyPlayingCard lastCard = (RummyPlayingCard) this.mSelectedCards.get(this.mSelectedCards.size() - 1);
                Iterator it = this.mSelectedImgList.iterator();
                while (it.hasNext()) {
                    ImageView img = (ImageView) it.next();
                    Object[] objArr = new Object[ID_DISCARD];
                    objArr[0] = img.getTag().toString();
                    String imgTagValue = String.format("%s", objArr);
                    String lastCardValue = String.format("%s%s-%s", new Object[]{lastCard.getSuit(), lastCard.getFace(), Integer.valueOf(lastCard.getIndex())});
                    if (imgTagValue.equalsIgnoreCase(lastCardValue)) {
                        this.mQuickAction = new RummyQuickAction(getContext(), ID_DISCARD);
                        if (this.mSelectedCards.size() > ID_DISCARD) {
                            this.mGroupView = new RummyActionItem((int) ID_GROUP, "GROUP");
                            this.mQuickAction.addActionItem(this.mGroupView);
                            this.mQuickAction.show(getLastSelectedView(img));
                            if (this.mIsMelding) {
                                enableView(this.mShowBtn);
                            } else {
                                disableView(this.mShowBtn);
                            }
                        } else if (numberOfCards > 13) {
                            this.mDiscardView = new RummyActionItem((int) ID_DISCARD, "DISCARD");
                            this.mDiscardView.setTag(lastCardValue);
                            this.mQuickAction.addActionItem(this.mDiscardView);
                            LinearLayout lastSelectedLayout = getLastSelectedView(img);
                            animateCard(lastSelectedLayout);
                            this.mQuickAction.show(lastSelectedLayout);
                            enableView(this.mShowBtn);
                        }
                        this.mQuickAction.setOnActionItemClickListener(new RummyQuickAction.OnActionItemClickListener() {
                            public void onItemClick(RummyQuickAction source, int pos, int actionId) {
                                if (actionId == RummyTablesFragment.ID_DISCARD) {
                                    RummyTablesFragment.this.mSelectedCards.remove(RummyTablesFragment.this.mSelectedCards.get(0));
                                    RummyTablesFragment.this.removeCardAndArrangeCards(RummyTablesFragment.this.mDiscardView);
                                    RummyTablesFragment.this.mQuickAction.dismiss();
                                } else if (actionId == RummyTablesFragment.ID_GROUP) {
                                    RummyTablesFragment.this.groupCards();
                                } else {
                                    RummyTablesFragment.this.meldCards();
                                }
                            }
                        });
                        return;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("TablesFragment@926", e + "");
        }
    }

    private LinearLayout getLastSelectedCardView(String img) {
        RelativeLayout mainHolder = this.mRummyView.getRootView();
        if (mainHolder == null) {
            return null;
        }
        for (int i = 0; i < mainHolder.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) mainHolder.getChildAt(i);
            if (linearLayout != null && linearLayout.getTag() != null && img.equalsIgnoreCase(linearLayout.getTag().toString())) {
                return linearLayout;
            }
        }
        return null;
    }

    private LinearLayout getLastSelectedView(ImageView img) {
        Iterator it = this.mSelectedLayoutList.iterator();
        while (it.hasNext()) {
            LinearLayout linearLayout = (LinearLayout) it.next();
            if (img.getTag().toString().equalsIgnoreCase(linearLayout.getTag().toString())) {
                return linearLayout;
            }
        }
        return null;
    }

    private void loadDummyCardsView() {
        try {
            Log.e(TAG, "loadDummyCardsView@954");
            clearAnimationData();
            LayoutParams lprams = new LayoutParams(-1, -1);
            for (int i = 12; i >= 0; i--) {
                ImageView image = new ImageView(RummyTablesFragment.this.mActivity);
                image.setLayoutParams(lprams);
                image.setImageResource(R.drawable.rummy_closedcard);
                image.setVisibility(View.INVISIBLE);
                this.mDummyLayout.addView(image);
                this.mDummyVies.add(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e + "");
        }
    }

    private void animatePickCaCard(int position, View destinationView, boolean isFaceDown, boolean isUser) {
        if (!((RummyTableActivity) this.mActivity).isFromIamBack()) {
            if (!(this.faceUpCardList == null || this.faceUpCardList.size() != 1 || isFaceDown)) {
                hideView(this.mOpenJokerCard);
            }
            clearAnimationData();
            LayoutParams lprams = new LayoutParams(-1, -1);
            ImageView image;
            ImageView iv;
            RummyTransferAnimation an;
            if (isFaceDown) {
                image = new ImageView(getContext());
                image.setLayoutParams(lprams);
                image.setImageResource(R.drawable.rummy_closedcard);
                image.setVisibility(View.INVISIBLE);
                this.mDummyLayout.addView(image);
                this.mDummyVies.add(image);
                iv = (ImageView) this.mDummyVies.get(position);
                iv.setVisibility(View.VISIBLE);
                an = new RummyTransferAnimation(iv);
                an.setDuration(80);
                an.setListener(new RummyAnimationListener() {
                    @Override
                    public void onAnimationEnd(RummyAnimation animation) {
                        RummyTablesFragment.this.clearAnimationData();
                    }

                    @Override
                    public void onAnimationStart(RummyAnimation animation) {

                    }
                });
                if (!isUser) {
                    an.setDestinationView(destinationView).animate();
                    return;
                } else if (this.mRummyView.getChildAt(this.mRummyView.getChildCount() - 1) != null) {
                    an.setDestinationView(destinationView).animate();
                    return;
                } else {
                    return;
                }
            }
            image = new ImageView(getContext());
            image.setLayoutParams(lprams);
            image.setVisibility(View.INVISIBLE);
            image.setImageDrawable(this.mOpenCard.getDrawable());
            this.mDummyOpenDeckLayout.removeAllViews();
            this.mDummyOpenDeckLayout.addView(image);
            this.mDummyVies.add(image);
            iv = (ImageView) this.mDummyVies.get(position);
            iv.setVisibility(View.VISIBLE);
            an = new RummyTransferAnimation(iv);
            an.setDuration(80);
            an.setListener(new RummyAnimationListener() {
                @Override
                public void onAnimationEnd(RummyAnimation animation) {
                    RummyTablesFragment.this.clearAnimationData();
                }

                @Override
                public void onAnimationStart(RummyAnimation animation) {

                }
            });
            an.setDestinationView(destinationView).animate();
        }
    }

    private void animateDiscardCard(int position, View destinationView) {
        if (!((RummyTableActivity) this.mActivity).isFromIamBack()) {
            final RelativeLayout playerDiscardLayout = (RelativeLayout) destinationView.findViewById(R.id.player_discard_dummy_layout);
            playerDiscardLayout.removeAllViews();
            playerDiscardLayout.invalidate();
            clearAnimationData();
            LayoutParams lprams = new LayoutParams(-1, -1);
            ImageView image = new ImageView(getContext());
            image.setLayoutParams(lprams);
            image.setImageResource(R.drawable.rummy_closedcard);
            image.setVisibility(View.INVISIBLE);
            playerDiscardLayout.addView(image);
            this.mDummyVies.add(image);
            ImageView iv = (ImageView) this.mDummyVies.get(position);
            iv.setVisibility(View.VISIBLE);
            RummyTransferAnimation an = new RummyTransferAnimation(iv);
            an.setDuration(80);
            an.setListener(new RummyAnimationListener() {
                public void onAnimationStart(RummyAnimation animation) {
                }

                public void onAnimationEnd(RummyAnimation animation) {
                    playerDiscardLayout.removeAllViews();
                    playerDiscardLayout.invalidate();
                    RummyTablesFragment.this.clearAnimationData();
                }
            });
            an.setDestinationView(this.mOpenCard).animate();
        }
    }

    private void clearAnimationData() {
        this.mDummyOpenDeckLayout.removeAllViews();
        this.mDummyOpenDeckLayout.invalidate();
        this.mDummyLayout.removeAllViews();
        this.mDummyLayout.invalidate();
        this.mDummyVies.clear();
    }

    public void showDeclareHelpView() {
        if (((RummyTableActivity) mActivity).getActiveTableId().equalsIgnoreCase(this.tableId)) {

            hideView(this.secure_bottom_view);
            showView(this.bottom_actions);

            showView(this.mDeclareBtn);
            enableView(this.mDeclareBtn);
            showView(this.sortCards);
            enableView(this.sortCards);
            showView(this.mShowBtn);
            hideView(this.mAutoDropPlayer);
            disableDropButton(this.mDropPlayer);
            dismissToolTipView();
            this.simpleTooltip = new Builder(this.mDeclareBtn.getContext()).anchorView(this.mDeclareBtn).text("Please group your cards and click on 'Declare' button.").gravity(48).animated(true).arrowColor(-1).backgroundColor(-1).transparentOverlay(false).build();
            this.simpleTooltip.show();
            return;
        }
        dismissToolTipView();
    }

    private void dismissToolTipView() {
        try {
            if (this.simpleTooltip != null) {
                this.simpleTooltip.dismiss();
            }
        } catch (Exception e) {
            RummyTLog.e(TAG, "Exception in dismissToolTipView");
        }
    }

    private void setUserOptions(boolean showOptions) {
        if (showOptions) {
            //showView(this.mExtraTimeBtn);
            showView(this.sortCards);
            showView(this.mDropPlayer);
            enableDropButton(this.mDropPlayer);
            if (this.mTableDetails == null || !(this.mTableDetails.getTableType().equalsIgnoreCase("BEST_OF_2") || this.mTableDetails.getTableType().equalsIgnoreCase("BEST_OF_3"))) {
                disableView(this.mAutoDropPlayer);
                hideView(this.mAutoDropPlayer);
            }else {
                disableDropButton(this.mDropPlayer);
                if(!this.isGameStarted)
                    hideView(this.mDropPlayer);
                hideView(this.mAutoDropPlayer);

            }

    //        showView(this.mShowBtn);
            return;
        }
        if (((RummyTableActivity) this.mActivity).isFromIamBack()) {
            return;
        }
        //invisibleView(this.mExtraTimeBtn);
        invisibleView(this.sortCards);
        disableDropButton(this.mDropPlayer);
        if(!this.isGameStarted)
            hideView(this.mDropPlayer);
        invisibleView(this.mAutoDropPlayer);
        invisibleView(this.mShowBtn);
    }

    private void initGameRoom() {
        hidePlayerView();
        hideView(this.mDeclareBtn);
        disableView(this.mShowBtn);
        //disableView(this.mDropPlayer);
        disableView(this.sortCards);
        disableView(this.mExtraTimeBtn);
        invisibleView(this.mUserTimerRootLayout);
    }

    private void hidePlayerView() {
        invisibleView(this.mSecondPlayerLayout);
        invisibleView(this.mThirdPlayerLayout);
        invisibleView(this.mFourthPlayerLayout);
        invisibleView(this.mFifthPlayerLayout);
        invisibleView(this.mSixthPlayerLayout);
    }

    private void showPlayerView() {
        showView(this.mSecondPlayerLayout);
        showView(this.mThirdPlayerLayout);
        showView(this.mFourthPlayerLayout);
        showView(this.mFifthPlayerLayout);
        showView(this.mSixthPlayerLayout);
    }

    private void hideTossCardsView() {
        hideView(this.mUserTossCard);
        hideView(this.mPlayer2TossCard);
        hideView(this.mPlayer3TossCard);
        hideView(this.mPlayer4TossCard);
        hideView(this.mPlayer5TossCard);
        hideView(this.mPlayer6TossCard);
    }

    private void init() {


        this.tableId = getTag();
        this.mIamBack = getArguments().getBoolean("iamBack");
        this.canShowCardDistributeAnimation = getArguments().getBoolean("canShowAnimation");
        this.mGamePlayerMap = new HashMap();
        this.mJoinedPlayersList = new ArrayList();
        this.faceDownCardList = new ArrayList();
        this.faceUpCardList = new ArrayList();
        setUpFullScreen();
        this.mSelectedCards = new ArrayList();
        this.playerCards = new ArrayList();
        this.mCards = new ArrayList();
        this.mSelectedImgList = new ArrayList();
        this.mSelectedLayoutList = new ArrayList();
        this.mGroupList = new ArrayList();
        this.mMeldGroupList = new ArrayList();
        this.mApplication = (RummyApplication.getInstance());
        if (this.mApplication != null) {
            this.userData = this.mApplication.getUserData();
        }

        avatars = new int[]{R.drawable.rummy_avtar_1,R.drawable.rummy_avtar_2,R.drawable.rummy_avtar_3,R.drawable.rummy_avtar_4,R.drawable.rummy_avtar_5,R.drawable.rummy_avtar_6};
        RummyUtils.shuffleArray(avatars);
    }


    /*
     *  Dynamic Layout -- PARTH
     */


    public void setScreenSize()
    {

        RummyUtils.setViewHeight(this.mSettingsBtn, RummyGameRoomCustomScreenLess700.game_room_setting_icon_height);
        RummyUtils.setViewHeight(this.mSettingsBtn, RummyGameRoomCustomScreenLess700.game_room_setting_icon_width);
        RummyUtils.setViewMargin(this.mSettingsBtn, 0, RummyGameRoomCustomScreenLess700.margin_10_dp,0,0);

        ////exit btn
      /*  Utils.setViewHeight(this.mLeaveTableBtn, GameRoomCustomScreenLess700.game_room_exit_iocn_height);
        Utils.setViewHeight(this.mLeaveTableBtn, GameRoomCustomScreenLess700.game_room_exit_iocn_width);
        Utils.setViewMargin(this.mLeaveTableBtn,GameRoomCustomScreenLess700.game_room_exit_icon_margin_left, GameRoomCustomScreenLess700.margin_10_dp,GameRoomCustomScreenLess700.game_room_exit_icon_margin_top,0);
*/
        ////top bar normal
        RummyUtils.setViewHeight(this.game_room_top_bar, RummyGameRoomCustomScreenLess700.game_room_top_bar_height);
        RummyUtils.setViewTextSize(this.mTableId, RummyGameRoomCustomScreenLess700.top_bar_text_size);
        RummyUtils.setViewTextSize(this.game_id_tb, RummyGameRoomCustomScreenLess700.top_bar_text_size);
        RummyUtils.setViewTextSize(this.mGameType, RummyGameRoomCustomScreenLess700.top_bar_text_size);
        RummyUtils.setViewHeight(this.iv_ruppe_icon_top_bar_game_room, RummyGameRoomCustomScreenLess700.game_room_top_bar_rupee_icon_height_width);
        RummyUtils.setViewWidth(this.iv_ruppe_icon_top_bar_game_room, RummyGameRoomCustomScreenLess700.game_room_top_bar_rupee_icon_height_width);
        RummyUtils.setViewTextSize(this.mBet, RummyGameRoomCustomScreenLess700.top_bar_text_size);
        RummyUtils.setViewHeight(this.iv_prize_icon_top_bar_game_room, RummyGameRoomCustomScreenLess700.game_room_top_bar_rupee_icon_height_width);
        RummyUtils.setViewWidth(this.iv_prize_icon_top_bar_game_room, RummyGameRoomCustomScreenLess700.game_room_top_bar_rupee_icon_height_width);
        RummyUtils.setViewTextSize(this.mPrizeMoney, RummyGameRoomCustomScreenLess700.top_bar_text_size);
        this.game_room_top_bar.invalidate();


        //////top bar tourney
        RummyUtils.setViewHeight(this.ll_tourney_top_bar, RummyGameRoomCustomScreenLess700.tourney_bar_height);
        RummyUtils.setViewWidth(this.ll_tourney_top_bar, RummyGameRoomCustomScreenLess700.tourney_bar_width);
        RummyUtils.setViewTextSize(this.tid_tourney_label_tv, RummyGameRoomCustomScreenLess700.top_bar_text_size);
        RummyUtils.setViewTextSize(this.tid_tourney_tv, RummyGameRoomCustomScreenLess700.top_bar_text_size);
        RummyUtils.setViewTextSize(this.gameid_tourney_label_tv, RummyGameRoomCustomScreenLess700.top_bar_text_size);
        RummyUtils.setViewTextSize(this.gameid_tourney_tv, RummyGameRoomCustomScreenLess700.top_bar_text_size);
        RummyUtils.setViewTextSize(this.tv_more_info_tourney, RummyGameRoomCustomScreenLess700.top_bar_text_size);

        RummyUtils.setViewHeight(this.expandTourneyInfo, RummyGameRoomCustomScreenLess700.tourney_bar_image_height_width);
        RummyUtils.setViewWidth(this.expandTourneyInfo, RummyGameRoomCustomScreenLess700.tourney_bar_image_height_width);

        RummyUtils.setViewHeight(this.collapseTourneyInfo, RummyGameRoomCustomScreenLess700.tourney_bar_image_height_width);
        RummyUtils.setViewWidth(this.collapseTourneyInfo, RummyGameRoomCustomScreenLess700.tourney_bar_image_height_width);

        RummyUtils.setViewWidth(this.tourney_expanded_layout, RummyGameRoomCustomScreenLess700.tourney_bar_width);



        /////// tourney level timer

     /*   Utils.setViewMargin(this.levelTimerLayout,GameRoomCustomScreenLess700.player6MarginLeft,0,0,0);
        Utils.setViewPaddingAll(this.ll_lavel_timer_level,GameRoomCustomScreenLess700.margin_5_dp);
        Utils.setViewPaddingAll(this.ll_lavel_timer_time,GameRoomCustomScreenLess700.margin_5_dp);

        Utils.setViewTextSize(this.levelTimerValueLebel,GameRoomCustomScreenLess700.tourney_level_timer_lebel_text_size);
        Utils.setViewTextSize(this.level_number_tv_label,GameRoomCustomScreenLess700.tourney_level_timer_lebel_text_size);

        Utils.setViewTextSize(this.level_number_tv,GameRoomCustomScreenLess700.tourney_level_timer_value_text_size);
        Utils.setViewTextSize(this.levelTimerValue,GameRoomCustomScreenLess700.tourney_level_timer_value_text_size);*/



        if(!isTablet(getActivity()))
        {
            //botom bar
            RummyUtils.setViewHeight(this.ll_bottom_bar, RummyGameRoomCustomScreenLess700.game_room_bottom_bar_height);

            RummyUtils.setViewHeight(this.sortCards, RummyGameRoomCustomScreenLess700.bottom_bar_btn_height);
            RummyUtils.setViewWidth(this.sortCards, RummyGameRoomCustomScreenLess700.bottom_bar_btn_width);
            RummyUtils.setViewMargin(this.sortCards, RummyGameRoomCustomScreenLess700.bottom_bar_btn_margin,0,0,0);

            RummyUtils.setViewHeight(this.mDropPlayer, RummyGameRoomCustomScreenLess700.bottom_bar_btn_height);
            RummyUtils.setViewWidth(this.mDropPlayer, RummyGameRoomCustomScreenLess700.bottom_bar_btn_width);
            RummyUtils.setViewMargin(this.mDropPlayer, RummyGameRoomCustomScreenLess700.bottom_bar_btn_margin,0,0,0);

            RummyUtils.setViewHeight(this.mAutoDropPlayer, RummyGameRoomCustomScreenLess700.bottom_bar_btn_height);
            RummyUtils.setViewWidth(this.mAutoDropPlayer, RummyGameRoomCustomScreenLess700.bottom_bar_btn_width);
            RummyUtils.setViewMargin(this.mAutoDropPlayer, RummyGameRoomCustomScreenLess700.bottom_bar_btn_margin,0,0,0);

            RummyUtils.setViewHeight(this.mShowBtn, RummyGameRoomCustomScreenLess700.bottom_bar_btn_height);
            RummyUtils.setViewWidth(this.mShowBtn, RummyGameRoomCustomScreenLess700.bottom_bar_btn_width);
            RummyUtils.setViewMargin(this.mShowBtn, RummyGameRoomCustomScreenLess700.bottom_bar_btn_margin,0,0,0);

            RummyUtils.setViewHeight(this.mDeclareBtn, RummyGameRoomCustomScreenLess700.bottom_bar_btn_height);
            RummyUtils.setViewWidth(this.mDeclareBtn, RummyGameRoomCustomScreenLess700.bottom_bar_btn_width);
            RummyUtils.setViewMargin(this.mDeclareBtn, RummyGameRoomCustomScreenLess700.bottom_bar_btn_margin,0,0,0);



        }else
        {
            RummyUtils.setViewHeight(this.ll_bottom_bar, RummyGameRoomCustomScreenMore700.game_room_bottom_bar_height);
            //botom bar
            RummyUtils.setViewHeight(this.ll_bottom_bar, RummyGameRoomCustomScreenMore700.game_room_bottom_bar_height);
            RummyUtils.setViewHeight(this.sortCards, RummyGameRoomCustomScreenMore700.bottom_bar_btn_height);
            RummyUtils.setViewWidth(this.sortCards, RummyGameRoomCustomScreenMore700.bottom_bar_btn_width);
            RummyUtils.setViewMargin(this.sortCards, RummyGameRoomCustomScreenMore700.bottom_bar_btn_margin,0,0,0);

            RummyUtils.setViewHeight(this.mDropPlayer, RummyGameRoomCustomScreenMore700.bottom_bar_btn_height);
            RummyUtils.setViewWidth(this.mDropPlayer, RummyGameRoomCustomScreenMore700.bottom_bar_btn_width);
            RummyUtils.setViewMargin(this.mDropPlayer, RummyGameRoomCustomScreenMore700.bottom_bar_btn_margin,0,0,0);

            RummyUtils.setViewHeight(this.mAutoDropPlayer, RummyGameRoomCustomScreenMore700.bottom_bar_btn_height);
            RummyUtils.setViewWidth(this.mAutoDropPlayer, RummyGameRoomCustomScreenMore700.bottom_bar_btn_width);
            RummyUtils.setViewMargin(this.mAutoDropPlayer, RummyGameRoomCustomScreenMore700.bottom_bar_btn_margin,0,0,0);

            RummyUtils.setViewHeight(this.mShowBtn, RummyGameRoomCustomScreenMore700.bottom_bar_btn_height);
            RummyUtils.setViewWidth(this.mShowBtn, RummyGameRoomCustomScreenMore700.bottom_bar_btn_width);
            RummyUtils.setViewMargin(this.mShowBtn, RummyGameRoomCustomScreenMore700.bottom_bar_btn_margin,0,0,0);

            RummyUtils.setViewHeight(this.mDeclareBtn, RummyGameRoomCustomScreenMore700.bottom_bar_btn_height);
            RummyUtils.setViewWidth(this.mDeclareBtn, RummyGameRoomCustomScreenMore700.bottom_bar_btn_width);
            RummyUtils.setViewMargin(this.mDeclareBtn, RummyGameRoomCustomScreenMore700.bottom_bar_btn_margin,0,0,0);
        }


        ///middel part rummy_close deck and stuff
        RummyUtils.setViewMargin(this.mGameDeckLayout,0,0,0,0);
        RummyUtils.setViewWidth(this.rl_close_deck_main_container, RummyGameRoomCustomScreenLess700.gameScreenClosedDeckWidth);
        RummyUtils.setViewWidth(this.mFaceDownCards, RummyGameRoomCustomScreenLess700.ImagefaceDownCardsWidth);
        RummyUtils.setViewWidth(this.rl_close_card_container, RummyGameRoomCustomScreenLess700.gameScreenOpenDeckWidth);
        RummyUtils.setViewWidth(this.rl_open_deck_container, RummyGameRoomCustomScreenLess700.gameScreenOpenDeckWidth);

      //  Utils.setViewWidth(mJokerCard,GameRoomCustomScreenLess700.jokerImageViewWidth);

        RummyUtils.setViewHeight(this.mGameDeckLayout, RummyGameRoomCustomScreenLess700.GameDeckHeight);

        RummyUtils.setViewHeight(this.iv_rdl_logo, RummyGameRoomCustomScreenLess700.tr_game_room_logo_Height);
        RummyUtils.setViewMargin(this.iv_rdl_logo, RummyGameRoomCustomScreenLess700.tr_game_room_logo_left_margin,0,0,0);

        RummyUtils.setViewHeight(this.rl_open_discard, RummyGameRoomCustomScreenLess700.open_card_show_btn_height_width);
        RummyUtils.setViewWidth(this.rl_open_discard, RummyGameRoomCustomScreenLess700.open_card_show_btn_height_width);
        RummyUtils.setViewMargin(this.rl_open_discard,0,0,0, RummyGameRoomCustomScreenLess700.open_card_show_btn_margin_bottom);

       /* Utils.setViewTextSize(this.mGameDeckLayout.findViewById(R.id.tv_joker_label),GameRoomCustomScreenLess700.middle_close_deck_text_size);
        Utils.setViewTextSize(this.mGameDeckLayout.findViewById(R.id.closed_deck_lable_tv),GameRoomCustomScreenLess700.middle_close_deck_text_size);
        Utils.setViewTextSize(this.mGameDeckLayout.findViewById(R.id.open_deck_lable_tv),GameRoomCustomScreenLess700.middle_close_deck_text_size);
        Utils.setViewTextSize(this.mGameDeckLayout.findViewById(R.id.closed_card_lable_tv),GameRoomCustomScreenLess700.middle_close_deck_text_size);*/

        //////////************ rummy_player 2 rummy_profile*************///////////


    //    setSizePlayer2Profile();

        //////////************ rummy_player 3 rummy_profile*************///////////

   //     setSizePlayer3Profile();

        //////////************ rummy_player 4 rummy_profile*************///////////
   //     setSizePlayer4Profile();

        //////////************ rummy_player 5 rummy_profile*************///////////
   //     setSizePlayer5Profile();

        //////////************ rummy_player 6 rummy_profile*************///////////
   //     setSizePlayer6Profile();

        //////////************ rummy_player 1 rummy_profile*************///////////
  //      setSizePlayer1Profile();


     if(!isTablet(getActivity()))
     {
         ///// Rummy View
         RummyUtils.setViewMargin(this.ll_rummy_view_container,0,0,0, RummyGameRoomCustomScreenLess700.game_room_bottom_bar_height);
         RummyUtils.setViewMargin(this.mRummyView, RummyGameRoomCustomScreenLess700.rummy_view_margin_left, RummyGameRoomCustomScreenLess700.rummy_view_margin_right,0, RummyGameRoomCustomScreenLess700.rummy_view_margin_bottom);
         RummyUtils.setViewMargin(this.resumeShadow, 5, 5, RummyGameRoomCustomScreenLess700.rummy_view_margin_bottom, 5);
         RummyUtils.setViewPaddingLeft(this.mRummyView, RummyGameRoomCustomScreenLess700.rummy_view_padding_left);

         RummyUtils.setViewMargin(this.rl_groups_container, RummyGameRoomCustomScreenLess700.rummy_view_margin_left, RummyGameRoomCustomScreenLess700.rummy_view_margin_right,0, RummyGameRoomCustomScreenLess700.rummy_group_containers_margin_bottom);
         RummyUtils.setViewPaddingLeft(this.rl_groups_container, RummyGameRoomCustomScreenLess700.rummy_view_padding_left);

         RummyUtils.setViewMargin(this.mUserDiscardLaout,0,0,0, RummyGameRoomCustomScreenLess700.user_discard_dummy_margin_bottom);
         RummyUtils.setViewHeight(this.mUserDiscardLaout, RummyGameRoomCustomScreenLess700.user_discard_dummy_layout_height);
         RummyUtils.setViewWidth(this.mUserDiscardLaout, RummyGameRoomCustomScreenLess700.user_discard_dummy_layout_width);

         ////// game start timer
         RummyUtils.setViewTextSize(this.game_start_time_tv, RummyGameRoomCustomScreenLess700.game_start_tv_text_size);
         RummyUtils.setViewTextSize(this.mGameShecduleTv, RummyGameRoomCustomScreenLess700.game_start_tv_text_size);
         RummyUtils.setViewMargin(this.mGameShecduleTv,0,0, RummyGameRoomCustomScreenLess700.margin_5_dp,0);

         ///// rummy_toss cards

         RummyUtils.setViewHeight(this.mPlayer2TossCard, RummyGameRoomCustomScreenLess700.toss_card_height);
         RummyUtils.setViewWidth(this.mPlayer2TossCard, RummyGameRoomCustomScreenLess700.toss_card_width);
         RummyUtils.setViewMargin(this.mPlayer2TossCard, RummyGameRoomCustomScreenLess700.toss_card_margin_left,0, RummyGameRoomCustomScreenLess700.toss_card_margin_top,0);


         RummyUtils.setViewHeight(this.mPlayer3TossCard, RummyGameRoomCustomScreenLess700.toss_card_height);
         RummyUtils.setViewWidth(this.mPlayer3TossCard, RummyGameRoomCustomScreenLess700.toss_card_width);
         RummyUtils.setViewMargin(this.mPlayer3TossCard, RummyGameRoomCustomScreenLess700.toss_card_margin_left,0, RummyGameRoomCustomScreenLess700.toss_card_margin_top,0);

         RummyUtils.setViewHeight(this.mPlayer4TossCard, RummyGameRoomCustomScreenLess700.toss_card_height);
         RummyUtils.setViewWidth(this.mPlayer4TossCard, RummyGameRoomCustomScreenLess700.toss_card_width);
         RummyUtils.setViewMargin(this.mPlayer4TossCard,0,0, RummyGameRoomCustomScreenLess700.toss_card_margin_top,0);

         RummyUtils.setViewHeight(this.mPlayer5TossCard, RummyGameRoomCustomScreenLess700.toss_card_height);
         RummyUtils.setViewWidth(this.mPlayer5TossCard, RummyGameRoomCustomScreenLess700.toss_card_width);
         RummyUtils.setViewMargin(this.mPlayer5TossCard, RummyGameRoomCustomScreenLess700.toss_card_margin_left,0, RummyGameRoomCustomScreenLess700.toss_card_margin_top,0);

         RummyUtils.setViewHeight(this.mPlayer6TossCard, RummyGameRoomCustomScreenLess700.toss_card_height);
         RummyUtils.setViewWidth(this.mPlayer6TossCard, RummyGameRoomCustomScreenLess700.toss_card_width);
         RummyUtils.setViewMargin(this.mPlayer6TossCard, RummyGameRoomCustomScreenLess700.toss_card_margin_left,0, RummyGameRoomCustomScreenLess700.toss_card_margin_top,0);


         RummyUtils.setViewHeight(this.mUserTossCard, RummyGameRoomCustomScreenLess700.toss_card_height);
         RummyUtils.setViewWidth(this.mUserTossCard, RummyGameRoomCustomScreenLess700.toss_card_width);
         RummyUtils.setViewMargin(this.mUserTossCard,0,0,0, RummyGameRoomCustomScreenLess700.game_room_bottom_bar_height);
     }

     else {
         ///// Rummy View
         RummyUtils.setViewMargin(this.ll_rummy_view_container,0,0,0, RummyGameRoomCustomScreenMore700.game_room_bottom_bar_height);
         RummyUtils.setViewMargin(this.mRummyView, RummyGameRoomCustomScreenMore700.rummy_view_margin_left, RummyGameRoomCustomScreenMore700.rummy_view_margin_right,0, RummyGameRoomCustomScreenMore700.rummy_view_margin_bottom);
         RummyUtils.setViewPaddingLeft(this.mRummyView, RummyGameRoomCustomScreenMore700.rummy_view_padding_left);

         RummyUtils.setViewMargin(this.rl_groups_container, RummyGameRoomCustomScreenMore700.rummy_view_margin_left, RummyGameRoomCustomScreenMore700.rummy_view_margin_right,0, RummyGameRoomCustomScreenMore700.rummy_group_containers_margin_bottom);
         RummyUtils.setViewPaddingLeft(this.rl_groups_container, RummyGameRoomCustomScreenMore700.rummy_view_padding_left);

         RummyUtils.setViewMargin(this.mUserDiscardLaout,0,0,0, RummyGameRoomCustomScreenMore700.user_discard_dummy_margin_bottom);
         RummyUtils.setViewHeight(this.mUserDiscardLaout, RummyGameRoomCustomScreenMore700.user_discard_dummy_layout_height);
         RummyUtils.setViewWidth(this.mUserDiscardLaout, RummyGameRoomCustomScreenMore700.user_discard_dummy_layout_width);

         ////// game start timer
         RummyUtils.setViewTextSize(this.game_start_time_tv, RummyGameRoomCustomScreenMore700.game_start_tv_text_size);
         RummyUtils.setViewTextSize(this.mGameShecduleTv, RummyGameRoomCustomScreenMore700.game_start_tv_text_size);
         RummyUtils.setViewMargin(this.mGameShecduleTv,0,0, RummyGameRoomCustomScreenMore700.margin_5_dp,0);

         ///// rummy_toss cards

         RummyUtils.setViewHeight(this.mPlayer2TossCard, RummyGameRoomCustomScreenMore700.toss_card_height);
         RummyUtils.setViewWidth(this.mPlayer2TossCard, RummyGameRoomCustomScreenMore700.toss_card_width);
         RummyUtils.setViewMargin(this.mPlayer2TossCard, RummyGameRoomCustomScreenMore700.toss_card_margin_left,0, RummyGameRoomCustomScreenMore700.toss_card_margin_top,0);


         RummyUtils.setViewHeight(this.mPlayer3TossCard, RummyGameRoomCustomScreenMore700.toss_card_height);
         RummyUtils.setViewWidth(this.mPlayer3TossCard, RummyGameRoomCustomScreenMore700.toss_card_width);
         RummyUtils.setViewMargin(this.mPlayer3TossCard, RummyGameRoomCustomScreenMore700.toss_card_margin_left,0, RummyGameRoomCustomScreenMore700.toss_card_margin_top,0);

         RummyUtils.setViewHeight(this.mPlayer4TossCard, RummyGameRoomCustomScreenMore700.toss_card_height);
         RummyUtils.setViewWidth(this.mPlayer4TossCard, RummyGameRoomCustomScreenMore700.toss_card_width);
         RummyUtils.setViewMargin(this.mPlayer4TossCard,0,0, RummyGameRoomCustomScreenMore700.toss_card_margin_top,0);

         RummyUtils.setViewHeight(this.mPlayer5TossCard, RummyGameRoomCustomScreenMore700.toss_card_height);
         RummyUtils.setViewWidth(this.mPlayer5TossCard, RummyGameRoomCustomScreenMore700.toss_card_width);
         RummyUtils.setViewMargin(this.mPlayer5TossCard, RummyGameRoomCustomScreenMore700.toss_card_margin_left,0, RummyGameRoomCustomScreenMore700.toss_card_margin_top,0);

         RummyUtils.setViewHeight(this.mPlayer6TossCard, RummyGameRoomCustomScreenMore700.toss_card_height);
         RummyUtils.setViewWidth(this.mPlayer6TossCard, RummyGameRoomCustomScreenMore700.toss_card_width);
         RummyUtils.setViewMargin(this.mPlayer6TossCard, RummyGameRoomCustomScreenMore700.toss_card_margin_left,0, RummyGameRoomCustomScreenMore700.toss_card_margin_top,0);


         RummyUtils.setViewHeight(this.mUserTossCard, RummyGameRoomCustomScreenMore700.toss_card_height);
         RummyUtils.setViewWidth(this.mUserTossCard, RummyGameRoomCustomScreenMore700.toss_card_width);
         RummyUtils.setViewMargin(this.mUserTossCard,0,0,0, RummyGameRoomCustomScreenMore700.game_room_bottom_bar_height);
     }


        //////rummy_drop and go layout

        //RummyUtils.setViewMargin(this.last_game_layout_root,RummyGameRoomCustomScreenMore700.margin_2_dp,RummyGameRoomCustomScreenMore700.margin_10_dp,RummyGameRoomCustomScreenMore700.margin_5_dp,0);
        //RummyUtils.setViewPaddingAll(this.last_game_layout,RummyGameRoomCustomScreenMore700.margin_5_dp);
        //RummyUtils.setViewHeight(this.last_game_layout,RummyGameRoomCustomScreenMore700.last_game_layout_height);

        RummyUtils.setViewHeight(this.mLastGamePlayer,RummyGameRoomCustomScreenMore700.last_game_cd_height_width);
        RummyUtils.setViewWidth(this.mLastGamePlayer,RummyGameRoomCustomScreenMore700.last_game_cd_height_width);

        RummyUtils.setViewHeight(this.mDropAndGoPlayer,RummyGameRoomCustomScreenMore700.last_game_cd_height_width);
        RummyUtils.setViewWidth(this.mDropAndGoPlayer,RummyGameRoomCustomScreenMore700.last_game_cd_height_width);

        RummyUtils.setViewTextSize(this.tv_drop_and_go_lable,RummyGameRoomCustomScreenMore700.drop_and_go_text_size);
        RummyUtils.setViewTextSize(this.tv_last_game_lable,RummyGameRoomCustomScreenMore700.drop_and_go_text_size);












    }

    private void setSizePlayer1Profile()
    {


        //  Utils.setViewWidth(this.mFifthPlayerLayout,GameRoomCustomScreenLess700.playerBoxWidth);

        //  Utils.setViewHeight(this.mFifthPlayerAutoTimerLayout,GameRoomCustomScreenLess700.playerBoxHeight);


        //******** rummy_player box
        RummyUtils.setViewHeight(this.mUserPlayerLayout, RummyGameRoomCustomScreenLess700.playerBoxHeight);
        RummyUtils.setViewWidth(this.mUserPlayerLayout, RummyGameRoomCustomScreenLess700.playerBoxWidth);

    /*    Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_details_main_root_layout),GameRoomCustomScreenLess700.playerBoxWidth);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_details_main_root_layout),GameRoomCustomScreenLess700.playerBoxHeight);*/



        RummyUtils.setViewTextSize(this.mUserPlayerLayout.findViewById(R.id.player_name_tv), RummyGameRoomCustomScreenLess700.playerProfileTextSize);


        RummyUtils.setViewMargin(mUserAutoChunksLayout, 0,0, RummyGameRoomCustomScreenLess700.auto_chunks_margin_top,0);

        RummyUtils.setViewWidth(this.mUserAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mUserAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mUserAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

        RummyUtils.setViewWidth(this.mUserAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mUserAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mUserAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

        RummyUtils.setViewWidth(this.mUserAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mUserAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mUserAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

       /* Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv),GameRoomCustomScreenLess700.player_system_iv_size);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv),GameRoomCustomScreenLess700.player_system_iv_size);
        Utils.setViewMargin(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv), 0,GameRoomCustomScreenLess700.player_system_iv_marginRight,GameRoomCustomScreenLess700.player_system_iv_marginTop,0);

        */

      /*  Utils.setViewWidth(player_1_autoplay_icon,GameRoomCustomScreenLess700.autoPlayIconSizeProfile);
        Utils.setViewHeight(this.player_1_autoplay_icon,GameRoomCustomScreenLess700.autoPlayIconSizeProfile);
        Utils.setViewTextSize(autoplay_count_player_6,GameRoomCustomScreenLess700.autoPlayCountTextSize);*/

      //  Utils.setViewWidth(player_6_autoplay_box,GameRoomCustomScreenLess700.autoPlayLayoutWidth);

        // Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_drop_iv_to_size),GameRoomCustomScreenLess700.dropIvWidth);
        // Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_drop_iv_to_size),GameRoomCustomScreenLess700.dropIvHeight);

//        Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_5_cards),GameRoomCustomScreenLess700.opponentCardsWidth);
//        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_5_cards),GameRoomCustomScreenLess700.opponentCardsHeight);
//        Utils.setViewMargin(this.mFifthPlayerLayout.findViewById(R.id.player_5_cards), 0,0,GameRoomCustomScreenLess700.opponentCardsMarginTop,0);

        /////////Timer layout

        //  Utils.setViewMargin(this.player_5_timer_root_layout, 0,GameRoomCustomScreenLess700.player5TimerMarginRight,GameRoomCustomScreenLess700.timer_margin_top,0);
        //  Utils.setViewWidth(this.mFifthPlayerTimerLayout,GameRoomCustomScreenLess700.timer_size);
        //  Utils.setViewHeight(this.mFifthPlayerTimerLayout,GameRoomCustomScreenLess700.timer_size);

        //  Utils.setViewWidth(this.mFifthPlayerTimerLayout.findViewById(R.id.timer_root_layout),GameRoomCustomScreenLess700.timer_size);
        //  Utils.setViewHeight(this.mFifthPlayerTimerLayout.findViewById(R.id.timer_root_layout),GameRoomCustomScreenLess700.timer_size);

   /*     Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),GameRoomCustomScreenLess700.player_dealer_icon_width);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),GameRoomCustomScreenLess700.player_dealer_icon_height);
        Utils.setViewMargin(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),0,0,GameRoomCustomScreenLess700.opponentCardsMarginTop,0);*/



        RummyUtils.setViewHeight(player_1_root_layout,100);
        RummyUtils.setViewWidth(player_1_root_layout,70);




    }

    private void setSizePlayer6Profile()
    {


        //  Utils.setViewWidth(this.mFifthPlayerLayout,GameRoomCustomScreenLess700.playerBoxWidth);

        //  Utils.setViewHeight(this.mFifthPlayerAutoTimerLayout,GameRoomCustomScreenLess700.playerBoxHeight);


        //******** rummy_player box
        RummyUtils.setViewHeight(this.mSixthPlayerLayout.findViewById(R.id.player_6_box), RummyGameRoomCustomScreenLess700.playerBoxHeight);
        RummyUtils.setViewWidth(this.mSixthPlayerLayout.findViewById(R.id.player_6_box), RummyGameRoomCustomScreenLess700.playerBoxWidth);

    /*    Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_details_main_root_layout),GameRoomCustomScreenLess700.playerBoxWidth);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_details_main_root_layout),GameRoomCustomScreenLess700.playerBoxHeight);*/



        RummyUtils.setViewTextSize(this.mSixthPlayerLayout.findViewById(R.id.player_name_tv), RummyGameRoomCustomScreenLess700.playerProfileTextSize);


        RummyUtils.setViewMargin(mSixthPlayerAutoChunksLayout, 0,0, RummyGameRoomCustomScreenLess700.auto_chunks_margin_top,0);

        RummyUtils.setViewWidth(this.mSixthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mSixthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mSixthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

        RummyUtils.setViewWidth(this.mSixthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mSixthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mSixthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

        RummyUtils.setViewWidth(this.mSixthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mSixthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mSixthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

       /* Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv),GameRoomCustomScreenLess700.player_system_iv_size);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv),GameRoomCustomScreenLess700.player_system_iv_size);
        Utils.setViewMargin(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv), 0,GameRoomCustomScreenLess700.player_system_iv_marginRight,GameRoomCustomScreenLess700.player_system_iv_marginTop,0);

        */

        RummyUtils.setViewWidth(player_6_autoplay_icon, RummyGameRoomCustomScreenLess700.autoPlayIconSizeProfile);
        RummyUtils.setViewHeight(this.player_6_autoplay_icon, RummyGameRoomCustomScreenLess700.autoPlayIconSizeProfile);
        RummyUtils.setViewTextSize(autoplay_count_player_6, RummyGameRoomCustomScreenLess700.autoPlayCountTextSize);

        RummyUtils.setViewWidth(player_6_autoplay_box, RummyGameRoomCustomScreenLess700.autoPlayLayoutWidth);
        RummyUtils.setViewHeight(player_6_autoplay_box, RummyGameRoomCustomScreenLess700.autoPlayLayoutHeight);

        // Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_drop_iv_to_size),GameRoomCustomScreenLess700.dropIvWidth);
        // Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_drop_iv_to_size),GameRoomCustomScreenLess700.dropIvHeight);

        RummyUtils.setViewWidth(this.mSixthPlayerLayout.findViewById(R.id.player_6_cards), RummyGameRoomCustomScreenLess700.opponentCardsWidth);
        RummyUtils.setViewHeight(this.mSixthPlayerLayout.findViewById(R.id.player_6_cards), RummyGameRoomCustomScreenLess700.opponentCardsHeight);
        RummyUtils.setViewMargin(this.mSixthPlayerLayout.findViewById(R.id.player_6_cards), 0,0, RummyGameRoomCustomScreenLess700.opponentCardsMarginTop,0);

        /////////Timer layout

        //  Utils.setViewMargin(this.player_5_timer_root_layout, 0,GameRoomCustomScreenLess700.player5TimerMarginRight,GameRoomCustomScreenLess700.timer_margin_top,0);
        //  Utils.setViewWidth(this.mFifthPlayerTimerLayout,GameRoomCustomScreenLess700.timer_size);
        //  Utils.setViewHeight(this.mFifthPlayerTimerLayout,GameRoomCustomScreenLess700.timer_size);

        //  Utils.setViewWidth(this.mFifthPlayerTimerLayout.findViewById(R.id.timer_root_layout),GameRoomCustomScreenLess700.timer_size);
        //  Utils.setViewHeight(this.mFifthPlayerTimerLayout.findViewById(R.id.timer_root_layout),GameRoomCustomScreenLess700.timer_size);

   /*     Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),GameRoomCustomScreenLess700.player_dealer_icon_width);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),GameRoomCustomScreenLess700.player_dealer_icon_height);
        Utils.setViewMargin(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),0,0,GameRoomCustomScreenLess700.opponentCardsMarginTop,0);*/


       RummyUtils.setViewMargin(player_6_root_layout,0,30,60,0);

       RummyUtils.setViewHeight(small_round_box_player_6,35);
       RummyUtils.setViewWidth(small_round_box_player_6,35);

       RummyUtils.setViewMargin(small_round_box_player_6,0,0,0,10);

    }

    private void setSizePlayer5Profile()
    {


      //  Utils.setViewWidth(this.mFifthPlayerLayout,GameRoomCustomScreenLess700.playerBoxWidth);

      //  Utils.setViewHeight(this.mFifthPlayerAutoTimerLayout,GameRoomCustomScreenLess700.playerBoxHeight);


        //******** rummy_player box
        RummyUtils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_5_box), RummyGameRoomCustomScreenLess700.playerBoxHeight);
        RummyUtils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_5_box), RummyGameRoomCustomScreenLess700.playerBoxWidth);

    /*    Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_details_main_root_layout),GameRoomCustomScreenLess700.playerBoxWidth);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_details_main_root_layout),GameRoomCustomScreenLess700.playerBoxHeight);*/



        RummyUtils.setViewTextSize(this.mFifthPlayerLayout.findViewById(R.id.player_name_tv), RummyGameRoomCustomScreenLess700.playerProfileTextSize);


        RummyUtils.setViewMargin(mFifthPlayerAutoChunksLayout, 0,0, RummyGameRoomCustomScreenLess700.auto_chunks_margin_top,0);

        RummyUtils.setViewWidth(this.mFifthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mFifthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mFifthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

        RummyUtils.setViewWidth(this.mFifthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mFifthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mFifthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

        RummyUtils.setViewWidth(this.mFifthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mFifthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mFifthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

       /* Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv),GameRoomCustomScreenLess700.player_system_iv_size);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv),GameRoomCustomScreenLess700.player_system_iv_size);
        Utils.setViewMargin(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv), 0,GameRoomCustomScreenLess700.player_system_iv_marginRight,GameRoomCustomScreenLess700.player_system_iv_marginTop,0);

        */

        RummyUtils.setViewWidth(player_5_autoplay_icon, RummyGameRoomCustomScreenLess700.autoPlayIconSizeProfile);
        RummyUtils.setViewHeight(this.player_5_autoplay_icon, RummyGameRoomCustomScreenLess700.autoPlayIconSizeProfile);
        RummyUtils.setViewTextSize(autoplay_count_player_5, RummyGameRoomCustomScreenLess700.autoPlayCountTextSize);

        RummyUtils.setViewWidth(player_5_autoplay_box, RummyGameRoomCustomScreenLess700.autoPlayLayoutWidth);
        RummyUtils.setViewHeight(player_5_autoplay_box, RummyGameRoomCustomScreenLess700.autoPlayLayoutHeight);

        // Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_drop_iv_to_size),GameRoomCustomScreenLess700.dropIvWidth);
        // Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_drop_iv_to_size),GameRoomCustomScreenLess700.dropIvHeight);

        RummyUtils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_5_cards), RummyGameRoomCustomScreenLess700.opponentCardsWidth);
        RummyUtils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_5_cards), RummyGameRoomCustomScreenLess700.opponentCardsHeight);
        RummyUtils.setViewMargin(this.mFifthPlayerLayout.findViewById(R.id.player_5_cards), 0,0, RummyGameRoomCustomScreenLess700.opponentCardsMarginTop,0);

        /////////Timer layout

      //  Utils.setViewMargin(this.player_5_timer_root_layout, 0,GameRoomCustomScreenLess700.player5TimerMarginRight,GameRoomCustomScreenLess700.timer_margin_top,0);
      //  Utils.setViewWidth(this.mFifthPlayerTimerLayout,GameRoomCustomScreenLess700.timer_size);
      //  Utils.setViewHeight(this.mFifthPlayerTimerLayout,GameRoomCustomScreenLess700.timer_size);

      //  Utils.setViewWidth(this.mFifthPlayerTimerLayout.findViewById(R.id.timer_root_layout),GameRoomCustomScreenLess700.timer_size);
      //  Utils.setViewHeight(this.mFifthPlayerTimerLayout.findViewById(R.id.timer_root_layout),GameRoomCustomScreenLess700.timer_size);

   /*     Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),GameRoomCustomScreenLess700.player_dealer_icon_width);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),GameRoomCustomScreenLess700.player_dealer_icon_height);
        Utils.setViewMargin(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),0,0,GameRoomCustomScreenLess700.opponentCardsMarginTop,0);*/




        RummyUtils.setViewMargin(player_5_root_layout,0,30,0,0);

        RummyUtils.setViewHeight(small_round_box_player_5,35);
        RummyUtils.setViewWidth(small_round_box_player_5,35);

        RummyUtils.setViewMargin(small_round_box_player_5,0,0,0,10);

    }

    private void setSizePlayer4Profile()
    {


        //  Utils.setViewWidth(this.mFifthPlayerLayout,GameRoomCustomScreenLess700.playerBoxWidth);

        //  Utils.setViewHeight(this.mFifthPlayerAutoTimerLayout,GameRoomCustomScreenLess700.playerBoxHeight);


        //******** rummy_player box
        RummyUtils.setViewHeight(this.mFourthPlayerLayout.findViewById(R.id.player_4_box), RummyGameRoomCustomScreenLess700.playerBoxHeight);
        RummyUtils.setViewWidth(this.mFourthPlayerLayout.findViewById(R.id.player_4_box), RummyGameRoomCustomScreenLess700.playerBoxWidth);

    /*    Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_details_main_root_layout),GameRoomCustomScreenLess700.playerBoxWidth);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_details_main_root_layout),GameRoomCustomScreenLess700.playerBoxHeight);*/



        RummyUtils.setViewTextSize(this.mFourthPlayerLayout.findViewById(R.id.player_name_tv), RummyGameRoomCustomScreenLess700.playerProfileTextSize);


        RummyUtils.setViewMargin(mFourthPlayerAutoChunksLayout, 0,0, RummyGameRoomCustomScreenLess700.auto_chunks_margin_top,0);

        RummyUtils.setViewWidth(this.mFourthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mFourthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mFourthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

        RummyUtils.setViewWidth(this.mFourthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mFourthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mFourthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

        RummyUtils.setViewWidth(this.mFourthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mFourthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mFourthPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

       /* Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv),GameRoomCustomScreenLess700.player_system_iv_size);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv),GameRoomCustomScreenLess700.player_system_iv_size);
        Utils.setViewMargin(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv), 0,GameRoomCustomScreenLess700.player_system_iv_marginRight,GameRoomCustomScreenLess700.player_system_iv_marginTop,0);

        */

        RummyUtils.setViewWidth(player_4_autoplay_icon, RummyGameRoomCustomScreenLess700.autoPlayIconSizeProfile);
        RummyUtils.setViewHeight(this.player_4_autoplay_icon, RummyGameRoomCustomScreenLess700.autoPlayIconSizeProfile);
        RummyUtils.setViewTextSize(autoplay_count_player_4, RummyGameRoomCustomScreenLess700.autoPlayCountTextSize);

        RummyUtils.setViewWidth(player_4_autoplay_box, RummyGameRoomCustomScreenLess700.autoPlayLayoutWidth);
        RummyUtils.setViewHeight(player_4_autoplay_box, RummyGameRoomCustomScreenLess700.autoPlayLayoutHeight);

        // Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_drop_iv_to_size),GameRoomCustomScreenLess700.dropIvWidth);
        // Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_drop_iv_to_size),GameRoomCustomScreenLess700.dropIvHeight);

        RummyUtils.setViewWidth(this.mFourthPlayerLayout.findViewById(R.id.player_4_cards), RummyGameRoomCustomScreenLess700.opponentCardsWidth);
        RummyUtils.setViewHeight(this.mFourthPlayerLayout.findViewById(R.id.player_4_cards), RummyGameRoomCustomScreenLess700.opponentCardsHeight);
        RummyUtils.setViewMargin(this.mFourthPlayerLayout.findViewById(R.id.player_4_cards), 0,0, RummyGameRoomCustomScreenLess700.opponentCardsMarginTop,0);

        /////////Timer layout

        //  Utils.setViewMargin(this.player_5_timer_root_layout, 0,GameRoomCustomScreenLess700.player5TimerMarginRight,GameRoomCustomScreenLess700.timer_margin_top,0);
        //  Utils.setViewWidth(this.mFifthPlayerTimerLayout,GameRoomCustomScreenLess700.timer_size);
        //  Utils.setViewHeight(this.mFifthPlayerTimerLayout,GameRoomCustomScreenLess700.timer_size);

        //  Utils.setViewWidth(this.mFifthPlayerTimerLayout.findViewById(R.id.timer_root_layout),GameRoomCustomScreenLess700.timer_size);
        //  Utils.setViewHeight(this.mFifthPlayerTimerLayout.findViewById(R.id.timer_root_layout),GameRoomCustomScreenLess700.timer_size);

   /*     Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),GameRoomCustomScreenLess700.player_dealer_icon_width);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),GameRoomCustomScreenLess700.player_dealer_icon_height);
        Utils.setViewMargin(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),0,0,GameRoomCustomScreenLess700.opponentCardsMarginTop,0);*/


        RummyUtils.setViewMargin(player_4_root_layout,0,0,0,0);

        RummyUtils.setViewHeight(small_round_box_player_4,35);
        RummyUtils.setViewWidth(small_round_box_player_4,35);

        RummyUtils.setViewMargin(small_round_box_player_4,0,0,0,10);




    }


    private void setSizePlayer2Profile()
    {


        //  Utils.setViewWidth(this.mFifthPlayerLayout,GameRoomCustomScreenLess700.playerBoxWidth);

        //  Utils.setViewHeight(this.mFifthPlayerAutoTimerLayout,GameRoomCustomScreenLess700.playerBoxHeight);


        //******** rummy_player box
        RummyUtils.setViewHeight(this.mSecondPlayerLayout.findViewById(R.id.player_2_box), RummyGameRoomCustomScreenLess700.playerBoxHeight);
        RummyUtils.setViewWidth(this.mSecondPlayerLayout.findViewById(R.id.player_2_box), RummyGameRoomCustomScreenLess700.playerBoxWidth);

    /*    Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_details_main_root_layout),GameRoomCustomScreenLess700.playerBoxWidth);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_details_main_root_layout),GameRoomCustomScreenLess700.playerBoxHeight);*/



        RummyUtils.setViewTextSize(this.mSecondPlayerLayout.findViewById(R.id.player_name_tv), RummyGameRoomCustomScreenLess700.playerProfileTextSize);


        RummyUtils.setViewMargin(mSecondPlayerAutoChunksLayout, 0,0, RummyGameRoomCustomScreenLess700.auto_chunks_margin_top,0);

        RummyUtils.setViewWidth(this.mSecondPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mSecondPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mSecondPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

        RummyUtils.setViewWidth(this.mSecondPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mSecondPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mSecondPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

        RummyUtils.setViewWidth(this.mSecondPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mSecondPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mSecondPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

       /* Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv),GameRoomCustomScreenLess700.player_system_iv_size);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv),GameRoomCustomScreenLess700.player_system_iv_size);
        Utils.setViewMargin(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv), 0,GameRoomCustomScreenLess700.player_system_iv_marginRight,GameRoomCustomScreenLess700.player_system_iv_marginTop,0);

        */

        RummyUtils.setViewWidth(player_2_autoplay_icon, RummyGameRoomCustomScreenLess700.autoPlayIconSizeProfile);
        RummyUtils.setViewHeight(this.player_2_autoplay_icon, RummyGameRoomCustomScreenLess700.autoPlayIconSizeProfile);
        RummyUtils.setViewTextSize(autoplay_count_player_2, RummyGameRoomCustomScreenLess700.autoPlayCountTextSize);

        RummyUtils.setViewWidth(player_2_autoplay_box, RummyGameRoomCustomScreenLess700.autoPlayLayoutWidth);
        RummyUtils.setViewHeight(player_2_autoplay_box, RummyGameRoomCustomScreenLess700.autoPlayLayoutHeight);

        // Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_drop_iv_to_size),GameRoomCustomScreenLess700.dropIvWidth);
        // Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_drop_iv_to_size),GameRoomCustomScreenLess700.dropIvHeight);

        RummyUtils.setViewWidth(this.mSecondPlayerLayout.findViewById(R.id.player_2_cards), RummyGameRoomCustomScreenLess700.opponentCardsWidth);
        RummyUtils.setViewHeight(this.mSecondPlayerLayout.findViewById(R.id.player_2_cards), RummyGameRoomCustomScreenLess700.opponentCardsHeight);
        RummyUtils.setViewMargin(this.mSecondPlayerLayout.findViewById(R.id.player_2_cards), 0,0, RummyGameRoomCustomScreenLess700.opponentCardsMarginTop,0);

        /////////Timer layout

        //  Utils.setViewMargin(this.player_5_timer_root_layout, 0,GameRoomCustomScreenLess700.player5TimerMarginRight,GameRoomCustomScreenLess700.timer_margin_top,0);
        //  Utils.setViewWidth(this.mFifthPlayerTimerLayout,GameRoomCustomScreenLess700.timer_size);
        //  Utils.setViewHeight(this.mFifthPlayerTimerLayout,GameRoomCustomScreenLess700.timer_size);

        //  Utils.setViewWidth(this.mFifthPlayerTimerLayout.findViewById(R.id.timer_root_layout),GameRoomCustomScreenLess700.timer_size);
        //  Utils.setViewHeight(this.mFifthPlayerTimerLayout.findViewById(R.id.timer_root_layout),GameRoomCustomScreenLess700.timer_size);

   /*     Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),GameRoomCustomScreenLess700.player_dealer_icon_width);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),GameRoomCustomScreenLess700.player_dealer_icon_height);
        Utils.setViewMargin(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),0,0,GameRoomCustomScreenLess700.opponentCardsMarginTop,0);*/


        RummyUtils.setViewMargin(player_2_root_layout,30,0,60,0);


        RummyUtils.setViewHeight(small_round_box_player_2,35);
        RummyUtils.setViewWidth(small_round_box_player_2,35);

        RummyUtils.setViewMargin(small_round_box_player_2,0,0,0,10);





    }

    private void setSizePlayer3Profile()
    {


        //  Utils.setViewWidth(this.mFifthPlayerLayout,GameRoomCustomScreenLess700.playerBoxWidth);

        //  Utils.setViewHeight(this.mFifthPlayerAutoTimerLayout,GameRoomCustomScreenLess700.playerBoxHeight);


        //******** rummy_player box
        RummyUtils.setViewHeight(this.mThirdPlayerLayout.findViewById(R.id.player_3_box), RummyGameRoomCustomScreenLess700.playerBoxHeight);
        RummyUtils.setViewWidth(this.mThirdPlayerLayout.findViewById(R.id.player_3_box), RummyGameRoomCustomScreenLess700.playerBoxWidth);

    /*    Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_details_main_root_layout),GameRoomCustomScreenLess700.playerBoxWidth);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_details_main_root_layout),GameRoomCustomScreenLess700.playerBoxHeight);*/



        RummyUtils.setViewTextSize(this.mThirdPlayerLayout.findViewById(R.id.player_name_tv), RummyGameRoomCustomScreenLess700.playerProfileTextSize);


        RummyUtils.setViewMargin(mThirdPlayerAutoChunksLayout, 0,0, RummyGameRoomCustomScreenLess700.auto_chunks_margin_top,0);

        RummyUtils.setViewWidth(this.mThirdPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mThirdPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mThirdPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_1), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

        RummyUtils.setViewWidth(this.mThirdPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mThirdPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mThirdPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_2), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

        RummyUtils.setViewWidth(this.mThirdPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewHeight(this.mThirdPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.chunksSize);
        RummyUtils.setViewMargin(this.mThirdPlayerAutoChunksLayout.findViewById(R.id.auto_chunk_3), RummyGameRoomCustomScreenLess700.auto_chunks_margin_left_right,0,0,0);

       /* Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv),GameRoomCustomScreenLess700.player_system_iv_size);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv),GameRoomCustomScreenLess700.player_system_iv_size);
        Utils.setViewMargin(this.mFifthPlayerLayout.findViewById(R.id.player_system_iv), 0,GameRoomCustomScreenLess700.player_system_iv_marginRight,GameRoomCustomScreenLess700.player_system_iv_marginTop,0);

        */

        RummyUtils.setViewWidth(player_3_autoplay_icon, RummyGameRoomCustomScreenLess700.autoPlayIconSizeProfile);
        RummyUtils.setViewHeight(this.player_3_autoplay_icon, RummyGameRoomCustomScreenLess700.autoPlayIconSizeProfile);
        RummyUtils.setViewTextSize(autoplay_count_player_3, RummyGameRoomCustomScreenLess700.autoPlayCountTextSize);

        RummyUtils.setViewWidth(player_3_autoplay_box, RummyGameRoomCustomScreenLess700.autoPlayLayoutWidth);
        RummyUtils.setViewHeight(player_3_autoplay_box, RummyGameRoomCustomScreenLess700.autoPlayLayoutHeight);

        // Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_drop_iv_to_size),GameRoomCustomScreenLess700.dropIvWidth);
        // Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_drop_iv_to_size),GameRoomCustomScreenLess700.dropIvHeight);

        RummyUtils.setViewWidth(this.mThirdPlayerLayout.findViewById(R.id.player_3_cards), RummyGameRoomCustomScreenLess700.opponentCardsWidth);
        RummyUtils.setViewHeight(this.mThirdPlayerLayout.findViewById(R.id.player_3_cards), RummyGameRoomCustomScreenLess700.opponentCardsHeight);
        RummyUtils.setViewMargin(this.mThirdPlayerLayout.findViewById(R.id.player_3_cards), 0,0, RummyGameRoomCustomScreenLess700.opponentCardsMarginTop,0);

        /////////Timer layout

        //  Utils.setViewMargin(this.player_5_timer_root_layout, 0,GameRoomCustomScreenLess700.player5TimerMarginRight,GameRoomCustomScreenLess700.timer_margin_top,0);
        //  Utils.setViewWidth(this.mFifthPlayerTimerLayout,GameRoomCustomScreenLess700.timer_size);
        //  Utils.setViewHeight(this.mFifthPlayerTimerLayout,GameRoomCustomScreenLess700.timer_size);

        //  Utils.setViewWidth(this.mFifthPlayerTimerLayout.findViewById(R.id.timer_root_layout),GameRoomCustomScreenLess700.timer_size);
        //  Utils.setViewHeight(this.mFifthPlayerTimerLayout.findViewById(R.id.timer_root_layout),GameRoomCustomScreenLess700.timer_size);

   /*     Utils.setViewWidth(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),GameRoomCustomScreenLess700.player_dealer_icon_width);
        Utils.setViewHeight(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),GameRoomCustomScreenLess700.player_dealer_icon_height);
        Utils.setViewMargin(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv),0,0,GameRoomCustomScreenLess700.opponentCardsMarginTop,0);*/



        RummyUtils.setViewMargin(player_3_root_layout,0,30,0,0);


        RummyUtils.setViewHeight(small_round_box_player_3,35);
        RummyUtils.setViewWidth(small_round_box_player_3,35);

        RummyUtils.setViewMargin(small_round_box_player_3,0,0,0,10);


    }


    public static boolean isTablet(Context context) {
        if(context != null)
        {
            return (context.getResources().getConfiguration().screenLayout
                    & Configuration.SCREENLAYOUT_SIZE_MASK)
                    >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        }
        else
        {
            return false;
        }

    }

    /*
     *
     */

    public void onDestroy() {
        super.onDestroy();
        cancelTimer(this.meldTimer);
        cancelTimer(this.playerTurnOutTimer);
        cancelTimer(this.mGameScheduleTimer);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        clearData();
    }

    String strJson = "";
    String comma = ",";
    public static ArrayList<String> alTrackList = new ArrayList<String>();

    private void getTrackSharedPrefs() {
        /*
        String[] trackKeys = {"userid", "tableid", "gameid", "subgameid", "startgame",
                "rummy_show", "rummy_drop", "eliminate", "gameend"};*/

        String[] trackKeys = new String[alTrackList.size()];
        trackKeys = alTrackList.toArray(trackKeys);
        comma = ",";
        strJson = "{\"events_list\"" + ": [";
        for (int i = 0; i < trackKeys.length; i++) {
//            Log.e(trackKeys[i]+"",PrefManagerTracker.getString(mContext, trackKeys[i], "NA")+"");
            if (i == trackKeys.length - 1) {
                comma = "";
            }
//            strJson += "\"" + trackKeys[i] + "\"" + ":" + "\"" + PrefManagerTracker.getString(mContext, trackKeys[i], "NA") + "\"" + comma;
            strJson += "{";
            strJson += "\"" + "playerid" + "\"" + ":" + "\"" + RummyPrefManagerTracker.getString(mActivity, "userid", "NA") + "\"" + ",";
            strJson += "\"" + "tableid" + "\"" + ":" + "\"" + RummyPrefManagerTracker.getString(mActivity, "tableid", "NA") + "\"" + ",";
            strJson += "\"" + "gameid" + "\"" + ":" + "\"" + RummyPrefManagerTracker.getString(mActivity, "gameid", "NA") + "\"" + ",";
            strJson += "\"" + "subgameid" + "\"" + ":" + "\"" + RummyPrefManagerTracker.getString(mActivity, "subgameid", "NA") + "\"" + ",";
            strJson += "\"" + "event" + "\"" + ":" + "\"" + trackKeys[i] + "\"" + ",";
            strJson += "\"" + "timestamp" + "\"" + ":" + "\"" + RummyPrefManagerTracker.getString(mActivity, trackKeys[i], "NA") + "\"" + ",";
            strJson += "\"" + "log_level" + "\"" + ":" + "\"" + "inf" + "\"" + ",";
            strJson += "\"" + "chipstype" + "\"" + ":" + "\"" + RummyPrefManagerTracker.getString(mActivity, "chipstype", "NA") + "\"" + ",";
            strJson += "\"" + "bet" + "\"" + ":" + "\"" + RummyPrefManagerTracker.getString(mActivity, "bet", "NA") + "\"" + ",";
            strJson += "\"" + "gametype" + "\"" + ":" + "\"" + RummyPrefManagerTracker.getString(mActivity, "gametype", "NA") + "\"" + "";
            strJson += "}" + comma;
        }
        strJson += "]}";
//        Log.e("strJson", strJson + "");
        try {
            jsonObject = new JSONObject(strJson);
            Log.e("Json", jsonObject.toString());
            threadRequest();
//            Log.e("Json", jsonObject.getString("userid"));
        } catch (Throwable t) {
            Log.e("Throwable", "Could not parse malformed JSON: \"" + strJson + "\"");
        }
        RummyPrefManagerTracker.clearPreferences(mActivity);
    }

    private void threadRequest() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jsonObjectRequest();
                    // The code written in doInBackground()
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    JSONObject jsonObject;

    private void jsonObjectRequest() {
        RequestQueue queue = Volley.newRequestQueue(mActivity);
        // url
        String url = RummyUtils.events_url;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, response.toString());
                        alTrackList.clear();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        queue.add(jsonObjReq);
    }

    public static Context getTableFragment() {

        return mActivity;
    }

    private void clearData() {
        loadDummyCardsView();
        RummyUtils.MELD_REQUEST = null;
        RummyUtils.SHOW_EVENT = null;
        this.game_id_tb.setText("");
        this.isCardPicked = false;
        this.turnCount = 0;
        this.isYourTurn = false;
        this.userNotDeclaredCards = false;
        this.isUserDropped = false;
        isCurrentlyMyTurn = false;
        this.isGameStarted = false;
        this.isTossEventRunning = false;
        this.isCardsDistributing = false;
        this.isMeldFragmentShowing = false;
        this.canLeaveTable = true;
        this.isPlacedShow = false;
        this.opponentValidShow = false;
        dismissQuickMenu();
        this.mSelectedCards.clear();
        this.mIsMelding = false;
        this.isUserPlacedValidShow = false;
        this.faceUpCardList.clear();
        this.faceDownCardList.clear();
        cancelTimer(this.meldTimer);
        cancelTimer(this.mGameScheduleTimer);
        cancelTimer(this.playerTurnOutTimer);
        this.mRummyView.removeViews();
        this.resetAllGroupsCountUI();
        this.mOpenCard.setVisibility(View.INVISIBLE);
        this.mUserTossCard.setVisibility(View.GONE);
        this.mJokerCard.setVisibility(View.GONE);
        this.mFaceDownCards.setVisibility(View.INVISIBLE);
        this.mGameShecduleTv.setVisibility(View.INVISIBLE);
        this.mOpenCard.setVisibility(View.INVISIBLE);
        this.mOpenJokerCard.setVisibility(View.GONE);
        this.mClosedCard.setVisibility(View.INVISIBLE);
        cancelTimer(this.meldTimer);
        cancelTimer(this.playerTurnOutTimer);
        this.mSecondPlayerLayout.setAlpha(1.0f);
        this.mThirdPlayerLayout.setAlpha(1.0f);
        this.mFourthPlayerLayout.setAlpha(1.0f);
        this.mFifthPlayerLayout.setAlpha(1.0f);
        this.mSixthPlayerLayout.setAlpha(1.0f);
        resetPlayerUi(this.mSecondPlayerLayout);
        resetPlayerUi(this.mThirdPlayerLayout);
        resetPlayerUi(this.mFourthPlayerLayout);
        resetPlayerUi(this.mFifthPlayerLayout);
        resetPlayerUi(this.mSixthPlayerLayout);
        resetPlayerUi(this.mUserPlayerLayout);
        disableView(this.mExtraTimeBtn);
        hideView(this.mDeclareBtn);
        hideView(this.mWrongMeldTv);
        setUserOptions(false);
        hideTimerUI();
        this.mGroupList.clear();
        invisibleView(this.mPlayer2Cards);
        invisibleView(this.mPlayer3Cards);
        invisibleView(this.mPlayer4Cards);
        invisibleView(this.mPlayer5Cards);
        invisibleView(this.mPlayer6Cards);
        resetWaitingplayer();
        hideView(this.mReshuffleView);
        sendTurnUpdateMessage(false);
        if (this.userData != null) {
            this.userData.setMiddleJoin(false);   //vikas change
        }

    }

    private void cancelTimer(CountDownTimer timer) {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        RummyCommonEventTracker.trackScreenName(RummyCommonEventTracker.GAME_TABLE_SCREEN, getContext());
    }

    private void setTableData(RummyTableDetails tableDetails) {
        Log.e("setTableData", "setTableData");
        myTableId = tableDetails.getTableId() + "";

        //  alAutoDrop.add(myTableId + "");
        //alAutoDropBoolean.add(false);

        this.mTableId.setText(String.format("%s%s", new Object[]{this.mActivity.getString(R.string.hash), tableDetails.getTableId()}));
        String gameType = RummyUtils.formatTableName(tableDetails.getTableType());
        if (gameType.equalsIgnoreCase("pr - joker"))
            this.mGameType.setText("POINTS");
        else
            this.mGameType.setText(gameType);
        this.mBet.setText(String.format("%s %s", new Object[]{tableDetails.getBet(), RummyUtils.getTableType(tableDetails.getTableCost())}));

      //  crashlytics.setCustomKey("table_id", String.format("%s%s", new Object[]{this.mActivity.getString(R.string.hash), tableDetails.getTableId()}));

        RummyPrefManagerTracker.saveString(getContext(), "gametype", String.format("%s %s", new Object[]{tableDetails.getBet(), RummyUtils.getTableType(tableDetails.getTableCost())}) + "");
        Log.e("gametype", String.format("%s %s", new Object[]{tableDetails.getBet(), RummyUtils.getTableType(tableDetails.getTableCost())}) + "");
        String chipsTypeBet = String.format("%s %s", new Object[]{tableDetails.getBet(), RummyUtils.getTableType(tableDetails.getTableCost())});
        String[] chipsTypeBetAry = chipsTypeBet.split(" ");
        RummyPrefManagerTracker.saveString(getContext(), "bet", chipsTypeBetAry[0] + "");
        Log.e("bet", chipsTypeBetAry[0] + "");
        RummyPrefManagerTracker.saveString(getContext(), "chipstype", chipsTypeBetAry[1] + "");
        Log.e("chipstype", chipsTypeBetAry[1] + "");

        this.tableType = tableDetails.getTableType();
        this.table_cost_type = tableDetails.getTableCost();
        this.betAmount = tableDetails.getBet();

//        Crashlytics.setString("game_type", String.format("%s %s", new Object[]{tableDetails.getBet(), Utils.getTableType(tableDetails.getTableCost())}));
//        Crashlytics.setString("game_bet", chipsTypeBetAry[0]);
//        Crashlytics.setString("chips_type", chipsTypeBetAry[1]);
    }

    private void dispatchCards() {
        new C17265().execute(new Void[0]);
    }

    private void animateDispatchCards() {
        if (this.count >= this.playerCards.size() - 1) {
            this.count = 0;
        }
    }

    private void setUpFullScreen() {
        this.mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setIdsToViews(View v) {

        this.btn_leave_table_game_result = (Button)v.findViewById(R.id.btn_leave_table_game_result);

        this.mPlayer2Cards = (ImageView) v.findViewById(R.id.player_2_cards);
        this.mPlayer3Cards = (ImageView) v.findViewById(R.id.player_3_cards);
        this.mPlayer4Cards = (ImageView) v.findViewById(R.id.player_4_cards);
        this.mPlayer5Cards = (ImageView) v.findViewById(R.id.player_5_cards);
        this.mPlayer6Cards = (ImageView) v.findViewById(R.id.player_6_cards);
        this.mDummyLayout = (RelativeLayout) v.findViewById(R.id.dummy_layout);
        this.mDummyOpenDeckLayout = (RelativeLayout) v.findViewById(R.id.dummy_open_deck_layout);
        this.mUserDiscardLaout = (RelativeLayout) v.findViewById(R.id.user_discard_dummy_layout);
        ((ListView) this.mActivity.findViewById(R.id.settingsListView)).setOnItemClickListener(this);
        this.mSubFragment = (FrameLayout) v.findViewById(R.id.inner_content_frame);
        this.mDialogLayout = (RelativeLayout) v.findViewById(R.id.dialog_layout);
        this.mReshuffleView = v.findViewById(R.id.reshuffle_view);
        hideView(this.mReshuffleView);
        this.winnerView = v.findViewById(R.id.winner_view);
        this.searchGameView = v.findViewById(R.id.search_table_view);
        this.splitRejectedView = v.findViewById(R.id.split_rjected_view);
        this.mDummyLayout = (RelativeLayout) v.findViewById(R.id.dummy_layout);
        this.rl_open_discard = (RelativeLayout) v.findViewById(R.id.rl_open_discard);
        this.mRummyView = (RummyView) v.findViewById(R.id.rummy_view);
        this.mGameShecduleTv = (TextView) v.findViewById(R.id.game_start_time_tv);
        this.mWrongMeldTv = (TextView) v.findViewById(R.id.wrong_meld_tv);
        this.mExtraTimeBtn = (ImageView) v.findViewById(R.id.extra_time_btn);
        this.sortCards = (Button) v.findViewById(R.id.sort_cards);
        this.mDropPlayer = (Button) v.findViewById(R.id.drop_player_iv);
        this.mAutoDropPlayer = v.findViewById(R.id.auto_drop_player_iv);

        //DROP AND GO
        this.mLastGamePlayer = v.findViewById(R.id.cb_last_game);
        this.ll_last_game_checkbox = v.findViewById(R.id.ll_last_game_checkbox);
        this.ll_drop_go_checkbox = v.findViewById(R.id.ll_drop_go_checkbox);
        this.tv_last_game_lable = v.findViewById(R.id.tv_last_game_lable);
        this.tv_drop_and_go_lable = v.findViewById(R.id.tv_drop_and_go_lable);
        this.mDropAndGoPlayer = v.findViewById(R.id.cb_drop_and_go);
        this.last_game_layout_root = v.findViewById(R.id.last_game_layout_root);
        this.last_game_layout = v.findViewById(R.id.last_game_layout);

        this.mShowBtn = (Button) v.findViewById(R.id.show_iv);
        this.mDeclareBtn = (Button) v.findViewById(R.id.declare_iv);
        this.mSettingsBtn = (ImageView) v.findViewById(R.id.settings_btn);
        this.mLeaveTableBtn = (ImageView) v.findViewById(R.id.exit_btn);
        this.mLobbyBtn = (ImageView) v.findViewById(R.id.lobby_back_btn);
        this.tvLobby = (TextView) v.findViewById(R.id.tvAddGame);
        this.mDrawerLayout = (DrawerLayout) this.mActivity.findViewById(R.id.drawer_layout);
        this.mDrawerLayout.addDrawerListener(new C17276());
        this.mClosedCard = (ImageView) v.findViewById(R.id.closed_card_iv);
        this.mUserTimerRootLayout = v.findViewById(R.id.timer_root_ayout);
        this.mUserTimerTv = (TextView) this.mUserTimerRootLayout.findViewById(R.id.player_timer_tv);
        this.mUserAutoTimerRootLayout = v.findViewById(R.id.auto_timer_root_ayout);
        this.mUserAutoTimerTv = (TextView) this.mUserAutoTimerRootLayout.findViewById(R.id.player_auto_timer_tv);
        this.mSecondPlayerAutoTimerLayout = v.findViewById(R.id.player_2_auto_timer_root_ayout);
        this.mSecondPlayerAutoTimerTv = (TextView) this.mSecondPlayerAutoTimerLayout.findViewById(R.id.player_auto_timer_tv);
        this.mThirdPlayerAutoTimerLayout = v.findViewById(R.id.player_3_auto_timer_root_ayout);
        this.mThirdPlayerAutoTimerTv = (TextView) this.mThirdPlayerAutoTimerLayout.findViewById(R.id.player_auto_timer_tv);
        this.mFourthPlayerAutoTimerLayout = v.findViewById(R.id.player_4_auto_timer_root_ayout);
        this.mFourthPlayerAutoTimerTv = (TextView) this.mFourthPlayerAutoTimerLayout.findViewById(R.id.player_auto_timer_tv);
        this.mFifthPlayerAutoTimerLayout = v.findViewById(R.id.player_5_auto_timer_root_ayout);
        this.mFifthPlayerAutoTimerTv = (TextView) this.mFifthPlayerAutoTimerLayout.findViewById(R.id.player_auto_timer_tv);
        this.mSixthPlayerAutoTimerLayout = v.findViewById(R.id.player_6_auto_timer_root_ayout);
        this.mSixthPlayerAutoTimerTv = (TextView) this.mSixthPlayerAutoTimerLayout.findViewById(R.id.player_auto_timer_tv);
        this.mUserAutoChunksLayout = v.findViewById(R.id.player_1_auto_chunks_root_ayout);
        this.mSecondPlayerAutoChunksLayout = v.findViewById(R.id.player_2_auto_chunks_root_ayout);
        this.mThirdPlayerAutoChunksLayout = v.findViewById(R.id.player_3_auto_chunks_root_ayout);
        this.mFourthPlayerAutoChunksLayout = v.findViewById(R.id.player_4_auto_chunks_root_ayout);
        this.mFifthPlayerAutoChunksLayout = v.findViewById(R.id.player_5_auto_chunks_root_ayout);
        this.mSixthPlayerAutoChunksLayout = v.findViewById(R.id.player_6_auto_chunks_root_ayout);
        this.mGameType = (TextView) v.findViewById(R.id.table_type_tv);
        this.mTableId = (TextView) v.findViewById(R.id.table_id_tv);
        this.mGameType = (TextView) v.findViewById(R.id.table_type_tv);
        this.mPrizeMoney = (TextView) v.findViewById(R.id.prize_tv);
        this.mBalanceMoney = (TextView) v.findViewById(R.id.balance_tv);
        this.mBet = (TextView) v.findViewById(R.id.bet_tv);
        this.mSecondPlayerLayout = v.findViewById(R.id.player_2_box);
        this.mThirdPlayerLayout = v.findViewById(R.id.player_3_box);
        this.mFourthPlayerLayout = v.findViewById(R.id.player_4_box);
        this.mFifthPlayerLayout = v.findViewById(R.id.player_5_box);
        this.mSixthPlayerLayout = v.findViewById(R.id.player_6_box);
        this.mUserPlayerLayout = v.findViewById(R.id.user_details_layout);
        this.mUserTossCard = (ImageView) v.findViewById(R.id.user_toss_card);
        this.mPlayer2TossCard = (ImageView) v.findViewById(R.id.player_2_toss_card);
        this.mPlayer3TossCard = (ImageView) v.findViewById(R.id.player_3_toss_card);
        this.mPlayer4TossCard = (ImageView) v.findViewById(R.id.player_4_toss_card);
        this.mPlayer5TossCard = (ImageView) v.findViewById(R.id.player_5_toss_card);
        this.mPlayer6TossCard = (ImageView) v.findViewById(R.id.player_6_toss_card);
        this.mOpenCard = (ImageView) v.findViewById(R.id.open_deck_card_iv);
        this.mOpenJokerCard = (ImageView) v.findViewById(R.id.open_jocker_iv);
        this.mJokerCard = (ImageView) v.findViewById(R.id.jokerImageView);
        this.mFaceDownCards = (ImageView) v.findViewById(R.id.faceDownCards);
        this.mSecondPlayerTimerLayout = v.findViewById(R.id.player_2_timer_layout);
        this.mSecondPlayerTimerTv = (TextView) this.mSecondPlayerTimerLayout.findViewById(R.id.player_timer_tv);
        this.mThirdPlayerTimerLayout = v.findViewById(R.id.player_3_timer_layout);
        this.mThirdPlayerTimerTv = (TextView) this.mThirdPlayerTimerLayout.findViewById(R.id.player_timer_tv);
        this.mFourthPlayerTimerLayout = v.findViewById(R.id.player_4_timer_layout);
        this.mFourthPlayerTimerTv = (TextView) this.mFourthPlayerTimerLayout.findViewById(R.id.player_timer_tv);
        this.mFifthPlayerTimerLayout = v.findViewById(R.id.player_5_timer_layout);
        this.mFifthPlayerTimerTv = (TextView) this.mFifthPlayerTimerLayout.findViewById(R.id.player_timer_tv);
        this.mSixthPlayerTimerLayout = v.findViewById(R.id.player_6_timer_layout);
        this.mSixthPlayerTimerTv = (TextView) this.mSixthPlayerTimerLayout.findViewById(R.id.player_timer_tv);
        this.mGameResultsView = v.findViewById(R.id.game_results_view);
        this.mMeldCardsView = v.findViewById(R.id.meld_cards_view);
        this.mSmartCorrectionView = v.findViewById(R.id.sc_view);
        this.mRummyView.setTableFragment(this);
        this.mGameDeckLayout = (LinearLayout) v.findViewById(R.id.game_deck_layout);
        this.mGameLogoIv = (ImageView) v.findViewById(R.id.game_taj_logo_iv);
        this.levelTimerLayout = (LinearLayout) v.findViewById(R.id.levelTimerLayout);
        this.tourneyBar = (RelativeLayout) v.findViewById(R.id.tourneyBar);
        this.tourney_expanded_layout = (LinearLayout) v.findViewById(R.id.tourney_expanded_layout);
        this.normal_game_bar = (RelativeLayout) v.findViewById(R.id.normal_game_bar);
        this.levelTimerValue = (TextView) v.findViewById(R.id.levelTimerValue);
        this.level_number_tv = (TextView) v.findViewById(R.id.level_number_tv);
        this.expandTourneyInfo = (ImageView) v.findViewById(R.id.expandTourneyInfo);
        this.collapseTourneyInfo = (ImageView) v.findViewById(R.id.collapseTourneyInfo);

        this.tourney_type_tv = (TextView) v.findViewById(R.id.tourney_type_tv);
        this.entry_tourney_tv = (TextView) v.findViewById(R.id.entry_tourney_tv);
        this.bet_tourney_tv = (TextView) v.findViewById(R.id.bet_tourney_tv);
        this.rebuy_tourney_tv = (TextView) v.findViewById(R.id.rebuy_tourney_tv);
        this.rank_tourney_tv = (TextView) v.findViewById(R.id.rank_tourney_tv);
        this.balance_tourney_tv = (TextView) v.findViewById(R.id.balance_tourney_tv);
        this.tourney_prize_tv = (TextView) v.findViewById(R.id.tourney_prize_tv);
        this.game_level_tv = (TextView) v.findViewById(R.id.game_level_tv);

        this.game_start_time_tv = (TextView) v.findViewById(R.id.game_start_time_tv);

        this.tid_tourney_tv = (TextView) v.findViewById(R.id.tid_tourney_tv);
        this.gameid_tourney_tv = (TextView) v.findViewById(R.id.gameid_tourney_tv);
        this.game_id_tb = (TextView) v.findViewById(R.id.game_id_tb);

        this.player_2_autoplay_box = (LinearLayout) v.findViewById(R.id.player_2_autoplay_box);
        this.player_3_autoplay_box = (LinearLayout) v.findViewById(R.id.player_3_autoplay_box);
        this.player_4_autoplay_box = (LinearLayout) v.findViewById(R.id.player_4_autoplay_box);
        this.player_5_autoplay_box = (LinearLayout) v.findViewById(R.id.player_5_autoplay_box);
        this.player_6_autoplay_box = (LinearLayout) v.findViewById(R.id.player_6_autoplay_box);

        this.autoplay_count_player_2 = (TextView) v.findViewById(R.id.autoplay_count_player_2);
        this.autoplay_count_player_3 = (TextView) v.findViewById(R.id.autoplay_count_player_3);
        this.autoplay_count_player_4 = (TextView) v.findViewById(R.id.autoplay_count_player_4);
        this.autoplay_count_player_5 = (TextView) v.findViewById(R.id.autoplay_count_player_5);
        this.autoplay_count_player_6 = (TextView) v.findViewById(R.id.autoplay_count_player_6);

        this.iv_bet_icon = v.findViewById(R.id.iv_bet_icon);
        this.iv_balance_icon = v.findViewById(R.id.iv_balance_icon);

        this.group_1 = v.findViewById(R.id.group_1);
        this.group_2 = v.findViewById(R.id.group_2);
        this.group_3 = v.findViewById(R.id.group_3);
        this.group_4 = v.findViewById(R.id.group_4);
        this.group_5 = v.findViewById(R.id.group_5);
        this.group_6 = v.findViewById(R.id.group_6);
        this.group_7 = v.findViewById(R.id.group_7);
        this.group_8 = v.findViewById(R.id.group_8);

        this.rl_groups_container = v.findViewById(R.id.rl_groups_container);

        this.players_join_status_layout = v.findViewById(R.id.players_join_status_layout);
        this.player_join_status_tv = v.findViewById(R.id.player_join_status_tv);


        this.clResumeGame =  v.findViewById(R.id.layout_resume_game);
        this.resumeShadow =  v.findViewById(R.id.resumeShadow);
        this.tvResume =  clResumeGame.findViewById(R.id.tvResume);
        this.tvResumeTitle =  clResumeGame.findViewById(R.id.tvResumeTitle);

        /*
         *  Added after dynamic layout
         */


        this.game_room_top_bar = (LinearLayout)v.findViewById(R.id.game_room_top_bar);
        this.ll_tourney_top_bar = (LinearLayout)v.findViewById(R.id.ll_tourney_top_bar);


        this.game_room_top_bar = (LinearLayout)v.findViewById(R.id.game_room_top_bar);
        this.ll_tourney_top_bar = (LinearLayout)v.findViewById(R.id.ll_tourney_top_bar);
        this.tid_tourney_label_tv = (TextView)v.findViewById(R.id.tid_tourney_label_tv);
        this.gameid_tourney_label_tv = (TextView)v.findViewById(R.id.gameid_tourney_label_tv);
        this.tv_more_info_tourney = (TextView)v.findViewById(R.id.tv_more_info_tourney);


        this.ll_bottom_bar  = (LinearLayout)v.findViewById(R.id.ll_bottom_bar);
       // this.iv_ruppe_icon_top_bar_game_room = (ImageView)v.findViewById(R.id.iv_ruppe_icon_top_bar_game_room);
       // this.iv_prize_icon_top_bar_game_room = (ImageView)v.findViewById(R.id.iv_prize_icon_top_bar_game_room);
        this.rl_close_deck_main_container = (RelativeLayout)v.findViewById(R.id.rl_close_deck_main_container);
        this.rl_close_card_container = (RelativeLayout)v.findViewById(R.id.rl_close_card_container);
        this.rl_open_deck_container = (RelativeLayout)v.findViewById(R.id.rl_open_deck);
        this.iv_rdl_logo = (ImageView)v.findViewById(R.id.iv_rdl_logo);

        this.ll_rummy_view_container = v.findViewById(R.id.ll_rummy_view_container);



     /*   this.player_4_rank_layout = v.findViewById(R.id.player_4_rank_layout);
        this.player_5_rank_layout = v.findViewById(R.id.player_5_rank_layout);
        this.player_6_rank_layout = v.findViewById(R.id.player_6_rank_layout);
        this.player_3_rank_layout = v.findViewById(R.id.player_3_rank_layout);
        this.player_2_rank_layout = v.findViewById(R.id.player_2_rank_layout);
        this.player_1_rank_layout = v.findViewById(R.id.player_1_rank_layout);*/



        circularProgressBar_player1 = mUserPlayerLayout.findViewById(R.id.circularProgressBar);


         player2Layout = v.findViewById(R.id.player_2);
        circularProgressBar_player2 = player2Layout.findViewById(R.id.circularProgressBar);

         player3Layout = v.findViewById(R.id.player_3);
        circularProgressBar_player3 = player3Layout.findViewById(R.id.circularProgressBar);

         player4Layout = v.findViewById(R.id.player_4);
        circularProgressBar_player4 =player4Layout.findViewById(R.id.circularProgressBar);

         player5Layout = v.findViewById(R.id.player_5);
        circularProgressBar_player5 =  player5Layout.findViewById(R.id.circularProgressBar);

         player6Layout = v.findViewById(R.id.player_6);
        circularProgressBar_player6 =  player6Layout.findViewById(R.id.circularProgressBar);


        small_round_box_player_1 = v.findViewById(R.id.small_round_box_player_1);
        small_round_box_player_2 = v.findViewById(R.id.small_round_box_player_2);
        small_round_box_player_3 = v.findViewById(R.id.small_round_box_player_3);
        small_round_box_player_4 = v.findViewById(R.id.small_round_box_player_4);
        small_round_box_player_5 = v.findViewById(R.id.small_round_box_player_5);
        small_round_box_player_6 = v.findViewById(R.id.small_round_box_player_6);


      //  player_1_autoplay_icon = v.findViewById(R.id.player_1_autoplay_icon);
        player_2_autoplay_icon = v.findViewById(R.id.player_2_autoplay_icon);
        player_3_autoplay_icon = v.findViewById(R.id.player_3_autoplay_icon);
        player_4_autoplay_icon = v.findViewById(R.id.player_4_autoplay_icon);
        player_5_autoplay_icon = v.findViewById(R.id.player_5_autoplay_icon);
        player_6_autoplay_icon = v.findViewById(R.id.player_6_autoplay_icon);

        player_1_root_layout = v.findViewById(R.id.player_1_root_layout);
        player_2_root_layout = v.findViewById(R.id.player_2_root_layout);
        player_3_root_layout = v.findViewById(R.id.player_3_root_layout);
        player_4_root_layout = v.findViewById(R.id.player_4_root_layout);
        player_5_root_layout = v.findViewById(R.id.player_5_root_layout);
        player_6_root_layout = v.findViewById(R.id.player_6_root_layout);


        //psp
        this.secure_bottom_view = v.findViewById(R.id.secure_bottom_view);
        this.bottom_actions = v.findViewById(R.id.bottom_actions);
        //this.private_club_layout = v.findViewById(R.id.private_club_layout);
        //this.resultPrivateBadge = v.findViewById(R.id.resultPrivateBadge);

        //psp


        /*
         *
         */
    }

    private void setListnersToViews() {
        this.sortCards.setOnClickListener(this);
        this.mSettingsBtn.setOnClickListener(this);
        this.mFaceDownCards.setOnClickListener(this);
        this.mOpenCard.setOnClickListener(this);
        this.mDropPlayer.setOnClickListener(this);
        this.mLeaveTableBtn.setOnClickListener(this);
        this.mShowBtn.setOnClickListener(this);
        this.mDeclareBtn.setOnClickListener(this);
        this.mLobbyBtn.setOnClickListener(this);
        this.tvLobby.setOnClickListener(this);
        this.mExtraTimeBtn.setOnClickListener(this);
        this.expandTourneyInfo.setOnClickListener(this);
        this.collapseTourneyInfo.setOnClickListener(this);
        this.tourney_expanded_layout.setOnClickListener(this);
        this.rl_open_discard.setOnClickListener(this);

        this.btn_leave_table_game_result.setOnClickListener(this);
        mAutoDropPlayer.setPadding(20,0,0,0);
        mAutoDropPlayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                invisibleView(mDropPlayer);
                if(isChecked)
                {
                    showAutoDropDialog();
                }
                else
                {
                    setAutoDropSetting(isChecked);
                }
            }
        });

        this.ll_last_game_checkbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RummyTablesFragment.this.mLastGamePlayer.isChecked()) {
                    RummyTablesFragment.this.mLastGamePlayer.setChecked(false);
                } else {
                    showLastGameConfirmDialog();

                }
            }
        });

        this.ll_drop_go_checkbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RummyTablesFragment.this.mDropAndGoPlayer.isChecked()) {
                    /*RummyTablesFragment.this.mDropAndGoPlayer.setChecked(false);
                    clResumeGame.setVisibility(View.GONE); //user have to press resume button*/
                } else {
                    if(!isYourTurn){
                    showDropGoConfirmDialog();
                    }else {
                        Toast.makeText(getActivity(),"You can not select Drop & Go on your turn",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        tvResume.setOnClickListener(v -> {
            hideResumeLayout();
        });
    }

   /* private void showAutoDropDialog()
    {
        this.dropDialog = getLeaveTableDialog(this.mActivity, this.mActivity.getString(R.string.auto_drop_game_msg));
        LinearLayout ll_drop_move_confirmation = this.dropDialog.findViewById(R.id.ll_drop_move_confirmation);
        final CheckBox cb_drop_move = this.dropDialog.findViewById(R.id.cb_drop_move);

        if(this.mTableDetails != null
                && this.mTableDetails.getMaxPlayer().equalsIgnoreCase("6")
                && this.mTableDetails.getTableType().equalsIgnoreCase(RummyUtils.PR_JOKER)
                && !strIsTourneyTable.equalsIgnoreCase("yes"))
        {
            cb_drop_move.setChecked(false);
            isMoveToOtherTable = false;
            ll_drop_move_confirmation.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cb_drop_move.isChecked())
                    {
                        cb_drop_move.setChecked(false);
                        isMoveToOtherTable = false;
                    }
                    else
                    {
                        cb_drop_move.setChecked(true);
                        isMoveToOtherTable = true;
                    }
                }
            });

           // showView(ll_drop_move_confirmation);
        }
        else
        {
            hideView(ll_drop_move_confirmation);
        }

        ((TextView) this.dropDialog.findViewById(R.id.dialog_msg_tv)).setText(getResources().getString(R.string.auto_drop_game_msg));
        ((Button) this.dropDialog.findViewById(R.id.yes_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RummyTablesFragment.this.dropDialog.dismiss();
                setAutoDropSetting(true);
            }
        });
        ((Button) this.dropDialog.findViewById(R.id.no_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RummyTablesFragment.this.dropDialog.dismiss();
                setAutoDropSetting(false);

            }
        });
        this.dropDialog.show();
    }*/

    private void showAutoDropDialog() {
        this.dropDialog = new RummyTableConfirmationDialog(this.mActivity,
                this.mActivity.getString(R.string.auto_drop_game_msg));

        if (this.mTableDetails != null
                && this.mTableDetails.getMaxPlayer().equalsIgnoreCase("6")
                && this.mTableDetails.getTableType().equalsIgnoreCase(RummyUtils.PR_JOKER)
                && !isTourneyTable()) {
            dropDialog.setDropMoveCheckBox(true);
            RummyTablesFragment.this.isMoveToOtherTable = true;
            dropDialog.moveConfirmationClickListener(v -> {
                if (dropDialog.isDropAndMoveChecked()) {
                    dropDialog.setDropMoveCheckBox(false);
                    RummyTablesFragment.this.isMoveToOtherTable = false;
                } else {
                    dropDialog.setDropMoveCheckBox(true);
                    RummyTablesFragment.this.isMoveToOtherTable = true;
                }
            });

        } else {
            dropDialog.moveConfirmationClickListener(null);
        }

        //dropDialog.setTitle("Drop Table?");
        this.dropDialog.setYesClickListener(v -> setAutoDropSetting(true));
        this.dropDialog.setNoAndCloseClickListener(v -> setAutoDropSetting(false));
        this.dropDialog.show();
    }





    private void setAutoDropCheck(boolean isChecked) {
        Log.e("myTableId", myTableId + "");
        mAutoDropPlayer.setChecked(isChecked);
    }

    private void showLastGameConfirmDialog() {
        final RummyTableConfirmationDialog dialog
                = new RummyTableConfirmationDialog(getContext(),"Are you sure you want to leave the\ntable before next game begins?" );
        dialog.setYesClickListener(v -> {
            RummyTablesFragment.this.mLastGamePlayer.setChecked(true);
            dialog.dismiss();

        });

        dialog.setNoAndCloseClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void discardToDiscard(View selectedCardIv){

        int numberOfCards = RummyTablesFragment.this.getTotalCards();
        if(isYourTurn && numberOfCards>13) {
            selectedCardIv.setVisibility(View.GONE);
            RummyTablesFragment.this.showView(selectedCardIv);
            String discardSuit = null;
            String disCardFace = null;

            Iterator playingCards = this.mGroupList.iterator();
            while (playingCards.hasNext()){
                ArrayList<RummyPlayingCard> groupList = (ArrayList) playingCards.next();
                Iterator it2 = groupList.iterator();
                while (it2.hasNext()) {
                    RummyPlayingCard playingCard = (RummyPlayingCard) it2.next();
                    if (selectedCardIv.getTag().toString().equalsIgnoreCase(String.format("%s%s-%s",
                            playingCard.getSuit(), playingCard.getFace(), playingCard.getIndex()))) {
                        RummyTablesFragment.this.mSelectedCards.remove(playingCard);
                        this.mGameShecduleTv.setVisibility(View.INVISIBLE);
                        discardSuit = playingCard.getSuit();
                        disCardFace = playingCard.getFace();
                        groupList.remove(playingCard);
                        clearSelectedCards();
                        setGroupView(false);
                        discardCard(discardSuit, disCardFace);
                        isYourTurn = false;
                        break;
                    }
                }

            }
            selectedCardIv.setVisibility(View.VISIBLE);

        }
    }



    private void showDropGoConfirmDialog() {
        final RummyTableConfirmationDialog dialog
                = new RummyTableConfirmationDialog(getContext(),"Are you sure you want to drop\nthe current game and go to lobby?" );
        dialog.setYesClickListener(v -> {
            RummyTablesFragment.this.mDropAndGoPlayer.setChecked(true);
            clResumeGame.setVisibility(View.VISIBLE);
            showResumeShadow();
            dialog.dismiss();



        });
        dialog.setNoAndCloseClickListener(v -> dialog.dismiss());
        dialog.show();

    }

    private void setAutoDropSetting(boolean isChecked) {
        Log.e("myTableId", myTableId + "");
        //  Log.e("alAutoDrop", alAutoDrop.size() + "");
        //  Log.e("alAutoDropBoolean", alAutoDropBoolean.size() + "");

        //   int intIndex = alAutoDrop.indexOf(myTableId);
        //   Log.e("intIndex", intIndex + "");
        //   alAutoDropBoolean.set(intIndex, isChecked);

        autoDropPlayerFlag = isChecked;
        //mAutoDropPlayer.setChecked(isChecked);
        mAutoDropPlayer.setVisibility(View.GONE);
        if(isChecked) {
            clResumeGame.setVisibility(View.VISIBLE);
            showResumeShadow();

        }else {
            //mAutoDropPlayer.setVisibility(View.VISIBLE);
            // enableView(mAutoDropPlayer);
        }

    }

    private String getGameId() {
        Log.e("game_id_tb", game_id_tb.getText() + "");
        String gameId = game_id_tb.getText() + "";
        String[] gameIdArray = gameId.split("-");
        gameId = gameIdArray[0];
        return gameId;
    }

    public boolean isActionPerformed() {
        return this.actionPerformed;
    }

    public void onClick(View v) {
        this.actionPerformed = true;
        int id = v.getId();
        if (id == R.id.tvAddGame || id == R.id.lobby_back_btn) {
            dismissQuickMenu();
            ((RummyTableActivity) this.mActivity).setIsBackPressed(true);
            RummyUtils.SHOW_LOBBY = true;
            ((RummyBaseActivity) this.mActivity).showLobbyScreen();
            //((TableActivity) this.mActivity).setUpAddGameLayout();
            return;
        } else if (id == R.id.rl_open_discard) {
            ((RummyTableActivity) this.mActivity).setUpPlayerDiscards();
            return;
        } else if (id == R.id.faceDownCards) {
            dismissQuickMenu();
            this.mGroupList = this.mRummyView.getUpdatedCardsGroup();
            if (this.mGroupList != null && this.mGroupList.size() > 0) {
                pickCardFromClosedDeck();
                return;
            }
            return;
        } else if (id == R.id.open_deck_card_iv) {
            dismissQuickMenu();
            this.mGroupList = this.mRummyView.getUpdatedCardsGroup();
            if (this.mGroupList != null && this.mGroupList.size() > 0) {
                pickCardFromOpenDeck();
                return;
            }
            return;
        } else if (id == R.id.exit_btn) {
            dismissQuickMenu();
            clearSelectedCard();
            int totalNoOfCard = getTotalCards();
            if (this.strIsTourneyTable.equalsIgnoreCase("yes")) {
                if (RummyTablesFragment.this.tableId != null) {
                    if (this.isTourneyEnd) {
                        Log.e("vikas", "is tourney end");
                        //RummyTablesFragment.this.mActivity.finish();
                        onLeaveTableSuccess();
                    } else if (this.userData.isMiddleJoin() || (this.canLeaveTable && totalNoOfCard <= 13)) {
                        Log.e("vikas", "is tourney end else part");
                        RummyTablesFragment.this.showLeaveTourneyDialog();
                    } else {
                        String message;
                        if (totalNoOfCard == 14) {
                            message = getString(R.string.leave_table_pick_card);
                        } else if (this.isTossEventRunning) {
                            message = getString(R.string.leave_table_toss);
                        } else if (this.isCardsDistributing) {
                            message = getString(R.string.leave_table_card_distribution);
                        } else if (this.isUserPlacedValidShow) {
                            message = getString(R.string.leave_table_show);
                        } else {
                            message = getString(R.string.leave_table_info);
                        }
                        showGenericDialogTF(this.mActivity, message);
                    }
                } else {
                    //RummyTablesFragment.this.mActivity.finish();
                    onLeaveTableSuccess();
                }
            } else {

                if (this.isUserDropped) {
                    showLeaveTableDialog();
                    return;
                } else if (this.userData != null && (this.userData.isMiddleJoin() || (this.canLeaveTable && totalNoOfCard <= 13))) {
                    showLeaveTableDialog();
                    return;
                } else {
                    String message;
                    if (totalNoOfCard == 14) {
                        message = getString(R.string.leave_table_pick_card);
                    } else if (this.isTossEventRunning) {
                        message = getString(R.string.leave_table_toss);
                    } else if (this.isCardsDistributing) {
                        message = getString(R.string.leave_table_card_distribution);
                    } else if (this.isUserPlacedValidShow) {
                        message = getString(R.string.leave_table_show);
                    } else {
                        message = getString(R.string.leave_table_info);
                    }
                    showGenericDialogTF(this.mActivity, message);
                }
            }
            return;
        } else if (id == R.id.settings_btn) {
            toggleNavigationMenu();
            return;
        } else if (id == R.id.extra_time_btn) {
            turnExtraTime();
            return;
        } else if (id == R.id.sort_cards) {
            dismissQuickMenu();
            clearSelectedCard();
            sortPlayerCards();
            return;
        } else if (id == R.id.drop_player_iv) {
            //disableView(mDropPlayer);
            dismissQuickMenu();
            showDropDialog();
            return;
        } else if (id == R.id.show_iv) {//                mBet.setText(cardsSize);
            disableView(mShowBtn);
            this.userPlacedShow = true;
            //this.groupCards();
            RummySoundPoolManager.getInstance().playSound(R.raw.rummy_toss);
            RummyVibrationManager.getInstance().vibrate(1);
            this.mRummyView.getPlayerDiscardCard().setVisibility(View.INVISIBLE);
            showPlaceShowDialog(v);
            return;
        } else if (id == R.id.declare_iv) {
            RummySoundPoolManager.getInstance().playSound(R.raw.rummy_toss);
            RummyVibrationManager.getInstance().vibrate(1);
            this.mRummyView.getPlayerDiscardCard().setVisibility(View.INVISIBLE);
            launchMeldFragment();
            return;
        } else if (id == R.id.expandTourneyInfo) {
            animateTourneyInfo(true);
            return;
        } else if (id == R.id.collapseTourneyInfo) {
            animateTourneyInfo(false);
            return;
        } else if (id == R.id.tourney_expanded_layout) {
            if (this.isTourneyBarVisible)
                animateTourneyInfo(false);
            else
                animateTourneyInfo(true);
            return;
        }
        else if(id == R.id.btn_leave_table_game_result)
        {
            dismissQuickMenu();
            int totalNoOfCard = getTotalCards();
            if (this.strIsTourneyTable.equalsIgnoreCase("yes")) {
                if (RummyTablesFragment.this.tableId != null) {
                    if (this.isTourneyEnd)
                        //RummyTablesFragment.this.mActivity.finish();
                        onLeaveTableSuccess();
                    else if (this.userData.isMiddleJoin() || (this.canLeaveTable && totalNoOfCard <= 13))
                        RummyTablesFragment.this.showLeaveTourneyDialog();
                    else {
                        String message;
                        if (totalNoOfCard == 14) {
                            message = getString(R.string.leave_table_pick_card);
                        } else if (this.isTossEventRunning) {
                            message = getString(R.string.leave_table_toss);
                        } else if (this.isCardsDistributing) {
                            message = getString(R.string.leave_table_card_distribution);
                        } else if (this.isUserPlacedValidShow) {
                            message = getString(R.string.leave_table_show);
                        } else {
                            message = getString(R.string.leave_table_info);
                        }
                        showGenericDialog(this.mActivity, message);
                    }
                } else {
                    //RummyTablesFragment.this.mActivity.finish();
                    onLeaveTableSuccess();
                }
            } else {

                if (this.isUserDropped) {
                    showLeaveTableDialog();
                    return;
                } else if (this.userData.isMiddleJoin() || (this.canLeaveTable && totalNoOfCard <= 13)) {
                    showLeaveTableDialog();
                    return;
                } else {
                    String message;
                    if (totalNoOfCard == 14) {
                        message = getString(R.string.leave_table_pick_card);
                    } else if (this.isTossEventRunning) {
                        message = getString(R.string.leave_table_toss);
                    } else if (this.isCardsDistributing) {
                        message = getString(R.string.leave_table_card_distribution);
                    } else if (this.isUserPlacedValidShow) {
                        message = getString(R.string.leave_table_show);
                    } else {
                        message = getString(R.string.leave_table_info);
                    }
                    showGenericDialog(this.mActivity, message);
                }
            }
        }
        return;
    }

    private void animateTourneyInfo(boolean showView) {
        tourney_expanded_layout.post(() -> {
            Rect rect = new Rect();
            tourney_expanded_layout.getLocalVisibleRect(rect);
            tourneyInfoMaxHeight = Math.max(tourneyInfoMaxHeight,Math.abs(rect.bottom-rect.top));
            RummySlideAnimation animation;
            if (showView) {
                animation = new RummySlideAnimation(tourney_expanded_layout, 0, tourneyInfoMaxHeight);
                showView(collapseTourneyInfo);
                hideView(expandTourneyInfo);
                isTourneyBarVisible = true;
            } else {
                animation = new RummySlideAnimation(tourney_expanded_layout, tourneyInfoMaxHeight, 0);
                hideView(collapseTourneyInfo);
                showView(expandTourneyInfo);
                isTourneyBarVisible = false;
            }

            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(400);
            tourney_expanded_layout.setAnimation(animation);
            tourney_expanded_layout.startAnimation(animation);
        });
       
    }

    private void requestSplit() {
        cancelTimer(this.mGameScheduleTimer);
        removeGameResultFragment();
        this.isSplitRequested = true;
        RummySplitRequest request = new RummySplitRequest();
        request.setUuid(RummyUtils.generateUuid());
        request.setTableId(this.tableId);
        request.setCommand("split");
        request.setUserId(this.userData.getUserId());
        request.setNick_name(this.userData.getNickName());
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(mActivity, RummyUtils.getObjXMl(request), this.splitListner);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            RummyTLog.d(TAG, "doMelds" + gameEngineNotRunning.getLocalizedMessage());
        }
    }

    public void showLeaveTableDialog() {
        if (this.mActivity != null && !this.mActivity.isFinishing()) {
            this.mLeaveDialog = getLeaveTableDialog(this.mActivity, this.mActivity.getString(R.string.leave_table_msg));
            this.mLeaveDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            ((TextView) this.mLeaveDialog.findViewById(R.id.tv_title_leave)).setVisibility(View.VISIBLE);
            ((Button) this.mLeaveDialog.findViewById(R.id.yes_btn)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    RummyTablesFragment.this.mLeaveDialog.dismiss();
                    if (RummyTablesFragment.this.tableId != null) {
                        if (RummyTablesFragment.this.strIsTourneyTable.equalsIgnoreCase("yes"))
                            RummyTablesFragment.this.leaveTournament();
                        else
                            RummyTablesFragment.this.leaveTable();
                    } else {
                        //RummyTablesFragment.this.mActivity.finish();
                        onLeaveTableSuccess();
                    }

                }
            });
            ((Button) this.mLeaveDialog.findViewById(R.id.no_btn)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    RummyTablesFragment.this.mLeaveDialog.dismiss();
                }
            });
            this.mLeaveDialog.show();

            this.mLeaveDialog.getWindow().getDecorView().setSystemUiVisibility(
                    mActivity.getWindow().getDecorView().getSystemUiVisibility());

            this.mLeaveDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        }
    }

    public void showLeaveTourneyDialog() {
        this.mLeaveDialog = getLeaveTableDialog(this.mActivity, this.mActivity.getString(R.string.leave_table_msg));
        this.mLeaveDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        ((TextView) this.mLeaveDialog.findViewById(R.id.tv_title_leave)).setVisibility(View.VISIBLE);
        ((Button) this.mLeaveDialog.findViewById(R.id.yes_btn)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                RummyTablesFragment.this.mLeaveDialog.dismiss();
                if (RummyTablesFragment.this.tableId != null) {
                    RummyTablesFragment.this.leaveTournament();
                } else {
                    //RummyTablesFragment.this.mActivity.finish();
                    onLeaveTableSuccess();
                }
            }
        });
        ((Button) this.mLeaveDialog.findViewById(R.id.no_btn)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                RummyTablesFragment.this.mLeaveDialog.dismiss();
            }
        });
        this.mLeaveDialog.show();

        this.mLeaveDialog.getWindow().getDecorView().setSystemUiVisibility(
                mActivity.getWindow().getDecorView().getSystemUiVisibility());

        this.mLeaveDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    @SuppressLint("WrongConstant")
    private void toggleNavigationMenu() {
        if (this.mDrawerLayout.isDrawerOpen(5)) {
            this.mDrawerLayout.closeDrawer(5);
            return;
        }
        this.mDrawerLayout.openDrawer(5);
        dismissQuickMenu();
        clearSelectedCard();
    }

    private void pickCardFromClosedDeck() {
        int numberOfCards = getTotalCards();
        if (canPickCard(numberOfCards) && numberOfCards < 14) {
            RummyTablesFragment.this.disableDropButton(RummyTablesFragment.this.mDropPlayer);
            RummySoundPoolManager.getInstance().playSound(R.raw.pick_discard);
            RummyVibrationManager.getInstance().vibrate(1);
            clearSelectedCards();
            if (this.faceDownCardList != null && this.faceDownCardList.size() > 0) {
                RummyPlayingCard playerCard = (RummyPlayingCard) this.faceDownCardList.get(this.faceDownCardList.size() - 1);
                cardPick(playerCard.getSuit(), playerCard.getFace(), "face_down");
                addCardToStack(playerCard);
                this.faceDownCardList.remove(this.faceDownCardList.size() - 1);
            }
        }
        else {
            clearSelectedCard();
        }
    }

    private void pickCardFromOpenDeck() {
        int totalCards = getTotalCards();
        if (canPickCard(totalCards) && totalCards < 14) {
            RummyPlayingCard playerCard = (RummyPlayingCard) this.faceUpCardList.get(this.faceUpCardList.size() - 1);
            if (this.isCardPicked) {
                Log.w(TAG, "Not the first card");
                if (this.mJockerCard != null && ((this.mJockerCard.getFace().equalsIgnoreCase(playerCard.getFace()) && this.turnCount > 1) || (this.mJockerCard.getFace().equalsIgnoreCase(playerCard.getFace()) && this.turnCount <= 1 && this.faceUpCardList.size() > 1))) {
                    showJokerInfo();
                } else if (this.turnCount > 1 && playerCard.getFace().equalsIgnoreCase("0")) {
                    showJokerInfo();
                } else if (this.turnCount <= 1 && playerCard.getFace().equalsIgnoreCase("0") && this.faceUpCardList.size() > 1) {
                    showJokerInfo();
                } else if (this.turnCount > 1 && this.mJockerCard.getFace().equalsIgnoreCase("0") && playerCard.getFace().equalsIgnoreCase("1")) {
                    showJokerInfo();
                } else if (this.turnCount > 1 || !this.mJockerCard.getFace().equalsIgnoreCase("0") || !playerCard.getFace().equalsIgnoreCase("1") || this.faceUpCardList.size() <= 1) {
                    clearSelectedCards();
                    RummySoundPoolManager.getInstance().playSound(R.raw.pick_discard);
                    RummyVibrationManager.getInstance().vibrate(1);
                    this.mGameShecduleTv.setVisibility(View.INVISIBLE);
                    cardPick(playerCard.getSuit(), playerCard.getFace(), "face_up");
                    RummyTablesFragment.this.disableDropButton(RummyTablesFragment.this.mDropPlayer);
                    addCardToStack(playerCard);
                    this.faceUpCardList.remove(this.faceUpCardList.size() - 1);
                    if (this.faceUpCardList.size() > 0) {
                        setOpenCard((RummyPlayingCard) this.faceUpCardList.get(this.faceUpCardList.size() - 1));
                        return;
                    }
                    this.mOpenJokerCard.setVisibility(View.INVISIBLE);
                    this.mOpenCard.setVisibility(View.INVISIBLE);
                } else {
                    showJokerInfo();
                }
            } else {
                Log.w(TAG, "Picking first card");
                clearSelectedCards();
                RummySoundPoolManager.getInstance().playSound(R.raw.pick_discard);
                RummyVibrationManager.getInstance().vibrate(1);
                this.mGameShecduleTv.setVisibility(View.INVISIBLE);
                cardPick(playerCard.getSuit(), playerCard.getFace(), "face_up");
                addCardToStack(playerCard);
                this.faceUpCardList.remove(this.faceUpCardList.size() - 1);
                if (this.faceUpCardList.size() > 0) {
                    setOpenCard((RummyPlayingCard) this.faceUpCardList.get(this.faceUpCardList.size() - 1));
                    return;
                }
                this.mOpenJokerCard.setVisibility(View.INVISIBLE);
                this.mOpenCard.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            clearSelectedCard();
        }
    }

    private void showJokerInfo() {
        this.mGameShecduleTv.setVisibility(View.VISIBLE);
        this.mGameShecduleTv.setText(this.mActivity.getString(R.string.joker_card_pick_info_msg));
    }

    public void addCardToStack(RummyPlayingCard playerCard) {
        ArrayList<RummyPlayingCard> cardsList = new ArrayList();
        ((ArrayList) this.mGroupList.get(this.mGroupList.size() - 1)).add(playerCard);
        setGroupView(false);
    }

    private boolean canPickCard(int numberOfCards) {
        if (numberOfCards > 13 && this.isYourTurn) {
            this.mGameShecduleTv.setText(R.string.card_pick_msg);
            this.mGameShecduleTv.setVisibility(View.VISIBLE);
            return false;
        } else if (numberOfCards == 13 && this.isYourTurn) {
            return true;
        } else {
            this.mGameShecduleTv.setText(R.string.info_wait_for_turn);
            this.mGameShecduleTv.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private void addCardToRummyView(final RummyPlayingCard playerCard, int index) {
        try {
            final LinearLayout cardLayout = this.mRummyView.getCard();

            //// setscreensize
            try {
                if(!isTablet(getActivity())) {
                    RummyUtils.setViewWidth((LinearLayout)cardLayout.findViewById(R.id.ll_cardview_main_container), RummyGameRoomCustomScreenLess700.stackCardWidth);
                    RummyUtils.setViewWidth((RelativeLayout)cardLayout.findViewById(R.id.card_view_root_layout), RummyGameRoomCustomScreenLess700.stackCardWidth);
                    RummyUtils.setViewHeight((RelativeLayout)cardLayout.findViewById(R.id.card_view_root_layout), RummyGameRoomCustomScreenLess700.stackCardHeight);
                    RummyUtils.setViewWidth((ImageView)cardLayout.findViewById(R.id.cardImageView), RummyGameRoomCustomScreenLess700.stackCardWidth);
                    RummyUtils.setViewHeight((ImageView)cardLayout.findViewById(R.id.cardImageView), RummyGameRoomCustomScreenLess700.stackCardHeight);
                    RummyUtils.setViewHeight((ImageView)cardLayout.findViewById(R.id.jokerCardImg), RummyGameRoomCustomScreenLess700.jokerCardHeight);
                    RummyUtils.setViewWidth((ImageView)cardLayout.findViewById(R.id.jokerCardImg), RummyGameRoomCustomScreenLess700.jokerCardWidth);
                    RummyUtils.setViewWidth((ImageView)cardLayout.findViewById(R.id.cardImageViewSelected), RummyGameRoomCustomScreenLess700.stackCardWidth);
                    RummyUtils.setViewHeight((ImageView)cardLayout.findViewById(R.id.cardImageViewSelected), RummyGameRoomCustomScreenLess700.stackCardHeight);
                    RummyUtils.setViewWidth((ImageView)cardLayout.findViewById(R.id.autoPlayIv), RummyGameRoomCustomScreenLess700.autoPlayIconSize);
                    RummyUtils.setViewHeight((ImageView)cardLayout.findViewById(R.id.autoPlayIv), RummyGameRoomCustomScreenLess700.autoPlayIconSize);
                }else {
                    RummyUtils.setViewWidth((LinearLayout)cardLayout.findViewById(R.id.ll_cardview_main_container), RummyGameRoomCustomScreenMore700.stackCardWidth);
                    RummyUtils.setViewWidth((RelativeLayout)cardLayout.findViewById(R.id.card_view_root_layout), RummyGameRoomCustomScreenMore700.stackCardWidth);
                    RummyUtils.setViewHeight((RelativeLayout)cardLayout.findViewById(R.id.card_view_root_layout), RummyGameRoomCustomScreenMore700.stackCardHeight);
                    RummyUtils.setViewWidth((ImageView)cardLayout.findViewById(R.id.cardImageView), RummyGameRoomCustomScreenMore700.stackCardWidth);
                    RummyUtils.setViewHeight((ImageView)cardLayout.findViewById(R.id.cardImageView), RummyGameRoomCustomScreenMore700.stackCardHeight);
                    RummyUtils.setViewHeight((ImageView)cardLayout.findViewById(R.id.jokerCardImg), RummyGameRoomCustomScreenMore700.jokerCardHeight);
                    RummyUtils.setViewWidth((ImageView)cardLayout.findViewById(R.id.jokerCardImg), RummyGameRoomCustomScreenMore700.jokerCardWidth);
                    RummyUtils.setViewWidth((ImageView)cardLayout.findViewById(R.id.cardImageViewSelected), RummyGameRoomCustomScreenMore700.stackCardWidth);
                    RummyUtils.setViewHeight((ImageView)cardLayout.findViewById(R.id.cardImageViewSelected), RummyGameRoomCustomScreenMore700.stackCardHeight);
                    RummyUtils.setViewWidth((ImageView)cardLayout.findViewById(R.id.autoPlayIv), RummyGameRoomCustomScreenMore700.autoPlayIconSize);
                    RummyUtils.setViewHeight((ImageView)cardLayout.findViewById(R.id.autoPlayIv), RummyGameRoomCustomScreenMore700.autoPlayIconSize);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }


            final ImageView cardImg = (ImageView) cardLayout.findViewById(R.id.cardImageView);
            ImageView jockerImg = (ImageView) cardLayout.findViewById(R.id.jokerCardImg);
            String cardValue = String.format("%s%s", new Object[]{playerCard.getSuit(), playerCard.getFace()});

            if (this.mJockerCard != null) {
                if (playerCard != null && playerCard.getFace().equalsIgnoreCase(this.mJockerCard.getFace())) {
                    jockerImg.setVisibility(View.VISIBLE);
                } else if (playerCard == null || !playerCard.getFace().equalsIgnoreCase("1")) {
                    jockerImg.setVisibility(View.GONE);
                } else if (this.mJockerCard.getFace().equalsIgnoreCase("0")) {
                    jockerImg.setVisibility(View.VISIBLE);
                } else {
                    jockerImg.setVisibility(View.GONE);
                }
            }
            int imgId = this.mActivity.getResources().getIdentifier(cardValue, "drawable", this.mActivity.getPackageName());
            cardImg.setVisibility(View.VISIBLE);
            cardImg.setImageResource(imgId);
            String tag = String.format("%s-%s", new Object[]{cardValue, String.valueOf(index)});
            cardImg.setTag(tag);
            cardLayout.setTag(tag);
            playerCard.setIndex(String.valueOf(index));
            cardImg.setId(index);
            cardLayout.setId(index);
            cardLayout.setOnClickListener(new OnClickListener() {
                class C17201 implements RummyQuickAction.OnActionItemClickListener {
                    C17201() {
                    }

                    public void onItemClick(RummyQuickAction source, int pos, int actionId) {
                        if (actionId == 1) {
                            RummyTablesFragment.this.mSelectedCards.remove(playerCard);
                            RummyTablesFragment.this.removeCardAndArrangeCards(RummyTablesFragment.this.mDiscardView);
                            RummyTablesFragment.this.mQuickAction.dismiss();
                        } else if (actionId == 2) {
                            RummyTablesFragment.this.groupCards();
                        } else {
                            RummyTablesFragment.this.meldCards();
                        }
                    }
                }

                class C17212 implements RummyQuickAction.OnActionItemClickListener {
                    C17212() {
                    }

                    public void onItemClick(RummyQuickAction source, int pos, int actionId) {
                        if (actionId == 1) {
                            RummyTablesFragment.this.mSelectedCards.remove(playerCard);
                            RummyTablesFragment.this.removeCardAndArrangeCards(RummyTablesFragment.this.mDiscardView);
                            RummyTablesFragment.this.mQuickAction.dismiss();
                        } else if (actionId == 2) {
                            RummyTablesFragment.this.groupCards();
                        } else {
                            RummyTablesFragment.this.meldCards();
                        }
                    }
                }

                public void onClick(View v) {
                    ImageView selectedCardIv = (ImageView) v.findViewById(R.id.cardImageViewSelected);
                    RummyTablesFragment.this.showView(selectedCardIv);
                    int numberOfCards = RummyTablesFragment.this.getTotalCards();
                    Iterator it = RummyTablesFragment.this.mSelectedCards.iterator();
                    while (it.hasNext()) {
                        String imgTagValue;
                        RummyPlayingCard card = (RummyPlayingCard) it.next();
                        if (String.format("%s", new Object[]{v.getTag().toString()}).equalsIgnoreCase(String.format("%s%s-%s", new Object[]{card.getSuit(), card.getFace(), card.getIndex()}))) {
                            RummyTablesFragment.this.hideView(selectedCardIv);
                            v.clearAnimation();
                            RummyTablesFragment.this.dismissQuickMenu();
                            RummyTablesFragment.this.mSelectedCards.remove(card);
                            RummyTablesFragment.this.disableView(RummyTablesFragment.this.mShowBtn);
                            if (RummyTablesFragment.this.mSelectedCards.size() > 0) {
                                RummyPlayingCard lastCard = (RummyPlayingCard) RummyTablesFragment.this.mSelectedCards.get(RummyTablesFragment.this.mSelectedCards.size() - 1);
                                it = RummyTablesFragment.this.mSelectedImgList.iterator();
                                while (it.hasNext()) {
                                    ImageView img = (ImageView) it.next();
                                    imgTagValue = String.format("%s", new Object[]{img.getTag().toString()});
                                    String lastCardValue = String.format("%s%s-%s", new Object[]{lastCard.getSuit(), lastCard.getFace(), lastCard.getIndex()});
                                    if (imgTagValue.equalsIgnoreCase(lastCardValue)) {
                                        RummyTablesFragment.this.mQuickAction = new RummyQuickAction(v.getContext(), 1);
                                        if (RummyTablesFragment.this.mSelectedCards.size() > 1) {
                                            RummyTablesFragment.this.mGroupView = new RummyActionItem(2, "GROUP");
                                            RummyTablesFragment.this.mQuickAction.addActionItem(RummyTablesFragment.this.mGroupView);
                                            RummyTablesFragment.this.mQuickAction.show(RummyTablesFragment.this.getLastSelectedView(img));
                                            if (RummyTablesFragment.this.mIsMelding) {
                                                RummyTablesFragment.this.enableView(RummyTablesFragment.this.mShowBtn);
                                            } else {
                                                RummyTablesFragment.this.disableView(RummyTablesFragment.this.mShowBtn);
                                            }
                                        } else if (numberOfCards > 13) {
                                            RummyTablesFragment.this.mDiscardView = new RummyActionItem(1, "DISCARD");
                                            RummyTablesFragment.this.mDiscardView.setTag(lastCardValue);
                                            RummyTablesFragment.this.mQuickAction.addActionItem(RummyTablesFragment.this.mDiscardView);
                                            LinearLayout linearLayout = RummyTablesFragment.this.getLastSelectedView(img);
                                            RummyTablesFragment.this.animateCard(linearLayout);
                                            RummyTablesFragment.this.mQuickAction.show(linearLayout);
                                            RummyTablesFragment.this.enableView(RummyTablesFragment.this.mShowBtn);
                                            RummyTablesFragment.this.setDiscardCard(lastCard);
                                        }
                                        RummyTablesFragment.this.mQuickAction.setOnActionItemClickListener(new C17201());
                                        return;
                                    }
                                }
                                return;
                            }
                            return;
                        }
                    }
                    RummyTablesFragment.this.mSelectedCards.add(playerCard);
                    RummyTablesFragment.this.mSelectedImgList.add(cardImg);
                    RummyTablesFragment.this.mSelectedLayoutList.add(cardLayout);
                    RummyTablesFragment.this.animateCard(v);
                    RummyTablesFragment.this.dismissQuickMenu();
                    RummyTablesFragment.this.mQuickAction = new RummyQuickAction(v.getContext(), 1);
                    if (RummyTablesFragment.this.mSelectedCards.size() > 1) {
                        RummyTablesFragment.this.mGroupView = new RummyActionItem(2, "GROUP");
                        RummyTablesFragment.this.mQuickAction.addActionItem(RummyTablesFragment.this.mGroupView);
                        RummyTablesFragment.this.mQuickAction.show(v);
                        if (RummyTablesFragment.this.mIsMelding) {
                            RummyTablesFragment.this.enableView(RummyTablesFragment.this.mShowBtn);
                        } else {
                            RummyTablesFragment.this.disableView(RummyTablesFragment.this.mShowBtn);
                        }
                    } else if (numberOfCards > 13 && RummyTablesFragment.this.mSelectedCards.size() == 1) {
                        String imgTagValue = String.format("%s", new Object[]{v.getTag().toString()});
                        RummyTablesFragment.this.mDiscardView = new RummyActionItem(1, "DISCARD");
                        RummyTablesFragment.this.mDiscardView.setTag(imgTagValue);
                        RummyTablesFragment.this.mQuickAction.addActionItem(RummyTablesFragment.this.mDiscardView);
                        RummyTablesFragment.this.mQuickAction.show(v);
                        RummyTablesFragment.this.setDiscardCard(playerCard);
                        RummyTablesFragment.this.enableView(RummyTablesFragment.this.mShowBtn);
                    }
                    RummyTablesFragment.this.mQuickAction.setOnActionItemClickListener(new C17212());
                }
            });
            this.playerCards.add(cardLayout);
            this.mRummyView.addCard(cardLayout,false);
        } catch (Exception e) {
            RummyTLog.e(TAG, "Exception :: addCardToRummyView " + e.getMessage());
        }
    }

    public void clearSelectedCard()
    {
        if(isAdded())
        {
            if(RummyTablesFragment.this.getTag() != null)
            {
                arrangeSelectedCards(RummyTablesFragment.this.getTag().toString());
                this.updateCardsGroup(this.mRummyView.getUpdatedCardsGroup());
            }

        }
    }

    private void meldCards() {
        ArrayList<RummyPlayingCard> meldCards = new ArrayList();
        RummyPlayingCard card = getDiscardedCard();
        String mCardValue = String.format("%s%s%s", new Object[]{card.getSuit(), card.getFace(), card.getIndex()});
        Iterator it = this.mSelectedCards.iterator();
        RummyCardDiscard playingCard = new RummyCardDiscard();
        while (it.hasNext()) {
            if (!String.format("%s%s%s", new Object[]{playingCard.getSuit(), playingCard.getFace(), ((RummyPlayingCard) it.next()).getIndex()}).equalsIgnoreCase(mCardValue)) {
                meldCards.add((RummyPlayingCard) it.next());
            }
        }
        this.mMeldGroupList.add(meldCards);
        this.mSelectedCards.clear();
        removeCardsOnMeld(meldCards);
    }

    private void setDiscardCard(RummyPlayingCard card) {
        this.mDiscardedCard = card;
    }

    private int getTotalCards() {
        int totalCards = 0;
        Iterator it = this.mGroupList.iterator();
        while (it.hasNext()) {
            Iterator it2 = ((ArrayList) it.next()).iterator();
            while (it2.hasNext()) {
                RummyPlayingCard card = (RummyPlayingCard) it2.next();
                totalCards++;
            }
        }
        return totalCards;
    }

    private void groupCards() {
        for (int i = this.mGroupList.size() - 1; i >= 0; i--) {
            ArrayList<RummyPlayingCard> cardGroup = (ArrayList) this.mGroupList.get(i);
            for (int j = cardGroup.size() - 1; j >= 0; j--) {
                RummyPlayingCard mcard = (RummyPlayingCard) cardGroup.get(j);
                String mCardValue = String.format("%s%s-%s", new Object[]{mcard.getSuit(), mcard.getFace(), mcard.getIndex()});
                for (int x = this.mSelectedCards.size() - 1; x >= 0; x--) {
                    RummyPlayingCard selectedCard = (RummyPlayingCard) this.mSelectedCards.get(x);
                    if (String.format("%s%s-%s", new Object[]{selectedCard.getSuit(), selectedCard.getFace(), selectedCard.getIndex()}).equalsIgnoreCase(mCardValue)) {
                        cardGroup.remove(j);
                    }
                }
            }
        }
        ArrayList<RummyPlayingCard> cardsList = new ArrayList();
        cardsList.addAll(this.mSelectedCards);
        this.mGroupList.add(cardsList);
        setGroupView(true);
        this.mGroupList = this.mRummyView.getUpdatedCardsGroup();
        updateCardsView();
        clearSelectedCards();
    }

    private void clearSelectedCards() {
        dismissQuickMenu();
        this.mSelectedCards.clear();
        this.mSelectedImgList.clear();
        this.mSelectedLayoutList.clear();
    }

    private void removeCardAndArrangeCards(RummyActionItem actionItem) {
        int numberOfCards = RummyTablesFragment.this.getTotalCards();
        if(isYourTurn && numberOfCards>13) {
            hideTimerUI();
            this.mGameShecduleTv.setVisibility(View.INVISIBLE);
            String discardTag = actionItem.getTag();
            String discardSuit = null;
            String disCardFace = null;
            Iterator it = this.mGroupList.iterator();
            while (it.hasNext()) {
                ArrayList<RummyPlayingCard> groupList = (ArrayList) it.next();
                Iterator it2 = groupList.iterator();
                while (it2.hasNext()) {
                    RummyPlayingCard card = (RummyPlayingCard) it2.next();
                    if (discardTag.equalsIgnoreCase(String.format("%s%s-%s", card.getSuit(), card.getFace(), card.getIndex()))) {
                        discardSuit = card.getSuit();
                        disCardFace = card.getFace();
                        groupList.remove(card);
                        break;
                    }
                }
            }

            clearSelectedCards();
            setGroupView(false);

            if (!(discardSuit == null || disCardFace == null)) {
                discardCard(discardSuit, disCardFace);
                this.isYourTurn = false;
            }
        }
    }

    private void getTableDetails() {
        if (this.tableId != null) {
            RummyTableDetailsRequest request = new RummyTableDetailsRequest();
            request.setCommand("get_table_details");
            request.setTableId(this.tableId);
            request.setUuid(RummyUtils.generateUuid());
            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.tableDetailsListner);
            } catch (RummyGameEngineNotRunning e) {
                Toast.makeText(this.mActivity, R.string.error_restart, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getTableDetailsFromAutoPlayResult() {
        if (this.tableId != null) {
            RummyTableDetailsRequest request = new RummyTableDetailsRequest();
            request.setCommand("get_table_details");
            request.setTableId(this.tableId);
            request.setUuid(RummyUtils.generateUuid());
            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.tableDetailsListner);
            } catch (RummyGameEngineNotRunning e) {
                Toast.makeText(this.mActivity, R.string.error_restart, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleGetTableDetailsEvent(RummyTableDetails tableDetails, String timeStampStr) {
        RummyTLog.e("vikas", "calling handlegettable detail event");
        if (tableDetails != null) {
            RummyTLog.e("vikas", "calling handlegettable detail event table details not null");
            if (tableDetails.getTournament_table() != null && tableDetails.getTournament_table().equalsIgnoreCase("yes")) {

                if (this.tableId != null && tableDetails.getTableId() != null && this.tableId.equalsIgnoreCase(tableDetails.getTableId())) {
                    RummyTLog.e("vikas", "tourney id by schedulename=" + tableDetails.getSchedulename());
                    this.strIsTourneyTable = "yes";
                    this.mTourneyId = tableDetails.getSchedulename();
                    checkTournament();
                }

            }

            if (tableDetails.getTableCost().equalsIgnoreCase("CASH_CASH")) {
                iv_bet_icon.setImageResource(R.drawable.rummy_cash_icon_tourney_result);
                iv_balance_icon.setImageResource(R.drawable.rummy_cash_icon_tourney_result);
                String balance = this.userData.getRealChips();
                mBalanceMoney.setText(balance);
            } else {
                iv_bet_icon.setImageResource(R.drawable.rummy_free_icon_tourney_result);
                iv_balance_icon.setImageResource(R.drawable.rummy_free_icon_tourney_result);
                String balance = this.userData.getFunChips();
                mBalanceMoney.setText(balance);
            }

            RummyGamePlayer player;
            int i;
            this.mTableDetails = tableDetails;
            ((RummyTableActivity) this.mActivity).clearDiscards(this.tableId);
            int maxNoOfPlayers = Integer.parseInt(this.mTableDetails.getMaxPlayer());
            ((RummyTableActivity) this.mActivity).setTableDetailsList(tableDetails);
            setTableData(tableDetails);
            showPlayersJoinedMessage = tableDetails.is_waiting_for_players();
            setDealerId(tableDetails.getDealer_id());
            String gameStartTime = tableDetails.getStarttime();
            if (gameStartTime != null && gameStartTime.matches(".*\\d.*") && !gameStartTime.equalsIgnoreCase("0.0") ) {
                int timerValue = (int) (Double.valueOf(gameStartTime).doubleValue() - Double.valueOf(timeStampStr).doubleValue());
                if (!this.opponentValidShow) {
                    startGameScheduleTimer(timerValue, false);
                }
            }
            List<RummyGamePlayer> playerList = tableDetails.getPlayers();
            int playerCount = playerList.size();
            if (playerCount == 1) {
                showView(this.mGameShecduleTv);
                this.mGameShecduleTv.setText(this.mActivity.getString(R.string.wait_for_next_player_msg));
                ((RummyTableActivity) this.mActivity).resetPlayerIconsOnTableBtn(this.tableId);
            }
            if (Integer.parseInt(tableDetails.getMaxPlayer()) == 2) {
                RummyTLog.e("vikas", "calling handlegettable detail event max palyer 2");
                hidePlayerView();
                showView(this.mUserPlayerLayout);
                showView(this.mFourthPlayerLayout);
            } else {
                RummyTLog.e("vikas", "calling handlegettable detail event max palyer 6");
                showPlayerView();
            }
            String middleJoin = tableDetails.getMiddlejoin();
            if (middleJoin != null && middleJoin.equalsIgnoreCase("True")) {
                setMiddleJoinPlayers(tableDetails, playerList);
            }
            for (RummyGamePlayer player2 : playerList) {
                if (player2.getUser_id().equalsIgnoreCase(this.userData.getUserId())) {
                    String autoPlayCountStr = player2.getAutoplay_count();
                    if (autoPlayCountStr != null) {
                        this.mUserAutoPlayCount = Integer.parseInt(autoPlayCountStr);
                        break;
                    }
                }
            }
            Collections.sort(playerList, new RummyPlayerComparator());
            RummyGamePlayer player2;
            if (Boolean.valueOf(tableDetails.getGameStart()).booleanValue()) {
                RummyGameDetails gameDetails = tableDetails.getGameDetails();
                if (gameDetails != null) {
                    ((RummyTableActivity) this.mActivity).updateGameId(tableDetails.getTableId(), gameDetails.getGameId());
                    this.game_id_tb.setText("#" + gameDetails.getGameId());
                    this.mGameId = gameDetails.getGameId();
                    this.gameid_tourney_tv.setText(this.mGameId);
                    this.mPrizeMoney.setText(gameDetails.getPrizeMoney());

                    if(this.mTableDetails != null && this.mTableDetails.getTableType().equalsIgnoreCase(RummyUtils.PR_JOKER))
                    {
                        showView(this.btn_leave_table_game_result);
                        //hideView(this.ll_top_bar_prize_section);
                        //hideView(this.divider_before_topbar_prize_section);
                        if (!isTourneyTable()) {
                            showView(this.last_game_layout_root);
                        }
                    }
                    else
                    {
                        hideView(this.btn_leave_table_game_result);
                        hideView(this.last_game_layout_root);
                        //showView(this.ll_top_bar_prize_section);
                        //showView(this.divider_before_topbar_prize_section);
                    }
                }
                ArrayList<RummyGamePlayer> sortedPlayerList = new ArrayList();
                if (maxNoOfPlayers == 2 || playerList.size() == 2) {
                    for (i = 0; i < playerList.size(); i++) {
                        player2 = (RummyGamePlayer) playerList.get(i);
                        if (player2.getUser_id().equalsIgnoreCase(this.userData.getUserId())) {
                            player2.setSeat("1");
                        } else {
                            player2.setSeat("4");
                        }
                        this.mJoinedPlayersList.add(player2);
                        this.mGamePlayerMap.put(player2.getUser_id(), player2);
                        Log.d(TAG, "SEATING VIA: player Size 2");
                        setUpPlayerUI(player2, false);
                        if (this.mTableDetails != null) {
                            RummyTLog.e("vikas", "callig setPlayerPositionsOnTableBtn from handleGetTableDetailsEvent");
                            setPlayerPositionsOnTableBtn(this.mTableDetails, player2, false);
                        }
                    }
                } else {
                    int userPlace = 0;
                    for (i = 0; i < playerList.size(); i++) {
                        if (((RummyGamePlayer) playerList.get(i)).getUser_id().equalsIgnoreCase(this.userData.getUserId())) {
                            userPlace = i;
                            break;
                        }
                    }
                    for (i = 0; i < playerList.size(); i++) {
                        sortedPlayerList.add(i, playerList.get(userPlace));
                        userPlace = (userPlace + 1) % playerList.size();
                    }
                    playerList.clear();
                    playerList.addAll(sortedPlayerList);
                    for (i = 0; i < playerList.size(); i++) {
                        player2 = (RummyGamePlayer) playerList.get(i);
                        player2.setSeat(String.valueOf(i + 1));
                        this.mJoinedPlayersList.add(player2);
                        this.mGamePlayerMap.put(player2.getUser_id(), player2);
                        RummyTLog.e("vikas", "SEATING VIA: player size not 2");
                        setUpPlayerUIForRunningGame(player2, false);
                        setPlayerPositionsOnTableBtn(tableDetails, player2, false);
                    }
                }
            } else {
                for (i = 0; i < playerCount; i++) {
                    player2 = (RummyGamePlayer) playerList.get(i);
                    player2.setSeat(String.valueOf(player2.getPlace()));
                    this.mJoinedPlayersList.add(player2);
                    this.mGamePlayerMap.put(player2.getUser_id(), player2);
                    RummyTLog.e(TAG, "SEATING VIA: getGameStart is false");
                    setUpPlayerUI(player2, false);
                    setPlayerPositionsOnTableBtn(tableDetails, player2, false);
                }
            }
            List<RummyGamePlayer> dropPayerList = tableDetails.getDropList();
            if (dropPayerList != null && dropPayerList.size() > 0) {
                setDroppedPlayers(playerList, dropPayerList);
                for (RummyGamePlayer player22 : dropPayerList) {
                    if (player22.isDropped()) {
                        handlePlayerDrop(Integer.parseInt(player22.getUser_id()));
                    }
                }
            }
            for (i = 0; i < this.mJoinedPlayersList.size(); i++) {
                setAutoPlayUIonReconnect((RummyGamePlayer) this.mJoinedPlayersList.get(i));
            }

            if (this.strIsTourneyTable.equalsIgnoreCase("yes")) {
                this.bet_tourney_tv.setText(tableDetails.getBet());
            }
        }
        broadCastEvents();
        getTableExtra();
        this.updatePlayersRank();

        updateTableTypeView(); //psp
    }

    private void setDroppedPlayers(List<RummyGamePlayer> playerList, List<RummyGamePlayer> dropPayerList) {
        if (dropPayerList != null && dropPayerList.size() > 0) {
            for (RummyGamePlayer player : playerList) {
                for (RummyGamePlayer dropPlayer : dropPayerList) {
                    if (player.getUser_id().equalsIgnoreCase(dropPlayer.getUser_id()) && !player.isMiddleJoin()) {
                        dropPlayer.setDropped(true);
                    }
                }
            }
        }
    }

    private void setAutoPlayUIonReconnect(RummyGamePlayer player) {
        String autoPlay = player.getAutoplay();
        if (autoPlay != null && autoPlay.equalsIgnoreCase("true")) {
            player.setAutoplay(player.getAutoplay());
            player.setTotalCount("5");
            String autoPlayCount = player.getAutoplay_count();
            if (autoPlayCount != null) {
                player.setAutoplay_count(autoPlayCount);
            }
            setAutoPlayUI(player);
        }
    }

    private void sendAutoPlayStatus() {
        try {
            RummyApplication app = (RummyApplication.getInstance());
            RummyTableDetailsRequest request = new RummyTableDetailsRequest();
            request.setCommand("autoplaystatus");
            request.setUuid(RummyUtils.generateUuid());
            request.setUserId(String.valueOf(app.getUserData().getUserId()));
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(mActivity, RummyUtils.getObjXMl(request), this.autoPLayListner);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            RummyTLog.d(TAG, "sendIamBack" + gameEngineNotRunning.getLocalizedMessage());
        }
    }

    private void setPlayerPositionsOnTableBtn(RummyTableDetails tableDetails, RummyGamePlayer player, boolean isLeft) {
        if (tableDetails != null) {
            RummyTLog.e("vikas", "calling set player position");
            ((RummyTableActivity) this.mActivity).setGameTableBtnUI(tableDetails.getTableId(), player, Integer.parseInt(tableDetails.getMaxPlayer()), isLeft);
        }
    }

    private void setMiddleJoinPlayers(RummyTableDetails tableDetails, List<RummyGamePlayer> playerList) {
        RummyMiddle middle = tableDetails.getMiddle();
        if (middle != null) {
            List<RummyGamePlayer> gamePlayerList = middle.getPlayer();
            if (gamePlayerList != null) {
                for (int i = 0; i < gamePlayerList.size(); i++) {
                    RummyGamePlayer gamePlayer = (RummyGamePlayer) gamePlayerList.get(i);
                    gamePlayer.setMiddleJoin(true);
                    playerList.add(gamePlayer);
                    if (gamePlayer.getUser_id().equalsIgnoreCase(this.userData.getUserId())) {
                        this.userData.setMiddleJoin(true);
                    }
                }
            }
        }
    }

    private void setDealerId(String dealerId) {
        if (dealerId != null && dealerId.length() > 0) {
            this.mDealerId = dealerId;
        }
    }

    private void broadCastEvents() {
        this.mApplication = RummyApplication.getInstance();
        List<RummyEvent> tempList = this.mApplication.getEventList();
        List<RummyEvent> eventList = new ArrayList();
        eventList.addAll(tempList);
        for (RummyEvent event : eventList) {
            onMessageEvent(event);
        }
    }

    private void setSeating(List<RummyGamePlayer> playerList) {
        int i = 0;
        while (i < playerList.size()) {
            RummyGamePlayer player = (RummyGamePlayer) playerList.get(i);
            int seat = i + 1;
            if (playerList.size() == 2 && i == 1) {
                seat += 2;
            }
            player.setBuyinammount(player.getMinimumBuyin());
            player.setSeat(String.valueOf(seat));
            this.mJoinedPlayersList.add(player);
            RummyTLog.e(TAG, "SEATING VIA: setSeating");
            setUpPlayerUI(player, false);
            if (this.mTableDetails != null) {
                setPlayerPositionsOnTableBtn(this.mTableDetails, player, false);
            }
            i++;
        }
    }

    public List<RummyGamePlayer> getJoinedPlayerList() {
        return this.mJoinedPlayersList;
    }

    private void resetDealer() {
        hideView(this.mUserPlayerLayout.findViewById(R.id.player_dealer_iv));
        hideView(this.mSecondPlayerLayout.findViewById(R.id.player_dealer_iv));
        hideView(this.mThirdPlayerLayout.findViewById(R.id.player_dealer_iv));
        hideView(this.mFourthPlayerLayout.findViewById(R.id.player_dealer_iv));
        hideView(this.mFifthPlayerLayout.findViewById(R.id.player_dealer_iv));
        hideView(this.mSixthPlayerLayout.findViewById(R.id.player_dealer_iv));
    }

    private void setUpPlayerUI(RummyGamePlayer player, boolean isPlayerJoined) {


        Log.d(TAG, "Setting player position-->> " + player.getNick_name() + " (" + player.getPlace() + ")");

        String seat;
        if (this.mGamePlayerMap.get(player.getUser_id()) != null) {
            player.setPlayerlevel(((RummyGamePlayer) this.mGamePlayerMap.get(player.getUser_id())).getPlayerlevel());
        }
        if (player.getSeat() != null) {
            seat = player.getSeat();
        } else {
            seat = "1";
        }
      // crashlytics.setCustomKey("player_" + seat, player.getUser_id() + " - " + player.getNick_name());


        switch (Integer.parseInt(seat)) {
            case 1:
                setUserDetails(this.mUserPlayerLayout, player, this.mDealerId, isPlayerJoined);
                mPlayerBoxesForRanks.put(player.getUser_id(), this.mUserPlayerLayout);

                if(RummyInstance.getInstance().getAssetsFolderName() != null && !RummyInstance.getInstance().getAssetsFolderName().equalsIgnoreCase(""))
                {
                    PicassoBridge.init(mActivity).load(RummyInstance.getInstance().getAssetsFolderName()).transform(new CircleTransform()).into(((ImageView) this.mUserPlayerLayout.findViewById(R.id.iv_avtar)));

                }
                else
                {
                    ((ImageView) this.mUserPlayerLayout.findViewById(R.id.iv_avtar)).setImageResource(avatars[0]);

                    if(player.getGender().equalsIgnoreCase("FEMALE")){
                        ((ImageView) this.mUserPlayerLayout.findViewById(R.id.iv_avtar)).setImageResource(R.drawable.rummy_avtar_female);
                    }
                }

                return;
            case 2:
                showView(this.mSecondPlayerLayout);
                setUpPlayerDetails(this.mSecondPlayerLayout, player, this.mDealerId, isPlayerJoined);
                mPlayerBoxesForRanks.put(player.getUser_id(), this.mSecondPlayerLayout);

                if(player.getGender() !=null && player.getGender().equalsIgnoreCase("FEMALE")){
                    PicassoBridge.init(mActivity).load(R.drawable.rummy_avtar_female).transform(new CircleTransform()).into(((ImageView) this.mSecondPlayerLayout.findViewById(R.id.iv_avtar)));
                }
                else
                {
                    PicassoBridge.init(mActivity).load(avatars[1]).transform(new CircleTransform()).into(((ImageView) this.mSecondPlayerLayout.findViewById(R.id.iv_avtar)));
                }

                return;
            case 3:
                showView(this.mThirdPlayerLayout);
                setUpPlayerDetails(this.mThirdPlayerLayout, player, this.mDealerId, isPlayerJoined);
                mPlayerBoxesForRanks.put(player.getUser_id(), this.mThirdPlayerLayout);

                if(player.getGender() !=null && player.getGender().equalsIgnoreCase("FEMALE")){
                    PicassoBridge.init(mActivity).load(R.drawable.rummy_avtar_female).transform(new CircleTransform()).into(((ImageView) this.mThirdPlayerLayout.findViewById(R.id.iv_avtar)));
                }
                else
                {
                    PicassoBridge.init(mActivity).load(avatars[2]).transform(new CircleTransform()).into(((ImageView) this.mThirdPlayerLayout.findViewById(R.id.iv_avtar)));
                }


                return;
            case 4:
                showView(this.mFourthPlayerLayout);
                setUpPlayerDetails(this.mFourthPlayerLayout, player, this.mDealerId, isPlayerJoined);
                mPlayerBoxesForRanks.put(player.getUser_id(), this.mFourthPlayerLayout);


                if(player.getGender() !=null && player.getGender().equalsIgnoreCase("FEMALE")){
                    PicassoBridge.init(mActivity).load(R.drawable.rummy_avtar_female).transform(new CircleTransform()).into(((ImageView) this.mFourthPlayerLayout.findViewById(R.id.iv_avtar)));
                }
                else
                {
                    PicassoBridge.init(mActivity).load(avatars[3]).transform(new CircleTransform()).into(((ImageView) this.mFourthPlayerLayout.findViewById(R.id.iv_avtar)));
                }

                return;
            case 5:
                showView(this.mFifthPlayerLayout);
                setUpPlayerDetails(this.mFifthPlayerLayout, player, this.mDealerId, isPlayerJoined);
                mPlayerBoxesForRanks.put(player.getUser_id(), this.mFifthPlayerLayout);

                if(player.getGender() !=null && player.getGender().equalsIgnoreCase("FEMALE")){
                    PicassoBridge.init(mActivity).load(R.drawable.rummy_avtar_female).transform(new CircleTransform()).into(((ImageView) this.mFifthPlayerLayout.findViewById(R.id.iv_avtar)));
                }
                else
                {
                    PicassoBridge.init(mActivity).load(avatars[4]).transform(new CircleTransform()).into(((ImageView) this.mFifthPlayerLayout.findViewById(R.id.iv_avtar)));
                }

                return;
            case 6:
                showView(this.mSixthPlayerLayout);
                setUpPlayerDetails(this.mSixthPlayerLayout, player, this.mDealerId, isPlayerJoined);
                mPlayerBoxesForRanks.put(player.getUser_id(), this.mSixthPlayerLayout);

                if(player.getGender() !=null && player.getGender().equalsIgnoreCase("FEMALE")){
                    PicassoBridge.init(mActivity).load(R.drawable.rummy_avtar_female).transform(new CircleTransform()).into(((ImageView) this.mSixthPlayerLayout.findViewById(R.id.iv_avtar)));
                }
                else
                {
                    PicassoBridge.init(mActivity).load(avatars[5]).transform(new CircleTransform()).into(((ImageView) this.mSixthPlayerLayout.findViewById(R.id.iv_avtar)));
                }

                return;
            default:
                return;
        }
    }

    /*Kept hold this functionality for next release*/
    private void updatePlayersJoinCount() {
        //psp
        if (this.mTableDetails != null && Integer.parseInt(this.mTableDetails.getMaxPlayer()) == 6) {

            int temp = 0;
            for (int i = 0; i < mJoinedPlayersList.size(); i++) {
                int flag = 0;
                for (int j = 0; j < i; j++) {
                    if (mJoinedPlayersList.get(i).getUser_id().equals(mJoinedPlayersList.get(j).getUser_id())) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {

//                    Log.e(TAG, "updatePlayersJoinCount: test psp -->" + mJoinedPlayersList.get(i).getNick_name() + "left --> " + mJoinedPlayersList.get(i).isLeft() + " drop --> " + mJoinedPlayersList.get(i).isDropped() + " middle joined --> " + mJoinedPlayersList.get(i).isMiddleJoin());

                    if (mJoinedPlayersList.get(i).isLeft()) {

//                        Log.e(TAG, "updatePlayersJoinCount: test psp --> Player ignored");
                    } else {
//                        Log.e(TAG, "updatePlayersJoinCount: test psp --> Player Added");
                        temp++;
                    }
                }
            }

//            Log.d(TAG, "updatePlayersJoinCount: psp --> real count : " + temp + " --> data " + new Gson().toJson(mJoinedPlayersList));

            if (temp > 5 || temp == 1) {
                hideView(this.players_join_status_layout);
                Log.e(TAG, "onMessageEvent: Not show count on toast--> fvsdf" );
            } else {
//                getTotalCards() < 1 --> might be useful
                if (!this.isGameStarted && getTotalCards() < 1 && showPlayersJoinedMessage) {

                    Log.e(TAG, "updatePlayersJoinCount:psp showing the toast" );

                    this.player_join_status_tv.setText(temp + " Players have joined. Waiting for more…");
                    showView(this.players_join_status_layout);
                } else {
                    hideView(this.players_join_status_layout);
                    Log.e(TAG, "onMessageEvent: Not show count on toast--> dsfds" );
                    Log.e(TAG, "updatePlayersJoinCount:psp NOT showing the toast" );
                }
            }


        } else {
            hideView(this.players_join_status_layout);
            Log.e(TAG, "onMessageEvent: Not show count on toast --> dfsad" );
        }

        if(mDropAndGoPlayer.isChecked()){
            hideView(this.players_join_status_layout);
            //TextView mTimerTv = (TextView) mGameResultsView.findViewById(R.id.game_timer);
           // hideView(mTimerTv);
        }

    }


    private void setUpPlayerUIForRunningGame(RummyGamePlayer player, boolean isPlayerJoined) {
        Log.d(TAG, "Setting player position-->> " + player.getNick_name() + " (" + player.getPlace() + ")");

        String seat;
        if (this.mGamePlayerMap.get(player.getUser_id()) != null) {
            player.setPlayerlevel(((RummyGamePlayer) this.mGamePlayerMap.get(player.getUser_id())).getPlayerlevel());
        }
        if (player.getSeat() != null) {
            seat = player.getSeat();
        } else {
            seat = "1";
        }
        switch (Integer.parseInt(seat)) {
            case 1:
                RummyTLog.e("vikas", "is middle join =" + player.isMiddleJoin() + "");
                if (player.isMiddleJoin()) {
                    this.mGameShecduleTv.setText("please wait for this hand to be completed ");
                    showView(mGameShecduleTv);
                    isSelfUserJoinInMiddleMatch = true;
                    setUserOptions(false);
                }
                setUserDetails(this.mUserPlayerLayout, player, this.mDealerId, isPlayerJoined);
                hideView(this.mAutoDropPlayer);
                mPlayerBoxesForRanks.put(player.getUser_id(), this.mUserPlayerLayout);
                if(RummyInstance.getInstance().getAssetsFolderName() != null && !RummyInstance.getInstance().getAssetsFolderName().equalsIgnoreCase(""))
                {
                    PicassoBridge.init(mActivity).load(RummyInstance.getInstance().getAssetsFolderName()).transform(new CircleTransform()).into(((ImageView) this.mUserPlayerLayout.findViewById(R.id.iv_avtar)));
                }
                else
                {
                    if(player.getGender() !=null && player.getGender().equalsIgnoreCase("FEMALE")){
                        PicassoBridge.init(mActivity).load(R.drawable.rummy_avtar_female).transform(new CircleTransform()).into(((ImageView) this.mUserPlayerLayout.findViewById(R.id.iv_avtar)));

                    }
                    else
                    {
                        PicassoBridge.init(mActivity).load(avatars[0]).transform(new CircleTransform()).into(((ImageView) this.mUserPlayerLayout.findViewById(R.id.iv_avtar)));
                        ((ImageView) this.mUserPlayerLayout.findViewById(R.id.iv_avtar)).setImageResource(avatars[0]);
                    }

                }

                if (player.isMiddleJoin()) {
                    showWaitingImage(this.mUserPlayerLayout);
                    isUserWaiting = true;
                    ll_last_game_checkbox.setClickable(false);
                    ll_drop_go_checkbox.setClickable(false);
                }
                return;
            case 2:
                showView(this.mSecondPlayerLayout);
                showView(this.mPlayer2Cards);
                setUpPlayerDetails(this.mSecondPlayerLayout, player, this.mDealerId, isPlayerJoined);
                mPlayerBoxesForRanks.put(player.getUser_id(), this.mSecondPlayerLayout);



                if(player.getGender() !=null && player.getGender().equalsIgnoreCase("FEMALE")){
                    PicassoBridge.init(mActivity).load(R.drawable.rummy_avtar_female).transform(new CircleTransform()).into(((ImageView) this.mSecondPlayerLayout.findViewById(R.id.iv_avtar)));
                }
                else
                {
                    PicassoBridge.init(mActivity).load(avatars[1]).transform(new CircleTransform()).into(((ImageView) this.mSecondPlayerLayout.findViewById(R.id.iv_avtar)));
                }

                if (player.isMiddleJoin()) {
                    showWaitingImage(this.mSecondPlayerLayout);
                }
                return;
            case 3:
                showView(this.mThirdPlayerLayout);
                showView(this.mPlayer3Cards);
                setUpPlayerDetails(this.mThirdPlayerLayout, player, this.mDealerId, isPlayerJoined);
                mPlayerBoxesForRanks.put(player.getUser_id(), this.mThirdPlayerLayout);


                if(player.getGender() !=null && player.getGender().equalsIgnoreCase("FEMALE")){
                    PicassoBridge.init(mActivity).load(R.drawable.rummy_avtar_female).transform(new CircleTransform()).into(((ImageView) this.mThirdPlayerLayout.findViewById(R.id.iv_avtar)));
                }
                else
                {
                    PicassoBridge.init(mActivity).load(avatars[2]).transform(new CircleTransform()).into(((ImageView) this.mThirdPlayerLayout.findViewById(R.id.iv_avtar)));
                }
                if (player.isMiddleJoin()) {
                    showWaitingImage(this.mThirdPlayerLayout);
                }
                return;
            case 4:
                showView(this.mFourthPlayerLayout);
                showView(this.mPlayer4Cards);
                setUpPlayerDetails(this.mFourthPlayerLayout, player, this.mDealerId, isPlayerJoined);
                mPlayerBoxesForRanks.put(player.getUser_id(), this.mFourthPlayerLayout);

                if(player.getGender() !=null && player.getGender().equalsIgnoreCase("FEMALE")){
                    PicassoBridge.init(mActivity).load(R.drawable.rummy_avtar_female).transform(new CircleTransform()).into(((ImageView) this.mFourthPlayerLayout.findViewById(R.id.iv_avtar)));
                }
                else
                {
                    PicassoBridge.init(mActivity).load(avatars[3]).transform(new CircleTransform()).into(((ImageView) this.mFourthPlayerLayout.findViewById(R.id.iv_avtar)));
                }
                if (player.isMiddleJoin()) {
                    showWaitingImage(this.mFourthPlayerLayout);
                }
                return;
            case 5:
                showView(this.mFifthPlayerLayout);
                showView(this.mPlayer5Cards);
                setUpPlayerDetails(this.mFifthPlayerLayout, player, this.mDealerId, isPlayerJoined);

                if(player.getGender() !=null && player.getGender().equalsIgnoreCase("FEMALE")){

                    PicassoBridge.init(mActivity).load(R.drawable.rummy_avtar_female).transform(new CircleTransform()).into(((ImageView) this.mFifthPlayerLayout.findViewById(R.id.iv_avtar)));
                }
                else
                {
                    PicassoBridge.init(mActivity).load(avatars[4]).transform(new CircleTransform()).into(((ImageView) this.mFifthPlayerLayout.findViewById(R.id.iv_avtar)));
                }
                mPlayerBoxesForRanks.put(player.getUser_id(), this.mFifthPlayerLayout);
                if (player.isMiddleJoin()) {
                    showWaitingImage(this.mFifthPlayerLayout);
                }
                return;
            case 6:
                showView(this.mSixthPlayerLayout);
                showView(this.mPlayer6Cards);
                setUpPlayerDetails(this.mSixthPlayerLayout, player, this.mDealerId, isPlayerJoined);
                mPlayerBoxesForRanks.put(player.getUser_id(), this.mSixthPlayerLayout);

                if(player.getGender() !=null && player.getGender().equalsIgnoreCase("FEMALE")) {

                    PicassoBridge.init(mActivity).load(R.drawable.rummy_avtar_female).transform(new CircleTransform()).into(((ImageView) this.mSixthPlayerLayout.findViewById(R.id.iv_avtar)));
                }
                else
                {
                    PicassoBridge.init(mActivity).load(avatars[5]).transform(new CircleTransform()).into(((ImageView) this.mSixthPlayerLayout.findViewById(R.id.iv_avtar)));
                }

                if (player.isMiddleJoin()) {
                    showWaitingImage(this.mSixthPlayerLayout);
                }
                return;
            default:
                return;
        }
    }

    private void resetWaitingplayer() {
        ll_last_game_checkbox.setClickable(true);
        ll_drop_go_checkbox.setClickable(true);
        isUserWaiting = false;
        hideView(this.mSecondPlayerLayout.findViewById(R.id.player_waiting_iv));
        hideView(this.mThirdPlayerLayout.findViewById(R.id.player_waiting_iv));
        hideView(this.mFourthPlayerLayout.findViewById(R.id.player_waiting_iv));
        hideView(this.mFifthPlayerLayout.findViewById(R.id.player_waiting_iv));
        hideView(this.mSixthPlayerLayout.findViewById(R.id.player_waiting_iv));
        hideView(this.mUserPlayerLayout.findViewById(R.id.player_waiting_iv));

    }

    private void blockUserView() {

        invisibleView(this.mUserPlayerLayout.findViewById(R.id.ll_player_content_layout));
        invisibleView(this.mSecondPlayerLayout.findViewById(R.id.ll_player_content_layout));
        invisibleView(this.mThirdPlayerLayout.findViewById(R.id.ll_player_content_layout));
        invisibleView(this.mFourthPlayerLayout.findViewById(R.id.ll_player_content_layout));
        invisibleView(this.mFifthPlayerLayout.findViewById(R.id.ll_player_content_layout));
        invisibleView(this.mSixthPlayerLayout.findViewById(R.id.ll_player_content_layout));

        invisibleView(this.mUserPlayerLayout.findViewById(R.id.player_name_layout));
        invisibleView(this.mSecondPlayerLayout.findViewById(R.id.player_name_layout));
        invisibleView(this.mThirdPlayerLayout.findViewById(R.id.player_name_layout));
        invisibleView(this.mFourthPlayerLayout.findViewById(R.id.player_name_layout));
        invisibleView(this.mFifthPlayerLayout.findViewById(R.id.player_name_layout));
        invisibleView(this.mSixthPlayerLayout.findViewById(R.id.player_name_layout));

        invisibleView(this.mUserPlayerLayout.findViewById(R.id.ll_player_point_round));
        invisibleView(this.mSecondPlayerLayout.findViewById(R.id.ll_player_point_round));
        invisibleView(this.mThirdPlayerLayout.findViewById(R.id.ll_player_point_round));
        invisibleView(this.mFourthPlayerLayout.findViewById(R.id.ll_player_point_round));
        invisibleView(this.mFifthPlayerLayout.findViewById(R.id.ll_player_point_round));
        invisibleView(this.mSixthPlayerLayout.findViewById(R.id.ll_player_point_round));

        invisibleView(this.mUserPlayerLayout.findViewById(R.id.ll_player_rank_square));
        invisibleView(this.mSecondPlayerLayout.findViewById(R.id.ll_player_rank_square));
        invisibleView(this.mThirdPlayerLayout.findViewById(R.id.ll_player_rank_square));
        invisibleView(this.mFourthPlayerLayout.findViewById(R.id.ll_player_rank_square));
        invisibleView(this.mFifthPlayerLayout.findViewById(R.id.ll_player_rank_square));
        invisibleView(this.mSixthPlayerLayout.findViewById(R.id.ll_player_rank_square));

        invisibleView(this.mUserPlayerLayout.findViewById(R.id.iv_avtar));
        invisibleView(this.mSecondPlayerLayout.findViewById(R.id.iv_avtar));
        invisibleView(this.mThirdPlayerLayout.findViewById(R.id.iv_avtar));
        invisibleView(this.mFourthPlayerLayout.findViewById(R.id.iv_avtar));
        invisibleView(this.mFifthPlayerLayout.findViewById(R.id.iv_avtar));
        invisibleView(this.mSixthPlayerLayout.findViewById(R.id.iv_avtar));

        showView(this.mUserPlayerLayout.findViewById(R.id.player_info_block));
        showView(this.mSecondPlayerLayout.findViewById(R.id.player_info_block));
        showView(this.mThirdPlayerLayout.findViewById(R.id.player_info_block));
        showView(this.mFourthPlayerLayout.findViewById(R.id.player_info_block));
        showView(this.mFifthPlayerLayout.findViewById(R.id.player_info_block));
        showView(this.mSixthPlayerLayout.findViewById(R.id.player_info_block));



    }

    private void unBlockUserView() {

        hideView(this.mUserPlayerLayout.findViewById(R.id.player_info_block));
        hideView(this.mSecondPlayerLayout.findViewById(R.id.player_info_block));
        hideView(this.mThirdPlayerLayout.findViewById(R.id.player_info_block));
        hideView(this.mFourthPlayerLayout.findViewById(R.id.player_info_block));
        hideView(this.mFifthPlayerLayout.findViewById(R.id.player_info_block));
        hideView(this.mSixthPlayerLayout.findViewById(R.id.player_info_block));

        showView(this.mUserPlayerLayout.findViewById(R.id.ll_player_content_layout));
        showView(this.mSecondPlayerLayout.findViewById(R.id.ll_player_content_layout));
        showView(this.mThirdPlayerLayout.findViewById(R.id.ll_player_content_layout));
        showView(this.mFourthPlayerLayout.findViewById(R.id.ll_player_content_layout));
        showView(this.mFifthPlayerLayout.findViewById(R.id.ll_player_content_layout));
        showView(this.mSixthPlayerLayout.findViewById(R.id.ll_player_content_layout));

        showView(this.mUserPlayerLayout.findViewById(R.id.player_name_layout));
        showView(this.mSecondPlayerLayout.findViewById(R.id.player_name_layout));
        showView(this.mThirdPlayerLayout.findViewById(R.id.player_name_layout));
        showView(this.mFourthPlayerLayout.findViewById(R.id.player_name_layout));
        showView(this.mFifthPlayerLayout.findViewById(R.id.player_name_layout));
        showView(this.mSixthPlayerLayout.findViewById(R.id.player_name_layout));

       /* showView(this.mUserPlayerLayout.findViewById(R.id.ll_player_point_round));
        showView(this.mSecondPlayerLayout.findViewById(R.id.ll_player_point_round));
        showView(this.mThirdPlayerLayout.findViewById(R.id.ll_player_point_round));
        showView(this.mFourthPlayerLayout.findViewById(R.id.ll_player_point_round));
        showView(this.mFifthPlayerLayout.findViewById(R.id.ll_player_point_round));
        showView(this.mSixthPlayerLayout.findViewById(R.id.ll_player_point_round));*/

        /*showView(this.mUserPlayerLayout.findViewById(R.id.ll_player_rank_square));
        showView(this.mSecondPlayerLayout.findViewById(R.id.ll_player_rank_square));
        showView(this.mThirdPlayerLayout.findViewById(R.id.ll_player_rank_square));
        showView(this.mFourthPlayerLayout.findViewById(R.id.ll_player_rank_square));
        showView(this.mFifthPlayerLayout.findViewById(R.id.ll_player_rank_square));
        showView(this.mSixthPlayerLayout.findViewById(R.id.ll_player_rank_square));*/

        showView(this.mUserPlayerLayout.findViewById(R.id.iv_avtar));
        showView(this.mSecondPlayerLayout.findViewById(R.id.iv_avtar));
        showView(this.mThirdPlayerLayout.findViewById(R.id.iv_avtar));
        showView(this.mFourthPlayerLayout.findViewById(R.id.iv_avtar));
        showView(this.mFifthPlayerLayout.findViewById(R.id.iv_avtar));
        showView(this.mSixthPlayerLayout.findViewById(R.id.iv_avtar));

    }



    public boolean isOpponentValidShow() {
        return this.opponentValidShow;
    }

    @Subscribe
    public void onMessageEvent(RummyEngineRequest engineRequest) {
        RummyTLog.e("PARTH", engineRequest.getCommand()+"---");
        try {
            if (!engineRequest.getTableId().equalsIgnoreCase(this.tableId)) {
                if (engineRequest.getCommand().equalsIgnoreCase("request_join_table")) {
                    RummyTLog.e("vikas", "calling request_join_table event from table Fragment");
                    RummyTLog.e("PARTH","enetr request_join_table");

                    if (this.mTourneyId == null || this.mTourneyId.equalsIgnoreCase("") || !this.mTourneyId.equalsIgnoreCase(engineRequest.getTournament_id())) {

                        RummyTLog.e("PARTH","this table is not tourney");

                        if(engineRequest.getOld_table_id() != null && engineRequest.getOld_table_id().equalsIgnoreCase(this.tableId))
                        {

                            RummyTLog.e("PARTH","all table is matched with curerent table");

                            RummyTablesFragment.this.resetAllPlayers();
                            RummyTablesFragment.this.resetDealer();
                            this.mRummyView.removeViews();
                            this.mRummyView.invalidate();
                            this.resetAllGroupsCountUI();
                            RummyTablesFragment.this.removeGameResultFragment();
                            RummyTablesFragment.this.removeMeldCardsFragment();
                            this.mSelectedCards.clear();
                            this.playerCards.clear();
                            this.mMeldGroupList.clear();

                            clearData();

                            List<RummyJoinedTable> joinedTables = RummyApplication.getInstance().getJoinedTableIds();
                            for (RummyJoinedTable joinedTable : joinedTables) {
                                if (joinedTable.getTabelId().equalsIgnoreCase(engineRequest.getOld_table_id())) {
                                    joinedTable.setTabelId(engineRequest.getTableId());
                                    joinedTable.setTourney(false);
                                    break;
                                }
                            }

                            //  ((RummyApplication) TablesFragment.this.mActivity.getApplication()).setJoinedTableIds(joinedTables);
                            Log.e("vikas","update fragment from event");
                            ((RummyTableActivity) RummyTablesFragment.this.mActivity).updateFragment(engineRequest.getOld_table_id(), engineRequest.getTableId(), null,"");
                            // ((TableActivity) TablesFragment.this.mActivity).updateFragment(TablesFragment.this.tableId, engineRequest.getTableId(), "tournament",engineRequest.getTournament_id());
                        }
                        else
                        {

                            RummyTLog.e("PARTH","all table is not matched with current table");

                            List<RummyJoinedTable> joinedTables = RummyApplication.getInstance().getJoinedTableIds();
                            if(joinedTables.size() < 2)
                            {
                                RummyJoinedTable joinedTable = new RummyJoinedTable();
                                joinedTable.setTabelId(engineRequest.getTableId());
                                joinedTable.setTourneyId(engineRequest.getTournament_id());
                                //  joinedTable.setTourney(true);
                                //    ((RummyApplication) TablesFragment.this.mActivity.getApplication()).setJoinedTableIds(joinedTable);

                                ((RummyTableActivity) RummyTablesFragment.this.mActivity).AddAnotherTourney(engineRequest.getTournament_id(), engineRequest.getTableId(), "tournament");
                            }
                        }


                    } else
                    {
                        RummyTLog.e("PARTH","this table is touney");
                        RummyTablesFragment.this.resetAllPlayers();
                        RummyTablesFragment.this.resetDealer();
                        this.mRummyView.removeViews();
                        this.mRummyView.invalidate();
                        this.resetAllGroupsCountUI();
                        RummyTablesFragment.this.removeGameResultFragment();
                        RummyTablesFragment.this.removeMeldCardsFragment();
                        this.mSelectedCards.clear();
                        this.playerCards.clear();
                        this.mMeldGroupList.clear();

                        clearData();


                     /*   JoinedTable joinedTable = new JoinedTable();
                        joinedTable.setTabelId(engineRequest.getTableId());
                        joinedTable.setTourneyId(engineRequest.getTournament_id());
                        ((RummyApplication) TablesFragment.this.mActivity.getApplication()).setJoinedTableIds(joinedTable);
                        ((TableActivity) TablesFragment.this.mActivity).updateFragment(TablesFragment.this.tableId, engineRequest.getTableId(), "tournament");*/

                        List<RummyJoinedTable> joinedTables = RummyApplication.getInstance().getJoinedTableIds();
                        for (RummyJoinedTable joinedTable : joinedTables) {
                            if (joinedTable.getTourneyId().equalsIgnoreCase(engineRequest.getTournament_id())) {
                                joinedTable.setTabelId(engineRequest.getTableId());
                                joinedTable.setTourney(true);
                                break;
                            }
                        }


                        Log.e("vikas","update fragment from event");
                        ((RummyTableActivity) RummyTablesFragment.this.mActivity).updateFragment(RummyTablesFragment.this.tableId, engineRequest.getTableId(), "tournament",engineRequest.getTournament_id());


                    }


                }
            } else {
                if (engineRequest.command.equalsIgnoreCase("meld")) {
                    unBlockUserView();
                    disableDropButton(this.mDropPlayer);
                    hideView(this.mAutoDropPlayer);
                    RummyTLog.e("vikas", "calling meld command with table id" + this.tableId);
                    ((RummyTableActivity) this.mActivity).closeSettingsMenu();
                    String successUserId = engineRequest.getSucessUserId();
                    String successUserName = engineRequest.getSucessUserName();
                    if (successUserId != null && successUserName != null) {
                        if (successUserId.equalsIgnoreCase(this.userData.getUserId())) {
                            showView(this.mClosedCard);
                            setUserOptionsOnValidShow();
                            return;
                        }
                        showView(this.mClosedCard);
                        this.meldTimeOut = engineRequest.getTimeout();
                        this.opponentValidShow = true;
                        RummySoundPoolManager.getInstance().playSound(R.raw.rummy_meld);
                        ((RummyTableActivity) this.mActivity).dismissScoreBoard();
                        removeGameResultFragment();
                        this.canLeaveTable = false;
                        this.isPlacedShow = true;
                        this.isTossEventRunning = false;
                        this.isCardsDistributing = false;
                        this.meldMsgUdid = engineRequest.getMsg_uuid();
                        startMeldTimer(Integer.parseInt(RummyUtils.formatString(engineRequest.getTimeout())), String.format("%s placed valid show, %s", new Object[]{successUserName, this.mActivity.getString(R.string.meld_success_msg)}), this.mGameShecduleTv);
                        if (getTotalCards() > 0) {
                            disableDropButton(this.mDropPlayer);
                            hideView(this.mAutoDropPlayer);
                            hideResumeLayout();
                            invisibleView(this.mShowBtn);
                            disableView(this.mShowBtn);
                            showView(this.mDeclareBtn);
                            enableView(this.mDeclareBtn);
                            if (!((RummyTableActivity) this.mActivity).isIamBackShowing()) {
                                showDeclareHelpView();
                                return;
                            }
                            return;
                        }
                        hideResumeLayout();
                        //invisibleView(this.mDeclareBtn);      // Before
                        showView(this.mDeclareBtn);             // After
                        showView(this.mShowBtn);
                        disableView(this.mShowBtn);

                        disableView(this.sortCards);
                    }
                } else if (engineRequest.command.equalsIgnoreCase("wrong_meld_correction")) {
                    RummyTLog.e("vikas", "calling wrong meld correction command with table id" + this.tableId);
                    this.isCardsDistributing = false;
                    this.isTossEventRunning = false;
                    this.canLeaveTable = false;
                    this.isUserPlacedValidShow = true;
                    showView(this.mSubFragment);
                    hideView(this.mGameResultsView);
                    hideView(this.mMeldCardsView);
                    showView(this.mSmartCorrectionView);
                    this.isSmartCorrectionShowing = true;
                    setSmartCorrectionView(engineRequest);
                    startWrongMeldTimer(Integer.parseInt(RummyUtils.formatString(engineRequest.getTimeout())), "Please send your cards: ");
                } else if (engineRequest.command.equalsIgnoreCase("rejoin")) {
                    showView(getGameResultsMessageView());
                    if(RummyTablesFragment.this.table_cost_type.equalsIgnoreCase("CASH_CASH")
                    ||RummyTablesFragment.this.table_cost_type.toLowerCase(Locale.ROOT).contains("cash")) {
                    hideWinnerView(this.mDialogLayout, this.winnerView, this.searchGameView, this.splitRejectedView);
                        showRejoinTablePopUp("Sorry, you are eliminated from this table. Do you want to rejoin?", engineRequest.getMsg_uuid());
                        startRejoinTimer(Integer.parseInt(RummyUtils.formatString(engineRequest.getTimeout())));
                    }
                } else if (engineRequest.command.equalsIgnoreCase("split")) {
                    cancelTimer(this.mGameScheduleTimer);
                    hideView(getGameResultsMessageView());
                    hideWinnerView(this.mDialogLayout, this.winnerView, this.searchGameView, this.splitRejectedView);
                    String message = "Player " + engineRequest.getRequester() + " placed a request to Split the prize money. Do you agree to split?";
                    showSplitPopUp(message, engineRequest.getMsg_uuid());
                    startSplitTimer(Integer.parseInt(RummyUtils.formatString(engineRequest.getTimeout())), message, engineRequest.getMsg_uuid());
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.e(TAG, e + "");
        } finally {
        }
    }

    private void setSmartCorrectionView(final RummyEngineRequest request) {
        setSmartCorrectionMeldView(this.mSmartCorrectionView, this.mJockerCard, request);
        ((ImageView) this.mSmartCorrectionView.findViewById(R.id.pop_up_close_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.mWrongMeldTv);
                RummyTablesFragment.this.cancelTimer(RummyTablesFragment.this.mWrongMeldTimer);
                RummyTablesFragment.this.sendSmartCorrectionAcceptRequest(request.getMsg_uuid(), false, request.getGameId());
                RummyTablesFragment.this.isSmartCorrectionShowing = false;
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.mSmartCorrectionView);
            }
        });
        ((Button) this.mSmartCorrectionView.findViewById(R.id.no_thanks_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.mWrongMeldTv);
                RummyTablesFragment.this.cancelTimer(RummyTablesFragment.this.mWrongMeldTimer);
                RummyTablesFragment.this.isSmartCorrectionShowing = false;
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.mSmartCorrectionView);
                RummyTablesFragment.this.sendSmartCorrectionAcceptRequest(request.getMsg_uuid(), false, request.getGameId());
            }
        });
        ((Button) this.mSmartCorrectionView.findViewById(R.id.sc_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.mWrongMeldTv);
                RummyTablesFragment.this.cancelTimer(RummyTablesFragment.this.mWrongMeldTimer);
                RummyTablesFragment.this.isSmartCorrectionShowing = false;
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.mSmartCorrectionView);
                RummyTablesFragment.this.sendSmartCorrectionAcceptRequest(request.getMsg_uuid(), true, request.getGameId());
            }
        });
    }

    private void startRejoinTimer(int scheduleTime) {
        try {
            cancelTimer(this.mRejoinTimer);
            this.mRejoinTimer = new CountDownTimer((long) (scheduleTime * 1000), 1000) {
                public void onTick(long millisUntilFinished) {
                    TextView textView = (TextView) RummyTablesFragment.this.searchGameView.findViewById(R.id.dialog_msg_tv);
                    textView.setText("Sorry, you are eliminated from this table.Do you want to rejoin ? time left [ " + (millisUntilFinished / 1000) + " ] seconds");
                    textView.setTextSize(14.0f);
                }

                public void onFinish() {
                    RummyTablesFragment.this.leaveTable();
                }
            }.start();
        } catch (Exception e) {
            RummyTLog.e(TAG, "Exception in startRejoinTimer :: " + e.getMessage());
        }
    }

    private void startSplitTimer(int scheduleTime, String messageStr, String megUuid) {
        try {
            cancelTimer(this.mRejoinTimer);
            final String str = messageStr;
            final String str2 = megUuid;
            this.mRejoinTimer = new CountDownTimer((long) (scheduleTime * 1000), 1000) {
                public void onTick(long millisUntilFinished) {
                    TextView textView = (TextView) RummyTablesFragment.this.searchGameView.findViewById(R.id.dialog_msg_tv);
                    textView.setText(str + " Please respond in " + (millisUntilFinished / 1000) + " seconds");
                    textView.setTextSize(14.0f);
                }

                public void onFinish() {
                    RummyTablesFragment.this.sendSplitAcceptRequest(str2, false);
                    RummyTablesFragment.this.cancelTimer(RummyTablesFragment.this.mRejoinTimer);
                    RummyTablesFragment.this.hideView(RummyTablesFragment.this.searchGameView);
                }
            }.start();
        } catch (Exception e) {
            RummyTLog.e(TAG, "Exception in startRejoinTimer :: " + e.getMessage());
        }
    }

    private void sendSmartCorrectionAcceptRequest(String msg_uuid, boolean isAccepted, String gameId) {
        RummySmartCorrectionRequest request = new RummySmartCorrectionRequest();
        request.setType("+OK");
        request.setText("200");
        request.setGameId(gameId);
        request.setAgree(isAccepted ? "1" : "0");
        request.setUuid(msg_uuid);
        request.setTableId(this.tableId);
        request.setUserId(this.userData.getUserId());
        request.setNickName(this.userData.getNickName());
        request.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(mActivity, RummyUtils.getObjXMl(request), null);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            RummyTLog.d(TAG, "doMelds" + gameEngineNotRunning.getLocalizedMessage());
        }
    }

    private void sendSplitAcceptRequest(String msg_uuid, boolean isAccepted) {
        RummyRejoinRequest request = new RummyRejoinRequest();
        request.setType(isAccepted ? "+OK" : "-ERR");
        request.setUuid(msg_uuid);
        request.setTableId(this.tableId);
        request.setUserId(this.userData.getUserId());
        request.setNickName(this.userData.getNickName());
        request.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(mActivity, RummyUtils.getObjXMl(request), null);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            RummyTLog.d(TAG, "doMelds" + gameEngineNotRunning.getLocalizedMessage());
        }
    }

    private void sendRejoinRequest(String msg_uuid) {
        RummyRejoinRequest request = new RummyRejoinRequest();
        request.setType("+OK");
        request.setUuid(msg_uuid);
        request.setTableId(this.tableId);
        request.setOrderId(RummyApplication.getInstance().getCurrentTableOrderId());
        request.setUserId(this.userData.getUserId());
        request.setNickName(this.userData.getNickName());
        request.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        request.setOrderId(RummyApplication.getInstance().getCurrentTableOrderId());
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(mActivity, RummyUtils.getObjXMl(request), this.rejoinListner);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            RummyTLog.d(TAG, "sendRejoinRequest" + gameEngineNotRunning.getLocalizedMessage());
        }
       
    }

    private void setUserOptionsOnValidShow() {
        showView(this.mShowBtn);
        invisibleView(this.mShowBtn);
        invisibleView(this.mDeclareBtn);
        disableView(this.mShowBtn);
        dismissToolTipView();
        disableDropButton(this.mDropPlayer);
        disableView(this.sortCards);
        this.canLeaveTable = false;
        this.isCardsDistributing = false;
        this.isTossEventRunning = false;
        this.isUserPlacedValidShow = true;
    }

    private void startMelding() {
        Log.d(TAG, "Inside startMelding************************************");
        this.mIsMelding = true;
        launchMeldFragment();
    }

    @Subscribe
    public void onMessageEvent(RummyEvent event) {

        try {

            if (!event.getEventName().equalsIgnoreCase("HEART_BEAT")) {
                if (RummyUtils.isAppInDebugMode()) {

                    Log.d(Cust_TAG, "vikas, onMessage event calling");
                    Log.d(Cust_TAG, "vikas, Event name:" + event.getEventName());
                }

            }

            if (event.getTableId() != null && event.getTableId().equalsIgnoreCase(this.tableId)) {
                dismissDialog();
                String eventName = event.getEventName();
                //            Log.e("eventName",eventName);
                if (eventName.equalsIgnoreCase("PLAYER_JOIN")) {
                    handlePlayerJoinEvent(event);
                    updatePlayersJoinCount();   //psp need to uncomment if req
                } else if (!eventName.equalsIgnoreCase("get_table_details")) {
                    Iterator<RummyGamePlayer> r3;
                    if (eventName.equalsIgnoreCase("PLAYER_QUIT")) {
                        if (event.getUserId() != Integer.parseInt(this.userData.getUserId())) {
                            updatePlayersJoinCount(); // psp need to uncomment if req
                       /*     showView(this.mGameShecduleTv);
                            this.mGameShecduleTv.setText(String.format("%s %s", new Object[]{event.getNickName(), "left the table"}));
                       */ }
                        handlePlayerQuitEvent(event);
                        if (this.isGameStarted) {
                            String joinedAs = event.getTableJoinAs();
                            if (joinedAs != null) {
                                if (joinedAs.equalsIgnoreCase("play")) {
                                    handlePlayerDrop(event.getUserId());
                                }
                            }
                        }
                        Date currentTime = Calendar.getInstance().getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                        String dateFormat = sdf.format(currentTime);
                        RummyPrefManagerTracker.saveString(getContext(), "playerquit", dateFormat + "");
                        alTrackList.add("playerquit");
                        Log.e("gameend", dateFormat + "");
                        getTrackSharedPrefs();
                    } else if (eventName.equalsIgnoreCase("GAME_SCHEDULE")) {
                        //                    Log.e("TwoTables","GAME_SCHEDULE");
                        //                    Log.e("TwoTables",mTableDetails.getTableType()+"");

                        if(RummyUtils.getVariantType(this.mTableDetails.getTableType()).equalsIgnoreCase("Points")
                                || isShowPlayersJoinedMessage()){
                            showView(this.secure_bottom_view); // psp
                            blockUserView(); //psp
                        }else{
                            hideView(this.secure_bottom_view); // psp
                            unBlockUserView();
                        }
                        resetUserSeatingTimer(); //psp
                        resetWaitingplayer();
                        if (this.mTableDetails != null && this.mTableDetails.getTableType().equalsIgnoreCase(RummyUtils.PR_JOKER) /*&& checkRebuyIn()*/) {
                            if (mLastGamePlayer.isChecked() && !RummyTablesFragment.this.isMoveToOtherTable) {
                                RummyTablesFragment.this.removeGameResultFragment();
                                RummyTablesFragment.this.leaveTable(); /// last game

                            }
                            hideView(this.mReshuffleView);
                            sendTurnUpdateMessage(false);
                            resetIamBackScreen();
                            this.canLeaveTable = true;
                            this.isGameDescheduled = false;
                            dismissQuickMenu();
                            resetDealer();
                            handleGameScheduleEvent(event);
                        } else if (this.mTableDetails != null && !this.mTableDetails.getTableType().equalsIgnoreCase(RummyUtils.PR_JOKER)) {
                            hideView(this.mReshuffleView);
                            sendTurnUpdateMessage(false);
                            resetIamBackScreen();
                            this.canLeaveTable = true;
                            this.isGameDescheduled = false;
                            dismissQuickMenu();
                            resetDealer();
                            handleGameScheduleEvent(event);
                        }

                        invisibleView(this.mGameDeckLayout);  // psp
                        showView(this.mGameLogoIv);            //  psp

                        //showSecureBottomView(); // psp
                        hideBottomActions();      // psp
    
    
                        //// old code
                        /*if (this.mTableDetails != null && this.mTableDetails.getTableType().equalsIgnoreCase(RummyUtils.PR_JOKER))
                            checkRebuyIn();

                        hideView(this.mReshuffleView);
                        sendTurnUpdateMessage(false);
                        resetIamBackScreen();
                        this.canLeaveTable = true;
                        this.isGameDescheduled = false;
                        dismissQuickMenu();
                        resetDealer();
                        handleGameScheduleEvent(event);*/
                        updatePlayersJoinCount();   //psp need to uncomment if req
                        updateTableTypeView(); //psp
                    } else if (eventName.equalsIgnoreCase("GAME_DESCHEDULE")) {
                        blockUserView(); //psp
                        resetUserSeatingTimer(); //psp
                        hideSecureBottomView();  // psp
                        hideBottomActions();      // psp
                        hidePlayerJoinStatus(); //psp

                        String reason = event.getReason();
                        if (reason != null) {
                            if (!reason.contains("Split")) {
                                sendTurnUpdateMessage(false);
                                this.isGameDescheduled = true;
                                RummyTablesFragment.this.mLastGamePlayer.setChecked(false);
                                RummyTablesFragment.this.mDropAndGoPlayer.setChecked(false);
                                clearOtherPlayersData();
                                resetDealer();
                                clearData();
                                showHideView(true, this.mGameShecduleTv, false);
                                this.mGameShecduleTv.setText(reason);
                                ((RummyTableActivity) this.mActivity).closeSettingsMenu();
                                //removeGameResultFragment();
                                return;
                            }
                        }
                        this.mGameShecduleTv.setText(reason);
                        if (this.isSplitRequested) {
                            this.mGameShecduleTv.setText(getString(R.string.split_request_messsage));
                            this.isSplitRequested = false;
                        }
                        showHideView(true, this.mGameShecduleTv, false);
                        cancelTimer(this.meldTimer);
                        cancelTimer(this.playerTurnOutTimer);
                    } else if (eventName.equalsIgnoreCase("TABLE_TOSS")) {
                        hidePlayerJoinStatus(); //psp
                        this.isGameDescheduled = false;
                        this.isTossEventRunning = true;
                        this.canLeaveTable = false;

                        unBlockUserView(); //psp
                        resetUserSeatingTimer(); //psp
                        hideView(this.players_join_status_layout);  //psp

                        hideSecureBottomView(); // psp
                        showBottomActions();      // psp

                        handleTossEvent(event);
                    } else if (eventName.equalsIgnoreCase("SITTING_SEQ")) {
                        unBlockUserView(); //psp
                        resetUserSeatingTimer(); //psp
                        hidePlayerJoinStatus(); //psp
                        Log.e(TAG, "onMessageEvent: Not show count on toastssddfd" );
                        hideSecureBottomView(); // psp
                        showBottomActions();      // psp


                        updateTableTypeView(); // psp

                        this.hideTossAnimLayout(); //psp
                        hideView(this.mDialogLayout);
                        hideView(this.splitRejectedView);
                        this.isGameDescheduled = false;
                        resetAllPlayers();
                        showView(this.mGameDeckLayout);
                        hideView(this.mGameLogoIv);
                        hideView(this.mReshuffleView);
                        resetDealer();
                        setDealerId(event.getDealerId());
                        this.canLeaveTable = false;
                        handleSittingSeqEvent(event);
                        RummySoundPoolManager.getInstance().playSound(R.raw.rummy_card_distribute);
                    } else if (eventName.equalsIgnoreCase("START_GAME")) {
                        isSelfUserJoinInMiddleMatch = false;
                        this.isGameStarted = true;
                        this.isGameDescheduled = false;
                        ((RummyTableActivity) this.mActivity).updateGameId(event.getTableId(), event.getGameId());
                        this.mGameId = event.getGameId();
                        this.gameid_tourney_tv.setText(this.mGameId);
                        // crashlytics.setCustomKey("game_id", mGameId);
    
                        showView(this.mGameDeckLayout);
                        hideView(this.mGameLogoIv);
                        showHideView(false, this.mGameShecduleTv, false);
                        this.mPrizeMoney.setText(event.getPrizeMoney());
    
                        if (this.mTableDetails != null && this.mTableDetails.getTableType().equalsIgnoreCase(RummyUtils.PR_JOKER)) {
                            showView(this.btn_leave_table_game_result);
                            if (!isTourneyTable()) {
                                showView(this.last_game_layout_root);
                            }
                        } else {
                            hideView(this.btn_leave_table_game_result);
                            hideView(this.last_game_layout_root);
        
                        }
    
                        dismissDialog(this.mLeaveDialog);
                        this.game_id_tb.setText("#" + event.getGameId());
                        this.isCardPicked = false;
    
                        Date currentTime = Calendar.getInstance().getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                        String dateFormat = sdf.format(currentTime);
                        RummyPrefManagerTracker.saveString(getContext(), "startgame", dateFormat + "");
                        alTrackList.add("startgame");
                        Log.e("startgame", dateFormat + "");
                        //         PrefManagerTracker.createSharedPreferencesTracker(getContext());
                        RummyPrefManagerTracker.saveString(getContext(), "userid", userData.getUserId() + "");
                        Log.e("userid", userData.getUserId() + "");
                        String tableIdTrack = mTableId.getText() + "";
                        tableIdTrack = tableIdTrack.replace("#", "");
                        RummyPrefManagerTracker.saveString(getContext(), "tableid", tableIdTrack + "");
                        Log.e("tableId", tableIdTrack + "");
                        // crashlytics.setCustomKey("chips_type", tableIdTrack);
                        String[] gameIdTrack = mGameId.split("-");
                        RummyPrefManagerTracker.saveString(getContext(), "gameid", gameIdTrack[0] + "");
                        Log.e("gameid", this.mGameId + "");
                        // crashlytics.setCustomKey("chips_type", gameIdTrack[0]);
                        String[] subGameId = mGameId.split("-");
                        if (subGameId.length > 1) {
                            RummyPrefManagerTracker.saveString(getContext(), "subgameid", subGameId[1] + "");
                            Log.e("subGameId", subGameId[1] + "");
                            //  crashlytics.setCustomKey("chips_type", subGameId[1]);
                        } else {
                            RummyPrefManagerTracker.saveString(getContext(), "subgameid", 0 + "");
                            Log.e("subGameId", 0 + "");
                            //  crashlytics.setCustomKey("chips_type", 0 + "");
                        }
    
    
                    } else if (eventName.equalsIgnoreCase("autoplaystatus")) {
                        if (!this.isGameDescheduled) {
                            updateUserOnAutoPlay(event);
                        }
                        this.isGameDescheduled = false;
                    } else if (eventName.equalsIgnoreCase("CARD_DISCARD")) {
                        handleCardDisCardEvent(event);
                        //                    Crashlytics.getInstance().crash();
                    } else if (eventName.equalsIgnoreCase("TURN_UPDATE") || eventName.equalsIgnoreCase("TURN_EXTRATIME_RECONNECT")) {
                        if (event.getUserId() != Integer.parseInt(this.userData.getUserId())) {
                            if(getTotalCards() > 13) {
                                //This check is to solve Player elimination issue of Discard Event taking long time to process in Engine. And user gets turn update
                                RummyGameEngine.getInstance().stop();
                                RummyGameEngine.getInstance().stopEngine();
                                RummyUtils.sendEvent(RummyGameEvent.SERVER_DISCONNECTED);
                                return;
                            }
                        }
                        unBlockUserView(); //psp
                        hidePlayerJoinStatus();// psp
                        Log.e(TAG, "onMessageEvent: Not show count on toastsscc" );

                        hideSecureBottomView(); // psp
                        showBottomActions();
                        if (RummyUtils.isAppInDebugMode()) {
                            Log.d(Cust_TAG, "vikas msg functionevent, calling " + eventName);
        
                        }
    
                        RummyStack stack = event.getTurnUpdateStack();
                        if (stack != null) {
                            this.faceDownCardList.clear();
                            this.faceDownCardList.addAll(stack.getFaceDownStack());
                        }
    
                        if (stack != null)  // change latest
                        {
                            this.faceUpCardList.clear();
                            this.faceUpCardList.addAll(stack.getFaceUpStack());
                            if (this.faceUpCardList.size() > 0) {
                                setOpenCard((RummyPlayingCard) this.faceUpCardList.get(this.faceUpCardList.size() - 1));
                            }
                        }
    
                        if (this.playerUserId == event.getUserId()) {
                            String autoPlayCountStr = event.getAutoPlayCount();
                            if (autoPlayCountStr != null && Integer.parseInt(autoPlayCountStr) < Integer.parseInt(this.autoPlayCount)) {
                                return;
                            }
                        }
                        if (event.getAutoPlayCount() != null) {
                            this.autoPlayCount = event.getAutoPlayCount();
                            this.playerUserId = event.getUserId();
                        } else {
                            this.autoPlayCount = "-1";
                            this.playerUserId = -1;
                        }
                        this.turnCount++;
                        this.canLeaveTable = true;
                        this.isTossEventRunning = false;
                        this.isCardsDistributing = false;
                        this.userData = getUserData();
                        if (event.getUserId() == Integer.parseInt(this.userData.getUserId())) {
                            isCurrentlyMyTurn = true;
                            Log.e("TURN_UPDATE", "My TURN_UPDATE");
                            dismissDialog(this.mLeaveDialog);
                            sendTurnUpdateMessage(true);
                            ((RummyTableActivity) this.mActivity).closeSettingsMenu();
                            removeGameResultFragment();
                            RummySoundPoolManager.getInstance().playSound(R.raw.rummy_bell);
                            RummyVibrationManager.getInstance().vibrate(1);
    
    
                            if (autoDropPlayerFlag) {
                                dropPlayer();
                            } else {
                                if (mDropAndGoPlayer.isChecked() && this.mTableDetails != null && this.mTableDetails.getTableType().equalsIgnoreCase(RummyUtils.PR_JOKER)) ///// implement drop and go
                                {
                                    dropPlayer();

                                } else {
                                    hideView(mAutoDropPlayer);
                                }
                                //goneView(mAutoDropPlayer);
                            }
                            hideView(clResumeGame);
                            hideResumeShadow();

                           /* for (int i=0; i<alAutoDrop.size();i++){
                                String strTableId = alAutoDrop.get(i);
                                int intIndex = alAutoDrop.indexOf(strTableId);
                                if(alAutoDropBoolean.get(intIndex)){
                                    autoDropPlayer(alAutoDrop.get(i)+"");
                                }else{

                                }
                            }*/
    
                        } else {
                            isCurrentlyMyTurn = false;
                            sendTurnUpdateMessage(false);
                        }
                        showView(this.mGameDeckLayout);
                        hideView(this.mGameLogoIv);
                        this.autoExtraTime = false;
                        handleTurnUpdateEvent(event);
                    } else if (eventName.equalsIgnoreCase("TURN_EXTRATIME")) {
                        handleTurnExtraTimeEvent(event);
                    } else if (eventName.equalsIgnoreCase("SEND_DEAL")) {
                        this.isGameStarted =true;
                        unBlockUserView();
                        resetUserSeatingTimer(); //psp
                        cancelTimer(this.mGameScheduleTimer);
                        RummyTablesFragment.this.mGameShecduleTv.setText("");
    
    
                        Log.d(TAG, "TABLE ID        : " + this.tableId);
                        Log.d(TAG, "TABLE ID IN DEAL: " + event.getTableId());
                        Log.d(TAG, "CARDS SIZE      : " + event.getPlayingCards().size());
    
                        Log.d(TAG, "Inside SEND_DEAL *********************************************************************");
                        this.isYourTurn = false;
                        hideView(this.mReshuffleView);
                        ((RummyTableActivity) this.mActivity).hideNavigationMenu();
                        this.isTossEventRunning = false;
                        this.isCardsDistributing = true;
                        enableView(this.sortCards);
                        showView(this.mGameDeckLayout);
    
                        if (this.canShowCardDistributeAnimation) {
                            animateCards(0, event);
                        } else {
                            handleSendDealEvent(event);
                            this.canShowCardDistributeAnimation = true;
                        }
    
                        hideView(this.mGameLogoIv);
                        r3 = this.mJoinedPlayersList.iterator();
                        while (r3.hasNext()) {
                            setUpPlayerCardsUI((RummyGamePlayer) r3.next());
                        }
                    } else if (eventName.equalsIgnoreCase("SEND_STACK")) {
                        this.isGameStarted =true;
                        ((RummyTableActivity) this.mActivity).hideNavigationMenu();
                        unBlockUserView();
                        resetUserSeatingTimer(); //psp
                        this.isTossEventRunning = false;
                        this.isCardsDistributing = true;
                        disableView(this.sortCards);
                        showView(this.mGameDeckLayout);
                        hideView(this.mGameLogoIv);
                        handleStackEvent(event);
                    } else if (eventName.equalsIgnoreCase("CARD_PICK")) {
                        this.isCardPicked = true;
                        handleCardPickEvent(event);
                        //                    Crashlytics.getInstance().crash();
                    } else if (eventName.equalsIgnoreCase("GAME_END")) {
                        try {
                            String userId = RummyPrefManager.getString(getContext(), RummyConstants.PLAYER_USER_ID, "");
    
                            if (RummyTablesFragment.this.table_cost_type != null && RummyTablesFragment.this.table_cost_type.equalsIgnoreCase("CASH_CASH")) {
                                RummyCommonEventTracker.trackGamePlayedEvent(RummyCommonEventTracker.Cash_Game_Played, userId, betAmount, tableType);
                            } else {
                                RummyCommonEventTracker.trackGamePlayedEvent(RummyCommonEventTracker.Free_Game_Played, userId, betAmount, tableType);
                            }
    
    
                        } catch (Exception e) {
                            Log.e("gopal", "error in tracking game played");
                        }
                        if (mDropAndGoPlayer.isChecked() && this.mTableDetails != null && this.mTableDetails.getTableType().equalsIgnoreCase(RummyUtils.PR_JOKER)) ///// implement drop and go
                        {
                            RummyTablesFragment.this.leaveTable();
                        }
                        hideView(clResumeGame);
                        hideResumeShadow();
                        showPlayersJoinedMessage = event.is_waiting_for_players();
                        Date currentTime = Calendar.getInstance().getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                        String dateFormat = sdf.format(currentTime);
                        RummyPrefManagerTracker.saveString(getContext(), "gameend", dateFormat + "");
                        alTrackList.add("gameend");
                        Log.e("gameend", dateFormat + "");
                        updateAutoPlayOnGameEnd();
                        resetIamBackScreen();
                        dismissToolTipView();
                        clearData();
                        getTrackSharedPrefs();
                    } else if (eventName.equalsIgnoreCase("STACK_RESUFFLE")) {
                        ((RummyTableActivity) this.mActivity).closeSettingsMenu();
                        clearStacks();
                        refreshStacks(event);
                    } else if (eventName.equalsIgnoreCase("meld_fail")) {
                        updateOpenDeckOnMeldFail(event);
                        cancelTimer(this.mGameScheduleTimer);
                        if (this.meldTimer != null) {
                            this.meldTimer.cancel();
                            this.meldTimer = null;
                            showHideView(false, this.mGameShecduleTv, false);
                        }
                        showView(this.mGameShecduleTv);
                        String playerName = event.getNickName();
                        if (Integer.parseInt(this.userData.getUserId()) == event.getUserId()) {
                            playerName = "You";
                            disbaleDeckCards();
                        } else {
                            enableDeckCards();
                        }
                        this.mGameShecduleTv.setText(String.format("%s %s", new Object[]{playerName, "placed the invalid show"}));
                        handlePlayerDrop(event.getUserId());
                        hideView(this.mClosedCard);
    
                    } else if (eventName.equalsIgnoreCase("meld_sucess")) {
                        hideView(this.mReshuffleView);
                        cancelTimer(this.meldTimer);
                        invisibleView(this.mGameShecduleTv);
                    } else if (eventName.equalsIgnoreCase("PRE_GAME_RESULT")) {
                        this.isSmartCorrectionShowing = false;
                        hideView(this.mReshuffleView);
                        clearAnimationData();
                        clearSelectedCards();
                        this.mRummyView.removeViews();
                        this.mRummyView.invalidate();
                        this.resetAllGroupsCountUI();
                        showView(this.mGameShecduleTv);
                        hideView(this.mDeclareBtn);
                        disableView(this.sortCards);
                        disableUserOptions();
                        ArrayList<RummyGamePlayer> mPlayersList = (ArrayList) event.getPlayer();
                        if (mPlayersList != null && mPlayersList.size() > 0) {
                            r3 = mPlayersList.iterator();
                            RummyGamePlayer player;
                            while (r3.hasNext()) {
                                player = (RummyGamePlayer) r3.next();
                                if (player != null && player.getMeldList() == null) {
                                    this.userNotDeclaredCards = true;
                                    break;
                                }
                            }
                        }
                        sendTurnUpdateMessage(false);
                        ((RummyTableActivity) this.mActivity).closeSettingsMenu();
                        launchGameResultsFragment(event);
                    } else if (eventName.equalsIgnoreCase("SHOW")) {
                        resetUserSeatingTimer(); //psp
                        unBlockUserView();
                        removeSeatingAnim();
                        Date currentTime = Calendar.getInstance().getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                        String dateFormat = sdf.format(currentTime);
                        RummyPrefManagerTracker.saveString(getContext(), "show", dateFormat + "");
                        alTrackList.add("show");
                        Log.e("show", dateFormat + "");
                        Log.d(TAG, "Inside SHOW************************************");
                        hideView(this.mReshuffleView);
                        sendTurnUpdateMessage(false);
                        ((RummyTableActivity) this.mActivity).closeSettingsMenu();
                        if (this.strIsTourneyTable.equalsIgnoreCase("yes") && this.playerCards.size() == 0)
                            clearData();
                        else
                            handleShowEvent(event);
                    } else if (eventName.equalsIgnoreCase("BALANCE_UPDATE")) {
                        Log.e("BALANCE_UPDATE", "BALANCE_UPDATE");
                        this.mTableDetails.getFun_chips();
                        Log.e("getFun_chips", this.mTableDetails.getFun_chips() + "@3010");
    
    
                    } else if (eventName.equalsIgnoreCase("GAME_RESULT")) {

                        /*if (this.mTableDetails.getTableType().equalsIgnoreCase(Utils.PR_JOKER))
                            checkRebuyIn();*/
    
                        this.isSmartCorrectionShowing = false;
                        hideView(this.mReshuffleView);
                        this.userNotDeclaredCards = false;
                        sendTurnUpdateMessage(false);
                        ((RummyTableActivity) this.mActivity).closeSettingsMenu();
                        handleGameResultsEvent(event);
                        ((RummyTableActivity) this.mActivity).updateScoreBoard(this.tableId, event);
                    } else if (eventName.equalsIgnoreCase("TABLE_CLOSED")) {
                        hideView(this.mReshuffleView);
                        ((RummyTableActivity) this.mActivity).closeSettingsMenu();
                        resetIamBackScreen();
                        this.isGameResultsShowing = true;
                        invisibleView(this.mUserPlayerLayout);
                        if (!this.isWinnerEventExecuted && (RummyTablesFragment.this.table_cost_type.equalsIgnoreCase("CASH_CASH")
                                ||RummyTablesFragment.this.table_cost_type.toLowerCase(Locale.ROOT).contains("cash"))) {
                            showMaxPointsPopUp(event);
                            this.isWinnerEventExecuted = false;
                        }
                        handleTableCloseEvent();
                        setTableButtonsUI();
                    } else if (!eventName.equalsIgnoreCase("PLAYER_ELIMINATE")) {
                        if (eventName.equalsIgnoreCase("rejoin")) {
                            String score = event.getRejoinScore();
                            int userId = event.getUserId();
                            RummyGamePlayer rejoinedPlayer = null;
                            r3 = this.mJoinedPlayersList.iterator();
                            RummyGamePlayer player;
                            while (r3.hasNext()) {
                                player = (RummyGamePlayer) r3.next();
                                if (player.getUser_id().equalsIgnoreCase(String.valueOf(userId))) {
                                    player.setTotalScore(score);
                                    rejoinedPlayer = player;
                                    break;
                                }
                            }
                            if (rejoinedPlayer != null) {
                                setPointsUI(userId, rejoinedPlayer);
                            }
                        } else if (eventName.equalsIgnoreCase("PLAYER_DROP")) {
                            Date currentTime = Calendar.getInstance().getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                            String dateFormat = sdf.format(currentTime);
                            RummyPrefManagerTracker.saveString(getContext(), "drop", dateFormat + "");
                            alTrackList.add("drop");
                            Log.e("drop", dateFormat + "");
                            ((RummyTableActivity) this.mActivity).closeSettingsMenu();
                            removeGameResultFragment();
                            if (event.getUserId() != Integer.parseInt(this.userData.getUserId())) {
                                this.mGameShecduleTv.setText(String.format("%s %s", new Object[]{event.getNickName(), "dropped"}));
                                showView(this.mGameShecduleTv);
                            } else {
                                dismissQuickMenu();
                                RummySoundPoolManager.getInstance().playSound(R.raw.rummy_drop);
                            }
                            handlePlayerDrop(event.getUserId());
                            if (this.strIsTourneyTable.equalsIgnoreCase("yes"))
                                this.removePlayerLevelFromBox(event.getNickName());
                        } else if (eventName.equalsIgnoreCase("TURN_TIMEOUT")) {
                            ((RummyTableActivity) this.mActivity).closeSettingsMenu();
                            removeGameResultFragment();
                            handlePlayerDrop(event.getUserId());
                        } else if (eventName.contains("_WINNER")) {
                            hideView(this.mReshuffleView);
                            ((RummyTableActivity) this.mActivity).closeSettingsMenu();
                            resetTimerInfo(this.mGameResultsView);
                            this.isGameResultsShowing = true;
                            this.isWinnerEventExecuted = true;
                            setTableButtonsUI();
                            RummySoundPoolManager.getInstance().playSound(R.raw.rummy_winners);
                            RummyVibrationManager.getInstance().vibrate(1);
                            List<RummyGamePlayer> gamePlayers = new ArrayList();
                            RummyGamePlayer player = new RummyGamePlayer();
                            player.setAmount(event.getPrizeMoney());
                            player.setNick_name(event.getWinnerNickName());
                            gamePlayers.add(player);
                            event.setPlayer(gamePlayers);
                            showWinnerFragment(this.mDialogLayout, this.winnerView, this.searchGameView, this.splitRejectedView, event, this.mTableDetails);
                            String joinAnotherGameMsg = "Congratulations , you won the game\nDo you want to join 1 more game?";
                            this.winnerId = event.getWinnerId();

/*
                                if (this.winnerId == null || !this.winnerId.equalsIgnoreCase(this.userData.getUserId())) {
                                    joinAnotherGame(event, "You have reached maximum number of points, would you like to play another game?");
                                } else {
                                    joinAnotherGame(event, joinAnotherGameMsg);
                                }*/  // latest changes
    
                        } else if (eventName.equalsIgnoreCase("VALID_SHOW")) {
                            hideView(this.mReshuffleView);
                            ((RummyTableActivity) this.mActivity).closeSettingsMenu();
                            removeGameResultFragment();
                            this.canLeaveTable = false;
                            this.isTossEventRunning = false;
                            this.isCardsDistributing = false;
                            String successUserName = event.getNickName();
                            if (event.getUserId() == Integer.parseInt(this.userData.getUserId())) {
                                showView(this.mClosedCard);
                                setUserOptionsOnValidShow();
                                showView(this.mGameShecduleTv);
                                startGameScheduleTimer(Integer.parseInt(RummyUtils.formatString(this.mTableDetails.getMeldTimeout())), true);
                            } else {
                                showView(this.mClosedCard);
                                this.isPlacedShow = true;
                                String message = String.format("%s placed valid show, %s", new Object[]{successUserName, this.mActivity.getString(R.string.meld_success_msg) + " "});
                                String timeOut = this.mTableDetails.getMeldTimeout();
                                if (this.meldTimeOut != null && this.meldTimeOut.length() > 0) {
                                    timeOut = this.meldTimeOut;
                                }
                                startMeldTimer(Integer.parseInt(RummyUtils.formatString(timeOut)), message, this.mGameShecduleTv);
                            }
                            if (getTotalCards() > 0) {
                                invisibleView(this.mShowBtn);
                                disableView(this.mShowBtn);
                                showView(this.mDeclareBtn);
                                enableView(this.mDeclareBtn);
                                if (!((RummyTableActivity) this.mActivity).isIamBackShowing()) {
                                    showDeclareHelpView();
                                    return;
                                }
                                return;
                            }
                            invisibleView(this.mDeclareBtn);
                            invisibleView(this.mShowBtn);
                            invisibleView(this.mShowBtn);
                            invisibleView(this.mDropPlayer);
                            invisibleView(this.sortCards);
                        } else if (eventName.equalsIgnoreCase("SEND_SLOTS")) {
                            unBlockUserView();
                            this.slotCards = new ArrayList();
                            this.slotCards.addAll(event.getTableCards().getCards());
                            setCardsOnIamBack(this.slotCards);
                        } else if (eventName.equalsIgnoreCase("SPLIT_STATUS")) {
                            if (event.getSplit().equalsIgnoreCase("True")) {
                                hideWinnerView(this.mDialogLayout, this.winnerView, this.searchGameView, this.splitRejectedView);
                                showSplitRequestPopUp(getString(R.string.split_request_pop_up_msg));
                            }
                        } else if (eventName.equalsIgnoreCase("SPLIT_RESULT")) {
                            hideView(this.mReshuffleView);
                            ((RummyTableActivity) this.mActivity).closeSettingsMenu();
                            resetTimerInfo(this.mGameResultsView);
                            this.isGameResultsShowing = true;
                            this.isWinnerEventExecuted = true;
                            setTableButtonsUI();
                            RummySoundPoolManager.getInstance().playSound(R.raw.rummy_winners);
                            RummyVibrationManager.getInstance().vibrate(1);
                            ArrayList arrayList = new ArrayList();
                            event.setPlayer(event.getSplitter().getPlayer());
                            showWinnerFragment(this.mDialogLayout, this.winnerView, this.searchGameView, this.splitRejectedView, event, this.mTableDetails);
                            //joinAnotherGame(event, getString(R.string.winner_congrats_pop_up_msg));
                        } else if (eventName.equalsIgnoreCase("SPLIT_FALSE")) {
                            String split = event.getSplit();
                            boolean isNameFound = false;
                            for (String equalsIgnoreCase : split.split(",")) {
                                if (equalsIgnoreCase.equalsIgnoreCase(this.userData.getNickName())) {
                                    isNameFound = true;
                                    break;
                                }
                            }
                            if (event.getSplit() != null && split.length() > 0 && !isNameFound) {
                                removeGameResultFragment();
                                showView(this.mDialogLayout);
                                invisibleView(this.winnerView);
                                invisibleView(this.searchGameView);
                                View splitRejectedView = this.mDialogLayout.findViewById(R.id.split_rjected_view);
                                showView(splitRejectedView);
                                ((TextView) splitRejectedView.findViewById(R.id.dialog_msg_tv)).setText("Player " + event.getSplit() + " has rejected your split request . You cannot split the prize money in this game.");
                                View view = splitRejectedView;
                                final View finalView = view;
                                ((Button) splitRejectedView.findViewById(R.id.ok_btn)).setOnClickListener(new OnClickListener() {
                                    public void onClick(View v) {
                                        RummyTablesFragment.this.hideView(finalView);
                                        RummyTablesFragment.this.hideView(RummyTablesFragment.this.mDialogLayout);
                                    }
                                });
                                view = splitRejectedView;
                                final View finalView1 = view;
                                ((ImageView) splitRejectedView.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new OnClickListener() {
                                    public void onClick(View v) {
                                        RummyTablesFragment.this.hideView(finalView1);
                                        RummyTablesFragment.this.hideView(RummyTablesFragment.this.mDialogLayout);
                                    }
                                });
                            }
                        } else if (eventName.equalsIgnoreCase("tournament_eleminate")) {
                            if (event.getTournamentId() != null && event.getTournamentId().equalsIgnoreCase(this.mTourneyId) && this.strIsTourneyTable.equalsIgnoreCase("yes")) {
                                Date currentTime = Calendar.getInstance().getTime();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                                String dateFormat = sdf.format(currentTime);
                                RummyPrefManagerTracker.saveString(getContext(), "eliminated", dateFormat + "");
                                alTrackList.add("eliminated");
                                Log.e("eliminated", dateFormat);
                                Log.d(TAG, "Eliminating player ---------------------------------------------------------");
                                clearSelectedCards();
                                clearStacks();
                                clearAnimationData();
                                removeMeldCardsFragment();
                                this.playerCards.clear();
                                this.mRummyView.removeViews();
                                this.mRummyView.invalidate();
                                this.resetAllGroupsCountUI();
                                this.mRummyView.setVisibility(View.INVISIBLE);
                                isCardsDistributing = false;
                                clearOtherPlayersData();
                                clearData();
    
                                if (this.levelTimer != null)
                                    this.levelTimer.cancel();
    
                                this.mGameShecduleTv.setText("You have been eliminated from tournament");
                                showView(this.mGameShecduleTv);
                            }
                        }
    
                    } else if (eventName.equalsIgnoreCase("PLAYER_ELIMINATE") && this.strIsTourneyTable.equalsIgnoreCase("yes")
                            && event.getNickName().equalsIgnoreCase(this.userData.getNickName())) {
                      /*  Date currentTime = Calendar.getInstance().getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                        String dateFormat = sdf.format(currentTime);
                        PrefManagerTracker.saveString(getContext(), "eliminated", dateFormat + "");
                        alTrackList.add("eliminated");
                        Log.e("eliminated", dateFormat);
                        Log.d(TAG, "Eliminating player ---------------------------------------------------------");
                        clearSelectedCards();
                        clearStacks();
                        clearAnimationData();
                        removeMeldCardsFragment();
                        this.playerCards.clear();
                        this.mRummyView.removeViews();
                        this.mRummyView.invalidate();
                        this.resetAllGroupsCountUI();
                        this.mRummyView.setVisibility(View.INVISIBLE);
                        isCardsDistributing = false;
                        clearData();*/
    
                        handlePlayerDrop(event.getUserId());
    
                    }
                }
                } else if (event.getEventName().equalsIgnoreCase("players_rank")) {
                if (event.getTournamentId() != null && event.getTournamentId().equalsIgnoreCase(this.mTourneyId) && this.strIsTourneyTable.equalsIgnoreCase("yes")) {
                    // this.mTourneyId = event.getTournamentId();
                    Log.e("vikas", "calling players_rank with tourney id =" + event.getTournamentId());
                    this.mPlayersRank = event;
                    this.mPlayersList = event.getPlayers();
                    this.updatePlayersRank();
                }
    
            } else if (event.getEventName().equalsIgnoreCase("end_tournament")) {
                    Log.e("vikas", "calling tournament end with tourney id =" + event.getTournamentId());
                    if (event.getTournamentId().equalsIgnoreCase(this.mTourneyId)) {
                        clearStacks();
                        clearData();
                        clearSelectedCards();
                        //resetAllPlayers();
                        clearOtherPlayersData();
                        this.levelTimerLayout.setVisibility(View.GONE);
        
                        if (this.levelTimer != null)
                            this.levelTimer.cancel();
        
                        this.canLeaveTable = true;
                        this.isTourneyEnd = true;
                        showView(this.mGameShecduleTv);
                        this.mGameShecduleTv.setText("This tournament has been closed.");
                        this.players_join_status_layout.setVisibility(View.GONE);
                        this.showGenericDialogWithMessage("This tournament has ended !", "end_tournament");
                    }
                } else if (event.getEventName().equalsIgnoreCase("level_start")) {
                /*if (!this.isMeldRequested) {
                    getLevelTimer();
                }*/
                    if (event.getTournamentId() != null && event.getTournamentId().equalsIgnoreCase(this.mTourneyId)) {
                        getLevelTimer();
                    }
    
                } else if (event.getEventName().equalsIgnoreCase("level_end")) {
                    if (event.getTournamentId() != null && event.getTournamentId().equalsIgnoreCase(this.mTourneyId)) {
                        getLevelTimer();
                    }
    
                } else if (event.getEventName().equalsIgnoreCase("tournament_result")) {
    
                    List<RummyGamePlayer> tourneyResults = event.getPlayers();
                    try {
                        if (!getActivity().isFinishing())
                            displayTourneyResults(tourneyResults);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
    
                } else if (event.getEventName().equalsIgnoreCase("disqualified")) {
                    if (event.getTournamentId() != null && event.getTournamentId().equalsIgnoreCase(this.mTourneyId) && event.getNickName().equalsIgnoreCase(this.userData.getNickName())) {
                        this.showGenericDialogWithMessage("Sorry! You have been disqualified from this tournament.", "disqualified");
                        clearSelectedCards();
                        clearData();
                        clearStacks();
                        clearAnimationData();
                        this.playerCards.clear();
                        removeMeldCardsFragment();
                    }
                } else if (event.getEventName().equalsIgnoreCase("tournament_eleminate")) {
                    if (event.getTournamentId() != null && event.getTournamentId().equalsIgnoreCase(this.mTourneyId) && this.strIsTourneyTable.equalsIgnoreCase("yes")) {
                        Date currentTime = Calendar.getInstance().getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                        String dateFormat = sdf.format(currentTime);
                        RummyPrefManagerTracker.saveString(getContext(), "eliminated", dateFormat + "");
                        alTrackList.add("eliminated");
                        Log.e("eliminated", dateFormat);
                        Log.d(TAG, "Eliminating tournament ---------------------------------------------------------");
                        clearSelectedCards();
                        clearStacks();
                        clearAnimationData();
                        removeMeldCardsFragment();
                        this.playerCards.clear();
                        this.mRummyView.removeViews();
                        this.resetAllGroupsCountUI();
                        this.mRummyView.invalidate();
                        this.mRummyView.setVisibility(View.INVISIBLE);
                        isCardsDistributing = false;
                        clearOtherPlayersData();
                        clearData();
        
                        if (this.levelTimer != null)
                            this.levelTimer.cancel();
        
        
                        if (isAdded()) {
                            this.showGenericDialog(getContext(), "You have been eliminated from tournament");
                        }
        
                        this.mGameShecduleTv.setText("You have been eliminated from tournament");
                        showView(this.mGameShecduleTv);
        
                    }
    
                } else if (event.getEventName().equalsIgnoreCase("tourney_end_wait")) {
                    if (event.getTournamentId() != null && event.getTournamentId().equalsIgnoreCase(this.mTourneyId) && this.strIsTourneyTable.equalsIgnoreCase("yes")) {
                        this.mGameShecduleTv.setText("Please wait for the games in level 3 to end and winners to be declared.");
                        showView(this.mGameShecduleTv);
        
                    }
                } else if (event.getEventName().equalsIgnoreCase("tournament_rebuyin")) {
                    if (event.getTournamentId() != null && event.getTournamentId().equalsIgnoreCase(this.mTourneyId) && this.strIsTourneyTable.equalsIgnoreCase("yes"))
                        this.showRebuyinDialog(event);
                }
            }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("OME", e + "");
        }

    }

    private void showSecureBottomView() {
        showView(this.secure_bottom_view); // psp


    }

    private void showBottomActions() {
        showView(this.bottom_actions);
    }

    private void hidePlayerJoinStatus() {
        hideView(this.players_join_status_layout);  //psp

    }

    private void hideBottomActions() {
        hideView(this.bottom_actions);
    }

    private void hideSecureBottomView() {
        hideView(secure_bottom_view);
    }

    private boolean checkRebuyIn() {
        boolean varToreturn = true;
        if (this.mTableDetails.getTableCost().equalsIgnoreCase("FUNCHIPS_FUNCHIPS")) {
            RummyTLog.e("TwoTables", mTableDetails.getMinimumbuyin() + "@1");
            RummyTLog.e("TwoTables", userData.getFunInPlay() + "@2");
            RummyTLog.e("TwoTables", RummyUtils.PR_JOKER_POINTS + "@3");
            if (Float.parseFloat(this.mTableDetails.getMinimumbuyin()) > Float.parseFloat(this.userData.getFunInPlay()))
            {
                this.leaveTable();
                if(!mDropAndGoPlayer.isChecked())
                showRebuyInPopTimer(this.tableId);
                varToreturn = false;
            }
            else
            {
                varToreturn = true;
            }



        } else if (this.mTableDetails.getTableCost().equalsIgnoreCase("CASH_CASH")) {
            if (Float.parseFloat(this.mTableDetails.getMinimumbuyin()) > Float.parseFloat(this.userData.getRealInPlay()))
            {
                //showRebuyInPopTimer();
                //TablesFragment.this.leaveTable();
                varToreturn = false;
            }
            else
            {
                varToreturn = true;
            }

        }
        return varToreturn;
    }

    private void showRebuyInPopTimer(String tableId) {
        Log.e("TwoTables", "showRebuyInPopTimer");
        final Dialog dialog = new Dialog(getContext(), R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rummy_dialog_rebuyin_timer);
        dialog.setCanceledOnTouchOutside(false);

        final TextView timer_tv = (TextView) dialog.findViewById(R.id.timer_tv);
        final Button yes_btn = (Button) dialog.findViewById(R.id.yes_btn);
        final Button no_btn = (Button) dialog.findViewById(R.id.no_btn);

        final CountDownTimer timer = new CountDownTimer(10 * 1000, 1000) { // timer for 10 seconds
            public void onTick(long millisUntilFinished) {
                timer_tv.setText("Time remaining: " + (millisUntilFinished / 1000) + " sec");
                timeLeft = Math.round(millisUntilFinished / 1000);
            }

            public void onFinish() {
                try {
                    dialog.dismiss();
                } catch (Exception e) {

                }
                if(tablePresentInCurrentJoinedTables(tableId))
                RummyTablesFragment.this.leaveTable();
            }
        }.start();

        no_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                timer.cancel();
                if (Float.parseFloat(mTableDetails.getMinimumbuyin()) > Float.parseFloat(userData.getRealInPlay())) {
                    RummyTablesFragment.this.leaveTable();
                }
            }
        });

        yes_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TablesFragment.this.rebuyChips();
                RummyTablesFragment.this.showBuyInPopUpSlider(RummyTablesFragment.this.mTableDetails);
                timer.cancel();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private boolean tablePresentInCurrentJoinedTables(String closedTableId) {
        boolean isTableIdPresent = false;
        List<RummyJoinedTable> joinedTables = RummyApplication.getInstance().getJoinedTableIds();
        for (RummyJoinedTable joinedTable : joinedTables) {
            if (closedTableId.equalsIgnoreCase(joinedTable.getTabelId())) {
                isTableIdPresent = true;
            }
        }

        return isTableIdPresent;
    }
    private void hideTable() {
        if (RummyTablesFragment.this.mDialogLayout.getVisibility() == View.VISIBLE) {
            RummyTablesFragment.this.hideView(RummyTablesFragment.this.mDialogLayout);
        } else if (RummyTablesFragment.this.mSubFragment.getVisibility() != View.VISIBLE) {
        } else {
            if (!((RummyTableActivity) RummyTablesFragment.this.mActivity).isIamBackShowing()) {
                ((RummyTableActivity) RummyTablesFragment.this.mActivity).showGameTablesLayout(RummyTablesFragment.this.tableId);
            }
            RummyTablesFragment.this.hideView(RummyTablesFragment.this.mSubFragment);
        }
    }

    int timeLeft;
    int flagRejoin = 0;

    private void showBuyInPopUpSlider(RummyTableDetails tableDetails) {
        String balance;

        final DecimalFormat format = new DecimalFormat("0.#");
        if (tableDetails.getTableCost().equalsIgnoreCase("CASH_CASH")) {
            balance = this.userData.getRealChips();
            mBalanceMoney.setText(balance);
        } else {
            balance = this.userData.getFunChips();
            mBalanceMoney.setText(balance);
        }
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogTheme);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.table_details_pop_up_small);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();

        final CountDownTimer timer = new CountDownTimer(timeLeft * 1000, 1000) { // timer for 10 seconds
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (flagRejoin == 0) {
                    try {
                        dialog.dismiss();
                        RummyTablesFragment.this.leaveTable();
                    } catch (Exception e) {
                    }
                } else {
                    hideTable();
                }
//                Toast.makeText(getContext(), "Eliminated", Toast.LENGTH_SHORT).show();
            }
        }.start();

        TextView minBuyTv = (TextView) dialog.findViewById(R.id.min_buy_value_tv);
        TextView maxBuyTv = (TextView) dialog.findViewById(R.id.max_buy_value_tv);
        final TextView balanceTv = (TextView) dialog.findViewById(R.id.balance_value_tv);
        TextView minValSeekbar = (TextView) dialog.findViewById(R.id.min_val_seekbar);
        TextView maxValSeekbar = (TextView) dialog.findViewById(R.id.max_val_seekbar);
        final EditText buyInTv = (EditText) dialog.findViewById(R.id.buy_in_value_tv);
        ((TextView) dialog.findViewById(R.id.bet_value_tv)).setText(tableDetails.getBet());
        minBuyTv.setText(tableDetails.getMinimumbuyin());
        maxBuyTv.setText(tableDetails.getMaximumbuyin());
        minValSeekbar.setText(tableDetails.getMinimumbuyin() + "");
        maxValSeekbar.setText(tableDetails.getMaximumbuyin() + "");
        final String maximumBuyIn = tableDetails.getMaximumbuyin();
        final int max = Integer.parseInt(maximumBuyIn);
        final int min = Integer.parseInt(tableDetails.getMinimumbuyin());
        boolean decreaseBalance = true;
        if (balance.contains(".")) {
            String subBalance = balance.substring(balance.lastIndexOf(".") + 1);
            if (subBalance != null && subBalance.length() > 0) {
                decreaseBalance = Integer.parseInt(subBalance) > 50;
            }
        }
        final float balanceInt = new Float((float) Math.round(Float.parseFloat(balance))).floatValue();
        balanceTv.setText(String.valueOf(format.format((double) balanceInt)));
        final RummyTableDetails table2 = tableDetails;
        ((Button) dialog.findViewById(R.id.join_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                flagRejoin = 1;
                if (buyInTv.getText() == null || buyInTv.getText().length() <= 0) {
                    RummyCommonMethods.showSnackbar(balanceTv, "Please enter minimum amount");
                    return;
                }
                float selectedBuyInAmt = Float.valueOf(buyInTv.getText().toString()).floatValue();
                if (selectedBuyInAmt <= balanceInt || selectedBuyInAmt >= Float.valueOf((float) max).floatValue()) {
                    if (selectedBuyInAmt > Float.valueOf((float) max).floatValue()) {
                        RummyCommonMethods.showSnackbar(balanceTv, "You can take only ( " + maximumBuyIn + " ) " + "in to the table");
                    } else if (selectedBuyInAmt < Float.valueOf((float) min).floatValue()) {
                        RummyCommonMethods.showSnackbar(balanceTv, "Please enter minimum amount");
                    } else {
                        RummyTablesFragment.this.rebuyChips(buyInTv.getText().toString());
                        dialog.dismiss();
                    }
                } else if (table2.getTableCost().contains("CASH_CASH")) {
                    RummyTablesFragment.this.showErrorBuyChipsDialog(getContext(), RummyTablesFragment.this.mActivity.getResources().getString(R.string.low_balance_free_chip));
                } else {
                    RummyTablesFragment.this.showErrorBuyChipsDialog(getContext(), RummyTablesFragment.this.mActivity.getResources().getString(R.string.low_balance_free_chip));
                }


            }
        });
        ((Button) dialog.findViewById(R.id.cancel_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.seek_bar);
        IndicatorSeekBar seekBar = dialog.findViewById(R.id.seek_bar_buy_in);
        seekBar.setMax((max - min) / 1);
        seekBar.setProgress(seekBar.getMax());
        if (Float.valueOf((float) max).floatValue() <= balanceInt) {
            buyInTv.setText(tableDetails.getMaximumbuyin());
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
       /* seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                    sliderValue = String.valueOf(format.format((double) f));
                } else {
                    sliderValue = String.valueOf(format.format((double) sliderValue_num));
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
    }
    
    private void rebuyChips(String chips) {
        
        RummyRebuyApiHelper.prRebuyIn(chips, this.mTableDetails, result -> {
                    if (result.isSuccess()) {
                        RummyRebuyRequest request = new RummyRebuyRequest();
                        request.setCommand("rebuyin");
                        request.setUuid(RummyUtils.generateUuid());
                        request.setTable_id(this.tableId);
                        request.setUser_id(this.userData.getUserId());
                        request.setRebuyinamt(chips);
                        request.setOrderId(result.getResult().optString("order_id",""));
                        
                        try {
                            RummyGameEngine.getInstance();
                            RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.rebuyResponseListener);
                        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                            RummyTLog.d(TAG, "rebuyChips" + gameEngineNotRunning.getLocalizedMessage());
                        }
                    } else if (!result.isLoading()) {
                        try {
                            showLongToast(requireContext(), result.getErrorMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        
        
    }


    private void updateOpenDeckOnMeldFail(RummyEvent event) {
        String suit = event.getSuit();
        String face = event.getFace();
        RummyPlayingCard meldFailCard = new RummyPlayingCard();
        meldFailCard.setFace(face);
        meldFailCard.setSuit(suit);
        setOpenCard(meldFailCard);
        this.faceUpCardList.add(meldFailCard);
    }

    private RummyLoginResponse getUserData() {
        if (this.userData == null) {
            RummyApplication app = RummyApplication.getInstance();
            if (app != null) {
                this.userData = app.getUserData();
            }
        }
        return this.userData;
    }

    private void resetIamBackScreen() {
        RummyIamBackFragment iamBackFragment = (RummyIamBackFragment) ((RummyTableActivity) this.mActivity).getFragmentByTag(RummyIamBackFragment.class.getName());
        if (iamBackFragment != null) {
            iamBackFragment.clearDiscardedCards();
        }
    }

    public void clearOtherPlayersData() {
        resetAllPlayers();
        ((RummyTableActivity) this.mActivity).resetPlayerIconsOnTableBtn(this.tableId);
        Iterator it = this.mJoinedPlayersList.iterator();
        while (it.hasNext()) {
            RummyGamePlayer gamePlayer = (RummyGamePlayer) it.next();
            if (gamePlayer.getUser_id().equalsIgnoreCase(this.userData.getUserId())) {
                RummyGamePlayer player = gamePlayer;
                RummyTLog.e(TAG, "SEATING VIA: clearOtherPlayersData");
                setUpPlayerUI(player, false);
                setPlayerPositionsOnTableBtn(this.mTableDetails, player, false);
                return;
            }
        }
    }

    private void handleCardDisCardEvent(RummyEvent event) {
        RummyGamePlayer player = null;
        Iterator it = this.mJoinedPlayersList.iterator();
        while (it.hasNext()) {
            RummyGamePlayer gamePlayer = (RummyGamePlayer) it.next();
            if (gamePlayer.getUser_id().equalsIgnoreCase(String.valueOf(event.getUserId()))) {
                player = gamePlayer;
                break;
            }
        }
        if (!(player == null || player.getSeat() == null)) {
            switch (Integer.parseInt(player.getSeat())) {
                case 2:
                    animateDiscardCard(0, this.mSecondPlayerLayout);
                    break;
                case 3:
                    animateDiscardCard(0, this.mThirdPlayerLayout);
                    break;
                case 4:
                    animateDiscardCard(0, this.mFourthPlayerLayout);
                    break;
                case 5:
                    animateDiscardCard(0, this.mFifthPlayerLayout);
                    break;
                case 6:
                    animateDiscardCard(0, this.mSixthPlayerLayout);
                    break;
            }
        }
        RummyPlayingCard discardCard = new RummyPlayingCard();
        discardCard.setFace(event.getFace());
        discardCard.setSuit(event.getSuit());
        setOpenCard(discardCard);
        this.faceUpCardList.add(discardCard);
        ((RummyTableActivity) this.mActivity).addDiscardToPlayer(event);
        if (event.getAutoPlay().equalsIgnoreCase("true") && event.getUserId() == Integer.parseInt(this.userData.getUserId())) {
            discardCardOnAutoPlay(discardCard);
            showAutoDiscardedCards(event, discardCard);
            RummyUtils.temp_last_auto_drop_card = discardCard.getFace()+"_"+discardCard.getSuit()+"_"+event.getTableId();
        }
    }

    private void showAutoDiscardedCards(RummyEvent event, RummyPlayingCard discardCard) {
        RummyIamBackFragment iamBackFragment = (RummyIamBackFragment) ((RummyTableActivity) this.mActivity).getFragmentByTag(RummyIamBackFragment.class.getName());
        if (iamBackFragment != null) {
            iamBackFragment.showAutoPlayCards(discardCard, event);
        }
    }

    private void discardCardOnAutoPlay(RummyPlayingCard discardCard) {
        String discardCardStr = String.format("%s%s", new Object[]{discardCard.getSuit(), discardCard.getFace()});
        String discardSuit = null;
        String disCardFace = null;
        boolean cardRemoved = false;
        for (int i = this.mGroupList.size() - 1; i >= 0; i--) {
            ArrayList<RummyPlayingCard> groupList = (ArrayList) this.mGroupList.get(i);
            for (int j = groupList.size() - 1; j >= 0; j--) {
                RummyPlayingCard card = (RummyPlayingCard) groupList.get(j);
                if (discardCardStr.equalsIgnoreCase(String.format("%s%s", new Object[]{card.getSuit(), card.getFace()}))) {
                    discardSuit = card.getSuit();
                    disCardFace = card.getFace();
                    groupList.remove(card);
                    cardRemoved = true;
                    break;
                }
            }
            if (cardRemoved) {
                break;
            }
        }
        if (!(discardSuit == null || disCardFace == null)) {
            updateDeckCardsOnDiscard(discardSuit, disCardFace);
        }
        setGroupView(false);
    }
    private void hideTossAnimLayout() {
      /*  hideView(this.mUserPlayerLayout.findViewById(R.id.toss_winner_layout));
        hideView(this.mSecondPlayerLayout.findViewById(R.id.toss_winner_layout));
        hideView(this.mThirdPlayerLayout.findViewById(R.id.toss_winner_layout));
        hideView(this.mFourthPlayerLayout.findViewById(R.id.toss_winner_layout));
        hideView(this.mFifthPlayerLayout.findViewById(R.id.toss_winner_layout));
        hideView(this.mSixthPlayerLayout.findViewById(R.id.toss_winner_layout));*/
    }


    private void showLastGamePostPopUp(final boolean willCloseActivity) {
        dismissLoadingDialog();
        final Dialog dialog = new Dialog(RummyTablesFragment.this.mActivity, R.style.DialogTheme);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.rummy_dialog_generic_game_room);
        ((TextView) dialog.findViewById(R.id.dialog_msg_tv)).setText("You have been removed from table\nas you chose Last Game option.");
        ((AppCompatTextView) dialog.findViewById(R.id.ok_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                dialog.dismiss();
                if (willCloseActivity) {
                    RummyTablesFragment.this.mActivity.finish();
                } else {
                    ((RummyTableActivity) RummyTablesFragment.this.mActivity).updateTableFragment(RummyTablesFragment.this.tableId);
                }

            }
        });

        ((AppCompatImageView) dialog.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                dialog.dismiss();
            }
        });



        dialog.show();
    }

    private void showResumeShadow(){
        showView(resumeShadow);
        mRummyView.setEnabled(false);
        ll_last_game_checkbox.setEnabled(false);
        mLastGamePlayer.setEnabled(false);
        ll_drop_go_checkbox.setEnabled(false);
        mDropAndGoPlayer.setEnabled(false);
        enableDeckCards();
        disbaleDeckCards();
        disableDropButton(this.mDropPlayer);
    }

    private void hideResumeShadow(){
        hideView(resumeShadow);
        mRummyView.setEnabled(true);
        ll_last_game_checkbox.setEnabled(true);
        mLastGamePlayer.setEnabled(true);
        ll_drop_go_checkbox.setEnabled(true);
        mDropAndGoPlayer.setEnabled(true);
        enableDropButton(this.mDropPlayer);
        enableDeckCards();
    }

    private void hideResumeLayout(){
        RummyTablesFragment.this.mDropAndGoPlayer.setChecked(false);
        autoDropPlayerFlag = false;
        setAutoDropSetting(false);
        clResumeGame.setVisibility(View.GONE);
        hideResumeShadow();
    }


    private void showDropAndGoPostPopUP(final boolean willCloseActivity) {
        final Dialog dialog = new Dialog(RummyTablesFragment.this.mActivity, R.style.DialogTheme);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.rummy_dialog_generic_game_room);
        ((TextView) dialog.findViewById(R.id.dialog_msg_tv)).setText("You have been removed from the table\n as you choose Drop & Go Option.");
        ((AppCompatTextView) dialog.findViewById(R.id.ok_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                mDropAndGoPlayer.setChecked(false);
                if (willCloseActivity) {
                    RummyTablesFragment.this.mActivity.finish();

                } else {
                    ((RummyTableActivity) RummyTablesFragment.this.mActivity).updateTableFragment(RummyTablesFragment.this.tableId);
                }


            }
        });

        ((AppCompatImageView) dialog.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                dialog.dismiss();
            }
        });



        dialog.show();
    }

    private void disableDropButton(View view){
        view.setEnabled(false);
        view.setClickable(false);
        view.setAlpha(RummyFancyCoverFlow.SCALEDOWN_GRAVITY_CENTER);

    }

    private void enableDropButton(View view){
        if(mRummyView.isEnabled()) {
            view.setEnabled(true);
            view.setClickable(true);
            view.setAlpha(1.0f);
        }
    }

    private void updateUserOnTurn(RummyEvent event) {
        RummyGamePlayer player = null;
        Iterator it = this.mJoinedPlayersList.iterator();
        while (it.hasNext()) {
            RummyGamePlayer gamePlayer = (RummyGamePlayer) it.next();
            if (gamePlayer.getUser_id().equalsIgnoreCase(String.valueOf(event.getUserId()))) {
                String autoPlayCount = event.getAutoPlayCount();
                String autoPlay = event.getAutoPlay();
                String totalCount = event.getTotalCounr();
                if (autoPlayCount != null) {
                    gamePlayer.setAutoplay_count(String.valueOf(Integer.parseInt(autoPlayCount) - 1));
                }
                if (autoPlay == null) {
                    autoPlay = "";
                }
                gamePlayer.setAutoplay(autoPlay);
                if (totalCount == null) {
                    totalCount = "";
                }
                gamePlayer.setTotalCount(totalCount);
                player = gamePlayer;
                if (player != null) {
                    setAutoPlayUI(player);
                }
            }
        }
        if (player != null) {
            setAutoPlayUI(player);
        }
    }

    private void updateAutoPlayOnGameEnd() {
        Iterator it = this.mJoinedPlayersList.iterator();
        while (it.hasNext()) {
            String seat = ((RummyGamePlayer) it.next()).getSeat();
            if (seat != null) {
                hideAutoPlayUI(Integer.parseInt(seat));
            }
        }
    }

    private void updateUserOnAutoPlay(RummyEvent event) {
        RummyGamePlayer player = null;
        Iterator it = this.mJoinedPlayersList.iterator();
        while (it.hasNext()) {
            RummyGamePlayer gamePlayer = (RummyGamePlayer) it.next();
            if (gamePlayer.getUser_id().equalsIgnoreCase(String.valueOf(event.getUserId()))) {
                gamePlayer.setAutoplay(event.getStatus());
                gamePlayer.setAutoplay_count("0");
                gamePlayer.setTotalCount("5");
                player = gamePlayer;
                break;
            }
        }
        if (player != null) {
            setAutoPlayUI(player);
        }
    }

    private void showGenericDialogTF(Context context, String message) {
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
                mActivity.getWindow().getDecorView().getSystemUiVisibility());

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    private void showSplitRequestPopUp(String message) {
        showView(this.searchGameView);
        TextView textView = (TextView) this.searchGameView.findViewById(R.id.dialog_msg_tv);
        textView.setText(message);
        textView.setTextSize(14.0f);
        ((ImageView) this.searchGameView.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.searchGameView);
            }
        });
        ((Button) this.searchGameView.findViewById(R.id.yes_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.searchGameView);
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.mDialogLayout);
                RummyTablesFragment.this.requestSplit();
            }
        });
        ((Button) this.searchGameView.findViewById(R.id.no_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.searchGameView);
            }
        });
    }

    public boolean isShowPlayersJoinedMessage() {
        return showPlayersJoinedMessage;
    }


    private void showRejoinTablePopUp(String message, final String megUuid) {
        showView(this.searchGameView);
        TextView textView = (TextView) this.searchGameView.findViewById(R.id.dialog_msg_tv);
        textView.setText(message);
        textView.setTextSize(14.0f);
        ((ImageView) this.searchGameView.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RummyTablesFragment.this.cancelTimer(RummyTablesFragment.this.mRejoinTimer);
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.searchGameView);
                RummyTablesFragment.this.leaveTable();
            }
        });
        ((Button) this.searchGameView.findViewById(R.id.yes_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RummyTablesFragment.this.cancelTimer(RummyTablesFragment.this.mRejoinTimer);
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.searchGameView);
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.mDialogLayout);
                RummyRebuyApiHelper.poolRebuyIn(RummyTablesFragment.this.mTableDetails,result -> {
                    if(result.isSuccess()){
                        RummyApplication.getInstance().setCurrentTableOrderId(result.getResult().optString("order_id",""));
                        RummyTablesFragment.this.sendRejoinRequest(megUuid);
                    }else if(!result.isLoading()){
                        showLongToast(requireContext(),result.getErrorMessage());
                    }
                });
            }
        });
        ((Button) this.searchGameView.findViewById(R.id.no_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RummyTablesFragment.this.cancelTimer(RummyTablesFragment.this.mRejoinTimer);
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.searchGameView);
                RummyTablesFragment.this.leaveTable();
            }
        });
    }

    private void showSplitPopUp(String message, final String megUuid) {
        showView(this.searchGameView);
        TextView textView = (TextView) this.searchGameView.findViewById(R.id.dialog_msg_tv);
        textView.setText(message);
        textView.setTextSize(14.0f);
        /*((ImageView) this.searchGameView.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RummyTablesFragment.this.sendSplitAcceptRequest(megUuid, false);
                RummyTablesFragment.this.cancelTimer(RummyTablesFragment.this.mRejoinTimer);
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.searchGameView);
                RummyTablesFragment.this.removeGameResultFragment();
            }
        });*/
        ((Button) this.searchGameView.findViewById(R.id.yes_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RummyTablesFragment.this.cancelTimer(RummyTablesFragment.this.mRejoinTimer);
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.searchGameView);
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.mDialogLayout);
                RummyTablesFragment.this.removeGameResultFragment();
                RummyTablesFragment.this.sendSplitAcceptRequest(megUuid, true);
            }
        });
        ((Button) this.searchGameView.findViewById(R.id.no_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RummyTablesFragment.this.sendSplitAcceptRequest(megUuid, false);
                RummyTablesFragment.this.removeGameResultFragment();
                RummyTablesFragment.this.cancelTimer(RummyTablesFragment.this.mRejoinTimer);
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.searchGameView);
            }
        });
    }

    private void joinAnotherGame(final RummyEvent event, String message) {
        String winnerName = "";
        String joinAnotherGameMsg = "Do you want to join 1 more game?";
        showView(this.searchGameView);
        TextView textView = (TextView) this.searchGameView.findViewById(R.id.dialog_msg_tv);
        textView.setText(message);
        textView.setTextSize(15.0f);
        ((ImageView) this.searchGameView.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (RummyTablesFragment.this.winnerView.getVisibility() == View.VISIBLE) {
                    RummyTablesFragment.this.invisibleView(RummyTablesFragment.this.searchGameView);
                    return;
                }
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.searchGameView);
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.mDialogLayout);
            }
        });
        ((Button) this.searchGameView.findViewById(R.id.yes_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                RummyTable table = ((RummyTableActivity)mActivity).getTableWithFilter(mTableDetails.getBet(),mTableDetails.getMaxPlayer(),mTableDetails.getTableCost(),mTableDetails.getTableType());
                if(table.getTable_cost() != null && table.getBet() != null)
                {
                    String balance;
                    RummyApplication app = (RummyApplication.getInstance());
                    DecimalFormat format = new DecimalFormat("0.#");
                    if (table.getTable_cost().equalsIgnoreCase("CASH_CASH")) {
                        balance = app.getUserData().getRealChips();
                    } else {
                        balance = app.getUserData().getFunChips();
                    }
                    if (Math.round(Float.parseFloat(balance)) >= Math.round(Float.parseFloat(table.getBet()))) {
                        try {
                            // call from type 1 for rejoin 2 for join new table 3 for rebuin
                            sendJoinTableDataToServer(table,table.getBet(),table.getTable_id(),2,event,"",mTableDetails);
                        }catch (Exception e){
                            RummyTLog.e(TAG+"",e+"");
                        }
                    } else if (table.getTable_cost().contains("CASH_CASH")) {
                        String msg = "";
                        if(table.getTable_type().startsWith(RummyUtils.PR))
                        {
                            msg = "" + mActivity.getResources().getString(R.string.rummy_low_balance_first) + " " + mActivity.getResources().getString(R.string.rupee_symbol) + getRestAmounttoAdd(table.getMinimumbuyin() + "") + " " + mActivity.getResources().getString(R.string.rummy_low_balance_second);
                            showErrorBalanceBuyChips(mActivity,msg,table.getMinimumbuyin());

                        }
                        else
                        {
                            msg = "" + mActivity.getResources().getString(R.string.rummy_low_balance_first) + " " + mActivity.getResources().getString(R.string.rupee_symbol) + getRestAmounttoAdd(table.getBet() + "") + " " + mActivity.getResources().getString(R.string.rummy_low_balance_second);
                            showErrorBalanceBuyChips(mActivity,msg,table.getBet());
                        }
                        /*String msg = "" + mActivity.getResources().getString(R.string.rummy_low_balance_first) + " " + mActivity.getResources().getString(R.string.rupee_symbol) + getRestAmounttoAdd(table.getMinimumbuyin() + "") + " " + mActivity.getResources().getString(R.string.rummy_low_balance_second);
                        showErrorBalanceBuyChips(mActivity,msg,table.getBet());*/
                    }
                    else if(mTableDetails.getTableCost().contains(RummyUtils.GAME_TABLE_COST_STRING_FREE))
                    {
                        showGenericDialog(mActivity,mActivity.getResources().getString(R.string.low_balance_free_chip));
                    }


                }
            }
        });
        ((Button) this.searchGameView.findViewById(R.id.no_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (RummyTablesFragment.this.winnerView.getVisibility() == View.VISIBLE) {
                    RummyTablesFragment.this.invisibleView(RummyTablesFragment.this.searchGameView);
                    return;
                }
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.searchGameView);
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.mDialogLayout);
            }
        });
    }

    private void handleTableCloseEvent() {
        resetTimerInfo(this.mGameResultsView);
        hidePlayerView();
        //disableView(this.mDropPlayer);
        disableView(this.mShowBtn);
        disableView(this.mDeclareBtn);
        disableView(this.sortCards);
        disableView(this.mExtraTimeBtn);
        clearData();
        this.mNoOfGamesPlayed = 0;
        showView(this.mGameShecduleTv);
        this.mGameShecduleTv.setText(this.mActivity.getString(R.string.table_close_msg));
    }

    private void dropUser() {
        if (mDropAndGoPlayer.isChecked() && this.mTableDetails != null && this.mTableDetails.getTableType().equalsIgnoreCase(RummyUtils.PR_JOKER)) ///// implement drop and go
        {
            RummyTablesFragment.this.leaveTable();
        }
        this.canLeaveTable = true;
        dismissQuickMenu();
        invisibleView(this.mUserAutoTimerRootLayout);
        invisibleView(this.mUserAutoChunksLayout);
        hideView(this.mDeclareBtn);
        showView(this.sortCards);
        disableView(this.sortCards);
        disableUserOptions();
        disbaleDeckCards();
        showHideView(false, this.mUserTimerRootLayout, false);
        disbaleDeckCards();
        showDropViewForUser();
        setPlayerUiOnDrop(this.mUserPlayerLayout);
    }

    private void disbaleDeckCards() {
        disableClick(this.mOpenCard);
        disableClick(this.mFaceDownCards);
    }

    private void showDropViewForUser() {
        this.mRummyView.removeViews();
        this.resetAllGroupsCountUI();
    }

    private void enableDeckCards() {
        enableView(this.mOpenCard);
        enableView(this.mFaceDownCards);
    }

    private void setAutoPlayCountOnTurn(RummyEvent event) {
        int userId = event.getUserId();
        String autoPlay = event.getAutoPlay();
        Iterator it;
        RummyGamePlayer player;
        if (autoPlay != null) {
            String autoPlayCount = event.getAutoPlayCount();
            it = this.mJoinedPlayersList.iterator();
            while (it.hasNext()) {
                player = (RummyGamePlayer) it.next();
                if (Integer.parseInt(player.getUser_id()) == userId) {
                    player.setAutoplay(autoPlay);
                    if (autoPlayCount != null) {
                        player.setAutoplay_count(String.valueOf(Integer.parseInt(autoPlayCount) - 1));
                    }
                    RummyGamePlayer gamePlayer = player;
                    return;
                }
            }
            return;
        }
        it = this.mJoinedPlayersList.iterator();
        while (it.hasNext()) {
            player = (RummyGamePlayer) it.next();
            if (Integer.parseInt(player.getUser_id()) == userId) {
                player.setAutoplay("false");
                player.setAutoplay_count(null);
                return;
            }
        }
    }

    private void handleTurnUpdateEvent(RummyEvent event) {
        int userId = event.getUserId();
        String timeOutString = event.getTimeOut();
        updateUserOnTurn(event);
        if (event.getAutoExtraTime() != null) {
            this.autoExtraTime = true;
            this.mAutoExtraTimeEvent = event;
            disableView(this.mExtraTimeBtn);
        }
        if (!timeOutString.equalsIgnoreCase("0")) {
            handleTurnUpdateEvent(userId, timeOutString);
        }
    }

    private void removeWinnerDialog() {
        List<Fragment> fragments = ((FragmentActivity) this.mActivity).getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof DialogFragment) {
                    RummyWinnerDialogFragment winnerDialogFragment = (RummyWinnerDialogFragment) fragment;
                    winnerDialogFragment.getDialog().dismiss();
                    ((FragmentActivity) this.mActivity).getSupportFragmentManager().beginTransaction().remove(winnerDialogFragment);
                    return;
                }
            }
        }
    }

    private void handlePlayerJoinEvent(RummyEvent event) {

        try {
            if (event.getUserId() != Integer.parseInt(this.userData.getUserId())) {
                RummyGamePlayer joinedPlayer;
                this.mGameShecduleTv.setText(R.string.game_start_msg);
                RummySoundPoolManager.getInstance().playSound(R.raw.rummy_sit);
                RummyVibrationManager.getInstance().vibrate(1);
                RummyGamePlayer player = null;
                boolean isPlayerFound = false;
                Iterator it = this.mJoinedPlayersList.iterator();
                while (it.hasNext()) {
                    joinedPlayer = (RummyGamePlayer) it.next();
                    if (joinedPlayer.getUser_id().equalsIgnoreCase(String.valueOf(event.getUserId()))) {
                        player = joinedPlayer;
                        isPlayerFound = true;
                        break;
                    }
                }
                if (player == null) {
                    player = new RummyGamePlayer();
                }
                player.setNick_name(event.getNickName());
                String availableSeat = event.getSeat();
                if (this.mTableDetails != null && Integer.parseInt(this.mTableDetails.getMaxPlayer()) == 6 && this.mTableDetails.getTableType().contains(RummyUtils.PR)) {
                    int index = 1;
                    boolean seatAssigned = false;
                    List<Integer> seatNumbers = getJoinedSeats();
                    for (int j = 0; j < seatNumbers.size(); j++) {
                        if (index != ((Integer) seatNumbers.get(j)).intValue()) {
                            availableSeat = String.valueOf(index);
                            seatAssigned = true;
                            break;
                        }
                        index++;
                    }
                    if (!seatAssigned) {
                        availableSeat = String.valueOf(seatNumbers.size() + 1);
                    }
                } else if (Integer.parseInt(this.mTableDetails.getMaxPlayer()) == 2 && this.mTableDetails.getTableType().contains(RummyUtils.PR)) {
                    int userSeat = 0;
                    it = this.mJoinedPlayersList.iterator();
                    while (it.hasNext()) {
                        joinedPlayer = (RummyGamePlayer) it.next();
                        if (joinedPlayer.getUser_id().equalsIgnoreCase(this.userData.getUserId())) {
                            userSeat = Integer.parseInt(joinedPlayer.getSeat());
                            break;
                        }
                    }
                    if (userSeat == 1) {
                        availableSeat = "4";
                    } else if (userSeat == 4) {
                        availableSeat = "1";
                    }
                }
                player.setSeat(availableSeat);
                String joinedAs = event.getTableJoinAs();
                if (joinedAs != null && joinedAs.equalsIgnoreCase("middle")) {
                    player.setMiddleJoin(true);
                }
                player.setPoints(event.getBuyinamount());
                player.setDEVICE_ID(event.getDeviceId());
                player.setUser_id(String.valueOf(event.getUserId()));
                player.setPlayerlevel(event.getPlayerLevel());
                if (!isPlayerFound) {
                    this.mJoinedPlayersList.add(player);
                    this.mGamePlayerMap.put(player.getUser_id(), player);

                    RummyTLog.e(TAG, "SEATING VIA: handlePlayerJoinEvent");
                    setUpPlayerUI(player, true);
                    if (this.mTableDetails != null) {
                        setPlayerPositionsOnTableBtn(this.mTableDetails, player, false);
                    }
                }//12345
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e + "");
        }

    }

    public RummyTableDetails getTableInfo() {
        return this.mTableDetails;
    }

    @NonNull
    private List<Integer> getJoinedSeats() {
        List<Integer> seatNumbers = new ArrayList();
        Iterator it = this.mJoinedPlayersList.iterator();
        while (it.hasNext()) {
            seatNumbers.add(Integer.valueOf(Integer.parseInt(((RummyGamePlayer) it.next()).getSeat())));
        }
        Collections.sort(seatNumbers);
        return seatNumbers;
    }

    private void handlePlayerQuitEvent(RummyEvent event) {
        if (event.getUserId() != Integer.parseInt(this.userData.getUserId())) {
            handlePlayerQuit(event.getUserId());
        } else {
            this.canLeaveTable = true;
            this.mGroupList.clear();
            clearOtherPlayersData();
            clearData();
            this.mGameShecduleTv.setText(String.format("%s %s", new Object[]{event.getNickName(), "left the table"}));
            this.mGameShecduleTv.setVisibility(View.VISIBLE);
        }
    }

    private void handleGameScheduleEvent(RummyEvent event) {
        disableView(this.mShowBtn);
        //disableView(this.mDropPlayer);
        disableView(this.sortCards);
        clearData();
        this.userPlacedShow = false;
        this.mClosedCard.setVisibility(View.INVISIBLE);
        enableDeckCards();
        int gameStartTime = (int) (Double.valueOf(event.getStartTime()).doubleValue() - Double.valueOf(event.getTimestamp()).doubleValue());
        this.mGameShecduleTv.setVisibility(View.VISIBLE);
        this.mGameShecduleTv.setText("Your game starts in " + gameStartTime + " seconds.");
        startGameScheduleTimer(gameStartTime, false);

    }

    private void handleTurnExtraTimeEvent(RummyEvent event) {
        this.mGameShecduleTv.setVisibility(View.INVISIBLE);
        String userName = event.getNickName();
        this.mGameShecduleTv.setText(String.format("%s%s%s", new Object[]{"Player ", userName, " chosen extra time"}));
        this.mGameShecduleTv.setVisibility(View.VISIBLE);
        int timerValue = Integer.parseInt(RummyUtils.formatString(event.getTimeOut()));
        hideTimerUI();
        handleTurnUpdateTimer(event.getUserId(), timerValue);
    }

    private void handleSendDealEvent(RummyEvent event) {
        setUserOptions(true);
        enableView(this.sortCards);
        this.mGroupList.clear();
        this.cardStack = new ArrayList();
        this.cardStack.addAll(event.getPlayingCards());
        this.mGroupList.add(this.cardStack);
        setGroupView(false);
    }

    private void handleStackEvent(RummyEvent event) {
        setJokerCard(event);
        this.faceDownCardList.clear();
        this.faceUpCardList.clear();
        this.faceDownCardList.addAll(event.getFaceDownStack());
        this.mFaceDownCards.setVisibility(View.VISIBLE);
        this.faceUpCardList.addAll(event.getFaceUpStack());
        if (this.faceUpCardList.size() > 0) {
            setOpenCard((RummyPlayingCard) this.faceUpCardList.get(this.faceUpCardList.size() - 1));
        }
    }

    private void handleCardPickEvent(RummyEvent event) {
        boolean isUser = false;
        try {
            boolean faceDown;
            if (event.getGameStack().equalsIgnoreCase("face_down")) {
                faceDown = true;
            } else {
                faceDown = false;
            }
            RummyGamePlayer player = null;
            Iterator it = this.mJoinedPlayersList.iterator();
            while (it.hasNext()) {
                RummyGamePlayer gamePlayer = (RummyGamePlayer) it.next();
                if (gamePlayer.getUser_id().equalsIgnoreCase(String.valueOf(event.getUserId()))) {
                    player = gamePlayer;
                    break;
                }
            }
            if (!(player == null || player.getSeat() == null)) {
                switch (Integer.parseInt(player.getSeat())) {
                    case 1:
                        isUser = true;
                        animatePickCaCard(0, this.mUserPlayerLayout, faceDown, true);
                        if (event.getAutoPlay().equalsIgnoreCase("true")) {
                            if (!event.getGameStack().equalsIgnoreCase("face_down")) {
                                if (event.getGameStack().equalsIgnoreCase("face_up") && this.faceUpCardList != null && this.faceUpCardList.size() > 0) {
                                    addCardToStack((RummyPlayingCard) this.faceUpCardList.get(this.faceUpCardList.size() - 1));
                                    this.faceUpCardList.remove(this.faceUpCardList.size() - 1);
                                    if (this.faceUpCardList.size() <= 0) {
                                        this.mOpenCard.setVisibility(View.INVISIBLE);
                                        break;
                                    } else {
                                        setOpenCard((RummyPlayingCard) this.faceUpCardList.get(this.faceUpCardList.size() - 1));
                                        break;
                                    }
                                }
                            }
                            clearSelectedCards();
                            if (this.faceDownCardList != null && this.faceDownCardList.size() > 0) {
                                addCardToStack((RummyPlayingCard) this.faceDownCardList.get(this.faceDownCardList.size() - 1));
                                this.faceDownCardList.remove(this.faceDownCardList.size() - 1);
                                break;
                            }
                        }
                        break;
                    case 2:
                        isUser = false;
                        animatePickCaCard(0, this.mSecondPlayerLayout, faceDown, false);
                        break;
                    case 3:
                        isUser = false;
                        animatePickCaCard(0, this.mThirdPlayerLayout, faceDown, false);
                        break;
                    case 4:
                        isUser = false;
                        animatePickCaCard(0, this.mFourthPlayerLayout, faceDown, false);
                        break;
                    case 5:
                        isUser = false;
                        animatePickCaCard(0, this.mFifthPlayerLayout, faceDown, false);
                        break;
                    case 6:
                        isUser = false;
                        animatePickCaCard(0, this.mSixthPlayerLayout, faceDown, false);
                        break;
                }
            }
            if (!isUser) {
                updatedDeckCards(faceDown);
            }
        } catch (Exception e) {
            RummyTLog.e(TAG, "Exception in handleCardPickEvent() : " + e.getMessage());
        }
    }

    private void updatedDeckCards(boolean faceDown) {
        if (faceDown) {
            if (this.faceDownCardList != null && this.faceDownCardList.size() > 0) {
                this.faceDownCardList.remove(this.faceDownCardList.size() - 1);
            }
        } else if (this.faceUpCardList != null && this.faceUpCardList.size() > 0) {
            this.faceUpCardList.remove(this.faceUpCardList.size() - 1);
            if (this.faceUpCardList.size() > 0) {
                setOpenCard((RummyPlayingCard) this.faceUpCardList.get(this.faceUpCardList.size() - 1));
            } else {
                this.mOpenCard.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void hideAutoPlayUI(int seat) {
        switch (seat) {
            case 2:
                //hideAutoPlayView(this.mSecondPlayerLayout);
                hideView(this.player_2_autoplay_box);
                return;
            case 3:
                //hideAutoPlayView(this.mThirdPlayerLayout);
                hideView(this.player_3_autoplay_box);
                return;
            case 4:
                //hideAutoPlayView(this.mFourthPlayerLayout);
                hideView(this.player_4_autoplay_box);
                return;
            case 5:
                //hideAutoPlayView(this.mFifthPlayerLayout);
                hideView(this.player_5_autoplay_box);
                return;
            case 6:
                //hideAutoPlayView(this.mSixthPlayerLayout);
                hideView(this.player_6_autoplay_box);
                return;
            default:
                return;
        }
    }

    private void setAutoPlayUI(RummyGamePlayer player) {
        switch (Integer.parseInt(player.getSeat())) {
            case 2:
                //setAutoPlayView(this.mSecondPlayerLayout, rummy_player);
                setAutoPlayUser(this.player_2_autoplay_box, autoplay_count_player_2, player);
                return;
            case 3:
                //setAutoPlayView(this.mThirdPlayerLayout, rummy_player);
                setAutoPlayUser(this.player_3_autoplay_box, autoplay_count_player_3, player);
                return;
            case 4:
                //setAutoPlayView(this.mFourthPlayerLayout, rummy_player);
                setAutoPlayUser(this.player_4_autoplay_box, autoplay_count_player_4, player);
                return;
            case 5:
                //setAutoPlayView(this.mFifthPlayerLayout, rummy_player);
                setAutoPlayUser(this.player_5_autoplay_box, autoplay_count_player_5, player);
                return;
            case 6:
                //setAutoPlayView(this.mSixthPlayerLayout, rummy_player);
                setAutoPlayUser(this.player_6_autoplay_box, autoplay_count_player_6, player);
                return;
            default:
                return;
        }
    }

    private void setAutoPlayUser(LinearLayout playerBox, TextView count_tv, RummyGamePlayer gamePlayer) {
        String autoPlay = gamePlayer.getAutoplay();
        String autoPlayCount = gamePlayer.getAutoplay_count();
        String totalCount = gamePlayer.getTotalCount();
        if (autoPlay == null) {
            hideView(playerBox);
        } else if (autoPlay.equalsIgnoreCase("True")) {
            showView(playerBox);
            if (autoPlayCount != null) {
                if(autoPlayCount.contains("-")){
                    count_tv.setText(  "0/" + totalCount);
                }else {
                    count_tv.setText(autoPlayCount + "/" + totalCount);
                }
            }
        } else {
            hideView(playerBox);
        }
    }

    private void handleGameResultsEvent(RummyEvent event) {
        hideView(this.mSubFragment);
        dismissDialog(this.showDialog);
        dismissDialog(this.dropDialog);
        dismissDialog(this.mLeaveDialog);
        this.mNoOfGamesPlayed++;
        String userScore = "0";
        Iterator it = ((ArrayList) event.getPlayer()).iterator();
        while (it.hasNext()) {
            RummyGamePlayer player = (RummyGamePlayer) it.next();
            setPointsUI(Integer.parseInt(player.getUser_id()), player);
            if (this.userData.getUserId().equalsIgnoreCase(player.getUser_id())) {
                userScore = player.getTotalScore();
                if (this.mTableDetails != null && this.mTableDetails.getTableType().equalsIgnoreCase(RummyUtils.PR_JOKER)
                        && !this.mLastGamePlayer.isChecked()
                        && !this.mDropAndGoPlayer.isChecked()) {
                    if (this.mTableDetails.getTableCost().equalsIgnoreCase("CASH_CASH")) {
                        if (Float.parseFloat(this.mTableDetails.getMinimumbuyin())*2 > Float.parseFloat(player.getTotalScore())) {
                            if(!mDropAndGoPlayer.isChecked()){
                                showRebuyInPopTimer(this.tableId);
                            }

                        }
                    }
                }
            }
        }
        
        launchGameResultsFragment(event);
        String tableType = event.getTableType();
        if (tableType.contains("101_POOL")) {
            if (Integer.parseInt(RummyUtils.formatString(userScore)) >= 101) {
                hideGameResultsTimer();
            //    showMaxPointsPopUp(event);
            }
        } else if (tableType.contains("201_POOL") && Integer.parseInt(RummyUtils.formatString(userScore)) >= 201) {
            hideGameResultsTimer();
           // showMaxPointsPopUp(event);
        }
        clearData();
    }

    private void hideGameResultsTimer() {
        hideView(getGameResultsMessageView());
    }

    private void showMaxPointsPopUp(RummyEvent event) {
        hideWinnerView(this.mDialogLayout, this.winnerView, this.searchGameView, this.splitRejectedView);
        joinAnotherGame(event, "You have reached maximum number of points,Would you like to play another game?");

    }

    private void handleShowEvent(RummyEvent event) {
        Log.d(TAG, "Inside handleShowEvent************************************");
        this.isYourTurn = false;
        this.isCardsDistributing = false;
        this.canLeaveTable = false;
        disbaleDeckCards();
        if (!(event.getSuit() == null || event.getFace() == null)) {
            RummyPlayingCard card = new RummyPlayingCard();
            card.setFace(event.getFace());
            card.setSuit(event.getSuit());
            setDiscardCard(card);
        }
        cancelTimer(this.playerTurnOutTimer);
        hideTimerUI();
        if (event.getUserId() != Integer.parseInt(this.userData.getUserId())) {
            RummyUtils.FLAG_OPPOSITE_USER = false;
            RummySoundPoolManager.getInstance().playSound(R.raw.rummy_meld);
            this.mClosedCard.setVisibility(View.VISIBLE);
            this.mGameShecduleTv.setVisibility(View.VISIBLE);
            String userName = event.getNickName();
            Log.e("show-user", userName + "");
            disableView(this.mShowBtn);
            disableDropButton(this.mDropPlayer);
            hideView(this.mDeclareBtn);
            String showMessage = this.mActivity.getString(R.string.show_message);
            String message = String.format("%s%s", new Object[]{userName, showMessage + " "});
            this.mGameShecduleTv.setText(message);
            startMeldTimer(Integer.parseInt(RummyUtils.formatString(event.getMeldTimeOut())), message, this.mGameShecduleTv);
            return;
        } else {
            String userName = event.getNickName();
            Log.e("show-user", userName + "");
        }
        this.isPlacedShow = true;
        showView(this.mDeclareBtn);
        enableView(this.mDeclareBtn);
        disableDropButton(this.mDropPlayer);
        removeGameResultFragment();
        startMelding();
        startMeldTimer(Integer.parseInt(RummyUtils.formatString(event.getMeldTimeOut())), this.mActivity.getString(R.string.send_your_cards_msg), this.mGameShecduleTv);
    }

    private void handleSittingSeqEvent(RummyEvent event) {
        showHideView(true, this.mGameShecduleTv, false);
        this.mGameShecduleTv.setText("Re-arranging seats");
        this.mJoinedPlayersList.clear();
        hideTossCardsView();
        List<RummyGamePlayer> gamePlayersList = event.getPlayer();
        ((RummyTableActivity) this.mActivity).resetPlayerIconsOnTableBtn(this.tableId);
        setSeating(gamePlayersList);
    }

    private void handleTossEvent(RummyEvent event) {
        RummySoundPoolManager.getInstance().playSound(R.raw.rummy_toss);
        RummyVibrationManager.getInstance().vibrate(1);
        hideTossCardsView();
        showHideView(true, this.mGameShecduleTv, false);
        String tossWinner = event.getTossWinner();
        if (tossWinner.equalsIgnoreCase(this.userData.getNickName())) {
            tossWinner = "You";
        }
        this.mGameShecduleTv.setText(String.format("%s %s", new Object[]{tossWinner, this.mActivity.getString(R.string.won_toss_msg)}));
        List<RummyGamePlayer> playerList = event.getPlayers();
        HashMap<String, Integer> cardMap = new HashMap();
        RummyCardDiscard player = new RummyCardDiscard();
        for (int i = 0; i < playerList.size(); i++) {
            String name = playerList.get(i).getSuit() + playerList.get(i).getFace();
            int resourceId = this.getResources().getIdentifier(name, "drawable", this.mActivity.getPackageName());
            cardMap.put(((RummyGamePlayer) playerList.get(i)).getUser_id(), resourceId);
        }
        for (Entry pair : cardMap.entrySet()) {
            setTossCards(pair);
        }
    }

    private void handleTurnUpdateEvent(int userId, String timerValueStr) {
        int timerValue = Integer.parseInt(RummyUtils.formatString(timerValueStr));
        disableUserOptions();
        Iterator it = this.mJoinedPlayersList.iterator();
        while (it.hasNext()) {
            RummyGamePlayer player = (RummyGamePlayer) it.next();
            if (player.getUser_id().equalsIgnoreCase(this.userData.getUserId())) {
                if (player.isDropped() || getTotalCards() <= 0) {
                    showView(this.sortCards);
                    disableView(this.sortCards);
                } else {
                    showView(this.sortCards);
                    enableView(this.sortCards);
                }
                hideTimerUI();
                handleTurnUpdateTimer(userId, timerValue);
            }
        }
        hideTimerUI();
        handleTurnUpdateTimer(userId, timerValue);
    }

    private void disableUserOptions() {
        showHideView(true, this.mDropPlayer, false);
        disableDropButton(this.mDropPlayer);
        //showHideView(true, this.mExtraTimeBtn, false);
        disableView(this.mExtraTimeBtn);
        showHideView(true, this.mShowBtn, false);
        disableView(this.mShowBtn);
    }

    private void setPointsUI(int userId, RummyGamePlayer player) {
        Iterator it = this.mJoinedPlayersList.iterator();
        while (it.hasNext()) {
            RummyGamePlayer gamePlayer = (RummyGamePlayer) it.next();
            if (gamePlayer.getUser_id().equalsIgnoreCase(String.valueOf(userId))) {
                player.setSeat(gamePlayer.getSeat());
                break;
            }
        }
        if (player != null && player.getSeat() != null) {
            switch (Integer.parseInt(player.getSeat())) {
                case 1:
                    setPlayerPoints(this.mUserPlayerLayout, player);
                    return;
                case 2:
                    setPlayerPoints(this.mSecondPlayerLayout, player);
                    return;
                case 3:
                    setPlayerPoints(this.mThirdPlayerLayout, player);
                    return;
                case 4:
                    setPlayerPoints(this.mFourthPlayerLayout, player);
                    return;
                case 5:
                    setPlayerPoints(this.mFifthPlayerLayout, player);
                    return;
                case 6:
                    setPlayerPoints(this.mSixthPlayerLayout, player);
                    return;
                default:
                    return;
            }
        }
    }

    private void setTossCards(Entry pair) {
        String userId = pair.getKey().toString();
        RummyGamePlayer player = null;
        Iterator it = this.mJoinedPlayersList.iterator();
        while (it.hasNext()) {
            RummyGamePlayer gamePlayer = (RummyGamePlayer) it.next();
            if (gamePlayer.getUser_id().equalsIgnoreCase(String.valueOf(userId))) {
                player = gamePlayer;
                break;
            }
        }
        int imageResource = Integer.parseInt(pair.getValue().toString());
        if (player != null && player.getSeat() != null) {
            Log.d("toss", player.getNick_name() + " : " + player.getSeat());
            switch (Integer.parseInt(player.getSeat())) {
                case 1:
                    this.mUserTossCard.setImageResource(imageResource);
                    this.mUserTossCard.setVisibility(View.VISIBLE);
                    return;
                case 2:
                    this.mPlayer2TossCard.setImageResource(imageResource);
                    this.mPlayer2TossCard.setVisibility(View.VISIBLE);
                    return;
                case 3:
                    this.mPlayer3TossCard.setImageResource(imageResource);
                    this.mPlayer3TossCard.setVisibility(View.VISIBLE);
                    return;
                case 4:
                    this.mPlayer4TossCard.setImageResource(imageResource);
                    this.mPlayer4TossCard.setVisibility(View.VISIBLE);
                    return;
                case 5:
                    this.mPlayer5TossCard.setImageResource(imageResource);
                    this.mPlayer5TossCard.setVisibility(View.VISIBLE);
                    return;
                case 6:
                    this.mPlayer6TossCard.setImageResource(imageResource);
                    this.mPlayer6TossCard.setVisibility(View.VISIBLE);
                    return;
                default:
                    return;
            }
        }
    }

    private void handlePlayerQuit(int userId) {
        RummyGamePlayer player = null;
        Iterator it = this.mJoinedPlayersList.iterator();
        while (it.hasNext()) {
            RummyGamePlayer gamePlayer = (RummyGamePlayer) it.next();
            if (gamePlayer.getUser_id().equalsIgnoreCase(String.valueOf(userId))) {
                player = gamePlayer;
                player.setLeft(true);
                break;
            }
        }
        if (player != null && player.getSeat() != null) {
            if (this.mTableDetails != null) {
                setPlayerPositionsOnTableBtn(this.mTableDetails, player, true);
            }
            switch (Integer.parseInt(player.getSeat())) {
                case 1:
                    resetPlayerData(this.mUserPlayerLayout);
                    this.mJoinedPlayersList.remove(player);
                    return;
                case 2:
                    resetPlayerData(this.mSecondPlayerLayout);
                    invisibleView(this.player_2_autoplay_box);
                    invisibleView(this.mPlayer2Cards);
                    this.mJoinedPlayersList.remove(player);
                    return;
                case 3:
                    resetPlayerData(this.mThirdPlayerLayout);
                    invisibleView(this.player_3_autoplay_box);
                    invisibleView(this.mPlayer3Cards);
                    this.mJoinedPlayersList.remove(player);
                    return;
                case 4:
                    resetPlayerData(this.mFourthPlayerLayout);
                    invisibleView(this.player_4_autoplay_box);
                    invisibleView(this.mPlayer4Cards);
                    this.mJoinedPlayersList.remove(player);
                    return;
                case 5:
                    resetPlayerData(this.mFifthPlayerLayout);
                    invisibleView(this.player_5_autoplay_box);
                    invisibleView(this.mPlayer5Cards);
                    this.mJoinedPlayersList.remove(player);
                    return;
                case 6:
                    resetPlayerData(this.mSixthPlayerLayout);
                    invisibleView(this.player_6_autoplay_box);
                    invisibleView(this.mPlayer6Cards);
                    this.mJoinedPlayersList.remove(player);
                    return;
                default:
                    return;
            }
        }
    }

    private void setPlayerUiOnDrop(View view) {
        // ((RelativeLayout) view.findViewById(R.id.player_details_root_layout)).setAlpha(CorouselView.SMALL_SCALE);
        showDropImage(view);
        RelativeLayout autoPlayLayout = (RelativeLayout) view.findViewById(R.id.auto_play_layout);
        if (autoPlayLayout != null) {
            invisibleView(autoPlayLayout);
        }
    }

    private void resetPlayerUi(View view) {
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.player_details_root_layout);
        float playerAlpha = relativeLayout.getAlpha();
        if (playerAlpha == 1.0f || playerAlpha == RummyCorouselView.SMALL_SCALE) {
            relativeLayout.setAlpha(1.0f);
        } else {
            //relativeLayout.setAlpha(FancyCoverFlow.SCALEDOWN_GRAVITY_CENTER);
        }
        hideDropPayerImage(view);
        setOpponentCardToView();
        ((TextView) view.findViewById(R.id.player_name_tv)).setAlpha(1f);
       // ((TextView) view.findViewById(R.id.player_points_tv)).setAlpha(1f);


       // ((ImageView) view.findViewById(R.id.iv_avtar)).setImageDrawable(null);

        (view.findViewById(R.id.player_rating_bar)).setAlpha(1f);
        (view.findViewById(R.id.player_system_iv)).setAlpha(1f);
        (view.findViewById(R.id.player_rank_tv)).setAlpha(1f);
    }

    private void setOpponentCardToView() {
        mPlayer2Cards.setImageResource(R.drawable.rummy_opponent_cards);
        mPlayer3Cards.setImageResource(R.drawable.rummy_opponent_cards);
        mPlayer4Cards.setImageResource(R.drawable.rummy_opponent_cards);
        mPlayer5Cards.setImageResource(R.drawable.rummy_opponent_cards);
        mPlayer6Cards.setImageResource(R.drawable.rummy_opponent_cards);
    }

    private void handlePlayerDrop(int userId) {
        if (userId == Integer.parseInt(this.userData.getUserId())) {
            this.isUserDropped = true;
        }
        RummyGamePlayer player = null;
        Iterator it = this.mJoinedPlayersList.iterator();
        while (it.hasNext()) {
            RummyGamePlayer gamePlayer = (RummyGamePlayer) it.next();
            if (gamePlayer.getUser_id().equalsIgnoreCase(String.valueOf(userId))) {
                player = gamePlayer;
                player.setDropped(true);
                break;
            }
        }
        if (player != null && player.getSeat() != null) {
            switch (Integer.parseInt(player.getSeat())) {
                case 1:
                    dropUser();
                    break;
                case 2:
                    setPlayerUiOnDrop(this.mSecondPlayerLayout);
                    invisibleView(this.player_2_autoplay_box);
                    invisibleView(this.mSecondPlayerTimerLayout);
                    invisibleView(this.mSecondPlayerAutoTimerLayout);
                    invisibleView(this.mSecondPlayerAutoChunksLayout);
                    mPlayer2Cards.setImageResource(R.drawable.rummy_drop_card_small);
                    break;
                case 3:
                    setPlayerUiOnDrop(this.mThirdPlayerLayout);
                    invisibleView(this.player_3_autoplay_box);
                    invisibleView(this.mThirdPlayerTimerLayout);
                    invisibleView(this.mThirdPlayerAutoTimerLayout);
                    invisibleView(this.mThirdPlayerAutoChunksLayout);
                    mPlayer3Cards.setImageResource(R.drawable.rummy_drop_card_small);
                    break;
                case 4:
                    setPlayerUiOnDrop(this.mFourthPlayerLayout);
                    invisibleView(this.player_4_autoplay_box);
                    invisibleView(this.mFourthPlayerTimerLayout);
                    invisibleView(this.mFourthPlayerAutoTimerLayout);
                    invisibleView(this.mFourthPlayerAutoChunksLayout);
                    mPlayer4Cards.setImageResource(R.drawable.rummy_drop_card_small);
                    break;
                case 5:
                    setPlayerUiOnDrop(this.mFifthPlayerLayout);
                    invisibleView(this.player_5_autoplay_box);
                    invisibleView(this.mFifthPlayerTimerLayout);
                    invisibleView(this.mFifthPlayerAutoTimerLayout);
                    invisibleView(this.mFifthPlayerAutoChunksLayout);
                    mPlayer5Cards.setImageResource(R.drawable.rummy_drop_card_small);
                    break;
                case 6:
                    setPlayerUiOnDrop(this.mSixthPlayerLayout);
                    invisibleView(this.player_6_autoplay_box);
                    invisibleView(this.mSixthPlayerTimerLayout);
                    invisibleView(this.mSixthPlayerAutoTimerLayout);
                    invisibleView(this.mSixthPlayerAutoChunksLayout);
                    mPlayer6Cards.setImageResource(R.drawable.rummy_drop_card_small);
                    break;
            }
            if (player.isLeft()) {
                this.mJoinedPlayersList.remove(player);
            }
        }
    }

    private void handleTurnUpdateTimer(int userId, int timerValue) {
        RummyGamePlayer player = null;
        Iterator it = this.mJoinedPlayersList.iterator();
        while (it.hasNext()) {
            RummyGamePlayer gamePlayer = (RummyGamePlayer) it.next();
            if (gamePlayer.getUser_id().equalsIgnoreCase(String.valueOf(userId))) {
                player = gamePlayer;
                break;
            }
        }
        if (player != null) {
            cancelTimer(this.playerTurnOutTimer);
            startPlayerTimer(timerValue, Integer.parseInt(player.getSeat()), player);
        }
    }

    private void startMeldTimer(int timeOut, String message, TextView timerTv) {
        if (this.meldTimer != null) {
            this.meldTimer.cancel();
            this.meldTimer = null;
        }
        TextView textView = timerTv;
        this.meldTimer = new RummyCountDownTimer(this, (long) (RummyUtils.TIMER_INTERVAL * timeOut), (long) RummyUtils.TIMER_INTERVAL, textView, (TextView) this.mMeldCardsView.findViewById(R.id.game_timer), message, this.isPlacedShow);
        this.meldTimer.start();
    }

    public void animateTableButtons() {
        if (this.mApplication.getJoinedTableIds().size() == 2) {
            ((RummyTableActivity) this.mActivity).flashButton(getTag());
        }
    }

    private void hideTimerUI() {
        if (this.playerTurnOutTimer != null) {
            this.playerTurnOutTimer.cancel();
        }
        invisibleView(this.mUserTimerRootLayout);
        invisibleView(this.mSecondPlayerTimerLayout);
        invisibleView(this.mThirdPlayerTimerLayout);
        invisibleView(this.mFourthPlayerTimerLayout);
        invisibleView(this.mFifthPlayerTimerLayout);
        invisibleView(this.mSixthPlayerTimerLayout);
        invisibleView(this.mUserAutoTimerRootLayout);
        invisibleView(this.mSecondPlayerAutoTimerLayout);
        invisibleView(this.mThirdPlayerAutoTimerLayout);
        invisibleView(this.mFourthPlayerAutoTimerLayout);
        invisibleView(this.mFifthPlayerAutoTimerLayout);
        invisibleView(this.mSixthPlayerAutoTimerLayout);
        invisibleView(this.mUserAutoChunksLayout);
        invisibleView(this.mSecondPlayerAutoChunksLayout);
        invisibleView(this.mThirdPlayerAutoChunksLayout);
        invisibleView(this.mFourthPlayerAutoChunksLayout);
        invisibleView(this.mFifthPlayerAutoChunksLayout);
        invisibleView(this.mSixthPlayerAutoChunksLayout);

        invisibleView(this.circularProgressBar_player1);
        invisibleView(this.circularProgressBar_player2);
        invisibleView(this.circularProgressBar_player3);
        invisibleView(this.circularProgressBar_player4);
        invisibleView(this.circularProgressBar_player5);
        invisibleView(this.circularProgressBar_player6);

        invisibleView(this.small_round_box_player_1);
        invisibleView(this.small_round_box_player_2);
        invisibleView(this.small_round_box_player_3);
        invisibleView(this.small_round_box_player_4);
        invisibleView(this.small_round_box_player_5);
        invisibleView(this.small_round_box_player_6);


    }

    private void clearStacks() {
        this.faceDownCardList.clear();
        this.faceUpCardList.clear();
    }

    private void refreshStacks(RummyEvent event) {
        this.faceDownCardList.addAll(event.getFaceDownStack());
        this.faceUpCardList.addAll(event.getFaceUpStack());
        ((TextView) this.mReshuffleView.findViewById(R.id.dialog_msg_tv)).setText("Cards reshuffled.");
        showView(this.mReshuffleView);
        ((Button) this.mReshuffleView.findViewById(R.id.ok_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.mReshuffleView);
            }
        });
        ((ImageView) this.mReshuffleView.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RummyTablesFragment.this.hideView(RummyTablesFragment.this.mReshuffleView);
            }
        });
    }

    public void dismissQuickMenu() {
        if (this.mQuickAction != null) {
            this.mQuickAction.dismiss();
        }
    }

    private void animateCard(View view) {
        android.view.animation.Animation animation = AnimationUtils.loadAnimation(mActivity.getApplicationContext(), R.anim.rummy_move_up);
        animation.setDuration(300);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    private void setOpenCard(RummyPlayingCard playingCard) {
        this.mOpenJokerCard.setVisibility(View.GONE);
        int imgId = getResources().getIdentifier(String.format("%s%s", new Object[]{playingCard.getSuit(), playingCard.getFace()}), "drawable", this.mActivity.getPackageName());
        this.mOpenCard.setVisibility(View.VISIBLE);
        this.mOpenCard.setImageResource(imgId);
        if (this.mJockerCard == null) {
            return;
        }
        if (this.mJockerCard.getFace().equalsIgnoreCase(playingCard.getFace())) {
            this.mOpenJokerCard.setVisibility(View.VISIBLE);
        } else if (playingCard == null || !playingCard.getFace().equalsIgnoreCase("1")) {
            this.mOpenJokerCard.setVisibility(View.GONE);
        } else if (this.mJockerCard.getFace().equalsIgnoreCase("0")) {
            this.mOpenJokerCard.setVisibility(View.VISIBLE);
        } else {
            this.mOpenJokerCard.setVisibility(View.GONE);
        }
    }

    private void setJokerCard(RummyEvent event) {
        if (Boolean.valueOf(event.getJocker()).booleanValue()) {
            RummyPlayingCard jokerCard = new RummyPlayingCard();
            jokerCard.setFace(event.getFace());
            jokerCard.setSuit(event.getSuit());
            RummyIamBackFragment iamBackFragment = (RummyIamBackFragment) ((RummyTableActivity) this.mActivity).getFragmentByTag(RummyIamBackFragment.class.getName());
            if (iamBackFragment != null) {
                iamBackFragment.setJokerCard(jokerCard);
            }
            this.mJockerCard = jokerCard;
            int imgId = getResources().getIdentifier(String.format("%s%s", new Object[]{event.getSuit(), event.getFace()}), "drawable", this.mActivity.getPackageName());
            this.mJokerCard.setVisibility(View.VISIBLE);
            this.mJokerCard.setImageResource(imgId);
        }
    }

    public TextView getGameResultsMessageView() {
        return (TextView) this.mGameResultsView.findViewById(R.id.game_timer);
    }

    private void startGameScheduleTimer(int scheduleTime, boolean isMelding) {
        try {
            setAutoDropSetting(false);
            cancelTimer(this.mGameScheduleTimer);
            final boolean z = isMelding;
            final int i = scheduleTime;
            this.mGameScheduleTimer = new CountDownTimer((long) (scheduleTime * 1000), 1000) {
                public void onTick(long millisUntilFinished) {
                    long timeFinished = millisUntilFinished / 1000;
                    if (!z) {
                        if (timeFinished <= 10) {
                            RummyTablesFragment.this.canLeaveTable = false;
                            RummyTablesFragment.this.dismissDialog(RummyTablesFragment.this.mLeaveDialog);
                        }
                        RummyTablesFragment.this.mGameShecduleTv.setText(RummyTablesFragment.this.mActivity != null ? RummyTablesFragment.this.mActivity.getString(R.string.game_starts_msg) + " " + timeFinished + " " + RummyTablesFragment.this.mActivity.getString(R.string.seconds_txt) : "");
                    }
                    if (z) {
                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mGameShecduleTv);
                        RummyTablesFragment.this.canLeaveTable = false;
                        RummyTablesFragment.this.isUserPlacedValidShow = true;
                        RummyTablesFragment.this.mGameShecduleTv.setText("Please wait while we check your opponent cards in " + timeFinished + " " + RummyTablesFragment.this.mActivity.getString(R.string.seconds_txt));
                        RummyTablesFragment.this.setGameResultsTimer(RummyTablesFragment.this.mGameResultsView, RummyTablesFragment.this.mActivity != null ? "Please wait while we check your opponent cards in " + timeFinished + " " + RummyTablesFragment.this.mActivity.getString(R.string.seconds_txt) : "");
                    }
                    if (RummyTablesFragment.this.isGameResultsShowing && !z) {
                        if (timeFinished == ((long) (i / 2))) {
                            RummyTablesFragment.this.removeGameResultFragment();
                        } else {
                            RummyTablesFragment.this.setGameResultsTimer(RummyTablesFragment.this.mGameResultsView, RummyTablesFragment.this.mActivity != null ? RummyTablesFragment.this.mActivity.getString(R.string.game_result_game_starts_msg) + " " + timeFinished + " " + RummyTablesFragment.this.mActivity.getString(R.string.seconds_txt) : "");
                        }
                    }
                    if (RummyTablesFragment.this.mMeldCardsView.getVisibility() == View.VISIBLE) {
                        ((TextView) RummyTablesFragment.this.mMeldCardsView.findViewById(R.id.game_timer)).setText(String.format("%s%s%s", new Object[]{RummyTablesFragment.this.mActivity.getString(R.string.game_result_game_starts_msg) + " ", String.valueOf(timeFinished), " " + RummyTablesFragment.this.mActivity.getString(R.string.seconds_txt)}));
                    }
                    RummyTablesFragment.this.setTableButtonsUI();
                }

                public void onFinish() {
                    if (z || RummyTablesFragment.this.mNoOfGamesPlayed > 0) {
                        RummyTablesFragment.this.mGameShecduleTv.setVisibility(View.INVISIBLE);
                    } else {
                        RummyTablesFragment.this.isTossEventRunning = true;
                        RummyTablesFragment.this.mGameShecduleTv.setText(R.string.toss_info_msg);
                    }
                    if (RummyTablesFragment.this.isGameResultsShowing && !z) {
                        RummyTablesFragment.this.removeGameResultFragment();
                    }
                    RummyTablesFragment.this.setGameResultsTimer(RummyTablesFragment.this.mGameResultsView, "");
                }
            }.start();
        } catch (Exception e) {
            RummyTLog.e(TAG, "Exception in startGameScheduleTimer :: " + e.getMessage());
        }
    }

    private void startWrongMeldTimer(int scheduleTime, String message) {
        try {
            hideView(this.mGameShecduleTv);
            final TextView timerTv = (TextView) this.mSmartCorrectionView.findViewById(R.id.sc_game_timer);
            cancelTimer(this.mWrongMeldTimer);
            final String str = message;
            this.mWrongMeldTimer = new CountDownTimer((long) (scheduleTime * 1000), 1000) {
                public void onTick(long millisUntilFinished) {
                    RummyTablesFragment.this.hideView(RummyTablesFragment.this.mGameShecduleTv);
                    RummyTablesFragment.this.showView(RummyTablesFragment.this.mWrongMeldTv);
                    long timeFinished = millisUntilFinished / 1000;
                    timerTv.setText(String.format("%s%s%s", new Object[]{str, Long.valueOf(millisUntilFinished / 1000), " seconds."}));
                }

                public void onFinish() {
                    RummyTablesFragment.this.isSmartCorrectionShowing = false;
                    RummyTablesFragment.this.hideView(RummyTablesFragment.this.mSmartCorrectionView);
                    RummyTablesFragment.this.hideView(RummyTablesFragment.this.mWrongMeldTv);
                }
            }.start();
        } catch (Exception e) {
            RummyTLog.e(TAG, "Exception in startWrongMeldTimer :: " + e.getMessage());
        }
    }

    public boolean isGameResultsShowing() {
        return this.isGameResultsShowing;
    }

    public boolean isMeldScreenIsShowing() {
        return this.isMeldFragmentShowing;
    }

    private void removeGameResultFragment() {
        hideView(this.searchGameView);
        hideView(this.mSubFragment);
        this.isGameResultsShowing = false;
        this.mMeldGroupList.clear();
        setTableButtonsUI();
    }

    private void removeMeldCardsFragment() {
        hideView(this.mSubFragment);
        this.isMeldFragmentShowing = false;
        this.mMeldGroupList.clear();
        setTableButtonsUI();
    }

    private void startPlayerTimer(int scheduleTime, int playerPosition, RummyGamePlayer player) {


        RummyIamBackFragment iamBackFragment = (RummyIamBackFragment) ((RummyTableActivity) this.mActivity).getFragmentByTag(RummyIamBackFragment.class.getName());
        if (iamBackFragment != null) {
            iamBackFragment.enableIamBackButton();
        }
        final int i = playerPosition;

        ////  if turn is not home rummy_player than autodroppalayer button should be rummy_show.

        if (i != 1 && !isUserWaiting && !isUserDropped) {
            if (mTableDetails == null || !(mTableDetails.getTableType().equalsIgnoreCase("BEST_OF_2") || mTableDetails.getTableType().equalsIgnoreCase("BEST_OF_3")))  //vikas change
            {
                showView(this.mDropPlayer);
                enableDropButton(this.mDropPlayer);
                //enableView(mAutoDropPlayer);
                hideView(mAutoDropPlayer);
            }
        } else {
            hideView(mAutoDropPlayer);
        }

        final RummyGamePlayer gamePlayer = player;
        this.playerTurnOutTimer = new CountDownTimer((long) (scheduleTime * 1000), 1000) {
            public void onTick(long millisUntilFinished) {
                int timeRemaining = (int) (millisUntilFinished / 1000);
                switch (i) {
                    case 1:
                        if (RummyTablesFragment.this.userData.getUserId().equalsIgnoreCase(gamePlayer.getUser_id())) {
                            RummyTablesFragment.this.isYourTurn = true;
                            RummyTablesFragment.this.showHideView(true, RummyTablesFragment.this.mDropPlayer, false);
                            int totalCards = RummyTablesFragment.this.getTotalCards();
                            if (totalCards < 14 && !RummyTablesFragment.this.isPlacedShow) {
                                if (RummyUtils.isAppInDebugMode()) {
                                    Log.d(Cust_TAG, "vikas, Total cards= less than 14 calling");
                                }
                                RummyTablesFragment.this.setDropButton();
                            } else if (totalCards == 14) {

                                if (RummyUtils.isAppInDebugMode()) {
                                    Log.d(Cust_TAG, "vikas, Total cards= 14 calling");
                                }
                                /*if(mTableDetails == null || !(mTableDetails.getTableType().equalsIgnoreCase("BEST_OF_2") || mTableDetails.getTableType().equalsIgnoreCase("BEST_OF_3")))  //vikas change
                                {
                                    showView(mAutoDropPlayer);
                                }*/

                                RummyTablesFragment.this.disableDropButton(RummyTablesFragment.this.mDropPlayer);
                            }
                            RummyTablesFragment.this.showHideView(true, RummyTablesFragment.this.mUserTimerRootLayout, false);

                            if (RummyTablesFragment.this.mApplication.getJoinedTableIds().size() == 2) {
                                ((RummyTableActivity) RummyTablesFragment.this.mActivity).flashButton(RummyTablesFragment.this.getTag());
                            }
                            if (RummyTablesFragment.this.mAutoExtraTimeEvent != null && Integer.parseInt(RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraTime()) == 0) {
                                //TablesFragment.this.showView(TablesFragment.this.mExtraTimeBtn);
                                RummyTablesFragment.this.enableView(RummyTablesFragment.this.mExtraTimeBtn);
                            }
                        } else {
                            if (RummyUtils.isAppInDebugMode()) {
                                Log.d(Cust_TAG, "vikas, userid not matched");
                            }
                            RummyTablesFragment.this.setUserOptions(false);
                            RummyTablesFragment.this.showHideView(true, RummyTablesFragment.this.mUserTimerRootLayout, false);
                        }
                        if (RummyTablesFragment.this.autoExtraTime) {
                            RummyTablesFragment.this.showView(RummyTablesFragment.this.mUserAutoTimerRootLayout);
                            if (!(RummyTablesFragment.this.mAutoExtraTimeEvent == null || RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraTime() == null)) {
                                RummyTablesFragment.this.mUserAutoTimerTv.setText(RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraTime());
                                RummyTablesFragment.this.setAutoExtraTimeChunks(RummyTablesFragment.this.mUserAutoChunksLayout, RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraChunks());
                            }
                        }
                        RummyTablesFragment.this.setUserTimer(millisUntilFinished, timeRemaining, RummyTablesFragment.this.mUserTimerRootLayout, RummyTablesFragment.this.mUserTimerTv, RummyTablesFragment.this.circularProgressBar_player1, RummyTablesFragment.this.isYourTurn);


                        RummyTablesFragment.this.small_round_box_player_1.setVisibility(View.VISIBLE);

                        break;
                    case 2:

                        if (RummyUtils.isAppInDebugMode()) {
                            Log.d(Cust_TAG, "vikas, user case 2 calling");
                        }
                        RummyTablesFragment.this.isYourTurn = false;
                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mSecondPlayerTimerLayout);
                        if (RummyTablesFragment.this.autoExtraTime) {
                            RummyTablesFragment.this.showView(RummyTablesFragment.this.mSecondPlayerAutoTimerLayout);
                            if (!(RummyTablesFragment.this.mAutoExtraTimeEvent == null || RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraTime() == null)) {
                                RummyTablesFragment.this.mSecondPlayerAutoTimerTv.setText(RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraTime());
                                RummyTablesFragment.this.setAutoExtraTimeChunks(RummyTablesFragment.this.mSecondPlayerAutoChunksLayout, RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraChunks());
                            }
                        }
                        RummyTablesFragment.this.setUserTimer(millisUntilFinished, timeRemaining, RummyTablesFragment.this.mSecondPlayerTimerLayout, RummyTablesFragment.this.mSecondPlayerTimerTv, RummyTablesFragment.this.circularProgressBar_player2, RummyTablesFragment.this.isYourTurn);

                        RummyTablesFragment.this.small_round_box_player_2.setVisibility(View.VISIBLE);

                        break;
                    case 3:

                        if (RummyUtils.isAppInDebugMode()) {
                            Log.d(Cust_TAG, "vikas, user case 3 calling");
                        }
                        RummyTablesFragment.this.isYourTurn = false;
                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mThirdPlayerTimerLayout);
                        if (RummyTablesFragment.this.autoExtraTime) {
                            RummyTablesFragment.this.showView(RummyTablesFragment.this.mThirdPlayerAutoTimerLayout);
                            if (!(RummyTablesFragment.this.mAutoExtraTimeEvent == null || RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraTime() == null)) {
                                RummyTablesFragment.this.mThirdPlayerAutoTimerTv.setText(RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraTime());
                                RummyTablesFragment.this.setAutoExtraTimeChunks(RummyTablesFragment.this.mThirdPlayerAutoChunksLayout, RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraChunks());
                            }
                        }
                        RummyTablesFragment.this.setUserTimer(millisUntilFinished, timeRemaining, RummyTablesFragment.this.mThirdPlayerTimerLayout, RummyTablesFragment.this.mThirdPlayerTimerTv, RummyTablesFragment.this.circularProgressBar_player3, RummyTablesFragment.this.isYourTurn);

                        RummyTablesFragment.this.small_round_box_player_3.setVisibility(View.VISIBLE);

                        break;
                    case 4:

                        if (RummyUtils.isAppInDebugMode()) {
                            Log.d(Cust_TAG, "vikas, user case 4 calling");
                        }
                        RummyTablesFragment.this.isYourTurn = false;
                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mFourthPlayerTimerLayout);
                        if (RummyTablesFragment.this.autoExtraTime) {
                            RummyTablesFragment.this.showView(RummyTablesFragment.this.mFourthPlayerAutoTimerLayout);
                            if (!(RummyTablesFragment.this.mAutoExtraTimeEvent == null || RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraTime() == null)) {
                                RummyTablesFragment.this.mFourthPlayerAutoTimerTv.setText(RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraTime());
                                RummyTablesFragment.this.setAutoExtraTimeChunks(RummyTablesFragment.this.mFourthPlayerAutoChunksLayout, RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraChunks());
                            }
                        }
                        RummyTablesFragment.this.setUserTimer(millisUntilFinished, timeRemaining, RummyTablesFragment.this.mFourthPlayerTimerLayout, RummyTablesFragment.this.mFourthPlayerTimerTv, RummyTablesFragment.this.circularProgressBar_player4, RummyTablesFragment.this.isYourTurn);

                        RummyTablesFragment.this.small_round_box_player_4.setVisibility(View.VISIBLE);

                        break;
                    case 5:
                        if (RummyUtils.isAppInDebugMode()) {
                            Log.d(Cust_TAG, "vikas, user case 5 calling");
                        }
                        RummyTablesFragment.this.isYourTurn = false;
                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mFifthPlayerTimerLayout);
                        if (RummyTablesFragment.this.autoExtraTime) {
                            RummyTablesFragment.this.showView(RummyTablesFragment.this.mFifthPlayerAutoTimerLayout);
                            if (!(RummyTablesFragment.this.mAutoExtraTimeEvent == null || RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraTime() == null)) {
                                RummyTablesFragment.this.mFifthPlayerAutoTimerTv.setText(RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraTime());
                                RummyTablesFragment.this.setAutoExtraTimeChunks(RummyTablesFragment.this.mFifthPlayerAutoChunksLayout, RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraChunks());
                            }
                        }
                        RummyTablesFragment.this.setUserTimer(millisUntilFinished, timeRemaining, RummyTablesFragment.this.mFifthPlayerTimerLayout, RummyTablesFragment.this.mFifthPlayerTimerTv, RummyTablesFragment.this.circularProgressBar_player5, RummyTablesFragment.this.isYourTurn);

                        RummyTablesFragment.this.small_round_box_player_5.setVisibility(View.VISIBLE);

                        break;
                    case 6:
                        if (RummyUtils.isAppInDebugMode()) {
                            Log.d(Cust_TAG, "vikas, user case 6 calling");
                        }
                        RummyTablesFragment.this.isYourTurn = false;
                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mSixthPlayerTimerLayout);
                        if (RummyTablesFragment.this.autoExtraTime) {
                            RummyTablesFragment.this.showView(RummyTablesFragment.this.mSixthPlayerAutoTimerLayout);
                            if (!(RummyTablesFragment.this.mAutoExtraTimeEvent == null || RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraTime() == null)) {
                                RummyTablesFragment.this.mSixthPlayerAutoTimerTv.setText(RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraTime());
                                RummyTablesFragment.this.setAutoExtraTimeChunks(RummyTablesFragment.this.mSixthPlayerAutoChunksLayout, RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraChunks());
                            }
                        }
                        RummyTablesFragment.this.setUserTimer(millisUntilFinished, timeRemaining, RummyTablesFragment.this.mSixthPlayerTimerLayout, RummyTablesFragment.this.mSixthPlayerTimerTv, RummyTablesFragment.this.circularProgressBar_player6, RummyTablesFragment.this.isYourTurn);

                        RummyTablesFragment.this.small_round_box_player_6.setVisibility(View.VISIBLE);
                        break;
                }
                if (!isSelfUserJoinInMiddleMatch) {
                    if (RummyTablesFragment.this.mGameShecduleTv.getVisibility() == View.VISIBLE) {
                        RummyTablesFragment.this.messageVisibleCount = RummyTablesFragment.this.messageVisibleCount + 1;
                        if (RummyTablesFragment.this.messageVisibleCount == 5) {
                            RummyTablesFragment.this.invisibleView(RummyTablesFragment.this.mGameShecduleTv);
                            RummyTablesFragment.this.messageVisibleCount = 0;
                        }
                    }
                }
            }

            public void onFinish() {
                if (RummyTablesFragment.this.isYourTurn) {
                    RummyIamBackFragment iamBackFragment = (RummyIamBackFragment) ((RummyTableActivity) RummyTablesFragment.this.mActivity).getFragmentByTag(RummyIamBackFragment.class.getName());
                    if (iamBackFragment != null) {
                        iamBackFragment.disableIamBackButton();
                    }
                }
                RummyTablesFragment.this.isYourTurn = false;
                RummyTablesFragment.this.cancelTimer(RummyTablesFragment.this.playerTurnOutTimer);
                if (RummyTablesFragment.this.autoExtraTime && RummyTablesFragment.this.mAutoExtraTimeEvent != null) {
                    String autoExtraTimeStr = RummyTablesFragment.this.mAutoExtraTimeEvent.getAutoExtraTime();
                    if (!(autoExtraTimeStr == null || autoExtraTimeStr.equalsIgnoreCase("0"))) {
                        RummyTablesFragment.this.startPlayerAutoTimer(Integer.parseInt(autoExtraTimeStr), i, gamePlayer);
                    }
                }
                switch (i) {
                    case 1:
                        RummyTablesFragment.this.mUserTimerTv.setText("0");
                        return;
                    case 2:
                        RummyTablesFragment.this.mSecondPlayerTimerTv.setText("0");
                        return;
                    case 3:
                        RummyTablesFragment.this.mThirdPlayerTimerTv.setText("0");
                        return;
                    case 4:
                        RummyTablesFragment.this.mFourthPlayerTimerTv.setText("0");
                        return;
                    case 5:
                        RummyTablesFragment.this.mFifthPlayerTimerTv.setText("0");
                        return;
                    case 6:
                        RummyTablesFragment.this.mSixthPlayerTimerTv.setText("0");
                        return;
                    default:
                        return;
                }
            }
        };
        this.playerTurnOutTimer.start();
    }

    private void setAutoExtraTimeChunks(View autoChunkLayout, String chunks) {
        showView(autoChunkLayout);
        ImageView chunk1Iv = (ImageView) autoChunkLayout.findViewById(R.id.auto_chunk_1);
        ImageView chunk2Iv = (ImageView) autoChunkLayout.findViewById(R.id.auto_chunk_2);
        ImageView chunk3Iv = (ImageView) autoChunkLayout.findViewById(R.id.auto_chunk_3);
        chunk1Iv.setImageResource(0);
        chunk2Iv.setImageResource(0);
        chunk3Iv.setImageResource(0);
        if (chunks != null && !chunks.isEmpty()) {
            switch (Integer.parseInt(chunks.split("/")[0])) {
                case 1:
                    //chunk1Iv.setImageResource(R.drawable.rummy_chunk1);
                    chunk1Iv.setImageResource(R.drawable.rummy_timer_on);
                    chunk2Iv.setImageResource(R.drawable.rummy_timer_off);
                    chunk3Iv.setImageResource(R.drawable.rummy_timer_off);
                    return;
                case 2:
                 /*   chunk1Iv.setImageResource(R.drawable.rummy_chunk1);
                    chunk2Iv.setImageResource(R.drawable.rummy_chunk2);*/

                    chunk1Iv.setImageResource(R.drawable.rummy_timer_on);
                    chunk2Iv.setImageResource(R.drawable.rummy_timer_on);
                    chunk3Iv.setImageResource(R.drawable.rummy_timer_off);

                    return;
                case 3:
                   /* chunk1Iv.setImageResource(R.drawable.rummy_chunk1);
                    chunk2Iv.setImageResource(R.drawable.rummy_chunk2);
                    chunk3Iv.setImageResource(R.drawable.rummy_chunk3);*/

                    chunk1Iv.setImageResource(R.drawable.rummy_timer_on);
                    chunk2Iv.setImageResource(R.drawable.rummy_timer_on);
                    chunk3Iv.setImageResource(R.drawable.rummy_timer_on);

                    return;
                default:
                    return;
            }
        }
    }

    private void startPlayerAutoTimer(int scheduleTime, int playerPosition, RummyGamePlayer player) {
        RummyIamBackFragment iamBackFragment = (RummyIamBackFragment) ((RummyTableActivity) this.mActivity).getFragmentByTag(RummyIamBackFragment.class.getName());
        if (iamBackFragment != null) {
            iamBackFragment.enableIamBackButton();
        }
        final int i = playerPosition;
        final RummyGamePlayer gamePlayer = player;

        ////  if turn is not home rummy_player than autodroppalayer button should be rummy_show.

        if (i != 1 && !isUserDropped && !isUserWaiting) {
            if (mTableDetails == null || !(mTableDetails.getTableType().equalsIgnoreCase("BEST_OF_2") || mTableDetails.getTableType().equalsIgnoreCase("BEST_OF_3")))  //vikas change
            {
                //enableView(mAutoDropPlayer);
                hideView(mAutoDropPlayer);
            }
        } else {
            hideView(mAutoDropPlayer);
        }
        this.playerTurnOutTimer = new CountDownTimer((long) (scheduleTime * 1000), 1000) {
            public void onTick(long millisUntilFinished) {
                int timeRemaining = (int) (millisUntilFinished / 1000);
                switch (i) {
                    case 1:
                        if (RummyTablesFragment.this.userData.getUserId().equalsIgnoreCase(gamePlayer.getUser_id())) {
                            RummyTablesFragment.this.isYourTurn = true;
                            RummyTablesFragment.this.showHideView(true, RummyTablesFragment.this.mDropPlayer, false);
                            int totalCards = RummyTablesFragment.this.getTotalCards();
                            if (totalCards < 14 && !RummyTablesFragment.this.isPlacedShow) {
                                if (RummyUtils.isAppInDebugMode()) {
                                    Log.d(Cust_TAG, "vikas, Total cards= less than 14 calling");
                                }
                                RummyTablesFragment.this.setDropButton();
                            } else if (totalCards == 14) {
                                if (RummyUtils.isAppInDebugMode()) {
                                    Log.d(Cust_TAG, "vikas, Total cards= 14 calling");
                                }
                              /*  if(mTableDetails == null || !(mTableDetails.getTableType().equalsIgnoreCase("BEST_OF_2") || mTableDetails.getTableType().equalsIgnoreCase("BEST_OF_3")))  //vikas change
                                {
                                    showView(mAutoDropPlayer);
                                }*/

                                RummyTablesFragment.this.disableDropButton(RummyTablesFragment.this.mDropPlayer);
                            }
                            RummyTablesFragment.this.showHideView(true, RummyTablesFragment.this.mUserTimerRootLayout, false);
                            if (RummyTablesFragment.this.mApplication.getJoinedTableIds().size() == 2) {
                                ((RummyTableActivity) RummyTablesFragment.this.mActivity).flashButton(RummyTablesFragment.this.getTag());
                            }
                        } else {
                            RummyTablesFragment.this.setUserOptions(false);
                            RummyTablesFragment.this.showHideView(true, RummyTablesFragment.this.mUserTimerRootLayout, false);
                        }
                        RummyTablesFragment.this.setUserAutoTimer(millisUntilFinished, timeRemaining, RummyTablesFragment.this.mUserAutoTimerRootLayout, RummyTablesFragment.this.isYourTurn);
                        break;
                    case 2:

                        if (RummyUtils.isAppInDebugMode()) {
                            Log.d(Cust_TAG, "vikas, user case 2 calling");
                        }
                        RummyTablesFragment.this.isYourTurn = false;
                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mSecondPlayerTimerLayout);
                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mSecondPlayerAutoTimerLayout);
                        RummyTablesFragment.this.setUserAutoTimer(millisUntilFinished, timeRemaining, RummyTablesFragment.this.mSecondPlayerAutoTimerLayout, RummyTablesFragment.this.isYourTurn);
                        break;
                    case 3:

                        if (RummyUtils.isAppInDebugMode()) {
                            Log.d(Cust_TAG, "vikas, user case 3 calling");
                        }
                        RummyTablesFragment.this.isYourTurn = false;
                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mThirdPlayerTimerLayout);
                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mThirdPlayerAutoTimerLayout);
                        RummyTablesFragment.this.setUserAutoTimer(millisUntilFinished, timeRemaining, RummyTablesFragment.this.mThirdPlayerAutoTimerLayout, RummyTablesFragment.this.isYourTurn);
                        break;
                    case 4:
                        if (RummyUtils.isAppInDebugMode()) {
                            Log.d(Cust_TAG, "vikas, user case 4 calling");
                        }
                        RummyTablesFragment.this.isYourTurn = false;
                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mFourthPlayerTimerLayout);
                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mFourthPlayerAutoTimerLayout);
                        RummyTablesFragment.this.setUserAutoTimer(millisUntilFinished, timeRemaining, RummyTablesFragment.this.mFourthPlayerAutoTimerLayout, RummyTablesFragment.this.isYourTurn);
                        break;
                    case 5:
                        if (RummyUtils.isAppInDebugMode()) {
                            Log.d(Cust_TAG, "vikas, user case 5 calling");
                        }
                        RummyTablesFragment.this.isYourTurn = false;
                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mFifthPlayerTimerLayout);
                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mFifthPlayerAutoTimerLayout);
                        RummyTablesFragment.this.setUserAutoTimer(millisUntilFinished, timeRemaining, RummyTablesFragment.this.mFifthPlayerAutoTimerLayout, RummyTablesFragment.this.isYourTurn);
                        break;
                    case 6:
                        if (RummyUtils.isAppInDebugMode()) {
                            Log.d(Cust_TAG, "vikas, user case 6 calling");
                        }
                        RummyTablesFragment.this.isYourTurn = false;
                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mSixthPlayerTimerLayout);
                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mSixthPlayerAutoTimerLayout);
                        RummyTablesFragment.this.setUserAutoTimer(millisUntilFinished, timeRemaining, RummyTablesFragment.this.mSixthPlayerAutoTimerLayout, RummyTablesFragment.this.isYourTurn);
                        break;
                }
                if (RummyTablesFragment.this.mGameShecduleTv.getVisibility() == View.VISIBLE) {
                    RummyTablesFragment.this.messageVisibleCount = RummyTablesFragment.this.messageVisibleCount + 1;
                    if (RummyTablesFragment.this.messageVisibleCount == 5) {
                        RummyTablesFragment.this.invisibleView(RummyTablesFragment.this.mGameShecduleTv);
                        RummyTablesFragment.this.messageVisibleCount = 0;
                    }
                }
            }

            public void onFinish() {
                if (RummyTablesFragment.this.isYourTurn) {
                    RummyIamBackFragment iamBackFragment = (RummyIamBackFragment) ((RummyTableActivity) RummyTablesFragment.this.mActivity).getFragmentByTag(RummyIamBackFragment.class.getName());
                    if (iamBackFragment != null) {
                        iamBackFragment.disableIamBackButton();
                    }
                }
                RummyTablesFragment.this.isYourTurn = false;
                RummyTablesFragment.this.cancelTimer(RummyTablesFragment.this.playerTurnOutTimer);
                switch (i) {
                    case 1:
                        RummyTablesFragment.this.mUserAutoTimerTv.setText("0");
                        return;
                    case 2:
                        RummyTablesFragment.this.mSecondPlayerAutoTimerTv.setText("0");
                        return;
                    case 3:
                        RummyTablesFragment.this.mThirdPlayerAutoTimerTv.setText("0");
                        return;
                    case 4:
                        RummyTablesFragment.this.mFourthPlayerAutoTimerTv.setText("0");
                        return;
                    case 5:
                        RummyTablesFragment.this.mFifthPlayerAutoTimerTv.setText("0");
                        return;
                    case 6:
                        RummyTablesFragment.this.mSixthPlayerAutoTimerTv.setText("0");
                        return;
                    default:
                        return;
                }
            }
        };
        this.playerTurnOutTimer.start();
    }

    private void setUpExtraTimeUI() {
        if (this.mAutoExtraTimeEvent == null) {
            //showHideView(true, this.mExtraTimeBtn, false);
            enableView(this.mExtraTimeBtn);
        } else if (Integer.parseInt(this.mAutoExtraTimeEvent.getAutoExtraChunks().split("/")[0]) == 1) {
            enableView(this.mExtraTimeBtn);
        } else {
            disableView(this.mExtraTimeBtn);
        }
    }

    private void setDropButton() {

       /* if (this.mTableDetails == null || !(this.mTableDetails.getTableType().equalsIgnoreCase("BEST_OF_2") || this.mTableDetails.getTableType().equalsIgnoreCase("BEST_OF_3"))) {
            showView(this.mDropPlayer);
            enableDropButton(this.mDropPlayer);
        } else {
            disableDropButton(this.mDropPlayer);
        }*/

        if (this.mTableDetails == null || !(this.mTableDetails.getTableType().equalsIgnoreCase("BEST_OF_2") || this.mTableDetails.getTableType().equalsIgnoreCase("BEST_OF_3"))) {
            enableView(this.mDropPlayer);
        } else {
            disableDropButton(this.mDropPlayer);
        }
    }

    private void setUserTimer(long millisUntilFinished, int timeRemaining, View layout, TextView timerIv, CircularProgressBar circularProgressBar, boolean isYourTurn) {
        layout.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.VISIBLE);
        if (!isYourTurn || timeRemaining > 10) {
            this.canLeaveTable = true;
        } else {
            dismissDialog(this.mLeaveDialog);
            this.canLeaveTable = false;
            this.isTossEventRunning = false;
            this.isCardsDistributing = false;
            RummySoundPoolManager.getInstance().playSound(R.raw.rummy_clock);
            RummyVibrationManager.getInstance().vibrate(1);
        }


        if (timeRemaining < 10) {
            timerIv.setTextColor(ContextCompat.getColor(this.mActivity, R.color.rummy_red));
            layout.setBackground(ContextCompat.getDrawable(this.mActivity, R.drawable.rummy_timer_border_red));
        } else if (timeRemaining < 20) {
            timerIv.setTextColor(ContextCompat.getColor(this.mActivity, R.color.rummy_yellow));
            layout.setBackground(ContextCompat.getDrawable(this.mActivity, R.drawable.rummy_timer_border_yellow));
        } else {
            timerIv.setTextColor(ContextCompat.getColor(this.mActivity, R.color.rummy_timerTextColor));
            layout.setBackground(ContextCompat.getDrawable(this.mActivity, R.drawable.rummy_timer_border_green));
        }
        timerIv.setText("" + (millisUntilFinished / 1000) + "");
        circularProgressBar.setProgress(millisUntilFinished / 1000);
        RummyIamBackFragment iamBackFragment;
        if (timeRemaining < 5) {
            iamBackFragment = (RummyIamBackFragment) ((RummyTableActivity) this.mActivity).getFragmentByTag(RummyIamBackFragment.class.getName());
            if (iamBackFragment == null) {
                return;
            }
            if (isYourTurn) {
                iamBackFragment.disableIamBackButton();
                return;
            } else {
                iamBackFragment.enableIamBackButton();
                return;
            }
        }
        iamBackFragment = (RummyIamBackFragment) ((RummyTableActivity) this.mActivity).getFragmentByTag(RummyIamBackFragment.class.getName());
        if (iamBackFragment != null) {
            iamBackFragment.enableIamBackButton();
        }
    }

    private void setUserAutoTimer(long millisUntilFinished, int timeRemaining, View layout, boolean isYourTurn) {
        layout.setVisibility(View.VISIBLE);
        TextView timerIv = (TextView) layout.findViewById(R.id.player_auto_timer_tv);
        if (!isYourTurn || timeRemaining > 10) {
            this.canLeaveTable = true;
        } else {
            dismissDialog(this.mLeaveDialog);
            this.canLeaveTable = false;
            this.isTossEventRunning = false;
            this.isCardsDistributing = false;
            RummySoundPoolManager.getInstance().playSound(R.raw.rummy_clock);
            RummyVibrationManager.getInstance().vibrate(1);
        }
        if (timeRemaining < 5) {
            if (isYourTurn) {
                setUpExtraTimeUI();
            }
        } else if (timeRemaining < 10) {
        }
        timerIv.setText("" + (millisUntilFinished / 1000) + "");
        RummyIamBackFragment iamBackFragment = (RummyIamBackFragment) ((RummyTableActivity) this.mActivity).getFragmentByTag(RummyIamBackFragment.class.getName());
        if (iamBackFragment == null) {
            return;
        }
        if (isYourTurn) {
            iamBackFragment.disableIamBackButton();
        } else {
            iamBackFragment.enableIamBackButton();
        }
    }

    private void sortPlayerCards() {
        dismissQuickMenu();
        sortPlayerStack();
    }

    public RummyPlayingCard getDiscardedCard() {
        return this.mDiscardedCard;
    }

    public void updateCardsGroup(ArrayList<ArrayList<RummyPlayingCard>> updatedCardsList) {
        this.mGroupList = updatedCardsList;
        updateCardsView();
    }

    public void updateCardsView() {
        setGroupView(false);
    }

    public void setSelectedCards() {
        RummyTLog.e(TAG, "Selected cards ::  " + this.mSelectedCards.size());
    }

    private void animateUserDiscardCard() {
        if (!((RummyTableActivity) this.mActivity).isFromIamBack()) {
            this.mUserDiscardLaout.removeAllViews();
            this.mUserDiscardLaout.invalidate();
            clearAnimationData();
            LayoutParams lprams = new LayoutParams(-1, -1);
            ImageView image = new ImageView(getContext());
            image.setLayoutParams(lprams);
            if (this.mDiscardImage != null) {
                image.setImageDrawable(this.mDiscardImage);
            } else {
                image.setImageResource(R.drawable.rummy_closedcard);
            }
            image.setVisibility(View.INVISIBLE);
            this.mUserDiscardLaout.addView(image);
            this.mDummyVies.add(image);
            ImageView iv = (ImageView) this.mDummyVies.get(0);
            iv.setVisibility(View.VISIBLE);
            RummyTransferAnimation an = new RummyTransferAnimation(iv);
            an.setDuration(80);
            an.setListener(new RummyAnimationListener() {
                public void onAnimationStart(RummyAnimation animation) {
                }

                public void onAnimationEnd(RummyAnimation animation) {
                    RummyTablesFragment.this.mUserDiscardLaout.removeAllViews();
                    RummyTablesFragment.this.mUserDiscardLaout.invalidate();
                    RummyTablesFragment.this.clearAnimationData();
                }
            });
            an.setDestinationView(this.mOpenCard).animate();
        }
    }

    private void discardCard(String suit, String face) {
        RummyCardDiscard request = new RummyCardDiscard();
        request.setEventName("CARD_DISCARD");
        request.setTableId(this.tableId);
        request.setUuid(RummyUtils.generateUuid());
        request.setNickName(this.userData.getNickName());
        request.setAutoPlay("False");
        request.setFace(face);
        request.setUserId(String.valueOf(this.userData.getUserId()));
        request.setSuit(suit);
        request.setCardSending("player");
        request.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        updateDeckCardsOnDiscard(suit, face);
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.disCardListner);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            RummyTLog.d(TAG, "discardCard" + gameEngineNotRunning.getLocalizedMessage());
        }
    }

    private void updateDeckCardsOnDiscard(String suit, String face) {
        if (this.mSelectedImgList.size() > 0) {
            this.mDiscardImage = ((ImageView) this.mSelectedImgList.get(0)).getDrawable();
        }
        animateUserDiscardCard();
        String cardValue = String.format("%s%s", new Object[]{suit, face});
        RummyPlayingCard discardCard = new RummyPlayingCard();
        discardCard.setFace(face);
        discardCard.setSuit(suit);
        setOpenCard(discardCard);
        this.faceUpCardList.add(discardCard);
        updateDiscardsList(discardCard);
        int imgId = getResources().getIdentifier(cardValue, "drawable", this.mActivity.getPackageName());
        this.mOpenCard.setVisibility(View.VISIBLE);
        this.mOpenCard.setImageResource(imgId);
        RummyEvent event = new RummyEvent();
        event.setFace(face);
        event.setSuit(suit);
        event.setNickName(this.userData.getNickName());
        ((RummyTableActivity) this.mActivity).addDiscardToPlayer(event);
        RummySoundPoolManager.getInstance().playSound(R.raw.pick_discard);
        RummyVibrationManager.getInstance().vibrate(1);
    }

    private void updateDiscardsList(RummyPlayingCard discardCard) {
        RummyEvent discardEvent = new RummyEvent();
        discardEvent.setFace(discardCard.getFace());
        discardEvent.setSuit(discardCard.getSuit());
        discardEvent.setUserId(Integer.parseInt(this.userData.getUserId()));
        discardEvent.setNickName(this.userData.getNickName());
        discardEvent.setTableId(this.tableId);
        discardEvent.setEventName("CARD_DISCARD");
        ((RummyTableActivity) this.mActivity).addDiscardToPlayer(discardEvent);
    }

    private void cardPick(String suit, String face, String stack) {
        if (stack.contains("down")) {
            animatePickCaCard(0, this.mUserTossCard, true, true);
        } else {
            animatePickCaCard(0, this.mUserTossCard, false, true);
        }
        RummyCardDiscard request = new RummyCardDiscard();
        request.setEventName("CARD_PICK");
        request.setStack(stack);
        request.setTableId(this.tableId);
        request.setUuid(RummyUtils.generateUuid());
        request.setNickName(this.userData.getNickName());
        request.setFace(face);
        request.setUserId(String.valueOf(this.userData.getUserId()));
        request.setSuit(suit);
        request.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.cardPickListner);
            isCardPicked = true;
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            RummyTLog.d(TAG, "cardPickListner" + gameEngineNotRunning.getLocalizedMessage());
        }
    }

    private void dropPlayer() {
        setUserOptions(false);
        setAutoDropSetting(false);
        RummyCardDiscard request = new RummyCardDiscard();
        request.setEventName("PLAYER_DROP");
        request.setUuid(RummyUtils.generateUuid());
        request.setTableId(this.tableId);
        request.setNickName(this.userData.getNickName());
        request.setUserId(String.valueOf(this.userData.getUserId()));
        request.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        request.setMoveMe(WordUtils.capitalize(String.valueOf(isMoveToOtherTable)));
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.dropPlayerListner);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            RummyTLog.d(TAG, "dropPlayer" + gameEngineNotRunning.getLocalizedMessage());
        }
    }

    /*private void autoDropPlayer(String pTableId) {
        Log.e("autoDropPlayer", pTableId+"");
        setUserOptions(false);
        CardDiscard request = new CardDiscard();
        request.setEventName("PLAYER_DROP");
        request.setUuid(Utils.generateUuid());
        request.setTableId(pTableId);
        request.setNickName(this.userData.getNickName());
        request.setUserId(String.valueOf(this.userData.getUserId()));
        request.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        try {
            GameEngine.getInstance();
            GameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), Utils.getObjXMl(request), this.dropPlayerListner);
        } catch (GameEngineNotRunning gameEngineNotRunning) {
            TLog.d(TAG, "dropPlayer" + gameEngineNotRunning.getLocalizedMessage());
        }
    }
*/
    private void turnExtraTime() {
        RummyLeaveTableRequest request = new RummyLeaveTableRequest();
        request.setEventName("extratime");
        request.setUuid(RummyUtils.generateUuid());
        request.setTableId(this.tableId);
        request.setNickName(this.userData.getNickName());
        request.setUserId(String.valueOf(this.userData.getUserId()));
        request.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.extraTimeListner);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            RummyTLog.d(TAG, "EXTRA_TIME" + gameEngineNotRunning.getLocalizedMessage());
        }
    }

    private void leaveTable() {
//
        //    int intIndex = alAutoDrop.indexOf(myTableId);
        //    alAutoDropBoolean.remove(intIndex);
        //    alAutoDrop.remove(tableId);
//
        if ((RummyApplication.getInstance()).getJoinedTableIds().size() == 1) {
            ((RummyTableActivity) this.mActivity).setIsBackPressed(true);
        }
        //showLoadingDialog(this.mActivity);
        RummyLeaveTableRequest request = new RummyLeaveTableRequest();
        request.setEventName("quit_table");
        request.setUuid(RummyUtils.generateUuid());
        request.setTableId(this.tableId);
        request.setNickName(this.userData.getNickName());
        request.setUserId(String.valueOf(this.userData.getUserId()));
        request.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.leaveTableListner);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            RummyTLog.d(TAG, "LEAVE_TABLE" + gameEngineNotRunning.getLocalizedMessage());
        }

    }

    public void sortPlayerStack() {
        this.mRummyView.removeViews();
        this.resetAllGroupsCountUI();
        this.playerCards.clear();
        clearSelectedCards();
        ArrayList<RummyPlayingCard> mHeartCards = new ArrayList();
        ArrayList<RummyPlayingCard> mClubsCards = new ArrayList();
        ArrayList<RummyPlayingCard> mDiamondCards = new ArrayList();
        ArrayList<RummyPlayingCard> mSpadeCards = new ArrayList();
        ArrayList<RummyPlayingCard> jokerCardList = new ArrayList();
        Iterator it = this.mGroupList.iterator();
        while (it.hasNext()) {
            Iterator it2 = ((ArrayList) it.next()).iterator();
            while (it2.hasNext()) {
                RummyPlayingCard card = (RummyPlayingCard) it2.next();
                if (card.getSuit().equalsIgnoreCase(RummyUtils.DIAMOND)) {
                    mDiamondCards.add(card);
                } else if (card.getSuit().equalsIgnoreCase(RummyUtils.CLUB)) {
                    mClubsCards.add(card);
                } else if (card.getSuit().equalsIgnoreCase(RummyUtils.SPADE)) {
                    mSpadeCards.add(card);
                } else if (card.getSuit().equalsIgnoreCase(RummyUtils.HEART)) {
                    mHeartCards.add(card);
                } else {
                    jokerCardList.add(card);
                }
            }
        }
        this.mGroupList.clear();
        if (mDiamondCards.size() > 0) {
            Collections.sort(mDiamondCards, new FaceComparator());
            this.mGroupList.add(mDiamondCards);
        }
        if (mClubsCards.size() > 0) {
            Collections.sort(mClubsCards, new FaceComparator());
            this.mGroupList.add(mClubsCards);
        }
        if (mSpadeCards.size() > 0) {
            Collections.sort(mSpadeCards, new FaceComparator());
            this.mGroupList.add(mSpadeCards);
        }
        if (mHeartCards.size() > 0) {
            Collections.sort(mHeartCards, new FaceComparator());
            this.mGroupList.add(mHeartCards);
        }
        if (jokerCardList.size() > 0) {
            Collections.sort(jokerCardList, new FaceComparator());
            this.mGroupList.add(jokerCardList);
        }
        setGroupView(false);
    }

    private void addSampleCards() {
        this.isYourTurn = true;
        ArrayList<RummyPlayingCard> group1 = new ArrayList();
        ArrayList<RummyPlayingCard> group2 = new ArrayList();
        ArrayList<RummyPlayingCard> group3 = new ArrayList();
        ArrayList<RummyPlayingCard> group4 = new ArrayList();
        ArrayList<RummyPlayingCard> group5 = new ArrayList();
        this.mCards.clear();
        setUserOptions(true);
        showView(this.mUserPlayerLayout);
        showView(this.sortCards);
        enableView(this.sortCards);
        RummyPlayingCard jokerCard = new RummyPlayingCard();
        jokerCard.setFace("10");
        jokerCard.setSuit("s");
        this.mJockerCard = jokerCard;
        RummyPlayingCard card = new RummyPlayingCard();
        card.setFace("10");
        card.setSuit("c");
        RummyPlayingCard card1 = new RummyPlayingCard();
        card1.setFace("11");
        card1.setSuit("c");
        RummyPlayingCard card2 = new RummyPlayingCard();
        card2.setFace("12");
        card2.setSuit("c");
        RummyPlayingCard card01 = new RummyPlayingCard();
        card01.setFace("13");
        card01.setSuit("c");
        group1.add(card);
        group1.add(card1);
        group1.add(card2);
        group1.add(card01);
        RummyPlayingCard card3 = new RummyPlayingCard();
        card3.setFace("10");
        card3.setSuit("s");
        RummyPlayingCard card4 = new RummyPlayingCard();
        card4.setFace("12");
        card4.setSuit("s");
        RummyPlayingCard card5 = new RummyPlayingCard();
        card5.setFace("13");
        card5.setSuit("s");
        RummyPlayingCard card6 = new RummyPlayingCard();
        card6.setFace("11");
        card6.setSuit("s");
        group2.add(card3);
        group2.add(card4);
        group2.add(card5);
        group2.add(card6);
        RummyPlayingCard card7 = new RummyPlayingCard();
        card7.setFace("6");
        card7.setSuit("d");
        RummyPlayingCard card8 = new RummyPlayingCard();
        card8.setFace("9");
        card8.setSuit("d");
        group3.add(card7);
        group3.add(card8);
        RummyPlayingCard card10 = new RummyPlayingCard();
        card10.setFace("8");
        card10.setSuit("c");
        RummyPlayingCard card11 = new RummyPlayingCard();
        card11.setFace("1");
        card11.setSuit("c");
        group4.add(card10);
        group4.add(card11);
        RummyPlayingCard card12 = new RummyPlayingCard();
        card12.setFace("3");
        card12.setSuit("s");
        RummyPlayingCard card13 = new RummyPlayingCard();
        card13.setFace("4");
        card13.setSuit("s");
        showView(this.mOpenCard);
        enableDeckCards();
        this.faceUpCardList = new ArrayList();
        this.faceUpCardList.add(card13);
        setOpenCard(card13);
        group5.add(card12);
        group5.add(card13);
        this.mGroupList = new ArrayList();
        this.mGroupList.add(group1);
        this.mGroupList.add(group2);
        this.mGroupList.add(group3);
        this.mGroupList.add(group4);
        this.mGroupList.add(group5);
        setGroupView(false);
    }

    private void setCardsOnIamBack(List<RummyPlayingCard> cards) {
        if (cards != null && this.cardStack != null) {
            int i;
            List<RummyPlayingCard> sortedCards = checkCardsWithSendDealStack(cards);
            if (sortedCards != null && sortedCards.size() > 0) {
                cards = sortedCards;
            }
            this.mGroupList = new ArrayList();
            ArrayList<Integer> slotsList = new ArrayList();
            for (i = 0; i < cards.size(); i++) {
                slotsList.add(Integer.valueOf(Integer.parseInt(((RummyPlayingCard) cards.get(i)).getSlot())));
            }
            int maxSlot = ((Integer) Collections.max(slotsList)).intValue();
            ArrayList<RummyPlayingCard> tempList = new ArrayList();
            for (i = 0; i <= maxSlot; i++) {
                tempList.add(i, null);
            }
            for (i = 0; i < cards.size(); i++) {
                RummyPlayingCard card = (RummyPlayingCard) cards.get(i);
                tempList.set(Integer.parseInt(card.getSlot()), card);
            }
            int groupCount = 1;
            for (i = 0; i < tempList.size(); i++) {
                if (tempList.get(i) == null) {
                    groupCount++;
                }
            }
            int index = 0;
            for (i = 0; i < groupCount; i++) {
                ArrayList<RummyPlayingCard> cardList = new ArrayList();
                for (int j = index; j < tempList.size(); j++) {
                    if (tempList.get(j) == null) {
                        index++;
                        break;
                    }
                    cardList.add(tempList.get(j));
                    index++;
                }
                if (cardList.size() > 0) {
                    this.mGroupList.add(cardList);
                }
            }
            setGroupView(false);
        }
    }

    private List<RummyPlayingCard> checkCardsWithSendDealStack(List<RummyPlayingCard> slotCards) {
        if (slotCards.size() != this.cardStack.size()) {
            Iterator<RummyPlayingCard> slotIter = slotCards.iterator();
            while (slotIter.hasNext()) {
                RummyPlayingCard slotCard = (RummyPlayingCard) slotIter.next();
                String slotFace = slotCard.getFace();
                String slotSuit = slotCard.getSuit();
                Iterator<RummyPlayingCard> stackIter = this.cardStack.iterator();
                boolean isCardFound = false;
                while (stackIter.hasNext()) {
                    RummyPlayingCard stackCard = (RummyPlayingCard) stackIter.next();
                    String stackFace = stackCard.getFace();
                    String stackSuit = stackCard.getSuit();
                    if (slotFace.equalsIgnoreCase(stackFace) && slotSuit.equalsIgnoreCase(stackSuit)) {
                        isCardFound = true;
                        break;
                    }
                }
                if (!isCardFound) {
                    slotIter.remove();
                }
            }
        }
        return slotCards;
    }

    private void setGroupView(boolean isGrouping) {
        Log.d(TAG, "Inside setGroupView *********************************************************************");
        Log.d(TAG, "Inside setGroupView: CARDS SIZE: " + this.playerCards.size());

        this.mRummyView.removeViews();
        this.resetAllGroupsCountUI();
        this.mRummyView.invalidate();
        this.mRummyView.setVisibility(View.VISIBLE);
        if (isGrouping) {
            setUpCardsOnGroup();
            return;
        }

        int index = 0;
        int groupCount = 0;
        Iterator it = this.mGroupList.iterator();
        while (it.hasNext()) {
            ArrayList<RummyPlayingCard> groupList = (ArrayList) it.next();
            if (groupList.size() > 0) {
                Iterator it2 = groupList.iterator();
                while (it2.hasNext()) {
                    addCardToRummyView((RummyPlayingCard) it2.next(), index);
                    index++;
                }
                addEmptyView();
                groupCount++;
            }
        }
        if (groupCount < 6) {
            //addEmptyView();
            //addEmptyView();
        }
        if (!this.isCardsDistributing) {
            sendCardsSlots();
        }

        if(this.mGroupList != null && this.mGroupList.size() == 1)
        {
            dismissQuickMenu();
            sortPlayerCards();
        }

        ArrayList<ArrayList<RummyPlayingCard>> tempGroupList = new ArrayList<>();
        for (int i =0 ;i < this.mGroupList.size();i++)
        {
            ArrayList<RummyPlayingCard> singleGroup = this.mGroupList.get(i);
            ArrayList<RummyPlayingCard> tempSingleGroup = new ArrayList<>();
            for(int j=0;j<singleGroup.size();j++)
            {

                tempSingleGroup.add(singleGroup.get(j));
            }

            if(tempSingleGroup != null && tempSingleGroup.size() > 0)
            {
                tempGroupList.add(tempSingleGroup);
            }
        }


        checkPureImpure(tempGroupList);
        tempGroupList = null;
    }

    private void setUpCardsOnGroup() {
        ArrayList<ArrayList<RummyPlayingCard>> updatedList = new ArrayList();
        ArrayList<RummyPlayingCard> groupedList = (ArrayList) this.mGroupList.get(this.mGroupList.size() - 1);
        updatedList.add(0, groupedList);
        this.mGroupList.remove(groupedList);
        Iterator it = this.mGroupList.iterator();
        while (it.hasNext()) {
            updatedList.add((ArrayList) it.next());
        }
        this.mGroupList = updatedList;
        int index = 0;
        int groupCount = 0;
        int noOfSlots = 0;
        it = this.mGroupList.iterator();
        while (it.hasNext()) {
            ArrayList<RummyPlayingCard> groupList = (ArrayList) it.next();
            if (groupList.size() > 0) {
                Iterator it2 = groupList.iterator();
                while (it2.hasNext()) {
                    addCardToRummyView((RummyPlayingCard) it2.next(), index);
                    index++;
                    noOfSlots++;
                }
                noOfSlots++;
                if (noOfSlots < 23 && groupList.size() > 1) {
                    addEmptyView();
                }
                groupCount++;            }
        }
        if (groupCount < 6) {
           // addEmptyView();
          //  addEmptyView();
        }
        sendCardsSlots();

        ArrayList<ArrayList<RummyPlayingCard>> tempGroupList = new ArrayList<>();
        for (int i =0 ;i < this.mGroupList.size();i++)
        {
            ArrayList<RummyPlayingCard> singleGroup = this.mGroupList.get(i);
            ArrayList<RummyPlayingCard> tempSingleGroup = new ArrayList<>();
            for(int j=0;j<singleGroup.size();j++)
            {
                tempSingleGroup.add(singleGroup.get(j));
            }

            if(tempSingleGroup != null && tempSingleGroup.size() > 0)
            {
                tempGroupList.add(tempSingleGroup);
            }
        }


        checkPureImpure(tempGroupList);
        tempGroupList = null;
    }

    public void sendCardsSlots() {
        ArrayList<RummyPlayingCard> cardsList;
        cardsList = this.mRummyView.getUpdateCardsSlots();
        RummyTableCards table = new RummyTableCards();
        table.setTableId(this.tableId);
        table.setCards(cardsList);
        ((RummyBaseActivity) this.mActivity).sendCardSlots(table);
    }



    private void checkPureImpure(ArrayList<ArrayList<RummyPlayingCard>> grouplist)
    {
        resetAllGroupsCountUI();
        int LOOP_THRESHOLD = 13;
        Map<String, Integer> counter = new HashMap<String, Integer>();
        counter.put("plf",0);
        counter.put("lf",0);
        counter.put("s",0);
        counter.put("aj",0);
        List<String> possition = new ArrayList();

        List<Boolean> meld_results = new ArrayList();

        int loop_count = 0;

        for(int i=0;i<grouplist.size();i++)
        {
            if(i >= LOOP_THRESHOLD)
                break;


            if(grouplist.get(i) != null)
            {
                if(counter.get("plf") < 4)
                {
                    boolean result = pureLifeCheck(grouplist.get(i));
                    if(result)
                    {
                        meld_results.add(result);
                        possition.add("plf");
                        counter.put("plf",counter.get("plf")+1);
                        continue;

                    }
                }

                if(counter.get("lf") < 4)
                {
                    boolean result = extraLifeCheck(grouplist.get(i));
                    if(result)
                    {
                        meld_results.add(result);
                        possition.add("lf");
                        counter.put("lf",counter.get("lf")+1);
                        continue;
                    }
                }
                boolean result = SetCheck(grouplist.get(i));
                if(result)
                {
                    meld_results.add(result);
                    possition.add("s");
                    counter.put("s",counter.get("s")+1);
                    continue;
                }

                if(grouplist.get(i).size() < 3)
                {
                    int count = 0;
                    for(int j=0; j< grouplist.get(i).size();j++)
                    {
                        if(isCardJoker(grouplist.get(i).get(j)))
                        {
                            count = count+1;
                        }
                    }

                    if(count == grouplist.get(i).size() && count != 0)
                    {
                        meld_results.add(true);
                        possition.add("aj");
                        counter.put("aj",counter.get("aj")+1);
                        continue;
                    }

                }

                meld_results.add(false);
                possition.add("none");


            }


        }

        Log.e("vikas pure impure","counter "+ new Gson().toJson(counter));
        Log.e("vikas pure impure","position "+possition.toString());
        Log.e("vikas pure impure","meld result "+meld_results.toString());

        countScore(possition,meld_results,grouplist);

        if(this.mRummyView != null)
        {
            // Log.e("vikas","left right margin group"+ this.mRummyView.getGroupStartEndMargin().toString());

            ArrayList<ArrayList<Integer>> groupsMargin = this.mRummyView.getGroupStartEndMargin();

            for (int i=0;i<groupsMargin.size();i++)
            {
                if(i == 0)
                {
                    setLeftMargin(this.group_1,groupsMargin.get(i));
                }
                else if(i == 1)
                {
                    setLeftMargin(this.group_2,groupsMargin.get(i));
                }
                else if(i == 2)
                {
                    setLeftMargin(this.group_3,groupsMargin.get(i));
                }
                else if(i == 3)
                {
                    setLeftMargin(this.group_4,groupsMargin.get(i));
                }
                else if(i == 4)
                {
                    setLeftMargin(this.group_5,groupsMargin.get(i));
                }
                else if(i == 5)
                {
                    setLeftMargin(this.group_6,groupsMargin.get(i));
                }
                else if(i == 6)
                {
                    setLeftMargin(this.group_7,groupsMargin.get(i));
                }
                else if(i == 7)
                {
                    setLeftMargin(this.group_8,groupsMargin.get(i));
                }
            }

            //Utils.setViewMargin(this.rl_groups_container,-(GameRoomCustomScreenLess700.stackCardWidth),0,0,0);

        }




    }

    private void setLeftMargin(View view , ArrayList<Integer> marginlist)
    {
        // fix_Left_margin_groups =  (int)this.mRummyView.getX();
      //  TLog.e("vikas","fix margin group ="+fix_Left_margin_groups);
        showView(view);

        int centerOfGroup = 0;

        if(!isTablet(getActivity()))
        {
             centerOfGroup = (marginlist.get(0) + marginlist.get(1)+ RummyUtils.convertDpToPixel(RummyGameRoomCustomScreenLess700.stackCardWidth))/2;
        }else
        {
             centerOfGroup = (marginlist.get(0) + marginlist.get(1)+ RummyUtils.convertDpToPixel(RummyGameRoomCustomScreenMore700.stackCardWidth))/2;
        }

        view.measure(0,0);
        int width = view.getMeasuredWidth();

        centerOfGroup = centerOfGroup - (width/2);
        //centerOfGroup = centerOfGroup + fix_Left_margin_groups;
        RummyUtils.setViewMargin(view, RummyUtils.convertPixelsToDp(centerOfGroup),0,0,0);



    }


    private void resetAllGroupsCountUI()
    {
        invisibleView(this.group_1);
        invisibleView(this.group_2);
        invisibleView(this.group_3);
        invisibleView(this.group_4);
        invisibleView(this.group_5);
        invisibleView(this.group_6);
        invisibleView(this.group_7);
        invisibleView(this.group_8);
    }

    private void countScore(List<String> position, List<Boolean> result, ArrayList<ArrayList<RummyPlayingCard>> grouplist)
    {
        int lifs = 0;
        lifs = (Collections.frequency(position,"plf") + Collections.frequency(position,"lf"));
        int p_life = Collections.frequency(position,"plf");

        Log.e("vikas","pure life count ="+p_life +" life count= "+lifs);
        boolean correct_show = true;

        if(p_life == 0 || lifs < 2)
        {
            correct_show = false;
        }

        for(int i=0;i < result.size(); i++)
        {
            if(!position.get(i).equalsIgnoreCase("plf") && !correct_show)
            {
                if(!position.get(i).equalsIgnoreCase("aj"))
                {
                    result.set(i,false);
                }

            }



            if(i == 0)
            {
                TextView group_count_1 = (TextView)this.group_1.findViewById(R.id.group_1_count);
                if(result.get(i))
                {
                    group_count_1.setText(getPureImpureText(position.get(i).toString()));
                    //((TextView)this.group_1.findViewById(R.id.group_1_count)).setText(getPureImpureText(position.get(i).toString()));
                    ((ImageView)this.group_1.findViewById(R.id.group_1_icon)).setImageDrawable(getResources().getDrawable(R.drawable.rummy_pure_tick));
                    Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score=0");
                }
                else
                {
                    group_count_1.setText(countScorewithCards(grouplist.get(i))+" POINTS");
                    //((TextView)this.group_1.findViewById(R.id.group_1_count)).setText("Invalid ("+countScorewithCards(grouplist.get(i))+")");
                    ((ImageView)this.group_1.findViewById(R.id.group_1_icon)).setImageDrawable(getResources().getDrawable(R.drawable.rummy_impure_cross));
                    Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score="+ countScorewithCards(grouplist.get(i)));
                }
                group_count_1.invalidate();
                group_count_1.requestLayout();
            }
            else if(i == 1)
            {
                TextView group_count_2 = (TextView)this.group_2.findViewById(R.id.group_2_count);
                if(result.get(i))
                {
                    group_count_2.setText(getPureImpureText(position.get(i).toString()));
                    //((TextView)this.group_2.findViewById(R.id.group_2_count)).setText(getPureImpureText(position.get(i).toString()));
                    ((ImageView)this.group_2.findViewById(R.id.group_2_icon)).setImageDrawable(getResources().getDrawable(R.drawable.rummy_pure_tick));
                    Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score=0");
                }
                else
                {
                    group_count_2.setText(countScorewithCards(grouplist.get(i))+" POINTS");
                    //((TextView)this.group_2.findViewById(R.id.group_2_count)).setText("Invalid ("+countScorewithCards(grouplist.get(i))+")");
                    ((ImageView)this.group_2.findViewById(R.id.group_2_icon)).setImageDrawable(getResources().getDrawable(R.drawable.rummy_impure_cross));
                    Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score="+ countScorewithCards(grouplist.get(i)));
                }
                group_count_2.invalidate();
                group_count_2.requestLayout();
            }
            else if(i == 2)
            {
                TextView group_count_3 = (TextView)this.group_3.findViewById(R.id.group_3_count);
                if(result.get(i))
                {
                    group_count_3.setText(getPureImpureText(position.get(i).toString()));
                    // ((TextView)this.group_3.findViewById(R.id.group_3_count)).setText(getPureImpureText(position.get(i).toString()));
                    ((ImageView)this.group_3.findViewById(R.id.group_3_icon)).setImageDrawable(getResources().getDrawable(R.drawable.rummy_pure_tick));
                    Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score=0");
                }
                else
                {
                    group_count_3.setText(countScorewithCards(grouplist.get(i))+" POINTS");
                    //((TextView)this.group_3.findViewById(R.id.group_3_count)).setText("Invalid ("+countScorewithCards(grouplist.get(i))+")");
                    ((ImageView)this.group_3.findViewById(R.id.group_3_icon)).setImageDrawable(getResources().getDrawable(R.drawable.rummy_impure_cross));
                    Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score="+ countScorewithCards(grouplist.get(i)));
                }

                group_count_3.invalidate();
                group_count_3.requestLayout();
            }
            else if(i == 3)
            {
                TextView group_count_4 =(TextView)this.group_4.findViewById(R.id.group_4_count);
                if(result.get(i))
                {
                    group_count_4.setText(getPureImpureText(position.get(i).toString()));
                    //((TextView)this.group_4.findViewById(R.id.group_4_count)).setText(getPureImpureText(position.get(i).toString()));
                    ((ImageView)this.group_4.findViewById(R.id.group_4_icon)).setImageDrawable(getResources().getDrawable(R.drawable.rummy_pure_tick));
                    Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score=0");
                }
                else
                {
                    group_count_4.setText(countScorewithCards(grouplist.get(i))+" POINTS");
                    // ((TextView)this.group_4.findViewById(R.id.group_4_count)).setText("Invalid ("+countScorewithCards(grouplist.get(i))+")");
                    ((ImageView)this.group_4.findViewById(R.id.group_4_icon)).setImageDrawable(getResources().getDrawable(R.drawable.rummy_impure_cross));
                    Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score="+ countScorewithCards(grouplist.get(i)));
                }

                group_count_4.invalidate();
                group_count_4.requestLayout();
            }
            else if(i == 4)
            {
                TextView group_count_5 =(TextView)this.group_5.findViewById(R.id.group_5_count);
                if(result.get(i))
                {
                    group_count_5.setText(getPureImpureText(position.get(i).toString()));
                    //((TextView)this.group_5.findViewById(R.id.group_5_count)).setText(getPureImpureText(position.get(i).toString()));
                    ((ImageView)this.group_5.findViewById(R.id.group_5_icon)).setImageDrawable(getResources().getDrawable(R.drawable.rummy_pure_tick));
                    Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score=0");
                }
                else
                {
                    group_count_5.setText(countScorewithCards(grouplist.get(i))+" POINTS");
                    //((TextView)this.group_5.findViewById(R.id.group_5_count)).setText("Invalid ("+countScorewithCards(grouplist.get(i))+")");
                    ((ImageView)this.group_5.findViewById(R.id.group_5_icon)).setImageDrawable(getResources().getDrawable(R.drawable.rummy_impure_cross));
                    Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score="+ countScorewithCards(grouplist.get(i)));
                }

                group_count_5.invalidate();
                group_count_5.requestLayout();
            }
            else if(i == 5)
            {
                TextView group_count_6 =(TextView)this.group_6.findViewById(R.id.group_6_count);
                if(result.get(i))
                {
                    group_count_6.setText(getPureImpureText(position.get(i).toString()));
                    // ((TextView)this.group_6.findViewById(R.id.group_6_count)).setText(getPureImpureText(position.get(i).toString()));
                    ((ImageView)this.group_6.findViewById(R.id.group_6_icon)).setImageDrawable(getResources().getDrawable(R.drawable.rummy_pure_tick));
                    Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score=0");
                }
                else
                {
                    group_count_6.setText(countScorewithCards(grouplist.get(i))+" POINTS");
                    // ((TextView)this.group_6.findViewById(R.id.group_6_count)).setText("Invalid ("+countScorewithCards(grouplist.get(i))+")");
                    ((ImageView)this.group_6.findViewById(R.id.group_6_icon)).setImageDrawable(getResources().getDrawable(R.drawable.rummy_impure_cross));
                    Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score="+ countScorewithCards(grouplist.get(i)));
                }
                group_count_6.invalidate();
                group_count_6.requestLayout();
            }
            else if(i == 6)
            {
                TextView group_count_7 =(TextView)this.group_7.findViewById(R.id.group_7_count);
                if(result.get(i))
                {
                    group_count_7.setText(getPureImpureText(position.get(i).toString()));
                    // ((TextView)this.group_7.findViewById(R.id.group_7_count)).setText(getPureImpureText(position.get(i).toString()));
                    ((ImageView)this.group_7.findViewById(R.id.group_7_icon)).setImageDrawable(getResources().getDrawable(R.drawable.rummy_pure_tick));
                    Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score=0");
                }
                else
                {
                    group_count_7.setText(countScorewithCards(grouplist.get(i))+" POINTS");
                    //((TextView)this.group_7.findViewById(R.id.group_7_count)).setText("Invalid ("+countScorewithCards(grouplist.get(i))+")");
                    ((ImageView)this.group_7.findViewById(R.id.group_7_icon)).setImageDrawable(getResources().getDrawable(R.drawable.rummy_impure_cross));
                    Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score="+ countScorewithCards(grouplist.get(i)));
                }

                group_count_7.invalidate();
                group_count_7.requestLayout();
            }
            else if(i == 7)
            {
                TextView group_count_8 = (TextView)this.group_8.findViewById(R.id.group_8_count);
                if(result.get(i))
                {
                    group_count_8.setText(getPureImpureText(position.get(i).toString()));
                    // ((TextView)this.group_8.findViewById(R.id.group_8_count)).setText(getPureImpureText(position.get(i).toString()));
                    ((ImageView)this.group_8.findViewById(R.id.group_8_icon)).setImageDrawable(getResources().getDrawable(R.drawable.rummy_pure_tick));
                    Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score=0");
                }
                else
                {
                    group_count_8.setText(countScorewithCards(grouplist.get(i))+" POINTS");
                    //((TextView)this.group_8.findViewById(R.id.group_8_count)).setText("Invalid ("+countScorewithCards(grouplist.get(i))+")");
                    ((ImageView)this.group_8.findViewById(R.id.group_8_icon)).setImageDrawable(getResources().getDrawable(R.drawable.rummy_impure_cross));
                    Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score="+ countScorewithCards(grouplist.get(i)));
                }

                group_count_8.invalidate();
                group_count_8.requestLayout();
            }


            invalidateAllGroup();
           /* if(result.get(i))
            {
                Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score=0");
            }
            else
            {
                Log.e("vikas","pure impure score, result="+result.get(i).toString()+" ,type= "+position.get(i)+" , score="+ countScorewithCards(grouplist.get(i)));
            }*/



        }


    }

    private void invalidateAllGroup()
    {
        this.group_1.invalidate();
        this.group_1.requestLayout();

        this.group_2.invalidate();
        this.group_2.requestLayout();

        this.group_3.invalidate();
        this.group_3.requestLayout();

        this.group_4.invalidate();
        this.group_4.requestLayout();

        this.group_5.invalidate();
        this.group_5.requestLayout();

        this.group_6.invalidate();
        this.group_6.requestLayout();

        this.group_7.invalidate();
        this.group_7.requestLayout();

        this.group_8.invalidate();
        this.group_8.requestLayout();


    }

    private String getPureImpureText(String str) {
        if (str.equalsIgnoreCase("plf")) {
          //  return "Pure Sequence";
            return "PURE";
        }
        else if (str.equalsIgnoreCase("lf"))
        {
           // return "Sequence";
            return "SEQUENCE";
        }
        else if (str.equalsIgnoreCase("s"))
        {
            return "SET";
        }
        else if (str.equalsIgnoreCase("aj"))
        {
            return "JOKER";
        }
        else
        {
            return "IMPURE";
        }
    }


    private int countScorewithCards(ArrayList<RummyPlayingCard> meld)
    {
        int count = 0;
        for(int i=0;i<meld.size();i++)
        {
            if(isCardJoker(meld.get(i)))
            {
                count = count + 0;
            }
            else
            {
                count = count + Integer.parseInt(meld.get(i).getValue());
            }

        }

        return count;
    }


    private boolean isCardJoker(RummyPlayingCard card)
    {
        if(Integer.parseInt(this.mJockerCard.getFace()) == 0)
        {
            if(Integer.parseInt(card.getFace()) == 1 || card.getFace().equalsIgnoreCase("0"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            if(this.mJockerCard.getFace().equalsIgnoreCase(card.getFace()) || card.getFace().equalsIgnoreCase("0"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    private boolean pureLifeCheck(ArrayList<RummyPlayingCard> life)
    {
        if(life.size() < 3)
        {
            return false;
        }
        else
        {
            life = setSequence(life);

            if(Integer.parseInt(life.get(life.size()-1).getFace()) == 13)
            {
                if(Integer.parseInt(life.get(0).getFace()) == 1 && Integer.parseInt(life.get(1).getFace()) != 2)
                {
                    RummyPlayingCard card =  life.remove(0);
                    card.setFace("14");
                    life.add(card);
                }
            }

            for(int i=0;i<life.size();i++)
            {
                if(i == life.size()-1)
                {
                    /*if(Integer.parseInt(life.get(life.size()-1).getFace()) == 14)
                    {
                        life.get(life.size()-1).setFace("1");
                    }*/

                    for (RummyPlayingCard temp : life) {
                        if(Integer.parseInt(temp.getFace()) == 14)
                        {
                            temp.setFace("1");
                        }
                    }
                    return true;
                }
                if((Math.abs(Integer.parseInt(life.get(i).getFace()) - Integer.parseInt(life.get(i+1).getFace())) == 1 || Math.abs(Integer.parseInt(life.get(i).getFace()) - Integer.parseInt(life.get(i+1).getFace())) == 12) && life.get(i).getSuit().equalsIgnoreCase(life.get(i+1).getSuit()))
                {
                    continue;
                }
                else
                {
                    /*if(Integer.parseInt(life.get(life.size()-1).getFace()) == 14)
                    {
                        life.get(life.size()-1).setFace("1");
                    }*/

                    for (RummyPlayingCard temp : life) {
                        if(Integer.parseInt(temp.getFace()) == 14)
                        {
                            temp.setFace("1");
                        }
                    }
                    return false;
                }
            }
            return false;
        }


    }


    private ArrayList<RummyPlayingCard> setSequence(ArrayList<RummyPlayingCard> meld)
    {
        boolean done = true;
        while(done)
        {
            int count = 0;
            for(int i=0;i < meld.size()-1;i++)
            {
                if(Integer.parseInt(meld.get(i).getFace()) > Integer.parseInt(meld.get(i+1).getFace()))
                {
                    RummyPlayingCard card = meld.get(i);
                    meld.set(i,meld.get(i+1));
                    meld.set(i+1,card);
                    count = count +1;
                }
            }
            if(count == 0)
            {
                done = false;
            }

        }

        return meld;
    }

    private boolean extraLifeCheck(ArrayList<RummyPlayingCard> life)
    {
        if(life.size() < 3)
        {
            return false;
        }
        else
        {
            int jo_count = 0;
            ArrayList<RummyPlayingCard> seqtemp = new ArrayList<>();

            for(int i =0; i < life.size(); i++)
            {
                if(isCardJoker(life.get(i)))
                {
                    jo_count = jo_count +1;
                }
                else
                {
                    seqtemp.add(life.get(i));
                }
            }
            if(seqtemp.size() ==0 )
            {
                return true;
            }

            seqtemp = setSequence(seqtemp);
            RummyPlayingCard basecard = seqtemp.get(0);
            for(int i=0;i <seqtemp.size();i++)
            {
                if(seqtemp.get(i).getSuit().equalsIgnoreCase(basecard.getSuit()))
                {
                    continue;
                }
                else
                {
                    return false;
                }
            }

            if(jo_count > 0)
            {
                if(Integer.parseInt(seqtemp.get(0).getFace()) == 1)
                {
                    if(Integer.parseInt(seqtemp.get(seqtemp.size()-1).getFace()) != 13)
                    {
                        if((((Math.abs(Integer.parseInt(seqtemp.get(0).getFace()) - Integer.parseInt(seqtemp.get(seqtemp.size()-1).getFace()) ) -1) - (seqtemp.size() -2) ) + Math.abs(life.size() -  Integer.parseInt(seqtemp.get(seqtemp.size()-1).getFace())) == jo_count) || Integer.parseInt(seqtemp.get(seqtemp.size()-1).getFace()) == 2)
                        {

                        }
                        else
                        {
                            RummyPlayingCard card = seqtemp.remove(0);
                            card.setFace("14");
                            seqtemp.add(card);
                        }
                    }
                    else
                    {
                        RummyPlayingCard card = seqtemp.remove(0);
                        card.setFace("14");
                        seqtemp.add(card);
                    }
                }
            }
            else
            {
                if(Integer.parseInt(seqtemp.get(0).getFace()) == 1)
                {
                    if(Integer.parseInt(seqtemp.get(seqtemp.size()-1).getFace()) == 13)    ///need to check
                    {
                        RummyPlayingCard card = seqtemp.remove(0);
                        card.setFace("14");
                        seqtemp.add(card);
                    }
                }
            }


            for(int i=0; i< seqtemp.size(); i++)
            {
                if(i == seqtemp.size()-1)
                {
                    if(Integer.parseInt(seqtemp.get(i).getFace()) == 14)
                    {
                        seqtemp.get(i).setFace("1");
                    }


                    for (RummyPlayingCard temp : life) {
                        if(Integer.parseInt(temp.getFace()) == 14)
                        {
                            temp.setFace("1");
                        }
                    }

                    return true;
                }

                if(Math.abs(Integer.parseInt(seqtemp.get(i).getFace()) - Integer.parseInt(seqtemp.get(i+1).getFace())) == 1 || Math.abs(Integer.parseInt(seqtemp.get(i).getFace()) - Integer.parseInt(seqtemp.get(i+1).getFace())) == 12)
                {
                    continue;
                }
                else
                {
                    if(jo_count > 0 && !seqtemp.get(i).getFace().equalsIgnoreCase(seqtemp.get(i+1).getFace()))
                    {
                        if(jo_count >= Math.abs( Integer.parseInt(seqtemp.get(i).getFace()) - Integer.parseInt(seqtemp.get(i+1).getFace())) - 1)
                        {
                            jo_count = jo_count - (Math.abs( Integer.parseInt(seqtemp.get(i).getFace()) - Integer.parseInt(seqtemp.get(i+1).getFace())) -1);
                        }
                        else
                        {
                            /*if(Integer.parseInt(life.get(life.size()-1).getFace()) == 14)
                            {
                                life.get(life.size() -1).setFace("1");
                            }
                            if(Integer.parseInt(life.get(0).getFace()) == 14)
                            {
                                life.get(0).setFace("1");
                            }
                            if(Integer.parseInt(life.get(1).getFace()) == 14)
                            {
                                life.get(1).setFace("1");
                            }*/
                            for (RummyPlayingCard temp : life) {
                                if(Integer.parseInt(temp.getFace()) == 14)
                                {
                                    temp.setFace("1");
                                }
                            }
                            return false;
                        }
                    }
                    else
                    {
                       /* if(Integer.parseInt(life.get(life.size()-1).getFace()) == 14)
                        {
                            life.get(life.size() -1).setFace("1");
                        }
                        if(Integer.parseInt(life.get(0).getFace()) == 14)
                        {
                            life.get(0).setFace("1");
                        }
                        if(Integer.parseInt(life.get(1).getFace()) == 14)
                        {
                            life.get(1).setFace("1");
                        }*/
                        for (RummyPlayingCard temp : life) {
                            if(Integer.parseInt(temp.getFace()) == 14)
                            {
                                temp.setFace("1");
                            }
                        }
                        return false;
                    }
                }

            }

            /*if(Integer.parseInt(life.get(life.size()-1).getFace()) == 14)
            {
                life.get(life.size() -1).setFace("1");

            }
            if(Integer.parseInt(life.get(0).getFace()) == 14)
            {
                life.get(0).setFace("1");
            }
            if(Integer.parseInt(life.get(1).getFace()) == 14)
            {
                life.get(1).setFace("1");
            }*/

            for (RummyPlayingCard temp : life) {
                if(Integer.parseInt(temp.getFace()) == 14)
                {
                    temp.setFace("1");
                }
            }

            return true;

        }
    }

    private boolean SetCheck(ArrayList<RummyPlayingCard> set)
    {
        if(set.size() < 3)
        {
            return  false;
        }
        else
        {
            int i =0;
            String check_face = "none";
            Map<String, Boolean> shcd = new HashMap<String, Boolean>();
            shcd.put("s",false);
            shcd.put("h",false);
            shcd.put("c",false);
            shcd.put("d",false);

            while (i < set.size())
            {
                if(isCardJoker(set.get(i)))
                {
                    i = i+1;
                    continue;
                }
                else
                {
                    check_face = set.get(i).getFace();
                    break;
                }
            }

            for(int j =0;j < set.size();j++)
            {
                if(set.get(j).getFace().equalsIgnoreCase(check_face) || isCardJoker(set.get(j)))
                {

                }
                else {
                    return false;
                }
            }

            for (int k=0;k <set.size(); k++)
            {
                if(isCardJoker(set.get(k)))
                {
                    continue;
                }

                if(shcd.get(set.get(k).getSuit()))
                {
                    return false;
                }

                if(!shcd.get(set.get(k).getSuit()))
                {
                    shcd.put(set.get(k).getSuit(),true);
                }
            }

            return true;

        }
    }



    private void addEmptyView() {

        final LinearLayout cardLayout = this.mRummyView.getCard();
        //// setscreensize


        if(!isTablet(getActivity()))
        {
            RummyUtils.setViewWidth((LinearLayout)cardLayout.findViewById(R.id.ll_cardview_main_container), RummyGameRoomCustomScreenLess700.stackCardWidth);
            RummyUtils.setViewWidth((RelativeLayout)cardLayout.findViewById(R.id.card_view_root_layout), RummyGameRoomCustomScreenLess700.stackCardWidth);
            RummyUtils.setViewHeight((RelativeLayout)cardLayout.findViewById(R.id.card_view_root_layout), RummyGameRoomCustomScreenLess700.stackCardHeight);
            RummyUtils.setViewWidth((ImageView)cardLayout.findViewById(R.id.cardImageView), RummyGameRoomCustomScreenLess700.stackCardWidth);
            RummyUtils.setViewHeight((ImageView)cardLayout.findViewById(R.id.cardImageView), RummyGameRoomCustomScreenLess700.stackCardHeight);
            RummyUtils.setViewHeight((ImageView)cardLayout.findViewById(R.id.jokerCardImg), RummyGameRoomCustomScreenLess700.jokerCardHeight);
            RummyUtils.setViewWidth((ImageView)cardLayout.findViewById(R.id.jokerCardImg), RummyGameRoomCustomScreenLess700.jokerCardWidth);
            RummyUtils.setViewWidth((ImageView)cardLayout.findViewById(R.id.cardImageViewSelected), RummyGameRoomCustomScreenLess700.stackCardWidth);
            RummyUtils.setViewHeight((ImageView)cardLayout.findViewById(R.id.cardImageViewSelected), RummyGameRoomCustomScreenLess700.stackCardHeight);
            RummyUtils.setViewWidth((ImageView)cardLayout.findViewById(R.id.autoPlayIv), RummyGameRoomCustomScreenLess700.autoPlayIconSize);
            RummyUtils.setViewHeight((ImageView)cardLayout.findViewById(R.id.autoPlayIv), RummyGameRoomCustomScreenLess700.autoPlayIconSize);
        }else
        {
            RummyUtils.setViewWidth((LinearLayout)cardLayout.findViewById(R.id.ll_cardview_main_container), RummyGameRoomCustomScreenMore700.stackCardWidth);
            RummyUtils.setViewWidth((RelativeLayout)cardLayout.findViewById(R.id.card_view_root_layout), RummyGameRoomCustomScreenMore700.stackCardWidth);
            RummyUtils.setViewHeight((RelativeLayout)cardLayout.findViewById(R.id.card_view_root_layout), RummyGameRoomCustomScreenMore700.stackCardHeight);
            RummyUtils.setViewWidth((ImageView)cardLayout.findViewById(R.id.cardImageView), RummyGameRoomCustomScreenMore700.stackCardWidth);
            RummyUtils.setViewHeight((ImageView)cardLayout.findViewById(R.id.cardImageView), RummyGameRoomCustomScreenMore700.stackCardHeight);
            RummyUtils.setViewHeight((ImageView)cardLayout.findViewById(R.id.jokerCardImg), RummyGameRoomCustomScreenMore700.jokerCardHeight);
            RummyUtils.setViewWidth((ImageView)cardLayout.findViewById(R.id.jokerCardImg), RummyGameRoomCustomScreenMore700.jokerCardWidth);
            RummyUtils.setViewWidth((ImageView)cardLayout.findViewById(R.id.cardImageViewSelected), RummyGameRoomCustomScreenMore700.stackCardWidth);
            RummyUtils.setViewHeight((ImageView)cardLayout.findViewById(R.id.cardImageViewSelected), RummyGameRoomCustomScreenMore700.stackCardHeight);
            RummyUtils.setViewWidth((ImageView)cardLayout.findViewById(R.id.autoPlayIv), RummyGameRoomCustomScreenMore700.autoPlayIconSize);
            RummyUtils.setViewHeight((ImageView)cardLayout.findViewById(R.id.autoPlayIv), RummyGameRoomCustomScreenMore700.autoPlayIconSize);
        }


        this.mRummyView.addCard(this.mRummyView.getCard(),false);
    }

    private void doShow(RummyPlayingCard card) {
        if (this.tableId != null && card != null) {
            RummyCardDiscard request = new RummyCardDiscard();
            request.setEventName("SHOW");
            request.setFace(card.getFace());
            request.setSuit(card.getSuit());
            request.setUuid(RummyUtils.generateUuid());
            request.setTableId(this.tableId);
            request.setNickName(this.userData.getNickName());
            request.setUserId(String.valueOf(this.userData.getUserId()));
            request.setTimeStamp(String.valueOf(System.currentTimeMillis()));
            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.showEventListner);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                RummyTLog.d(TAG, "doShow()" + gameEngineNotRunning.getLocalizedMessage());
            }
        }
    }

    private void launchMeldFragment() {
        hideView(this.mSmartCorrectionView);
        final ArrayList<ArrayList<RummyPlayingCard>> updatedCardGroups = getUpdatedCardGroups();
        checkMeld(updatedCardGroups);
        this.isGameResultsShowing = false;
        clearSelectedCards();
        dismissQuickMenu();
        dismissToolTipView();
        setGroupView(false);
        hideView(this.mGameResultsView);
        this.isMeldFragmentShowing = true;
        RummyMeldCard meldCard = new RummyMeldCard();
        meldCard.msgUuid = this.meldMsgUdid;
        meldCard.tableId = this.tableId;
        meldCard.meldGroup = updatedCardGroups;
        meldCard.dicardCard = getDiscardedCard();
        meldCard.isValidShow = this.opponentValidShow;
        meldCard.jokerCard = this.mJockerCard;
        Button mCancelBtn = (Button) this.mMeldCardsView.findViewById(R.id.meld_cancel_btn);
        ((Button) this.mMeldCardsView.findViewById(R.id.meld_yes_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                RummyTablesFragment.this.dismissToolTipView();
                RummyTablesFragment.this.sendCardsToEngine(updatedCardGroups);
            }
        });
        mCancelBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                RummyTablesFragment.this.removeMeldCardsFragment();
                RummyTablesFragment.this.showDeclareHelpView();
            }
        });

        ((TextView) (this.mMeldCardsView.findViewById(R.id.table_id_tv))).setText(this.mTableId.getText().toString());
        ((TextView) (this.mMeldCardsView.findViewById(R.id.game_id_tv))).setText(this.mGameId);

        handlePopUpCloseBtn(this.mMeldCardsView);
        setMeldCardsView(this.mMeldCardsView, meldCard);
        showView(this.mSubFragment);
        hideSecureBottomView();

        setTableButtonsUI();
    }

    public ArrayList<ArrayList<RummyPlayingCard>> getUpdatedCardGroups() {
        return this.mRummyView.getUpdatedCardsGroup();
    }

    public void sendCardsToEngine(ArrayList<ArrayList<RummyPlayingCard>> updatedCardGroups) {
        if (this.opponentValidShow) {
            sendCardsOnDeclare(updatedCardGroups);
        } else {
            sendMeldCards(updatedCardGroups);
        }
    }

    private void handlePopUpCloseBtn(View view) {
        ((ImageView) view.findViewById(R.id.close_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    RummyTablesFragment.this.hideView(RummyTablesFragment.this.mDialogLayout);
                    RummyTablesFragment.this.removeGameResultFragment();
                    RummyTablesFragment.this.removeMeldCardsFragment();
                    if (RummyTablesFragment.this.getTag() != null) {
                        RummyTablesFragment.this.showQuickAction(RummyTablesFragment.this.getTag());
                    }

                } catch (Exception e) {
                    Log.e("handlePopUpCloseBtn", e + "");
                }
            }
        });
    }

    private void launchGameResultsFragment(RummyEvent event) {
        hideView(this.mSmartCorrectionView);
        this.isGameResultsShowing = true;
        dismissToolTipView();
        hideView(this.mMeldCardsView);
        setGameResultsView(this.mGameResultsView, event);
        handlePopUpCloseBtn(this.mGameResultsView);
        showView(this.mSubFragment);
        setTableButtonsUI();
    }

    private void removeCardsOnMeld(ArrayList<RummyPlayingCard> meldList) {
        for (int i = this.mGroupList.size() - 1; i >= 0; i--) {
            ArrayList<RummyPlayingCard> cardGroup = (ArrayList) this.mGroupList.get(i);
            for (int j = cardGroup.size() - 1; j >= 0; j--) {
                RummyPlayingCard mcard = (RummyPlayingCard) cardGroup.get(j);
                String mCardValue = String.format("%s%s%s", new Object[]{mcard.getSuit(), mcard.getFace(), mcard.getIndex()});
                for (int x = meldList.size() - 1; x >= 0; x--) {
                    RummyPlayingCard selectedCard = (RummyPlayingCard) meldList.get(x);
                    if (String.format("%s%s%s", new Object[]{selectedCard.getSuit(), selectedCard.getFace(), selectedCard.getIndex()}).equalsIgnoreCase(mCardValue)) {
                        cardGroup.remove(j);
                    }
                }
            }
        }
        setGroupView(false);
    }

    public void updateGroupView(ArrayList<ArrayList<RummyPlayingCard>> updatedGroiupList) {
        this.mGroupList.clear();
        this.mGroupList.addAll(updatedGroiupList);
        setGroupView(false);
    }

    private void removeDiscardedCardOnShow(RummyPlayingCard discardedCard) {
     //   this.mSelectedCards.clear();
        String mCardValue = String.format("%s%s-%s", new Object[]{discardedCard.getSuit(), discardedCard.getFace(), discardedCard.getIndex()});

        RummyPlayingCard card;
        for (int i = 0; i < this.mGroupList.size(); i++) {
            for (int k = 0; k < this.mGroupList.get(i).size(); k++) {
                card = this.mGroupList.get(i).get(k);

                if (String.format("%s%s-%s", new Object[]{card.getSuit(), card.getFace(), card.getIndex()}).equalsIgnoreCase(mCardValue)) {
                    try {
                        this.mGroupList.get(i).remove(discardedCard);
                    } catch (Exception e) {
                        Log.e("flow", "EXP: TablesFrag : removeDiscardedCardOnShow -->> " + e.toString());
                    }
                    break;
                }
            }
        }

        this.tempDiscardedCard = discardedCard;
        setGroupView(false);

        this.mGroupList = this.mRummyView.getUpdatedCardsGroup();
        updateCardsView();

    }

    private void showPlaceShowDialog(View v) {
        disableDropButton(this.mDropPlayer);
        this.showDialog = getLeaveTableDialog(v.getContext(), this.mActivity.getString(R.string.place_show_msg));
        this.showDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        ((Button) this.showDialog.findViewById(R.id.yes_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RummyTablesFragment.this.showDialog.dismiss();
                RummyPlayingCard discardedCard = RummyTablesFragment.this.getDiscardedCard();
                RummyTablesFragment.this.removeDiscardedCardOnShow(discardedCard);
               // TablesFragment.this.groupCards();
                RummyTablesFragment.this.clearSelectedCards();
                RummyTablesFragment.this.dismissQuickMenu();
                RummyTablesFragment.this.showHideView(false, RummyTablesFragment.this.mShowBtn, true);
                RummyTablesFragment.this.showHideView(true, RummyTablesFragment.this.mDeclareBtn, false);
                RummyTablesFragment.this.disableDropButton(RummyTablesFragment.this.mDropPlayer);
              //  PlayingCard discardedCard = TablesFragment.this.getDiscardedCard();
                RummyTablesFragment.this.mClosedCard.setVisibility(View.VISIBLE);
                RummyTablesFragment.this.doShow(discardedCard);
              //  TablesFragment.this.removeDiscardedCardOnShow(discardedCard);
            }
        });
        ((Button) this.showDialog.findViewById(R.id.no_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RummyTablesFragment.this.showHideView(true, RummyTablesFragment.this.mShowBtn, false);
                RummyTablesFragment.this.enableView(RummyTablesFragment.this.mShowBtn);
                RummyTablesFragment.this.showDialog.dismiss();
            }
        });
        this.showDialog.show();

        this.showDialog.getWindow().getDecorView().setSystemUiVisibility(
                mActivity.getWindow().getDecorView().getSystemUiVisibility());

        this.showDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    /*private void showDropDialog() {
     *//*   this.dropDialog = getLeaveTableDialog(this.mActivity, this.mActivity.getString(R.string.drop_game_msg));
        this.dropDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        ((Button) this.dropDialog.findViewById(R.id.yes_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TablesFragment.this.dropDialog.dismiss();
                TablesFragment.this.dropPlayer();
            }
        });
        ((Button) this.dropDialog.findViewById(R.id.no_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TablesFragment.this.dropDialog.dismiss();
                TablesFragment.this.enableView(TablesFragment.this.mDropPlayer);
            }
        });
        this.dropDialog.rummy_show();*//*

        this.dropDialog = getLeaveTableDialog(this.mActivity, this.mActivity.getString(R.string.drop_game_msg));
        LinearLayout ll_drop_move_confirmation = this.dropDialog.findViewById(R.id.ll_drop_move_confirmation);
        final CheckBox cb_drop_move = this.dropDialog.findViewById(R.id.cb_drop_move);

        if(this.mTableDetails != null
                && this.mTableDetails.getMaxPlayer().equalsIgnoreCase("6")
                && this.mTableDetails.getTableType().equalsIgnoreCase(RummyUtils.PR_JOKER)
                && !strIsTourneyTable.equalsIgnoreCase("yes"))
        {
            cb_drop_move.setChecked(false);
            isMoveToOtherTable = false;
            ll_drop_move_confirmation.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cb_drop_move.isChecked())
                    {
                        cb_drop_move.setChecked(false);
                        isMoveToOtherTable = false;
                    }
                    else
                    {
                        cb_drop_move.setChecked(true);
                        isMoveToOtherTable = true;
                    }
                }
            });

           // showView(ll_drop_move_confirmation);
        }
        else
        {
            hideView(ll_drop_move_confirmation);
        }

        ((Button) this.dropDialog.findViewById(R.id.yes_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RummyTablesFragment.this.dropDialog.dismiss();
                RummyTablesFragment.this.dropPlayer();
            }
        });
        ((Button) this.dropDialog.findViewById(R.id.no_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RummyTablesFragment.this.dropDialog.dismiss();
                RummyTablesFragment.this.enableView(RummyTablesFragment.this.mDropPlayer);
            }
        });
        this.dropDialog.show();


        this.dropDialog.getWindow().getDecorView().setSystemUiVisibility(
                mActivity.getWindow().getDecorView().getSystemUiVisibility());

        this.dropDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }*/

    //primary dialog for drop & move
    private void showDropDialog() {
        if (isCurrentlyMyTurn) {
            this.dropDialog = new RummyTableConfirmationDialog(this.mActivity,
                    this.mActivity.getString(R.string.drop_game_msg));
        }else{
            this.dropDialog = new RummyTableConfirmationDialog(this.mActivity,
                    this.mActivity.getString(R.string.auto_drop_game_msg));
        }
        if (this.mTableDetails != null
                && this.mTableDetails.getMaxPlayer().equalsIgnoreCase("6")
                && this.mTableDetails.getTableType().equalsIgnoreCase(RummyUtils.PR_JOKER)
                && !isTourneyTable()
                && !this.is_private) {
            dropDialog.setDropMoveCheckBox(true);
            RummyTablesFragment.this.isMoveToOtherTable = true;
            dropDialog.moveConfirmationClickListener(v -> {
                if (dropDialog.isDropAndMoveChecked()) {
                    dropDialog.setDropMoveCheckBox(false);
                    RummyTablesFragment.this.isMoveToOtherTable = false;
                } else {
                    dropDialog.setDropMoveCheckBox(true);
                    RummyTablesFragment.this.isMoveToOtherTable = true;
                }

                RummyTLog.e("vikas", "ismoveTootherTable = " + RummyTablesFragment.this.isMoveToOtherTable);
            });

        } else {
            dropDialog.moveConfirmationClickListener(null);
        }   ///smart seating

        //dropDialog.setTitle("Drop Table?");
        this.dropDialog.setYesClickListener(v -> {
            if (isCurrentlyMyTurn) {
                RummyTablesFragment.this.dropDialog.dismiss();
                RummyTablesFragment.this.dropPlayer();
            } else {
                setAutoDropSetting(true);
            }
        });
        this.dropDialog.setNoAndCloseClickListener(v -> {
            if (isCurrentlyMyTurn) {
                RummyTablesFragment.this.dropDialog.dismiss();
                RummyTablesFragment.this.showView(RummyTablesFragment.this.mDropPlayer);
            } else {
                setAutoDropSetting(false);
            }
        });
        this.dropDialog.show();
    }

    public void searchTable(RummyEvent event,RummyTable rummyTable) {
        if (this.mTableDetails != null) {
            RummySearchTableRequest request = new RummySearchTableRequest();
            request.setCommand("search_join_table");
            request.setTableId(rummyTable.getTable_id());
            if(event != null)
            request.setUuid(event.getMsg_uuid());

            request.setBet(this.mTableDetails.getBet());
            request.setUserId(this.userData.getUserId());
            request.setTableType(this.mTableDetails.getTableType());
            request.setTableCost(this.mTableDetails.getTableCost());
            request.setMaxPlayers(this.mTableDetails.getMaxPlayer());
            request.setConversion(this.mTableDetails.getConversion());
            request.setStreamId(this.mTableDetails.getStreamid());
            request.setStreamName(this.mTableDetails.getStreamname());
            request.setUnique_gamesettings_id(this.mTableDetails.getUnique_gamesettings_id());
            request.setNickName(this.userData.getNickName());
            request.setTableJoinAs("play");
            request.setBuyinamount("0");
            //request.setUnique_gamesettings_id(this.mTableDetails.getUnique_gamesettings_id());
            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.searchTableResponse);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mActivity, R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.d(TAG, "getTableData" + gameEngineNotRunning.getLocalizedMessage());
            }
        }
    }

    private void resetAllPlayers() {
        resetPlayerData(this.mUserPlayerLayout);
        resetPlayerData(this.mSecondPlayerLayout);
        resetPlayerData(this.mThirdPlayerLayout);
        resetPlayerData(this.mFourthPlayerLayout);
        resetPlayerData(this.mFifthPlayerLayout);
        resetPlayerData(this.mSixthPlayerLayout);
        invisibleView(this.mPlayer2Cards);
        invisibleView(this.mPlayer3Cards);
        invisibleView(this.mPlayer4Cards);
        invisibleView(this.mPlayer5Cards);
        invisibleView(this.mPlayer6Cards);

        resetWaitingplayer();

        invisibleView(this.player_2_autoplay_box);
        invisibleView(this.player_3_autoplay_box);
        invisibleView(this.player_4_autoplay_box);
        invisibleView(this.player_5_autoplay_box);
        invisibleView(this.player_6_autoplay_box);
    }

    private void resetOtherPlayers() {
        resetPlayerData(this.mSecondPlayerLayout);
        resetPlayerData(this.mThirdPlayerLayout);
        resetPlayerData(this.mFourthPlayerLayout);
        resetPlayerData(this.mFifthPlayerLayout);
        resetPlayerData(this.mSixthPlayerLayout);
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    private void sendCardsOnDeclare(ArrayList<ArrayList<RummyPlayingCard>> meldGroup) {
        List<RummyMeldBox> mMeldBoxes = new ArrayList();
        Iterator it = meldGroup.iterator();
        while (it.hasNext()) {
            ArrayList<RummyPlayingCard> meldCards = (ArrayList) it.next();
            RummyMeldBox box = new RummyMeldBox();
            box.setMeldBoxes(meldCards);
            mMeldBoxes.add(box);
        }
        RummyMeldReply request = new RummyMeldReply();
        request.setText("200");
        request.setType("+OK");
        request.setUuid(this.meldMsgUdid);
        request.setTableId(this.tableId);
        request.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        request.setMeldBoxes(mMeldBoxes);
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(mActivity, RummyUtils.getObjXMl(request), this.declareListner);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            RummyTLog.d(TAG, "doMelds" + gameEngineNotRunning.getLocalizedMessage());
        }
    }

    private void sendMeldCards(ArrayList<ArrayList<RummyPlayingCard>> meldGroup) {
        if (this.mDiscardedCard != null) {
            this.isMeldRequested = true;
            List<RummyMeldBox> mMeldBoxes = new ArrayList();
            Iterator it = meldGroup.iterator();
            while (it.hasNext()) {
                ArrayList<RummyPlayingCard> meldCards = (ArrayList) it.next();
                RummyMeldBox box = new RummyMeldBox();
                box.setMeldBoxes(meldCards);
                mMeldBoxes.add(box);
            }
            RummyMeldRequest request = new RummyMeldRequest();
            request.setCommand("meld");
            request.setMeldBoxes(mMeldBoxes);
            request.setFace(this.mDiscardedCard.getFace());
            request.setSuit(this.mDiscardedCard.getSuit());
            request.setUuid(RummyUtils.generateUuid());
            request.setTableId(this.tableId);
            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.meldListner);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                RummyTLog.d(TAG, "doMelds" + gameEngineNotRunning.getLocalizedMessage());
            }
        }
    }

    private void handleBackButton(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode != 4) {
                    return false;
                }
                if (RummyTablesFragment.this.mDialogLayout.getVisibility() == View.VISIBLE) {
                    RummyTablesFragment.this.hideView(RummyTablesFragment.this.mDialogLayout);
                    return true;
                } else if (RummyTablesFragment.this.mSubFragment.getVisibility() != View.VISIBLE) {
                    return false;
                } else {
                    if (!((RummyTableActivity) RummyTablesFragment.this.mActivity).isIamBackShowing()) {
                        ((RummyTableActivity) RummyTablesFragment.this.mActivity).showGameTablesLayout(RummyTablesFragment.this.tableId);
                    }
                    RummyTablesFragment.this.hideView(RummyTablesFragment.this.mSubFragment);
                    return true;
                }
            }
        });
    }

    private void getTableExtra() {
        if (this.tableId != null) {
            RummyTableDetailsRequest request = new RummyTableDetailsRequest();
            request.setCommand("get_table_extra");
            request.setTableId(this.tableId);
            request.setUuid(RummyUtils.generateUuid());
            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.tableExtraLisner);
            } catch (RummyGameEngineNotRunning e) {
                Toast.makeText(this.mActivity, R.string.error_restart, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updatedStacks(RummyPickDiscard pickDiscard) {
        if (pickDiscard.getDeck().equalsIgnoreCase("face_down")) {
            Iterator it = this.faceDownCardList.iterator();
            while (it.hasNext()) {
                RummyPlayingCard card = (RummyPlayingCard) it.next();
                if (card.getFace().equalsIgnoreCase(pickDiscard.getFace()) && card.getSuit().equalsIgnoreCase(pickDiscard.getSuit())) {
                    this.faceDownCardList.remove(card);
                    return;
                }
            }
        }
    }

   /* public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        this.mDrawerLayout.closeDrawer(5);
        switch (position) {
            case 1:
                //To Refresh
                return;
            case 2:
                ((TableActivity) this.mActivity).setGameInfo();
                ((TableActivity) this.mActivity).showGameInfo();
                //((TableActivity) this.mActivity).setUpPlayerDiscards();
                return;
            case 3:
                ((TableActivity) this.mActivity).setUpGameSettings();
                return;
            case 4:
                ((TableActivity) this.mActivity).setLastHandEvent();
                return;
            case 5:
                ((TableActivity) this.mActivity).showScoreBoardView();
                return;
            case 6:
                ((TableActivity) this.mActivity).setGameInfo();
                ((TableActivity) this.mActivity).setReportProblem();
                return;
            default:
                return;
        }
    }*/


    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        this.mDrawerLayout.closeDrawer(Gravity.RIGHT);
        switch (position) {
            case 1:
                ((RummyTableActivity) this.mActivity).setGameInfo(isGameStarted,showPlayersJoinedMessage);
                ((RummyTableActivity) this.mActivity).showGameInfo();
                //((TableActivity) this.mActivity).setUpPlayerDiscards();
                return;
            case 2:
                ((RummyTableActivity) this.mActivity).setUpGameSettings();
                return;
            case 3:
                ((RummyTableActivity) this.mActivity).setLastHandEvent();
                return;
            case 4:
                ((RummyTableActivity) this.mActivity).showScoreBoardView();
                return;
            case 5:
                ((RummyTableActivity) this.mActivity).setGameInfo(isGameStarted,showPlayersJoinedMessage);
                ((RummyTableActivity) this.mActivity).setReportProblem();
                return;
            default:
                return;
        }
    }

    public void showLastGameResult(RummyEvent lastHandEvent) {
        if (lastHandEvent != null) {
            launchGameResultsFragment(lastHandEvent);
        } else {
            showGenericDialogTF(this.mActivity, "No entries found.");
        }
    }

    public boolean canShowGameButtons() {
        if (this.mSubFragment.getVisibility() == View.VISIBLE) {
            return false;
        }
        return true;
    }

    public void showDeclareButton()
    {
        showView(bottom_actions);
        disableDropButton(this.mDropPlayer);
        //hideView(mDropPlayer);
        showView(this.mDeclareBtn);
        //showDeclareHelpView();
                        /* if(this.mDeclareBtn.getVisibility() != View.VISIBLE)
        showView(this.mDeclareBtn);*/
    }
    /*private void animateCards(int position, final Event event) {
        if (((TableActivity) this.mActivity).isFromIamBack()) {
            handleSendDealEvent(event);
        } else if (position > this.mDummyVies.size() - 1) {
            handleSendDealEvent(event);
        } else {
            final ImageView iv = (ImageView) this.mDummyVies.get(position);
            iv.setVisibility(View.VISIBLE);
            TransferAnimation an = new TransferAnimation(iv);
            an.setDuration(50);
            an.setListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    TablesFragment.this.hideView(TablesFragment.this.mUserTossCard);
                    LinearLayout linearLayout = TablesFragment.this.mRummyView.getCard();
                    ((ImageView) linearLayout.findViewById(R.id.cardImageView)).setImageResource(R.drawable.rummy_closedcard);
                    TablesFragment.this.hideView(iv);

                    TablesFragment.this.mRummyView.addCard(linearLayout);
                    if (TablesFragment.this.count < TablesFragment.this.mDummyVies.size()) {
                        TablesFragment.this.count++;
                        TablesFragment.this.animateCards(TablesFragment.this.count, event);
                        return;
                    }
                    TablesFragment.this.count = 0;
                    TablesFragment.this.clearAnimationData();
                    TablesFragment.this.handleSendDealEvent(event);
                }
            });
            an.setDestinationView(this.mUserTossCard).animate();
        }
    }*/

    private void animateCards(int position, final RummyEvent event) {
        if (((RummyTableActivity) this.mActivity).isFromIamBack()) {
            RummyTablesFragment.this.count = 0;
            handleSendDealEvent(event);
            Log.e("gopal", "card animation calling i am from back");
        } else if (position > this.mDummyVies.size() - 1) {
            Log.e("gopal", "card animation calling plosition > mDummyVies");
            RummyTablesFragment.this.count = 0;
            handleSendDealEvent(event);
        } else {
            Log.e("gopal", "card animation calling else part");
            final ImageView iv = (ImageView) this.mDummyVies.get(position);
            iv.setVisibility(View.VISIBLE);
            RummyTransferAnimation an = new RummyTransferAnimation(iv);
            an.setDuration(50);
            an.setListener(new RummyAnimationListener() {
                public void onAnimationStart(RummyAnimation animation) {
                }

                public void onAnimationEnd(RummyAnimation animation) {
                    RummyTablesFragment.this.hideView(RummyTablesFragment.this.mUserTossCard);
                    LinearLayout linearLayout = RummyTablesFragment.this.mRummyView.getCard();
                    ((ImageView) linearLayout.findViewById(R.id.cardImageView)).setImageResource(R.drawable.rummy_closedcard);
                    RummyTablesFragment.this.hideView(iv);

                    RummyTablesFragment.this.mRummyView.addCard(linearLayout,false);
                    if (RummyTablesFragment.this.count < RummyTablesFragment.this.mDummyVies.size()) {
                        RummyTablesFragment.this.count++;
                        RummyTablesFragment.this.animateCards(RummyTablesFragment.this.count, event);
                        return;
                    }
                    RummyTablesFragment.this.count = 0;
                    RummyTablesFragment.this.clearAnimationData();
                    RummyTablesFragment.this.handleSendDealEvent(event);
                }
            });
            an.setDestinationView(this.mUserTossCard).animate();
        }
    }

    @Subscribe
    public void onMessageEvent(RummyGameEvent event) {
        if (event.name().equalsIgnoreCase("SERVER_DISCONNECTED")) {
            cancelTimers();
        }
    }

    public void cancelTimers() {
        hideQuickAction();
        cancelTimer(this.playerTurnOutTimer);
        cancelTimer(this.mGameScheduleTimer);
        cancelTimer(this.meldTimer);
    }

    private void setUpPlayerCardsUI(RummyGamePlayer player) {
        String seat;
        if (this.mGamePlayerMap.get(player.getUser_id()) != null) {
            player.setPlayerlevel(((RummyGamePlayer) this.mGamePlayerMap.get(player.getUser_id())).getPlayerlevel());
        }
        if (player.getSeat() != null) {
            seat = player.getSeat();
        } else {
            seat = "1";
        }
        switch (Integer.parseInt(seat)) {
            case 2:
                showView(this.mPlayer2Cards);
                return;
            case 3:
                showView(this.mPlayer3Cards);
                return;
            case 4:
                showView(this.mPlayer4Cards);
                return;
            case 5:
                showView(this.mPlayer5Cards);
                return;
            case 6:
                showView(this.mPlayer6Cards);
                return;
            default:
                return;
        }
    }

    private void sendTurnUpdateMessage(boolean isYoursTurn) {
        try {
            Intent intent = new Intent("TURN_UPDATE_EVENT");
            intent.putExtra("turn_update", isYoursTurn);
            LocalBroadcastManager.getInstance(this.mActivity).sendBroadcast(intent);
        } catch (Exception e) {
            RummyTLog.e(TAG, "Error : sendTurnUpdateMessage() :" + e.getLocalizedMessage());
        }
    }

    private void checkMeld(ArrayList<ArrayList<RummyPlayingCard>> meldGroup) {
        Log.d(TAG, "Inside checkMeld************************************");
        meldGroup = checkDiscardedCardIfPresent(meldGroup);

        List<RummyMeldBox> mMeldBoxes = new ArrayList();
        Iterator it = meldGroup.iterator();
        while (it.hasNext()) {
            ArrayList<RummyPlayingCard> meldCards = (ArrayList) it.next();
            RummyMeldBox box = new RummyMeldBox();
            box.setMeldBoxes(meldCards);
            mMeldBoxes.add(box);
        }
        RummyMeldRequest request = new RummyMeldRequest();
        request.setCommand("check_meld");
        request.setMeldBoxes(mMeldBoxes);
        request.setFace("null");
        request.setSuit("null");
        request.setUuid(RummyUtils.generateUuid());
        request.setTableId(this.tableId);
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(mActivity, RummyUtils.getObjXMl(request), this.checkMeldListner);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            RummyTLog.d(TAG, "doMelds" + gameEngineNotRunning.getLocalizedMessage());
        }
    }

    private ArrayList<ArrayList<RummyPlayingCard>> checkDiscardedCardIfPresent(ArrayList<ArrayList<RummyPlayingCard>> meldGroup) {
        try {
            int flag = 0;
            String discard = this.mDiscardedCard.getFace() + this.mDiscardedCard.getSuit();

            for (int i = 0; i < meldGroup.size(); i++) {
                for (int k = 0; k < meldGroup.get(i).size(); k++) {
                    flag++;
                }
            }

            Log.d("local", "Total cards: " + flag);

            if (flag > 13) {
                for (int i = 0; i < meldGroup.size(); i++) {
                    for (int k = 0; k < meldGroup.get(i).size(); k++) {
                        flag++;
                        if (discard.equalsIgnoreCase(meldGroup.get(i).get(k).getFace() + meldGroup.get(i).get(k).getSuit())) {
                            if (meldGroup.get(i).size() > 1)
                                meldGroup.get(i).remove(k);
                            else if (meldGroup.get(i).size() == 1)
                                meldGroup.remove(i);

                            return meldGroup;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: checkDiscardedCardIfPresent(TablesFrag) -->> " + e.toString());
        }
        return meldGroup;
    }


    // TOURNAMENT METHODS //////////////////////////////////////////////////////////////////////////////////

    private void checkGameType() {
        try {
            if (getArguments().getString("gameType") != null && getArguments().getString("gameType").equalsIgnoreCase("tournament")) {
                levelTimerLayout.setVisibility(View.VISIBLE);
                normal_game_bar.setVisibility(View.GONE);
                tourneyBar.setVisibility(View.VISIBLE);

                tourney_expanded_layout.post(new Runnable() {
                    @Override
                    public void run() {
                        tourneyInfoMaxHeight = tourney_expanded_layout.getHeight();
                        animateTourneyInfo(false);
                    }
                });

                if (getArguments().getString("tourneyId") != null)
                    this.mTourneyId = getArguments().getString("tourneyId");

                RummyTLog.e(TAG, "Tourney ID: " + this.mTourneyId);
                this.tid_tourney_tv.setText(this.mTourneyId);
            } else {
                levelTimerLayout.setVisibility(View.GONE);
                normal_game_bar.setVisibility(View.VISIBLE);
                tourneyBar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: checkGameType-->> " + e.toString());
        }
    }

    private void checkTournament() {
        try {
            if (this.strIsTourneyTable.equalsIgnoreCase("yes")) {
                levelTimerLayout.setVisibility(View.VISIBLE);
                normal_game_bar.setVisibility(View.GONE);
                tourneyBar.setVisibility(View.VISIBLE);
                showPlayerView();
                tourney_expanded_layout.post(new Runnable() {
                    @Override
                    public void run() {
                        tourneyInfoMaxHeight = tourney_expanded_layout.getHeight();
                        animateTourneyInfo(false);
                        Rect rect = new Rect();
                        tourney_expanded_layout.getLocalVisibleRect(rect);
                        tourneyInfoMaxHeight = Math.max(tourneyInfoMaxHeight,Math.abs(rect.bottom-rect.top));
                    }
                });

                Log.d(TAG, "Tourney ID: " + this.mTourneyId);
                this.tid_tourney_tv.setText(this.mTourneyId);
            } else {
                levelTimerLayout.setVisibility(View.GONE);
                normal_game_bar.setVisibility(View.VISIBLE);
                tourneyBar.setVisibility(View.GONE);
            }



        } catch (Exception e) {
            Log.e(TAG, "EXP: checkGameType-->> " + e.toString());
        }
    }

    private void setLevelTimer() {
        try {
            if (this.levelTimer != null)
                this.levelTimer.cancel();

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa");
            level_number_tv.setText(this.mLevelsResponse.getLeveldetails());

            if (this.mLevelsResponse.getEntry().equalsIgnoreCase("0") || this.mLevelsResponse.getEntry().equalsIgnoreCase("0.0")) {
                this.tourney_type_tv.setText("FREE TOURNEY");
                this.entry_tourney_tv.setText("Free");
            } else {
                this.tourney_type_tv.setText("CASH TOURNEY");
                this.entry_tourney_tv.setText(this.mLevelsResponse.getEntry());
            }

            if (this.mLevelsResponse.getFinalprize().equalsIgnoreCase("true"))
                this.tourney_prize_tv.setText(this.mLevelsResponse.getPrize());
            else
                this.tourney_prize_tv.setText("TBA");

            this.rebuy_tourney_tv.setText(this.mLevelsResponse.getNextlevelrebuyin());
            this.game_level_tv.setText(this.mLevelsResponse.getCurrentlevelminimumchips() + "/" + this.mLevelsResponse.getNextlevelminchips());

            Date start = new Date();
            start = sdf.parse(sdf.format(start));
            Date end = sdf.parse(RummyUtils.convertTimeStampToAnyDateFormat(this.mLevelsResponse.getLeveltimer(), "hh:mm:ss aa"));

            long millis = end.getTime() - start.getTime();
            RummyTLog.e("vikas", "level timer startdate =" + start.toString());
            RummyTLog.e("vikas", "level timer endate =" + end.toString());
            RummyTLog.e("vikas", "level timer in millies =" + millis);

            RummyTablesFragment.this.levelTimer = new CountDownTimer(millis, 1000) {

                public void onTick(long millisUntilFinished) {
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);

                    if (minutes >= 1)
                        millisUntilFinished = millisUntilFinished - (minutes * (1000 * 60));
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                    String strMins = "00", strSeconds = "00";
                    if (String.valueOf(minutes).length() == 1)
                        strMins = "0" + String.valueOf(minutes);
                    else
                        strMins = String.valueOf(minutes);

                    if (String.valueOf(seconds).length() == 1)
                        strSeconds = "0" + String.valueOf(seconds);
                    else
                        strSeconds = String.valueOf(seconds);

                    //   TLog.e("vikas","level timer ="+strMins + ":" + strSeconds);
                    RummyTablesFragment.this.levelTimerValue.setText(strMins + ":" + strSeconds);
                }

                public void onFinish() {
                    //Log.d("local","done!");
                }

            }.start();
        } catch (Exception e) {
            Log.e("vikas", "EXP: setLevelTimer-->> " + e.toString());
        }
    }

    private void getTournamentDetails() {
        try {
            RummyTLog.e("vikas", "Getting Tournament details");
            RummyTournamentsDetailsRequest request = new RummyTournamentsDetailsRequest();
            request.setCommand("get_tournament_details");
            request.setUuid(RummyUtils.generateUuid());
            request.setTournament_id(this.mTourneyId);

            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.tournamentsDetailsListener);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mActivity.getApplicationContext(), R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.e(TAG, "getTourneyDetails" + gameEngineNotRunning.getLocalizedMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: getTourneyDetails-->> " + e.toString());
        }
    }

    private void getLevelTimer() {
        try {
            RummyTournamentsDetailsRequest request = new RummyTournamentsDetailsRequest();
            request.setCommand("get_level_timer");
            request.setUuid(RummyUtils.generateUuid());
            request.setTournament_id(this.mTourneyId);

            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.levelsTimerListener);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mActivity.getApplicationContext(), R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.e(TAG, "get_level_timer" + gameEngineNotRunning.getLocalizedMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: get_level_timer-->> " + e.toString());
        }
    }

    private void updatePlayersRank() {
        try {
            for (int i = 0; i < this.mPlayerBoxesAll.size(); i++) {
                ((TextView) (this.mPlayerBoxesAll.get(i).findViewById(R.id.player_rank_tv))).setText("");
                ((TextView) (this.mPlayerBoxesAll.get(i).findViewById(R.id.player_rank_tv))).setVisibility(View.GONE);
                ((LinearLayout) this.mPlayerBoxesAll.get(i).findViewById(R.id.ll_player_rank_square)).setVisibility(View.GONE);
            }

            if (this.strIsTourneyTable.equalsIgnoreCase("yes") && this.mPlayersRank != null && this.mPlayersRank.getTournamentId().equalsIgnoreCase(this.mTourneyId)) {
                for (int i = 0; i < this.mPlayersList.size(); i++) {
                    if (this.mPlayerBoxesForRanks.size() > 0)
                        setUpPlayerRank(this.mPlayerBoxesForRanks.get(this.mPlayersList.get(i).getUser_id()), this.mPlayersList.get(i));

                    if (this.mPlayersList.get(i).getUser_id().equalsIgnoreCase(this.userData.getUserId()))
                        this.rank_tourney_tv.setText(this.mPlayersList.get(i).getRank() +"/"+this.mPlayersRank.getHighest_rank());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: updatePlayersRank-->> " + e.toString());
        }
    }

    /*private void updatePlayersRank() {
        try {
            for (int i = 0; i < this.mPlayerBoxesAll.size(); i++) {
                if (this.mPlayerBoxesAll.get(i) == this.mSecondPlayerLayout) {
                    View view = (View) this.mSecondPlayerLayout;
                    invisibleView(view.findViewById(R.id.ll_player_rank_square));
                    ((TextView) (view.findViewById(R.id.player_rank_tv))).setText("");
                    ((TextView) (view.findViewById(R.id.player_rank_tv))).setVisibility(View.GONE);
                } else if (this.mPlayerBoxesAll.get(i) == this.mThirdPlayerLayout) {
                    View view = (View) this.mThirdPlayerLayout;
                    invisibleView(view.findViewById(R.id.ll_player_rank_square));
                    ((TextView) (view.findViewById(R.id.player_rank_tv))).setText("");
                    ((TextView) (view.findViewById(R.id.player_rank_tv))).setVisibility(View.GONE);
                } else if (this.mPlayerBoxesAll.get(i) == this.mFourthPlayerLayout) {
                    View view = (View) this.mFourthPlayerLayout;
                    invisibleView(view.findViewById(R.id.ll_player_rank_square));
                    ((TextView) (view.findViewById(R.id.player_rank_tv))).setText("");
                    ((TextView) (view.findViewById(R.id.player_rank_tv))).setVisibility(View.GONE);
                } else if (this.mPlayerBoxesAll.get(i) == this.mFifthPlayerLayout) {
                    View view = (View) this.mFifthPlayerLayout;
                    invisibleView(view.findViewById(R.id.ll_player_rank_square));
                    ((TextView) (view.findViewById(R.id.player_rank_tv))).setText("");
                    ((TextView) (view.findViewById(R.id.player_rank_tv))).setVisibility(View.GONE);
                } else if (this.mPlayerBoxesAll.get(i) == this.mSixthPlayerLayout) {
                    View view = (View) this.mSixthPlayerLayout;
                    invisibleView(view.findViewById(R.id.ll_player_rank_square));
                    ((TextView) (view.findViewById(R.id.player_rank_tv))).setText("");
                    ((TextView) (view.findViewById(R.id.player_rank_tv))).setVisibility(View.GONE);
                } else if (this.mPlayerBoxesAll.get(i) == this.mUserPlayerLayout) {
                    View view = (View) this.mUserPlayerLayout;
                    hideView(this.player_1_rank_layout);
                    hideView(view.findViewById(R.id.ll_player_rank_square));
                    ((TextView) (view.findViewById(R.id.player_rank_tv))).setText("");
                    ((TextView) (view.findViewById(R.id.player_rank_tv))).setVisibility(View.GONE);
                }

                // ((LinearLayout) (this.mPlayerBoxesAll.get(i).findViewById(R.id.player_rank_layout_root))).setVisibility(View.INVISIBLE);

            }

            if (this.strIsTourneyTable.equalsIgnoreCase("yes") && this.mPlayersRank != null && this.mPlayersRank.getTournamentId().equalsIgnoreCase(this.mTourneyId)) {
                for (int i = 0; i < this.mPlayersList.size(); i++) {
                    if (this.mPlayerBoxesForRanks.size() > 0) {
                        if (this.mPlayerBoxesForRanks.get(this.mPlayersList.get(i).getNick_name()) == this.mSecondPlayerLayout) {
                            setUpPlayerRank(this.mPlayerBoxesForRanks.get(this.mPlayersList.get(i).getNick_name()), this.mPlayersList.get(i), this.mSecondPlayerLayout);
                        } else if (this.mPlayerBoxesForRanks.get(this.mPlayersList.get(i).getNick_name()) == this.mThirdPlayerLayout) {
                            setUpPlayerRank(this.mPlayerBoxesForRanks.get(this.mPlayersList.get(i).getNick_name()), this.mPlayersList.get(i), this.mThirdPlayerLayout);
                        } else if (this.mPlayerBoxesForRanks.get(this.mPlayersList.get(i).getNick_name()) == this.mFourthPlayerLayout) {
                            setUpPlayerRank(this.mPlayerBoxesForRanks.get(this.mPlayersList.get(i).getNick_name()), this.mPlayersList.get(i), this.mFourthPlayerLayout);
                        } else if (this.mPlayerBoxesForRanks.get(this.mPlayersList.get(i).getNick_name()) == this.mFifthPlayerLayout) {
                            setUpPlayerRank(this.mPlayerBoxesForRanks.get(this.mPlayersList.get(i).getNick_name()), this.mPlayersList.get(i), this.mFifthPlayerLayout);
                        } else if (this.mPlayerBoxesForRanks.get(this.mPlayersList.get(i).getNick_name()) == this.mSixthPlayerLayout) {
                            setUpPlayerRank(this.mPlayerBoxesForRanks.get(this.mPlayersList.get(i).getNick_name()), this.mPlayersList.get(i), this.mSixthPlayerLayout);
                        }


                    }


                    if (this.mPlayersList.get(i).getNick_name().equalsIgnoreCase(this.userData.getNickName())) {
                        //this.rank_tourney_tv.setText(this.mPlayersList.get(i).getRank());
                        this.rank_tourney_tv.setText(this.mPlayersList.get(i).getRank() +"/"+this.mPlayersRank.getHighest_rank());
                        showView(player_1_rank_layout);
                        setUpPlayerRank(mUserPlayerLayout, this.mPlayersList.get(i), this.mUserPlayerLayout);

                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: updatePlayersRank-->> " + e.toString());
        }
    }*/

    private void checkTourneyBalance() {
        try {
            Iterator it = RummyUtils.tableDetailsList.iterator();
            while (it.hasNext()) {
                RummyEvent event = (RummyEvent) it.next();
                if (event.getEventName().equalsIgnoreCase("TOURNEY_BALANCE")) {
                    List<RummyTourney> listT = event.getTourneys();

                    for (RummyTourney t : listT) {
                        if (t.getTournament_id().equalsIgnoreCase(this.mTourneyId))
                            this.balance_tourney_tv.setText(t.getTourney_inplay());
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: checkTourneyBalance-->> " + e.toString());
        }
    }

    private void showGenericDialogWithMessage(String message, final String action) {
        final Dialog dialog = new Dialog(mActivity, R.style.DialogTheme);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rummy_dialog_generic);
        dialog.setCanceledOnTouchOutside(false);

        TextView dialog_msg_tv = (TextView) dialog.findViewById(R.id.dialog_msg_tv);
        Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);

        dialog_msg_tv.setText(message);

        if (action.equalsIgnoreCase("disqualified")) {
            RummyTablesFragment.this.disqualifyDialog = dialog;
        }
     /*   Handler mainHandler = new Handler(getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Log.e("gopal", "end tournament Called");
                if (dialog != null)
                    dialog.dismiss();
                if (TablesFragment.this.mActivity != null && !TablesFragment.this.mActivity.isFinishing())
                    TablesFragment.this.mActivity.finish();
            }
        };
        mainHandler.postDelayed(myRunnable, 3000);*/

        ok_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.show();

      /*  dialog.getWindow().getDecorView().setSystemUiVisibility(
                mActivity.getWindow().getDecorView().getSystemUiVisibility());

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);*/
    }

    private void displayTourneyResults(List<RummyGamePlayer> tourneyResults) {
       /* final Dialog dialog = new Dialog(getContext(), R.style.DialogTheme);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rummy_dialog_tournament_results);
        dialog.setCanceledOnTouchOutside(true);

        ListView tournament_results = (ListView) dialog.findViewById(R.id.tournament_results);
        ImageView popUpCloseBtn = (ImageView) dialog.findViewById(R.id.popUpCloseBtn);
        popUpCloseBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TourneyResultsAdapter tournamentsResultsAdapter = new TourneyResultsAdapter(getContext(), tourneyResults);
        tournament_results.setAdapter(tournamentsResultsAdapter);
        if (tournamentsResultsAdapter != null) {
            tournamentsResultsAdapter.notifyDataSetChanged();
        }
        if (isAdded()) {
            dialog.rummy_show();

            dialog.getWindow().getDecorView().setSystemUiVisibility(
                    mActivity.getWindow().getDecorView().getSystemUiVisibility());

            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
*/

        final Dialog dialog = new Dialog(getContext(), R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rummy_dialog_tournament_results);
        dialog.setCanceledOnTouchOutside(true);

        ListView tournament_results = (ListView) dialog.findViewById(R.id.tournament_results);
        ImageView popUpCloseBtn = (ImageView) dialog.findViewById(R.id.popUpCloseBtn);
        popUpCloseBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        RummyTourneyResultsAdapter tournamentsResultsAdapter = new RummyTourneyResultsAdapter(getContext(), tourneyResults);
        tournament_results.setAdapter(tournamentsResultsAdapter);
        if (tournamentsResultsAdapter != null) {
            tournamentsResultsAdapter.notifyDataSetChanged();
        }
        dialog.show();
    }

    private void removePlayerLevelFromBox(String nickName) {
        String name = "";
        for (int i = 0; i < this.mPlayerBoxesAll.size(); i++) {
            name = ((TextView) (this.mPlayerBoxesAll.get(i).findViewById(R.id.player_name_tv))).getText().toString();

            if (name.equalsIgnoreCase(nickName)) {
                ((TextView) (this.mPlayerBoxesAll.get(i).findViewById(R.id.player_rank_tv))).setText("");
                ((TextView) (this.mPlayerBoxesAll.get(i).findViewById(R.id.player_rank_tv))).setVisibility(View.GONE);
                break;
            }
        }
    }

    private void showRebuyinDialog(final RummyEvent event) {
        if (this.buyInTimerTourney != null) {
            this.buyInTimerTourney.cancel();
        }
        final Dialog dialog = new Dialog(mActivity, R.style.DialogTheme);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rummy_dialog_leave_table);
        dialog.setCanceledOnTouchOutside(false);

        final TextView dialog_msg_tv = (TextView) dialog.findViewById(R.id.dialog_msg_tv);
        Button yes_btn = (Button) dialog.findViewById(R.id.yes_btn);
        Button no_btn = (Button) dialog.findViewById(R.id.no_btn);

        //final String message = "As you do not have sufficient chips for next game, you have to rebuy. " + event.getBuyin_amount() + " cash chips will be deducted. Please click YES to rebuy.";
        final String message = "You do not have enough balance for next game, you have to rebuy. " + event.getBuyin_amount() + " cash chips will be deducted. Please click YES to rebuy.";
        //long millis = Integer.parseInt(event.getMeldTimeOut()) * 1000;

        long millis = 25000;

        if(event.getMeldTimeOut().contains("."))
        {
            String[] separated = event.getMeldTimeOut().split(".");
            millis = Integer.parseInt(separated[0]) * 1000; ;

        }
        else
        {
            millis = Integer.parseInt(event.getMeldTimeOut()) * 1000;

        }

        Log.e("PARTH","TIMER "+millis);
        Log.e("PARTH","MELD TIMEOUT "+event.getMeldTimeOut());

        this.buyInTimerTourney = new CountDownTimer(millis, 1000) {

            public void onTick(long millisUntilFinished) {
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                dialog_msg_tv.setText(message + "\nYou have " + seconds + " seconds left.");

                Log.e("PARTH","SECONDS"+seconds);

            }

            public void onFinish() {
                clearData();
                clearSelectedCards();
                clearStacks();
                clearOtherPlayersData();
                Log.e("PARTH","ON finish called");


                try {
                    if (mActivity != null && !mActivity.isFinishing() && dialog != null) {

                        if (RummyTablesFragment.this.levelTimer != null) {
                            RummyTablesFragment.this.levelTimer.cancel();
                        }
                        if (RummyTablesFragment.this.disqualifyDialog != null) {
                            RummyTablesFragment.this.disqualifyDialog.dismiss();
                            RummyTablesFragment.this.disqualifyDialog = null;
                        }
                        dialog.dismiss();

                    }
                } catch (final IllegalArgumentException e) {
                    // Handle or log or ignore
                    e.printStackTrace();
                } catch (final Exception e) {
                    // Handle or log or ignore
                    e.printStackTrace();
                }
                RummyTablesFragment.this.isTourneyEnd = true;
            }

        }.start();

        yes_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doRebuyin(event);
                if (RummyTablesFragment.this.buyInTimerTourney != null) {
                    RummyTablesFragment.this.buyInTimerTourney.cancel();
                }

                if (RummyTablesFragment.this.disqualifyDialog != null) {
                    RummyTablesFragment.this.disqualifyDialog.dismiss();
                    RummyTablesFragment.this.disqualifyDialog = null;
                }

                clearData();
                clearSelectedCards();
                clearStacks();

                RummyTablesFragment.this.mRummyView.removeViews();

                dialog.dismiss();
            }
        });

        no_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   clearData();
                clearSelectedCards();
                clearStacks();
                TablesFragment.this.isTourneyEnd = true;

                try {
                    if (mActivity != null && !mActivity.isFinishing() && dialog != null) {
                        dialog.dismiss();
                        TablesFragment.this.showGenericDialogWithMessage("This tournament has ended !", "end_tournament");
                    }
                } catch (Exception e) {
                    // Handle or log or ignore
                }*/

                if (RummyTablesFragment.this.buyInTimerTourney != null) {
                    RummyTablesFragment.this.buyInTimerTourney.cancel();
                }
                clearData();
                clearSelectedCards();
                clearStacks();

                dialog.dismiss();
                RummyTablesFragment.this.isTourneyEnd = true;
            }
        });



        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        dialog.getWindow().getDecorView().setSystemUiVisibility(
                mActivity.getWindow().getDecorView().getSystemUiVisibility());

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    private void doRebuyin(RummyEvent event) {
        try {
            RummyTourneyApiHelper.rebuyin(this.mTourneyId, event.getTo_level(), event.getValue_rake(), new RummyApiCallHelper.ApiResponseListener() {
                @Override
                public void response(RummyApiResult<JSONObject> result) {
                    if (!result.isLoading()){
                        if(result.isSuccess()){
                            reBuyInOrderId = result.getResult().optString("order_id", "");
                            reBuyInAmount = event.getValue_rake();
                            reBuyInLevel = event.getTo_level();
                            RummyTournamentsDetailsRequest request = new RummyTournamentsDetailsRequest();
                            request.setCommand("tournament_rebuyin");
                            request.setUuid(RummyUtils.generateUuid());
                            request.setAmount(event.getValue_rake());
                            request.setLevel(event.getTo_level());
                            request.setOrderId(reBuyInOrderId);
                            request.setTournament_id(RummyTablesFragment.this.mTourneyId);
    
                            try {
                                RummyGameEngine.getInstance();
                                RummyGameEngine.sendRequestToEngine(RummyTablesFragment.this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), RummyTablesFragment.this.rebuyinListener);
                            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                                Toast.makeText(RummyTablesFragment.this.mActivity.getApplicationContext(), R.string.error_restart, Toast.LENGTH_SHORT).show();
                                RummyTLog.e(TAG, "doRebuyin" + gameEngineNotRunning.getLocalizedMessage());
                            }
                        }else {
                            RummyTLog.e(TAG, "doRebuyin" + result.getErrorMessage());
                        }
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "EXP: doRebuyin-->> " + e.toString());
        }
    }

    private void leaveTournament() {
        Log.e("vikas", "enter leave tournament function");
        if (RummyApplication.getInstance().getJoinedTableIds().size() == 1) {
            Log.e("vikas", "in Leave tournament function table size == 1");
            ((RummyTableActivity) this.mActivity).setIsBackPressed(true);
        }
        RummyTLog.e("vikas", "enter leave tournament function with tournetid=" + this.mTourneyId);
        showLoadingDialog(this.mActivity);
        RummyLeaveTableRequest request = new RummyLeaveTableRequest();
        request.setEventName("leave_tournament");
        request.setUuid(RummyUtils.generateUuid());
        request.setTournament_id(this.mTourneyId);
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(this.mActivity.getApplicationContext(), RummyUtils.getObjXMl(request), this.leaveTableListner);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            RummyTLog.d(TAG, "leave_tournament" + gameEngineNotRunning.getLocalizedMessage());
        }
    }

    private void doMeldTemp(RummyEngineRequest engineRequest) {
        showView(this.mDropPlayer);

        ((RummyTableActivity) this.mActivity).closeSettingsMenu();
        String successUserId = engineRequest.getSucessUserId();
        String successUserName = engineRequest.getSucessUserName();
        if (successUserId != null && successUserName != null) {
            if (successUserId.equalsIgnoreCase(this.userData.getUserId())) {
                showView(this.mClosedCard);
                setUserOptionsOnValidShow();
                return;
            }
            showView(this.mClosedCard);
            this.meldTimeOut = engineRequest.getTimeout();
            this.opponentValidShow = true;
            RummySoundPoolManager.getInstance().playSound(R.raw.rummy_meld);
            ((RummyTableActivity) this.mActivity).dismissScoreBoard();
            removeGameResultFragment();
            this.canLeaveTable = false;
            this.isPlacedShow = true;
            this.isTossEventRunning = false;
            this.isCardsDistributing = false;
            this.meldMsgUdid = engineRequest.getMsg_uuid();
            startMeldTimer(Integer.parseInt(RummyUtils.formatString(engineRequest.getTimeout())), String.format("%s placed valid show, %s", new Object[]{successUserName, this.mActivity.getString(R.string.meld_success_msg)+" "}), this.mGameShecduleTv);
            if (getTotalCards() > 0) {
                invisibleView(this.mShowBtn);
                disableView(this.mShowBtn);
                showView(this.mDeclareBtn);
                enableView(this.mDeclareBtn);
                if (!((RummyTableActivity) this.mActivity).isIamBackShowing()) {
                    showDeclareHelpView();
                    return;
                }
                return;
            }
            //invisibleView(this.mDeclareBtn);      // Before
            showView(this.mDeclareBtn);             // After
            showView(this.mShowBtn);
            disableView(this.mShowBtn);
            disableDropButton(this.mDropPlayer);
            hideView(this.mAutoDropPlayer);
            disableView(this.sortCards);
        }

        RummyUtils.MELD_REQUEST = null;
    }

    private void doShowTemp(RummyEvent event) {
        showView(this.mDropPlayer);
        enableDropButton(this.mDropPlayer);
     //   showView(this.sortCards);
        showView(this.mShowBtn);

        Log.d(TAG, "Inside SHOW************************************TEMP");
        hideView(this.mReshuffleView);
        sendTurnUpdateMessage(false);
        ((RummyTableActivity) this.mActivity).closeSettingsMenu();
        if (this.strIsTourneyTable.equalsIgnoreCase("yes") && this.playerCards.size() == 0)
            clearData();
        else
            handleShowEvent(event);

        RummyUtils.SHOW_EVENT = null;
    }

    private void showInitialButtons() {
        Log.e(TAG, "SHOWING INITIAL BUTTONS ++++++++++++++++++++++");
        showView(sortCards);
        showView(mDropPlayer);
        showView(mShowBtn);
        hideView(mDeclareBtn);
    }

    private void removeSeatingAnim() {
        ((RummyTableActivity) this.mActivity).resetPlayerIconsOnTableBtn(this.tableId);
        // this.keepDefaultSeatingIcons();
    }

    private void resetUserSeatingTimer() {
        //psp
        // ((TableActivity) TablesFragment.this.mActivity).updateTableUserTurnCount(TablesFragment.this.getTag(),0,false); /*Kept false to hide the view*/
        //psp
    }

    public void updateTableTypeView() {
        //psp
        if ( mTableDetails != null) {
            if (mTableDetails.getTableCost().contains("FUN")) {
                //this.iv_ruppe_icon_top_bar_game_room.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.rummy_ic_topbar_funchips));
                ((RummyTableActivity) RummyTablesFragment.this.mActivity).updateTableType(RummyTablesFragment.this.getTag(), "FUN", mTableDetails.getBet(), RummyUtils.formatTableName(mTableDetails.getTableType()), mTableDetails.getTournament_table());

            } else {
                //this.iv_ruppe_icon_top_bar_game_room.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.rummy_ic_freegames_icon_white));
                ((RummyTableActivity) RummyTablesFragment.this.mActivity).updateTableType(RummyTablesFragment.this.getTag(), "CASH", mTableDetails.getBet(), RummyUtils.formatTableName(mTableDetails.getTableType()), mTableDetails.getTournament_table());
            }
        }
        //psp

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




    public void showWinnerFragment(final RelativeLayout linearLayout, final View winnerView, final View searchView, View splitRejectedView, RummyEvent winnerEvent, RummyTableDetails tableDetails) {
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
            /*if (searchView.getVisibility() == View.VISIBLE) {
               RummyBaseFragment.this.invisibleView(winnerView);
               return;
            }
            RummyBaseFragment.this.hideView(winnerView);
            RummyBaseFragment.this.hideView(linlevelTimerLayoutearLayout);*/

                hideView(winnerView);
                joinAnotherGame(winnerEvent,"Do you want to join 1 more game?");
            }
        });
    }


    public void sendJoinTableDataToServer(final RummyTable tableToJoin, final String buyInAmount, final String tableId, final int call_from_type, final RummyEvent event, final String msgUdid, final RummyTableDetails tableDetails) // 1 for eliminate and 2 for winner
    {

        RummyUtils.showLoadingDialog(mActivity);
        try {
            final String TOKEN = RummyPrefManager.getString(mActivity, ACCESS_TOKEN_REST, "");

            RummyTLog.e("token =",TOKEN);

            String url = RummyUtils.getApiSeverAddress()+ RummyUtils.gameJoinPR;
            RequestQueue queue = Volley.newRequestQueue(mActivity);

            Map<String, String> params = new HashMap<String, String>();
            params.clear();
            final RummyApplication rummyApplication = RummyApplication.getInstance();
            if(call_from_type == 1 || call_from_type == 3)   // 1 for rejoin 2 for join new table 3 for rebuin
            {
                params.put("tableid", tableId);
                params.put("bet", tableDetails.getBet());
                params.put("amount",buyInAmount);
                params.put("game_type",tableDetails.getTableType());
                params.put("id",tableDetails.getGamesettingid());
                params.put("amount_type",tableDetails.getTableCost());
                params.put("user_id",RummyApplication.getInstance().getUserData().getUserId());





                rummyApplication.setCurrentTableAmount(tableDetails.getBet());
                rummyApplication.setCurrentTableBet(tableDetails.getBet());
                rummyApplication.setCurrentTableGameType(tableDetails.getTableType());
                rummyApplication.setCurrentTableSeqId(tableDetails.getGamesettingid());
                rummyApplication.setCurrentTableUserId(RummyApplication.getInstance().getUserData().getUserId());
                rummyApplication.setCurrentTableId(tableId);
                rummyApplication.setCurrentTableCostType(tableDetails.getTableCost());

            }
            else
            {
                params.put("tableid", tableToJoin.getTable_id());
                params.put("bet", tableToJoin.getBet());
                params.put("amount",buyInAmount);
                params.put("game_type",tableToJoin.getTable_type());
                params.put("id",tableToJoin.getId());
                params.put("amount_type",tableToJoin.getTable_cost());
                params.put("user_id",RummyApplication.getInstance().getUserData().getUserId());





                rummyApplication.setCurrentTableAmount(buyInAmount);
                rummyApplication.setCurrentTableBet(tableToJoin.getBet());
                rummyApplication.setCurrentTableGameType(tableToJoin.getTable_type());
                rummyApplication.setCurrentTableSeqId(tableToJoin.getId());
                rummyApplication.setCurrentTableUserId(RummyApplication.getInstance().getUserData().getUserId());
                rummyApplication.setCurrentTableId(tableId);
                rummyApplication.setCurrentTableCostType(tableToJoin.getTable_cost());

            }



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

                                    if(call_from_type == 1)
                                    {
                                        RummyTablesFragment.this.cancelTimer(RummyTablesFragment.this.mRejoinTimer);
                                        RummyTablesFragment.this.hideView(RummyTablesFragment.this.searchGameView);
                                        RummyTablesFragment.this.hideView(RummyTablesFragment.this.mDialogLayout);
                                        RummyTablesFragment.this.sendRejoinRequest(msgUdid);
                                        //  RummyTablesFragment.this.searchTable(event,tableToJoin);
                                    }
                                    else if(call_from_type == 2)
                                    {
                                        RummyTablesFragment.this.removeGameResultFragment();
                                        RummyTablesFragment.this.hideView(RummyTablesFragment.this.searchGameView);
                                        RummyTablesFragment.this.hideView(RummyTablesFragment.this.mDialogLayout);
                                        RummyTablesFragment.this.invisibleView(RummyTablesFragment.this.mGameShecduleTv);
                                        RummyTablesFragment.this.mApplication.refreshTableIds(RummyTablesFragment.this.tableId);
                                        RummyTablesFragment.this.showView(RummyTablesFragment.this.mUserPlayerLayout);
                                        RummyTablesFragment.this.searchTable(event,tableToJoin);
                                    }
                                    else
                                    {
                                        RummyTablesFragment.this.rebuyChips(buyInAmount);
                                    }
                                }
                                else
                                {
                                    if(call_from_type == 1 || call_from_type == 3)
                                    {
                                        String msg = "" + mActivity.getResources().getString(R.string.rummy_low_balance_first) + " " + mActivity.getResources().getString(R.string.rupee_symbol) + getRestAmounttoAdd(tableDetails.getBet() + "") + " " + mActivity.getResources().getString(R.string.rummy_low_balance_second);
                                        showErrorBalanceBuyChips(mActivity,msg,tableDetails.getBet());
                                        //RummyUtils.showGenericMsgDialog(mContext,message);
                                    }
                                    else
                                    {
                                        String msg = "" + mActivity.getResources().getString(R.string.rummy_low_balance_first) + " " + mActivity.getResources().getString(R.string.rupee_symbol) + getRestAmounttoAdd(tableToJoin.getBet() + "") + " " + mActivity.getResources().getString(R.string.rummy_low_balance_second);
                                        showErrorBalanceBuyChips(mActivity,msg,tableDetails.getBet());
                                        //RummyUtils.showGenericMsgDialog(mContext,message);
                                    }

                                }
                            } catch (Exception e) {
                                RummyTLog.e(TAG, "JsonException" + e.toString());
                                RummyUtils.showGenericMsgDialog(mActivity,"Something went wrong");
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
                    RummyUtils.showGenericMsgDialog(mActivity,"Something went wrong, Please try again");
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
            RummyUtils.showGenericMsgDialog(mActivity,"Something went wrong, Please try again after some time");
            e.printStackTrace();
        }
    }






    public class CircleTransform implements TransformationCompat {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;

            canvas.drawCircle(r, r, r-25, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    
    boolean isTourneyTable(){
        return this.strIsTourneyTable.equalsIgnoreCase("yes");
    }
}
