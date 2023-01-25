package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import in.glg.rummy.api.requests.RummyBaserequest;

@Root(name = "request", strict = false)
public class RummyFavouriteRequest extends RummyBaserequest {
    @Attribute(name = "user_id")
    private String user_id;
    @Attribute(name = "nick_name")
    private String nick_name;
    @Attribute(name = "command")
    private String command;
    @Attribute(name = "set")
    private String set;
    @Attribute(name = "table_cost")
    private String tableCost;
    @Attribute(name = "table_id")
    private String tableId;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getTableCost() {
        return tableCost;
    }

    public void setTableCost(String tableCost) {
        this.tableCost = tableCost;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

}
