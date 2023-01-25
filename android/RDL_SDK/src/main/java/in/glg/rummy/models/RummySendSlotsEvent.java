package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import in.glg.rummy.api.response.RummyBaseResponse;

@Root(name = "event",strict = false)
public class RummySendSlotsEvent extends RummyBaseResponse {
    @Attribute(name = "event_name", required = false)
    private String eventName;
    @Element(name = "table", required = false)
    private RummyTableCards table;
    @Attribute(name = "table_id", required = false)
    private String tableId;

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

    public RummyTableCards getTable() {
        return this.table;
    }

    public void setTable(RummyTableCards table) {
        this.table = table;
    }

    public int getErrorMessage() {
        return 0;
    }
}
