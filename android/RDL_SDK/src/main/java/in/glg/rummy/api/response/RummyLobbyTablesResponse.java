package in.glg.rummy.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import in.glg.rummy.models.RummyTable;

@Root(strict = false)
public class RummyLobbyTablesResponse extends RummyBaseResponse {
    @Attribute(name = "data", required = false)
    private String data;
    @ElementList(name = "tables", required = false)
    private List<RummyTable> tables;

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<RummyTable> getTables() {
        return this.tables;
    }

    public void setTables(List<RummyTable> tables) {
        this.tables = tables;
    }

    public int getErrorMessage() {
        return 0;
    }
}
