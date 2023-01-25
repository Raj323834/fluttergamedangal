package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

import in.glg.rummy.api.response.RummyBaseResponse;

@Root(name = "event", strict = false)
public class RummyEvent extends RummyBaseResponse implements Serializable
{
    @Attribute(name = "chiptype", required = false)
    private String chiptype;
    @Attribute(name = "buyin_amount", required = false)
    private String buyin_amount;
    @Attribute(name = "to_level", required = false)
    private String to_level;
    @Attribute(name = "value_rake", required = false)
    private String value_rake;
    @Attribute(name = "auto_extra_chunks", required = false)
    private String autoExtraChunks;
    @Attribute(name = "auto_extra_time", required = false)
    private String autoExtraTime;
    @Attribute(name = "autoplay", required = false)
    private String autoPlay;
    @Attribute(name = "autoplaycount", required = false)
    private String autoPlayCount;
    @Attribute(name = "bet", required = false)
    private String bet;
    @Attribute(name = "bonuschips", required = false)
    private String bonusChips;
    @Attribute(name = "bonusinplay", required = false)
    private String bonusInplay;
    @Attribute(name = "buyinamount", required = false)
    private String buyinamount;
    @Attribute(name = "char_no", required = false)
    private String charNo;
    @Attribute(name = "conversion", required = false)
    private String conversion;
    @Attribute(name = "current_players", required = false)
    private String currentPlayers;
    @Attribute(name = "dealer_id", required = false)
    private String dealerId;
    @Attribute(name = "dealer_nick_name", required = false)
    private String dealerNickName;
    @Attribute(name = "deposit_amount", required = false)
    private String depositAmount;
    @Attribute(name = "description", required = false)
    private String description;
    @Attribute(name = "DEVICE_ID", required = false)
    private String deviceId;
    @Attribute(name = "droppoint", required = false)
    private String dropPoint;
    @Attribute(name = "event_name", required = false)
    private String eventName;
    @Attribute(name = "face", required = false)
    private String face;
    @ElementList(name = "face_down_stack", required = false)
    private List<RummyPlayingCard> faceDownStack;
    @ElementList(name = "face_up_stack", required = false)
    private List<RummyPlayingCard> faceUpStack;
    @Attribute(name = "fun_chips", required = false)
    private String funChips;
    @Attribute(name = "fun_inplay", required = false)
    private String funInPlay;
    @Attribute(name = "game_id", required = false)
    private String gameId;
    @Attribute(name = "game_schedule", required = false)
    private String gameSchedule;
    @Attribute(name = "stack", required = false)
    private String gameStack;
    @Attribute(name = "game_start", required = false)
    private String gameStart;
    @Element(name = "games", required = false)
    private RummyGames games;
    @Attribute(name = "gender", required = false)
    private String gender;
    @Attribute(name = "id", required = false)
    private String id;
    @Attribute(name = "isIamBack", required = false)
    private boolean isIamBack;
    @Attribute(name = "jocker", required = false)
    private String jocker;
    @Attribute(name = "joined_players", required = false)
    private String joinedPlayers;
    @Attribute(name = "joker", required = false)
    private String joker;
    @Attribute(name = "level_id", required = false)
    private String levelId;
    @Attribute(name = "loyaltychips", required = false)
    private String loyaltyChips;
    @Attribute(name = "loyaltyinplay", required = false)
    private String loyaltyInPlay;
    @Attribute(name = "maximumbuyin", required = false)
    private String maxBuyIn;
    @Attribute(name = "maxplayer", required = false)
    private String maxPlayer;
    @Attribute(name = "timeout", required = false)
    private String meldTimeOut;
    @Attribute(name = "message", required = false)
    private String message;
    @Attribute(name = "minimumbuyin", required = false)
    private String minBuyIn;
    @Attribute(name = "minplayer", required = false)
    private String minPlayer;
    @Attribute(name = "nick_name", required = false)
    private String nickName;
    @Attribute(name = "ntable", required = false)
    private String noOfTables;
    @Attribute(name = "player_type", required = false)
    private String playeType;
    @ElementList(data = false, inline = true, name = "player", required = false)
    private List<RummyGamePlayer> player;
    @Attribute(name = "player_count", required = false)
    private String playerCount;
    @Attribute(name = "player_in", required = false)
    private String playerIn;
    @Attribute(name = "playerlevel", required = false)
    private String playerLevel;
    @ElementList(name = "players", required = false)
    private List<RummyGamePlayer> players;
    @ElementList(inline = true, name = "card", required = false)
    private List<RummyPlayingCard> playingCards;
    @Attribute(name = "prize", required = false)
    private String prize;
    @Attribute(name = "prizemoney", required = false)
    private String prizeMoney;
    @Attribute(name = "rake_amount", required = false)
    private String rakeAmount;
    @Attribute(name = "real_chips", required = false)
    private String reaChips;
    @Attribute(name = "real_inplay", required = false)
    private String realInPlay;
    @Attribute(name = "reason", required = false)
    private String reason;
    @Attribute(name = "rebuyinamt", required = false)
    private String rebuyInAmt;
    @Attribute(name = "score", required = false)
    private String rejoinScore;
    @Attribute(name = "schedule_name", required = false)
    private String scheduleName;
    @Attribute(name = "seat", required = false)
    private String seat;
    @Attribute(name = "socialchips", required = false)
    private String socialChips;
    @Attribute(name = "socialinplay", required = false)
    private String socialInPlay;
    @Attribute(name = "split", required = false)
    private String split;
    @Element(name = "splitter", required = false)
    private RummySplitter splitter;
    @Attribute(name = "starttime", required = false)
    private String startTime;
    @Attribute(name = "status", required = false)
    private String status;
    @Attribute(name = "stream", required = false)
    private String stream;
    @Attribute(name = "stream_cost", required = false)
    private String streamCost;
    @Attribute(name = "suit", required = false)
    private String suit;
    @Element(name = "table", required = false)
    private RummyTableCards tableCards;
    @Attribute(name = "table_cost", required = false)
    private String tableCost;
    @Element(name = "table_details", required = false)
    private RummyTableDetails tableDetails;
    @Element(name = "gamedetails", required = false)
    private RummyTableDetails gamedetails;
    @Attribute(name = "table_id", required = false)
    private String tableId;
    @Attribute(name = "table_join_as", required = false)
    private String tableJoinAs;
    @Attribute(name = "table_type", required = false)
    private String tableType;
    @Attribute(name = "tdsamount", required = false)
    private String tdsAmount;
    @Attribute(name = "text", required = false)
    private String text;
    @Attribute(name = "time_out", required = false)
    private String timeOut;
    @Attribute(name = "toss_winner", required = false)
    private String tossWinner;
    @Attribute(name = "totalcount", required = false)
    private String totalCounr;
    @Attribute(name = "totalplayers", required = false)
    private String totalNoOfPlayers;
    @Attribute(name = "total_player", required = false)
    private String totalPlayers;
    @Attribute(name = "ttable", required = false)
    private String totalTables;
    @Attribute(name = "total_players", required = false)
    private String totalgamePlayers;
    @Element(name = "tournament", required = false)
    private RummyTournament tournament;

