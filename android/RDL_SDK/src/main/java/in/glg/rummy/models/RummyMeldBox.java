package in.glg.rummy.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "box",strict = false)
public class RummyMeldBox implements Serializable {
    @ElementList(inline = true, name = "card", required = false)
    private List<RummyPlayingCard> meldBoxes;

    public List<RummyPlayingCard> getMeldBoxes() {
        return this.meldBoxes;
    }

    public void setMeldBoxes(List<RummyPlayingCard> meldBoxes) {
        this.meldBoxes = meldBoxes;
    }
}
