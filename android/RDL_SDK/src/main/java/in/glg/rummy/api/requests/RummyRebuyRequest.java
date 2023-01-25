package in.glg.rummy.api.requests;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "request", strict = false)
public class RummyRebuyRequest extends RummyBaserequest {
    @Attribute(name = "command", required = false)
    private String command;
    
    @Attribute(name = "table_id", required = false)
    private String table_id;
    
    @Attribute(name = "user_id", required = false)
    private String user_id;
    
    @Attribute(name = "rebuyinamt", required = false)
    private String rebuyinamt;
    
    @Attribute(name = "order_id", required = false)
    private String orderId;
    
    public String getCommand() {
        return command;
    }
    
    public void setCommand(String command) {
        this.command = command;
    }
    
    public String getTable_id() {
        return table_id;
    }
    
    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }
    
    public String getUser_id() {
        return user_id;
    }
    
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    
    public String getRebuyinamt() {
        return rebuyinamt;
    }
    
    public void setRebuyinamt(String rebuyinamt) {
        this.rebuyinamt = rebuyinamt;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
