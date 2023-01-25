package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(strict = false)
public class RummyTableCards implements Serializable {
    @ElementList(inline = true, name = "card")
    private List<RummyPlayingCard> cards;
    @Attribute(name = "table_id", required = false)
    private String tableId;

    public List<RummyPlayingCard> getCards() {
        return this.cards;
    }

    public void setCards(List<RummyPlayingCard> cards) {
        this.cards = cards;
    }

    public String getTableId() {
        return this.tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
}
