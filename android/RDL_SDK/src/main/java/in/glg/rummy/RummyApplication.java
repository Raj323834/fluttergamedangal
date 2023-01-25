package in.glg.rummy;

import android.content.Context;


import com.koushikdutta.async.AsyncSocket;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import in.glg.rummy.api.response.RummyLobbyTablesResponse;
import in.glg.rummy.api.response.RummyLoginResponse;
import in.glg.rummy.models.RummyAuthReq;
import in.glg.rummy.models.RummyEvent;
import in.glg.rummy.models.RummyJoinedTable;
import in.glg.rummy.models.RummyTable;
import in.glg.rummy.utils.RummyISoundPoolLoaded;
import in.glg.rummy.utils.RummySoundPoolManager;
import in.glg.rummy.utils.RummyUtils;
import in.glg.rummy.utils.RummyVibrationManager;



public class RummyApplication  {
    String TAG = getClass().getSimpleName()+"";
    private int balance;
    private List<RummyEvent> eventList = new ArrayList();
    private List<RummyTable> MyfaverateList = new ArrayList<>();
    private List<RummyJoinedTable> joinedTableIds;
    private AsyncSocket socket;
    private RummyLoginResponse userData;
    private RummyLobbyTablesResponse lobbyTablesData;
    private String leaderBoardResponse = "";

    private static RummyApplication ourInstance ;

    private Context mContext;

    public static boolean userNeedsAuthentication = true;

    private String CurrentTableId = "";
    private String CurrentTableBet = "";
    private String CurrentTableAmount = "";
    private String CurrentTableGameType = "";
    private String CurrentTableSeqId = "";
    private String CurrentTableUserId = "";
    private String CurrentTableOrderId = "";
    private String CurrentTableCostType = "";



    public List<RummyTable> getMyfaverateList() {
        return MyfaverateList;
    }

    public void setMyfaverateList(List<RummyTable> myfaverateList) {
        MyfaverateList = myfaverateList;
    }

    public void clearFavoriteList()
    {
        if(this.MyfaverateList != null)
        {
            this.MyfaverateList.clear();
        }
    }

    public void addFavorite(RummyTable t)
    {
        t.setFavorite("true");
        this.MyfaverateList.add(t);
    }

    public void removeFavorite(RummyTable t)
    {
        boolean flag = false;
        for(int i=0;i<MyfaverateList.size();i++)
        {

            RummyTable tableToCompare = MyfaverateList.get(i);
            if(tableToCompare.getTable_type().equalsIgnoreCase(t.getTable_type()) && tableToCompare.getBet().equalsIgnoreCase(t.getBet()) && tableToCompare.getMaxplayer().equalsIgnoreCase(t.getMaxplayer()))
            {
                MyfaverateList.remove(MyfaverateList.get(i));
                flag = true;
                break;
            }
        }

        if(!flag)
        {
            t.setFavorite("false");
            MyfaverateList.add(t);
        }
    }

    public String getLeaderBoardResponse() {
        return leaderBoardResponse;
    }

    public void setLeaderBoardResponse(String leaderBoardResponse) {
        this.leaderBoardResponse = leaderBoardResponse;
    }

    class C16231 implements RummyISoundPoolLoaded {
        C16231() {
        }

        public void onSuccess() {
            RummySoundPoolManager.getInstance().setPlaySound(true);
        }
    }

    public List<RummyEvent> getEventList() {
        return this.eventList;
    }

    public void setEventList(List<RummyEvent> eventList) {
        this.eventList = eventList;
    }

