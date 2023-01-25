package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "card", strict = false)
public class RummyPlayingCard implements Serializable {
    @Attribute
    private String face;
    @Attribute(name = "index", required = false)
    private String index;
    @Attribute(name = "slot", required = false)
    private String slot;
    @Attribute
    private String suit;
    @Attribute(name = "tableId", required = false)
    private String tableId;
    @Attribute(name = "user_id", required = false)
    private String userId;
    @Attribute(name = "userName", required = false)
    private String userName;

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

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userId) {
        this.userName = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTableId() {
        return this.tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getIndex() {
        return this.index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getSlot() {
        return this.slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getValue() {

        if(!this.getFace().equalsIgnoreCase("1") && !this.getFace().equalsIgnoreCase("11") && !this.getFace().equalsIgnoreCase("12") && !this.getFace().equalsIgnoreCase("13") && !this.getFace().equalsIgnoreCase("14"))
        {
            return this.getFace();
        }
        else
        {
            return "10";
        }

    }
}
