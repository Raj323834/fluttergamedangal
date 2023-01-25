package in.glg.rummy.models;

import java.util.ArrayList;

public class RummyMeldCard {
    public RummyPlayingCard dicardCard;
    public boolean isValidShow;
    public RummyPlayingCard jokerCard;
    public ArrayList<ArrayList<RummyPlayingCard>> meldGroup;
    public String msgUuid;
    public String tableId;
}
