package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GridLogic on 12/12/17.
 */

@Root(strict = false)
public class RummyTournamentTables implements Serializable
{
    @Attribute(name = "bet", required = false)
    private String bet;

    @Attribute(name = "current_players", required = false)
    private String current_players;

    @Attribute(name = "game_schedule", required = false)
    private String game_schedule;

    @Attribute(name = "game_start", required = false)
    private String game_start;

    @Attribute(name = "high", required = false)
    private String high;

    @Attribute(name = "low", required = false)
    private String low;

    @Attribute(name = "maximumbuyin", required = false)
    private String maximumbuyin;

    @Attribute(name = "maxplayer", required = false)
    private String maxplayer;

    @Attribute(name = "minimumbuyin", required = false)
    private String minimumbuyin;

    @Attribute(name = "minplayer", required = false)
    private String minplayer;

    @Attribute(name = "schedule_name", required = false)
    private String schedule_name;

    @Attribute(name = "table_cost", required = false)
    private String table_cost;

    @Attribute(name = "table_id", required = false)
    private String table_id;

    @Attribute(name = "table_type", required = false)
    private String table_type;

    @Attribute(name = "total_player", required = false)
    private String total_player;

    @Attribute(name = "joined_players", required = false)
    private String joined_players;

    @Element(name = "player", required = false)
    private RummyPlayerModel tourney_player;

    @ElementList(name = "players", required = false)
    private List<RummyGamePlayer> players;

    public List<RummyGamePlayer> getPlayers() {
        return players;
    }

    public void setTables(List<RummyGamePlayer> players) {
        this.players = players;
    }

    public RummyPlayerModel getTourney_player() {
        return tourney_player;
    }

    public void setTourney_player(RummyPlayerModel tourney_player) {
        this.tourney_player = tourney_player;
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public String getCurrent_players() {
        return current_players;
    }

    public void setCurrent_players(String current_players) {
        this.current_players = current_players;
    }

    public String getGame_schedule() {
        return game_schedule;
    }

    public void setGame_schedule(String game_schedule) {
        this.game_schedule = game_schedule;
    }

    public String getGame_start() {
        return game_start;
    }

    public void setGame_start(String game_start) {
        this.game_start = game_start;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getMaximumbuyin() {
        return maximumbuyin;
    }

    public void setMaximumbuyin(String maximumbuyin) {
        this.maximumbuyin = maximumbuyin;
    }

    public String getMaxplayer() {
        return maxplayer;
    }

    public void setMaxplayer(String maxplayer) {
        this.maxplayer = maxplayer;
    }

    public String getMinimumbuyin() {
        return minimumbuyin;
    }

    public void setMinimumbuyin(String minimumbuyin) {
        this.minimumbuyin = minimumbuyin;
    }

    public String getMinplayer() {
        return minplayer;
    }

    public void setMinplayer(String minplayer) {
        this.minplayer = minplayer;
    }

    public String getSchedule_name() {
        return schedule_name;
    }

    public void setSchedule_name(String schedule_name) {
        this.schedule_name = schedule_name;
    }

    public String getTable_cost() {
        return table_cost;
    }

    public void setTable_cost(String table_cost) {
        this.table_cost = table_cost;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public String getTable_type() {
        return table_type;
    }

    public void setTable_type(String table_type) {
        this.table_type = table_type;
    }

    public String getTotal_player() {
        return total_player;
    }

    public void setTotal_player(String total_player) {
        this.total_player = total_player;
    }

    public String getJoined_players() {
        return joined_players;
    }

    public void setJoined_players(String joined_players) {
        this.joined_players = joined_players;
    }
}
