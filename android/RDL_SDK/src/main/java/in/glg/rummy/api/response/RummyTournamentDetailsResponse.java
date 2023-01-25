package in.glg.rummy.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import in.glg.rummy.models.RummyLevels;

/**
 * Created by GridLogic on 7/12/17.
 */

@Root(strict = false)
public class RummyTournamentDetailsResponse extends RummyBaseResponse
{
    @Attribute(name = "cancel_or_withdraw_registration_time", required = false)
    private String cancel_or_withdraw_registration_time;

    @Attribute(name = "cash_prize", required = false)
    private String cash_prize;

    @Attribute(name = "amounttype", required = false)
    private String amounttype;

    @Attribute(name = "currentlevelminimumchips", required = false)
    private String currentlevelminimumchips;

    @Attribute(name = "currentlevelrebuyin", required = false)
    private String currentlevelrebuyin;

    @Attribute(name = "nextlevelminchips", required = false)
    private String nextlevelminchips;

    @Attribute(name = "nextlevelrebuyin", required = false)
    private String nextlevelrebuyin;

    @Attribute(name = "leveldetails", required = false)
    private String leveldetails;

    @Attribute(name = "leveltimer", required = false)
    private String leveltimer;

    @Attribute(name = "prize", required = false)
    private String prize;

    @Attribute(name = "current_level", required = false)
    private String current_level;

    @Attribute(name = "entry", required = false)
    private String entry;

    @Attribute(name = "finalprize", required = false)
    private String finalprize;

    @Attribute(name = "max_registration", required = false)
    private String max_registration;

    @Attribute(name = "players", required = false)
    private String players;

    @Attribute(name = "registrations_start_date", required = false)
    private String registrations_start_date;

    @Attribute(name = "show_end_date", required = false)
    private String show_end_date;

    @Attribute(name = "show_start_date", required = false)
    private String show_start_date;

    @Attribute(name = "status", required = false)
    private String status;

    @Attribute(name = "time_to_close_registrations", required = false)
    private String time_to_close_registrations;

    @Attribute(name = "tournament_SD", required = false)
    private String tournament_SD;

    @Attribute(name = "tournament_id", required = false)
    private String tournament_id;

    @Attribute(name = "tournament_name", required = false)
    private String tournament_name;

    @Attribute(name = "tournament_start_date", required = false)
    private String tournament_start_date;

    @Attribute(name = "tournament_type", required = false)
    private String tournament_type;

    @Attribute(name = "tourney_cost", required = false)
    private String tourney_cost;

    @Attribute(name = "wait_list", required = false)
    private String wait_list;

    @Attribute(name = "status_in_tourney", required = false)
    private String status_in_tourney;

    @Attribute(name = "eligible", required = false)
    private String eligible;

    @Attribute(name = "tourney_chips", required = false)
    private String tourney_chips;

    @Attribute(name = "tourney_inplay", required = false)
    private String tourney_inplay;

    @Attribute(name = "registered", required = false)
    private String registered;

    @Attribute(name = "data", required = false)
    private String data;

    @Attribute(name = "vipcode", required = false)
    private String vipcode;

    @Attribute(name = "waitstatus", required = false)
    private String waitstatus;

    @Attribute(name = "topfive_players", required = false)
    private String topfive_players;

    @Attribute(name = "my_current_level", required = false)
    private String my_current_level;

    @Attribute(name = "rank", required = false)
    private String rank;

    @ElementList(name = "levels", required = false)
    private List<RummyLevels> levels;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getMy_current_level() {
        return my_current_level;
    }

    public void setMy_current_level(String my_current_level) {
        this.my_current_level = my_current_level;
    }

    public List<RummyLevels> getLevels() {
        return levels;
    }

    public void setLevels(List<RummyLevels> levels) {
        this.levels= levels;
    }

    public int getErrorMessage() {
        return 0;
    }

    public String getCancel_or_withdraw_registration_time() {
        return cancel_or_withdraw_registration_time;
    }

    public void setCancel_or_withdraw_registration_time(String cancel_or_withdraw_registration_time) {
        this.cancel_or_withdraw_registration_time = cancel_or_withdraw_registration_time;
    }

    public String getCash_prize() {
        return cash_prize;
    }

    public void setCash_prize(String cash_prize) {
        this.cash_prize = cash_prize;
    }

    public String getCurrent_level() {
        return current_level;
    }

