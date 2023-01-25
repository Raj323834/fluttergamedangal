package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by GridLogic on 12/12/17.
 */
@Root(strict = false)
public class RummyPrizeList implements Serializable
{
    @Attribute(name = "gift_id", required = false)
    private String gift_id;

    @Attribute(name = "percentage", required = false)
    private String percentage;

    @Attribute(name = "prize_amount", required = false)
    private String prize_amount;

    @Attribute(name = "rank", required = false)
    private String rank;

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
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
