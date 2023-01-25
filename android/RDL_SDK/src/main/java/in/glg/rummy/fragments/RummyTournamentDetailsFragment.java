package in.glg.rummy.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import in.glg.rummy.R;
import in.glg.rummy.RummyApplication;
import in.glg.rummy.adapter.RummyJoinedPlayersAdapter;
import in.glg.rummy.adapter.RummyLevelsAdapter;
import in.glg.rummy.adapter.RummyPrizeListAdapter;
import in.glg.rummy.adapter.RummyTourneyTablesAdapter;
import in.glg.rummy.adapter.RummyWaitingPlayersAdapter;
import in.glg.rummy.adapter.RummyWinnersAdapter;
import in.glg.rummy.api.RummyOnResponseListener;
import in.glg.rummy.api.requests.RummyGetTableDetailsRequest;
import in.glg.rummy.api.requests.RummyTournamentsDetailsRequest;
import in.glg.rummy.api.response.RummyBaseReply;
import in.glg.rummy.api.response.RummyJoinedPlayersResponse;
import in.glg.rummy.api.response.RummyLoginResponse;
import in.glg.rummy.api.response.RummyPrizeListResponse;
import in.glg.rummy.api.response.RummyTournamentDetailsResponse;
import in.glg.rummy.api.response.RummyTournamentsListener;
import in.glg.rummy.api.response.RummyTournamentsTablesResponse;
import in.glg.rummy.api.response.RummyTourneyRegistrationResponse;
import in.glg.rummy.api.response.RummyWaitListResponse;
import in.glg.rummy.api.response.RummyWinnerResponse;
import in.glg.rummy.engine.RummyGameEngine;
import in.glg.rummy.enums.RummyGameEvent;
import in.glg.rummy.exceptions.RummyGameEngineNotRunning;
import in.glg.rummy.models.RummyEvent;
import in.glg.rummy.models.RummyGamePlayer;
import in.glg.rummy.models.RummyJoinedPlayers;
import in.glg.rummy.models.RummyLevels;
import in.glg.rummy.models.RummyPrizeList;
import in.glg.rummy.models.RummyWaitingPlayers;
import in.glg.rummy.utils.RummyApiCallHelper;
import in.glg.rummy.utils.RummyApiResult;
import in.glg.rummy.utils.RummyCommonEventTracker;
import in.glg.rummy.utils.RummyTLog;
import in.glg.rummy.utils.RummyTourneyApiHelper;
import in.glg.rummy.utils.RummyUtils;

public class RummyTournamentDetailsFragment extends RummyBaseFragment implements View.OnClickListener {
    private String TAG = RummyTournamentDetailsFragment.class.getName();


    private RelativeLayout rl_schedules_label;
    private RelativeLayout rl_tables_label;
    private RelativeLayout rl_cash_prize_label;
    private RelativeLayout rl_joined_player_label;
    private RelativeLayout rl_waitlist_label;

    private LinearLayout llSchedulesContent;
    private LinearLayout llTablesContent;
    private LinearLayout llPrizeListContent;
    private LinearLayout llJoinedPlayerContent;
    private LinearLayout llWaitlistContent;

    private String mTourneyId;
    private FragmentActivity mContext;
    private RummyApplication mRummyApplication;
    private RummyLoginResponse userData;
    private List<RummyLevels> mLevels;
    private List<RummyPrizeList> mPrizeList;
    private List<RummyJoinedPlayers> mJoinedPlayers;
    private List<RummyWaitingPlayers> mWaitingPlayers;

    private RummyTournamentDetailsResponse mTourneyDetailsResponse;
    private RummyTourneyRegistrationResponse mTourneyRegistrationResponse;
    private RummyTournamentsListener mTournamentsListener;
    private RummyTournamentDetailsResponse mLevelsResponse;

    private Button register_btn_players;
    private Button register_btn_schedules;
    private Button deregister_btn_players;
    private Button deregister_btn_schedules;
    private Button deregister_btn_info;
    private Button register_btn_info;

    private RecyclerView levelsList;
    private RecyclerView prizeList;
    private RecyclerView joinedPlayersList;
    private RecyclerView waitingPlayersList;
    private RecyclerView tourneyTablesList;

    private LinearLayout mPrizeInfo_label;
    private LinearLayout mSchedulesTables_label;
    private LinearLayout mPlayers_label;
    private ImageView mPrizeInfo_iv;
    private ImageView mSchedules_iv;
    private ImageView mPlayers_iv;
    private TextView mPrizeInfo_tv;
    private TextView mSchedules_tv;
    private TextView mPlayers_tv;


    private TextView tableLabel;
    private TextView schedulesLabel_tv;
    private TextView schedulesValue_tv;
    private TextView joinedPlayers_tv;
    private TextView waitingPlayers_tv;
    private TextView playerInfo_tv;
    private TextView tourneyInfo_tv;
    private TextView prizeAmountLabel_tv;
    private TextView positionLabel_tv;
    private TextView playerLabel_tv;
    private TextView levelStatus_tv;
    private TextView levelCountdown_tv;
    private TextView tourneyStart_tv;

    private TextView tid_tv;
    private TextView tourneyType_tv;
    private TextView entryFee_tv;
    private TextView joined_tv;
    private TextView regCloses_tv;
    private TextView balance_tv;
    private TextView yourRank_tv;
    private TextView level_tv;
    private TextView timeBetweenLevels_tv;
    private TextView status_tv;
    private TextView tourneyCompleteTime_tv;

    private LinearLayout runningStatus;
    private LinearLayout completedStatus;
    private LinearLayout othersStatus;

    private Dialog mLoadingDialog;

    private static boolean prizeInfoSelected = true;
    private static boolean schedulesSelected = false;
    private static boolean playersSelected = false;

    private RummyLevelsAdapter levelsAdapter;
    private RummyPrizeListAdapter prizeListAdapter;
    private RummyWinnersAdapter winnersAdapter;
    private RummyJoinedPlayersAdapter joinedPlayersAdapter;
    private RummyWaitingPlayersAdapter waitingPlayersAdapter;
    private RummyTourneyTablesAdapter tourneyTablesAdapter;

    private TextView tourney_name;
    private TextView tvTotalPrizeMoney;

    private TextView tv_start_day_tourney;
    private TextView tv_start_hour_tourney;
    private TextView tv_start_min_tourney;
    private TextView tv_start_sec_tourney;

    private LinearLayout llCompleteTourneyButton;
    private TextView tv_tourney_compl_time;
    private TextView tv_reg_starts;
    private ImageView iv_tourney_compl_image;


    CountDownTimer levelTimer;
    CountDownTimer tournamentStartTimer;

    RefreshTournamentsList mCallback;

