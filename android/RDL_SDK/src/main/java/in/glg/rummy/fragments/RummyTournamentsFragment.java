package in.glg.rummy.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.koushikdutta.async.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import in.glg.rummy.R;
import in.glg.rummy.RummyApplication;
import in.glg.rummy.activities.RummyInstance;
import in.glg.rummy.adapter.RummyJoinedTournamentsAdapter;
import in.glg.rummy.adapter.RummyTournamentsAdapterNew;
import in.glg.rummy.api.RummyOnResponseListener;
import in.glg.rummy.api.requests.RummyTournamentsDataRequest;
import in.glg.rummy.api.requests.RummyTournamentsDetailsRequest;
import in.glg.rummy.api.response.RummyLoginResponse;
import in.glg.rummy.api.response.RummyTournamentsDataResponse;
import in.glg.rummy.api.response.RummyTourneyRegistrationResponse;
import in.glg.rummy.engine.RummyGameEngine;
import in.glg.rummy.exceptions.RummyGameEngineNotRunning;
import in.glg.rummy.models.RummyBaseTrRequest;
import in.glg.rummy.models.RummyEvent;
import in.glg.rummy.models.RummyTournament;
import in.glg.rummy.utils.RummyApiCallHelper;
import in.glg.rummy.utils.RummyApiResult;
import in.glg.rummy.utils.RummyCommonEventTracker;
import in.glg.rummy.utils.RummyErrorCodes;
import in.glg.rummy.utils.RummyConstants;
import in.glg.rummy.utils.RummyResultListener;
import in.glg.rummy.utils.RummyTLog;
import in.glg.rummy.utils.RummyTourneyApiHelper;
import in.glg.rummy.utils.RummyUtils;

/**
 * Created by GridLogic on 1/12/17.
 */

public class RummyTournamentsFragment extends RummyBaseFragment {
    private String TAG = RummyTournamentsFragment.class.getName();
    private RummyApplication mRummyApplication;
    private FragmentActivity mContext;
    private RummyLoginResponse userData;

    //FirebaseCrashlytics crashlytics;

    private RummyTournamentsAdapterNew tournamentsAdapter;
    private RummyJoinedTournamentsAdapter joinedTournamentsAdapter;
    private List<RummyTournament> tournaments;
    private RecyclerView tourneyList;
    private RecyclerView myJoinedTourneyList;
    private TextView noTournaments_tv;
    private ProgressBar tourney_progress;
    private CheckBox free_cb, cash_cb;

    private TextView mFunChips;
    private TextView mUser;
    private TextView mRealChips;
    private TextView mRealInPlay;
    private TextView mFunInPlay;
    private Button mBuyChipsBtn;
    private ImageView mChipRelodBtn;

    private TextView mNoOfPlayers;
    private TextView mNoOfTables;

    private RummyTourneyRegistrationResponse mTourneyRegistrationResponse;
    private RummyTournament mcurrentTourney = null;
    private String mcurrentTurneyChips="";
    private int tourneyPosition = 0;



    private LinearLayout llOtherTourney, llMyTourney;
    private List<RummyTournament> joinedTournaments = new ArrayList<>();
    private List<RummyTournament> otherTournaments = new ArrayList<>();
    private TextView tvMyTourneyLabel, tvOtherTourneyLable;

    LinearLayout ll_filter_all,ll_filter_cash,ll_filter_free,ll_filter_joined;
    TextView tv_filter_all,tv_filter_cash,tv_filter_free,tv_filter_joined;

    private static String tourneyFilter = "";


