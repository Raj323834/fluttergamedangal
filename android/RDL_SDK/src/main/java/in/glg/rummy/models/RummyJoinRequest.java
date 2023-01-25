package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import in.glg.rummy.api.requests.RummyBaserequest;

@Root(name = "request", strict = false)
public class RummyJoinRequest extends RummyBaserequest {
    @Attribute(name = "buyinamount")
    private String buyinamount;
    @Attribute(name = "char_no")
    private String charNo;
    @Attribute(name = "command")
    private String command;
    @Attribute(name = "seat")
    private int seat;
    @Attribute(name = "table_cost")
    private String tableCost;
    @Attribute(name = "table_id")
    private String tableId;
    @Attribute(name = "table_join_as")
    private String tableJoinAs;
    @Attribute(name = "table_type")
    private String tableType;
    @Attribute(name = "order_id",required = false)
    private String orderId;

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

    public String getTableJoinAs() {
        return this.tableJoinAs;
    }

    public void setTableJoinAs(String tableJoinAs) {
        this.tableJoinAs = tableJoinAs;
    }

    public String getTableType() {
        return this.tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getTableCost() {
        return this.tableCost;
    }

    public void setTableCost(String tableCost) {
        this.tableCost = tableCost;
    }

    public String getBuyinamount() {
        return this.buyinamount;
    }

    public void setBuyinamount(String buyinamount) {
        this.buyinamount = buyinamount;
    }

    public int getSeat() {
        return this.seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getCharNo() {
        return this.charNo;
    }

    public void setCharNo(String charNo) {
        this.charNo = charNo;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
