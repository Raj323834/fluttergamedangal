package in.glg.rummy.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;



import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import in.glg.rummy.R;
import in.glg.rummy.RummyApplication;
import in.glg.rummy.activities.RummyHomeActivity;
import in.glg.rummy.activities.RummyInstance;
import in.glg.rummy.api.RummyOnResponseListener;
import in.glg.rummy.api.response.RummyLoginResponse;
import in.glg.rummy.engine.RummyGameEngine;
import in.glg.rummy.exceptions.RummyGameEngineNotRunning;
import in.glg.rummy.models.RummyBaseTrRequest;
import in.glg.rummy.models.RummyEvent;
import in.glg.rummy.utils.RummyCommonEventTracker;
import in.glg.rummy.utils.RummyErrorCodes;
import in.glg.rummy.utils.RummyPrefManager;
import in.glg.rummy.utils.RummyTLog;
import in.glg.rummy.utils.RummyUtils;

import static in.glg.rummy.utils.RummyConstants.ACCESS_TOKEN_REST;


public class RummyHomeFragmentScroller extends RummyBaseFragment implements OnItemClickListener, OnClickListener, OnCheckedChangeListener {
    private static final String TAG = RummyHomeFragmentScroller.class.getSimpleName();
    private RummyOnResponseListener chipLoadListner = new RummyOnResponseListener(RummyLoginResponse.class) {
        public void onResponse(Object response) {
            if (response != null) {
                RummyLoginResponse loginResponse = (RummyLoginResponse) response;
                String message = "";
                int code = Integer.parseInt(loginResponse.getCode());
                if (code == RummyErrorCodes.SUCCESS) {
                    RummyHomeFragmentScroller.this.mFunChips.setText(loginResponse.getFunChips());
                    (RummyApplication.getInstance()).getUserData().setFunChips(loginResponse.getFunChips());
                    message = String.format("%s %s %s", new Object[]{RummyHomeFragmentScroller.this.mContext.getString(R.string.chips_reload_success_msg), loginResponse.getFunChips(), RummyHomeFragmentScroller.this.mContext.getString(R.string.lobby_chips_title).toLowerCase()});
                } else if (code == RummyErrorCodes.PLAYER_HAS_CHIPS_MORE_THAN_MINIMUN) {
                    message = String.format("%s %s %s", new Object[]{RummyHomeFragmentScroller.this.mContext.getString(R.string.balance_reload_err_msg), loginResponse.getMinToReload(), RummyHomeFragmentScroller.this.mContext.getString(R.string.lobby_chips_title).toLowerCase()});
                }
                RummyHomeFragmentScroller.this.showGenericDialog(RummyHomeFragmentScroller.this.mContext, message);
            }
        }
    };

    private Button mBuyChipsBtn;
    private ImageView mChipRelodBtn;
    private FragmentActivity mContext;
    private TextView mFunChips;
    private TextView mFunInPlay;

   // FirebaseCrashlytics crashlytics;
    //private RadioGroup mRadioGroup;
    private TextView mRealChips;
    private TextView mRealInPlay;

    private TextView mUser;
    private static String TOKEN = "";
    private LinearLayout llCashGames, llFreeGames;
    private ImageView ivFreeGames, ivCashGames;
    TextView cashBtn, freeBtn;

    private LinearLayout llDealsRummy, llPoolsRummy, llStrikesRummy, llTournaments, llFavouriteRummy;
    private TextView mNoOfPlayers;
    private TextView mNoOfTables;


    private void setBuildVersion() {
       // this.mVersionTv.setText("Version: " + RummyUtils.getVersionNumber(this.getContext()));
    }