    private RummyOnResponseListener tournamentRegistrationResponse = new RummyOnResponseListener(RummyTourneyRegistrationResponse.class) {
        @Override
        public void onResponse(Object response) {
            if (response != null) {
                RummyTournamentsFragment.this.hideProgress();
                RummyTournamentsFragment.this.mTourneyRegistrationResponse = (RummyTourneyRegistrationResponse) response;

                if (RummyTournamentsFragment.this.mTourneyRegistrationResponse != null
                        && RummyTournamentsFragment.this.mTourneyRegistrationResponse.getCode().equalsIgnoreCase("200")) {
                    if (RummyTournamentsFragment.this.mTourneyRegistrationResponse.getData().equalsIgnoreCase("register_tournament")) {
                        //LobbyFragment.this.mTourneyDetailsResponse.setTourney_chips(LobbyFragment.this.mTourneyRegistrationResponse.getTournament_chips());
                        //LobbyFragment.this.getTournamentDetails();

                            RummyCommonEventTracker.trackTournamentRegisterDeRegister(RummyCommonEventTracker.Tournament_Register, RummyTournamentsFragment.this.mTourneyRegistrationResponse.getUser_id(),
                                    RummyTournamentsFragment.this.mTourneyRegistrationResponse.getTournament_id(), RummyTournamentsFragment.this.mcurrentTurneyChips, RummyTournamentsFragment.this.mContext);

                        RummyTournamentsFragment.this.openRegisterDeRegisterConfirmDialog(mcurrentTourney, "ok", "Congratulations! Your seat is confirmed for\n" +
                                "the tournament. Click on the Take Seat\nButton when the tournament starts.", tourneyPosition);
                        //LobbyFragment.this.showGenericDialogWithMessage("Congratulations! You have been registered for the tourney and you have " + ((TourneyRegistrationResponse) response).getTournament_chips() + " T. chips for the tournament.");
                    } else if (RummyTournamentsFragment.this.mTourneyRegistrationResponse.getData().equalsIgnoreCase("deregister_tournament")) {


                            RummyCommonEventTracker.trackTournamentRegisterDeRegister(RummyCommonEventTracker.Tournament_DE_Register, RummyTournamentsFragment.this.mTourneyRegistrationResponse.getUser_id(),
                                    RummyTournamentsFragment.this.mTourneyRegistrationResponse.getTournament_id(), RummyTournamentsFragment.this.mcurrentTurneyChips, RummyTournamentsFragment.this.mContext);
                        //LobbyFragment.this.getTournamentDetails();
                        RummyTournamentsFragment.this.openRegisterDeRegisterConfirmDialog(mcurrentTourney, "ok", "Your entry has been withdrawn from this\n" +
                                "tournament. Your account will be credited\nwith entry fees (if any).", tourneyPosition);
                    }

                } else if (RummyTournamentsFragment.this.mTourneyRegistrationResponse != null) {
                    if (RummyTournamentsFragment.this.mTourneyRegistrationResponse.getCode().equalsIgnoreCase("7013")) {
                        RummyTournamentsFragment.this.showGenericDialogWithMessage("You cannot cancel your registration request since the tourney is going to start");
                    } else if (RummyTournamentsFragment.this.mTourneyRegistrationResponse.getCode().equalsIgnoreCase("507")) {
                        if (mcurrentTourney != null && mcurrentTourney.getTourneyCost().equalsIgnoreCase("LOYALTYPOINTS_CASH"))
                            RummyTournamentsFragment.this.showErrorLoyaltyChipsDialog(RummyTournamentsFragment.this.mContext, "You don't have sufficient loyalty chips to register for this tourney.");
                        else
                            RummyTournamentsFragment.this.showErrorBuyChipsDialog(RummyTournamentsFragment.this.mContext, "You don't have sufficient balance to register for this tourney.");
                    } else if (RummyTournamentsFragment.this.mTourneyRegistrationResponse.getCode().equalsIgnoreCase("7046")) {
                        RummyTournamentsFragment.this.showGenericDialogWithMessage("Please verify your Email/phone in My Account");
                    } else if (RummyTournamentsFragment.this.mTourneyRegistrationResponse.getCode().equalsIgnoreCase("7042")) {
                        RummyTournamentsFragment.this.showGenericDialogWithMessage("Please verify your Email in My Account");
                    } else if (RummyTournamentsFragment.this.mTourneyRegistrationResponse.getCode().equalsIgnoreCase("7041")) {
                        RummyTournamentsFragment.this.showErrorBuyChipsDialog(RummyTournamentsFragment.this.mContext, "To register for this tourney, you need to have a minimum deposit within last 7 days.");
                    } else if (RummyTournamentsFragment.this.mTourneyRegistrationResponse.getCode().equalsIgnoreCase("7028")) {
                        RummyTournamentsFragment.this.showGenericDialogWithMessage("Registrations for this tournament have been blocked in your state");
                    } else if (RummyTournamentsFragment.this.mTourneyRegistrationResponse.getCode().equalsIgnoreCase("7043")) {
                        RummyTournamentsFragment.this.showGenericDialogWithMessage("Please verify your Mobile Number to play tournaments.");
                    }
                }
            }
        }
    };

    private RummyOnResponseListener chipLoadListner = new RummyOnResponseListener(RummyLoginResponse.class) {
        public void onResponse(Object response) {
            if (response != null) {
                RummyLoginResponse loginResponse = (RummyLoginResponse) response;
                String message = "";
                int code = Integer.parseInt(loginResponse.getCode());
                if (code == RummyErrorCodes.SUCCESS) {
                    (RummyApplication.getInstance()).getUserData().setFunChips(loginResponse.getFunChips());
                    message = String.format("%s %s %s", new Object[]{RummyTournamentsFragment.this.mContext.getString(R.string.chips_reload_success_msg), loginResponse.getFunChips(), RummyTournamentsFragment.this.mContext.getString(R.string.lobby_chips_title).toLowerCase()});
                } else if (code == RummyErrorCodes.PLAYER_HAS_CHIPS_MORE_THAN_MINIMUN) {
                    message = String.format("%s %s %s", new Object[]{RummyTournamentsFragment.this.mContext.getString(R.string.balance_reload_err_msg), loginResponse.getMinToReload(), RummyTournamentsFragment.this.mContext.getString(R.string.lobby_chips_title).toLowerCase()});
                }
                RummyTournamentsFragment.this.showGenericDialog(RummyTournamentsFragment.this.mContext, message);
            }
        }
    };

    private RummyOnResponseListener tournamentsDataListener = new RummyOnResponseListener(RummyTournamentsDataResponse.class) {
        public void onResponse(Object response) {
            if (response != null) {
                hideProgress();
                RummyTournamentsFragment.this.tournaments = ((RummyTournamentsDataResponse) response).getTournaments();
                if (!((RummyTournamentsDataResponse) response).getCode().equalsIgnoreCase("200")) {
                    RummyTournamentsFragment.this.noTournaments_tv.setVisibility(View.VISIBLE);
                    RummyTournamentsFragment.this.tourneyList.setVisibility(View.GONE);
                } else {
                    RummyTLog.e("vikas","tourney listener response code 200");
                    if (RummyTournamentsFragment.this.tournaments != null && RummyTournamentsFragment.this.tournaments.size() > 0) {
                        RummyTLog.e("vikas","tourney listener tourney size="+ RummyTournamentsFragment.this.tournaments.size());
                        RummyTournamentsFragment.this.noTournaments_tv.setVisibility(View.GONE);
                        RummyTournamentsFragment.this.tourneyList.setVisibility(View.VISIBLE);
                        RummyTournamentsFragment.this.populateTourneyData();
                    } else {
                        RummyTournamentsFragment.this.noTournaments_tv.setVisibility(View.VISIBLE);
                        RummyTournamentsFragment.this.tourneyList.setVisibility(View.GONE);
                    }
                }
            }
        }
    };

