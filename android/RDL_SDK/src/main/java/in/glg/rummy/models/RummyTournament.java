package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "tournaments",strict = false)
public class RummyTournament implements Serializable {
    @Attribute(name = "cash_prize", required = false)
    private String cashPrize;
    @Attribute(name = "entry", required = false)
    private String entry;
    @Attribute(name = "finalprize", required = false)
    private String finalPrize;
    @Attribute(name = "players", required = false)
    private String players;
    @Attribute(name = "start_date", required = false)
    private String startDate;
    @Attribute(name = "status", required = false)
    private String status;
    @Attribute(name = "tournament_name", required = false)
    private String tournamentName;
    @Attribute(name = "tournament_sd", required = false)
    private String tournamentSd;
    @Attribute(name = "tourney_cost", required = false)
    private String tourneyCost;
    @Attribute(name = "tourney_id", required = false)
    private String tourneyId;
    @Attribute(name = "type", required = false)
    private String type;
    @Attribute(name = "reg_status", required = false)
    private String reg_status;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCashPrize() {
        return this.cashPrize;
    }

    public void setCashPrize(String cashPrize) {
        this.cashPrize = cashPrize;
    }

    public String getEntry() {
        return this.entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getFinalPrize() {
        return this.finalPrize;
    }

    public void setFinalPrize(String finalPrize) {
        this.finalPrize = finalPrize;
    }

    public String getPlayers() {
        return this.players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTournamentName() {
        return this.tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getTournamentSd() {
        return this.tournamentSd;
    }

    public void setTournamentSd(String tournamentSd) {
        this.tournamentSd = tournamentSd;
    }

    public String getTourneyCost() {
        return this.tourneyCost;
    }

    public void setTourneyCost(String tourneyCost) {
        this.tourneyCost = tourneyCost;
    }

    public String getTourneyId() {
        return this.tourneyId;
    }

    public void setTourneyId(String tourneyId) {
        this.tourneyId = tourneyId;
    }

    public String getReg_status() {
        return reg_status;
    }

    public void setReg_status(String reg_status) {
        this.reg_status = reg_status;
    }
}
