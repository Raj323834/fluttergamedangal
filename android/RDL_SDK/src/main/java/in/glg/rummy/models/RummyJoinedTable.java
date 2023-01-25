package in.glg.rummy.models;

public class RummyJoinedTable {

    private String tabelId = "";
    private String tourneyId = "";
    private boolean isTourney = false;

    public String getTabelId() {
        return tabelId;
    }

    public void setTabelId(String tabelId) {
        this.tabelId = tabelId;
    }

    public String getTourneyId() {
        return tourneyId;
    }

    public void setTourneyId(String tourneyId) {
        this.tourneyId = tourneyId;
    }

    public boolean isTourney() {
        return isTourney;
    }

    public void setTourney(boolean tourney) {
        isTourney = tourney;
    }
}
