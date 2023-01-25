package in.glg.rummy.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by GridLogic on 13/12/17.
 */

@Root(strict = false)
public class RummyTourneyRegistrationResponse extends RummyBaseResponse
{
    @Attribute(name = "tournament_id", required = false)
    private String tournament_id;

    @Attribute(name = "data", required = false)
    private String data;

    @Attribute(name = "user_id", required = false)
    private String user_id;

    @Attribute(name = "nick_name", required = false)
    private String nick_name;

    @Attribute(name = "waiting", required = false)
    private String waiting;

    @Attribute(name = "tournament_chips", required = false)
    private String tournament_chips;

    @Attribute(name = "vipcode", required = false)
    private String vipcode;

    @Attribute(name = "amount", required = false)
    private String amount;

    @Attribute(name = "days", required = false)
    private String days;

    public String getTournament_id() {
        return tournament_id;
    }

    public void setTournament_id(String tournament_id) {
        this.tournament_id = tournament_id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

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

    public String getWaiting() {
        return waiting;
    }

    public void setWaiting(String waiting) {
        this.waiting = waiting;
    }

    public String getTournament_chips() {
        return tournament_chips;
    }

    public void setTournament_chips(String tournament_chips) {
        this.tournament_chips = tournament_chips;
    }

    public String getVipcode() {
        return vipcode;
    }

    public void setVipcode(String vipcode) {
        this.vipcode = vipcode;
    }

    public void setAmount(String amount){
        this.amount = amount;
    }

    public String getAmount(){
        return amount;
    }

    public void setDays(String amount){
        this.days = days;
    }

    public String getDays(){
        return days;
    }

    @Override
    public int getErrorMessage() {
        return 0;
    }
}
