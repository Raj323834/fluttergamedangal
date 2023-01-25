package in.glg.rummy.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import in.glg.rummy.models.RummyGamePlayer;

/**
 * Created by GridLogic on 31/1/18.
 */

@Root(strict = false)
public class RummyWinnerResponse extends RummyBaseResponse
{
    @Attribute(name = "data", required = false)
    private String data;

    @Attribute(name = "tournament_id", required = false)
    private String tournament_id;

    @ElementList(name = "players", required = false)
    private List<RummyGamePlayer> players;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTournament_id() {
        return tournament_id;
    }

    public void setTournament_id(String tournament_id) {
        this.tournament_id = tournament_id;
    }

    public List<RummyGamePlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<RummyGamePlayer> players) {
        this.players = players;
    }

    @Override
    public int getErrorMessage() {
        return 0;
    }
}
