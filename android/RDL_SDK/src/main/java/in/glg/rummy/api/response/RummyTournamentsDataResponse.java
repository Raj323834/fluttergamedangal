package in.glg.rummy.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import in.glg.rummy.models.RummyTournament;

/**
 * Created by GridLogic on 1/12/17.
 */

@Root(strict = false)
public class RummyTournamentsDataResponse extends RummyBaseResponse
{
    @Attribute(name = "data", required = false)
    private String data;

    @ElementList(name = "tournaments", required = false)
    private List<RummyTournament> tournaments;

    public List<RummyTournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<RummyTournament> tournaments) {
        this.tournaments = tournaments;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getErrorMessage() {
        return 0;
    }
}
