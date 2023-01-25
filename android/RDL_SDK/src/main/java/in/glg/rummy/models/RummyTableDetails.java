package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(strict = false)
public class RummyTableDetails implements Serializable {
    @Attribute(name = "aienable", required = false)
    private String aienable;
    @Attribute(name = "bet", required = false)
    private String bet;
    @Attribute(name = "conversion", required = false)
    private String conversion;
    @Attribute(name = "dealer_id", required = false)
    private String dealer_id;
    @Attribute(name = "dealer_nick_name", required = false)
    private String dealer_nick_name;
    @ElementList(name = "drop_list", required = false)
    private List<RummyGamePlayer> dropList;
    @Attribute(name = "enablechat", required = false)
    private String enableChat;
    @Attribute(name = "extratime", required = false)
    private String extratime;
    @Element(name = "gamedetails", required = false)
    private RummyGameDetails gameDetails;
    @Attribute(name = "gameId", required = false)
    private String gameId;
    @Attribute(name = "game_start", required = false)
    private String gameStart;
    @Attribute(name = "game_id", required = false)
    private String game_id;
    @Attribute(name = "prizemoney", required = false)
    private String prizemoney;
    @Attribute(name = "gamecount", required = false)
    private String gamecount;
    @Attribute(name = "gamesettingid", required = false)
    private String gamesettingid;
    @Attribute(name = "maxplayer", required = false)
    private String maxPlayer;
    @Attribute(name = "maximumbuyin", required = false)
    private String maximumbuyin;
    @Attribute(name = "meld_timeout", required = false)
    private String meldTimeout;
    @Element(name = "middle", required = false)
    private RummyMiddle middle;
    @Attribute(name = "middlejoin", required = false)
    private String middlejoin;
    @Attribute(name = "minplayer", required = false)
    private String minPlayer;
    @Attribute(name = "minimumbuyin", required = false)
    private String minimumbuyin;
    @Attribute(name = "nick_name", required = false)
    private String nickName;
    @ElementList(name = "players", required = false)
    private List<RummyGamePlayer> players;
    @Attribute(name = "rejoin", required = false)
    private String reJoin;
    @Attribute(name = "schedulename", required = false)
    private String schedulename;
    @Attribute(name = "servicefee", required = false)
    private String serviceFee;
    @Attribute(name = "split", required = false)
    private String split;
    @Attribute(name = "splittype", required = false)
    private String splitType;
    @Attribute(name = "starttime", required = false)
    private String starttime;
    @Attribute(name = "streamid", required = false)
    private String streamid;
    @Attribute(name = "streamname", required = false)
    private String streamname;
    @Attribute(name = "subgame_start", required = false)
    private String subgameStart;
    @Attribute(name = "table_cost", required = false)
    private String tableCost;
    @Attribute(name = "table_id", required = false)
    private String tableId;
    @Attribute(name = "table_type", required = false)
    private String tableType;
    @Attribute(name = "tournament_table", required = false)
    private String tournament_table;
    @Attribute(name = "turn_timeout", required = false)
    private String turnTimeout;
    @Attribute(name = "fun_chips", required = false)
    private String fun_chips;
    @Attribute(name = "unique_gamesettings_id", required = false)
    private String unique_gamesettings_id;

    @Attribute(name = "is_waiting_for_players", required = false)
    private boolean is_waiting_for_players = false;

    public boolean is_waiting_for_players() {
        return is_waiting_for_players;
    }

    public void setIs_waiting_for_players(boolean is_waiting_for_players) {
        this.is_waiting_for_players = is_waiting_for_players;
    }


