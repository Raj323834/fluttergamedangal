package in.glg.rummy.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import in.glg.rummy.models.RummyTournamentTables;

/**
 * Created by GridLogic on 12/12/17.
 */

@Root(strict = false)
public class RummyTournamentsTablesResponse extends RummyBaseResponse
{
    @Attribute(name = "data", required = false)
    private String data;

    @Attribute(name = "topfive_players", required = false)
    private String topfive_players;

    @Attribute(name = "tournament_id", required = false)
    private String tournament_id;

    @ElementList(name = "tables", required = false)
    private List<RummyTournamentTables> tables;

    @Override
    public int getErrorMessage() {
        return 0;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTopfive_players() {
        return topfive_players;
    }

    public void setTopfive_players(String topfive_players) {
        this.topfive_players = topfive_players;
    }

    public String getTournament_id() {
        return tournament_id;
    }

    public void setTournament_id(String tournament_id) {
        this.tournament_id = tournament_id;
    }

    public List<RummyTournamentTables> getTables() {
        return tables;
    }

    public void setTables(List<RummyTournamentTables> tables) {
        this.tables = tables;
    }
}
