package in.glg.rummy.activities;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

//import com.crashlytics.android.Crashlytics;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import in.glg.rummy.GameRoom.RummyTableActivity;
import in.glg.rummy.R;
import in.glg.rummy.RummyApplication;
import in.glg.rummy.engine.RummyGameEngine;
import in.glg.rummy.enums.RummyGameEvent;
import in.glg.rummy.fragments.RummyHomeFragmentScroller;
import in.glg.rummy.fragments.RummyLobbyFragment;
import in.glg.rummy.fragments.RummyTournamentDetailsFragment;
import in.glg.rummy.fragments.RummyTournamentsFragment;
import in.glg.rummy.interfaces.RummyHomeActivityCloseListener;
import in.glg.rummy.interfaces.RummyRefreshBankAccountList;
import in.glg.rummy.models.RummyEngineRequest;
import in.glg.rummy.models.RummyEvent;
import in.glg.rummy.models.RummyJoinedTable;
import in.glg.rummy.service.RummyNetworkChangeReceiver;
import in.glg.rummy.utils.RummyPrefManager;
import in.glg.rummy.utils.RummyTLog;
import in.glg.rummy.utils.RummyUtils;

public class RummyHomeActivity extends RummyBaseActivity implements OnCheckedChangeListener, RummyNetworkChangeReceiver.OnConnectionChangeListener, RummyRefreshBankAccountList,
        RummyTournamentDetailsFragment.RefreshTournamentsList {
    public static int flagBackKey = 0;
    private static final String TAG = RummyHomeActivity.class.getSimpleName();
    private boolean isBackPressed = false;
    private RummyApplication mApplication;
    private boolean mIsActivityVisble = false;
    private boolean mIsYourTurn = false;
    private RummyNetworkChangeReceiver mNetworkChangeReceiver;
    private RadioGroup mRadioGroup;
    private String mSelectedVariant;
    private String mSelectedPoolType="";
    private String mSelectedPlayers="";
    private String mSelectedDealType="";
    private String mFavouriteGame;
    private RelativeLayout mainLayout;

    private RummyApplication mRummyApplication;

    private RadioButton rb_home, rb_home_image;
    private RadioButton rb_lobby, rb_lobby_image;
    private RadioButton rb_tournament, rb_tournament_image;
    private RadioButton rb_tables, rb_tables_image;
    private RadioButton rb_support, rb_support_image;
    private RadioButton rb_more, rb_more_image;
    private ArrayList<RadioButton> rb_list;
    private ArrayList<RadioButton> rb_list_images;

    private List<String> tableIdList = new ArrayList<>();
    private List<String> tourneyIdList = new ArrayList<>();
    private int count=0;
    private boolean isFromExitBtn = false;

    private BroadcastReceiver mTurnUpdateReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            RummyHomeActivity.this.mIsYourTurn = intent.getBooleanExtra("turn_update", false);
            RummyHomeActivity.this.setTablesTab();
        }
    };

    RummyHomeActivityCloseListener homeActivityCloseListener = new RummyHomeActivityCloseListener() {
        @Override
        public void onClose() {
            exitLogic();
        }
    };

    static RummyHomeActivity mHomeActivity;

    public void launchFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_content_frame, fragment, tag);
        //fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void navigateToLoadingScreen(boolean isSocketDisconnected) {

        if (RummyUtils.isAppInDebugMode()) {
            Log.d(TAG, "vikas calling navigateToloadingScreen");
        }

        disableHearBeat();
        Intent intent = new Intent(this, RummyLoadingActivity.class);
        intent.putExtra("isSocketDisconnected", isSocketDisconnected);
        startActivity(intent);
    }


    private void refreshLobby() {
        if (RummyUtils.isFromSocketDisconnection && getLastCheckedRadioButton() == R.id.lobby) {
            this.rb_home.setChecked(true);
            this.rb_lobby.setChecked(true);
            //this.mRadioGroup.check(R.id.home);
            //this.mRadioGroup.check(R.id.lobby);
        }
        RummyUtils.isFromSocketDisconnection = false;
    }

    private void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public int getLastCheckedRadioButton() {
        return RummyPrefManager.getInt(this, "lastCheckedItem", R.id.home);
    }

    protected int getLayoutResource() {
        return R.layout.rummy_activity_home;
    }

    @Override
    protected int getToolbarResource() {
        return 0;
    }


    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.home) {
            saveLastCheckedRadioButton(checkedId);
            launchFragment(new RummyHomeFragmentScroller(), RummyHomeFragmentScroller.class.getName());
            toggleSelectorLabels(rb_home);
            SetAllMenuImageDefault();
            if (rb_home.isChecked())
                rb_home_image.setBackground(getResources().getDrawable(R.drawable.rummy_menu_home_selected_icon));

            return;
        } else if (checkedId == R.id.lobby) {
          /*  if (getLastCheckedRadioButton() == R.id.tournaments) {
                return;
            }*/
            saveLastCheckedRadioButton(checkedId);
            Fragment fragment = new RummyLobbyFragment();
            Bundle bundle = new Bundle();

            if (RummyUtils.GAME_ROOM_ADD_GAME_VARIANT.length() > 0) {
                this.mSelectedVariant = RummyUtils.GAME_ROOM_ADD_GAME_VARIANT;
                this.mSelectedPoolType = RummyUtils.GAME_ROOM_ADD_GAME_POOL_TYPE;
                this.mSelectedPlayers = RummyUtils.GAME_ROOM_ADD_GAME_PLAYER;

                RummyUtils.GAME_ROOM_ADD_GAME_VARIANT = "";
                RummyUtils.GAME_ROOM_ADD_GAME_POOL_TYPE = "";
                RummyUtils.GAME_ROOM_ADD_GAME_PLAYER = "";
            }

            bundle.putString("pool_type", this.mSelectedPoolType);
            bundle.putString("player_type", this.mSelectedPlayers);
            bundle.putString("game_variant", this.mSelectedVariant);
            bundle.putString("fav_variant", this.mFavouriteGame);

            fragment.setArguments(bundle);
            launchFragment(fragment, RummyLobbyFragment.class.getName());

            toggleSelectorLabels(rb_lobby);
            SetAllMenuImageDefault();
            if (rb_lobby.isChecked())
                rb_lobby_image.setBackground(getResources().getDrawable(R.drawable.rummy_menu_lobby_selected_icon));

            return;
        } else if (checkedId == R.id.tables) {
            List<RummyJoinedTable> joinedTableList = (RummyApplication.getInstance()).getJoinedTableIds();
            if (joinedTableList != null && joinedTableList.size() > 0) {
                Intent playIntent = new Intent(this, RummyTableActivity.class);
                playIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(playIntent);
                this.mRadioGroup.check(getLastCheckedRadioButton());
                return;
            }
            return;
        } else if (checkedId == R.id.more) {
                rb_more.setChecked(false);
                if(mApplication != null && mApplication.getJoinedTableIds().size() > 0){
                    exitCurrentTableDialog("Table is active in background, Do you really want to exit?");
                }else{
                    exitDialog("Are you sure you want to exit Rummy?");
                }

            return;
        } else if (checkedId == R.id.support) {
            saveLastCheckedRadioButton(checkedId);
          //  launchFragment(new SupportFragment(), SupportFragment.class.getName());
            toggleSelectorLabels(rb_support);

            SetAllMenuImageDefault();
            if (rb_support.isChecked())
                rb_support_image.setBackground(getResources().getDrawable(R.drawable.rummy_menu_support_selected_icon));

            mainLayout.setBackgroundColor(getResources().getColor(R.color.rummy_white));
            return;
        } else if (checkedId == R.id.tournaments) {
            Log.e("checkedId", checkedId + "");
            saveLastCheckedRadioButton(checkedId);
            toggleSelectorLabels(rb_tournament);
            SetAllMenuImageDefault();
            rb_tournament_image.setBackground(getResources().getDrawable(R.drawable.rummy_tourney_selected));
            launchFragment(new RummyTournamentsFragment(), RummyTournamentsFragment.class.getName());
            return;
        }
        return;
    }

    private void SetAllMenuImageDefault() {
        rb_support_image.setBackground(getResources().getDrawable(R.drawable.rummy_menu_support_icon));
       // rb_more_image.setBackground(getResources().getDrawable(R.drawable.rummy_menu_more_icon));
        rb_lobby_image.setBackground(getResources().getDrawable(R.drawable.rummy_menu_lobby_icon));
        rb_home_image.setBackground(getResources().getDrawable(R.drawable.rummy_menu_home_icon));
        rb_tournament_image.setBackground(getResources().getDrawable(R.drawable.rummy_tourney));
    }

    public void TestLog() {
        Log.e("TestLog", "TestLog");
//        saveLastCheckedRadioButton(2131297534);
        launchFragment(new RummyTournamentsFragment(), RummyTournamentsFragment.class.getName());
    }

    public void showActiveTableBeforeExitDialog(Context context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.rummy_dialog_generic);
        ((TextView) dialog.findViewById(R.id.dialog_msg_tv)).setText(message);
        ((Button) dialog.findViewById(R.id.ok_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rb_tables.performClick();
            }
        });
        ((ImageView) dialog.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void toggleSelectorLabels(RadioButton btn) {
        for (int i = 0; i < rb_list.size(); i++) {
            if (rb_list.get(i) == btn) {
                rb_list.get(i).setTextColor(getResources().getColor(R.color.rummy_colorAccent));
                //rb_list.get(i).setBackgroundColor(getResources().getColor(R.color.rummy_black));
                rb_list_images.get(i).setChecked(true);
            } else {
                rb_list.get(i).setTextColor(getResources().getColor(R.color.rummy_white));
                //rb_list.get(i).setBackgroundColor(getResources().getColor(R.color.rummy_transparent));
                rb_list_images.get(i).setChecked(false);
            }
        }
    }

    public void onConnectionChange(boolean isConnected) {
        RummyTLog.e(TAG, "isConnected :: " + isConnected);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHomeActivity = this;

        setUpFullScreen();

        setIdsToViews();

        this.mRummyApplication = (RummyApplication.getInstance());

        RummyInstance.getInstance().setHomeActivityCloseListener(homeActivityCloseListener);



      //  RummyRegistrationActivity reg = RummyRegistrationActivity.getInstance();
      /*  if (reg != null)
            reg.finish();*/

        this.mIsActivityVisble = true;
        this.mApplication = (RummyApplication.getInstance());
        if (getIntent() != null && getIntent().getBooleanExtra("isFromReg", false)) {
            showSuccessPopUp();
        }
        this.mRadioGroup = (RadioGroup) findViewById(R.id.tab_group);
        this.mRadioGroup.setOnCheckedChangeListener(this);
        this.rb_home.setChecked(true);
        //this.mRadioGroup.check(R.id.home);
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mTurnUpdateReceiver, new IntentFilter("TURN_UPDATE_EVENT"));


        RadioButton lbBtn = (RadioButton) findViewById(R.id.home);
        lbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mFavouriteGame="0";
            }
        });

        //Crashlytics.getInstance().crash();
        //calculateHashKey("com.xetanetworks.rummydangal");
    }

    private void calculateHashKey(String yourPackageName) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    yourPackageName,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }



    private void setIdsToViews() {
        rb_home = findViewById(R.id.home);
        rb_home_image = findViewById(R.id.rb_home_image);
        rb_lobby = findViewById(R.id.lobby);
        rb_lobby_image = findViewById(R.id.rb_lobby_image);
        rb_tournament = findViewById(R.id.tournaments);
        rb_tournament_image = findViewById(R.id.rb_tourney_image);
        rb_tables = findViewById(R.id.tables);
        rb_tables_image = findViewById(R.id.rb_table_image);
        rb_support = findViewById(R.id.support);
        rb_support_image = findViewById(R.id.rb_support_image);
        rb_more = findViewById(R.id.more);
        rb_more_image = findViewById(R.id.rb_more_image);
        mainLayout = findViewById(R.id.main_layout);

        rb_list = new ArrayList<>();
        rb_list.add(rb_home);
        rb_list.add(rb_lobby);
        rb_list.add(rb_tournament);
        rb_list.add(rb_tables);
        rb_list.add(rb_support);
        rb_list.add(rb_more);

        rb_list_images = new ArrayList<>();
        rb_list_images.add(rb_home_image);
        rb_list_images.add(rb_lobby_image);
        rb_list_images.add(rb_tournament_image);
        rb_list_images.add(rb_tables_image);
        rb_list_images.add(rb_support_image);
        rb_list_images.add(rb_more_image);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.disableHearBeat();
        this.unregisterEventBus();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mTurnUpdateReceiver);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("back_button", "CODE: " + keyCode);
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.e("back_button", "Count: " + count);

        if (keyCode == 4) {
            if(mApplication != null && mApplication.getJoinedTableIds().size() > 0){
                exitCurrentTableDialog("Table is active in background, Do you really want to exit?");
            }else{
                exitDialog("Do You Want To Exit?");
            }
        }
        return false;
    }

    private void exitLogic() {
        /*finish();
        RummyUtils.HOME_BACK_PRESSED = true;
        this.isBackPressed = true;
        unregisterEventBus();
        ActivityCompat.finishAffinity(this);
        RummyGameEngine.getInstance().stop();*/

        finish();
        if (RummyLoadingActivity.getInstance() != null) {
            RummyLoadingActivity.getInstance().finish();
        }
        RummyUtils.HOME_BACK_PRESSED = true;
        this.isBackPressed = true;
        RummyTLog.e("vikas", "home presse =" + RummyUtils.HOME_BACK_PRESSED);
        unregisterEventBus();
        RummyGameEngine.getInstance().stop();
    }

    private void exitDialog(String msg) {
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rummy_dialog_confirm);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        Button yes_btn = (Button) dialog.findViewById(R.id.yes_btn);
        Button no_btn = (Button) dialog.findViewById(R.id.no_btn);

        title.setText(msg);

        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                    if (RummyLoadingActivity.getInstance() != null) {
                        RummyLoadingActivity.getInstance().finish();
                    }
                    RummyInstance.getInstance().close();


            }
        });

        dialog.show();
    }

    private void exitCurrentTableDialog(String msg) {
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rummy_dialog_confirm);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        Button exit_btn = (Button) dialog.findViewById(R.id.yes_btn);
        Button continue_btn = (Button) dialog.findViewById(R.id.no_btn);

        exit_btn.setText("Yes");
        continue_btn.setText("Go To Table");

        title.setText(msg);

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                rb_tables.performClick();
            }
        });

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                if (RummyLoadingActivity.getInstance() != null) {
                    RummyLoadingActivity.getInstance().finish();
                }
                RummyInstance.getInstance().close();
                isFromExitBtn = true;

            }
        });

        dialog.show();
    }

    @Subscribe
    public void onMessageEvent(RummyEvent event) {
        String eventName = event.getEventName();
        if (eventName.equalsIgnoreCase("tournament_to_start")) {
            if (!isFinishing()) {
                try {
                    showCustomDialog(mHomeActivity, "Your Registered Tournament is going to Start");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Subscribe
    public void onMessageEvent(RummyGameEvent event) {
        if (!this.isBackPressed && this.mIsActivityVisble) {
            if (event.name().equalsIgnoreCase("SERVER_DISCONNECTED") && !this.isBackPressed) {
                disableHearBeat();
                unregisterEventBus();
                if(!RummyUtils.isLogginOut && isFromExitBtn){
                    navigateToLoadingScreen(true);
                    isFromExitBtn = false;
                }

            } else if (event.name().equalsIgnoreCase("OTHER_LOGIN")) {
                unregisterEventBus();
                handleOtherLogin();
            }

        }
    }

    protected void onPause() {
        super.onPause();
        this.mIsActivityVisble = false;
    }


    public void showCustomDialog(Context context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.rummy_dialog_generic);
        ((TextView) dialog.findViewById(R.id.dialog_msg_tv)).setText(message);
        ((Button) dialog.findViewById(R.id.ok_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((ImageView) dialog.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        Handler mHandler = new Handler();
        Runnable mRunnable = new Runnable () {

            public void run() {
                if(dialog != null) dialog.dismiss();
            }
        };
        mHandler.postDelayed(mRunnable,20000);

    }

    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        if (RummyUtils.SHOW_LOBBY && !RummyUtils.HOME_BACK_PRESSED) {
           // this.mRadioGroup.check(R.id.lobby);
            this.rb_lobby.setChecked(true);
            RummyUtils.SHOW_LOBBY = false;
        }
        this.mIsActivityVisble = true;
        registerEventBus();
        if (!RummyGameEngine.getInstance().isSocketConnected() && !RummyUtils.HOME_BACK_PRESSED) {
            navigateToLoadingScreen(false);
        }

        if (!RummyUtils.HOME_BACK_PRESSED) {
            setTablesTab();
            refreshLobby();
        }


        count = 0;

        tableIdList.clear();
        tourneyIdList.clear();

    }

    protected void onStart() {
        super.onStart();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    protected void onStop() {
        super.onStop();
        this.unregisterEventBus();
    }

    public void saveLastCheckedRadioButton(int id) {
        RummyPrefManager.saveInt(this, "lastCheckedItem", id);
    }

    public void setGameVariant(String var1) {
        this.mSelectedVariant = var1;
    }
    public void setGameFavourite(String var1) {
        this.mFavouriteGame = var1;
    }

    public void setTablesTab() {
        this.mApplication = (RummyApplication.getInstance());
        if(this.mApplication != null)
        {
            List<RummyJoinedTable> joinedTableIds = this.mApplication.getJoinedTableIds();
            RadioButton tablesButton = (RadioButton) findViewById(R.id.tables);
            if (joinedTableIds == null && joinedTableIds.size() == 0) {
                this.mIsYourTurn = false;
            }
            if (this.mIsYourTurn) {
                //tablesButton.setBackgroundResource(R.drawable.rummy_tables_on_alert);
                rb_tables_image.setBackgroundResource(R.drawable.rummy_tables_on_alert);
                tablesButton.setEnabled(true);
            } else if (joinedTableIds == null || joinedTableIds.size() <= 0) {
                //tablesButton.setBackgroundResource(R.drawable.rummy_table_off);
                rb_tables_image.setBackgroundResource(R.drawable.rummy_menu_table_icon);
                tablesButton.setEnabled(false);
            } else {
                //tablesButton.setBackgroundResource(R.drawable.rummy_table_on);
                rb_tables_image.setBackgroundResource(R.drawable.rummy_table_on);
                tablesButton.setEnabled(true);
            }
        }
        else {
            RadioButton tablesButton = (RadioButton) findViewById(R.id.tables);
            rb_tables_image.setBackgroundResource(R.drawable.rummy_menu_table_icon);
            tablesButton.setEnabled(false);
        }

    }

    public void showFragment(int id) {
        this.mRadioGroup.check(id);
    }

    @Override
    public void refreshTourneyList() {
        try {
            RummyTournamentsFragment frag = (RummyTournamentsFragment)
                    getSupportFragmentManager().findFragmentByTag(RummyTournamentsFragment.class.getName());
            frag.getTournamentsData();
        } catch (Exception e) {
            Log.e(TAG, "EXP: refreshTourneyList -->> " + e.toString());
        }
    }

    @Subscribe
    public void onMessageEvent(RummyEngineRequest engineRequest) {
        String command = engineRequest.getCommand();

        if (command.equalsIgnoreCase("request_join_table")) {
            boolean isTournySame = false;
            List<RummyJoinedTable> joinedTableslist = this.mRummyApplication.getJoinedTableIds();
            for (int m=0;m<joinedTableslist.size();m++)
            {
                if(joinedTableslist.get(m).getTourneyId().equalsIgnoreCase(engineRequest.getTournament_id()))
                {
                    isTournySame = true;
                    break;
                }
            }

            if(!isTournySame)
            {
                tableIdList.add(engineRequest.getTableId());
                tourneyIdList.add(engineRequest.getTournament_id());
                boolean isaalowToadd = false;
                if(count==0) {
                    count++;
                    Log.e("gopal", " home activity tourney event -> rummy_table id " + tableIdList.get(0));
                    Log.e("gopal", " home activity tourney event -> tourney id " + tourneyIdList.get(0));
                    if(RummyHomeActivity.this.mRummyApplication.getJoinedTableIds().size() >= 2)
                    {
                        List<RummyJoinedTable> joinedTables = this.mRummyApplication.getJoinedTableIds();
                        for (RummyJoinedTable joinedTable : joinedTables) {
                            if (!joinedTable.isTourney()) {
                                joinedTables.remove(joinedTable);
                                isaalowToadd = true;
                                Log.e("vikas","enter  update rummy_table fragment remove rummy_table id  ="+joinedTable.getTabelId());
                                break;
                            }
                        }
                        if(isaalowToadd)
                        {
                            RummyJoinedTable joinedTable = new RummyJoinedTable();
                            joinedTable.setTabelId(tableIdList.get(0));
                            joinedTable.setTourney(true);
                            joinedTable.setTourneyId(tourneyIdList.get(0));
                            RummyHomeActivity.this.mRummyApplication.setJoinedTableIds(joinedTable);
                        }


                    }
                    else
                    {
                        isaalowToadd = true;
                        if(isaalowToadd)
                        {
                            RummyJoinedTable joinedTable = new RummyJoinedTable();
                            joinedTable.setTabelId(tableIdList.get(0));
                            joinedTable.setTourney(true);
                            joinedTable.setTourneyId(tourneyIdList.get(0));
                            RummyHomeActivity.this.mRummyApplication.setJoinedTableIds(joinedTable);
                        }
                    }
                    if(isaalowToadd)
                    {
                        Intent playIntent = new Intent(RummyHomeActivity.this, RummyTableActivity.class);
                        playIntent.putExtra("tableId", tableIdList.get(0));
                        playIntent.putExtra("tourneyId", tourneyIdList.get(0));
                        playIntent.putExtra("iamBack", false);
                        playIntent.putExtra("gameType", "tournament");
                        playIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(playIntent);
                    }



                }
                else
                {


                    if(RummyHomeActivity.this.mRummyApplication.getJoinedTableIds().size() >= 2)
                    {
                        List<RummyJoinedTable> joinedTables = this.mRummyApplication.getJoinedTableIds();
                        for (RummyJoinedTable joinedTable : joinedTables) {
                            if (!joinedTable.isTourney()) {
                                joinedTables.remove(joinedTable);
                                isaalowToadd = true;
                                Log.e("vikas","enter  update rummy_table fragment remove rummy_table id  ="+joinedTable.getTabelId());
                                break;
                            }
                        }
                        if(isaalowToadd)
                        {
                            RummyJoinedTable joinedTable = new RummyJoinedTable();
                            joinedTable.setTabelId(tableIdList.get(1));
                            joinedTable.setTourney(true);
                            joinedTable.setTourneyId(tourneyIdList.get(1));
                            RummyHomeActivity.this.mRummyApplication.setJoinedTableIds(joinedTable);
                            tableIdList.clear();
                            tourneyIdList.clear();
                            count = 0;
                        }


                    }
                    else
                    {
                        Log.e("gopal", " home activity tourney event -> rummy_table id " + tableIdList.get(1));
                        Log.e("gopal", " home activity tourney event -> tourney id " + tourneyIdList.get(1));
                        RummyJoinedTable joinedTable = new RummyJoinedTable();
                        joinedTable.setTabelId(tableIdList.get(1));
                        joinedTable.setTourney(true);
                        joinedTable.setTourneyId(tourneyIdList.get(1));
                        RummyHomeActivity.this.mRummyApplication.setJoinedTableIds(joinedTable);

                        tableIdList.clear();
                        tourneyIdList.clear();
                        count = 0;
                    }

                }
            }
            else
            {
                List<RummyJoinedTable> joinedTables = this.mRummyApplication.getJoinedTableIds();
                for (RummyJoinedTable joinedTable : joinedTables) {
                    if (joinedTable.getTourneyId().equalsIgnoreCase(engineRequest.getTournament_id())) {
                        joinedTable.setTabelId(engineRequest.getTableId());
                        joinedTable.setTourney(true);
                        break;
                    }
                }
            }

        }
    }

    @Override
    public void sendData() {
        FragmentManager fm = getSupportFragmentManager();
        //WithdrawFragment frag = (WithdrawFragment) fm.findFragmentByTag(WithdrawFragment.class.getName());
    }

    public static RummyHomeActivity getInstance() {
        return mHomeActivity;
    }


    public void doWithHandler() {
        Log.e("gopal", "Handler Called");
        count = 0;
        if (tableIdList.size() > 1) {
            Log.e("gopal", " home activity tourney event -> rummy_table id " + tableIdList.get(1));
            Log.e("gopal", " home activity tourney event -> tourney id " + tourneyIdList.get(1));
            RummyJoinedTable joinedTable = new RummyJoinedTable();
            joinedTable.setTabelId(tableIdList.get(1));
            joinedTable.setTourney(true);
            joinedTable.setTourneyId(tourneyIdList.get(1));
            RummyHomeActivity.this.mRummyApplication.setJoinedTableIds(joinedTable);
            Intent playIntent = new Intent(RummyHomeActivity.this, RummyTableActivity.class);
            playIntent.putExtra("tableId", tableIdList.get(1));
            playIntent.putExtra("tourneyId", tourneyIdList.get(1));
            playIntent.putExtra("iamBack", false);
            playIntent.putExtra("gameType", "tournament");
            playIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            tableIdList.clear();
            tourneyIdList.clear();
            startActivity(playIntent);

        } else {
            tableIdList.clear();
            tourneyIdList.clear();
        }
    }
}
