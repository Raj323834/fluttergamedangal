package in.glg.rummy.activities;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import in.glg.rummy.GameRoom.RummyTableActivity;
import in.glg.rummy.RummyApplication;
import in.glg.rummy.interfaces.LobbyTableUpdateListener;
import in.glg.rummy.interfaces.RummyHomeActivityCloseListener;
import in.glg.rummy.interfaces.RummyListener;
import in.glg.rummy.interfaces.RummyTableActivityCloseListener;
import in.glg.rummy.interfaces.RummyTableRefreshListener;
import in.glg.rummy.models.RummyGamePlayer;
import in.glg.rummy.models.RummyTable;
import in.glg.rummy.utils.RummyUtils;

public class RummyInstance {
    public static String sdkMode = "test";
    private Context context;
    private String merchan_id,userToken, helpLink,assetsFolderName,user_name,email,phone;
    private RummyListener listener;
    private RummyHomeActivityCloseListener homeActivityCloseListener = null;
    private RummyTableActivityCloseListener tableActivityCloseListener = null;
    private RummyTableRefreshListener tableRefreshListener = null;

    private LobbyTableUpdateListener lobbyTableUpdateListener = null;

    private List<RummyTable> rummyTables = new ArrayList<>();


    private static final RummyInstance instance = new RummyInstance() ;

    private RummyInstance()
    {
        if (instance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public int init(Context appContext, String email, String phone, String userToken, String user_name , String merchantId, String helpLink, String sdkMode, String assetsFolderName, RummyListener listener)
    {
        if(appContext != null && RummyUtils.isStringEmpty(userToken) && RummyUtils.isStringEmpty(user_name) && RummyUtils.isStringEmpty(merchantId) && RummyUtils.isStringEmpty(helpLink) && RummyUtils.isStringEmpty(sdkMode) && RummyUtils.isStringEmpty(assetsFolderName) && listener != null)
        {
            if(context instanceof Application) {
                this.context = appContext;
            }else {
                this.context = appContext.getApplicationContext();
            }
            this.merchan_id = merchantId;
            this.helpLink = helpLink;
            this.sdkMode = sdkMode;
            this.assetsFolderName = assetsFolderName;
            this.userToken = userToken;
            this.listener = listener;
            this.user_name = user_name;
            this.email = email;
            this.phone = phone;
            RummyUtils.setSdkMode(sdkMode);
            RummyApplication.inItsingleton(context);
            return 0;   // when return true
        }
        else
        {
            return 1;  /// this is when return false
        }

    }
    public void setRummyTables(List<RummyTable> rummyTables)
    {
        this.rummyTables = rummyTables;
    }

    public String getHelpLink()
    {
        return this.helpLink;
    }
    public String getAssetsFolderName()
    {
        return this.assetsFolderName;
    }
    public RummyListener getRummyListener()
    {
        return this.listener;
    }

    public static RummyInstance getInstance()
    {
        return instance;
    }

    /*public String getUserName()
    {
        return this.userName;
    }*/

    public String getSdkMode()
    {
        return this.sdkMode;
    }

    public void startSDK()
    {
        if(this.context != null
                && RummyUtils.isStringEmpty(this.userToken)
                && RummyUtils.isStringEmpty(this.user_name)
                && RummyUtils.isStringEmpty(this.merchan_id)
                && RummyUtils.isStringEmpty(helpLink)
                && RummyUtils.isStringEmpty(sdkMode)
                && RummyUtils.isStringEmpty(assetsFolderName) && listener != null)
        {

            Intent i = new Intent(context, RummyJsonActivity.class);
            i.putExtra("merchant_id",this.merchan_id);
            i.putExtra("userid",this.userToken);
            i.putExtra("user_name",this.user_name);
            i.putExtra("email",this.email);
            i.putExtra("phone",this.phone);
            //   i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        else
        {
            Toast.makeText(this.context,"Please initialize sdk first", Toast.LENGTH_LONG).show();
        }
    }


    public void close() {

        if(RummyTableActivity.getInstance() != null)
        {
            RummyTableActivity.getInstance().finish();
        }

        if(RummyApplication.getInstance() != null)
        {
            RummyApplication.getInstance().unregisterEventBus();
        }

        if(tableActivityCloseListener != null)
        {
            tableActivityCloseListener.onClose();
        }

        if(homeActivityCloseListener != null)
        {
            homeActivityCloseListener.onClose();
        }


       /* if(TableActivity.getInstance() != null)
        {
            TableActivity.getInstance().finish();
        }
        if(HomeActivity.getInstance() != null)
        {
            HomeActivity.getInstance().exitLogic();
            RummyApplication.getInstance().clearInstance();
        }*/




    }



    public void setHomeActivityCloseListener(RummyHomeActivityCloseListener listener)
    {
        this.homeActivityCloseListener = listener;
    }

    public void setTableActivityCloseListener(RummyTableActivityCloseListener listener)
    {
        this.tableActivityCloseListener = listener;
    }

    public List<RummyTable> getRummyTables()
    {
        return this.rummyTables;
    }

    public void refreshTableActivityTables(List<RummyTable> rummyTables)
    {
        if(this.tableRefreshListener != null)
        {
            this.tableRefreshListener.refreshTable(rummyTables);
        }

    }
    public void setTableRefreshListener(RummyTableRefreshListener listener)
    {
        this.tableRefreshListener = listener;
    }
    public void unregisterEventBus() {
        EventBus.getDefault().removeAllStickyEvents();
    }

    public void setLobbyTableUpdateListener(LobbyTableUpdateListener listener)
    {
        this.lobbyTableUpdateListener = listener;
    }


    public void setLobbyTableUi(String tableId, RummyGamePlayer player, int maxPlayerCount, boolean isLeft)
    {
        if(this.lobbyTableUpdateListener != null)
        {
            this.lobbyTableUpdateListener.setTableUi(tableId,player,maxPlayerCount,isLeft);
        }
    }

    public void reSetLobbyTableUi(String tableId)
    {
        if(this.lobbyTableUpdateListener != null)
        {
            this.lobbyTableUpdateListener.reSetTableUi(tableId);
        }
    }
}
