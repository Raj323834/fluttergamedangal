package in.glg.rummy.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class RummyScoreBoard {
    @ElementList(data = false, inline = true, name = "game", required = false)
    private List<RummyGame> game;

    public List<RummyGame> getGame() {
        return this.game;
    }

    public void setGame(List<RummyGame> game) {
        this.game = game;
    }
}
