package in.glg.rummy.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(strict = false)
public class RummySplitter implements Serializable {
    @ElementList(data = false, inline = true, name = "player", required = false)
    private List<RummyGamePlayer> player;

    public List<RummyGamePlayer> getPlayer() {
        return this.player;
    }

    public void setPlayer(List<RummyGamePlayer> player) {
        this.player = player;
    }
}