    private void setIdsToViews(View v) {
        this.mChipRelodBtn = (ImageView) v.findViewById(R.id.reload_chips_btn);
        this.mUser = (TextView) v.findViewById(R.id.user_name_tv);
        this.mRealChips = (TextView) v.findViewById(R.id.user_real_chips_value_tv);
        this.mRealInPlay = (TextView) v.findViewById(R.id.inplay_value_tv);
        this.mFunChips = (TextView) v.findViewById(R.id.user_fun_chips_tv);
        this.mFunInPlay = (TextView) v.findViewById(R.id.inplay_fun_tv);


        this.mBuyChipsBtn = (Button) v.findViewById(R.id.buyChipsBtn);
       // this.mVersionTv = (TextView) v.findViewById(R.id.build_version_tv);
        this.llDealsRummy = (LinearLayout) v.findViewById(R.id.llDealsRummy);
        this.llPoolsRummy = (LinearLayout) v.findViewById(R.id.llPoolsRummy);
        this.llStrikesRummy = (LinearLayout) v.findViewById(R.id.llStrikesRummy);
        this.llTournaments = (LinearLayout) v.findViewById(R.id.llTournaments);
        this.llFavouriteRummy = (LinearLayout) v.findViewById(R.id.llFavouriteRummy);
        this.mNoOfPlayers = (TextView) v.findViewById(R.id.no_of_players_tv);
        this.mNoOfTables = (TextView) v.findViewById(R.id.no_of_tables_tv);

        this.llFreeGames = (LinearLayout) v.findViewById(R.id.llFreeGames);
        this.llCashGames = (LinearLayout) v.findViewById(R.id.llCashGames);
        this.ivFreeGames = (ImageView) v.findViewById(R.id.iv_free_game_icon);
        this.ivCashGames = (ImageView) v.findViewById(R.id.iv_cash_game_icon);

        this.mBuyChipsBtn.setOnClickListener(this);

        this.llDealsRummy.setOnClickListener(this);
        this.llPoolsRummy.setOnClickListener(this);
        this.llStrikesRummy.setOnClickListener(this);
        this.llTournaments.setOnClickListener(this);
        this.llFavouriteRummy.setOnClickListener(this);

        this.llFreeGames.setOnClickListener(this);
        this.llCashGames.setOnClickListener(this);


        int tableCost = RummyPrefManager.getInt(getContext(), "tableCost", 1);
        //this.mRadioGroup = (RadioGroup) v.findViewById(R.id.pre_lobby_tab_group);
        //this.mRadioGroup.setOnCheckedChangeListener(this);
        cashBtn = (TextView) v.findViewById(R.id.cash_games_btn);
        freeBtn = (TextView) v.findViewById(R.id.free_games_btn);
      //  Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
      //  cashBtn.setTypeface(font);
      //  freeBtn.setTypeface(font);

        if (tableCost == 1) {
            checkForFreeCashStatus(llCashGames.getId());
        } else {
            checkForFreeCashStatus(llFreeGames.getId());
        }

    }

    private void setUpGameTypeView(View v) {
        this.mChipRelodBtn.setOnClickListener(this);
    }

