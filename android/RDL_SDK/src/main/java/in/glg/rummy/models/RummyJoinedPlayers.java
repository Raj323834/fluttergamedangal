package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by GridLogic on 12/12/17.
 */
@Root(strict = false)
public class RummyJoinedPlayers implements Serializable
{
    @Attribute(name = "nick_name", required = false)
    private String nick_name;

    @Attribute(name = "rank", required = false)
    private String rank;

    @Attribute(name = "tourney_chips", required = false)
    private String tourney_chips;

    @Attribute(name = "tourney_inplay", required = false)
    private String tourney_inplay;

    @Attribute(name = "user_id", required = false)
    private String user_id;

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTourney_chips() {
        return tourney_chips;
    }

    public void setTourney_chips(String tourney_chips) {
        this.tourney_chips = tourney_chips;
    }

    public String getTourney_inplay() {
        return tourney_inplay;
    }

    public void setTourney_inplay(String tourney_inplay) {
        this.tourney_inplay = tourney_inplay;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
