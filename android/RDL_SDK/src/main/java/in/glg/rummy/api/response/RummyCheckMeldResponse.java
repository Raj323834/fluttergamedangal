package in.glg.rummy.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import in.glg.rummy.models.RummyResults;

@Root(strict = false)
public class RummyCheckMeldResponse extends RummyBaseResponse {
    @Attribute(name = "data", required = false)
    private String data;
    @Attribute(name = "meldtimer", required = false)
    private String meldTimer;
    @Element(name = "results", required = false)
    private RummyResults results;
    @Attribute(name = "table_id", required = false)
    private String tableId;

    public RummyResults getResults() {
        return this.results;
    }

    public void setResults(RummyResults results) {
        this.results = results;
    }

    public String getMeldTimer() {
        return this.meldTimer;
    }

    public void setMeldTimer(String meldTimer) {
        this.meldTimer = meldTimer;
    }

    public String getTableId() {
        return this.tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getErrorMessage() {
        return 0;
    }
}