    private void setUserDetails(RummyLoginResponse userData) {
        if (userData != null) {
            String rupeeSymbol = this.mContext.getString(R.string.rupee_text);
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
            //realChipsStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this.mContext, R.color.rummy_white)), 0, 1, 33);
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
            realInPlayStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this.mContext, R.color.rummy_app_blue)), 0, 1, 33);
            this.mRealChips.setText(realChipsStr);
            this.mRealInPlay.setText(realInPlayStr);
            this.mFunChips.setText(funChips);
            this.mFunInPlay.setText(funInPlay);

          //  crashlytics.setCustomKey("real_chips", realChipsStr + "");
           // crashlytics.setCustomKey("real_inplay", realInPlayStr + "");
          //  crashlytics.setCustomKey("fun_chips", funChips + "");
          //  crashlytics.setCustomKey("fun_inplay", funInPlay + "");

            this.mRealChips.setMovementMethod(new ScrollingMovementMethod());
            this.mRealInPlay.setMovementMethod(new ScrollingMovementMethod());
            this.mFunChips.setMovementMethod(new ScrollingMovementMethod());
            this.mFunInPlay.setMovementMethod(new ScrollingMovementMethod());

        }
    }


    public void loadChips() {
        RummyBaseTrRequest request = new RummyBaseTrRequest();
        request.setCommand("chipreload");
        request.setUuid(RummyUtils.generateUuid());
        try {
            RummyGameEngine.getInstance();
            RummyGameEngine.sendRequestToEngine(this.mContext.getApplicationContext(), RummyUtils.getObjXMl(request), this.chipLoadListner);
        } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
            Toast.makeText(this.mContext, R.string.error_restart, Toast.LENGTH_SHORT).show();
            RummyTLog.d(TAG, "getTableData" + gameEngineNotRunning.getLocalizedMessage());
        }
    }

    public void onCheckedChanged(RadioGroup radioGroup, int checkedItemId) {
        cashBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.rummy_app_blue));
        freeBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.rummy_app_blue));
        llFreeGames.setBackground(getResources().getDrawable(R.drawable.rummy_cash_free_btn_unselected_bg));
        llCashGames.setBackground(getResources().getDrawable(R.drawable.rummy_cash_free_btn_unselected_bg));
       // ivFreeGames.setBackground(getResources().getDrawable(R.drawable.rummy_free_game_icon));
       // ivCashGames.setBackground(getResources().getDrawable(R.drawable.rummy_cash_games_icon));
        if (checkedItemId == R.id.cash_games_btn) {
            cashBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.rummy_highlight_font_scroller));
            RummyPrefManager.saveInt(getContext(), "tableCost", 1);
            return;
        } else if (checkedItemId == R.id.free_games_btn) {
            freeBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.rummy_highlight_font_scroller));
            RummyPrefManager.saveInt(getContext(), "tableCost", 0);
            return;
        }
        return;
    }

    private void checkForFreeCashStatus(int Id) {
        cashBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.rummy_app_blue));
        freeBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.rummy_app_blue));
        llFreeGames.setBackground(getResources().getDrawable(R.drawable.rummy_cash_free_btn_unselected_bg));
        llCashGames.setBackground(getResources().getDrawable(R.drawable.rummy_cash_free_btn_unselected_bg));
       // ivFreeGames.setImageResource(R.drawable.rummy_free_game_icon);
       // ivCashGames.setImageResource(R.drawable.rummy_cash_games_icon);
        if (Id == R.id.llCashGames) {
            cashBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.rummy_highlight_font_scroller));
            llCashGames.setBackground(getResources().getDrawable(R.drawable.rummy_cash_free_btn_selected_bg));
         //   ivCashGames.setImageResource(R.drawable.rummy_cash_game_selected_icon);
            RummyPrefManager.saveInt(getContext(), "tableCost", 1);
            return;
        } else if (Id == R.id.llFreeGames) {
            freeBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.rummy_highlight_font_scroller));
            llFreeGames.setBackground(getResources().getDrawable(R.drawable.rummy_cash_free_btn_selected_bg));
        //    ivFreeGames.setImageResource(R.drawable.rummy_free_game_selected_icon);
            RummyPrefManager.saveInt(getContext(), "tableCost", 0);
            return;
        }
        return;
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.buyChipsBtn) {//showGenericDialog(getContext(), String.format("%s - %s", new Object[]{"To buy Real Chips, please deposit from website", Utils.getWebSite()}));
            RummyInstance.getInstance().getRummyListener().lowBalance(0);
           // checkPlayerDeposit(getContext());
            return;
        } else if (id == R.id.reload_chips_btn) {
            loadChips();
            return;
        } else if (id == R.id.llDealsRummy || id == R.id.llPoolsRummy || id == R.id.llStrikesRummy || id == R.id.llTournaments || id == R.id.llFavouriteRummy) {
            GoToSelectedGameType(v.getId());
            return;
        } else if (id == R.id.llCashGames) {
            checkForFreeCashStatus(v.getId());
            return;
        } else if (id == R.id.llFreeGames) {
            checkForFreeCashStatus(v.getId());
            return;
        }
        return;
    }


    private void GoToSelectedGameType(int itemId) {
        String variantType;
        String fav = "0";
        if (itemId == R.id.llStrikesRummy) {
            variantType = "Strikes";
        } else if (itemId == R.id.llPoolsRummy) {
            variantType = "Pools";
        } else if (itemId == R.id.llTournaments) {
            variantType = "Tourneys";
            ((RummyHomeActivity) getActivity()).showFragment(R.id.tournaments);
            return;
        } else if (itemId == R.id.llDealsRummy) {
            variantType = "Deals";
        } else {
            variantType = "favourite";
            fav = "1";
        }
        ((RummyHomeActivity) getActivity()).setGameVariant(variantType);
        ((RummyHomeActivity) getActivity()).setGameFavourite(fav);
        ((RummyHomeActivity) getActivity()).showFragment(R.id.lobby);
    }

    private void openBrowserToBuyChips() {
        String url = RummyUtils.getApiSeverAddress() + "sendpaymentrequest/?client_type=" + RummyUtils.CLIENT_TYPE + "&device_type=" + getDeviceType();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Bundle bundle = new Bundle();
        Log.d(TAG, "TOKEN: " + TOKEN);
        bundle.putString("Authorization", "Token " + TOKEN);
        i.putExtra(Browser.EXTRA_HEADERS, bundle);
        startActivity(i);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.rummy_fragment_home_scroller, container, false);
        try {
            this.mContext = getActivity();
            RummyLoginResponse userData = (RummyApplication.getInstance()).getUserData();
            setIdsToViews(v);
            setUpGameTypeView(v);
            setUserDetails(userData);

            //for Rating bar
            setPlayerLevel(v.findViewById(R.id.player_rating_bar), userData.getPlayerLevel());

            setBuildVersion();
            this.TOKEN = RummyPrefManager.getString(getContext(), ACCESS_TOKEN_REST, "");


            if (RummyUtils.deepLinkText.length() > 0) {
                if (RummyUtils.deepLinkText.equalsIgnoreCase(RummyUtils.Deposit_deeplink)) {
                    mBuyChipsBtn.performClick();
                } else if (RummyUtils.deepLinkText.equalsIgnoreCase(RummyUtils.Lobby_deeplink)) {
                    ((RummyHomeActivity) getActivity()).setGameFavourite("0");
                    ((RummyHomeActivity) getActivity()).showFragment(R.id.lobby);
                }
                RummyUtils.deepLinkText = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e + "");
        }

        return v;
    }

    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
            Log.d("flow", "onDestroy: Deregister HF");
        }

    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void saveLastCheckedRadioButton(int id) {
        RummyPrefManager.saveInt(getContext(), "lastCheckedItem", id);
    }

    @Subscribe
    public void onMessageEvent(RummyEvent event) {
        if (event.getEventName().equalsIgnoreCase("BALANCE_UPDATE")) {
            RummyLoginResponse userData = (RummyApplication.getInstance()).getUserData();
            userData.setFunChips(event.getFunChips());
            userData.setFunInPlay(event.getFunInPlay());
            userData.setRealChips(event.getReaChips());
            userData.setRealInPlay(event.getRealInPlay());
            setUserDetails(userData);
            RummyCommonEventTracker.UpdateBalaceProperty(mContext,event.getReaChips());

        } else if (event.getEventName().equalsIgnoreCase("HEART_BEAT")) {
            mNoOfPlayers.setText(String.format("%s %s", new Object[]{event.getTotalNoOfPlayers(), "Players"}));
            mNoOfTables.setText(String.format("%s %s", new Object[]{event.getNoOfTables(), "Tables"}));
        }
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


        RummyCommonEventTracker.trackScreenName(RummyCommonEventTracker.PRE_LOBBY_SCREEN, getContext());
    }

    public class WebViewController extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
