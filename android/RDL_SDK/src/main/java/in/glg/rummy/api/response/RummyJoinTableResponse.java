package in.glg.rummy.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import in.glg.rummy.models.RummyPickDiscard;

@Root(strict = false)
public class RummyJoinTableResponse extends RummyBaseResponse {
    @Attribute(name = "auto_extra_time", required = false)
    private String autoExtraTime;
    @Attribute(name = "buyinamount", required = false)
    private String buyinamount;
    @Attribute(name = "char_no", required = false)
    private String charNo;
    @Attribute(name = "data", required = false)
    private String data;
    @Attribute(name = "game_id", required = false)
    private String gameId;
    @Attribute(name = "nick_name", required = false)
    private String nickName;
    @Element(name = "pickcards", required = false)
    private RummyPickDiscard pickcards;
    @Attribute(name = "prizemoney", required = false)
    private String prizeMoney;
    @Attribute(name = "seat", required = false)
    private String seat;
    @Attribute(name = "table_id", required = false)
    private String tableId;
    @Attribute(name = "table_join_as", required = false)
    private String tableJoinAs;
    @Attribute(name = "time_out", required = false)
    private String timeOut;
    @Attribute(name = "tournament_table", required = false)
    private String tournamentTable;

    public String getPrizeMoney() {
        return this.prizeMoney;
    }

    public void setPrizeMoney(String prizeMoney) {
        this.prizeMoney = prizeMoney;
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getTimeOut() {
        return this.timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getErrorMessage() {
        return 0;
    }

    public String getBuyinamount() {
        return this.buyinamount;
    }

    public void setBuyinamount(String buyinamount) {
        this.buyinamount = buyinamount;
    }

    public String getCharNo() {
        return this.charNo;
    }

    public void setCharNo(String charNo) {
        this.charNo = charNo;
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

    public String getTableJoinAs() {
        return this.tableJoinAs;
    }

    public void setTableJoinAs(String tableJoinAs) {
        this.tableJoinAs = tableJoinAs;
    }

    public String getSeat() {
        return this.seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }
}
