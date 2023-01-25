package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(strict = false)
public class RummyTable implements Serializable {
    @Attribute(name = "bet", required = false)
    private String bet;
    @Attribute(name = "conversion", required = false)
    private String conversion;
    @Attribute(name = "current_players", required = false)
    private String current_players;
    @Attribute(name = "favorite", required = false)
    private String favorite;
    @Attribute(name = "game_schedule", required = false)
    private String game_schedule;
    @Attribute(name = "game_start", required = false)
    private String game_start;
    @Attribute(name = "id", required = false)
    private String id;
    @Attribute(name = "joined_players", required = false)
    private String joined_players;
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
    @Attribute(name = "status", required = false)
    private String status;
    @Attribute(name = "stream_id", required = false)
    private String stream_id;
    @Attribute(name = "stream_name", required = false)
    private String stream_name;
    @Attribute(name = "table_cost", required = false)
    private String table_cost;
    @Attribute(name = "table_id", required = false)
    private String table_id;
    @Attribute(name = "table_type", required = false)
    private String table_type;
    @Attribute(name = "total_player", required = false)
    private String total_player;
    @Attribute(name = "site_id", required = false)
    private String site_id;

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getConversion() {
        return this.conversion;
    }

    public void setConversion(String conversion) {
        this.conversion = conversion;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJoined_players() {
        return this.joined_players;
    }

    public void setJoined_players(String joined_players) {
        this.joined_players = joined_players;
    }

    public String getTotal_player() {
        return this.total_player;
    }

    public void setTotal_player(String total_player) {
        this.total_player = total_player;
    }

    public String getTable_id() {
        return this.table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public String getTable_cost() {
        return this.table_cost;
    }

    public void setTable_cost(String table_cost) {
        this.table_cost = table_cost;
    }

    public String getStream_id() {
        return this.stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getTable_type() {
        return this.table_type;
    }

    public void setTable_type(String table_type) {
        this.table_type = table_type;
    }

    public String getMaxplayer() {
        return this.maxplayer;
    }

    public void setMaxplayer(String maxplayer) {
        this.maxplayer = maxplayer;
    }

    public String getMinimumbuyin() {
        return this.minimumbuyin;
    }

    public void setMinimumbuyin(String minimumbuyin) {
        this.minimumbuyin = minimumbuyin;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaximumbuyin() {
        return this.maximumbuyin;
    }

    public void setMaximumbuyin(String maximumbuyin) {
        this.maximumbuyin = maximumbuyin;
    }

    public String getGame_schedule() {
        return this.game_schedule;
    }

    public void setGame_schedule(String game_schedule) {
        this.game_schedule = game_schedule;
    }

    public String getSchedule_name() {
        return this.schedule_name;
    }

    public void setSchedule_name(String schedule_name) {
        this.schedule_name = schedule_name;
    }

    public String getBet() {
        return this.bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public String getStream_name() {
        return this.stream_name;
    }

    public void setStream_name(String stream_name) {
        this.stream_name = stream_name;
    }

    public String getMinplayer() {
        return this.minplayer;
    }

    public void setMinplayer(String minplayer) {
        this.minplayer = minplayer;
    }

    public String getCurrent_players() {
        return this.current_players;
    }

    public void setCurrent_players(String current_players) {
        this.current_players = current_players;
    }

    public String getGame_start() {
        return this.game_start;
    }

    public void setGame_start(String game_start) {
        this.game_start = game_start;
    }
}
