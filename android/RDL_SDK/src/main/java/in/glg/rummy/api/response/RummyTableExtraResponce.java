package in.glg.rummy.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import in.glg.rummy.models.RummyEvent;
import in.glg.rummy.models.RummyPickDiscard;
import in.glg.rummy.models.RummyScoreBoard;
import in.glg.rummy.models.RummyTableDetails;

@Root(strict = false)
public class RummyTableExtraResponce extends RummyBaseResponse {
    @Attribute(name = "data", required = false)
    private String data;
    @Element(name = "event", required = false)
    private RummyEvent event;
    @Attribute(name = "fun_chips", required = false)
    private String funChips;
    @Attribute(name = "message", required = false)
    private String message;
    @Attribute(name = "nick_name", required = false)
    private String nickName;
    @ElementList(name = "pickdiscards", required = false)
    private List<RummyPickDiscard> pickDiscardsList;
    @Element(name = "scoreboard", required = false)
    private RummyScoreBoard scoreBoard;
    @Attribute(name = "table_id", required = false)
    private String tableId;
    @Element(name = "table_details", required = false)
    private RummyTableDetails table_details;
    @Attribute(name = "tournament_table", required = false)
    private String tournamentTable;

    public String getTournamentTable() {
        return this.tournamentTable;
    }

    public void setTournamentTable(String tournamentTable) {
        this.tournamentTable = tournamentTable;
    }

    public List<RummyPickDiscard> getPickDiscardsList() {
        return this.pickDiscardsList;
    }

    public void setPickDiscardsList(List<RummyPickDiscard> pickDiscardsList) {
        this.pickDiscardsList = pickDiscardsList;
    }

    public RummyScoreBoard getScoreBoard() {
        return this.scoreBoard;
    }

    public void setScoreBoard(RummyScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public RummyEvent getEvent() {
        return this.event;
    }

    public void setEvent(RummyEvent event) {
        this.event = event;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorMessage() {
        return 0;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTableId() {
        return this.tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public RummyTableDetails getTableDeatils() {
        return this.table_details;
    }

    public void setTableDetails(RummyTableDetails tables) {
        this.table_details = tables;
    }

    public RummyTableDetails getTable_details() {
        return this.table_details;
    }

    public void setTable_details(RummyTableDetails table_details) {
        this.table_details = table_details;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFunChips() {
        return this.funChips;
    }

    public void setFunChips(String funchips) {
        this.funChips = funchips;
    }
}