    public void setCurrent_level(String current_level) {
        this.current_level = current_level;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getFinalprize() {
        return finalprize;
    }

    public void setFinalprize(String finalprize) {
        this.finalprize = finalprize;
    }

    public String getMax_registration() {
        return max_registration;
    }

    public void setMax_registration(String max_registration) {
        this.max_registration = max_registration;
    }

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public String getRegistrations_start_date() {
        return registrations_start_date;
    }

    public void setRegistrations_start_date(String registrations_start_date) {
        this.registrations_start_date = registrations_start_date;
    }

    public String getShow_end_date() {
        return show_end_date;
    }

    public void setShow_end_date(String show_end_date) {
        this.show_end_date = show_end_date;
    }

    public String getShow_start_date() {
        return show_start_date;
    }

    public void setShow_start_date(String show_start_date) {
        this.show_start_date = show_start_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime_to_close_registrations() {
        return time_to_close_registrations;
    }

    public void setTime_to_close_registrations(String time_to_close_registrations) {
        this.time_to_close_registrations = time_to_close_registrations;
    }

    public String getTournament_SD() {
        return tournament_SD;
    }

    public void setTournament_SD(String tournament_SD) {
        this.tournament_SD = tournament_SD;
    }

    public String getTournament_id() {
        return tournament_id;
    }

    public void setTournament_id(String tournament_id) {
        this.tournament_id = tournament_id;
    }

    public String getTournament_name() {
        return tournament_name;
    }

    public void setTournament_name(String tournament_name) {
        this.tournament_name = tournament_name;
    }

    public String getTournament_start_date() {
        return tournament_start_date;
    }

    public void setTournament_start_date(String tournament_start_date) {
        this.tournament_start_date = tournament_start_date;
    }

    public String getTournament_type() {
        return tournament_type;
    }

    public void setTournament_type(String tournament_type) {
        this.tournament_type = tournament_type;
    }

    public String getTourney_cost() {
        return tourney_cost;
    }

    public void setTourney_cost(String tourney_cost) {
        this.tourney_cost = tourney_cost;
    }

    public String getWait_list() {
        return wait_list;
    }

    public void setWait_list(String wait_list) {
        this.wait_list = wait_list;
    }

    public String getStatus_in_tourney() {
        return status_in_tourney;
    }

    public void setStatus_in_tourney(String status_in_tourney) {
        this.status_in_tourney = status_in_tourney;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getVipcode() {
        return vipcode;
    }

    public void setVipcode(String vipcode) {
        this.vipcode = vipcode;
    }

    public String getWaitstatus() {
        return waitstatus;
    }

    public void setWaitstatus(String waitstatus) {
        this.waitstatus = waitstatus;
    }

    public String getEligible() {
        return eligible;
    }

    public void setEligible(String eligible) {
        this.eligible = eligible;
    }

    public String getTourney_chips() {
        return tourney_chips;
    }

    public void setTourney_chips(String tourney_chips) {
        this.tourney_chips = tourney_chips;
    }

    public String getTourney_inplay() {
        return tourney_inplay;
    }

    public void setTourney_inplay(String tourney_inplay) {
        this.tourney_inplay = tourney_inplay;
    }

    public String getTopfive_players() {
        return topfive_players;
    }

    public void setTopfive_players(String topfive_players) {
        this.topfive_players = topfive_players;
    }

    public String getLeveldetails() {
        return leveldetails;
    }

    public void setLeveldetails(String leveldetails) {
        this.leveldetails = leveldetails;
    }

    public String getLeveltimer() {
        return leveltimer;
    }

    public void setLeveltimer(String leveltimer) {
        this.leveltimer = leveltimer;
    }

    public String getAmounttype() {
        return amounttype;
    }

    public void setAmounttype(String amounttype) {
        this.amounttype = amounttype;
    }

    public String getCurrentlevelminimumchips() {
        return currentlevelminimumchips;
    }

    public void setCurrentlevelminimumchips(String currentlevelminimumchips) {
        this.currentlevelminimumchips = currentlevelminimumchips;
    }

    public String getCurrentlevelrebuyin() {
        return currentlevelrebuyin;
    }

    public void setCurrentlevelrebuyin(String currentlevelrebuyin) {
        this.currentlevelrebuyin = currentlevelrebuyin;
    }

    public String getNextlevelminchips() {
        return nextlevelminchips;
    }

    public void setNextlevelminchips(String nextlevelminchips) {
        this.nextlevelminchips = nextlevelminchips;
    }

    public String getNextlevelrebuyin() {
        return nextlevelrebuyin;
    }

    public void setNextlevelrebuyin(String nextlevelrebuyin) {
        this.nextlevelrebuyin = nextlevelrebuyin;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }
}