    private void populateTourneyData() {
        sortTournaments(this.tournaments);
        //SetTourneyAdapters();
        /*if (otherTournaments != null && otherTournaments.size() > 0) {
            Log.e("gopal", "Other tourney list size " + otherTournaments.size());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            tourneyList.setLayoutManager(mLayoutManager);
            tourneyList.setItemAnimator(new DefaultItemAnimator());
            this.tournamentsAdapter = new TournamentsAdapterNew(this.mContext, otherTournaments, this);
            this.tourneyList.setAdapter(tournamentsAdapter);
            this.tournamentsAdapter.notifyDataSetChanged();
            this.tournamentsAdapter.notifyDataSetChanged();
        }


        if (joinedTournaments != null && joinedTournaments.size() > 0) {
            Log.e("gopal", "my joind tourney list size " + joinedTournaments.size());
            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(mContext);
            myJoinedTourneyList.setLayoutManager(mLayoutManager1);
            myJoinedTourneyList.setItemAnimator(new DefaultItemAnimator());
            this.joinedTournamentsAdapter = new JoinedTournamentsAdapter(this.mContext, joinedTournaments, this);
            this.myJoinedTourneyList.setAdapter(joinedTournamentsAdapter);
            this.joinedTournamentsAdapter.notifyDataSetChanged();
        }*/

       // checkFilter();
    }


    private void SetTourneyAdapters()
    {
        if (otherTournaments != null) {
            Log.e("gopal", "Other tourney list size " + otherTournaments.size());
            /*if(tournamentsAdapter==null) {*/
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                tourneyList.setLayoutManager(mLayoutManager);
                tourneyList.setItemAnimator(new DefaultItemAnimator());
                this.tournamentsAdapter = new RummyTournamentsAdapterNew(this.mContext, this.otherTournaments, this);
                this.tourneyList.setAdapter(tournamentsAdapter);
                this.tournamentsAdapter.notifyDataSetChanged();
           /* }
            else
                this.tournamentsAdapter.notifyDataSetChanged();*/

        }

        if (joinedTournaments != null) {
            Log.e("gopal", "my joind tourney list size " + joinedTournaments.size());
            /*if(joinedTournamentsAdapter==null) {*/
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                myJoinedTourneyList.setLayoutManager(mLayoutManager);
                myJoinedTourneyList.setItemAnimator(new DefaultItemAnimator());
                this.joinedTournamentsAdapter = new RummyJoinedTournamentsAdapter(this.mContext, this.joinedTournaments, this);
                this.myJoinedTourneyList.setAdapter(joinedTournamentsAdapter);
                this.joinedTournamentsAdapter.notifyDataSetChanged();
            /*}
            else
                this.joinedTournamentsAdapter.notifyDataSetChanged();*/
        }
    }
    // Applying bubble rummy_sort
    private void sortTournaments(List<RummyTournament> tournaments) {


        try {
            String formatDate = "dd MMM yyyy hh:mm aa";
            SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
            Date fromDate, toDate;
            RummyTournament tourney;
            List<RummyTournament> tournamentsListTemp = new ArrayList<>();
            Date todayDate = new Date();
            for (int i = 0; i < tournaments.size(); i++) {
                for (int j = 1; j < (tournaments.size() - i); j++) {
                    fromDate = sdf.parse(RummyUtils.convertTimeStampToAnyDateFormat(tournaments.get(j - 1).getStartDate(), formatDate));
                    toDate = sdf.parse(RummyUtils.convertTimeStampToAnyDateFormat(tournaments.get(j).getStartDate(), formatDate));

                    if (fromDate.compareTo(toDate) > 0) {
                        tourney = tournaments.get(j - 1);
                        tournaments.set((j - 1), tournaments.get(j));
                        tournaments.set((j), tourney);
                    }
                }
            }

            this.tournaments = tournaments;
            SortTourneyInternalList(this.tournaments);

        } catch (Exception e) {
            Log.e(TAG, "EXP: Sorting tournaments data -->> " + e.toString());
        }



    }

    private void checkFilter() {
        Log.d(TAG, "checking filter");
        if (RummyConstants.T_FILTER.equalsIgnoreCase("cash"))
            cash_cb.setChecked(true);
        else if (RummyConstants.T_FILTER.equalsIgnoreCase("free"))
            free_cb.setChecked(true);
        else {
            free_cb.setChecked(false);
            cash_cb.setChecked(false);
        }
    }