    private String mCurrentTourneyChips = "";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (RefreshTournamentsList) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement RefreshTournamentsList");
        }
    }

    private RummyOnResponseListener tournamentsResponseListener = new RummyOnResponseListener(RummyTournamentsListener.class) {
        @Override
        public void onResponse(Object response) {
            if (response != null) {
                RummyTournamentDetailsFragment.this.mTournamentsListener = (RummyTournamentsListener) response;

                if (RummyTournamentDetailsFragment.this.mTourneyDetailsResponse.getCode().equalsIgnoreCase("200")) {
                    Log.d(TAG, "Received at: " + tournamentsResponseListener);
                } else {
                    Log.d(TAG, RummyTournamentDetailsFragment.this.mTourneyDetailsResponse.getCode() +
                            " : tournamentsResponseListener");
                }
            }
        }
    };

    private RummyOnResponseListener tournamentsDetailsListener = new RummyOnResponseListener(RummyTournamentDetailsResponse.class) {
        @Override
        public void onResponse(Object response) {
            try {
                if (response != null) {
                    RummyTournamentDetailsFragment.this.mTourneyDetailsResponse = (RummyTournamentDetailsResponse) response;

                    if (RummyTournamentDetailsFragment.this.mTourneyDetailsResponse.getCode().equalsIgnoreCase("200")) {
                        RummyTournamentDetailsFragment.this.mLevels = ((RummyTournamentDetailsResponse) response).getLevels();
                        RummyTournamentDetailsFragment.this.displayTourneyPrizeInfoData();
                        RummyTournamentDetailsFragment.this.populateLevelsData();
                        RummyTournamentDetailsFragment.this.setTimeBetweenNextLevel();
                        RummyTournamentDetailsFragment.this.getLeaderBoard();
                    } else {
                        Log.d(TAG, RummyTournamentDetailsFragment.this.mTourneyDetailsResponse.getCode() +
                                " : tournamentsDetailsListener");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e + "");
            }
        }
    };

    private RummyOnResponseListener winnersResponse = new RummyOnResponseListener(RummyWinnerResponse.class) {
        @Override
        public void onResponse(Object response) {
            if (response != null) {
                if (((RummyWinnerResponse) response).getCode().equalsIgnoreCase("200")) {
                    RummyTournamentDetailsFragment.this.showWinnersList(((RummyWinnerResponse) response).getPlayers());
                } else {
                    Log.d(TAG, RummyTournamentDetailsFragment.this.mTourneyDetailsResponse.getCode() +
                            " : winnersResponse");
                }
            }
        }
    };

    private RummyOnResponseListener tournamentTablesResponse = new RummyOnResponseListener(RummyTournamentsTablesResponse.class) {
        @Override
        public void onResponse(Object response) {
            if (response != null) {
                if (((RummyTournamentsTablesResponse) response).getCode().equalsIgnoreCase("200")) {
                    RummyTournamentDetailsFragment.this.showRunningTables((RummyTournamentsTablesResponse) response);
                    //TournamentDetailsFragment.this.getLeaderBoard();
                } else {
                    Log.d(TAG, ((RummyTournamentsTablesResponse) response).getCode() +
                            " : tournamentTablesResponse");
                }
            }
        }
    };

    private RummyOnResponseListener leaderBoardResponse = new RummyOnResponseListener(RummyJoinedPlayersResponse.class) {
        @Override
        public void onResponse(Object response) {
            try {
                if (response != null) {
                    if (((RummyJoinedPlayersResponse) response).getCode().equalsIgnoreCase("200")) {
                        RummyTournamentDetailsFragment.this.mJoinedPlayers = ((RummyJoinedPlayersResponse) response).getJoinedPlayers();
                        RummyTournamentDetailsFragment.this.populateJoinedPlayers();
                        RummyTournamentDetailsFragment.this.getPlayerWaitList();
                    } else {
                        Log.d(TAG, ((RummyJoinedPlayersResponse) response).getCode() +
                                " : leaderBoardResponse");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e + "");
            }
        }
    };

    private RummyOnResponseListener prizeListResponse = new RummyOnResponseListener(RummyPrizeListResponse.class) {
        @Override
        public void onResponse(Object response) {
            if (response != null) {
                if (((RummyPrizeListResponse) response).getCode().equalsIgnoreCase("200")) {
                    RummyTournamentDetailsFragment.this.mPrizeList = ((RummyPrizeListResponse) response).getPrize_list();
                    RummyTournamentDetailsFragment.this.populatePrizeList();

                    if (RummyTournamentDetailsFragment.this.mTourneyDetailsResponse.getStatus().equalsIgnoreCase("running"))
                        RummyTournamentDetailsFragment.this.getLevelTimer();
                } else {
                    Log.d(TAG, ((RummyPrizeListResponse) response).getCode() +
                            "prizeListResponse");
                }
            }
        }
    };

    private RummyOnResponseListener waitListResponse = new RummyOnResponseListener(RummyWaitListResponse.class) {
        @Override
        public void onResponse(Object response) {
            if (response != null) {
                if (((RummyWaitListResponse) response).getCode().equalsIgnoreCase("200")) {
                    RummyTournamentDetailsFragment.this.mWaitingPlayers = ((RummyWaitListResponse) response).getWaitingPlayers();
                    RummyTournamentDetailsFragment.this.populateWaitingPlayers();
                    if (mTourneyDetailsResponse != null) {
                        if (RummyTournamentDetailsFragment.this.mTourneyDetailsResponse.getStatus().equalsIgnoreCase("completed"))
                            RummyTournamentDetailsFragment.this.getWinnerList();
                        else
                            RummyTournamentDetailsFragment.this.getPrizeList();
                    } else {
                        if (RummyTournamentDetailsFragment.this.getViewLifecycleOwner().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
                            getTournamentDetails();
                    }
                } else {
                    Log.d(TAG, ((RummyWaitListResponse) response).getCode() + "waitListResponse");
                }
            }
        }
    };

    private RummyOnResponseListener levelsTimerListener = new RummyOnResponseListener(RummyTournamentDetailsResponse.class) {
        @Override
        public void onResponse(Object response) {
            if (response != null) {
                RummyTournamentDetailsFragment.this.mLevelsResponse = (RummyTournamentDetailsResponse) response;

                if (RummyTournamentDetailsFragment.this.mLevelsResponse.getCode().equalsIgnoreCase("200")) {
                    if (RummyTournamentDetailsFragment.this.mLevelsResponse.getData().equalsIgnoreCase("get_level_timer")) {
                        Log.d(TAG, "SETTING LEVEL TIMER");
                        RummyTournamentDetailsFragment.this.setLevelTimer();
                        RummyTournamentDetailsFragment.this.getTournamentTables();
                    }
                } else {
                    Log.d(TAG, RummyTournamentDetailsFragment.this.mLevelsResponse.getCode() + " : tournamentsDetailsListener");
                }
            }
        }
    };

    private RummyOnResponseListener tournamentRegistrationResponse = new RummyOnResponseListener(RummyTourneyRegistrationResponse.class) {
        @Override
        public void onResponse(Object response) {
            if (response != null) {
                RummyTournamentDetailsFragment.this.hideProgress();
                RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse = (RummyTourneyRegistrationResponse) response;

                if (RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse != null
                        && RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse.getCode().equalsIgnoreCase("200")) {
                    if (RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse.getData().equalsIgnoreCase("register_tournament")) {
                        RummyTournamentDetailsFragment.this.mTourneyDetailsResponse.setTourney_chips(RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse.getTournament_chips());

                            RummyCommonEventTracker.trackTournamentRegisterDeRegister(RummyCommonEventTracker.Tournament_Register, RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse.getUser_id(),
                                    RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse.getTournament_id(), RummyTournamentDetailsFragment.this.mCurrentTourneyChips, RummyTournamentDetailsFragment.this.mContext);
                        RummyTournamentDetailsFragment.this.getTournamentDetails();
                        RummyTournamentDetailsFragment.this.showGenericDialogWithMessage("Congratulations! You have been registered for the tourney and you have " + ((RummyTourneyRegistrationResponse) response).getTournament_chips() + " T. chips for the tournament.");
                    } else if (RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse.getData().equalsIgnoreCase("deregister_tournament")) {


                            RummyCommonEventTracker.trackTournamentRegisterDeRegister(RummyCommonEventTracker.Tournament_DE_Register, RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse.getUser_id(),
                                    RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse.getTournament_id(), RummyTournamentDetailsFragment.this.mCurrentTourneyChips, RummyTournamentDetailsFragment.this.mContext);
                        RummyTournamentDetailsFragment.this.getTournamentDetails();
                        RummyTournamentDetailsFragment.this.showGenericDialogWithMessage("Deregistered from tournament !");
                    }

                } else if (RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse != null) {
                    if (RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse.getCode().equalsIgnoreCase("7013")) {
                        RummyTournamentDetailsFragment.this.showGenericDialogWithMessage("You cannot cancel your registration request since the tourney is going to start");
                    } else if (RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse.getCode().equalsIgnoreCase("507")) {
                        if (mTourneyDetailsResponse.getTourney_cost().equalsIgnoreCase("LOYALTYPOINTS_CASH"))
                            RummyTournamentDetailsFragment.this.showErrorLoyaltyChipsDialog(RummyTournamentDetailsFragment.this.mContext, "You don't have sufficient loyalty chips to register for this tourney.");
                        else
                            RummyTournamentDetailsFragment.this.showErrorBuyChipsDialog(RummyTournamentDetailsFragment.this.mContext, "You don't have sufficient balance to register for this tourney.");
                    } else if (RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse.getCode().equalsIgnoreCase("7046")) {
                        RummyTournamentDetailsFragment.this.showGenericDialogWithMessage("Please verify your Email/phone in My Account");
                    } else if (RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse.getCode().equalsIgnoreCase("7042")) {
                        RummyTournamentDetailsFragment.this.showGenericDialogWithMessage("Please verify your Email in My Account");
                    } else if (RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse.getCode().equalsIgnoreCase("7041")) {
                        RummyTournamentDetailsFragment.this.showErrorBuyChipsDialog(RummyTournamentDetailsFragment.this.mContext, "To register for this tourney, you need to have a minimum deposit within last 7 days.");
                    } else if (RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse.getCode().equalsIgnoreCase("7028")) {
                        RummyTournamentDetailsFragment.this.showGenericDialogWithMessage("Registrations for this tournament have been blocked in your state");
                    } else if (RummyTournamentDetailsFragment.this.mTourneyRegistrationResponse.getCode().equalsIgnoreCase("7043")) {
                        RummyTournamentDetailsFragment.this.showGenericDialogWithMessage("Please verify your Mobile Number to play tournaments.");
                    }
                }
            }
        }
    };

    private void setLevelTimer() {
        try {
            this.othersStatus.setVisibility(View.GONE);
            this.completedStatus.setVisibility(View.GONE);
            this.runningStatus.setVisibility(View.VISIBLE);

            if (this.levelTimer != null)
                this.levelTimer.cancel();

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa");
            this.levelStatus_tv.setText(this.mLevelsResponse.getLeveldetails());

            Date start = new Date();
            start = sdf.parse(sdf.format(start));
            Date end = sdf.parse(RummyUtils.convertTimeStampToAnyDateFormat(this.mLevelsResponse.getLeveltimer(), "hh:mm:ss aa"));

            long millis = end.getTime() - start.getTime();

            this.levelTimer = new CountDownTimer(millis, 1000) {

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

                    levelCountdown_tv.setText(strMins + ":" + strSeconds);
                }

                public void onFinish() {
                    //Log.d("local","done!");
                }

            }.start();
        } catch (Exception e) {
            Log.e(TAG, "EXP: setLevelTimer-->> " + e.toString());
        }
    }

    private void setTourneyStartTimer() {
        try {

            if (this.tournamentStartTimer != null)
                this.tournamentStartTimer.cancel();

            SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy hh:mm:ss aa");

            Date start = new Date();
            start = sdf.parse(sdf.format(start));
            Date end = sdf.parse(RummyUtils.convertTimeStampToAnyDateFormat(this.mTourneyDetailsResponse.getTournament_start_date(), "dd MM yyyy hh:mm:ss aa"));

            long millis = end.getTime() - start.getTime();

            if(millis>0) {
                this.tournamentStartTimer = new CountDownTimer(millis, 1000) {

                    public void onTick(long millisUntilFinished) {

                        long seconds = millisUntilFinished / 1000;
                        long minutes = seconds / 60;
                        long hours = minutes / 60;
                        long days = hours / 24;
                        String time = days + ":" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60;

                        String dayVal = "00", hourVal = "00", minVal = "00", secVal = "00";
                        if (days + "".length() == 1)
                            dayVal = "0" + days;
                        else
                            dayVal = days + "";
                        if ((hours % 24) + "".length() == 1)
                            hourVal = "0" + (hours % 24);
                        else
                            hourVal = (hours % 24) + "";
                        if ((minutes % 60) + "".length() == 1)
                            minVal = "0" + (minutes % 60);
                        else
                            minVal = (minutes % 60) + "";
                        if ((seconds % 60) + "".length() == 1)
                            secVal = "0" + (seconds % 60);
                        else
                            secVal = (seconds % 60) + "";

                        tv_start_day_tourney.setText(dayVal);
                        tv_start_hour_tourney.setText(hourVal);
                        tv_start_min_tourney.setText(minVal);
                        tv_start_sec_tourney.setText(secVal);

                    /*long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);

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
                        strSeconds = String.valueOf(seconds);*/

                        //levelCountdown_tv.setText(strMins + ":" + strSeconds);
                    }

                    public void onFinish() {
                        tv_start_day_tourney.setText("00");
                        tv_start_hour_tourney.setText("00");
                        tv_start_min_tourney.setText("00");
                        tv_start_sec_tourney.setText("00");
                    }

                }.start();
            }
            else
            {
                tv_start_day_tourney.setText("00");
                tv_start_hour_tourney.setText("00");
                tv_start_min_tourney.setText("00");
                tv_start_sec_tourney.setText("00");
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: tourney start timer-->> " + e.toString());
        }
    }

    private void getWinnerList() {
        try {
            RummyTournamentsDetailsRequest request = new RummyTournamentsDetailsRequest();
            request.setCommand("prize_distribution");
            request.setUuid(RummyUtils.generateUuid());
            request.setTournament_id(this.mTourneyId);

            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mContext.getApplicationContext(), RummyUtils.getObjXMl(request), this.winnersResponse);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mContext.getApplicationContext(), R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.e(TAG, "getWinnerList" + gameEngineNotRunning.getLocalizedMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: getWinnerList-->> " + e.toString());
        }
    }

    private void showWinnersList(List<RummyGamePlayer> players) {
        hideView(this.othersStatus);
        hideView(this.runningStatus);
        showView(this.completedStatus);

        showView(this.playerLabel_tv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        prizeList.setLayoutManager(mLayoutManager);
        prizeList.setItemAnimator(new DefaultItemAnimator());
        this.winnersAdapter = new RummyWinnersAdapter(this.mContext, players, this);
        this.prizeList.setAdapter(winnersAdapter);
        if (this.winnersAdapter != null) {
            this.winnersAdapter.notifyDataSetChanged();
        }
    }

    private void toggleDeregisterButtonVisibility(int visibility) {
        this.deregister_btn_players.setVisibility(visibility);
        this.deregister_btn_schedules.setVisibility(visibility);
        this.deregister_btn_info.setVisibility(visibility);
    }

    private void toggleRegisterButtonVisibility(int visibility) {
        this.register_btn_players.setVisibility(visibility);
        this.register_btn_schedules.setVisibility(visibility);
        this.register_btn_info.setVisibility(visibility);
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
                    launchTLFragment(new RummyTournamentsFragment(), RummyTournamentsFragment.class.getName());
                } else
                    dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void populateJoinedPlayers() {
        if(this.mJoinedPlayers!=null) {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            joinedPlayersList.setLayoutManager(mLayoutManager);
            joinedPlayersList.setItemAnimator(new DefaultItemAnimator());
            this.joinedPlayersAdapter = new RummyJoinedPlayersAdapter(this.mContext, this.mJoinedPlayers, this);
            this.joinedPlayersList.setAdapter(joinedPlayersAdapter);
            if (this.joinedPlayersAdapter != null) {
                this.joinedPlayersAdapter.notifyDataSetChanged();
            }

            if (mJoinedPlayers.size() > 0)
                joined_tv.setText(this.mJoinedPlayers.size() + "/" + this.mTourneyDetailsResponse.getMax_registration());
            else
                joined_tv.setText("0/" + this.mTourneyDetailsResponse.getMax_registration());

            if (mTourneyDetailsResponse.getStatus().equalsIgnoreCase("running") || mTourneyDetailsResponse.getStatus().equalsIgnoreCase("completed")) {
                for (RummyJoinedPlayers player : this.mJoinedPlayers) {
                    if (player.getNick_name().equalsIgnoreCase(userData.getNickName())) {
                        Log.d("flow", "INSIDE THERE");
                        this.yourRank_tv.setText(player.getRank());
                        break;
                    }
                }
            }
        }
    }

    private void populateWaitingPlayers() {
        if(this.mWaitingPlayers!=null) {
            this.waitingPlayersAdapter = new RummyWaitingPlayersAdapter(this.mContext, this.mWaitingPlayers, this);
            this.waitingPlayersList.setAdapter(waitingPlayersAdapter);
            this.waitingPlayersList.setLayoutManager(new LinearLayoutManager(requireContext()));
            if (this.waitingPlayersAdapter != null) {
                this.waitingPlayersAdapter.notifyDataSetChanged();
            }
        }
    }

    private void populateLevelsData() {

        if(this.mLevels!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                levelsList.setNestedScrollingEnabled(true);
            }

            this.levelsAdapter = new RummyLevelsAdapter(this.mContext, this.mLevels, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            levelsList.setLayoutManager(mLayoutManager);
            levelsList.setItemAnimator(new DefaultItemAnimator());
            this.levelsList.setAdapter(levelsAdapter);
            if (this.levelsAdapter != null) {
                this.levelsAdapter.notifyDataSetChanged();
            }
        }
    }

    private void populatePrizeList() {
        try {
            if(this.mPrizeList!=null) {
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                prizeList.setLayoutManager(mLayoutManager);
                prizeList.setItemAnimator(new DefaultItemAnimator());
                this.prizeListAdapter = new RummyPrizeListAdapter(this.mContext, this.mPrizeList, this, this.mTourneyDetailsResponse.getStatus());
                this.prizeList.setAdapter(prizeListAdapter);
                if (this.prizeListAdapter != null) {
                    this.prizeListAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,e+"");
        }
    }

    private void displayTourneyPrizeInfoData() {
        try {
            String strStatus = this.mTourneyDetailsResponse.getStatus();

            setTourneyStartTimer();

            this.tid_tv.setText(mTourneyDetailsResponse.getTournament_id());
           // this.entryFee_tv.setText(mTourneyDetailsResponse.getEntry());
            this.regCloses_tv.setText(RummyUtils.convertTimeStampToAnyDateFormat(mTourneyDetailsResponse.getTime_to_close_registrations(), "dd MMM yyyy | hh:mm aa").toUpperCase());
            this.tourneyStart_tv.setText(RummyUtils.convertTimeStampToAnyDateFormat(mTourneyDetailsResponse.getTournament_start_date(), "dd MMM yyyy").toUpperCase()+"\n"+
                    RummyUtils.convertTimeStampToAnyDateFormat(mTourneyDetailsResponse.getTournament_start_date(), "hh:mm aa").toUpperCase());
            this.tv_reg_starts.setText(RummyUtils.convertTimeStampToAnyDateFormat(mTourneyDetailsResponse.getRegistrations_start_date(), "dd MMM hh:mm aa").toUpperCase());
            this.level_tv.setText(mTourneyDetailsResponse.getCurrent_level());

            String[] value = mTourneyDetailsResponse.getTournament_name().split("_");
            if (value != null && value.length == 2) {
                tourney_name.setText(value[0]);
            } else
                this.tourney_name.setText(mTourneyDetailsResponse.getTournament_name());

            if(mTourneyDetailsResponse.getEntry().equalsIgnoreCase("0") || mTourneyDetailsResponse.getEntry().equalsIgnoreCase("0.0")) {
                this.entryFee_tv.setText("FREE");
                tvTotalPrizeMoney.setText(mTourneyDetailsResponse.getCash_prize());

            }
            else {
                this.entryFee_tv.setText(mTourneyDetailsResponse.getEntry());
                tvTotalPrizeMoney.setText(mContext.getResources().getString(R.string.rupee_symbol)+mTourneyDetailsResponse.getCash_prize());

            }

            if (mTourneyDetailsResponse.getTournament_type().equalsIgnoreCase("REGURLAR"))
                this.tourneyType_tv.setText("Regular");
            else
                this.tourneyType_tv.setText(RummyUtils.toTitleCase(mTourneyDetailsResponse.getTournament_type().toUpperCase()));

            if (strStatus.equalsIgnoreCase("canceled")) {
                this.yourRank_tv.setText("0");
                this.balance_tv.setText("NA");
                this.status_tv.setText("Cancelled");
            } else if (strStatus.equalsIgnoreCase("registration open") || strStatus.equalsIgnoreCase("announced")
                    || strStatus.equalsIgnoreCase("registrations closed")) {
                this.yourRank_tv.setText("0");
                this.balance_tv.setText(mTourneyDetailsResponse.getTourney_chips());
                this.status_tv.setText(RummyUtils.toTitleCase(mTourneyDetailsResponse.getStatus()));
            } else {
                Log.d("flow", "INSIDE HERE");
                this.yourRank_tv.setText("0");
                this.balance_tv.setText("NA");
                this.status_tv.setText(RummyUtils.toTitleCase(mTourneyDetailsResponse.getStatus()));
            }

            if (strStatus.equalsIgnoreCase("canceled") || strStatus.equalsIgnoreCase("announced") || strStatus.equalsIgnoreCase("running")
                    || strStatus.equalsIgnoreCase("completed") || strStatus.equalsIgnoreCase("registrations closed")) {
                this.toggleRegisterButtonVisibility(View.GONE);
                this.toggleDeregisterButtonVisibility(View.GONE);

                if(strStatus.equalsIgnoreCase("completed"))
                {
                    llCompleteTourneyButton.setVisibility(View.VISIBLE);
                    iv_tourney_compl_image.setVisibility(View.VISIBLE);
                    this.tv_tourney_compl_time.setText(RummyUtils.convertTimeStampToAnyDateFormat(mTourneyDetailsResponse.getTime_to_close_registrations(), "dd MMM yyyy | hh:mm aa").toUpperCase());
                }

            } else {
                if (this.mTourneyDetailsResponse.getRegistered().equalsIgnoreCase("YES")) {
                    if (RummyUtils.compareDateWithCurrentDate(this.mTourneyDetailsResponse.getCancel_or_withdraw_registration_time()).equalsIgnoreCase(RummyUtils.DATE_IS_BEFORE)) {
                        this.toggleRegisterButtonVisibility(View.GONE);
                        this.toggleDeregisterButtonVisibility(View.GONE);
                    } else {
                        this.toggleRegisterButtonVisibility(View.GONE);
                        this.toggleDeregisterButtonVisibility(View.VISIBLE);
                    }
                } else if (this.mTourneyDetailsResponse.getStatus_in_tourney().equalsIgnoreCase("NotRegistered")) {
                    this.toggleRegisterButtonVisibility(View.VISIBLE);
                    this.toggleDeregisterButtonVisibility(View.GONE);
                }
            }

            if (mLevels.size() > 0 && this.mTourneyDetailsResponse.getMy_current_level() != null) {
                //this.levelStatus_tv.setText(this.mTourneyDetailsResponse.getMy_current_level()+"/"+mLevels.size());
            }

            this.schedulesValue_tv.setText("(" + RummyUtils.convertTimeStampToAnyDateFormat(mTourneyDetailsResponse.getTournament_start_date(), "dd-MMM hh:mm aa") + ")");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e + "");
        }

    }

    private void setTimeBetweenNextLevel() {
        int currentLevel = Integer.parseInt(this.mTourneyDetailsResponse.getCurrent_level());
        for (int i = 0; i < this.mLevels.size(); i++) {
            /*if(currentLevel == (i-1))
            {
                int he = (int) Float.parseFloat(this.mLevels.get(i - 1).getDelay_between_level());
                this.timeBetweenLevels_tv.setText(String.valueOf(he) + " sec");
            }*/
            if (i == 0) {
                int he = (int) Float.parseFloat(this.mLevels.get(i).getDelay_between_level());
                this.timeBetweenLevels_tv.setText(String.valueOf(he) + " sec");
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.rummy_fragment_tourney_details_new, container, false);

        this.mTourneyId = getArguments().getString("tourneyID");

        setIdsToViews(v);
        init();
        setListeners();
        handleBackButton(v);

        if (RummyGameEngine.getInstance().isSocketConnected()) {
            getTournamentDetails();
        }

        ImageView back_button = (ImageView) v.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TournamentDetailsFragment.this.popFragment(TournamentDetailsFragment.class.getName());
                launchTLFragment(new RummyTournamentsFragment(), RummyTournamentsFragment.class.getName());
                mCallback.refreshTourneyList();
            }
        });

        return v;
    }

    private void getTournamentTables() {
        try {
            RummyTournamentsDetailsRequest request = new RummyTournamentsDetailsRequest();
            request.setCommand("get_tournament_tables");
            request.setUuid(RummyUtils.generateUuid());
            request.setTournament_id(this.mTourneyId);

            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mContext.getApplicationContext(), RummyUtils.getObjXMl(request), this.tournamentTablesResponse);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mContext.getApplicationContext(), R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.e(TAG, "getTournamentTables" + gameEngineNotRunning.getLocalizedMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: getTournamentTables-->> " + e.toString());
        }
    }

    private boolean checkFragmentVisible() {
        return this.getFragmentManager().findFragmentByTag(RummyTournamentDetailsFragment.class.getName()).isVisible();
    }

    private void setListeners() {
        this.mPrizeInfo_label.setOnClickListener(this);
        this.mSchedulesTables_label.setOnClickListener(this);
        this.mPlayers_label.setOnClickListener(this);
        this.register_btn_players.setOnClickListener(this);
        this.register_btn_schedules.setOnClickListener(this);
        this.deregister_btn_players.setOnClickListener(this);
        this.deregister_btn_schedules.setOnClickListener(this);
        this.register_btn_info.setOnClickListener(this);
        this.deregister_btn_info.setOnClickListener(this);

        this.rl_schedules_label.setOnClickListener(this);
        this.rl_tables_label.setOnClickListener(this);
        this.rl_cash_prize_label.setOnClickListener(this);
        this.rl_joined_player_label.setOnClickListener(this);
        this.rl_waitlist_label.setOnClickListener(this);
    }

    private void init() {
        this.mContext = getActivity();
        this.mRummyApplication = RummyApplication.getInstance();
        RummyApplication app = RummyApplication.getInstance();
        if (app != null) {
            this.userData = app.getUserData();
        }

        this.tableLabel.setPaintFlags(this.tableLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        this.schedulesLabel_tv.setPaintFlags(this.schedulesLabel_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        this.schedulesValue_tv.setPaintFlags(this.schedulesValue_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        this.joinedPlayers_tv.setPaintFlags(this.joinedPlayers_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        this.waitingPlayers_tv.setPaintFlags(this.waitingPlayers_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        this.playerInfo_tv.setPaintFlags(this.playerInfo_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        this.prizeAmountLabel_tv.setPaintFlags(this.prizeAmountLabel_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        this.playerLabel_tv.setPaintFlags(this.playerLabel_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        this.positionLabel_tv.setPaintFlags(this.positionLabel_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        this.tourneyInfo_tv.setPaintFlags(this.tourneyInfo_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        prizeInfoSelected = true;
        schedulesSelected = false;
        playersSelected = false;
    }

    private void hideProgress() {
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();
    }

    private void showProgress() {
        mLoadingDialog = new Dialog(mContext, R.style.DialogTheme);
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLoadingDialog.setContentView(R.layout.rummy_dialog_loading);
        mLoadingDialog.setCanceledOnTouchOutside(false);

        mLoadingDialog.show();
    }

    private void handleBackButton(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode != 4) {
                    return false;
                }
                //TournamentDetailsFragment.this.popFragment(TournamentDetailsFragment.class.getName());
                launchTLFragment(new RummyTournamentsFragment(), RummyTournamentsFragment.class.getName());
                mCallback.refreshTourneyList();
                return true;
            }
        });
    }

    public void launchTLFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_content_frame, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void setIdsToViews(View v) {
        this.mPrizeInfo_label = (LinearLayout) v.findViewById(R.id.prizeInfo_label);
        this.mSchedulesTables_label = (LinearLayout) v.findViewById(R.id.schedulesTables_label);
        this.mPlayers_label = (LinearLayout) v.findViewById(R.id.players_label);
        this.mPrizeInfo_iv = (ImageView) v.findViewById(R.id.prizeInfo_iv);
        this.mSchedules_iv = (ImageView) v.findViewById(R.id.schedules_iv);
        this.mPlayers_iv = (ImageView) v.findViewById(R.id.players_iv);
        this.mPrizeInfo_tv = (TextView) v.findViewById(R.id.prizeInfo_tv);
        this.mSchedules_tv = (TextView) v.findViewById(R.id.schedules_tv);
        this.mPlayers_tv = (TextView) v.findViewById(R.id.players_tv);


        this.tableLabel = (TextView) v.findViewById(R.id.tableLabel);
        this.schedulesLabel_tv = (TextView) v.findViewById(R.id.schedulesLabel_tv);
        this.schedulesValue_tv = (TextView) v.findViewById(R.id.schedulesValue_tv);
        this.joinedPlayers_tv = (TextView) v.findViewById(R.id.joinedPlayers_tv);
        this.waitingPlayers_tv = (TextView) v.findViewById(R.id.waitingPlayers_tv);
        this.tourneyInfo_tv = (TextView) v.findViewById(R.id.tourneyInfo_tv);
        this.playerInfo_tv = (TextView) v.findViewById(R.id.playerInfo_tv);
        this.prizeAmountLabel_tv = (TextView) v.findViewById(R.id.prizeAmountLabel_tv);
        this.positionLabel_tv = (TextView) v.findViewById(R.id.positionLabel_tv);
        this.playerLabel_tv = (TextView) v.findViewById(R.id.playerLabel_tv);
        this.levelStatus_tv = (TextView) v.findViewById(R.id.levelStatus_tv);
        this.levelCountdown_tv = (TextView) v.findViewById(R.id.levelCountdown_tv);
        this.tourneyStart_tv = (TextView) v.findViewById(R.id.tourneyStart_tv);

        this.tid_tv = (TextView) v.findViewById(R.id.tid_tv);
        this.tourneyType_tv = (TextView) v.findViewById(R.id.tourneyType_tv);
        this.entryFee_tv = (TextView) v.findViewById(R.id.entryFee_tv);
        this.joined_tv = (TextView) v.findViewById(R.id.joined_tv);
        this.regCloses_tv = (TextView) v.findViewById(R.id.regCloses_tv);
        this.balance_tv = (TextView) v.findViewById(R.id.balance_tv);
        this.yourRank_tv = (TextView) v.findViewById(R.id.yourRank_tv);
        this.level_tv = (TextView) v.findViewById(R.id.level_tv);
        this.timeBetweenLevels_tv = (TextView) v.findViewById(R.id.timeBetweenLevels_tv);
        this.status_tv = (TextView) v.findViewById(R.id.status_tv);
        this.tourneyCompleteTime_tv = (TextView) v.findViewById(R.id.tourneyCompleteTime_tv);

        this.levelsList = (RecyclerView) v.findViewById(R.id.levelsList);
        this.prizeList = (RecyclerView) v.findViewById(R.id.prizeList);
        this.joinedPlayersList = (RecyclerView) v.findViewById(R.id.joinedPlayersList);
        this.waitingPlayersList = (RecyclerView) v.findViewById(R.id.waitingPlayersList);
        this.tourneyTablesList = (RecyclerView) v.findViewById(R.id.tourneyTablesList);

        this.register_btn_players = (Button) v.findViewById(R.id.register_btn_players);
        this.register_btn_schedules = (Button) v.findViewById(R.id.register_btn_schedules);
        this.deregister_btn_players = (Button) v.findViewById(R.id.deregister_btn_players);
        this.deregister_btn_schedules = (Button) v.findViewById(R.id.deregister_btn_schedules);
        this.register_btn_info = (Button) v.findViewById(R.id.register_btn_info);
        this.deregister_btn_info = (Button) v.findViewById(R.id.deregister_btn_info);

        this.runningStatus = (LinearLayout) v.findViewById(R.id.runningStatus);
        this.completedStatus = (LinearLayout) v.findViewById(R.id.completedStatus);
        this.othersStatus = (LinearLayout) v.findViewById(R.id.othersStatus);

        this.tourney_name = (TextView) v.findViewById(R.id.tourney_name);
        this.tvTotalPrizeMoney = (TextView) v.findViewById(R.id.tvTotalPrizeMoney);

        this.tv_start_day_tourney = (TextView) v.findViewById(R.id.tv_start_day_tourney);
        this.tv_start_hour_tourney = (TextView) v.findViewById(R.id.tv_start_hour_tourney);
        this.tv_start_min_tourney = (TextView) v.findViewById(R.id.tv_start_min_tourney);
        this.tv_start_sec_tourney = (TextView) v.findViewById(R.id.tv_start_sec_tourney);

        this.llCompleteTourneyButton = (LinearLayout) v.findViewById(R.id.completed_ll_info);
        this.tv_tourney_compl_time = (TextView) v.findViewById(R.id.tv_tourney_comp_time);
        this.tv_reg_starts = (TextView) v.findViewById(R.id.tv_reg_starts);
        this.iv_tourney_compl_image = (ImageView) v.findViewById(R.id.iv_complete_tourney);

        this.rl_schedules_label = (RelativeLayout) v.findViewById(R.id.rl_schedules_label);
        this.rl_tables_label = (RelativeLayout) v.findViewById(R.id.rl_tables_label);
        this.rl_cash_prize_label = (RelativeLayout) v.findViewById(R.id.rl_cash_prize_label);
        this.rl_joined_player_label = (RelativeLayout) v.findViewById(R.id.rl_joined_player_label);
        this.rl_waitlist_label = (RelativeLayout) v.findViewById(R.id.rl_waitlist_label);

        this.llSchedulesContent = (LinearLayout) v.findViewById(R.id.llSchedulesContent);
        this.llTablesContent = (LinearLayout) v.findViewById(R.id.llTablesContent);
        this.llPrizeListContent = (LinearLayout) v.findViewById(R.id.llPrizeListContent);
        this.llJoinedPlayerContent = (LinearLayout) v.findViewById(R.id.llJoinedPlayerContent);
        this.llWaitlistContent = (LinearLayout) v.findViewById(R.id.llWaitlistContent);
    }

    private void getTournamentDetails() {
        try {
            RummyTournamentsDetailsRequest request = new RummyTournamentsDetailsRequest();
            request.setCommand("get_tournament_details");
            request.setUuid(RummyUtils.generateUuid());
            request.setTournament_id(this.mTourneyId);

            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mContext.getApplicationContext(), RummyUtils.getObjXMl(request), this.tournamentsDetailsListener);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mContext.getApplicationContext(), R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.e(TAG, "getTourneyDetails" + gameEngineNotRunning.getLocalizedMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: getTourneyDetails-->> " + e.toString());
        }
    }

    // joined players
    private void getLeaderBoard() {
        try {
            RummyTournamentsDetailsRequest request = new RummyTournamentsDetailsRequest();
            request.setCommand("leader_board");
            request.setUuid(RummyUtils.generateUuid());
            request.setTournament_id(this.mTourneyId);

            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mContext.getApplicationContext(), RummyUtils.getObjXMl(request), this.leaderBoardResponse);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mContext.getApplicationContext(), R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.e(TAG, "getLeaderBoard" + gameEngineNotRunning.getLocalizedMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: getLeaderBoard-->> " + e.toString());
        }
    }

    // rummy_prize List
    private void getPrizeList() {
        try {
            RummyTournamentsDetailsRequest request = new RummyTournamentsDetailsRequest();
            request.setCommand("get_prize_list");
            request.setUuid(RummyUtils.generateUuid());
            request.setTournament_id(this.mTourneyId);

            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mContext.getApplicationContext(), RummyUtils.getObjXMl(request), this.prizeListResponse);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mContext.getApplicationContext(), R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.e(TAG, "getPrizeList" + gameEngineNotRunning.getLocalizedMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: getPrizeList-->> " + e.toString());
        }
    }

    // players wait list
    private void getPlayerWaitList() {
        try {
            RummyTournamentsDetailsRequest request = new RummyTournamentsDetailsRequest();
            request.setCommand("tournament_wait_list");
            request.setUuid(RummyUtils.generateUuid());
            request.setTournament_id(this.mTourneyId);

            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mContext.getApplicationContext(), RummyUtils.getObjXMl(request), this.waitListResponse);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mContext.getApplicationContext(), R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.e(TAG, "getPlayerWaitList" + gameEngineNotRunning.getLocalizedMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: getPlayerWaitList-->> " + e.toString());
        }
    }

    private void getTableDetails(String tableId) {
        try {
            RummyGetTableDetailsRequest request = new RummyGetTableDetailsRequest();
            request.setCommand("get_table_details");
            request.setUuid(RummyUtils.generateUuid());
            request.setTable_id(tableId);

            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mContext.getApplicationContext(), RummyUtils.getObjXMl(request), this.tournamentsResponseListener);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mContext.getApplicationContext(), R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.e(TAG, "getTableDetails" + gameEngineNotRunning.getLocalizedMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: getTableDetails-->> " + e.toString());
        }
    }

    @Override
    public void onClick(View view) {
        try {
           /* if (view == mPrizeInfo_label) {
                if (!prizeInfoSelected) {
                    schedulesSelected = true;
                    playersSelected = true;
                    this.togglePrizeInfo();
                    this.toggleSchedules();
                    this.togglePlayers();
                    this.showView(this.prizeInfo_content);
                    this.hideView(schedules_content);
                    this.hideView(players_content);
                }
            } else if (view == mSchedulesTables_label) {
                if (!schedulesSelected) {
                    prizeInfoSelected = true;
                    playersSelected = true;
                    this.toggleSchedules();
                    this.togglePrizeInfo();
                    this.togglePlayers();
                    this.hideView(this.prizeInfo_content);
                    this.showView(schedules_content);
                    this.hideView(players_content);
                }
            } else if (view == mPlayers_label) {
                if (!playersSelected) {
                    prizeInfoSelected = true;
                    schedulesSelected = true;
                    this.togglePlayers();
                    this.togglePrizeInfo();
                    this.toggleSchedules();
                    this.hideView(this.prizeInfo_content);
                    this.hideView(schedules_content);
                    this.showView(players_content);
                }
            }*/if(view==rl_schedules_label)
            {
                if(schedulesSelected) {
                    llSchedulesContent.setVisibility(View.GONE);
                    schedulesSelected=false;

                    rl_schedules_label.setBackground(getResources().getDrawable(R.drawable.rummy_tourney_bottom_menu_bg));
                    ((ImageView)rl_schedules_label.findViewById(R.id.schedule_arrow_image)).setImageResource(R.drawable.rummy_arrow_right_black);
                    ((TextView)rl_schedules_label.findViewById(R.id.schedule_tv)).setTextColor(getResources().getColor(R.color.rummy_black));

                }
                else
                {
                    llSchedulesContent.setVisibility(View.VISIBLE);
                    schedulesSelected=true;

                    rl_schedules_label.setBackground(getResources().getDrawable(R.drawable.rummy_tourney_top_bar_bg));
                    ((ImageView)rl_schedules_label.findViewById(R.id.schedule_arrow_image)).setImageResource(R.drawable.rummy_arrow_down_white);
                    ((TextView)rl_schedules_label.findViewById(R.id.schedule_tv)).setTextColor(getResources().getColor(R.color.rummy_grey));


                }
            }
            else if(view==rl_tables_label)
            {
                if(llTablesContent.getVisibility()==View.VISIBLE) {
                    llTablesContent.setVisibility(View.GONE);

                    rl_tables_label.setBackground(getResources().getDrawable(R.drawable.rummy_tourney_bottom_menu_bg));
                    ((ImageView)rl_tables_label.findViewById(R.id.tables_arraw_img)).setImageResource(R.drawable.rummy_arrow_right_black);
                    ((TextView)rl_tables_label.findViewById(R.id.tables_tv)).setTextColor(getResources().getColor(R.color.rummy_black));

                }
                else
                {
                    llTablesContent.setVisibility(View.VISIBLE);

                    rl_tables_label.setBackground(getResources().getDrawable(R.drawable.rummy_tourney_top_bar_bg));
                    ((ImageView)rl_tables_label.findViewById(R.id.tables_arraw_img)).setImageResource(R.drawable.rummy_arrow_down_white);
                    ((TextView)rl_tables_label.findViewById(R.id.tables_tv)).setTextColor(getResources().getColor(R.color.rummy_grey));


                }
            }
            else if(view==rl_cash_prize_label) {
                if (llPrizeListContent.getVisibility() == View.VISIBLE) {
                    llPrizeListContent.setVisibility(View.GONE);

                    rl_cash_prize_label.setBackground(getResources().getDrawable(R.drawable.rummy_tourney_bottom_menu_bg));
                    ((ImageView)rl_cash_prize_label.findViewById(R.id.cash_prize_arraw_img)).setImageResource(R.drawable.rummy_arrow_right_black);
                    ((TextView)rl_cash_prize_label.findViewById(R.id.cash_prize_tv)).setTextColor(getResources().getColor(R.color.rummy_black));

                } else {
                    llPrizeListContent.setVisibility(View.VISIBLE);

                    rl_cash_prize_label.setBackground(getResources().getDrawable(R.drawable.rummy_tourney_top_bar_bg));
                    ((ImageView)rl_cash_prize_label.findViewById(R.id.cash_prize_arraw_img)).setImageResource(R.drawable.rummy_arrow_down_white);
                    ((TextView)rl_cash_prize_label.findViewById(R.id.cash_prize_tv)).setTextColor(getResources().getColor(R.color.rummy_grey));


                }
            }
            else if(view==rl_joined_player_label) {
                if (llJoinedPlayerContent.getVisibility() == View.VISIBLE) {
                    llJoinedPlayerContent.setVisibility(View.GONE);

                    rl_joined_player_label.setBackground(getResources().getDrawable(R.drawable.rummy_tourney_bottom_menu_bg));
                    ((ImageView)rl_joined_player_label.findViewById(R.id.joined_player_arraw_img)).setImageResource(R.drawable.rummy_arrow_right_black);
                    ((TextView)rl_joined_player_label.findViewById(R.id.joined_player_tv)).setTextColor(getResources().getColor(R.color.rummy_black));

                } else {
                    llJoinedPlayerContent.setVisibility(View.VISIBLE);

                    rl_joined_player_label.setBackground(getResources().getDrawable(R.drawable.rummy_tourney_top_bar_bg));
                    ((ImageView)rl_joined_player_label.findViewById(R.id.joined_player_arraw_img)).setImageResource(R.drawable.rummy_arrow_down_white);
                    ((TextView)rl_joined_player_label.findViewById(R.id.joined_player_tv)).setTextColor(getResources().getColor(R.color.rummy_grey));


                }
            }
            else if(view==rl_waitlist_label) {
                if (llWaitlistContent.getVisibility() == View.VISIBLE) {
                    llWaitlistContent.setVisibility(View.GONE);

                    rl_waitlist_label.setBackground(getResources().getDrawable(R.drawable.rummy_tourney_bottom_menu_bg));
                    ((ImageView)rl_waitlist_label.findViewById(R.id.waitilist_arraw_img)).setImageResource(R.drawable.rummy_arrow_right_black);
                    ((TextView)rl_waitlist_label.findViewById(R.id.waitlist_tv)).setTextColor(getResources().getColor(R.color.rummy_black));



                } else {
                    llWaitlistContent.setVisibility(View.VISIBLE);

                    rl_waitlist_label.setBackground(getResources().getDrawable(R.drawable.rummy_tourney_top_bar_bg));
                    ((ImageView)rl_waitlist_label.findViewById(R.id.waitilist_arraw_img)).setImageResource(R.drawable.rummy_arrow_down_white);
                    ((TextView)rl_waitlist_label.findViewById(R.id.waitlist_tv)).setTextColor(getResources().getColor(R.color.rummy_grey));

                }
            }
            else if (view == register_btn_players || view == register_btn_schedules || view == register_btn_info) {
                Log.e("register_btn_info","register_btn_info");
                if (this.mTourneyDetailsResponse != null) {
                    disableView(register_btn_info);
                    disableClick(register_btn_info);
                    enableView(deregister_btn_info);

                    if (this.mTourneyDetailsResponse != null && this.mTourneyDetailsResponse.getEntry() != null) {
                        if (this.mTourneyDetailsResponse.getEntry().equalsIgnoreCase("0") ||
                                this.mTourneyDetailsResponse.getEntry().equalsIgnoreCase("0.0")) {
                            this.mCurrentTourneyChips = "funchips";
                            this.registerTournament();
                        } else {
                            this.mCurrentTourneyChips = "cash";
                            this.openConfirmDialog("register");
                        }
                    }
                }
            } else if (view == deregister_btn_players || view == deregister_btn_schedules || view == deregister_btn_info) {
                if (this.mTourneyDetailsResponse != null) {
                    enableView(register_btn_info);
                    disableView(deregister_btn_info);
                    disableClick(deregister_btn_info);
                    if (this.mTourneyDetailsResponse != null && this.mTourneyDetailsResponse.getEntry() != null) {

                        if (this.mTourneyDetailsResponse.getEntry().equalsIgnoreCase("0") ||
                                this.mTourneyDetailsResponse.getEntry().equalsIgnoreCase("0.0")) {
                            this.mCurrentTourneyChips = "funchips";
                            this.deregisterTournament();
                        } else {
                            this.mCurrentTourneyChips = "cash";
                            this.openConfirmDialog("deregister");
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "@onclick " + e.toString());
        }
    }

    private void openConfirmDialog(String action) {
        final Dialog dialog = new Dialog(mContext, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rummy_dialog_leave_table);
        dialog.setCanceledOnTouchOutside(false);

        TextView dialog_msg_tv = (TextView) dialog.findViewById(R.id.dialog_msg_tv);
        Button yes_btn = (Button) dialog.findViewById(R.id.yes_btn);
        Button no_btn = (Button) dialog.findViewById(R.id.no_btn);

        //Do you want to register for the tourney? 500 Chips will be deducted from your account.
        // Are you sure, you want to unregister from TID 15129?

        if (action.equalsIgnoreCase("register")) {
            if (this.mTourneyDetailsResponse.getTourney_cost().equalsIgnoreCase("LOYALTYPOINTS_CASH"))
                dialog_msg_tv.setText("Do you want to register for the tourney? " + this.mTourneyDetailsResponse.getEntry() + " Loyalty Chips will be deducted from your account.");
            else
                dialog_msg_tv.setText("Do you want to register for the tourney? " + this.mTourneyDetailsResponse.getEntry() + " Chips will be deducted from your account.");

            yes_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    RummyTournamentDetailsFragment.this.registerTournament();
                }
            });

            no_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    enableView(register_btn_info);
                }
            });
        } else if (action.equalsIgnoreCase("deregister")) {
            dialog_msg_tv.setText("Are you sure, you want to unregister from TID " + this.mTourneyId + "?");

            yes_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    RummyTournamentDetailsFragment.this.deregisterTournament();
                }
            });

            no_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    enableView(deregister_btn_info);
                }
            });
        }

        dialog.show();

    }

    private void registerTournament() {
        try {
            this.showProgress();
           /* RummyTournamentsDetailsRequest request = new RummyTournamentsDetailsRequest();
            request.setCommand("register_tournament");
            request.setUuid(RummyUtils.generateUuid());
            request.setTournament_id(this.mTourneyId);
            request.setLevel("1");
            request.setPlayer_amount("0");
            //request.setPlayer_amount(this.mTourneyDetailsResponse.getEntry());
            request.setVipcode("None");*/

          /*  try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mContext.getApplicationContext(), RummyUtils.getObjXMl(request), this.tournamentRegistrationResponse);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mContext.getApplicationContext(), R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.e(TAG, "registerTournament" + gameEngineNotRunning.getLocalizedMessage());
                this.hideProgress();
            }*/
            RummyTourneyApiHelper.registerTourney(this.mTourneyId, result -> {
                if(result.isSuccess()){
                    RummyTournamentDetailsFragment.this.hideProgress();
                    this.mTourneyDetailsResponse.setTourney_chips(result.getResult().optString("tournament_chips"));
                    
                    RummyCommonEventTracker.trackTournamentRegisterDeRegister(RummyCommonEventTracker.Tournament_Register,
                            RummyApplication.getInstance().getUserData().getUserId(),
                            RummyTournamentDetailsFragment.this.mTourneyDetailsResponse.getTournament_id(),
                            RummyTournamentDetailsFragment.this.mCurrentTourneyChips,
                            RummyTournamentDetailsFragment.this.mContext);
                    RummyTournamentDetailsFragment.this.getTournamentDetails();
                    RummyTournamentDetailsFragment.this.
                            showGenericDialogWithMessage("Congratulations! You have been registered for the tourney and you have "
                                    + result.getResult().optString("tournament_chips")+ " T. chips for the tournament.");
                }else {
                    hideProgress();
                    try {
                        JSONObject jsonObject = new JSONObject(result.getErrorMessage());
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
            
        } catch (Exception e) {
            Log.e(TAG, "EXP: registerTournament-->> " + e.toString());
            this.hideProgress();
        }
    }

    private void deregisterTournament() {
        try {
            this.showProgress();
        /*    RummyTournamentsDetailsRequest request = new RummyTournamentsDetailsRequest();
            request.setCommand("deregister_tournament");
            request.setUuid(RummyUtils.generateUuid());
            request.setTournament_id(this.mTourneyId);
            request.setLevel("1");

            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mContext.getApplicationContext(), RummyUtils.getObjXMl(request), this.tournamentRegistrationResponse);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mContext.getApplicationContext(), R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.e(TAG, "deregisterTournament" + gameEngineNotRunning.getLocalizedMessage());
                this.hideProgress();
            }*/
            RummyTourneyApiHelper.deRegisterTourney(this.mTourneyId, result -> {
                if(result.isSuccess()){
                    RummyTournamentDetailsFragment.this.hideProgress();
    
    
                    RummyCommonEventTracker.trackTournamentRegisterDeRegister(RummyCommonEventTracker.Tournament_DE_Register,
                            RummyApplication.getInstance().getUserData().getUserId(),
                            RummyTournamentDetailsFragment.this.mTourneyDetailsResponse.getTournament_id(),
                            RummyTournamentDetailsFragment.this.mCurrentTourneyChips,
                            RummyTournamentDetailsFragment.this.mContext);
                    RummyTournamentDetailsFragment.this.getTournamentDetails();
                    RummyTournamentDetailsFragment.this.showGenericDialogWithMessage("Deregistered from tournament !");
                }else if(!result.isLoading() && !result.isConsumed()){
                    hideProgress();
                    showLongToast(getActivity(),result.getErrorMessage());
                }
            });
            
        } catch (Exception e) {
            Log.e(TAG, "EXP: deregisterTournament-->> " + e.toString());
            this.hideProgress();
        }
    }

    private void togglePrizeInfo() {
        if (prizeInfoSelected) {
            this.mPrizeInfo_tv.setTextColor(ContextCompat.getColor(this.mContext, R.color.rummy_white));
            this.mPrizeInfo_iv.setImageResource(R.drawable.rummy_prizeinfo);
            prizeInfoSelected = false;
        } else {
            this.mPrizeInfo_tv.setTextColor(ContextCompat.getColor(this.mContext, R.color.rummy_colorAccent));
            this.mPrizeInfo_iv.setImageResource(R.drawable.rummy_prizeinfo_on);
            prizeInfoSelected = true;
        }
    }

    private void toggleSchedules() {
        if (schedulesSelected) {
            this.mSchedules_tv.setTextColor(ContextCompat.getColor(this.mContext, R.color.rummy_white));
            this.mSchedules_iv.setImageResource(R.drawable.rummy_tables_off);
            schedulesSelected = false;
        } else {
            this.mSchedules_tv.setTextColor(ContextCompat.getColor(this.mContext, R.color.rummy_colorAccent));
            this.mSchedules_iv.setImageResource(R.drawable.rummy_tables_on);
            schedulesSelected = true;
        }
    }

    private void togglePlayers() {
        if (playersSelected) {
            this.mPlayers_tv.setTextColor(ContextCompat.getColor(this.mContext, R.color.rummy_white));
            this.mPlayers_iv.setImageResource(R.drawable.rummy_player);
            playersSelected = false;
        } else {
            this.mPlayers_tv.setTextColor(ContextCompat.getColor(this.mContext, R.color.rummy_colorAccent));
            this.mPlayers_iv.setImageResource(R.drawable.rummy_player_on);
            playersSelected = true;
        }
    }

    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


        RummyCommonEventTracker.trackScreenName(RummyCommonEventTracker.TOURNEY_DETAILS_SCREEN, getContext());
    }

    @Subscribe
    public void onMessageEvent(RummyEvent event) {
        String eventName = event.getEventName();
        if (eventName.equalsIgnoreCase("BALANCE_UPDATE")) {
            RummyLoginResponse userData = (RummyApplication.getInstance()).getUserData();
            userData.setFunChips(event.getFunChips());
            userData.setFunInPlay(event.getFunInPlay());
            userData.setRealChips(event.getReaChips());
            userData.setRealInPlay(event.getRealInPlay());
            userData.setLoyalityChips(event.getLoyaltyChips());
            RummyCommonEventTracker.UpdateBalaceProperty(mContext,event.getReaChips());
        
        }else {
    
            if (!eventName.equalsIgnoreCase("gamesetting_update") && !eventName.equalsIgnoreCase("HEART_BEAT") &&
                    event.getTournamentId().equalsIgnoreCase(this.mTourneyId)) {
                if (eventName.equalsIgnoreCase("stop_registration")) {
                    this.toggleDeregisterButtonVisibility(View.INVISIBLE);
                    this.toggleRegisterButtonVisibility(View.INVISIBLE);
                    this.getTournamentDetails();
            
                    if (event.getTournamentId().equalsIgnoreCase(this.mTourneyId))
                        this.showGenericDialogWithMessage("Registrations for this tournament have been closed.");
                } else if (eventName.equalsIgnoreCase("start_registration")) {
                    this.toggleDeregisterButtonVisibility(View.INVISIBLE);
                    this.toggleRegisterButtonVisibility(View.VISIBLE);
            
                    this.getTournamentDetails();
                    this.showGenericDialogWithMessage("Registrations for this tournament are now open.");
                } else if (eventName.equalsIgnoreCase("tournament_cancel")) {
                    this.status_tv.setText("Cancelled");
                    this.toggleDeregisterButtonVisibility(View.INVISIBLE);
                    this.toggleRegisterButtonVisibility(View.INVISIBLE);
            
                    this.getTournamentDetails();
                    this.showGenericDialogWithMessage("This tournament has been cancelled");
                } else if (eventName.equalsIgnoreCase("player_registered") || eventName.equalsIgnoreCase("player_deregistered")) {
                    if (event.getTournamentId().equalsIgnoreCase(this.mTourneyId))
                        this.getLeaderBoard();
                } else if (eventName.equalsIgnoreCase("stop_cancel_registration")) {
                    this.toggleDeregisterButtonVisibility(View.GONE);
            
                    if (this.mTourneyDetailsResponse.getRegistered().equalsIgnoreCase("YES"))
                        this.toggleRegisterButtonVisibility(View.INVISIBLE);
                    else
                        this.toggleRegisterButtonVisibility(View.VISIBLE);
            
                    this.showGenericDialogWithMessage("Time to cancel registrations has ended.");
                } else if (eventName.equalsIgnoreCase("start_tournament") && !this.mTourneyDetailsResponse.getRegistered().equalsIgnoreCase("yes")) {
                    this.getLevelTimer();
                    this.status_tv.setText("Running");
                    this.getTournamentDetails();
                }
            } else if (eventName.equalsIgnoreCase("BALANCE_UPDATE")) {
                RummyLoginResponse userData = (RummyApplication.getInstance()).getUserData();
                userData.setFunChips(event.getFunChips());
                userData.setFunInPlay(event.getFunInPlay());
                userData.setRealChips(event.getReaChips());
                userData.setRealInPlay(event.getRealInPlay());
                RummyCommonEventTracker.UpdateBalaceProperty(mContext, event.getReaChips());
        
            }
        }
    }

    @Subscribe
    public void onMessageEvent(RummyGameEvent event) {
        if (event.name().equalsIgnoreCase("SERVER_CONNECTED") || !event.name().equalsIgnoreCase("SERVER_DISCONNECTED")) {
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void sendReply(String msg_uuid) {
        try {
            RummyBaseReply reply = new RummyBaseReply();
            reply.setCode("200");
            reply.setUuid(msg_uuid);
            reply.setType("+OK");

            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mContext.getApplicationContext(), RummyUtils.getObjXMl(reply), this.tournamentsResponseListener);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mContext.getApplicationContext(), R.string.error_restart, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: getTableDetails-->> " + e.toString());
        }
    }

    private void updateUserData(RummyEvent event) {
        if (event != null && this.userData != null) {
            this.userData.setFunChips(event.getFunChips());
            this.userData.setFunInPlay(event.getFunInPlay());
            this.userData.setRealChips(event.getReaChips());
            this.userData.setRealInPlay(event.getRealInPlay());
        }
    }

    public interface RefreshTournamentsList {
        public void refreshTourneyList();
    }

    private void getLevelTimer() {
        try {
            RummyTournamentsDetailsRequest request = new RummyTournamentsDetailsRequest();
            request.setCommand("get_level_timer");
            request.setUuid(RummyUtils.generateUuid());
            request.setTournament_id(this.mTourneyId);

            try {
                RummyGameEngine.getInstance();
                RummyGameEngine.sendRequestToEngine(this.mContext.getApplicationContext(), RummyUtils.getObjXMl(request), this.levelsTimerListener);
            } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
                Toast.makeText(this.mContext.getApplicationContext(), R.string.error_restart, Toast.LENGTH_SHORT).show();
                RummyTLog.e(TAG, "get_level_timer" + gameEngineNotRunning.getLocalizedMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: get_level_timer-->> " + e.toString());
        }
    }

    private void showRunningTables(RummyTournamentsTablesResponse response) {
        try {
            Log.d("local", "TOTAL TABLES: " + response.getTables().size());
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                tourneyTablesList.setNestedScrollingEnabled(true);
            }
            this.tourneyTablesAdapter = new RummyTourneyTablesAdapter(this.mContext, response.getTables());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            tourneyTablesList.setLayoutManager(mLayoutManager);
            tourneyTablesList.setItemAnimator(new DefaultItemAnimator());
            this.tourneyTablesList.setAdapter(tourneyTablesAdapter);
            if (this.tourneyTablesAdapter != null) {
                this.tourneyTablesAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e(TAG, "EXP: showRunningTables-->> " + e.toString());
        }
    }
}
