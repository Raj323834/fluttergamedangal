package in.glg.rummy.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import in.glg.rummy.models.RummyTableDetails;

@Root(strict = false)
public class RummyTableDeatailResponse extends RummyBaseResponse {
    @Attribute
    private String data;
    @Attribute(name = "fun_chips", required = false)
    private String funChips;
    @Attribute(name = "rank", required = false)
    private String rank;
    @Attribute(name = "message", required = false)
    private String message;
    @Attribute(name = "nick_name", required = false)
    private String nickName;
    @Attribute(name = "table_id", required = false)
    private String tableId;
    @Element(name = "table_details", required = false)
    private RummyTableDetails table_details;
    @Attribute(name = "tournament_table", required = false)
    private String tournamentTable;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTournamentTable() {
        if(this.tournamentTable != null)
        {
            return this.tournamentTable;
        }
        else
        {
            return "";
        }

    }

    public void setTournamentTable(String tournamentTable) {
        this.tournamentTable = tournamentTable;
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