    public void launchTDFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_content_frame, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Subscribe
    public void onMessageEvent(RummyEvent event) {
        String eventName = event.getEventName();
        if (eventName.equalsIgnoreCase("show_tournament")) {
            RummyTournament tournament = new RummyTournament();
            tournament.setTourneyId(event.getTournament().getTourneyId());
            tournament.setEntry(event.getTournament().getEntry());
            tournament.setStartDate(event.getTournament().getStartDate());
            tournament.setStatus(event.getTournament().getStatus());
            tournament.setPlayers(event.getTournament().getPlayers());
            tournament.setCashPrize(event.getTournament().getCashPrize());
            tournament.setTournamentName(event.getTournament().getTournamentName());

            if (this.tournaments.size() == 0) {
                getTournamentsData();
            } else {
                this.tournaments.add(tournament);
                this.tournamentsAdapter.addNewItem();

                updateFilter();
            }
        } else if (eventName.equalsIgnoreCase("end_tournament")) {
            for (int i = 0; i < this.tournaments.size(); i++) {
                if (this.tournaments.get(i).getTourneyId().equalsIgnoreCase(event.getTournamentId())) {
                    this.tournaments.get(i).setStatus("completed");
                    break;
                }
            }

            updateList();
            updateFilter();


        } else if (eventName.equalsIgnoreCase("start_tournament")) {
            for (int i = 0; i < this.tournaments.size(); i++) {
                if (this.tournaments.get(i).getTourneyId().equalsIgnoreCase(event.getTournamentId())) {
                    this.tournaments.get(i).setStatus("running");
                    break;
                }
            }
            updateList();
            updateFilter();


        } else if (eventName.equalsIgnoreCase("stop_registration")) {
            for (int i = 0; i < this.tournaments.size(); i++) {
                if (this.tournaments.get(i).getTourneyId().equalsIgnoreCase(event.getTournamentId())) {
                    this.tournaments.get(i).setStatus("registrations closed");
                    break;
                }
            }
            updateList();
            updateFilter();


        } else if (eventName.equalsIgnoreCase("tournament_cancel")) {
            for (int i = 0; i < this.tournaments.size(); i++) {
                if (this.tournaments.get(i).getTourneyId().equalsIgnoreCase(event.getTournamentId())) {
                    this.tournaments.get(i).setStatus("canceled");
                    break;
                }
            }
            updateList();
            updateFilter();


        } else if (eventName.equalsIgnoreCase("start_registration")) {
            for (int i = 0; i < this.tournaments.size(); i++) {
                if (this.tournaments.get(i).getTourneyId().equalsIgnoreCase(event.getTournamentId())) {
                    this.tournaments.get(i).setStatus("registration open");
                    break;
                }
            }
            updateList();
            updateFilter();


        } else if (eventName.equalsIgnoreCase("BALANCE_UPDATE")) {
            RummyLoginResponse userData = ((RummyApplication.getInstance())).getUserData();
            userData.setFunChips(event.getFunChips());
            userData.setFunInPlay(event.getFunInPlay());
            userData.setRealChips(event.getReaChips());
            userData.setRealInPlay(event.getRealInPlay());
            userData.setLoyalityChips(event.getLoyaltyChips());
            setUserNameAndChipsDetails(userData);
            RummyCommonEventTracker.UpdateBalaceProperty(mContext,event.getReaChips());


        } else if (eventName.equalsIgnoreCase("player_registered") || eventName.equalsIgnoreCase("player_deregistered")) {
            for (int i = 0; i < this.tournaments.size(); i++) {
                if (this.tournaments.get(i).getTourneyId().equalsIgnoreCase(event.getTournamentId())) {
                    this.tournaments.get(i).setPlayers(event.getTotalgamePlayers());
                    break;
                }
            }
            updateList();
            updateFilter();


        } else if (eventName.equalsIgnoreCase("tournament_closed")) {
            for (int i = 0; i < this.tournaments.size(); i++) {
                if (this.tournaments.get(i).getTourneyId().equalsIgnoreCase(event.getTournamentId())) {
                    this.tournaments.remove(i);
                    break;
                }
            }
            updateList();
            updateFilter();


        }
        else if(event.getEventName().equalsIgnoreCase("HEART_BEAT"))
        {
            mNoOfPlayers.setText(String.format("%s %s", new Object[]{event.getTotalNoOfPlayers(), "Players"}));
            mNoOfTables.setText(String.format("%s %s", new Object[]{event.getNoOfTables(), "Tables"}));
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.rummy_fragment_tournaments, container, false);
        Log.e("tournaments", "tournaments");
      //  crashlytics=FirebaseCrashlytics.getInstance();
        setIdsToViews(v);
        setListenersToViews();
        init();
    
        if (RummyGameEngine.getInstance().isSocketConnected()) {
            getTournamentsData();
        }
        if (RummyApplication.getInstance() != null) {
            RummyLoginResponse userData = ((RummyApplication.getInstance())).getUserData();
            setUserNameAndChipsDetails(userData);
            if (userData != null) {
                setPlayerLevel(v.findViewById(R.id.player_rating_bar), userData.getPlayerLevel());
            }
        }

        return v;
    }

