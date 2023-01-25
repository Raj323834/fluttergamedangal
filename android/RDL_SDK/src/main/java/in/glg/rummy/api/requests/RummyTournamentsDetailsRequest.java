package in.glg.rummy.api.requests;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by GridLogic on 4/12/17.
 */

@Root(name = "request",strict = false)
public class RummyTournamentsDetailsRequest extends RummyBaserequest
{
    @Attribute(name = "command")
    private String command;

    @Attribute(name = "tournament_id", required = false)
    private String tournament_id;

    @Attribute(name = "level", required = false)
    private String level;

    @Attribute(name = "player_amount", required = false)
    private String player_amount;

    @Attribute(name = "vipcode", required = false)
    private String vipcode;

    @Attribute(name = "amount", required = false)
    private String amount;
    
    @Attribute(name = "order_id", required = false)
    private String orderId;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTournament_id() {
        return tournament_id;
    }

    public void setTournament_id(String tournament_id) {
        this.tournament_id = tournament_id;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPlayer_amount() {
        return player_amount;
    }

    public void setPlayer_amount(String player_amount) {
        this.player_amount = player_amount;
    }

    public String getVipcode() {
        return vipcode;
    }

    public void setVipcode(String vipcode) {
        this.vipcode = vipcode;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
