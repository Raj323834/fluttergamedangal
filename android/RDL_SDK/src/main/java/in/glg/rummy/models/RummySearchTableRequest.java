package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import in.glg.rummy.api.requests.RummyBaserequest;

@Root(name = "request", strict = false)
public class RummySearchTableRequest extends RummyBaserequest {
    @Attribute(name = "bet")
    private String bet;
    @Attribute(name = "command")
    private String command;
    @Attribute(name = "conversion")
    private String conversion;
    @Attribute(name = "maxplayers")
    private String maxPlayers;
    @Attribute(name = "nick_name")
    private String nickName;
    @Attribute(name = "stream_id")
    private String streamId;
    @Attribute(name = "stream_name")
    private String streamName;
    @Attribute(name = "table_cost")
    private String tableCost;
    @Attribute(name = "table_id")
    private String tableId;
    @Attribute(name = "table_type")
    private String tableType;
    @Attribute(name = "user_id")
    private String userId;
    @Attribute(name = "buyinamount")
    private String buyinamount;
    @Attribute(name = "table_join_as")
    private String tableJoinAs;
    @Attribute(name = "unique_gamesettings_id")
    private String unique_gamesettings_id;
    @Attribute(name = "order_id",required = false)
    private String orderId;

    public String getStreamId() {
        return this.streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getStreamName() {
        return this.streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public String getBet() {
        return this.bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public String getMaxPlayers() {
        return this.maxPlayers;
    }

    public void setMaxPlayers(String maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getConversion() {
        return this.conversion;
    }

    public void setConversion(String conversion) {
        this.conversion = conversion;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

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
        return buyinamount;
    }

    public void setBuyinamount(String buyinamount) {
        this.buyinamount = buyinamount;
    }

    public String getTableJoinAs() {
        return tableJoinAs;
    }

    public void setTableJoinAs(String tableJoinAs) {
        this.tableJoinAs = tableJoinAs;
    }

    public String getUnique_gamesettings_id() {
        return unique_gamesettings_id;
    }

    public void setUnique_gamesettings_id(String unique_gamesettings_id) {
        this.unique_gamesettings_id = unique_gamesettings_id;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
