package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by GridLogic on 7/12/17.
 */
@Root(strict = false)
public class RummyLevels implements Serializable
{
    @Attribute(name = "aienable", required = false)
    private String aienable;

    @Attribute(name = "bet", required = false)
    private String bet;

    @Attribute(name = "delay_between_level", required = false)
    private String delay_between_level;

    @Attribute(name = "enablechat", required = false)
    private String enablechat;

    @Attribute(name = "end_time", required = false)
    private String end_time;

    @Attribute(name = "extratime", required = false)
    private String extratime;

    @Attribute(name = "gamesettings_id", required = false)
    private String gamesettings_id;

    @Attribute(name = "level_buying", required = false)
    private String level_buying;

    @Attribute(name = "level_id", required = false)
    private String level_id;

    @Attribute(name = "maximumbuyin", required = false)
    private String maximumbuyin;

    @Attribute(name = "maxplayer", required = false)
    private String maxplayer;

    @Attribute(name = "meld_timeout", required = false)
    private String meld_timeout;

    @Attribute(name = "middlejoin", required = false)
    private String middlejoin;

    @Attribute(name = "minimumbuyin", required = false)
    private String minimumbuyin;

    @Attribute(name = "minplayer", required = false)
    private String minplayer;

    @Attribute(name = "qualifying_points", required = false)
    private String qualifying_points;

    @Attribute(name = "rejoin", required = false)
    private String rejoin;

    @Attribute(name = "split", required = false)
    private String split;

    @Attribute(name = "splittype", required = false)
    private String splittype;

    @Attribute(name = "start_time", required = false)
    private String start_time;

    @Attribute(name = "table_type", required = false)
    private String table_type;

    @Attribute(name = "time_duration", required = false)
    private String time_duration;

    @Attribute(name = "turn_timeout", required = false)
    private String turn_timeout;

    public String getAienable() {
        return aienable;
    }

    public void setAienable(String aienable) {
        this.aienable = aienable;
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public String getDelay_between_level() {
        return delay_between_level;
    }

    public void setDelay_between_level(String delay_between_level) {
        this.delay_between_level = delay_between_level;
    }

    public String getEnablechat() {
        return enablechat;
    }

    public void setEnablechat(String enablechat) {
        this.enablechat = enablechat;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getExtratime() {
        return extratime;
    }

    public void setExtratime(String extratime) {
        this.extratime = extratime;
    }

    public String getGamesettings_id() {
        return gamesettings_id;
    }

    public void setGamesettings_id(String gamesettings_id) {
        this.gamesettings_id = gamesettings_id;
    }

    public String getLevel_buying() {
        return level_buying;
    }

    public void setLevel_buying(String level_buying) {
        this.level_buying = level_buying;
    }

    public String getLevel_id() {
        return level_id;
    }

    public void setLevel_id(String level_id) {
        this.level_id = level_id;
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

    public String getMeld_timeout() {
        return meld_timeout;
    }

    public void setMeld_timeout(String meld_timeout) {
        this.meld_timeout = meld_timeout;
    }

    public String getMiddlejoin() {
        return middlejoin;
    }

    public void setMiddlejoin(String middlejoin) {
        this.middlejoin = middlejoin;
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

    public String getQualifying_points() {
        return qualifying_points;
    }

    public void setQualifying_points(String qualifying_points) {
        this.qualifying_points = qualifying_points;
    }

    public String getRejoin() {
        return rejoin;
    }

    public void setRejoin(String rejoin) {
        this.rejoin = rejoin;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public String getSplittype() {
        return splittype;
    }

    public void setSplittype(String splittype) {
        this.splittype = splittype;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getTable_type() {
        return table_type;
    }

    public void setTable_type(String table_type) {
        this.table_type = table_type;
    }

    public String getTime_duration() {
        return time_duration;
    }

    public void setTime_duration(String time_duration) {
        this.time_duration = time_duration;
    }

    public String getTurn_timeout() {
        return turn_timeout;
    }

    public void setTurn_timeout(String turn_timeout) {
        this.turn_timeout = turn_timeout;
    }
}
