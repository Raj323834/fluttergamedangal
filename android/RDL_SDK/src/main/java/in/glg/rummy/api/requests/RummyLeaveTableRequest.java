package in.glg.rummy.api.requests;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "request",strict = false)
public class RummyLeaveTableRequest extends RummyBaserequest {
    @Attribute(name = "autoplay", required = false)
    private String autoPlay;
    @Attribute(name = "card_sending", required = false)
    private String cardSending;
    @Attribute(name = "command", required = false)
    private String eventName;
    @Attribute(name = "face", required = false)
    private String face;
    @Attribute(name = "nick_name", required = false)
    private String nickName;
    @Attribute(name = "stack", required = false)
    private String stack;
    @Attribute(name = "suit", required = false)
    private String suit;
    @Attribute(name = "table_id", required = false)
    private String tableId;
    @Attribute(name = "timestamp", required = false)
    private String timeStamp;
    @Attribute(name = "user_id", required = false)
    private String userId;
    @Attribute(name = "tournament_id", required = false)
    private String tournament_id;

    public String getStack() {
        return this.stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getCardSending() {
        return this.cardSending;
    }

    public void setCardSending(String cardSending) {
        this.cardSending = cardSending;
    }

    public String getAutoPlay() {
        return this.autoPlay;
    }

    public void setAutoPlay(String autoPlay) {
        this.autoPlay = autoPlay;
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTableId() {
        return this.tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getFace() {
        return this.face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getSuit() {
        return this.suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getTournament_id() {
        return tournament_id;
    }

    public void setTournament_id(String tournament_id) {
        this.tournament_id = tournament_id;
    }
}
