package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "result",strict = false)
public class RummyCheckMeldResult {
    @Attribute(name = "result", required = false)
    private String result;
    @Attribute(name = "score", required = false)
    private String score;
    @Attribute(name = "type", required = false)
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScore() {
        return this.score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