    public String getFun_chips(){
        return fun_chips;
    }
    public void setFun_chips(String fun_chips){
        this.fun_chips = fun_chips;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getPrizemoney() {
        return prizemoney;
    }

    public void setPrizemoney(String prizemoney) {
        this.prizemoney = prizemoney;
    }

    public String getTableId() {
        return this.tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getGameStart() {
        return this.gameStart;
    }

    public void setGameStart(String gameStart) {
        this.gameStart = gameStart;
    }

    public String getSubgameStart() {
        return this.subgameStart;
    }

    public void setSubgameStart(String subgameStart) {
        this.subgameStart = subgameStart;
    }

    public String getTableType() {
        if(this.tableType != null)
        {
            return this.tableType;
        }
        else
        {
            return "";
        }

    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getTableCost() {
        return this.tableCost;
    }

    public void setTableCost(String tableCost) {
        this.tableCost = tableCost;
    }

    public String getTurnTimeout() {
        return this.turnTimeout;
    }

    public void setTurnTimeout(String turnTimeout) {
        this.turnTimeout = turnTimeout;
    }

    public String getMaxPlayer() {
        return this.maxPlayer;
    }

    public void setMaxPlayer(String maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public String getMinPlayer() {
        return this.minPlayer;
    }

    public void setMinPlayer(String minPlayer) {
        this.minPlayer = minPlayer;
    }

    public String getMeldTimeout() {
        return this.meldTimeout;
    }

    public void setMeldTimeout(String meldTimeout) {
        this.meldTimeout = meldTimeout;
    }

    public String getEnableChat() {
        return this.enableChat;
    }

    public void setEnableChat(String enableChat) {
        this.enableChat = enableChat;
    }

    public String getReJoin() {
        return this.reJoin;
    }

    public void setReJoin(String reJoin) {
        this.reJoin = reJoin;
    }

    public String getSplit() {
        return this.split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public String getBet() {
        return this.bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public String getSplitType() {
        return this.splitType;
    }

    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }

    public String getServiceFee() {
        return this.serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getMinimumbuyin() {
        return this.minimumbuyin;
    }

    public void setMinimumbuyin(String minimumbuyin) {
        this.minimumbuyin = minimumbuyin;
    }

    public String getMaximumbuyin() {
        return this.maximumbuyin;
    }

    public void setMaximumbuyin(String maximumbuyin) {
        this.maximumbuyin = maximumbuyin;
    }

    public String getMiddlejoin() {
        return this.middlejoin;
    }

    public void setMiddlejoin(String middlejoin) {
        this.middlejoin = middlejoin;
    }

    public String getGamecount() {
        return this.gamecount;
    }

    public void setGamecount(String gamecount) {
        this.gamecount = gamecount;
    }

    public String getExtratime() {
        return this.extratime;
    }

    public void setExtratime(String extratime) {
        this.extratime = extratime;
    }

    public String getAienable() {
        return this.aienable;
    }

    public void setAienable(String aienable) {
        this.aienable = aienable;
    }

    public String getConversion() {
        return this.conversion;
    }

    public void setConversion(String conversion) {
        this.conversion = conversion;
    }

    public String getStreamid() {
        return this.streamid;
    }

    public void setStreamid(String streamid) {
        this.streamid = streamid;
    }

    public String getGamesettingid() {
        return this.gamesettingid;
    }

    public void setGamesettingid(String gamesettingid) {
        this.gamesettingid = gamesettingid;
    }

    public String getStreamname() {
        return this.streamname;
    }

    public void setStreamname(String streamname) {
        this.streamname = streamname;
    }

    public String getSchedulename() {
        return this.schedulename;
    }

    public void setSchedulename(String schedulename) {
        this.schedulename = schedulename;
    }

    public String getDealer_id() {
        return this.dealer_id;
    }

    public void setDealer_id(String dealer_id) {
        this.dealer_id = dealer_id;
    }

    public String getDealer_nick_name() {
        return this.dealer_nick_name;
    }

    public void setDealer_nick_name(String dealer_nick_name) {
        this.dealer_nick_name = dealer_nick_name;
    }

    public List<RummyGamePlayer> getPlayers() {
        return this.players;
    }

    public void setPlayers(List<RummyGamePlayer> players) {
        this.players = players;
    }

    public String getStarttime() {
        return this.starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getTournament_table() {
        return this.tournament_table;
    }

    public void setTournament_table(String tournament_table) {
        this.tournament_table = tournament_table;
    }

    public List<RummyGamePlayer> getDropList() {
        return this.dropList;
    }

    public void setDropList(List<RummyGamePlayer> dropList) {
        this.dropList = dropList;
    }

    public RummyGameDetails getGameDetails() {
        return this.gameDetails;
    }

    public void setGameDetails(RummyGameDetails gameDetails) {
        this.gameDetails = gameDetails;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public RummyMiddle getMiddle() {
        return this.middle;
    }

    public void setMiddle(RummyMiddle middle) {
        this.middle = middle;
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getUnique_gamesettings_id() {
        return unique_gamesettings_id;
    }

    public void setUnique_gamesettings_id(String unique_gamesettings_id) {
        this.unique_gamesettings_id = unique_gamesettings_id;
    }
}
