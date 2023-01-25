package in.glg.rummy.POJOClasses;

/**
 * Created by GridLogic on 26/7/17.
 */

public class RummyLobbyPOJO
{
    private String tableID;
    private String entryFee;
    private String cashPrize;
    private String players;
    private String seating;
    private String action;

    public RummyLobbyPOJO(String tableID, String entryFee, String cashPrize, String players, String seating, String action) {
        this.tableID = tableID;
        this.entryFee = entryFee;
        this.cashPrize = cashPrize;
        this.players = players;
        this.seating = seating;
        this.action = action;
    }

    public String getTableID() {
        return tableID;
    }

    public void setTableID(String tableID) {
        this.tableID = tableID;
    }

    public String getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(String entryFee) {
        this.entryFee = entryFee;
    }

    public String getCashPrize() {
        return cashPrize;
    }

    public void setCashPrize(String cashPrize) {
        this.cashPrize = cashPrize;
    }

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public String getSeating() {
        return seating;
    }

    public void setSeating(String seating) {
        this.seating = seating;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
