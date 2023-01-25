package in.glg.rummy.POJOClasses;

import java.io.Serializable;

/**
 * Created by GridLogic on 29/7/17.
 */

public class RummyTablesPOJO implements Serializable
{
    private String stream_name;
    private String current_players;
    private String bet;
    private String table_cost;
    private String total_player;
    private String table_type;
    private String conversion;
    private String status;
    private String game_start;
    private String favorite;
    private String id;
    private String maximumbuyin;
    private String game_schedule;
    private String schedule_name;
    private String table_id;
    private String maxplayer;
    private String minimumbuyin;
    private String stream_id;
    private String minplayer;

    public RummyTablesPOJO(String stream_name, String current_players, String bet, String table_cost, String total_player, String table_type, String conversion,
                           String status, String game_start, String favorite, String id, String maximumbuyin, String game_schedule, String schedule_name,
                           String table_id, String maxplayer, String minimumbuyin, String stream_id, String minplayer)
    {
        this.stream_name = stream_name;
        this.current_players = current_players;
        this.bet = bet;
        this.table_cost = table_cost;
        this.total_player = total_player;
        this.table_type = table_type;
        this.conversion = conversion;
        this.status = status;
        this.game_start = game_start;
        this.favorite = favorite;
        this.id = id;
        this.maximumbuyin = maximumbuyin;
        this.game_schedule = game_schedule;
        this.schedule_name = schedule_name;
        this.table_id = table_id;
        this.maxplayer = maxplayer;
        this.minimumbuyin = minimumbuyin;
        this.stream_id = stream_id;
        this.minplayer = minplayer;
    }

    public String getStream_name() {
        return stream_name;
    }

    public void setStream_name(String stream_name) {
        this.stream_name = stream_name;
    }

    public String getCurrent_players() {
        return current_players;
    }

    public void setCurrent_players(String current_players) {
        this.current_players = current_players;
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public String getTable_cost() {
        return table_cost;
    }

    public void setTable_cost(String table_cost) {
        this.table_cost = table_cost;
    }

    public String getTotal_player() {
        return total_player;
    }

    public void setTotal_player(String total_player) {
        this.total_player = total_player;
    }

    public String getTable_type() {
        return table_type;
    }

    public void setTable_type(String table_type) {
        this.table_type = table_type;
    }

    public String getConversion() {
        return conversion;
    }

    public void setConversion(String conversion) {
        this.conversion = conversion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGame_start() {
        return game_start;
    }

    public void setGame_start(String game_start) {
        this.game_start = game_start;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaximumbuyin() {
        return maximumbuyin;
    }

    public void setMaximumbuyin(String maximumbuyin) {
        this.maximumbuyin = maximumbuyin;
    }

    public String getGame_schedule() {
        return game_schedule;
    }

    public void setGame_schedule(String game_schedule) {
        this.game_schedule = game_schedule;
    }

    public String getSchedule_name() {
        return schedule_name;
    }

    public void setSchedule_name(String schedule_name) {
        this.schedule_name = schedule_name;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
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

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getMinplayer() {
        return minplayer;
    }

    public void setMinplayer(String minplayer) {
        this.minplayer = minplayer;
    }
}
