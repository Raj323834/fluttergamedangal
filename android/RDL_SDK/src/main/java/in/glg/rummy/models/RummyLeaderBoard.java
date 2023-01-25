package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class RummyLeaderBoard {

    @Attribute(name = "rank", required = false)
    private String rank;

    @Attribute(name = "username", required = false)
    private String username;

    @Attribute(name = "winning", required = false)
    private String winning;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWinning() {
        return winning;
    }

    public void setWinning(String winning) {
        this.winning = winning;
    }
}
