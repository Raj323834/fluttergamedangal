package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by GridLogic on 13/12/17.
 */

@Root(name = "tourney",strict = false)
public class RummyTourney implements Serializable
{
    @Attribute(name = "tournament_id", required = false)
    private String tournament_id;

    @Attribute(name = "tourney_chips", required = false)
    private String tourney_chips;

    @Attribute(name = "tourney_inplay", required = false)
    private String tourney_inplay;


    public String getTournament_id() {
        return tournament_id;
    }

    public void setTournament_id(String tournament_id) {
        this.tournament_id = tournament_id;
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
}