    @ElementList(name = "tourneys", required = false)
    private List<RummyTourney> tourneys;

    @Attribute(name = "tournament_id", required = false)
    private String tournamentId;
    @Attribute(name = "highest_rank", required = false)
    private String highest_rank;
    @Attribute(name = "tournament", required = false)
    private String tournamentStr;
    @Attribute(name = "tournament_type", required = false)
    private String tournamentType;
    @Attribute(name = "tournament_table", required = false)
    private String tournament_table;
    @Attribute(name = "tourney_cost", required = false)
    private String tourneyCost;
    @Attribute(name = "turn_count", required = false)
    private String turnCount;
    @Element(name = "stack", required = false)
    private RummyStack turnUpdateStack;
    @Attribute(name = "user_id", required = false)
    private int userId;
    @Attribute(name = "vipcode", required = false)
    private String vipcode;
    @Attribute(name = "waiting", required = false)
    private String waiting;
    @Attribute(name = "winner_id", required = false)
    private String winnerId;
    @Attribute(name = "winner_nickname", required = false)
    private String winnerNickName;
    @Attribute(name = "withdrawable_amount", required = false)
    private String withDrawableAmt;
    @Attribute(name = "start_time", required = false)
    private String start_time;
    @Attribute(name = "tourney", required = false)
    private String tourney;

    @Attribute(name = "site_id", required = false)
    private String site_id;

    @Attribute(name = "is_waiting_for_players", required = false)
    private boolean is_waiting_for_players = false;


    @Attribute(name = "waitlist_players", required = false)
    private String waitlist_players;


    public String getWaitlist_players() {
        return waitlist_players;
    }

    public void setWaitlist_players(String waitlist_players) {
        this.waitlist_players = waitlist_players;
    }

    public boolean is_waiting_for_players() {
        return is_waiting_for_players;
    }

