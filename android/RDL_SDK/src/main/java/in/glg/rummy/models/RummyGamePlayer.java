package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "player",strict = false)
public class RummyGamePlayer implements Serializable {
    @Attribute(name = "DEVICE_ID", required = false)
    private String DEVICE_ID;
    @Attribute(name = "aienable", required = false)
    private String aiEnable;
    @Attribute(name = "amount", required = false)
    private String amount;
    @Attribute(name = "autoplay", required = false)
    private String autoplay;
    @Attribute(name = "autoplay_count", required = false)
    private String autoplay_count;
    @Attribute(name = "buyinammount", required = false)
    private String buyinammount;
    @Attribute(name = "char_no", required = false)
    private String char_no;
    @Attribute(name = "device_info_id", required = false)
    private String deviceInfoId;
    @Attribute(name = "face", required = false)
    private String face;
    @Attribute(name = "gender", required = false)
    private String gender;
    @Attribute(name = "dropped", required = false)
    private boolean isDropped;
    @Attribute(name = "isLeft", required = false)
    private boolean isLeft;
    @Attribute(name = "isMiddleJoin", required = false)
    private boolean isMiddleJoin;
    @ElementList(name = "meld", required = false)
    private List<RummyMeldBox> meldList;
    @Attribute(name = "minimumbuyin", required = false)
    private String minimumBuyin;
    @Attribute(name = "nick_name", required = false)
    private String nick_name;
    @Attribute(name = "place", required = false)
    private String place;
    @Attribute(name = "player_type", required = false)
    private String playerType;
    @Attribute(name = "playerlevel", required = false)
    private String playerlevel;
    @Attribute(name = "points", required = false)
    private String points;
    @Attribute(name = "result", required = false)
    private String result;
    @Attribute(name = "sc_use", required = false)
    private String scUse;
    @Attribute(name = "score", required = false)
    private String score;
    @Attribute(name = "seat", required = false)
    private String seat;
    @Attribute(name = "suit", required = false)
    private String suit;
    @Attribute(name = "totalcount", required = false)
    private String totalCount;
    @Attribute(name = "total_score", required = false)
    private String totalScore;
    @Attribute(name = "type", required = false)
    private String type;
    @Attribute(name = "user_id", required = false)
    private String user_id;
    @Attribute(name = "gift_id", required = false)
    private String gift_id;
    @Attribute(name = "position", required = false)
    private String position;
    @Attribute(name = "prize_amount", required = false)
    private String prize_amount;
    @Attribute(name = "rank", required = false)
    private String rank;
    @Attribute(name = "site_id", required = false)
    private String site_id;

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getAutoplay() {
        return this.autoplay;
    }

    public void setAutoplay(String autoplay) {
        this.autoplay = autoplay;
    }

    public String getAutoplay_count() {
        return this.autoplay_count;
    }

    public void setAutoplay_count(String autoplay_count) {
        this.autoplay_count = autoplay_count;
    }

    public String getBuyinammount() {
        return this.buyinammount;
    }

    public void setBuyinammount(String buyinammount) {
        this.buyinammount = buyinammount;
    }

    public String getChar_no() {
        return this.char_no;
    }

    public void setChar_no(String char_no) {
        this.char_no = char_no;
    }

    public String getNick_name() {
        return this.nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPlace() {
        return this.place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlayerlevel() {
        return this.playerlevel;
    }

    public void setPlayerlevel(String playerlevel) {
        this.playerlevel = playerlevel;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDEVICE_ID() {
        return this.DEVICE_ID;
    }

    public void setDEVICE_ID(String DEVICE_ID) {
        this.DEVICE_ID = DEVICE_ID;
    }

    public String getAiEnable() {
        return this.aiEnable;
    }

    public void setAiEnable(String aiEnable) {
        this.aiEnable = aiEnable;
    }

    public String getDeviceInfoId() {
        return this.deviceInfoId;
    }

    public void setDeviceInfoId(String deviceInfoId) {
        this.deviceInfoId = deviceInfoId;
    }

    public String getSeat() {
        return this.seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getScore() {
        return this.score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPoints() {
        return this.points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getScUse() {
        return this.scUse;
    }

    public void setScUse(String scUse) {
        this.scUse = scUse;
    }

    public String getTotalScore() {
        return this.totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public List<RummyMeldBox> getMeldList() {
        return this.meldList;
    }

    public void setMeldList(List<RummyMeldBox> meldList) {
        this.meldList = meldList;
    }

    public boolean isDropped() {
        return this.isDropped;
    }

    public void setDropped(boolean dropped) {
        this.isDropped = dropped;
    }

    public String getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getSuit() {
        return this.suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getFace() {
        return this.face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getPlayerType() {
        return this.playerType;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public String getMinimumBuyin() {
        return this.minimumBuyin;
    }

    public void setMinimumBuyin(String minimumBuyin) {
        this.minimumBuyin = minimumBuyin;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isLeft() {
        return this.isLeft;
    }

    public void setLeft(boolean left) {
        this.isLeft = left;
    }

    public boolean isMiddleJoin() {
        return this.isMiddleJoin;
    }

    public void setMiddleJoin(boolean middleJoin) {
        this.isMiddleJoin = middleJoin;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPrize_amount() {
        return prize_amount;
    }

    public void setPrize_amount(String prize_amount) {
        this.prize_amount = prize_amount;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }


}
