package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import in.glg.rummy.api.requests.RummyBaserequest;

@Root(name = "request", strict = false)
public class RummySplitRequest extends RummyBaserequest {
    @Attribute(name = "command", required = false)
    private String command;
    @Attribute(name = "nick_name", required = false)
    private String nick_name;
    @Attribute(name = "table_id", required = false)
    private String tableId;
    @Attribute(name = "user_id", required = false)
    private String userId;

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

    public String getNick_name() {
        return this.nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
