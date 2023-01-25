package in.glg.rummy.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import in.glg.rummy.api.requests.RummyBaserequest;

@Root(name = "request", strict = false)
public class RummyBaseTrRequest extends RummyBaserequest {
    @Attribute(name = "command")
    private String command;

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
