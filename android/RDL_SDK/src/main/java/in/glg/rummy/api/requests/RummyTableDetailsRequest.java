package in.glg.rummy.api.requests;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "request",strict = false)
public class RummyTableDetailsRequest extends RummyBaserequest {
    @Attribute(name = "command")
    private String command;
    @Attribute(name = "table_id", required = false)
    private String tableId;
    @Attribute(name = "user_id", required = false)
    private String userId;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getTableId() {
        return this.tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
}