    private void setListenersToViews() {
        free_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
           /*     if (b) {
                    cash_cb.setChecked(false);
                    if (otherTournaments != null && otherTournaments.size() > 0) {
                        tournamentsAdapter.filter("FREE", RummyTournamentsFragment.this.otherTournaments);
                        if (joinedTournamentsAdapter != null && joinedTournaments.size() > 0) {
                            joinedTournamentsAdapter.filter("FREE", RummyTournamentsFragment.this.joinedTournaments);
                        }
                    }
                } else {
                    if (otherTournaments != null && otherTournaments.size() > 0) {
                        tournamentsAdapter.filter("ALL", RummyTournamentsFragment.this.otherTournaments);
                        if (joinedTournamentsAdapter != null && joinedTournaments.size() > 0) {
                            joinedTournamentsAdapter.filter("FREE", RummyTournamentsFragment.this.joinedTournaments);
                        }
                    }
                }*/
                if (b) {
                    RummyConstants.T_FILTER = "free";
                    cash_cb.setChecked(false);
                    updateFilter();
                }
                else if (!cash_cb.isChecked())
                    RummyConstants.T_FILTER = "all";
            }
        });

        cash_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               /* if (b) {
                    free_cb.setChecked(false);
                    if (otherTournaments != null && otherTournaments.size() > 0)
                    {
                        tournamentsAdapter.filter("CASH", RummyTournamentsFragment.this.otherTournaments);
                        if (joinedTournamentsAdapter != null && joinedTournaments.size() > 0) {
                            joinedTournamentsAdapter.filter("CASH", RummyTournamentsFragment.this.joinedTournaments);
                        }
                    }

                } else {
                    if (otherTournaments != null && otherTournaments.size() > 0)
                    {
                        tournamentsAdapter.filter("ALL", RummyTournamentsFragment.this.otherTournaments);
                        if (joinedTournamentsAdapter != null && joinedTournaments.size() > 0) {
                            joinedTournamentsAdapter.filter("ALL", RummyTournamentsFragment.this.joinedTournaments);
                        }
                    }

                }*/

                if (b) {
                    RummyConstants.T_FILTER = "cash";
                    free_cb.setChecked(false);
                    updateFilter();
                }
                else if (!free_cb.isChecked())
                    RummyConstants.T_FILTER = "all";
            }
        });

        this.ll_filter_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllTab();
              //  ll_filter_all.setBackground(getResources().getDrawable(R.drawable.btn_yellow_square_less_corner_bg));
                tv_filter_all.setTextColor(mContext.getResources().getColor(R.color.rummy_yellow));
                cash_cb.setChecked(false);
                free_cb.setChecked(false);
                RummyConstants.T_FILTER = "all";
                updateFilter();
            }
        });
        this.ll_filter_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllTab();
               // ll_filter_cash.setBackground(getResources().getDrawable(R.drawable.btn_yellow_square_less_corner_bg));
                tv_filter_cash.setTextColor(mContext.getResources().getColor(R.color.rummy_yellow));
                cash_cb.setChecked(true);
            }
        });
        this.ll_filter_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllTab();
                //ll_filter_free.setBackground(getResources().getDrawable(R.drawable.btn_yellow_square_less_corner_bg));
                tv_filter_free.setTextColor(mContext.getResources().getColor(R.color.rummy_yellow));
                free_cb.setChecked(true);
            }
        });
        this.ll_filter_joined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllTab();
                tv_filter_joined.setTextColor(mContext.getResources().getColor(R.color.rummy_yellow));

                if (tournaments != null && tournaments.size() > 0)
                    tournamentsAdapter.filter("JOINED", RummyTournamentsFragment.this.tournaments);
            }
        });

        mBuyChipsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RummyInstance.getInstance().getRummyListener().lowBalance(0);
                //checkPlayerDeposit(mContext);
            }
        });
        mChipRelodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadChips();
            }
        });
    }

    private void loadChips() {
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

    private  void resetAllTab()
    {
        tv_filter_all.setTextColor(mContext.getResources().getColor(R.color.rummy_white));
        tv_filter_cash.setTextColor(mContext.getResources().getColor(R.color.rummy_white));
        tv_filter_free.setTextColor(mContext.getResources().getColor(R.color.rummy_white));
        tv_filter_joined.setTextColor(mContext.getResources().getColor(R.color.rummy_white));
    }
    private void updateFilter() {
        if(this.tournaments!=null) {
            SortTourneyInternalList(this.tournaments);
            if (tournamentsAdapter != null && otherTournaments.size() > 0) {
                if (free_cb.isChecked())
                    tournamentsAdapter.filter("FREE", RummyTournamentsFragment.this.otherTournaments);
                else if (cash_cb.isChecked())
                    tournamentsAdapter.filter("CASH", RummyTournamentsFragment.this.otherTournaments);
                else
                    tournamentsAdapter.filter("ALL", RummyTournamentsFragment.this.otherTournaments);
            } else {
                tvOtherTourneyLable.setVisibility(View.GONE);
            }
        }



        if (joinedTournamentsAdapter != null && joinedTournaments.size() >0) {
            if (free_cb.isChecked())
                joinedTournamentsAdapter.filter("FREE", RummyTournamentsFragment.this.joinedTournaments);
            else if (cash_cb.isChecked())
                joinedTournamentsAdapter.filter("CASH", RummyTournamentsFragment.this.joinedTournaments);
            else
                joinedTournamentsAdapter.filter("ALL", RummyTournamentsFragment.this.joinedTournaments);
            if(joinedTournamentsAdapter.getItemCount()>0){
                tvMyTourneyLabel.setVisibility(View.VISIBLE);
                if(tournamentsAdapter != null && tournamentsAdapter.getItemCount() >0){
                    tvOtherTourneyLable.setVisibility(View.VISIBLE);
                }else {
                    tvOtherTourneyLable.setVisibility(View.GONE);
                }
            }else {
                tvOtherTourneyLable.setVisibility(View.GONE);
                tvMyTourneyLabel.setVisibility(View.GONE);
            }
        }
        
    }

    private void init() {
        this.mContext = getActivity();
        this.mRummyApplication = (RummyApplication.getInstance());
        RummyApplication app = (RummyApplication.getInstance());
        if (app != null) {
            this.userData = app.getUserData();
        }

        RummyConstants.T_FILTER = "ALL";
    }

    private void setIdsToViews(View v) {
        this.tourneyList = (RecyclerView) v.findViewById(R.id.tourneyList);
        this.myJoinedTourneyList = (RecyclerView) v.findViewById(R.id.myTourneyList);
        this.noTournaments_tv = (TextView) v.findViewById(R.id.noTournaments_tv);
        this.tourney_progress = (ProgressBar) v.findViewById(R.id.tourney_progress);
        this.free_cb = (CheckBox) v.findViewById(R.id.free_cb);
        this.cash_cb = (CheckBox) v.findViewById(R.id.cash_cb);

        this.mFunChips = (TextView) v.findViewById(R.id.user_fun_chips_tv);
        this.mUser = (TextView) v.findViewById(R.id.user_name_tv);
        this.mRealChips = (TextView) v.findViewById(R.id.user_real_chips_value_tv);
        this.mRealInPlay = (TextView) v.findViewById(R.id.inplay_value_tv);
        this.mFunInPlay = (TextView) v.findViewById(R.id.inplay_fun_tv);

        this.mChipRelodBtn = (ImageView) v.findViewById(R.id.reload_chips_btn);
        this.mBuyChipsBtn = (Button) v.findViewById(R.id.buyChipsBtn);

        this.mNoOfPlayers = (TextView) v.findViewById(R.id.no_of_players_tv);
        this.mNoOfTables = (TextView) v.findViewById(R.id.no_of_tables_tv);

        this.ll_filter_all = (LinearLayout)v.findViewById(R.id.ll_filter_all);
        this.ll_filter_cash = (LinearLayout)v.findViewById(R.id.ll_filter_cash);
        this.ll_filter_free = (LinearLayout)v.findViewById(R.id.ll_filter_free);
        this.ll_filter_joined = (LinearLayout)v.findViewById(R.id.ll_filter_joined);

        this.tv_filter_all = (TextView)v.findViewById(R.id.tv_filter_all);
        this.tv_filter_cash = (TextView)v.findViewById(R.id.tv_filter_cash);
        this.tv_filter_free = (TextView)v.findViewById(R.id.tv_filter_free);
        this.tv_filter_joined = (TextView)v.findViewById(R.id.tv_filter_joined);

        this.llOtherTourney = (LinearLayout) v.findViewById(R.id.llOtherTourney);
        this.llMyTourney = (LinearLayout) v.findViewById(R.id.llMyTourney);

        this.tvMyTourneyLabel = (TextView) v.findViewById(R.id.tvMyTourneyLabel);
        this.tvOtherTourneyLable = (TextView) v.findViewById(R.id.tvOtherTourneyLabel);
    }




    private void setUserNameAndChipsDetails(RummyLoginResponse userData) {
        if (userData != null) {
            String rupeeSymbol = this.mContext.getString(R.string.rupee_text);
            this.mUser.setText(userData.getNickName());
            String funChips = userData.getFunChips();
            if (funChips == null || funChips.length() <= 0) {
                funChips = "0";
            }else{
                try {
                    funChips = new DecimalFormat("0,#").format(funChips);
                }catch (Exception e){
                }
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
          //  crashlytics.setCustomKey("real_inplay", realInPlayStr + "");
            //crashlytics.setCustomKey("fun_chips", funChips + "");
          //  crashlytics.setCustomKey("fun_inplay", funInPlay + "");

            this.mRealChips.setMovementMethod(new ScrollingMovementMethod());
            this.mRealInPlay.setMovementMethod(new ScrollingMovementMethod());
            this.mFunChips.setMovementMethod(new ScrollingMovementMethod());
            this.mFunInPlay.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    public void getTournamentsData() {
        try {
            showProgress();
            RummyTournamentsDataRequest request = new RummyTournamentsDataRequest();
            request.setCommand("list_tournaments");
            request.setUuid(RummyUtils.generateUuid());

            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mContext.getApplicationContext(), RummyUtils.getObjXMl(request), this.tournamentsDataListener);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mContext, R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.e(TAG, "getTourneyData" + gameEngineNotRunning.getLocalizedMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: getTournamentsData-->> " + e.toString());
            hideProgress();
        }
    }

    private void showProgress() {
        this.tourney_progress.setVisibility(View.VISIBLE);
       // this.tourneyList.setVisibility(View.GONE);
    }

    private void hideProgress() {
        this.tourney_progress.setVisibility(View.GONE);
        this.tourneyList.setVisibility(View.VISIBLE);
    }

    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


        RummyCommonEventTracker.trackScreenName(RummyCommonEventTracker.TOURNEY_LOBBY_SCREEN, getContext());
    }

    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void updateList() {
        if (this.tournamentsAdapter != null) {
            this.tournamentsAdapter.notifyDataSetChanged();
        }
        if (this.joinedTournamentsAdapter != null) {
            this.joinedTournamentsAdapter.notifyDataSetChanged();
        }
    }

    public void openRegisterDeRegisterConfirmDialog(final RummyTournament tournament, String type, String message, final int posotion) {

        final Dialog dialog = new Dialog(mContext, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rummy_dialog_join_tourney);
        dialog.setCanceledOnTouchOutside(false);

        TextView dialog_msg_tv = (TextView) dialog.findViewById(R.id.dialog_msg_tv);
        TextView tourneyName = (TextView) dialog.findViewById(R.id.tv_tourney_name);
        TextView startTime = (TextView) dialog.findViewById(R.id.tv_startTime);
        TextView entryFee = (TextView) dialog.findViewById(R.id.tv_entryFee);
        Button yes_btn = (Button) dialog.findViewById(R.id.yes_btn);
        Button no_btn = (Button) dialog.findViewById(R.id.no_btn);

        no_btn.setText("NO");
        no_btn.setBackground(mContext.getResources().getDrawable(R.drawable.rummy_curve_edges_no_button));
        no_btn.setTextColor(mContext.getResources().getColor(R.color.rummy_black));
        yes_btn.setVisibility(View.VISIBLE);

        if (tournament.getTournamentName() != null) {
            if (tournament.getTournamentName().contains("_")) {
                String[] value = tournament.getTournamentName().split("_");
                if (value.length == 2) {
                    tourneyName.setText(value[0]);
                } else
                    tourneyName.setText(tournament.getTournamentName());
            } else {
                tourneyName.setText(tournament.getTournamentName());
            }

        }

        if (tournament.getEntry().equalsIgnoreCase("0") || tournament.getEntry().equalsIgnoreCase("0.0")) {
            entryFee.setText("FREE");

        } else if (tournament.getTourneyCost()!=null && tournament.getTourneyCost().equalsIgnoreCase("LOYALTYPOINTS_CASH")) {
            entryFee.setText("LOYALTY");
        } else {
            entryFee.setText(mContext.getResources().getString(R.string.rupee_symbol) + tournament.getEntry());
        }

        if (tournament.getStartDate() != null && tournament.getStartDate().length() > 0) {
            /*startTime.setText(Utils.convertTimeStampToAnyDateFormat(tournament.getStartDate(), "hh:mm aa").toUpperCase()+","+Utils.convertTimeStampToAnyDateFormat(tournament.getStartDate(), "MMM-dd").toUpperCase()
                            +","+Utils.convertTimeStampToAnyDateFormat(tournament.getStartDate(), "EEEE").toUpperCase()
                    );*/

            startTime.setText(RummyUtils.convertTimeStampToAnyDateFormat(tournament.getStartDate(), "hh:mm aa,MMM-dd,EEEE").toUpperCase());

        }

        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });

        dialog_msg_tv.setText(message);

        if (type.equalsIgnoreCase("register")) {
            /*if (tournament.getTourneyCost().equalsIgnoreCase("LOYALTYPOINTS_CASH"))
                dialog_msg_tv.setText("Do you want to register for the tourney? " + tournament.getEntry() + " Loyalty Chips will be deducted from your account.");
            else
                dialog_msg_tv.setText("Do you want to register for the tourney? " + tournament.getEntry() + " Chips will be deducted from your account.");*/

            yes_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    RummyTournamentsFragment.this.registerTournament(tournament, posotion);
                }
            });

        } else if (type.equalsIgnoreCase("deregister")) {
            //dialog_msg_tv.setText("Are you sure, you want to unregister from TID " + tournament.getTourneyId() + "?");
            yes_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    RummyTournamentsFragment.this.deregisterTournament(tournament, posotion);
                }
            });
        } else {
            yes_btn.setVisibility(View.GONE);
            no_btn.setText("OK");
            no_btn.setTextColor(mContext.getResources().getColor(R.color.rummy_white));
            no_btn.setBackground(mContext.getResources().getDrawable(R.drawable.rummy_curve_edges_ok_button));

            String id = mcurrentTourney.getTourneyId();
            for (int i = 0; i < tournaments.size(); i++) {
                if (id.equalsIgnoreCase(tournaments.get(i).getTourneyId())) {
                    if (mcurrentTourney.getReg_status() != null && mcurrentTourney.getReg_status().equalsIgnoreCase("true")) {
                        tournaments.get(i).setReg_status("False");
                    } else {
                        tournaments.get(i).setReg_status("True");
                    }

                    break;
                }
            }

            updateList();
            updateFilter();
        }

        dialog.show();

    }

    public void registerTournament(RummyTournament tournament, int posotion) {
        try {
            this.mcurrentTourney = tournament;

            if(mcurrentTourney!=null && mcurrentTourney.getEntry()!=null) {
                if (mcurrentTourney.getEntry().equalsIgnoreCase("0") || mcurrentTourney.getEntry().equalsIgnoreCase("0.0")) {
                    this.mcurrentTurneyChips = "funchips";
                } else if (mcurrentTourney.getTourneyCost() != null && mcurrentTourney.getTourneyCost().equalsIgnoreCase("LOYALTYPOINTS_CASH")) {
                    this.mcurrentTurneyChips = "loyaltypointscash";
                } else {
                    this.mcurrentTurneyChips = "cash";
                }
            }
            this.tourneyPosition = posotion;
            this.showProgress();
           /* RummyTournamentsDetailsRequest request = new RummyTournamentsDetailsRequest();
            request.setCommand("register_tournament");
            request.setUuid(RummyUtils.generateUuid());
            request.setTournament_id(tournament.getTourneyId());
            request.setLevel("1");
            request.setPlayer_amount("0");
            //request.setPlayer_amount(this.mTourneyDetailsResponse.getEntry());
            request.setVipcode("None");

            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mContext.getApplicationContext(), RummyUtils.getObjXMl(request), this.tournamentRegistrationResponse);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mContext.getApplicationContext(), R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.e(TAG, "registerTournament" + gameEngineNotRunning.getLocalizedMessage());
                this.hideProgress();
            }*/
            RummyTourneyApiHelper.registerTourney(tournament.getTourneyId(), new RummyApiCallHelper.ApiResponseListener() {
                @Override
                public void response(RummyApiResult<JSONObject> result) {
                    handleResult(result, new RummyResultListener() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            hideProgress();
                            RummyTournamentsFragment.this.openRegisterDeRegisterConfirmDialog(mcurrentTourney,
                                    "ok", "Congratulations! Your seat is confirmed for\n" +
                                    "the tournament. Click on the Take Seat\nButton when the tournament starts.",
                                    tourneyPosition);
                        }
    
                        @Override
                        public void onFailed(String error) {
                            super.onFailed(error);
                            hideProgress();
                            try {
                                JSONObject jsonObject = new JSONObject(error);
                                if(jsonObject.getString("code").equalsIgnoreCase("7041")){
                                    showErrorBuyChipsDialog(getActivity(),jsonObject.getString("message"));
                                }else{
                                    showLongToast(getActivity(),jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                showLongToast(getActivity(),result.getErrorMessage());
                            }

                        }
                    });
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "EXP: registerTournament-->> " + e.toString());
            this.hideProgress();
        }
    }

    public void deregisterTournament(RummyTournament tournament, int posotion) {
        try {
            this.showProgress();
            this.mcurrentTourney = tournament;
            if(mcurrentTourney!=null && mcurrentTourney.getEntry()!=null) {
                if (mcurrentTourney.getEntry().equalsIgnoreCase("0") || mcurrentTourney.getEntry().equalsIgnoreCase("0.0")) {
                    this.mcurrentTurneyChips = "funchips";
                } else if (mcurrentTourney.getTourneyCost() != null && mcurrentTourney.getTourneyCost().equalsIgnoreCase("LOYALTYPOINTS_CASH")) {
                    this.mcurrentTurneyChips = "loyaltypointscash";
                } else {
                    this.mcurrentTurneyChips = "cash";
                }
            }
            this.tourneyPosition = posotion;
           /* RummyTournamentsDetailsRequest request = new RummyTournamentsDetailsRequest();
            request.setCommand("deregister_tournament");
            request.setUuid(RummyUtils.generateUuid());
            request.setTournament_id(tournament.getTourneyId());
            request.setLevel("1");

            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mContext.getApplicationContext(), RummyUtils.getObjXMl(request), this.tournamentRegistrationResponse);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mContext.getApplicationContext(), R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.e(TAG, "deregisterTournament" + gameEngineNotRunning.getLocalizedMessage());
                this.hideProgress();
            }*/
    
            RummyTourneyApiHelper.deRegisterTourney(tournament.getTourneyId(), new RummyApiCallHelper.ApiResponseListener() {
                @Override
                public void response(RummyApiResult<JSONObject> result) {
                    handleResult(result, new RummyResultListener() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            hideProgress();
                            RummyTournamentsFragment.this.openRegisterDeRegisterConfirmDialog(mcurrentTourney, "ok", "Your entry has been withdrawn from this\n" +
                                    "tournament. Your account will be credited\nwith entry fees (if any).", tourneyPosition);
                        }
    
                        @Override
                        public void onFailed(String error) {
                            super.onFailed(error);
                            hideProgress();
                        }
                    });
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "EXP: deregisterTournament-->> " + e.toString());
            this.hideProgress();
        }
    }

    private void showGenericDialogWithMessage(final String message) {
        final Dialog dialog = new Dialog(mContext, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rummy_dialog_generic);
        dialog.setCanceledOnTouchOutside(false);

        TextView dialog_msg_tv = (TextView) dialog.findViewById(R.id.dialog_msg_tv);
        Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);

        dialog_msg_tv.setText(message);

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                dialog.dismiss();
            }

        }.start();

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.equalsIgnoreCase("This tournament has been cancelled")) {
                    dialog.dismiss();
                } else
                    dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void SortTourneyInternalList(List<RummyTournament> tournamentsListTemp) {
        joinedTournaments.clear();
        otherTournaments.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy hh:mm:ss aa");
        List<RummyTournament> myJoinedTourneyListTemp = new ArrayList<>();
        List<RummyTournament> otherTourneyListTemp = new ArrayList<>();
        List<RummyTournament> otherTourneyListFinal = new ArrayList<>();
        for (RummyTournament t : tournamentsListTemp) {
            if (t.getStatus().equalsIgnoreCase("registration open") && t.getReg_status() != null
                    && t.getReg_status().equalsIgnoreCase("true")) {
                myJoinedTourneyListTemp.add(t);
            }
            else if(t.getStatus().equalsIgnoreCase("registrations closed") && t.getReg_status() !=null && t.getReg_status().equalsIgnoreCase("true"))
            {
                myJoinedTourneyListTemp.add(t);
            }
            else
                {
                otherTourneyListTemp.add(t);
            }
        }


        otherTourneyListTemp = SortDateWiseAscendingOrder(otherTourneyListTemp);
        for(int j = 0; j<otherTourneyListTemp.size();j++)
        {
            RummyTournament t = otherTourneyListTemp.get(j);
            if(t.getStatus().equalsIgnoreCase("registration open"))
            {
                otherTourneyListFinal.add(t);
            }
        }




        for(int j = 0; j<otherTourneyListTemp.size();j++)
        {
            RummyTournament t = otherTourneyListTemp.get(j);
            if(t.getStatus().equalsIgnoreCase("running"))
            {
                otherTourneyListFinal.add(t);
            }
        }

        for(int j = 0; j<otherTourneyListTemp.size();j++)
        {
            RummyTournament t = otherTourneyListTemp.get(j);
            long millis = 0;
            try {
                Date start = new Date();
                start = sdf.parse(sdf.format(start));
                Date end = sdf.parse(RummyUtils.convertTimeStampToAnyDateFormat(t.getStartDate(), "dd MM yyyy hh:mm:ss aa"));

                millis = end.getTime() - start.getTime();
            }
            catch (Exception e)
            {

            }
            if(t.getStatus().equalsIgnoreCase("announced")
                    && millis > 0)
            {
                otherTourneyListFinal.add(t);
            }

        }

        for(int j = 0; j<otherTourneyListTemp.size();j++)
        {
            RummyTournament t = otherTourneyListTemp.get(j);
            long millis = 0;
            try {
                Date start = new Date();
                start = sdf.parse(sdf.format(start));
                Date end = sdf.parse(RummyUtils.convertTimeStampToAnyDateFormat(t.getStartDate(), "dd MM yyyy hh:mm:ss aa"));

                millis = end.getTime() - start.getTime();
            }
            catch (Exception e)
            {

            }
            if(t.getStatus().equalsIgnoreCase("completed") || (t.getStatus().equalsIgnoreCase("announced")
                    && millis <= 0))
            {
                otherTourneyListFinal.add(t);
            }
        }


        for(int j = 0; j<otherTourneyListTemp.size();j++)
        {
            RummyTournament t = otherTourneyListTemp.get(j);
            if(t.getStatus().equalsIgnoreCase("canceled"))
            {
                otherTourneyListFinal.add(t);
            }
        }
       /* for(int i=otherTourneyListTemp.size()-1;i>=0;i--)
        {
            otherTourneyListFinal.add(otherTourneyListTemp.get(i));
        }*/

        if (myJoinedTourneyListTemp.size() > 0) {
            Log.e("gopal","1 take");
            this.joinedTournaments = myJoinedTourneyListTemp;
            llMyTourney.setVisibility(View.VISIBLE);
            tvMyTourneyLabel.setVisibility(View.VISIBLE);
        } else {
            Log.e("gopal","2 take");
            llMyTourney.setVisibility(View.GONE);
            tvMyTourneyLabel.setVisibility(View.GONE);
        }

        if (otherTourneyListFinal.size() > 0 && myJoinedTourneyListTemp.size() == 0) {
            Log.e("gopal","3 take");
            otherTournaments = otherTourneyListFinal;
            llOtherTourney.setVisibility(View.VISIBLE);
            tvOtherTourneyLable.setVisibility(View.GONE);
        } else if(otherTourneyListFinal.size()>0) {
            Log.e("gopal","4 take");
            otherTournaments = otherTourneyListFinal;
            llOtherTourney.setVisibility(View.VISIBLE);
            tvOtherTourneyLable.setVisibility(View.VISIBLE);
        }

        SetTourneyAdapters();
    }


    private List<RummyTournament> SortDateWiseAscendingOrder(List<RummyTournament> myList) {
        List<RummyTournament> checkList = new ArrayList<>();
        try {
            if (myList != null && myList.size() > 0)
                checkList = myList;

            final SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy hh:mm:ss aa");
            Collections.sort(checkList, new Comparator<RummyTournament>() {
                public int compare(RummyTournament o1, RummyTournament o2) {
                    String strDt1 = o1.getStartDate(); // your first date here
                    String strDt2 = o2.getStartDate(); // second date here

                    if (strDt1 == null || strDt2 == null || strDt1.length() == 0 || strDt2.length() == 0) {
                        return 0;
                    } else {
                        Date dt1 = null;
                        Date dt2 = null;
                        try {
                            dt1 = sdf.parse(RummyUtils.convertTimeStampToAnyDateFormat(strDt1, "dd MM yyyy hh:mm:ss aa"));
                            dt2 = sdf.parse(RummyUtils.convertTimeStampToAnyDateFormat(strDt2, "dd MM yyyy hh:mm:ss aa"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (dt1 == null || dt2 == null)
                            return 0;
                        return dt1.compareTo(dt2);
                    }
                }


            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return checkList;
    }

}