    private RummyApplication(Context context)
    {

        if (ourInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        else
        {
            this.mContext = context;
            this.joinedTableIds = new ArrayList();
            this.eventList = new ArrayList();
            initSoundPoolManager();
            initVibrations();

        }
    }

    public static void inItsingleton(Context context)
    {
        if(ourInstance == null)
        {
            ourInstance = new RummyApplication(context);
        }
    }

    public static RummyApplication getInstance() {
        return ourInstance;
    }



    public void registerEventBus()
    {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initVibrations() {
        RummyVibrationManager.CreateInstance();
        try {
            RummyVibrationManager.getInstance().InitializeVibrator(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RummyVibrationManager.getInstance().setVibration(true);
    }

    private void initSoundPoolManager() {
        RummySoundPoolManager.CreateInstance();
        List<Integer> sounds = new ArrayList();
        sounds.add(Integer.valueOf(R.raw.rummy_bell));
        sounds.add(Integer.valueOf(R.raw.rummy_card_distribute));
        sounds.add(Integer.valueOf(R.raw.rummy_clock));
        sounds.add(Integer.valueOf(R.raw.pick_discard));
        sounds.add(Integer.valueOf(R.raw.rummy_sit));
        sounds.add(Integer.valueOf(R.raw.rummy_toss));
        sounds.add(Integer.valueOf(R.raw.rummy_drop));
        sounds.add(Integer.valueOf(R.raw.rummy_meld));
        sounds.add(Integer.valueOf(R.raw.rummy_winners));
        RummySoundPoolManager.getInstance().setSounds(sounds);
        try {
            RummySoundPoolManager.getInstance().InitializeSoundPool(mContext, new C16231());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void init() {
        this.joinedTableIds = new ArrayList();
        this.eventList = new ArrayList();
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onMessageEvent(RummyEvent event) {
        if (event.getTableId() != null) {
            this.eventList.add(event);
        }
    }

    public void setUserData(RummyLoginResponse userData) {
        this.userData = userData;
    }

    public RummyLoginResponse getUserData() {
        return this.userData;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return this.balance;
    }

    public void setSocket(AsyncSocket socket) {
        this.socket = socket;
    }

    public AsyncSocket getSocket() {
        return this.socket;
    }


    public List<RummyJoinedTable> getJoinedTableIds() {
        return this.joinedTableIds;
    }

    public void setJoinedTableIds(RummyJoinedTable joinedTable) {
        this.joinedTableIds.add(joinedTable);
    }


    public void refreshTableIds(String tableId) {

        for(int i=0;i<this.joinedTableIds.size();i++)
        {
            if(tableId.equalsIgnoreCase(this.joinedTableIds.get(i).getTabelId()))
            {
                this.joinedTableIds.remove(this.joinedTableIds.get(i));
                return;
            }
        }
    }

    public void clearJoinedTablesIds() {
        if (this.joinedTableIds != null) {
            this.joinedTableIds.clear();
        }
        if (this.eventList != null) {
            this.eventList.clear();
        }
        if (RummyUtils.tableDetailsList != null) {
            RummyUtils.tableDetailsList.clear();
        }
    }

    public void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void clearEventList() {
        if (this.eventList != null) {
            this.eventList.clear();
        }
    }

    public Context getContext()
    {
        return this.mContext;
    }

    public String getCurrentTableOrderId() {
        return CurrentTableOrderId;
    }

    public void setCurrentTableOrderId(String currentTableOrderId) {
        CurrentTableOrderId = currentTableOrderId;
    }

    public String getCurrentTableUserId() {
        return CurrentTableUserId;
    }

    public void setCurrentTableUserId(String currentTableUserId) {
        CurrentTableUserId = currentTableUserId;
    }

    public String getCurrentTableSeqId() {
        return CurrentTableSeqId;
    }

    public void setCurrentTableSeqId(String currentTableSeqId) {
        CurrentTableSeqId = currentTableSeqId;
    }

    public String getCurrentTableGameType() {
        return CurrentTableGameType;
    }

    public void setCurrentTableGameType(String currentTableGameType) {
        CurrentTableGameType = currentTableGameType;
    }

    public String getCurrentTableAmount() {
        return CurrentTableAmount;
    }

    public void setCurrentTableAmount(String currentTableAmount) {
        CurrentTableAmount = currentTableAmount;
    }

    public String getCurrentTableBet() {
        return CurrentTableBet;
    }

    public void setCurrentTableBet(String currentTableBet) {
        CurrentTableBet = currentTableBet;
    }

    public String getCurrentTableId() {
        return CurrentTableId;
    }

    public void setCurrentTableId(String currentTableId) {
        CurrentTableId = currentTableId;
    }

    public String getCurrentTableCostType() {
        return CurrentTableCostType;
    }

    public void setCurrentTableCostType(String currentTableCostType) {
        CurrentTableCostType = currentTableCostType;
    }
}
