package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "pickdiscard",strict = false)
public class RummyPickDiscard {
    @Attribute(name = "autoplay", required = false)
    private String autoPlay;
    @Attribute(name = "deck", required = false)
    private String deck;
    @Attribute(name = "face", required = false)
    private String face;
    @Attribute(name = "nick_name", required = false)
    private String nickName;
    @Attribute(name = "suit", required = false)
    private String suit;
    @Attribute(name = "type", required = false)
    private String type;
    @Attribute(name = "user_id", required = false)
    private String userId;

    public String getAutoPlay() {
        return this.autoPlay;
    }

    public void setAutoPlay(String autoPlay) {
        this.autoPlay = autoPlay;
    }

    public String getDeck() {
        return this.deck;
    }

    public void setDeck(String deck) {
        this.deck = deck;
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

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
