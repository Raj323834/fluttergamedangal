package in.glg.rummy.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(strict = false)
public class RummyResults implements Serializable {
    @ElementList(inline = true, name = "result", required = false)
    private List<RummyCheckMeldResult> checkMeldResults;

    public List<RummyCheckMeldResult> getCheckMeldResults() {
        return this.checkMeldResults;
    }

    public void setCheckMeldResults(List<RummyCheckMeldResult> checkMeldResults) {
        this.checkMeldResults = checkMeldResults;
    }
}
