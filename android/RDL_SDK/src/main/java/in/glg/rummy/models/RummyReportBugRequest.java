package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import in.glg.rummy.api.requests.RummyBaserequest;

@Root(name = "event",strict = false)
public class RummyReportBugRequest extends RummyBaserequest {
    @Attribute(name = "bugexplanation", required = false)
    private String bugExplanation;
    @Attribute(name = "bug_type", required = false)
    private String bugType;
    @Attribute(name = "event_name", required = false)
    private String eventName;
    @Attribute(name = "game_id", required = false)
    private String gameId;
    @Attribute(name = "game_type", required = false)
    private String gameType;
    @Attribute(name = "table_id", required = false)
    private String tableId;

    public String getBugType() {
        return this.bugType;
    }

    public void setBugType(String bugType) {
        this.bugType = bugType;
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getTableId() {
        return this.tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getBugExplanation() {
        return this.bugExplanation;
    }

    public void setBugExplanation(String bugExplanation) {
        this.bugExplanation = bugExplanation;
    }

    public String getGameType() {
        return this.gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }
}
