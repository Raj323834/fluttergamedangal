package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import in.glg.rummy.api.response.RummyBaseResponse;

@Root(name = "request", strict = false)
public class RummyEngineRequest extends RummyBaseResponse {
    @Attribute(name = "bet", required = false)
    public String bet;
    @Attribute(name = "bonuschips", required = false)
    public String bonusChips;
    @Attribute(name = "bonusinplay", required = false)
    public String bonusInPlay;
    @ElementList(name = "cmeld", required = false)
    private List<RummyMeldBox> checkMeldList;
    @Attribute(name = "command", required = false)
    public String command;
    @Attribute(name = "data", required = false)
    public String data;
    @Attribute(name = "droppoint", required = false)
    public String droppoint;
    @Attribute(name = "event_name", required = false)
    public String eventName;
    @Attribute(name = "face", required = false)
    public String face;
    @Attribute(name = "game_id", required = false)
    private String gameId;
    @Attribute(name = "nick_name", required = false)
    private String nick_name;
    @Attribute(name = "requester", required = false)
    private String requester;
    @Attribute(name = "sucess_userid", required = false)
    public String sucessUserId;
    @Attribute(name = "sucess_nickname", required = false)
    public String sucessUserName;
    @Attribute(name = "suit", required = false)
    public String suit;
    @Attribute(name = "table_id", required = false)
    public String tableId;
    @Attribute(name = "timeout", required = false)
    public String timeout;
    @Attribute(name = "user_id", required = false)
    private String userId;
    @Attribute(name = "buyinamount", required = false)
    private String buyinamount;
    @Attribute(name = "char_no", required = false)
    private String char_no;
    @Attribute(name = "level", required = false)
    private String level;
    @Attribute(name = "seat", required = false)
    private String seat;
    @Attribute(name = "table_cost", required = false)
    private String table_cost;
    @Attribute(name = "table_join_as", required = false)
    private String table_join_as;
    @Attribute(name = "table_type", required = false)
    private String table_type;
    @Attribute(name = "tournament_id", required = false)
    private String tournament_id;
    @ElementList(name = "wmeld", required = false)
    private List<RummyMeldBox> wrongMeldList;
    @Attribute(name = "old_table_id", required = false)
    private String old_table_id;

    public int getErrorMessage() {
        return 0;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequester() {
        return this.requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDroppoint() {
        return this.droppoint;
    }

    public void setDroppoint(String droppoint) {
        this.droppoint = droppoint;
    }

    public String getBet() {
        return this.bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public String getBonusChips() {
        return this.bonusChips;
    }

    public void setBonusChips(String bonusChips) {
        this.bonusChips = bonusChips;
    }

    public String getBonusInPlay() {
        return this.bonusInPlay;
    }

    public void setBonusInPlay(String bonusInPlay) {
        this.bonusInPlay = bonusInPlay;
    }

    public String getTimeout() {
        return this.timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getSucessUserName() {
        return this.sucessUserName;
    }

    public void setSucessUserName(String sucessUserName) {
        this.sucessUserName = sucessUserName;
    }

    public String getSucessUserId() {
        return this.sucessUserId;
    }

    public void setSucessUserId(String sucessUserId) {
        this.sucessUserId = sucessUserId;
    }

    public String getTableId() {
        return this.tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getFace() {
        return this.face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getSuit() {
        return this.suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<RummyMeldBox> getCheckMeldList() {
        return this.checkMeldList;
    }

    public void setCheckMeldList(List<RummyMeldBox> checkMeldList) {
        this.checkMeldList = checkMeldList;
    }

    public List<RummyMeldBox> getWrongMeldList() {
        return this.wrongMeldList;
    }

    public void setWrongMeldList(List<RummyMeldBox> wrongMeldList) {
        this.wrongMeldList = wrongMeldList;
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getBuyinamount() {
        return buyinamount;
    }

    public void setBuyinamount(String buyinamount) {
        this.buyinamount = buyinamount;
    }

    public String getChar_no() {
        return char_no;
    }

    public void setChar_no(String char_no) {
        this.char_no = char_no;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getTable_cost() {
        return table_cost;
    }

    public void setTable_cost(String table_cost) {
        this.table_cost = table_cost;
    }

    public String getTable_join_as() {
        return table_join_as;
    }

    public void setTable_join_as(String table_join_as) {
        this.table_join_as = table_join_as;
    }

    public String getTable_type() {
        return table_type;
    }

    public void setTable_type(String table_type) {
        this.table_type = table_type;
    }

    public String getTournament_id() {
        return tournament_id;
    }

    public void setTournament_id(String tournament_id) {
        this.tournament_id = tournament_id;
    }

    public String getOld_table_id() {
        return old_table_id;
    }

    public void setOld_table_id(String old_table_id) {
        this.old_table_id = old_table_id;
    }
}
