package in.glg.rummy.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class RummyDropList {
    @ElementList(name = "player", required = false)
    private List<RummyGamePlayer> players;
}
