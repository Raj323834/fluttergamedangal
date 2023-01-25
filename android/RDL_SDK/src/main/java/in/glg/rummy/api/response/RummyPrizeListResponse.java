package in.glg.rummy.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import in.glg.rummy.models.RummyPrizeList;

/**
 * Created by GridLogic on 12/12/17.
 */

@Root(strict = false)
public class RummyPrizeListResponse extends RummyBaseResponse
{
    @Attribute(name = "data", required = false)
    private String data;

    @Attribute(name = "tournament_id", required = false)
    private String tournament_id;

    @ElementList(name = "prize_list", required = false)
    private List<RummyPrizeList> prize_list;

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

    public List<RummyPrizeList> getPrize_list() {
        return prize_list;
    }

    public void setPrize_list(List<RummyPrizeList> prize_list) {
        this.prize_list = prize_list;
    }

    @Override
    public int getErrorMessage() {
        return 0;
    }
}
