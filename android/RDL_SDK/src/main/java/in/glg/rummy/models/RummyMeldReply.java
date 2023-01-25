package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import in.glg.rummy.api.requests.RummyBaserequest;

@Root(name = "reply",strict = false)
public class RummyMeldReply extends RummyBaserequest {
    @ElementList(inline = true, name = "box", required = false)
    private List<RummyMeldBox> meldBoxes;
    @Attribute(name = "table_id")
    private String tableId;
    @Attribute(name = "text")
    private String text;
    @Attribute(name = "timestamp")
    private String timeStamp;
    @Attribute(name = "type")
    private String type;

    public String getTableId() {
        return this.tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<RummyMeldBox> getMeldBoxes() {
        return this.meldBoxes;
    }

    public void setMeldBoxes(List<RummyMeldBox> meldBoxes) {
        this.meldBoxes = meldBoxes;
    }
}