    public void setIs_waiting_for_players(boolean is_waiting_for_players) {
        this.is_waiting_for_players = is_waiting_for_players;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public RummyTableDetails getGamedetails() {
        return gamedetails;
    }

    public void setGamedetails(RummyTableDetails gamedetails) {
        this.gamedetails = gamedetails;
    }

    public List<RummyTourney> getTourneys() {
        return tourneys;
    }

    public void setTourneys(List<RummyTourney> tourneys) {
        this.tourneys = tourneys;
    }

    public String getTourney() {
        return tourney;
    }

    public void setTourney(String tourney) {
        this.tourney = tourney;
    }

    public String getSplit() {
        return this.split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public String getRejoinScore() {
        return this.rejoinScore;
    }

    public void setRejoinScore(String rejoinScore) {
        this.rejoinScore = rejoinScore;
    }

    public String getAutoExtraTime() {
        return this.autoExtraTime;
    }

    public void setAutoExtraTime(String autoExtraTime) {
        this.autoExtraTime = autoExtraTime;
    }

    public String getAutoExtraChunks() {
        return this.autoExtraChunks;
    }

    public void setAutoExtraChunks(String autoExtraChunks) {
        this.autoExtraChunks = autoExtraChunks;
    }

    public String getWinnerId() {
        return this.winnerId;
    }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }

    public String getLoyaltyInPlay() {
        return this.loyaltyInPlay;
    }

    public void setLoyaltyInPlay(String loyaltyInPlay) {
        this.loyaltyInPlay = loyaltyInPlay;
    }

    public String getMeldTimeOut() {
        return this.meldTimeOut;
    }

    public void setMeldTimeOut(String meldTimeOut) {
        this.meldTimeOut = meldTimeOut;
    }

    public String getWithDrawableAmt() {
        return this.withDrawableAmt;
    }

    public void setWithDrawableAmt(String withDrawableAmt) {
        this.withDrawableAmt = withDrawableAmt;
    }

    public String getJoker() {
        return this.joker;
    }

    public void setJoker(String joker) {
        this.joker = joker;
    }

    public RummyStack getTurnUpdateStack() {
        return this.turnUpdateStack;
    }

    public void setTurnUpdateStack(RummyStack turnUpdateStack) {
        this.turnUpdateStack = turnUpdateStack;
    }

    public RummySplitter getSplitter() {
        return this.splitter;
    }

    public void setSplitter(RummySplitter splitter) {
        this.splitter = splitter;
    }

    public RummyTableDetails getTableDetails() {
        return this.tableDetails;
    }

    public void setTableDetails(RummyTableDetails tableDetails) {
        this.tableDetails = tableDetails;
    }

    public String getCurrentPlayers() {
        return this.currentPlayers;
    }

    public void setCurrentPlayers(String currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public String getPlayerIn() {
        return this.playerIn;
    }

    public void setPlayerIn(String playerIn) {
        this.playerIn = playerIn;
    }

    public String getPrizeMoney() {
        return this.prizeMoney;
    }

    public void setPrizeMoney(String prizeMoney) {
        this.prizeMoney = prizeMoney;
    }

    public String getJocker() {
        return this.jocker;
    }

    public void setJocker(String jocker) {
        this.jocker = jocker;
    }

    public List<RummyPlayingCard> getFaceDownStack() {
        return this.faceDownStack;
    }

    public void setFaceDownStack(List<RummyPlayingCard> faceDownStack) {
        this.faceDownStack = faceDownStack;
    }

    public List<RummyPlayingCard> getFaceUpStack() {
        return this.faceUpStack;
    }

    public void setFaceUpStack(List<RummyPlayingCard> faceUpStack) {
        this.faceUpStack = faceUpStack;
    }

    public String getGameStack() {
        return this.gameStack;
    }

    public void setGameStack(String gameStack) {
        this.gameStack = gameStack;
    }

    public List<RummyPlayingCard> getPlayingCards() {
        return this.playingCards;
    }

    public void setPlayingCards(List<RummyPlayingCard> playingCards) {
        this.playingCards = playingCards;
    }

    public String getPlayerCount() {
        return this.playerCount;
    }

    public void setPlayerCount(String playerCount) {
        this.playerCount = playerCount;
    }

    public String getRakeAmount() {
        return this.rakeAmount;
    }

    public void setRakeAmount(String rakeAmount) {
        this.rakeAmount = rakeAmount;
    }

    public String getStreamCost() {
        return this.streamCost;
    }

    public void setStreamCost(String streamCost) {
        this.streamCost = streamCost;
    }

    public String getTdsAmount() {
        return this.tdsAmount;
    }

    public void setTdsAmount(String tdsAmount) {
        this.tdsAmount = tdsAmount;
    }

    public String getSocialInPlay() {
        return this.socialInPlay;
    }

    public void setSocialInPlay(String socialInPlay) {
        this.socialInPlay = socialInPlay;
    }

    public String getWinnerNickName() {
        return this.winnerNickName;
    }

    public void setWinnerNickName(String winnerNickName) {
        this.winnerNickName = winnerNickName;
    }

    public String getPrize() {
        return this.prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public RummyTournament getTournament() {
        return this.tournament;
    }

    public void setTournament(RummyTournament tournament) {
        this.tournament = tournament;
    }

    public String getTotalNoOfPlayers() {
        return this.totalNoOfPlayers;
    }

    public void setTotalNoOfPlayers(String totalNoOfPlayers) {
        this.totalNoOfPlayers = totalNoOfPlayers;
    }

    public String getTotalTables() {
        return this.totalTables;
    }

    public void setTotalTables(String totalTables) {
        this.totalTables = totalTables;
    }

    public String getNoOfTables() {
        return this.noOfTables;
    }

    public void setNoOfTables(String noOfTables) {
        this.noOfTables = noOfTables;
    }

    public String getJoinedPlayers() {
        return this.joinedPlayers;
    }

    public void setJoinedPlayers(String joinedPlayers) {
        this.joinedPlayers = joinedPlayers;
    }

    public String getSocialChips() {
        return this.socialChips;
    }

    public String getLoyaltyChips() {
        return this.loyaltyChips;
    }

    public void setLoyaltyChips(String loyaltyChips) {
        this.loyaltyChips = loyaltyChips;
    }

    public RummyGames getGames() {
        return this.games;
    }

    public void setGames(RummyGames games) {
        this.games = games;
    }

    public void setSocialChips(String socialChips) {
        this.socialChips = socialChips;
    }

    public String getRealInPlay() {
        return this.realInPlay;
    }

    public void setRealInPlay(String realInPlay) {
        this.realInPlay = realInPlay;
    }

    public String getReaChips() {
        return this.reaChips;
    }

    public void setReaChips(String reaChips) {
        this.reaChips = reaChips;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAutoPlayCount() {
        return this.autoPlayCount;
    }

    public void setAutoPlayCount(String autoPlayCount) {
        this.autoPlayCount = autoPlayCount;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFunChips() {
        return this.funChips;
    }

    public void setFunChips(String funChips) {
        this.funChips = funChips;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getBonusInplay() {
        return this.bonusInplay;
    }

    public void setBonusInplay(String bonusInplay) {
        this.bonusInplay = bonusInplay;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPlayerLevel() {
        return this.playerLevel;
    }

    public void setPlayerLevel(String playerLevel) {
        this.playerLevel = playerLevel;
    }

    public String getSeat() {
        return this.seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getTableJoinAs() {
        return this.tableJoinAs;
    }

    public void setTableJoinAs(String tableJoinAs) {
        this.tableJoinAs = tableJoinAs;
    }

    public String getTournament_table() {
        return this.tournament_table;
    }

    public void setTournament_table(String tournament_table) {
        this.tournament_table = tournament_table;
    }

    public String getGameSchedule() {
        return this.gameSchedule;
    }

    public void setGameSchedule(String gameSchedule) {
        this.gameSchedule = gameSchedule;
    }

    public String getGameStart() {
        return this.gameStart;
    }

    public void setGameStart(String gameStart) {
        this.gameStart = gameStart;
    }

    public String getMaxBuyIn() {
        return this.maxBuyIn;
    }

    public void setMaxBuyIn(String maxBuyIn) {
        this.maxBuyIn = maxBuyIn;
    }

    public String getMaxPlayer() {
        return this.maxPlayer;
    }

    public void setMaxPlayer(String maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public String getMinBuyIn() {
        return this.minBuyIn;
    }

    public void setMinBuyIn(String minBuyIn) {
        this.minBuyIn = minBuyIn;
    }

    public String getMinPlayer() {
        return this.minPlayer;
    }

    public void setMinPlayer(String minPlayer) {
        this.minPlayer = minPlayer;
    }

    public String getTotalPlayers() {
        return this.totalPlayers;
    }

    public void setTotalPlayers(String totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public String getPlayeType() {
        return this.playeType;
    }

    public void setPlayeType(String playeType) {
        this.playeType = playeType;
    }

    public List<RummyGamePlayer> getPlayer() {
        return this.player;
    }

    public void setPlayer(List<RummyGamePlayer> player) {
        this.player = player;
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getScheduleName() {
        return this.scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getStream() {
        return this.stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getTableCost() {
        return this.tableCost;
    }

    public void setTableCost(String tableCost) {
        this.tableCost = tableCost;
    }

    public String getTableId() {
        return this.tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTableType() {
        return this.tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSuit() {
        return this.suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getFace() {
        return this.face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getAutoPlay() {
        return this.autoPlay;
    }

    public void setAutoPlay(String autoPlay) {
        this.autoPlay = autoPlay;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDealerId() {
        return this.dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getDropPoint() {
        return this.dropPoint;
    }

    public void setDropPoint(String dropPoint) {
        this.dropPoint = dropPoint;
    }

    public String getBet() {
        return this.bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public String getConversion() {
        return this.conversion;
    }

    public void setConversion(String conversion) {
        this.conversion = conversion;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getBonusChips() {
        return this.bonusChips;
    }

    public void setBonusChips(String bonusChips) {
        this.bonusChips = bonusChips;
    }

    public String getTossWinner() {
        return this.tossWinner;
    }

    public void setTossWinner(String tossWinner) {
        this.tossWinner = tossWinner;
    }

    public String getDealerNickName() {
        return this.dealerNickName;
    }

    public void setDealerNickName(String dealerNickName) {
        this.dealerNickName = dealerNickName;
    }

    public String getTurnCount() {
        return this.turnCount;
    }

    public void setTurnCount(String turnCount) {
        this.turnCount = turnCount;
    }

    public String getLevelId() {
        return this.levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getTournamentId() {
        return this.tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public List<RummyGamePlayer> getPlayers() {
        return this.players;
    }

    public void setPlayers(List<RummyGamePlayer> players) {
        this.players = players;
    }

    public String getBuyinamount() {
        return this.buyinamount;
    }

    public void setBuyinamount(String buyinamount) {
        this.buyinamount = buyinamount;
    }

    public String getDepositAmount() {
        return this.depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getCharNo() {
        return this.charNo;
    }

    public void setCharNo(String charNo) {
        this.charNo = charNo;
    }

    public String getTimeOut() {
        return this.timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getTotalCounr() {
        return this.totalCounr;
    }

    public void setTotalCounr(String totalCounr) {
        this.totalCounr = totalCounr;
    }

    public String getFunInPlay() {
        return this.funInPlay;
    }

    public void setFunInPlay(String funInPlay) {
        this.funInPlay = funInPlay;
    }

    public int getErrorMessage() {
        return 0;
    }

    public RummyTableCards getTableCards() {
        return this.tableCards;
    }

    public void setTableCards(RummyTableCards tableCards) {
        this.tableCards = tableCards;
    }

    public boolean isIamBack() {
        return this.isIamBack;
    }

    public void setIamBack(boolean iamBack) {
        this.isIamBack = iamBack;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRebuyInAmt() {
        return rebuyInAmt;
    }

    public void setRebuyInAmt(String rebuyInAmt) {
        this.rebuyInAmt = rebuyInAmt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTotalgamePlayers() {
        return totalgamePlayers;
    }

    public void setTotalgamePlayers(String totalgamePlayers) {
        this.totalgamePlayers = totalgamePlayers;
    }

    public String getTournamentStr() {
        return tournamentStr;
    }

    public void setTournamentStr(String tournamentStr) {
        this.tournamentStr = tournamentStr;
    }

    public String getTournamentType() {
        return tournamentType;
    }

    public void setTournamentType(String tournamentType) {
        this.tournamentType = tournamentType;
    }

    public String getTourneyCost() {
        return tourneyCost;
    }

    public void setTourneyCost(String tourneyCost) {
        this.tourneyCost = tourneyCost;
    }

    public String getVipcode() {
        return vipcode;
    }

    public void setVipcode(String vipcode) {
        this.vipcode = vipcode;
    }

    public String getWaiting() {
        return waiting;
    }

    public void setWaiting(String waiting) {
        this.waiting = waiting;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getTo_level() {
        return to_level;
    }

    public void setTo_level(String to_level) {
        this.to_level = to_level;
    }

    public String getValue_rake() {
        return value_rake;
    }

    public void setValue_rake(String value_rake) {
        this.value_rake = value_rake;
    }

    public String getChiptype() {
        return chiptype;
    }

    public void setChiptype(String chiptype) {
        this.chiptype = chiptype;
    }

    public String getBuyin_amount() {
        return buyin_amount;
    }

    public void setBuyin_amount(String buyin_amount) {
        this.buyin_amount = buyin_amount;
    }

    public String getHighest_rank() {
        return highest_rank;
    }

    public void setHighest_rank(String highest_rank) {
        this.highest_rank = highest_rank;
    }
}
